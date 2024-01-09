package org.pingle.pingleserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.pingle.pingleserver.domain.enums.MRole;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMeeting extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Enumerated(EnumType.STRING)
    private MRole meetingRole;

    @Builder
    public UserMeeting(User user, Meeting meeting, MRole meetingRole) {
        this.user = user;
        this.meeting = meeting;
        this.meetingRole = meetingRole;
    }
}
