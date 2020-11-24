package com.foxconn.fii.receiver.test.controller;

import com.foxconn.fii.data.primary.model.entity.TestResource;
import com.foxconn.fii.receiver.test.service.TestResourceService;
import com.foxconn.fii.security.model.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class TestMvcController {

    @Value("${server.domain}")
    private String domain;

    @Autowired
    private TestResourceService testResourceService;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("path", "home");
        model.addAttribute("title", "Smart Factory Management");
        return commonView(model, "application-me", null);
    }

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("path", "home");
        model.addAttribute("title", "Smart Factory Management");
        return commonView(model, "application-me", null);
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("path", "about");
        model.addAttribute("title", "About Smart Factory Management");
        return commonView(model, "application-me", null);
    }

    @RequestMapping("/404-error-page")
    public String Error404Page(Model model) {
        model.addAttribute("path", "404-error-page");
        return commonView(model, "application", null);
    }

    @RequestMapping("/401-error-page")
    public String Error401Page(Model model) {
        model.addAttribute("path", "401-error-page");
        return commonView(model, "application", null);
    }

    @RequestMapping("/station-status")
    public String stationStatus(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Tester Status Management");
        model.addAttribute("path", "station-status");

        return commonView(model, "application", null);
    }

    @RequestMapping("/station-dashboard")
    public String stationDashboard(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Tester Dashboard");
        model.addAttribute("path", "station-dashboard");

        return commonView(model, "application", null);
    }

    @RequestMapping("/station-detail")
    public String stationDetail(Model model, String factory, String modelName, String groupName, String stationName, String timeSpan) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("timeSpan", timeSpan);
        model.addAttribute("title", "Tester Reason Analysis");
        model.addAttribute("path", "station-detail");

        return commonView(model, "application", null);
    }

    @RequestMapping("/add-station-detail")
    public String addStationDetail(Model model, String factory, String modelName, String groupName, String stationName, String timeSpan) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("timeSpan", timeSpan);
        model.addAttribute("title", "Tester Group Detail Management");
        model.addAttribute("path", "add-station-detail");

        return commonView(model, "application", null);
    }
    @RequestMapping("/ete-detail")
    public String eteDetail(Model model, String factory, String modelName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("title", "Tester E.T.E Detail Management");
        model.addAttribute("path", "ete-detail");

        return commonView(model, "application", null);
    }

    @RequestMapping("/ete-test")
    public String eteTest(Model model, String factory, String modelName, String groupName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("title", "Tester E.T.E Test Management");
        model.addAttribute("path", "ete-test");

        return commonView(model, "application-ete", null);
    }

    @RequestMapping("/rtr-detail")
    public String rtrDetail(Model model, String factory, String modelName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("title", "Tester Retest Rate Detail Management");
        model.addAttribute("path", "rtr-detail");

        return commonView(model, "application", null);
    }
    @RequestMapping("/report-rtr-detail")
    public String reportrtrDetail(Model model, String factory, String modelName, String groupName, String timeSpan) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
         model.addAttribute("timeSpan", timeSpan);
        model.addAttribute("title", "Report Retest Rate Detail");
        model.addAttribute("path", "report-rtr-detail");

        return commonView(model, "application", null);
    }

    @RequestMapping("/station-cpk")
    public String stationCpk(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Tester CPK Analysis");
        model.addAttribute("path", "station-cpk");

        return commonView(model, "application", null);
    }

    @RequestMapping("/cpk-tool")
    public String cpkTool(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "CPK Tool");
        model.addAttribute("path", "cpk-tool");

        return commonView(model, "application", null);
    }

     @RequestMapping("/copy-ic-data")
    public String copyIcData(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Copy IC Data");
        model.addAttribute("path", "copy-ic-data");

        return commonView(model, "application", null);
    }

    @RequestMapping("/maintain")
    public String maintainPage(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Maintain");

        model.addAttribute("path", "maintain");

        return commonView(model, "application", null);
    }

    @Deprecated
    @RequestMapping("/troubleshooting-guide")
    public String troubleShooting(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("path", "troubleshooting-guide");

        return commonView(model, "application", null);
    }

    @RequestMapping("/overview")
    public String overview(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Overview Factory");
        model.addAttribute("path", "overview");

        return commonView(model, "application", null);
    }

    @RequestMapping("/resource")
    public String resource(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Engineer Information");
        model.addAttribute("path", "resource");

        return commonView(model, "application", null);
    }

    @RequestMapping("/engineer-management")
    public String engineerManagement(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Engineer Management");
        model.addAttribute("path", "engineer-management");

        return commonView(model, "application", null);
    }

    @RequestMapping("/engineer-top-3")
    public String engineerTop3(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Engineer Top 3");
        model.addAttribute("path", "engineer-top-3");

        return commonView(model, "application", null);
    }
    @RequestMapping("/engineer-work-shift")
    public String engineerWorkShift(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Engineer Work Shift Handle");
        model.addAttribute("path", "engineer-work-shift");

        return commonView(model, "application", null);
    }

    @RequestMapping("/cycletime")
    public String cycleTime(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Cycle Time");
        model.addAttribute("path", "cycletime");

        return commonView(model, "application", null);
    }

    @RequestMapping("/error-analysis")
    public String errorAnalysis(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Error Analysis");
        model.addAttribute("path", "error-analysis");

        return commonView(model, "application", null);
    }

    @RequestMapping("/cpk-config")
    public String cpkConfig(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "CPK Config");
        model.addAttribute("path", "cpk-config");

        return commonView(model, "application", null);
    }

    @RequestMapping("/abnormal-dashboard")
    public String abnormalDashboard(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Abnormal Dashboard");
        model.addAttribute("path", "abnormal-dashboard");

        return commonView(model, "application", null);
    }

    @RequestMapping("/production-plan")
    public String productionPlan(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Production Plan");
        model.addAttribute("path", "production-plan");

        return commonView(model, "application", null);
    }

    @RequestMapping("/total-model-oba")
    public String totalModelOba(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Total Model OBA Tester Status");
        model.addAttribute("path", "total-model-oba");

        return commonView(model, "application", null);
    }

     @RequestMapping("/every-model-test-oba")
    public String everyModelTestOba(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "All Model OBA Tester Status");
        model.addAttribute("path", "every-model-test-oba");

        return commonView(model, "application", null);
    }

    @RequestMapping("/resource-status")
    public String resourceStatus(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("path", "resource-status");

        return commonView(model, "application", null);
    }

    @RequestMapping("/uph-tracking")
    public String uphTracking(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "UPH Tracking Dashboard");
        model.addAttribute("path", "uph-tracking");

        return commonView(model, "application", null);
    }

    @RequestMapping("/uph-tracking-all")
    public String uphTrackingAll(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "UPH Tracking By All Model");
        model.addAttribute("path", "uph-tracking-all");

        return commonView(model, "application", null);
    }

    @RequestMapping("/manager-model")
    public String managerModel(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Manager Model");
        model.addAttribute("path", "manager-model");

        return commonView(model, "application", null);
    }

     @RequestMapping("/manager-group")
    public String managerGroup(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Manager Group");
        model.addAttribute("path", "manager-group");

        return commonView(model, "application", null);
    }

    @RequestMapping("/manager-model-smt")
    public String managerModelSMT(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Manager Model SMT");
        model.addAttribute("path", "manager-model-smt");

        return commonView(model, "application", null);
    }

    @RequestMapping("/test-status")
    public String testStatus(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("path", "test-status");

        return commonView(model, "application", null);
    }
    @RequestMapping("/task-management")
    public String taskManagement(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Task Management");
        model.addAttribute("path", "task-management");

        return commonView(model, "application", null);
    }
    @RequestMapping("/task-management-confirm")
    public String taskManagementConfirm(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Task Management Confirm");
        model.addAttribute("path", "task-management-confirm");

        return commonView(model, "application", 4);
    }
    @RequestMapping("/uph-tracking-dashboard")
    public String uphTrackingDashboard(Model model, String factory) {
        factory = factory.toLowerCase();
        model.addAttribute("factory", factory);
        model.addAttribute("path", "uph-tracking-dashboard");
        model.addAttribute("title", "UPH Tracking Dashboard");
        return commonView(model, "application", null);
    }
    @RequestMapping("/wip-group")
    public String wipGroup(Model model, String factory) {
        factory = factory.toLowerCase();
        model.addAttribute("factory", factory);
        model.addAttribute("path", "wip-group");
        model.addAttribute("title", "WIP Group");
        return commonView(model, "application", null);
    }
    @RequestMapping("/wip-output-group")
    public String wipOutputGroup(Model model, String factory) {
        factory = factory.toLowerCase();
        model.addAttribute("factory", factory);
        model.addAttribute("path", "wip-output-group");
        model.addAttribute("title", "WIP Output Group");
        return commonView(model, "application", null);
    }
    @RequestMapping("/performance-and-top-issue")
    public String performanceTopIssue(Model model, String factory) {
        factory = factory.toLowerCase();
        model.addAttribute("factory", factory);
        model.addAttribute("path", "performance-and-top-issue");
        model.addAttribute("title", "Performance And Top Issue");
        return commonView(model, "application", null);
    }


    // NBB (Khang)
    @RequestMapping("/assembly-line")
    public String assemblyLine(Model model) {
        model.addAttribute("path", "assembly-line");
        model.addAttribute("factory", "nbb");
        return commonView(model, "application", null);
    }
    @RequestMapping("/nbb-output")
    public String nbbOutput(Model model) {
        model.addAttribute("path", "nbb-output");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Input & Output");
        return commonView(model, "application", null);
    }
    @RequestMapping("/nbb-output-line")
    public String nbbOutputLine(Model model) {
        model.addAttribute("path", "nbb-output-line");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Output By Line");
        return commonView(model, "application", null);
    }

    @RequestMapping("/nbb-wip")
    public String nbbWip(Model model) {
        model.addAttribute("path", "nbb-wip");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB WIP");
        return commonView(model, "application", null);
    }

    @RequestMapping("/nbb-uph-by-line")
    public String nbbUPHLine(Model model) {
        model.addAttribute("path", "nbb-uph-by-line");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB UPH By Line");
        return commonView(model, "application", null);
    }

    @RequestMapping("/nbb-uph-by-line-packing")
    public String nbbUPHLinePacking(Model model) {
        model.addAttribute("path", "nbb-uph-by-line-packing");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB UPH By Line Packing");
        return commonView(model, "application", null);
    }

    @RequestMapping("/nbb-yield-overall")
    public String nbbYieldOverall(Model model) {
        model.addAttribute("path", "nbb-yield-overall");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Yield Overall");
        return commonView(model, "application", null);
    }
    @RequestMapping("/nbb-error-overall")
    public String nbbPareto(Model model) {
        model.addAttribute("path", "nbb-error-overall");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Error Overall");
        return commonView(model, "application", null);
    }
    @RequestMapping("/nbb-kitting")
    public String nbbKitting(Model model) {
        model.addAttribute("path", "nbb-kitting");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Kitting");
        return commonView(model, "application", null);
    }
    @RequestMapping("/nbb-compare-data-sfis")
    public String nbbCompareDataSFIS(Model model) {
        model.addAttribute("path", "nbb-compare-data-sfis");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Compare Data SFIS");
        return commonView(model, "application", null);
    }
    @RequestMapping("/nbb-missing-data-sfis")
    public String nbbLackDataSFIS(Model model) {
        model.addAttribute("path", "nbb-missing-data-sfis");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Missing Data SFIS");
        return commonView(model, "application", null);
    }
    @RequestMapping("/nbb-overall-smt")
    public String nbbOverqallSMT(Model model) {
        model.addAttribute("path", "nbb-overall-smt");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Yield Over SMT");
        return commonView(model, "application", null);
    }
    @RequestMapping("/managenbb")
    public String nbbManagenbb(Model model) {
        model.addAttribute("path", "nbb-manage-dt");
        model.addAttribute("title", "NBB Screw Missing Inspection");
        return commonView(model, "application", null);
    }
    @RequestMapping("/vi")
    public String nbbVinbb(Model model) {
        model.addAttribute("path", "nbb-vi-dt");
        model.addAttribute("title", "Smart Factory Management");
        return commonView(model, "application", null);
    }

    @RequestMapping("/nbb-overview")
    public String nbbOverview(Model model) {
        model.addAttribute("path", "nbb-overview");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB Overview");
        return commonView(model, "application", null);
    }

    @RequestMapping("/nbb-vlrr")
    public String nbbVlrr(Model model) {
        model.addAttribute("path", "nbb-vlrr");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB VLRR");
        return commonView(model, "application", null);
    }

    @RequestMapping("/temperature-and-humidity")
    public String nbbTempAndHum(Model model) {
        model.addAttribute("path", "nbb-temperature-and-humidity");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "BN3 Temperature And Humidity");
        return commonView(model, "application", null);
    }

    @RequestMapping("/nbb-mo-history")
    public String nbbMOHistory(Model model) {
        model.addAttribute("path", "nbb-mo-history");
        model.addAttribute("factory", "nbb");
        model.addAttribute("title", "NBB WIP Group");
        return commonView(model, "application", null);
    }
    @RequestMapping("/input-output")
    public String inOutput(Model model, String factory) {
        factory = factory.toLowerCase();
        model.addAttribute("factory", factory);
        model.addAttribute("path", "input-output");
        model.addAttribute("title", "Input & Output");
        return commonView(model, "application", null);
    }


    @RequestMapping("/icivet")
    public String icivet(Model model, String factory) {
        model.addAttribute("factory", factory);

        model.addAttribute("domain", domain);
        model.addAttribute("path", "icivet");
        return commonView(model, "application", null);
    }

    @RequestMapping("/icivet-my-tasks")
    public String icivetMyTasks(Model model, String factory) {
        model.addAttribute("factory", factory);

        model.addAttribute("domain", domain);
        model.addAttribute("path", "icivet-my-tasks");
        return commonView(model, "application", null);
    }

    @RequestMapping("/icivet/task/handle")
    public String icivetHandleTask(Model model, Long trackingId) {
        model.addAttribute("trackingId", trackingId);

        model.addAttribute("domain", domain);
        model.addAttribute("path", "icivet-task-handle");
        return commonView(model, "application", null);
    }

    @RequestMapping("/icivet/task")
    public String icivetTask(Model model, Long trackingId) {
        model.addAttribute("trackingId", trackingId);

        model.addAttribute("domain", domain);
        model.addAttribute("path", "icivet-task");
        return commonView(model, "application", null);
    }

    //QUANG CHAU
    @RequestMapping("/qc-uph-tracking")
    public String qcuphTracking(Model model, String factory) {
        factory = factory.toLowerCase();
        model.addAttribute("factory", factory);
        model.addAttribute("path", "qc-uph-tracking");
        model.addAttribute("title", "S03 UPH Tracking");
        return commonView(model, "application", null);
    }

    // COUNT NBB TEST equipment (Giang)
    @RequestMapping("/nbb-test-equipment-used-count")
    public String nbbTestEquipmentUsedCount(Model model) {
        model.addAttribute("domain", domain);
        model.addAttribute("path", "nbb-test-equipment-used-count");

        return commonView(model, "application", null);
    }

    @RequestMapping("/customer/home")
    public String homecustomer(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Smart Factory Management");
       
        model.addAttribute("path", "home");

        return commonView(model, "customer/applicationcustomer", null);
    }

     @RequestMapping("/customer/customer-status")
    public String stationStatuscustomer(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Tester Status Management");
       
        model.addAttribute("path", "status-customer");

         return commonView(model, "customer/applicationcustomer", null);
    }

    @RequestMapping("/customer/customer-dashboard")
    public String stationDashboardcustomer(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Tester Station Dashboard");

        model.addAttribute("path", "dashboard-customer");

        return commonView(model, "customer/applicationcustomer", null);
    }

    @RequestMapping("/customer/customer-detail")
    public String stationDetailcustomer(Model model, String factory, String modelName, String groupName, String stationName, String timeSpan) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("timeSpan", timeSpan);
        model.addAttribute("title", "Tester Station Detail");

        model.addAttribute("path", "detail-customer");

        return commonView(model, "customer/applicationcustomer", null);
    }

    @RequestMapping("/customer/customer-cpk")
    public String stationCpkcustomer(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title", "Tester CPK Analysis");

        model.addAttribute("path", "cpk-customer");

        return commonView(model, "customer/applicationcustomer", null);
    }

    @RequestMapping("/qrscan")
    public String qrscan(Model model) {
        model.addAttribute("domain", domain);
        model.addAttribute("path", "qrscan");
        return commonView(model, "application", null);
    }

    @RequestMapping("/customer/customer-pe-re-tab")
    public String peReTabcustomer(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title","ETE & TOP 3 REPAIR ISSUE");

        model.addAttribute("path", "pe-re-tab-ete-customer");

        return commonView(model, "customer/applicationcustomer", null);
    }

    @RequestMapping("/customer/customer-line")
    public String lineTabcustomer(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title","Line Dashboard");

        model.addAttribute("path", "line-customer");

        return commonView(model, "customer/applicationcustomer", null);
    }

    @RequestMapping("/total-line-dashboard")
    public String totalLineDashboard(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title","Total Line Dashboard");
        model.addAttribute("path", "total-line-dashboard");

        return commonView(model, "application", null);
    }

    @RequestMapping("/line-dashboard")
    public String lineDashboard(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title","Line Dashboard");
        model.addAttribute("path", "line-dashboard");

        return commonView(model, "application", null);
    }

    @RequestMapping("/customer/customer-total-line")
    public String totalLineTabcustomer(Model model, String factory, String modelName, String groupName, String stationName) {
        model.addAttribute("factory", factory);
        model.addAttribute("modelName", modelName);
        model.addAttribute("groupName", groupName);
        model.addAttribute("stationName", stationName);
        model.addAttribute("title","Total Line Dashboard");

        model.addAttribute("path", "total-line-customer");

        return commonView(model, "customer/applicationcustomer", null);
    }

    @RequestMapping("/line-balance")
    public String lineBalance(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title","SMT Line Balance");
        model.addAttribute("path", "line-balance");

        return commonView(model, "application", null);
    }

    @RequestMapping("/line-man")
    public String lineMan(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title","SMT Line & Man calculations");
        model.addAttribute("path", "line-man");

        return commonView(model, "application", null);
    }

    @RequestMapping("/calc-line-balance")
    public String calcLineBalance(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title","SMT Line Balance Calculate");
        model.addAttribute("path", "calc-line-balance");

        return commonView(model, "application", null);
    }


    @RequestMapping("/pe-daily-report")
    public String teReport(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "PE Daily Report");
        model.addAttribute("path", "pe-daily-report");

        return commonView(model, "application", null);
    }

    @RequestMapping("/te-report")
    public String peDailyReport(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "TE Report");
        model.addAttribute("path", "te-report");

        return commonView(model, "application", null);
    }

    @RequestMapping("/log-reader-config")
    public String logReaderConfig(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Log Reader Config");
        model.addAttribute("path", "log-reader-config");

        return commonView(model, "application", null);
    }

    @RequestMapping("/log-reader-view")
    public String logReader(Model model, String factory) {
        model.addAttribute("factory", factory);
        model.addAttribute("title", "Log File Reader");
        model.addAttribute("path", "log-reader");

        return commonView(model, "application", null);
    }


    private String commonView(Model model, String view, Integer level) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String employee = "";
        if (principal instanceof UserContext) {
            employee = ((UserContext) principal).getUsername();
        } else if (principal instanceof String && !"anonymousUser".equals(principal)) {
            employee = (String) principal;
        }
        int groupLevel = -1;
        if (!StringUtils.isEmpty(employee)) {
            TestResource resource = testResourceService.findByEmployeeNo(employee);
            if (resource != null) {
                model.addAttribute("employeeId", resource.getEmployeeNo());
                model.addAttribute("employeeName", resource.getName());
                model.addAttribute("employeeDem", resource.getDem());
                model.addAttribute("employeeShift", resource.getShift());
                model.addAttribute("employeeLevel", resource.getGroupLevel());
                groupLevel = resource.getGroupLevel();
            } else {
                model.addAttribute("employeeId", employee);
                model.addAttribute("employeeName", employee);
                model.addAttribute("employeeLevel", 10);
                groupLevel = 10;
            }
        }

        if (level != null) {
            if (StringUtils.isEmpty(employee) || groupLevel < level) {
                return "redirect:401-error-page";
            }
        }

        return view;
    }

}
