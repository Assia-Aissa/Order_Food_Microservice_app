package com.food.ordering.system.payment.service.domain.service.impl;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.domain.valueobject.PaiementStatus;
import com.food.ordering.system.payment.service.domain.entity.EntreeCredit;
import com.food.ordering.system.payment.service.domain.entity.HistoriqueCredit;
import com.food.ordering.system.payment.service.domain.entity.Paiement;
import com.food.ordering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.payment.service.domain.service.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.valueObject.HistoriqueCreditId;
import com.food.ordering.system.payment.service.domain.valueObject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {

    @Override
    public PaymentEvent validateAndInitiatePayment(Paiement paiement,
                                                   EntreeCredit entréeCrédit,
                                                   List<HistoriqueCredit> creditHistories,
                                                   List<String> failureMessages,
                                                   DomainEventPublisher<PaymentCompletedEvent>
                                                           paymentCompletedEventDomainEventPublisher,
                                                   DomainEventPublisher<PaymentFailedEvent>
                                                           paymentFailedEventDomainEventPublisher) {
        paiement.validatePayment(failureMessages);
        paiement.initializePayment();
        validateCreditEntry(paiement, entréeCrédit, failureMessages);
        subtractCreditEntry(paiement, entréeCrédit);
        updateCreditHistory(paiement, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(entréeCrédit, creditHistories, failureMessages);

        if (failureMessages.isEmpty()) {
            log.info("Paiement is initiated for order id: {}", paiement.getCommandeId().getValue());
            paiement.updateStatus(PaiementStatus.COMPLETED);
            return new PaymentCompletedEvent(paiement, ZonedDateTime.now(ZoneId.of(UTC)),
                    paymentCompletedEventDomainEventPublisher);
        } else {
            log.info("Paiement initiation is failed for order id: {}", paiement.getCommandeId().getValue());
            paiement.updateStatus(PaiementStatus.FAILED);
            return new PaymentFailedEvent(paiement, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages,
                    paymentFailedEventDomainEventPublisher);
        }
    }

    @Override
    public PaymentEvent validateAndCancelPayment(Paiement paiement,
                                                 EntreeCredit entréeCrédit,
                                                 List<HistoriqueCredit> creditHistories,
                                                 List<String> failureMessages,
                                                 DomainEventPublisher<PaymentCancelledEvent>
                                                         paymentCancelledEventDomainEventPublisher,
                                                 DomainEventPublisher<PaymentFailedEvent>
                                                         paymentFailedEventDomainEventPublisher) {
        paiement.validatePayment(failureMessages);
        addCreditEntry(paiement, entréeCrédit);
        updateCreditHistory(paiement, creditHistories, TransactionType.CREDIT);

        if (failureMessages.isEmpty()) {
            log.info("Paiement is cancelled for order id: {}", paiement.getCommandeId().getValue());
            paiement.updateStatus(PaiementStatus.CANCELLED);
            return new PaymentCancelledEvent(paiement, ZonedDateTime.now(ZoneId.of(UTC)),
                    paymentCancelledEventDomainEventPublisher);
        } else {
            log.info("Paiement cancellation is failed for order id: {}", paiement.getCommandeId().getValue());
            paiement.updateStatus(PaiementStatus.FAILED);
            return new PaymentFailedEvent(paiement, ZonedDateTime.now(ZoneId.of(UTC)), failureMessages,
                    paymentFailedEventDomainEventPublisher);
        }
    }

    private void validateCreditEntry(Paiement paiement, EntreeCredit entréeCrédit, List<String> failureMessages) {
        if (paiement.getPrice().isGreaterThan(entréeCrédit.getTotalCreditAmount())) {
            log.error("Customer with id: {} doesn't have enough credit for paiement!",
                    paiement.getClientId().getValue());
            failureMessages.add("Customer with id=" + paiement.getClientId().getValue()
                    + " doesn't have enough credit for paiement!");
        }
    }

    private void subtractCreditEntry(Paiement paiement, EntreeCredit entréeCrédit) {
        entréeCrédit.subtractCreditAmount(paiement.getPrice());
    }

    private void updateCreditHistory(Paiement paiement,
                                     List<HistoriqueCredit> creditHistories,
                                     TransactionType transactionType) {
        creditHistories.add(HistoriqueCredit.builder()
                .creditHistoryId(new HistoriqueCreditId(UUID.randomUUID()))
                .customerId(paiement.getClientId())
                .amount(paiement.getPrice())
                .transactionType(transactionType)
                .build());
    }


    private void validateCreditHistory(EntreeCredit entréeCrédit,
                                       List<HistoriqueCredit> creditHistories,
                                       List<String> failureMessages) {
        Monnaie totalCreditHistory = getTotalHistoryAmount(creditHistories, TransactionType.CREDIT);
        Monnaie totalDebitHistory = getTotalHistoryAmount(creditHistories, TransactionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.error("Customer with id: {} doesn't have enough credit according to credit history",
                    entréeCrédit.getClientId().getValue());
            failureMessages.add("Customer with id=" + entréeCrédit.getClientId().getValue() +
                    " doesn't have enough credit according to credit history!");
        }

        if (!entréeCrédit.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
            log.error("Credit history total is not equal to current credit for customer id: {}!",
                    entréeCrédit.getClientId().getValue());
            failureMessages.add("Credit history total is not equal to current credit for customer id: {}" +
                    entréeCrédit.getClientId().getValue() + "!");
        }
    }

    private Monnaie getTotalHistoryAmount(List<HistoriqueCredit> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> transactionType == creditHistory.getTransactionType())
                .map(HistoriqueCredit::getAmount)
                .reduce(Monnaie.ZERO, Monnaie::add);
    }

    private void addCreditEntry(Paiement paiement, EntreeCredit entréeCrédit) {
        entréeCrédit.addCreditAmount(paiement.getPrice());
    }
}
