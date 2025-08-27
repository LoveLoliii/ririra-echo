package monster.loli.ririraecho.entity;

import lombok.Data;

@Data
public class Payload {
    private String id;
    private int op;
    private String t;
    private D d;

    @Data
    public static class D {
        private String user_id;
        private String plain_token;
        private String event_ts;
        private String group_openid;
        private String guild_id;
        private String channel_id;
        private String id;
        private String content;
        private String timestamp;

        private Target target;
        private Emoji emoji;
        private Author author;

        @Data
        public static class Target {
            private String id;
            private String type;
        }

        @Data
        public static class Emoji {
            private String id;
            private int type;
        }

        @Data
        public static class Author {
            private String id;
        }
    }
}
