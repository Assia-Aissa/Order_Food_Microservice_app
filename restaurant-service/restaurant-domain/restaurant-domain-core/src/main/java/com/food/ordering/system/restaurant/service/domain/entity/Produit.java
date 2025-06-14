package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.domain.valueobject.ProduitId;
import lombok.Getter;

@Getter
public class Produit extends BaseEntity<ProduitId> {
    private String name;
    private Monnaie price;
    private final int quantity;
    private boolean available;

    public void updateWithConfirmedNamePriceAndAvailability(String name, Monnaie price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }

    private Produit(Builder builder) {
        setId(builder.produitId);
        name = builder.name;
        price = builder.price;
        quantity = builder.quantity;
        available = builder.available;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private ProduitId produitId;
        private String name;
        private Monnaie price;
        private int quantity;
        private boolean available;

        private Builder() {
        }

        public Builder productId(ProduitId val) {
            produitId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Monnaie val) {
            price = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder available(boolean val) {
            available = val;
            return this;
        }

        public Produit build() {
            return new Produit(this);
        }
    }
}
