package org.pingle.pingleserver.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.pingle.pingleserver.domain.enums.MCategory;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<UserMeeting> userMeetingList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private MCategory category;

    private String name;

    private Integer maxParticipants;

    private String chatLink;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Builder
    public Meeting(Pin pin, MCategory category, String name, Integer maxParticipants, String chatLink, LocalDateTime startAt, LocalDateTime endAt) {
        this.pin = pin;
        this.category = category;
        this.name = name;
        this.maxParticipants = maxParticipants;
        this.chatLink = chatLink;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
