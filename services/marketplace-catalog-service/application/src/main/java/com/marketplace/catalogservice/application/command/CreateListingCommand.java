package com.marketplace.catalogservice.application.command;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder(toBuilder = true)
public record CreateListingCommand(

        String title,
        String description,
        BigDecimal price,
        String currency,
        String category,
        List<String> tags,
        List<String> previewImageUrls
) {}