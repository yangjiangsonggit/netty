package com.yjs.netty.util;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.nio.charset.Charset;

/**
 * <pre>
 *		通用工具类
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/6
 */
public final class CommonUtil {


	public static String convertByteBufToString(ByteBuf buf) {
		String str;
		// 处理堆缓冲区
		if(buf.hasArray()) {
			str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
			// 处理直接缓冲区以及复合缓冲区
		} else {
			byte[] bytes = new byte[buf.readableBytes()];
			buf.getBytes(buf.readerIndex(), bytes);
			str = new String(bytes, 0, buf.readableBytes(), Charset.defaultCharset());
		}
		return str;
	}

	public static ChannelFuture sync(ServerBootstrap serverBootstrap, int port) throws InterruptedException {
		ChannelFuture future = serverBootstrap.bind(port).sync();
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					System.out.println("服务端绑定成功");
				} else {
					future.cause().printStackTrace();
				}
			}
		});
		return future;
	}
}
