/**
 * 
 */
package espresso.iso;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.impl.SimpleTraceGenerator;
import com.solab.iso8583.parse.ConfigParser;

import espresso.util.Util;

/**
 * @author deva
 *
 */
public class MessageTest {

	public static void print(IsoMessage m) {
		System.out.printf("TYPE: %04x\n", m.getType());
		for (int i = 2; i < 128; i++) {
			if (m.hasField(i)) {
				System.out.printf("F %3d(%s): %s -> '%s'\n", i, m.getField(i).getType(), m.getObjectValue(i),
						m.getField(i).toString());
			}
		}
	}
	
	public static IsoMessage createKeyEx() throws Exception {
		MessageFactory<IsoMessage> mfact = ConfigParser.createFromClasspathConfig("j8583-config.xml");
		IsoMessage isomsg = mfact.newMessage(0x800);
		isomsg.setValue(7, Util.getDate10(), IsoType.DATE10, 10);
		isomsg.setValue(11, Util.getTime(), IsoType.TIME, 6);
		isomsg.setValue(96, "00022020", IsoType.ALPHA, 8);
		isomsg.setValue(70, "161", IsoType.NUMERIC, 3);
		isomsg.setValue(125, "632D826A17939BA5CBA7A84BF771DCD51E7E", IsoType.LLLVAR, 36);
		return isomsg;
	}
	
	public static IsoMessage parseSignOnMessage(String message) throws Exception {
		MessageFactory<IsoMessage> mfact = ConfigParser.createFromClasspathConfig("j8583-config.xml");
		mfact.setAssignDate(true);
		return mfact.parseMessage(message.getBytes(), 0);
	}
	
	public static void main_cvv(String[] args) {
		String authData = "TDAV09918461063CV0711 949M";
		int avIndex = authData.indexOf("AV");
		// TDAV1171846    23CV0711 949M
		String avsResult = "U";
		if(avIndex > 0 ) {
			int avsLength = Integer.parseInt(authData.substring( avIndex+ 2, avIndex + 4));
			String avsData = authData.substring(avIndex+4, avIndex + 4 + avsLength);
			String zipcode = null;
			String addressLine1 = null;
			if(avsData.length() > 9) {
				zipcode = avsData.substring(0,9).trim();
				addressLine1 = avsData.substring(9).trim();
			} else {
				zipcode = avsData.trim();					
			}
			
			boolean zip9Match = false;
			boolean zip5Match = false;
			boolean addressMatch = false;
			if(zipcode != null && zipcode.length() >=9  && zipcode.startsWith("9")) 
			{
				zip9Match = true;
			}
			if(zipcode != null && zipcode.length() <9  && zipcode.startsWith("5")) 
			{
				zip5Match = true;
			}
			if(addressLine1 != null && addressLine1.length() > 3 && addressLine1.startsWith("106")) {
				addressMatch = true;
			}
			if(zip9Match && addressMatch) {
				avsResult ="X";
			} else if(zip5Match && addressMatch) {
				avsResult ="Y";
			} else if (zip9Match) {
				avsResult ="W";
			} else if (zip5Match) {
				avsResult ="Z";
			} else if (addressMatch){
				avsResult ="A";
			} else {
				avsResult ="N";
			}
			avsResult = "AR01" + avsResult;
		} else {
			avsResult = "AR01U";
		}
		
		int cvIndex = authData.indexOf("CV");
		String cvvResult = "CR01P";
		if(cvIndex > 0) {
			int cvvLength = Integer.parseInt(authData.substring( cvIndex+ 2, cvIndex + 4));
			String cvvIndicator = null;
			String cvvRespType = null;
			String cvvData = null;
			if(cvvLength >= 1)
				cvvIndicator = authData.substring(cvIndex+4, cvIndex + 4 + 1);			
			if(cvvLength >=2)
				cvvRespType = authData.substring(cvIndex+4+1, cvIndex + 4 + 2);
			if(cvvLength >= 3 && cvvLength <=6)
				cvvData = authData.substring(cvIndex+4+2, authData.length());
			if (cvvLength == 7) 
				cvvData = authData.substring(cvIndex+4+2, authData.length()-1);
			System.out.println(cvvData + " " + cvvIndicator + "  " + cvvRespType);
			if(cvvIndicator !=null ) {
				if(cvvData != null) {
					cvvData = cvvData.trim();
					if(cvvData.equals(StringUtils.reverse(cvvData))) {
						cvvResult = "CR01M";
					} else {
						cvvResult = "CR01N";
					}
				} else {
					cvvResult = "CR01N";
				}
			}
			
		}
		System.out.println("avsResult " + avsResult);
		System.out.println("cvvResult " + cvvResult);
		
	}
	
