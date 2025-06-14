package com.food.ordering.system.order.service.dataaccess.customer.mapper;

import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import com.food.ordering.system.order.service.domain.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Client customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Client(new ClientId(customerEntity.getId()));
    }
}
