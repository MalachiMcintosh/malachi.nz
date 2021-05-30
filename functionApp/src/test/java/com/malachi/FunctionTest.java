package com.malachi;

import com.microsoft.azure.functions.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


/**
 * Unit test for Function class.
 */
public class FunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testHttpTriggerJava() throws Exception {
        // Setup
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<String>> req = mock(HttpRequestMessage.class);

        final Map<String, String> queryParams = new HashMap<>();
        queryParams.put("id", "count");
        doReturn(queryParams).when(req).getQueryParameters();

        final Optional<String> queryBody = Optional.empty();
        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        // Invoke
        // final HttpResponseMessage ret = new Function().run(req, context);
        
        // Optional<String>  opt = Optional.empty();        
        Optional<String>  opt = Optional.of("count");        
        OutputBinding<Counter> out = (OutputBinding<Counter>)mock(OutputBinding.class);
        // Logger.getGlobal().info(req.getQueryParameters().toString());
        final HttpResponseMessage ret = new Function().getCVPageCount(req, opt, out, context);
        // final HttpResponseMessage ret = new Function().getCVPageCount(req, opt, context);

        // Verify
        assertEquals(ret.getStatus(), HttpStatus.OK);
        assertEquals(ret.getBody(), "Counter in progress");
    }
}
