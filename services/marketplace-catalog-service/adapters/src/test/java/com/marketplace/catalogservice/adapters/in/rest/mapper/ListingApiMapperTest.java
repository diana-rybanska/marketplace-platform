package com.marketplace.catalogservice.adapters.in.rest.mapper;

import com.marketplace.catalog.adapters.in.api.model.*;
import com.marketplace.catalogservice.adapters.testdata.ListingRequestTestDataFactory;
import com.marketplace.catalogservice.adapters.testdata.ListingTestDataFactory;
import com.marketplace.catalogservice.application.command.CreateListingCommand;
import com.marketplace.catalogservice.application.command.PatchListingCommand;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.PreviewImage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ListingApiMapperTest {

    private final ListingApiMapper mapper =
            Mappers.getMapper(ListingApiMapper.class);

    @Nested
    class ToCreateListingCommandTests {

        @Test
        void toCreateListingCommand_full() {
            CreateListingRequest request =
                    ListingRequestTestDataFactory.aCreateListingRequest();

            CreateListingCommand result = mapper.toCreateListingCommand(request);

            assertThat(result).isNotNull();

            assertThat(result)
                    .extracting(
                            CreateListingCommand::title,
                            CreateListingCommand::description,
                            CreateListingCommand::category,
                            CreateListingCommand::currency,
                            CreateListingCommand::price
                    )
                    .containsExactly(
                            request.getTitle(),
                            request.getDescription(),
                            request.getCategory(),
                            request.getCurrency(),
                            BigDecimal.valueOf(request.getPrice())
                    );

            assertThat(result.previewImageUrls())
                    .isNotNull()
                    .hasSize(2)
                    .isEqualTo(mapper.mapUriListToStringList(request.getPreviewImages()));

            assertThat(result.tags())
                    .isNotNull()
                    .hasSize(2)
                    .containsExactlyElementsOf(request.getTags());
        }

        @Test
        void toCreateListingCommand_nullValues() {
            CreateListingRequest request =
                    ListingRequestTestDataFactory.aCreateListingRequest()
                            .price(null)
                            .tags(null)
                            .previewImages(null);

            CreateListingCommand result = mapper.toCreateListingCommand(request);

            assertThat(result).isNotNull();
            assertThat(result.price()).isNull();
            assertThat(result.tags()).isNull();
            assertThat(result.previewImageUrls()).isNull();
        }

        @Test
        void toCreateListingCommand_nullInput() {
            CreateListingCommand result = mapper.toCreateListingCommand(null);
            assertThat(result).isNull();
        }
    }


    @Nested
    class ToPatchListingCommandTests {

        @Test
        void toPatchListingCommand_full() {
            UUID uuid = UUID.randomUUID();
            PatchListingRequest request =
                    ListingRequestTestDataFactory.aPatchListingRequest();

            PatchListingCommand result = mapper.toPatchListingCommand(uuid, request);

            assertThat(result).isNotNull();

            assertThat(result)
                    .extracting(
                            r -> r.id().getValue(),
                            PatchListingCommand::title,
                            PatchListingCommand::description,
                            PatchListingCommand::category,
                            PatchListingCommand::currency,
                            PatchListingCommand::price,
                            PatchListingCommand::status
                    )
                    .containsExactly(
                            uuid,
                            request.getTitle(),
                            request.getDescription(),
                            request.getCategory(),
                            request.getCurrency(),
                            BigDecimal.valueOf(request.getPrice()),
                            ListingStatus.valueOf(request.getStatus().getValue())
                    );

            assertThat(result.previewImageUrls())
                    .isNotNull()
                    .hasSize(2)
                    .isEqualTo(mapper.mapUriListToStringList(request.getPreviewImages()));

            assertThat(result.tags())
                    .isNotNull()
                    .hasSize(2)
                    .containsExactlyElementsOf(request.getTags());
        }

        @Test
        void toPatchListingCommand_nullValues() {
            PatchListingRequest request =
                    ListingRequestTestDataFactory.aPatchListingRequest();

            request.setPrice(null);
            request.setTags(null);
            request.setPreviewImages(null);

            PatchListingCommand result =
                    mapper.toPatchListingCommand(UUID.randomUUID(), request);

            assertThat(result).isNotNull();
            assertThat(result.price()).isNull();
            assertThat(result.previewImageUrls()).isNull();
            assertThat(result.tags()).isNull();
        }

        @Test
        void toPatchListingCommand_nullPatchInput() {
            PatchListingCommand result =
                    mapper.toPatchListingCommand(UUID.randomUUID(), null);

            assertThat(result).isNotNull();
        }

        @Test
        void toPatchListingCommand_nullInput() {
            PatchListingCommand result =
                    mapper.toPatchListingCommand(null, null);

            assertThat(result).isNull();
        }
    }


    @Nested
    class ToListingDetailResponseTests {

        @Test
        void toListingDetailResponse_full() {
            Listing listing = ListingTestDataFactory.aListing();

            ListingDetailResponse result = mapper.toListingDetailResponse(listing);

            assertThat(result).isNotNull();
            assertThat(result.getListing()).isNotNull();

            assertThat(result.getListing())
                    .extracting(
                            ListingDetailResponseListing::getId,
                            ListingDetailResponseListing::getCurrency,
                            ListingDetailResponseListing::getDescription,
                            ListingDetailResponseListing::getCreatedAt,
                            ListingDetailResponseListing::getUpdatedAt
                    )
                    .containsExactly(
                            listing.getId().getValue(),
                            listing.getCurrency().getCode(),
                            listing.getDescription(),
                            listing.getCreatedAt(),
                            listing.getUpdatedAt()
                    );

            assertThat(result.getListing().getPreviewImages())
                    .isNotNull()
                    .hasSize(2)
                    .isEqualTo(mapper.mapPreviewImageUrls(listing.getPreviewImages()));
        }

        @Test
        void toListingDetailResponse_nullValues() {
            Listing listing = ListingTestDataFactory.aListing()
                    .toBuilder()
                    .id(null)
                    .currency(null)
                    .previewImages(null)
                    .build();

            ListingDetailResponse result = mapper.toListingDetailResponse(listing);

            assertThat(result).isNotNull();
            assertThat(result.getListing()).isNotNull();
            assertThat(result.getListing().getId()).isNull();
            assertThat(result.getListing().getCurrency()).isNull();
            assertThat(result.getListing().getPreviewImages()).isEmpty();
        }

        @Test
        void toListingDetailResponse_emptyListing() {
            Listing listing = Listing.builder().build();

            ListingDetailResponse result = mapper.toListingDetailResponse(listing);

            assertThat(result).isNotNull();
            assertThat(result.getListing()).isNotNull();
        }

        @Test
        void toListingDetailResponse_nullInput() {
            ListingDetailResponse result = mapper.toListingDetailResponse(null);
            assertThat(result).isNull();
        }
    }

    @Nested
    class ToListingCardResponseTests {

        @Test
        void toListingCardResponse_full() {
            Listing listing = ListingTestDataFactory.aListing();

            ListingCardResponse result = mapper.toListingCardResponse(listing);

            assertThat(result).isNotNull();

            assertThat(result)
                    .extracting(
                            ListingCardResponse::getId,
                            ListingCardResponse::getCurrency,
                            ListingCardResponse::getTitle,
                            ListingCardResponse::getPrice,
                            ListingCardResponse::getCategory,
                            ListingCardResponse::getStatus
                    )
                    .containsExactly(
                            listing.getId().getValue(),
                            listing.getCurrency().getCode(),
                            listing.getTitle(),
                            listing.getPrice().doubleValue(),
                            listing.getCategory(),
                            com.marketplace.catalog.adapters.in.api.model.ListingStatus.ACTIVE
                    );

            assertThat(result.getTags())
                    .isNotNull()
                    .containsExactlyElementsOf(listing.getTags());

            assertThat(result.getPreviewImages())
                    .isNotNull()
                    .isEqualTo(mapper.mapPreviewImageUrls(listing.getPreviewImages()));
        }

        @Test
        void toListingCardResponse_nullValues() {
            Listing listing = ListingTestDataFactory.aListing().toBuilder()
                    .id(null)
                    .price(null)
                    .currency(null)
                    .tags(null)
                    .previewImages(null)
                    .build();

            ListingCardResponse result = mapper.toListingCardResponse(listing);

            assertThat(result).isNotNull();
            assertThat(result)
                    .extracting(
                            ListingCardResponse::getId,
                            ListingCardResponse::getPrice,
                            ListingCardResponse::getCurrency
                    )
                    .containsExactly(
                            null, null, null
                    );

            assertThat(result.getTags()).isEmpty();
            assertThat(result.getPreviewImages()).isEmpty();
        }


        @Test
        void toListingCardResponse_nullInput() {
            ListingCardResponse result = mapper.toListingCardResponse(null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class MapUriListToStringListTests {

        @Test
        void mapUriListToStringList_full() {
            List<URI> input = List.of(
                    URI.create("https://cdn/img1.jpg"),
                    URI.create("https://cdn/img2.jpg")
            );

            List<String> result = mapper.mapUriListToStringList(input);

            assertThat(result)
                    .containsExactly(
                            "https://cdn/img1.jpg",
                            "https://cdn/img2.jpg"
                    );
        }

        @Test
        void mapUriListToStringList_containsNullItem() {
            List<URI> input = Arrays.asList(
                    URI.create("https://cdn/img1.jpg"),
                    null
            );

            List<String> result = mapper.mapUriListToStringList(input);

            assertThat(result)
                    .containsExactly(
                            "https://cdn/img1.jpg",
                            null
                    );
        }

        @Test
        void mapUriListToStringList_nullInput() {
            List<String> result = mapper.mapUriListToStringList(null);

            assertThat(result).isNull();
        }
    }

    @Nested
    class MapPreviewImageUrlsTests {

        @Test
        void mapPreviewImageUrls_nullInput() {
            List<URI> result = mapper.mapPreviewImageUrls(null);

            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }

        @Test
        void mapPreviewImageUrls_sortsByPosition() {
            List<PreviewImage> images = List.of(
                    ListingTestDataFactory.previewImage(2, "http://img.com/b.jpg"),
                    ListingTestDataFactory.previewImage(1, "http://img.com/a.jpg")
            );

            List<URI> result = mapper.mapPreviewImageUrls(images);

            assertThat(result)
                    .containsExactly(
                            URI.create("http://img.com/a.jpg"),
                            URI.create("http://img.com/b.jpg")
                    );
        }
    }

}
