app.controller(
	"UsersCtrl",
	["$scope", "$location", "UserSrv", "AccessManager",
		function ($scope, $location, service, accessManager) {
			console.log("UsersCtrl loaded...")

            /*
            Esta variable se utiliza para mostrar u ocultar los botones de la barra de
            navegacion de la pagina de inicio del administrador. Si esta variable
            tiene el valor false, se ocultan los botones. En cambio, si tiene el valor
            true, se muestran los botones.
            */
            $scope.superuserPermission = true;

			function findAll() {
				service.findAll(function (error, data) {
					if (error) {
						alert("Ocurrió un error: " + error);
						return;
					}
					
					$scope.data = data;
				})
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

            /*
            Con el uso de JWT se evita que el administrador visualice el listado de
            los datos correspondientes a este controller sin tener una sesion
            abierta, pero sin este control, el administrador puede acceder a la pagina
            de inicio sin tener una sesion abierta. Por lo tanto, si el administrador
            NO tiene una sesion abierta, se le impide el acceso a la pagina de inicio
            y se lo redirige a la pagina de inicio de sesion del administrador.
            */
            if (!accessManager.isUserLoggedIn()) {
                $location.path("/admin");
                return;
            }

			/*
			Si el flujo de ejecucion llega a este punto, se debe a que
			el administrador inicio sesion, por lo tanto, se debe mostrar
			esta lista
			*/
			findAll();
		}]);
