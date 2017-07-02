/* globals $ */
(function() {
    'use strict';

    angular
        .module('pokeapiApp')
        .directive('pokemonPanel', pokemonPanel);

    function pokemonPanel () {

        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/pokemon/pokemon-panel.html',
            scope: {
                pokemon: '=',
                remove: '&'
            },
            controller: PokemonPanelController,
            controllerAs: 'vm',
            bindToController: true,
        };

        return directive;

    }

    PokemonPanelController.$inject = ['$scope', '$uibModal', 'AlertService', 'TeamPokemonAbility'];

    function PokemonPanelController($scope, $uibModal, AlertService, TeamPokemonAbility) {

        var vm = this;

        vm.addAbility = addAbility;
        vm.removeAbility = removeAbility;

        init();

        function init() {
            if (!vm.pokemon.abilities) {
                vm.pokemon.abilities = [];
            }
        }

        function addAbility() {
            if (vm.pokemon.abilities.length === 4) {
                AlertService.error('pokeapiApp.team.ability.validation.max');
                return;
            }
            $uibModal.open({
                templateUrl: 'app/components/pokemon/ability-select-dialog.html',
                controller: 'AbilitySelectDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg'
            }).result.then(function (data) {
                vm.pokemon.abilities.push({
                    abilityId: data.id,
                    abilityName: data.name
                });
            });

        }

        function removeAbility(index) {
            vm.isRemoving = true;
            if (vm.pokemon.abilities[index].id) {
                TeamPokemonAbility.remove({id: vm.pokemon.abilities[index].id}, function () {
                    vm.pokemon.abilities.splice(index, 1);
                    vm.isRemoving = false;
                }, function (error) {
                    AlertService.error(error.data.message);
                    vm.isRemoving = false;
                })
            } else {
                vm.pokemon.abilities.splice(index, 1);
                vm.isRemoving = false;
            }
        }

    }

})();
