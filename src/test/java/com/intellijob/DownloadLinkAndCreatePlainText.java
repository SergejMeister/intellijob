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

import com.civis.utils.html.parser.HtmlParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class DownloadLinkAndCreatePlainText {


    public static void main(String[] args) {

        String link =
                "http://stellenanzeige.monster.de/SOFTWAREENTWICKLER-M-W-HUMAN-MACHINE-INTERFACE-HTML-C-C-Job-M%C3%BCnchen-Bayern-Deutschland-158791402.aspx?mescoid=1500127001001&jobPosition=18";
        try {
            Document doc = Jsoup.connect(link).get();
            String plainText = new HtmlParser(doc.html()).toPlainText().getContent();
            int test = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
