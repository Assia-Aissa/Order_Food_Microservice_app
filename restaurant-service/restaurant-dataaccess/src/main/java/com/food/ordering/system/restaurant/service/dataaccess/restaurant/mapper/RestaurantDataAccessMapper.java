package com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.domain.valueobject.CommandeId;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.domain.valueobject.ProduitId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.entity.OrderApprovalEntity;
import com.food.ordering.system.restaurant.service.domain.entity.ApprobationCommande;
import com.food.ordering.system.restaurant.service.domain.entity.DétailsCommande;
import com.food.ordering.system.restaurant.service.domain.entity.Produit;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.valueobject.ApprobationCommandeId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getDétailsCommande().getProduits().stream()
                .map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity =
                restaurantEntities.stream().findFirst().orElseThrow(() ->
                        new RestaurantDataAccessException("No restaurants found!"));

        List<Produit> restaurantProduits = restaurantEntities.stream().map(entity ->
                        Produit.builder()
                                .productId(new ProduitId(entity.getProductId()))
                                .name(entity.getProductName())
                                .price(new Monnaie(entity.getProductPrice()))
                                .available(entity.getProductAvailable())
                                .build())
                .collect(Collectors.toList());

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getRestaurantId()))
                .orderDetail(DétailsCommande.builder()
                        .products(restaurantProduits)
                        .build())
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }

    public OrderApprovalEntity orderApprovalToOrderApprovalEntity(ApprobationCommande approbationCommande) {
        return OrderApprovalEntity.builder()
                .id(approbationCommande.getId().getValue())
                .restaurantId(approbationCommande.getRestaurantId().getValue())
                .orderId(approbationCommande.getCommandeId().getValue())
                .status(approbationCommande.getApprovalStatus())
                .build();
    }

    public ApprobationCommande orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
        return ApprobationCommande.builder()
                .orderApprovalId(new ApprobationCommandeId(orderApprovalEntity.getId()))
                .restaurantId(new RestaurantId(orderApprovalEntity.getRestaurantId()))
                .orderId(new CommandeId(orderApprovalEntity.getOrderId()))
                .approvalStatus(orderApprovalEntity.getStatus())
                .build();
    }

}
