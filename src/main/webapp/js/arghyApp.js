(function(){

	var app = angular.module('stock', []);

	var Users;
	
	var stockCtrl = this;

	app.controller('StockController', ['$http', function($http){
		var stockCtrl = this;
		this.users = [];
		this.symbols = [];
	
		$http.get('http://localhost:8080/stock/customer/get').success(function(usersData) {
		    stockCtrl.users = usersData;
		    console.log(usersData);
		});
		$http.get('http://localhost:8080/stock/symbol/get').success(function(symbolsData) {
		    stockCtrl.symbols = symbolsData;
		    console.log(symbolsData);
		});
		
	     this.symbolClicked = function(id)
	     {
              if (this.selected != id) {
                  this.selected = id;           
              }
          };

          this.isSelected = function (id)
          {
              return id === this.selected;
          };
		
	     this.collapse = function($event)
	     {
              this.selected = null;
              $event.stopPropagation();
          };
          
          this.showSymbols = function()
		{
			console.log('login: ');
			console.log(stockCtrl.symbols);
			$http({
				method: 'GET',
				url: 'http://localhost:8080/stock/symbol/get'
				}).then(function(response) {
					var symbols = response.data;
					if (symbols === 'null')
					{
						console.log('Symbols Not Recieved!');
					}
					
					else
					{
						console.log('Successfully Recieved!');
					}
					// alert(response);
					console.log(symbols);
			});
		}
          
		
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
