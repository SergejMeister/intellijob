package com.intellijob.webservices;

import junit.framework.Assert;
import org.junit.Test;


public class MailServicesTest extends BaseWebServiceTester {

    @Test
    public void testSearchMailLive() throws Exception {
        if (!RUNNING_LIVE) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        Assert.assertEquals(requestMailData.getConnectionType(), mailDefaultConnectionType);
        Assert.assertEquals(requestMailData.getMailAccount(), mailDefaultAccount);
        Assert.assertEquals(requestMailData.getUsername(), mailDefaultUsername);
        Assert.assertEquals(requestMailData.getPassword(), mailDefaultPassword);
    }
}