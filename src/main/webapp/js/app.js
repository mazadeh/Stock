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
					console.log(response);
			});
		}
		
		this.signUp = function()
		{
			$http({
				method: 'POST',
				url: 'http://localhost:8080/stock/customer/add',
		    params: { id: stockCtrl.currentUser.id , password: stockCtrl.currentUser.password ,
		    					firstname: stockCtrl.currentUser.firstname , lastname: stockCtrl.currentUser.lastname }
				}).then(function(response) {
					console.log(response);
					if (response.data.hasOwnProperty('id'))
					{
						stockCtrl.currentUser = response.data;
						$('#signUpModal').modal('hide');
					}
					else
					{
						var ul = document.createElement('ul');
						for (var error in response.data)
						{
							var li = document.createElement('li');
							var errText = document.createTextNode(response.data[error]);
							li.appendChild(errText);
							ul.appendChild(li);
						}

						var element = document.getElementById("signUpModalError");
						element.innerHTML = '';
						element.appendChild(ul);
					}
			});
		}
	}]);
})();
