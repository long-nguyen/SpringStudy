package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BatchInfo;
import com.sforce.async.BatchStateEnum;
import com.sforce.async.BulkConnection;
import com.sforce.async.CSVReader;
import com.sforce.async.ContentType;
import com.sforce.async.JobInfo;
import com.sforce.async.JobStateEnum;
import com.sforce.async.OperationEnum;
import com.sforce.soap.enterprise.Connector;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * This sample demonstrates how to insert data to saleforce using BULK API.
 * Maximum 5000 batch/job.
 * 10000 rows/batch
 * @author long.nguyen-tien
 *
 */
public class InsertSample {
	//https://www.salesforce.com/us/developer/docs/api_asynch/
    public void run(String username, String password) throws ConnectionException, AsyncApiException, IOException {
        BulkConnection connection = getBulkConnection(username, password);
        //Create a job for Merchant object. It is a custom object so it need __c
	    JobInfo job = createJob("Merchant__c", connection);
	    //Send batch data to server, got array of batch info
	    List<BatchInfo> batchInfoList = createBatchesFromCSVFile(connection, job,
	        "src/main/resources/MerchantList.csv");
	    //Close job, all neccessary batch are added to server
	    closeJob(connection, job.getId());
	    //Start monitoring batch process 
	    awaitCompletion(connection, job, batchInfoList);
	    //finally get result
	    checkResults(connection, job, batchInfoList);
	    System.out.println("Done all"); 
    }
    
    /**
     * Login then create Bulk connection
     * @param userName
     * @param password
     * @return
     * @throws ConnectionException
     * @throws AsyncApiException
     */
    private BulkConnection getBulkConnection(String userName, String password) throws ConnectionException, AsyncApiException {
        //We need partner connection first to detect the service endpoint, config for bulk connection(Login step)
        ConnectorConfig enterpriseConfig = new ConnectorConfig();
        enterpriseConfig.setUsername(userName);
        enterpriseConfig.setPassword(password);
        Connector.newConnection(enterpriseConfig);
        
        //Create bulk connection
        ConnectorConfig config = new ConnectorConfig();
        config.setSessionId(enterpriseConfig.getSessionId());
        String soapEndpoint = enterpriseConfig.getServiceEndpoint();
        System.out.println(soapEndpoint);
        String apiVersion = "32.0";
        String restEndpoint = soapEndpoint.substring(0, soapEndpoint.indexOf("Soap"))+"async/"+apiVersion;
        //Format of endpoint:Web_Services_SOAP_endpoint_instance_name/services/async/APIversion/Resource_address

        config.setRestEndpoint(restEndpoint);
        config.setCompression(false);
        config.setTraceMessage(true);
        BulkConnection connection = new BulkConnection(config);
        return connection;
    }

    
    /**
     * Create a job, which content info about:
     * + What action to be made: insert, update..
     * + What type of object to be executed on: Merchant...
     * + what conent type: CSV, XML
     * @param sObjectType
     * @param connection
     * @return
     * @throws AsyncApiException
     */
    private JobInfo createJob(String sObjectType, BulkConnection connection) throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setObject(sObjectType);
        //the job can insert, update,delete...
        //With insert, you dont need record's id, just data
        //With update, you need record's id
        //With delete, you need only id
        //with Upsert, you need external id
        job.setOperation(OperationEnum.insert);
        //Any data sending using bulk must in csv or xml
        job.setContentType(ContentType.CSV);
        job = connection.createJob(job);
        return job;
    }
    
    /**
     * Divide a big csv file into smaller file, each file will generate a batch
     * @param connection
     * @param jobInfo
     * @param csvFileName
     * @return
     * @throws IOException
     * @throws AsyncApiException
     */
    private List<BatchInfo> createBatchesFromCSVFile(BulkConnection connection, JobInfo jobInfo, String csvFileName) throws IOException, AsyncApiException {
        //Final batch info(batch status) array
        List<BatchInfo> batchInfos = new ArrayList<BatchInfo>();
        //We need a reader to read file
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName)));
        //Read CSV file header
        byte[] headerBytes = (reader.readLine() + "\n").getBytes();
        int headerByteLength = headerBytes.length;
        File tmpFile = File.createTempFile("bulkAPIInsert", ".csv");
        //Split the CSV file into multiple csv file, all include the same header. Each sub-csv file is equivalent to a batch
        try {
                FileOutputStream tmpOut = new FileOutputStream(tmpFile);
                int maxBytesPerBatch = 10000000;
                int maxRowsPerBatch = 10000; 
                int currentByte = 0;
                int currentLine = 0;
                String nextLine;
                while((nextLine = reader.readLine()) != null) {
                        byte []bytes = (nextLine + "\n").getBytes("UTF-8");
                        if (currentByte + bytes.length > maxBytesPerBatch || currentLine > maxRowsPerBatch) {
                                //When finish creating a csv file, let add file content to batch, send it to server to be queued
                                createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
                                currentByte = 0;
                                currentLine = 0;
                        }
                        if (currentByte == 0) {
                                //Start new csv file
                                tmpOut = new FileOutputStream(tmpFile);
                                tmpOut.write(headerBytes);
                                currentByte = headerByteLength;
                                currentLine = 1;
                        }
                        tmpOut.write(bytes);
                        currentByte += bytes.length;
                        currentLine ++;
                }
                if (currentLine > 1) {
                        //Finally, if we still have data, let send it
                        createBatch(tmpOut, tmpFile, batchInfos, connection, jobInfo);
                }
        } finally{
                reader.close();
                tmpFile.delete();
                System.out.println("Done create batch. Send all files content to server to be queued");
        }
            
        return batchInfos;
    }

    /**
     * Create a batch from stream(file)
     * @param tmpOut
     * @param tmpFile
     * @param batchInfos
     * @param connection
     * @param jobInfo
     * @throws IOException
     * @throws AsyncApiException
     */
    private void createBatch(FileOutputStream tmpOut, File tmpFile, List<BatchInfo>batchInfos, BulkConnection connection, JobInfo jobInfo) throws IOException, AsyncApiException {
        tmpOut.flush();
        tmpOut.close();
        FileInputStream tmpInputStream = new FileInputStream(tmpFile);
        try {
                BatchInfo batchInfo = connection.createBatchFromStream(jobInfo, tmpInputStream);
                System.out.println(batchInfo);
                batchInfos.add(batchInfo);
        } finally {
                tmpInputStream.close();
        }
    }
    
    /**
     * Close the job when finish adding all batches
     * @param connection
     * @param jobId
     * @throws AsyncApiException
     */
    private void closeJob(BulkConnection connection, String jobId) throws AsyncApiException {
        JobInfo job = new JobInfo();
        job.setId(jobId);
        job.setState(JobStateEnum.Closed);
        connection.updateJob(job);
    }
    
    /**
 * Wait for a job to complete by polling the Bulk API.
 * We call bulk api to query the status of our batch 
 * @param connection
 *            BulkConnection used to check results.
 * @param job
 *            The job awaiting completion.
 * @param batchInfoList
 *            List of batches for this job.
 * @throws AsyncApiException
 */
