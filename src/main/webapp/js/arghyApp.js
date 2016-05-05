(function(){

	var app = angular.module('arghyStock', []);
	var Users;

	app.controller('StockController', ['$http', function($http){
		var usersCtrl = this;
		this.users = [];
		this.symbols = [];
		this.selected = null;
	
		$http.get('http://localhost:3000/users').success(function(pollsData) {
		    usersCtrl.users = pollsData;
		});
		$http.get('http://localhost:3000/symbols').success(function(symbolsData) {
		    usersCtrl.symbols = symbolsData;
		});
		
	   this.symbolClicked = function(id) {
            if (this.selected != id) {
                this.selected = id;           
            }
        };

        this.isSelected = function (id) {
            return id === this.selected;
        };
		
	   this.collapse = function($event) {
            this.selected = null;
            $event.stopPropagation();
        };
		
	}]);
	
	
})();
