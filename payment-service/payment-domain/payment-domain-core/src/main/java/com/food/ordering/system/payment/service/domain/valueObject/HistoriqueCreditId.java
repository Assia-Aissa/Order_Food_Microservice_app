package com.food.ordering.system.payment.service.domain.valueObject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class HistoriqueCreditId extends BaseId<UUID> {
    public HistoriqueCreditId(UUID value) {
        super(value);
    }
}
