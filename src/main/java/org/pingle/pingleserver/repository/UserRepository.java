package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByProviderAndSerialIdAndIsDeleted(Provider provider, String serialId, Boolean deleted);
    Optional<User> findByProviderAndSerialIdAndIsDeleted(Provider provider, String serialId, Boolean deleted);

    Optional<User> findByIdAndIsDeleted(Long id, Boolean deleted);

}
