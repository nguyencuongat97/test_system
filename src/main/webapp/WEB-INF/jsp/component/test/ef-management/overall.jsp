<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/tables/datatables/datatables.min.js"></script>
<style>
    .panel {
        margin-bottom: 12.5px;
        background-color: #fff;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.20), 0 1px 2px rgba(0, 0, 0, 0.24);
    }
    #using {
        background: #28a745;
        color: #fff;
        box-shadow: 0.1rem 0.2rem 0.5rem #272727;
        /* background-image: linear-gradient(#28a745, #24bd47); */
    }
    #offline {
        background: #ababab;
        box-shadow: 0.1rem 0.2rem 0.5rem #272727;
        /* background-image: linear-gradient(#969696, #ababab); */
    }
    #cabrating {
        background: #1a9bba;
        color: #fff;
        box-shadow: 0.1rem 0.2rem 0.5rem #272727;
        /* background-image: linear-gradient(#1a9bba, #33bdde); */
    }
    #repairing {
        background: #fac010;
        box-shadow: 0.1rem 0.2rem 0.5rem #272727;
        /* background-image: linear-gradient(#fac010, #fdda6f); */
    }
    .inner h4 {
        font-weight: 700;
        margin: 0px 0 0 20px;
    }
    .inner p {
        margin: 0 0 0px 20px;
    }
    .content_ef {
        padding: 0.5rem;
        padding-top: 1rem;
        background-image: linear-gradient(#f7f7f7, #c2c2c2);
    }
    .tableFixHead {
        overflow-y: auto;
        height: 210px;
    }
    .pre-scrollable {
        max-height: 210px !important;
    }
    .tableFixHead thead th {
        position: sticky;
        top: 0;
    }
    table {
        border-collapse: collapse;
        width: 100%;
    }
    .table-fixed th {
        background: #eee;
    }
    ul.nav.nav-tabs {
        margin-bottom: 0rem;
    }
    .table-responsive.pre-scrollable.tableFixHead {
        max-height: 230px;
        border: 1px solid #dddddd;
        border-top: none;
    }
        ::-webkit-scrollbar-thumb {
        border-radius: 5px;
        background: #cecfd0;
    }
    .infchart {
        padding: 0px 6.5px 0px 6.5px;
    }
    .inner {
        padding: 1rem;
    }
    .panel-tittle span {
        font-size: 16px;
        font-weight: 600;
        padding-left: 1rem;
    }
    .panel-tittle {
        border-bottom: 1px solid #dddddd;
        margin-bottom: 1rem;
        width: 100%;
        margin-left: 0.05rem;
        padding-top: 0.7rem;
    }
    .panel-body {
        padding: 0px;
    }
    .panel-flat {
        height: 250px;
    }
    .inner sup {
        font-size: 17px;
    }
    .dataTables_paginate .paginate_button {
        padding: 1px;
        min-width: 30px;
    }
    .dataTables_filter {
        float: right;
        margin: 7px;
    }
    .table>thead>tr>th,
    .table>tbody>tr>th,
    .table>tfoot>tr>th,
    .table>thead>tr>td,
    .table>tbody>tr>td,
    .table>tfoot>tr>td {
        padding: 6px 8px;
    }
    .dataTable thead .sorting,
    .dataTable thead .sorting_asc,
    .dataTable thead .sorting_desc,
    .dataTable thead .sorting_asc_disabled,
    .dataTable thead .sorting_desc_disabled {
        padding-right: 35px;
    }
    .dataTable thead .sorting:before,
    .dataTable thead .sorting:after,
    .dataTable thead .sorting_asc:after,
    .dataTable thead .sorting_desc:after,
    .dataTable thead .sorting_asc_disabled:after,
    .dataTable thead .sorting_desc_disabled:after {
        right: 8px;
    }
    .dataTables_filter input {
        height: 27px;
        padding: 0px 0;
    }
    .dataTables_filter {
        display: none;
    }
    .tableFitter>label:after {
        content: "\e98e";
        font-family: 'icomoon';
        font-size: 12px;
        display: inline-block;
        /* position: absolute;
        top: 50%;
        right: 0;
        margin-top: -6px; */
        color: #999999;
        line-height: 1;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
    }
    .tableFitter input {
        outline: 0;
        width: 200px;
        height: 26px;
        padding: 8px 5px;
        padding-right: 24px;
        font-size: 13px;
        line-height: 1.5384616;
        color: #333333;
        background-color: transparent;
        border: 1px solid transparent;
        border-width: 1px 0;
        border-bottom-color: #ddd;
    }
    .reminderAlert {
        color: red;
        font-weight: 700;
        background: #f1e2e2;
    }
    .warning {
        color: #ff7600;
        font-weight: 700;
    }

    .loader {
        display: none;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) url('/assets/images/loadingg.gif') 50% 50% no-repeat;
    }

    .tableFitter label {
        border-style: inset;
        border-width: medium;
        border-color: #ccc;
    }
</style>

