(function () {
    'use strict';

    angular
        .module('pokeapiApp')
        .controller('TeamDialogController', TeamDialogController);

    TeamDialogController.$inject = ['$timeout', '$scope', '$state', '$stateParams', 'entity', 'Team', 'TeamPokemon', '$uibModal'];

    function TeamDialogController($timeout, $scope, $state, $stateParams, entity, Team, TeamPokemon, $uibModal) {
        var vm = this;

        vm.team = entity;
        vm.clear = clear;
        vm.save = save;
        vm.addPokemon = addPokemon;
        vm.removePokemon = removePokemon;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $state.go('team', null, {reload: 'team'});
        }

        function save() {
            vm.isSaving = true;
            if (vm.team.id !== null) {
                Team.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Team.save(vm.team, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('pokeapiApp:teamUpdate', result);
            $state.go('team', null, {reload: 'team'});
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        function addPokemon() {
            $uibModal.open({
                templateUrl: 'app/components/pokemon/pokemon-select-dialog.html',
                controller: 'PokemonSelectDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg'
            }).result.then(function (data) {
                vm.team.pokemons.push({
                    pokemonId: data.id,
                    pokemonName: data.name,
                    abilities: []
                });
            });

        }

        function removePokemon(index) {
            vm.isRemoving = true;
            if (vm.team.pokemons[index].id) {
                TeamPokemon.remove({id: vm.team.pokemons[index].id}, function () {
                    vm.team.pokemons.splice(index, 1);
                    vm.isRemoving = false;
                }, function (error) {
                    AlertService.error(error.data.message);
                    vm.isRemoving = false;
                })
            } else {
                vm.team.pokemons.splice(index, 1);
                vm.isRemoving = false;
            }
        }


    }
})();
