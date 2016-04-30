(function(){

var app = angular.module('store', []);

app.controller('StoreController', function(){
	this.product = gem;
	this.address = arraytext;
});

var gem = {
	name: 'Dodecahedron',
	price: 2.95,
	description: '. . .',
}

var myloc = window.location.href;
var locarray = myloc.split("/");
delete locarray[(locarray.length-1)];
var arraytext = locarray.join("/");

})();
