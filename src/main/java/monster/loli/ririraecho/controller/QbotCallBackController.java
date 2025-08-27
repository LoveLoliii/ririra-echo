package monster.loli.ririraecho.controller;

import monster.loli.ririraecho.entity.Payload;
import monster.loli.ririraecho.util.MessageTaskEvent;
import monster.loli.ririraecho.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.NamedParameterSpec;

@RestController
@RequestMapping("/webhook")
public class QbotCallBackController {

    private static final String BOT_SECRET = "your_bot_secret"; // 你的 BOT_SECRET

    @PostMapping
    public ResponseEntity<?> webhook(@RequestBody Payload body) {
        int op = body.getOp();
        String plainToken = body.getD().getPlain_token();
        String eventTs = body.getD().getEvent_ts();

        System.out.println("📦 接收到的 Payload: " + body);

        if (op == 13) {
            // 验证回调地址
            if (plainToken == null || eventTs == null) {
                return ResponseEntity.badRequest().body("{\"error\": \"Invalid payload\"}");
            }

            try {
                // === 生成 Ed25519 KeyPair (基于种子) ===
                byte[] seed = repeatSeed(BOT_SECRET, 32);
                KeyPair keyPair = generateEd25519KeyPair(seed);

                // === 生成签名 ===
                byte[] msg = (eventTs + plainToken).getBytes(StandardCharsets.UTF_8);
                Signature sig = Signature.getInstance("Ed25519");
                sig.initSign(keyPair.getPrivate());
                sig.update(msg);
                byte[] signatureBytes = sig.sign();

                // 转 Hex
                String signatureHex = bytesToHex(signatureBytes);

                return ResponseEntity.ok(new VerifyResponse(plainToken, signatureHex));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("{\"error\": \"internal error\"}");
            }
        } else {
            // 事件分发处理
            botEventsHandle(body);
            return ResponseEntity.ok("{}");
        }
    }

    /**
     * 重复填充 BOT_SECRET 至 32 字节
     */
    private byte[] repeatSeed(String secret, int length) {
        byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
        byte[] seed = new byte[length];
        for (int i = 0; i < length; i++) {
            seed[i] = raw[i % raw.length];
        }
        return seed;
    }

    /**
     * 生成 Ed25519 KeyPair
     */
    private KeyPair generateEd25519KeyPair(byte[] seed) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("Ed25519");
        NamedParameterSpec paramSpec = new NamedParameterSpec("Ed25519");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");
        kpg.initialize(paramSpec, new SecureRandom(seed));
        return kpg.generateKeyPair();
    }

    /**
     * bytes 转 hex
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private void botEventsHandle(Payload body) {
        // 事件处理逻辑
        System.out.println("🤖 收到事件: " + body.getT());
        SpringUtil.publishEvent(new MessageTaskEvent(body.getT(), body));

    }

    // === 返回给平台的对象 ===
    static class VerifyResponse {
        public String plain_token;
        public String signature;

        public VerifyResponse(String plain_token, String signature) {
            this.plain_token = plain_token;
            this.signature = signature;
        }
    }
}
