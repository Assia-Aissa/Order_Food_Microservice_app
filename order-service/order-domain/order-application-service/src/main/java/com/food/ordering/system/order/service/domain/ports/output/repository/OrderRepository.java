package com.food.ordering.system.order.service.domain.ports.output.repository;

import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.entity.Commande;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {

    Commande save(Commande commande);

    Optional<Commande> findById(OrderId orderId);

    Optional<Commande> findByTrackingId(TrackingId trackingId);
}
