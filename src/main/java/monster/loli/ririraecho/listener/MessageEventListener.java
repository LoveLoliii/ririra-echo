package monster.loli.ririraecho.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monster.loli.ririraecho.entity.Constant;
import monster.loli.ririraecho.entity.Payload;
import monster.loli.ririraecho.util.MessageTaskEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class MessageEventListener {
    @Value("${ririra.qbot.appId}")
    private String appId;
    @Value("${ririra.qbot.secret}")
    private String secret;


    @EventListener(condition = "event.tag() == '" + Constant.MessageTaskTag.GROUP_AT_MESSAGE_CREATE + "'")
    public void rrAddTask(MessageTaskEvent event) {
        log.info("RR add task: {}", event.getData());
        //  类型检查
        Payload payload = event.data(Payload.class);
        Payload.D data = payload.getD();
        int op = payload.getOp() ;
        String t = payload.getT();
        String access_token = getQbotAccessToken(appId, secret);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String getQbotAccessToken(String appId, String clientSecret) {
        String url = "https://bots.qq.com/app/getAppAccessToken";

        try {
            // 请求体 JSON
            String jsonBody = MAPPER.writeValueAsString(Map.of(
                    "appId", appId,
                    "clientSecret", clientSecret
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Map<String, Object> resMap = MAPPER.readValue(response.body(), Map.class);

            if (resMap.get("access_token") != null) {
                System.out.println("获取 accessToken 成功: " + resMap);
                return resMap.get("access_token").toString();
            } else {
                System.err.println("获取 accessToken 失败，响应异常: " + resMap);
                throw new RuntimeException("无效的 accessToken 响应");
            }

        } catch (Exception e) {
            System.err.println("获取 accessToken 出错: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @EventListener(condition = "event.tag() == '" + Constant.MessageTaskTag.MESSAGE_CREATE + "'")
    public void addTask(MessageTaskEvent event) {
        log.info("add task: {}", event.getData());
        //  类型检查
        Payload payload = event.data(Payload.class);
    }

    @EventListener(condition = "event.tag() == '" + Constant.MessageTaskTag.MESSAGE_REACTION_ADD + "'")
    public void addReactionTask(MessageTaskEvent event) {
        log.info("add reaction task: {}", event.getData());
        //  类型检查
        Payload payload = event.data(Payload.class);
    }

}
