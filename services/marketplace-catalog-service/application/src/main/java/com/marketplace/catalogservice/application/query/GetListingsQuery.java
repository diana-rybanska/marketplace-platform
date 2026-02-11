package com.marketplace.catalogservice.application.query;

import lombok.Builder;

import java.util.List;

@Builder
public record GetListingsQuery(
        String category,
        List<String> tags
) {}
