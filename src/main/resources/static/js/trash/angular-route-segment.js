'use strict';
(function(angular) {

var mod = angular.module( 'route-segment', [] );
mod.provider( '$routeSegment',
        ['$routeProvider', function $routeSegmentProvider ($routeProvider) {

    var $routeSegmentProvider = this;
    var options = $routeSegmentProvider.options = {
        autoLoadTemplates: true,
        strictMode: false
    };

    var segments = this.segments = {},
        rootPointer = pointer(segments, null),
        segmentRoutes = {};

    function camelCase(name) {
        return name.replace(/([\:\-\_]+(.))/g, function(_, separator, letter, offset) {
            return offset ? letter.toUpperCase() : letter;
        });
    }

    function pointer(segment, parent) {

        if(!segment)
            throw new Error('Invalid pointer segment');

        var lastAddedName;

        return {

            segment: function(name, params) {
                segment[camelCase(name)] = {name: name, params: params};
                lastAddedName = name;
                return this;
            },

            within: function(childName) {
                var child;
                childName = childName || lastAddedName;

                if(child = segment[camelCase(childName)]) {
                    if(child.children == undefined)
                        child.children = {};
                }
                else {
                    if(options.strictMode)
                        throw new Error('Cannot get into unknown `'+childName+'` segment');
                    else {
                        child = segment[camelCase(childName)] = {params: {}, children: {}};
                    }
                }
                return pointer(child.children, this);
            },

            up: function() {
                return parent;
            },

            root: function() {
                return rootPointer;
            }
        }
    }

    $routeSegmentProvider.when = function(path, name, route) {
        if (route == undefined)
            route = {};
        route.segment = name;

        $routeProvider.when(path, route);
        segmentRoutes[name] = path;
        return this;
    };

    // Extending the provider with the methods of rootPointer
    // to start configuration.
    angular.extend($routeSegmentProvider, rootPointer);

    this.$get = ['$rootScope', '$q', '$http', '$templateCache', '$route', '$routeParams', '$injector',
                 function($rootScope, $q, $http, $templateCache, $route, $routeParams, $injector) {

        var $routeSegment = {

                name: '',
                $routeParams: angular.copy($routeParams),
                chain: [],

                startsWith: function (val) {
                    var regexp = new RegExp('^'+val);
                    return regexp.test($routeSegment.name);
                },

                /**
                 * @ngdoc method
                 * @name $routeSegment#contains
                 * @param {String} val segment name to test
                 * @returns {Boolean} true if given segment present in the current route
                 * @description Helper method for checking whether current route contains the given string
                 */
                contains: function (val) {
                    for(var i=0; i<this.chain.length; i++)
                        if(this.chain[i] && this.chain[i].name == val)
                            return true;
                    return false;
                },

                /**
                 * @ngdoc method
                 * @name $routeSegment#getSegmentUrl
                 * @param {String} segmentName The name of a segment as defined in `when()`
                 * @param {Object} routeParams Route params hash to be put into route URL template
                 * @returns {String} segment url
                 * @throws {Error} if url for the given name is not found
                 * @description A method for reverse routing which can return the route URL for the specified segment name
                 */
                getSegmentUrl: function(segmentName, routeParams) {
                    var url, i, m;
                    if(!segmentRoutes[segmentName])
                        throw new Error('Can not get URL for segment with name `'+segmentName+'`');

                    routeParams = angular.extend({}, $routeParams, routeParams || {});

                    url = segmentRoutes[segmentName];
                    for(i in routeParams) {
                        var regexp = new RegExp('\:'+i+'[\*\?]?','g');
                        url = url.replace(regexp, routeParams[i]);
                    }
                    url = url.replace(/\/\:.*?\?/g, '');

                    if(m = url.match(/\/\:([^\/]*)/))
                        throw new Error('Route param `'+m[1]+'` is not specified for route `'+segmentRoutes[segmentName]+'`');

                    return url;
                }
        };

        var resolvingSemaphoreChain = {};

        // When a route changes, all interested parties should be notified about new segment chain
        $rootScope.$on('$routeChangeSuccess', function(event, args) {

            var route = args.$route || args.$$route; 
            if(route && route.segment) {

                var segmentName = route.segment;
                var segmentNameChain = segmentName.split(".");
                var updates = [], lastUpdateIndex = -1;

                for(var i=0; i < segmentNameChain.length; i++) {
                    
                    var newSegment = getSegmentInChain( i, segmentNameChain );

                    if(resolvingSemaphoreChain[i] != newSegment.name || updates.length > 0 || isDependenciesChanged(newSegment)) {

                        if($routeSegment.chain[i] && $routeSegment.chain[i].name == newSegment.name &&
                            updates.length == 0 && !isDependenciesChanged(newSegment))
                            // if we went back to the same state as we were before resolving new segment
                            resolvingSemaphoreChain[i] = newSegment.name;
                        else {
                            updates.push({index: i, newSegment: newSegment});
                            lastUpdateIndex = i;
                        }
                    }
                }

                var curSegmentPromise = $q.when();

                if(updates.length > 0) {

                    for(var i=0; i<updates.length; i++) {
                        (function(i) {
                            curSegmentPromise = curSegmentPromise.then(function() {

                                return updateSegment(updates[i].index, updates[i].newSegment);

                            }).then(function(result) {

                                if(result.success != undefined) {

                                    broadcast(result.success);

                                    for(var j = updates[i].index + 1; j < $routeSegment.chain.length; j++) {

                                        if($routeSegment.chain[j]) {
                                            if ($routeSegment.chain[j].clearWatcher) {
                                                $routeSegment.chain[j].clearWatcher();
                                            }
                                            
                                            $routeSegment.chain[j] = null;
                                            updateSegment(j, null);
                                        }
                                    }
                                }
                            })
                        })(i);
                    }
                }

                curSegmentPromise.then(function() {

                    // Removing redundant segment in case if new segment chain is shorter than old one
                    if($routeSegment.chain.length > segmentNameChain.length) {
                        var oldLength = $routeSegment.chain.length;
                        var shortenBy = $routeSegment.chain.length - segmentNameChain.length;
                        $routeSegment.chain.splice(-shortenBy, shortenBy);
                        for(var i=segmentNameChain.length; i < oldLength; i++) {
                            updateSegment(i, null);
                            lastUpdateIndex = $routeSegment.chain.length-1;
                        }
                    }
                }).then(function() {

                    var defaultChildUpdatePromise = $q.when();

                    if(lastUpdateIndex == $routeSegment.chain.length-1) {

                        var curSegment = getSegmentInChain(lastUpdateIndex, $routeSegment.name.split("."));

                        while(curSegment) {
                            var children = curSegment.children, index = lastUpdateIndex+1;
                            curSegment = null;
                            for (var i in children) {
                                (function(i, children, index) {
                                    if (children[i].params['default']) {
                                        defaultChildUpdatePromise = defaultChildUpdatePromise.then(function () {
                                            return updateSegment(index, {name: children[i].name, params: children[i].params})
                                                .then(function (result) {
                                                    if (result.success) broadcast(result.success);
                                                });
                                        });
                                        curSegment = children[i];
                                        lastUpdateIndex = index;
                                    }
                                })(i, children, index);


                            }
                        }
                    }

                    return defaultChildUpdatePromise;
                });
            }
        });

        function isDependenciesChanged(segment) {

            var result = false;
            if(segment.params.dependencies)
                angular.forEach(segment.params.dependencies, function(name) {
                    if(!angular.equals($routeSegment.$routeParams[name], $routeParams[name]))
                        result = true;
                });
            return result;
        }

        function updateSegment(index, segment) {

            if($routeSegment.chain[index] && $routeSegment.chain[index].clearWatcher) {
                $routeSegment.chain[index].clearWatcher();
            }

            if(!segment) {
                resolvingSemaphoreChain[index] = null;
                broadcast(index);
                return;
            }

            resolvingSemaphoreChain[index] = segment.name;

            if(segment.params.untilResolved) {
                return resolve(index, segment.name, segment.params.untilResolved)
                    .then(function(result) {
                        if(result.success != undefined)
                            broadcast(index);
                        return resolve(index, segment.name, segment.params);
                    })
            }
            else
                return resolve(index, segment.name, segment.params);
        }

        function resolve(index, name, params) {

            var locals = angular.extend({}, params.resolve);

            angular.forEach(locals, function(value, key) {
                locals[key] = angular.isString(value) ? $injector.get(value) : $injector.invoke(value);
            });

            if(params.template) {

                locals.$template = params.template;
                if(angular.isFunction(locals.$template))
                    locals.$template = $injector.invoke(locals.$template);
            }

            if(options.autoLoadTemplates && params.templateUrl) {

                locals.$template = params.templateUrl;
                if(angular.isFunction(locals.$template))
                    locals.$template = $injector.invoke(locals.$template);

                locals.$template =
                    $http.get(locals.$template, {cache: $templateCache})
                        .then(function (response) {
                            return response.data;
                        });
            }

            return $q.all(locals).then(

                    function(resolvedLocals) {

                        if(resolvingSemaphoreChain[index] != name)
                            return $q.reject();

                        /**
                         * @ngdoc type
                         * @module route-segment
                         * @name $routeSegment.Segment
                         * @description Segment record
                         */
                        $routeSegment.chain[index] = {
                                /**
                                 * @ngdoc property
                                 * @name $routeSegment.Segment#name
                                 * @type {String}
                                 * @description segment name as registered with {@link $routeSegmentProvider#segment $routeSegmentProvider#segment}
                                 */
                                name: name,
                                /**
                                 * @ngdoc property
                                 * @name $routeSegment.Segment#params
                                 * @type {Object=}
                                 * @description [$routeParams](https://docs.angularjs.org/api/ngRoute/provider/$routeProvider) parameters for segment
                                 */
                                params: params,
                                /**
                                 * @ngdoc property
                                 * @name $routeSegment.Segment#locals
                                 * @type {Object=}
                                 * @description resolved segment data
                                 */
                                locals: resolvedLocals,
                                /**
                                 * @ngdoc method
                                 * @name $routeSegment.Segment#reload
                                 * @description reloads current segment from scratch
                                 */
                                reload: function() {
                                    var originalSegment = getSegmentInChain(index, $routeSegment.name.split("."));
                                    updateSegment(index, originalSegment).then(function(result) {
                                        if(result.success != undefined)
                                            broadcast(index);
                                    })
                                }
                            };

                        if(params.watcher) {

                            var getWatcherValue = function() {
                                if(!angular.isFunction(params.watcher) && !angular.isArray(params.watcher))
                                    throw new Error('Watcher is not a function in segment `'+name+'`');

                                return $injector.invoke(
                                    params.watcher,
                                    {},
                                    {segment: $routeSegment.chain[index]});
                            }

                            var lastWatcherValue = getWatcherValue();

                            $routeSegment.chain[index].clearWatcher = $rootScope.$watch(
                                getWatcherValue,
                                function(value) {
                                    if(value == lastWatcherValue) // should not being run when $digest-ing at first time
                                        return;
                                    lastWatcherValue = value;
                                    $routeSegment.chain[index].reload();
                                })
                        }

                        return {success: index};
                    },

                    function(error) {

                        if(params.resolveFailed) {
                            var newResolve = {error: function() { return $q.when(error); }};
                            return resolve(index, name, angular.extend({resolve: newResolve}, params.resolveFailed));
                        }
                        else
                            throw new Error('Resolving failed with a reason `'+error+'`, but no `resolveFailed` ' +
                                            'provided for segment `'+name+'`');
                    })
        }

        function broadcast(index) {

            $routeSegment.$routeParams = angular.copy($routeParams);

            $routeSegment.name = '';
            for(var i=0; i<$routeSegment.chain.length; i++)
                if($routeSegment.chain[i])
                    $routeSegment.name += $routeSegment.chain[i].name+".";
            $routeSegment.name = $routeSegment.name.substr(0, $routeSegment.name.length-1);

            /**
             * @ngdoc event
             * @name $routeSegment#routeSegmentChange
             * @eventType broadcast on $rootScope
             * @param {Object} object object containing
             * - `index` {number} index id in {@link $routeSegment#chain $routeSegment#chain}
             * - segment {$routeSegment.Segment|Null} current segment
             * @description event is thrown when segment is loaded
             */
            $rootScope.$broadcast( 'routeSegmentChange', {
                index: index,
                segment: $routeSegment.chain[index] || null } );
        }

        function getSegmentInChain(segmentIdx, segmentNameChain) {

            if(!segmentNameChain)
                return null;

            if(segmentIdx >= segmentNameChain.length)
                return null;

            var curSegment = segments, nextName;
            for(var i=0;i<=segmentIdx;i++) {

                nextName = segmentNameChain[i];

                if(curSegment[ camelCase(nextName) ] != undefined)
                    curSegment = curSegment[ camelCase(nextName) ];

                if(i < segmentIdx)
                    curSegment = curSegment.children;
            }

            return {
                name: nextName,
                params: curSegment.params,
                children: curSegment.children
            };
        }

        return $routeSegment;
    }];
}]);

/**
 * @ngdoc filter
 * @module route-segment
 * @name routeSegmentUrl
 * @param {String} name fully qualified segment name
 * @param {Object} params params to resolve segment
 * @returns {string} given url
 * @description Returns url for a given segment
 *
 * Usage:
 * ```html
 * <a ng-href="{{ 'index.list' | routeSegmentUrl }}">
 * <a ng-href="{{ 'index.list.itemInfo' | routeSegmentUrl: {id: 123} }}">
 * ```
 */
mod.filter('routeSegmentUrl', ['$routeSegment', function($routeSegment) {
    var filter = function(segmentName, params) {
        return $routeSegment.getSegmentUrl(segmentName, params);
    };
    filter.$stateful = true;
    return filter;
}]);

/**
 * @ngdoc filter
 * @module route-segment
 * @name routeSegmentEqualsTo
 * @param {String} name fully qualified segment name
 * @returns {boolean} true if given segment name is the active one
 * @description Check whether active segment equals to the given segment name
 *
 * Usage:
 * ```html
 * <li ng-class="{active: ('index.list' | routeSegmentEqualsTo)}">
 * ```
 */
mod.filter('routeSegmentEqualsTo', ['$routeSegment', function($routeSegment) {
    var filter = function(value) {
        return $routeSegment.name == value;
    };
    filter.$stateful = true;
    return filter;
}]);

/**
 * @ngdoc filter
 * @module route-segment
 * @name routeSegmentStartsWith
 * @param {String} name segment name
 * @returns {boolean} true if active segment name begins with given name
 * @description Check whether active segment starts with the given segment name
 *
 * Usage:
 * ```html
 * <li ng-class="{active: ('section1' | routeSegmentStartsWith)}">
 * ```
 */
mod.filter('routeSegmentStartsWith', ['$routeSegment', function($routeSegment) {
    var filter = function(value) {
        return $routeSegment.startsWith(value);
    };
    filter.$stateful = true;
    return filter;
}]);

/**
 * @ngdoc filter
 * @module route-segment
 * @name routeSegmentContains
 * @param {String} name segment name
 * @returns {boolean} true if active segment contains given name
 * @description Check whether active segment contains the given segment name
 *
 * Usage:
 * ```html
 * <li ng-class="{active: ('itemInfo' | routeSegmentContains)}">
 * ```
 */
mod.filter('routeSegmentContains', ['$routeSegment', function($routeSegment) {
    var filter = function(value) {
        return $routeSegment.contains(value);
    };
    filter.$stateful = true;
    return filter;
}]);

/**
 * @ngdoc filter
 * @module route-segment
 * @name routeSegmentParam
 * @param {String} name param name
 * @returns {string|undefined} param value or undefined
 * @description Returns segment parameter by name
 *
 * Usage:
 * ```html
 * <li ng-class="{active: ('index.list.itemInfo' | routeSegmentEqualsTo) && ('id' | routeSegmentParam) == 123}">
 * ```
 */
mod.filter('routeSegmentParam', ['$routeSegment', function($routeSegment) {
    var filter = function(value) {
        return $routeSegment.$routeParams[value];
    };
    filter.$stateful = true;
    return filter;
}]);


})(angular);;'use strict';

