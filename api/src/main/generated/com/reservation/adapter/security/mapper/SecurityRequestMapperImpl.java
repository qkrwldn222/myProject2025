package com.reservation.adapter.security.mapper;

import com.reservation.adapter.security.config.OAuth2UserInfo;
import com.reservation.adapter.security.model.SignupRequest;
import com.reservation.application.user.model.SignupCommand;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
public class SecurityRequestMapperImpl implements SecurityRequestMapper {

    @Override
    public SignupCommand toCommand(SignupRequest request) {
        if ( request == null ) {
            return null;
        }

        SignupCommand signupCommand = new SignupCommand();

        signupCommand.setUserID( request.getUserID() );
        signupCommand.setUserName( request.getUserName() );
        signupCommand.setPassword( request.getPassword() );
        signupCommand.setRoleCode( request.getRoleCode() );

        return signupCommand;
    }

    @Override
    public SignupCommand toCommand(OAuth2UserInfo userInfo) {
        if ( userInfo == null ) {
            return null;
        }

        SignupCommand signupCommand = new SignupCommand();

        return signupCommand;
    }
}
