package io.github.lujianbo.mqtt.service;

import io.github.lujianbo.mqtt.manager.MQTTContext;

/**
 * Created by lujianbo on 2016/4/12.
 */
public class MQTTMessageHandlerFactoryImpl implements MQTTMessageHandlerFactory {


    private MQTTContext context;

    @Override
    public MQTTMessageHandler register(MQTTSession session) {
        return new MQTTMessageHandler(session);
    }
}
