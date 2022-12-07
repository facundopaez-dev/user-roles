app.controller(
    "CropsCtrl",
    ["$scope", "$location", "$route", "CropSrv", "AccessFactory",
        function ($scope, $location, $route, service, AccessFactory) {
            console.log("CropCtrl loaded...");

            function findAll() {
                service.findAll(function (error, data) {
                    if (error) {
                        alert("Ocurrió un error: " + error);
                        return;
                    }

                    $scope.data = data;
                })
            }

            $scope.delete = function (id) {
                console.log("Deleting: " + id);
                
                service.remove(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $location.path("/adminHome/crop");
                    $route.reload();
                });
            }

            /*
            Para ver el listado de cultivos, el administrador tiene que iniciar sesion,
            por lo tanto, si no tiene una sesion abierta, se le debe impedir el acceso
            a la pagina de listado de cultivos y se lo debe redirigir a la pagina de
            inicio de sesion del administrador
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getAdminLoginRoute());
                return;
            }

			/*
			Si el flujo de ejecucion llega a este punto, se debe a que
			el administrador inicio sesion, por lo tanto, se debe mostrar
			esta lista
			*/
            findAll();
        }]);
