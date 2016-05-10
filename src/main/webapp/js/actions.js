var showHome = function()
{
	hideAll();
	document.getElementById('homeCarousel').style.display = 'block';
}

var hideHome = function()
{
	document.getElementById('homeCarousel').style.display = 'none';
}

var showDashboard = function()
{
	hideAll();
	$('.dashboard-sidebar').show();
	$('.sell-buy-table').show();
	$('.customer-table').show();
}

var hideDashboard = function()
{
	$('.dashboard-sidebar').hide();
	$('.sell-buy-table').hide();
	$('.customer-table').hide();
}

var showStatus = function()
{
	hideAll();
	$('.symbol-table').show();
}

var hideStatus = function()
{
	$('.symbol-table').hide();
}

var hideAll = function()
{
	hideHome();
	hideDashboard();
	hideStatus();
}
