package com.marketplace.catalogservice.application.usecase;

import com.marketplace.catalogservice.application.port.in.SearchListingsUseCase;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.application.query.SearchListingsQuery;
import com.marketplace.catalogservice.domain.listing.Listing;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import static com.marketplace.catalogservice.domain.enums.ListingStatus.ACTIVE;

@RequiredArgsConstructor
public class SearchListingsUseCaseImpl implements SearchListingsUseCase {

    private final ListingRepository listingRepository;

    @Override
    public Flux<Listing> search(SearchListingsQuery query) {
        return listingRepository.searchListings(
                ACTIVE,
                query.query(),
                query.category()
        );
    }
}
