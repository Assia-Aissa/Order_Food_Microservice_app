package com.food.ordering.system.payment.service.domain.valueObject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class EntreeCreditId extends BaseId<UUID> {
    public EntreeCreditId(UUID value) {
        super(value);
    }
}
