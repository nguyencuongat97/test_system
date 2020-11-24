<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<!-- <link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" /> -->
<link rel="stylesheet" href="/assets/css/custom/station-dashboard.css" />
<div class="loader"></div>
<div class="row">
    <div class="panel panel-overview" style="margin:unset; background-color:#272727; color:#fff; text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
        <span style="font-size:14px;"><span class="text-uppercase" name="factory">${factory}</span> - TESTER E.T.E DETAIL MANAGEMENT</span>
    </div>
    <div class="col-sm-11">
        <div class="input-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        </div>
    </div>
    <div class="col-sm-1">
        <div style="width: 90%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;background:#ddd; color: #333;" onclick="exportActionHistories()"><i class="fa fa-download"></i></button>
        </div>
    </div>
    <div class="col-xs-12 table-responsive pre-scrollable" id="old-style" style="margin: 10px 0px; max-height: calc(100vh - 165px);">
        <table class="table table-xxs table-bordered" id="tbl-model-status" style="background-color: #fff; font-size: 16px; font-weight: 400;">
            <thead style="background-color: #545b62; color: #fff">
                <tr class="thead">
                    <th>No</th>
                    <th>Model Name</th>
                    <th>Group Name</th>
                    <th>Wip</th>
                    <th>OutPut</th>
                    <th class="typeETE">YieldRate</th>
                    <th>Retest Rate</th>
                    <th>F.Fail</th>
                    <th>Fail</th>
                    <th style="text-align: center;" colspan="4">Error</th>
                </tr>
            </thead>
            <tbody class="thbody">
            </tbody>
        </table>
    </div>
    <button id="btnBackToDashboard" class="btn btn-sm" title="Back to Dashboard"><i class="fa fa-arrow-left"></i></button>
</div>
<!-- Modal -->
<div class="modal fade" id="modalSetup" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content" style="background-color: #333; color: #ccc;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
            </div>
            <div class="modal-body">
                <div class="row" style="margin-bottom: 0.5rem;">
                    <div class="col-sm-11">
                        <div class="panel panel-overview" style="margin:unset; padding: 3px;background-color:#575757; color:#fff; text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
                            <span style="font-weight:bold">STATION DETAIL</span>
                        </div>
                    </div>
                    <div class="col-sm-1">
                        <button id="btnReport" class="btn btn-xs" style="width: 100%;background:#ddd; color: #333;">Report</button>
                    </div>
                </div>
                <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px);">
                    <table id="tblModelMeta" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px; color: #fff;">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Model Name</th>
                                <th>Group Name</th>
                                <th>Station Name</th>
                                <th>Wip</th>
                                <th>OutPut</th>
                                <th>F.P.R</th>
                                <th>Retest Rate</th>
                                <th>YieldRate</th>
                                <th>F.Fail</th>
                                <th>Fail</th>
                                <th style="text-align: center;" colspan="4">Error</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="modalError" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content" style="background-color: #333; color: #ccc; margin: 3%; margin-top: 4%;">
            <div class="modal-header">
                <button type="button" class="close" id="close3" data-dismiss="modal" style="margin-top: -10px;"><i class="icon icon-cross" style="color: #ccc"></i></button>
                <div class="modal-title">
                    <select class=" bootstrap-select selecthtmlMN3 selectMN" name="modelName3" id="idSelectMN" data-width:="auto"></select>
                    <select class="bootstrap-select selecthtmlGN3 selectGN" name="groupName3" id="idSelectGN" data-width:="auto"></select>
                    <select class="bootstrap-select selecthtmlSN3 selectSN" name="stationName3" data-width:="auto" id="idSelectSN"></select>
                </div>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px); margin-top: -10px;">
                    <table id="tblModelErrorMGS" class="table table-bordered table-xxs table-sticky" style="font-size: 12px; margin-bottom: 10px; color: #fff;">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Model Name</th>
                                <th>Error Code</th>
                                <th>Group Name </th>
                                <th>Station Name</th>
                                <th>Description</th>
                                <th>Intput</th>
                                <th>Output</th>
                                <th>Test F.Qty</th>
                                <th>F.Qty</th>
                                <th>F.Rate</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- MODAL ERROR TOP GROUP-->
