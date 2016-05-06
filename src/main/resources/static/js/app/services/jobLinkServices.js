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
        .factory('JobLinkServices', ['$http', '$rootScope',
            function ($http) {

                var urlBase = "api/joblinks";
                var jobLinkServices = {};

                /**
                 * Get all job links.
                 *
                 * @returns {HttpPromise}
                 */
                jobLinkServices.getJobLinks = function () {
                    return $http.get(urlBase);
                };

                /**
                 * Get all job links.
                 *
                 * @returns {HttpPromise}
                 */
                jobLinkServices.getJobLinksPage = function (pageIndex, limit) {
                    return $http.get(urlBase + "/" + pageIndex + "/" + limit);
                };

                /**
                 * Download job links and return downloaded job object.
                 *
                 * @returns {HttpPromise}
                 */
                jobLinkServices.downloadById = function (jobLinkId) {
                    var downloadUrl = urlBase + "/" + jobLinkId;
                    var emptyPayLoad = {};
                    return $http.put(downloadUrl, emptyPayLoad);
                };

                /**
                 * Download job links and return downloaded job object.
                 *
                 * @returns {HttpPromise}
                 */
                jobLinkServices.downloadAll = function () {
                    var emptyPayLoad = {};
                    return $http.put(urlBase, emptyPayLoad);
                };

                /**
                 * Delete jobLink by id.
                 *
                 * @returns {HttpPromise}
                 */
                jobLinkServices.deleteById = function (jobLinkId) {
                    return $http.delete(urlBase + "/" + jobLinkId);
                };

                return jobLinkServices;
            }]);