private void awaitCompletion(BulkConnection connection, JobInfo job, List<BatchInfo> batchInfoList) throws AsyncApiException {
    long sleepTime = 0L;
    Set<String> incomplete = new HashSet<String>();
    for (BatchInfo bi : batchInfoList) {
        incomplete.add(bi.getId());
    }
    while (!incomplete.isEmpty()) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {}
        System.out.println("Awaiting results..." + incomplete.size());
        sleepTime = 10000L;
        BatchInfo[] statusList =
          connection.getBatchInfoList(job.getId()).getBatchInfo();
        for (BatchInfo b : statusList) {
            if (b.getState() == BatchStateEnum.Completed
              || b.getState() == BatchStateEnum.Failed) {
                if (incomplete.remove(b.getId())) {
                    System.out.println("BATCH STATUS:\n" + b);
                }
            }
        }
    }
}

    
    /**
     * Gets the results of the operation and checks for errors.
     * Final result will be a csv with format:
     * + batch_id, created, success, failed
     * --> To know which batch is failed to restart it 
     */
    private void checkResults(BulkConnection connection, JobInfo job, List<BatchInfo> batchInfoList) throws AsyncApiException, IOException {
        // batchInfoList was populated when batches were created and submitted
        for (BatchInfo b : batchInfoList) {
            CSVReader rdr = new CSVReader(connection.getBatchResultStream(job.getId(), b.getId()));
            List<String> resultHeader = rdr.nextRecord();
            int resultCols = resultHeader.size();

            List<String> row;
            while ((row = rdr.nextRecord()) != null) {
                Map<String, String> resultInfo = new HashMap<String, String>();
                for (int i = 0; i < resultCols; i++) {
                    resultInfo.put(resultHeader.get(i), row.get(i));
                }
                boolean success = Boolean.valueOf(resultInfo.get("Success"));
                boolean created = Boolean.valueOf(resultInfo.get("Created"));
                String id = resultInfo.get("Id");
                String error = resultInfo.get("Error");
                if (success && created) {
                    System.out.println("Created row with id " + id);
                } else if (!success) {
                    System.out.println("Failed with error: " + error);
                }
            }
        }
    }
}
