<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/pareto.js"></script>
<style>
    body{
        font-family: Arial, Helvetica, sans-serif;
    }

    #header{
        text-align: center;
        font-size: 18px;
        font-weight: bold;
        margin-top: 1px;
        margin-bottom: 5px;
    }
    .chart-sm{
        background-color: #FFFFFF;
    }
    #tblDetail thead th {
        background: #efefef;
        font-weight: bold;
        text-align: center;
        border: 1px solid #cccccc;
        padding: 5px !important;
    }

    #tblDetail tbody td{
        border: 1px solid #cccccc;
        background-color: #ffffff;
        padding: 3px 10px;
    }

    .progress-bar {
        background-color: #F1923E;
        box-shadow: 1px 1px #ffffff;
    }
    ::-webkit-scrollbar-thumb {
        border-radius: 5px;
        background: #999999;
    }
</style>


<div class="panel panel-re panel-flat row no-margin">
    <div class="col-lg-12" style="background-color: #F5EAAB;">
        <div class="panel panel-overview" id="header">
            <span>PE Daily Report</span>
        </div>
        <div class="row no-margin">
            <div class="panel panel-overview input-group" style="margin-bottom: 5px; float: left;">
                <span class="input-group-addon" style="padding: 0px 5px; color: #333333;"><i class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 30px;">
            </div>
        </div>
        <div class="row no-margin">
            <div id="chartModelName" class="col-xs-12 col-sm-4 chart-sm" style="margin: 10px 0px;"></div>
            <div id="chartErrorCode" class="col-xs-12 col-sm-8 chart-sm" style="margin: 10px 0px;"></div>
            <!-- <div class="col-xs-12 col-sm-8 chart-sm scrolling-wrapper-flexbox" style="margin: 10px 0px; overflow-x: scroll; white-space: nowrap;">
                <div style="height: 100%; width: 3690px;">
                    <div id="chartErrorCode" style="height: 100%; width: 100%;"></div>
                </div>
            </div> -->
        </div>
        <div class="row no-margin">
            <div id="chartReasonCode" class="col-xs-12 col-sm-6 chart-sm" style="margin: 10px 0px;"></div>
            <div id="chartLocationCode" class="col-xs-12 col-sm-6 chart-sm" style="margin: 10px 0px;"></div>
        </div>
        <div class="row mt-10">
            <div class="col-sm-12">
                <table id="tblDetail" class="table table-hover">
                    <thead>
                        <tr>
                            <th>Model Name</th>
                            <th>Error Code</th>
                            <th>Reason Code</th>
                            <th>Location Code</th>
                            <th style="width: 30%; z-index: 10">QTY</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <!-- <div class="export pull-right" style="margin: 10px 10px 0px 0px;">
                <a class="btn btn-lg" id="btnExport"
                    style="font-size: 13px; padding: 5px; border-radius: 10px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div> -->
        </div>
    </div>
