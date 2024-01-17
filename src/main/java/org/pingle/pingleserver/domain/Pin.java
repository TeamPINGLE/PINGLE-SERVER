package org.pingle.pingleserver.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    @OneToMany(mappedBy = "pin")
    private List<Meeting> meetings;

    @Embedded
    private Point point;

    @Embedded
    private Address address;

    private String name;

    @Builder
    public Pin(Team team, Point point, Address address, String name) {
        this.team = team;
        this.point = point;
        this.address = address;
        this.name = name;
    }
}
