<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"  %> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Display</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

		<style>
			p {
    			text-align: center;
    			color: black;
			} 
		</style>


  </head>
  
  <body>  
    <p>${pageCnt } results of '${query }' are found: </p>
    <br>
    <c:forEach var = "page" items = "${sessionScope.pageList }">
				<tr>
					<td>TFIDF: <span name="tfidf" >${page.tfidf }</span></td>
					<br>
					<td >url: <a href="${page.url} ">${page.url}</a></td>
					<br>
					<td >urlId: <span name = "urlId">${page.urlId }</span></td>
					<br>								
				</tr>
				<br>
	</c:forEach>
         
         
         
     
         
    <br>
  </body>
</html>
