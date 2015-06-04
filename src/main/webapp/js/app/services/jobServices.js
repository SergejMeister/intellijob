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
        .factory('JobServices', ['$http', '$rootScope',
            function ($http) {

                var urlBase = "/intellijob/api/jobs";
                var jobServices = {};

                /**
                 * Get all mails.
                 *
                 * @returns {HttpPromise}
                 */
                jobServices.getAllJobs = function () {
                    return $http.get(urlBase);
                };

                /**
                 * Get all job links.
                 *
                 * @returns {HttpPromise}
                 */
                jobServices.getJobPage = function (pageIndex, limit) {
                    return $http.get(urlBase + "/" + pageIndex + "/" + limit);
                };

                /**
                 * Get job by id.
                 *
                 * @returns {HttpPromise}
                 */
                jobServices.getJobById = function (jobId) {
                    return $http.get(urlBase + "/" + jobId);
                };

                /**
                 * Run extract service.
                 *
                 * This service analyse the job content and extract specific information.
                 * @param jobId affected job id.
                 * @returns {HttpPromise}
                 */
                jobServices.extractDataById = function (jobId) {
                    var extractUrl = urlBase + "/" + jobId + "/extract";
                    var emptyPayLoad = {}
                    return $http.post(extractUrl, emptyPayLoad);
                }

                return jobServices;
            }]);
