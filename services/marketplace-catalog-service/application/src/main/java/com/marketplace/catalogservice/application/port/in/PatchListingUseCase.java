package com.marketplace.catalogservice.application.port.in;

import com.marketplace.catalogservice.application.command.PatchListingCommand;
import com.marketplace.catalogservice.domain.listing.Listing;
import reactor.core.publisher.Mono;

public interface PatchListingUseCase {

    Mono<Listing> patchListing(PatchListingCommand command);
}
