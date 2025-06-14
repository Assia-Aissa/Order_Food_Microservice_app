package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CommandeId;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.domain.valueobject.CommandeStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class DétailsCommande extends BaseEntity<CommandeId> {
    private CommandeStatus commandeStatus;
    private Monnaie totalAmount;
    private final List<Produit> produits;

    private DétailsCommande(Builder builder) {
        setId(builder.commandeId);
        commandeStatus = builder.commandeStatus;
        totalAmount = builder.totalAmount;
        produits = builder.produits;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private CommandeId commandeId;
        private CommandeStatus commandeStatus;
        private Monnaie totalAmount;
        private List<Produit> produits;

        private Builder() {
        }

        public Builder orderId(CommandeId val) {
            commandeId = val;
            return this;
        }

        public Builder orderStatus(CommandeStatus val) {
            commandeStatus = val;
            return this;
        }

        public Builder totalAmount(Monnaie val) {
            totalAmount = val;
            return this;
        }

        public Builder products(List<Produit> val) {
            produits = val;
            return this;
        }

        public DétailsCommande build() {
            return new DétailsCommande(this);
        }
    }
}
