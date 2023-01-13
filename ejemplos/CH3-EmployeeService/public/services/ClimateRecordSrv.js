app.service("ClimateRecordSrv", ["$http", function ($http) {

    this.findAll = function (callback) {
        $http.get("rest/climateRecord").then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    };

    this.find = function (id, callback) {
        $http.get("rest/climateRecord/" + id).then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    };

    this.create = function (data, callback) {
        $http.post("rest/climateRecord/", data)
            .then(
                function (result) {
                    callback(false, result.data);
                },
                function (error) {
                    callback(error);
                });
    };

    this.delete = function (id, callback) {
        $http.delete("rest/climateRecord/" + id).then(
            function (result) {
                callback(false, result.data);
            },
            function (error) {
                callback(error);
            });
    };

    this.modify = function (data, callback) {
        $http.put("rest/climateRecord/" + data.id, data)
            .then(
                function (result) {
                    callback(false, result.data);
                },
                function (error) {
                    callback(error);
                });
    };

}]);
