package com.malachi;

import com.microsoft.azure.functions.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;
import java.util.logging.Logger;

import com.google.gson.*;

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
        

        when(req.getHttpMethod()).thenReturn(HttpMethod.GET);
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

        @SuppressWarnings("unchecked")
        OutputBinding<Counter> out = mock(OutputBinding.class);
        Counter counter = new Counter(1);

        Gson gson = new Gson();
        String counterJson = gson.toJson(counter);
        Optional<String> opt = Optional.of(counterJson);
        final Counter result = new Counter();

        doAnswer(new Answer<Counter>() {
            @Override
            public Counter answer(InvocationOnMock invocation) {
                Counter arg = (Counter) invocation.getArguments()[0];
                int count = arg.getCount();
                result.setCount(count);
                return result;
            }
        }).when(out).setValue(any(Counter.class));

        when(out.getValue()).thenReturn(result);

        Logger.getGlobal().info(req.getQueryParameters().toString());
        
        final HttpResponseMessage ret = Function.getCVPageCount(req, opt, out, context);

        // Verify
        assertEquals(HttpStatus.OK, ret.getStatus());
        assertEquals(2, out.getValue().getCount());

    }
}
