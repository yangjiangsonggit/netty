package com.yjs.netty.datagram;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * <pre>
 *		UDP
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/7
 */
public class DatagramEchoServer {

	public static final int INET_PORT = 9997;

	public static void main(String[] args) {

		NioEventLoopGroup eventExecutors = new NioEventLoopGroup(1);
		try {

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventExecutors)
				.channel(NioDatagramChannel.class)
				.handler(new DatagramEchoServerHandler());
		ChannelFuture future = bootstrap.bind(INET_PORT).sync();

		System.out.println("DatagramChannelEchoServer已启动，端口：" + INET_PORT);

		future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventExecutors.shutdownGracefully();
		}

	}
}
