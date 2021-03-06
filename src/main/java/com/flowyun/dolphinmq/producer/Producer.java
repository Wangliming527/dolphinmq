package com.flowyun.dolphinmq.producer;

import com.flowyun.dolphinmq.common.DolphinMQConfig;
import com.flowyun.dolphinmq.common.Message;
import com.flowyun.dolphinmq.utils.BeanMapUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RFuture;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.stream.StreamAddArgs;
import org.redisson.api.stream.TrimStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 生产者基类
 *
 * @author Barry
 * @since 2021/6/28 14:40
 */
@Slf4j
@Component
public class Producer {
    private RedissonClient client;
    /**
     * 超过了该长度stream前面部分会被持久化（非严格模式——MAXLEN~）
     */

    private DolphinMQConfig config;

    @Autowired
    public void setConfig(DolphinMQConfig config) {
        this.config = config;
    }

    public void setClient(RedissonClient client) {
        this.client = client;
    }

    public Producer() {

    }


    /**
     * 异步发送消息到Redis
     *
     * @param msg 消息
     * @author Barry
     * @since 2021/6/28 15:38
     **/
    public void sendMessageAsync(Message msg) {
        if (StringUtil.isNullOrEmpty(msg.getTopic())) {
            throw new NullPointerException("Message topic is required");
        }
        RStream<Object, Object> stream = client.getStream(msg.getTopic());
        RFuture<Void> sendMessageFuture =
                stream.addAsync(
                        msg.getId(),
                        StreamAddArgs.entries(BeanMapUtils.getObjectObjectMap(msg.getProperties())).trim(TrimStrategy.MAXLEN, config.getTrimThreshold()));
        sendMessageFuture.thenAccept(res -> log.debug("stream : {} add message:{} success",
                msg.getTopic(),
                msg.getProperties())).exceptionally(exception -> {
            log.debug("stream : {} add message:{} error, exception:{}",
                    msg.getTopic(),
                    msg.getProperties(),
                    exception.getMessage());
            return null;
        });
    }
}
