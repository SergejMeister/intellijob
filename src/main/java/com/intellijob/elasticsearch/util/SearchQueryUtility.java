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

package com.intellijob.elasticsearch.util;

import com.intellijob.Constants;
import com.intellijob.domain.skills.SkillRatingNode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.BoostingQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Collection;
import java.util.StringTokenizer;

/**
 * Utility class for different query builders.
 */
public final class SearchQueryUtility {

    public static final String OR_SEPARATOR = ",";

    private SearchQueryUtility() {
    }

    /**
     * Build simple SearchQuery for full text searching.
     * <p>
     * The searchOriginData will be split by comma and link with OR.
     * The search data separated with whitespace will be linked with AND.
     *
     * @param searchOriginData search data.
     * @param pageRequest      paging data (offset,limit)
     *
     * @return build search query
     */
    public static SearchQuery buildFullTextSearchQuery(String searchOriginData, PageRequest pageRequest) {
        String[] searchDataArray = searchOriginData.split(OR_SEPARATOR);
        QueryBuilder builder;
        if (searchDataArray.length == 1) {
            builder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchOriginData.trim())
                    .operator(MatchQueryBuilder.Operator.AND);
        } else {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().minimumNumberShouldMatch(1);
            for (String searchData : searchDataArray) {
                StringTokenizer stringTokenizer = new StringTokenizer(searchData);
                if (stringTokenizer.countTokens() > 1) {
                    QueryBuilder matchQuery = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim())
                            .operator(MatchQueryBuilder.Operator.AND);
                    boolQueryBuilder.should(matchQuery);
                } else {
                    QueryBuilder matchQuery = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim());
                    boolQueryBuilder.should(matchQuery);
                }
            }
            builder = boolQueryBuilder;
        }

        return new NativeSearchQueryBuilder().withQuery(builder).withPageable(pageRequest).build();
    }

    /**
     * Build boosting query to boost all founded term for specified userSkills.
     * TODO this is not working because a positiveBoostingQuery requires a negativeBoostingQuery(why???)
     * <p>
     * BoostValue = {userSkillRating} / 10.
     * <p>
     * {userSkillRating} = <code>SkillRatingNode.getRating()</code>
     *
     * @param skillRatingNodes list of user skills with rating.
     * @param offset           list offset.
     * @param limit            list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildBoostingQueryAndBoostRatingField(Collection<SkillRatingNode> skillRatingNodes,
                                                                    int offset, int limit) {
        BoostingQueryBuilder boostingQueryBuilder = QueryBuilders.boostingQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            float myBoostValue = ((float) skillRatingNode.getRating()) / 10;
            QueryBuilder positiveQueryBuilder =
                    QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, skillRatingNode.getSkillNode().getName())
                            .operator(MatchQueryBuilder.Operator.AND);
            boostingQueryBuilder.positive(positiveQueryBuilder).boost(myBoostValue);
        }

        SearchQuery searchQuery = buildNativeQuery(boostingQueryBuilder, offset, limit);
        return searchQuery;
    }

    /**
     * @param skillRatingNodes list of user skills with rating.
     * @param offset           list offset.
     * @param limit            list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildFunctionScoreQueryAndBoostRatingField(Collection<SkillRatingNode> skillRatingNodes,
                                                                         int offset,
                                                                         int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            QueryBuilder functionScoreQuery = new FunctionScoreQueryBuilder(
                    QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, skillRatingNode.getSkillNode().getName())).add(
                    ScoreFunctionBuilders.fieldValueFactorFunction(Constants.DB_FIELD_RATING));
            boolQueryBuilder.should(functionScoreQuery);
        }

        SearchQuery searchQuery = buildNativeQuery(boolQueryBuilder, offset, limit);
        return searchQuery;
    }

    /**
     * @param skillRatingNodes list of user skills with rating.
     * @param offset           list offset.
     * @param limit            list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildBoolQueryAndBoostRatingField(Collection<SkillRatingNode> skillRatingNodes,
                                                                int offset,
                                                                int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {

            float boostValue = ((float) skillRatingNode.getRating()) / 10;
            QueryBuilder positiveQueryBuilder =
                    QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, skillRatingNode.getSkillNode().getName())
                            .boost(boostValue);
            boolQueryBuilder.should(positiveQueryBuilder);
        }

        SearchQuery searchQuery = buildNativeQuery(boolQueryBuilder, offset, limit);
        return searchQuery;
    }

    private static SearchQuery buildNativeQuery(QueryBuilder queryBuilder, int offset, int limit) {
        PageRequest request = new PageRequest(offset, limit);
        return new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(request).build();
    }
}
