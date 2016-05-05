(function(){

	var app = angular.module('stock', []);

	var Users;

	app.controller('StockController', ['$http', function($http){
		var usersCtrl = this;
		this.users = [];
	
		$http.get('http://localhost:8080/stock/customer/get?id=1').success(function(usersData) {
		    usersCtrl.users = usersData;
		    console.log(usersData);
		});
		
	}]);
	
})();
