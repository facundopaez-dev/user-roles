app.controller(
    "ParcelsCtrl",
    ["$scope", "$location", "$route", "ParcelSrv", "AccessManager",
        function ($scope, $location, $route, service, accessManager) {
            console.log("ParcelsCtrl loaded...")

            function findAll() {
                service.findAll(function (error, data) {
                    if (error) {
                        alert("Ocurrió un error: " + error);
                        return;
                    }
                    $scope.data = data;
                })
            }

            $scope.delete = function (id) {
                console.log("Deleting: " + id)

                service.delete(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $location.path("/home/parcel");
                    $route.reload()
                });
            }

            $scope.logout = function () {
                /*
                El objeto $scope envia el evento llamado "CallLogout" hacia arriba
                en la jerarquia de objetos $scope. Esto es necesario para implementar
                el cierre de sesion del usuario cliente, cierre que es llevado a cabo por el
                archivo HomeCtrl, en el cual esta suscrito el objeto $rootScope al evento
                "CallLogout". Cuando el objeto $rootScope escucha el evento "CallLogout",
                invoca a la funcion logout(), la cual esta definida en el archivo mencionado.
                */
                $scope.$emit("CallLogout", {});
            }

            /*
            Con el uso de JWT se evita que el usuario visualice el listado de
            los datos correspondientes a este controller sin tener una sesion
            abierta, pero sin este control, el usuario puede acceder a la pagina
            de inicio sin tener una sesion abierta. Por lo tanto, si el usuario
            NO tiene una sesion abierta, se le impide el acceso a la pagina de
            inicio y se lo redirige a la pagina de inicio de sesion.
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
            web de inicio de sesion del usuario para poder acceder a la pagina web
            de listado de los datos correspondientes a este controller.
            */
            if (accessManager.isUserLoggedIn() && accessManager.loggedAsAdmin()) {
                $location.path("/adminHome");
                return;
            }

            /*
            Si el flujo de ejecucion llega a este punto, se debe a que
            el usuario inicio sesion, por lo tanto, se debe mostrar
            esta lista
            */
            findAll();
        }]);
