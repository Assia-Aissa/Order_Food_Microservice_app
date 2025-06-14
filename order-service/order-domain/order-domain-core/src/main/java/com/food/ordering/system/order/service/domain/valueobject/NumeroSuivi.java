package com.food.ordering.system.order.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class NumeroSuivi extends BaseId<UUID> {
    public NumeroSuivi(UUID value) {
        super(value);
    }
}
