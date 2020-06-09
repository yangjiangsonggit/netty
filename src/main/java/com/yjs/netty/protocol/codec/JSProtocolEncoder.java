package com.yjs.netty.protocol.codec;

import com.yjs.netty.protocol.model.JSProtocol;
import com.yjs.netty.protocol.model.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * <pre>
 *
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/9
 */
public class JSProtocolEncoder  extends MessageToByteEncoder<JSProtocol> {

	@Override
	protected void encode(ChannelHandlerContext ctx, JSProtocol msg, ByteBuf out) throws Exception {

		if (msg == null || msg.getProtocolHeader() == null) {
			throw new Exception("消息为空");
		}

		byte[] body = msg.getBody().getBytes(CharsetUtil.UTF_8);
		int length = body.length;
		ProtocolHeader header = msg.getProtocolHeader();

		out.writeByte(header.getMagic());
		out.writeByte(header.getMsgType());
		out.writeShort(header.getReserve());
		out.writeShort(header.getSn());
		out.writeInt(length);
		out.writeBytes(body);
	}
}
