package com.reservation.application.code.mapper;

import com.reservation.application.code.model.GroupCodeSearchCommand;
import com.reservation.infrastructure.code.model.GroupCodeDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
public class CodeAppRequestMapperImpl implements CodeAppRequestMapper {

    @Override
    public GroupCodeDTO toGroupCodeDTO(GroupCodeSearchCommand command) {
        if ( command == null ) {
            return null;
        }

        GroupCodeDTO groupCodeDTO = new GroupCodeDTO();

        groupCodeDTO.setGroupCode( command.getGroupCode() );
        groupCodeDTO.setUseYn( command.getUseYn() );

        return groupCodeDTO;
    }
}
