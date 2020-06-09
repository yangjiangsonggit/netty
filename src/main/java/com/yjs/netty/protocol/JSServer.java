package com.yjs.netty.protocol;


import com.yjs.netty.protocol.codec.DecoderConstant;
import com.yjs.netty.protocol.codec.JSProtocolDecoder;
import com.yjs.netty.util.CommonUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * <pre>
 *		服务器端
 * </pre>
 *
 * @author yangjs
 * @version 1.0
 * @since 2020/6/9
 */
public class JSServer {


	public static final int PORT = 8899;

	public static void main(String[] args) {


		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("decoder",
									new JSProtocolDecoder(DecoderConstant.MAX_FRAME_LENGTH,
											DecoderConstant.LENGTH_FIELD_OFFSET,
											DecoderConstant.LENGTH_FIELD_LENGTH,
											DecoderConstant.LENGTH_ADJUSTMENT,
											DecoderConstant.INITIAL_BYTES_TO_STRIP));


						}
					});

			ChannelFuture future = CommonUtil.sync(serverBootstrap, PORT);
			System.out.println("服务端已经启动,端口:{}" + PORT);
			future.channel().closeFuture().sync();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}



}
