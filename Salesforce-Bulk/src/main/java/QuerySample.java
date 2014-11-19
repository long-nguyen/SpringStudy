package main.java;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BatchStateEnum;
import com.sforce.async.BulkConnection;
import com.sforce.async.ConcurrencyMode;
import com.sforce.async.ContentType;
import com.sforce.async.JobInfo;
import com.sforce.async.OperationEnum;
import com.sforce.async.QueryResultList;
import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * This sample demonstrates how to query salesforce data 
 * Query data into CSV file
 * Maximum: 15GB size --> divided into 15 file. 
 * Start query -> server add to batch queue --> response csv.
 * Query result is different API(a location of result CSV file)
 * @author long.nguyen-tien
 *
 */

//TODO: WIth the error Caused by: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
//http://www.mkyong.com/webservices/jax-ws/suncertpathbuilderexception-unable-to-find-valid-certification-path-to-requested-target/
//http://salesforce.stackexchange.com/questions/5603/why-do-i-get-pkix-path-building-failed-exception-with-my-callout
public class QuerySample {

	//https://www.salesforce.com/us/developer/docs/api_asynch/
	// Bulk Query details
	
	private BulkConnection bulkConnection;
	EnterpriseConnection connection;
	
	public void run(String username, String password) throws ConnectionException, AsyncApiException, IOException {
		doBulkQuery(username, password);
		connection.logout();
        System.out.println("Done all"); 
	}
	
	private boolean login(String username, String password) {
		try {
			//We need partner connection first to detect the service endpoint, config for bulk connection(Login step)
	        ConnectorConfig enterpriseConfig = new ConnectorConfig();
	        enterpriseConfig.setUsername(username);
	        enterpriseConfig.setPassword(password);
	        connection = Connector.newConnection(enterpriseConfig);
	        
	        //Create bulk connection
	        ConnectorConfig config = new ConnectorConfig();
	        config.setSessionId(enterpriseConfig.getSessionId());
	        String soapEndpoint = enterpriseConfig.getServiceEndpoint();
	        System.out.println(soapEndpoint);
	        String apiVersion = "32.0";
	        String restEndpoint = soapEndpoint.substring(0, soapEndpoint.indexOf("Soap"))+"async/"+apiVersion;
	        //Format of endpoint:Web_Services_SOAP_endpoint_instance_name/services/async/APIversion/Resource_address
	
	        config.setRestEndpoint(restEndpoint);
	        //TODO: When run in release mode, shoud set Compression is true 
	        config.setCompression(false);
	        config.setTraceMessage(true);
	        //TODO: Update this trace file location
	        config.setTraceFile("src/main/resources/trace.txt");
	        config.setPrettyPrintXml(true);
	        bulkConnection = new BulkConnection(config);
	        return true;
        } catch (ConnectionException | FileNotFoundException | AsyncApiException e) {
			e.printStackTrace();
		}
        return false;
	}
	
	public void doBulkQuery(String username, String password) {
	    if (!login(username, password)) {
	    	return;
	    }
	    try {
	    	JobInfo job = new JobInfo();
			job.setObject("Merchant__c");

			job.setOperation(OperationEnum.query);
			job.setConcurrencyMode(ConcurrencyMode.Parallel);
			job.setContentType(ContentType.CSV);

			job = bulkConnection.createJob(job);
			assert job.getId() != null;

			job = bulkConnection.getJobStatus(job.getId());

			String query = "SELECT Name, shop_code__c FROM Merchant__c";

			//Start query(send to server)--------------------------------
			long start = System.currentTimeMillis();
			BatchInfo info = null;
			ByteArrayInputStream bout = new ByteArrayInputStream(query.getBytes());
			info = bulkConnection.createBatchFromStream(job, bout);
			
			//Start checking result(sleep, polling)------------------------------
			String[] queryResults = null;

			for (int i = 0; i < 10000; i++) {
				Thread.sleep(i == 0 ? 30 * 1000 : 30 * 1000); // 30 sec(
				info = bulkConnection.getBatchInfo(job.getId(), info.getId());

				if (info.getState() == BatchStateEnum.Completed) {
					QueryResultList list = bulkConnection.getQueryResultList(job.getId(), info.getId());
					queryResults = list.getResult();
					break;
				} else if (info.getState() == BatchStateEnum.Failed) {
					System.out.println("-------------- failed ----------" + info);
					break;
				} else {
					System.out.println("-------------- waiting ----------" + info);
				}
			}
			//Got result-----------------------------------------------------------------
			if (queryResults != null) {
				//NOTE: print this result to file
				InputStream ip;
				OutputStream op = new FileOutputStream("src/main/resources/result.csv");
				byte[] buffer = new byte[8 * 1024];
				for (String resultId : queryResults) {
					ip = bulkConnection.getQueryResultStream(job.getId(), info.getId(), resultId);
					ip.read(buffer);
					op.write(buffer);
					ip.close();
				}
				op.close();
			}
		} catch (AsyncApiException aae) {
			aae.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
