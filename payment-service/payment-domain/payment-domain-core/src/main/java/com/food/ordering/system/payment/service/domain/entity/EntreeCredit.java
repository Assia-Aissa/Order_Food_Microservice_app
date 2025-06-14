package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.payment.service.domain.valueObject.EntreeCreditId;
import lombok.Getter;

@Getter
public class EntreeCredit extends BaseEntity<EntreeCreditId> {

    private final ClientId clientId;
    private Monnaie totalCreditAmount;

    public void addCreditAmount(Monnaie amount) {
        totalCreditAmount = totalCreditAmount.add(amount);
    }

    public void subtractCreditAmount(Monnaie amount) {
        totalCreditAmount = totalCreditAmount.subtract(amount);
    }

    private EntreeCredit(Builder builder) {
        setId(builder.entreeCreditId);
        clientId = builder.clientId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private EntreeCreditId entreeCreditId;
        private ClientId clientId;
        private Monnaie totalCreditAmount;

        private Builder() {
        }

        public Builder creditEntryId(EntreeCreditId val) {
            entreeCreditId = val;
            return this;
        }

        public Builder customerId(ClientId val) {
            clientId = val;
            return this;
        }

        public Builder totalCreditAmount(Monnaie val) {
            totalCreditAmount = val;
            return this;
        }

        public EntreeCredit build() {
            return new EntreeCredit(this);
        }
    }
}
