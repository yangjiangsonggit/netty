package com.yjs.netty.protocol.codec;

import com.yjs.netty.protocol.model.JSProtocol;
import com.yjs.netty.protocol.model.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.CharsetUtil;

import java.nio.ByteOrder;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 *		江松协议解码器
 * </pre>
 *
 * maxFrameLength - 发送的数据帧最大长度
 * lengthFieldOffset - 定义长度域位于发送的字节数组中的下标。换句话说：发送的字节数组中下标为${lengthFieldOffset}的地方是长度域的开始地方
 * lengthFieldLength - 用于描述定义的长度域的长度。换句话说：发送字节数组bytes时, 字节数组bytes[lengthFieldOffset, lengthFieldOffset+lengthFieldLength]域对应于的定义长度域部分
 * lengthAdjustment - 满足公式: 发送的字节数组bytes.length - lengthFieldLength = bytes[lengthFieldOffset, lengthFieldOffset+lengthFieldLength] + lengthFieldOffset + lengthAdjustment
 * initialBytesToStrip - 接收到的发送数据包，去除前initialBytesToStrip位
 * failFast - true: 读取到长度域超过maxFrameLength，就抛出一个 TooLongFrameException。false: 只有真正读取完长度域的值表示的字节之后，才会抛出 TooLongFrameException，默认情况下设置为true，建议不要修改，否则可能会造成内存溢出
 * ByteOrder - 数据存储采用大端模式或小端模式
 *
 * @see io.netty.handler.codec.LengthFieldBasedFrameDecoder
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/8
 */
public class JSProtocolDecoder extends LengthFieldBasedFrameDecoder {


	public JSProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	public JSProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip, boolean failFast) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
	}


	@Override
	protected JSProtocol decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf decodeBuf = (ByteBuf) super.decode(ctx, in);

		if (Objects.isNull(decodeBuf)) {
			return null;
		}

		if (decodeBuf.readableBytes() < DecoderConstant.HEADER_SIZE) {
			return null;
		}

		ProtocolHeader protocolHeader = new ProtocolHeader();
		protocolHeader.setMagic(decodeBuf.readByte());
		protocolHeader.setMsgType(decodeBuf.readByte());
		protocolHeader.setReserve(decodeBuf.readShort());
		protocolHeader.setSn(decodeBuf.readShort());
		int len = decodeBuf.readInt();
		protocolHeader.setLen(len);

		if (decodeBuf.readableBytes() < len) {
			return null;
		}

		ByteBuf bodyBuf = decodeBuf.readBytes(len);
		String body = bodyBuf.toString(CharsetUtil.UTF_8);

		JSProtocol jsProtocol = new JSProtocol();
		jsProtocol.setProtocolHeader(protocolHeader);
		jsProtocol.setBody(body);

		System.out.println("服务端解析到消息 -> " + body);
		return jsProtocol;
	}

}
