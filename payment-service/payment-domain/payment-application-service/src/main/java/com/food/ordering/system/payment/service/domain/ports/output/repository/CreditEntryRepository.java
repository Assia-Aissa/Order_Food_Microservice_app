package com.food.ordering.system.payment.service.domain.ports.output.repository;

import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.payment.service.domain.entity.EntreeCredit;

import java.util.Optional;

public interface CreditEntryRepository {

    EntreeCredit save(EntreeCredit entréeCrédit);

    Optional<EntreeCredit> findByCustomerId(ClientId clientId);
}
