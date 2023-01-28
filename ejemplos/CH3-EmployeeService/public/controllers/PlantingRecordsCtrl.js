app.controller(
    "PlantingRecordsCtrl",
    ["$scope", "$route", "$location", "PlantingRecordSrv", "ParcelSrv", "AccessManager", "ErrorResponseManager", "AuthHeaderManager",
        function ($scope, $route, $location, service, parcelSrv, accessManager, errorResponseManager, authHeaderManager) {
            console.log("Cargando PlantingRecordsCtrl...")

            /*
            Cuando el usuario abre una sesion satisfactoriamente y no la cierra,
            y accede a la aplicacion web mediante una nueva pestaña, el encabezado
            de autorizacion HTTP tiene el valor undefined. En consecuencia, las
            peticiones HTTP con este encabezado no seran respondidas por la
            aplicacion del lado servidor, ya que esta opera con JWT para la
            autenticacion, la autorizacion y las operaciones con recursos
            (lectura, modificacion y creacion).

            Este es el motivo por el cual se hace este control. Si el encabezado
            HTTP de autorizacion tiene el valor undefined, se le asigna el JWT
            del usuario.

            De esta manera, cuando el usuario abre una sesion satisfactoriamente
            y no la cierra, y accede a la aplicacion web mediante una nueva pestaña,
            el encabezado HTTP de autorizacion contiene el JWT del usuario, y, por
            ende, la peticion HTTP que se realice en la nueva pestaña, sera respondida
            por la aplicacion del lado servidor.
            */
            if (authHeaderManager.isUndefined()) {
                authHeaderManager.setJwtAuthHeader();
            }

            function findAll() {
                service.findAll(function (error, plantingRecords) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
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
                        errorResponseManager.checkResponse(error);
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
            Con el uso de JWT se evita que el usuario visualice el listado de
            los datos correspondientes a este controller sin tener una sesion
            abierta, pero sin este control, el usuario puede acceder a la pagina
            de inicio sin tener una sesion abierta. Por lo tanto, si el usuario
            NO tiene una sesion abierta, se le impide el acceso a la pagina de
            inicio y se lo redirige a la pagina de inicio de sesion.
            */
            if (!accessManager.isUserLoggedIn()) {
                $location.path("/");
                return;
            }

            /*
            Si el usuario que tiene una sesion abierta tiene permiso de
            administrador, se lo redirige a la pagina de inicio del
            administrador. De esta manera, un administrador debe cerrar
            la sesion que abrio a traves de la pagina web de inicio de sesion
            del administrador, y luego abrir una sesion a traves de la pagina
            web de inicio de sesion del usuario para poder acceder a la pagina web
            de listado de los datos correspondientes a este controller.
            */
            if (accessManager.isUserLoggedIn() && accessManager.loggedAsAdmin()) {
                $location.path("/adminHome");
                return;
            }

            /*
            Si el flujo de ejecucion llega a este punto, se debe a que
            el usuario inicio sesion, por lo tanto, se debe mostrar
            esta lista
            */
            findAll();
        }]);