<div class="modal fade" id="modalErrorGroup" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content" style="background-color: #333; color: #ccc;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
                <div class="modal-title">
                    <select class=" bootstrap-select selecthtmlMN2 selectMN" name="modelName2" id="idGroup1" data-width:="auto"></select>
                    <select class="bootstrap-select selecthtmlGN2 selectGN" name="groupName2" id="idGroup2" data-width:="auto"></select>
                    <select class="bootstrap-select selecthtmlSN2 selectSN" name="stationName2" data-width:="auto" id="idGroup3"></select>
                </div>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px);">
                    <table id="tblModelErrorGroup" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px; color: #fff;">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Model Name</th>
                                <th>Error Code</th>
                                <th>Group Name </th>
                                <th>Station Name</th>
                                <th>Description</th>
                                <th>Intput</th>
                                <th>Output</th>
                                <th>Test F.Qty</th>
                                <th>F.Qty</th>
                                <th>F.Rate</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- MODAL ERROR CODE GROUP -->
<div class="modal fade" id="modalErrorCodeGroup" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content" style="background-color: #333;">
            <div class="modal-header">
                <button type="button" id="close-error-2" class="close" data-dismiss="modal" style="margin-top: -10px;"><i class="icon icon-cross" style="color: #ccc"></i></button>
                <div class="modal-title">
                    <span style="font-weight:bold">STATION ERROR CODE</span>
                </div>
            </div>
            <div class="modal-body" style="padding: 0 20px 10px 20px;">
                <div class="row no-margin no-padding">
                    <div class="col-sm-5 col-xs-12 no-padding" id="chart-defected-issue" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <div class="col-sm-7 col-xs-12 no-padding" id="chart-ntf-issue" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                </div>
                <div class="row no-margin no-padding">
                    <div class="col-xs-12 table-responsive pre-scrollable" style="padding: 0; margin: 10px 0px; max-height: 190px;">
                        <table class="table table-xxs table-bordered table-sticky text-nowrap" id="tbl-top-3-error" style="background-color: #fff; font-weight: 400;">
                            <thead style="background-color: #545b62; color: #fff">
                                <tr class="thead">
                                    <th style="max-width: 45px;">No</th>
                                    <th style="max-width: 14%;">Error</th>
                                    <th style="max-width: 27%;">Root Cause</th>
                                    <th style="max-width: 26%;">Action</th>
                                    <th style="max-width: 17%;">Description</th>
                                    <th>Input</th>
                                    <th>Pass</th>
                                    <th style="padding: 0px 5px;">Test F.Qty</th>
                                    <th style="padding: 0px 11px;">F.Qty</th>
                                    <th>F.Rate</th>
                                    <th style="max-width: 11%; z-index: 1">Event</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    #btnBackToDashboard {
        padding: 3px 10px;
        position: fixed;
        top: 51px;
        left: 13px;
        background: rgba(255,255,255,0.5);
    }
    #btnBackToDashboard:hover{
        background: rgba(255,255,255,1);
    }
    .text-semibold {
        font-weight: 550;
    }

    .errortop {
        width: 6%;
    }

    .btn-group button {
        margin-right: 10px;
        border-radius: 5px !important;
        border: 1px solid #ccc;
        color: #333;
        padding: 5px !important;
    }

    .styletextshad {
        color: #333;
        text-shadow: 2px 1px 2px #756d6d;
    }

    .selectMN {
        background: #fff;
        color: #333 !important;
        width: 13% !important;
        border-radius: 3px;
    }

    .selectGN {
        background: #fff;
        color: #333 !important;
        width: 12% !important;
        border-radius: 3px;
    }

    .selectSN {
        background: #fff;
        color: #333 !important;
        width: 12% !important;
        border-radius: 3px;
    }

    .table-sticky thead th {
        border-top: none !important;
        border-bottom: none !important;
        box-shadow: inset 0 1px 1px #fff, inset 0 -1px 0 #fff;
    }

    #tblModelMeta thead th,
    #tblModelErrorGroup thead th,
    #tbl-top-3-error thead th {
        background-color: #545b62;
        position: sticky !important;
        text-transform: uppercase !important;
        padding: 5px 5px !important;
    }

    #tblModelErrorMGS thead th,
    #tblModelErrorGroup thead th,
    #tblModelErrorStation thead th {
        background-color: #424242;
        text-transform: uppercase;
        position: sticky !important;
        text-transform: uppercase !important;
        padding: 5px 5px;
    }

    #tblModelMeta tbody {
        background: #fff;
        color: #333;
        font-size: 13px;
    }

    #tblModelErrorGroup tbody {
        background: #fff;
        color: #333;
    }

    #tblModelErrorMGS tbody,
    #tblModelErrorGroup tbody,
    #tblModelErrorStation tbody {
        background: #fff;
        color: #333;
    }

    #tbl-model-status thead th {
        background-color: #545b62 !important;
        color: #fff;
        position: sticky !important;
        text-transform: uppercase !important;
        padding: 3px !important;
    }

    .table-xxs>tbody>tr>td,
    .table-xxs>tfoot>tr>td {
        padding: 3px !important;
    }

    .class-group-name {
        color: #333;
    }

    .caret:after {
        padding-right: 5px;
    }

    .selectMN .btn span {
        padding-left: 5px;
    }

    .selectGN .btn span {
        padding-left: 5px;
    }

    .selectSN .btn span {
        padding-left: 5px;
    }

    .selectMN .btn span.bs-caret .caret {
        padding-left: 5px;
        margin-right: 5px;
    }

    .selectGN .btn span.bs-caret .caret {
        padding-left: 5px;
        margin-right: 5px;
    }

    .selectSN .btn span.bs-caret .caret {
        padding-left: 5px;
        margin-right: 5px;
    }

    .selectSN .btn span.bs-caret .caret {
        margin-right: 5px;
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
</style>
<script>
    var dataset = {
        factory: '${factory}',
        modelName: '${modelName}',
    };

    var dataGlo = {};
    var dataGroup = {};

    function loadModelList() {
        $('.loader').css('display', 'block');
        $.ajax({
            type: "GET",
            url: "/api/test/group/total",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                modelName: dataset.modelName,
                includeStation: false,
                mode: 'PE'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var html = '';
                var number = 0;
                for (var i = 0; i < data.length; i++) {
                    dataGlo.groupName = data[i].groupName;
                    var rtr = data[i].retestRate;
                    var fpr = data[i].firstPassRate;
                    var ffail = data[i].firstFail;
                    var sfail = data[i].secondFail;
                    if (rtr < 0) {
                        rtr = 0;
                        sfail = ffail;
                        fpr = (data[i].inPutNew - ffail) * 100.0 / data[i].inPutNew;
                    }
                    number++;
                    html += '<tr><td>' + number + '</td>' +
                        '<td>' + data[i].modelName + '</td>' +
                        '<td class="text-semibold" title="Click to move Station Detail"><a class="group-name-detail" onclick=getGroup("' + data[i].groupName + '") data-model="' + data[i].groupName + '" >' + data[i].groupName + '</a></td>' +
                        '<td>' + data[i].wip + '</td>' +
                        '<td>' + data[i].pass + '</td>' +
                        '<td class=" styletextshad  ' + convertStatus(data[i].yieldRate, 95, 98) + '" data-toggle="modal" data-target="#modalSetup" onclick=showModal("' + data[i].groupName + '") title="Click to show Station"><a class="class-group-name">' + data[i].yieldRate.toFixed(2) + '%</a></td>' +
                        '<td class=" styletextshad  ' + convertRetestRate(data[i].retestRate, 10, 3) + '" data-toggle="modal" data-target="#modalSetup" onclick=showModal("' + data[i].groupName + '") title="Click to show Station"><a class="class-group-name">' + data[i].retestRate.toFixed(2) + '%</a></td>' +
                        '<td>' + data[i].firstFail + '</td>'+
                        '<td>' + data[i].secondFail + '</td>';

                    var valueTop3 = data[i].errorMetaMap;
                    var keys = Object.keys(valueTop3);
                    for (let k = 0; k < 3; k++) {
                        if (keys.length == 3) {
                            var subString = valueTop3[keys[k]].errorCode.length;
                            var str1 = valueTop3[keys[k]].errorCode.replace(/\r\n|\r|\n/g, "");
                            if (subString > 7) {
                                var str = str1.slice(0, 7) + '...';
                                html += '<td class="text-semibold errortop" title="' + str1 + '\n'+valueTop3[keys[k]].errorDescription+'"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + str1 + '\')">' + str + '</a></td>';
                            } else {
                                html += '<td class="text-semibold errortop" title="' + str1 + '\n'+valueTop3[keys[k]].errorDescription+'"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + str1 + '\')">' + str1 + '</a></td>';
                            }
                        } else {
                            if (keys[k] == undefined) {
                                html += '<td class="text-semibold errortop"></td>';
                            } else {
                                var subString = valueTop3[keys[k]].errorCode.length;
                                var str1 = valueTop3[keys[k]].errorCode.replace(/\r\n|\r|\n/g, "");
                                if (subString > 7) {
                                    var str = str1.slice(0, 7) + '...';
                                    html += '<td class="text-semibold errortop" title="' + str1 + '\n'+valueTop3[keys[k]].errorDescription+'"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + str1 + '\')">' + str + '</a></td>';
                                } else {
                                    html += '<td class="text-semibold errortop" title="' + str1 + '\n'+valueTop3[keys[k]].errorDescription+'"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + str1 + '\')">' + str1 + '</a></td>';
                                }
                            }
                        }
                    }
                    if (Object.keys(valueTop3).length == 3) {
                        html += '<td class="text-semibold errortop"><a class="class-error" data-toggle="modal" data-target="#modalErrorGroup" onclick=showModalErrorDashboard("' + data[i].modelName + '","' + data[i].groupName + '") title="Click to show Station Error Group">OTHER</a></td></tr>';
                    } else {
                        html += '<td class="text-semibold errortop"></td></tr>';
                    }
                }
                $(".thbody").html(html);
                $('.model-name').on('click', function(event) {
                    var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName + "&timeSpan=" + dataset.timeSpan;
                    openWindow(url);
                });

            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                $('.loader').css('display', 'none');
            }
        });
    }

    function getGroup(group) {
        var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName + "&groupName=" + group + "&timeSpan=" + dataset.timeSpan;
        openWindow(url);
    }

    function showModal(groupName) {
        $("#modalSetup").css('opacity', '1');
        $('#btnReport').on('click',function(){
            var url = "/report-rtr-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName + "&groupName=" + groupName + "&timeSpan=" + dataset.timeSpan;
            openWindow(url);
        });
        $('.loader').css('display', 'block');
        $.ajax({
            type: "GET",
            url: "/api/test/station/total",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                modelName: dataset.modelName,
                groupName: groupName,
                includeStation: false,
                mode: 'PE'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var html = '';
                var number = 0;
                for (var i = 0; i < data.length; i++) {
                    dataStation = data[i].stationName;
                    var rtr = data[i].retestRate;
                    var fpr = data[i].firstPassRate;
                    var ffail = data[i].firstFail;
                    var sfail = data[i].secondFail;
                    if (rtr < 0) {
                        rtr = 0;
                        sfail = ffail;
                        fpr = (data[i].inPutNew - ffail) * 100.0 / data[i].inPutNew;
                    }
                    number++;
                    html += '<tr id="row"><td>' + number + '</td>' +
                        '<td>' + data[i].modelName + '</td>' +
                        '<td>' + data[i].groupName + '</td>' +
                        '<td class="text-semibold" title="Click to move Station Detail"><a class="station-name" onclick=getStationName("' + data[i].groupName + '","' + data[i].stationName + '") data-model="' + data[i].stationName + '" >' + data[i].stationName + '</a></td>' +
                        '<td>' + data[i].wip + '</td>' +
                        '<td>' + data[i].pass + '</td>' +
                        '<td class="' + convertStatus(fpr, 90, 97) + '">' + fpr.toFixed(2) + '%</td>' +
                        '<td class="' + convertStatus((100 - rtr), 90, 95) + '">' + rtr.toFixed(2) + '%</td>' +
                        '<td class="' + convertStatus(data[i].yieldRate, 90, 97) + '">' + data[i].yieldRate.toFixed(2) + '%</td>' +
                        '<td>' + data[i].firstFail + '</td>' +
                        '<td>' + data[i].secondFail + '</td>';

                        var valueTop3 = data[i].errorMetaMap;
                    var keys = Object.keys(valueTop3);
                    for (let k = 0; k < 3; k++) {
                        if (keys.length == 3) {
                            var subString = valueTop3[keys[k]].errorCode.length;
                            var str1 = valueTop3[keys[k]].errorCode.replace(/\r\n|\r|\n/g, "");
                            if (subString > 7) {
                                var str = str1.slice(0, 7) + '...';
                                html += '<td class="text-semibold errortop" title="' + str1 + '\n'+valueTop3[keys[k]].errorDescription+'"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + str1 + '\')">' + str + '</a></td>';
                            } else {
                                html += '<td class="text-semibold errortop" title="' + str1 + '\n'+valueTop3[keys[k]].errorDescription+'"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + str1 + '\')">' + str1 + '</a></td>';
                            }
                        } else {
                            if (keys[k] == undefined) {
                                html += '<td class="text-semibold errortop"></td>';
                            } else {
                                var subString = valueTop3[keys[k]].errorCode.length;
                                var str1 = valueTop3[keys[k]].errorCode.replace(/\r\n|\r|\n/g, "");
                                if (subString > 7) {
                                    var str = str1.slice(0, 7) + '...';
                                    html += '<td class="text-semibold errortop" title="' + str1 + '\n'+valueTop3[keys[k]].errorDescription+'"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + str1 + '\')">' + str + '</a></td>';
                                } else {
                                    html += '<td class="text-semibold errortop" title="' + str1 + '\n'+valueTop3[keys[k]].errorDescription+'"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + str1 + '\')">' + str1 + '</a></td>';
                                }
                            }
                        }
                    }
                    if (Object.keys(valueTop3).length == 3) {
                        html += '<td class="text-semibold errortop"><a class="class-error" data-toggle="modal" data-target="#modalErrorGroup" onclick=showModalErrorDashboard("' + data[i].modelName + '","' + data[i].groupName + '") title="Click to show Station Error Group">OTHER</a></td></tr>';
                    } else {
                        html += '<td class="text-semibold errortop"></td></tr>';
                    }
                }
                $("#tblModelMeta tbody").html(html);
                $('.model-name-station').on('click', function(event) {
                    var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName;
                    openWindow(url);
                });
                $('.group-name-station').on('click', function(event) {
                    var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName + "&groupName=" + groupName;
                    openWindow(url);
                });
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                $('.loader').css('display', 'none');
            }
        });
    }

    function getStationName(groupName, stationName) {
        var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName + "&groupName=" + groupName + "&stationName=" + stationName;
        openWindow(url);
    }

    function showModalErrorDashboard(modelNameError2, groupName2) {
        dataGroup.modelName = modelNameError2;
        dataGroup.groupName = groupName2;
        dataGroup.stationName = '';
        functionSelectvalueMNGroup();
        loadTableGroup();
    }

    function loadTableGroup() {
        $('.loader').css('display', 'block');
        $.ajax({
            type: "GET",
            url: "/api/test/station/error/detail",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                modelName: dataGroup.modelName,
                groupName: dataGroup.groupName,
                stationName: dataGroup.stationName,
                mode: 'PE'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var html = '';
                var number = 0;
                for (i in data) {
                    var valueDescription = data[i].errorDescription;
                    var valueStationName = data[i].stationName;
                    if (valueDescription == null) {
                        valueDescription = '';
                    }
                    if (valueStationName == null) {
                        valueStationName = '';
                    }
                    var valuefail;
                    if (data[i].wip == 0 || data[i].wip == null) {
                        valuefail = 0;
                    } else {
                        valuefail = data[i].fail * 100 / data[i].wip;
                    }
                    number++;
                    html += '<tr id="row"><td>' + number + '</td>' +
                        '<td>' + data[i].modelName + '</td>' +
                        '<td class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\'' + data[i].modelName + '\',\'' + data[i].groupName + '\',\'' + data[i].errorCode + '\')" title="Click to show Error Code"><a class="class-error-code" >' + data[i].errorCode + '</a></td>' +
                        '<td>' + data[i].groupName + '</td>' +
                        '<td>' + valueStationName + '</td>' +
                        '<td>' + valueDescription + '</td>' +
                        '<td>' + data[i].wip + '</td>' +
                        '<td>' + data[i].pass + '</td>' +
                        '<td>' + data[i].testFail + '</td>' +
                        '<td>' + data[i].fail + '</td>' +
                        '<td>' + valuefail.toFixed(2) + ' %</td>' +
                        '</tr>';
                }

                $("#tblModelErrorGroup tbody").html(html);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                $('.loader').css('display', 'none');
            }
        });
    }

    function functionSelectvalueMNGroup() {
        functionSelectvalueMNGroup1();
        functionSelectvalueMNGroup2();
        functionSelectvalueMNGroup3();
    }

    function functionSelectvalueMNGroup1() {
        $.ajax({
            type: "GET",
            url: "/api/test/model",
            data: {
                factory: dataset.factory,
                parameter: dataset.parameter,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var arrayModel = [];
                var html = '<option value="" disabled="" selected>Select Model</option>';
                var selector = $('#idGroup1');
                selector.children('option').remove();
                for (i in data) {
                    if (arrayModel.indexOf(data[i].modelName) == -1) {
                        arrayModel.push(data[i].modelName);
                    }
                }
                for (j in arrayModel) {
                    if (arrayModel[j] == dataset.modelName) {
                        html += '<option class="classvalueMN2" value="' + arrayModel[j] + '" selected>' + arrayModel[j] + '</option>';
                    } else {
                        html += '<option class="classvalueMN2" value="' + arrayModel[j] + '">' + arrayModel[j] + '</option>';
                    }
                }
                $('#idGroup1').html(html);
                selector.selectpicker('refresh');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function functionSelectvalueMNGroup2() {
        $.ajax({
            type: "GET",
            url: "/api/test/group",
            data: {
                factory: dataset.factory,
                modelName: dataGroup.modelName,
                parameter: dataset.parameter,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var arrayGroup = [];
                var selector = $('#idGroup2');
                var html = '<option value="">All</option>';
                selector.children('option').remove();
                if (dataGroup.groupName == '') {
                    $('.selecthtmlSN2').addClass('hidden');
                } else {
                    $('.selecthtmlSN2').removeClass('hidden');
                }
                for (i in data) {
                    if (arrayGroup.indexOf(data[i].groupName) == -1) {
                        arrayGroup.push(data[i].groupName);
                    }
                }
                for (j in arrayGroup) {
                    if (arrayGroup[j] == dataGroup.groupName) {
                        html += '<option class="classvalueGN" value="' + arrayGroup[j] + '" selected>' + arrayGroup[j] + '</option>';
                    } else {
                        html += '<option class="classvalueGN" value="' + arrayGroup[j] + '">' + arrayGroup[j] + '</option>';
                    }
                }
                $('#idGroup2').html(html);
                selector.selectpicker('refresh');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function functionSelectvalueMNGroup3() {
        $.ajax({
            type: "GET",
            url: "/api/test/station",
            data: {
                factory: dataset.factory,
                modelName: dataGroup.modelName,
                groupName: dataGroup.groupName,
                parameter: dataset.parameter,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var arrayStation = [];
                var selector = $('#idGroup3');
                var html = '<option value="">All</option>';
                selector.children('option').remove();
                for (i in data) {
                    if ((arrayStation.indexOf(data[i].stationName) == -1) && data[i].stationName != null) {
                        arrayStation.push(data[i].stationName);
                    }
                }
                for (j in arrayStation) {
                    if (arrayStation[j] == dataGroup.stationName) {
                        html += '<option class="classvalueSN" value="' + arrayStation[j] + '" selected>' + arrayStation[j] + '</option>';
                    } else {
                        html += '<option class="classvalueSN" value="' + arrayStation[j] + '">' + arrayStation[j] + '</option>';
                    }
                }
                $('#idGroup3').html(html);
                selector.selectpicker('refresh');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }
    $('select[name=modelName2]').on('change', function() {
        dataGroup.modelName = this.value;
        dataGroup.groupName = "";
        dataGroup.stationName = "";
        functionSelectvalueMNGroup2();
        functionSelectvalueMNGroup3();
        loadTableGroup();
    });
    $('select[name=groupName2]').on('change', function() {
        dataGroup.groupName = this.value;
        if ($('#idGroup2').val() == '' || $('#idGroup2').val() == null) {
            $('.selecthtmlSN2').addClass('hidden');
            dataGroup.groupName = "";
            dataGroup.stationName = "";
            functionSelectvalueMNGroup3();
            loadTableGroup();
        } else {
            $('.selecthtmlSN2').removeClass('hidden');
            dataGroup.groupName = this.value;
            dataGroup.stationName = "";
            functionSelectvalueMNGroup3();
            loadTableGroup();
        }
    });
    $('select[name=stationName2]').on('change', function() {
        dataGroup.stationName = this.value;
        if ($('#idGroup3').val() == '') {
            dataGroup.stationName = '';
            loadTableGroup();
        } else {
            dataGroup.stationName = this.value;
            loadTableGroup();
        }
    });

    function closeModal() {
        $("#mydiv").removeClass("show");
    }
    $("#close3").click(function() {
        $("#modalSetup").css('opacity', '1');
    });
    $("#close-error-2").click(function() {
        $("#modalErrorGroup").css('opacity', '1');
        $("#modalSetup").css('opacity', '1');
    });
    $("#close-error-3").click(function() {
        $("#modalSetup").css('opacity', '1');
    });
    $("#modalErrorCodeGroup").on('hide.bs.modal', function() {
        $("#modalErrorGroup").css('opacity', '1');
        $("#modalSetup").css('opacity', '1');
    });

    function showModalErrorCode(model2, group2, errorCode) {
        $("#modalErrorGroup").css('opacity', '0.5');
        $("#modalSetup").css('opacity', '0.5');
        $('.loader').css('display', 'block');
        $.ajax({
            type: 'GET',
            url: '/api/test/error/defectedIssue',
            data: {
                factory: dataset.factory,
                modelName: model2,
                errorCode: errorCode,
                mode: 'PE'
            },
            success: function(data) {
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var categories = new Array(keys.length);
                for (i in keys) {
                    categories[i] = keys[i];
                    dataChart[i] = {
                        name: keys[i],
                        y: data[keys[i]]
                    }
                }

                Highcharts.chart('chart-defected-issue', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: errorCode + ' DEFECTED ISSUE'
                    },
                    xAxis: {
                        categories: categories,
                        type: 'category',
                        labels: {
                            rotation: -30,
                            align: 'right',
                            autoRotation: false,
                            style: {
                                fontSize: '11px'
                            },
                        }
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                    },
                    legend: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    plotOptions: {
                        column: {
                            stacking: 'normal',
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart,
                        marker: {
                            enabled: false
                        },
                    }]
                });
                $('.loader').css('display', 'none');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });

        $.ajax({
            type: 'GET',
            url: '/api/test/station/ntfIssue',
            data: {
                factory: dataset.factory,
                modelName: model2,
                errorCode: errorCode,
                mode: 'PE'
            },
            success: function(data) {
                $('.loader').css('display', 'block');
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var categories = new Array(keys.length);
                for (i in keys) {
                    categories[i] = keys[i];
                    dataChart[i] = {
                        name: keys[i],
                        y: data[keys[i]]
                    }
                }

                Highcharts.chart('chart-ntf-issue', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: (errorCode + ' NTF ISSUE BY TESTER')
                    },
                    xAxis: {
                        categories: categories,
                        labels: {
                            rotation: -30,
                            align: 'right',
                            style: {
                                fontSize: '11px'
                            }
                        },
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                    },
                    legend: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    plotOptions: {
                        column: {
                            stacking: 'normal',
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
                    }]
                });
                $('.loader').css('display', 'none');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });

        $.ajax({
            type: "GET",
            url: "/api/test/station/errorDetail",
            data: {
                factory: dataset.factory,
                modelName: model2,
                groupName: group2,
                timeSpan: dataset.timeSpan,
                mode: 'PE'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $('.loader').css('display', 'block');
                loadDataTableTop3ErrorFromJson(data);
                $('.loader').css('display', 'none');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function loadDataTableTop3ErrorFromJson(json) {
        $('#tbl-top-3-error>tbody>tr').remove();
        var stt = 0;
        var body = '';
        var tmpRC, tmpA, tmpD;
        if (!$.isEmptyObject(json)) {
            var keys = Object.keys(json);
            for (i in keys) {
                tmpRC = convertText(json[keys[i]].rootCause);
                if (json[keys[i]].factory == 'B06') {
                    tmpD = convertText(json[keys[i]].description);
                } else {
                    tmpD = convertText(json[keys[i]].errorDescription);
                }
                tmpA = convertText(json[keys[i]].action);

                stt++;
                body += '<tr id="row' + stt + '"><td>' + stt + '</td>' +
                    '<td class="valueError' + stt + '"><span>' + json[keys[i]].errorCode + '</span></td>' +
                    '<td><span class="hidden" id="idNote' + stt + '">' + json[keys[i]].idNoteError + '</span><textarea id="txtEditRC' + stt + '" class="form-control hidden" name="" type="text" value="">' + tmpRC + '</textarea><span id="rootCause' + stt + '">' + tmpRC + '</span></td>' +
                    '<td><textarea id="txtEditAction' + stt + '" class="form-control hidden" name="" type="text" value="">' + tmpA + '</textarea><span id="action' + stt + '">' + tmpA + '</span></td>' +
                    '<td><textarea id="txtEditD' + stt + '" class="form-control hidden" name="" type="text" value="">' + tmpD + '</textarea><span id="errorDescription' + stt + '">' + tmpD + '</span></td>' +
                    '<td>' + json[keys[i]].wip + '</td><td>' + json[keys[i]].pass + '</td><td>' + json[keys[i]].testFail + '</td><td>' + json[keys[i]].fail + '</td><td>' + (json[keys[i]].fail * 100 / json[keys[i]].wip).toFixed(2) + ' %</td>' +
                    '<td>' +
                    '<button class="btn btn-warning" id="editRow' + stt + '" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit(' + stt + ')"><i class="icon icon-pencil"></i></button>' +
                    '<button class="btn btn-success hidden" id="confirmRow' + stt + '" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel(' + stt + ')"><i class="icon icon-checkmark"></i></button>' +
                    '<button class="btn btn-danger hidden" id="cancelRow' + stt + '" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit(' + stt + ')"><i class="icon icon-spinner11"></i></button>' +
                    '</td></tr>';
            }
            $('#tbl-top-3-error>tbody').html(body);
        } else {
            $('#tbl-top-3-error>tbody').html('<tr><td colspan="11" align="center">-- NO ERROR --</td></tr>');
        }
    }
    //event
    function convertText(str) {
        var value = '';
        if (str != null) {
            value = str.replace(/<br\s*\/?>/g, '\n');
        }
        return value;
    }

    function edit(stt) {
        $("#editRow" + stt).addClass("hidden");
        $("#confirmRow" + stt).removeClass("hidden");
        $("#cancelRow" + stt).removeClass("hidden");

        $("#rootCause" + stt).addClass("hidden");
        $("#txtEditRC" + stt).removeClass("hidden");

        $("#action" + stt).addClass("hidden");
        $("#txtEditAction" + stt).removeClass("hidden");

        $("#errorDescription" + stt).addClass("hidden");
        $("#txtEditD" + stt).removeClass("hidden");

        $(".btn-warning").attr("disabled", "disabled");
    }

    function confirmModel(stt) {
        var errorC = $('.valueError' + stt + ' a').html();
        var id = Number($('#idNote' + stt).html());
        var editRC = $('#txtEditRC' + stt).val().replace(/\r\n|\r|\n/g, "<br />");
        var editD = $('#txtEditD' + stt).val().replace(/\r\n|\r|\n/g, "<br />");
        var editA = $('#txtEditAction' + stt).val().replace(/\r\n|\r|\n/g, "<br />");

        var data = new FormData();
        data.append('factory', dataset.factory);
        data.append('modelName', dataset.modelName);
        data.append('timeSpan', dataset.timeSpan);
        data.append('action', editA);
        data.append('rootCause', editRC);
        data.append('error', errorC);
        data.append('id', id);
        if (dataset.factory == 'B06') {
            data.append('description', editD);
        } else {
            data.append('errorDescription', editD);
        }

        $.ajax({
            type: 'POST',
            url: '/api/test/station/noteError',
            data: data,
            processData: false,
            contentType: false,
            mimeType: "multipart/form-data",
            success: function(response) {
                if (response) {
                    $("#editRow" + stt).removeClass("hidden");
                    $("#confirmRow" + stt).addClass("hidden");
                    $("#cancelRow" + stt).addClass("hidden");

                    $("#rootCause" + stt).html(editRC);
                    $("#action" + stt).html(editA);
                    $("#errorDescription" + stt).html(editD);

                    $('#action' + stt).removeClass("hidden");
                    $('#rootCause' + stt).removeClass("hidden");
                    $('#errorDescription' + stt).removeClass("hidden");

                    $("#txtEditRC" + stt).addClass("hidden");
                    $("#txtEditAction" + stt).addClass("hidden");
                    $("#txtEditD" + stt).addClass("hidden");

                    $(".btn-warning").removeAttr("disabled");
                } else {
                    alert('FAIL!!');
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function cancelEdit(stt) {
        $("#editRow" + stt).removeClass("hidden");
        $("#confirmRow" + stt).addClass("hidden");
        $("#cancelRow" + stt).addClass("hidden");

        $("#rootCause" + stt).removeClass("hidden");
        $("#txtEditRC" + stt).addClass("hidden");

        $("#action" + stt).removeClass("hidden");
        $("#txtEditAction" + stt).addClass("hidden");

        $("#errorDescription" + stt).removeClass("hidden");
        $("#txtEditD" + stt).addClass("hidden");

        $(".btn-warning").removeAttr("disabled");
    }
    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
                dataset.timeSpan = startDate + ' - ' + endDate;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    function searchParams(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if (results == null){
            return null;
        }
        else {
            return decodeURIComponent(results[1]) || 0;
        }
    }
    function getParameter(){
        var urlString = window.location.href;
        var timeSpan = searchParams("timeSpan");
      
        if(timeSpan == undefined || timeSpan == null){
            getTimeNow();
        }
        else{
            var time = timeSpan.split(' - ');
            $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(time[0]));
            $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(time[1]));
            dataset.timeSpan = timeSpan ;
            loadModelList();
        }
    }
    $(document).ready(function() {
        getParameter();
        $('input[name=timeSpan]').on('change', function() {
            dataset.timeSpan = this.value;
            loadModelList();
        });

        $('#btnBackToDashboard').on('click', function() {
            window.location.href = "/station-dashboard?factory="+dataset.factory;
        });
    });
</script>