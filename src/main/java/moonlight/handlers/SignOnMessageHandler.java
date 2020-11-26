/**
 * 
 */
package moonlight.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.impl.SimpleTraceGenerator;

import espresso.util.Util;

/**
 * @author deva
 *
 */
public class SignOnMessageHandler implements MessageHandler{
	
	private MessageFactory<IsoMessage> mfact;

	public SignOnMessageHandler(MessageFactory<IsoMessage> mfact) {
		this.mfact = mfact;
	}
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");

	@Override
	public IsoMessage handleMessage(IsoMessage isoMessage) {
		IsoMessage responseMessage = new IsoMessage();
		System.out.println("Field value = " + isoMessage.getField(70).getValue());
		responseMessage.setType(0x810);
		responseMessage.setValue(7, sdf.format(new Date()), IsoType.DATE10, 10);
		responseMessage.setValue(39,"00",IsoType.ALPHA,2);
		responseMessage.copyFieldsFrom(isoMessage, 11,70);
		Util.print(responseMessage);
		return responseMessage;
	}

	@Override
	public boolean canHandle(IsoMessage isoMessage) {
		
		if(isoMessage.getType() == 0x800) {
			return true;
		}
		return false;
	}

	@Override
	public IsoMessage createMessage(Map<String, Object> messageParams) {
		
		mfact.setAssignDate(true);
		mfact.setTraceNumberGenerator(new SimpleTraceGenerator((int) (System.currentTimeMillis() % 100000)));
		System.err.println("NEW MESSAGE");
		IsoMessage isomsg = mfact.newMessage(0x800);
		isomsg.setValue(7, Util.getDate10(), IsoType.DATE10, 10);
		isomsg.setValue(11, Util.getTime(), IsoType.TIME, 6);
		if(messageParams.get("securitycode") != null)
			isomsg.setValue(96, "00022020", IsoType.ALPHA, 8);
			isomsg.setValue(70, "161", IsoType.NUMERIC, 3);
			isomsg.setValue(125, "632D826A17939BA5CBA7A84BF771DCD51E7E", IsoType.LLLVAR, 36);
		if("SIGNON".equals(messageParams.get("msgtype")) ) {
			isomsg.setValue(70, "061", IsoType.NUMERIC, 3);
		}else if("NETADM".equals(messageParams.get("msgtype")) ) {
			isomsg.setValue(70, "301", IsoType.NUMERIC, 3);
//			isomsg.setValue(96, "", IsoType.BINARY, 0);
		} else {
			isomsg.setValue(70, "161", IsoType.NUMERIC, 3);
			isomsg.setValue(125, "632D826A17939BA5CBA7A84BF771DCD51E7E", IsoType.LLLVAR, 36);
		}
		
		Util.print(isomsg);
		return isomsg;
	}

}
