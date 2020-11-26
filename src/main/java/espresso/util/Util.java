/**
 * 
 */
package espresso.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.solab.iso8583.IsoMessage;

/**
 * @author deva
 *
 */
public class Util {
	
	private static SimpleDateFormat date10 = new SimpleDateFormat("MMddHHmmss");
	private static SimpleDateFormat time = new SimpleDateFormat("HHmmss");
	private static SimpleDateFormat date4 = new SimpleDateFormat("MMdd");

	public static void print(IsoMessage m) {
		System.out.printf("TYPE: %04x\n", m.getType());
		for (int i = 2; i < 128; i++) {
			if (m.hasField(i)) {
				System.out.printf("F %3d(%s): %s -> '%s'\n", i, m.getField(i).getType(), m.getObjectValue(i),
						m.getField(i).toString());
			}
		}
	}
	
	public static String getDate10() {
		return date10.format(new Date());
	}

	public static String getTime() {
		return time.format(new Date());
	}

	public static String getDate4() {
		return date4.format(new Date());
	}
}
