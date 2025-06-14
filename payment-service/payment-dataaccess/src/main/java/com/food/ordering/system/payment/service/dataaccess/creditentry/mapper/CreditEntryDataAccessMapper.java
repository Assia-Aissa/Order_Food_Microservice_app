package com.food.ordering.system.payment.service.dataaccess.creditentry.mapper;

import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.ordering.system.payment.service.domain.entity.EntreeCredit;
import com.food.ordering.system.payment.service.domain.valueObject.EntreeCreditId;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {

    public EntreeCredit creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
        return EntreeCredit.builder()
                .creditEntryId(new EntreeCreditId(creditEntryEntity.getId()))
                .customerId(new ClientId(creditEntryEntity.getCustomerId()))
                .totalCreditAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
                .build();
    }

    public CreditEntryEntity creditEntryToCreditEntryEntity(EntreeCredit entréeCrédit) {
        return CreditEntryEntity.builder()
                .id(entréeCrédit.getId().getValue())
                .customerId(entréeCrédit.getClientId().getValue())
                .totalCreditAmount(entréeCrédit.getTotalCreditAmount().getAmount())
                .build();
    }

}
