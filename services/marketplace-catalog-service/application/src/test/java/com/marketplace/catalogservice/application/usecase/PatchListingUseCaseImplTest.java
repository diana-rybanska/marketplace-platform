package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.command.PatchListingCommand;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.application.testdata.ListingTestDataFactory;
import com.marketplace.catalogservice.application.testdata.PatchListingCommandTestDataFactory;
import com.marketplace.catalogservice.domain.exceptions.ListingNotFoundException;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatchListingUseCaseImplTest {

    @Mock
    ListingRepository listingRepository;

    @InjectMocks
    PatchListingUseCaseImpl useCase;

    @Test
    void patchListing_shouldPatchAllFields() {

        Listing existing = ListingTestDataFactory.aListing();

        PatchListingCommand command = PatchListingCommandTestDataFactory
                .aPatchListingCommand(existing.getId());

        when(listingRepository.findById(existing.getId()))
                .thenReturn(Mono.just(existing));

        when(listingRepository.save(any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Listing result = useCase.patchListing(command).block();

        assertThat(result).isNotNull();

        assertThat(result)
                .extracting(
                        Listing::getTitle,
                        Listing::getDescription,
                        Listing::getPrice,
                        l -> l.getCurrency().getCode(),
                        Listing::getCategory,
                        Listing::getTags,
                        Listing::getStatus
                )
                .containsExactly(
                        command.title(),
                        command.description(),
                        command.price(),
                        command.currency(),
                        command.category(),
                        command.tags(),
                        command.status()
                );

        assertThat(result.getPreviewImages())
                .hasSize(command.previewImageUrls().size());

        verify(listingRepository).save(any());
    }

    @Test
    void patchListing_shouldKeepExistingValues_whenNulls() {

        Listing existing = ListingTestDataFactory.aListing();

        PatchListingCommand command = PatchListingCommand.builder()
                .id(existing.getId())
                .build();

        when(listingRepository.findById(existing.getId()))
                .thenReturn(Mono.just(existing));

        when(listingRepository.save(any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Listing result = useCase.patchListing(command).block();

        assertThat(result.getTitle()).isEqualTo(existing.getTitle());
        assertThat(result.getDescription()).isEqualTo(existing.getDescription());
        assertThat(result.getPrice()).isEqualTo(existing.getPrice());
        assertThat(result.getCurrency()).isEqualTo(existing.getCurrency());
        assertThat(result.getCategory()).isEqualTo(existing.getCategory());
        assertThat(result.getTags()).isEqualTo(existing.getTags());
        assertThat(result.getStatus()).isEqualTo(existing.getStatus());
    }

    @Test
    void patchListing_shouldThrow_whenListingNotFound() {

        ListingId id = new ListingId(UUID.randomUUID());

        when(listingRepository.findById(id))
                .thenReturn(Mono.empty());

        PatchListingCommand command = PatchListingCommand.builder()
                .id(id)
                .build();

        StepVerifier.create(useCase.patchListing(command))
                .expectError(ListingNotFoundException.class)
                .verify();
    }
}

