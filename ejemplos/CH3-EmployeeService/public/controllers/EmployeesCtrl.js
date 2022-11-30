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
				/*
				TODO: Pensar en si es necesario tener en cuenta el permiso del usuario
				para poder realizar esta accion

				Entiendo que si se impide el acceso a la lista de empleados cuando
				el usuario no esta logeado, tambien se impide la accion de borrado
				*/

				console.log("Deleting: " + id);

				servicio.delete(id, function (error, data) {
					if (error) {
						console.log(error);
						return;
					}
					$location.path("/home/employee");
					$route.reload()
				});
			}

			/*
			Si el usuario no inicio sesion, se lo redirige a la pantalla
			de inicio de sesion
			*/
			if (!AccessFactory.isUserLoggedIn()) {
				$location.path(AccessFactory.getLoginRoute());
				return;
			}

			/*
			TODO: Hay que tener en cuenta el permiso que tiene el usuario
			a la hora de mostrar la lista de empleados, ya que el usuario
			que tiene permiso privilegiado tambien sea logea, y al estar
			logeado tiene acceso a los empleados, cuando en realidad no
			deberia
			*/

			/*
			Si el flujo de ejecucion llega a este punto, se debe a que
			el usuario inicio sesion, por lo tanto, se debe mostrar
			esta lista
			*/
			findAll();
		}]);
