package com.marketplace.catalogservice.application.testdata;

import com.marketplace.catalogservice.application.command.PatchListingCommand;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.ListingId;

import java.math.BigDecimal;
import java.util.List;

public final class PatchListingCommandTestDataFactory {

    private PatchListingCommandTestDataFactory() {}

    public static PatchListingCommand aPatchListingCommand(ListingId id) {
        return PatchListingCommand.builder()
                .id(id)
                .title("Updated title")
                .description("Updated description")
                .price(BigDecimal.valueOf(120))
                .currency("EUR")
                .category("fashion")
                .tags(List.of("dress", "summer"))
                .previewImageUrls(List.of(
                        "http://img.com/a.jpg",
                        "http://img.com/b.jpg"
                ))
                .status(ListingStatus.ACTIVE)
                .build();
    }
}
