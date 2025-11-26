package service;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

class TestHttpExchange {
    private final HttpExchange delegate;
    private final String requestMethod;
    private URI requestURI;
    private final InputStream requestBody;
    private final ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
    private int responseCode;
    private long responseLength;

    public TestHttpExchange(String method, String path, String body) {
        this.requestMethod = method;
        this.requestURI = URI.create("http://localhost:8080" + path);
        this.requestBody = new ByteArrayInputStream(
                body != null ? body.getBytes(StandardCharsets.UTF_8) : new byte[0]
        );
        this.delegate = createDelegate();
    }

    public HttpExchange getDelegate() {
        return delegate;
    }

    private HttpExchange createDelegate() {
        return new HttpExchange() {
            @Override public String getRequestMethod() { return requestMethod; }
            @Override public URI getRequestURI() { return requestURI; }
            @Override public InputStream getRequestBody() { return requestBody; }

            @Override
            public Headers getRequestHeaders() {
                return null;
            }

            @Override public Headers getResponseHeaders() { return new Headers(); }
            @Override public void sendResponseHeaders(int code, long length) {
                responseCode = code;
                responseLength = length;
            }
            @Override public OutputStream getResponseBody() { return responseBody; }
            @Override public void close() {}
            @Override public HttpContext getHttpContext() { return null; }
            @Override public InetSocketAddress getRemoteAddress() { return null; }
            @Override public int getResponseCode() { return responseCode; }
            @Override public InetSocketAddress getLocalAddress() { return null; }
            @Override public String getProtocol() { return "HTTP/1.1"; }
            @Override public Object getAttribute(String name) { return null; }
            @Override public void setAttribute(String name, Object value) {}


            @Override public void setStreams(InputStream i, OutputStream o) {}

            @Override
            public HttpPrincipal getPrincipal() {
                return null;
            }
        };
    }

    public int getResponseCode() { return responseCode; }
    public String getResponseBodyAsString() {
        return responseBody.toString(StandardCharsets.UTF_8);
    }
}