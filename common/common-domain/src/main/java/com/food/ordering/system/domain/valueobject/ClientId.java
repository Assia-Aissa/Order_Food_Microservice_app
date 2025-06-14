package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public class ClientId extends BaseId<UUID> {
    public ClientId(UUID value) {
        super(value);
    }
}
