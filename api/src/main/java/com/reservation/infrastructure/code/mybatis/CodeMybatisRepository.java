package com.reservation.infrastructure.code.mybatis;

import com.reservation.infrastructure.code.model.GroupCodeDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodeMybatisRepository {

  List<GroupCodeDTO> searchGroupCodes(GroupCodeDTO groupCodeDTO);

  void insertGroupCode(GroupCodeDTO groupCodeDTO);

  void updateGroupCode(GroupCodeDTO groupCodeDTO);
}
