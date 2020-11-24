<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!-- Main sidebar -->
<div class="sidebar sidebar-main fii-sidebar-menu leftCol" id="mySidebar">
    <div class="sidebar-content">

        <!-- Main navigation -->
        <div class="sidebar-category sidebar-category-visible">
            <div class="category-content no-padding">
                <ul class="navigation navigation-main navigation-accordion">

                    <!-- Main -->
                    <!-- <li class="imenu" path="home"><a href="/home"><i class="icon-home4"></i><span>Home</span></a></li> -->
                    <li id="bu_qv">
                        <a href="#"><i class="icon-stack2"></i> <span>QV (桂武)</span></a>
                        <ul id="ul_bu_qv">
                            <li><a href="#"><i class="icon-stack2"></i><span>B04</span></a>
                                <ul>
                                    <li class="imenu" path="b04-overview"><a href="/overview?factory=B04">Overview</a></li>
                                    <li class="imenu" path="re-checklist"><a data-href="/re/checklist" onclick="openWindow(this.dataset.href);">8S Management System</a></li>
                                    <li class="imenu"><a href="#">SMT & PTH Management</a>
                                        <ul>
                                            <li class="imenu" path="b04-total-line-dashboard"><a data-href="/total-line-dashboard?factory=B04" onclick="openWindow(this.dataset.href);">Total Line Dashboard</a></li>
                                            <li class="imenu" path="b04-aoi-status-tab"><a onclick="addTabTest('Status B04','Status-b04', 'http://10.224.81.70:8888/aoi-analytics/aoi-status-tab?factory=B04')">AOI Status </a></li>
                                            <li class="imenu"><a onclick="addTabTest('Detail B04','Detail-b04', 'http://10.225.35.80:2810/')">AOI Detail</a></li>
                                            <li class="imenu"><a onclick="addTabTest('Error B04', 'Error-b04', 'http://10.225.35.80:2810/error')">AOI Offset Error</a></li>
                                            <li class="imenu"><a onclick="addTabTest('Escape B04', 'Escape-b04', 'http://10.225.35.80:2810/escape')">AOI Escape</a></li>
                                        </ul>
                                    </li>
                                    <li><a href="#">Tester Management</a>
                                        <ul>
                                            <li class="imenu" path="b04-station-dashboard"><a href="/station-dashboard?factory=B04">Tester Dashboard</a></li>
                                            <li class="imenu" path="b04-station-status"><a href="/station-status?factory=B04">Tester Status</a></li>
                                            <li class="imenu" path="b04-station-detail"><a href="/station-detail?factory=B04">Tester Reason Analysis</a></li>
                                            <li class="imenu" path="b04-station-cpk"><a href="/station-cpk?factory=B04">Tester CPK Analysis</a></li>
                                            <li class="imenu" path="b04-cycletime"><a href="/cycletime?factory=B04">Cycle Time</a></li>
                                            <li class="imenu" path="b04-production-plan"><a href="/production-plan?factory=B04">Production Plan</a></li>
                                            <li class="imenu" path="b04-performance-and-top-issue"><a href="/performance-and-top-issue?factory=B04">Performance And Top Issue</a></li>
                                            <li class="imenu" path="b04-error-analysis"><a href="/error-analysis?factory=B04">Error Analysis</a></li>
                                            <!-- <li class="imenu" path="b04-maintain"><a data-href="/maintain?factory=B04" onclick="openWindow(this.dataset.href);">Tester Maintain Control</a></li> -->
                                        </ul>
                                    </li>
                                    <li><a href="#">Engineer Management</a>
                                        <ul>
                                            <!-- <li class="imenu" path="b04-resource"><a data-href="/resource?factory=B04" onclick="openWindow(this.dataset.href);">Engineer Information</a></li> -->
                                            <li class="imenu" path="b04-engineer-management"><a href="/engineer-management?factory=B04">Engineer Management</a></li>
                                            <li class="imenu" path="b04-engineer-top-3"><a href="/engineer-top-3?factory=B04">Engineer Top 3</a></li>
                                            <li class="imenu" path="b04-engineer-work-shift"><a href="/engineer-work-shift?factory=B04">Engineer Work Shift</a></li>
                                            <li class="imenu" path="b04-task-management"><a href="/task-management?factory=B04">Task Management</a></li>
                                            <li class="imenu" path="b04-task-management-confirm"><a href="/task-management-confirm?factory=B04">Task Management Confirm</a></li>
                                        </ul>
                                    </li>
                                    <li class="imenu" path="re-home"><a href="/re?factory=B04">RE Repair Management</a></li>
                                    <li class="imenu" path="nbb-vi-dt"><a href="/vi">Auto soldering VI</a></li>
                                </ul>
                            </li>
                                <li><a href="#"> <span>B05</span> <i class="icon-stack2"></i></a>
                                    <ul>
                                        <li class="imenu"><a data-href="#">SMT & PTH Management</a></li>
                                        <li><a href="#">Tester Management</a>
                                            <ul>
                                                <li class="imenu" path="b05-station-status"><a data-href="/station-status?factory=B05" onclick="openWindow(this.dataset.href);">Tester Status</a></li>
                                                <li class="imenu" path="b05-station-detail"><a data-href="/station-detail?factory=B05" onclick="openWindow(this.dataset.href);">Tester Reason Analysis</a></li>
                                                <!-- <li class="imenu" path="b05-resource"><a data-href="/resource?factory=B05" onclick="openWindow(this.dataset.href);">Engineer Information</a></li> -->
                                                <li class="imenu" path="b05-engineer-management"><a data-href="/engineer-management?factory=B05" onclick="openWindow(this.dataset.href);">Engineer Management</a></li>
                                                <li class="imenu" path="b05-engineer-top-3"><a data-href="/engineer-top-3?factory=B05" onclick="openWindow(this.dataset.href);">Engineer Top 3</a></li>
                                            </ul>
                                        </li>
                                        <li class="imenu" path="automation-home"><a data-href="//10.225.35.80:2810" onclick="openWindow(this.dataset.href);">Automation Management</a></li>
                                    </ul>
                                </li>
                                <li><a href="#"> <span>B06</span> <i class="icon-stack2"></i></a>
                                    <ul>
                                        <li class="imenu"><a href="#">SMT & PTH Management</a>
                                            <ul>
                                                <li class="imenu" path="b06-total-line-dashboard"> <a data-href="/total-line-dashboard?factory=B06" onclick="openWindow(this.dataset.href);">Total Line Dashboard</a></li>
                                                <li class="imenu" path="b06-line-balance"> <a href="/line-balance?factory=B06">SMT Line Balance</a></li>
                                                <li class="imenu" path="b06-line-man"> <a href="/line-man?factory=B06">FATP Line & man calculations</a></li>
                                                <li class="imenu" path="b06-calc-line-balance"> <a href="/calc-line-balance?factory=B06">SMT line requirements calculate</a></li>
                                                <li class="imenu"><a onclick="addTabTest('Status B06', 'Status-b06', 'http://10.224.81.70:8888/aoi-analytics/aoi-status-tab?factory=B06')">AOI Status</a></li>
                                                <li class="imenu"><a onclick="addTabTest('Detail B06', 'Detail-b06', 'http://10.225.35.80:2810/')">AOI Detail</a> </li>
                                                <li class="imenu"><a onclick="addTabTest('Error B06', 'Error-b06', 'http://10.225.35.80:2810/error')">AOI Offset Error</a></li>
                                                <li class="imenu"><a onclick="addTabTest('Escape B06', 'Escape-b06', 'http://10.225.35.80:2810/escape')">AOI Escape</a></li>
                                                <li class="imenu" path="b06-offset-abnormal-report"><a data-href="/aoi-analytics/smt/offset-abnormal-list" onclick="openWindow(this.dataset.href);">Offset Abnormal Report</a></li>
                                            </ul>
                                        </li>
                                        <li><a href="#">Tester Management</a>
                                            <ul>
                                                <li class="imenu" path="b06-station-dashboard"><a data-href="/station-dashboard?factory=B06" onclick="openWindow(this.dataset.href);">Tester Dashboard</a></li>
                                                <li class="imenu" path="b06-station-status"><a data-href="/station-status?factory=B06" onclick="openWindow(this.dataset.href);">Tester Status</a></li>
                                                <li class="imenu" path="b06-station-detail"><a data-href="/station-detail?factory=B06" onclick="openWindow(this.dataset.href);">Tester Reason Analysis</a></li>
                                                <li class="imenu" path="b06-station-cpk"><a data-href="/station-cpk?factory=B06" onclick="openWindow(this.dataset.href);">Tester CPK Analysis</a></li>
                                                <li class="imenu" path="b06-cpk-tool"><a data-href="/cpk-tool?factory=B06" onclick="openWindow(this.dataset.href);"> CPK Tool</a></li>
                                                <li class="imenu" path="b06-cpk-config"><a data-href="/cpk-config?factory=B06" onclick="openWindow(this.dataset.href);"> CPK Config</a></li>
                                                <li class="imenu" path="b06-copy-ic-data"><a data-href="/copy-ic-data?factory=B06" onclick="openWindow(this.dataset.href);">Copy IC Data</a></li>
                                                <li class="imenu" path="b06-cycletime"><a href="/cycletime?factory=B06">Cycle Time</a></li>
                                                <li class="imenu" path="b06-production-plan"><a href="/production-plan?factory=B06">Production Plan</a></li>
                                                <!-- <li class="imenu" path="re-c03-in-out"><a href="/re/in-out-ui?factory=B06"><span>RE REPORT</span></a></li> -->
                                                <li class="imenu" path="b06-pe-daily-report"><a href="/pe-daily-report?factory=B06"><span>PE Daily Report</span></a></li>
                                                <li class="imenu" path="b06-te-report"><a href="/te-report?factory=B06"><span>TE Report</span></a></li>
                                                <li class="imenu" path="ef-overall"><a href="/ef-management/overall"><span>EF Management</span></a></li>
                                            </ul>
                                        </li>
                                        <li><a href="#">Engineer Management</a>
                                            <ul>
                                                <!-- <li class="imenu" path="b06-resource"><a data-href="/resource?factory=B06" onclick="openWindow(this.dataset.href);">Engineer Information</a></li> -->
                                                <li class="imenu" path="b06-engineer-management"><a data-href="/engineer-management?factory=B06" onclick="openWindow(this.dataset.href);">Engineer Management</a></li>
                                                <li class="imenu" path="b06-engineer-top-3"><a data-href="/engineer-top-3?factory=B06" onclick="openWindow(this.dataset.href);">Engineer Top 3</a></li>
                                                <li class="imenu" path="b06-engineer-work-shift"><a href="/engineer-work-shift?factory=B06">Engineer Work Shift</a></li>
                                                <li class="imenu" path="b06-task-management"><a data-href="/task-management?factory=B06" onclick="openWindow(this.dataset.href);">Task Management</a></li>
                                                <li class="imenu" path="b06-task-management-confirm"><a data-href="/task-management-confirm?factory=B06" onclick="openWindow(this.dataset.href);">Task Management Confirm</a></li>
                                            </ul>
                                        </li>
                                        <li><a href="#">RE Repair Management</a>
                                            <ul>
                                                <li class="imenu" path="b06-re-checkin-out"><a href="/re/checkinout?factory=B06">Check In Out</a></li>
                                                <li class="imenu" path="b06-re-bonepile-detail"><a href="/re/bonepile-detail?factory=B06">Bone Pile</a></li>
                                                <li class="imenu" path="b06-re-stock-bc8m"><a href="/re/stock-management/bc8m?factory=B06">BC8M</a></li>
                                                <li class="imenu" path="b06-re-stock-rma"><a href="/re/stock-management/rma?factory=B06">RMA</a></li>
                                                <li class="imenu" path="b06-re-capacity"><a href="/re/capacity?factory=B06">Capacity</a></li>
                                                <li class="imenu" path="b06-re-capacity"><a data-href="http://10.224.92.184/re" onclick="openWindow(this.dataset.href);">RE Smart Debug</a></li>
                                                <li class="imenu" path="b06-re-capacity"><a data-href="http://10.224.92.188:8051" onclick="openWindow(this.dataset.href);">RE B06 System</a></li>
                                            </ul>
                                        </li>
                                        <li class="imenu" path="b06-uph-tracking"><a data-href="/uph-tracking?factory=B06" onclick="openWindow(this.dataset.href);">UPH Tracking Dashboard</a></li>
                                        <li class="imenu" path="b06-uph-tracking-all"><a data-href="/uph-tracking-all?factory=B06" onclick="openWindow(this.dataset.href);">UPH Tracking By All Model</a></li>
                                    </ul>
                                </li>
                                <li><a href="#"> <span>C03</span> <i class="icon-stack2"></i></a>
                                    <ul>
                                        <li><a href="#">Tester Management</a>
                                            <ul>
                                                <li class="imenu" path="c03-station-dashboard"><a href="/station-dashboard?factory=C03">Tester Dashboard</a></li>
                                                <li class="imenu" path="c03-station-status"><a href="/station-status?factory=C03">Tester Status</a></li>
                                                <li class="imenu" path="c03-station-detail"><a href="/station-detail?factory=C03">Tester Reason Analysis</a></li>
                                                <li class="imenu" path="re-c03-in-out"><a href="/re/in-out-ui?factory=C03"><span>RE REPORT</span></a></li>
                                                <li class="imenu" path="c03-pe-daily-report"><a href="/pe-daily-report?factory=C03"><span>PE Daily Report</span></a></li>
                                                <li class="imenu" path="c03-te-report"><a href="/te-report?factory=C03"><span>TE Report</span></a></li>
                                            </ul>
                                        </li>
                                        <!-- <li class="imenu" path="c03-uph-tracking"><a data-href="/uph-tracking?factory=C03" onclick="openWindow(this.dataset.href);">UPH Tracking Dashboard</a></li> -->
                                        <li class="imenu" path="c03-uph-tracking-dashboard"><a href="/uph-tracking-dashboard?factory=C03"><span>UPH Tracking Dashboard</span></a>
                                        </li>
                                        <li class="imenu" path="c03-uph-tracking-all"><a data-href="/uph-tracking-all?factory=C03" onclick="openWindow(this.dataset.href);">UPH Tracking By All Model</a></li>
                                        <li class="imenu" path="c03-wip-group"><a data-href="/wip-group?factory=C03" onclick="openWindow(this.dataset.href);"><span>WIP Group</span></a></li>
                                    </ul>
                                </li>
                                <li>
                                    <a href="#"><i class="icon-stack2"></i><span>BN3</span></a>
                                    <ul>
                                        <li class="imenu" path="nbb-uph-tracking-dashboard"><a href="/uph-tracking-dashboard?factory=NBB"><span>UPH Tracking Dashboard</span></a></li>
                                        <li class="imenu" path="nbb-station-dashboard"> <a href="/station-dashboard?factory=NBB">Tester Dashboard</a></li>
                                        <!-- <li class="imenu" path="nbb-uph-by-line"><a data-href="/nbb-uph-by-line" onclick="openWindow(this.dataset.href);">UPH Tracking Dashboard</a></li> -->
                                        <li class="imenu" path="nbb-abnormal-dashboard"> <a href="/abnormal-dashboard?factory=NBB">Abnormal Dashboard</a></li>
                                        <li class="imenu" path="nbb-input-output"><a data-href="/input-output?factory=NBB" onclick="openWindow(this.dataset.href);">Input & Output</a></li>
                                        <li class="imenu" path="nbb-wip-group"><a data-href="/wip-group?factory=NBB" onclick="openWindow(this.dataset.href);"><span>WIP Group</span></a></li>
                                        <li class="imenu" path="nbb-overview"><a data-href="/nbb-overview" onclick="openWindow(this.dataset.href);">NBB Overview</a></li>
                                        <li class="imenu" path="nbb-cycletime"><a href="/cycletime?factory=NBB">Cycle Time</a></li>
                                        <li class="imenu" path="nbb-temperature-and-humidity"><a data-href="/temperature-and-humidity" onclick="openWindow(this.dataset.href);">Temperature And Humidity</a></li>
                                        <li> <a href="#"><span>Other</span></a>
                                            <ul>
                                                <li class="imenu" path="nbb-wip"><a data-href="/nbb-wip" onclick="openWindow(this.dataset.href);">WIP</a></li>
                                                <li class="imenu" path="nbb-uph-by-line-packing"><a data-href="/nbb-uph-by-line-packing" onclick="openWindow(this.dataset.href);">UPH By Line Packing</a></li>
                                                <li class="imenu" path="nbb-vlrr"><a data-href="/nbb-vlrr" onclick="openWindow(this.dataset.href);">NBB VLRR</a></li>
                                                <li class="imenu" path="nbb-output-line"><a data-href="/nbb-output-line" onclick="openWindow(this.dataset.href);">Output By Line</a></li>
                                                <li class="imenu" path="nbb-yield-overall"><a data-href="/nbb-yield-overall" onclick="openWindow(this.dataset.href);">Yield Overall</a></li>
                                                <li class="imenu" path="nbb-error-overall"><a data-href="/nbb-error-overall" onclick="openWindow(this.dataset.href);">Test Error Overall</a></li>
                                                <li class="imenu" path="nbb-manage-dt"><a data-href="/managenbb" onclick="openWindow(this.dataset.href);">Screw Missing Inspection</a></li>
                                                <li class="imenu" path="nbb-manage-group"><a data-href="/manager-group?factory=NBB" onclick="openWindow(this.dataset.href);">Manager Group</a></li>
                                                <li class="imenu" path="nbb-uph-tracking-all"><a data-href="/uph-tracking-all?factory=NBB" onclick="openWindow(this.dataset.href);">UPH Tracking By All Model</a></li>
                                            </ul>
                                        </li>
                                    </ul>
                                </li>
                        </ul>
                    </li>
                    <li id="bu_qc">
                        <a href="#"><i class="icon-stack2"></i> <span>QC (光州)</span></a>
                        <ul id="ul_bu_qc">
                            <li><a href="#">F12</a>
                                <ul>
                                    <li><a href="#">Tester Management</a>
                                        <ul>
                                            <li class="imenu" path="s03-station-dashboard"><a href="/station-dashboard?factory=S03">Tester Dashboard</a></li>
                                            <li class="imenu" path="re-c03-in-out"><a href="/re/in-out-ui?factory=S03"><span>RE REPORT</span></a></li>
                                            <li class="imenu" path="s03-pe-daily-report"><a href="/pe-daily-report?factory=S03"><span>PE Daily Report</span></a></li>
                                            <li class="imenu" path="s03-te-report"><a href="/te-report?factory=S03"><span>TE Report</span></a></li>
                                        </ul>
                                    </li>
                                    <!-- <li class="imenu" path="qc-qc-uph-tracking"><a href="/qc-uph-tracking?factory=QC"><span>UPH Tracking Dashboard</span></a></li> -->
                                    <li class="imenu" path="s03-uph-tracking-dashboard"><a href="/uph-tracking-dashboard?factory=S03"><span>UPH Tracking Dashboard</span></a></li>
                                    <li class="imenu" path="s03-wip-group"><a href="/wip-group?factory=S03"><span>WIP Group</span></a></li>
                                </ul>
                            </li>

                        </ul>
                    </li>
                    <li id="bu_dv">
                        <a href="#"><i class="icon-stack2"></i> <span>DV (黃田)</span></a>
                        <ul id="ul_bu_dv">
                            <li><a href="#">A02</a>
                                <ul>
                                    <li class="imenu" path="a02-input-output"><a href="/input-output?factory=A02">Input & Output</a></li>
                                </ul>
                            </li>
                            <li><a href="#">B01</a>
                                <ul>
                                    <li class="imenu" path="b01-input-output"><a href="/input-output?factory=B01">Input & Output</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li><a href="#"> <span>HR (人資)</span> <i class="fa fa-user" style="font-size: 17px;"></i></a>
                        <ul>
                            <li class="imenu" path="hr-index"><a data-href="/hr" onclick="openWindow(this.dataset.href);">DashBoard HR</a></li>
                            <li class="imenu" path="hr-workCount"><a data-href="/hr/workcount" onclick="openWindow(this.dataset.href);">Work Count</a></li>
                            <li class="imenu" path="foreign-work-count"><a data-href="/hr/foreign-work-count" onclick="openWindow(this.dataset.href);">Foreign Work Count</a></li>
                        </ul>
                    </li>
                    <!-- /main -->
                    <li class="imenu" path="about">
                        <a href="/about"><i class="fa fa-info-circle" style="font-size: 16px;"></i><span>About Us</span></a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- /main navigation -->

    </div>
</div>
<!-- /main sidebar -->
<script src="/assets/js/custom/common.js"></script>
<script>
        activeMenu('${path}', '${factory}');
</script>