package com.malachi;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.OutputBinding;

import com.google.gson.*;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
    /**
     * This function listens at endpoint "/api/GetCVPageCount". Invoke it using
     * "curl" command in bash: 1. curl {your host}/api/GetCVPageCount
     * 
     * http://localhost:7071/api/GetCVPageCount?id=count
     */
    @FunctionName("GetCVPageCount")
    public static HttpResponseMessage getCVPageCount(@HttpTrigger(name = "req", methods = {
            HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            @CosmosDBInput(name = "pagecounterdb", databaseName = "pagecounterdb", 
            collectionName = "pagecounterdbcontainer", id = "{Query.id}", partitionKey = "{Query.id}", 
            connectionStringSetting = "CosmosDbConnectionString") Optional<String> item,
            @CosmosDBOutput(name = "pagecounterdbupdate", databaseName = "pagecounterdb", 
            collectionName = "pagecounterdbcontainer", connectionStringSetting = "CosmosDbConnectionString") 
            OutputBinding<Counter> update,
            final ExecutionContext context) {
        Gson gson = new Gson();
        String itemFromDB = item.get();
        context.getLogger().info("counter: " + itemFromDB);
        Counter counter = gson.fromJson(itemFromDB, Counter.class); // deserializes json into Counter
        Counter updated = counter;
        updated.increment();
        update.setValue(updated);
        String count = counter.getCountString();
        context.getLogger().info("Java HTTP trigger processed a request for cv page count.");
        if (count != null) {
            context.getLogger().info("Count is " + count);
            HttpResponseMessage resp = request.createResponseBuilder(HttpStatus.OK).body(gson.toJson(counter)).build();
            return resp;
        } else {
            context.getLogger().info("Count not retrieved.");
            String body = "Count not retrieved.";
            HttpResponseMessage resp = request.createResponseBuilder(HttpStatus.OK).body(body).build();
            return resp;
        }
    }

}
