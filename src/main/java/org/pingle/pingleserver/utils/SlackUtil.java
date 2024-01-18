package org.pingle.pingleserver.utils;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackUtil {

    @Value("${slack.webhook.team-created}")
    private static String teamCreatedWebhookUrl;
    @Value("${slack.webhook.user-join}")
    private static String userJoinWebhookUrl;
    @Value("${slack.webhook.meeting-created}")
    private static String meetingCreatedWebhookUrl;
    @Value("${slack.webhook.server-error}")
    private static String serverErrorWebhookUrl;

    public static WebhookResponse alertUserSignUp (String name, String email) {
        try {
            String text = name + "(" + email + ")" +"님이 가입했습니다.";
            WebhookResponse response;
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            response = slack.send(userJoinWebhookUrl, payload);
            return response;
        } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
    }
    public static WebhookResponse alertCreateTeam(String teamName) {
        try {
            String text = "팀 : " + teamName + "이 생성되었습니다.";
            WebhookResponse response;
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            response = slack.send(teamCreatedWebhookUrl, payload);
            return response;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    public static WebhookResponse alertCreateMeeting(String location, String meetingName) {
        try {
            String text = location + "에서" + meetingName + "번개가 생성되었습니다.";
            WebhookResponse response;
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            response = slack.send(meetingCreatedWebhookUrl, payload);
            return response;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    public static WebhookResponse alertError(Exception exception) {
        try {
            String text = exception.getClass().getName() + "  "
                    + exception.getMessage() + "\n"
                    + Arrays.toString(exception.getStackTrace());
            WebhookResponse response;
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            response = slack.send(serverErrorWebhookUrl, payload);
            return response;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
