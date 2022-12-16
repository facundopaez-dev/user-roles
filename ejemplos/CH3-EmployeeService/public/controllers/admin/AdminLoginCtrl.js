app.controller(
    "AdminLoginCtrl",
    ["$scope", "$location", "$window", "UserSrv", "AccessFactory",
        function ($scope, $location, $window, userService, accessFactory) {

            $scope.login = function () {
                userService.authenticateAdmin($scope.data, function (error, data) {
                    /*
                    Si la autenticacion del administrador falla por uno de los siguientes motivos:
                    1. No hay una cuenta registrada con el nombre de usuario dado. En otras palabras, no existe el usuario ingresado.
                    2. Existe el usuario, pero no tiene el permiso de super usuario.

                    No se debe redireccionar al usuario a la pagina de inicio del administrador. En otras palabras, no se le debe
                    mostrar la pagina de inicio del administrador.
                    */
                    if (error) {
                        alert(error.data.errorMessage);
                        console.log(error);
                        return;
                    }

                    /*
                    Si el flujo de ejecucion de esta funcion llega a este punto, es porque la autenticacion
                    del administrador fue exitosa. Por lo tanto, se almacena el JWT, devuelto por el servidor,
                    en el almacenamiento de sesion del navegador web y se redirecciona al administrador a la
                    pagina de inicio del administrador.
                    */
                    $window.sessionStorage.setItem(accessFactory.getKeyStore(), data.jwt);
                    $location.path("/adminHome");
                });
            }

        }]);