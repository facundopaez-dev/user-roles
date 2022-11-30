app.controller(
    "PlantingRecordsCtrl",
    ["$scope", "$route", "$location", "PlantingRecordSrv", "ParcelSrv",
        function ($scope, $route, $location, service, parcelSrv) {
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

            findAll();
        }]);
