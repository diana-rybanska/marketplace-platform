package com.marketplace.catalogservice.adapters.config;

import com.marketplace.catalogservice.application.port.in.*;
import com.marketplace.catalogservice.application.port.out.ListingRepository;
import com.marketplace.catalogservice.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogServiceConfig {

    @Bean
    public CreateListingUseCase createListingUseCase(
            ListingRepository listingRepository
    ) {
        return new CreateListingUseCaseImpl(listingRepository);
    }

    @Bean
    public GetListingByIdUseCase getListingByIdUseCas(
            ListingRepository listingRepository
    ) {
        return new GetListingByIdUseCaseImpl(listingRepository);
    }

    @Bean
    public GetListingsUseCase getListingsUseCase(
            ListingRepository listingRepository
    ) {
        return new GetListingsUseCaseImpl(listingRepository);
    }

    @Bean
    public PatchListingUseCase patchListingUseCase(
            ListingRepository listingRepository
    ) {
        return new PatchListingUseCaseImpl(listingRepository);
    }

    @Bean
    public SearchListingsUseCase searchListingsUseCase(
            ListingRepository listingRepository
    ) {
        return new SearchListingsUseCaseImpl(listingRepository);
    }
}
