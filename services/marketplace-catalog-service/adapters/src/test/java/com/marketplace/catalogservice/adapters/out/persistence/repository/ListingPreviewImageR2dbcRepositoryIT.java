package com.marketplace.catalogservice.adapters.out.persistence.repository;

import com.marketplace.catalogservice.adapters.config.AbstractPostgresIT;
import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingEntity;
import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingPreviewImageEntity;
import com.marketplace.catalogservice.adapters.testdata.ListingEntityTestDataFactory;
import com.marketplace.catalogservice.adapters.testdata.ListingPreviewImageEntityTestDataFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;;

@DataR2dbcTest
class ListingPreviewImageR2dbcRepositoryIT extends AbstractPostgresIT {

    @Autowired
    ListingPreviewImageR2dbcRepository repository;

    @Autowired
    ListingR2dbcRepository listingRepository;

    @Test
    void saveAll_and_findByListingIdOrderByPosition() {

        ListingEntity listing =
                listingRepository.save(ListingEntityTestDataFactory.newListingEntity())
                        .block();

        UUID listingId = listing.getId();

        List<ListingPreviewImageEntity> images =
                ListingPreviewImageEntityTestDataFactory.newPreviewImages(listingId);

        repository.saveAll(images).then().block();

        List<ListingPreviewImageEntity> result =
                repository.findByListingIdOrderByPosition(listingId)
                        .collectList()
                        .block();

        assertThat(result).hasSize(2);
    }
}


