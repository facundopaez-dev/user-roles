app.controller(
    "CropCtrl",
    ["$scope", "$location", "$routeParams", "CropSrv", "ExpirationSrv", "AccessManager", "ErrorResponseManager",
        function ($scope, $location, $params, service, expirationSrv, accessManager, errorResponseManager) {

            console.log("CropCtrl cargado, accion: " + $params.action)

            /*
            Con el uso de JWT se evita que el administrador cree, edite o visualice
            un dato, correspondiente a este controller, sin tener una sesion
            abierta, pero sin este control, el administrador puede acceder la pagina
            de inicio sin tener una sesion abierta. Por lo tanto, si el
            administrador NO tiene una sesion abierta, se le impide el acceso a la
            pagina de inicio y se lo redirige a la pagina de inicio de sesion del
            administrador.
            */
            if (!accessManager.isUserLoggedIn()) {
                $location.path("/admin");
                return;
            }

            /*
            Si el usuario que tiene una sesion abierta no tiene permiso de administrador,
            no se le da acceso a la pagina correspondiente a este controller y se lo redirige
            a la pagina de inicio del usuario
            */
            if (accessManager.isUserLoggedIn() && !accessManager.loggedAsAdmin()) {
                $location.path("/home");
                return;
            }

            if (['new', 'edit', 'view'].indexOf($params.action) == -1) {
                alert("Acción inválida: " + $params.action);
                $location.path("/adminHome/crop");
            }

            function find(id) {
                service.find(id, function (error, data) {
                    if (error) {
                        console.log(error);
                        return;
                    }

                    $scope.data = data;
                });
            }

            $scope.save = function () {
                service.create($scope.data, function (error, data) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
                        return;
                    }

                    $scope.data = data;
                    $location.path("/adminHome/crop");
                });
            }

            $scope.modify = function () {
                service.modify($scope.data, function (error, data) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
                        return;
                    }

                    $scope.data = data;
                    $location.path("/adminHome/crop")
                });
            }

            $scope.cancel = function () {
                $location.path("/adminHome/crop");
            }

            $scope.logout = function () {
                /*
                El objeto $scope envia el evento llamado "AdminLogoutCall" hacia arriba
                en la jerarquia de objetos $scope. Esto es necesario para implementar
                el cierre de sesion del administrador, cierre que es llevado a cabo por el
                archivo AdminHomeCtrl, en el cual esta suscrito el objeto $rootScope al evento
                "AdminLogoutCall". Cuando el objeto $rootScope escucha el evento "AdminLogoutCall",
                invoca a la funcion logout(), la cual esta definida en el archivo mencionado.
                */
                $scope.$emit("AdminLogoutCall", {});
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

                De esta manera, este control tambien se realiza para la funcion find.
                Este es el motivo por el cual no se invoca la funcion checkResponse
                de la factory ErrorResponseManager, en dicha funcion.
                */
                expirationSrv.checkExpiration(function (error) {
                    if (error) {
                        console.log(error);
                        errorResponseManager.checkResponse(error);
                    }
                });
            }

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);
