package com.marketplace.catalogservice.adapters.out.persistence.repository;

import com.marketplace.catalogservice.adapters.config.AbstractPostgresIT;
import com.marketplace.catalogservice.adapters.out.persistence.entity.ListingEntity;
import com.marketplace.catalogservice.adapters.testdata.ListingEntityTestDataFactory;
import com.marketplace.catalogservice.domain.enums.ListingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
class ListingR2dbcRepositoryIT extends AbstractPostgresIT {

    @Autowired
    ListingR2dbcRepository repository;

    @Test
    void findListings_shouldFilterByStatusCategoryAndTags() {

        ListingEntity listing1 = ListingEntityTestDataFactory.newListingEntity();
        listing1.setCategory("fashion");
        listing1.setTags(List.of("dress"));
        listing1.setStatus(ListingStatus.ACTIVE);

        ListingEntity listing2 = ListingEntityTestDataFactory.newListingEntity();
        listing2.setCategory("electronics");
        listing2.setTags(List.of("phone"));
        listing2.setStatus(ListingStatus.ACTIVE);

        repository.saveAll(List.of(listing1, listing2))
                .then()
                .block();

        var result =
                repository.findListings(
                                ListingStatus.ACTIVE,
                                "fashion",
                                new String[]{"dress"})
                        .collectList()
                        .block();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("fashion");
    }

    @Test
    void searchListings_shouldSearchByTitle() {

        ListingEntity listing1 = ListingEntityTestDataFactory.newListingEntity();
        listing1.setTitle("Red summer dress");
        listing1.setStatus(ListingStatus.ACTIVE);

        ListingEntity listing2 = ListingEntityTestDataFactory.newListingEntity();
        listing2.setTitle("Laptop");
        listing2.setStatus(ListingStatus.ACTIVE);

        repository.saveAll(List.of(listing1, listing2))
                .then()
                .block();

        var result =
                repository.searchListings(
                                ListingStatus.ACTIVE,
                                "dress",
                                null)
                        .collectList()
                        .block();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("dress");
    }
}

