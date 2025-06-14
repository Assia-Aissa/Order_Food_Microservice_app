package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.Monnaie;
import com.food.ordering.system.domain.valueobject.ApprobationCommandeStatus;
import com.food.ordering.system.domain.valueobject.CommandeStatus;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.valueobject.ApprobationCommandeId;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
   private ApprobationCommande approbationCommande;
   private boolean active;
   private final DétailsCommande détailsCommande;

   public void validateOrder(List<String> failureMessages) {
       if (détailsCommande.getCommandeStatus() != CommandeStatus.PAID) {
           failureMessages.add("Payment is not completed for order: " + détailsCommande.getId());
       }
       Monnaie totalAmount = détailsCommande.getProduits().stream().map(product -> {
           if (!product.isAvailable()) {
               failureMessages.add("Produit with id: " + product.getId().getValue()
                       + " is not available");
           }
           return product.getPrice().multiply(product.getQuantity());
       }).reduce(Monnaie.ZERO, Monnaie::add);

       if (!totalAmount.equals(détailsCommande.getTotalAmount())) {
           failureMessages.add("Price total is not correct for order: " + détailsCommande.getId());
       }
   }

   public void constructOrderApproval(ApprobationCommandeStatus approbationCommandeStatus) {
       this.approbationCommande = ApprobationCommande.builder()
               .orderApprovalId(new ApprobationCommandeId(UUID.randomUUID()))
               .restaurantId(this.getId())
               .orderId(this.getDétailsCommande().getId())
               .approvalStatus(approbationCommandeStatus)
               .build();
   }

    public void setActive(boolean active) {
        this.active = active;
    }

    private Restaurant(Builder builder) {
        setId(builder.restaurantId);
        approbationCommande = builder.approbationCommande;
        active = builder.active;
        détailsCommande = builder.détailsCommande;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private ApprobationCommande approbationCommande;
        private boolean active;
        private DétailsCommande détailsCommande;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderApproval(ApprobationCommande val) {
            approbationCommande = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(DétailsCommande val) {
            détailsCommande = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
