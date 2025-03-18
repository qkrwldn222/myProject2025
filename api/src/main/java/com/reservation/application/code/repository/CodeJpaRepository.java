package com.reservation.application.code.repository;

import com.reservation.domain.Code;
import java.util.List;
import java.util.Optional;

public interface CodeJpaRepository {
  List<Code> findAll();

  Optional<Code> findById(Long id);

  Code save(Code code);

  void deleteById(Long id);
}
