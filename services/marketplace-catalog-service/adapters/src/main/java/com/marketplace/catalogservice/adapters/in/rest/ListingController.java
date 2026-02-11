package com.marketplace.catalogservice.adapters.in.rest;

import com.marketplace.catalog.adapters.in.api.ListingsApi;
import com.marketplace.catalog.adapters.in.api.model.*;
import com.marketplace.catalogservice.adapters.in.rest.mapper.ListingApiMapper;
import com.marketplace.catalogservice.application.port.in.*;
import com.marketplace.catalogservice.application.query.GetListingsQuery;
import com.marketplace.catalogservice.application.query.SearchListingsQuery;
import com.marketplace.catalogservice.domain.listing.ListingId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ListingController implements ListingsApi {

    private final ListingApiMapper apiMapper;
    private final CreateListingUseCase createListingUseCase;
    private final GetListingByIdUseCase getListingByIdUseCase;
    private final GetListingsUseCase getListingsUseCase;
    private final PatchListingUseCase patchListingUseCase;
    private final SearchListingsUseCase searchListingsUseCase;

    @Override
    public Mono<ResponseEntity<ListingDetailResponse>> createListing(
            Mono<CreateListingRequest> createListingRequest, ServerWebExchange exchange) {
        return createListingRequest
                .map(apiMapper::toCreateListingCommand)
                .flatMap(createListingUseCase::createListing)
                .map(apiMapper::toListingDetailResponse)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<ListingDetailResponse>> getListingById(UUID id,
            ServerWebExchange exchange) {
        return getListingByIdUseCase
                .getById(new ListingId(id))
                .map(apiMapper::toListingDetailResponse)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<ListingCardResponse>>> getListings(String category,
            String tags, ServerWebExchange exchange) {
        List<String> tagList = tags == null
                ? List.of()
                : Arrays.stream(tags.split(",")).toList();

        GetListingsQuery query = GetListingsQuery.builder()
                .category(category)
                .tags(tagList)
                .build();

        Flux<ListingCardResponse> response =
                getListingsUseCase.getListings(query)
                        .map(apiMapper::toListingCardResponse);

        return Mono.just(ResponseEntity.ok(response));
    }

    @Override
    public Mono<ResponseEntity<ListingDetailResponse>> patchListing(UUID id,
            Mono<PatchListingRequest> patchListingRequest, ServerWebExchange exchange) {
        return patchListingRequest
                .map(req -> apiMapper.toPatchListingCommand(id, req))
                .flatMap(patchListingUseCase::patchListing)
                .map(apiMapper::toListingDetailResponse)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<ListingCardResponse>>> searchListings(String query,
            String category, ServerWebExchange exchange) {
        SearchListingsQuery searchQuery = SearchListingsQuery.builder()
                .query(query)
                .category(category)
                .build();

        Flux<ListingCardResponse> response =
                searchListingsUseCase.search(searchQuery)
                        .map(apiMapper::toListingCardResponse);

        return Mono.just(ResponseEntity.ok(response));
    }
}
