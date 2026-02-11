package com.marketplace.catalogservice.adapters.out.persistence.adapter;

import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingEntity;
import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingPreviewImageEntity;
import com.marketplace.catalogservice.adapters.out.persistence.mapper.ListingPersistenceMapper;
import com.marketplace.catalogservice.adapters.out.persistence.repository.ListingPreviewImageR2dbcRepository;
import com.marketplace.catalogservice.adapters.out.persistence.repository.ListingR2dbcRepository;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.catalogservice.domain.listing.ListingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListingRepositoryAdapter implements ListingRepository {

    private final ListingR2dbcRepository listingRepository;
    private final ListingPreviewImageR2dbcRepository previewImageRepository;
    private final ListingPersistenceMapper mapper;

    @Override
    public Mono<Listing> save(Listing listing) {

        ListingEntity entity = mapper.toListingEntity(listing);

        return listingRepository
                .save(entity)
                .flatMap(saved -> {

                    List<ListingPreviewImageEntity> images =
                            mapper.toListingPreviewImageEntities(
                                    listing.getPreviewImages(),
                                    listing.getId().getValue()
                            );

                    return (images == null || images.isEmpty())
                            ? Mono.just(saved)
                            : previewImageRepository.saveAll(images).then(Mono.just(saved));
                })
                .map(saved -> {
                    Listing domain = mapper.toListing(saved);
                    if (domain == null) {
                        throw new IllegalStateException("Mapper returned null Listing");
                    }
                    return domain;
                });
    }

    @Override
    public Mono<Listing> findById(ListingId id) {
        return listingRepository
                .findById(id.getValue())
                .flatMap(entity ->
                        previewImageRepository
                                .findByListingIdOrderByPosition(entity.getId())
                                .collectList()
                                .map(images -> mapper.toListing(entity, images))
                );
    }

    @Override
    public Flux<Listing> findListings(ListingStatus status, String category, List<String> tags) {
        String[] tagArray = tags == null || tags.isEmpty()
                ? null
                : tags.toArray(new String[0]);

        return listingRepository
                .findListings(status, category, tagArray)
                .flatMap(entity ->
                        previewImageRepository
                                .findByListingIdOrderByPosition(entity.getId())
                                .collectList()
                                .map(images -> mapper.toListing(entity, images))
                );
    }

    @Override
    public Flux<Listing> searchListings(ListingStatus status, String query, String category) {
        String q = (query == null || query.isBlank()) ? null : query.trim();

        return listingRepository
                .searchListings(status, q, category)
                .flatMap(entity ->
                        previewImageRepository
                                .findByListingIdOrderByPosition(entity.getId())
                                .collectList()
                                .map(images -> mapper.toListing(entity, images))
                );
    }
}
