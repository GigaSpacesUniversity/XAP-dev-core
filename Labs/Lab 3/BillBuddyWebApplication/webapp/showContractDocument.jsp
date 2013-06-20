<%@page import="com.gigaspaces.document.SpaceDocument"%>
<%@page import="com.c123.billbuddy.dal.BillBuddyDal"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page session="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>Bill Buddy</title>
	<link rel="stylesheet" type="text/css" href="css/reset.css" media="all" />
	<link rel="stylesheet" type="text/css" href="css/layout.css" media="all" />
	<link rel="stylesheet" type="text/css" href="css/styles.css" media="all" />
</head>
<body>
	<%@ include file="/WEB-INF/snippets/header.jsp"%>
<%
	Integer merchantID = new Integer(request.getParameter("mID"));
	int rowIterator = 0;
	SpaceDocument contractDocument = BillBuddyDal.getInstance().getContractDocument(merchantID);
	if(contractDocument != null){
	%>
<div id="wrapper">
	<div id="main">	
		<table>
		<caption>Contract Document Details for Merchant #<%=merchantID %>:</caption>
		<%
		
		for(String key:contractDocument.getProperties().keySet()){
			%>
				<tr class="<%= rowIterator++%2==0 ? "odd" : "even" %>">
					<th><%=key %></th>
					<td><%=contractDocument.getProperty(key) %></td>
				</tr>
			
			<%
		}
		
		%>
		</table>
	<%
	}else{%>
		<h1>NO DOCUEMENT FOUND</h1>
		<h1><%=merchantID %> IS THE MERCHANT ID</h1>
	<%
	}
%>
	</div>
	<div id="nav">
		<ul>
			<li><a href="show.jsp?type=users">Users</a></li>
			<li><a href="show.jsp?type=merchants">Merchants</a></li>
			<li><a href="show.jsp?type=BillBuddy&topType=merchant">Bill Buddy</a></li>
		</ul>
	</div>
</div>
	<%@ include file="/WEB-INF/snippets/footer.jsp" %>

</body>
</html>