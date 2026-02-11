package com.marketplace.catalogservice.application.port.in;

import com.marketplace.catalogservice.application.command.CreateListingCommand;
import com.marketplace.catalogservice.domain.listing.Listing;
import reactor.core.publisher.Mono;

public interface CreateListingUseCase {

    Mono<Listing> createListing(CreateListingCommand command);
}
