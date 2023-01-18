app.controller(
    "AdminLoginCtrl",
    ["$scope", "$location", "AuthSrv", "JwtManager", "AuthHeaderManager", "AccessManager",
        function ($scope, $location, authService, jwtManager, authHeaderManager, accessManager) {

            /*
            Este control es para evitar que el administrador que tiene una
            sesion abierta, vuelva a la pagina de inicio de sesion de
            administrador al presionar el boton de retroceso.

            Si el administrador tiene una sesion abierta y presiona el boton de
            retroceso, se lo redirige a la pagina de inicio del administrador
            (admin-home).
            */
            if (accessManager.isUserLoggedIn()) {
                $location.path("/adminHome");
                return;
            }

            $scope.login = function () {
                authService.authenticateAdmin($scope.data, function (error, data) {
                    /*
                    Si la autenticacion del administrador falla por uno de los siguientes motivos:
                    1. No hay una cuenta registrada con el nombre de usuario dado. En otras palabras, no existe el usuario ingresado.
                    2. Existe el usuario, pero no tiene el permiso de super usuario.

                    No se debe redireccionar al usuario a la pagina de inicio del administrador. En otras palabras, no se le debe
                    mostrar la pagina de inicio del administrador.
                    */
                    if (error) {
                        alert(error.data.message);
                        console.log(error);
                        return;
                    }

                    /*
                    Si el flujo de ejecucion de esta funcion llega a este punto, es porque la autenticacion
                    del administrador fue exitosa. Por lo tanto, se almacena el JWT, devuelto por el servidor,
                    en el almacenamiento de sesion del navegador web y se redirecciona al administrador a la
                    pagina de inicio del administrador.
                    */
                    jwtManager.setJwt(data.jwt);

                    /*
                    Cuando el administrador inicia sesion satisfactoriamente, se establece su JWT en el
                    encabezado de autorizacion HTTP para todas las peticiones HTTP, ya que se usa
                    JWT para la autenticacion, la autorizacion y las operaciones con datos
                    */
                    authHeaderManager.setJwtAuthHeader();
                    $location.path("/adminHome");
                });
            }

        }]);
