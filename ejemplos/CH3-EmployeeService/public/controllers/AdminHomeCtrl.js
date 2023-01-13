app.controller(
    "AdminHomeCtrl",
    ["$scope", "$rootScope", "$location", "AccessFactory", "JwtManager", "AuthHeaderManager",
        function ($scope, $rootScope, $location, AccessFactory, jwtManager, authHeaderManager) {

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
                Cuando el administrador cierra su sesion, se elimina su JWT
                del almacenamiento de sesion del navegador web
                */
                jwtManager.removeJwt();

                /*
                Cuando el administrador cierra su sesion, se elimina el contenido
                del encabezado de autorizacion HTTP
                */
                authHeaderManager.clearAuthHeader();

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

        }]);