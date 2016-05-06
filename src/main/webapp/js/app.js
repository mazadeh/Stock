(function(){

	var app = angular.module('stock', []);

	app.controller('StockController', ['$http', function($http){
		var stockCtrl = this;
		this.users = [];
		this.currentUser = {firstname: 'میهمان'};
	
		$http.get('http://localhost:8080/stock/customer/get').success(function(usersData) {
		    stockCtrl.users = usersData;
		    console.log(usersData);
		});
		
		this.signIn = function()
		{
			//console.log('login: ');
			//console.log(stockCtrl.currentUser);
			$http({
				method: 'POST',
				url: 'http://localhost:8080/stock/customer/get', 
		    params: { id: stockCtrl.currentUser.id }
				}).then(function(response) {
					var user = response.data;
					if (user === 'null')
					{
						console.log('Customer does not exist!');
						var element = document.getElementById('signInModalError');
						element.innerHTML = 'چنین کاربری تعریف نشده است!';
					}
					else if (stockCtrl.currentUser.password === user.password)
					{
						stockCtrl.currentUser = user;
						console.log('Hello ' + stockCtrl.currentUser.firstname);
						$('#signInModal').modal('hide');
					}
					else
					{
						console.log('Wrong password!');
						var element = document.getElementById('signInModalError');
						element.innerHTML = 'گذرواژه صحیح نمی باشد!';
					}
					// alert(response);
					//console.log(user);
			});
		}
	}]);
})();
