app.controller(
  "EmployeeCtrl",
  ["$scope", "$location", "$routeParams", "EmployeeSrv", "AccessFactory",
    function ($scope, $location, $params, servicio, AccessFactory) {

      console.log("EmployeeCtrl loaded with action: " + $params.action);

      /*
      Para crear, editar y ver un empleado, el usuario tiene que
      iniciar sesion, por lo tanto, si no tiene una sesion abierta,
      se le debe impedir el acceso a las rutas de creacion, edicion
      y visualizacion de un empleado
      */
      if (!AccessFactory.isUserLoggedIn()) {
        $location.path(AccessFactory.getLoginRoute());
        return;
      }

      if (['new', 'edit', 'view'].indexOf($params.action) == -1) {
        alert("Acción inválida: " + $params.action);
        $location.path("/home/employee");
      }

      function find(id) {
        servicio.find(id, function (error, data) {
          if (error) {
            console.log(error);
            alert(error.data.message);
            $location.path("/home/employee");
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

      $scope.logout = function () {
        /*
        El objeto $scope envia el evento llamado "CallLogout" hacia arriba
        en la jerarquia de objetos $scope. Esto es necesario para implementar
        el cierre de sesion del usuario cliente, cierre que es llevado a cabo por el
        archivo HomeCtrl, en el cual esta suscrito el objeto $rootScope al evento
        "CallLogout". Cuando el objeto $rootScope escucha el evento "CallLogout",
        invoca a la funcion logout(), la cual esta definida en el archivo mencionado.
        */
        $scope.$emit("CallLogout", {});
      }

      $scope.action = $params.action;

      if ($scope.action == 'edit' || $scope.action == 'view') {
        find($params.id);
      }

    }]);