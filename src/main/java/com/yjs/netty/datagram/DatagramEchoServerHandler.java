package com.yjs.netty.datagram;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * <pre>
 *		server处理器
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/7
 */
public class DatagramEchoServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		DatagramPacket packet = (DatagramPacket) msg;
		InetSocketAddress sender = packet.sender();
		System.out.println("host: " + sender.toString());
		System.out.println("收到消息: " + packet.content().toString(CharsetUtil.UTF_8));

		DatagramPacket result = new DatagramPacket(packet.content(), packet.sender());
		ctx.writeAndFlush(result);
	}


}
