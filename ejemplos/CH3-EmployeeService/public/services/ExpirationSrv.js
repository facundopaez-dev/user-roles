app.service("ExpirationSrv", ["$http", function ($http) {

    /*
    Esta funcion es utilizada por los controllers (Home.js y AdminHome.js)
    de las paginas web que no realizan peticiones HTTP, para comprobar si
    el JWT del usuario que tiene una sesion abierta, expiro o no
    */
    this.checkExpiration = function (callback) {
        $http.get("rest/expiration").then(
            function (result) {
                callback(false);
            },
            function (error) {
                callback(error);
            });
    };

}]);