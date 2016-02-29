<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tweets mapper</title>
<style>
*{
margin:0;
padding:0;
}
body{
  background:#000;
  height:100%;
  weidth:100%;
}
select{
position:relative;
z-index: 1000;
}
</style>
<script
src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>


</head>
<body>



<form name="auto" method="post" action=mapServlet>
		<input type="hidden" name="tagSelected" value="all">
		
	</form>
	<script>
		document.auto.submit();
	</script> 
</body>
</html>
