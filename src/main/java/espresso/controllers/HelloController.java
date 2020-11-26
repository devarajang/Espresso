package espresso.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.solab.iso8583.IsoMessage;

import espresso.iso.EspressoServer;

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

}