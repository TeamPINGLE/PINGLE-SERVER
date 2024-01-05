package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Long> {
}
