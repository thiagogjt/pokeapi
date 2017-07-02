(function() {
    'use strict';
    angular
        .module('pokeapiApp')
        .factory('TeamPokemon', TeamPokemon);

    TeamPokemon.$inject = ['$resource'];

    function TeamPokemon ($resource) {
        var resourceUrl =  'api/team-pokemons/:id';

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
