package monster.loli.ririraecho.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.List;

/**
 * 统一消息事件
 */
public class MessageTaskEvent extends ApplicationEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String tag;
    private String data;

    public MessageTaskEvent() {
        super("-");
        this.tag = "-";
    }

    public MessageTaskEvent(String tag) {
        super(tag);
        this.tag = tag;
    }

    public MessageTaskEvent(String tag, Object data) {
        super(tag);
        this.tag = tag;
        try {
            this.data = MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("序列化 data 失败", e);
        }
    }

    public <T> T data(Class<T> clazz) {
        if (this.data == null) return null;
        try {
            return MAPPER.readValue(this.data, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化 data 失败", e);
        }
    }

    public <T> List<T> dataList(Class<T> clazz) {
        if (this.data == null) return null;
        try {
            return MAPPER.readValue(this.data,
                    MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("反序列化 dataList 失败", e);
        }
    }

    public String tag() {
        return this.tag;
    }

    public String getTag() {
        return this.tag;
    }

    public String getData() {
        return this.data;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setData(String data) {
        this.data = data;
    }
}
