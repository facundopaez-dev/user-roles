app.controller(
    "CropCtrl",
    ["$scope", "$location", "$routeParams", "CropSrv", "AccessFactory",
        function ($scope, $location, $params, service, AccessFactory) {

            console.log("CropCtrl cargado, accion: " + $params.action)

            /*
            Esta variable se utiliza para mostrar u ocultar los botones de la barra de
            navegacion de la pagina de inicio del administrador. Si esta variable
            tiene el valor false, se ocultan los botones. En cambio, si tiene el valor
            true, se muestran los botones.
            */
            $scope.superuserPermission = true;

            /*
            Para crear, editar y ver un cultivo, el administrador tiene que
            iniciar sesion, por lo tanto, si no tiene una sesion abierta, se
            le debe impedir el acceso a las paginas de creacion, edicion y
            visualizacioon de un cultivo
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getAdminLoginRoute());
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

            if ($scope.action == 'edit' || $scope.action == 'view') {
                find($params.id);
            }

        }]);
