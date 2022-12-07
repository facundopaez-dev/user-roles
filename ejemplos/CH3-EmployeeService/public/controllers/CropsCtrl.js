app.controller(
    "CropsCtrl",
    ["$scope", "$location", "$route", "CropSrv",
        function ($scope, $location, $route, service) {
            console.log("CropCtrl loaded...");

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
                
                service.remove(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $location.path("/adminHome/crop");
                    $route.reload();
                });
            }

            findAll();
        }]);
