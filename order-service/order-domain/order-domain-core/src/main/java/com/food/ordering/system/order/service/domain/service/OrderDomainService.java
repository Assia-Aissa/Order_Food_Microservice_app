package com.food.ordering.system.order.service.domain.service;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Commande;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Commande commande,
                                               Restaurant restaurant,
                                               DomainEventPublisher<OrderCreatedEvent>
                                                       orderCreatedEventDomainEventPublisher);

    OrderPaidEvent payOrder(Commande commande,
                            DomainEventPublisher<OrderPaidEvent>
                                    orderPaidEventDomainEventPublisher);

    void approveOrder(Commande commande);

    OrderCancelledEvent cancelOrderPayment(Commande commande,
                                           List<String> failureMessages,
                                           DomainEventPublisher<OrderCancelledEvent>
                                                   orderCancelledEventDomainEventPublisher);

    void cancelOrder(Commande commande, List<String> failureMessages);
}
