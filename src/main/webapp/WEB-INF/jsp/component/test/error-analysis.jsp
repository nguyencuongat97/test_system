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
    }

    .load-list-data li:hover {
        background-color: #d7d7d7;
        cursor: pointer;
    }

    .load-list-data>li {
        border-bottom: 1px solid #e0e0e0;
        padding: 4px;
    }

    .load-list-data>li>a {
        color: #000 !important;
    }

    /* 
    .button:hover,
    .button:focus {
        -webkit-transform: translateY(-5px) scale(1.02);
        transform: translateY(-5px) scale(1.02);
        box-shadow: 0px 5px 12px rgba(126, 142, 177, 0.2);
        transition: all 0.4s;
    } */

    .btn-edit {
        margin: auto;
    }

    .btn-save {
        background: #319D5A;
        color: #fff;
        border: 1px solid #319D5A;
        margin: auto;

    }

    .btn-cancel {
        background: red;
        color: #fff;
        border: 1px solid red;
        margin: auto;

    }

    @media screen and (max-width: 1024px) {
        .btn-action {
            width: 100%;
            margin: auto;
        }

    }

    @media only screen and (min-width: 1024px) and (max-width: 1280px) {
        .btn-action {
            width: 100%;
            margin: auto;
        }
    }

    @media only screen and (min-width: 1280px) and (max-width: 1365px) {
        .btn-action {
            width: 100%;
            margin: auto;
        }
    }

    @media only screen and (min-width: 1366px) and (max-width: 1599px) {
        .btn-action {
            width: 70%;
            margin: auto;
        }
    }

    @media only screen and (min-width: 1600px) and (max-width: 1919px) {
        .btn-action {
            width: 50%;
            margin: auto;
        }
    }

    @media only screen and (min-width: 1920px) {
        .btn-action {
            width: 50%;
            margin: auto;
        }
    }

    .select-status {
        width: 100%;
    }

    .select-action {
        width: 100%;
    }

    .caret {
        font-style: normal;
        font-weight: normal;
        border: 0;
        margin: 0;
        width: auto;
        float: right;
        height: auto;
        text-align: center;
        margin-top: 3px;
    }

    .btn-select {
        color: #000;
        background: #fff;
        border: 1px solid #969090;
        width: 80%;
    }

    .btn {
        position: relative;
        font-weight: 500;
        text-transform: uppercase;
        padding: 6px 17px;
    }

    .btn-add {
        width: 10%;
        float: right;
        text-align: center;
        border: 1px solid #d7d7d7;
    }

    .w3-input {
        height: 30px;
        padding: 8px;
        display: block;
        border: none;
        border-bottom: 1px solid #ccc;
        width: 100%;
    }

    .w3-round-large {
        border-radius: 5px;
    }

    .w3-border {
        border: 1px solid #ccc !important;
        color: #0075FF;
    }

    #tbl-error thead tr:first-child th {
        position: sticky;
        top: 0;
    }

    #tbl-error tr th {
        color: #fff;
        padding: 0px 5px;
    }

    #tbl-error tr td {
        padding: 6px;
    }

    .txt-check input:disabled {
        cursor: not-allowed;
    }
