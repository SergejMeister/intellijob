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

import com.intellijob.BaseTester;
import com.intellijob.models.HtmlLink;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Test util html link parser.
 */
public class HtmlParserTest extends BaseTester {

    public static final String MONSTER_MAIL_PATH = "mails/monsterjobs.html";
    public static final String STEPSTONE_MAIL_PATH = "mails/stepstonejobs.html";

    @Test
    public void testFindAllHtmlLinksInMonserMail() {
        String monsterMailContent = getMailContent(MONSTER_MAIL_PATH);
        List<HtmlLink> links = HtmlParser.parse(monsterMailContent);
        Assert.assertEquals("This mail content include only 20 links!", 20, links.size());
    }

    @Test
    public void testFindHtmlLinksInMonsterMailWithFilter() {
        String monsterMailContent = getMailContent(MONSTER_MAIL_PATH);
        List<String> linkMatchers = Collections.singletonList("stellenanzeige.monster.de");
        HtmlLinkParseFilter htmlLinkParseFilter = getHtmlParseFilter(linkMatchers);
        List<HtmlLink> links = HtmlParser.parse(monsterMailContent, htmlLinkParseFilter);
        Assert.assertEquals("This mail content include only 4 job links!", 4, links.size());
    }

    @Test
    public void testFindAllHtmlLinksInStepStoneMail() {
        String stepStoneMailContent = getMailContent(STEPSTONE_MAIL_PATH);
        List<HtmlLink> links = HtmlParser.parse(stepStoneMailContent);
        Assert.assertEquals("This mail content include only 32 links!", 32, links.size());
    }

    @Test
    public void testFindHtmlLinksInStepStoneMailWithFilter() {
        String stepStoneMailContent = getMailContent(STEPSTONE_MAIL_PATH);
        //ja.cfm in url means in stepstone mail job agent!
        List<String> linkMatchers = Collections.singletonList("www.stepstone.de/ja.cfm");
        HtmlLinkParseFilter htmlLinkParseFilter = getHtmlParseFilter(linkMatchers);
        List<HtmlLink> links = HtmlParser.parse(stepStoneMailContent, htmlLinkParseFilter);
        Assert.assertEquals("This mail content include only 16 job links!", 16, links.size());
    }

    private String getMailContent(String fileName) {
        String mailContent = "";
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            mailContent = IOUtils.toString(inputStream, "UTF-8");
            inputStream.close();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return mailContent;
    }

    private HtmlLinkParseFilter getHtmlParseFilter(List<String> linkMatchers) {
        HtmlLinkParseFilter htmlLinkParseFilter = new HtmlLinkParseFilter();
        htmlLinkParseFilter.setNullableText(Boolean.FALSE);
        htmlLinkParseFilter.setLinkMatchers(linkMatchers);
        return htmlLinkParseFilter;
    }
}
