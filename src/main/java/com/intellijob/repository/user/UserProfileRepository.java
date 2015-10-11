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

package com.intellijob.repository.user;

import com.intellijob.domain.User;
import org.springframework.data.mongodb.repository.Query;

/**
 * Repository for user profile.
 * <p>
 * Include only methods to handle user profile data.
 */
public interface UserProfileRepository extends UserRepository {

    /**
     * Returns user object with userid and profile data.
     *
     * @param userId affected user id.
     *
     * @return user with userId and profile data.
     */
    @Query(value = "{ 'id' : ?0 }", fields = "{ 'profile' : 1}")
    User findByUserId(String userId);
}
