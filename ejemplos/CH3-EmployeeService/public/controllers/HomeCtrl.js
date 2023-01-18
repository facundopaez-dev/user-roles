app.controller(
    "HomeCtrl",
    ["$scope", "$rootScope", "$location", "JwtManager", "AuthHeaderManager", "AccessManager",
        function ($scope, $rootScope, $location, jwtManger, authHeaderManager, accessManager) {


            /*
            Si el usuario no tiene una sesion abierta, no se le da acceso
            a la pagina de inicio y se lo redirige a la pagina de inicio
            de sesion
            */
            if (!accessManager.isUserLoggedIn()) {
                $location.path("/");
                return;
            }

            $scope.logout = function () {
                /*
                Cuando el usuario cliente cierra su sesion, se elimina su JWT
                del almacenamiento de sesion del navegador web
                */
                jwtManger.removeJwt();

                /*
                Cuando el usuario cierra su sesion, se elimina el contenido
                del encabezado de autorizacion HTTP, ya que de no hacerlo la
                aplicacion usara el mismo JWT para todas las peticiones HTTP,
                lo cual, producira que la aplicacion del lado servidor
                devuelva datos que no pertenecen al usuario que tiene una
                sesion abierta
                */
                authHeaderManager.clearAuthHeader();

                /*
                Cuando el usuario cliente cierra su sesion, se lo redirige a la pagina de
                inicio de sesion
                */
                $location.path("/");
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