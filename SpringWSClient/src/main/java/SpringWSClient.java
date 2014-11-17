package main.java;

import main.java.wsdl.ObjectFactory;
import main.java.wsdl.SayHello;
import main.java.wsdl.SayHelloResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component("springWSClient")
public class SpringWSClient {

	private static final ObjectFactory WS_CLIENT_FACTORY = new ObjectFactory();

	 @Autowired
	private WebServiceTemplate webServiceTemplate;

	@Autowired
	public SpringWSClient(WebServiceTemplate webServiceTemplate) {
		this.webServiceTemplate = webServiceTemplate;
	}


	public String sayHello(String who) {
		SayHello request = WS_CLIENT_FACTORY.createSayHello();
		request.setName(WS_CLIENT_FACTORY.createSayHelloName(who));
		SayHelloResponse response = (SayHelloResponse) webServiceTemplate.marshalSendAndReceive(request);
		return response.getReturn().getValue();
	}

}
