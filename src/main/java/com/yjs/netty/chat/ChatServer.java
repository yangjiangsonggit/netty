package com.yjs.netty.chat;


import com.yjs.netty.util.CommonUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.SSLEngine;

/**
 * <pre>
 *		聊天服务器端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/13
 */
public class ChatServer {


	public static final int PORT = 8890;
	public static final int MAX_CONTENT_LENGTH = 64 * 1024 * 1024;
	public static final String WS_URI = "/ws";
//	public static SslContext sslContext;

	public static void main(String[] args) {


		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			SelfSignedCertificate cert = new SelfSignedCertificate();
//			sslContext = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();

			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 60000)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
//							SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
//							sslEngine.setUseClientMode(false);
//							pipeline.addLast("sslHandler", new SslHandler(sslEngine));
							pipeline.addLast("httpServerCodec", new HttpServerCodec());
							//大数据流支持
							pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
							//聚合content
							pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(MAX_CONTENT_LENGTH));
							pipeline.addLast("httpRequestHandler", new HttpRequestHandler(WS_URI));
							//负责websocket握手以及控制帧（Close、Ping、Pong）的处理
							pipeline.addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler(WS_URI));
							pipeline.addLast("textWebSocketFrameHandler", new TextWebSocketFrameHandler());

						}
					});

			ChannelFuture future = CommonUtil.sync(serverBootstrap, PORT);
			System.out.println("服务端已经启动,端口:{}" + PORT);
			future.channel().closeFuture().sync();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			System.out.println("服务端已经关闭,端口:{}" + PORT);
		}
	}



}
