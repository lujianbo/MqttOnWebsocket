package io.github.lujianbo.sentinelmq.net.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 完成 websocket 的握手和后续处理器的配置
 */
public class WebSocketHandShaker extends SimpleChannelInboundHandler<FullHttpRequest> {


    private String uri = "/mqtt";

    private String subprotocols = "mqttv3.1";

    public WebSocketHandShaker() {

    }

    public WebSocketHandShaker(String uri, String subprotocols) {
        this.uri = uri;
        this.subprotocols = subprotocols;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        // Allow only GET methods.
        if (req.method() != GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }

        /**
         * 当且仅当uri为指定path的时候,进行websocket通讯的升级
         * */
        if (uri.equals(req.uri())
                //CONNECTION 字段的值为 UPGRADE, firefox上存在多个值的情况
                && req.headers().get(HttpHeaderNames.CONNECTION).contains(HttpHeaderValues.UPGRADE)
                //UPGRADE 字段的值为 WEBSOCKET
                && HttpHeaderValues.WEBSOCKET.contentEqualsIgnoreCase(req.headers().get(HttpHeaderNames.UPGRADE))
                ) {
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    uri, subprotocols, true, 5 * 1024 * 1024);
            WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                //不支持的协议
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                //握手结束后补充如下协议
                handshaker.handshake(ctx.channel(), req);
            }
            return;
        }
        //错误的情况
        sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND));
    }


    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getSubprotocols() {
        return subprotocols;
    }

    public void setSubprotocols(String subprotocols) {
        this.subprotocols = subprotocols;
    }
}
