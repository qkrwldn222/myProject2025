package com.reservation.adapter.code.mapper;

import com.reservation.adapter.code.model.CodeDeleteRequest;
import com.reservation.adapter.code.model.CodeOperateRequest;
import com.reservation.adapter.code.model.CodeSaveRequest;
import com.reservation.adapter.code.model.CodeUpdateRequest;
import com.reservation.application.code.model.CodeDeleteCommand;
import com.reservation.application.code.model.CodeOperateCommand;
import com.reservation.application.code.model.CodeSaveCommand;
import com.reservation.application.code.model.CodeUpdateCommand;
import com.reservation.application.code.model.GroupCodeSearchCommand;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
public class CodeRequestMapperImpl implements CodeRequestMapper {

    @Override
    public CodeOperateCommand toOperateCommand(CodeOperateRequest request) {
        if ( request == null ) {
            return null;
        }

        CodeOperateCommand codeOperateCommand = new CodeOperateCommand();

        codeOperateCommand.setSaveCommands( codeSaveRequestListToCodeSaveCommandList( request.saveRequests() ) );
        codeOperateCommand.setUpdateCommands( codeUpdateRequestListToCodeUpdateCommandList( request.updateRequests() ) );
        codeOperateCommand.setDeleteCommands( codeDeleteRequestListToCodeDeleteCommandList( request.deleteRequests() ) );

        return codeOperateCommand;
    }

    @Override
    public GroupCodeSearchCommand tosSearchGroupCodeCommand(String groupCode, String useYn) {
        if ( groupCode == null && useYn == null ) {
            return null;
        }

        GroupCodeSearchCommand groupCodeSearchCommand = new GroupCodeSearchCommand();

        groupCodeSearchCommand.setGroupCode( groupCode );
        groupCodeSearchCommand.setUseYn( useYn );

        return groupCodeSearchCommand;
    }

    protected CodeSaveCommand codeSaveRequestToCodeSaveCommand(CodeSaveRequest codeSaveRequest) {
        if ( codeSaveRequest == null ) {
            return null;
        }

        CodeSaveCommand codeSaveCommand = new CodeSaveCommand();

        codeSaveCommand.setCode( codeSaveRequest.code() );
        codeSaveCommand.setName( codeSaveRequest.name() );
        codeSaveCommand.setGroupCode( codeSaveRequest.groupCode() );
        codeSaveCommand.setAttr01( codeSaveRequest.attr01() );
        codeSaveCommand.setAttr02( codeSaveRequest.attr02() );
        codeSaveCommand.setAttr03( codeSaveRequest.attr03() );
        codeSaveCommand.setAttr04( codeSaveRequest.attr04() );
        codeSaveCommand.setAttr05( codeSaveRequest.attr05() );
        codeSaveCommand.setAttr06( codeSaveRequest.attr06() );
        codeSaveCommand.setAttr07( codeSaveRequest.attr07() );
        codeSaveCommand.setAttr08( codeSaveRequest.attr08() );
        codeSaveCommand.setAttr09( codeSaveRequest.attr09() );

        return codeSaveCommand;
    }

    protected List<CodeSaveCommand> codeSaveRequestListToCodeSaveCommandList(List<CodeSaveRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<CodeSaveCommand> list1 = new ArrayList<CodeSaveCommand>( list.size() );
        for ( CodeSaveRequest codeSaveRequest : list ) {
            list1.add( codeSaveRequestToCodeSaveCommand( codeSaveRequest ) );
        }

        return list1;
    }

    protected CodeUpdateCommand codeUpdateRequestToCodeUpdateCommand(CodeUpdateRequest codeUpdateRequest) {
        if ( codeUpdateRequest == null ) {
            return null;
        }

        CodeUpdateCommand codeUpdateCommand = new CodeUpdateCommand();

        codeUpdateCommand.setName( codeUpdateRequest.name() );
        codeUpdateCommand.setAttr01( codeUpdateRequest.attr01() );
        codeUpdateCommand.setAttr02( codeUpdateRequest.attr02() );
        codeUpdateCommand.setAttr03( codeUpdateRequest.attr03() );
        codeUpdateCommand.setAttr04( codeUpdateRequest.attr04() );
        codeUpdateCommand.setAttr05( codeUpdateRequest.attr05() );
        codeUpdateCommand.setAttr06( codeUpdateRequest.attr06() );
        codeUpdateCommand.setAttr07( codeUpdateRequest.attr07() );
        codeUpdateCommand.setAttr08( codeUpdateRequest.attr08() );
        codeUpdateCommand.setAttr09( codeUpdateRequest.attr09() );
        codeUpdateCommand.setId( codeUpdateRequest.id() );

        return codeUpdateCommand;
    }

    protected List<CodeUpdateCommand> codeUpdateRequestListToCodeUpdateCommandList(List<CodeUpdateRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<CodeUpdateCommand> list1 = new ArrayList<CodeUpdateCommand>( list.size() );
        for ( CodeUpdateRequest codeUpdateRequest : list ) {
            list1.add( codeUpdateRequestToCodeUpdateCommand( codeUpdateRequest ) );
        }

        return list1;
    }

    protected CodeDeleteCommand codeDeleteRequestToCodeDeleteCommand(CodeDeleteRequest codeDeleteRequest) {
        if ( codeDeleteRequest == null ) {
            return null;
        }

        CodeDeleteCommand codeDeleteCommand = new CodeDeleteCommand();

        codeDeleteCommand.setId( codeDeleteRequest.id() );

        return codeDeleteCommand;
    }

    protected List<CodeDeleteCommand> codeDeleteRequestListToCodeDeleteCommandList(List<CodeDeleteRequest> list) {
        if ( list == null ) {
            return null;
        }

        List<CodeDeleteCommand> list1 = new ArrayList<CodeDeleteCommand>( list.size() );
        for ( CodeDeleteRequest codeDeleteRequest : list ) {
            list1.add( codeDeleteRequestToCodeDeleteCommand( codeDeleteRequest ) );
        }

        return list1;
    }
}
