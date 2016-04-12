package io.github.lujianbo.netty.handler;


import io.github.lujianbo.mqtt.domain.MQTTMessage;
import io.github.lujianbo.mqtt.service.MQTTMessageHandler;
import io.github.lujianbo.mqtt.service.MQTTMessageHandlerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * 完成MQTT和Netty部分的对接，实现数据的上下行
 */
public class MQTTServerHandler extends SimpleChannelInboundHandler<MQTTMessage> {

    private MQTTMessageHandlerFactory factory;

    private MQTTMessageHandler handler;


    public MQTTServerHandler(MQTTMessageHandlerFactory factory) {
        this.factory = factory;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handler = factory.register(new MQTTNettySession(ctx.channel()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MQTTMessage msg) throws Exception {
        handler.onRead(msg);
    }

}
