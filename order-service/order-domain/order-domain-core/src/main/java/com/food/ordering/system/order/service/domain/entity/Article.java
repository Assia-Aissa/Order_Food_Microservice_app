package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.valueobject.ArticleId;
import lombok.Getter;

@Getter
public class Article extends BaseEntity<ArticleId> {
    private OrderId orderId;
    private final Produit produit;
    private final int quantity;
    private final Money price;
    private final Money subTotal;

    void initializeOrderItem(OrderId orderId, ArticleId articleId) {
        this.orderId = orderId;
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
        private Money price;
        private Money subTotal;

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

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder subTotal(Money val) {
            subTotal = val;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }
}
