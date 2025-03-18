package com.reservation.infrastructure.code.Repository;

import com.reservation.application.code.model.GroupCodeSaveCommand;
import com.reservation.application.code.model.GroupCodeSearchCommand;
import com.reservation.application.code.model.GroupCodeUpdateCommand;
import com.reservation.application.code.repository.CodeRepository;
import com.reservation.infrastructure.code.mapper.InfrastructureCodeRequestMapper;
import com.reservation.infrastructure.code.model.GroupCodeDTO;
import com.reservation.infrastructure.code.mybatis.CodeMybatisRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CodeRestRepository implements CodeRepository {

  private final CodeMybatisRepository codeMybatisRepository;

  @Override
  public List<GroupCodeDTO> searchGroupCodes(GroupCodeSearchCommand command) {
    GroupCodeDTO groupCodeDTO = InfrastructureCodeRequestMapper.INSTANCE.toGroupCodeDTO(command);
    return codeMybatisRepository.searchGroupCodes(groupCodeDTO);
  }

  @Override
  public void saveGroupCode(GroupCodeSaveCommand command) {
    GroupCodeDTO groupCodeDTO = InfrastructureCodeRequestMapper.INSTANCE.toGroupCodeDTO(command);
    codeMybatisRepository.insertGroupCode(groupCodeDTO);
  }

  @Override
  public void updateGroupCode(GroupCodeUpdateCommand command) {
    GroupCodeDTO groupCodeDTO = InfrastructureCodeRequestMapper.INSTANCE.toGroupCodeDTO(command);
    codeMybatisRepository.updateGroupCode(groupCodeDTO);
  }
}
