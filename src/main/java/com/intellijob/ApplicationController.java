/*
 * Copyright 2015 Sergej Meister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellijob;


import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * Application controller.
 */
@SpringBootApplication
@EnableWebMvc
public class ApplicationController extends WebMvcConfigurerAdapter {

    private static final Logger LOG = Logger.getLogger(ApplicationController.class);
    private static ConfigurableApplicationContext context;


    /**
     * Application Start.
     *
     * @param args application start parameters.
     *
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        context = SpringApplication.run(ApplicationController.class, args);
        LOG.info("\n<========================================================================= \n" +
                    "                     IntelliJob started successful ! \n" +
                    "=========================================================================>\n");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("views/");
        resolver.setSuffix(".html");

        return resolver;
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
