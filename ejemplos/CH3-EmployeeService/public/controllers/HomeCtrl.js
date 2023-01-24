app.controller(
    "HomeCtrl",
    ["$scope", "$rootScope", "$location", "ExpirationSrv", "JwtManager", "AuthHeaderManager", "AccessManager", "ErrorResponseManager",
        function ($scope, $rootScope, $location, expirationSrv, jwtManger, authHeaderManager, accessManager, errorResponseManager) {

            /*
            Se comprueba si el JWT del usuario que tiene una sesion abierta, expiro
            o no. En el caso en el que JWT expiro, se redirige al usuario a la
            pagina web de inicio de sesion del usuario. En caso contrario,
            se realizan controles y el usuario puede cerrar su sesion, siempre
            y cuando dichos controles lo permitan.
            */
            expirationSrv.checkExpiration(function (error) {
                if (error) {
                    console.log(error);
                    errorResponseManager.checkResponse(error);
                }
            });

            /*
            Si el usuario no tiene una sesion abierta, no se le da acceso
            a la pagina de inicio y se lo redirige a la pagina de inicio
            de sesion
            */
            if (!accessManager.isUserLoggedIn()) {
                $location.path("/");
                return;
            }

            /*
            Si el usuario que tiene una sesion abierta tiene permiso de
            administrador, se lo redirige a la pagina de inicio del
            administrador. De esta manera, un administrador debe cerrar
            la sesion que abrio a traves de la pagina web de inicio de sesion
            del administrador, y luego abrir una sesion a traves de la pagina
            web de inicio de sesion del usuario para poder acceder a la pagina
            de inicio del usuario.
            */
            if (accessManager.isUserLoggedIn() && accessManager.loggedAsAdmin()) {
                $location.path("/adminHome");
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