app.controller(
    "HomeCtrl",
    ["$scope", "$location", "$routeParams",
        function ($scope, $location, $params) {

            $scope.logout = function () {
                // 1. Limpiar la cookie establecida en la funcion login anterior
                // 2. Redireccionar al usuario a la pantalla de inicio                
                $location.path("/login");
            }

        }]);
