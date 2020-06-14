package com.yjs.netty.chat;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Objects;

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

	public static final String MODE = "r";
	private final String wsUri;

	private static File clientIndex;

	public static final String CHAT_CLIENT_HTML = "client_index.html";

	static {
		URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
		try {
			String path = location.toURI() + CHAT_CLIENT_HTML;
			path = !path.contains("file:") ? path : path.substring(5);
		clientIndex = new File(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HttpRequestHandler(String wsUri) {
		this.wsUri = wsUri;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		//如果前缀匹配则升级协议为WebSocket
		if (wsUri.equalsIgnoreCase(request.uri())) {
			//增加引用计数
			ctx.fireChannelRead(request.retain());
		} else {
			if (HttpUtil.is100ContinueExpected(request)) {
				//满足http 1.1规范
				send100Continue(ctx);
			}

			if (clientIndex.isHidden() || !clientIndex.exists()) {
				System.out.println("文件不存在");
				return;
			}
			if (!clientIndex.isFile()) {
				System.out.println("禁止访问");
				return;
			}

			RandomAccessFile indexFile = new RandomAccessFile(clientIndex, MODE);
			long length = indexFile.length();
			HttpResponse response = new DefaultHttpResponse(request.protocolVersion(),
					HttpResponseStatus.OK);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");
			boolean keepAlive = HttpUtil.isKeepAlive(request);
			if (keepAlive) {
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, length);
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}
			ctx.write(response);

			if (ctx.pipeline().get(SslHandler.class) == null) {
				ctx.write(new DefaultFileRegion(indexFile.getChannel(), 0, length));
			} else {
				ctx.write(new ChunkedNioFile(indexFile.getChannel()));
			}
			ChannelFuture channelFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if (!keepAlive) {
				channelFuture.addListener(ChannelFutureListener.CLOSE);
			}
			indexFile.close();

		}


	}

	private void send100Continue(ChannelHandlerContext ctx) {
		DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.CONTINUE);
		ctx.writeAndFlush(defaultFullHttpResponse);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
