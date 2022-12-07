app.controller(
    "AdminHomeCtrl",
    ["$scope", "$rootScope", "$location", "$window", "AccessFactory",
        function ($scope, $rootScope, $location, $window, AccessFactory) {

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

        }]);