package com.reservation.infrastructure.code;

import com.reservation.domain.Code;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeJpaRepository extends JpaRepository<Code, Long> {

  List<Code> findAll();
}
