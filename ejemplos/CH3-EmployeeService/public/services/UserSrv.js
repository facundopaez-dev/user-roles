app.service(
	"UserSrv",
	["$http",
		function ($http) {

			this.findAll = function (callback) {
				$http.get("rest/users").then(
					function (result) {
						callback(false, result.data);
					},
					function (error) {
						callback(error);
					});
			}

			this.find = function (id, callback) {
				$http.get("rest/users/" + id).then(
					function (result) {
						callback(false, result.data);
					},
					function (error) {
						callback(error);
					});
			}

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

			this.authenticateAdmin = function (data, callback) {
				$http.post("rest/users/authenticationAdmin", data)
					.then(
						function (result) {
							callback(false, result.data);
						},
						function (error) {
							callback(error);
						});
			};

		}
	]);
