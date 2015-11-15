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

/**
 * A directive for adding google places autocomplete to a text box
 * google places autocomplete info: https://developers.google.com/maps/documentation/javascript/places
 *
 * Simple Usage:
 *
 * <input type="text" ng-autocomplete="result"/>
 *
 * creates the autocomplete text box and gives you access to the result
 *
 *   + `ng-autocomplete="result"`: specifies the directive, $scope.result will hold the textbox result
 *
 *
 * Advanced Usage:
 *
 * <input type="text" ng-autocomplete="result" details="details" options="options"/>
 *
 *   + `ng-autocomplete="result"`: specifies the directive, $scope.result will hold the textbox autocomplete result
 *
 *   + `details="details"`: $scope.details will hold the autocomplete's more detailed result; latlng. address components, etc.
 *
 *   + `options="options"`: options provided by the user that filter the autocomplete results
 *
 *      + options = {
 *           types: type,        string, values can be 'geocode', 'establishment', '(regions)', or '(cities)'
 *           bounds: bounds,     google maps LatLngBounds Object
 *           country: country    string, ISO 3166-1 Alpha-2 compatible country code. examples; 'ca', 'us', 'gb'
 *         }
 *
 *
 */

intelliJob.directive('ngAutocomplete', function ($parse) {
    return {
        scope: {
            details: '=',
            ngAutocomplete: '=',
            options: '='
        },

        link: function (scope, element, attrs, model) {

            //options for autocomplete
            var opts;

            //convert options provided to opts
            var initOpts = function () {
                opts = {};
                if (scope.options) {
                    if (scope.options.types) {
                        opts.types = [];
                        opts.types.push(scope.options.types)
                    }
                    if (scope.options.bounds) {
                        opts.bounds = scope.options.bounds
                    }
                    if (scope.options.country) {
                        opts.componentRestrictions = {
                            country: scope.options.country
                        }
                    }
                }
            };
            initOpts();

            //create new autocomplete
            //reinitializes on every change of the options provided
            var newAutocomplete = function () {
                scope.gPlace = ['Deutsch', 'Englisch', 'Russisch'];
                //google.maps.event.addListener(scope.gPlace, 'place_changed', function () {
                //    scope.$apply(function () {
                //        scope.details = scope.gPlace.getPlace();
                //        scope.ngAutocomplete = element.val();
                //    });
                //});
                //scope.gPlace = new google.maps.places.Autocomplete(element[0], opts);
                //google.maps.event.addListener(scope.gPlace, 'place_changed', function () {
                //    scope.$apply(function () {
                //        scope.details = scope.gPlace.getPlace();
                //        scope.ngAutocomplete = element.val();
                //    });
                //})
            };
            newAutocomplete();

            //watch options provided to directive
            scope.watchOptions = function () {
                return scope.options
            };
            scope.$watch(scope.watchOptions, function () {
                initOpts();
                newAutocomplete();
                element[0].value = '';
                scope.ngAutocomplete = element.val();
            }, true);
        }
    };
});
