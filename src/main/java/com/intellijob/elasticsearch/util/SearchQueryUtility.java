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
import com.intellijob.elasticsearch.domain.EsUserSkills;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.BoostingQueryBuilder;
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
    public static SearchQuery buildFullTextSearchBoolQuery(String searchOriginData, PageRequest pageRequest) {
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
                            .operator(MatchQueryBuilder.Operator.AND).maxExpansions(30);
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
     * <p>
     * Deprecated because ths search query ignore the receivedDate and should be extra sort.
     *
     * @param searchOriginData search data.
     * @param pageRequest      paging data (offset,limit)
     *
     * @return build search query
     */
    @Deprecated
    public static SearchQuery buildFullTextSearchMatchQuery_2(String searchOriginData, PageRequest pageRequest) {
        QueryBuilder builder = generateDefaultQueryBuilderForFullTextSearch(searchOriginData);
        return new NativeSearchQueryBuilder().withQuery(builder).withPageable(pageRequest).build();
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
    public static SearchQuery buildFullTextSearchMatchQuery_3(String searchOriginData,
                                                              PageRequest pageRequest) {
        QueryBuilder builder = generateDefaultQueryBuilderForFullTextSearch(searchOriginData);
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(builder);
        functionBuilder.add(ScoreFunctionBuilders.fieldValueFactorFunction(Constants.DB_FIELD_RECEIVED_DATE)
                .factor(1.5f));
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
        QueryBuilder builder = generateDefaultQueryBuilderForFullTextSearch(searchOriginData);
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(builder);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        return new NativeSearchQueryBuilder().withQuery(functionBuilder).withPageable(pageRequest).build();
    }

    /**
     * @param skillRatingNodes list of user skills with rating.
     * @param offset           list offset.
     * @param limit            list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildBoolQueryAndBoostRatingField_1(Collection<SkillRatingNode> skillRatingNodes,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {

            float boostValue = ((float) skillRatingNode.getRating()) / 10;
            QueryBuilder matchQueryBuilder = QueryBuilders
                    .matchQuery(Constants.DB_FIELD_CONTENT, skillRatingNode.getSkillNode().getName())
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder);
        }

        SearchQuery searchQuery = buildNativeQuery(boolQueryBuilder, offset, limit);
        return searchQuery;
    }

    public static SearchQuery buildBoolQueryAndBoostRatingField_2(Collection<SkillRatingNode> skillRatingNodes,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            float boostValue = (float) skillRatingNode.getRating();
            QueryBuilder matchQueryBuilder = QueryBuilders
                    .matchQuery(Constants.DB_FIELD_CONTENT, skillRatingNode.getSkillNode().getName())
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder);
        }

        SearchQuery searchQuery = buildNativeQuery(boolQueryBuilder, offset, limit);
        return searchQuery;
    }

    public static SearchQuery buildBoolQueryAndBoostRatingField_3(Collection<SkillRatingNode> skillRatingNodes,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            float boostValue = (float) skillRatingNode.getRating() * 10;
            QueryBuilder matchQueryBuilder = QueryBuilders
                    .matchQuery(Constants.DB_FIELD_CONTENT, skillRatingNode.getSkillNode().getName())
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder);
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
    public static SearchQuery buildBoolQueryAndBoostRatingField_4(Collection<SkillRatingNode> skillRatingNodes,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            float boostValue = (float) skillRatingNode.getRating() * 10;
            QueryBuilder matchQueryBuilder = QueryBuilders
                    .matchQuery(Constants.DB_FIELD_CONTENT, skillRatingNode.getSkillNode().getName()).
                            operator(MatchQueryBuilder.Operator.AND).maxExpansions(DEFAULT_EXPANSION_VALUE)
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder);
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
    public static SearchQuery buildBoolQueryAndBoostRatingField_5(Collection<SkillRatingNode> skillRatingNodes,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            float boostValue = (float) skillRatingNode.getRating() * 10;
            String searchTerm = skillRatingNode.getSkillNode().getName();
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm)
                    .operator(MatchQueryBuilder.Operator.OR).fuzziness(5)
                    .maxExpansions(DEFAULT_EXPANSION_VALUE)
                    .prefixLength(searchTerm.length() - 1)
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder);
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
    public static SearchQuery buildBoolQueryAndBoostRatingField_6(Collection<SkillRatingNode> skillRatingNodes,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            float boostValue = (float) skillRatingNode.getRating() * 10;
            String searchTerm = skillRatingNode.getSkillNode().getName();
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm)
                    .operator(MatchQueryBuilder.Operator.OR).fuzziness(5)
                    .maxExpansions(DEFAULT_EXPANSION_VALUE)
                    .prefixLength(searchTerm.length() - 1)
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder);
        }

        FilteredQueryBuilder filteredQuery = QueryBuilders
                .filteredQuery(boolQueryBuilder, FilterBuilders.termFilter(Constants.DB_FIELD_READ, Boolean.FALSE));
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(filteredQuery);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        SearchQuery searchQuery = buildNativeQuery(functionBuilder, offset, limit);
        return searchQuery;
    }

    /**
     * @param skillRatingNodes list of user skills with rating.
     * @param offset           list offset.
     * @param limit            list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildBoolQueryAndBoostRatingField_7(Collection<SkillRatingNode> skillRatingNodes,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            float weightValue = (float) skillRatingNode.getRating() * 100;
            String searchTerm = skillRatingNode.getSkillNode().getName();

            QueryBuilder matchQueryBuilder100 =
                    QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm).operator(
                            MatchQueryBuilder.Operator.AND)
                            .fuzziness(1).minimumShouldMatch("100%");
            FunctionScoreQueryBuilder functionScoreQuery100 = QueryBuilders.functionScoreQuery(matchQueryBuilder100)
                    .add(ScoreFunctionBuilders.weightFactorFunction(weightValue));
            boolQueryBuilder.should(functionScoreQuery100);

            QueryBuilder matchQueryBuilder50 =
                    QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm).operator(
                            MatchQueryBuilder.Operator.OR).minimumShouldMatch("80%");
            FunctionScoreQueryBuilder functionScoreQuery50 = QueryBuilders.functionScoreQuery(matchQueryBuilder50)
                    .add(ScoreFunctionBuilders.weightFactorFunction(weightValue / 2));
            boolQueryBuilder.should(functionScoreQuery50);
        }

        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        SearchQuery searchQuery = buildNativeQuery(functionBuilder, offset, limit);
        return searchQuery;
    }

    /**
     * @param skillRatingNodes list of user skills with rating.
     * @param offset           list offset.
     * @param limit            list limit.
     *
     * @return searchQuery
     */
    public static SearchQuery buildBoolQueryAndBoostRatingField_8(Collection<SkillRatingNode> skillRatingNodes,
                                                                  int offset,
                                                                  int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (SkillRatingNode skillRatingNode : skillRatingNodes) {
            float boostValue = (float) skillRatingNode.getRating() * 10;
            String searchTerm = skillRatingNode.getSkillNode().getName();
            QueryBuilder matchQueryBuilder100 = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm)
                    .operator(MatchQueryBuilder.Operator.AND)
                    .minimumShouldMatch("100%")
                    .boost(boostValue);
            boolQueryBuilder.should(matchQueryBuilder100);

            QueryBuilder matchQueryBuilder50 = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm)
                    .operator(MatchQueryBuilder.Operator.OR)
                    .minimumShouldMatch("70%")
                    .boost(boostValue / 2);
            boolQueryBuilder.should(matchQueryBuilder50);

//            String script = "_score + 12;";
//            FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(matchQueryBuilder)
//                    .add(ScoreFunctionBuilders.scriptFunction(script).param("rating", boostValue));
        }

        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        SearchQuery searchQuery = buildNativeQuery(functionBuilder, offset, limit);
        return searchQuery;
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
    public static SearchQuery buildBoolQueryAndBoostRatingField_9(Collection<SkillRatingNode> skillRatingNodes,
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
    public static SearchQuery buildBoolQueryAndBoostRatingFieldUsingEsUserSkills(
            Collection<EsUserSkills> skillRatingNodes,
            int offset,
            int limit) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (EsUserSkills esUserSkill : skillRatingNodes) {
            float boostValue = esUserSkill.getRating() * 10;
            if (esUserSkill.isParent()) {
                // for parent override.
                boostValue = esUserSkill.getRating();
            }
            String searchTerm = esUserSkill.getName();
            QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(Constants.DB_FIELD_CONTENT, searchTerm)
                    .operator(MatchQueryBuilder.Operator.OR).fuzziness(5).maxExpansions(DEFAULT_EXPANSION_VALUE)
                    .prefixLength(searchTerm.length() - 1).boost(boostValue);

            FunctionScoreQueryBuilder weightScoreQuery = QueryBuilders.functionScoreQuery(matchQueryBuilder)
                    .add(ScoreFunctionBuilders.weightFactorFunction(boostValue));
            boolQueryBuilder.should(weightScoreQuery);
        }

        FilteredQueryBuilder filteredQuery = QueryBuilders
                .filteredQuery(boolQueryBuilder, FilterBuilders.termFilter(Constants.DB_FIELD_READ, Boolean.FALSE));
        FunctionScoreQueryBuilder functionBuilder = QueryBuilders.functionScoreQuery(filteredQuery);
        functionBuilder.add(ScoreFunctionBuilders
                .exponentialDecayFunction(Constants.DB_FIELD_RECEIVED_DATE, DEFAULT_DECAY_FOR_RECEIVED_DATE));

        SearchQuery searchQuery = buildNativeQuery(functionBuilder, offset, limit);
        return searchQuery;
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
                        .operator(MatchQueryBuilder.Operator.OR).maxExpansions(DEFAULT_EXPANSION_VALUE);
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
