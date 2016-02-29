var i=0;
var points;
function InitProject()
{
	console.log("inside init project");
	points = new google.maps.MVCArray();
	var data = getStoredTweets();
	data!="" ? heatmap.setData(getLatLngFromString(data)) : console.log("Data is null");
	timer = setInterval(function() {
		getRealTimeTweets();
	}, 5000);
}

function getRealTimeTweets(){
	console.log("inside getRealTimeTweets");
	var data = getDataFromDynamo();
	console.log("Response from ajax servlet "+data+" --> Plotting it on map...");
	//points.push(getLatLngFromString(data))
	data!="" ? heatmap.setData(getLatLngFromString(data)) : console.log("Data is null");
	
}

function getStoredTweets(){
	console.log("inside StoredTweets");
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open("GET", "InitialLoadServlet", false);
	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlhttp.send();
	return xmlhttp.responseText;
}

function getDataFromDynamo(){
	console.log("inside getRealTimeTweets");
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open("GET", "AjaxServlet", false);
	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlhttp.send();
	return xmlhttp.responseText;
}

function showIp() {
	console.log("ajax loaded");
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.open("GET", "AjaxServlet", false);
	xmlhttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	xmlhttp.send();
}

function getLatLngFromString(ll) {
	console.log("Inside latlong:" + ll)
	var latlng = ll.split(';')
		
	for (var i=0;i<latlng.length-1;i++)
		{
		var latlngnew = latlng[i].split(',');
		points.push(new google.maps.LatLng(latlngnew[0], latlngnew[1]));
		
		}
	
	return points;
}