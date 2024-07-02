package org.example;

import org.eclipse.jetty.client.ContentResponse;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.transport.HttpClientConnectionFactory;
import org.eclipse.jetty.client.transport.HttpClientTransportDynamic;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.transport.ClientConnectionFactoryOverHTTP2;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.jupiter.api.Test;

public class H2SharepointTest
{
    @Test
    public void testRequest() throws Exception
    {
        var connector = new ClientConnector();
        connector.setSslContextFactory(new SslContextFactory.Client());
        var httpClient = new HttpClient(new HttpClientTransportDynamic(connector,
            HttpClientConnectionFactory.HTTP11,
            new ClientConnectionFactoryOverHTTP2.HTTP2(new HTTP2Client(connector))
        ));
        httpClient.start();
        ContentResponse response = httpClient.newRequest("https://microsoft.sharepoint.com")
            .onRequestQueued(r -> System.out.println("request queued"))
            .onRequestBegin(r -> System.out.println("request begin"))
            .onRequestContent((r, c) -> System.out.println("request content"))
            .onRequestSuccess(r -> System.out.println("request success"))
            .onResponseBegin(r -> System.out.println("response begin"))
            .onResponseContent((r, c) -> System.out.println("response content"))
            .onResponseSuccess(r -> System.out.println("response success"))
            .send();
        System.out.println(response);
        httpClient.stop();
    }
}
