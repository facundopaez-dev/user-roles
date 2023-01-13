app.service("PlantingRecordSrv", ["$http", function ($http) {

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
