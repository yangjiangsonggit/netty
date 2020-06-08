package com.yjs.netty.protocol.model;

import lombok.Data;

/**
 * <pre>
 *		江松协议头
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/8
 */
@Data
public class ProtocolHeader {

	/**  魔数  */
	private byte  magic;
	/**  消息类型  */
	private byte  msgType;
	/**  保留字  */
	private short reserve;
	/**  序列号  */
	private short sn;
	/**  长度  */
	private int   len;

}
