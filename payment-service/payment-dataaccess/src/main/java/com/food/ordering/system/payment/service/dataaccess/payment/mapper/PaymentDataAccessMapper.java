package com.food.ordering.system.payment.service.dataaccess.payment.mapper;

import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.payment.service.dataaccess.payment.entity.PaymentEntity;
import com.food.ordering.system.payment.service.domain.entity.Paiement;
import com.food.ordering.system.payment.service.domain.valueObject.PaiementId;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataAccessMapper {

    public PaymentEntity paymentToPaymentEntity(Paiement paiement) {
        return PaymentEntity.builder()
                .id(paiement.getId().getValue())
                .customerId(paiement.getClientId().getValue())
                .orderId(paiement.getOrderId().getValue())
                .price(paiement.getPrice().getAmount())
                .status(paiement.getPaymentStatus())
                .createdAt(paiement.getCreatedAt())
                .build();
    }

    public Paiement paymentEntityToPayment(PaymentEntity paymentEntity) {
        return Paiement.builder()
                .paymentId(new PaiementId(paymentEntity.getId()))
                .customerId(new ClientId(paymentEntity.getCustomerId()))
                .orderId(new OrderId(paymentEntity.getOrderId()))
                .price(new Money(paymentEntity.getPrice()))
                .createdAt(paymentEntity.getCreatedAt())
                .build();
    }

}
