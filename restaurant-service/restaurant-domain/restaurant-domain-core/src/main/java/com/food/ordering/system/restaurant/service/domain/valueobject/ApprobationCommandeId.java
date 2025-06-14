package com.food.ordering.system.restaurant.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class ApprobationCommandeId extends BaseId<UUID> {
    public ApprobationCommandeId(UUID value) {
        super(value);
    }
}
