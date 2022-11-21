app.controller(
	"EmployeesCtrl",
	["$scope", "$location", "$route", "EmployeeSrv",
		function ($scope, $location, $route, servicio) {
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
					$route.reload()
				});
			}


			findAll();
		}]);
