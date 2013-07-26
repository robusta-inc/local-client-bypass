package com.robusta.server.access.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public enum AccessPolicy {
    LOCAL_HOST_POLICY {
        @Override
        boolean allowed(HttpServletRequest request) {
            return new ClientIpRequest(request).isLocal();
        }
    }, SAME_HOST_POLICY {
        @Override
        boolean allowed(HttpServletRequest request) {
            return new ClientIpRequest(request).isSameHost(SERVER_IP_ADDRESS);
        }
    }, AUTH_COOKIE_POLICY {
        @Override
        boolean allowed(HttpServletRequest request) {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    };
    public static final String SERVER_IP_DETERMINATION_FAILURE = "SERVER_IP_DETERMINATION_FAILURE";
    private static final String SERVER_IP_ADDRESS = serverIpAddress();
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessPolicy.class);

    abstract boolean allowed(HttpServletRequest request);

    private static String serverIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.error("Unable to determine the server machine's IP address, SAME_HOST_POLICY will not work.", e);
        }
        return SERVER_IP_DETERMINATION_FAILURE;
    }
}
