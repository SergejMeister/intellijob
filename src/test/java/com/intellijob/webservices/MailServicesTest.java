package com.intellijob.webservices;

import com.intellijob.mail.dto.ResponseMailSearchData;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;


public class MailServicesTest extends BaseWebServiceTester {

    @Test
    public void testLiveSearchMail() throws Exception {
        if (!RUNNING_LIVE) {
            Assert.assertTrue("Don't run this test.", Boolean.TRUE);
            return;
        }

        //add to showcase
        Map<String, String> map = new HashMap<>();
        map.put(PROP_KEY_CONNECTION_TYPE, requestMailData.getConnectionType());
        map.put(PROP_KEY_MAIL_ACCOUNT, requestMailData.getMailAccount());
        map.put(PROP_KEY_MAIL_USERNAME, requestMailData.getUsername());
        map.put(PROP_KEY_MAIL_PASSWORD, requestMailData.getPassword());

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(Endpoints.MAIL_SEARCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestMailData))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ResponseMailSearchData responseMailSearchData = mapper.readValue(content, ResponseMailSearchData.class);
        System.out.println(responseMailSearchData.getMessage());
        Assert.assertNull(responseMailSearchData.getError());
    }

    @Test
    public void testRequestMailAccountData() {
        Assert.assertEquals("imap", requestMailData.getConnectionType());
        Assert.assertEquals("gmail", requestMailData.getMailAccount());
        Assert.assertEquals("DefaultUsername", requestMailData.getUsername());
        Assert.assertEquals("DefaultPassword", requestMailData.getPassword());
    }
}