app.controller(
    "PlantingRecordCtrl",
    ["$scope", "$route", "$location", "$routeParams", "PlantingRecordSrv", "ParcelSrv", "AccessFactory",
        function ($scope, $route, $location, $params, service, parcelService, AccessFactory) {
            
            console.log("PlantingRecordCtrl cargado, accion: " + $params.action);

            /*
            Para crear, editar y ver un registro de plantacion, el usuario tiene que
            iniciar sesion, por lo tanto, si no tiene una sesion abierta,
            se le debe impedir el acceso a las rutas de creacion, edicion
            y visualizacion de un registro de plantacion
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getLoginRoute());
                return;
            }

            if (['new', 'edit', 'view'].indexOf($params.action) == -1) {
                alert("Acción inválida: " + $params.action);
                $location.path("/home/plantingRecord");
            }

            function find(id) {
                service.find(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.plantingRecord = data;

                    if ($scope.plantingRecord.seedDate != null) {
                        $scope.plantingRecord.seedDate = new Date($scope.plantingRecord.seedDate);
                    }

                    if ($scope.plantingRecord.harvestDate != null) {
                        $scope.plantingRecord.harvestDate = new Date($scope.plantingRecord.harvestDate);
                    }

                });
            }

            $scope.save = function () {
                service.create($scope.plantingRecord, function (error, data) {
                    if (error) {
                        alert(error.statusText);
                        return;
                    }

                    $scope.plantingRecord = data;
                });

                $location.path("/home/plantingRecord");
                $route.reload();
            }

            $scope.modify = function (plantingRecord) {
                service.modify($scope.plantingRecord, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.plantingRecord = data;
                });

                $location.path("/home/plantingRecord")
                $route.reload();
            }

            function findAllParcels() {
                parcelService.findAll(function (error, parcels) {
                    if (error) {
                        alert(error);
                        return;
                    }

                    $scope.parcels = parcels;
                })
            }

            $scope.cancel = function () {
                $location.path("/home/plantingRecord");
            }

            $scope.action = $params.action;

            if ($scope.action == 'new' || $scope.action == 'edit' || $scope.action == 'view') {
                findAllParcels();
            }

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);