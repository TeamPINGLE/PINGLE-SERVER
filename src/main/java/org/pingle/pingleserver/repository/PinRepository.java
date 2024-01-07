package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Long> {
    boolean existsByPoint(Point point);
    Pin findByPoint(Point point);
}
