app.controller(
    "AdminHomeCtrl",
    ["$scope", "$rootScope", "$location", "ExpirationSrv", "LogoutSrv", "JwtManager", "AuthHeaderManager", "AccessManager", "ErrorResponseManager",
        function ($scope, $rootScope, $location, expirationSrv, logoutSrv, jwtManager, authHeaderManager, accessManager, errorResponseManager) {

            /*
            Si el administrador no tiene una sesion abierta, no se le da acceso
            a la pagina de inicio del administrador y se lo redirige a la pagina
            de inicio de sesion del administrador
            */
            if (!accessManager.isUserLoggedIn()) {
                $location.path("/admin");
                return;
            }

            /*
            Si el usuario que tiene una sesion abierta no tiene permiso de administrador,
            no se le da acceso a la pagina correspondiente a este controller y se lo redirige
            a la pagina de inicio del usuario
             */
            if (accessManager.isUserLoggedIn() && !accessManager.loggedAsAdmin()) {
                $location.path("/home");
                return;
            }

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
            Se comprueba si el JWT del administrador que tiene una sesion abierta, expiro
            o no. En el caso en el que JWT expiro, se redirige al administrador a la
            pagina web de inicio de sesion del administrador. En caso contrario,
            se realizan controles y el administrador puede cerrar su sesion, siempre
            y cuando dichos controles lo permitan.
            */
            expirationSrv.checkExpiration(function (error) {
                if (error) {
                    console.log(error);
                    errorResponseManager.checkResponse(error);
                }
            });

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
                Cuando el administrador cierra su sesion, se elimina su JWT
                del almacenamiento de sesion del navegador web
                */
                jwtManager.removeJwt();

                /*
                Cuando el administrador cierra su sesion, se elimina el contenido
                del encabezado de autorizacion HTTP
                */
                authHeaderManager.clearAuthHeader();

                /*
                Cuando un administrador cierra su sesion, la variable booleana que se utiliza
                para controlar su acceso a las paginas web a las que accede un usuario, se
                establece en false, ya que de no hacerlo dicha variable tendria el valor
                true y se le impediria el acceso a dichas paginas web a un administrador
                cuando inicie sesion a traves de la pagina de inicio de sesion del usuario
                */
                accessManager.clearAsAdmin();

                /*
                Cuando el administrador cierra su sesion, se lo redirige a la pagina de
                inicio de sesion del administrador
                */
                $location.path("/admin");
            }

            /*
            El objeto $rootScope esta suscrito al evento "AdminLogoutCall". Esto es necesario
            para implementar el cierre de sesion del administrador. Cuando el objeto $rootScope
            escucha el evento "AdminLogoutCall", invoca a la funcion logout(), la cual, como su
            nombre lo indica, realiza el cierre de sesion del administrador.
            */
            $rootScope.$on("AdminLogoutCall", function () {
                $scope.logout();
            });

        }]);