</div>
<script>
    var dataset = {
        factory: '${factory}'
    }

    function init() {
        loadModelName();
        loadErrorCode();
        loadReasonCode();
        loadLocationCode();

        loadTable();
    }

    function loadErrorCode() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_pe_count_model_name",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                parameter: 'TEST_CODE'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var dataChart = [], categories = [];
                var scroll = 50;
                if(!$.isEmptyObject(data)){
                    var dem = 0;
                    var colorIndex = 0;
                    var colorChart = ['#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92'];
                    for(i in data){
                        if(i != 'N/A'){
                            if(colorIndex == 9){
                                colorIndex = 0;
                            }
                            categories.push(i);
                            dataChart.push({y:data[i], color: colorChart[colorIndex]});
                            colorIndex++;
                            dem++;
                        }
                    }
                    scroll *= dem;
                }
                drawParetoScroll(dataChart, categories, scroll, 'chartErrorCode', 'Error Code');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadModelName() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_pe_count_model_name",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                parameter: 'MODEL_NAME'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var dataChart = [], categories = [];
                var scroll = 50;
                if(!$.isEmptyObject(data)){
                    var dem = 0;
                    var colorIndex = 0;
                    var colorChart = ['#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92'];
                    for(i in data){
                        if(i != 'N/A'){
                            if(colorIndex == 9){
                                colorIndex = 0;
                            }
                            categories.push(i);
                            dataChart.push({y:data[i], color: colorChart[colorIndex]});
                            colorIndex++;
                            dem++;
                        }
                    }
                    scroll *= dem;
                }
                drawParetoScroll(dataChart, categories, scroll, 'chartModelName', 'Model Name');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadReasonCode() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_pe_count_model_name",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                parameter: 'REASON_CODE'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var dataChart = [], categories = [];
                var scroll = 50;
                if(!$.isEmptyObject(data)){
                    var dem = 0;
                    var colorIndex = 0;
                    var colorChart = ['#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92'];
                    for(i in data){
                        if(i != 'N/A'){
                            if(colorIndex == 9){
                                colorIndex = 0;
                            }
                            categories.push(i);
                            dataChart.push({y:data[i], color: colorChart[colorIndex]});
                            colorIndex++;
                            dem++;
                        }
                    }
                    scroll *= dem;
                }
                drawParetoScroll(dataChart, categories, scroll, 'chartReasonCode', 'Reason Code');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadLocationCode() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_pe_count_model_name",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                parameter: 'LOCATION_CODE'
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var dataChart = [], categories = [];
                var scroll = 50;
                if(!$.isEmptyObject(data)){
                    var dem = 0;
                    var colorIndex = 0;
                    var colorChart = ['#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92'];
                    for(i in data){
                        if(i != 'N/A'){
                            if(colorIndex == 9){
                                colorIndex = 0;
                            }
                            categories.push(i);
                            dataChart.push({y:data[i], color: colorChart[colorIndex]});
                            colorIndex++;
                            dem++;
                        }
                    }
                    scroll *= dem;
                }
                drawParetoScroll(dataChart, categories, scroll, 'chartLocationCode', 'Location Code');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function drawParetoScroll(dataChart, categories, scroll, idChart, title) {
        Highcharts.chart(idChart, {
            chart: {
                type: 'column',
                scrollablePlotArea: {
                    minWidth: scroll,
                    scrollPositionX: 0
                },
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: title,
                style: {
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            tooltip: {
                shared: true
            },
            xAxis: {
                categories: categories,
                allowDecimals: false,
                crosshair: true,
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '11px',
                        color: '#000000'
                    }
                }
            },
            yAxis: [{
                title: {
                    text: ''
                }
            }, {
                title: {
                    text: ''
                },
                minPadding: 0,
                maxPadding: 0,
                max: 100,
                min: 0,
                opposite: true,
                labels: {
                    format: "{value:.2f}%"
                }
            }],
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
            series: [{
                type: 'pareto',
                name: ' ',
                yAxis: 1,
                zIndex: 10,
                baseSeries: 1,
                color: Highcharts.getOptions().colors[3],
                tooltip: {
                    valueDecimals: 2,
                    valueSuffix: '%'
                }
            }, {
                name: ' ',
                type: 'column',
                zIndex: 2,
                // color: '#1F77B4',
                data: dataChart
            }]
        });
    }

    function loadTable(){
        $('#tblDetail>tbody').html('');
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_pe_detail",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if(!$.isEmptyObject(data)){
                    var html = '';
                    var maxValue = 1;
                    for(i in data){
                        if(maxValue < data[i].QTY){
                            maxValue = data[i].QTY;
                        }
                    }
                    for(i in data){
                        var per = data[i].QTY / maxValue * 100;
                        html += '<tr><td class="model_' + data[i].MODEL_NAME.replace(/\s+/g,'_') + '"><b>' + data[i].MODEL_NAME + '</b></td>'
                             +  '<td class="model_' + data[i].MODEL_NAME.replace(/\s+/g,'_') + '_error_' + data[i].TEST_CODE.replace(/\s+/g,'_') + '">' +data[i].TEST_CODE + '</td>'
                             +  '<td>' + data[i].REASON_CODE + '</td>'
                             +  '<td>' + data[i].LOCATION_CODE + '</td>'
                             +  '<td><div class="progress">'
                             +  '<div class="progress-bar" role="progressbar" style="width: ' + per + '%">' +data[i].QTY + '</div>'
                             +  '</div></td></tr>';
                    }
                    $('#tblDetail>tbody').html(html);

                    for(i in data){
                        var classModel = $('.model_' + data[i].MODEL_NAME.replace(/\s+/g,'_'));
                        var classError = $('.model_' + data[i].MODEL_NAME.replace(/\s+/g,'_') + '_error_' + data[i].TEST_CODE.replace(/\s+/g,'_'));
                        if(classModel.length > 1){
                            for(i in classModel){
                                classModel[0].rowSpan = classModel.length;
                                if(i > 0){
                                    classModel[i].className += ' hidden';
                                }
                            }
                        }
                        if(classError.length > 1){
                            for(i in classError){
                                classError[0].rowSpan = classError.length;
                                if(i > 0){
                                    classError[i].className += ' hidden';
                                }
                            }
                        }
                    }
                } else{
                    alert('NO DATA !!');
                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var startDate = moment(current).add(-1, "day").format("YYYY/MM/DD") + ' 00:00:00';
                var endDate = moment(current).format("YYYY/MM/DD") + ' 00:00:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
                dataset.timeSpan = startDate + ' - ' + endDate;
                init();

                $('input[name=timeSpan]').on('change', function () {
                    dataset.timeSpan = this.value;
                    init();
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    $(document).ready(function () {
        getTimeNow();
    });
</script>