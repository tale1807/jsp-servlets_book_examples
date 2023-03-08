<html>
<head>
    <title>
        Displaying the server time
    </title>
</head>
<body>
<h2>Hello World!</h2>
Welcome the server time is now
<%
java.util.Calendar now = java.util.Calendar.getInstance();
int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
int minute = now.get(java.util.Calendar.MINUTE);

if(hour < 10)
    out.println("0" + hour);
else
    out.println(hour);
out.println(":");

if(minute < 10)
    out.println("0" + minute);
    else
        out.println(minute);
%>
</body>
<br>
<A href="/myapp/HelloServlet">HelloServlet</A>
<A href="/myapp/LoginServlet">SessionLoginServlet</A>
<A href="/myapp/Filtered">FilteredServlet</A>
</html>
