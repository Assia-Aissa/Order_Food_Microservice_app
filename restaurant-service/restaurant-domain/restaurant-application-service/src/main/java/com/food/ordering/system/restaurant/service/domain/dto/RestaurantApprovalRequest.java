package com.food.ordering.system.restaurant.service.domain.dto;

import com.food.ordering.system.domain.valueobject.RestaurantCommandeStatus;
import com.food.ordering.system.restaurant.service.domain.entity.Produit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RestaurantApprovalRequest {
    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private RestaurantCommandeStatus restaurantCommandeStatus;
    private java.util.List<Produit> produits;
    private java.math.BigDecimal price;
    private java.time.Instant createdAt;
}
