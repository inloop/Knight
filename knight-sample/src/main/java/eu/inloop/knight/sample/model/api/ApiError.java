package eu.inloop.knight.sample.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Class {@link ApiError}.
 *
 * @author f3rog
 * @version 2015-07-10
 */
public class ApiError {

    public static class Error {

        @SerializedName("message")
        private String mMessage;
        @SerializedName("code")
        private int mCode;

        public String getMessage() {
            return mMessage;
        }

        public int getCode() {
            return mCode;
        }

    }

    @SerializedName("error")
    private Error mError;

    public Error getError() {
        return mError;
    }
}
