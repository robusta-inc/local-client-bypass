package com.robusta.server.access.utils;

import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

public class AccessPolicyTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testName() throws Exception {
        System.out.println("InetAddress.getLocalHost() = " + InetAddress.getLocalHost().getHostAddress());
    }
}