	public static void main(String[] args) throws Exception {
	// 0100	//IsoMessage message = parseSignOnMessage("0100F23A4401A8E190E2000000000000001016494149002228765700000000000000990005211348500026021348500521052130009011010000005891111111111111374941490022287657=230510100000155000000000000017151       1              1005 convention plaza  st louis     MOUS021MERCHANT NAME@URL.COM840481958CA4F58876E0032040111010000A25101729000         840022B2580001PULINT058  0  000");
	// 0120	//IsoMessage message = parseSignOnMessage("0120F23A4401AAE180F2000000000000001016494149002228765700000000000000990005211348500026021348500521052130009011010000005891111111111111374941490022287657=23051010000015500000000000001715911       1              1005 convention plaza  st louis     MOUS021MERCHANT NAME@URL.COM8400032040111010000A25101729000         8400044031022B2580001PULINT058     000");
	// 0121	//IsoMessage message = parseSignOnMessage("0121F23A4401AAE18072000000000000003016494149002228765700000000000000000005211346560026011346560521052159629021010000005891111111111111374941490022287657=23051010000015500000000000001714911       1              1005 convention plaza  st louis     MOUS021MERCHANT NAME@URL.COM8400111000008001101729000         8400044031022B2580001PPEINT058     036TDAV30627006   St Pa               R060ND50PS46TR161234567890123456FC03987NR15654321012345   PD0202");
	// 0200 //IsoMessage message = parseSignOnMessage("0200F23A4401A8E19062000000000000001016494149002228765700200000000000600005211318460025951318460521052160119011010000005891111111111111374941490022287657=230510100000155000000000000017081       1              1005 convention plaza  st louis     MOUS013MERCHANT NAME840481958CA4F58876E0110000000002201729000         840022B2580001PULINT058  0  000");
	// 0220  //IsoMessage message = parseSignOnMessage("0220F23A4401AAE18072000000000000001016494149002228765700200000000000600005211318460025951318460521052160119011010000005891111111111111374941490022287657=23051010000015500000000000001708911       1              1005 convention plaza  st louis     MOUS013MERCHANT NAME8400110000000002201729000         8400044031022B2580001PULINT058     000");
		IsoMessage message = parseSignOnMessage("0100F23E440108E1806200000000000000321649414901305806130000000000000026001123233632851759183632112322111123654006010100000136803282385175904770626000350200043882WALMART EGIFT CARD     BENTONVILLE  ARUS018WALMART EGIFT CARD8400111012100725001705000303030000840022B2IN0136VNTINT136  0  028TDAV1171846    23CV0711 949M038PR29V0010013820328849447623085841PI0100604851759405523     032823851759W380328849923671      00100591");
		System.out.println(message.debugString());
		print(message);
		
		
		
	}

