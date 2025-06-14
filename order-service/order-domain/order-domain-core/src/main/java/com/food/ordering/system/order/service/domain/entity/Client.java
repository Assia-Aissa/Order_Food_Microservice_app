package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.ClientId;

public class Client extends AggregateRoot<ClientId> {
    public Client() {
    }

    public Client(ClientId clientId) {
        super.setId(clientId);
    }
}
