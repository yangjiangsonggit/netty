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
public class NioEchoServer {

	public static int PORT = 9922;

	public static void main(String[] args) {

		ServerSocketChannel serverChannel;
		Selector selector;
		try {
			serverChannel = ServerSocketChannel.open();
			InetSocketAddress address = new InetSocketAddress(PORT);
			serverChannel.bind(address);
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);

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
				processKey(selector, key);
			}
		}
	}

	/**
	 * <pre>
	 *		处理
	 * </pre>
	 *
	 * @param selector 可选择通道对象的多路复用器
	 * @param key 表示可选择通道与选择器注册的令牌
	 */
	private static void processKey(Selector selector, SelectionKey key) {

		try {
			// 可连接
			if (key.isAcceptable()) {
				ServerSocketChannel server = (ServerSocketChannel) key.channel();
				SocketChannel socketChannel = server.accept();

				System.out.println("NioEchoServer接受客户端的连接：" + socketChannel);

				// 设置为非阻塞
				socketChannel.configureBlocking(false);

				// 客户端注册到Selector
				SelectionKey clientKey = socketChannel.register(selector, SelectionKey.OP_READ);

				// 分配缓存区
				ByteBuffer buffer = ByteBuffer.allocate(100);
				clientKey.attach(buffer);
			}

			// 可读
			if (key.isReadable()) {
				SocketChannel client = (SocketChannel) key.channel();
				ByteBuffer output = (ByteBuffer) key.attachment();
				client.read(output);

				System.out.println(client.getRemoteAddress()
						+ " -> NioEchoServer读取到消息：" + output.toString());

				key.interestOps(SelectionKey.OP_WRITE);
			}

			// 可写
			if (key.isWritable()) {
				SocketChannel client = (SocketChannel) key.channel();
				ByteBuffer output = (ByteBuffer) key.attachment();
				output.flip();
				client.write(output);

				System.out.println("NioEchoServer写入到  -> "
						+ client.getRemoteAddress() + "：" + output.toString());

				output.compact();

				key.interestOps(SelectionKey.OP_READ);
			}
		} catch (IOException ex) {
			key.cancel();
			try {
				key.channel().close();
			} catch (IOException cex) {
				System.out.println(
						"NioEchoServer异常!" + cex.getMessage());
			}
		}
	}

}