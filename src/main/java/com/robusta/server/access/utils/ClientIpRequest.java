package com.robusta.server.access.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.net.InetAddress;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * {@link HttpServletRequest} encapsulation to provide an api
 * to determine the Origin IP Address (client browser/equivalent)
 *
 * @author sudhir.ravindramohan
 * @since 26/07/2013
 */
public class ClientIpRequest extends HttpServletRequestWrapper {

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String LOCAL_IP_ADDRESS = "127.0.0.1";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public ClientIpRequest(HttpServletRequest request) {
        super(request);
    }

    public String getClientIPAddress() {
        String forwardedHeader = getHeader(X_FORWARDED_FOR);
        if(isNotBlank(forwardedHeader)) {
            logger.trace("Got a valid (non blank) forwarded header, processing for client IP");
            return logAndReturn(firstElementFromCommaSeparatedListOfForwards(forwardedHeader));
        }
        logger.trace("No forwarded header, processing using getRemoteAddr() api");
        return logAndReturn(getRemoteAddr());
    }

    private String logAndReturn(String remoteAddr) {
        logger.debug("Returning Client IP address value: '{}'", remoteAddr);
        return remoteAddr;
    }

    private String firstElementFromCommaSeparatedListOfForwards(String forwardedHeader) {
        logger.debug("Forwarded Header value sent for Client IP Processing: '{}'", forwardedHeader);
        String clientIp = split(forwardedHeader, ",")[0].trim();
        logger.debug("Client IP obtained from Forwarded Header (1st token) : '{}'", clientIp);
        return clientIp;
    }

    public boolean isLocal() {
        return getClientIPAddress().equals(LOCAL_IP_ADDRESS);
    }

    public boolean isSameHost(String serverIpAddress) {
        return getClientIPAddress().equals(serverIpAddress);
    }
}
