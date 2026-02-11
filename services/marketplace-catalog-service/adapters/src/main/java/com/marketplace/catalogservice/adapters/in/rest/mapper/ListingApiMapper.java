package com.marketplace.catalogservice.adapters.in.rest.mapper;

import com.marketplace.catalog.adapters.in.api.model.CreateListingRequest;
import com.marketplace.catalog.adapters.in.api.model.ListingCardResponse;
import com.marketplace.catalog.adapters.in.api.model.ListingDetailResponse;
import com.marketplace.catalog.adapters.in.api.model.PatchListingRequest;
import com.marketplace.catalogservice.application.command.CreateListingCommand;
import com.marketplace.catalogservice.application.command.PatchListingCommand;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.PreviewImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ListingApiMapper {

    /* ---------- REQUEST ---------- */

    @Mapping(target = "previewImageUrls", source = "previewImages")
    CreateListingCommand toCreateListingCommand(CreateListingRequest request);

    @Mapping(target = "id", expression = "java(new ListingId(id))")
    @Mapping(target = "previewImageUrls", source = "request.previewImages")
    PatchListingCommand toPatchListingCommand(UUID id, PatchListingRequest request);

    /* ---------- RESPONSE ---------- */

    @Mapping(target = "listing.id", source = "id.value")
    @Mapping(target = "listing.currency", source = "currency.code")
    @Mapping(target = "listing.previewImages", source = "previewImages")
    @Mapping(target = "listing.description", source = "description")
    @Mapping(target = "listing.createdAt", source = "createdAt")
    @Mapping(target = "listing.updatedAt", source = "updatedAt")
    ListingDetailResponse toListingDetailResponse(Listing listing);


    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "currency", source = "currency.code")
    ListingCardResponse toListingCardResponse(Listing listing);


    /* ---------- helpers ---------- */

    default List<String> mapUriListToStringList(List<URI> uris) {
        if (uris == null) {
            return null;
        }
        return uris.stream()
                .map(u -> u == null ? null : u.toString())
                .toList();
    }

    default List<URI> mapPreviewImageUrls(List<PreviewImage> images) {
        if (images == null) {
            return List.of();
        }

        return images.stream()
                .sorted(Comparator.comparing(PreviewImage::getPosition))
                .map(img -> URI.create(img.getUrl()))
                .toList();
    }
}
