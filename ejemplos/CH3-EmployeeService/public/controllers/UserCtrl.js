app.controller(
    "UserCtrl",
    ["$scope", "$location", "$routeParams", "UserSrv",
        function ($scope, $location, $params, service) {

            console.log("UserCtrl cargado, accion: " + $params.action)

            if (['view'].indexOf($params.action) == -1) {
                alert("Acción inválida: " + $params.action);
                $location.path("/user");
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

            $scope.goBack = function () {
                $location.path("/user");
            }

            $scope.action = $params.action;

            if ($scope.action == 'view') {
                find($params.id);
            }

        }]);
