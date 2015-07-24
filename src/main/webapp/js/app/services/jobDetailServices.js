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
        .factory('JobDetailServices', ['$http', '$rootScope',
            function ($http) {

                var urlBase = "/intellijob/api/jobdetails";
                var jobDetailServices = {};

                /**
                 * Get all jobDetails.
                 *
                 * @returns {HttpPromise}
                 */
                jobDetailServices.getAllJobDetails = function () {
                    return $http.get(urlBase);
                };

                /**
                 * Get page with fixed size of jobDetails.
                 *
                 * @returns {HttpPromise}
                 */
                jobDetailServices.getJobDetailPage = function (pageIndex, limit) {
                    return $http.get(urlBase + "/" + pageIndex + "/" + limit);
                };

                /**
                 * Get jobDetail by id.
                 *
                 * @returns {HttpPromise}
                 */
                jobDetailServices.getJobDetailById = function (jobDetailId) {
                    return $http.get(urlBase + "/" + jobDetailId);
                };

                /**
                 * Delete jobDetail by id.
                 *
                 * @returns {HttpPromise}
                 */
                jobDetailServices.deleteById = function (jobDetailId) {
                    return $http.delete(urlBase + "/" + jobDetailId);
                };


                return jobDetailServices;
            }]);
