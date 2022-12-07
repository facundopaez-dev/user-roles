app.controller(
    "CropCtrl",
    ["$scope", "$location", "$routeParams", "CropSrv", "AccessFactory",
        function ($scope, $location, $params, service, AccessFactory) {

            console.log("CropCtrl cargado, accion: " + $params.action)

            /*
            Para crear, editar y ver un cultivo, el administrador tiene que
            iniciar sesion, por lo tanto, si no tiene una sesion abierta, se
            le debe impedir el acceso a las paginas de creacion, edicion y
            visualizacioon de un cultivo
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getAdminLoginRoute());
                return;
            }

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
