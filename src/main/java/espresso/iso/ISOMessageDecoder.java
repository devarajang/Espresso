/**
 * 
 */
package espresso.iso;

import java.text.ParseException;
import java.util.List;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author deva
 *
 */
public class ISOMessageDecoder extends ByteToMessageDecoder {
	
	private MessageFactory<IsoMessage> messageFactory;
	
	
	public ISOMessageDecoder(MessageFactory<IsoMessage> messageFactory) {
		this.messageFactory = messageFactory;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
		if (!byteBuf.isReadable()) {
            return;
        }
        final byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        final IsoMessage isoMessage = messageFactory.parseMessage(bytes, 0);
        if (isoMessage != null) {
                        out.add(isoMessage);
        } else {
            throw new ParseException("Can't parse ISO8583 message", 0);
        }
	}
	

}
