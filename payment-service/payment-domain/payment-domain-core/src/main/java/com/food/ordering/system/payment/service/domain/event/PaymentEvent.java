package com.food.ordering.system.payment.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.payment.service.domain.entity.Paiement;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public abstract class PaymentEvent implements DomainEvent<Paiement> {

    private final Paiement paiement;
    private final ZonedDateTime createdAt;
    private final List<String> failureMessages;

    public PaymentEvent(Paiement paiement, ZonedDateTime createdAt, List<String> failureMessages) {
        this.paiement = paiement;
        this.createdAt = createdAt;
        this.failureMessages = failureMessages;
    }

}
