package com.food.ordering.system.order.service.domain.valueobject;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Adresse {
    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;

    public Adresse(UUID id, String street, String postalCode, String city) {
        this.id = id;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adresse that = (Adresse) o;
        return street.equals(that.street) && postalCode.equals(that.postalCode) && city.equals(that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode, city);
    }
}
