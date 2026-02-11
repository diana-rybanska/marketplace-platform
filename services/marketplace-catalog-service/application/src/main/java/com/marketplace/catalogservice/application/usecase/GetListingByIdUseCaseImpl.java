package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.port.in.GetListingByIdUseCase;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.domain.exceptions.ListingNotFoundException;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetListingByIdUseCaseImpl implements GetListingByIdUseCase {

    private final ListingRepository listingRepository;

    @Override
    public Mono<Listing> getById(ListingId id) {

        return listingRepository.findById(id)
                .switchIfEmpty(Mono.error(new ListingNotFoundException(id.getValue())));
    }
}
