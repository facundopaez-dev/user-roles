app.controller(
    "UsersCtrl",
    ["$scope", "$location", "UserSrv", "AccessManager", "ErrorResponseManager", "AuthHeaderManager", "LogoutManager",
        function ($scope, $location, service, accessManager, errorResponseManager, authHeaderManager, logoutManager) {
            console.log("UsersCtrl loaded...")

            /*
            Con el uso de JWT se evita que el administrador visualice el listado de
            los datos correspondientes a este controller sin tener una sesion
            abierta, pero sin este control, el administrador puede acceder a la pagina
            de inicio sin tener una sesion abierta. Por lo tanto, si el administrador
            NO tiene una sesion abierta, se le impide el acceso a la pagina de inicio
            y se lo redirige a la pagina de inicio de sesion del administrador.
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

            function findAll() {
                service.findAll(function (error, data) {
                    if (error) {
                        console.log(error),
                            errorResponseManager.checkResponse(error);
                        return;
                    }

                    $scope.data = data;
                })
            }

            $scope.logout = function () {
                /*
                LogoutManager es la factory encargada de realizar el cierre de
                sesion del usuario. Durante el cierre de sesion, la funcion
                logout de la factory mencionada, realiza la peticion HTTP de
                cierre de sesion (elimina logicamente la sesion activa del
                usuario en la base de datos, la cual, esta en el lado servidor),
                la eliminacion del JWT del usuario, el borrado del contenido del
                encabezado HTTP de autorizacion, el establecimiento en false del
                valor asociado a la clave "superuser" del almacenamiento local del
                navegador web y la redireccion a la pagina web de inicio de sesion
                correspondiente dependiendo si el usuario inicio sesion como
                administrador o no.
                */
                logoutManager.logout();
            }

            /*
            Si el flujo de ejecucion llega a este punto, se debe a que
            el administrador inicio sesion, por lo tanto, se debe mostrar
            esta lista
            */
            findAll();
        }]);
