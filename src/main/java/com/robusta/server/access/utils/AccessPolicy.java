package com.robusta.server.access.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class AccessPolicy {

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

    public static class AccessPolicies {
        AccessPolicy LOCAL_HOST_POLICY = new AccessPolicy() {
            @Override
            boolean allowed(HttpServletRequest request) {
                return new ClientIpRequest(request).isLocal();
            }
        };
        AccessPolicy SAME_HOST_POLICY = new AccessPolicy() {
            @Override
            boolean allowed(HttpServletRequest request) {
                return new ClientIpRequest(request).isSameHost(SERVER_IP_ADDRESS);
            }
        };
        AccessPolicy AUTH_COOKIE_POLICY = new AccessPolicy() {
            @Override
            boolean allowed(HttpServletRequest request) {
                return false;
            }
        };
    }
}
