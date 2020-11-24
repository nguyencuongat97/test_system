<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<!-- <link rel="stylesheet" href="/assets/custom/css/re/onlineWip.css" /> -->
<link rel="stylesheet" href="/assets/custom/css/re/bonepile.css" />

<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<!-- <link rel="stylesheet" href="/assets/css/custom/style.css" /> -->

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header" style="font-size: 18px;">
            <b><span>B04-BBD-RE Online WIP Management</span></b>
        </div>
        <div class="row" style="margin:unset">
            <div class=" panel panel-overview input-group"
                style="width:65%; margin-bottom: 5px; background: #333;float: left;">
                <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                        class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                    style="height: 30px; color: #fff;">
            </div>
            <div class=" panel chooseModel"
                style="width: 26%;margin: 0 1% 5px;float: left;background: #333; height: 30px;">
                <select class=" form-control selectpicker chooseModelExport" id="slModelName" data-live-search="true"
                    style=" color: #fff;padding: 5px;border-bottom: none;">
                    <!-- <option value="" disabled="" selected="">Choose Model Export</option> -->
                </select>
            </div>
            <!-- <div class=" wrapperBtnExport"> -->
            <a class=" btn btn-lg" id="btnExport" onclick="exportDataDetailModelname()"
                style="height:29px;width:7%;font-size: 13px; padding: 7px; border-radius: 5px; color: #fff;background: #444;">
                <i class="fa fa-download"></i> EXPORT
            </a>
            <!-- </div> -->
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-5 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="chart1"></div>
            </div>
            <div class="col-xs-12 col-sm-7 infchart ">
                <div class="panel panel-flat panel-body chart-sm" id="chart2"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart " id="wipWaitingRepair">
                <div class="panel panel-flat panel-body chart-sm" id="chart3"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="chart4"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="chart5"></div>
            </div>
            <div class="col-xs-12 col-sm-12 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="in_out_trend"></div>
            </div>
            <div class="col-xs-12 col-sm-3 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="defected_status"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="defected_issues"></div>
            </div>
            <div class="col-xs-12 col-sm-5 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="column_chart"></div>
            </div>
            <div class="col-xs-12 col-sm-3 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="output_section"></div>
            </div>
            <div class="col-xs-12 col-sm-5 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="drilldown_chart"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="pie_chart"></div>
            </div>
        </div>
        <!-- <div class="row">
            <div class="col-sm-12 table-responsive pre-scrollable">
                <table id="checkOutTable" class="table table-xxs table-custom table-bordered  table-sticky"
                    style="background: #333; color: #ccc; text-align: center;">
                    <thead class="bg-dark-gray">
                        <tr>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <iframe id="txtArea1" style="display:none"></iframe>
            <div class="export pull-right" style="margin: 10px 10px 0px 0px;">
                <a class="btn btn-lg" id="btnExport2"
                    style="font-size: 13px; padding: 5px; border-radius: 10px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div> -->
    </div>
</div>
<style>
    #reWipTable thead tr th {
        text-align: center;
    }

    .rate-red {
        background: #e75849;
        font-weight: bold;
        color: #333;
    }

    .rate-green {
        background: #02ba6b;
        font-weight: bold;
        color: #333;
    }

    .rate-yellow {
        background: #ffaa00;
        font-weight: bold;
        color: #333;
    }

    .chooseModelExport .dropdown-toggle {
        color: #fff;
        border-bottom: none;
        height: 30px;
    }

    .chooseModelExport .dropdown-toggle .bs-caret .caret {
        padding-right: 20px;
        color: #fff;
    }

    .chooseModelExport.open {
        height: 30px;
    }

    .chooseModelExport .dropdown-toggle .filter-option {
        color: #fff;
        padding-left: 5px;
    }

    #chart3 .highcharts-container .highcharts-button .highcharts-button-box {
        display: none;
    }

    #output_section .highcharts-container .highcharts-button .highcharts-button-box {
        display: none;
    }
