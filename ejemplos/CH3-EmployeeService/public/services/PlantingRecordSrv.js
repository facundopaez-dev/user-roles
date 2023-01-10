app.service("PlantingRecordSrv", ["$http", "JwtManager", function ($http, jwtManager) {
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
        $http.get("rest/plantingRecord").then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    };

    this.find = function (id, callback) {
        $http.get("rest/plantingRecord/" + id).then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    }

    this.create = function (data, callback) {
        $http.post("rest/plantingRecord", data)
            .then(
                function (result) {
                    callback(false, result.data);
                },
                function (error) {
                    callback(error);
                });
    };

    this.delete = function (id, callback) {
        $http.delete("rest/plantingRecord/" + id).then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    };

    this.modify = function (plantingRecord, callback) {
        $http.put("rest/plantingRecord/" + plantingRecord.id, plantingRecord)
            .then(
                function (result) {
                    callback(false, result.data);
                },
                function (error) {
                    callback(error);
                });
    };

}]);
