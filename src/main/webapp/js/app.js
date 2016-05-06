(function(){

	var app = angular.module('stock', []);

	var Users;
	
	var stockCtrl = this;

	app.controller('StockController', ['$http', function($http){
		var usersCtrl = this;
		this.users = [];
	
		$http.get('http://localhost:8080/stock/customer/get').success(function(usersData) {
		    usersCtrl.users = usersData;
		    console.log(usersData);
		});
		
	}]);
	
	app.controller('FormController', ['$http', function($http){
		
		var tmp = this;
		var customer = {};
		this.addReview = function(product)
		{
			console.log(tmp.review);
		}
		
		this.signIn = function()
		{
			console.log('login: ');
			console.log(tmp.customer);
			$http({
				method: 'GET',
				url: 'http://localhost:8080/stock/customer/get', 
		    params: { id: tmp.customer.id }
				}).then(function(response) {
					var user = response.data;
					if (user === 'null')
					{
						console.log('Customer does not exist!');
					}
					else if (tmp.customer.password === user.password)
					{
						tmp.customer = user;
						console.log('Hello ' + tmp.customer.firstname);
					}
					else
					{
						console.log('Wrong password!');
					}
					// alert(response);
					console.log(user);
			});
		}
	}]);
	
})();
