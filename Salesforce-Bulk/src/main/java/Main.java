package main.java;

import java.io.IOException;

import com.sforce.async.AsyncApiException;
import com.sforce.ws.ConnectionException;

//https://www.salesforce.com/us/developer/docs/api_asynch/
public class Main {
	
	private static final String USERNAME = "long.nguyen.rak@gmail.com";
	private static final String PASSWORD = "Wolverine88";
	static final String TOKEN = "scYPIR9S2HtCBdv5gVO7CyVY";

	public static void main(String[] args) throws ConnectionException, AsyncApiException, IOException {
//		InsertSample sample = new InsertSample();
//		sample.run(USERNAME, PASSWORD + TOKEN);
		QuerySample sample = new QuerySample();
		sample.run(USERNAME, PASSWORD + TOKEN);
	}
}
