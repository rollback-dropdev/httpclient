package dev.codeflush.httpclient.request;

import dev.codeflush.httpclient.request.body.RequestBody;
import dev.codeflush.httpclient.Endpoint;
import org.junit.Test;

import static dev.codeflush.httpclient.Endpoint.HTTPS;
import static org.junit.Assert.*;

public class RequestTest {

    @Test
    public void simpleGetRequest() {
        Request request = Endpoint.forHost(HTTPS, "codeflush.dev")
                .get()
                .build();

        assertEquals("https://codeflush.dev", request.getRequestURL().toString());
    }

    @Test
    public void getRequestWithParameters() {
        Request request = Endpoint.forHost(HTTPS, "codeflush.dev")
                .get()
                .parameter("param1", "value1")
                .parameter("param2")
                .parameter("param3")
                .build();

        assertEquals("https://codeflush.dev?param1=value1&param2&param3", request.getRequestURL().toString());
    }

    @Test
    public void getRequestWithParametersFromEndpointAndRequest() {
        Request request = Endpoint.forURL("https://codeflush.dev?param0=value0")
                .get()
                .parameter("param1", "value1")
                .parameter("param2")
                .parameter("param3")
                .build();

        assertEquals("https://codeflush.dev?param0=value0&param1=value1&param2&param3", request.getRequestURL().toString());
    }

    @Test
    public void template() {
        Request.Template template = Endpoint.forHost(HTTPS, "codeflush.dev")
                .get()
                .parameter("somePredefinedParameter", "value")
                .template();

        Request request = template.enrich()
                .parameter("someExtraParameter", "value")
                .build();

        assertEquals("https://codeflush.dev?somePredefinedParameter=value&someExtraParameter=value", request.getRequestURL().toString());
    }

    @Test
    public void changesDontChangeTheTemplate() {
        Request.Template template = Endpoint.forHost(HTTPS, "codeflush.dev")
                .get()
                .parameter("param0", "value0")
                .template();

        Request request = template.enrich()
                .parameter("param0", "overriddenValue")
                .build();

        assertEquals("https://codeflush.dev?param0=overriddenValue", request.getRequestURL().toString());

        request = template.enrich()
                .parameter("param1", "value1")
                .build();

        assertEquals("https://codeflush.dev?param0=value0&param1=value1", request.getRequestURL().toString());
    }

    @Test
    public void bodyIsOverriden() {
        RequestBody body1 = RequestBody.forText("");
        RequestBody body2 = RequestBody.forText("");

        Request.TemplateWithBody template = Endpoint.forHost(HTTPS, "codeflush.dev")
                .post()
                .body(body1)
                .template();

        Request request = template.enrich()
                .body(body2)
                .build();

        assertNotSame(body1, request.getBody());
        assertSame(body2, request.getBody());
    }
}