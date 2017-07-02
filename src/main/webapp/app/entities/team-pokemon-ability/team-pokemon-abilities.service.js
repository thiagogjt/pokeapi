(function() {
    'use strict';
    angular
        .module('pokeapiApp')
        .factory('TeamPokemonAbility', TeamPokemonAbility);

    TeamPokemonAbility.$inject = ['$resource'];

    function TeamPokemonAbility ($resource) {
        var resourceUrl =  'api/team-pokemon-ability/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
