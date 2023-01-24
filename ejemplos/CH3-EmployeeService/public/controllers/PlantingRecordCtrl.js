app.controller(
    "PlantingRecordCtrl",
    ["$scope", "$route", "$location", "$routeParams", "PlantingRecordSrv", "ParcelSrv", "ExpirationSrv", "AccessManager", "ErrorResponseManager",
        function ($scope, $route, $location, $params, service, parcelService, expirationSrv, accessManager, errorResponseManager) {

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
                        console.log(error);
                        errorResponseManager.checkResponse(error);
                        return;
                    }

                    $scope.plantingRecord = data;
                    $location.path("/home/plantingRecord");
                });
            }

            $scope.modify = function () {
                service.modify($scope.plantingRecord, function (error, data) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
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
                        console.log(error);
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
                /*
                Cada vez que el usuario presiona los botones para crear, editar o
                ver un dato correspondiente a este controller, se debe comprobar
                si su JWT expiro o no. En el caso en el que JWT expiro, se redirige
                al usuario a la pagina web de inicio de sesion correspondiente. En caso
                contrario, se realiza la accion solicitada por el usuario mediante
                el boton pulsado.

                De esta manera, este control tambien se realiza para las funciones
                find y findAllParcels. Este es el motivo por el cual no se invoca
                la funcion checkResponse de la factory ErrorResponseManager, en
                dichas funciones.
                */
                expirationSrv.checkExpiration(function (error) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
                    }
                });

                /*
                Si el usuario que tiene una sesion abierta tiene permiso de
                administrador, se lo redirige a la pagina de inicio del
                administrador. De esta manera, un administrador debe cerrar
                la sesion que abrio a traves de la pagina web de inicio de sesion
                del administrador, y luego abrir una sesion a traves de la pagina
                web de inicio de sesion del usuario para poder acceder a la pagina web
                de creacion, edicion o visualizacion de un dato correspondiente
                a este controller.
                */
                if (accessManager.isUserLoggedIn() && accessManager.loggedAsAdmin()) {
                    $location.path("/adminHome");
                    return;
                }

                findAllParcels();
            }

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);