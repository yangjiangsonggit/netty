package com.yjs.netty.datagram;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * <pre>
 *		UDP客户端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/7
 */
public class DatagramEchoClient {

	public static final int INET_PORT = 9996;

	private static Channel channel;

	public static void main(String[] args) {

		NioEventLoopGroup group = new NioEventLoopGroup(1);
		try {

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)
				.channel(NioDatagramChannel.class)
				.handler(new DatagramEchoClientHandler());
		ChannelFuture future = bootstrap.bind(INET_PORT).sync();
		System.out.println("DatagramChannelEchoClient已启动，端口：" + INET_PORT);
		channel = future.channel();
			sendMsg();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

	}

	private static void sendMsg() {
		System.out.println("请输入消息:");
		Scanner sc = new Scanner(System.in);
		String input;
		while (sc.hasNextLine()) {
			input = sc.nextLine();
			ByteBuf buffer = Unpooled.wrappedBuffer(input.getBytes(CharsetUtil.UTF_8));
			DatagramPacket packet = new DatagramPacket(buffer,
					new InetSocketAddress("127.0.0.1", DatagramEchoServer.INET_PORT));
			channel.writeAndFlush(packet);
		}
		sc.close();
	}
}
