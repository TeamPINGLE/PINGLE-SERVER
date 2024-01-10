package org.pingle.pingleserver.repository;

import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.domain.enums.Provider;
import org.pingle.pingleserver.dto.type.ErrorMessage;
import org.pingle.pingleserver.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    default User findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ErrorMessage.USER_NOT_FOUND));
    }

    default User findByIdAndIsDeletedOrThrow(Long id, Boolean deleted) {
        return findByIdAndIsDeleted(id, false).orElseThrow(
                () -> new CustomException(ErrorMessage.USER_NOT_FOUND));
    }
    Optional<User> findByIdAndIsDeleted(Long id, Boolean deleted);
    boolean existsByProviderAndSerialIdAndIsDeleted(Provider provider, String serialId, Boolean deleted);
    Optional<User> findByProviderAndSerialIdAndIsDeleted(Provider provider, String serialId, Boolean deleted);
    Optional<User> findByRefreshTokenAndIsDeleted(String refreshToken, Boolean deleted);
}
