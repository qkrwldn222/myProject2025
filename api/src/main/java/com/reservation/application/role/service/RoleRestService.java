package com.reservation.application.role.service;

import com.reservation.application.role.model.RoleSearchCommand;
import com.reservation.domain.Role;
import com.reservation.infrastructure.role.repository.RoleJpaRepository;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class RoleRestService implements RoleService {

  private final RoleJpaRepository roleJpaRepository;
  private final RoleService proxyInstance;

  public RoleRestService(RoleJpaRepository roleJpaRepository) {
    this.roleJpaRepository = roleJpaRepository;
    this.proxyInstance = createProxy();
  }

  // 현재 서비스 클래스를 Proxy로 감싸서 자기 자신을 프록시 객체로 생성
  private RoleService createProxy() {
    return (RoleService)
        Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[] {RoleService.class}, // 인터페이스 기반 프록시
            new InvocationHandler() {
              private final RoleService target = RoleRestService.this;

              @Override
              public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(target, args); // 실제 객체의 메서드 실행
              }
            });
  }

  public List<Role> getAllRoles() {
    return proxyInstance.findAll();
  }

  @Override
  public List<Role> searchAllByCondition(RoleSearchCommand command) {
    List<Role> roles = getAllRoles();

    if (StringUtils.hasText(command.getName()))
      roles.removeIf(role -> !role.getName().contains(command.getName()));

    if (StringUtils.hasText(command.getCode()))
      roles.removeIf(role -> !role.getRoleType().getCode().contains(command.getCode()));

    if (!ObjectUtils.isEmpty(command.getName()))
      roles.removeIf(role -> role.getRank() != command.getRank());

    return roles;
  }

  @Override
  @Cacheable(value = "roles", sync = true)
  public List<Role> findAll() {
    return roleJpaRepository.findAll();
  }

  @Override
  public Role findOneByCode(String code) {
    return getAllRoles().stream()
        .filter(role -> role.getRoleType().getCode().equals(code))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Role findOneByName(String name) {
    return roleJpaRepository.findOneByName(name).orElse(null);
  }
}
