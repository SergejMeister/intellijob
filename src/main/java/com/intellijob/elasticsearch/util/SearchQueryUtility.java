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
import com.intellijob.elasticsearch.domain.EsUserSkills;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
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

    /**
     * Default 2 weeks.
     */
    public static final String DEFAULT_DECAY_FOR_RECEIVED_DATE = "14d";

    /**
     * Expansion value, means how many
     */
    public static final int DEFAULT_EXPANSION_VALUE = 30;

    private SearchQueryUtility() {
    }

    /**
     * Build simple SearchQuery for full text searching.
     * <p>
     * The searchOriginData will be split by comma and link with OR.
     * The search data separated with whitespace will be linked with AND.
     * <p>
     * Deprecated because it is not so optimal like other search queries. Please use <code>buildFullTextSearchMatchQuery_4</code>
     *
     * @param searchOriginData search data.
     * @param pageRequest      paging data (offset,limit)
     *
     * @return build search query
     */
    @Deprecated
    public static SearchQuery buildFullTextSearchBoolQuery_1(String searchOriginData, PageRequest pageRequest) {
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
     * Build simple SearchQuery for full text searching.
     * <p>
     * The searchOriginData will be split by comma and link with OR.
     * The search data separated with whitespace will be linked with AND.
     * This query use  <code>ScoreFunctionBuilders.exponentialDecayFunction</code> to calculate receivedDate of last 14 days.
     *
     * @param searchOriginData search data.
     * @param pageRequest      paging data (offset,limit)
     *
     * @return build search query
     */
    public static SearchQuery buildFullTextSearchMatchQuery_3(String searchOriginData, PageRequest pageRequest) {
        String[] searchDataArray = searchOriginData.split(OR_SEPARATOR);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (searchDataArray.length == 1) {
            boolQueryBuilder.should(QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchOriginData.trim())
                    .operator(MatchQueryBuilder.Operator.AND));
        } else {
            for (String searchData : searchDataArray) {
                QueryBuilder matchQuery = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim())
                        .operator(MatchQueryBuilder.Operator.AND);
                boolQueryBuilder.should(matchQuery);
            }
        }

        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE)
                .setDecay(0.5));

        return new NativeSearchQueryBuilder().withQuery(functionBuilder).withPageable(pageRequest).build();
    }

    /**
     * Build simple SearchQuery for full text searching.
     * <p>
     * The searchOriginData will be split by comma and link with OR.
     * The search data separated with whitespace will be linked with AND.
     * This query use  <code>ScoreFunctionBuilders.exponentialDecayFunction</code> to calculate receivedDate of last 14 days.
     *
     * @param searchOriginData search data.
     * @param pageRequest      paging data (offset,limit)
     *
     * @return build search query
     */
    public static SearchQuery buildFullTextSearchMatchQuery_4(String searchOriginData, PageRequest pageRequest) {
        String[] searchDataArray = searchOriginData.split(OR_SEPARATOR);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery(Constants.DB_FIELD_READ, false));
        if (searchDataArray.length == 1) {
            boolQueryBuilder.should(QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchOriginData.trim())
                    .operator(MatchQueryBuilder.Operator.AND));
        } else {
            for (String searchData : searchDataArray) {
                QueryBuilder matchQuery = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim())
                        .operator(MatchQueryBuilder.Operator.AND);
                boolQueryBuilder.should(matchQuery);
            }
        }

        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE)
                .setDecay(0.5));

        return new NativeSearchQueryBuilder().withQuery(functionBuilder).withPageable(pageRequest).build();
    }

    /**
     * Build simple SearchQuery for full text searching.
     * <p>
     * The searchOriginData will be split by comma and link with OR.
     * The search data separated with whitespace will be linked with AND.
     * This query use  <code>ScoreFunctionBuilders.exponentialDecayFunction</code> to calculate receivedDate of last 14 days.
     *
     * @param searchOriginData search data.
     * @param pageRequest      paging data (offset,limit)
     *
     * @return build search query
     */
    public static SearchQuery buildFullTextSearchMatchQueryWithName(String searchOriginData, PageRequest pageRequest) {
        String[] searchDataArray = searchOriginData.split(OR_SEPARATOR);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (searchDataArray.length == 1) {
            BoolQueryBuilder contentNameBoolBuilder = QueryBuilders.boolQuery();
            contentNameBoolBuilder.should(QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchOriginData.trim())
                    .operator(MatchQueryBuilder.Operator.AND));
            contentNameBoolBuilder.should(QueryBuilders.matchQuery(Constants.DB_FIELD_NAME, searchOriginData.trim())
                    .operator(MatchQueryBuilder.Operator.AND).boost(10));
            boolQueryBuilder.should(contentNameBoolBuilder);
        } else {
            for (String searchData : searchDataArray) {
                BoolQueryBuilder contentNameBoolBuilder = QueryBuilders.boolQuery();
                contentNameBoolBuilder.should(QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim())
                        .operator(MatchQueryBuilder.Operator.AND));
                contentNameBoolBuilder.should(QueryBuilders.matchQuery(Constants.DB_FIELD_NAME, searchData.trim())
                        .operator(MatchQueryBuilder.Operator.AND).boost(10));
                boolQueryBuilder.should(contentNameBoolBuilder);
            }
        }

        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE)
                .setDecay(0.5));

        return new NativeSearchQueryBuilder().withQuery(functionBuilder).withPageable(pageRequest).build();
    }

    /**
     * Build simple SearchQuery for full text searching.
     * <p>
     * The searchOriginData will be split by comma and link with OR.
     * The search data separated with whitespace will be linked with AND.
     * This query use  <code>ScoreFunctionBuilders.exponentialDecayFunction</code> to calculate receivedDate of last 14 days.
     *
     * @param searchOriginData search data.
     * @param pageRequest      paging data (offset,limit)
     *
     * @return build search query
     */
    public static SearchQuery buildFullTextSearchMatchQueryUsingOrConjunction(String searchOriginData,
                                                                              PageRequest pageRequest) {
        String[] searchDataArray = searchOriginData.split(OR_SEPARATOR);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (searchDataArray.length == 1) {
            boolQueryBuilder.should(QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchOriginData.trim())
                    .operator(MatchQueryBuilder.Operator.OR));
        } else {
            for (String searchData : searchDataArray) {
                QueryBuilder matchQuery = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim())
                        .operator(MatchQueryBuilder.Operator.OR);
                boolQueryBuilder.should(matchQuery);
            }
        }

        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE)
                .setDecay(0.5));

        return new NativeSearchQueryBuilder().withQuery(functionBuilder).withPageable(pageRequest).build();
    }

    /**
     * Build simple SearchQuery for full text searching.
     * <p>
     * The searchOriginData will be split by comma and link with OR.
     * The search data separated with whitespace will be linked with AND.
     * This query use  <code>ScoreFunctionBuilders.exponentialDecayFunction</code> to calculate receivedDate of last 14 days.
     *
     * @param searchData  search data.
     * @param pageRequest paging data (offset,limit)
     *
     * @return build search query
     */
    public static SearchQuery buildFullTextQuery(String searchData, PageRequest pageRequest) {
        QueryBuilder builder = generateDefaultQueryBuilderForFullTextSearch(searchData);
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(builder);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        return new NativeSearchQueryBuilder().withQuery(functionBuilder).withPageable(pageRequest).build();
    }


    /**
     * Build simple SearchQuery for full text searching.
     * TODO: important to describe this in master thesis!
     * <p>
     * The searchOriginData will be split by comma and link with OR.
     * The search data separated with whitespace will be linked with AND.
     * <p>
     * Deprecated because: see comments.
     * This query use  <code>ScoreFunctionBuilders.fieldValueFactorFunction</code> to mark receiveDate as very important searchField and to boost this field with factor 1.5.
     * But the evaluation test show, that the search result is not so optimal as on <code>ScoreFunctionBuilders.exponentialDecayFunction</code>
     *
     * @param searchOriginData search data.
     * @param pageRequest      paging data (offset,limit)
     *
     * @return build search query
     */
    @Deprecated
    public static SearchQuery buildFullTextSearchMatchQuery_Boosting_FieldReceivedDate(String searchOriginData,
                                                                                       PageRequest pageRequest) {
        QueryBuilder builder = generateDefaultQueryBuilderForFullTextSearch(searchOriginData);
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(builder);
        functionBuilder.add(ScoreFunctionBuilders.fieldValueFactorFunction(Constants.DB_FIELD_RECEIVED_DATE)
                .factor(1.5f));
        return new NativeSearchQueryBuilder().withQuery(functionBuilder).withPageable(pageRequest).build();
    }

    /**
     * @param userSkills list of user skills with rating.
     * @param offset     list offset.
     * @param limit      list limit.
     *
     * @return searchQuery
     */
    @Deprecated
    public static SearchQuery buildBoolQueryAndBoostRatingFieldUsingExpansion(Collection<EsUserSkills> userSkills,
                                                                              int offset,
                                                                              int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (EsUserSkills userSkill : userSkills) {
            float boostValue = userSkill.getRating() * 10;
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, userSkill.getName())
                    .operator(MatchQueryBuilder.Operator.AND).fuzziness(5).maxExpansions(DEFAULT_EXPANSION_VALUE)
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder);
        }

        return buildNativeQuery(boolQueryBuilder, offset, limit);
    }

    /**
     * @param userSkills list of user skills with rating.
     * @param offset     list offset.
     * @param limit      list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildBoolQueryAndBoostRatingField_1(Collection<EsUserSkills> userSkills,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (EsUserSkills userSkill : userSkills) {
            float boostValue = userSkill.getRating() * 10;
            String searchTerm = userSkill.getName();
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm)
                    .operator(MatchQueryBuilder.Operator.AND)
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder);
        }

        FilteredQueryBuilder filteredQuery = QueryBuilders
                .filteredQuery(boolQueryBuilder, FilterBuilders.termFilter(Constants.DB_FIELD_READ, Boolean.FALSE));
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(filteredQuery);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        return buildNativeQuery(functionBuilder, offset, limit);
    }

    /**
     * @param userSkills list of user skills with rating.
     * @param offset     list offset.
     * @param limit      list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildBoolQueryAndBoostRatingField_2(Collection<EsUserSkills> userSkills,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (EsUserSkills userSkill : userSkills) {
            float boostValue = userSkill.getRating() * 10;
            String searchTerm = userSkill.getName();
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm)
                    .operator(MatchQueryBuilder.Operator.OR)
                    .boost(boostValue);

            FunctionScoreQueryBuilder weightScoreQuery = QueryBuilders.functionScoreQuery(matchQueryBuilder)
                    .add(ScoreFunctionBuilders.weightFactorFunction(boostValue));
            boolQueryBuilder.should(weightScoreQuery);
        }

        FilteredQueryBuilder filteredQuery = QueryBuilders
                .filteredQuery(boolQueryBuilder, FilterBuilders.termFilter(Constants.DB_FIELD_READ, Boolean.FALSE));
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(filteredQuery);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        return buildNativeQuery(functionBuilder, offset, limit);
    }

    /**
     * Build a rating search query.
     *
     * @param skills list of user skills with rating.
     * @param offset list offset.
     * @param limit  list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildRatingQuery(Collection<EsUserSkills> skills, int offset, int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (EsUserSkills esUserSkill : skills) {
            float boostValue = esUserSkill.getRating() * 10;
            if (esUserSkill.isParent()) {
                // for parent override.
                boostValue = esUserSkill.getRating();
            }
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, esUserSkill.getName())
                    .operator(MatchQueryBuilder.Operator.OR).boost(boostValue);

            FunctionScoreQueryBuilder weightScoreQuery = QueryBuilders.functionScoreQuery(matchQueryBuilder)
                    .add(ScoreFunctionBuilders.weightFactorFunction(boostValue));
            boolQueryBuilder.should(weightScoreQuery);
        }

        FilteredQueryBuilder filteredQuery = QueryBuilders
                .filteredQuery(boolQueryBuilder, FilterBuilders.termFilter(Constants.DB_FIELD_READ, Boolean.FALSE));
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(filteredQuery);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        return buildNativeQuery(functionBuilder, offset, limit);
    }

    private static SearchQuery buildNativeQuery(QueryBuilder queryBuilder, int offset, int limit) {
        PageRequest request = new PageRequest(offset, limit);
        return new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(request).build();
    }

    private static QueryBuilder generateDefaultQueryBuilderForFullTextSearch(String searchOriginData) {
        String[] searchDataArray = searchOriginData.split(OR_SEPARATOR);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (searchDataArray.length == 1) {
            boolQueryBuilder.should(QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchOriginData.trim())
                    .operator(MatchQueryBuilder.Operator.AND));
        } else {
            for (String searchData : searchDataArray) {
                QueryBuilder matchQuery = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchData.trim())
                        .operator(MatchQueryBuilder.Operator.AND);
                boolQueryBuilder.should(matchQuery);
            }
        }

        return QueryBuilders
                .filteredQuery(boolQueryBuilder, FilterBuilders.termFilter(Constants.DB_FIELD_READ, Boolean.FALSE));
    }

    public static SearchQuery buildAutocompleteKnowledgeQuery(String searchWord) {
//        CompletionSuggestionFuzzyBuilder completionSuggestionFuzzyBuilder =
//                new CompletionSuggestionFuzzyBuilder(EsConstants.FIELD_SUGGEST_KNOWLEDGE).text(searchWord).field(
//                        EsConstants.FIELD_SUGGEST_KNOWLEDGE).size(50);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("nameNgram", searchWord))
                .must(QueryBuilders.matchQuery("nameSimple", searchWord));
//        boolQueryBuilder.must(QueryBuilders.matchQuery("nameNgram", searchWord)).must(QueryBuilders.matchQuery("nameSimple", searchWord));
//        boolQueryBuilder.should(QueryBuilders.matchQuery("nameNgram", searchWord));

        return buildNativeQuery(boolQueryBuilder, 0, 20);
    }
}
