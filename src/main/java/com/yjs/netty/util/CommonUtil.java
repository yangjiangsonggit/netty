package com.yjs.netty.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * <pre>
 *
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
}
