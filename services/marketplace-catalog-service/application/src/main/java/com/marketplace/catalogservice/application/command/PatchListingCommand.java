package com.marketplace.catalogservice.application.command;

import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.ListingId;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PatchListingCommand(

        ListingId id,
        String title,
        String description,
        BigDecimal price,
        String currency,
        String category,
        List<String> tags,
        List<String> previewImageUrls,
        ListingStatus status
) {}

