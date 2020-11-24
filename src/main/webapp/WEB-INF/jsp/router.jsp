<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
    <c:when test="${path == '' || path == 'home'}">
        <%@ include file="component/home.jsp" %>
    </c:when>

    <c:when test="${path == 'about'}">
        <%@ include file="component/about.jsp" %>
    </c:when>

    <c:when test="${path == '404-error-page'}">
        <%@ include file="component/404-error-page.jsp" %>
    </c:when>

    <c:when test="${path == '401-error-page'}">
        <%@ include file="component/401-error-page.jsp" %>
    </c:when>

    <c:when test="${path == 'station-status'}">
        <%@ include file="component/test/station-error.jsp" %>
    </c:when>

    <c:when test="${path == 'station-dashboard'}">
        <%@ include file="component/test/station-dashboard.jsp" %>
    </c:when>

    <c:when test="${path == 'station-detail'}">
        <%@ include file="component/test/station-detail.jsp" %>
    </c:when>

    <c:when test="${path == 'add-station-detail'}">
        <%@ include file="component/test/add-station-detail.jsp" %>
    </c:when>

    <c:when test="${path == 'ete-detail'}">
        <%@ include file="component/test/ete-detail.jsp" %>
    </c:when>

    <c:when test="${path == 'ete-test'}">
        <%@ include file="component/test/ete-test.jsp" %>
    </c:when>

    <c:when test="${path == 'rtr-detail'}">
        <%@ include file="component/test/retest-rate-detail.jsp" %>
    </c:when>
    <c:when test="${path == 'report-rtr-detail'}">
        <%@ include file="component/test/report-rtrdetail.jsp" %>
    </c:when>

    <c:when test="${path == 'station-cpk'}">
        <%@ include file="component/test/cpk.jsp" %>
    </c:when>

    <c:when test="${path == 'cpk-tool'}">
        <%@ include file="component/test/cpk-tool.jsp" %>
    </c:when>

    <c:when test="${path == 'copy-ic-data'}">
        <%@ include file="component/test/copy_ic_data.jsp" %>
    </c:when>

    <c:when test="${path == 'total-line-dashboard'}">
        <%@ include file="component/test/total-line-dashboard.jsp" %>
    </c:when>

    <c:when test="${path == 'line-dashboard'}">
        <%@ include file="component/test/line-dashboard.jsp" %>
    </c:when>

    <c:when test="${path == 'resource'}">
        <%@ include file="component/test/resource.jsp" %>
    </c:when>

    <c:when test="${path == 'total-model-oba'}">
        <%@ include file="component/test/total-model-oba.jsp" %>
    </c:when>

    <c:when test="${path == 'every-model-test-oba'}">
        <%@ include file="component/test/every-model-test-status.jsp" %>
    </c:when>

    <c:when test="${path == 'engineer-management'}">
        <%@ include file="component/test/engineer-management.jsp" %>
    </c:when>

    <c:when test="${path == 'engineer-top-3'}">
        <%@ include file="component/test/engineer-top-3.jsp" %>
    </c:when>

    <c:when test="${path == 'engineer-work-shift'}">
        <%@ include file="component/test/engineer-work-shift.jsp" %>
    </c:when>

    <c:when test="${path == 'manager-model'}">
        <%@ include file="component/test/manager-model.jsp" %>
    </c:when>

    <c:when test="${path == 'manager-group'}">
        <%@ include file="component/test/manager-group.jsp" %>
    </c:when>

    <c:when test="${path == 'manager-model-smt'}">
        <%@ include file="component/test/manager-model-smt.jsp" %>
    </c:when>

    <c:when test="${path == 'overview'}">
        <%@ include file="component/test/overview.jsp" %>
    </c:when>
    <c:when test="${path == 'test-status'}">
        <%@ include file="component/test/test-status.jsp" %>
    </c:when>
    <c:when test="${path == 'task-management'}">
        <%@ include file="component/test/task-management.jsp" %>
    </c:when>
    <c:when test="${path == 'task-management-confirm'}">
        <%@ include file="component/test/task-management-confirm.jsp" %>
    </c:when>

    <c:when test="${path == 'maintain'}">
        <%@ include file="component/test/maintain.jsp" %>
    </c:when>
    <c:when test="${path == 'uph-tracking'}">
        <%@ include file="component/test/uph-tracking.jsp" %>
    </c:when>
    <c:when test="${path == 'uph-tracking-all'}">
        <%@ include file="component/test/uph-tracking-all.jsp" %>
    </c:when>
    <c:when test="${path == 'cycletime'}">
        <%@ include file="component/test/cycletime.jsp" %>
    </c:when>
    <c:when test="${path == 'abnormal-dashboard'}">
        <%@ include file="component/test/abnormal-dashboard.jsp" %>
    </c:when>
    <c:when test="${path == 'production-plan'}">
        <%@ include file="component/test/production-plan.jsp" %>
    </c:when>
    <c:when test="${path == 'uph-tracking-dashboard'}">
        <%@ include file="component/test/uph-tracking-dashboard.jsp" %>
    </c:when>
    <c:when test="${path == 'wip-group'}">
        <%@ include file="component/test/wip-group.jsp" %>
    </c:when>
    <c:when test="${path == 'wip-output-group'}">
        <%@ include file="component/test/wip-output-group.jsp" %>
    </c:when>
    <c:when test="${path == 'performance-and-top-issue'}">
        <%@ include file="component/test/performance-and-top-issue.jsp" %>
    </c:when>
    <c:when test="${path == 'log-reader-config'}">
        <%@ include file="component/test/log-reader-config.jsp" %>
    </c:when>
    <c:when test="${path == 'log-reader'}">
        <%@ include file="component/test/log-reader.jsp" %>
    </c:when>




    <c:when test="${path == 'assembly-line'}">
        <%@ include file="component/nbb/assembly-line.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-output'}">
        <%@ include file="component/nbb/nbb-output.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-output-line'}">
        <%@ include file="component/nbb/nbb-output-line.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-wip'}">
        <%@ include file="component/nbb/nbb-wip.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-uph-by-line'}">
        <%@ include file="component/nbb/nbb-uph-by-line.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-uph-by-line-packing'}">
        <%@ include file="component/nbb/nbb-uph-by-line-packing.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-yield-overall'}">
        <%@ include file="component/nbb/nbb-yield-overall.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-error-overall'}">
        <%@ include file="component/nbb/nbb-error-overall.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-kitting'}">
        <%@ include file="component/nbb/nbb-material.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-compare-data-sfis'}">
        <%@ include file="component/nbb/nbb-compare-data-sfis.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-missing-data-sfis'}">
        <%@ include file="component/nbb/nbb-missing-data-sfis.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-overall-smt'}">
        <%@ include file="component/nbb/nbb-overall-smt.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-manage-dt'}">
        <%@ include file="component/nbb/nbb-manage.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-vi-dt'}">
        <%@ include file="component/nbb/nbb-vi.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-overview'}">
        <%@ include file="component/nbb/overview-nbb.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-temperature-and-humidity'}">
        <%@ include file="component/nbb/nbb-temperature-and-humidity.jsp" %>
    </c:when>
    <c:when test="${path == 'nbb-mo-history'}">
        <%@ include file="component/nbb/nbb-mo-history.jsp" %>
    </c:when>
    <c:when test="${path == 'input-output'}">
        <%@ include file="component/nbb/input-output.jsp" %>
    </c:when>



    <c:when test="${path == 'icivet'}">
        <%@ include file="component/test/mobile/icivet-tasks.jsp" %>
    </c:when>

    <c:when test="${path == 'icivet-my-tasks'}">
        <%@ include file="component/test/mobile/icivet-my-tasks.jsp" %>
    </c:when>

    <c:when test="${path == 'icivet-task'}">
        <%@ include file="component/test/mobile/icivet-task.jsp" %>
    </c:when>

    <c:when test="${path == 'icivet-task-handle'}">
        <%@ include file="component/test/mobile/icivet-task-handle.jsp" %>
    </c:when>



    <c:when test="${path == 're-home'}">
        <%@ include file="component/re/home.jsp" %>
    </c:when>

    <c:when test="${path == 're-stock-management'}">
        <%@ include file="component/re/stock-management.jsp" %>
    </c:when>

    <c:when test="${path == 're-stock-bc8m'}">
        <%@ include file="component/re/bc8m.jsp" %>
    </c:when>

    <c:when test="${path == 're-stock-rma'}">
        <%@ include file="component/re/rma.jsp" %>
    </c:when>

    <c:when test="${path == 're-stock-online'}">
        <%@ include file="component/re/online.jsp" %>
    </c:when>

    <c:when test="${path == 're-bonepile'}">
        <%@ include file="component/re/bonepile.jsp" %>
    </c:when>

    <c:when test="${path == 're-checkin'}">
        <%@ include file="component/re/checkin.jsp" %>
    </c:when>

    <c:when test="${path == 're-checkout'}">
        <%@ include file="component/re/checkout.jsp" %>
    </c:when>

    <c:when test="${path == 're-output'}">
        <%@ include file="component/re/output.jsp" %>
    </c:when>

    <c:when test="${path == 're-capacity'}">
        <%@ include file="component/re/capacity.jsp" %>
    </c:when>

    <c:when test="${path == 're-checklist'}">
        <%@ include file="component/re/checklist.jsp" %>
    </c:when>

    <c:when test="${path == 're-standard'}">
        <%@ include file="component/re/standard.jsp" %>
    </c:when>

    <c:when test="${path == 're-daily'}">
        <%@ include file="component/re/dailychecklist.jsp" %>
    </c:when>

    <c:when test="${path == 're-ownerdefine'}">
        <%@ include file="component/re/ownerdefine.jsp" %>
    </c:when>

    <c:when test="${path == 're-result'}">
        <%@ include file="component/re/resultchecklist.jsp" %>
    </c:when>

    <c:when test="${path == 're-leaderconfirm'}">
        <%@ include file="component/re/dailyleaderconfirm.jsp" %>
    </c:when>

    <c:when test="${path == 're-auditor'}">
        <%@ include file="component/re/auditteam.jsp" %>
    </c:when>

    <c:when test="${path == 're-ownerdefinearea'}">
        <%@ include file="component/re/ownerdefinearea.jsp" %>
    </c:when>

    <c:when test="${path == 're-c03-in-out'}">
        <%@ include file="component/re/c03-in-out.jsp" %>
    </c:when>

    <c:when test="${path == 'machines-stt'}">
        <%@ include file="component/re/machines-stt.jsp" %>
    </c:when>

    <c:when test="${path == 'line-stt'}">
        <%@ include file="component/re/line-stt.jsp" %>
    </c:when>

    <c:when test="${path == 'line-detail'}">
        <%@ include file="component/re/line-detail.jsp" %>
    </c:when>

    <c:when test="${path == 're-online-wip'}">
        <%@ include file="component/re/onlinewip.jsp" %>
    </c:when>

    <c:when test="${path == 're-bonepile-detail'}">
        <%@ include file="component/re/re-bonepiledetail.jsp" %>
    </c:when>

    <c:when test="${path == 're-checkin-out'}">
        <%@ include file="component/re/checkin-out.jsp" %>
    </c:when>

    <c:when test="${path == 'nbb-test-equipment-used-count'}">
        <%@ include file="component/nbb/nbb-test-equipment-used-count.jsp" %>
    </c:when>

    <c:when test="${path == 'nbb-vlrr'}">
        <%@ include file="component/nbb/nbb-vlrr.jsp" %>
    </c:when>

    <c:when test="${path == 'qrscan'}">
        <%@ include file="component/test/qrscan.jsp" %>
    </c:when>

    <c:when test="${path == 'ef-overall'}">
        <%@ include file="component/test/ef-management/overall.jsp" %>
    </c:when>



    <c:when test="${path == 'hr-index'}">
        <%@ include file="component/hr/hr-index.jsp" %>
    </c:when>
    <c:when test="${path == 'hr-workCount'}">
        <%@ include file="component/hr/hr-workCount.jsp" %>
    </c:when>
    <c:when test="${path == 'foreign-work-count'}">
        <%@ include file="component/hr/foreign-work-count.jsp" %>
    </c:when>

    <c:when test="${path == 'line-balance'}">
        <%@ include file="component/smt/line-balance.jsp" %>
    </c:when>

    <c:when test="${path == 'line-man'}">
        <%@ include file="component/smt/line-man-calculation.jsp" %>
    </c:when>

    <c:when test="${path == 'calc-line-balance'}">
        <%@ include file="component/smt/calc-line-balance.jsp" %>
    </c:when>



    <c:when test="${path == 'pe-daily-report'}">
        <%@ include file="component/test/pe-daily-report.jsp" %>
    </c:when>
    <c:when test="${path == 'te-report'}">
        <%@ include file="component/test/te-report.jsp" %>
    </c:when>

    <c:when test="${path == 'error-analysis'}">
        <%@ include file="component/test/error-analysis.jsp" %>
    </c:when>

    <c:when test="${path == 'cpk-config'}">
        <%@ include file="component/test/cpk-config.jsp" %>
    </c:when>

</c:choose>