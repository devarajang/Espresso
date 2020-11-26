/**
 * 
 */
package espresso.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;

import moonlight.handlers.FinancialMessageHandler;
import moonlight.handlers.MessageHandler;
import moonlight.handlers.SignOnMessageHandler;

/**
 * @author deva
 *
 */
public class MessageHandlerUtil {
	
	private Map<Integer, MessageHandler> isoMessageHandlers = new HashMap<Integer, MessageHandler>();
	
	public MessageHandlerUtil() throws IOException {
		MessageFactory<IsoMessage> mfact = ConfigParser.createFromClasspathConfig("j8583-config.xml");
		isoMessageHandlers.put(0x800, new SignOnMessageHandler(mfact));
		isoMessageHandlers.put(0x200, new FinancialMessageHandler(mfact));
		isoMessageHandlers.put(0x201, new FinancialMessageHandler(mfact));
	}
	
	public MessageHandler getByType(int type) {
		return isoMessageHandlers.get(type);
	}

	public  Collection<MessageHandler> getAll() {
		return isoMessageHandlers.values();
	}

}
