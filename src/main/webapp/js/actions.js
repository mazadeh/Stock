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
	$('.cashe-box').show();
	$('.customer-share-table').show();
	$('.sell-buy-table').show();
	$('.customer-table').show();
	$('.increase-cashe-table').show();
}

var hideDashboard = function()
{
	$('.dashboard-sidebar').hide();
	$('.cashe-box').hide();
	$('.customer-share-table').hide();
	$('.sell-buy-table').hide();
	$('.customer-table').hide();
	$('.increase-cashe-table').hide();
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

var reload;

var refresh = function()
{
	if (reload != null)
		reload();
	setTimeout(refresh, 15000);
}

refresh();
