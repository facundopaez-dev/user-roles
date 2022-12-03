app.controller(
	"UsersCtrl",
	["$scope", "UserSrv",
		function ($scope, service) {
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

			findAll();
		}]);
