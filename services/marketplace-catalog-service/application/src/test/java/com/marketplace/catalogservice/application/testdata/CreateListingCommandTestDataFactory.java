package com.marketplace.catalogservice.application.testdata;

import com.marketplace.catalogservice.application.command.CreateListingCommand;

import java.math.BigDecimal;
import java.util.List;

public final class CreateListingCommandTestDataFactory {

    private CreateListingCommandTestDataFactory() {}

    public static CreateListingCommand aCreateListingCommand() {
        return new CreateListingCommand(
                "Test title",
                "Test description",
                BigDecimal.TEN,
                "EUR",
                "fashion",
                List.of("tag1", "tag2"),
                List.of("img1.jpg", "img2.jpg")
        );
    }
}
