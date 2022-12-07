app.controller(
    "PlantingRecordsCtrl",
    ["$scope", "$route", "$location", "PlantingRecordSrv", "ParcelSrv", "AccessFactory",
        function ($scope, $route, $location, service, parcelSrv, AccessFactory) {
            console.log("Cargando PlantingRecordsCtrl...")

            function findAll() {
                service.findAll(function (error, plantingRecords) {
                    if (error) {
                        alert("Ocurrió un error: " + error);
                        return;
                    }

                    $scope.plantingRecords = plantingRecords;
                })
            }

            // Esto es necesario para la busqueda que se hace cuando se ingresan caracteres
            $scope.findParcel = function (parcelName) {
                return parcelSrv.findByName(parcelName).
                    then(function (response) {
                        var parcels = [];
                        for (var i = 0; i < response.data.length; i++) {
                            parcels.push(response.data[i]);
                        }
                        return parcels;
                    });;
            }

            $scope.delete = function (id) {
                console.log("Deleting: " + id);

                service.delete(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $location.path("/home/plantingRecord");
                    $route.reload()
                });
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

			/*
			Para ver el listado de registros de plantacion, el usuario tiene que iniciar sesion,
			por lo tanto, si no tiene una sesion abierta, se le debe impedir el acceso
			a la pagina de listado de registros de plantacion y se lo debe redirigir a la pagina de
			inicio de sesion
			*/
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getLoginRoute());
                return;
            }

            /*
            Si el flujo de ejecucion llega a este punto, se debe a que
            el usuario inicio sesion, por lo tanto, se debe mostrar
            esta lista
            */
            findAll();
        }]);
