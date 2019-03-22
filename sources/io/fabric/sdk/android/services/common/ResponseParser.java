package io.fabric.sdk.android.services.common;

import org.apache.http.HttpStatus;

public class ResponseParser {
    public static final int ResponseActionDiscard = 0;
    public static final int ResponseActionRetry = 1;

    public static int parse(int statusCode) {
        if (statusCode >= 200 && statusCode <= 299) {
            return 0;
        }
        if (statusCode >= 300 && statusCode <= 399) {
            return 1;
        }
        if (statusCode >= HttpStatus.SC_BAD_REQUEST && statusCode <= 499) {
            return 0;
        }
        if (statusCode >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            return 1;
        }
        return 1;
    }
}
