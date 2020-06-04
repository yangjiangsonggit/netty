package com.yjs.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

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
							//						pipeline.addLast()
						}
					});

			ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			log.error("客户端异常");
			workerGroup.shutdownGracefully();
		}
	}
}