/**
 * @ngdoc module
 * @module view-segment
 * @name view-segment
 * @packageName angular-route-segment
 * @requires route-segment
 * @description
 * view-segment is a replacement for [ngView](https://docs.angularjs.org/api/ngRoute/directive/ngView) AngularJS directive.
 *
 * {@link appViewSegment appViewSegment} tags in the DOM will be populated with the corresponding route segment item.
 * You must provide a segment index as an argument to this directive to make it aware about which segment level in the tree
 * it should be linked to.
 *
 * *index.html*:
 * ```html
 * <ul>
 *     <li ng-class="{active: $routeSegment.startsWith('s1')}">
 *         <a href="/section1">Section 1</a>
 *     </li>
 *     <li ng-class="{active: $routeSegment.startsWith('s2')}">
 *         <a href="/section2">Section 2</a>
 *     </li>
 * </ul>
 * <div id="contents" app-view-segment="0"></div>
 * ```
 *
 * *section1.html*: (it will be loaded to div#contents in index.html)
 * ```html
 * <h4>Section 1</h4>
 * Section 1 contents.
 * <div app-view-segment="1"></div>
 * ```
 *
 * ...etc. You can reach any nesting level here. Every view will be handled independently, keeping the state of top-level views.
 *
 * You can also use filters to define link hrefs. It will resolve segment URLs automatically:
 *
 * ```html
 * <ul>
 *     <li ng-class="{active: ('s1' | routeSegmentStartsWith)}">
 *         <a href="{{ 's1' | routeSegmentUrl }}">Section 1</a>
 *     </li>
 *     <li ng-class="{active: ('s2' | routeSegmentStartsWith)}">
 *         <a href="{{ 's2' | routeSegmentUrl }}">Section 2</a>
 *     </li>
 * </ul>
 * ```
 */
