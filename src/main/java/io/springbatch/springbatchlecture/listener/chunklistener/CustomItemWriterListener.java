package io.springbatch.springbatchlecture.listener.chunklistener;

import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class CustomItemWriterListener implements ItemWriteListener<String> {

    @Override
    public void beforeWrite(List items) {
        System.out.println(">> before Write");
    }

    @Override
    public void afterWrite(List items) {
        System.out.println(">> after Write");
    }

    @Override
    public void onWriteError(Exception exception, List items) {
        System.out.println(">> on Write Write");
    }
}
