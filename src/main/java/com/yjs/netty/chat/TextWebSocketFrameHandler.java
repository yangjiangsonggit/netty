package com.yjs.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * <pre>
 *		Web套接字文本消息处理
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/13
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		Channel currentChannel = ctx.channel();
		for (Channel channel : channels) {
			if (channel != currentChannel) {
				channel.writeAndFlush(new TextWebSocketFrame("[用户" + currentChannel.remoteAddress() + "]" + msg.text()));
			} else {
				channel.writeAndFlush(new TextWebSocketFrame("[你]" + msg.text()));
			}
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception { // (2)
		Channel incoming = ctx.channel();

//		channels.writeAndFlush(new TextWebSocketFrame("[用户] - " + incoming.remoteAddress() + " 加入聊天"));

		channels.add(incoming);
		System.out.println("用户:" + incoming.remoteAddress() + "加入聊天");
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel incoming = ctx.channel();

//		channels.writeAndFlush(new TextWebSocketFrame("[用户] - " + incoming.remoteAddress() + " 离开聊天"));

		System.out.println("用户:" + incoming.remoteAddress() + "离开聊天");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
		Channel incoming = ctx.channel();
		channels.writeAndFlush(new TextWebSocketFrame("[用户] - " + incoming.remoteAddress() + " 加入聊天"));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
		Channel incoming = ctx.channel();
		channels.writeAndFlush(new TextWebSocketFrame("[用户] - " + incoming.remoteAddress() + " 离开聊天"));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) // (7)
			throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("用户:" + incoming.remoteAddress() + "异常");
		cause.printStackTrace();
		ctx.close();
	}

}
