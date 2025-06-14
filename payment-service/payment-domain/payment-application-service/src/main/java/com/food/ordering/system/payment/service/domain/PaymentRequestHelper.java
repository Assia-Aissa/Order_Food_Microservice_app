package com.food.ordering.system.payment.service.domain;


import com.food.ordering.system.domain.valueobject.ClientId;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entity.EntreeCredit;
import com.food.ordering.system.payment.service.domain.entity.HistoriqueCredit;
import com.food.ordering.system.payment.service.domain.entity.Paiement;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import com.food.ordering.system.payment.service.domain.service.PaymentDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompletedMessagePublisher paymentCompletedEventDomainEventPublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher;
    private final PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository,
                                CreditEntryRepository creditEntryRepository,
                                CreditHistoryRepository creditHistoryRepository,
                                PaymentCompletedMessagePublisher paymentCompletedEventDomainEventPublisher,
                                PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher,
                                PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentCompletedEventDomainEventPublisher = paymentCompletedEventDomainEventPublisher;
        this.paymentCancelledEventDomainEventPublisher = paymentCancelledEventDomainEventPublisher;
        this.paymentFailedEventDomainEventPublisher = paymentFailedEventDomainEventPublisher;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received paiement complete event for order id: {}", paymentRequest.getOrderId());
        Paiement paiement = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        EntreeCredit entréeCrédit = getCreditEntry(paiement.getClientId());
        List<HistoriqueCredit> creditHistories = getCreditHistory(paiement.getClientId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent =
                paymentDomainService.validateAndInitiatePayment(paiement, entréeCrédit, creditHistories, failureMessages,
                        paymentCompletedEventDomainEventPublisher, paymentFailedEventDomainEventPublisher);
        persistDbObjects(paiement, entréeCrédit, creditHistories, failureMessages);
        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received paiement rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Paiement> paymentResponse = paymentRepository
                .findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentResponse.isEmpty()) {
            log.error("Paiement with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Paiement with order id: " +
                    paymentRequest.getOrderId() + " could not be found!");
        }
        Paiement paiement = paymentResponse.get();
        EntreeCredit entréeCrédit = getCreditEntry(paiement.getClientId());
        List<HistoriqueCredit> creditHistories = getCreditHistory(paiement.getClientId());
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService
                .validateAndCancelPayment(paiement, entréeCrédit, creditHistories, failureMessages,
                        paymentCancelledEventDomainEventPublisher, paymentFailedEventDomainEventPublisher);
        persistDbObjects(paiement, entréeCrédit, creditHistories, failureMessages);
        return paymentEvent;
    }

    private EntreeCredit getCreditEntry(ClientId clientId) {
        Optional<EntreeCredit> creditEntry = creditEntryRepository.findByCustomerId(clientId);
        if (creditEntry.isEmpty()) {
            log.error("Could not find credit entry for customer: {}", clientId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit entry for customer: " +
                    clientId.getValue());
        }
        return creditEntry.get();
    }

    private List<HistoriqueCredit> getCreditHistory(ClientId clientId) {
        Optional<List<HistoriqueCredit>> creditHistories = creditHistoryRepository.findByCustomerId(clientId);
        if (creditHistories.isEmpty()) {
            log.error("Could not find credit history for customer: {}", clientId.getValue());
            throw new PaymentApplicationServiceException("Could not find credit history for customer: " +
                    clientId.getValue());
        }
        return creditHistories.get();
    }

    private void persistDbObjects(Paiement paiement,
                                  EntreeCredit entréeCrédit,
                                  List<HistoriqueCredit> creditHistories,
                                  List<String> failureMessages) {
        paymentRepository.save(paiement);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(entréeCrédit);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }
    }

}
