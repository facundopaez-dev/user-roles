app.controller(
    "CropCtrl",
    ["$scope", "$location", "$routeParams", "CropSrv",
        function ($scope, $location, $params, service) {

            console.log("CropCtrl cargado, accion: " + $params.action)

            if (['new', 'edit', 'view'].indexOf($params.action) == -1) {
                alert("Acción inválida: " + $params.action);
                $location.path("/adminHome/crop");
            }

            function find(id) {
                service.find(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.data = data;
                });
            }

            $scope.save = function () {
                service.create($scope.data, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.data = data;
                    $location.path("/adminHome/crop");
                });
            }

            $scope.modify = function () {
                service.modify($scope.data, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.data = data;
                    $location.path("/adminHome/crop")
                });
            }

            $scope.cancel = function () {
                $location.path("/adminHome/crop");
            }

            $scope.action = $params.action;

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);
