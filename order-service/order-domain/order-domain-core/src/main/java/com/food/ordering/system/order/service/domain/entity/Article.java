package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.CommandeId;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.order.service.domain.valueobject.ArticleId;
import lombok.Getter;

@Getter
public class Article extends BaseEntity<ArticleId> {
    private CommandeId commandeId;
    private final Produit produit;
    private final int quantity;
    private final Monnaie price;
    private final Monnaie subTotal;

    void initializeOrderItem(CommandeId commandeId, ArticleId articleId) {
        this.commandeId = commandeId;
        super.setId(articleId);
    }

    boolean isPriceValid() {
        return price.isGreaterThanZero() &&
                price.equals(produit.getPrice()) &&
                price.multiply(quantity).equals(subTotal);
    }

    private Article(Builder builder) {
        super.setId(builder.articleId);
        produit = builder.produit;
        quantity = builder.quantity;
        price = builder.price;
        subTotal = builder.subTotal;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private ArticleId articleId;
        private Produit produit;
        private int quantity;
        private Monnaie price;
        private Monnaie subTotal;

        private Builder() {
        }

        public Builder orderItemId(ArticleId val) {
            articleId = val;
            return this;
        }

        public Builder product(Produit val) {
            produit = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder price(Monnaie val) {
            price = val;
            return this;
        }

        public Builder subTotal(Monnaie val) {
            subTotal = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }
}
