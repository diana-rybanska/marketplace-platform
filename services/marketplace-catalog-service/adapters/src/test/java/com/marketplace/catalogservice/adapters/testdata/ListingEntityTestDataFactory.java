package com.marketplace.catalogservice.adapters.testdata;

import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.marketplace.catalogservice.domain.enums.ListingStatus.ACTIVE;

public final class ListingEntityTestDataFactory {

    private ListingEntityTestDataFactory() {}

    public static ListingEntity aListingEntity() {
        return ListingEntity.builder()
                .id(UUID.randomUUID())
                .title("Test title")
                .description("Test description")
                .price(BigDecimal.TEN)
                .currency("EUR")
                .category("fashion")
                .status(ACTIVE)
                .tags(List.of("tag1", "tag2"))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public static ListingEntity newListingEntity() {
        return ListingEntity.builder()
                .title("Test title")
                .description("Test description")
                .price(BigDecimal.TEN)
                .currency("EUR")
                .category("fashion")
                .status(ACTIVE)
                .tags(List.of("tag1", "tag2"))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

}
