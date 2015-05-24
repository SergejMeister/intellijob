package com.intellijob;


import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;

public class TestApplicationController extends ApplicationController {

    private static final Logger LOG = Logger.getLogger(TestApplicationController.class);
    private static ConfigurableApplicationContext context;


    /**
     * Returns application context.
     *
     * @return the context
     */
    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}
