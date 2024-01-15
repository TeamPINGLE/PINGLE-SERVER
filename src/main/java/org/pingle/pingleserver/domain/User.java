package org.pingle.pingleserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pingle.pingleserver.domain.enums.Provider;
import org.pingle.pingleserver.domain.enums.URole;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity  {
    private static final Long MEMBER_INFO_RETENTION_PERIOD = 180L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serialId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private URole role;

    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String refreshToken;

    private String email;

    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user")
    private List<UserTeam> userTeams;

    @Builder
    public User(String serialId, String name, String email, Provider provider, URole role, String refreshToken) {
        this.serialId = serialId;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now().plusDays(MEMBER_INFO_RETENTION_PERIOD);
    }

    public void recover() {
        this.isDeleted = false;
        this.deletedAt = null;
    }
}
