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

package com.intellijob.utility;

import com.intellijob.models.HtmlLink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to parse html links.
 */
public abstract class HtmlParser {

    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    /**
     * Returns all html links.
     *
     * @param htmlContent html content.
     *
     * @return founded links.
     */
    public static List<HtmlLink> parse(final String htmlContent) {
        List<HtmlLink> result = new ArrayList<>();

        Document doc = Jsoup.parse(htmlContent);
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String aTag = link.toString();
            String href = link.attr("abs:href");
            String text = link.text().trim();
            HtmlLink htmlLink = new HtmlLink(aTag, href, text);
            result.add(htmlLink);
        }
        return result;
    }

    /**
     * Returns all links matches for given filter.
     * <p>
     * Get all links and filter it.
     *
     * @param mailContent         mail content.
     * @param htmlLinkParseFilter filter.
     *
     * @return founded links.
     */
    public static List<HtmlLink> parse(final String mailContent, HtmlLinkParseFilter htmlLinkParseFilter) {
        List<HtmlLink> notFilteredResult = parse(mailContent);
        return doFilter(notFilteredResult, htmlLinkParseFilter);
    }

    private static List<HtmlLink> doFilter(List<HtmlLink> notFilteredResult, HtmlLinkParseFilter htmlLinkParseFilter) {
        List<HtmlLink> filteredResult = new ArrayList<>();
        for (HtmlLink htmlLink : notFilteredResult) {
            if (htmlLinkParseFilter.matches(htmlLink)) {
                filteredResult.add(htmlLink);
            }
        }
        return filteredResult;
    }

    /**
     * Returns all text data.
     *
     * @param htmlContent html content.
     *
     * @return text.
     */
    public static String parseToText(final String htmlContent) {


        //List<String> sentences = new ArrayList<>();
        Document doc = Jsoup.parse(htmlContent);
        String plainTextWithHTags = doc.text();
        String plainText = removeTags(plainTextWithHTags);
        String[] words = plainText.split(" ");
        int wordCountPerSentence = 20;
        int wordCount = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words) {
            if (word.length() > 1) {
                stringBuilder.append(word);
                stringBuilder.append(" ");
                wordCount++;
            }
            if (wordCount == wordCountPerSentence) {
                stringBuilder.append("\n");
                wordCount = 0;
            }

        }

        return stringBuilder.toString();
//        Elements elements = doc.body().select("*");
//        Set<String> textNodes = new HashSet<>();
//        for (Element element : elements) {
//            for (Node child : element.childNodes()) {
//                child.h
//                if (child instanceof TextNode) {
//                    String textNode = ((TextNode)child).text();
//                    //String textNode = element.
//                    if (textNode.contains(".")) {
//                        String[] sentences = textNode.split(".");
//                        for(int i=0; i<sentences.length; i++){
//                            textNodes.add(sentences[i].trim());
//                        }
//                    }else{
//                        textNodes.add(textNode.trim());
//                    }
//
//                }
//            }
//        }
//
//        for (String textNode : textNodes) {
//            stringBuilder.append(textNode);
//            stringBuilder.append("\n");
//        }
//        return stringBuilder.toString();
    }

    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll("");
    }

//    public static String parseToText(final String htmlContent) {
//
//        StringBuilder stringBuilder = new StringBuilder();
//        //List<String> sentences = new ArrayList<>();
//        Document doc = Jsoup.parse(htmlContent);
//        Elements elements = doc.body().select("*");
//
//        for (Element element : elements) {
//
//            String sentence = element.ownText();
//            if (isValidText(sentence)) {
//                String trimSentence = sentence.trim();
//                stringBuilder.append(trimSentence);
//                if (!trimSentence.endsWith(".")) {
//                    stringBuilder.append(".");
//                }
//                stringBuilder.append("\n");
//            }
//        }
//        return stringBuilder.toString();
//    }

    public static Boolean isValidText(String text) {
        String result = text.replaceAll("[-+.^:,|<>{}]", "");
        if (result.isEmpty()) {
            return Boolean.FALSE;
        }


        return Boolean.TRUE;
    }

}
