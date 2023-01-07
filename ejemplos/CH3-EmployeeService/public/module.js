var app = angular.module('app', ['ngRoute']);

/*
Cuando el inyector termina de cargar los modulos, se debe inicializar
la variable keyStore del factory AccessFactory. Esta variable contiene
la clave con la que se accede al usuario almacenado en la sesion, el cual
se almacena cuando inicia sesion en la aplicacion. Por lo tanto, debe ser
incializada antes de utilizarse.
*/
app.run(function (AccessFactory) {
	AccessFactory.setKeyStore();
	// console.log("Se establecio el valor de key store");
	// console.log("El valor de key store es: " + AccessFactory.getKeyStore());
});

app.config(['$routeProvider', function (routeprovider) {

	routeprovider
		.when('/', {
			templateUrl: 'partials/user/user-login.html',
			controller: 'UserLoginCtrl'
		})
		.when('/home', {
			templateUrl: 'partials/home.html',
			controller: 'HomeCtrl'
		})

		.when('/home/employee', {
			templateUrl: 'partials/employee-list.html',
			controller: 'EmployeesCtrl'
		})
		.when('/home/employee/:action', {
			templateUrl: 'partials/employee-form.html',
			controller: 'EmployeeCtrl'
		})
		.when('/home/employee/:action/:id', {
			templateUrl: 'partials/employee-form.html',
			controller: 'EmployeeCtrl'
		})

		.when('/home/parcel', {
			templateUrl: 'partials/parcel-list.html',
			controller: 'ParcelsCtrl'
		})
		.when('/home/parcel/:action', {
			templateUrl: 'partials/parcel-form.html',
			controller: 'ParcelCtrl'
		})
		.when('/home/parcel/:action/:id', {
			templateUrl: 'partials/parcel-form.html',
			controller: 'ParcelCtrl'
		})

		.when('/home/plantingRecord', {
			templateUrl: 'partials/plantingRecord-list.html',
			controller: 'PlantingRecordsCtrl'
		})
		.when('/home/plantingRecord/:action', {
			templateUrl: 'partials/plantingRecord-form.html',
			controller: 'PlantingRecordCtrl'
		})
		.when('/home/plantingRecord/:action/:id', {
			templateUrl: 'partials/plantingRecord-form.html',
			controller: 'PlantingRecordCtrl'
		})

		.when('/home/climateRecord', {
			templateUrl: 'partials/climateRecord-list.html',
			controller: 'ClimateRecordsCtrl'
		})
		.when('/home/climateRecord/:action', {
			templateUrl: 'partials/climateRecord-form.html',
			controller: 'ClimateRecordCtrl'
		})
		.when('/home/climateRecord/:action/:id', {
			templateUrl: 'partials/climateRecord-form.html',
			controller: 'ClimateRecordCtrl'
		})

		.when('/admin', {
			templateUrl: 'partials/admin/admin-login.html',
			controller: 'AdminLoginCtrl'
		})

		.when('/adminHome', {
			templateUrl: 'partials/admin-home.html',
			controller: 'AdminHomeCtrl'
		})

		.when('/adminHome/crop', {
			templateUrl: 'partials/crop-list.html',
			controller: 'CropsCtrl'
		})
		.when('/adminHome/crop/:action', {
			templateUrl: 'partials/crop-form.html',
			controller: 'CropCtrl'
		})
		.when('/adminHome/crop/:action/:id', {
			templateUrl: 'partials/crop-form.html',
			controller: 'CropCtrl'
		})

		.when('/adminHome/user', {
			templateUrl: 'partials/user-list.html',
			controller: 'UsersCtrl'
		})
		.when('/adminHome/user/:action', {
			templateUrl: 'partials/user-form.html',
			controller: 'UserCtrl'
		})
		.when('/adminHome/user/:action/:id', {
			templateUrl: 'partials/user-form.html',
			controller: 'UserCtrl'
		})

		.otherwise({
			templateUrl: 'partials/404.html'
		})
}]);

app.factory('AccessFactory', function ($window) {
	/*
	TODO: Lo que se me ocurre es almacenar este secreto (keyStore) en el servidor, el cual
	lo debera devolver hasheado al cliente, el cual lo debera almacenar en una variable
	como esta para que sea utilizado para hacer el chequeo de la ruta del login y en las
	demas rutas que se lo necesite
	*/
	/*
	Esta variable contiene el valor (clave) con la que se recupera el usuario
	almacenado en la sesion del navegador web
	*/
	let keyStore;
	const loginRoute = "/";
	const adminLoginRoute = "/admin";

	return {
		setKeyStore: function () {
			// console.log("Establecio el valor de la variable loggedUser");
			keyStore = "loggedUser";
		},

		getKeyStore: function () {
			// console.log("Valor de key store dentro de su factory: " + keyStore);
			return keyStore;
		},

		getLoginRoute: function () {
			return loginRoute;
		},

		getAdminLoginRoute: function () {
			return adminLoginRoute;
		},

		isUserLoggedIn: function () {
			/*
			Si el usuario se autentico correctamente, el JWT asociado a
			el esta almacenado en el almacenamiento de sesion del navegador
			web (ver los archivos AdminLoginCtrl.js y UserLoginCtrl.js). Por
			lo tanto, esta funcion retorna el valor booleano true.
			*/
			if ($window.sessionStorage.getItem(keyStore)) {
				return true;
			}

			return false;
		},
	}
});

/*
JwtManager es la factory que se utiliza para el almacenamiento,
obtencion y eliminacion de JWT
*/
app.factory('JwtManager', function ($window) {
	/*
	Con el valor de esta constante se obtiene el JWT del usuario que se
	autentica satisfactoriamente
	*/
	const key = "loggedUser";

	return {
		/*
		Cuando el usuario se autentica satisfactoriamente, se debe invocar
		a esta funcion para almacenar el JWT del usuario en el almacenamiento
		de sesion del navegador web
		*/
		setJwt: function (jwt) {
			$window.sessionStorage.setItem(key, jwt);
		},

		/*
		Esta funcion es necesaria para establecer el JWT (del usuario
		que se autentica satisfactoriamente) en el encabezado de
		autorizacion de cada peticion HTTP sea del cliente REST que
		sea, un navegador web, una aplicacion del estilo POSTMAN, etc.
		*/
		getJwt: function () {
			return $window.sessionStorage.getItem(key);
		},

		/*
		Esta funcion debe ser invocada cuando el usuario cierra su
		sesion, momento en el cual se debe eliminar su JWT del
		almacenamiento de sesion del navegador web
		*/
		removeJwt: function () {
			$window.sessionStorage.removeItem(key);
		}
	}
});

// TODO: Proteger las peticiones que el usuario no debe realizar sin una sesion iniciada