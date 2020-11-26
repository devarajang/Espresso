/**
 * 
 */
package espresso.iso;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author deva
 *
 */
public class MessageLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

	public MessageLengthFieldBasedFrameDecoder() {
		super(ByteOrder.BIG_ENDIAN, 8192, 0, 2, 0, 2, true);
	}

	@Override
    protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
        byte[] lengthBytes = new byte[length];
        buf.getBytes(offset, lengthBytes);
        ByteBuffer wrapped = ByteBuffer.wrap(lengthBytes); // big-endian by default
        short messageLength = wrapped.getShort(); // 1

        //String s = new String(lengthBytes, CharsetUtil.US_ASCII);
        System.out.println("Length of message " + messageLength);
        return messageLength;
    }
	
}
