package com.robusta.server.access.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ClientIpRequestTest {
    public static final String LOCAL_IP = "127.0.0.1";
    @Mock private HttpServletRequest request;
    private ClientIpRequest clientIpRequest;
    public static final String CLIENT_IP = "172.1.1.1";
    private static final String FORWARD_HEADER_VALUE = CLIENT_IP + ", 170.2.1.2, 190.4.43.67";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(request.getRemoteAddr()).thenReturn(LOCAL_IP);
        clientIpRequest = new ClientIpRequest(request);
    }

    @Test
    public void testGetClientIPAddress_whenForwardHeaderIsNotAvailable_shouldReturnRemoteAddressOfRequest() throws Exception {
        assertThat(clientIpRequest.getClientIPAddress(), equalTo(LOCAL_IP));
    }

    @Test
    public void testGetClientIPAddress_whenForwardHeaderIsAvailable_shouldReturnFromForwardHeader() throws Exception {
        when(request.getHeader(ClientIpRequest.X_FORWARDED_FOR)).thenReturn(FORWARD_HEADER_VALUE);
        assertThat(clientIpRequest.getClientIPAddress(), equalTo(CLIENT_IP));
    }
}
