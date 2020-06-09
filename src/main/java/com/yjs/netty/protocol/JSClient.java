package com.yjs.netty.protocol;

import com.yjs.netty.protocol.codec.JSProtocolEncoder;
import com.yjs.netty.protocol.model.JSProtocol;
import com.yjs.netty.protocol.model.ProtocolHeader;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * <pre>
 *		客户端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/9
 */
public class JSClient {


	public static void main(String[] args) {

		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("encoder", new JSProtocolEncoder());
						}
					});


			ChannelFuture future = bootstrap.connect("127.0.0.1", JSServer.PORT).sync();
			System.out.println("客户端启动");
			Channel channel = future.channel();
			sendMsg(channel);
			future.channel().closeFuture().sync();


		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			group.shutdownGracefully();

		}

	}

	private static void sendMsg(Channel channel) {
		System.out.println("请输入消息:");
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine()) {
			String body = sc.nextLine();

			JSProtocol msg = new JSProtocol();
			ProtocolHeader protocolHeader = new ProtocolHeader();
			protocolHeader.setMagic((byte) 0x01);
			protocolHeader.setMsgType((byte) 0x01);
			protocolHeader.setReserve((short) 0);
			protocolHeader.setSn((short) 0);


			byte[] bodyBytes = body.getBytes(CharsetUtil.UTF_8);
			int bodySize = bodyBytes.length;
			protocolHeader.setLen(bodySize);

			msg.setProtocolHeader(protocolHeader);
			msg.setBody(body);

			channel.writeAndFlush(msg);
		}
		sc.close();
	}
}
