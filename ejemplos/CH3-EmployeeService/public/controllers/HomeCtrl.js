app.controller(
    "HomeCtrl",
    ["$scope", "$rootScope", "$location", "ExpirationSrv", "LogoutSrv", "JwtManager", "AuthHeaderManager", "AccessManager", "ErrorResponseManager",
        function ($scope, $rootScope, $location, expirationSrv, logoutSrv, jwtManger, authHeaderManager, accessManager, errorResponseManager) {

            console.log("HomeCtrl loaded");

            /*
            Cuando el usuario abre una sesion satisfactoriamente y no la cierra,
            y accede a la aplicacion web mediante una nueva pestaña, el encabezado
            de autorizacion HTTP tiene el valor undefined. En consecuencia, las
            peticiones HTTP con este encabezado no seran respondidas por la
            aplicacion del lado servidor, ya que esta opera con JWT para la
            autenticacion, la autorizacion y las operaciones con recursos
            (lectura, modificacion y creacion).

            Este es el motivo por el cual se hace este control. Si el encabezado
            HTTP de autorizacion tiene el valor undefined, se le asigna el JWT
            del usuario.

            De esta manera, cuando el usuario abre una sesion satisfactoriamente
            y no la cierra, y accede a la aplicacion web mediante una nueva pestaña,
            el encabezado HTTP de autorizacion contiene el JWT del usuario, y, por
            ende, la peticion HTTP que se realice en la nueva pestaña, sera respondida
            por la aplicacion del lado servidor.
            */
            if (authHeaderManager.isUndefined()) {
                authHeaderManager.setJwtAuthHeader();
            }

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
                Con esta peticion se elimina logicamente de la base de datos
                (en el backend) la sesion activa del usuario. Si no se hace
                esta eliminacion lo que sucedera es que, cuando el usuario
                que abrio y cerro su sesion, intente abrir otra sesion, la
                aplicacion no se lo permitira, ya que la sesion anteriormente
                cerrada aun sigue activa.

                Cuando se elimina logicamente una sesion activa de la base
                de datos subyacente (en el backend), la sesion pasa a estar
                inactiva. De esta manera, el usuario que abrio y cerro su
                sesion, puede abrir nuevamente otra sesion.
                */
                logoutSrv.logout(function (error) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
                    }
                });

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