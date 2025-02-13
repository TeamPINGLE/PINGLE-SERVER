package org.pingle.pingleserver.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pingle.pingleserver.ServiceSliceTest;
import org.pingle.pingleserver.domain.Meeting;
import org.pingle.pingleserver.domain.Pin;
import org.pingle.pingleserver.domain.Team;
import org.pingle.pingleserver.domain.User;
import org.pingle.pingleserver.exception.CustomException;
import org.pingle.pingleserver.fixture.MeetingFixture;
import org.pingle.pingleserver.fixture.PinFixture;
import org.pingle.pingleserver.fixture.TeamFixture;
import org.pingle.pingleserver.fixture.UserFixture;
import org.pingle.pingleserver.repository.MeetingRepository;
import org.pingle.pingleserver.repository.PinRepository;
import org.pingle.pingleserver.repository.TeamRepository;
import org.pingle.pingleserver.repository.UserMeetingRepository;
import org.pingle.pingleserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

class UserMeetingServiceTest extends ServiceSliceTest {
    @Autowired
    private UserMeetingService userMeetingService;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PinRepository pinRepository;
    @Autowired
    private UserMeetingRepository userMeetingRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("번개에 참여하면 참여자로 등록된다.")
    void participateMeeting() {
        // given
        Meeting meeting = createMeeting();
        meetingRepository.save(meeting);

        User user = UserFixture.create();
        userRepository.save(user);
        // when
        userMeetingService.participateMeeting(user.getId(), meeting.getId());
        // then
        Assertions.assertThat(userMeetingRepository.existsByUserIdAndMeeting(user.getId(), meeting)).isTrue();
        Assertions.assertThat(userMeetingRepository.findAllByMeeting(meeting)).hasSize(1);
    }

    private Meeting createMeeting() {
        Team team = teamRepository.save(TeamFixture.create());
        Pin pin = pinRepository.save(PinFixture.create(team));
        return meetingRepository.save(MeetingFixture.create(pin, 10, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
    }

    @Test
    @DisplayName("번개에 참여자가 중복 참여하면 예외를 발생한다.")
    void failParticipateMeetingOnDuplicate() {
        // given
        Meeting meeting = createMeeting();
        meetingRepository.save(meeting);

        User user = UserFixture.create();
        userRepository.save(user);

        userMeetingService.participateMeeting(user.getId(), meeting.getId());
        // when
        Assertions.assertThatThrownBy(() -> userMeetingService.participateMeeting(user.getId(), meeting.getId()))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("동일한 참여자가 같은 번개에 동시에 중복 참여하면 예외를 발생한다.")
    void failParticipateMeetingOnDuplicateConcurrently() throws InterruptedException {
        // given
        Meeting meeting = createMeeting();
        meetingRepository.save(meeting);

        User user = UserFixture.create();
        userRepository.save(user);

        int THREAD_COUNT = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(THREAD_COUNT);
        // when
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            futures.add(executorService.submit(() -> {
                try {
                    userMeetingService.participateMeeting(user.getId(), meeting.getId());
                    return null;
                } finally {
                    countDownLatch.countDown();
                }
            }));
        }
        countDownLatch.await();
        // then
        Assertions.assertThatThrownBy(() -> {
                    for (Future<?> future : futures) {
                        future.get();
                    }
                }).isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(CustomException.class);
    }
}
