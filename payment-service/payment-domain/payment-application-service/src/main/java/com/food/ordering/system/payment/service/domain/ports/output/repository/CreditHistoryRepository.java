package com.food.ordering.system.payment.service.domain.ports.output.repository;

import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.payment.service.domain.entity.HistoriqueCredit;

import java.util.List;
import java.util.Optional;

public interface CreditHistoryRepository {

    HistoriqueCredit save(HistoriqueCredit historiqueCrédit);

    Optional<List<HistoriqueCredit>> findByCustomerId(ClientId clientId);
}
