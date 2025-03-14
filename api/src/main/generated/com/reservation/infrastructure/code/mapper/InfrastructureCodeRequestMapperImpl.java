package com.reservation.infrastructure.code.mapper;

import com.reservation.application.code.model.GroupCodeSaveCommand;
import com.reservation.application.code.model.GroupCodeSearchCommand;
import com.reservation.application.code.model.GroupCodeUpdateCommand;
import com.reservation.infrastructure.code.model.GroupCodeDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
public class InfrastructureCodeRequestMapperImpl implements InfrastructureCodeRequestMapper {

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

    @Override
    public GroupCodeDTO toGroupCodeDTO(GroupCodeSaveCommand command) {
        if ( command == null ) {
            return null;
        }

        GroupCodeDTO groupCodeDTO = new GroupCodeDTO();

        groupCodeDTO.setGroupCode( command.getGroupCode() );
        groupCodeDTO.setUseYn( command.getUseYn() );
        groupCodeDTO.setDescription( command.getDescription() );

        groupCodeDTO.setCreateBy( getCurrentUsername() );
        groupCodeDTO.setUpdateBy( getCurrentUsername() );

        return groupCodeDTO;
    }

    @Override
    public GroupCodeDTO toGroupCodeDTO(GroupCodeUpdateCommand command) {
        if ( command == null ) {
            return null;
        }

        GroupCodeDTO groupCodeDTO = new GroupCodeDTO();

        groupCodeDTO.setId( command.getId() );
        groupCodeDTO.setUseYn( command.getUseYn() );
        groupCodeDTO.setDescription( command.getDescription() );

        groupCodeDTO.setUpdateBy( getCurrentUsername() );
        groupCodeDTO.setCreateBy( getCurrentUsername() );

        return groupCodeDTO;
    }
}
