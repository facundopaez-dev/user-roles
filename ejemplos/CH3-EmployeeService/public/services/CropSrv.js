app.service("CropSrv", ["$http", "JwtManager", function ($http, jwtManager) {
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
        $http.get("rest/crop").then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    };

    this.create = function (data, callback) {
        $http.post("rest/crop/", data)
            .then(
                function (result) {
                    callback(false, result.data);
                },
                function (error) {
                    callback(error);
                });
    };

    this.remove = function (id, callback) {
        $http.delete("rest/crop/" + id).then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    };

    this.modify = function (data, callback) {
        $http.put("rest/crop/" + data.id, data)
            .then(
                function (result) {
                    callback(false, result.data);
                },
                function (error) {
                    callback(error);
                });
    };

    this.find = function (id, callback) {
        $http.get("rest/crop/" + id).then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    }

}]);