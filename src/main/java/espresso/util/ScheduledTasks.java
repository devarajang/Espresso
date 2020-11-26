package espresso.util;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.MessageFactory;
import com.solab.iso8583.parse.ConfigParser;

import espresso.handlers.SignOnMessageHandler;
import espresso.iso.EspressoServer;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private SignOnMessageHandler messageHandler;
	
	private Map<String, Object> messageParams = new HashMap<String, Object>();
	
	public ScheduledTasks() {
		MessageFactory<IsoMessage> mfact;
		try {
			mfact = ConfigParser.createFromClasspathConfig("j8583-config.xml");
			messageHandler = new SignOnMessageHandler(mfact);
			messageParams.put("msgtype", "NETADM");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Scheduled(fixedRate = 30000)
	public void reportCurrentTime() {
		
		log.info("The time is now {}", dateFormat.format(new Date()));
		EspressoServer.channels.forEach(channel -> {
			IsoMessage isoMessage = messageHandler.createMessage(messageParams);
			System.out.println("Channel Id " + channel.id());
			channel.writeAndFlush(isoMessage);
		
			}
		);
		
	}
}