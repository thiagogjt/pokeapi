(function() {
    'use strict';

    angular
        .module('pokeapiApp')
        .controller('AbilitySelectDialogController', AbilitySelectDialogController);

    AbilitySelectDialogController.$inject = ['$uibModalInstance', 'Ability', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function AbilitySelectDialogController ($uibModalInstance, Ability, ParseLinks, AlertService, paginationConstants) {
        var vm = this;

        vm.clear = clear;

        vm.abilities = [];

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
            Ability.query({
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
                vm.abilities = data;
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
            Ability.get({ idOrName: idOrName }, onSuccess, onError);

            function onSuccess(data) {
                vm.abilities = [data]
            }

            function onError(error) {
                AlertService.error(error.data.message);
                vm.loadAll();
            }
        }

        function select(ability) {
            $uibModalInstance.close(ability);
        }

    }
})();
