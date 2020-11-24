<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<link rel="stylesheet" href="/assets/css/custom/station-error.css">
<link rel="stylesheet" href="/assets/css/custom/style.css">
<script src="/assets/js/custom/jquery-ui.js"></script>
<!-- <div class="loader"></div> -->
<div class="panel panel-re panel-flat row" style="overflow: hidden;">
    <div class="col-lg-12 nav-tools">

    </div>
    <div>
        <button class="btn" id="btnSetup" onclick="showNavTools()"> <i class="fa fa-cog"></i> </button>
    </div>
    <div style="width: 100%; padding: 0; margin: 0;">
        <img id="imgLayout" src="/assets/images/custom/testLayout.png" />
    </div>
    <div id="view-content">
        <div id="testIssue" class=" btnView view-issue" style="top: 60px; left: 254px;" data-popup="popover" data-content="abc" data-container="body">8.39%</div id="test">
        <div id="testNormal" class=" btnView view-normal" style="top: 55px; left: 399px;" data-popup="popover" data-container="body">1.77%</div>
        <div id="testWarning" class=" btnView view-warning" style="top: 107px; left: 1369px;" data-popup="popover" data-container="body">5.18%</div>
        <div id="testIdle" class=" btnView view-idle" style="top: 98px; left: 586px;" data-popup="popover" data-container="body">66.65%</div>
    </div> 
</div>



<div class="panel-notify-detail">
    <button type="button" class="close" style="position: absolute;right: 10px;top: 10px;z-index: 1;" onclick="hiddenPanelRight();"><i class="icon-cross"></i></button>
    <%@ include file="trouble-and-history.jsp" %>
</div>

<%@ include file="modal/modal-guiding.jsp" %>

<%@ include file="modal/modal-search-solution.jsp" %>
<%@ include file="modal/modal-confirm-solution.jsp" %>
<%@ include file="modal/modal-add-new-solution.jsp" %>

<%@ include file="modal/modal-station-detail.jsp" %>
<%@ include file="modal/modal-history-action.jsp" %>

<script src="/assets/js/custom/notify-list.js"></script>
<script src="/assets/js/custom/troubleshooting.js"></script>

