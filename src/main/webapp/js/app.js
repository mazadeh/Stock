(function(){

	var app = angular.module('stock', []);

	var Users;

	app.controller('StockController', ['$http', function($http){
		var usersCtrl = this;
		this.users = [];
	
		$http.get('http://localhost:3000/users').success(function(pollsData) {
		    usersCtrl.users = pollsData;
		});
		
	}]);
	
})();
