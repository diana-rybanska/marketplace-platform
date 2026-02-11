package com.marketplace.catalogservice.application.port.in;

import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import reactor.core.publisher.Mono;

public interface GetListingByIdUseCase {

    Mono<Listing> getById(ListingId id);
}
