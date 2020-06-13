package com.yjs.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.File;
import java.net.URL;

/**
 * <pre>
 *		http处理
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/13
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String wsUri;

	private static File CLIENT_INDEX;

	public static final String CHAT_CLIENT_HTML = "ChatClient.html";

	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			String path = location.toURI() + CHAT_CLIENT_HTML;
			path = !path.contains("file:") ? path : path.substring(5);
			CLIENT_INDEX = new File(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HttpRequestHandler(String wsUri) {
		this.wsUri = wsUri;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {



	}
}
