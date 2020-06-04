package com.yjs.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/4
 */
@Slf4j
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		log.info("通道已经注册,context -> {}" , ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("通道已经激活,context -> {}" , ctx.toString());

	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		ByteBuf msgCast = (ByteBuf) msg;
		log.info("读取数据,context -> {},msg -> {}" , ctx.toString(), msg);
		ctx.writeAndFlush(msg);
	}



	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		log.info("读取数据完成,context -> {}" , ctx.toString());
	}


	@Override
	@SuppressWarnings("deprecation")
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		log.info("读取数据异常,context -> {},cause -> {}" , ctx.toString(), cause.toString());
	}


}
