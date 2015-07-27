/*
 * Copyright 2015 Sergej Meister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellijob;


import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This is a config to run different admin services.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class AdminConfig {

    private static final Logger LOG = Logger.getLogger(AdminConfig.class);

    private static ConfigurableApplicationContext context;

//    @Autowired
//    private CleanUpDuplicatedLinkService cleanUpDuplicatedLinkService;


    /**
     * Application Start.
     *
     * @param args application start parameters.
     *
     * @throws Exception any exceptions.
     */
    public static void main(final String[] args) throws Exception {
        SpringApplication application = new SpringApplication(AdminConfig.class);
        context = application.run(args);
        LOG.info("\n <========================================================================= \n"
                + "                     Admin Tools for IntelliJob started successful ! \n"
                + "=========================================================================> \n");

//        CleanUpDuplicatedLinkService cleanUpDuplicatedLinkService = context.getBean(CleanUpDuplicatedLinkService.class);
//        cleanUpDuplicatedLinkService.run();
    }

    /**
     * Returns application context.
     *
     * @return the context
     */
    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}
