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

package com.intellijob.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Response mail data to transfer list of model object <code>Mail</code>.
 */
public class ResponseMailListData extends ResponseData {

    private List<ResponseMailData> mails;
    private Integer mailsCount;

    public ResponseMailListData() {
        this.mails = new ArrayList<>();
        this.mailsCount = 0;
    }

    public List<ResponseMailData> getMails() {
        return mails;
    }

    public void setMails(List<ResponseMailData> mails) {
        this.mails = mails;
    }

    public Integer getMailsCount() {
        return mailsCount;
    }

    public void setMailsCount(Integer mailsCount) {
        this.mailsCount = mailsCount;
    }
}
