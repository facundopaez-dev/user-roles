app.controller(
	"UsersCtrl",
	["$scope", "$location", "UserSrv", "AccessFactory",
		function ($scope, $location, service, AccessFactory) {
			console.log("UsersCtrl loaded...")

			function findAll() {
				service.findAll(function (error, data) {
					if (error) {
						alert("Ocurrió un error: " + error);
						return;
					}
					
					$scope.data = data;
				})
			}

            /*
            Para ver el listado de usuarios registrados en el sistema, el administrador
			tiene que iniciar sesion, por lo tanto, si no tiene una sesion abierta, se le
			debe impedir el acceso a la pagina de listado de usuarios registrados en el
			sistema y se lo debe redirigir a la pagina de inicio de sesion del administrador
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getAdminLoginRoute());
                return;
            }

			/*
			Si el flujo de ejecucion llega a este punto, se debe a que
			el administrador inicio sesion, por lo tanto, se debe mostrar
			esta lista
			*/
			findAll();
		}]);
