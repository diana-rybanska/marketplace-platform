package com.marketplace.catalogservice.application.port.in;

import com.marketplace.catalogservice.application.query.GetListingsQuery;
import com.marketplace.catalogservice.domain.listing.Listing;
import reactor.core.publisher.Flux;

public interface GetListingsUseCase {

    Flux<Listing> getListings(GetListingsQuery query);
}
