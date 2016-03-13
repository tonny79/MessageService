<jsp:useBean id="messageManager" scope="session" class="zhulin.project.MessageManager" />
<html>
  <head>
    <title>Message Service</title>
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
    <h1>Welcome to message service!</h1>
    <%
      String message=request.getParameter("message");
      if(message!=null&&!message.trim().equals("")){
         messageManager.sendMessage(message);
      }
    %>
    <form action="messagemanager.jsp" method="post">
      <table border="0">
        <tr>
          <td align="left">Message:</td>
          <td align="left"><input type="text" name="message" size="100" /></td>
          <td align="left"><input type="submit" value="Send!" /></td>
        </tr>
      </table>
    </form>
    <%
      String[] messages=messageManager.getMessages();
      for(int i=0;i<messages.length;i++){
    %>
          <%=messages[i]%><br />
    <%
      }
    %>
  </body>
</html>