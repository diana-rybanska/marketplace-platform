package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.application.query.GetListingsQuery;
import com.marketplace.catalogservice.application.testdata.ListingTestDataFactory;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.Listing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetListingsUseCaseImplTest {

    @Mock
    ListingRepository listingRepository;

    @InjectMocks
    GetListingsUseCaseImpl useCase;

    @Test
    void getListings_shouldCallRepositoryWithActiveStatus() {

        GetListingsQuery query = GetListingsQuery.builder()
                .category("fashion")
                .tags(List.of("dress"))
                .build();

        Listing listing = ListingTestDataFactory.aListing();

        when(listingRepository.findListings(
                ListingStatus.ACTIVE,
                query.category(),
                query.tags()
        )).thenReturn(Flux.just(listing));

        Flux<Listing> result = useCase.getListings(query);

        StepVerifier.create(result)
                .expectNext(listing)
                .verifyComplete();

        verify(listingRepository).findListings(
                ListingStatus.ACTIVE,
                query.category(),
                query.tags()
        );
    }
}

