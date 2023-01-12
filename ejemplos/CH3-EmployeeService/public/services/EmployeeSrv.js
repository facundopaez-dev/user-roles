app.service(
	"EmployeeSrv",
	["$http",
		function ($http) {

			this.findAll = function (callback) {
				$http.get("rest/employees").then(
					function (result) {
						callback(false, result.data);
					},
					function (error) {
						callback(error);
					});
			}

			this.find = function (id, callback) {
				$http.get("rest/employees/" + id).then(
					function (result) {
						callback(false, result.data);
					},
					function (error) {
						callback(error);
					});
			}

			this.save = function (data, callback) {
				$http.post("rest/employees", data)
					.then(
						function (result) {
							callback(false, result.data);
						},
						function (error) {
							callback(error);
						});
			}

			this.updateSalary = function (id, salary, callback) {
				console.log("Actualizando: " + id + " - " + salary);
				$http({
					method: "PUT",
					url: "rest/employees/" + id,
					params: { "salary": salary }
				})
					.then(
						function (result) {
							callback(false, result.data);
						},
						function (error) {
							callback(error);
						});
			}

			this.delete = function (id, callback) {
				$http.delete("rest/employees/" + id)
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
