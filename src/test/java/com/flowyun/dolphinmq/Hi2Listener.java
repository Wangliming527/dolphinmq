package com.flowyun.dolphinmq;

import com.flowyun.dolphinmq.consumer.MsgListener;
import lombok.extern.slf4j.Slf4j;

/**
 * Hi2Listener
 *
 * @author Barry
 * @since 2021/7/5 10:07
 */
@Slf4j
public class Hi2Listener extends MsgListener<Testbean> {

    @Override
    public void consume(Testbean dto) {
        log.info("hi2 :dto:{}", dto);
    }
}
