package com.marketplace.catalogservice.adapters.out.adapter;

import com.marketplace.catalogservice.adapters.out.persistence.adapter.ListingRepositoryAdapter;
import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingEntity;
import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingPreviewImageEntity;
import com.marketplace.catalogservice.adapters.out.persistence.mapper.ListingPersistenceMapper;
import com.marketplace.catalogservice.adapters.out.persistence.repository.ListingPreviewImageR2dbcRepository;
import com.marketplace.catalogservice.adapters.out.persistence.repository.ListingR2dbcRepository;
import com.marketplace.catalogservice.adapters.testdata.ListingTestDataFactory;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingRepositoryAdapterTest {

    @Mock
    private ListingR2dbcRepository listingRepository;

    @Mock
    private ListingPreviewImageR2dbcRepository previewImageRepository;

    @Mock
    private ListingPersistenceMapper mapper;

    @InjectMocks
    private ListingRepositoryAdapter adapter;

    @Nested
    class SaveTests {

        @Test
        void save_shouldSaveListingAndImages() {

            Listing listing = ListingTestDataFactory.aListing();

            ListingEntity entity = new ListingEntity();
            Listing savedDomain = ListingTestDataFactory.aListing();

            when(mapper.toListingEntity(listing)).thenReturn(entity);
            when(listingRepository.save(entity)).thenReturn(Mono.just(entity));
            when(mapper.toListingPreviewImageEntities(any(), any()))
                    .thenReturn(List.of(new ListingPreviewImageEntity()));
            when(previewImageRepository.saveAll(anyList())).thenReturn(Flux.empty());
            when(mapper.toListing(entity)).thenReturn(savedDomain);

            StepVerifier.create(adapter.save(listing))
                    .expectNext(savedDomain)
                    .verifyComplete();
        }

        @Test
        void save_withoutImages() {

            Listing listing = ListingTestDataFactory.aListing().toBuilder()
                    .previewImages(List.of())
                    .build();

            ListingEntity entity = new ListingEntity();

            when(mapper.toListingEntity(listing)).thenReturn(entity);
            when(listingRepository.save(entity)).thenReturn(Mono.just(entity));
            when(mapper.toListingPreviewImageEntities(any(), any())).thenReturn(List.of());
            when(mapper.toListing(entity)).thenReturn(listing);

            StepVerifier.create(adapter.save(listing))
                    .expectNext(listing)
                    .verifyComplete();

            verify(previewImageRepository, never()).saveAll(anyList());
        }

        @Test
        void save_mapperReturnsNullImages() {

            Listing listing = ListingTestDataFactory.aListing().toBuilder()
                    .previewImages(null)
                    .build();

            ListingEntity entity = new ListingEntity();
            ListingEntity savedEntity = new ListingEntity();

            when(mapper.toListingEntity(listing)).thenReturn(entity);
            when(listingRepository.save(entity)).thenReturn(Mono.just(savedEntity));
            when(mapper.toListingPreviewImageEntities(any(), any())).thenReturn(null);

            when(mapper.toListing(savedEntity))
                    .thenReturn(listing);

            StepVerifier.create(adapter.save(listing))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void save_mapperReturnsNullDomain_shouldThrowError() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingEntity entity = new ListingEntity();

            when(mapper.toListingEntity(listing)).thenReturn(entity);
            when(listingRepository.save(entity)).thenReturn(Mono.just(entity));
            when(mapper.toListingPreviewImageEntities(any(), any())).thenReturn(List.of());
            when(mapper.toListing(entity)).thenReturn(null);

            StepVerifier.create(adapter.save(listing))
                    .expectError(IllegalStateException.class)
                    .verify();
        }

    }

    @Nested
    class FindByIdTests {

        @Test
        void findById_listingFound_withImages() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingId id = listing.getId();

            ListingEntity entity = mock(ListingEntity.class);
            when(entity.getId()).thenReturn(id.getValue());

            List<ListingPreviewImageEntity> images = List.of(
                    mock(ListingPreviewImageEntity.class),
                    mock(ListingPreviewImageEntity.class)
            );

            when(listingRepository.findById(id.getValue()))
                    .thenReturn(Mono.just(entity));

            when(previewImageRepository.findByListingIdOrderByPosition(id.getValue()))
                    .thenReturn(Flux.fromIterable(images));

            when(mapper.toListing(entity, images))
                    .thenReturn(listing);

            StepVerifier.create(adapter.findById(id))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void findById_listingFound_withoutImages() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingId id = listing.getId();

            ListingEntity entity = mock(ListingEntity.class);
            when(entity.getId()).thenReturn(id.getValue());

            when(listingRepository.findById(id.getValue()))
                    .thenReturn(Mono.just(entity));

            when(previewImageRepository.findByListingIdOrderByPosition(id.getValue()))
                    .thenReturn(Flux.empty());

            when(mapper.toListing(entity, List.of()))
                    .thenReturn(listing);

            StepVerifier.create(adapter.findById(id))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void findById_listingNotFound() {

            ListingId id = new ListingId(UUID.randomUUID());

            when(listingRepository.findById(id.getValue()))
                    .thenReturn(Mono.empty());

            StepVerifier.create(adapter.findById(id))
                    .verifyComplete();
        }
    }

    @Nested
    class FindListingsTests {

        @Test
        void findListings_withTags() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingEntity entity = mock(ListingEntity.class);
            when(entity.getId()).thenReturn(listing.getId().getValue());

            List<String> tags = List.of("tag1", "tag2");

            when(listingRepository.findListings(eq(ListingStatus.ACTIVE), eq("fashion"), any()))
                    .thenReturn(Flux.just(entity));

            when(previewImageRepository.findByListingIdOrderByPosition(entity.getId()))
                    .thenReturn(Flux.empty());

            when(mapper.toListing(entity, List.of()))
                    .thenReturn(listing);

            StepVerifier.create(adapter.findListings(ListingStatus.ACTIVE, "fashion", tags))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void findListings_tagsNull() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingEntity entity = mock(ListingEntity.class);
            when(entity.getId()).thenReturn(listing.getId().getValue());

            when(listingRepository.findListings(ListingStatus.ACTIVE, "fashion", null))
                    .thenReturn(Flux.just(entity));

            when(previewImageRepository.findByListingIdOrderByPosition(entity.getId()))
                    .thenReturn(Flux.empty());

            when(mapper.toListing(entity, List.of()))
                    .thenReturn(listing);

            StepVerifier.create(adapter.findListings(ListingStatus.ACTIVE, "fashion", null))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void findListings_tagsEmpty() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingEntity entity = mock(ListingEntity.class);
            when(entity.getId()).thenReturn(listing.getId().getValue());

            when(listingRepository.findListings(ListingStatus.ACTIVE, "fashion", null))
                    .thenReturn(Flux.just(entity));

            when(previewImageRepository.findByListingIdOrderByPosition(entity.getId()))
                    .thenReturn(Flux.empty());

            when(mapper.toListing(entity, List.of()))
                    .thenReturn(listing);

            StepVerifier.create(adapter.findListings(ListingStatus.ACTIVE, "fashion", List.of()))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void findListings_emptyResult() {

            when(listingRepository.findListings(any(), any(), any()))
                    .thenReturn(Flux.empty());

            StepVerifier.create(adapter.findListings(ListingStatus.ACTIVE, "fashion", List.of()))
                    .verifyComplete();
        }
    }

    @Nested
    class SearchListingsTests {

        @Test
        void searchListings_success() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingEntity entity = mock(ListingEntity.class);
            when(entity.getId()).thenReturn(listing.getId().getValue());

            when(listingRepository.searchListings(ListingStatus.ACTIVE, "dress", "fashion"))
                    .thenReturn(Flux.just(entity));

            when(previewImageRepository.findByListingIdOrderByPosition(entity.getId()))
                    .thenReturn(Flux.empty());

            when(mapper.toListing(entity, List.of()))
                    .thenReturn(listing);

            StepVerifier.create(adapter.searchListings(ListingStatus.ACTIVE, "dress", "fashion"))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void searchListings_queryNull() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingEntity entity = mock(ListingEntity.class);
            when(entity.getId()).thenReturn(listing.getId().getValue());

            when(listingRepository.searchListings(ListingStatus.ACTIVE, null, "fashion"))
                    .thenReturn(Flux.just(entity));

            when(previewImageRepository.findByListingIdOrderByPosition(entity.getId()))
                    .thenReturn(Flux.empty());

            when(mapper.toListing(entity, List.of()))
                    .thenReturn(listing);

            StepVerifier.create(adapter.searchListings(ListingStatus.ACTIVE, null, "fashion"))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void searchListings_queryBlank_trimmedToNull() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingEntity entity = mock(ListingEntity.class);
            when(entity.getId()).thenReturn(listing.getId().getValue());

            when(listingRepository.searchListings(ListingStatus.ACTIVE, null, "fashion"))
                    .thenReturn(Flux.just(entity));

            when(previewImageRepository.findByListingIdOrderByPosition(entity.getId()))
                    .thenReturn(Flux.empty());

            when(mapper.toListing(entity, List.of()))
                    .thenReturn(listing);

            StepVerifier.create(adapter.searchListings(ListingStatus.ACTIVE, "   ", "fashion"))
                    .expectNext(listing)
                    .verifyComplete();
        }

        @Test
        void searchListings_emptyResult() {

            when(listingRepository.searchListings(any(), any(), any()))
                    .thenReturn(Flux.empty());

            StepVerifier.create(adapter.searchListings(ListingStatus.ACTIVE, "dress", "fashion"))
                    .verifyComplete();
        }
    }

}
