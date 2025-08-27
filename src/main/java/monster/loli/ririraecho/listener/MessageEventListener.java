package monster.loli.ririraecho.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import monster.loli.ririraecho.entity.Constant;
import monster.loli.ririraecho.entity.Payload;
import monster.loli.ririraecho.util.MessageTaskEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MessageEventListener {

    @EventListener(condition = "event.tag() == '" + Constant.MessageTaskTag.GROUP_AT_MESSAGE_CREATE + "'")
    public void rrAddTask(MessageTaskEvent event) {
        log.info("RR add task: {}", event.getData());
        //  类型检查
        Payload payload = event.data(Payload.class);
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
