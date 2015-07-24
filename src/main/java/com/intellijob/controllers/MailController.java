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

package com.intellijob.controllers;

import com.intellijob.domain.Mail;
import com.intellijob.exceptions.BaseException;
import com.intellijob.mail.models.MailModel;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface mail controller.
 * <p>
 * This is a layer between services and dao.
 */
public interface MailController {

    /**
     * Save mail model.
     * <p>
     * Convert mail model to domain object mail and save.
     * If mail does not exist, than create a new mail.
     * If mail exists, than update mail.
     *
     * @param mailModel mail model to create or to update.
     *
     * @return created or updated mail with id.
     */
    Mail saveModel(MailModel mailModel);

    /**
     * Save list of mail models.
     * <p>
     * Convert mail model to domain object mail and save.
     * If mail does not exist, than create a new mail.
     * If mail exists, than update mail.
     *
     * @param mailModel mail model to create or to update.
     *
     * @return list of created or updated mails.
     */
    List<Mail> saveModel(List<MailModel> mailModel);

    /**
     * Returns mail by given mail id.
     *
     * @param mailId mail id.
     *
     * @return founded mail.
     */
    Mail findMail(String mailId) throws BaseException;

    /**
     * Returns all mails order by received date.
     *
     * @return list of mails.
     */
    List<Mail> findAll();

    /**
     * Returns mail page.
     *
     * @param pageIndex page index.
     * @param limit     limit mails per page.
     *
     * @return page of mails.
     */
    Page<Mail> findPage(int pageIndex, int limit);

    /**
     * Delete mail by specified id.
     * @param mailId mailId to delete.
     *
     * @return deleted Mail.
     *
     * @throws BaseException exception, if no mail found for specified id.
     */
    Mail deleteById(String mailId) throws BaseException;
}
