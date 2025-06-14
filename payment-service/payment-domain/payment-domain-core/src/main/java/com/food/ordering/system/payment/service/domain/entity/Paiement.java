package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.domain.valueobject.CommandeId;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.domain.valueobject.PaiementStatus;
import com.food.ordering.system.payment.service.domain.valueObject.PaiementId;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class Paiement extends AggregateRoot<PaiementId> {

    private final CommandeId commandeId;
    private final ClientId clientId;
    private final Monnaie price;

    private PaiementStatus paiementStatus;
    private ZonedDateTime createdAt;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";

    public void initializePayment() {
        setId(new PaiementId(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public void validatePayment(List<String> failureMessages) {
        if (price == null || !price.isGreaterThanZero()) {
            failureMessages.add("Total price must be greater than zero!");
        }
    }

    public void updateStatus(PaiementStatus paiementStatus) {
        this.paiementStatus = paiementStatus;
    }

    private Paiement(Builder builder) {
        setId(builder.paiementId);
        commandeId = builder.commandeId;
        clientId = builder.clientId;
        price = builder.price;
        paiementStatus = builder.paiementStatus;
        createdAt = builder.createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private PaiementId paiementId;
        private CommandeId commandeId;
        private ClientId clientId;
        private Monnaie price;
        private PaiementStatus paiementStatus;
        private ZonedDateTime createdAt;

        private Builder() {
        }

        public Builder paymentId(PaiementId val) {
            paiementId = val;
            return this;
        }

        public Builder orderId(CommandeId val) {
            commandeId = val;
            return this;
        }

        public Builder customerId(ClientId val) {
            clientId = val;
            return this;
        }

        public Builder price(Monnaie val) {
            price = val;
            return this;
        }

        public Builder paymentStatus(PaiementStatus val) {
            paiementStatus = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public Paiement build() {
            return new Paiement(this);
        }
    }
}
