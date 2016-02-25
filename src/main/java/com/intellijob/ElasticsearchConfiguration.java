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
import org.springframework.util.StringUtils;

import javax.annotation.PreDestroy;

/**
 * ElasticSearch Configuration class.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.intellijob.elasticsearch.repository")
public class ElasticsearchConfiguration {

    private static final Logger LOG = Logger.getLogger(ElasticsearchConfiguration.class);

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.clusterName}")
    private String clusterName;

    @Value("${elasticsearch.http.enabled}")
    private boolean httpEnabled;

    @Value("${elasticsearch.http.cors.enabled}")
    private boolean httpCorsEnabled;

    @Value("${elasticsearch.path.data}")
    private String pathData;

    private Node node;

    /**
     * Init elasticsearch client and open connection to cluster.
     *
     * @return default ElasticsearchTemplate connected to cluster.
     */
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate() {
        LOG.info("Start elasticsearch server");
        NodeBuilder nodeBuilder = new NodeBuilder();
        nodeBuilder.clusterName(clusterName).local(false);
        if (StringUtils.hasLength(pathData)) {
            nodeBuilder.settings().put("path.data", pathData);
        }
        nodeBuilder.settings().put("http.enabled", httpEnabled);
        nodeBuilder.settings().put("http.cors.enabled", httpCorsEnabled);
        nodeBuilder.settings().put("network.host", host);

        node = nodeBuilder.node();
        return new ElasticsearchTemplate(node.client());
    }


    /**
     * Close connection to cluster!
     */
    @PreDestroy
    void destroy() {
        if (node != null) {
            LOG.info("Destroy elasticsearch server.");
            try {
                node.close();
            } catch (Exception e) {
                LOG.error("Elasticsearch server can not be destroyed!", e);
            }
        }
    }

    public Node getNode() {
        return node;
    }

}
