package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.command.CreateListingCommand;
import com.marketplace.catalogservice.application.port.in.CreateListingUseCase;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.Currency;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import com.marketplace.catalogservice.domain.listing.PreviewImage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class CreateListingUseCaseImpl implements CreateListingUseCase {

    private final ListingRepository listingRepository;

    @Override
    public Mono<Listing> createListing(CreateListingCommand command) {

        Listing listing = Listing.builder()
                .id(new ListingId(UUID.randomUUID()))
                .title(command.title())
                .description(command.description())
                .price(command.price())
                .currency(new Currency(command.currency()))
                .category(command.category())
                .status(ListingStatus.DRAFT)
                .tags(command.tags())
                .previewImages(mapPreviewImages(command.previewImageUrls()))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        return listingRepository.save(listing);
    }

    private List<PreviewImage> mapPreviewImages(List<String> urls) {
        if (urls == null) {
            return List.of();
        }

        AtomicInteger pos = new AtomicInteger(0);

        return urls.stream()
                .map(url -> PreviewImage.builder()
                        .id(UUID.randomUUID())
                        .url(url)
                        .position(pos.getAndIncrement())
                        .build())
                .toList();
    }
}

