package com.food.ordering.system.order.service.domain.service.impl;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Commande;
import com.food.ordering.system.order.service.domain.entity.Produit;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.service.OrderDomainService;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {


    /**
     * Validates the restaurant and initiates the commande.
     *
     * @param commande the commande to be validated and initiated
     * @param restaurant the restaurant to validate against
     * @return an event indicating the commande has been created
     */
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Commande commande, Restaurant restaurant,
                                                      DomainEventPublisher<OrderCreatedEvent>
                                                              orderCreatedEventDomainEventPublisher) {
        validateRestaurant(restaurant);
        setOrderProductInformation(commande, restaurant);
        commande.validateOrder();
        commande.initializeOrder();
        log.info("Commande with id: {} is initiated", commande.getId().getValue());
        return new OrderCreatedEvent(commande, ZonedDateTime.now(ZoneId.of(UTC)), orderCreatedEventDomainEventPublisher);
    }

    /**
     * Marks the commande as paid.
     *
     * @param commande the commande to be marked as paid
     * @return an event indicating the commande has been paid
     */
    @Override
    public OrderPaidEvent payOrder(Commande commande,
                                   DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher) {
        commande.pay();
        log.info("Commande with id: {} is paid", commande.getId().getValue());
        return new OrderPaidEvent(commande, ZonedDateTime.now(ZoneId.of(UTC)), orderPaidEventDomainEventPublisher);
    }

    /**
     * Approves the commande.
     *
     * @param commande the commande to be approved
     */
    @Override
    public void approveOrder(Commande commande) {
        commande.approve();
        log.info("Commande with id: {} is approved", commande.getId().getValue());
    }

    /**
     * Cancels the commande payment.
     *
     * @param commande the commande for which the payment is to be cancelled
     * @param failureMessages the list of failure messages
     * @return an event indicating the commande payment has been cancelled
     */
    @Override
    public OrderCancelledEvent cancelOrderPayment(Commande commande, List<String> failureMessages,
                                                  DomainEventPublisher<OrderCancelledEvent>
                                                          orderCancelledEventDomainEventPublisher) {
        commande.initCancel(failureMessages);
        log.info("Commande payment is cancelling for commande id: {}", commande.getId().getValue());
        return new OrderCancelledEvent(commande, ZonedDateTime.now(ZoneId.of(UTC)),
                orderCancelledEventDomainEventPublisher);
    }

    /**
     * Cancels the commande.
     *
     * @param commande the commande to be cancelled
     * @param failureMessages the list of failure messages
     */
    @Override
    public void cancelOrder(Commande commande, List<String> failureMessages) {
        commande.cancel(failureMessages);
        log.info("Commande with id: {} is cancelled", commande.getId().getValue());
    }

    /**
     * Validates the restaurant.
     *
     * @param restaurant the restaurant to be validated
     * @throws OrderDomainException if the restaurant is not active
     */
    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
                    " is currently not active!");
        }
    }

    /**
     * Sets the product information for the commande based on the restaurant's products.
     * <p>
     * This method iterates over each item in the commande and matches it with the corresponding
     * product in the restaurant. If a match is found, it updates the product information
     * in the commande with the confirmed name and price from the restaurant.
     *
     * @param commande the commande containing the items to be updated
     * @param restaurant the restaurant containing the products to match against
     */
    private void setOrderProductInformation(Commande commande, Restaurant restaurant) {
        commande.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Produit currentProduit = orderItem.getProduit();
            if (currentProduit.equals(restaurantProduct)) {
                currentProduit.updateWithConfirmedNameAndPrice(restaurantProduct.getName(),
                        restaurantProduct.getPrice());
            }
        }));
    }
}
