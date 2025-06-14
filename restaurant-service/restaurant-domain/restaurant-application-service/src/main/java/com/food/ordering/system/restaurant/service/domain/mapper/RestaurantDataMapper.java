package com.food.ordering.system.restaurant.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CommandeId;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.domain.valueobject.CommandeStatus;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.DétailsCommande;
import com.food.ordering.system.restaurant.service.domain.entity.Produit;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataMapper {
    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest
                                                                             restaurantApprovalRequest) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .orderDetail(DétailsCommande.builder()
                        .orderId(new CommandeId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                        .products(restaurantApprovalRequest.getProduits().stream().map(
                                product -> Produit.builder()
                                        .productId(product.getId())
                                        .quantity(product.getQuantity())
                                        .build())
                                .collect(Collectors.toList()))
                        .totalAmount(new Monnaie(restaurantApprovalRequest.getPrice()))
                        .orderStatus(CommandeStatus.valueOf(restaurantApprovalRequest.getRestaurantCommandeStatus().name()))
                        .build())
                .build();
    }
}
