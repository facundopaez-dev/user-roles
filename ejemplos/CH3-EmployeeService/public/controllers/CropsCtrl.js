app.controller(
    "CropsCtrl",
    ["$scope", "$location", "$route", "CropSrv", "AccessManager", "ErrorResponseManager",
        function ($scope, $location, $route, service, accessManager, errorResponseManager) {
            console.log("CropsCtrl loaded...");

            function findAll() {
                service.findAll(function (error, data) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
                        return;
                    }

                    $scope.data = data;
                })
            }

            $scope.delete = function (id) {
                console.log("Deleting: " + id);

                service.remove(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
                        return;
                    }

                    $location.path("/adminHome/crop");
                    $route.reload();
                });
            }

            $scope.logout = function () {
                /*
                El objeto $scope envia el evento llamado "AdminLogoutCall" hacia arriba
                en la jerarquia de objetos $scope. Esto es necesario para implementar
                el cierre de sesion del administrador, cierre que es llevado a cabo por el
                archivo AdminHomeCtrl, en el cual esta suscrito el objeto $rootScope al evento
                "AdminLogoutCall". Cuando el objeto $rootScope escucha el evento "AdminLogoutCall",
                invoca a la funcion logout(), la cual esta definida en el archivo mencionado.
                */
                $scope.$emit("AdminLogoutCall", {});
            }

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
            Si el flujo de ejecucion llega a este punto, se debe a que
            el administrador inicio sesion, por lo tanto, se debe mostrar
            esta lista
            */
            findAll();
        }]);
