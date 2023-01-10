app.controller(
	"UsersCtrl",
	["$scope", "$location", "$window", "UserSrv", "AccessFactory",
		function ($scope, $location, $window, service, AccessFactory) {
			console.log("UsersCtrl loaded...")

            /*
            Esta variable se utiliza para mostrar u ocultar los botones de la barra de
            navegacion de la pagina de inicio del administrador. Si esta variable
            tiene el valor false, se ocultan los botones. En cambio, si tiene el valor
            true, se muestran los botones.
            */
            $scope.superuserPermission = true;

			function findAll() {
				service.findAll(function (error, data) {
					if (error) {
						alert("Ocurrió un error: " + error);
						return;
					}
					
					$scope.data = data;
				})
			}

            $scope.logout = function () {
                /*
                El objeto $scope envia el evento llamado "AdminLogoutCall" hacia arriba
                en la jerarquia de objetos $scope. Esto es necesario para implementar
                el cierre de sesion del administrador, cierre que es llevado a cabo por el
                archivo AdminHomeCtrl, en el cual esta suscrito el objeto $rootScope al evento
                "AdminLogoutCall". Cuando el objeto $rootScope escucha el evento "AdminLogoutCall",
                invoca a la funcion logout(), la cual esta definida en el archivo mencionado.
                */
                $scope.$emit("AdminLogoutCall", {});
            }

            /*
            Para ver el listado de usuarios registrados en el sistema, el administrador
			tiene que iniciar sesion, por lo tanto, si no tiene una sesion abierta, se le
			debe impedir el acceso a la pagina de listado de usuarios registrados en el
			sistema y se lo debe redirigir a la pagina de inicio de sesion del administrador
            */
            if (!AccessFactory.isUserLoggedIn()) {
                $location.path(AccessFactory.getAdminLoginRoute());
                return;
            }

            /*
            Esta funcion comprueba que el usuario conectado tenga el permiso de super usuario
            (administrador). Si el usuario conectado no tiene dicho permiso, se muestra el mensaje
            "Acceso no autorizado" y se lo redirige a la pagina de inicio del usuario. En cambio,
            si lo tiene, se muestra la pagina correspondiente a este controlador y se muestran
            los botones de la barra de navegacion del administrador. La manera en la que se
            muestran estos botones es asignando el valor true a la variable superuserPermission.
            */
            // function isSuperuser() {
            //     /*
            //     El contenido almacenado en el almacenamiento de sesion del navegador web es un string
            //     en formato JSON, con lo cual, se lo debe convertir para poder accedr a sus propiedades
            //     */
            //     let data = JSON.parse($window.sessionStorage.getItem(AccessFactory.getKeyStore()));

            //     service.isSuperuser(data, function (error, data) {
            //         if (error) {
            //             alert(error.data.message);
            //             $location.path("/home");
            //             return;
            //         }

            //         $scope.superuserPermission = true;
            //     })
            // }

            // isSuperuser();

			/*
			Si el flujo de ejecucion llega a este punto, se debe a que
			el administrador inicio sesion, por lo tanto, se debe mostrar
			esta lista
			*/
			findAll();
		}]);
