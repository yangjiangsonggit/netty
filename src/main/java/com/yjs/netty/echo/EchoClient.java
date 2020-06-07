package com.yjs.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * <pre>
 *		Echo客户端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/4
 */
@Slf4j
public class EchoClient {


	public static final String HOST = "127.0.0.1";
	public static final int PORT = 9994;
	public static Channel channel;


	public static void main(String[] args) {

		Bootstrap bootstrap = new Bootstrap();

		NioEventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try {

			EchoClientInboundHandler echoClientInboundHandler = new EchoClientInboundHandler();
			//		new EchoClientOutboundHandle()
			bootstrap.group(workerGroup)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(echoClientInboundHandler);
//							pipeline.addLast(new StringEncoder());
						}
					});

			ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
			log.info("连接到服务端{}:{}",HOST,PORT);

			channel = future.channel();
			sendMsg();
			future.channel().closeFuture().sync();


		} catch (Exception e) {
			log.error("客户端异常",e.fillInStackTrace());
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	private static void sendMsg() {
		System.out.println("请输入消息:");
		Scanner sc = new Scanner(System.in);
		String input;
		while (sc.hasNextLine()) {
			input = sc.nextLine();
			ByteBuf buffer = Unpooled.wrappedBuffer(input.getBytes(CharsetUtil.UTF_8));
			channel.writeAndFlush(buffer);
		}
		sc.close();
	}
}
