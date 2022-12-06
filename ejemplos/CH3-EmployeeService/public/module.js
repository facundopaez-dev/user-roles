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
			resolve: {
				check: function ($location, AccessFactory) {
					/*
					Si el usuario se autentifico correctamente, sus datos estan
					almacenados en la sesion del navegador web. Por lo tanto, si
					vuelve a la pagina de inicio de sesion, se lo redirecciona
					al home (pagina de inicio)
					*/
					if (AccessFactory.isUserLoggedIn()) {
						console.log("Usuario con sesion ya iniciada");
						console.log("Redireccionamiento a home (pagina de inicio)");
						$location.path("/home");
					}
				}
			},
			templateUrl: 'partials/login.html',
			controller: 'AccessCtrl'
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

		.when('/crop', {
			templateUrl: 'partials/crop-list.html',
			controller: 'CropsCtrl'
		})
		.when('/crop/:action', {
			templateUrl: 'partials/crop-form.html',
			controller: 'CropCtrl'
		})
		.when('/crop/:action/:id', {
			templateUrl: 'partials/crop-form.html',
			controller: 'CropCtrl'
		})

		.when('/user', {
			templateUrl: 'partials/user-list.html',
			controller: 'UsersCtrl'
		})
		.when('/user/:action', {
			templateUrl: 'partials/user-form.html',
			controller: 'UserCtrl'
		})
		.when('/user/:action/:id', {
			templateUrl: 'partials/user-form.html',
			controller: 'UserCtrl'
		})

		.when('/admin', {
			templateUrl: 'partials/login-admin.html',
			controller: 'AccessCtrl'
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

		isUserLoggedIn: function () {
			/*
			Si el usuario se autentifico correctamente, sus datos estan
			almacenados en la sesion del navegador web (ver funcion login del
			archivo AccessCtrl.js). Por lo tanto, isUserLoggedIn retorna el valor
			booleano true.
			*/
			if ($window.sessionStorage.getItem(keyStore)) {
				return true;
			}

			return false;
		},
	}
});

// TODO: Proteger las peticiones que el usuario no debe realizar sin una sesion iniciada