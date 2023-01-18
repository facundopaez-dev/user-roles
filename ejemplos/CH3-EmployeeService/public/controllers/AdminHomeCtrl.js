app.controller(
    "AdminHomeCtrl",
    ["$scope", "$rootScope", "$location", "JwtManager", "AuthHeaderManager", "AccessManager",
        function ($scope, $rootScope, $location, jwtManager, authHeaderManager, accessManager) {

            /*
            Esta variable se utiliza para mostrar u ocultar los botones de la barra de
            navegacion de la pagina de inicio del administrador. Si esta variable
            tiene el valor false, se ocultan los botones. En cambio, si tiene el valor
            true, se muestran los botones.
            */
            // $scope.superuserPermission = false;
            $scope.superuserPermission = true;

            /*
            Si el administrador no tiene una sesion abierta, no se le da acceso
            a la pagina de inicio del administrador y se lo redirige a la pagina
            de inicio de sesion del administrador
            */
            if (!accessManager.isUserLoggedIn()) {
                $location.path("/admin");
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
                Cuando un administrador cierra su sesion, la variable booleana que se utiliza
                para controlar su acceso a las paginas web a las que accede un usuario, se
                establece en false, ya que de no hacerlo dicha variable tendria el valor
                true y se le impediria el acceso a dichas paginas web a un administrador
                cuando inicie sesion a traves de la pagina de inicio de sesion del usuario
                */
                accessManager.clearAsAdmin();

                /*
                Cuando el administrador cierra su sesion, se lo redirige a la pagina de
                inicio de sesion del administrador
                */
                $location.path("/admin");
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