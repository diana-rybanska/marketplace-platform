package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.port.in.GetListingsUseCase;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.application.query.GetListingsQuery;
import com.marketplace.catalogservice.domain.listing.Listing;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import static com.marketplace.catalogservice.domain.enums.ListingStatus.ACTIVE;

@RequiredArgsConstructor
public class GetListingsUseCaseImpl implements GetListingsUseCase {

    private final ListingRepository listingRepository;

    @Override
    public Flux<Listing> getListings(GetListingsQuery query) {
        return listingRepository.findListings(
                ACTIVE,
                query.category(),
                query.tags()
        );
    }
}
