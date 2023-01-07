app.controller(
    "HomeCtrl",
    ["$scope", "$rootScope", "$location", "$window", "AccessFactory", "JwtManager",
        function ($scope, $rootScope, $location, $window, AccessFactory, jwtManger) {

            /*
            Para acceder a la pagina de inicio de la aplicacion, el usuario cliente
            tiene que iniciar sesion, por lo tanto, si no tiene una sesion abierta,
            se le debe impedir el acceso a esta pagina y se lo debe redigir a la
            pagina de inicio de sesion
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getLoginRoute());
                return;
            }

            $scope.logout = function () {
                /*
                Cuando el usuario cliente cierra su sesion, se elimina su JWT
                del almacenamiento de sesion del navegador web
                */
                jwtManger.removeJwt();

                /*
                Cuando el usuario cliente cierra su sesion, se lo redirige a la pagina de
                inicio de sesion
                */
                $location.path(AccessFactory.getLoginRoute());
            }

            /*
            El objeto $rootScope esta suscrito al evento "CallLogout". Esto es necesario
            para implementar el cierre de sesion del usuario cliente. Cuando el objeto
            $rootScope escucha el evento "CallLogout", invoca a la funcion logout(), la
            cual, como su nombre lo indica, realiza el cierre de sesion del usario cliente.
            */
            $rootScope.$on("CallLogout", function () {
                $scope.logout();
            });

        }]);