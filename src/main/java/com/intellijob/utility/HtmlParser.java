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
import java.util.stream.Collectors;

/**
 * Utility class to parseLink html links.
 */
public abstract class HtmlParser {

    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");
    private static final Pattern MAIL_PATTERN = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
    private static final String RESOURCE_NAME_PATTERN = "[\\w\\d.:#@%/;$()~_?\\+-=&]*";
    private static final String WWW_PATTERN = "www\\." + RESOURCE_NAME_PATTERN;
    private static final String HTTP_PATTERN = "http://" + RESOURCE_NAME_PATTERN;
    private static final String HTTPS_PATTERN = "https://" + RESOURCE_NAME_PATTERN;
    private static final Pattern WEB_URL_PATTERN =
            Pattern.compile(HTTP_PATTERN + "|" + HTTPS_PATTERN + "|" + WWW_PATTERN);

    /**
     * Returns all html links.
     *
     * @param htmlContent html content.
     *
     * @return founded links.
     */
    public static List<HtmlLink> parseLink(final String htmlContent) {
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
    public static List<HtmlLink> parseLink(final String mailContent, HtmlLinkParseFilter htmlLinkParseFilter) {
        List<HtmlLink> notFilteredResult = parseLink(mailContent);
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
    public static String toPlainText(final String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        String plainTextWithHTags = doc.text();
        String plainText = removeTags(plainTextWithHTags);
        String[] words = plainText.split(" ");
        int wordCountPerSentence = 25;
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
    }

    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll("");
    }

    public static String parseEmail(String plainText) {
        Matcher m = MAIL_PATTERN.matcher(plainText);
        while (m.find()) {
            return m.group();
        }
        return null;
    }


    public static List<String> extractUrls(String value) {
        List<String> result = new ArrayList<>();
        Matcher m = WEB_URL_PATTERN.matcher(value);
        while (m.find()) {
            result.add(value.substring(m.start(0), m.end(0)));
        }
        return removeDuplicated(result);
    }

    private static List<String> removeDuplicated(List<String> urls) {
        return urls.parallelStream().distinct().collect(Collectors.toList());
    }

}