<div class="loader"></div>
<div class="panel panel-re row">
    <div class="row">
        <div class="pull-left">
            <div class="input-group">
                <span class="input-group-addon" style="padding: 0px 10px 0px 20px; color: inherit;"><i class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpanPage" style="height: 28px; width: 22rem;">
                <span class="btn btn-border" style="padding: 5px 10px 5px 10px; color: inherit;" onclick="timeSpanChange(-1)"><i class="fa fa-angle-double-left"></i>prev shift</span>
                <span>||</span>
                <span class="btn btn-border" style="padding: 5px 10px 5px 10px; color: inherit;" onclick="timeSpanChange(1)">next shift<i class="fa fa-angle-double-right"></i></span>
            </div>
        </div>
    </div>

    <div class="content_ef">
        <div class="row no-margin">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#tabContentEquipment" data-toggle="tab">Equipments</a>
                </li>
                <li><a href="#tabContentFixture" data-toggle="tab" id="loadTabDetailFi" class="loadTable">Fixture</a></li>
            </ul>
        </div>
        <div class="tab-content" style="padding-top: 10px;">
            <div id="tabContentEquipment" class="tab-pane active">
                <div class="row no-margin">
                    <div class="col-xs-12 col-sm-3">
                        <div id="using" class="panel panel-body">
                            <div class="inner">
                                <h4 id="h4UsingEq"></h4>
                                <p id="pUsingEq"></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-3">
                        <div id="offline" class="panel panel-body">
                            <div class="inner">
                                <h4 id="h4OfflineEq"></h4>
                                <p id="pOfflineEq"></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-3">
                        <div id="cabrating" class="panel panel-body">
                            <div class="inner">
                                <h4 id="h4CabratingEq"></h4>
                                <p id="pCabratingEq"></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-3">
                        <div id="repairing" class="panel panel-body">
                            <div class="inner">
                                <h4 id="h4RepairingEq"></h4>
                                <p id="pRepairingEq"></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row no-margin">
                    <div class="col-xs-12 col-sm-5 infchart">
                        <div class=" panel panel-flat panel-body">
                            <div class="panel-tittle row" style="margin-bottom: 0;">
                                <span><i class="fa fa-line-chart"></i> Performance</span>
                                <!-- <div class="pull-right">
                                    <div class="input-group">
                                        <input type="text" class="form-control datetimerange" side="right" name="timeSpanE" style="height: 28px; width: 22rem;">
                                        <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i
                                            class="icon-calendar22"></i></span>
                                    </div>
                                </div> -->
                            </div>
                            <div class="panel-body">
                                <div id="peformanceChartE" style="height: 207px;"></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-4 infchart">
                        <div class="panel panel-flat panel-body">
                            <ul class="nav nav-tabs">
                                <li id="liBuyE" class="active"><a href="#tabBuyE" data-toggle="tab" style="font-size:13px; padding: 6px;">Buy</a>
                                </li>
                                <li id="liBorrowE"><a href="#tabBorrowE" data-toggle="tab" style="font-size:13px; padding: 6px;">Borrow</a></li>
                                <li id="liRentE"><a href="#tabRentE" data-toggle="tab" style="font-size:13px; padding: 6px;">Rent</a></li>
                            </ul>
                            <div class="tab-content">
                                <div id="tabBuyE" class="table-responsive pre-scrollable tableFixHead tab-pane active">
                                    <table id="tblBuyE" class="table table-xxs table-fixed text-nowrap table-sticky">
                                        <thead>
                                            <tr>
                                                <th> No </th>
                                                <th>Name</th>
                                                <th>E/F</th>
                                                <th>Type</th>
                                                <th>L.Calib Time</th>
                                                <th>Due date</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                                <div id="tabBorrowE" class="table-responsive pre-scrollable tableFixHead tab-pane">
                                    <table id="tblBorrowE" class="table table-xxs table-fixed text-nowrap table-sticky">
                                        <thead>
                                            <tr>
                                                <th> No </th>
                                                <th>Name</th>
                                                <th>E/F</th>
                                                <th>Type</th>
                                                <th>Borrow Time</th>
                                                <th>Due date</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                                <div id="tabRentE" class="table-responsive pre-scrollable tableFixHead tab-pane">
                                    <table id="tblRentE" class="table table-xxs table-fixed text-nowrap table-sticky">
                                        <thead>
                                            <tr>
                                                <th> No </th>
                                                <th>Name</th>
                                                <th>E/F</th>
                                                <th>Type</th>
                                                <th>Rent Time</th>
                                                <th>Due date</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-3 infchart">
                        <div class=" panel panel-flat panel-body">
                            <div class="panel-tittle row" style="margin-bottom: 0;">
                                <span id="spanBuychartEq">Buy</span>
                            </div>
                            <div id="buyChartEq" style="height: 219px;"></div>
                        </div>
                    </div>
                </div>
                <div class="row no-margin">
                    <div class="col-xs-12 col-sm-3 infchart">
                        <div class=" panel panel-flat panel-body">
                            <div class="panel-tittle row">
                                <span><i class="fa fa-pie-chart"></i> Owner Type</span>
                            </div>
                            <div id="ownedTypeChartE" style="height: 207px;"></div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-9 infchart">
                        <div class=" panel panel-flat panel-body">
                            <div class="panel-tittle row">
                                <span>Online Equipments by Device Type</span>
                            </div>
                            <div id="onlineChartE" style="height: 207px;"></div>
                        </div>
                    </div>
                </div>
                <div class="row no-margin">
                    <div class="col-xs-12 col-sm-12">
                        <div class="panel panel-body" style="min-height: 420px;">
                            <div class="panel-tittle row">
                                <div class="col-md-2 col-xs-12">
                                    <span>Detail Infor</span>
                                </div>
                                <div class="col-md-10 col-xs-12">
                                    <div class="pull-left" style="margin-right: 1rem;">
                                        <div class="tableFitter">
                                            <label><input type="search" id="searchboxEq" placeholder="Search..." aria-controls="tblEfDetail"></label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="divTableEquipment" class="table-responsive col-md-12" style="max-height: auto;max-width: 100%">
                                <table id="tblEfDetailEquipment" class="table table-custom table-bordered table-sticky">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Name</th>
                                            <th>Type</th>
                                            <th>Ownner</th>
                                            <th>Line</th>
                                            <th>User Model</th>
                                            <th>Station</th>
                                            <th>ATE</th>
                                            <th>Begin Time</th>
                                            <th>Last Online Time</th>
                                            <th>Used Time</th>
                                            <th>Free Time</th>
                                            <th>Time Online</th>
                                            <th>Time Offline</th>
                                            <th>Total Time</th>
                                            <th>Performance (%)</th>
                                        </tr>
                                    </thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tabContentFixture" class="tab-pane">
                <div class="row no-margin">
                    <div class="col-xs-12 col-sm-3">
                        <div id="using" class="panel panel-body">
                            <div class="inner">
                                <h4 id="h4UsingFi"></h4>
                                <p id="pUsingFi"></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-3">
                        <div id="offline" class="panel panel-body">
                            <div class="inner">
                                <h4 id="h4OfflineFi"></h4>
                                <p id="pOfflineFi"></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-3">
                        <div id="cabrating" class="panel panel-body">
                            <div class="inner">
                                <h4 id="h4CabratingFi"></h4>
                                <p id="pCabratingFi"></p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-3">
                        <div id="repairing" class="panel panel-body">
                            <div class="inner">
                                <h4 id="h4RepairingFi"></h4>
                                <p id="pRepairingFi"></p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row no-margin">
                    <div class="col-xs-12 col-sm-5 infchart">
                        <div class=" panel panel-flat panel-body">
                            <div class="panel-tittle row" style="margin-bottom: 0;">
                                <span><i class="fa fa-line-chart"></i> Performance</span>
                                <!-- <div class="pull-right">
                                    <div class="input-group">
                                        <input type="text" class="form-control datetimerange" side="right" name="timeSpanF" style="height: 28px; width: 22rem;">
                                        <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i
                                            class="icon-calendar22"></i></span>
                                    </div>
                                </div> -->
                            </div>
                            <div class="panel-body">
                                <div id="peformanceChartF" style="height: 207px;">aaaa</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-4 infchart">
                        <div class="panel panel-flat panel-body">
                            <ul class="nav nav-tabs">
                                <li id="liBuyF" class="active"><a href="#tabBuyF" data-toggle="tab" style="font-size:13px; padding: 6px;">Buy</a>
                                </li>
                                <li id="liBorrowF"><a href="#tabBorrowF" data-toggle="tab" style="font-size:13px; padding: 6px;">Borrow</a></li>
                                <li id="liRentF"><a href="#tabRentF" data-toggle="tab" style="font-size:13px; padding: 6px;">Rent</a></li>
                            </ul>
                            <div class="tab-content">
                                <div id="tabBuyF" class="table-responsive pre-scrollable tableFixHead tab-pane active">
                                    <table id="tblBuyF" class="table table-xxs table-fixed text-nowrap table-sticky">
                                        <thead>
                                            <tr>
                                                <th> No </th>
                                                <th>Name</th>
                                                <th>E/F</th>
                                                <th>Type</th>
                                                <th>L.Calib Time</th>
                                                <th>Due date</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                                <div id="tabBorrowF" class="table-responsive pre-scrollable tableFixHead tab-pane">
                                    <table id="tblBorrowF" class="table table-xxs table-fixed text-nowrap table-sticky">
                                        <thead>
                                            <tr>
                                                <th> No </th>
                                                <th>Name</th>
                                                <th>E/F</th>
                                                <th>Type</th>
                                                <th>Borrow Time</th>
                                                <th>Due date</th>
                                            </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                                <div id="tabRentF" class="table-responsive pre-scrollable tableFixHead tab-pane">
                                    <table id="tblRentF" class="table table-xxs table-fixed text-nowrap table-sticky">
                                        <thead>
                                            <tr>
                                                <th> No </th>
                                                <th>Name</th>
                                                <th>E/F</th>
                                                <th>Type</th>
                                                <th>Rent Time</th>
                                                <th>Due date</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-3 infchart">
                        <div class=" panel panel-flat panel-body">
                            <div class="panel-tittle row" style="margin-bottom: 0;">
                                <span id="spanBuychartFi">Buy</span>
                            </div>
                            <div id="buyChartFi" style="height: 219px;"></div>
                        </div>
                    </div>
                </div>
                <div class="row no-margin">
                    <div class="col-xs-12 col-sm-3 infchart">
                        <div class=" panel panel-flat panel-body">
                            <div class="panel-tittle row">
                                <span><i class="fa fa-pie-chart"></i> Owner Type</span>
                            </div>
                            <div id="ownedTypeChartF" style="height: 207px;"></div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-9 infchart">
                        <div class=" panel panel-flat panel-body">
                            <div class="panel-tittle row">
                                <span>Online Equipments by Device Type</span>
                            </div>
                            <div id="onlineChartF" style="height: 207px;"></div>
                        </div>
                    </div>
                </div>
                <div class="row no-margin">
                    <div class="col-xs-12 col-sm-12">
                        <div class="panel panel-body" style="min-height: 420px;">
                            <div class="panel-tittle row">
                                <div class="col-md-2 col-xs-12">
                                    <span>Detail Infor</span>
                                </div>
                                <div class="col-md-10 col-xs-12">
                                    <div class="pull-left" style="margin-right: 1rem;">
                                        <div class="tableFitter">
                                            <label><input type="search" id="searchboxFi" placeholder="Search..." aria-controls="tblEfDetail"></label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="divTableFixture" class="table-responsive col-md-12" style="max-height: auto;max-width: 100%">
                                <table id="tblEfDetailFixture" class="table table-custom table-bordered table-sticky">
                                    <thead>
                                        <tr>
                                            <th>No</th>
                                            <th>Name</th>
                                            <th>Type</th>
                                            <th>Ownner</th>
                                            <th>Line</th>
                                            <th>User Model</th>
                                            <th>Station</th>
                                            <th>ATE</th>
                                            <th>Begin Time</th>
                                            <th>Last Online Time</th>
                                            <th>Used Time</th>
                                            <th>Free Time</th>
                                            <th>Time Online</th>
                                            <th>Time Offline</th>
                                            <th>Total Time</th>
                                            <th>Performance (%)</th>
                                        </tr>
                                    </thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var ownerType = 'buy';
    var dataset = {};
    var loadingMap = {
        "equipmentTrend" : false,
        "equipmentOnlineType" : false,
        "equipmentPerformance" : false,
        "fixtureTrend" : false,
        "fixtureOnlineType" : false,
        "fixturePerformance" : false,
    };
    getTimeNow();
    function init(){
        // getTimeNow();
        loadEfSumary();
        loadTblReminder();
        loadBuyChart(ownerType);
        loadOwnedTypeChartE();
        loadOwnedTypeChartF();
        loadOnlineChartE();
        loadOnlineChartF();
        loadDetailEq();
        // loadDetailFi();
        loadPerformanceChartEq();
        loadPerformanceChartFi();
    }

    function loadEfSumary() {
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/equipment/status/sumary",
            data: {
                factory: 'b06',
                ownerType: ''
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var data = res;
                for (i in data) {
                    if (data[i].rate != 0) {
                        data[i].rate = data[i].rate.toFixed(2);
                    }
                }
                $('#h4UsingEq').html('' + data.using.rate + '%');
                $('#pUsingEq').html('Using (' + data.using.qty + '/' + data.using.total + ')');
                $('#h4OfflineEq').html('' + data.offline.rate + '%');
                $('#pOfflineEq').html('Offline (' + data.offline.qty + '/' + data.offline.total + ')');
                $('#h4CabratingEq').html('' + data.calibrating.rate + '%');
                $('#pCabratingEq').html('Calibrating (' + data.calibrating.qty + '/' + data.calibrating.total + ')');
                $('#h4RepairingEq').html('' + data.repairing.rate + '%');
                $('#pRepairingEq').html('Repairing (' + data.repairing.qty + '/' + data.repairing.total + ')');
            },
            error: function() {
                alert("Fail!");
            }
        });
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/fixture/status/sumary",
            data: {
                factory: 'b06',
                ownerType: ''
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var data = res;
                for (i in data) {
                    if (data[i].rate != 0) {
                        data[i].rate = data[i].rate.toFixed(2);
                    }
                }
                $('#h4UsingFi').html('' + data.using.rate + '%');
                $('#pUsingFi').html('Using (' + data.using.qty + '/' + data.using.total + ')');
                $('#h4OfflineFi').html('' + data.offline.rate + '%');
                $('#pOfflineFi').html('Offline (' + data.offline.qty + '/' + data.offline.total + ')');
                $('#h4CabratingFi').html('' + data.calibrating.rate + '%');
                $('#pCabratingFi').html('Calibrating (' + data.calibrating.qty + '/' + data.calibrating.total + ')');
                $('#h4RepairingFi').html('' + data.repairing.rate + '%');
                $('#pRepairingFi').html('Repairing (' + data.repairing.qty + '/' + data.repairing.total + ')');
            },
            error: function() {
                alert("Fail!");
            }
        });
    }

    function loadDetailEq() {
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/equipment/detail/list",
            data: {
                factory: 'B06',
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var html1 = '';
                var index = 0;
                $('#tblEfDetailEquipment tbody').html('');
                for (i in res) {
                    var data = res['equipmentResult'];
                    for (j in data) {
                        index = Number(j) + 1;
                        html1 += '<tr><td>' + index + '</td>' +
                            '<td style="white-space: initial;">' + data[j].name + '</td>' +
                            '<td>' + data[j].type + '</td>' +
                            '<td>' + data[j].ownner + '</td>' +
                            '<td>' + data[j].line + '</td>' +
                            '<td>' + data[j].model + '</td>' +
                            '<td>' + data[j].station + '</td>' +
                            '<td>' + data[j].ate + '</td>' +
                            '<td>' + data[j].beginTime + '</td>' +
                            '<td>' + data[j].latestOnlineTime + '</td>' +
                            '<td>' + data[j].useTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].freeTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].onlineTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].offlineTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].totalTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].performance.toFixed(2) + '%</td></tr>';
                    }
                }
                $('#tblEfDetailEquipment tbody').html(html1);
                var myTable1 = $('#tblEfDetailEquipment').DataTable({
                    scrollX: true,
                    "bLengthChange": false,
                    "bInfo": false,
                    "bAutoWidth": false,
                    "columnDefs": [{
                        "width": "0.5rem",
                        "targets": 0
                    }, ]
                });
                $("#searchboxEq").keyup(function() {
                    myTable1.search(this.value).draw();
                });
                loadingMap["equipmentPerformance"] = false;
                checkLoading();
            },
            error: function() {
                alert("Fail!");
            }
        });
    }
    function loadDetailFi() {
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/fixture/detail/list",
            data: {
                factory: 'B06',
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var html2 = '';
                var index = 0;
                $('#tblEfDetailFixture tbody').html('');
                for (i in res) {
                    var data = res['fixtureResult'];
                    for (j in data) {
                        index = Number(j) + 1;
                        html2 += '<tr><td>' + index + '</td>' +
                            '<td style="white-space: initial;">' + data[j].name + '</td>' +
                            '<td>' + data[j].type + '</td>' +
                            '<td>' + data[j].ownner + '</td>' +
                            '<td>' + data[j].line + '</td>' +
                            '<td>' + data[j].model + '</td>' +
                            '<td>' + data[j].station + '</td>' +
                            '<td>' + data[j].ate + '</td>' +
                            '<td>' + data[j].beginTime + '</td>' +
                            '<td>' + data[j].latestOnlineTime + '</td>' +
                            '<td>' + data[j].useTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].freeTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].onlineTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].offlineTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].totalTime.toFixed(2) + '</td>' +
                            '<td>' + data[j].performance.toFixed(2) + '%</td></tr>';
                    }
                }
                $('#tblEfDetailFixture tbody').html(html2);
                var myTable2 = $('#tblEfDetailFixture').DataTable({
                    scrollX: true,
                    "bLengthChange": false,
                    "bInfo": true,
                    "bAutoWidth": false,
                    "columnDefs": [{
                        "width": "0.5rem",
                        "targets": 0
                    }, ]
                });
                $("#searchboxFi").keyup(function() {
                    myTable2.search(this.value).draw();
                });
                loadingMap["fixturePerformance"] = false;
                checkLoading();
            },
            error: function() {
                alert("Fail!");
            }
        });
    }

    function loadPerformanceChartEq() {
        var dataHE = [],
            dataLE = [];
        var startTime = dataset.timeSpan.slice(0,16);
        var endTime = dataset.timeSpan.slice(22,38);
        var SYear = new Date(startTime).getFullYear();
        var SMonth = new Date(startTime).getMonth();
        var SDay = new Date(startTime).getDate();
        var EYear = new Date(endTime).getFullYear();
        var EMonth = new Date(endTime).getMonth();
        var EDay = new Date(endTime).getDate();

        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/equipment/performance/trend",
            data: {
                factory: 'b06',
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var nameH = '';
                var nameL = '';
                for (i in res) {
                    data = res[i].data;
                    if (i == 'highestEquipment') {
                        for (j in data) {
                            dataHE.push(data[j].USEDTIME);
                        }
                        nameH = res[i].meta['EQUIPMENT_NAME'];
                    }
                    if (i == 'lowestEquipment') {
                        for (j in data) {
                            dataLE.push(data[j].USEDTIME);
                        }
                        nameL = res[i].meta['EQUIPMENT_NAME'];
                    }
                }
                var serries = [{
                    name: nameH,
                    data: dataHE
                },{
                    name: nameL,
                    data: dataLE
                }];

                Highcharts.chart('peformanceChartE', {
                    chart: {
                        type: 'spline',
                        scrollablePlotArea: {
                            minWidth: 0,
                            scrollPositionX: 1
                        }
                    },
                    title: {
                        text: '',
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    yAxis: {
                        title: '',
                        minorGridLineWidth: 0,
                        gridLineWidth: 0,
                        alternateGridColor: null,
                    },
                    tooltip: {
                        valueSuffix: ' times/h'
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 2,
                            states: {
                                hover: {
                                    lineWidth: 3
                                }
                            },
                            marker: {
                                enabled: false
                            },
                            pointInterval: 3600000,
                            pointStart: Date.UTC(SYear, SMonth, SDay, 7, 30, 0),
                            pointEnd: Date.UTC(EYear, EMonth, EDay, 7, 30, 0),
                            label: {
                                enabled: false
                            }
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: serries,
                    // series: [{
                    //     name: 'H.Equipment',
                    //     data: dataHE

                    // }, {
                    //     name: 'L.Equipment',
                    //     data: dataLE
                    // }],
                });

                loadingMap["equipmentTrend"] = false;
                checkLoading();
            },
            error: function() {
                alert("Fail!");
            }
        });
    }
    function loadPerformanceChartFi() {
        var dataHF = [],
            dataLF = [];
        var startTime = dataset.timeSpan.slice(0,16);
        var endTime = dataset.timeSpan.slice(22,38);
        var SYear = new Date(startTime).getFullYear();
        var SMonth = new Date(startTime).getMonth();
        var SDay = new Date(startTime).getDate();
        var EYear = new Date(endTime).getFullYear();
        var EMonth = new Date(endTime).getMonth();
        var EDay = new Date(endTime).getDate();
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/fixture/performance/trend",
            data: {
                factory: 'b06',
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var nameH = '';
                var nameL = '';
                for (i in res) {
                    data = res[i].data;
                    if (i == 'highestFixture') {
                        for (j in data) {
                            dataHF.push(data[j].USEDTIME);
                        }
                        nameH = res[i].meta['FIXTURE_CODE'];
                    }
                    if (i == 'lowestFixture') {
                        for (j in data) {
                            dataLF.push(data[j].USEDTIME);
                        }
                        nameL = res[i].meta['FIXTURE_CODE'];
                    }
                }
                var serries = [{
                    name: nameH,
                    color: '#1f7f23',
                    data: dataHF
                },{
                    name: nameL,
                    color: '#333333',
                    data: dataLF
                }];
                Highcharts.chart('peformanceChartF', {
                    chart: {
                        type: 'spline',
                        scrollablePlotArea: {
                            minWidth: 0,
                            scrollPositionX: 1
                        }
                    },
                    title: {
                        text: '',
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    yAxis: {
                        title: '',
                        minorGridLineWidth: 0,
                        gridLineWidth: 0,
                        alternateGridColor: null,
                    },
                    tooltip: {
                        valueSuffix: ' times/h'
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 2,
                            states: {
                                hover: {
                                    lineWidth: 3
                                }
                            },
                            marker: {
                                enabled: false
                            },
                            pointInterval: 3600000,
                            pointStart: Date.UTC(SYear, SMonth, SDay, 7, 30, 0),
                            pointEnd: Date.UTC(EYear, EMonth, EDay, 7, 30, 0),
                            label: {
                                enabled: false
                            }
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: serries,
                    // series: [{
                    //     name: 'H.Fixture',
                    //     color: '#1f7f23',
                    //     data: dataHF
                    // }, {
                    //     name: 'L.Fixture',
                    //     color: '#333333',
                    //     data: dataLF
                    // }],
                });
                loadingMap["fixtureTrend"] = false;
                checkLoading();
            },
            error: function() {
                alert("Fail!");
            }
        });
    }

    $("#liBuyE").on("click", function() {
        $('#spanBuychartEq').html('Buy');
        ownerType = 'buy';
        loadBuyChart(ownerType);
    });
    $("#liBorrowE").on("click", function() {
        $('#spanBuychartEq').html('Borow');
        ownerType = 'borrow';
        loadBuyChart(ownerType);
    });
    $("#liRentE").on("click", function() {
        $('#spanBuychartEq').html('Rent');
        ownerType = 'rent';
        loadBuyChart(ownerType);
    });
    $("#liBuyF").on("click", function() {
        $('#spanBuychartFi').html('Buy');
        ownerType = 'buy';
        loadBuyChart(ownerType);
    });
    $("#liBorrowF").on("click", function() {
        $('#spanBuychartFi').html('Borow');
        ownerType = 'borrow';
        loadBuyChart(ownerType);
    });
    $("#liRentF").on("click", function() {
        $('#spanBuychartFi').html('Rent');
        ownerType = 'rent';
        loadBuyChart(ownerType);
    });

    $('#loadTabDetailFi').on('click', function() {
        var cList = this.classList;
        for(i in cList){
            if(cList[i] == 'loadTable'){
                loadDetailFi();
                $('#loadTabDetailFi').removeClass('loadTable');
            }
        }
    });

    function loadTblReminder() {
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/equipment/duedate/reminder",
            data: {
                factory: 'b06',
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var index = 1;
                var html;
                for (i in res) {
                    data = res[i];
                    for (j in data) {
                        if (data[j].receiveTime != null) {
                            data[j].receiveTime = moment(new Date(data[j].receiveTime)).format("YYYY/MM/DD");
                            data[j].returnTime = moment(new Date(data[j].returnTime)).format("YYYY/MM/DD");
                        }
                    }
                    if (i == 'buy') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html = '<tr><td>' + index + '</td>' +
                                '<td style="white-space: initial;">' + convertNull(data[j].name) + '</td>' +
                                '<td>' + convertNull(data[j].EF) + '</td>' +
                                '<td>' + convertNull(data[j].type) + '</td>' +
                                '<td>' + convertNull(data[j].receiveTime) + '</td>' +
                                '<td class="reminderbuy' + j + '">' + convertNull(data[j].returnTime) + '</td>' +
                                '</tr>';
                            $('#tblBuyE tbody').append(html);
                            if (data[j].reminder == 'alert') {
                                $('.reminderbuy' + j + '').addClass('reminderAlert');
                            } else if (data[j].reminder == 'warning') {
                                $('.reminderbuy' + j + '').addClass('warning');
                            }
                        }
                    } else if (i == 'borrow') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html = '<tr><td>' + index + '</td>' +
                                '<td style="white-space: initial;">' + convertNull(data[j].name) + '</td>' +
                                '<td>' + convertNull(data[j].EF) + '</td>' +
                                '<td>' + convertNull(data[j].type) + '</td>' +
                                '<td>' + convertNull(data[j].receiveTime) + '</td>' +
                                '<td class="reminderborrow' + j + '">' + convertNull(data[j].returnTime) + '</td>' +
                                '</tr>';
                            $('#tblBorrowE tbody').append(html);
                            if (data[j].reminder == 'alert') {
                                $('.reminderborrow' + j + '').addClass('reminderAlert');
                            } else if (data[j].reminder == 'warning') {
                                $('.reminderborrow' + j + '').addClass('warning');
                            }
                        }

                    } else if (i == 'rent') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html = '<tr><td>' + index + '</td>' +
                                '<td style="white-space: initial;">' + convertNull(data[j].name) + '</td>' +
                                '<td>' + convertNull(data[j].EF) + '</td>' +
                                '<td>' + convertNull(data[j].type) + '</td>' +
                                '<td>' + convertNull(data[j].receiveTime) + '</td>' +
                                '<td class="reminderrent' + j + '">' + convertNull(data[j].returnTime) + '</td>' +
                                '</tr>';
                            $('#tblRentE tbody').append(html);
                            if (data[j].reminder == 'alert') {
                                $('.reminderrent' + j + '').addClass('reminderAlert');
                            } else if (data[j].reminder == 'warning') {
                                $('.reminderrent' + j + '').addClass('warning');
                            }
                        }

                    }
                }
            },
            error: function() {
                alert("Fail!");
            }
        });
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/fixture/duedate/reminder",
            data: {
                factory: 'b06',
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var index = 1;
                var html;
                for (i in res) {
                    data = res[i];
                    for (j in data) {
                        if (data[j].receiveTime != null) {
                            data[j].receiveTime = moment(new Date(data[j].receiveTime)).format("YYYY/MM/DD");
                            data[j].returnTime = moment(new Date(data[j].returnTime)).format("YYYY/MM/DD");
                        }
                    }
                    if (i == 'buy') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html = '<tr><td>' + index + '</td>' +
                                '<td style="white-space: initial;">' + convertNull(data[j].name) + '</td>' +
                                '<td>' + convertNull(data[j].EF) + '</td>' +
                                '<td>' + convertNull(data[j].type) + '</td>' +
                                '<td>' + convertNull(data[j].receiveTime) + '</td>' +
                                '<td class="reminderbuy' + j + '">' + convertNull(data[j].returnTime) + '</td>' +
                                '</tr>';
                            $('#tblBuyF tbody').append(html);
                            if (data[j].reminder == 'alert') {
                                $('.reminderbuy' + j + '').addClass('reminderAlert');
                            } else if (data[j].reminder == 'warning') {
                                $('.reminderbuy' + j + '').addClass('warning');
                            }
                        }
                    } else if (i == 'borrow') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html = '<tr><td>' + index + '</td>' +
                                '<td style="white-space: initial;">' + convertNull(data[j].name) + '</td>' +
                                '<td>' + convertNull(data[j].EF) + '</td>' +
                                '<td>' + convertNull(data[j].type) + '</td>' +
                                '<td>' + convertNull(data[j].receiveTime) + '</td>' +
                                '<td class="reminderborrow' + j + '">' + convertNull(data[j].returnTime) + '</td>' +
                                '</tr>';
                            $('#tblBorrowF tbody').append(html);
                            if (data[j].reminder == 'alert') {
                                $('.reminderborrow' + j + '').addClass('reminderAlert');
                            } else if (data[j].reminder == 'warning') {
                                $('.reminderborrow' + j + '').addClass('warning');
                            }
                        }

                    } else if (i == 'rent') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html = '<tr><td>' + index + '</td>' +
                                '<td style="white-space: initial;">' + convertNull(data[j].name) + '</td>' +
                                '<td>' + convertNull(data[j].EF) + '</td>' +
                                '<td>' + convertNull(data[j].type) + '</td>' +
                                '<td>' + convertNull(data[j].receiveTime) + '</td>' +
                                '<td class="reminderrent' + j + '">' + convertNull(data[j].returnTime) + '</td>' +
                                '</tr>';
                            $('#tblRentF tbody').append(html);
                            if (data[j].reminder == 'alert') {
                                $('.reminderrent' + j + '').addClass('reminderAlert');
                            } else if (data[j].reminder == 'warning') {
                                $('.reminderrent' + j + '').addClass('warning');
                            }
                        }

                    }
                }
            },
            error: function() {
                alert("Fail!");
            }
        });
    }

    function loadBuyChart(ownerType) {
        var colors = ['#ababab', '#28a745', '#1a9bba', '#fac010'];
        dataBuyChart = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/equipment/status/sumary",
            data: {
                factory: 'b06',
                ownerType: ownerType
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                dataBuyChart = [];
                data = res;
                for (i in data) {
                    dataBuyChart.push({
                        name: i,
                        y: data[i].qty,
                        total: data[i].total
                    });
                }
                Highcharts.chart('buyChartEq', {
                    chart: {
                        type: 'pie'
                    },
                    title: {
                        text: '',
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.y}/{point.total}</b>'
                    },
                    accessibility: {
                        point: {
                            valueSuffix: '%'
                        }
                    },
                    plotOptions: {
                        pie: {
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '<b>{point.name}</b><br>{point.percentage:.1f} %',
                                distance: -25,
                                filter: {
                                    property: 'percentage',
                                    operator: '>',
                                    value: 20
                                },
                                connectorColor: 'silver'
                            },
                            showInLegend: true
                        }
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '12px',
                            fontWeight: '100'
                        },
                        layout: 'horizontal',
                        align: 'center',
                        verticalAlign: 'bottom'
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function(color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')]
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataBuyChart
                    }]
                });
            },
            error: function() {
                alert("Fail!");
            }
        });

        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/fixture/status/sumary",
            data: {
                factory: 'b06',
                ownerType: ownerType
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                dataBuyChart = [];
                data = res;
                for (i in data) {
                    dataBuyChart.push({
                        name: i,
                        y: data[i].qty,
                        total: data[i].total
                    });
                }
                Highcharts.chart('buyChartFi', {
                    chart: {
                        type: 'pie'
                    },
                    title: {
                        text: '',
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.y}/{point.total}</b>'
                    },
                    accessibility: {
                        point: {
                            valueSuffix: '%'
                        }
                    },
                    plotOptions: {
                        pie: {
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '<b>{point.name}</b><br>{point.percentage:.1f} %',
                                distance: -25,
                                filter: {
                                    property: 'percentage',
                                    operator: '>',
                                    value: 20
                                },
                                connectorColor: 'silver'
                            },
                            showInLegend: true
                        }
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '12px',
                            fontWeight: '100'
                        },
                        layout: 'horizontal',
                        align: 'center',
                        verticalAlign: 'bottom'
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function(color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')]
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataBuyChart
                    }]
                });
            },
            error: function() {
                alert("Fail!");
            }
        });
    }

    function loadOwnedTypeChartE() {
        var colors = ['#7cb5ec', '#90ed7d', '#8085e9'];
        var dataOwnerChart = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/equipment/owner/sumary",
            data: {
                factory: 'b06',
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                data = res;
                for (i in data) {
                    dataOwnerChart.push({
                        name: data[i].owner,
                        y: data[i].qty,
                        totalError: data[i].qty
                    });
                }
                Highcharts.chart('ownedTypeChartE', {
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },
                    title: {
                        text: '',
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.y}/{point.total}</b>'
                    },
                    accessibility: {
                        point: {
                            valueSuffix: '%'
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '<span style="color:{point.color}"><b>{point.name}</b><br>{point.percentage:.1f} %</span>',
                                distance: -25,
                                filter: {
                                    property: 'percentage',
                                    operator: '>',
                                    value: 20
                                },
                                connectorColor: 'silver'
                            },
                            showInLegend: true
                        },
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '11px',
                            fontWeight: '100'
                        },
                        layout: 'horizontal',
                        align: 'center',
                        verticalAlign: 'bottom'
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function(color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataOwnerChart,
                        colorByPoint: true,
                        marker: {
                            enabled: false
                        },
                    }]
                });
            },
            error: function() {
                alert("Fail!");
            }
        });
    }
    function loadOwnedTypeChartF() {
        var colors = ['#7cb5ec', '#90ed7d', '#8085e9'];
        var dataOwnerChart = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/fixture/owner/sumary",
            data: {
                factory: 'b06',
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                data = res;
                for (i in data) {
                    dataOwnerChart.push({
                        name: data[i].owner,
                        y: data[i].qty,
                        totalError: data[i].qty
                    });
                }
                Highcharts.chart('ownedTypeChartF', {
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },
                    title: {
                        text: '',
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.y}/{point.total}</b>'
                    },
                    accessibility: {
                        point: {
                            valueSuffix: '%'
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '<b>{point.name}</b><br>{point.percentage:.1f} %',
                                distance: -25,
                                filter: {
                                    property: 'percentage',
                                    operator: '>',
                                    value: 20
                                },
                                connectorColor: 'silver'
                            },
                            showInLegend: true
                        }
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '11px',
                            fontWeight: '100'
                        },
                        layout: 'horizontal',
                        align: 'center',
                        verticalAlign: 'bottom'
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function(color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataOwnerChart,
                    }]
                });
            },
            error: function() {
                alert("Fail!");
            }
        });
    }

    function loadOnlineChartE() {
        var dataOnlineChart = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/equipment/status/online/by/type",
            data: {
                factory: 'b06',
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var data = res;
                for (i in data) {
                    if(data[i].TYPE == null){
                        data[i].TYPE = 'null';
                    }
                    dataOnlineChart[i] = {
                        name: data[i].TYPE,
                        y: Number(data[i].RATE),
                        qty: Number(data[i].QTY),
                        total: Number(data[i].TOTAL)
                    };
                }
                var colors = ['#28a745', '#ababab', '#1a9bba', '#fac010'];
                Highcharts.chart('onlineChartE', {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: '',
                    },
                    xAxis: {
                        type: 'category',
                        labels: {
                            rotation: -45,
                            style: {
                                fontSize: '13px',
                                fontFamily: 'Verdana, sans-serif'
                            }
                        }
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Rate (%)'
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.qty}/{point.total}</b>'
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function(color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataOnlineChart,
                        dataLabels: {
                            enabled: true,
                            format: '{point.y:.1f}%', // one decimal
                            style: {
                                fontSize: '9px',
                                fontFamily: 'Verdana, sans-serif'
                            }
                        }
                    }]
                });
                loadingMap["equipmentOnlineType"]= false;
                checkLoading();
            },
            error: function() {
                alert("Fail!");
            }
        });
    }
    function loadOnlineChartF() {
        var dataOnlineChart = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/fixture/status/online/by/type",
            data: {
                factory: 'b06',
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function(res) {
                var data = res;
                for (i in data) {
                    if(data[i].TYPE == null){
                        data[i].TYPE = 'null';
                    }
                    dataOnlineChart[i] = {
                        name: data[i].TYPE,
                        y: Number(data[i].RATE),
                        qty: Number(data[i].QTY),
                        total: Number(data[i].TOTAL)
                    };
                }
                var colors = ['#28a745', '#ababab', '#1a9bba', '#fac010'];
                Highcharts.chart('onlineChartF', {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: '',
                    },
                    xAxis: {
                        type: 'category',
                        labels: {
                            rotation: -45,
                            style: {
                                fontSize: '13px',
                                fontFamily: 'Verdana, sans-serif'
                            }
                        }
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Rate (%)'
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.qty}/{point.total}</b>'
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function(color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataOnlineChart,
                        dataLabels: {
                            enabled: true,
                            format: '{point.y:.1f}%', // one decimal
                            style: {
                                fontSize: '9px',
                                fontFamily: 'Verdana, sans-serif'
                            }
                        }
                    }]
                });
                loadingMap["fixtureOnlineType"] = false;
                checkLoading();
            },
            error: function() {
                alert("Fail!");
            }
        });
    }

    function convertNull(text){
        var value = '';
        if(text != null){
            value = text;
        }
        return value;
    }
    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/time/shift/now",
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $('input[name=timeSpanPage]').data('daterangepicker').setStartDate(new Date(data.startTime));
                $('input[name=timeSpanPage]').data('daterangepicker').setEndDate(new Date(data.endTime));

                dataset.timeSpan = data.startTime + ' - ' + data.endTime;
                init();
                
                $('input[name=timeSpanPage]').on('change', function() {
                    dataset.timeSpan = this.value;
                    reloadTimeSpanView();
                });
            },
            error: function() {
                alert("Fail!");
            }
        });
    }
    function timeSpanChange(shift) {
        var settings = {
            "url": "/api/test/efmanager/time/timespan/add",
            "method": "GET",
            "timeout": 0,
            "data" : {
                timeSpan : dataset.timeSpan,
                addShift : shift,
            },
        };

        $.ajax(settings).done(function (response) {
            dataset.timeSpan = response;
            $('input[name=timeSpanPage]').val(response);
            reloadTimeSpanView();
        });
    }

    function reloadTimeSpanView() {
        $('.loader').css('display', 'block');
        loadingMap = {
            "equipmentTrend" : true,
            "equipmentOnlineType" : true,
            "equipmentPerformance" : true,
            "fixtureTrend" : true,
            "fixtureOnlineType" : true,
            "fixturePerformance" : true,
        };

        loadPerformanceChartEq();
        loadPerformanceChartFi();

        loadOnlineChartE()
        loadOnlineChartF();

        var table = $('#tblEfDetailEquipment').DataTable();
        table.destroy();
        loadDetailEq();

        var table = $('#tblEfDetailFixture').DataTable();
        table.destroy();
        loadDetailFi();
    }

    function checkLoading() {
        if (    !loadingMap["equipmentTrend"] 
                && !loadingMap["equipmentOnlineType"] 
                && !loadingMap["equipmentPerformance"] 
                && !loadingMap["fixtureTrend"] 
                && !loadingMap["fixtureOnlineType"] 
                && !loadingMap["fixturePerformance"] ) 
        {
            $('.loader').css('display', 'none');
            return true;
        }

        return false;
    }

</script>