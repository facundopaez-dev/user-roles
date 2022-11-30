app.controller(
    "AccessCtrl",
    ["$scope", "$location", "$window", "UserSrv", "AccessFactory",
        function ($scope, $location, $window, userService, accessFactory) {

            $scope.login = function () {
                userService.authenticateUser($scope.username, $scope.password, function (error, data) {
                    // Si la autenticacion falla, no se le muestra la pantalla de inicio al usuario
                    if (error) {
                        alert("Nombre de usuario o contraseña incorrectos");
                        console.log(error);
                        return;
                    }

                    /*
                    Si el flujo de ejecucion de esta funcion, llego a este punto, la autentificacion
                    del usuario fue exitosa. Por lo tanto, se guarda el usuario en el almacenamiento
                    de sesion del navegador web y se redirecciona al usuario a la pagina de inicio de
                    la aplicacion.
                    */
                    $window.sessionStorage.setItem(accessFactory.getKeyStore(), $window.JSON.stringify(data));
                    $location.path("/home");
                });
            }

        }]);