</style>
<div class="row">

    <div class="col-lg-12" style="background: #fff;">
        <div class="panel panel-overview"
            style="margin:unset; background-color:#fff; color:#000; text-align:center; padding: 4px;">
            <span style="font-size:16px;">ERROR ANALYSIS</span>
        </div>
        <div class="row" style="margin: unset;">
            <div class="input-group"
                style="background-color: #d7d7d7; color: #000; height: 26px; margin: 5px 0px;padding: 3px;border-radius: 2px;">
                <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i
                        class="icon-calendar22"></i></span>
                <input type="text" class="form-control" name="timeSpan" style="height: 26px; color: inherit;"
                    id="input-time-span">
            </div>
        </div>
        <div class="row" style="margin: unset; margin-top: 5px;">
            <div class="dropdown dropbtn">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><span
                        id="txt-model">Select
                        Model</span>
                    <span class="caret"></span></button>
                <div class="dropdown-menu hight-dropdown" style="max-height:400px; overflow: scroll;">
                    <div class="bs-searchbox"><input type="text" class="form-control" autocomplete="off"
                            id="filter-model"></div>
                    <ul class="load-list-data" id="list-model">
                        <li><a href="#">No data</a></li>
                    </ul>
                </div>
            </div>
            <div class="dropdown dropbtn">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><span
                        id="txt-station">Select
                        Station</span>
                    <span class="caret"></span></button>
                <div class="dropdown-menu hight-dropdown" style="max-height:400px; overflow: scroll;">
                    <div class="bs-searchbox"><input type="text" class="form-control" autocomplete="off"
                            id="filter-station"></div>
                    <ul class="load-list-data" id="list-station">
                        <li><a href="#">No data</a></li>
                    </ul>
                </div>
            </div>
            <div class="dropdown dropbtn">
                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><span
                        id="txt-machine">Select
                        Machine</span>
                    <span class="caret"></span></button>
                <div class="dropdown-menu hight-dropdown" style="max-height:400px; overflow: scroll;">
                    <div class="bs-searchbox"><input type="text" class="form-control" autocomplete="off"
                            id="filter-machine"></div>
                    <ul class="load-list-data" id="list-machine">
                        <li><a href="#">No data</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row" style="margin: unset; margin-top: 10px;">
            <div style="height: 70vh; overflow: scroll;">
                <table class="table table-bordered tbl" style="text-align: center;" id="tbl-error">
                    <thead style="text-align: center;">
                        <th style="width: 2%;">#</th>
                        <th style="width: 12%;">Error</th>
                        <th style="width: 20%;">Error Description</th>
                        <th style="width: 12%;">RF TP</th>
                        <th style="width: 15%;">Status</th>
                        <th style="width: 15%;">Action</th>
                        <th style="width: 8%;">RC test</th>
                        <th style="width: 10%;">#</th>
                    </thead>
                    <tbody>
                        <tr>
                            <td colspan="8" style="text-align: center;"><i class="fa fa-inbox margin-right"></i>No
                                data!</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script>
    var dataset = {
        factory: 'B04',
        modelName: '',
        groupName: '',
        stationName: '',
        timeSpan: '',
        errorCode: '',
        rootCause: ''
    }
    var dataModel = [];
    var dataGroup = [];
    var dataMachine = [];
    var dataError = [];
    var dataTest = [];
    var dataRootcause = {};
    var dataAction = {};


    init();
    function init() {
        loadDataModel();
        loadDataGroup();
        loadDataMachine();
    }

    $('#input-time-span').daterangepicker({
        timePicker: true,
        //   timePickerIncrement: 30,
        locale: {
            format: 'YYYY/MM/DD hh:mm:ss'
        }
    });
    var start = moment().format("YYYY/MM/DD") + ' 07:29:59';
    var end = moment().add(1, "day").format("YYYY/MM/DD") + ' 07:29:59';
    $('#input-time-span').data('daterangepicker').setStartDate(new Date(start));
    $('#input-time-span').data('daterangepicker').setEndDate(new Date(end));

    function loadDataModel() {
        $.ajax({
            type: "GET",
            url: "/api/test/model-error",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                dataModel = res["data"];
                if (dataModel.length > 0) {
                    var htmlRow = "";
                    for (var i = 0; i < dataModel.length; i++) {
                        htmlRow += ' <li row-index ="' + i + '">' +
                            '<a href="#">' + dataModel[i] + '</a>' +
                            '</li>';
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
            url: "/api/test/group-error",
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                dataGroup = res["data"];
                if (dataGroup.length > 0) {
                    var htmlRow = "";
                    for (var i = 0; i < dataGroup.length; i++) {
                        htmlRow += ' <li row-index ="' + i + '">' +
                            '<a href="#">' + dataGroup[i] + '</a>' +
                            '</li>';
                    }
                    $("#list-station").html(htmlRow);
                    clickRowGroup();
                }

            },
            faillure: function (error) {

            }
        });
    }

    function loadDataMachine() {
        $.ajax({
            type: "GET",
            url: "/api/test/station-error",
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName,
                groupName: dataset.groupName
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var data = res['data'];
                if (data.length > 0) {
                    var htmlRow = "";
                    for (var i = 0; i < data.length; i++) {
                        htmlRow += ' <li>' + data[i] + '</li>';
                    }
                    $("#list-machine").html(htmlRow);
                    clickRowMachine();
                }

            },
            faillure: function (error) {

            }
        });
    }

    function loadDataError() {
        dataset.timeSpan = $("#input-time-span").val();
        $.ajax({
            type: "GET",
            url: "/api/test/error-analysis",
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName,
                groupName: dataset.groupName,
                stationName: dataset.stationName,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                dataError = res['data'];
                if (dataError.length > 0) {
                    var htmlRow = "";
                    for (var i = 0; i < dataError.length; i++) {
                        var errorDescription, rootCause, action;
                        errorDescription = rootCause = action = "";
                        errorDescription = dataError[i].errorDescription;

                        if (dataError[i]['solution'] != null) {
                            rootCause = dataError[i].solution.rootCause;
                            action = dataError[i].solution.action;
                        }

                        var htmlCheck = "";
                        if (dataError[i].runCorrelationTest) {
                            htmlCheck = " checked";
                        }

                        htmlRow += '<tr id="error-' + i + '" code="' + dataError[i].errorCode + '" id-data="' + dataError[i]['id'] + '">' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + dataError[i].errorCode + '</td>' +
                            '<td>' + errorDescription + '</td>' +
                            '<td>' + dataError[i].component + '</td>' +
                            '<td class="txt-rootcause"><span>' + rootCause + '</span><div class="dropdown dropbtn select-status"></div></td>' +
                            '<td class="txt-action"><span>' + action + '</span><div class="dropdown dropbtn select-action"></div></td>' +
                            '<td class="txt-check"><input type="checkbox" class="checked" ' + htmlCheck + '></td>' +
                            '<td>' +
                            '<div class="btn-action">' +
                            '<button class="button btn-edit" title="Edit" data-toggle="tooltip" view-parent="#error-' + i + '"><i class="fa fa-edit" style="font-size: 20px;"></i></button>' +
                            '<button style="float: left;" class="button btn-save" title="Save" data-toggle="tooltip" view-parent="#error-' + i + '"><i class="fa fa-check" style="font-size: 20px;"></i></button>' +
                            '<button style="float: right;" class="button btn-cancel" title="Cancel" data-toggle="tooltip" view-parent="#error-' + i + '"><i class="fa fa-times" style="font-size: 20px;"></i></button>' +
                            '<div>' +
                            '</td>' +
                            '</tr>';
                    }
                    $("#tbl-error tbody").html(htmlRow);
                    $(".txt-check input").attr("disabled", true);
                    disableView("");
                    actionRowError();
                    updateChecked();
                }

            },
            faillure: function (error) {

            }
        });
    }

    function actionRowError() {
        $(".btn-edit").click(function () {

            var viewParent = $(this).attr("view-parent");
            $(viewParent + " .txt-check input").attr("disabled", false);
            enableView(viewParent);
            var errorCode = $(viewParent).attr("code");
            loadDataRootcause(viewParent, errorCode);
        });
        $(".btn-save").click(function () {
            var viewParent = $(this).attr("view-parent");
            $(viewParent + " .txt-check input").attr("disabled", false);
            disableView(viewParent);
            saveError(viewParent);
        });

        $(".btn-cancel").click(function () {
            var viewParent = $(this).attr("view-parent");
            disableView(viewParent);
            $(viewParent + " .txt-check input").attr("disabled", true);

        });
    }

    function enableView(viewParent) {
        $(viewParent + " .btn-save").css("display", "block");
        $(viewParent + " .btn-cancel").css("display", "block");
        $(viewParent + " .btn-edit").css("display", "none");
    }

    function disableView(viewParent) {
        console.log(viewParent);
        $(viewParent + " .btn-save").css("display", "none");
        $(viewParent + " .btn-cancel").css("display", "none");
        $(viewParent + " .btn-edit").css("display", "block");
        $(viewParent + " .txt-input-status").css("display", "none");
        $(viewParent + " .txt-input-action").css("display", "none");
        $(viewParent + " .txt-rootcause span").css("display", "block");
        $(viewParent + " .txt-action span").css("display", "block");
    }

    function changeInputRootCause(viewParent) {
        var errorCode = $(viewParent).attr("code");
        var inputStatus = $(viewParent + " .txt-input-status").val();
        console.log("OBJ: " + inputStatus);
        loadDataAction(viewParent, errorCode, inputStatus);
    }

    function loadDataRootcause(viewParent, errorCode) {
        $.ajax({
            type: "GET",
            url: "/api/test/solution/root-cause",
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName,
                errorCode: errorCode,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                // dataRootcause = res["data"];
                dataRootcause[errorCode] = res['data'];
                var htmlRow = '<input class="w3-input w3-border w3-round-large btn-select dropdown-toggle txt-input-status" onchange="changeInputRootCause(\'' + viewParent + '\')" type="text" data-toggle="dropdown" placeholder="Select Status" >' +
                    '<div class="dropdown-menu hight-dropdown" style="max-height:400px; overflow: scroll;">' +
                    '<ul class="load-list-data list-root-cause">';
                if (dataRootcause[errorCode].length > 0) {
                    for (var i = 0; i < dataRootcause[errorCode].length; i++) {
                        htmlRow += '<li row-index="' + i + '" error-code="' + errorCode + '">' + dataRootcause[errorCode][i] + '</li>';
                    }
                }

                htmlRow += '</ul>';
                $(viewParent + " .select-status").html(htmlRow);
                var txtStatus = $(viewParent + " .txt-rootcause span").html();
                $(viewParent + " .txt-rootcause span").css("display", "none");
                $(viewParent + " .txt-input-status").val(txtStatus);
                clickRowStatus(viewParent);
                if (txtStatus.trim().length > 0) {
                    loadDataAction(viewParent, errorCode, txtStatus);
                }

            },
            faillure: function (error) {

            }
        });
    }


    function loadDataAction(viewParent, errorCode, rootCause) {
        $.ajax({
            type: "GET",
            url: "/api/test/solution/action",
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName,
                errorCode: errorCode,
                rootCause: rootCause,
                timeSpan: dataset.timeSpan

            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                dataAction[errorCode] = res["data"];
                var htmlRow = '<input class="w3-input w3-border w3-round-large btn-select dropdown-toggle txt-input-action" id-data="0" type="text" data-toggle="dropdown" placeholder="Select Action" >' +
                    '<div class="dropdown-menu hight-dropdown" style="max-height:400px; overflow: scroll;">' +
                    '<ul class="load-list-data list-action">';
                if (dataAction[errorCode].length > 0) {
                    for (var i = 0; i < dataAction[errorCode].length; i++) {
                        htmlRow += '<li error-code="' + errorCode + '">' + dataAction[errorCode][i]["action"] + '</li>';
                    }
                }
                htmlRow += '</ul>';
                $(viewParent + " .select-action").html(htmlRow);
                var txtAction = $(viewParent + " .txt-action span").html();
                $(viewParent + " .txt-action span").css("display", "none");
                $(viewParent + " .txt-input-action").val(txtAction);
                clickRowAction(viewParent);
            },
            faillure: function (error) {

            }
        });
    }

    function updateChecked(viewParent) {
        var rootCause = $(viewParent + " .txt-input-status").val();
        var action = $(viewParent + " .txt-input-action").val();
        var rcTest = $(viewParent + ' .checked').is(":checked");
        $(viewParent + " .txt-rootcause span").html(rootCause);
        $(viewParent + " .txt-action span").html(action);
        $(viewParent + " .check").prop("checked", rcTest);

    }

    function saveError(viewParent) {
        var rootCause = $(viewParent + " .txt-input-status").val();
        var action = "";

        if ($(viewParent + " .txt-input-action").length) {
            action = $(viewParent + " .txt-input-action").val().trim();
        } else {
            action = $(viewParent + " .txt-action span").html();
        }

        var rcTest = $(viewParent + ' .checked').is(":checked");
        var timeSpan = $("#input-time-span").val();
        var start = timeSpan.split("-")[0].trim();
        var end = timeSpan.split("-")[1].trim();
        var idError = $(viewParent).attr("id-data") * 1;
        start = start.replace("/", "-").replace("/", "-");
        end = end.replace("/", "-").replace("/", "-");

        var errorCode = $(viewParent).attr("code");
        var solution = {
            "rootCause": rootCause,
            "action": action
        };
        
        if (dataAction[errorCode] != null) {
            for (var i = 0; i < dataAction[errorCode].length; i++) {
                if (action == dataAction[errorCode][i]['action']) {
                    solution = { "id": dataAction[errorCode][i]['id'] };
                    break;
                }
            }
        }

        var dataReq = {
            "id": idError,
            "factory": "B04",
            "modelName": dataset.modelName,
            "groupName": dataset.groupName,
            "stationName": dataset.stationName,
            "startDate": start,
            "endDate": end,
            "errorCode": errorCode,
            "solution": solution,
            "runCorrelationTest": rcTest
        }

        if (rootCause.length == 0 || action.length == 0) {
            alert("Fail!");
            return;
        }

        $.ajax({
            type: 'POST',
            url: "/api/test/error-analysis",
            cache: false,
            data: JSON.stringify(dataReq),
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                // res = JSON.parse(res);
                var code = res['code'];
                var mess = res['message'];
                alert(mess);
                if (code == "SUCCESS") {
                    updateChecked(viewParent);
                }
            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    function clickRowStatus(viewParent) {
        $(viewParent + " .select-status ul li").click(function () {
            var value = $(this).html();
            $(viewParent + " .txt-input-status").val(value);
            changeInputRootCause(viewParent);
        })
    }

    function clickRowAction(viewParent) {
        $(viewParent + " .select-action ul li").click(function () {
            var value = $(this).html();
            $(viewParent + " .txt-input-action").val(value);
        })
    }

    function clickRowModel() {
        $("#list-model li").click(function () {
            var rowIndex = $(this).attr("row-index");
            dataset.modelName = dataModel[rowIndex]
            $("#txt-model").html(dataset.modelName);
            $("#txt-station").html("Select Station");
            $("#txt-machine").html("Select Machine");
            $("#tbl-error tbody").html("");
            dataset.groupName = "";
            dataset.stationName = "";
            loadDataGroup();
        });
    }

    function clickRowGroup() {
        $("#list-station li").click(function () {
            var rowIndex = $(this).attr("row-index");
            dataset.groupName = dataGroup[rowIndex];
            $("#txt-station").html(dataset.groupName);
            $("#txt-machine").html("Select Machine");
            $("#tbl-error tbody").html("");
            dataset.stationName = "";
            loadDataMachine();
        });
    }

    function clickRowMachine(viewParent) {
        $("#list-machine li").click(function () {
            var val = $(this).html();
            dataset.stationName = val
            $("#txt-machine").html(val);
            loadDataError();
        });
    }

    $("#filter-model").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#list-model li").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $("#filter-station").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#list-station li").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $("#filter-machine").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#list-machine li").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $("#input-time-span").change(function () {
        if (dataset.stationName.trim().length != 0) {
            loadDataError();
        }
    });
</script>