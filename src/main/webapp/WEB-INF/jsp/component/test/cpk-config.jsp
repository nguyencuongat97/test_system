<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<style>
    #header {
        text-align: center;
        font-size: 16px;
        font-weight: bold;
        padding: 6px;
    }

    .dropbtn {
        float: left;
        margin-right: 5px;
    }

    .select-drop {
        background: #fff;
        border: 1px solid #d7d7d7;
        border-radius: 5px;
        color: #000;
    }

    .table {
        font-size: 16px;
    }

    .table {
        margin-bottom: 0;
    }

    .table-bordered {
        border: 1px solid #ddd;
    }

    .tbl {
        width: 100%;
        max-width: 100%;
        margin-bottom: 20px;
    }

    .tbl thead th {
        text-align: center;
        background: #285BAC;
        color: #fff;
    }


    .button {
        background: #FFC04A;
        border: 1px solid #FFC04A;
        border-radius: 3px;
        padding: 2px 5px;
    }

    .btn-primary {
        color: #fff;
        background-color: #285BAC;
        border-color: #285BAC;
    }

    .btn-primary:active,
    .btn-primary.active,
    .open>.dropdown-toggle.btn-primary {
        background-color: #285BAC;
    }

    .btn-primary:active:hover,
    .btn-primary.active:hover,
    .open>.dropdown-toggle.btn-primary:hover,
    .btn-primary:active:focus,
    .btn-primary.active:focus,
    .open>.dropdown-toggle.btn-primary:focus,
    .btn-primary:active.focus,
    .btn-primary.active.focus,
    .open>.dropdown-toggle.btn-primary.focus {
        background-color: #285BAC;
    }

    .btn-primary:focus,
    .btn-primary.focus,
    .btn-primary:hover {
        background-color: #285BAC;
    }

    .hight-dropdown {
        max-height: 400px;
        overflow: scroll;
        margin-top: 7px;
    }

    .load-list-data {
        list-style-type: none;
        padding: 0px;
    }

    .load-list-data li:hover {
        background-color: #d7d7d7;
        cursor: pointer;
    }

    .load-list-data>li {
        border-bottom: 1px solid #e0e0e0;
        padding: 8px;
    }

    .load-list-data>li>a {
        color: #000 !important;
    }

    .card {
        box-shadow: 0 0 1px rgba(0, 0, 0, .125), 0 1px 3px rgba(0, 0, 0, .2);
        margin-bottom: 1rem;
    }

    .card {
        position: relative;
        display: -ms-flexbox;
        display: flex;
        -ms-flex-direction: column;
        flex-direction: column;
        min-width: 0;
        word-wrap: break-word;
        background-color: #fff;
        background-clip: border-box;
        border: 0 solid rgba(0, 0, 0, .125);
        border-radius: .25rem;
    }

    .card-header {
        padding: 6px;
        background: #d7d7d7;
    }

    .card-header {
        background-color: transparent;
        border-bottom: 1px solid rgba(0, 0, 0, .125);
        padding: .75rem 1.25rem;
        position: relative;
        border-top-left-radius: .25rem;
        border-top-right-radius: .25rem;
    }

    .card-header {
        padding: 0px;
        background: #fff;
    }

    .nav-tabs {
        margin-bottom: 0px;
    }



    .nav-tabs>li>a {
        margin-right: 0;
        color: #000;
        border-radius: 0;
        text-transform: uppercase;
    }

    .btn {
        position: relative;
        font-weight: 500;
        text-transform: uppercase;
        border-width: 0;
        padding: 6px 17px;
    }

    .tbl {
        font-size: 16px;
        margin: 0px;
    }

    .table-bordered {
        border: 1px solid #ddd;
    }

    .tbl {
        width: 100%;
        max-width: 100%;
    }

    .tbl thead th {
        text-align: center;
        background: #285BAC;
        color: #fff;
        padding: 5px;
    }

    .tbl tbody tr td {
        text-align: center;
        padding: 5px;
    }

    .nav-tabs>li.active>a,
    .nav-tabs>li.active>a:hover,
    .nav-tabs>li.active>a:focus {
        color: #285BAC;
        background-color: #fff;
        border: 1px solid #285BAC;
        border-bottom-color: transparent;
        cursor: default;
    }

    .nav-tabs {
        border-bottom: 1px solid #285BAC;
    }

    /* width */
    ::-webkit-scrollbar {
        width: 5px;

    }

    ::-webkit-scrollbar-thumb {
        border-radius: 5px;
        background: #285BAC;
    }

    /* Handle on hover */
    ::-webkit-scrollbar-thumb:hover {
        background: #013992;
    }

    .tbl-detail-item tr:first-child th {
        position: sticky;
        top: 0;
    }

    @media screen and (max-width: 1024px) {
        .contact_us_info {
            background: #edeeef none repeat scroll 0 0;
            border: 1px solid #e9eaec;
            min-height: 140px;
            padding: 14px 20px;
            width: 100%;
            resize: none;
        }

        .col-md-4 {
            height: 80%;
        }
    }

    @media only screen and (min-width: 1024px) and (max-width: 1280px) {
        .contact_us_info {
            background: #edeeef none repeat scroll 0 0;
            border: 1px solid #e9eaec;
            min-height: 140px;
            padding: 14px 20px;
            width: 100%;
            resize: none;
        }

        .col-md-4 {
            height: 80%;
        }
    }

    @media only screen and (min-width: 1280px) and (max-width: 1365px) {
        .contact_us_info {
            background: #edeeef none repeat scroll 0 0;
            border: 1px solid #e9eaec;
            min-height: 140px;
            padding: 14px 20px;
            width: 100%;
            resize: none;
        }

        .col-md-4 {
            height: 80%;
        }
    }

    @media only screen and (min-width: 1366px) and (max-width: 1599px) {
        .contact_us_info {
            background: #edeeef none repeat scroll 0 0;
            border: 1px solid #e9eaec;
            min-height: 140px;
            padding: 14px 20px;
            width: 100%;
            resize: none;
        }

        .col-md-4 {
            height: 80%;
        }
    }

    @media only screen and (min-width: 1600px) and (max-width: 1919px) {
        .contact_us_info {
            background: #edeeef none repeat scroll 0 0;
            border: 1px solid #e9eaec;
            min-height: 200px;
            padding: 14px 20px;
            width: 100%;
            resize: none;
        }

        .col-md-4 {
            height: 60%;
        }
    }

    @media only screen and (min-width: 1920px) {
        .contact_us_info {
            background: #edeeef none repeat scroll 0 0;
            border: 1px solid #e9eaec;
            min-height: 200px;
            padding: 14px 20px;
            width: 100%;
            resize: none;
        }

        .col-md-4 {
            height: 65%;
        }
    }



    .tbl-detail-item tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    .tbl-detail-item tr:hover {
        background-color: #ddd;
    }

    .tbl-detail-item tbody tr:hover {
        background: #d7d7d7;
        cursor: pointer;
    }

    .form-group {
        margin-bottom: 0px;
        position: relative;
    }

    .input-group.date .input-group-addon {
        cursor: pointer;
    }

    .input-group-addon:last-child {
        border-left: 0;
    }

    .input-group .form-control:focus {
        z-index: 3;
    }

    .input-group .form-control,
    .input-group-addon,
    .input-group-btn {
        display: table-cell;
    }

    .input-group-addon {
        padding: 6px 12px;
        font-size: 14px;
        font-weight: 400;
        line-height: 1;
        color: #555;
        text-align: center;
        background-color: #eee;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    .input-group .form-control {
        position: relative;
        z-index: 2;
        float: left;
        width: 100%;
        margin-bottom: 0;
    }

    .form-control:focus {
        border-color: #66afe9;
        outline: 0;
        -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6);
        box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px rgba(102, 175, 233, .6);
    }

    .form-control {
        display: block;
        width: 100%;
        height: 34px;
        padding: 6px 12px;
        font-size: 14px;
        line-height: 1.42857143;
        color: #555;
        background-color: #fff;
        background-image: none;
        border: 1px solid #ccc;
        border-radius: 4px;
        -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
        box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075);
        -webkit-transition: border-color ease-in-out .15s, -webkit-box-shadow ease-in-out .15s;
        -o-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
        transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    }

    .glyphicon {
        font-size: 16px;
        vertical-align: middle;
        top: 1px;
    }

    .title-detail {
        text-align: center;
        padding: 0 8px 8px;
        font-weight: bold;
    }

    .btn-search {
        background: #d7d7d7;
        color: black;
        border: 1px solid #d7d7d7;
        border-radius: 0px 3px 3px 0px;
    }

    .w3-border {
        border: 1px solid #ccc !important;
    }

    .w3-round-large {
        border-radius: 5px;
    }

    .w3-input {
        height: 30px;
        padding: 4px;
        display: block;
        border: none;
        border-bottom: 1px solid #ccc;
        width: 100%;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
    }

    .button-save {
        background: #285BAC;
        color: #fff;
        border-radius: 5px;
        padding: 5px 10px;
        float: right;
        margin-right: 8px;
    }

    .button-clear {
        background: red;
        color: #fff;
        border-radius: 5px;
        padding: 5px 10px;
        float: right;
    }

    .button-edit {
        background: #d7d7d7;
        color: #000;
        border-radius: 5px;
        padding: 5px 10px;
        float: right;
    }

    .button-update {
        background: #d7d7d7;
        color: #000;
        border-radius: 5px;
        padding: 5px 10px;
    }

    .button-cancel {
        background: #d7d7d7;
        color: #000;
        border-radius: 5px;
        padding: 5px 10px;
    }

    .list-time {
        list-style-type: none;
    }

    .list-time li:hover {
        background-color: #d7d7d7;
        cursor: pointer;
    }

    .view-update-report {
        float: right;
    }

    .view-update-alarm {
        float: right;
    }

    .check-all-station {
        float: left;
        padding: 10px;
    }

    #tbl-cpkconfig tbody tr.active td {
        background-color: #8fc3f7;
    }

    #save-report:disabled {
        cursor: not-allowed;
        background: #d7d7d7;
    }

    #clear-view:disabled {
        cursor: not-allowed;
        background: #d7d7d7;
    }

    .tab-unactive:hover,
    .tab-unactive:focus {
        -webkit-transform: translateY(-5px) scale(1.02);
        transform: translateY(-5px) scale(1.02);
        box-shadow: 0px 5px 12px rgba(126, 142, 177, 0.2);
        transition: all 0.4s;
        cursor: not-allowed;
    }

    .dropdown-menu {
        position: absolute;
        top: 100%;
        left: 0;
        z-index: 1000;
        display: none;
        float: left;
        min-width: 160px;
        padding: 5px 0;
        margin: 2px 0 0;
        list-style: none;
        font-size: 13px;
        text-align: center;
        background-color: #fff;
        border: 1px solid transparent;
        border-radius: 3px;
        -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
        background-clip: padding-box;
    }

    .dropdown-menu-a {
        position: absolute;
        top: 100%;
        left: 0;
        z-index: 1000;
        display: none;
        float: left;
        min-width: 160px;
        padding: 5px 0;
        margin: 2px 0 0;
        list-style: none;
        font-size: 13px;
        text-align: left;
        background-color: #fff;
        border: 1px solid transparent;
        border-radius: 3px;
        -webkit-box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);
        background-clip: padding-box;
    }
