<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
    <style type="text/css">
        TABLE {
            border-collapse: collapse;
            border: 4px solid darkgray;
        }
        TD, TH {
            padding: 5px;
            border: 3px solid darkgray;
            text-align: left;
        }

        tr.red{
            background-color: red;
        }

        tr.green{
            background-color:green;
        }

    </style>
</head>
<body>
<h3><a href="/topjava">Home</a></h3>
<h2>Meals</h2>
<h2>Meal list</h2>
<table>
    <c:forEach items="${mealsList}" var="meal">
        <c:set var="cleanedDateTime" value="${fn:replace(meal.dateTime, 'T', ' ')}" />
        <fmt:parseDate value="${ cleanedDateTime }" pattern="yy-MM-dd HH:mm" var="parsedDateTime"/>

        <tr class=${meal.exceed ? "red":"green"}>
            <td>${parsedDateTime}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>