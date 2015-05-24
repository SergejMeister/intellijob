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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a help model to filter the links.
 */
public class HtmlLinkParseFilter {

    private Boolean textCanBeNull;
    private List<String> linkMatchers;

    public HtmlLinkParseFilter() {
        this.textCanBeNull = Boolean.TRUE;
        this.linkMatchers = new ArrayList<>();
    }

    public void setNullableText(Boolean textCanBeNull) {
        this.textCanBeNull = textCanBeNull;
    }

    public void setLinkMatchers(List<String> linkMatchers) {
        this.linkMatchers = linkMatchers;
    }

    public Boolean matches(HtmlLink htmlLink) {
        Boolean textFilterStatus = matchText(htmlLink.getValue());
        if (!textFilterStatus) {
            return Boolean.FALSE;
        }

        return matchLink(htmlLink.getHref());
    }

    public Boolean matchText(String linkText) {
        if (textCanBeNull) {
            //can be null, that means this filter is deactivated and always true.
            return Boolean.TRUE;
        }

        return StringUtils.hasLength(linkText);
    }

    public Boolean matchLink(String link) {
        if (linkMatchers.isEmpty()) {
            //is empty, that means this filter is deactivated and always true.
            return Boolean.TRUE;
        }

        for (String linkMatcher : linkMatchers) {
            if (link.contains(linkMatcher)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }
}
