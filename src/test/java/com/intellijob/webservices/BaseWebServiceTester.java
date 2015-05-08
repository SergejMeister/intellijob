package com.intellijob.webservices;


import com.intellijob.TestApplicationController;
import com.intellijob.mail.dto.RequestMailData;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestApplicationController.class}, loader = SpringApplicationContextLoader.class)
@WebAppConfiguration
@ImportResource({
        "classpath*:developerMailAccount.properties"
})
public abstract class BaseWebServiceTester {

    /**
     * Constants.
     */
    protected final static Boolean RUNNING_LIVE = Boolean.TRUE;
    protected static final String FILENAME_DEVELOPER_MAIL_ACCOUNT = "developerMailAccount.properties";
    protected static final String FILENAME_TEST_MAIL_ACCOUNT = "testMailAccount.properties";
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
        Properties mailProperties;
        if (RUNNING_LIVE) {
            mailProperties = readProperty(FILENAME_DEVELOPER_MAIL_ACCOUNT);
        } else {
            mailProperties = readProperty(FILENAME_TEST_MAIL_ACCOUNT);
        }
        mailDefaultAccount = mailProperties.getProperty(PROP_KEY_MAIL_ACCOUNT);
        mailDefaultConnectionType = mailProperties.getProperty(PROP_KEY_CONNECTION_TYPE);
        mailDefaultUsername = mailProperties.getProperty(PROP_KEY_MAIL_USERNAME);
        mailDefaultPassword = mailProperties.getProperty(PROP_KEY_MAIL_PASSWORD);
        requestMailData = createRequestMailData(mailDefaultAccount, mailDefaultUsername, mailDefaultPassword,
                mailDefaultConnectionType);
    }


    private static Properties readProperty(String fileName) {
        Properties properties = new Properties();
//        System.out.println(BaseWebServiceTester.class.getResource(".").getPath());
//        System.out.println(Thread.currentThread().getContextClassLoader().getResource(".").getPath());
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return properties;
    }

    private static RequestMailData createRequestMailData(String mailAccount, String username, String password,
                                                         String connectionType) {
        RequestMailData requestMailData = new RequestMailData(username, password);
        requestMailData.setMailAccount(mailAccount);
        requestMailData.setConnectionType(connectionType);
        return requestMailData;
    }
}
