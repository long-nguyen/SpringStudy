package main.java;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.DescribeGlobalResult;
import com.sforce.soap.enterprise.DescribeSObjectResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.Field;
import com.sforce.soap.enterprise.FieldType;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.PicklistEntry;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.Merchant__c;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class QuickStartSample {
	static final String TOKEN = "scYPIR9S2HtCBdv5gVO7CyVY";
	// For managing connection to Salesforce
	static EnterpriseConnection connection;
	String authEndPoint;
	private String username;
	private String password;
	
	public QuickStartSample(String username, String pass) {
		this.username = username;
		this.password = pass;
	}
	
	public void run() {
		if (login()) {
			//View all accessible objects
//			Util.describeAllObject(connection);
			//Check metadata of our Merchant object(custom object must have __c)
//			Util.describeSingleObject(connection, "Merchant__c");
//			Samples.queryContacts(connection);
//			Samples.createAccounts(connection);
//			Samples.updateAccounts(connection);
//			Samples.deleteAccounts(connection);
			
			queryMerchants();
			
			logout();	
		}
	}



	private boolean login() {
		try {
			ConnectorConfig config = new ConnectorConfig();
			config.setUsername(this.username);
			//Password must include token
			config.setPassword(this.password + TOKEN);
			// It will show the trace message(SOAP)
//			config.setTraceMessage(true);
			// This part is to login, need username and password
			connection = Connector.newConnection(config);

			GetUserInfoResult userInfo = connection.getUserInfo();
			if (userInfo == null) {
				throw new ConnectionException();
			}
			// display some current settings
			System.out.println("Auth EndPoint: " + config.getAuthEndpoint());
			System.out.println("Service EndPoint: " + config.getServiceEndpoint());
			System.out.println("Username: " + config.getUsername());
			System.out.println("Fullname: " + userInfo.getUserFullName());
			System.out.println("Email: " + userInfo.getUserEmail());
			System.out.println("SessionId: " + config.getSessionId());
			return true;
		} catch (ConnectionException e1) {
			e1.printStackTrace();
			return false;
		}
	}
	
	
	private void queryMerchants() {
		System.out.println("Querying Merchants:");

		try {
			// query for the 5 newest contacts
			QueryResult queryResults = connection
					.query("SELECT shop_code__c, Name FROM Merchant__c  ORDER BY CreatedDate DESC LIMIT 5");
			if (queryResults.getSize() > 0) {
				for (int i = 0; i < queryResults.getRecords().length; i++) {
					// cast the SObject to a strongly-typed Contact
					Merchant__c c = (Merchant__c) queryResults.getRecords()[i];
					System.out.println("shop_code: " + c.getShop_code__c() + " - Name: " + c.getName());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void logout() {
		try {
			connection.logout();
			System.out.println("Logged out");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
