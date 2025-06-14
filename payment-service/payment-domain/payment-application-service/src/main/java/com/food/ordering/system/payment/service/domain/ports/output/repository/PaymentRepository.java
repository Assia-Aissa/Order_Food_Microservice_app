package com.food.ordering.system.payment.service.domain.ports.output.repository;

import com.food.ordering.system.payment.service.domain.entity.Paiement;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Paiement save(Paiement paiement);

    Optional<Paiement> findByOrderId(UUID orderId);
}
