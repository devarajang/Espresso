/**
 * 
 */
package espresso.handlers;

import org.apache.commons.lang3.StringUtils;

/**
 * @author deva
 *
 */
public abstract class TxnMessageHandler implements MessageHandler {

	public String getAVSResult(String authData) {
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
		return avsResult;
	}
	
	public String getCVVResult(String authData) {
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
		return cvvResult;
		
	}

}
