<%@ page import="java.util.List"%>
<%@ page import="zhulin.project.dm.dao.DeviceStatus"%>
<%@ page import="zhulin.project.dm.DeviceInfo" %>
<jsp:useBean id="deviceManager" scope="session"
	class="zhulin.project.dm.DeviceManager" />

<html>
<head>
<title>Device Detail</title>
<STYLE>
BODY {
	FONT-SIZE: 14px;
	COLOR: black;
	FONT-FAMILY: Tahoma, Arial, sans-serif;
	BACKGROUND-COLOR: white
}

H1 {
	FONT-SIZE: 22px;
	COLOR: white;
	FONT-FAMILY: Tahoma, Arial, sans-serif;
	BACKGROUND-COLOR: #525d76
}

TD {
	FONT-SIZE: 14px;
	COLOR: black;
	FONT-FAMILY: Tahoma, Arial, sans-serif;
	BACKGROUND-COLOR: white
}

TH {
	FONT-WEIGHT: bold;
	FONT-SIZE: 14px
}

PRE {
	BORDER-RIGHT: #525d76 1px dotted;
	BORDER-TOP: #525d76 1px dotted;
	FONT-SIZE: 14px;
	BORDER-LEFT: #525d76 1px dotted;
	BORDER-BOTTOM: #525d76 1px dotted
}

TT {
	BORDER-RIGHT: #525d76 1px dotted;
	BORDER-TOP: #525d76 1px dotted;
	FONT-SIZE: 14px;
	BORDER-LEFT: #525d76 1px dotted;
	BORDER-BOTTOM: #525d76 1px dotted
}

A {
	COLOR: black
}

A.name {
	COLOR: black
}

INPUT {
	FONT-SIZE: 14px;
	COLOR: black;
	FONT-FAMILY: Tahoma, Arial, sans-serif;
	BACKGROUND-COLOR: white
}
</STYLE>
</head>
<body>
	<a href="dm.jsp">Back</a><p/>
	<%
      String id=request.getParameter("id");
      boolean found=false;
      if(id!=null&&!id.trim().equals("")){
    	 DeviceInfo device=deviceManager.getDeviceInfo(Integer.parseInt(id));
    	 if(device!=null){
    		 String temp=request.getParameter("temperature");
    		 if(temp!=null&&!temp.trim().equals("")){
    			 DeviceStatus status=new DeviceStatus(Integer.parseInt(temp));
    			 deviceManager.addDeviceStatus(device.id, status);
    		 }
    		 
    		 
    		 found=true;
    %>
	<table border="1">
		<tr>
			<th>ID</th>
			<th>Name</th>
			<th>Memory</th>
			<th>Type</th>
			<th>Location</th>
		</tr>
		<tr>
			<td align="left"><%=device.id%></td>
			<td align="left"><%=device.name%></td>
			<td align="left"><%=device.memory%></td>
			<td align="left"><%=device.type%></td>
			<td align="left"><%=device.location%></td>
		</tr>
	</table>
	<p />
	<form action="device.jsp" method="post">
	  <!-- Pass Device ID -->
	  <input type="hidden" name="id" value="<%=device.id %>" />
      <table border="0">
        <tr>
          <td align="left">Send Temperature Signal:</td>
          <td align="left"><input type="text" name="temperature" size="10" /></td>
          <td align="left"><input type="submit" value="Send" /></td>
        </tr>
      </table>
    </form>
    <p />
    <table>
      <tr>
        <th>ID</th>
        <th>Temperature</th>
        <th>Detect Date</th>
      </tr>
      <%
         List<DeviceStatus> statuses=deviceManager.getDeviceStatus(device.id);
         for(DeviceStatus status:statuses){
      %>
      <tr>
        <td><%=status.getId() %></td>
        <td><%=status.getTemperature() %></td>
        <td><%=status.getCreatedDate() %></td>
      </tr>
      
      <% } %>
    </table>
	<%
    	}
      }
    %>
	<%if(!found){ %>
	<p />
	<b>Plese input a correct device id in order to check device detail!</b>
	<%} %>
</body>
</html>