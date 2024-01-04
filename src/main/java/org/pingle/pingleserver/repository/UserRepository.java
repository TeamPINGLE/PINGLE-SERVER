package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
