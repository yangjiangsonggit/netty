package com.yjs.netty.protocol.codec;

/**
 * <pre>
 *		解码器常量
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/8
 */
public final class DecoderConstant {

	/**  帧的最大长度  */
	public static final int MAX_FRAME_LENGTH 		= 10 * 1024 * 1024;

	/**  length字段所占的字节长  */
	public static final int LENGTH_FIELD_LENGTH 	= 4;

	/**  length字段偏移的地址  */
	public static final int LENGTH_FIELD_OFFSET		= 6;

	/**  修改帧数据长度字段中定义的值，可以为负数 因为有时候我们习惯把头部记入长度,若为负数,则说明要推后多少个字段 */
	public static final int LENGTH_ADJUSTMENT 		= 0;

	/**  解析时候跳过多少个长度  */
	public static final int INITIAL_BYTES_TO_STRIP 	= 0;

	/**  消息头长度  */
	public static final int HEADER_SIZE 			= 10;

}
