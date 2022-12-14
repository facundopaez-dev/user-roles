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

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);
