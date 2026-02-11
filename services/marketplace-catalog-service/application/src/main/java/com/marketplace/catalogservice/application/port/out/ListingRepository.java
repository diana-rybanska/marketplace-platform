package com.marketplace.catalogservice.application.port.out;

import com.marketplace.catalogservice.application.command.PatchListingCommand;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ListingRepository {

    Mono<Listing> save(Listing listing);

    Mono<Listing> findById(ListingId id);

    Flux<Listing> findListings(ListingStatus status, String category, List<String> tags);

    Flux<Listing> searchListings(ListingStatus status, String query, String category);
}
