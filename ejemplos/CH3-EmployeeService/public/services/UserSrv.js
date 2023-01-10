app.service(
	"UserSrv",
	["$http", "JwtManager",
		function ($http, jwtManager) {
			/*
			Por convencion, se usa la palabra "Bearer" en el encabezado de
			autorizacion de una peticion HTTP para indicar que se usa un
			JWT para autenticacion (principalmente), y ademas y opcionalmente,
			tambien para autorizacion.
			
			Por lo tanto, si el primer valor del encabezado de autorizacion de
			una peticion HTTP es la palabra "Bearer", entonces el segundo
			valor es un JWT.
			*/
			$http.defaults.headers.common.Authorization = 'Bearer ' + jwtManager.getJwt();

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
