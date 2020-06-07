package com.yjs.netty.echo;

import com.yjs.netty.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *		服务端处理器
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
		log.info("服务端通道已经注册");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("服务端通道已经激活");
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf byteBuf = (ByteBuf) msg;
		String s = byteBuf.toString(CharsetUtil.UTF_8);
//		String str = CommonUtil.convertByteBufToString(byteBuf);
		log.info("服务端读取数据,msg -> {}" , s);
		ctx.writeAndFlush(byteBuf);
//		byteBuf.clear();

	}

//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//		log.info("服务端读取数据,context -> {},msg -> {}" , ctx.toString(), msg);
//		ctx.writeAndFlush(msg);
//	}


	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		log.info("服务端读取数据完成");
	}


	@Override
	@SuppressWarnings("deprecation")
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		log.info("服务端读取数据异常,cause -> {}" , cause.toString());
	}


}
