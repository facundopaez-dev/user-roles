app.controller(
    "PlantingRecordCtrl",
    ["$scope", "$route", "$location", "$routeParams", "PlantingRecordSrv", "ParcelSrv", "AccessManager",
        function ($scope, $route, $location, $params, service, parcelService, accessManager) {

            console.log("PlantingRecordCtrl cargado, accion: " + $params.action);

            /*
            Con el uso de JWT se evita que el usuario cree, edite o visualice
            un dato, correspondiente a este controller, sin tener una sesion
            abierta, pero sin este control, el usuario puede acceder la pagina
            de inicio sin tener una sesion abierta. Por lo tanto, si el
            usuario NO tiene una sesion abierta, se le impide el acceso a la
            pagina de inicio y se lo redirige a la pagina de inicio de sesion.
            */
            if (!accessManager.isUserLoggedIn()) {
                $location.path("/");
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
                        alert(error.data.message);
                        $location.path("/home/plantingRecord");
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