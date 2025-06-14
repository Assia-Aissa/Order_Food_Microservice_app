package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.order.service.domain.entity.Commande;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public abstract class OrderEvent implements DomainEvent<Commande> {
    private final Commande commande;
    private final ZonedDateTime createdAt;

    public OrderEvent(Commande commande, ZonedDateTime createdAt) {
        this.commande = commande;
        this.createdAt = createdAt;
    }

}
