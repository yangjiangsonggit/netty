package com.yjs.netty.protocol.model;

/**
 * <pre>
 *		消息类型
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/8
 */
public enum MsgType {

	/**  上传数据  */
	UPLOAD_MSG("上传数据",(byte) 0x00),

	/**  心跳  */
	HEART_BEAT("心跳",(byte) 0x01);


	private final String type;
	private final byte val;

	public String getType() {
		return type;
	}

	public byte getVal() {
		return val;
	}

	MsgType(String type,byte val) {
		this.type = type;
		this.val = val;
	}
}
