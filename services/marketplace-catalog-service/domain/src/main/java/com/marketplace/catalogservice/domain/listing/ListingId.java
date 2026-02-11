package com.marketplace.catalogservice.domain.listing;

import lombok.Value;

import java.util.UUID;

@Value
public class ListingId {

    UUID value;

    public ListingId(UUID value) {
        this.value = value;
    }
}

