app.controller(
    "PlantingRecordsCtrl",
    ["$scope", "$route", "$location", "PlantingRecordSrv", "ParcelSrv", "AccessFactory",
        function ($scope, $route, $location, service, parcelSrv, AccessFactory) {
            console.log("Cargando PlantingRecordsCtrl...")

            function findAll() {
                service.findAll(function (error, plantingRecords) {
                    if (error) {
                        alert("Ocurrió un error: " + error);
                        return;
                    }

                    $scope.plantingRecords = plantingRecords;
                })
            }

            // Esto es necesario para la busqueda que se hace cuando se ingresan caracteres
            $scope.findParcel = function (parcelName) {
                return parcelSrv.findByName(parcelName).
                    then(function (response) {
                        var parcels = [];
                        for (var i = 0; i < response.data.length; i++) {
                            parcels.push(response.data[i]);
                        }
                        return parcels;
                    });;
            }

            $scope.delete = function (id) {
                console.log("Deleting: " + id);

                service.delete(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $location.path("/home/plantingRecord");
                    $route.reload()
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
