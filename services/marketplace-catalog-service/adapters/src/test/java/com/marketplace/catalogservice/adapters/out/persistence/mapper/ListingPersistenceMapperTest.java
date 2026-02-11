package com.marketplace.catalogservice.adapters.out.persistence.mapper;

import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingEntity;
import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingPreviewImageEntity;
import com.marketplace.catalogservice.adapters.testdata.ListingEntityTestDataFactory;
import com.marketplace.catalogservice.adapters.testdata.ListingPreviewImageEntityTestDataFactory;
import com.marketplace.catalogservice.adapters.testdata.ListingTestDataFactory;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.PreviewImage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ListingPersistenceMapperTest {

    private final ListingPersistenceMapper mapper =
            Mappers.getMapper(ListingPersistenceMapper.class);

    @Nested
    class ToListingEntityTests {

        @Test
        void toListingEntity_full() {
            Listing listing = ListingTestDataFactory.aListing();

            ListingEntity result = mapper.toListingEntity(listing);

            assertThat(result).isNotNull();

            assertThat(result)
                    .extracting(
                            ListingEntity::getId,
                            ListingEntity::getTitle,
                            ListingEntity::getDescription,
                            ListingEntity::getPrice,
                            ListingEntity::getCurrency,
                            ListingEntity::getCategory,
                            ListingEntity::getStatus,
                            ListingEntity::getTags
                    )
                    .containsExactly(
                            listing.getId().getValue(),
                            listing.getTitle(),
                            listing.getDescription(),
                            listing.getPrice(),
                            listing.getCurrency().getCode(),
                            listing.getCategory(),
                            listing.getStatus(),
                            listing.getTags()
                    );
        }

        @Test
        void toListingEntity_nullInput() {
            ListingEntity result = mapper.toListingEntity(null);

            assertThat(result).isNull();
        }

        @Test
        void toListingEntity_nullValues() {
            Listing listing = ListingTestDataFactory.aListing()
                    .toBuilder()
                    .id(null)
                    .currency(null)
                    .tags(null)
                    .build();

            ListingEntity result = mapper.toListingEntity(listing);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isNull();
            assertThat(result.getCurrency()).isNull();
            assertThat(result.getTags()).isNull();
        }
    }

    @Nested
    class ToListingPreviewImageEntityTests {

        @Test
        void toListingPreviewImageEntity_full() {
            PreviewImage image =
                    ListingTestDataFactory.previewImage(1, "http://img.com/a.jpg");

            UUID listingId = UUID.randomUUID();

            ListingPreviewImageEntity result =
                    mapper.toListingPreviewImageEntity(image, listingId);

            assertThat(result).isNotNull();

            assertThat(result)
                    .extracting(
                            ListingPreviewImageEntity::getId,
                            ListingPreviewImageEntity::getListingId,
                            ListingPreviewImageEntity::getUrl,
                            ListingPreviewImageEntity::getPosition
                    )
                    .containsExactly(
                            image.getId(),
                            listingId,
                            image.getUrl(),
                            image.getPosition()
                    );
        }

        @Test
        void toListingPreviewImageEntity_nullInput() {
            ListingPreviewImageEntity result =
                    mapper.toListingPreviewImageEntity(null, UUID.randomUUID());

            assertThat(result).isNull();
        }
    }

    @Nested
    class ToListingPreviewImageEntitiesTests {

        @Test
        void toListingPreviewImageEntities_full() {
            List<PreviewImage> images = ListingTestDataFactory.previewImages();
            UUID listingId = UUID.randomUUID();

            List<ListingPreviewImageEntity> result =
                    mapper.toListingPreviewImageEntities(images, listingId);

            assertThat(result)
                    .isNotNull()
                    .hasSize(images.size());

            assertThat(result)
                    .extracting(
                            ListingPreviewImageEntity::getListingId
                    )
                    .containsOnly(listingId);

            assertThat(result)
                    .extracting(
                            ListingPreviewImageEntity::getUrl,
                            ListingPreviewImageEntity::getPosition
                    )
                    .containsExactly(
                            tuple(images.get(0).getUrl(), images.get(0).getPosition()),
                            tuple(images.get(1).getUrl(), images.get(1).getPosition())
                    );
        }

        @Test
        void toListingPreviewImageEntities_nullInput() {
            List<ListingPreviewImageEntity> result =
                    mapper.toListingPreviewImageEntities(null, UUID.randomUUID());

            assertThat(result).isNull();
        }
    }

    @Nested
    class ToListingTests {

        @Test
        void toListing_full() {
            ListingEntity entity = ListingEntityTestDataFactory.aListingEntity();

            Listing result = mapper.toListing(entity);

            assertThat(result).isNotNull();

            assertThat(result)
                    .extracting(
                            r -> r.getId().getValue(),
                            r -> r.getCurrency().getCode(),
                            Listing::getTitle,
                            Listing::getDescription,
                            Listing::getPrice,
                            Listing::getCategory,
                            Listing::getStatus,
                            Listing::getTags
                    )
                    .containsExactly(
                            entity.getId(),
                            entity.getCurrency(),
                            entity.getTitle(),
                            entity.getDescription(),
                            entity.getPrice(),
                            entity.getCategory(),
                            entity.getStatus(),
                            entity.getTags()
                    );

            assertThat(result.getPreviewImages()).isNull();
        }

        @Test
        void toListing_nullInput() {
            Listing result = mapper.toListing(null);

            assertThat(result).isNull();
        }

        @Test
        void toListing_nullValues() {
            ListingEntity entity = ListingEntityTestDataFactory.aListingEntity();
            entity.setTags(null);

            Listing result = mapper.toListing(entity);

            assertThat(result.getTags()).isNull();
        }
    }

    @Nested
    class ToListingWithImagesTests {

        @Test
        void toListing_withImages_full() {
            ListingEntity entity = ListingEntityTestDataFactory.aListingEntity();
            List<ListingPreviewImageEntity> images =
                    ListingPreviewImageEntityTestDataFactory.previewImages(entity.getId());

            Listing result = mapper.toListing(entity, images);

            assertThat(result).isNotNull();

            assertThat(result)
                    .extracting(
                            r -> r.getId().getValue(),
                            r -> r.getCurrency().getCode(),
                            Listing::getTitle,
                            Listing::getDescription,
                            Listing::getPrice,
                            Listing::getCategory,
                            Listing::getStatus,
                            Listing::getTags
                    )
                    .containsExactly(
                            entity.getId(),
                            entity.getCurrency(),
                            entity.getTitle(),
                            entity.getDescription(),
                            entity.getPrice(),
                            entity.getCategory(),
                            entity.getStatus(),
                            entity.getTags()
                    );

            assertThat(result.getPreviewImages())
                    .isNotNull()
                    .hasSize(images.size())
                    .isEqualTo(mapper.toPreviewImages(images));
        }

        @Test
        void toListing_withImages_nullValues() {
            ListingEntity entity = ListingEntityTestDataFactory.aListingEntity();
            entity.setTags(null);

            Listing result = mapper.toListing(entity, null);

            assertThat(result).isNotNull();
            assertThat(result.getTags()).isNull();
            assertThat(result.getPreviewImages()).isNull();
        }


        @Test
        void toListing_withImages_nullInput() {
            Listing result = mapper.toListing(null, null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class ToPreviewImageTests {

        @Test
        void toPreviewImage_full() {
            UUID listingId = UUID.randomUUID();
            ListingPreviewImageEntity entity =
                    ListingPreviewImageEntityTestDataFactory
                            .previewImage(listingId, 1, "http://img.com/a.jpg");

            PreviewImage result = mapper.toPreviewImage(entity);

            assertThat(result).isNotNull();

            assertThat(result)
                    .extracting(
                            PreviewImage::getId,
                            PreviewImage::getUrl,
                            PreviewImage::getPosition
                    )
                    .containsExactly(
                            entity.getId(),
                            entity.getUrl(),
                            entity.getPosition()
                    );
        }

        @Test
        void toPreviewImage_nullInput() {
            PreviewImage result = mapper.toPreviewImage(null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class ToPreviewImagesTests {

        @Test
        void toPreviewImages_full() {
            UUID listingId = UUID.randomUUID();
            List<ListingPreviewImageEntity> entities =
                    ListingPreviewImageEntityTestDataFactory.previewImages(listingId);

            List<PreviewImage> result = mapper.toPreviewImages(entities);

            assertThat(result)
                    .isNotNull()
                    .hasSize(entities.size())
                    .isEqualTo(
                            entities.stream()
                                    .map(mapper::toPreviewImage)
                                    .toList()
                    );
        }

        @Test
        void toPreviewImages_nullInput() {
            List<PreviewImage> result = mapper.toPreviewImages(null);

            assertThat(result).isNull();
        }
    }

}
