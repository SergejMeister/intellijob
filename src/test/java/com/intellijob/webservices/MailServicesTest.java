package com.intellijob.webservices;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Sergej Meister on 5/5/15.
 */
public class MailServicesTest extends BaseWebServiceTester {

    @Test
    public void testSearchMailLive() throws Exception {
        if (!RUNNING_LIVE) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        Assert.assertTrue(Boolean.TRUE);

    }
}