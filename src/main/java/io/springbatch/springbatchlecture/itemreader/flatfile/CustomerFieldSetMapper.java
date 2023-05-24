package io.springbatch.springbatchlecture.itemreader.flatfile;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFieldSetMapper implements FieldSetMapper<CustomerFileField> {

    @Override
    public CustomerFileField mapFieldSet(FieldSet fs) throws BindException {
        if (fs == null) {
            return null;
        }

        CustomerFileField customer = new CustomerFileField();
        customer.setName(fs.readString("name"));
        customer.setAge(fs.readInt("age"));
        customer.setYear(fs.readString("year"));

        return customer;
    }
}