</style>
<div class="row" style="height: 90vh;">
    <div class="col-lg-12" style="background: #fff; height: 100%;">
        <div class="panel panel-overview"
            style="margin:unset; background-color:#fff; color:#000; text-align:center; padding: 4px;">
            <span style="font-size:16px;">CPK CONFIG</span>
        </div>
        <div class="row" style="margin: unset; margin-top: 5px;">
            <div class="dropdown dropbtn">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><span
                        id="txt-model">Select Model</span>
                    <span class="caret"></span></button>
                <div class="dropdown-menu hight-dropdown"
                    style="max-height:400px; overflow: scroll; text-align: left; ">
                    <div class="bs-searchbox"><input type="text" class="form-control" autocomplete="off"
                            id="filter-model"></div>
                    <ul class="load-list-data" id="list-model">
                        <li>No data</li>
                    </ul>
                </div>
            </div>
            <div class="dropdown dropbtn">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><span
                        id="txt-group">Select
                        Station</span>
                    <span class="caret"></span></button>
                <div class="dropdown-menu hight-dropdown" style="max-height:400px; overflow: scroll; text-align: left;">
                    <div class="bs-searchbox"><input type="text" class="form-control" autocomplete="off"
                            id="filter-group"></div>
                    <ul class="load-list-data" id="list-group">
                        <li>No data</li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row" style="height: 90%; margin-top: 10px;">
            <div class="col-md-8" style="height: 95%;">
                <div class="card" style="height: 100%;">
                    <div class="card-header">
                        <h6 class="card-title col-md-6" style="margin-left: 8px;">DETAIL</h6>
                        <div class="form-group" style="width: 200px;float: right;margin: 5px 5px 0px 0px;">
                            <div class="input-group date">
                                <input type="text" class="form-control" placeholder="Search..."
                                    style="border: 1px solid #d7d7d7;padding: 8px;" id="txt-search-table">
                                <span class="input-group-addon" style="padding: 5px 10px 5px 10px;">
                                    <span class="fa fa-search"></span>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="card-body" style="height: 100%; overflow: scroll;">
                        <table class="tbl table-bordered tbl-detail-item" id="tbl-cpkconfig">
                            <thead>
                                <th style="width: 2%;">#</th>
                                <th style="width: 30%;">Test Item</th>
                                <th style="width: 10%;">LSL</th>
                                <th style="width: 10%;">USL</th>
                                <th style="width: 8%;">Report Time</th>
                                <th style="width: 8%;">Alarm Time</th>
                            </thead>
                            <tbody>
                                <tr>
                                    <td colspan="6">No data !</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card" style="height: 100%;">
                    <div class="card-header">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#home">REPORT</a></li>
                            <li><a data-toggle="tab" href="#menu1">ALARM</a></li>
                        </ul>
                    </div>
                    <div class="card-body" style="height: 100%; overflow: scroll;">
                        <div class="tab-content">
                            <div id="home" class="tab-pane fade in active">
                                <div class="row" style="margin:30px 20px 20px 20px;">
                                    <div class="title-detail">
                                        <span id="txt-title-report"></span>
                                    </div>
                                    <div class="form-group">
                                        <div class='input-group date' id='datetimepicker1'>
                                            <input type='datetime' class="form-control" placeholder="Select Report Time"
                                                id="txt-time-report" />
                                            <span class="input-group-addon" style="padding: 5px 10px 5px 10px;">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </div>
                                    <span style="padding: 0px; margin:10px 5px 5px 0px ; font-weight: bold;"
                                        class="col-md-12">Input Mail</span>
                                    <textarea class="contact_us_info" placeholder="Mail *" onkeyup="checkInputMail()"
                                        id="txt-mail-report"></textarea>
                                    <div class="row col-md-12" style="margin-top:8px; padding:0px;">
                                        <div class="check-all-station">
                                            <input type="checkbox" id="cb-all-report"><span
                                                style="margin-left: 5px;">All Station</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div id="menu1" class="tab-pane fade">
                                <div class="row" style="margin:30px 20px 20px 20px;">
                                    <div class="title-detail ">
                                        <span id="txt-alarm" style="font-size: 15px;"></span>
                                    </div>
                                    <div class="target" style=" margin: 10px 5px 5px 0px;">
                                        <span style="padding: 0px;font-weight: bold;">Alarm Target:</span>
                                        <input type="number"
                                            style="margin-left:8px;border: 0px solid #d7d7d7; border-bottom: 1px solid #d7d7d7;"
                                            id="txt-target">
                                    </div>
                                    <div class="form-group">
                                        <div class='input-group date' id='datetimepicker2'>
                                            <input type='datetime' class="form-control" placeholder="Select Report Time"
                                                id="txt-interval-time" />
                                            <span class="input-group-addon" style="padding: 5px 10px 5px 10px;">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </div>
                                    <span style="padding: 0px; margin:10px 5px 5px 0px ; font-weight: bold;"
                                        class="col-md-12">Input Mail</span>
                                    <textarea class="contact_us_info" placeholder="Mail *" onkeyup="checkInputMail()"
                                        id="txt-mail-alarm"></textarea>
                                    <div class="row col-md-12" style="margin-top:8px; padding:0px;">
                                        <div class="check-all-station">
                                            <input type="checkbox" id="cb-all-alarm"><span style="margin-left: 5px;">All
                                                Station</span>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-unactive"
                            style="position: absolute; width: 100%;height: 100%; top: 0px; left: 0px;"></div>
                    </div>
                </div>
                <div class="">
                    <input type="hidden" id="position-config" value="-1" id-row="">
                    <button class="button-clear" onclick="loadDataItem(null);" id="clear-view">Cancel</button>
                    <button class="button-save" onclick="saveReport()" id="save-report">Save</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>

    var dataset = {
        factory: '${factory}',
        modelName: "",
        groupName: "",
    }
    var dataModel = [];
    var dataGroup = [];
    var dataCpkConfig = [];
    var idConfig = 0;
    init();
    function init() {
        loadDataModel();
        loadDataGroup();
        activeView();
        $("#save-report").attr("disabled", true);
        $("#clear-view").attr("disabled", true);
    }
    function unactiveView() {
        $("#txt-time-report").attr("disabled", true);
        $(".tab-unactive").css("display", "block");
    }

    function activeView() {
        $("#txt-time-report").attr("disabled", false);
        $(".tab-unactive").css("display", "none");
    }

    function loadDataModel() {
        $.ajax({
            type: "GET",
            url: "/api/test/model",
            data: {
                factory: dataset.factory
            },
            contentType: "application/json; charset=utf-8",
            success: function (rsp) {
                dataModel = rsp;
                if (dataModel.length > 0) {
                    var htmlRow = "";
                    for (var i = 0; i < dataModel.length; i++) {
                        htmlRow += '<li row-index="' + i + '">' + dataModel[i].modelName + '</li>'
                    }
                    $("#list-model").html(htmlRow);
                    clickRowModel();
                }
            },
            faillure: function (error) {

            }
        });
    }
    function loadDataGroup() {
        $.ajax({
            type: "GET",
            url: "/api/test/group",
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function (rsp) {
                dataGroup = rsp;
                if (dataGroup.length > 0) {
                    var htmlRow = "";
                    for (var i = 0; i < dataGroup.length; i++) {
                        htmlRow += '<li row-index="' + i + '">' + dataGroup[i].groupName + '</li>'
                    }
                    $("#list-group").html(htmlRow);
                    clickRowGroup();
                }
            },
            faillure: function (error) {

            }
        });
    }

    function loadDataCpkConfig() {
        $.ajax({
            type: "GET",
            url: "/api/test/station/cpk-config",
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName,
                groupName: dataset.groupName
            },
            contentType: "application/json; charset=utf-8",
            success: function (rsp) {
                dataCpkConfig = rsp["data"];
                if (dataCpkConfig.length > 0) {
                    var htmlRow = "";
                    for (var i = 0; i < dataCpkConfig.length; i++) {
                        var templowSpec = " - ";
                        var temphighSpec = " - ";
                        var timeRport = "--:--";
                        var timeAlarm = "--:--";
                        if (dataCpkConfig[i].scheduleTime != null && dataCpkConfig[i].scheduleTime.length > 0) {
                            timeRport = dataCpkConfig[i].scheduleTime;
                        }

                        if (dataCpkConfig[i].alarmTimeInterval != null) {
                            timeAlarm = dataCpkConfig[i].alarmTimeInterval;
                        }

                        if (dataCpkConfig[i].lowSpec != null) {
                            templowSpec = dataCpkConfig[i].lowSpec
                        }
                        if (dataCpkConfig[i].highSpec != null) {
                            temphighSpec = dataCpkConfig[i].highSpec
                        }
                        htmlRow += '<tr row-index="' + i + '" id="row-' + i + '">' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + dataCpkConfig[i].parameters + '</td>' +
                            '<td>' + templowSpec + '</td>' +
                            '<td>' + temphighSpec + '</td>' +
                            '<td>' + timeRport + '</td>' +
                            '<td>' + timeAlarm + '</td>' +
                            '</tr>';

                    }
                    $("#tbl-cpkconfig tbody").html(htmlRow);
                    clickRowTableConfig();
                } else {
                    $("#tbl-cpkconfig tbody").html('<tr><td colspan="6">No data !</td></tr>');
                }
            },
            faillure: function (error) {

            }
        });
    }

    function checkInputMail() {

    }
    function saveReport() {

        var rowIndex = $("#position-config").val();
        var scheduleTime = $("#txt-time-report").val();
        var scheduleMailList = $("#txt-mail-report").val();
        var alarmTimeInterval = $("#txt-interval-time").val();
        var alarmMailList = $("#txt-mail-alarm").val();
        var alarmCpkTarget = $("#txt-target").val();
        var scheduleAllStation = $('#cb-all-report').is(":checked");
        var alarmAllStation = $('#cb-all-alarm').is(":checked");
        var parameters = dataCpkConfig[rowIndex].parameters;
        var lowSpec = dataCpkConfig[rowIndex].lowSpec;
        var highSpec = dataCpkConfig[rowIndex].highSpec;
        var alarmLastTime = dataCpkConfig[rowIndex].alarmLastTime;

        if (scheduleTime.length == 0) {
            scheduleTime = null;
        }

        if (alarmTimeInterval.length == 0) {
            alarmTimeInterval = null;
        }

        var dataReq = {
            "id": idConfig,
            "factory": dataset.factory,
            "modelName": dataset.modelName,
            "groupName": dataset.groupName,
            "parameters": parameters,
            "lowSpec": lowSpec,
            "highSpec": highSpec,
            "scheduleTime": scheduleTime,
            "scheduleMailList": scheduleMailList,
            "scheduleAllStation": scheduleAllStation,
            "alarmTimeInterval": alarmTimeInterval,
            "alarmCpkTarget": alarmCpkTarget,
            "alarmMailList": alarmMailList,
            "alarmLastTime": alarmLastTime,
            "alarmAllStation": alarmAllStation
        }
        $.ajax({
            type: 'POST',
            url: "/api/test/station/cpk-config",
            cache: false,
            data: JSON.stringify(dataReq),
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                // res = JSON.parse(res);
                var mess = res['message'];
                var code = res["code"];
                alert(mess);
                if (code == "SUCCESS") {
                    loadDataCpkConfig();
                    $("#save-report").attr("disabled", true);
                    $("#clear-view").attr("disabled", true);
                }

            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    function clickRowModel() {
        $("#list-model li").click(function () {
            var rowIndex = $(this).attr("row-index");
            dataset.modelName = dataModel[rowIndex].modelName;
            $("#txt-model").html(dataset.modelName);
            dataset.groupName = "";
            $("#txt-group").html("Select Station");
            $("#tbl-cpkconfig tbody").html("");
            loadDataGroup();
            unactiveView();
        })
    }

    function clickRowGroup() {
        $("#list-group li").click(function () {
            var rowIndex = $(this).attr("row-index");
            dataset.groupName = dataGroup[rowIndex].groupName;
            $("#txt-group").html(dataset.groupName);
            $("#tbl-cpkconfig tbody").html("");
            loadDataCpkConfig();
            unactiveView();
        })
    }

    function clickRowTableConfig() {
        $("#tbl-cpkconfig tbody tr").click(function () {
            var idV = $(this).attr("id");
            $("#position-config").attr("id-row", "#" + idV)
            loadDataItem("#" + idV);
        })
    }

    $(".tbl-detail-item tbody tr").click(function () {
        console.log("test-click");
    })

    $("#filter-model").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#list-model li").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $("#filter-group").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#list-group li").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    $("#txt-search-table").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#tbl-cpkconfig tbody tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    function loadDataItem(idView) {
        if (idView == null) {
            idView = $("#position-config").attr("id-row");
        }

        $('#tbl-cpkconfig tbody tr').removeClass("active");
        $(idView).addClass("active");
        activeView();
        $("#save-report").attr("disabled", false);
        $("#clear-view").attr("disabled", false);
        var rowIndex = $(idView).attr("row-index");
        $("#position-config").val(rowIndex);

        idConfig = dataCpkConfig[rowIndex].id;
        $("#txt-title-report").html(dataCpkConfig[rowIndex].parameters);
        $("#txt-mail-report").val(dataCpkConfig[rowIndex].scheduleMailList);
        $("#txt-target").val(dataCpkConfig[rowIndex].alarmCpkTarget);
        $("#txt-alarm").html(dataCpkConfig[rowIndex].parameters);
        var tempShecduleTime = dataCpkConfig[rowIndex].scheduleTime;
        var tempIntervalTime = dataCpkConfig[rowIndex].alarmTimeInterval;
        if (tempShecduleTime != null) {
            $("#txt-time-report").val(tempShecduleTime);
            $(function () {
                $('#datetimepicker1').datetimepicker({
                    format: 'HH:mm',
                    stepping: "30"
                }).data("DateTimePicker").date(tempShecduleTime);
            });
        }
        else {
            $("#txt-time-report").val("");
            $(function () {
                $('#datetimepicker1').datetimepicker({
                    format: 'HH:mm',
                    stepping: "30"
                }).data("DateTimePicker").date(new Date());
                $("#txt-time-report").val("");
            });

        }

        if (tempIntervalTime != null) {
            $("#txt-interval-time").val(tempIntervalTime);
            $(function () {
                $('#datetimepicker2').datetimepicker({
                    format: 'H',
                    stepping: "60"
                }).data("DateTimePicker").date(tempIntervalTime);
            });
        }
        else {
            $("#txt-interval-time").val("");
            $(function () {
                $('#datetimepicker2').datetimepicker({
                    format: 'H',
                    stepping: "60"
                }).data("DateTimePicker").date(new Date());
                $("#txt-interval-time").val("");
            });

        }
        $("#txt-mail-alarm").val(dataCpkConfig[rowIndex].alarmMailList);
        $('#cb-all-report').prop('checked', dataCpkConfig[rowIndex].scheduleAllStation);
        $('#cb-all-alarm').prop('checked', dataCpkConfig[rowIndex].alarmAllStation);

    }
</script>