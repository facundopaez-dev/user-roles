app.service(
	"UserSrv",
	["$http",
		function ($http) {

			this.authenticateUser = function (username, password, callback) {
				$http({
					url: 'rest/users/authentication/' + username,
					method: 'GET',
					params: { "password": password }
				}).then(
					function (response) {
						return callback(false, response.data);
					},
					function (response) {
						return callback(response);
					});
			}

		}
	]);
