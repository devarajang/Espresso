/**
 * 
 */
package espresso.handlers;

import java.util.Map;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;

/**
 * @author deva
 *
 */
public class ReversalMessageHandler implements MessageHandler {
	
	private MessageFactory<IsoMessage> mfact;
	
	public ReversalMessageHandler(MessageFactory<IsoMessage> mfact) {
		this.mfact = mfact;
	}

	@Override
	public IsoMessage createMessage(Map<String, Object> messageParams) {
		return null;
	}

	@Override
	public IsoMessage handleMessage(IsoMessage isoMessage) {
		IsoMessage response = mfact.newMessage(isoMessage.getType()+16);
		
		String responseFields = "2, 3, 4, 5, 6, 7, 9, 11, 12, 13, 15, 28, 29, 37, 38, 39, 44, 46, 49, 50, 53, 55, 58, 61, 63, 64, 128";
		String[] strings = responseFields.split(",");
		for (String string : strings) {
			int fieldNum = Integer.parseInt(string.trim());
			if(isoMessage.hasField(fieldNum)) {
				response.copyFieldsFrom(isoMessage, fieldNum);
			}
		}
		response.setValue(39, "00", IsoType.ALPHA, 2);

		System.out.println(response.debugString());
		return response;
	}

	@Override
	public boolean canHandle(IsoMessage isoMessage) {
		if(isoMessage.getType() == 0x420 || isoMessage.getType() == 0x421) {
			return true;
		}
		return false;
	}

}
