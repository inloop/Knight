package eu.inloop.knight.sample.model.api;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Class {@link ApiCallback}.
 *
 * @author f3rog
 * @version 2015-07-10
 */
public abstract class ApiCallback<T> implements Callback<T> {

    @Override
    public void success(T t, Response response) {
        onSuccess(t, response);
        onDone(true);
    }

    @Override
    public void failure(RetrofitError error) {
        onFailure((ApiError) error.getBodyAs(ApiError.class), error);
        onDone(false);
    }

    public abstract void onSuccess(T t, Response response);

    public abstract void onFailure(ApiError apiError, RetrofitError error);

    public void onDone(boolean success) {

    }

}