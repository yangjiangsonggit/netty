package com.yjs.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * <pre>
 *
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/14
 */
public class HttpRequestServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		this.readRequest(msg);

		String sendMsg;
		String uri = msg.uri();

		switch (uri) {
			case "/":
				sendMsg = "<h3>Netty HTTP Server</h3><p>Welcome to <a href=\"https://www.chinawyny.com/\">www.chinawyny.com</a></p>";
				break;
			case "/hi":
				sendMsg = "<h3>Netty HTTP Server</h3><p>Hello Word!</p>";
				break;
			default:
				sendMsg = "<h3>Netty HTTP Server</h3><p>666</p>";
				break;
		}

		ByteBuf bf = Unpooled.copiedBuffer(sendMsg, CharsetUtil.UTF_8);

		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, bf);
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, sendMsg.length());
		response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");
		boolean keepAlive = HttpUtil.isKeepAlive(msg);
		if (keepAlive) {
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, sendMsg.length());
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
		}
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
	}

	private void readRequest(FullHttpRequest msg) {
		System.out.println("======请求行======");
		System.out.println(msg.method() + " " + msg.uri() + " " + msg.protocolVersion());

		System.out.println("======请求头======");
		for (String name : msg.headers().names()) {
			System.out.println(name + ": " + msg.headers().get(name));
		}

		System.out.println("======消息体======");
		System.out.println(msg.content().toString(CharsetUtil.UTF_8));
	}

}
