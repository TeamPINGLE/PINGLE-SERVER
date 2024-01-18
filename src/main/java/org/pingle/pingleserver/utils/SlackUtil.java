package org.pingle.pingleserver.utils;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class SlackUtil {

    @Value("${slack.webhook.team-created}")
    private String teamCreatedWebhookUrl;
    @Value("${slack.webhook.user-join}")
    private String userJoinWebhookUrl;
    @Value("${slack.webhook.meeting-created}")
    private String meetingCreatedWebhookUrl;
    @Value("${slack.webhook.server-error}")
    private String serverErrorWebhookUrl;

    public void alertUserSignUp (String name, String email) {
        try {
            String text = name + "(" + email + ")" +"님이 가입했습니다.";
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            slack.send(userJoinWebhookUrl, payload);
        } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    public void alertCreateTeam(String teamName) {
        try {
            String text = "팀 : " + teamName + "이 생성되었습니다.";
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            slack.send(teamCreatedWebhookUrl, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void alertCreateMeeting(String location, String meetingName) {
        try {
            String text = location + "에서" + meetingName + "번개가 생성되었습니다.";
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            slack.send(meetingCreatedWebhookUrl, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void alertError(Exception exception) {
        try {
            String text = exception.getClass().getName() + "  "
                    + exception.getMessage() + "\n"
                    + Arrays.toString(exception.getStackTrace());
            Slack slack = Slack.getInstance();
            Payload payload = Payload.builder().text(text).build();
            slack.send(serverErrorWebhookUrl, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
