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

package com.intellijob.repository;

import com.intellijob.domain.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for domain object mail.
 */
public interface MailRepository extends MongoRepository<Mail, String> {

    /**
     * Delete a mail.
     *
     * @param mail mail to delete.
     */
    void delete(Mail mail);

    /**
     * Returns all mails.
     *
     * @return list of mails.
     */
    List<Mail> findAll();

    //Mail findById(String id);

//    /**
//     * Returns all mails with paging.
//     *
//     * @param pageable current page.
//     *
//     * @return page with mails.
//     */
//    Page findAll(Pageable pageable);

    /**
     * Find mail by given id.
     *
     * @param id mails id.
     *
     * @return optional mail object.
     */
    Mail findOne(String id);

    /**
     * Save mail.
     * <p>
     * If mail does not exist, than create a new mail.
     * If mail exists, than update mail.
     *
     * @param mail mail to create or to update.
     *
     * @return created mail with id or updated mail.
     */
    Mail save(Mail mail);
}
