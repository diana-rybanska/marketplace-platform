package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.application.query.SearchListingsQuery;
import com.marketplace.catalogservice.application.testdata.ListingTestDataFactory;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.Listing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchListingsUseCaseImplTest {

    @Mock
    ListingRepository listingRepository;

    @InjectMocks
    SearchListingsUseCaseImpl useCase;

    @Test
    void search_shouldCallRepositoryWithActiveStatusAndReturnResults() {
        SearchListingsQuery query = SearchListingsQuery.builder()
                .query("dress")
                .category("fashion")
                .build();

        Listing listing = ListingTestDataFactory.aListing();

        when(listingRepository.searchListings(
                ListingStatus.ACTIVE,
                query.query(),
                query.category()
        )).thenReturn(Flux.just(listing));

        List<Listing> result =
                useCase.search(query)
                        .collectList()
                        .block();

        assertThat(result)
                .hasSize(1)
                .containsExactly(listing);

        verify(listingRepository).searchListings(
                ListingStatus.ACTIVE,
                query.query(),
                query.category()
        );
    }
}

