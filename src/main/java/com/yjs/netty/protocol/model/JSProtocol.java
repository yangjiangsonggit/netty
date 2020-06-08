package com.yjs.netty.protocol.model;

import lombok.Data;

/**
 * <pre>
 *		江松协议
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/8
 */
@Data
public class JSProtocol {

	private ProtocolHeader protocolHeader = new ProtocolHeader();

	private String body;

}
