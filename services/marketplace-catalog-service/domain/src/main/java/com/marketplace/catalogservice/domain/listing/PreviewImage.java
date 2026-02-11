package com.marketplace.catalogservice.domain.listing;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PreviewImage {

    UUID id;
    String url;
    Integer position;
}
