package com.food.ordering.system.payment.service.dataaccess.credithistory.mapper;

import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import com.food.ordering.system.payment.service.domain.entity.HistoriqueCredit;
import com.food.ordering.system.payment.service.domain.valueObject.HistoriqueCreditId;
import org.springframework.stereotype.Component;

@Component
public class CreditHistoryDataAccessMapper {

    public HistoriqueCredit creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity) {
        return HistoriqueCredit.builder()
                .creditHistoryId(new HistoriqueCreditId(creditHistoryEntity.getId()))
                .customerId(new ClientId(creditHistoryEntity.getCustomerId()))
                .amount(new Money(creditHistoryEntity.getAmount()))
                .transactionType(creditHistoryEntity.getType())
                .build();
    }

    public CreditHistoryEntity creditHistoryToCreditHistoryEntity(HistoriqueCredit historiqueCrédit) {
        return CreditHistoryEntity.builder()
                .id(historiqueCrédit.getId().getValue())
                .customerId(historiqueCrédit.getClientId().getValue())
                .amount(historiqueCrédit.getAmount().getAmount())
                .type(historiqueCrédit.getTransactionType())
                .build();
    }

}
