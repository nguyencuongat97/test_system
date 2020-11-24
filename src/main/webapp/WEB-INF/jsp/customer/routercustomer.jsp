<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
    <c:when test="${path == '' || path == 'home'}">
        <%@ include file="../component/home.jsp" %>
    </c:when>

    <c:when test="${path == 'status-customer'}">
        <%@ include file="component/customer-station-error.jsp" %>
    </c:when>

    <c:when test="${path == 'dashboard-customer'}">
        <%@ include file="component/customer-station-dashboard.jsp" %>
    </c:when>

    <c:when test="${path == 'detail-customer'}">
        <%@ include file="component/customer-station-detail.jsp" %>
    </c:when>

    <c:when test="${path == 'cpk-customer'}">
        <%@ include file="component/customer-cpk.jsp" %>
    </c:when>

    <c:when test="${path == 'pe-re-tab-ete-customer'}">
        <%@ include file="component/pe-re-tab-ete.jsp" %>
    </c:when>

    <c:when test="${path == 'line-customer'}">
        <%@ include file="component/customer-line.jsp" %>
    </c:when>

    <c:when test="${path == 'total-line-customer'}">
        <%@ include file="component/customer-total-line.jsp" %>
    </c:when>

</c:choose>