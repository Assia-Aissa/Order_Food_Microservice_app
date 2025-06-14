package com.food.ordering.system.restaurant.service.domain.ports.output.repository;

import com.food.ordering.system.restaurant.service.domain.entity.ApprobationCommande;

public interface OrderApprovalRepository {
    ApprobationCommande save(ApprobationCommande approbationCommande);
}
