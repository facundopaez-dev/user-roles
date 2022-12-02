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

            $scope.modify = function () {
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

            $scope.logout = function () {
                /*
                El objeto $scope envia el evento llamado "CallLogout" hacia arriba
                en la jerarquia de objetos $scope. Esto es necesario para implementar
                el cierre de sesion del usuario cliente, cierre que es llevado a cabo por el
                archivo HomeCtrl, en el cual esta suscrito el objeto $rootScope al evento
                "CallLogout". Cuando el objeto $rootScope escucha el evento "CallLogout",
                invoca a la funcion logout(), la cual esta definida en el archivo mencionado.
                */
                $scope.$emit("CallLogout", {});
            }

            $scope.action = $params.action;

            if ($scope.action == 'new' || $scope.action == 'edit' || $scope.action == 'view') {
                findAllParcels();
            }

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);