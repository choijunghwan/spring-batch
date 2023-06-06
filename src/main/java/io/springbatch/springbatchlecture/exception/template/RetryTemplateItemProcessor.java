package io.springbatch.springbatchlecture.exception.template;

import io.springbatch.springbatchlecture.exception.retry.RetryableException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.classify.Classifier;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

public class RetryTemplateItemProcessor implements ItemProcessor<String, Customer> {

    @Autowired
    private RetryTemplate retryTemplate;

    private int cnt;

    @Override
    public Customer process(String item) throws Exception {

        Classifier<Throwable, Boolean> rollbackClassifier = new BinaryExceptionClassifier(true);

        /**
         * doWithRetry 에서 에러가 발생하면 -> recover가 동작
         * recover에서 new Customer(item)을 반환해주기 때문에 정상적으로 동작한다
         */
        Customer customer = retryTemplate.execute(
                new RetryCallback<Customer, RuntimeException>() {
            @Override
            public Customer doWithRetry(RetryContext context) throws RuntimeException {

                if (item.equals("1") || item.equals("2")) {
                    cnt++;
                    throw new RetryableException("failed cnt :" + cnt);
                }
                return new Customer(item);
            }
        }, new RecoveryCallback<Customer>() {
            @Override
            public Customer recover(RetryContext context) throws Exception {
                return new Customer(item);
            }
        }, new DefaultRetryState(item, rollbackClassifier)); // DefaultRetryState 가 없으면 retryState 값은 null로 설정된다
        return customer;
    }
}
