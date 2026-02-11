package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.command.CreateListingCommand;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.application.testdata.CreateListingCommandTestDataFactory;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.Listing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateListingUseCaseImplTest {

    @Mock
    ListingRepository listingRepository;

    @InjectMocks
    CreateListingUseCaseImpl useCase;

    @Test
    void createListing_shouldMapAllFields_andSave() {

        CreateListingCommand command =
                CreateListingCommandTestDataFactory.aCreateListingCommand();

        when(listingRepository.save(any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Listing result =
                useCase.createListing(command)
                        .block();

        assertThat(result).isNotNull();

        assertThat(result)
                .extracting(
                        Listing::getTitle,
                        Listing::getDescription,
                        Listing::getCategory,
                        l -> l.getCurrency().getCode(),
                        Listing::getPrice,
                        Listing::getTags
                )
                .containsExactly(
                        command.title(),
                        command.description(),
                        command.category(),
                        command.currency(),
                        command.price(),
                        command.tags()
                );

        assertThat(result.getStatus()).isEqualTo(ListingStatus.DRAFT);
        assertThat(result.getPreviewImages()).hasSize(2);

        verify(listingRepository).save(any());
    }

    @Test
    void createListing_shouldHandleNullPreviewImages() {

        CreateListingCommand command = CreateListingCommandTestDataFactory.aCreateListingCommand()
                        .toBuilder()
                        .previewImageUrls(null)
                        .build();

        when(listingRepository.save(any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Listing result =
                useCase.createListing(command)
                        .block();

        assertThat(result.getPreviewImages()).isEmpty();
    }

    @Test
    void createListing_shouldHandleEmptyPreviewImages() {

        CreateListingCommand command =
                CreateListingCommandTestDataFactory.aCreateListingCommand()
                        .toBuilder()
                        .previewImageUrls(List.of())
                        .build();

        when(listingRepository.save(any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        Listing result =
                useCase.createListing(command)
                        .block();

        assertThat(result.getPreviewImages()).isEmpty();
    }
}

