package com.marketplace.catalogservice.adapters.testdata;

import com.marketplace.catalog.adapters.in.api.model.CreateListingRequest;
import com.marketplace.catalog.adapters.in.api.model.PatchListingRequest;

import java.net.URI;
import java.util.List;

import static com.marketplace.catalog.adapters.in.api.model.ListingStatus.ACTIVE;

public final class ListingRequestTestDataFactory {

    private ListingRequestTestDataFactory() {
    }

    public static CreateListingRequest aCreateListingRequest() {
        CreateListingRequest request = new CreateListingRequest();
        request.setTitle("Dress");
        request.setDescription("Summer dress");
        request.setCurrency("eur");
        request.setCategory("dresses");
        request.setPrice(12.0);
        request.setTags(List.of("tag1", "tag2"));
        request.setPreviewImages(List.of(
                URI.create("https://cdn/img1.jpg"),
                URI.create("https://cdn/img2.jpg")
        ));
        return request;
    }

    public static PatchListingRequest aPatchListingRequest() {
        PatchListingRequest request = new PatchListingRequest();
        request.setTitle("Updated dress");
        request.setDescription("Updated summer dress");
        request.setCurrency("EUR");
        request.setCategory("dresses");
        request.setPrice(25.50);
        request.setStatus(ACTIVE);
        request.setTags(List.of("summer", "linen"));
        request.setPreviewImages(List.of(
                URI.create("https://cdn.test.com/img1.jpg"),
                URI.create("https://cdn.test.com/img2.jpg")
        ));
        return request;
    }
}

