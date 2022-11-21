app.controller(
  "EmployeeCtrl",
  ["$scope", "$location", "$routeParams", "EmployeeSrv",
    function ($scope, $location, $params, servicio) {

      console.log("EmployeeCtrl loaded with action: " + $params.action)

      if (['new', 'edit', 'view'].indexOf($params.action) == -1) {
        alert("Acción inválida: " + $params.action);
        $location.path("/home/employee");
      }

      function find(id) {
        servicio.find(id, function (error, data) {
          if (error) {
            console.log(error);
            return;
          }
          $scope.data = data;
        });
      }

      $scope.save = function () {
        servicio.save($scope.data, function (error, data) {
          if (error) {
            console.log(error);
            return;
          }
          $scope.data = data;
          $location.path("/home/employee");
        });
      }

      $scope.update = function () {
        servicio.updateSalary(
          $scope.data.id,
          $scope.data.salary,
          function (error, data) {
            if (error) {
              console.log(error);
              return;
            }
            $scope.data = data;
            $location.path("/home/employee");
          });
      }

      $scope.cancel = function () {
        $location.path("/home/employee");
      }

      $scope.action = $params.action;

      if ($scope.action == 'edit' || $scope.action == 'view') {
        find($params.id);
      }

    }]);