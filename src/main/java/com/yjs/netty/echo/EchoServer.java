package com.yjs.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *		Echo服务端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/4
 */
@Slf4j
public class EchoServer {

	public static final int PORT = 9994;

	public static void main(String[] args) {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		EchoServerHandler echoServerHandler = new EchoServerHandler();

		try {
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
//							pipeline.addLast(new StringDecoder());
							pipeline.addLast(echoServerHandler);
						}
					});

			ChannelFuture future = serverBootstrap.bind(PORT).sync();
			log.info("服务端已经启动,端口:{}",PORT);
			future.channel().closeFuture().sync();


		} catch (Exception e) {
			log.error("服务器异常",e);

		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();

		}


	}
}
