package com.marketplace.catalogservice.adapters.out.persistence.repository;

import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingPreviewImageEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ListingPreviewImageR2dbcRepository extends
        ReactiveCrudRepository<ListingPreviewImageEntity, UUID> {

    Flux<ListingPreviewImageEntity> findByListingIdOrderByPosition(UUID listingId);
}
