app.controller(
    "UserLoginCtrl",
    ["$scope", "$location", "AuthSrv", "JwtManager", "AuthHeaderManager", "AccessManager",
        function ($scope, $location, authService, jwtManager, authHeaderManager, accessManager) {

            /*
            Este control es para evitar que el usuario que tiene una
            sesion abierta, vuelva a la pagina de inicio de sesion al
            presionar el boton de retroceso.

            Si el usuario tiene una sesion abierta y presiona el boton de
            retroceso, se lo redirige a la pagina de inicio (home).
            */
            if (accessManager.isUserLoggedIn()) {
                $location.path("/home");
                return;
            }

            $scope.login = function () {
                authService.authenticateUser($scope.data, function (error, data) {
                    /*
                    Si la autenticacion del usuario falla por uno de los siguientes motivos:
                    1. No hay una cuenta registrada con el nombre de usuario dado. En otras palabras, no existe el usuario ingresado.
                    2. El nombre de usuario y/o la contraseña ingresados son incorrectos.

                    No se debe redirigir al usuario a la pagina de inicio. En otras palabras, no se le debe mostrar la pagina de
                    inicio.
                    */
                    if (error) {
                        alert(error.data.message);
                        console.log(error);
                        return;
                    }

                    /*
                    Si el flujo de ejecucion de esta funcion llega a este punto, es porque la autenticacion
                    del usuario fue exitosa. Por lo tanto, se almacena el JWT, devuelto por el servidor,
                    en el almacenamiento de sesion del navegador web y se redirecciona al usuario a la
                    pagina de inicio.
                    */
                    jwtManager.setJwt(data.jwt);

                    /*
                    Cuando el usuario inicia sesion satisfactoriamente, se establece su JWT en el
                    encabezado de autorizacion HTTP para todas las peticiones HTTP, ya que se usa
                    JWT para la autenticacion, la autorizacion y las operaciones con datos
                    */
                    authHeaderManager.setJwtAuthHeader();
                    $location.path("/home");
                });
            }

        }]);
