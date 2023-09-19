package com.function;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;


/**
 * Azure Functions with Timer trigger.
 */
public class TimerTriggerSample {
    
    private static final String CONNECTION_NAME="AzureWebJobsStorage";
    private static final String CSV= ".json";

    /**
     * This function will be invoked periodically according to the specified schedule.
     */
    @FunctionName("TimerTriggerSample")
    public void run(
        @TimerTrigger(name = "TimerTriggerSample", schedule = "0 */5 * * * *") String timerInfo,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now());

        String inputString = "Hello World!";
         byte[] byteArrray = inputString.getBytes();
         try{
            this.uploadBlob("invoice-bucket", "abc", byteArrray); 
         }catch(Exception exception) {
            exception.printStackTrace();
            exception.getMessage();
         }  
       
    }
      public void uploadBlob(String containerName, String fileName, byte[] content) throws IOException {
        BlobContainerClient container= this.getContainerReference(containerName);
        BlobClient blob= container.getBlobClient(fileName+CSV);
        InputStream inputContent= new ByteArrayInputStream(content);
        blob.upload(inputContent, inputContent.available());
    }
        /**
     * To get target container reference object
     * 
     * @param destinationContainerName     Target container name to copy blobs from source container
     * @return                              BlobContainerClient object of the target container
     */
    public BlobContainerClient getContainerReference(String destinationContainerName) {
        String connectionString= System.getenv(CONNECTION_NAME);
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        BlobContainerClient containerReference= blobServiceClient.getBlobContainerClient(destinationContainerName);
        return containerReference;
    }

}
