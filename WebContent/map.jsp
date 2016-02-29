<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Display</title>
<link rel="stylesheet" type="text/css" href="CSS/MapStyle.css">
<script src="JavaScript/ajaxServConn.js" type="text/javascript"></script>
</head>
<body onload="InitProject()">
	<div id="floating-panel">
	<button onclick ="InitProject()">Plot a Tweet</button>
      <button onclick="toggleHeatmap()">Toggle Heatmap</button>
      <button onclick="changeGradient()">Change gradient</button>
      <button onclick="changeRadius()">Change radius</button>
      <button onclick="changeOpacity()">Change opacity</button>
    </div>
    <div id="map"></div>
	<script src="JavaScript/MapPlot.js" type="text/javascript"></script> 
	<script async defer
        src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCmEaFbuyp5Rz5iJ7YMfywzdgGWDncg5zE&signed_in=true&libraries=visualization&callback=initMap">
    </script>
</body>
</html>