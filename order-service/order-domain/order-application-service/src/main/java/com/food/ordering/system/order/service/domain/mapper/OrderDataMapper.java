package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Commande;
import com.food.ordering.system.order.service.domain.entity.Article;
import com.food.ordering.system.order.service.domain.entity.Produit;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        return Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .products(createOrderCommand.getItems().stream().map(orderItem ->
                        new Produit(new ProductId(orderItem.getProductId())))
                        .collect(Collectors.toList()))
                .build();
    }
    
    public Commande createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Commande.builder()
                .customerId(new ClientId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();
    }

    public CreateOrderResponse orderToCreateOrderResponse(Commande commande, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(commande.getTrackingId().getValue())
                .orderStatus(commande.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Commande commande) {
        return TrackOrderResponse.builder()
                .orderTrackingId(commande.getTrackingId().getValue())
                .orderStatus(commande.getOrderStatus())
                .failureMessages(commande.getFailureMessages())
                .build();
    }

    private List<Article> orderItemsToOrderItemEntities(
            List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                        Article.builder()
                                .product(new Produit(new ProductId(orderItem.getProductId())))
                                .price(new Money(orderItem.getPrice()))
                                .quantity(orderItem.getQuantity())
                                .subTotal(new Money(orderItem.getSubTotal()))
                                .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }
}
