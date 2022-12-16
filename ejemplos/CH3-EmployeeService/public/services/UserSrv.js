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

			this.authenticateUser = function (data, callback) {
				$http.post("rest/auth/user", data)
					.then(
						function (result) {
							callback(false, result.data);
						},
						function (error) {
							callback(error);
						});
			};

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

			this.isSuperuser = function (data, callback) {
				$http.get("rest/users/checkSuperuserPermission/" + data.username).then(
					function (result) {
						callback(false, result.data);
					},
					function (error) {
						callback(error);
					});
			}

		}
	]);
