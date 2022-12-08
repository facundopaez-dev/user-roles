app.controller(
    "CropCtrl",
    ["$scope", "$location", "$window", "$routeParams", "CropSrv", "UserSrv", "AccessFactory",
        function ($scope, $location, $window, $params, service, userService, AccessFactory) {

            console.log("CropCtrl cargado, accion: " + $params.action)

            /*
            Esta variable se utiliza para mostrar u ocultar los botones de la barra de
            navegacion de la pagina de inicio del administrador. Si esta variable
            tiene el valor false, se ocultan los botones. En cambio, si tiene el valor
            true, se muestran los botones.
            */
            $scope.superuserPermission = false;

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

            /*
            Esta funcion comprueba que el usuario conectado tenga el permiso de super usuario
            (administrador). Si el usuario conectado no tiene dicho permiso, se muestra el mensaje
            "Acceso no autorizado" y se lo redirige a la pagina de inicio del usuario. En cambio,
            si lo tiene, se muestra la pagina correspondiente a este controlador y se muestran
            los botones de la barra de navegacion del administrador. La manera en la que se
            muestran estos botones es asignando el valor true a la variable superuserPermission.
            */
            function isSuperuser() {
                /*
                El contenido almacenado en el almacenamiento de sesion del navegador web es un string
                en formato JSON, con lo cual, se lo debe convertir para poder accedr a sus propiedades
                */
                let data = JSON.parse($window.sessionStorage.getItem(AccessFactory.getKeyStore()));

                userService.isSuperuser(data, function (error, data) {
                    if (error) {
                        alert(error.data.errorMessage);
                        $location.path("/home");
                        return;
                    }

                    $scope.superuserPermission = true;
                })
            }

            isSuperuser();

        }]);
