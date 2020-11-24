<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/custom/css/re/home.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<style>
    .ic-body:hover {
        transform: scale(1.1);
    }

    label.label.ic-label {
        cursor: pointer;
    }
</style>
<div class="panel panel-re panel-flat row">
    <div class="panel panel-overview" id="header" style="color: #ccc; background: #333; margin-bottom: 10px;">
        <b><span>B04-BBD-RE Repair Management System</span></b>
    </div>
    <div class="panel panel-default row ic-row">
        <a data-href="/re/stock-management" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-2 ic-head">
                <div style="cursor:pointer; margin-bottom: 5px; margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/59-512.png"></div>
                <label class="label ic-label">Stock Management</label>
            </div>
        </a>
        <a data-href="/re/bonepile-detail?factory=B04" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-2 ic-body col-xs-5ths">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/BONE PILE.png"></div>
                <label class="label ic-label">Bone Pile</label>
            </div>
        </a>
        <a data-href="/re/stock-management/bc8m?factory=B04" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-2 ic-body col-xs-5ths">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/BC8M11.png"></div>
                <label class="label ic-label">BC8M</label>
            </div>
        </a>
        <!-- <a data-href="/re/online" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-2 ic-body">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/Online_wip.png"></div>
                <label class="label ic-label">Online Wip</label>
            </div>
        </a> -->
        <a data-href="/re/stock-management/rma?factory=B04" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-2 ic-body col-xs-5ths">
                <div style="cursor:pointer; margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/RMA.png"></div>
                <label class="label ic-label">RMA</label>
            </div>
        </a>
        <!-- <a data-href="/re/bonepile" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-2 ic-body">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/Bone_Pile.png"></div>
                <label class="label ic-label">Total Wip</label>
            </div>
        </a> -->
    </div>
    <div class="panel panel-default row ic-row">
        <div class="col-xs-5ths ic-head">
            <div style="cursor:pointer; margin-bottom: 5px; margin-top: 10px;"><img class="ic-img"
                    src="/assets/images/custom/re/io_management.png"></div>
            <label class="label ic-label">I/O Management</label>
        </div>
        <!-- <a data-href="/re/checkin" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-5ths ic-body">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/Check_in.png"></div>
                <label class="label ic-label">Check In</label>
            </div>
        </a>
        <a data-href="/re/checkout" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-5ths ic-body">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/Check_out.png"></div>
                <label class="label ic-label">Check Out</label>
            </div>
        </a> -->
        <a data-href="/re/checkinout?factory=B04" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-5ths ic-body">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/Check_out.png"></div>
                <label class="label ic-label">Check In Out</label>
            </div>
        </a>
        <div class="col-xs-5ths ic-body">
            <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                    src="/assets/images/custom/re/Search_Data.png"></div>
            <label class="label ic-label">Search Data</label>
        </div>
        <div class="col-xs-5ths ic-body">
            <div style="cursor:pointer; margin-top: 10px;"><img class="ic-img"
                    src="/assets/images/custom/re/Return.png"></div>
            <label class="label ic-label">Return</label>
        </div>
    </div>
    <div class="panel panel-default row ic-row">
        <div class="col-xs-5ths ic-head">
            <div style="cursor:pointer; margin-bottom: 5px; margin-top: 10px;"><img class="ic-img"
                    src="/assets/images/custom/re/management.png"></div>
            <label class="label  ic-label">Engineer Management</label>
        </div>
        <a data-href="/re/capacity?factory=B04" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-5ths ic-body">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                        src="/assets/images/custom/re/rate_capacity.png"></div>
                <label class="label ic-label">Capacity</label>
            </div>
        </a>
        <div class="col-xs-5ths ic-body">
            <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img" style="width: 55px !important;"
                    src="/assets/images/custom/re/Training_Control.png"></div>
            <label class="label ic-label">Training Control</label>
        </div>
        <div class="col-xs-5ths ic-body">
            <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                    src="/assets/images/custom/re/OT_Control.png">
            </div>
            <label class="label ic-label">OT Control</label>
        </div>
        <a data-href="/re/checklist" onclick="hrefWindow(this.dataset.href);">
            <div class="col-xs-5ths ic-body">
                <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img" src="/assets/images/custom/re/8s.png">
                </div>
                <label class="label ic-label">Discipline & 8S Control</label>
            </div>
        </a>
    </div>
    <div class="panel panel-default row ic-row">
        <div class="col-xs-2 ic-head">
            <div style="cursor:pointer; margin-bottom: 5px; margin-top: 10px;"><img class="ic-img"
                    src="/assets/images/custom/re/Document_Mn.png"></div>
            <label class="label ic-label">Document Management</label>
        </div>
        <div class="col-xs-2 ic-body">
            <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img" src="/assets/images/custom/re/BOM.png">
            </div>
            <label class="label ic-label">BOM</label>
        </div>
        <div class="col-xs-2 ic-body">
            <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img" src="/assets/images/custom/re/SOP.png">
            </div>
            <label class="label ic-label">SOP</label>
        </div>
        <div class="col-xs-2 ic-body">
            <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                    src="/assets/images/custom/re/Logs_Test_search.png"></div>
            <label class="label ic-label">Logs Test Search</label>
        </div>
        <div class="col-xs-2 ic-body">
            <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img" style="width: 150px !important;"
                    src="/assets/images/custom/re/SI_Repair_Tool.png"></div>
            <label class="label ic-label">SI Repair Tool</label>
        </div>
        <div class="col-xs-2 ic-body">
            <div style="cursor:pointer;margin-top: 10px;"><img class="ic-img"
                    src="/assets/images/custom/re/SMT_Repair_Tool.png"></div>
            <label class="label ic-label">SMT Repair Tool</label>
        </div>
    </div>
</div>