package eu.inloop.knight.sample.module;

import android.app.Application;
import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import eu.inloop.knight.AppProvided;
import eu.inloop.knight.sample.model.api.IApi;
import eu.inloop.knight.sample.util.NetUtils;
import eu.inloop.knight.scope.AppScope;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

@AppProvided
@Module
public class AppModule {

    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MiB

    @Provides
    @AppScope
    EventBus providesEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @AppScope
    OkClient providesHttpClient(Application context) {
        // Setup Caching
        OkHttpClient okHttpClient = new OkHttpClient();
        try {

            File cacheDirectory = new File(context.getCacheDir(), "responses");
            Cache cache = new Cache(cacheDirectory, CACHE_SIZE);
            okHttpClient.setCache(cache);

            // Intercept and change cache headers
            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .removeHeader("Expires")
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", "public, max-age=60")
                            .build();
                }
            });

        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }

        return new OkClient(okHttpClient);
    }

    @Provides
    @AppScope
    IApi providesApi(final NetUtils netUtils, OkClient client) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(IApi.API_ENDPOINT)
                .setClient(client)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Content-type", "application/json");
                        if (netUtils.isNetworkAvailable()) {
                            int maxAge = 60; // read from cache for 1 minute
                            request.addHeader("Cache-Control", "public, max-age=" + maxAge);
                        } else {
                            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                            request.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                        }
                    }
                })
                .build();

        return restAdapter.create(IApi.class);
    }

}
