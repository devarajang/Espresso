/**
 * 
 */
package moonlight.handlers;

import java.util.Map;

import com.solab.iso8583.IsoMessage;

/**
 * @author deva
 *
 */
public interface MessageHandler {
	
	public IsoMessage createMessage(Map<String,Object> messageParams);

	public IsoMessage handleMessage(IsoMessage isoMessage);
	
	public boolean canHandle(IsoMessage isoMessage);
}
