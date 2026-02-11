package com.marketplace.catalogservice.application.query;

import lombok.Builder;

@Builder
public record SearchListingsQuery(
        String query,
        String category
) {}
