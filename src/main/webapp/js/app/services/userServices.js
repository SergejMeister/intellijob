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

angular.module('intelliJob')
        .factory('UserServices', ['$http', '$rootScope',
            function ($http) {

                var urlBase = '/intellijob/api/users';

                var urlViewsBase = '/intellijob/api/views/users';

                var userServices = {};

                /**
                 * Get user.
                 *
                 * @returns {HttpPromise}
                 */
                userServices.getUser = function () {
                    return $http.get(urlBase);
                };

                /**
                 * Get user by id.
                 *
                 * @returns {HttpPromise}
                 */
                userServices.getUserById = function (userId) {
                    return $http.get(urlBase + '/' + userId);
                };

                /**
                 * Get user by id.
                 *
                 * @returns {HttpPromise}
                 */
                userServices.getViewUserModelById = function (userId) {
                    return $http.get(urlViewsBase + '/' + userId);
                };

                /**
                 * Post and create a new user.
                 *
                 * @returns {HttpPromise}
                 */
                userServices.createUser = function (newUser) {
                    return $http.post(urlBase, newUser);
                };

                /**
                 * Put and update a new user.
                 *
                 * @returns {HttpPromise}
                 */
                userServices.updateUser = function (user) {
                    return $http.put(urlBase, user);
                };

                /**
                 * Delete a user by id.
                 *
                 * @returns {HttpPromise}
                 */
                userServices.deleteUserById = function (userId) {
                    var params = {userId: userId};
                    return $http.delete(urlBase, params);
                };

                return userServices;
            }]);