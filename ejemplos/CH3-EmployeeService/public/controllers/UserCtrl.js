app.controller(
    "UserCtrl",
    ["$scope", "$location", "$routeParams", "UserSrv", "AccessFactory",
        function ($scope, $location, $params, service, AccessFactory) {

            console.log("UserCtrl cargado, accion: " + $params.action);

            /*
            Esta variable se utiliza para mostrar u ocultar los botones de la barra de
            navegacion de la pagina de inicio del administrador. Si esta variable
            tiene el valor false, se ocultan los botones. En cambio, si tiene el valor
            true, se muestran los botones.
            */
            $scope.superuserPermission = true;

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

            if ($scope.action == 'view') {
                find($params.id);
            }

        }]);
