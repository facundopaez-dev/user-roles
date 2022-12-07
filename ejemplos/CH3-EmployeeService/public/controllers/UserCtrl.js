app.controller(
    "UserCtrl",
    ["$scope", "$location", "$routeParams", "UserSrv", "AccessFactory",
        function ($scope, $location, $params, service, AccessFactory) {

            console.log("UserCtrl cargado, accion: " + $params.action)

            /*
            Para ver un usuario y modificar su permiso, el administrador tiene que
            iniciar sesion, por lo tanto, si no tiene una sesion abierta, se le debe
            impedir el acceso a las paginas de visualizacion de un usuario y modificacion
            del permiso del mismo
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getAdminLoginRoute());
                return;
            }

            if (['view'].indexOf($params.action) == -1) {
                alert("Acción inválida: " + $params.action);
                $location.path("/adminHome/user");
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

            $scope.goBack = function () {
                $location.path("/adminHome/user");
            }

            $scope.action = $params.action;

            if ($scope.action == 'view') {
                find($params.id);
            }

        }]);