</style>
<script>
    var dataset = {
        factory: 'B04'
    }
    var colorStatus = ['#2b908f', '#90ee7e', '#f45b5b', '#aaeeee'];
    init();
    function init() {
        loadWipInOutStatus();
        loadWipReMainStatus();
        waitingRepairStutus();
        wipInOutTrendChart();
        wipDefectedStatus();
        loadOutputSection();
        // loadTableData();
    }
    // Create the chart
    function loadWipInOutStatus() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/wipInOutStatus",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var totalInput = data.totalInput;
                var totalOutput = data.totalOutput;
                var totalRemain = data.totalRemain;
                var dataInput = data.dataInput;
                var dataOutput = data.dataOutput;
                var dataRemain = data.dataRemain;
                var dataInOut = [], arrayDrillDownIn = [], arrayDrillDownOut = [], arrayDrillDownRemain = [], dataDrip = [];
                dataInOut.push({ name: "RE INTPUT", y: totalInput, drilldown: "INPUT" });
                dataInOut.push({ name: "RE OUTPUT", y: totalOutput, drilldown: "OUTPUT" });
                dataInOut.push({ name: "RE REMAIN", y: totalRemain, drilldown: "REMAIN" });

                for (var key in dataInput) {
                    if (dataInput.hasOwnProperty(key)) {
                        var element = dataInput[key];
                        var tmpArr = [];
                        tmpArr.push(key);
                        tmpArr.push(element);
                        arrayDrillDownIn.push(tmpArr);
                    }
                }
                dataDrip.push({ name: "RE INPUT", id: "INPUT", data: arrayDrillDownIn });

                for (var key in dataOutput) {
                    if (dataOutput.hasOwnProperty(key)) {
                        var element = dataOutput[key];
                        var tmpArr = [];
                        tmpArr.push(key);
                        tmpArr.push(element);
                        arrayDrillDownOut.push(tmpArr);

                    }
                }
                dataDrip.push({ name: "RE OUTPUT", id: "OUTPUT", data: arrayDrillDownOut });
                for (var key in dataRemain) {
                    if (dataRemain.hasOwnProperty(key)) {
                        var element = dataRemain[key];
                        var tmpArr = [];
                        tmpArr.push(key);
                        tmpArr.push(element);
                        arrayDrillDownRemain.push(tmpArr);

                    }
                }
                dataDrip.push({ name: "RE REMAIN", id: "REMAIN", data: arrayDrillDownRemain });
                Highcharts.chart('chart1', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE - WIP IN OUT STATUS',
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                            },
                            cursor: 'pointer',
                            events: {
                                click: function (event) {
                                    var drilldown = event.point.drilldown;
                                    if (drilldown == undefined || drilldown.levelNumber == '') {
                                    }
                                }
                            }
                        },
                    },
                    yAxis: {
                        plotLines: [{
                            value: 0,
                            width: 1,
                        }],
                        title: {
                            text: ''
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
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b><br/>'
                    },
                    series: [{
                        data: dataInOut,
                        colorByPoint: true
                    }],
                    drilldown: {
                        series: dataDrip
                    }
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadWipReMainStatus() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/remainDaily",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                //console.log(data);
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var categories = new Array(keys.length);
                for (i in keys) {
                    categories[i] = keys[i];
                    dataChart[i] = { name: keys[i], y: data[keys[i]] }
                }
                Highcharts.chart('chart2', {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'RE - WIP REMAIND STATUS',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                        tickInterval: Math.ceil(keys.length / 7)
                    },
                    plotOptions: {
                        series: {
                            dataLabels: {
                                enabled: true,
                                color: '#f0f0f3',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    yAxis: {
                        plotLines: [{
                            value: 0,
                            width: 1,
                            //    color: colorStatus
                        }],
                        title: {
                            text: ''
                        }
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
                    series: [{
                        name: ' ',
                        // colorByPoint: true,
                        data: dataChart
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function waitingRepairStutus() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/modelByTime",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (!$.isEmptyObject(data)) {
                    var keys = Object.keys(data);
                    var dataChart = [], categories = [], drilldown = [];

                    for (i = 0; i < keys.length; i++) {
                        var modelMap = data[keys[i]].data;
                        if (modelMap.length < 15) {
                            var drillData = new Array(modelMap.length);
                            for (j in modelMap) {
                                drillData[j] = {
                                    name: modelMap[j].key,
                                    y: modelMap[j].value
                                }
                            }
                        }
                        else {
                            var drillData = new Array();
                            for (j = 0; j < 15; j++) {
                                drillData[j] = {
                                    name: modelMap[j].key,
                                    y: modelMap[j].value
                                }
                            }

                        }
                        categories[i] = keys[i];
                        dataChart[i] = {
                            name: keys[i],
                            y: data[keys[i]].size,
                            drilldown: (modelMap.length > 0 ? keys[i] : null)
                        };
                        drilldown[i] = {
                            name: keys[i],
                            id: keys[i],
                            data: drillData
                        };
                    }
                }
                Highcharts.chart('chart3', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE - WIP WAITING REPAIR STATUS (BY TIME)',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category'
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        }
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        var section_name = this.name;
                                        top15ModelWaitingRepair(section_name, drilldown);
                                    }
                                }
                            }

                        },
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
                    },
                    legend: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
                    }],
                    // drilldown: {
                    //     enabled:false,
                    // }
                });
                top15ModelWaitingRepair(dataChart[7].name, drilldown);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function top15ModelWaitingRepair(section_name, drilldown) {
        var drillData;
        for (i in drilldown) {
            if (drilldown[i].name == section_name) {
                drillData = drilldown[i].data;
            }
        }
        Highcharts.chart('chart4', {
            chart: {
                type: 'bar',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: 'Top 15 model waiting repair',
                style: {
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category',
            },
            yAxis: {
                min: 0,
                title: {
                    text: ' '
                },
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                    },
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function () {
                                var class_name = this.name;
                                loadPieChart(class_name);
                            }
                        }
                    }
                }
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
            series: [{
                name: ' ',
                colorByPoint: true,
                data: drillData,
                marker: {
                    enabled: false
                },
            }]
        });
        loadPieChart(drillData[0].name);
    }
    function loadPieChart(class_name) {
        $.ajax({
            type: 'GET',
            url: '/api/re/online/errorCodeWip',
            data: {
                factory: dataset.factory,
                // timeSpan: dataset.timeSpan,
                modelName: class_name,
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array();
                var categories = new Array();
                if (keys.length < 15) {
                    for (i in keys) {
                        categories[i] = keys[i];
                        dataChart[i] = { name: keys[i], y: data[keys[i]] }
                    }
                }
                else {
                    for (i = 0; i < 15; i++) {
                        categories[i] = keys[i];
                        dataChart[i] = { name: keys[i], y: data[keys[i]] }
                    }
                }
                Highcharts.chart('chart5', {
                    chart: {
                        type: 'pie',
                        options3d: {
                            enabled: true,
                            alpha: 45,
                            beta: 0
                        }
                    },
                    title: {
                        text: class_name,
                        style: {
                            fontSize: '15px',
                            fontWeight: 'bold',
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            depth: 35,
                            dataLabels: {
                                enabled: false
                            },
                            showInLegend: true
                        }
                    },
                    legend: {
                        style: {
                            color: '#ccc'
                        },
                        itemStyle: {
                            fontSize: '11px',
                            color: '#ccc'
                        },
                        layout: 'horizontal',
                        align: 'left',
                        verticalAlign: 'bottom'
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
                        data: dataChart
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function wipInOutTrendChart() {
        var dt = {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan
        };
        $.ajax({
            type: "GET",
            url: "/api/re/online/wipInOutTrendChart",
            data: dt,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var input = new Array(keys.length);
                var output = new Array(keys.length);
                //var remain = new Array(keys.length);
                var yield_rate = new Array(keys.length);
                for (j in keys) {
                    var value = data[keys[j]];
                    if (!$.isEmptyObject(value)) {
                        var yt = parseFloat(((value.output / value.input) * 100).toFixed(2));

                        input[j] = { name: keys[j], y: value.input };
                        output[j] = { name: keys[j], y: value.output };
                        // remain[j] = { name: keys[j], y: value.remain };
                        yield_rate[j] = { name: keys[j], y: yt };
                    } else {
                        input[j] = { name: keys[j], y: 0 };
                        output[j] = { name: keys[j], y: 0 };
                        // remain[j] = { name: keys[j], y: 0 };
                        yield_rate[j] = { name: keys[j], y: 0 };
                    }
                }
                Highcharts.chart('in_out_trend', {
                    chart: {
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE - WIP  IN_OUT  TREND CHART',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                        tickInterval: Math.ceil(keys.length / 7),
                    },
                    yAxis: [{ // Primary yAxis
                        title: {
                            text: '',
                        }
                    }, { // Secondary yAxis
                        title: {
                            text: '',
                            style: {
                                color: Highcharts.getOptions().colors[0]
                            }
                        },
                        labels: {
                            format: '{value}%',
                            style: {
                                //color: Highcharts.getOptions().colors[4]
                            }
                        },
                        opposite: true
                    }],
                    tooltip: {
                        shared: true
                    },
                    legend: {
                        style: {
                            fontSize: '11px'
                        },
                        layout: 'horizontal',
                        align: 'right',
                        verticalAlign: 'right'
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
                        name: 'Yield Rate',
                        type: 'column',
                        yAxis: 1,
                        data: yield_rate,
                        tooltip: {
                            valueSuffix: '%'
                        },
                        dataLabels: {
                            enabled: true,
                            format: '{y}%'
                        }
                    }, {
                        name: 'Input',
                        type: 'line',
                        data: input
                    }, {
                        name: 'Output',
                        type: 'line',
                        data: output
                    }
                        // , {
                        //     name: 'Remain',
                        //     type: 'line',
                        //     data: remain,
                        //     color: Highcharts.getOptions().colors[6]
                        // }
                    ]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function wipDefectedStatus() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/ProcessNTF",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var categories = new Array(keys.length);
                for (i in keys) {
                    categories[i] = keys[i];
                    dataChart[i] = { name: keys[i], y: data[keys[i]] }
                }
                Highcharts.chart('defected_status', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',

                        }
                    },
                    title: {
                        text: 'RE - WIP REPAIR CHECK OUT BY DEFECT STATUS',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category'
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        }
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                                color: '#f0f0f3',
                                fontWeight: 'bold'
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        var section_name = this.name;
                                        loadDefectedIssues(section_name);
                                    }
                                }
                            }

                        },
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
                    },
                    legend: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
                    }],
                });
                loadDefectedIssues('PROCESS');
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadDefectedIssues(section_name) {
        $.ajax({
            type: "GET",
            url: "/api/re/online/defected/all",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                defected: section_name

            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var categories = new Array(keys.length);
                for (i in keys) {
                    categories[i] = keys[i];
                    dataChart[i] = { name: keys[i], y: data[keys[i]] }
                }
                //    if(section_name=='other'){
                //        return;
                //    }
                Highcharts.chart('defected_issues', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: ' ',

                    },
                    xAxis: {
                        type: 'category'
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        }
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                                color: '#f0f0f3',
                                fontWeight: 'bold',
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        var model_name = this.name;
                                        loadColumnschart1(model_name, section_name);
                                    }
                                }
                            }

                        },
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
                    },
                    legend: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
                    }]
                });
                if (dataChart == 0) {
                    return;
                } else {
                    loadColumnschart1(dataChart[0].name, section_name);
                }

            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadColumnschart1(model_name, section_name) {
        $.ajax({
            type: "GET",
            url: "/api/re/online/defected/detail",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                defected: section_name,
                modelName: model_name
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var categories = new Array(keys.length);
                for (i in keys) {
                    categories[i] = keys[i];
                    dataChart[i] = { name: keys[i], y: data[keys[i]] }
                }
                //    if(section_name=='other'){
                //        return;
                //    }
                Highcharts.chart('column_chart', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE Repair Check out by Defect ' + section_name + '',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category'
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        }
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                                color: '#f0f0f3',
                                fontWeight: 'bold',
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        var model_name = this.name;
                                        if (model_name == 0 || model_name == '') {
                                            //text:{'No data to display'};
                                        }
                                    }
                                }
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
                    series: [{
                        name: ' ',
                        // colorByPoint: true,
                        data: dataChart,
                        marker: {
                            enabled: false
                        },
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadOutputSection() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/modelnamesismtpth",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var totalSI = data.qtySI;
                var totalSMT = data.qtySMT;
                var totalPTH = data.qtyPTH;
                var dataSI = data.dataSI;
                var dataSMT = data.dataSMT;
                var dataPTH = data.dataPTH;
                var dataAll = [], arrayDrillDownSI = [], arrayDrillDownSMT = [], arrayDrillDownPTH = [], dataDrip = [], drilldown = [];
                dataAll.push({ name: "SI", y: totalSI, drilldown: "SI" });
                dataAll.push({ name: "SMT", y: totalSMT, drilldown: "SMT" });
                dataAll.push({ name: "PTH", y: totalPTH, drilldown: "PTH" });

                for (var key in dataSI) {
                    if (dataSI.hasOwnProperty(key)) {
                        var element = dataSI[key];
                        arrayDrillDownSI.push({ name: key, y: element });
                    }
                }
                drilldown.push({ name: "SI", id: "SI", data: arrayDrillDownSI });

                for (var key in dataSMT) {
                    if (dataSMT.hasOwnProperty(key)) {
                        var element = dataSMT[key];
                        arrayDrillDownSMT.push({ name: key, y: element });

                    }
                }
                drilldown.push({ name: "SMT", id: "SMT", data: arrayDrillDownSMT });

                for (var key in dataPTH) {
                    if (dataPTH.hasOwnProperty(key)) {
                        var element = dataPTH[key];
                        arrayDrillDownPTH.push({ name: key, y: element });

                    }
                }
                drilldown.push({ name: "PTH", id: "PTH", data: arrayDrillDownPTH });
                Highcharts.chart('output_section', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        // text: 'RE - WIP REPAIR CHECK OUT BY DEFECT STATUS',
                        text: ' ',
                    },
                    xAxis: {
                        type: 'category'
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        }
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                                color: '#f0f0f3',
                                fontWeight: 'bold'
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        // var drilldown = event.point.drilldown;
                                        // if (drilldown == undefined || drilldown.levelNumber == '') {
                                        // }
                                        var section_name = this.name;
                                        loadDrillDownChart(section_name, drilldown);
                                    }
                                }
                            }

                        },
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
                    },
                    legend: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: [{
                        data: dataAll,
                        colorByPoint: true
                    }],
                    // drilldown: {
                    //     series: dataDrip
                    // }
                });
                loadDrillDownChart(dataAll[0].name, drilldown);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadDrillDownChart(section_name, drilldown) {
        var drillData;
        for (i in drilldown) {
            if (drilldown[i].name == section_name) {
                drillData = drilldown[i].data;
            }
            else if (drilldown[i].name == undefined || drilldown[i].name == '') {
                drillData = '';
            }
        }

        Highcharts.chart('drilldown_chart', {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: ' ',

            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                min: 0,
                title: {
                    text: ' '
                }
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        color: '#f0f0f3',
                        fontWeight: 'bold',
                    },
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function (event) {
                                var model_name = this.name;
                                // if (model_name == 0 || model_name == '') {
                                // }
                                loadPiechart2(model_name, section_name);
                            }
                        }
                    }

                },
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
            },
            legend: {
                enabled: false
            },
            credits: {
                enabled: false
            },
            navigation: {
                buttonOptions: {
                    enabled: false
                }
            },
            series: [{
                name: ' ',
                colorByPoint: true,
                data: drillData,
                // marker: {
                //     enabled: false
                // },
            }]
        });
        if (drillData == 0 || drillData == '') {
            loadPiechart2();
        } else {
            loadPiechart2(drillData[0].name, section_name);
        }

    }
    function loadPiechart2(model_name, section_name) {
        $.ajax({
            type: 'GET',
            url: '/api/re/online/modelnamesismtpth/error',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                modelName: model_name
            },
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array();
                var categories = new Array();
                if (keys.length < 15) {
                    for (i in keys) {
                        categories[i] = keys[i];
                        dataChart[i] = { name: keys[i], y: data[keys[i]] }
                    }
                }
                else {
                    for (i = 0; i < 15; i++) {
                        categories[i] = keys[i];
                        dataChart[i] = { name: keys[i], y: data[keys[i]] }
                    }
                }
                Highcharts.chart('pie_chart', {
                    chart: {
                        type: 'pie',
                        options3d: {
                            enabled: true,
                            alpha: 45,
                            beta: 0
                        }
                    },
                    title: {
                        text: model_name,
                        style: {
                            fontSize: '15px',
                            fontWeight: 'bold',
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            depth: 35,
                            dataLabels: {
                                enabled: false,
                                color: '#f0f0f3',
                                fontWeight: 'bold'
                            },
                            point: {
                                events: {
                                    click: function (event) {
                                        var model_name = this.name;
                                        if (model_name == 0 || model_name == '') {
                                            //text:{'No data to display'};
                                        }
                                        //loadPiechart2(model_name,section_name);
                                    }
                                }
                            },
                            showInLegend: true
                        }
                    },
                    legend: {
                        style: {
                            color: '#ccc'
                        },
                        itemStyle: {
                            fontSize: '11px',
                            color: '#ccc'
                        },
                        layout: 'horizontal',
                        align: 'left',
                        verticalAlign: 'bottom'
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
                        data: dataChart
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    //table
    // function loadTableData() {
    //         $.ajax({
    //             type: 'GET',
    //             url: '/api/re/status/daily/model',
    //             data: {
    //                 factory: dataset.factory,
    //                 timeSpan: dataset.timeSpan
    //             },
    //             contentType: "application/json; charset=utf-8",
    //             success: function (data) {
    //                 $('#checkOutTable>tbody>tr').remove();
    //                 $('#checkOutTable>thead>tr>th').remove();

    //                 var body = '';
    //                 var thead = '<th>MODEL_NAME</th>';

    //                 var giangModel = [];
    //                 var giangShift = {};
    //                 if (!$.isEmptyObject(data)) {
    //                     var caKeys = Object.keys(data);
    //                     for (i in caKeys) {
    //                         giangShift[caKeys[i]] = [];
    //                         if (!$.isEmptyObject(data[caKeys[i]])) {
    //                             var modelKeys = Object.keys(data[caKeys[i]]);
    //                             for (j in modelKeys) {
    //                                 var index = giangModel.indexOf(modelKeys[j]);
    //                                 if (index > -1) {
    //                                     giangShift[caKeys[i]][index] = data[caKeys[i]][modelKeys[j]];
    //                                 } else {
    //                                     giangModel.push(modelKeys[j]);
    //                                     giangShift[caKeys[i]][giangModel.length - 1] = data[caKeys[i]][modelKeys[j]];
    //                                 }
    //                             }
    //                         }
    //                     }

    //                     for (i in Object.keys(giangShift)) {
    //                         thead += '<th>' + Object.keys(giangShift)[i] + '</th>';
    //                     }
    //                     $('#checkOutTable>thead>tr').append(thead);
    //                     var classRate;

    //                     for (j in giangModel) {
    //                         body += '<tr><td class="bg-dark-gray">' + giangModel[j] + '</td>';
    //                         var values = new Array();
    //                         for(i in giangShift)
    //                         {
    //                             values.push(giangShift[i]);
    //                         }
    //                         for (i in Object.keys(giangShift)) {
    //                             if ((values[i])[j] === undefined || (values[i])[j] === null) {
    //                                 body += '<td></td>';
    //                             }
    //                             else {
    //                                 if ((values[i])[j].yieldRate >= 92) {
    //                                     classRate = "rate-green";
    //                                 }
    //                                 else if ((values[i])[j].yieldRate > 70 && (values[i])[j].yieldRate < 92) {
    //                                     classRate = "rate-yellow";
    //                                 }
    //                                 else {
    //                                     classRate = "rate-red";
    //                                 }
    //                                 body += '<td class=' + classRate + '>' + (values[i])[j].yieldRate.toFixed(2) + '% <span class="rate-percent">(' + (values[i])[j].output + '/' + (values[i])[j].input + ')</span></td>';
    //                             }
    //                         }
    //                     }
    //                     body += '</tr>';
    //                 }
    //                 $('#checkOutTable>tbody').append(body);
    //             },
    //             error: function () {
    //                 alert("Fail!");
    //             }
    //         });
    //     }
    //     var tableToExcel = (function () {
    //         var uri = 'data:application/vnd.ms-excel;base64,'
    //             , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
    //             , base64 = function (s) { return window.btoa(unescape(encodeURIComponent(s))) }
    //             , format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) }
    //         return function (table, name) {
    //             if (!table.nodeType) table = document.getElementById(table)
    //             var ctx = { worksheet: name || 'Worksheet', table: table.innerHTML }
    //             var blob = new Blob([format(template, ctx)]);
    //             var blobURL = window.URL.createObjectURL(blob);
    //             return blobURL;
    //         }
    //     })();
    //     $("#btnExport2").click(function () {
    //         var startDateCode = getFormatDate(new Date(getTimeSpan().startDate));
    //         var endDateCode = getFormatDate(new Date(getTimeSpan().endDate));
    //         console.log(startDateCode);
    //         console.log(endDateCode);
    //         var isIE = /*@cc_on!@*/false || !!document.documentMode;
    //         if(isIE){
    //             var tab_text="<table border='2px'><tr bgcolor='#F79646'>";
    //             var textRange; var j=0;
    //             tab = document.getElementById('checkOutTable'); // id of table

    //             for(j = 0 ; j < tab.rows.length ; j++)
    //             {
    //                 tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
    //                 //tab_text=tab_text+"</tr>";
    //             }

    //             tab_text=tab_text+"</table>";
    //             tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
    //             tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
    //             tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

    //             var ua = window.navigator.userAgent;
    //             var msie = ua.indexOf("MSIE ");

    //             if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
    //             {
    //                 txtArea1.document.open("txt/html","replace");
    //                 txtArea1.document.write(tab_text);
    //                 txtArea1.document.close();
    //                 txtArea1.focus();
    //                 sa=txtArea1.document.execCommand("SaveAs",true,'RE Checkout.xls');
    //             }
    //             else                 //other browser not tested on IE 11
    //             sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
    //             return (sa);
    //         }
    //         else{
    //             var blobURL = tableToExcel('checkOutTable', 'test_table');
    //             $(this).attr('download', 'RE Checkout (' + startDateCode + ' - ' + endDateCode + ').xls')
    //             $(this).attr('href', blobURL);
    //         }
    //     });
    //     var startDateCode = getFormatDate(new Date(getTimeSpan().startDate));

    //     function getFormatDate(d) {
    //         return d.getFullYear() + "/" +
    //             d.getMonth() + "/" +
    //             d.getDate() + " " +
    //             d.getHours() + ":" +
    //             d.getMinutes();
    //     }

    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    $(document).ready(function () {
        var spcTS = getTimeSpan();
        $('.datetimerange').data('daterangepicker').setStartDate(spcTS.startDate);
        $('.datetimerange').data('daterangepicker').setEndDate(spcTS.endDate);

        $('input[name=timeSpan]').on('change', function () {
            dataset.timeSpan = this.value;
            init();
        });
    });

    getListModelName();
    function getListModelName() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/getListModel",
            data: {
                factory: dataset.factory

            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var html = '<option value="" disabled="" selected="">Choose Model Export</option>';
                for (var i = 0; i < data.length; i++) {
                    html += '<option>' + data[i] + '</option>';
                }
                // html += '';
                $('#slModelName').html(html);
                $('#slModelName').selectpicker('refresh');
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function exportDataDetailModelname() {
        var modelN = $('#slModelName').val();
        if (modelN == '' || modelN == null) {
            window.location.href = '/api/re/online/importonlinewip';
        } else {
            window.location.href = '/api/re/online/importonlinewip?modelName=' + modelN + '';
        }

    }
</script>