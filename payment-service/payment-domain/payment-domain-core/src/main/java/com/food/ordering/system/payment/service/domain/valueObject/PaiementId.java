package com.food.ordering.system.payment.service.domain.valueObject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class PaiementId extends BaseId<UUID> {
    public PaiementId(UUID value) {
        super(value);
    }
}
