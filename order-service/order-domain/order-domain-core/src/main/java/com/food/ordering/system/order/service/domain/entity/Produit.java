package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.domain.valueobject.ProduitId;

public class Produit extends BaseEntity<ProduitId> {
    private String name;
    private Monnaie price;

    public Produit(ProduitId produitId, String name, Monnaie price) {
        super.setId(produitId);
        this.name = name;
        this.price = price;
    }

    public Produit(ProduitId produitId) {
        super.setId(produitId);
    }

    public void updateWithConfirmedNameAndPrice(String name, Monnaie price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Monnaie getPrice() {
        return price;
    }
}
