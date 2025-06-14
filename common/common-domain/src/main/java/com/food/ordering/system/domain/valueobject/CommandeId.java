package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public class CommandeId extends BaseId<UUID> {
    public CommandeId(UUID value) {
        super(value);
    }
}
