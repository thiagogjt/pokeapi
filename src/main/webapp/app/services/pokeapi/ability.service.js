(function () {
    'use strict';

    angular
        .module('pokeapiApp')
        .factory('Ability', Ability);

    Ability.$inject = ['$resource'];

    function Ability ($resource) {
        var resourceUrl =  'api/abilities/:idOrName';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
        });
    }
})();
