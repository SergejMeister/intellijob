/*
 * Copyright 2015 Sergej Meister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
angular.module('intelliJob')
        .factory('MailServices', ['$http', '$rootScope',
            function ($http) {

                var urlBase = "/intellijob/api/mails";
                var mailServices = {};

                /**
                 * Search mails in mail box.
                 *
                 * @param requestMailData.
                 *
                 * @returns {HttpPromise}
                 */
                mailServices.searchMail = function (requestMailData) {
                    return $http.post(urlBase + '/search', requestMailData);
                };

                /**
                 * Get all mails.
                 *
                 * @returns {HttpPromise}
                 */
                mailServices.getMails = function () {
                    return $http.get(urlBase);
                };

                /**
                 * Get all job links.
                 *
                 * @returns {HttpPromise}
                 */
                mailServices.getMailPage = function (pageIndex, limit) {
                    return $http.get(urlBase + "/" + pageIndex + "/" + limit);
                };

                /**
                 * Get mail by given id.
                 *
                 * @returns {HttpPromise}
                 */
                mailServices.getMail = function (mailId) {
                    return $http.get(urlBase + "/" + mailId);
                };

                /**
                 * Delete mail by given id.
                 *
                 * @returns {HttpPromise}
                 */
                mailServices.deleteById = function (mailId) {
                    return $http.delete(urlBase + "/" + mailId);
                };


                return mailServices;
            }]);
