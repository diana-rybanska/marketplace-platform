package com.marketplace.catalogservice.adapters.testdata;

import com.marketplace.catalogservice.domain.listing.Currency;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import com.marketplace.catalogservice.domain.listing.PreviewImage;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static com.marketplace.catalogservice.domain.enums.ListingStatus.ACTIVE;

public final class ListingTestDataFactory {

    private ListingTestDataFactory() {}

    public static Listing aListing() {
        return Listing.builder()
                .id(new ListingId(UUID.randomUUID()))
                .title("Test title")
                .description("Test description")
                .price(BigDecimal.TEN)
                .currency(new Currency("EUR"))
                .category("fashion")
                .status(ACTIVE)
                .tags(List.of("tag1","tag2"))
                .previewImages(previewImages())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public static List<PreviewImage> previewImages() {
        return List.of(
                previewImage(0, "http://img.com/a.jpg"),
                previewImage(1, "http://img.com/b.jpg")
        );
    }

    public static PreviewImage previewImage(int position, String url) {
        return PreviewImage.builder()
                .id(UUID.randomUUID())
                .url(url)
                .position(position)
                .build();
    }
}