	public static IsoMessage getSignonMessage() throws Exception {
		MessageFactory<IsoMessage> mfact = ConfigParser.createFromClasspathConfig("j8583-config.xml");
		mfact.setAssignDate(true);
		mfact.setTraceNumberGenerator(new SimpleTraceGenerator((int) (System.currentTimeMillis() % 100000)));
		System.err.println("NEW MESSAGE");
		IsoMessage isomsg = mfact.newMessage(0x800);
		isomsg.setValue(7, "0516160450", IsoType.DATE10, 10);
		isomsg.setValue(11, "160450", IsoType.TIME, 6);
		isomsg.setValue(70, "061", IsoType.NUMERIC, 3);
		isomsg.setValue(96, "022020", IsoType.LLBIN, 8);
		print(isomsg);
		return isomsg;
//		System.out.println(isomsg.debugString());
//		System.out.println(new String(isomsg.writeData()));
	}
	public static IsoMessage getAuthMessage() throws Exception {
		MessageFactory<IsoMessage> mfact = ConfigParser.createFromClasspathConfig("j8583-config.xml");
		mfact.setAssignDate(true);
		mfact.setTraceNumberGenerator(new SimpleTraceGenerator((int) (System.currentTimeMillis() % 100000)));

		// Create a new message
		System.err.println("NEW MESSAGE");
		IsoMessage isomsg = mfact.newMessage(0x200);
		isomsg.setValue(2, "5439954000844876", IsoType.LLVAR, 0);// 4757570163847923
		isomsg.setValue(3, "000000", IsoType.NUMERIC, 6);
		isomsg.setValue(4, "000000000500", IsoType.NUMERIC, 12);
		isomsg.setValue(7, "0728153654", IsoType.DATE10, 10);
		isomsg.setValue(11, "000235", IsoType.NUMERIC, 6);
		isomsg.setValue(12, "163654", IsoType.TIME, 6);
		isomsg.setValue(13, "0728", IsoType.DATE4, 4);
		isomsg.setValue(15, "1407", IsoType.DATE4, 4);
		isomsg.setValue(17, "0728", IsoType.DATE4, 4);
		
		
	
		
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
		isomsg.setValue(49, "826", IsoType.ALPHA, 3);
//		isomsg.setValue(56, "1510", IsoType.LLLVAR, 4);
//		isomsg.setValue(59, "0051427189|0000|POM209000741", IsoType.LLLVAR, 28);
		return isomsg;
	}
	
	public static void main41(String[] args) throws Exception {
		
		IsoMessage isomsg = getSignonMessage();

		print(isomsg);
		//System.out.println(new String(isomsg.writeData()));
		byte[] message = isomsg.writeData();
//		IsoMessage parseMessage = mfact.parseMessage(message, 0);
		
		 SSLContext sslContext = SSLContext.getInstance("TLS");

		// set up a TrustManager that trusts everything
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
		      public X509Certificate[] getAcceptedIssuers() {
		          System.out.println("getAcceptedIssuers =============");
		          return null;
		      }

		      public void checkClientTrusted(X509Certificate[] certs,
		              String authType) {
		          System.out.println("checkClientTrusted =============");
		      }

		      public void checkServerTrusted(X509Certificate[] certs,
		              String authType) {
		          System.out.println("checkServerTrusted =============");
		      }
		} }, new SecureRandom());

		SSLSocketFactory factory = sslContext.getSocketFactory();
		
		SSLSocket socket =
                (SSLSocket)factory.createSocket("localhost", 3030);
		socket.startHandshake();
		
		
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
		byte[] lengthBytes = ByteBuffer.allocate(2).putShort((short) message.length).array();
		System.out.println("Message Length "  + message.length);
//		StringBuilder sb = new StringBuilder();
//		sb.append((char)lengthBytes[0]);
//		sb.append((char)lengthBytes[1]);
		dOut.write(lengthBytes);
        dOut.write(message);

		dOut.flush();
		InputStream is = socket.getInputStream();
		byte[] buffer = new byte[1024];
	    int read;
	    while((read = is.read(buffer)) != -1) {
	        String output = new String(buffer, 0, read);
	        System.out.print(output);
	        System.out.flush();
	    };

		is.close();
		
		dOut.close();
		socket.close();
	}
}
