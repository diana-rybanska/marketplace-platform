package com.marketplace.catalogservice.adapters.in.rest;

import com.marketplace.catalog.adapters.in.api.model.*;
import com.marketplace.catalogservice.adapters.in.rest.mapper.ListingApiMapper;
import com.marketplace.catalogservice.adapters.testdata.ListingRequestTestDataFactory;
import com.marketplace.catalogservice.adapters.testdata.ListingTestDataFactory;
import com.marketplace.catalogservice.application.command.CreateListingCommand;
import com.marketplace.catalogservice.application.command.PatchListingCommand;
import com.marketplace.catalogservice.application.port.in.*;
import com.marketplace.catalogservice.domain.exceptions.ListingNotFoundException;
import com.marketplace.catalogservice.domain.listing.Listing;
import com.marketplace.common.errors.GlobalExceptionHandler;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebFluxTest(ListingController.class)
@Import(GlobalExceptionHandler.class)
public class ListingControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ListingApiMapper apiMapper;
    @MockitoBean
    private CreateListingUseCase createListingUseCase;
    @MockitoBean
    private GetListingByIdUseCase getListingByIdUseCase;
    @MockitoBean
    private GetListingsUseCase getListingsUseCase;
    @MockitoBean
    private PatchListingUseCase patchListingUseCase;
    @MockitoBean
    private SearchListingsUseCase searchListingsUseCase;

    @Nested
    class CreateListing {

        @Test
        void createListing() {

            CreateListingRequest request = ListingRequestTestDataFactory.aCreateListingRequest();
            CreateListingCommand command = mock(CreateListingCommand.class);
            Listing listing = ListingTestDataFactory.aListing();
            ListingDetailResponse response = new ListingDetailResponse();

            when(apiMapper.toCreateListingCommand(any())).thenReturn(command);
            when(createListingUseCase.createListing(command)).thenReturn(Mono.just(listing));
            when(apiMapper.toListingDetailResponse(listing)).thenReturn(response);

            webTestClient.post()
                    .uri("/listings")
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody();
        }

        @Test
        void createListing_validationError() {

            CreateListingRequest request = new CreateListingRequest(); // empty

            webTestClient.post()
                    .uri("/listings")
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isBadRequest();
        }
    }

    @Nested
    class GetListingByIdTests {

        @Test
        void getListingById() {

            UUID id = UUID.randomUUID();

            Listing listing = ListingTestDataFactory.aListing();
            ListingDetailResponseListing listingDto = new ListingDetailResponseListing();
            listingDto.setId(id);

            ListingDetailResponse response = new ListingDetailResponse();
            response.setListing(listingDto);

            when(getListingByIdUseCase.getById(any()))
                    .thenReturn(Mono.just(listing));

            when(apiMapper.toListingDetailResponse(any()))
                    .thenReturn(response);


            webTestClient.get()
                    .uri("/listings/{id}", id)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(ListingDetailResponse.class)
                    .value(res ->
                            assertThat(res.getListing().getId()).isEqualTo(id)
                    );
        }

        @Test
        void getListingById_notFound() {

            UUID id = UUID.randomUUID();

            when(getListingByIdUseCase.getById(any()))
                    .thenReturn(Mono.error(new ListingNotFoundException(id)));

            webTestClient.get()
                    .uri("/listings/{id}", id)
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    class PatchListingTests {

        @Test
        void patchListing() {

            UUID id = UUID.randomUUID();
            PatchListingRequest request = ListingRequestTestDataFactory.aPatchListingRequest();

            PatchListingCommand command = mock(PatchListingCommand.class);
            Listing listing = ListingTestDataFactory.aListing();
            ListingDetailResponse response = new ListingDetailResponse();

            when(apiMapper.toPatchListingCommand(eq(id), any())).thenReturn(command);
            when(patchListingUseCase.patchListing(command)).thenReturn(Mono.just(listing));
            when(apiMapper.toListingDetailResponse(listing)).thenReturn(response);

            webTestClient.patch()
                    .uri("/listings/{id}", id)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        void patchListing_notFound() {

            UUID id = UUID.randomUUID();
            PatchListingRequest request = ListingRequestTestDataFactory.aPatchListingRequest();

            PatchListingCommand command = mock(PatchListingCommand.class);

            when(apiMapper.toPatchListingCommand(eq(id), any()))
                    .thenReturn(command);

            when(patchListingUseCase.patchListing(command))
                    .thenReturn(Mono.error(new ListingNotFoundException(id)));

            webTestClient.patch()
                    .uri("/listings/{id}", id)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    class GetListingsTests {

        @Test
        void getListings() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingCardResponse card = new ListingCardResponse();

            when(getListingsUseCase.getListings(any()))
                    .thenReturn(Flux.just(listing));

            when(apiMapper.toListingCardResponse(listing))
                    .thenReturn(card);

            webTestClient.get()
                    .uri("/listings")
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Nested
    class SearchListingsTests {

        @Test
        void searchListings() {

            Listing listing = ListingTestDataFactory.aListing();
            ListingCardResponse card = new ListingCardResponse();

            when(searchListingsUseCase.search(any()))
                    .thenReturn(Flux.just(listing));

            when(apiMapper.toListingCardResponse(listing))
                    .thenReturn(card);

            webTestClient.get()
                    .uri("/listings/search?query=test")
                    .exchange()
                    .expectStatus().isOk();
        }
    }

}