/**
 * @ngdoc directive
 * @module view-segment
 * @name appViewSegment
 * @requires https://docs.angularjs.org/api/ngRoute/service/$route $route
 * @requires https://docs.angularjs.org/api/ng/service/$compile $compile
 * @requires https://docs.angularjs.org/api/ng/service/$controller $controller
 * @requires https://docs.angularjs.org/api/ngRoute/service/$routeParams $routeParams
 * @requires $routeSegment
 * @requires https://docs.angularjs.org/api/ng/service/$q $q
 * @requires https://docs.angularjs.org/api/auto/service/$injector $injector
 * @requires https://docs.angularjs.org/api/ng/service/$timeout $timeout
 * @requires https://docs.angularjs.org/api/ng/service/$animate $animate
 * @restrict ECA
 * @priority 400
 * @param {String} appViewSegment render depth level
 * @description Renders active segment as specified by parameter
 *
 * It is based on [ngView directive code](https://github.com/angular/angular.js/blob/master/src/ngRoute/directive/ngView.js)
 */

(function(angular) {

    angular.module( 'view-segment', [ 'route-segment' ] ).directive( 'appViewSegment',
    ['$route', '$compile', '$controller', '$routeParams', '$routeSegment', '$q', '$injector', '$timeout', '$animate',
        function($route, $compile, $controller, $routeParams, $routeSegment, $q, $injector, $timeout, $animate) {

            return {
                restrict : 'ECA',
                priority: 400,
                transclude: 'element',

                compile : function(tElement, tAttrs) {

                    return function($scope, element, attrs, ctrl, $transclude) {

                        var currentScope, currentElement, currentSegment = {}, onloadExp = tAttrs.onload || '',
                        viewSegmentIndex = parseInt(tAttrs.appViewSegment), updatePromise, previousLeaveAnimation;

                        if($routeSegment.chain[viewSegmentIndex]) {
                            updatePromise = $timeout(function () {
                                update($routeSegment.chain[viewSegmentIndex]);
                            }, 0);
                        }
                        else {
                            update();
                        }

                        // Watching for the specified route segment and updating contents
                        $scope.$on('routeSegmentChange', function(event, args) {

                            if(updatePromise)
                                $timeout.cancel(updatePromise);

                            if(args.index == viewSegmentIndex && currentSegment != args.segment) {
                                update(args.segment);
                            }
                        });

                        function clearContent() {
                            if (previousLeaveAnimation) {
                                $animate.cancel(previousLeaveAnimation);
                                previousLeaveAnimation = null;
                            }

                            if (currentScope) {
                                currentScope.$destroy();
                                currentScope = null;
                            }
                            if (currentElement) {
                                previousLeaveAnimation = $animate.leave(currentElement);
                                if(previousLeaveAnimation) {
                                    previousLeaveAnimation.then(function () {
                                        previousLeaveAnimation = null;
                                    });
                                }
                                currentElement = null;
                            }
                        }

                        function update(segment) {

                            currentSegment = segment;

                            var newScope = $scope.$new();

                            var clone = $transclude(newScope, function(clone) {
                                if(segment) {
                                    clone.data('viewSegment', segment);
                                }
                                $animate.enter(clone, null, currentElement || element);
                                clearContent();
                            });

                            currentElement = clone;
                            currentScope = newScope;
                            /*
                             * @ngdoc event
                             * @name appViewSegment#$viewContentLoaded
                             * @description Indicates that segment content has been loaded and transcluded
                             */
                            currentScope.$emit('$viewContentLoaded');
                            currentScope.$eval(onloadExp);
                        }
                    }
                }
            }
        }]);

    angular.module( 'view-segment').directive( 'appViewSegment',
        ['$route', '$compile', '$controller', function($route, $compile, $controller) {

                return {
                    restrict: 'ECA',
                    priority: -400,
                    link: function ($scope, element) {

                        var segment = element.data('viewSegment') || {};

                        var locals = angular.extend({}, segment.locals),
                                template = locals && locals.$template;

                            if(template) {
                                element.html(template);
                            }

                            var link = $compile(element.contents());

                            if (segment.params && segment.params.controller) {
                                locals.$scope = $scope;
                                var controller = $controller(segment.params.controller, locals);
                                if(segment.params.controllerAs)
                                    $scope[segment.params.controllerAs] = controller;
                                element.data('$ngControllerController', controller);
                                element.children().data('$ngControllerController', controller);
                            }

                            link($scope);
                    }
                }

            }]);

})(angular);
