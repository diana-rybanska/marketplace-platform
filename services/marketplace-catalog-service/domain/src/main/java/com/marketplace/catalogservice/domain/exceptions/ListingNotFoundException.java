package com.marketplace.catalogservice.domain.exceptions;

import com.marketplace.common.errors.enums.ErrorCode;
import com.marketplace.common.errors.exceptions.MpException;

import java.util.UUID;

public class ListingNotFoundException extends MpException {

    public ListingNotFoundException(UUID id) {
        super(
                ErrorCode.LISTING_NOT_FOUND,
                "Listing with id " + id + " not found");
    }

}