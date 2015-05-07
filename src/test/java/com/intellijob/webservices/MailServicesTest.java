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

        Assert.assertEquals("imap", requestMailData.getConnectionType());
        Assert.assertEquals("gmail", requestMailData.getMailAccount());
        Assert.assertEquals("DefaultUsername", requestMailData.getUsername());
        Assert.assertEquals("DefaultPassword", requestMailData.getPassword());
    }
}