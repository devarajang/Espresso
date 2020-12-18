package espresso.controllers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.solab.iso8583.IsoMessage;

import espresso.iso.EspressoServer;
import espresso.models.PaymentCard;
import espresso.models.RequestResponseLog;

@RestController
public class HelloController {
	
	@Autowired
	EspressoServer server;

	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@PostMapping( value = "/auth", produces = "application/json" , consumes = "application/json")
	public IsoMessage auth(Map<String,Object> authData) {
		
		IsoMessage authMessage =  server.auth(authData);
		System.out.println(authMessage.debugString());
		return authMessage;
	}
	
	@PostMapping( value = "/keyex", produces = "application/json" , consumes = "application/json")
	public IsoMessage keyex( @RequestBody Map<String,Object> keyexData) {
		
		IsoMessage authMessage =  server.keyex(keyexData);
		System.out.println(authMessage.debugString());
		return authMessage;
	}
	
	@PostMapping( value = "/netadm", produces = "application/json" , consumes = "application/json")
	public IsoMessage netadm(Map<String,Object> netadmData) {
		
		netadmData.put("msgtype", "NETADM");
		IsoMessage authMessage =  server.netadm(netadmData);
		System.out.println(authMessage.debugString());
		return authMessage;
	}
	
	@PostMapping( value = "/log", produces = "application/json" , consumes = "application/json")
	public Set<RequestResponseLog> requestLog(@RequestBody Map<String,Object> authData) {
		return EspressoServer.request_log.get(authData.get("cardNumber"));
	}
	
	@PostMapping( value = "/samples", produces = "application/json" , consumes = "application/json")
	public Map<String, String> requestLog() {
		Map<String, String> cards = new HashMap<String, String>();
		cards.put("CardWithBal", "4941490130580613");
		cards.put("ErrorResponse", "5337540539641355");
		cards.put("Insufficient", "5337540539608537,53375405599426");
		cards.put("CorrectCVV", "949,121,353");
		cards.put("InCorrectCVV", "234,456,567");
		cards.put("CorrectZip5", "53076 , 54098, ");
		cards.put("CorrectZip9", "902012112, 987653113");
		return cards;
	}

}