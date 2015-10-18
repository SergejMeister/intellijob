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
        .factory('AuditServices', ['$http', '$rootScope',
            function ($http) {

                var urlBase = "/intellijob/api/audit";
                var auditServices = {};

                /**
                 * Get current audit data and history data with paging.
                 */
                auditServices.getAudit = function (pageIndex, limit) {
                    return $http.get(urlBase + "/" + pageIndex + "/" + limit);
                };

                /**
                 * POST-request to save current audit data.
                 */
                auditServices.save = function (currentAuditData) {
                    return $http.post(urlBase, currentAuditData);
                };

                /**
                 * DELETE-Request to delete history audit data by id.
                 */
                auditServices.deleteById = function (auditId) {
                    return $http.delete(urlBase + "/" + auditId);
                };

                return auditServices;
            }]);
