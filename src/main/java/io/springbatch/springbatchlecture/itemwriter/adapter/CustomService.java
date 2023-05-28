package io.springbatch.springbatchlecture.itemwriter.adapter;

public class CustomService<T> {

    public void customWrite(T item) {
        System.out.println(item);
    }
}
