package com.reservation.adapter.security.mapper;

import com.reservation.adapter.security.model.SignupRequest;
import com.reservation.application.user.model.SignupCommand;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-22T22:57:55+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Eclipse Adoptium)"
)
public class SecurityRequestMapperImpl implements SecurityRequestMapper {

    @Override
    public SignupCommand toCommand(SignupRequest request) {
        if ( request == null ) {
            return null;
        }

        SignupCommand signupCommand = new SignupCommand();

        return signupCommand;
    }
}
