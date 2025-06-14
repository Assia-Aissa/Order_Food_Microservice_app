package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.ApprobationCommandeStatus;
import com.food.ordering.system.domain.valueobject.CommandeId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.valueobject.ApprobationCommandeId;
import lombok.Getter;

@Getter
public class ApprobationCommande extends BaseEntity<ApprobationCommandeId> {
    private final RestaurantId restaurantId;
    private final CommandeId commandeId;
    private final ApprobationCommandeStatus approvalStatus;

    private ApprobationCommande(Builder builder) {
        setId(builder.approbationCommandeId);
        restaurantId = builder.restaurantId;
        commandeId = builder.commandeId;
        approvalStatus = builder.approvalStatus;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private ApprobationCommandeId approbationCommandeId;
        private RestaurantId restaurantId;
        private CommandeId commandeId;
        private ApprobationCommandeStatus approvalStatus;

        private Builder() {
        }

        public Builder orderApprovalId(ApprobationCommandeId val) {
            approbationCommandeId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderId(CommandeId val) {
            commandeId = val;
            return this;
        }

        public Builder approvalStatus(ApprobationCommandeStatus val) {
            approvalStatus = val;
            return this;
        }

        public ApprobationCommande build() {
            return new ApprobationCommande(this);
        }
    }
}
