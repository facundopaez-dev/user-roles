app.service("CropSrv", ["$http", function ($http) {

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