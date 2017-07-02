(function() {
    'use strict';

    angular
        .module('pokeapiApp')
        .controller('PokemonSelectDialogController', PokemonSelectDialogController);

    PokemonSelectDialogController.$inject = ['$uibModalInstance', 'Pokemon', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function PokemonSelectDialogController ($uibModalInstance, Pokemon, ParseLinks, AlertService, paginationConstants) {
        var vm = this;

        vm.clear = clear;

        vm.pokemons = [];

        vm.loadPage = loadPage;
        vm.loadAll = loadAll;
        vm.find = find;
        vm.select = select;
        vm.predicate = 'id';
        vm.reverse = true;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 1;

        loadAll();

        function loadAll () {
            Pokemon.query({
                page: vm.page -1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.pokemons = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.loadAll();
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function find(idOrName) {
            if (!idOrName) {
                vm.loadAll();
                return;
            }
            Pokemon.get({ idOrName: idOrName }, onSuccess, onError);

            function onSuccess(data) {
                vm.pokemons = [data]
            }

            function onError(error) {
                AlertService.error(error.data.message);
                vm.loadAll();
            }
        }

        function select(pokemon) {
            $uibModalInstance.close(pokemon);
        }

    }
})();
