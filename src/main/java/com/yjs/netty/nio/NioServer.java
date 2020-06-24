package com.yjs.netty.nio;

import com.yjs.netty.util.CommonUtil;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * <pre>
 *		NIO服务端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/9
 */
public class NioServer {

	public static int PORT = 9922;

	public static Selector selector;

	public static String content = "";

	public static void main(String[] args) throws Exception {

		try {
			ServerSocketChannel server = ServerSocketChannel.open();
			server.bind(new InetSocketAddress(PORT));
			server.configureBlocking(false);
			selector = Selector.open();
			server.register(selector, SelectionKey.OP_ACCEPT);

			System.out.println("NioEchoServer已启动，端口：" + PORT);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

		while (true) {
			try {
				selector.select();
			} catch (IOException e) {
				System.out.println("NioEchoServer异常!" + e.getMessage());
			}
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				processKey(key);
			}
		}
	}

	/**
	 * <pre>
	 *		处理
	 * </pre>
	 *
	 * @param key 表示可选择通道与选择器注册的令牌
	 */
	public static void processKey(SelectionKey key) throws Exception{

		ByteBuffer buffer = ByteBuffer.allocate(1024);

		if (key.isAcceptable()) {
			ServerSocketChannel server = (ServerSocketChannel)key.channel();
			SocketChannel client = server.accept();
			client.configureBlocking(false);
			client.register(selector, SelectionKey.OP_READ);
			key.interestOps(SelectionKey.OP_ACCEPT);
			System.out.println("接收到客户端连接: " + client.toString());
		} else if(key.isReadable()) {
			SocketChannel client = (SocketChannel)key.channel();

			int len = client.read(buffer);
			if(len > 0){
				buffer.flip();
				content = new String(buffer.array(), 0, len);
				System.out.println("读取到客户端消息: " + content);
//				client.register(selector, SelectionKey.OP_WRITE);
			}
			buffer.clear();

		}
	}

}