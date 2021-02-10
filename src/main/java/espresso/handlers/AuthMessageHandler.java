/**
 * 
 */
package espresso.handlers;

import java.util.Map;
import java.util.Random;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;

import espresso.util.Util;

/**
 * @author deva
 *
 */
public class AuthMessageHandler extends TxnMessageHandler {

	private MessageFactory<IsoMessage> mfact;
	
	
	
	public AuthMessageHandler(MessageFactory<IsoMessage> mfact) {
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
		
		String responseFields = "2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 18, 19, 20, 21, 22, 23, 25, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,  45, 46, 47, 48, 49, 50, 51, 53, 54, 55,57, 58, 59,60, 61, 63, 67, 98, 100, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 113,  120,  122, 123, 124, 125, 126, 127, 128";
		String[] strings = responseFields.split(",");
		for (String string : strings) {
			int fieldNum = Integer.parseInt(string.trim());
			if(isoMessage.hasField(fieldNum)) {
				response.copyFieldsFrom(isoMessage, fieldNum);
			}
		}
//		Random random = new Random();
//		int rand = random.nextInt(10);
		String cardNumber = isoMessage.getField(2).toString();
		int rand = Integer.parseInt(""+cardNumber.charAt(15));
		
		Random rand1 = new Random();
		int approval_code = rand1.nextInt(89999) + 100000;
		response.setValue(38, Integer.toString(approval_code) , IsoType.ALPHA, 6);
		
		switch(rand) {
			case 0:
			case 1:
			case 2:
			case 3:
				response.setValue(39, "00", IsoType.ALPHA, 2);
				break;
			case 4:
			case 5:
				response.setValue(39, "06", IsoType.ALPHA, 2);
                break;
            case 6:            	
            case 7:
            	response.setValue(39, "51", IsoType.ALPHA, 2);
                break;
            case 8:
            	response.setValue(39, "59", IsoType.ALPHA, 2);
                break;
            case 9:
            	response.setValue(39, "96", IsoType.ALPHA, 2);
                break;
		
		}
		
		// 0100F23E440108E1806200000000000000321649414901305806130000000000000026001123233632851759183632112322111123654006010100000136803282385175904770626000350200043882WALMART EGIFT CARD     BENTONVILLE  ARUS018WALMART EGIFT CARD8400111012100725001705000303030000840022B2IN0136VNTINT136  0  028TDAV1171846    23CV0711 949M038PR29V0010013820328849447623085841PI0100604851759405523     032823851759W380328849923671      00100591
		
		if(isoMessage.hasField(123)) {
			String authData = isoMessage.getField(123).toString();
			String avsResult = getAVSResult(authData);
			String cvvResult = getCVVResult(authData);
			String tdResult = "TD" + avsResult + cvvResult;
			response.setValue(123, tdResult, IsoType.LLLVAR, tdResult.length());
		}
		

		System.out.println(response.debugString());
		return response;
	}

	@Override
	public boolean canHandle(IsoMessage isoMessage) {
		if(isoMessage.getType() == 0x100 || isoMessage.getType() == 0x101) {
			return true;
		}
		return false;
	}

}
