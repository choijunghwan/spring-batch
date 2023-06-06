package io.springbatch.springbatchlecture.exception.retry;

public class RetryableException extends RuntimeException {
    public RetryableException(String message) {
        super(message);
    }

    public RetryableException() {
        super();
    }
}
