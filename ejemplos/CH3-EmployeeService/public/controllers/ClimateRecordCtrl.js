app.controller(
    "ClimateRecordCtrl",
    ["$scope", "$location", "$routeParams", "ClimateRecordSrv", "ParcelSrv", "AccessFactory",
        function ($scope, $location, $params, service, parcelService, AccessFactory) {

            console.log("ClimateRecordCtrl cargado, accion: " + $params.action);

            /*
            Para crear, editar y ver un registro climatico, el usuario tiene que
            iniciar sesion, por lo tanto, si no tiene una sesion abierta,
            se le debe impedir el acceso a las rutas de creacion, edicion
            y visualizacion de un registro climatico
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getLoginRoute());
                return;
            }

            if (['new', 'edit', 'view'].indexOf($params.action) == -1) {
                alert("Acción inválida: " + $params.action);
                $location.path("/home/climateRecord");
            }

            function find(id) {
                service.find(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.data = data;

                    if ($scope.data.date != null) {
                        $scope.data.date = new Date($scope.data.date);
                    }

                });
            }

            $scope.save = function () {
                service.create($scope.data, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.data = data;
                    $location.path("/home/climateRecord");
                });
            }

            $scope.modify = function () {
                service.modify($scope.data, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.data = data;

                    if ($scope.data.date != null) {
                        $scope.data.date = new Date($scope.data.date);
                    }

                    $location.path("/home/climateRecord")
                });
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
                $location.path("/home/climateRecord");
            }

            $scope.action = $params.action;

            if ($scope.action == 'new' || $scope.action == 'edit' || $scope.action == 'view') {
                findAllParcels();
            }

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);