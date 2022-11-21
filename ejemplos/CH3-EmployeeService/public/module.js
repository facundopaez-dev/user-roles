var app = angular.module('app', ['ngRoute']);

app.run(function (AccessFactory) {
	AccessFactory.setKeyStore();
	// console.log("Se establecio el valor de key store");
	// console.log("El valor de key store es: " + AccessFactory.getKeyStore());
});

app.config(['$routeProvider', function (routeprovider) {

	routeprovider
		.when('/', {
			resolve: {
				check: function ($location, $window, AccessFactory) {
					/*
					Si el usuario se autentifico correctamente, sus datos estan
					almacenados en la sesion del navegador web. Por lo tanto, si
					vuelve a la pantalla de inicio de sesion, se lo redirecciona
					a la pantalla de inicio
					*/
					// if ($window.sessionStorage.getItem("loggedUser")) {
					if ($window.sessionStorage.getItem(AccessFactory.getKeyStore())) {
						console.log("Usuario con sesion ya iniciada");
						console.log("Retroceso a home");
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
		.otherwise({
			templateUrl: 'partials/404.html'
		})
}]);

app.factory('AccessFactory', function () {
	/*
	TODO: Lo que se me ocurre es almacenar este secreto (keyStore) en el servidor, el cual
	lo debera devolver hasheado al cliente, el cual lo debera almacenar en una variable
	como esta para que sea utilizado para hacer el chequeo de la ruta del login y en las
	demas rutas que se lo necesite
	*/
	let keyStore;

	return {
		setKeyStore: function () {
			// console.log("Establecio el valor de la variable loggedUser");
			keyStore = "loggedUser";
		},

		getKeyStore: function () {
			// console.log("Valor de key store dentro de su factory: " + keyStore);
			return keyStore;
		}
	}
});

// TODO: Proteger las rutas a las que el usuario no debe acceder sin una sesion iniciada
// TODO: Proteger las peticiones que el usuario no debe realizar sin una sesion iniciada