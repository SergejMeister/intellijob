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
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PreDestroy;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.intellijob.elasticsearch.repository")
public class ElasticsearchConfiguration {

    private static final Logger LOG = Logger.getLogger(ElasticsearchConfiguration.class);

    @Value("${elasticsearch.port}")
    int port;

    @Value("${elasticsearch.host}")
    String hostname;

    @Value("${elasticsearch.clusterName}")
    String clusterName;

    Node node;


//    @Bean
//    public Client client() {
//        TransportClient client = new TransportClient();
//        TransportAddress address = new InetSocketTransportAddress(hostname, port);
//        client.addTransportAddress(address);
//
//        return client;
//    }

//    /**
//     * Init elasticsearch client and returns ElasticsearchTemplate.
//     *
//     * @return default ElasticsearchTemplate
//     */
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchTemplate(client());
//    }

    /**
     * Init elasticsearch client and returns ElasticsearchTemplate for Cluster intelliJob.
     *
     * @return default ElasticsearchTemplate for Cluster intelliJob.
     */
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        LOG.info("Start elasticsearch server");
        node = NodeBuilder.nodeBuilder().clusterName(clusterName).local(true).node();
        return new ElasticsearchTemplate(node.client());
    }

    @PreDestroy void destroy() {
        if (node != null) {
            LOG.info("Destroy elasticsearch server.");
            try {
                node.close();
            } catch (Exception e) {
                LOG.error("Elasticsearch server can not be destroyed!", e);
            }
        }
    }

}
