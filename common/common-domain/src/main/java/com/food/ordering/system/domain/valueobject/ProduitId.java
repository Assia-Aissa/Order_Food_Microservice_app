package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public class ProduitId extends BaseId<UUID> {
    public ProduitId(UUID value) {
        super(value);
    }
}
