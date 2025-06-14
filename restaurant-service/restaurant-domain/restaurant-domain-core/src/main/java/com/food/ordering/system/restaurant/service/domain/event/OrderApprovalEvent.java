package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.entity.ApprobationCommande;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public abstract class OrderApprovalEvent implements DomainEvent<ApprobationCommande> {
    private final ApprobationCommande approbationCommande;
    private final RestaurantId restaurantId;
    private final List<String> failureMessages;
    private final ZonedDateTime createdAt;

    public OrderApprovalEvent(ApprobationCommande approbationCommande,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {
        this.approbationCommande = approbationCommande;
        this.restaurantId = restaurantId;
        this.failureMessages = failureMessages;
        this.createdAt = createdAt;
    }

}
