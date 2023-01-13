app.service(
    "ParcelSrv",
    ["$http",
        function ($http) {

            this.findAll = function (callback) {
                $http.get("rest/parcel").then(
                    function (result) {
                        callback(false, result.data);
                    },
                    function (error) {
                        callback(error);
                    });
            }

            this.find = function (id, callback) {
                $http.get("rest/parcel/" + id).then(
                    function (result) {
                        callback(false, result.data);
                    },
                    function (error) {
                        callback(error);
                    });
            }

            this.save = function (data, callback) {
                $http.post("rest/parcel", data)
                    .then(
                        function (result) {
                            callback(false, result.data);
                        },
                        function (error) {
                            callback(error);
                        });
            }

            this.update = function (data, callback) {
                $http.put("rest/parcel/" + data.id, data)
                    .then(
                        function (result) {
                            callback(false, result.data);
                        },
                        function (error) {
                            callback(error);
                        });
            };

            this.delete = function (id, callback) {
                $http.delete("rest/parcel/" + id)
                    .then(
                        function (result) {
                            callback(false, result.data);
                        },
                        function (error) {
                            callback(error);
                        });
            }

        }
    ]);
