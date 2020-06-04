package com.yjs.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.unix.Buffer;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

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
public class EchoClientInboundHandler extends ChannelInboundHandlerAdapter {


	public static final int MAX = 10;
	public static final int INITIAL_CAPACITY = 256;
	private final AtomicInteger count = new AtomicInteger(0);
	private ByteBuf msg;

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		log.info("通道已经注册,context -> {}" , ctx.toString());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("通道已经激活,context -> {}" , ctx.toString());
		msg = Unpooled.buffer(INITIAL_CAPACITY);
//		new Thread(() -> {
			log.info("请在控制台输入消息内容");
			Scanner scanner = new Scanner(System.in);
//			while (count.incrementAndGet() > MAX) {
				if(scanner.hasNext()){
					String input = scanner.nextLine();
					msg.writeBytes(input.getBytes());
					ctx.writeAndFlush(msg);
				}
//			}
			scanner.close();
//		}).start();

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		log.info("读取数据,context -> {},msg -> {}" , ctx.toString(), new String(new byte[]{(byte)msg}));
//		ctx.writeAndFlush(msg);
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
