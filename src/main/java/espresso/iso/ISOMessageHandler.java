/**
 * 
 */
package espresso.iso;

import java.util.Optional;
import java.util.Random;

import com.solab.iso8583.IsoMessage;

import espresso.handlers.MessageHandler;
import espresso.util.MessageHandlerUtil;
import espresso.util.Util;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author deva
 *
 */
public class ISOMessageHandler extends ChannelInboundHandlerAdapter {

	
	private MessageHandlerUtil util;

	public ISOMessageHandler(MessageHandlerUtil util)  {
		this.util = util;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		// closed on shutdown.
		EspressoServer.channels.add(ctx.channel());
		super.channelActive(ctx);
		System.out.println(" channel count - " + EspressoServer.channels.size());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		IsoMessage incomingMessage = (IsoMessage) msg;

		Optional<MessageHandler> messageHandler = this.util.getAll().stream()
				.filter(m -> m.canHandle(incomingMessage)).findFirst();
		System.out.println("Receive Channel Id " + ctx.channel().id());
		if (messageHandler.isPresent()) {
			IsoMessage response = messageHandler.get().handleMessage(incomingMessage);
			Random random = new Random();
			Thread.sleep(1000 + random.nextInt(3000));
			EspressoServer.channels.forEach(ch -> {
				if(ch.id() != ctx.channel().id()) {
					System.out.println("Reply Channel Id " + ctx.channel().id());
					ch.writeAndFlush(response);
					return;
				}
			});
			
			ctx.channel().writeAndFlush(response);
			
		} else {
			Util.print(incomingMessage);
			//response = messageHandler.get().handleMessage(incomingMessage);
//			System.out.println("Invalid Message received" + incomingMessage.debugString());
		}

		
		//future.addListener(ChannelFutureListener.CLOSE);
		System.out.println(incomingMessage.debugString());
	}

}
