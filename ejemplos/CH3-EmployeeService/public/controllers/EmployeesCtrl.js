app.controller(
	"EmployeesCtrl",
	["$scope", "$location", "$route", "EmployeeSrv", "AccessFactory",
		function ($scope, $location, $route, servicio, AccessFactory) {
			console.log("EmployeesCtrl loaded...")

			function findAll() {
				servicio.findAll(function (error, data) {
					if (error) {
						alert("Ocurrió un error: " + error);
						return;
					}
					$scope.data = data;
				})
			}

			$scope.delete = function (id) {
				console.log("Deleting: " + id);

				servicio.delete(id, function (error, data) {
					if (error) {
						console.log(error);
						return;
					}

					$location.path("/home/employee");
					$route.reload();
				});
			}

			$scope.logout = function () {
				/*
				El objeto $scope envia el evento llamado "CallLogout" hacia arriba
				en la jerarquia de objetos $scope. Esto es necesario para implementar
				el cierre de sesion del usuario cliente, cierre que es llevado a cabo por el
				archivo HomeCtrl, en el cual esta suscrito el objeto $rootScope al evento
				"CallLogout". Cuando el objeto $rootScope escucha el evento "CallLogout",
				invoca a la funcion logout(), la cual esta definida en el archivo mencionado.
				*/
				$scope.$emit("CallLogout", {});
			}

			/*
			Para ver el listado de empleados, el usuario tiene que iniciar sesion,
			por lo tanto, si no tiene una sesion abierta, se le debe impedir el acceso
			a la pagina de listado de empleados y se lo debe redirigir a la pagina de
			inicio de sesion
			*/
			if (!AccessFactory.isUserLoggedIn()) {
				$location.path(AccessFactory.getLoginRoute());
				return;
			}

			/*
			Si el flujo de ejecucion llega a este punto, se debe a que
			el usuario inicio sesion, por lo tanto, se debe mostrar
			esta lista
			*/
			findAll();
		}]);
