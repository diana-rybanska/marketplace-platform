package com.marketplace.catalogservice.adapters.out.persistence.repository;

import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingEntity;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ListingR2dbcRepository extends ReactiveCrudRepository<ListingEntity, UUID> {

    @Query("""
        SELECT *
        FROM listing
        WHERE status = :status
          AND (:category IS NULL OR category = :category)
          AND (:tags IS NULL OR tags && :tags)
    """)
    Flux<ListingEntity> findListings(ListingStatus status, String category, String[] tags);

    @Query("""
        SELECT *
        FROM listing
        WHERE status = :status
          AND (:category IS NULL OR category = :category)
          AND (:q IS NULL OR title ILIKE CONCAT('%', :q, '%'))
        ORDER BY updated_at DESC
    """)
    Flux<ListingEntity> searchListings(ListingStatus status, String q, String category);
}
