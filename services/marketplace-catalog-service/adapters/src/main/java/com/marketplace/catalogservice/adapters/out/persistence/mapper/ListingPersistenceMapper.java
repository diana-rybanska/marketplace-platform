package com.marketplace.catalogservice.adapters.out.persistence.mapper;

import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingEntity;
import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingPreviewImageEntity;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.PreviewImage;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ListingPersistenceMapper {

    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "currency", source = "currency.code")
    ListingEntity toListingEntity(Listing listing);

    @Mapping(target = "listingId", expression = "java(listingId)")
    ListingPreviewImageEntity toListingPreviewImageEntity(PreviewImage image,
            @Context UUID listingId);

    List<ListingPreviewImageEntity> toListingPreviewImageEntities(
            List<PreviewImage> images,
            @Context UUID listingId
    );

    @Mapping(target = "id", expression = "java(new ListingId(entity.getId()))")
    @Mapping(target = "currency", expression = "java(new Currency(entity.getCurrency()))")
    @Mapping(target = "previewImages", ignore = true)
    Listing toListing(ListingEntity entity);

    @Mapping(target = "id", expression = "java(new ListingId(entity.getId()))")
    @Mapping(target = "currency", expression = "java(new Currency(entity.getCurrency()))")
    @Mapping(target = "previewImages", source = "images")
    Listing toListing(ListingEntity entity, List<ListingPreviewImageEntity> images);

    PreviewImage toPreviewImage(ListingPreviewImageEntity entity);

    List<PreviewImage> toPreviewImages(List<ListingPreviewImageEntity> entities);
}
