package com.marketplace.catalogservice.domain.listing;

import lombok.Value;

@Value
public class Currency {

    String code;

    public Currency(String code) {
        this.code = code.toUpperCase();
    }
}
