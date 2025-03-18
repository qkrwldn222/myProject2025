package com.reservation.infrastructure.code.Repository;

import com.reservation.domain.Code;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSpringDataJpaRepository extends JpaRepository<Code, Long> {

  List<Code> findAll();
}
