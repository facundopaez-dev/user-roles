app.controller(
    "HomeCtrl",
    ["$scope", "$location", "AccessFactory",
        function ($scope, $location, AccessFactory) {

            /*
            Para acceder a la pagina de inicio de la aplicacion, el usuario
            tiene que iniciar sesion, por lo tanto, si no tiene una sesion abierta,
            se le debe impedir el acceso a esta pagina
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getLoginRoute());
                return;
            }

            $scope.logout = function () {
                // 1. Limpiar la cookie establecida en la funcion login anterior
                // 2. Redireccionar al usuario a la pantalla de inicio                
                $location.path(AccessFactory.getLoginRoute());
            }
            
        }]);
