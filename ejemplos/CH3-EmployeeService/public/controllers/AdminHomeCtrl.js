app.controller(
    "AdminHomeCtrl",
    ["$scope", "$rootScope", "$location", "$window", "UserSrv", "AccessFactory",
        function ($scope, $rootScope, $location, $window, userService, AccessFactory) {

            /*
            Esta variable se utiliza para mostrar u ocultar los botones de la barra de
            navegacion de la pagina de inicio del administrador. Si esta variable
            tiene el valor false, se ocultan los botones. En cambio, si tiene el valor
            true, se muestran los botones.
            */
            // $scope.superuserPermission = false;
            $scope.superuserPermission = true;

            /*
            Para acceder a la pagina de inicio del administrador, este tiene que
            iniciar sesion, por lo tanto, si no tiene una sesion abierta, se le
            debe impedir el acceso a esta pagina y se lo debe redirigir a la pagina
            de inicio de sesion del administrador
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getAdminLoginRoute());
                return;
            }

            $scope.logout = function () {
                /*
                Cuando el administrador cierra su sesion, se eliminan sus datos
                del almacenamiento de sesion del navegador web
                */
                $window.sessionStorage.removeItem(AccessFactory.getKeyStore());

                /*
                Cuando el administrador cierra su sesion, se lo redirige a la pagina de
                inicio de sesion del administrador
                */
                $location.path(AccessFactory.getAdminLoginRoute());
            }

            /*
            El objeto $rootScope esta suscrito al evento "AdminLogoutCall". Esto es necesario
            para implementar el cierre de sesion del administrador. Cuando el objeto $rootScope
            escucha el evento "AdminLogoutCall", invoca a la funcion logout(), la cual, como su
            nombre lo indica, realiza el cierre de sesion del administrador.
            */
            $rootScope.$on("AdminLogoutCall", function () {
                $scope.logout();
            });

            /*
            Esta funcion comprueba que el usuario conectado tenga el permiso de super usuario
            (administrador). Si el usuario conectado no tiene dicho permiso, se muestra el mensaje
            "Acceso no autorizado" y se lo redirige a la pagina de inicio del usuario. En cambio,
            si lo tiene, se muestra la pagina correspondiente a este controlador y se muestran
            los botones de la barra de navegacion del administrador. La manera en la que se
            muestran estos botones es asignando el valor true a la variable superuserPermission.
            */
            // function isSuperuser() {
            //     /*
            //     El contenido almacenado en el almacenamiento de sesion del navegador web es un string
            //     en formato JSON, con lo cual, se lo debe convertir para poder accedr a sus propiedades
            //     */
            //     let data = JSON.parse($window.sessionStorage.getItem(AccessFactory.getKeyStore()));

            //     userService.isSuperuser(data, function (error, data) {
            //         if (error) {
            //             alert(error.data.message);
            //             $location.path("/home");
            //             return;
            //         }

            //         $scope.superuserPermission = true;
            //     })
            // }

            // isSuperuser();
        }]);