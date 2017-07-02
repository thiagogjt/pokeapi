(function() {
    'use strict';

    angular
        .module('pokeapiApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['Team'];

    function TeamController(Team) {

        var vm = this;

        vm.teams = [];

        loadAll();

        function loadAll() {
            Team.query(function(result) {
                vm.teams = result;
                vm.searchQuery = null;
            });
        }
    }
})();
