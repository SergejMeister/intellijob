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
intelliJob.directive('starRating', function () {
            return {
                restrict: 'A',
                template: '<ul class="rating">'
                + '	<li ng-repeat="star in stars" ng-class="star" ng-click="starDisabled || toggle($index)">'
                + '\u2605'
                + '</li>'
                + '</ul>',
                scope: {
                    ratingValue: '=',
                    max: '=',
                    onRatingSelected: '&',
                    starDisabled: '='
                },
                link: function (scope, elem, attrs) {
                    if (!scope.starDisabled) {
                        scope.starDisabled = false;
                    }
                    var updateStars = function () {
                        scope.stars = [];
                        for (var i = 0; i < scope.max; i++) {
                            scope.stars.push({
                                filled: i < scope.ratingValue
                            });
                        }
                    };

                    scope.toggle = function (index) {
                        scope.ratingValue = index + 1;
                        scope.onRatingSelected({
                            rating: index + 1
                        });
                    };

                    scope.$watch('ratingValue',
                            function (oldVal, newVal) {
                                if (newVal) {
                                    updateStars();
                                }
                            }
                    );
                }
            };
        }
);