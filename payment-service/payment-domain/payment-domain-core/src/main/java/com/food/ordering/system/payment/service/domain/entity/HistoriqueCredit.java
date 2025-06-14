package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.payment.service.domain.valueObject.HistoriqueCreditId;
import com.food.ordering.system.payment.service.domain.valueObject.TransactionType;
import lombok.Getter;

@Getter
public class HistoriqueCredit extends BaseEntity<HistoriqueCreditId> {

    private final ClientId clientId;
    private final Monnaie amount;
    private final TransactionType transactionType;

    private HistoriqueCredit(Builder builder) {
        setId(builder.historiqueCreditId);
        clientId = builder.clientId;
        amount = builder.amount;
        transactionType = builder.transactionType;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private HistoriqueCreditId historiqueCreditId;
        private ClientId clientId;
        private Monnaie amount;
        private TransactionType transactionType;

        private Builder() {
        }

        public Builder creditHistoryId(HistoriqueCreditId val) {
            historiqueCreditId = val;
            return this;
        }

        public Builder customerId(ClientId val) {
            clientId = val;
            return this;
        }

        public Builder amount(Monnaie val) {
            amount = val;
            return this;
        }

        public Builder transactionType(TransactionType val) {
            transactionType = val;
            return this;
        }

        public HistoriqueCredit build() {
            return new HistoriqueCredit(this);
        }
    }
}
