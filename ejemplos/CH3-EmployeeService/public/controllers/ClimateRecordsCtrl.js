app.controller(
    "ClimateRecordsCtrl",
    ["$scope", "$location", "$route", "ClimateRecordSrv", "AccessFactory",
        function ($scope, $location, $route, service, AccessFactory) {
            console.log("ClimateRecordsCtrl cargado...")

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
                console.log("Deleting: " + id);

                service.delete(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $location.path("/home/climateRecord");
                    $route.reload();
                });
            }

            /*
            Si el usuario no inicio sesion, se lo redirige a la pantalla
            de inicio de sesion
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getLoginRoute());
                return;
            }

            /*
            Si el flujo de ejecucion llega a este punto, se debe a que
            el usuario inicio sesion, por lo tanto, se debe mostrar
            esta lista
            */
            findAll();
        }]);
