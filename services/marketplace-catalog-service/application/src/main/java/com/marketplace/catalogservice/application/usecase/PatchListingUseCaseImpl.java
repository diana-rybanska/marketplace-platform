package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.command.PatchListingCommand;
import com.marketplace.catalogservice.application.port.in.PatchListingUseCase;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.domain.exceptions.ListingNotFoundException;
import com.marketplace.catalogservice.domain.listing.Currency;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.PreviewImage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class PatchListingUseCaseImpl implements PatchListingUseCase {

    private final ListingRepository listingRepository;

    @Override
    public Mono<Listing> patchListing(PatchListingCommand command) {
        return listingRepository.findById(command.id())
                .switchIfEmpty(Mono.error(
                        new ListingNotFoundException(command.id().getValue())
                ))
                .map(existing -> applyPatch(existing, command))
                .flatMap(listingRepository::save);
    }

    private Listing applyPatch(Listing existing, PatchListingCommand command) {

        return existing.toBuilder()
                .title(command.title() != null ? command.title() : existing.getTitle())
                .description(command.description() != null ? command.description()
                        : existing.getDescription())
                .price(command.price() != null ? command.price() : existing.getPrice())
                .currency(command.currency() != null
                        ? new Currency(command.currency())
                        : existing.getCurrency())
                .category(command.category() != null ? command.category() : existing.getCategory())
                .tags(command.tags() != null ? command.tags() : existing.getTags())
                .previewImages(command.previewImageUrls() != null
                        ? mapPreviewImages(command.previewImageUrls())
                        : existing.getPreviewImages())
                .status(command.status() != null ? command.status() : existing.getStatus())
                .updatedAt(OffsetDateTime.now())
                .build();
    }


    private List<PreviewImage> mapPreviewImages(List<String> urls) {

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
