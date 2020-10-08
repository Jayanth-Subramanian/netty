package com.netty.chat.client;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatClient {

	static final String HOST = "127.0.0.1";
	static String clientName;

	public static void main(String[] args) throws Exception {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter your name: ");
		if (scanner.hasNext()) {
			clientName = scanner.nextLine();
			System.out.println("Welcome " + clientName);
		}

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline p = ch.pipeline();

							p.addLast(new StringDecoder());
							p.addLast(new StringEncoder());
//							p.addLast(new LengthFieldPrepender(2));

							p.addLast("clientHandler", new ChatClientHandler());
						}
					});

			ChannelFuture f = b.connect(HOST, 8007).sync();
			while (scanner.hasNext()) {
				String input = scanner.nextLine();
				Channel channel = f.sync().channel();
				channel.writeAndFlush("[" + clientName + "]: " + input);
				channel.flush();
			}

			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
			scanner.close();
		}
	}
}