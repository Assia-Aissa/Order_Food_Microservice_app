package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.ArticleId;
import com.food.ordering.system.order.service.domain.valueobject.Adresse;
import com.food.ordering.system.order.service.domain.valueobject.NumeroSuivi;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Commande extends AggregateRoot<CommandeId> {
    private final ClientId clientId;
    private final RestaurantId restaurantId;
    private final Adresse deliveryAddress;
    private final Monnaie price;
    private final List<Article> items;

    private NumeroSuivi numeroSuivi;
    private CommandeStatus commandeStatus;
    private List<String> failureMessages;

    public static final String FAILURE_MESSAGE_DELIMITER = ",";


    /**
     * Initializes the order by setting its ID, tracking ID, and status.
     * Also initializes the order items.
     */
    public void initializeOrder() {
        setId(new CommandeId(UUID.randomUUID()));
        numeroSuivi = new NumeroSuivi(UUID.randomUUID());
        commandeStatus = CommandeStatus.PENDING;
        initializeOrderItems();
    }

    /**
     * Validates the order by checking its initial state, total price, and items price.
     */
    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    /**
     * Marks the order as paid.
     *
     * @throws OrderDomainException if the order is not in the correct state for the pay operation
     */
    public void pay() {
        if (commandeStatus != CommandeStatus.PENDING) {
            throw new OrderDomainException("Commande is not in correct state for pay operation!");
        }
        commandeStatus = CommandeStatus.PAID;
    }

    /**
     * Approves the order.
     *
     * @throws OrderDomainException if the order is not in the correct state for the approval operation
     */
    public void approve() {
        if(commandeStatus != CommandeStatus.PAID) {
            throw new OrderDomainException("Commande is not in correct state for approve operation!");
        }
        commandeStatus = CommandeStatus.APPROVED;
    }

    /**
     * Initiates the cancellation of the order payment.
     *
     * @param failureMessages the list of failure messages
     * @throws OrderDomainException if the order is not in the correct state for the initCancel operation
     */
    public void initCancel(List<String> failureMessages) {
        if (commandeStatus != CommandeStatus.PAID) {
            throw new OrderDomainException("Commande is not in correct state for initCancel operation!");
        }
        commandeStatus = CommandeStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    /**
     * Cancels the order.
     *
     * @param failureMessages the list of failure messages
     * @throws OrderDomainException if the order is not in the correct state for the cancel operation
     */
    public void cancel(List<String> failureMessages) {
        if (!(commandeStatus == CommandeStatus.CANCELLING || commandeStatus == CommandeStatus.PENDING)) {
            throw new OrderDomainException("Commande is not in correct state for cancel operation!");
        }
        commandeStatus = CommandeStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    /**
     * Updates the failure messages for the order.
     *
     * @param failureMessages the list of failure messages to be added
     */
    private void updateFailureMessages(List<String> failureMessages) {
        if (this.failureMessages != null && failureMessages != null) {
            this.failureMessages.addAll(failureMessages.stream().filter(message -> !message.isEmpty()).toList());
        }
        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }

    /**
     * Validates the initial state of the order.
     *
     * @throws OrderDomainException if the order is not in the correct state for initialization
     */
    private void validateInitialOrder() {
        if (commandeStatus != null || getId() != null) {
            throw new OrderDomainException("Commande is not in correct state for initialization!");
        }
    }

    /**
     * Validates the total price of the order.
     *
     * @throws OrderDomainException if the total price is not greater than zero
     */
    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    /**
     * Validates the price of each item in the order and the total price of the order.
     *
     * @throws OrderDomainException if the total price of the items does not match the total price of the order
     */
    private void validateItemsPrice() {
        Monnaie orderItemsTotal = items.stream().map(article -> {
            validateItemPrice(article);
            return article.getSubTotal();
        }).reduce(Monnaie.ZERO, Monnaie::add);

        if (!price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                    + " is not equal to Commande items total: " + orderItemsTotal.getAmount() + "!");
        }
    }

    /**
     * Validates the price of an individual order item.
     *
     * @param article the order item to be validated
     * @throws OrderDomainException if the price of the order item is not valid
     */
    private void validateItemPrice(Article article) {
        if (!article.isPriceValid()) {
            throw new OrderDomainException("Commande item price: " + article.getPrice().getAmount() +
                    " is not valid for product " + article.getProduit().getId().getValue());
        }
    }

    /**
     * Initializes the order items by setting their IDs.
     */
    private void initializeOrderItems() {
        long itemId = 1;
        for (Article article : items) {
            article.initializeOrderItem(super.getId(), new ArticleId(itemId++));
        }
    }

    private Commande(Builder builder) {
        super.setId(builder.commandeId);
        clientId = builder.clientId;
        restaurantId = builder.restaurantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        numeroSuivi = builder.numeroSuivi;
        commandeStatus = builder.commandeStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CommandeId commandeId;
        private ClientId clientId;
        private RestaurantId restaurantId;
        private Adresse deliveryAddress;
        private Monnaie price;
        private List<Article> items;
        private NumeroSuivi numeroSuivi;
        private CommandeStatus commandeStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder orderId(CommandeId val) {
            commandeId = val;
            return this;
        }

        public Builder customerId(ClientId val) {
            clientId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder deliveryAddress(Adresse val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Monnaie val) {
            price = val;
            return this;
        }

        public Builder items(List<Article> val) {
            items = val;
            return this;
        }

        public Builder trackingId(NumeroSuivi val) {
            numeroSuivi = val;
            return this;
        }

        public Builder orderStatus(CommandeStatus val) {
            commandeStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Commande build() {
            return new Commande(this);
        }
    }
}