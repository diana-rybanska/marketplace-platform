package com.marketplace.catalogservice.application.port.in;

import com.marketplace.catalogservice.application.query.SearchListingsQuery;
import com.marketplace.catalogservice.domain.listing.Listing;
import reactor.core.publisher.Flux;

public interface SearchListingsUseCase {
    Flux<Listing> search(SearchListingsQuery query);
}