<style>
#imgLayout{
    margin: auto;
    width: 1366px;
    height: 525px;
}
.nav-tools{
    display: none;
    position: absolute;
    top: 2px;
    background-color: rgba(51, 51, 51, 0.7);
    min-height: 4rem;
    padding: 5px;
    z-index: 1;
}
.btnView{
    width: 6rem; 
    height: 3.5rem; 
    padding: 0.5rem 0; 
    text-align: center;
    border: 1px solid #000;
    position: absolute;
    border-radius: 0.7rem;
    font-size: 1.4rem;
    font-weight: bold;
    color: #272727;
    box-shadow: 0.5rem 0.5rem 2rem #272727;  
    cursor: pointer;
}
.view-issue{
    background-image: linear-gradient(#ff4456, #f59b9b);
}
.view-warning{
    background-image: linear-gradient(#ffe92b, #fff8e2);
}
.view-normal{
    background-image: linear-gradient(#53f75a, #ACC284);
}
.view-idle{
    background-image: linear-gradient(#9e9e9e, #E6E6E6);
}
#btnSetup{
    position: absolute; 
    top: 7px; 
    right: 5px;
    background: rgba(51, 51, 51, 0.5);
    padding: 5px 10px;
    z-index: 2;
}

#btnSetup:hover{
    background: #272727;
    color: #ffffff;
}
#btnSetup.active{
    background: #272727;
    color: #ffffff;
}

.btn-border{
    border: 1px solid; 
    padding: 0.4rem 0.8rem;
}

::-webkit-scrollbar {
    display: none;
}
.stt {
    height: 175px;
    margin-bottom: 0;
    min-width: 210px;
}

</style>

<script>
    var dataset = {};
    function randomX(){
        var r = Math.floor(Math.random() * 500) + 1;
        return r;
    }
    function randomY(){
        var r = Math.floor(Math.random() * 1300) + 1;
        return r;
    }
    var per;
    fixResponse();
    function fixResponse(){
        var scrWidth = $( document ).width();
        var t = scrWidth - 1366;
        var widthImg = 1366 + t;
        $('#imgLayout').css('width',widthImg);
        per = widthImg/1366;

        var heightImg = 525*per;
        $('#imgLayout').css('height',heightImg);
    }
    function showNavTools(){
        if($('.nav-tools').css('display') == 'block'){
            $('.nav-tools').css('display','none');
            $('#btnSetup').removeClass('active');
        } else{
            $('.nav-tools').css('display','block');
            $('#btnSetup').addClass('active');
        }
    }

// ******************** station-error.js ******************** //
loadStatus('B04','2020/01/09 07:30 - 2020/01/10 07:30');
function loadStatus(factory, timeSpan) {
    $.ajax({
        type: "GET",
        url: "/api/test/station/status2",
        data: {
            factory: factory,
            timeSpan: timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                var arrModelNames = [];
                var countHighchartsID = 0;

                countHighchartsID = 0;

                countHighchartsID = loadStationStatus(timeSpan, 'warning', data.WARNING.data, arrModelNames, countHighchartsID);

                // countHighchartsID = loadStationStatus(timeSpan, 'idle', data.IDLE.data, arrModelNames, countHighchartsID);

                // countHighchartsID = loadStationStatus(timeSpan, 'normal', data.NORMAL.data, arrModelNames, countHighchartsID);
                show();
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}

function loadStationStatus(timeSpan, type, dataLocked, arrModelNames, countHighchartsID) {
    if (!$.isEmptyObject(dataLocked)) {
        var stationLContent = '';
        var modelsHtml = '';
        var html = '';
        for(i = 0; i< dataLocked.length; i++){
            html += '<div id="stationNum' + i + '" class="btnView view-'+type+'" style="top: '+randomX()+'px; left: '+randomY()+'px;" data-popup="popover" data-container="body">' + dataLocked[i].retestRate.toFixed(2) + '%</div>';           
        }
        $('#view-content').append(html);
        for (i = 0; i < dataLocked.length; i++) {
            var timeLock = dataLocked[i].notifyTime;
            stationLContent = '<div class="card cldelsttlock countStation' + countHighchartsID + '" model="' + dataLocked[i].modelName + '" ' + type + '><input class="modelNameHide hide" type="radio" value="' + dataLocked[i].modelName + '">'
                + '<div class="panel stt" data-type="station" data-id="' + dataLocked[i].id + '" data-factory="' + dataLocked[i].factory + '" data-model-name="' + dataLocked[i].modelName + '" data-group-name="' + dataLocked[i].groupName + '" data-station-name="' + dataLocked[i].stationName + '" data-status="' + dataLocked[i].status + '">'
                + '<div class="pnlTitle">'
                + '<label class="stationName" style="margin: 0px; max-height: 50px;"><a onclick="showPanelDetail();stationClick(this.parentElement.parentElement.parentElement);"><b>' + dataLocked[i].stationName + '</b></a></label>'
                + (type == 'locked' ? ('<span class="lockType" title="Error times in row">' + (dataLocked[i].status == 'LOCKED A' || dataLocked[i].status == 'ISSUE A' ? 'A' : 'B') + '</span>') : '')
                + (timeLock != null ? ('<label class="timeLock">' + timeLock.slice(11, 19) + '</label>') : '')
                + '<label class="rated"><a style="color:#333" title="Detail" onclick="moveToDetailPage(this.parentElement.parentElement.parentElement)"><h3 class="text-semibold">' + dataLocked[i].retestRate.toFixed(2) + '%</h3></a></label>'
                + '<label class="pnlModel"><a style="color:#333" title="Model" ><h6 class="text-semibold">' + dataLocked[i].modelName + '(' + dataLocked[i].totalOutput + '/' + dataLocked[i].totalPlan + ')' + '</h6></a></label></div>'
                + '<div class="panel panel-flat panel-body" id="chart-content' + countHighchartsID + '" style="height: 80px; margin:0px; padding: 0px;"></div>'
                + '<div class="passwip">First Pass/Input: <span class="pull-right">' + ((dataLocked[i].wip - dataLocked[i].firstFail) < 0 ? 0 : (dataLocked[i].wip - dataLocked[i].firstFail)) + '/' + dataLocked[i].wip + '</span></div>'
                + '<div class="label_notify">Notify: <a class="pull-right ' + dataLocked[i].status + '" data-toggle="modal" data-target="#modal-history-action" onclick="notifyOnclick(this)">' + dataLocked[i].notifyShiftIndexes.length + '</a></div></div></div>';

            var idChart = 'chart-content' + countHighchartsID;

            countHighchartsID++;
            var requestData = {
                factory: dataLocked[i].factory,
                stationName: dataLocked[i].stationName,
                groupName: dataLocked[i].groupName,
                modelName: dataLocked[i].modelName,
                timeSpan: timeSpan
            };

            var colorChart = getColorChart(type, dataLocked[i].retestRate);
            showRate(idChart, requestData, colorChart.colorChart, colorChart.lineColorChart, dataLocked[i].notifyShiftIndexes);
            $('#stationNum' + i).attr('data-content',stationLContent);
        }       
        $('[data-popup=popover]').popover({
            template: '<div class="popover" style=" border: 1px solid #4e7af3; box-shadow: 1rem 1rem 2rem #1b1a1a;"><div class="arrow"></div><div class="popover-content" style="padding:0px"></div></div>',
            placement: 'auto right',
            trigger: 'hover click', 
            html: true
        });
    }
    return countHighchartsID;
}

function getColorChart(type, retestRate) {
    if (type == "idle") {
        return { colorChart: '#e0e0e0', lineColorChart: '#d3d3d3' };
    }
    if (retestRate >= 10) {
        return { colorChart: '#e6717c', lineColorChart: '#dc3545' };
    }
    else if (retestRate >= 3 && retestRate < 10) {
        return { colorChart: '#ffda6a', lineColorChart: '#ffca2b' };
    }
    else {
        return { colorChart: '#c4d4a8', lineColorChart: '#acc284' };
    }
}

function showByStatus(status) {
    if (dataset.status == status) {
        dataset.status = 'all';
    } else {
        dataset.status = status;
    }
    show();
}

function show() {
    if (dataset.selectedModel == 'all') {
        if (dataset.status == 'all') {
            $('.cldelsttlock').css("display", "block");
        } else {
            $('.cldelsttlock').css("display", "none");

            $('.cldelsttlock[' + dataset.status + ']').css("display", "block");
        }
    } else {
        if (dataset.status == 'all') {
            $('.cldelsttlock').css("display", "none");

            $('.cldelsttlock[model="' + dataset.selectedModel + '"]').css("display", "block");
        } else {
            $('.cldelsttlock').css("display", "none");

            $('.cldelsttlock[model="' + dataset.selectedModel + '"][' + dataset.status + ']').css("display", "block");
        }
    }
}

function showRate(idChart, requestData, colorChart, lineColorChart, notifyIndexes) {
    $.ajax({
        type: "GET",
        url: "/api/test/station/hourly",
        data: requestData,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                var categories = [];
                var dataChart = [];
                $.each(data, function (key, value) {
                    var timeRate = key;
                    categories.push(timeRate);
                    var dataRate;
                    if (value != null) {
                        dataRate = value.retestRate;
                    }
                    else {
                        dataRate = 0;
                    }
                    dataChart.push({ name: key, y: dataRate });
                });
                drawRChart(idChart, categories, dataChart, colorChart, lineColorChart, notifyIndexes);
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}

function drawRChart(idChart, categories, dataChart, colorChart, lineColorChart, notifyIndexes) {
    var plotLines = [];
    for (i in notifyIndexes) {
        plotLines[i] = {
            value: notifyIndexes[i],
            color: 'red',
            dashStyle: 'longdash',
            width: 1,
            zIndex: 10
        }
    }

    Highcharts.chart(idChart, {
        chart: {
            type: 'areaspline',
            margin: [0, 0, 0, 0],
            style: {
                fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
            }
        },
        title: {
            text: ''
        },
        xAxis: {
            labels: {
                enabled: false
            },
            gridLineWidth: 0,
            minorGridLineWidth: 0,
            tickLength: 0,
            tickWidth: 0,
            plotLines: plotLines,
        },
        yAxis: {
            visible: false
        },
        legend: {
            enabled: false
        },
        series: [{
            name: ' ',
            data: dataChart,
            lineColor: lineColorChart,
            color: colorChart,
            fillOpacity: 10,
            marker: {
                enabled: false
            },
            threshold: null
        }],
        tooltip: {
            pointFormat: '<span style="color:{series.color}">●</span> <b>{point.y:.2f}%</b>'
        },
        plotOptions: {
            series: {
                animation: false
            }
        },
        navigation: {
            buttonOptions: {
                enabled: false
            }
        },
        credits: {
            enabled: false
        }
    });
}

function stationClick(context) {
    dataset.factory = context.dataset.factory;
    dataset.modelName = context.dataset.modelName;
    dataset.groupName = context.dataset.groupName;
    dataset.stationName = context.dataset.stationName;

    loadModal(context.dataset.status);
}

function loadModal(status) {
    $('h5[name="station-name"]').html(dataset.stationName + " | " + dataset.groupName + " | " + dataset.modelName);
    if (status == "ISSUE A" || status == "ISSUE B" || status == "LOCKED A" || status == "LOCKED B") {
        $('.panel-heading').css("background-color", "#e6717c")
    } else if (status == "WARNING" || status == "ISSUE W") {
        $('.panel-heading').css("background-color", "#ffda6a")
    } else if (status == "IDLE") {
        $('.panel-heading').css("background-color", "#e6e6e6")
    } else {
        $('.panel-heading').css("background-color", "#fff")
    }

    $.ajax({
        type: "GET",
        url: "/api/test/station/detail",
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            groupName: dataset.groupName,
            stationName: dataset.stationName,
            timeSpan: dataset.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                $('label[name="retest-rate"]').html(data.retestRate.toFixed(2) + "%");
                $('label[name="first-pass-rate"]').html(data.firstPassRate.toFixed(2) + "%");
                $('label[name="hit-rate"]').html(data.hitRate.toFixed(2) + "%");
                $('.downtime').html(data.downTime);
                $('label[name="wip"]').html(data.wip);
                $('label[name="first-fail"]').html(data.firstFail);
                $('label[name="second-fail"]').html(data.secondFail);

                var html = '';
                if (!$.isEmptyObject(data.errorMetaMap)) {
                    $.each(data.errorMetaMap, function (key, value) {
                        html += '<label class="btn btn-sm bg-gray" data-popup="tooltip-custom" data-placement="bottom" title="' + value.errorDescription + '" data-tracking-id="' + (data.trackingId != null ? data.trackingId : '') + '" data-model-name="' + data.modelName + '" data-error-code="' + key + '" data-error-desc="' + value.errorDescription + '" onclick="showPanelRight();showTroubleAndHistory(this);" ><input type="radio" name="error-code">' + key + '</label>';
                    });
                }
                $('.btn-group[name="btn-group-error-code"]').html(html);

                var modalFooter = '<div class="row" align="center" data-type="station" data-id="' + data.id + '" data-factory="' + data.factory + '" data-model-name="' + data.modelName + '" data-group-name="' + data.groupName + '" data-station-name="' + data.stationName + '">'
                    + ' <button class="btn btnAction" data-href="10.224.76.108:554/Streaming/Channels/1" onclick="openVLC(this.dataset.href);">CAMVIEW</button> '
                    + ' <button class="btn btnAction" onclick="moveToDetailPage(this.parentElement)">DETAIL</button> '
                    + ' <button class="btn btnAction" data-href="' + (data.ip != null ? ('vnc://' + data.ip) : '#') + '" onclick="hrefWindow(this.dataset.href);">VNC</button></button>';
                $('.modalDetail').html(modalFooter);

                $('[data-popup=tooltip-custom]').tooltip({
                    template: '<div class="tooltip"><div><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div></div>'
                });
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}

function showTroubleAndHistory(context) {
    dataset.trackingId = context.dataset.trackingId;
    dataset.errorCode = context.dataset.errorCode;
    dataset.errorDesc = context.dataset.errorDesc;

    loadTroubleAndHistory();
}

function loadModelList() {
    clearTimeout(dataset.timeout['loadProgressStatus']);
    $.ajax({
        type: "GET",
        url: "/api/test/model/daily",
        data: {
            factory: dataset.factory,
            customerName: "",
            timeSpan: dataset.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            //console.log("model/daily:", data);
            if (!$.isEmptyObject(data)) {
                var html = '<label class="btn modelSort bg-gray" locked warning idle style="margin: 0px 1px 1px 0px;" >' +
                    '<input class="control-primary hidden" type="radio" name="model-name" value="all" > ALL' +
                    '</label>';

                var htmlTbl = '';

                var totalPlan = 0;
                var totalFail = 0;
                var totalRFail = 0;
                var totalFFail = 0;
                var totalRetest = 0;
                var totalInput = 0;
                var totalOutPut = 0;
                var totalWeeklyOutPut = 0;
                for (i in data) {
                    var value = '<div><b>Output/Plan: </b><span>' + data[i].output + '/' + data[i].plan + '</span></div>' +
                        '<div><b>First Pass Rate: </b><span>' + data[i].firstPassRate.toFixed(2) + '%</span></div>' +
                        '<div><b>Retest Rate: </b><span>' + data[i].retestRate.toFixed(2) + '%</span></div>';

                    for (j in data[i].groupList) {
                        value += '<div><b>Retest Rate - ' + data[i].groupList[j].groupName + ': </b><span>' + data[i].groupList[j].retestRate.toFixed(2) + ' %</span></div>';
                    }

                    html += '<label class="btn modelSort ' + convertStatus((100 - data[i].retestRate), 90, 97) + '" model="' + data[i].modelName + '" data-popup="popover" data-trigger="hover" data-placement="bottom" data-html="true" data-content="' + value + '" style="margin: 0px 1px 1px 0px;">' +
                        '<input class="control-primary hidden" type="radio" name="model-name" value="' + data[i].modelName + '">' + data[i].modelName +
                        //'<span class="badge bg-warning-400" style="position: absolute; top: -13px; right: 0; border-radius: 0px">'+data[i].retestRate.toFixed(2)+'</span>' +
                        '</label>';
                    var rtr = data[i].retestRateNew;
                    var fpr = data[i].firstPassRate;
                    var ffail = data[i].firstFail;
                    var sfail = data[i].secondFail;

                    //                    if (rtr > 10) {
                    //                        ffail = parseInt(data[i].inPutNew * 10 / 100) + sfail;
                    //                        rtr = (ffail - sfail) * 100.0 / data[i].inPutNew;
                    //                        fpr = (data[i].inPutNew - ffail) * 100.0 / data[i].inPutNew;
                    //                    }

                    if (rtr < 0) {
                        rtr = 0;
                        sfail = ffail;
                        fpr = (data[i].inPutNew - ffail) * 100.0 / data[i].inPutNew;
                    }
                    if (dataset.factory == 'B04') {
                        console.log("b04: ", dataset.factory)
                        htmlTbl += '<tr class="row-model" data-model="' + data[i].modelName + '">' +
                            '<td>' + (Number(i) + 1) + '</td>' +
                            '<td class="text-semibold" ><a class="model-name" data-model="' + data[i].modelName + '" >' + data[i].modelName + '</a></td>' +
                            '<td class="target" onclick=showModal("' + data[i].modelName + '") title="Click to Set Plan"><a class="model-plan" data-model="' + data[i].plan + '">' + data[i].plan + '</a></td>' +
                            '<td>' + data[i].inPutNew + '</td>' +
                            '<td>' + data[i].outPutNew + '</td>' +
                            '<td>' + data[i].outputWeekly + '</td>' +
                            '<td>' + data[i].hitRate.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus(data[i].eteNew, 95, 98) + '" >' + data[i].eteNew.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus(fpr, 90, 97) + '" >' + fpr.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus((100 - rtr), 90, 95) + '" >' + rtr.toFixed(2) + '%</td>' +
                            // '<td>' + data[i].retestNew + '</td>' +
                            // '<td>' + data[i].secondFail + '</td>' +
                            '<td>' + ffail + '</td>' +
                            '<td>' + sfail + '</td>' +
                            '</tr>';
                        totalPlan += data[i].plan;
                    } else if (dataset.factory == 'B06') {
                        htmlTbl += '<tr class="row-model" data-model="' + data[i].modelName + '">' +
                            '<td>' + (Number(i) + 1) + '</td>' +
                            '<td class="text-semibold" ><a class="model-name" data-model="' + data[i].modelName + '" >' + data[i].modelName + '</a></td>' +
                            '<td class="target" onclick=showModal("' + data[i].modelName + '") title="Click to Set Plan"><a class="model-plan" data-model="' + data[i].planMonth + '">' + data[i].planMonth + '</a></td>' +
                            '<td>' + data[i].inPutNew + '</td>' +
                            '<td>' + data[i].outPutNew + '</td>' +
                            '<td>' + data[i].outputWeekly + '</td>' +
                            '<td>' + data[i].hitRateMonth.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus(data[i].eteNew, 95, 98) + '" >' + data[i].eteNew.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus(fpr, 90, 97) + '" >' + fpr.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus((100 - rtr), 90, 95) + '" >' + rtr.toFixed(2) + '%</td>' +
                            // '<td>' + data[i].retestNew + '</td>' +

                            // '<td>' + data[i].secondFail + '</td>' +
                            '<td>' + ffail + '</td>' +
                            '<td>' + sfail + '</td>' +
                            '</tr>';
                        totalPlan += data[i].planMonth;
                    }

                    totalFail += data[i].secondFail;
                    //totalRFail += data[i].secondFail;
                    totalFFail += data[i].firstFail;
                    //totalRetest += data[i].retestNew;
                    totalInput += data[i].inPutNew;
                    totalOutPut += data[i].outPutNew;
                    totalWeeklyOutPut += data[i].outputWeekly;

                }
                var totalHitRate = ((totalWeeklyOutPut / totalPlan) * 100).toFixed(2);
                htmlTbl += '<tr>' +
                    '<td></td>' +
                    '<td class="text-semibold" >TOTAL</td>' +
                    '<td>' + totalPlan + '</td>' +
                    '<td>' + totalInput + '</td>' +
                    '<td>' + totalOutPut + '</td>' +
                    '<td>' + totalWeeklyOutPut + '</td>' +
                    '<td>' + totalHitRate + '%</td>' +
                    '<td></td>' +
                    '<td></td>' +
                    '<td></td>' +
                    '<td>' + totalFFail + '</td>' +
                    '<td>' + totalFail + '</td>' +
                    '</tr>';

                $('.mds').html(html);
                $('#tbl-model-status>tbody').html(htmlTbl);
                $('[data-popup=popover]').popover({
                    template: '<div class="popover" style="width: 250px; background-color: #e0e0e0; border: 1px solid #4e7af3; z-index-: 100"><div class="arrow"></div><div class="popover-content"></div></div>'
                });
                $('.model-name').on('click', function (event) {
                    var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + this.dataset.model;
                    openWindow(url);
                });
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
        complete: function () {
            dataset.timeout['loadModelList'] = setTimeout(loadModelList, 180000);
        }
    });
}
function changeStyle() {
    if (dataset.style != 'new-style') {
        dataset.style = 'new-style';
        $('#old-style').addClass("hidden");
        $('#new-style').removeClass("hidden");
    } else {
        dataset.style = 'old-style';
        $('#new-style').addClass("hidden");
        $('#old-style').removeClass("hidden");
    }
}
function showModal(model) {
    $("#mydiv").addClass("show");
    // $("#mydiv").draggable({ handle: "#mydivheader"});
    $.ajax({
        type: "GET",
        url: "/api/test/model/plan",
        data: {
            factory: dataset.factory,
            modelName: model
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log("data modal: ", data);
            if (dataset.factory == 'B04') {
                console.log("bo4 modal: ", dataset.factory)
                var html = '';
                if (data) {
                    html = '<tr id="row">'
                        + '<td><span class="hidden" id="idPlanConfirm">' + data.id + '</span><div id="txtEditModel" data-info="' + data.modelName + '">' + data.modelName + '</div></td>'
                        + '<td><input id="txtEditPlan" class="form-control hidden" name="plan" type="number" value="' + data.plan + '" /><span id="plan">' + data.plan + '</span></td>'
                        + '<td><input id="txtEditMO" class="form-control hidden" name="mo" type="text" value="' + data.mo + '" /><span id="mo">' + data.mo + '</span></td>'
                        + '<td><div id="txtEditShift">' + data.shift + '</div></td>'
                        + '<td><div id="txtEditShiftTime">' + data.shiftTime + '</div></td>'
                        + '<td>'
                        + '<button class="btn btn-warning" id="editRow" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit()">'
                        + '<i class="icon icon-pencil"></i>'
                        + '</button>'
                        + '<button class="btn btn-success hidden" id="confirmRow" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel(\'' + data.modelName + '\')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button><button class="btn btn-danger hidden" id="cancelRow" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit()">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    $("#tblTarget tbody").html(html);

                } else {
                    html = '<tr id="rowAdd">'
                        + '<td><div id="txtEditModelNameAdd">' + model + '</div></td>'
                        + '<td><input id="txtAddPlan" class="form-control" name="plan" type="number"/></td>'
                        + '<td><input id="txtAddMO" class="form-control" name="mo" type="text"/></td>'
                        + '<td><div id="txtEditShift" name="shift" type="text" value=""></div></td>'
                        + '<td><div id="txtEditShiftTime" name="shiftTime" type="text" value=""></div></td>'
                        + '<td>'
                        + '<button class="btn btn-success " id="addPlan" title="Add" style="padding: 4px 10px;" onclick="AddNew(\'' + model + '\')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button>'
                        + '<button class="btn btn-danger " id="cancelAdd" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="closeModal()">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    $("#tblTarget tbody").html(html);

                }
            } else if (dataset.factory == 'B06') {
                console.log("bo6 modal: ", dataset.factory)
                var html = '';
                if (data) {
                    html = '<tr id="row">'
                        + '<td><span class="hidden" id="idPlanConfirm">' + data.id + '</span><div id="txtEditModel" data-info="' + data.modelName + '">' + data.modelName + '</div></td>'
                        + '<td><input id="txtEditPlan" class="form-control hidden" name="plan" type="number" value="' + data.planMonth + '" /><span id="plan">' + data.planMonth + '</span></td>'
                        + '<td><input id="txtEditMO" class="form-control hidden" name="mo" type="text" value="' + data.mo + '" /><span id="mo">' + data.mo + '</span></td>'
                        + '<td><div id="txtEditShift">' + data.shift + '</div></td>'
                        + '<td><div id="txtEditShiftTime">' + data.shiftTime + '</div></td>'
                        + '<td>'
                        + '<button class="btn btn-warning" id="editRow" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit()">'
                        + '<i class="icon icon-pencil"></i>'
                        + '</button>'
                        + '<button class="btn btn-success hidden" id="confirmRow" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel(\'' + data.modelName + '\')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button><button class="btn btn-danger hidden" id="cancelRow" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit()">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    $("#tblTarget tbody").html(html);

                } else {
                    html = '<tr id="rowAdd">'
                        + '<td><div id="txtEditModelNameAdd">' + model + '</div></td>'
                        + '<td><input id="txtAddPlan" class="form-control" name="plan" type="number"/></td>'
                        + '<td><input id="txtAddMO" class="form-control" name="mo" type="text"/></td>'
                        + '<td><div id="txtEditShift" name="shift" type="text" value=""></div></td>'
                        + '<td><div id="txtEditShiftTime" name="shiftTime" type="text" value=""></div></td>'
                        + '<td>'
                        + '<button class="btn btn-success " id="addPlan" title="Add" style="padding: 4px 10px;" onclick="AddNew(\'' + model + '\')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button>'
                        + '<button class="btn btn-danger " id="cancelAdd" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="closeModal()">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    $("#tblTarget tbody").html(html);

                }

            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
//event 
function edit() {
    $("#editRow").addClass("hidden");
    $("#confirmRow").removeClass("hidden");
    $("#cancelRow").removeClass("hidden");

    $("#mo").addClass("hidden");
    $("#txtEditMO").removeClass("hidden");

    $("#plan").addClass("hidden");
    $("#txtEditPlan").removeClass("hidden");

    $(".btn-warning").attr("disabled", "disabled");
}
function cancelEdit() {
    $("#editRow").removeClass("hidden");
    $("#confirmRow").addClass("hidden");
    $("#cancelRow").addClass("hidden");

    $("#mo").removeClass("hidden");
    $("#txtEditMO").addClass("hidden");

    $("#plan").removeClass("hidden");
    $("#txtEditPlan").addClass("hidden");

    $(".btn-warning").removeAttr("disabled");
}
function confirmModel(modelName) {
    editPlan = Number($('#txtEditPlan').val());
    editMO = $('#txtEditMO').val();
    model = $('#txtEditModel').val();
    var idPlanConfirm = Number($('#idPlanConfirm').html());
    data = {
        factory: dataset.factory,
        modelName: modelName,
        plan: editPlan,
        mo: editMO,
        id: idPlanConfirm
    }
    $.ajax({
        type: 'POST',
        url: '/api/test/model/saveplan',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response) {
                showModal(modelName);
                loadModelList();
            }
            else {
                alert("Set Plan Fail!! Please Try Again!");
                showModal(modelName);
            }


        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
    // }
}
function AddNew(modelName) {
    var addPlan = Number($('#txtAddPlan').val());
    var addMO = $('#txtAddMO').val();
    var data = {
        factory: dataset.factory,
        modelName: modelName,
        plan: addPlan,
        mo: addMO,
        id: 0,
    }
    $.ajax({
        type: 'POST',
        url: '/api/test/model/saveplan',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response) {
                showModal(modelName);
                loadModelList();
            }
            else {
                alert("Set Plan Fail!! Please Try Again!");
                showModal(modelName);
            }

        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
    // }

}
function closeModal() {
    $("#mydiv").removeClass("show");
}

// ******************** end station-error.js ******************** //
















//     testSetPop();
//     function testSetPop(){
//         $('[data-popup=popover]').popover({
//             template: '<div class="popover" style=" border: 1px solid #4e7af3; box-shadow: 1rem 1rem 2rem #1b1a1a;"><div class="arrow"></div><div class="popover-content" style="padding:0px"></div></div>',
//             placement: 'auto right',
//             trigger: 'hover click', 
//             html: true
//         });
//         var html = '<div><img src="/assets/images/custom/testPopup.png" /></div>';
//         $('#testWarning').attr("data-content",html);
//     }


// loadStationStatus();
// function loadStationStatus() {
//     var timeLock = "2020-01-09 14:55:44";
//     var stationLContent = '<div class="card cldelsttlock countStation0" model="25-1103" warning style="display: block; margin-left: 1px;"><input class="modelNameHide hide" type="radio" value="25-1103">'
//                 + '<div class="panel stt" data-type="station" data-id="5110331" data-factory="B04" data-model-name="25-1103" data-group-name="PPT" data-station-name="10.224.83.188" data-status="WARNING">'
//                 + '<div class="pnlTitle">'
//                 + '<label class="stationName" style="margin: 0px; max-height: 50px;"><a onclick="showPanelDetail();stationClick(this.parentElement.parentElement.parentElement);"><b>10.224.83.188</b></a></label>'
//                 + ('warning' == 'locked' ? ('<span class="lockType" title="Error times in row">' + ('WARNING' == 'LOCKED A' || 'WARNING' == 'ISSUE A' ? 'A' : 'B') + '</span>') : '')
//                 + (timeLock != null ? ('<label class="timeLock">' + timeLock.slice(11, 19) + '</label>') : '')
//                 + '<label class="rated"><a style="color:#333" title="Detail" onclick="moveToDetailPage(this.parentElement.parentElement.parentElement)"><h3 class="text-semibold">3.21%</h3></a></label>'
//                 + '<label class="pnlModel"><a style="color:#333" title="Model" ><h6 class="text-semibold">25-1103(1276/6000)</h6></a></label></div>'
//                 + '<div class="panel panel-flat panel-body" id="chart-content0" style="height: 80px; margin:0px; padding: 0px;"></div>'
//                 + '<div class="passwip">First Pass/Input: <span class="pull-right">' + ((1276 - 50) < 0 ? 0 : (1276 - 50)) + '/' + 1276 + '</span></div>'
//                 + '<div class="label_notify">Notify: <a class="pull-right WARNING" data-toggle="modal" data-target="#modal-history-action" onclick="notifyOnclick(this)">3</a></div></div></div>';
//     $('#testIdle').attr("data-content",stationLContent); 
//     showRate();
// }
// function showRate() {
//     $.ajax({
//         type: "GET",
//         url: "/api/test/station/hourly?factory=B04&stationName=10.224.83.188&groupName=PPT&modelName=25-1103&timeSpan=2020%2F01%2F09+07%3A30+-+2020%2F01%2F09+23%3A59",
//         contentType: "application/json; charset=utf-8",
//         success: function (data) {
//             if (!$.isEmptyObject(data)) {
//                 var categories = [];
//                 var dataChart = [];
//                 $.each(data, function (key, value) {
//                     var timeRate = key;
//                     categories.push(timeRate);
//                     var dataRate;
//                     if (value != null) {
//                         dataRate = value.retestRate;
//                     }
//                     else {
//                         dataRate = 0;
//                     }
//                     dataChart.push({ name: key, y: dataRate });
//                 });
//                 drawRChart(categories, dataChart);
//             }
//         },
//         failure: function (errMsg) {
//             console.log(errMsg);
//         }
//     });
// }

// function drawRChart(categories, dataChart) {
//     Highcharts.chart('chart-content0', {
//         chart: {
//             type: 'areaspline',
//             margin: [0, 0, 0, 0],
//             style: {
//                 fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
//             }
//         },
//         title: {
//             text: ''
//         },
//         xAxis: {
//             labels: {
//                 enabled: false
//             },
//             gridLineWidth: 0,
//             minorGridLineWidth: 0,
//             tickLength: 0,
//             tickWidth: 0,
//         },
//         yAxis: {
//             visible: false
//         },
//         legend: {
//             enabled: false
//         },
//         series: [{
//             name: ' ',
//             data: dataChart,
//             lineColor: '#dc3545',
//             color: '#e6717c',
//             fillOpacity: 10,
//             marker: {
//                 enabled: false
//             },
//             threshold: null
//         }],
//         tooltip: {
//             pointFormat: '<span style="color:{series.color}">●</span> <b>{point.y:.2f}%</b>'
//         },
//         plotOptions: {
//             series: {
//                 animation: false
//             }
//         },
//         navigation: {
//             buttonOptions: {
//                 enabled: false
//             }
//         },
//         credits: {
//             enabled: false
//         }
//     });
// }

</script>
