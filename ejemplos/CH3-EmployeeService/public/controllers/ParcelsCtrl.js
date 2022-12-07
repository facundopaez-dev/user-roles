app.controller(
    "ParcelsCtrl",
    ["$scope", "$location", "$route", "ParcelSrv", "AccessFactory",
        function ($scope, $location, $route, service, AccessFactory) {
            console.log("ParcelsCtrl loaded...")

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
                console.log("Deleting: " + id)

                service.delete(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }
                    
                    $location.path("/home/parcel");
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
			Para ver el listado de parcelas, el usuario tiene que iniciar sesion,
			por lo tanto, si no tiene una sesion abierta, se le debe impedir el acceso
			a la pagina de listado de parcelas y se lo debe redirigir a la pagina de
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
