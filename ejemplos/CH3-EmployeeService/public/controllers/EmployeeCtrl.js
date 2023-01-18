app.controller(
  "EmployeeCtrl",
  ["$scope", "$location", "$routeParams", "EmployeeSrv", "AccessManager",
    function ($scope, $location, $params, servicio, accessManager) {

      console.log("EmployeeCtrl loaded with action: " + $params.action);

      /*
      Con el uso de JWT se evita que el usuario cree, edite o visualice
      un dato, correspondiente a este controller, sin tener una sesion
      abierta, pero sin este control, el usuario puede acceder la pagina
      de inicio sin tener una sesion abierta. Por lo tanto, si el
      usuario NO tiene una sesion abierta, se le impide el acceso a la
      pagina de inicio y se lo redirige a la pagina de inicio de sesion.
      */
      if (!accessManager.isUserLoggedIn()) {
        $location.path("/");
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

      if ($scope.action == 'new' || $scope.action == 'edit' || $scope.action == 'view') {
        /*
        Si el usuario que tiene una sesion abierta tiene permiso de
        administrador, se lo redirige a la pagina de inicio del
        administrador. De esta manera, un administrador debe cerrar
        la sesion que abrio a traves de la pagina web de inicio de sesion
        del administrador, y luego abrir una sesion a traves de la pagina
        web de inicio de sesion del usuario para poder acceder a la pagina web
        de creacion, edicion o visualizacion de un dato correspondiente
        a este controller.
        */
        if (accessManager.isUserLoggedIn() && accessManager.loggedAsAdmin()) {
            $location.path("/adminHome");
            return;
        }
    }

    if ($scope.action == 'edit' || $scope.action == 'view') {
        find($params.id);
      }

    }]);