app.controller(
    "UserLoginCtrl",
    ["$scope", "$location", "$window", "AuthSrv", "AccessFactory",
        function ($scope, $location, $window, authService, accessFactory) {

            /*
            Si el usuario se autentico correctamente, su JWT esta
            almacenado en el almacenamiento de sesion del navegador web.
            Por lo tanto, si el usuario presiona el boton de retroceso del
            navegador web, se lo redirecciona a la pagina de inicio (home)
            */
            if (accessFactory.isUserLoggedIn()) {
                console.log("Usuario con sesion ya iniciada");
                console.log("Redireccionamiento a home (pagina de inicio)");
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
                    $window.sessionStorage.setItem(accessFactory.getKeyStore(), data.jwt);
                    $location.path("/home");
                });
            }

        }]);
