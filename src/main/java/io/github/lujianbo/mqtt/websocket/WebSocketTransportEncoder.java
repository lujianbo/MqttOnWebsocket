package io.github.lujianbo.mqtt.websocket;

import io.github.lujianbo.mqtt.codec.message.MQTTMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * 将ByteBuf 封装到 BinaryWebSocketFrame
 * */
public class WebSocketTransportEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        BinaryWebSocketFrame frame=new BinaryWebSocketFrame(msg);
        out.add(frame);
    }
}
