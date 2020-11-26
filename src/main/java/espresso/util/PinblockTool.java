/**
 * 
 */
package espresso.util;

import org.apache.commons.lang3.StringUtils;

import com.solab.iso8583.util.HexCodec;

/**
 * @author deva
 *
 */
public class PinblockTool {

	public static String encode(String pan, String pin) {

		String pinData = StringUtils.rightPad(StringUtils.leftPad(Integer.toString(pin.length()), 2, '0') + pin, 16,
				'F');
		String panData = StringUtils.leftPad(pan.substring(3, 15), 16, '0');

		final byte[] pinBytes = HexCodec.hexDecode(pinData);
		final byte[] panBytes = HexCodec.hexDecode(panData);

		final byte[] pinblock = new byte[8];
		for (int i = 0; i < 8; i++)
			pinblock[i] = (byte) (pinBytes[i] ^ panBytes[i]);
		return HexCodec.hexEncode(pinblock, 0, pinblock.length);
	}

	public static String decode(String pinblock, String pan) {
		String panData = StringUtils.leftPad(pan.substring(3, 15), 16, '0');
		byte[] bPan = HexCodec.hexDecode(panData);

		byte[] bPinBlock = HexCodec.hexDecode(pinblock);

		final byte[] bPin = new byte[8];
		for (int i = 0; i < 8; i++)
			bPin[i] = (byte) (bPinBlock[i] ^ bPan[i]);

		final String pinData = HexCodec.hexEncode(bPin,0,bPin.length);
		final int pinLen = Integer.parseInt(pinData.substring(0, 2));
		return pinData.substring(2, 2 + pinLen);
	}

	public static void main(String[] args) {
		String enc = encode("4941490022287657", "5678");
		System.out.println("PinblockTool.main() " + enc);
		System.out.println(decode(enc, "4941490022287657"));
	}
}
