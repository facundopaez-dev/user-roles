var app = angular.module('app',['ngRoute']);

app.config(['$routeProvider', function(routeprovider) {

	routeprovider
	.when('/',{
		templateUrl:'partials/employee-list.html',
		controller: 'EmployeesCtrl'
	})
	.when('/:action',{
		templateUrl:'partials/employee-form.html',
		controller: 'EmployeeCtrl'
	})
	.when('/:action/:id',{
		templateUrl:'partials/employee-form.html',
		controller: 'EmployeeCtrl'
	})
	.otherwise({
		templateUrl: 'partials/404.html'
	})
}])
