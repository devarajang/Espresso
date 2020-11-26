/**
 * 
 */
package espresso.iso;

import java.nio.ByteBuffer;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author deva
 *
 */
public class ISOMessageEncoder extends MessageToByteEncoder<IsoMessage> {

	private final int lengthHeaderLength;
    private final boolean encodeLengthHeaderAsString;

    public ISOMessageEncoder(int lengthHeaderLength, boolean encodeLengthHeaderAsString) {
        this.lengthHeaderLength = lengthHeaderLength;
        this.encodeLengthHeaderAsString = encodeLengthHeaderAsString;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, IsoMessage isoMessage, ByteBuf out) {
        if (lengthHeaderLength == 0) {
            final byte[] bytes = isoMessage.writeData();
            out.writeBytes(bytes);
        } else if (encodeLengthHeaderAsString) {
        	isoMessage.setValue(60, "0051427189|0000|Qolo209000741", IsoType.LLLVAR, 28);
        	isoMessage.setValue(61, "0051427189|0000|Sutton209000741", IsoType.LLLVAR, 28);
            byte[] bytes = isoMessage.writeData();
            String lengthHeader = String.format("%0" + lengthHeaderLength + "d", bytes.length);
            out.writeBytes(lengthHeader.getBytes(CharsetUtil.US_ASCII));
            out.writeBytes(bytes);
        } else {
        	byte[] bytes = isoMessage.writeData();
        	byte[] lengthBytes = ByteBuffer.allocate(2).putShort((short) bytes.length).array();
            out.writeBytes(lengthBytes);
            out.writeBytes(bytes);
            
        }
    }
}
