package com.marketplace.catalogservice.adapters.testdata;

import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingPreviewImageEntity;

import java.util.List;
import java.util.UUID;

public final class ListingPreviewImageEntityTestDataFactory {

    private ListingPreviewImageEntityTestDataFactory() {}

    public static List<ListingPreviewImageEntity> previewImages(UUID listingId) {
        return List.of(
                previewImage(listingId, 0, "http://img.com/a.jpg"),
                previewImage(listingId, 1, "http://img.com/b.jpg")
        );
    }

    public static ListingPreviewImageEntity previewImage(
            UUID listingId,
            int position,
            String url
    ) {
        return ListingPreviewImageEntity.builder()
                .id(UUID.randomUUID())
                .listingId(listingId)
                .url(url)
                .position(position)
                .build();
    }

    public static List<ListingPreviewImageEntity> newPreviewImages(UUID listingId) {
        return List.of(
                newPreviewImage(listingId, 0, "http://img.com/a.jpg"),
                newPreviewImage(listingId, 1, "http://img.com/b.jpg")
        );
    }

    public static ListingPreviewImageEntity newPreviewImage(
            UUID listingId,
            int position,
            String url
    ) {
        return ListingPreviewImageEntity.builder()
                .listingId(listingId)
                .url(url)
                .position(position)
                .build();
    }

}
