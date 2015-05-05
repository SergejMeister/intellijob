package com.intellijob.webservices;


import com.intellijob.ApplicationController;
import com.intellijob.mail.dto.RequestMailData;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationController.class)
//@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public abstract class BaseWebServiceTester {

    /**
     * Constants.
     */
    protected final static Boolean RUNNING_LIVE = Boolean.TRUE;
    protected static final String PROP_FILENAME_REQUEST_MAIL = "developerMailAccount.properties";
    protected final static String PROP_KEY_CONNECTION_TYPE = "connectiontype";
    protected final static String PROP_KEY_MAIL_ACCOUNT = "mailaccount";
    protected final static String PROP_KEY_MAIL_USERNAME = "mailusername";
    protected final static String PROP_KEY_MAIL_PASSWORD = "mailpassword";


    /**
     * Static variables.
     */
    protected static String mailDefaultAccount;
    protected static String mailDefaultConnectionType;
    protected static String mailDefaultUsername;
    protected static String mailDefaultPassword;
    protected static RequestMailData requestMailData;

    @BeforeClass
    public static void beforeClass() {
        Properties mailProperties = readProperty(PROP_FILENAME_REQUEST_MAIL);
        mailDefaultAccount = mailProperties.getProperty(PROP_KEY_MAIL_ACCOUNT);
        mailDefaultConnectionType = mailProperties.getProperty(PROP_KEY_CONNECTION_TYPE);
        mailDefaultUsername = mailProperties.getProperty(PROP_KEY_MAIL_USERNAME);
        mailDefaultPassword = mailProperties.getProperty(PROP_KEY_MAIL_PASSWORD);
        requestMailData = createRequestMailData(mailDefaultAccount, mailDefaultUsername, mailDefaultPassword, mailDefaultConnectionType);

        if (RUNNING_LIVE) {
            //TODO execute tests live!
        }
    }


    private static Properties readProperty(String fileName) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return properties;
    }

    private static RequestMailData createRequestMailData(String mailAccount, String username, String password, String connectionType) {
        RequestMailData requestMailData = new RequestMailData(username, password);
        requestMailData.setMailAccount(mailAccount);
        requestMailData.setConnectionType(connectionType);
        return requestMailData;
    }
}
