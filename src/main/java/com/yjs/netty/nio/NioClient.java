package com.yjs.netty.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * <pre>
 *		NIO客户端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/9
 */
public class NioClient {


	public static final String HOST_NAME = "127.0.0.1";

	public static volatile SocketChannel client;
	public static volatile Selector selector;


	public static void main(String[] args) throws IOException {
		InetSocketAddress serverAddress = new InetSocketAddress("localhost", NioServer.PORT);
		client = SocketChannel.open(serverAddress);
		client.configureBlocking(false);
		selector = Selector.open();
		client.register(selector, SelectionKey.OP_READ);

//		new Reader().start();
		new Writer().start();
	}

	private static class Writer extends Thread {
		@Override
		public void run() {
			try {
				client = SocketChannel.open();
				client.connect(new InetSocketAddress(HOST_NAME, NioServer.PORT));
			} catch (IOException e) {
				System.err.println("NioEchoClient异常： " + e.getMessage());
				System.exit(1);
			}

			try (BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
				String userInput;
				while ((userInput = stdIn.readLine()) != null) {

					// 写消息到管道
					client.write(StandardCharsets.UTF_8.encode(userInput));
					System.out.println("客户端发送echo: " + userInput);
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

	private static class Reader extends Thread {
		@Override
		public void run() {
			try {
				while(true) {
					int readyChannels = selector.select();
					if(readyChannels == 0) {
						continue;
					}
					Set<SelectionKey> selectedKeys = selector.selectedKeys();
					Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
					while(keyIterator.hasNext()) {
						SelectionKey key = keyIterator.next();
						keyIterator.remove();
						process(key);
					}
				}
			} catch (IOException e){
				e.printStackTrace();
			}
		}

		private void process(SelectionKey key) throws IOException {
			if(key.isReadable()){
				SocketChannel sc = (SocketChannel)key.channel();

				ByteBuffer buff = ByteBuffer.allocate(1024);
				StringBuilder content = new StringBuilder();
				while(sc.read(buff) > 0) {
					buff.flip();
					content.append(StandardCharsets.UTF_8.decode(buff));
				}

				System.out.println("收到服务端发回消息:" + content);
				key.interestOps(SelectionKey.OP_READ);
			}
		}
	}

}