package com.cnaps.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnaps.model.BulkValidation;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.IGoogleAuthenticator;

@Service
public class BulkServiceOTP {
    @Autowired
    private final IGoogleAuthenticator googleAuthenticator;

    public BulkServiceOTP() {
        this.googleAuthenticator = new GoogleAuthenticator();
    }

    public boolean isCodeValid(BulkValidation bulkValidation) {
        return googleAuthenticator.authorize(bulkValidation.getCle(), bulkValidation.getCode());
    }
}
