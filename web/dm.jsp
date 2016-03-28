<%@ page import="java.util.List"%>
<%@ page import="zhulin.project.DeviceInfo"%>
<%@ page import="zhulin.project.Location"%>
<%@ page import="zhulin.project.Device"%>
<jsp:useBean id="deviceManager" scope="session" class="zhulin.project.DeviceManager" />

<html>
  <head>
    <title>Device Manager</title>
    <STYLE>BODY {
	FONT-SIZE: 14px; COLOR: black; FONT-FAMILY: Tahoma,Arial,sans-serif; BACKGROUND-COLOR: white
}
H1 {
	FONT-SIZE: 22px; COLOR: white; FONT-FAMILY: Tahoma,Arial,sans-serif; BACKGROUND-COLOR: #525d76
}
TD {
	FONT-SIZE: 14px; COLOR: black; FONT-FAMILY: Tahoma,Arial,sans-serif; BACKGROUND-COLOR: white
}
TH {
	FONT-WEIGHT: bold; FONT-SIZE: 14px
}
PRE {
	BORDER-RIGHT: #525d76 1px dotted; BORDER-TOP: #525d76 1px dotted; FONT-SIZE: 14px; BORDER-LEFT: #525d76 1px dotted; BORDER-BOTTOM: #525d76 1px dotted
}
TT {
	BORDER-RIGHT: #525d76 1px dotted; BORDER-TOP: #525d76 1px dotted; FONT-SIZE: 14px; BORDER-LEFT: #525d76 1px dotted; BORDER-BOTTOM: #525d76 1px dotted
}
A {
	COLOR: black
}
A.name {
	COLOR: black
}

INPUT{
	FONT-SIZE: 14px; COLOR: black; FONT-FAMILY: Tahoma,Arial,sans-serif; BACKGROUND-COLOR: white
}
</STYLE>
  </head>
  <body>
    <h1>Welcome to device manager!</h1>
    <%
      String name=request.getParameter("name");
      if(name!=null&&!name.trim().equals("")){
         int type=Integer.parseInt(request.getParameter("type"));
         int memory=Integer.parseInt(request.getParameter("memory"));
         int x=Integer.parseInt(request.getParameter("locationX"));
         int y=Integer.parseInt(request.getParameter("locationY"));
         deviceManager.addDevice(new DeviceInfo(name,memory,
              type==1?Device.DeviceType.INFLAMER:Device.DeviceType.BOILER,new Location(x,y)));
      }
    %>
    <form action="dm.jsp" method="post">
      <table border="0">
        <tr>
          <td align="left">Name:</td>
          <td align="left"><input type="text" name="name" size="10" /></td>
        </tr>
        <tr>
          <td align="left">Type:</td>
          <td align="left"><select name="type"><option value="0">Boiler</option><option value="1">Inflamer</option></select></td>
        </tr>
        <tr>
          <td align="left">Memory:</td>
          <td align="left"><input type="text" name="memory" size="10" /></td>
        </tr>
        <tr>
          <td align="left">X:<input type="text" name="locationX" size="5" /></td>
          <td align="left">Y:<input type="text" name="locationY" size="5" /></td>
        </tr>
        <tr>
          <td align="left"><input type="submit" value="Register" /></td>
        </tr>
      </table>
    </form>
    <p />
    <table border="1">
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Memory</th>
        <th>Type</th>
        <th>Location</th>
      </tr>
      <%
         List<DeviceInfo> devices=deviceManager.getDevicesXML();
         for(DeviceInfo device:devices){
      %>  
      <tr>
        <td align="left"><%=device.id%></td>
        <td align="left"><%=device.name%></td>
        <td align="left"><%=device.memory%></td>
        <td align="left"><%=device.type%></td>
        <td align="left"><%=device.location%></td>
      </tr>
      <%
         }
      %>
    </table>
  </body>
</html>