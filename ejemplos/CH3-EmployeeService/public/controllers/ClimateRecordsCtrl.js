app.controller(
    "ClimateRecordsCtrl",
    ["$scope", "$location", "$route", "ClimateRecordSrv",
        function ($scope, $location, $route, service) {
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

            findAll();
        }]);
