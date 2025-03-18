package com.reservation.infrastructure.code.Repository;

import com.reservation.application.code.repository.CodeJpaRepository;
import com.reservation.domain.Code;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CodeJpaRepositoryAdapter implements CodeJpaRepository {
  private final CodeSpringDataJpaRepository codeJpaRepository;

  @Override
  public List<Code> findAll() {
    return codeJpaRepository.findAll();
  }

  @Override
  public Optional<Code> findById(Long id) {
    return codeJpaRepository.findById(id);
  }

  @Override
  public Code save(Code code) {
    return codeJpaRepository.save(code);
  }

  @Override
  public void deleteById(Long id) {
    codeJpaRepository.deleteById(id);
  }
}
