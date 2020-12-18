/**
 * 
 */
package espresso.handlers;

import java.util.Map;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;

import espresso.util.Util;

/**
 * @author deva
 *
 */
public class AuthAdviceHandler implements MessageHandler {

	private MessageFactory<IsoMessage> mfact;
	
	
	
	public AuthAdviceHandler(MessageFactory<IsoMessage> mfact) {
		this.mfact = mfact;
	}

	@Override
	public IsoMessage createMessage(Map<String, Object> messageParams) {
		IsoMessage isomsg = mfact.newMessage(0x100);
		isomsg.setValue(2, messageParams.get("cardnumber"), IsoType.LLVAR, 0);// 4757570163847923
		isomsg.setValue(3, "000000", IsoType.NUMERIC, 6);
		isomsg.setValue(4, "000000000500", IsoType.NUMERIC, 12);
		isomsg.setValue(7, Util.getDate10(), IsoType.DATE10, 10);
		isomsg.setValue(11, "000235", IsoType.NUMERIC, 6);
		isomsg.setValue(12, Util.getTime(), IsoType.TIME, 6);
		isomsg.setValue(13, Util.getDate4(), IsoType.DATE4, 4);
		isomsg.setValue(15, Util.getDate4(), IsoType.DATE4, 4);
		isomsg.setValue(17, Util.getDate4(), IsoType.DATE4, 4);
		
		
	
		
//		isomsg.setValue(18, "0000", IsoType.NUMERIC, 4);
//		isomsg.setValue(22, "900", IsoType.NUMERIC, 3);
//		isomsg.setValue(25, "00", IsoType.NUMERIC, 2);
//		isomsg.setValue(28, "C00000000", IsoType.ALPHA, 9);
		isomsg.setValue(32, "000020", IsoType.LLVAR, 0);
//		isomsg.setValue(33, "000020", IsoType.LLVAR, 0);
		isomsg.setValue(35, "5439954000844876=14072210000003755400", IsoType.LLVAR, 37);// 4593610089798970
		isomsg.setValue(37, "POM209000741", IsoType.ALPHA, 12);// POM209000235
		isomsg.setValue(41, "25623359", IsoType.ALPHA, 8);
//		isomsg.setValue(42, "000000000031906", IsoType.ALPHA, 15);
		isomsg.setValue(43, "25595570 Andy Mullen   Welwyn GardenHeGB", IsoType.ALPHA, 40);
		isomsg.setValue(49, "840", IsoType.ALPHA, 3);
//		isomsg.setValue(56, "1510", IsoType.LLLVAR, 4);
//		isomsg.setValue(59, "0051427189|0000|POM209000741", IsoType.LLLVAR, 28);
		return isomsg;
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
		if(isoMessage.getType() == 0x120 || isoMessage.getType() == 0x121) {
			return true;
		}
		return false;
	}

}
