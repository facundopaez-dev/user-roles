app.controller(
    "ParcelCtrl",
    ["$scope", "$location", "$routeParams", "ParcelSrv", "AccessFactory",
        function ($scope, $location, $params, service, AccessFactory) {
            
            console.log("ParcelCtrl loaded with action: " + $params.action);

            /*
            Para crear, editar y ver una parcela, el usuario tiene que
            iniciar sesion, por lo tanto, si no tiene una sesion abierta,
            se le debe impedir el acceso a las rutas de creacion, edicion
            y visualizacion de una parcela
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getLoginRoute());
                return;
            }

            if (['new', 'edit', 'view'].indexOf($params.action) == -1) {
                alert("Acción inválida: " + $params.action);
                $location.path("/home/parcel");
            }

            function find(id) {
                service.find(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.data = data;

                    /*
                    Con esta linea de codigo se evita que AngularJS lance el error del
                    formato de la fecha: Model is not a date object
                    */
                    if ($scope.data.registrationDate != null) {
                        $scope.data.registrationDate = new Date($scope.data.registrationDate);
                    }

                });
            }

            $scope.save = function () {
                service.save($scope.data, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }
                    $scope.data = data;
                    $location.path("/home/parcel")
                });
            }

            $scope.update = function () {
                service.update($scope.data, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }
                    $scope.data = data;

                    /*
                    Con esta linea de codigo se evita que AngularJS lance el error del
                    formato de la fecha: Model is not a date object
                    */
                    if ($scope.data.registrationDate != null) {
                        $scope.data.registrationDate = new Date($scope.data.registrationDate);
                    }

                    $location.path("/home/parcel")
                });
            }

            $scope.cancel = function () {
                $location.path("/home/parcel");
            }

            $scope.action = $params.action;

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);
