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

/**
 * Mail utility class to parse mail data.
 */
public abstract class HtmlLinkParser {

    /**
     * Returns all html links.
     *
     * @param mailContent mail content.
     *
     * @return founded links.
     */
    public static List<HtmlLink> parse(final String mailContent) {
        List<HtmlLink> result = new ArrayList<>();

        Document doc = Jsoup.parse(mailContent);
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


}
