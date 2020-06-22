package com.yjs.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <pre>
 *		NIO客户端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/9
 */
public class NioEchoClient {


	public static final String HOST_NAME = "127.0.0.1";

	public static void main(String[] args) {

		SocketChannel socketChannel = null;
		try {
			socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress(HOST_NAME, NioEchoServer.PORT));
		} catch (IOException e) {
			System.err.println("NioEchoClient异常： " + e.getMessage());
			System.exit(1);
		}

		ByteBuffer writeBuffer = ByteBuffer.allocate(32);
		ByteBuffer readBuffer = ByteBuffer.allocate(32);

		try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
			String userInput;
			while ((userInput = stdIn.readLine()) != null) {
				writeBuffer.put(userInput.getBytes());
				writeBuffer.flip();
				writeBuffer.rewind();

				// 写消息到管道
				socketChannel.write(writeBuffer);

				// 管道读消息
				socketChannel.read(readBuffer);

				// 清理缓冲区
				writeBuffer.clear();
				readBuffer.clear();
				System.out.println("echo: " + userInput);
			}
		} catch (UnknownHostException e) {
			System.err.println("不明主机，主机名为： " + HOST_NAME);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("不能从主机中获取I/O，主机名为：" + HOST_NAME);
			System.exit(1);
		}
	}

}