package io.github.lujianbo.netty.handler;


import io.github.lujianbo.mqtt.domain.MQTTMessage;
import io.github.lujianbo.mqtt.service.MQTTSession;
import io.netty.channel.Channel;

/**
 *
 */
public class MQTTNettySession implements MQTTSession{

    private Channel channel;

    public MQTTNettySession(Channel channel){
        this.channel=channel;
    }

    @Override
    public void write(MQTTMessage message) {
        channel.write(message);
    }

    @Override
    public void close() {
        channel.close();
    }

    @Override
    public void onException() {
        channel.close();
    }
}
