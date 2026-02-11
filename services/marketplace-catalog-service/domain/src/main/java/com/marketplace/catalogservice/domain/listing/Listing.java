package com.marketplace.catalogservice.domain.listing;

import com.marketplace.catalogservice.domain.enums.ListingStatus;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Listing {

    ListingId id;
    String title;
    String description;
    BigDecimal price;
    Currency currency;
    String category;
    ListingStatus status;
    List<String> tags;
    List<PreviewImage> previewImages;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
