package io.springbatch.springbatchlecture.listener.retrylistener;

public class CustomRetryException extends Exception {
    public CustomRetryException(String msg) {
        super(msg);
    }
}
