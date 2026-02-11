package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.application.testdata.ListingTestDataFactory;
import com.marketplace.catalogservice.domain.exceptions.ListingNotFoundException;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetListingByIdUseCaseImplTest {

    @Mock
    ListingRepository listingRepository;

    @InjectMocks
    GetListingByIdUseCaseImpl useCase;

    @Test
    void getById_shouldReturnListing_whenFound() {

        Listing listing = ListingTestDataFactory.aListing();
        ListingId id = listing.getId();

        when(listingRepository.findById(id))
                .thenReturn(Mono.just(listing));

        Listing result =
                useCase.getById(id)
                        .block();

        assertThat(result).isEqualTo(listing);
        verify(listingRepository).findById(id);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {

        ListingId id = new ListingId(UUID.randomUUID());

        when(listingRepository.findById(id))
                .thenReturn(Mono.empty());

        assertThatThrownBy(() -> useCase.getById(id).block())
                .isInstanceOf(ListingNotFoundException.class);

        verify(listingRepository).findById(id);
    }
}

