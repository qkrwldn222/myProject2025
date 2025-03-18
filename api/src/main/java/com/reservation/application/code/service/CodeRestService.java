package com.reservation.application.code.service;

import com.reservation.application.code.mapper.CodeAppResponseMapper;
import com.reservation.application.code.model.*;
import com.reservation.application.code.repository.CodeRepository;
import com.reservation.domain.Code;
import com.reservation.infrastructure.code.Repository.CodeJpaRepositoryAdapter;
import com.reservation.infrastructure.code.model.GroupCodeDTO;
import java.lang.reflect.Proxy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CodeRestService implements CodeService {
  private static final Logger logger = LoggerFactory.getLogger(CodeRestService.class);

  private final CodeJpaRepositoryAdapter codeJpaRepository;
  private final CodeRepository codeRepository;

  public List<Code> findAllCodes() {
    CodeService proxyInstance =
        (CodeService)
            Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {CodeService.class},
                (proxy, method, args) -> method.invoke(this, args));
    return proxyInstance.findAll();
  }

  @Cacheable(value = "codes")
  @Override
  public List<Code> findAll() {
    return codeJpaRepository.findAll();
  }

  // 캐시 초기화 (무효화)
  @CacheEvict(value = "codes", allEntries = true)
  public void clearMenuCache() {
    logger.info("공통 코드 정보 캐시를 초기화했습니다.");
  }

  @Override
  public List<Code> searchCodes(String groupCode) {
    return findAllCodes().stream().filter(code -> code.getGroupCode().equals(groupCode)).toList();
  }

  @Override
  @Transactional
  public void operateCodes(CodeOperateCommand command) {
    command.validate();
  }

  @Override
  @Transactional
  public void saveGroupCode(GroupCodeSaveCommand command) {
    command.validate();
    codeRepository.saveGroupCode(command);
  }

  @Override
  @Transactional
  public void updateGroupCode(GroupCodeUpdateCommand command) {
    command.validate();
    codeRepository.updateGroupCode(command);
  }

  @Override
  public List<GroupCodeSearchResponse> searchGroupCodes(GroupCodeSearchCommand command) {
    List<GroupCodeDTO> groupCodeDTOS = codeRepository.searchGroupCodes(command);
    List<GroupCodeSearchResponse> result =
        CodeAppResponseMapper.INSTANCE.toGroupCodeSearchResponses(groupCodeDTOS);
    result.stream()
        .forEach(
            groupCodeDTO -> {
              System.out.println(groupCodeDTO.getId());
            });
    return result;
  }
}
