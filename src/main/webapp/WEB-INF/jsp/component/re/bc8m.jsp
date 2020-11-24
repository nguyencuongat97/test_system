<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/bc8m.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />

<div class="panel panel-re panel-flat row" style="margin: unset;">
    <div class="panel panel-overview" id="header" style="font-size: 18px;">
        <b><span class="text-uppercase" name="factory">${factory}</span><Span id="teamS"></Span><span>-RE BC8M
                Management</span></b>
    </div>
    <!-- <div class="row" style="margin:unset">
        <div class=" panel panel-overview input-group"
            style="width:65%; margin-bottom: 5px; background: #333;float: left;">
            <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                    class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                style="height: 30px; color: #fff;">
        </div>
        <div class=" panel chooseModel" id="divChooseModel"
            style="width: 26%;margin: 0 1% 5px;float: left;background: #333; height: 30px;">
            <select class=" form-control selectpicker chooseModelExport" title="Choose Model Export" id="slModelName"
                multiple="multiple" data-actions-box="true" data-live-search="true"
                style=" color: #fff;padding: 5px;border-bottom: none;">
              
            </select>
        </div>
        <a class=" btn btn-lg" id="btnExportModel" onclick="exportDataDetailModelname()"
            style="height:29px;width:7%;font-size: 13px; padding: 7px; border-radius: 5px; color: #fff;background: #444;">
            <i class="fa fa-download"></i> EXPORT
        </a>
    </div> -->
    <div class="row" style="margin:unset">
        <div class=" panel panel-overview input-group"
            style="width:92%; margin-bottom: 5px; background: #333;float: left;">
            <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                    class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                style="height: 30px; color: #fff;">
        </div>
        <div class=" panel chooseModel"
            style="width: 26%;margin: 0 1% 5px;float: left;background: #333; height: 30px;display: none;">
            <select class=" form-control selectpicker chooseModelExport" id="slModelName" data-live-search="true"
                style=" color: #fff;padding: 5px;border-bottom: none;">
                <!-- <option value="" disabled="" selected="">Choose Model Export</option> -->
            </select>
        </div>
        <!-- <div class=" wrapperBtnExport"> -->
        <a class=" btn btn-lg" id="btnExportModel" onclick="exportDataDetailModelname()"
            style="height:29px;width:7%;font-size: 13px; padding: 7px; border-radius: 5px; color: #fff;background: #444;float: right;">
            <i class="fa fa-download"></i> EXPORT
        </a>
        <!-- </div> -->
    </div>
    <div class="row" style="margin: unset;">
        <div class="col-xs-12 col-sm-3">
            <div id="bc8mInOut" class="panel panel-flat panel-body chart-xs"></div>
        </div>
        <div class="col-xs-12 col-sm-5">
            <div id="bc8mStatus" class="panel panel-flat panel-body chart-xs"></div>
        </div>
        <div class="col-xs-12 col-sm-4">
            <div id="bc8mInOut16" class="panel panel-flat panel-body chart-xs"></div>
        </div>
    </div>
    <div class="row" style="margin: unset;">
        <div class="col-xs-12 col-sm-6">
            <div id="bc8mTimeHour" class="panel panel-flat panel-body chart-xs"></div>
        </div>
        <div class="col-xs-12 col-sm-6">
            <div id="bc8mDefect" class="panel panel-flat panel-body chart-xs"></div>
        </div>
    </div>
    <!-- <div class="row" style="margin-top: 20px; padding: 10px;">
        <div class="table-responsive pre-scrollable col-md-12" style="max-height: auto; color: #ccc; max-width: 99%">
            <table id="bc8mTable" class="table table-custom table-bordered table-sticky">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>Type</th>
                        <th>Model</th>
                        <th>Station</th>
                        <th>MO</th>
                        <th>SN</th>
                        <th>MAC</th>
                        <th>Qty</th>
                        <th>Error code</th>
                        <th>Description</th>
                        <th>FA</th>
                        <th>Input time</th>
                        <th>Output time</th>
                        <th>Owner</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
        <iframe id="txtArea1" style="display:none"></iframe>
        <div class="export pull-right" style="margin: 10px 10px 0px 0px;">
            <a class="btn btn-lg" id="btnExport"
                style="font-size: 13px; padding: 5px; border-radius: 10px; color: #ccc">
                <i class="fa fa-download"></i> EXPORT
            </a>
        </div>
    </div> -->
</div>
<script>
    var team = "-JBD";
    var dataset = {
        factory: '${factory}'
    }
    init();

    function init() {
        // loadBc8mStatus();
        //  loadBc8mInOut();
        // loadBc8mDefect();
        // loadBc8mInOut16();
        //loadTableData();
        if (dataset.factory == "B04") {
            team = "-BBD";
        }
        $('#teamS').text(team)
        getRemainAndModelName();
        //getTop15ErrorCodeBC8M();
        wipInOutTrendChart();
        countSerialNumberInputByTime();
    }

    function loadBc8mStatus() {
        $.ajax({
            type: "GET",
            url: "/api/re/bc8m/model",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var values = new Array();
                for (i in data) {
                    values.push(data[i]);
                }
                var dataChart = new Array();
                if (keys.length < 15) {
                    for (i in keys) {
                        dataChart[i] = {
                            name: keys[i],
                            y: values[i]
                        }
                    }
                } else {
                    for (j = 0; j < 15; j++) {
                        dataChart[j] = {
                            name: keys[j],
                            y: values[j]
                        }
                    }
                }
                loadPiechart(keys[0]);

                Highcharts.chart('bc8mStatus', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE BC8M STATUS BY MODEL (TOTAL)',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                        allowDecimals: false,
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        var model_name = this.name;
                                        loadPiechart(model_name)
                                    }
                                }
                            }
                        }
                    },
                    yAxis: {
                        plotLines: [{
                            value: 0,
                            width: 1,
                            color: 'blue'
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
                    series: [{
                        name: ' ',
                        data: dataChart
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function getRemainAndModelName() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/bc8mremain",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var dataChart = new Array();
                var dataChartStatus = new Array();
                dataChart.push({ name: "INPUT", y: data.totalInput })
                dataChart.push({ name: "OUTPUT", y: data.totalOutput })
                dataChart.push({ name: "REMAIN", y: data.totalRemain })

                hightChartInOutStatus1("bc8mInOut", "RE BC8M TOTAL IN/OUT  STATUS", dataChart);
                var model1;
                var count = true;
                for (let key in data.dataRemain) {
                    if (data.dataRemain.hasOwnProperty(key)) {
                        if (count) {
                            model1 = key;
                        }
                        count = false;
                        let element = data.dataRemain[key];
                        dataChartStatus.push({ name: key, y: element })
                    }
                }
                hightChartInOutStatus("bc8mStatus", "RE BC8M STATUS BY MODEL (TOTAL)", dataChartStatus);
                getTop15ErrorCodeBC8M(model1);
            }
        })
    }

    function getTop15ErrorCodeBC8M(modelName) {
        $.ajax({
            type: "GET",
            url: "/api/re/online/bc8mtop15errorcode",
            data: {
                factory: dataset.factory,
                modelName: modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var dataChart = new Array();
                var count = 0;
                for (let key in data) {
                    if (data.hasOwnProperty(key)) {
                        let element = data[key];
                        dataChart.push({ name: key, y: element });
                        count++;
                        if (count == 15) {
                            break;
                        }
                    }
                }
                hightChartErrorCodeColum("bc8mInOut16", "TOP 15 BY ERROR CODE", dataChart);
            }
        })
    }

    function loadBc8mInOut() {
        $.ajax({
            type: "GET",
            url: "/api/re/bc8m/io",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var values = new Array();
                for (i in data) {
                    values.push(data[i]);
                }
                var dataChart = new Array();

                for (i in keys) {
                    dataChart[i] = {
                        name: keys[i],
                        y: values[i]
                    }
                }
                Highcharts.chart('bc8mInOut', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE BC8M TOTAL IN/OUT  STATUS',
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
                            }
                        }
                    },
                    yAxis: {
                        plotLines: [{
                            value: 0,
                            width: 1,
                            color: 'blue'
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
                    series: [{
                        name: '',
                        data: dataChart,
                        // colorByPoint: true
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadBc8mDefect() {
        Highcharts.chart('bc8mDefect', {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: 'RE BC8M DEFECT STATUS',
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
                    allowPointSelect: true,
                    dataLabels: {
                        enabled: true,
                    }
                }
            },
            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: 'blue'
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
                data: [{
                    name: 'Waiting Sensor Board',
                    y: 899,
                    drilldown: 'sensorBoard',

                }, {
                    name: 'Waiting Scrap',
                    y: 525,
                    drilldown: 'scrap'
                }, {
                    name: 'Hard Repair',
                    y: 125,
                    drilldown: 'repair'
                }, {
                    name: 'Repair OK',
                    y: 25,
                    drilldown: 'repairOK'
                }],
                colorByPoint: true
            }],

            drilldown: {
                series: [{
                    name: 'Waiting Sensor Board',
                    id: 'sensorBoard',
                    data: [
                        ['26-1693', 219],
                        ['26-2450-0401', 214],
                        ['26-2452-0481', 91],
                        ['26-2450-0451', 68],
                        ['26-2450-0481', 57],
                        ['26-2448-0401', 44],
                        ['26-1204', 30],
                        ['26-2473', 26],
                        ['26-2452-0401', 24],
                        ['26-2454-0103', 20],
                        ['26-1691', 9],
                        ['26-2446-0401', 7],
                    ]
                }, {
                    name: 'Waiting Scrap',
                    id: 'scrap',
                    data: [
                        ['26-1204', 310],
                        ['26-2452-0401', 125],
                        ['26-2473', 60],
                        ['26-2452-0451', 59],
                        ['26-2454-0401', 49],
                        ['26-1691', 44],
                        ['26-2452-0481', 15],
                        ['26-1693', 12],
                        ['26-2454-0451', 12],
                        ['26-2450-0481', 9],
                        ['26-2450-0401', 7],
                        ['45-1189-0401', 6],
                        ['26-1692', 5],
                        ['26-2448-0401', 5],
                        ['26-2450-0451', 5],

                    ]
                }, {
                    name: 'Hard Repair',
                    id: 'repair',
                    data: [
                        ['45-1189-0451', 26],
                        ['45-1189-0401', 24],
                        ['T77X974.01', 14],
                        ['40-1514', 10],
                        ['T77X850.11', 10],
                        ['P02X007.00', 7],
                        ['45-1187-0481', 5],
                        ['P100009110', 5],
                        ['K17X001.00', 5],
                        ['45-1189-0481', 4],
                        ['40-1672-0481', 3],
                        ['M46X001.00', 3],
                        ['T77X850.10', 3],
                        ['26-2444-0401', 2],
                        ['26-2452-0481', 2],
                        ['P02H003.01', 1],

                    ]
                }, {
                    name: 'Repair OK',
                    id: 'repairOK',
                    data: [
                        ['T99X171T00', 7],
                        ['P02X007.00', 8],
                        ['T77X974.01', 10],

                    ]
                }]
            }
        });
    }
    function loadBc8mInOut16() {
        $.ajax({
            type: "GET",
            url: "/api/re/bc8m/daily",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var dataActual = new Array();
                var dataPlan = new Array();
                var dataOutput = new Array();
                var dataRemain = new Array();
                for (i in keys) {
                    if (data[keys[i]] === null || data[keys[i]] === undefined) {
                        dataOutput[i] = 0;
                        dataRemain[i] = 0;
                    }
                    else {
                        var tat = parseFloat(data[keys[i]].tat.toFixed(2));
                        dataOutput[i] = tat;
                        dataRemain[i] = data[keys[i]].remain;
                    }
                    dataActual[i] = {
                        name: keys[i],
                        y: dataOutput[i]
                    }
                    dataRemain[i] = {
                        name: keys[i],
                        y: dataRemain[i]
                    }
                }
                Highcharts.chart('bc8mInOut16', {
                    chart: {
                        type: 'line',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE BC8M OUTPUT',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                    },
                    yAxis: [{ // Primary yAxis
                        title: {
                            enabled: false
                        }
                    }, { // Secondary yAxis
                        title: {
                            text: '',
                            style: {
                                color: '#f45b5b'
                            }
                        },
                        labels: {
                            format: '{value}',
                            style: {
                                //color: Highcharts.getOptions().colors[4]
                            }
                        },
                        opposite: true
                    }],
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
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                        }
                    },
                    series: [
                        {
                            name: 'Actual(BC8M)',
                            type: 'column',

                            color: '#f45b5b',
                            data: dataRemain,
                            dataLabels: {
                                enabled: true,
                            }
                        },
                        {
                            name: 'TAT',
                            type: 'line',
                            yAxis: 1,
                            color: Highcharts.getOptions().colors[4],
                            data: dataOutput,
                            dataLabels: {
                                enabled: true,
                                format: '{y}',
                                color: Highcharts.getOptions().colors[4]
                            }
                        }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadTableData() {
        $.ajax({
            type: 'GET',
            url: '/api/re/bc8m/data',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $('#bc8mTable>tbody>tr').remove();
                var html = '';
                if (!$.isEmptyObject(data)) {
                    var keys = data.data;
                    var dt_length = keys.length - 1;
                    var dt_minsize;
                    if (dt_length >= 10) {
                        dt_minsize = dt_length - 10;
                    }
                    else {
                        dt_minsize = 0;
                    }
                    var number = 0;
                    for (i = dt_length; i > dt_minsize; i--) {
                        for (j in keys[i]) {
                            if (keys[i][j] == null || keys[i][j] == undefined) {
                                keys[i][j] = " ";
                            }
                        }
                        number++;
                        html += '<tr class="bg-secondary tbRow" role="row" style="border-color: #2E9AFE;">' +
                            '<td name="no" tabindex="0" rowspan="1" colspan="1">' + number + '</td>' +
                            '<td name="type" tabindex="0" rowspan="1" colspan="1">' + keys[i].status + '</td>' +
                            '<td name="model" tabindex="0" rowspan="1" colspan="1">' + keys[i].modelName + '</td>' +
                            '<td name="station" tabindex="0" rowspan="1" colspan="1">' + keys[i].stationName + '</td>' +
                            '<td name="mo" tabindex="0" rowspan="1" colspan="1">' + keys[i].mo + '</td>' +
                            '<td name="sn" tabindex="0" rowspan="1" colspan="1">' + keys[i].serialNumber + '</td>' +
                            '<td name="mac" tabindex="0" rowspan="1" colspan="1"></td>' +
                            '<td name="qty" tabindex="0" rowspan="1" colspan="1"></td>' +
                            '<td name="errorCode" tabindex="0" rowspan="1" colspan="1">' + keys[i].errorCode + '</td>' +
                            '<td name="description" tabindex="0" rowspan="1" colspan="1"></td>' +
                            '<td name="fa" tabindex="0" rowspan="1" colspan="1"></td>' +
                            '<td name="inputTime" tabindex="0" rowspan="1" colspan="1">' + keys[i].createdAt + '</td>' +
                            '<td name="outputTime" tabindex="0" rowspan="1" colspan="1"></td>' +
                            '<td name="owner" tabindex="0" rowspan="1" colspan="1"></td>' +
                            '<td name="status" tabindex="0" rowspan="1" colspan="1"></td>' +
                            '</tr>';
                    }
                } else {
                    $('#bc8mTable>tbody').append('<tr><td colspan="10" align="center">-- NO DATA --</td></tr>');
                }
                $('#bc8mTable>tbody').append(html);

            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    function loadPiechart(model_name) {
        $.ajax({
            type: 'GET',
            url: '/api/re/bc8m/model/error',
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

                Highcharts.chart('pieChartBc8m', {
                    chart: {
                        type: 'pie',
                        options3d: {
                            enabled: true,
                            alpha: 45,
                            beta: 0
                        },
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
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
                            borderWidth: 0,
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
                        itemStyle: {
                            fontSize: '11px',
                            fontWeight: '100'
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
                        name: '',
                        data: dataChart
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }


    var tableToExcel = (function () {
        var uri = 'data:application/vnd.ms-excel;base64,'
            , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
            , base64 = function (s) { return window.btoa(unescape(encodeURIComponent(s))) }
            , format = function (s, c) { return s.replace(/{(\w+)}/g, function (m, p) { return c[p]; }) }
        return function (table, name) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = { worksheet: name || 'Worksheet', table: table.innerHTML }
            var blob = new Blob([format(template, ctx)]);
            var blobURL = window.URL.createObjectURL(blob);
            return blobURL;
        }
    })();
    $("#btnExport").click(function () {
        var startDateCode = getFormatDate(new Date(getTimeSpan().startDate));
        var endDateCode = getFormatDate(new Date(getTimeSpan().endDate));
        // console.log(startDateCode);
        var isIE = /*@cc_on!@*/false || !!document.documentMode;
        if (isIE) {
            var tab_text = "<table border='2px'><tr bgcolor='#F79646'>";
            var textRange; var j = 0;
            tab = document.getElementById('bc8mTable'); // id of table

            for (j = 0; j < tab.rows.length; j++) {
                tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
                //tab_text=tab_text+"</tr>";
            }

            tab_text = tab_text + "</table>";
            tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
            tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
            tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

            var ua = window.navigator.userAgent;
            var msie = ua.indexOf("MSIE ");

            if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
            {
                txtArea1.document.open("txt/html", "replace");
                txtArea1.document.write(tab_text);
                txtArea1.document.close();
                txtArea1.focus();
                sa = txtArea1.document.execCommand("SaveAs", true, 'RE BC8M.xls');
            }
            else                 //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        }
        else {
            var blobURL = tableToExcel('bc8mTable', 'test_table');
            $(this).attr('download', 'RE BC8M (' + startDateCode + ' - ' + endDateCode + ').xls')
            $(this).attr('href', blobURL);
        }
    });
    var startDateCode = getFormatDate(new Date(getTimeSpan().startDate));

    function getFormatDate(d) {
        return d.getFullYear() + "/" +
            d.getMonth() + "/" +
            d.getDate() + " " +
            d.getHours() + ":" +
            d.getMinutes();
    }

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
                // <option value="" disabled="" selected="">Choose Model Export</option>
                var html = '';
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
    // $('#divChooseModel').click(function(){
    //     $('li.active a span.check-mark').css('color','#fff !important');
    // });
    function exportDataDetailModelname() {
        // var modelN = $('#slModelName').val();
        // var stringSub = '';
        // for (let index = 0; index < modelN.length; index++) {
        //     if (index == modelN.length - 1) {
        //         stringSub += modelN[index];
        //     } else {
        //         stringSub += modelN[index] + ';';
        //     }
        // }
        window.location.href = '/api/re/online/downloadBC8M?factory=' + dataset.factory;

    }
    function countSerialNumberInputByTime() {
        var dt = {
            factory: dataset.factory
        };
        $.ajax({
            type: "GET",
            url: "/api/re/online/inputTime",
            data: dt,
            contentType: "application/json; charset=utf-8",
            success: function (array) {
                var dataChart = [];
                if (array.length > 0) {
                    hightChartInOutStatus("bc8mTimeHour", "BC8M WAITING REPAIR (BY DAY)", array)
                } else {
                    alert("Data  Reamin by Time => REQUEST FALSE");
                }

            }
        })
    }

    function wipInOutTrendChart() {
        var dt = {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan
        };
        $.ajax({
            type: "GET",
            url: "/api/re/online/bc8minoutchart",
            data: dt,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var arrInput = [];
                var arrOutput = [];
                var arrUser = [];
                for (let i = 0; i < data.length; i++) {
                    let element = data[i];
                    arrUser.push(Object.keys(element)[0]);
                    let input = Object.values(element)[0].input;
                    let output = Object.values(element)[0].output;
                    arrInput.push(input);
                    arrOutput.push(output);
                }
                var dt = new Array();
                dt.push({ name: "INPUT", data: arrInput });
                dt.push({ name: "OUTPUT", data: arrOutput });
                hightCHartTrendChart("bc8mDefect", "BC8M CHECK IN/OUT TREND CHART", arrUser, dt)
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    // FUNCTION

    function hightChartInOutStatus1(idChart, textHtml, dataChart) {
        Highcharts.chart(idChart, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: textHtml,
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
                    }

                }
            },
            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: 'blue'
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
            series: [{
                name: '',
                data: dataChart
            }]
        });
    }

    function hightChartInOutStatus(idChart, textHtml, dataChart) {
        Highcharts.chart(idChart, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: textHtml,
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
                    point: {
                        events: {
                            click: function (event) {
                                var model_name = this.name;
                                getTop15ErrorCodeBC8M(model_name)
                            }
                        }
                    }
                }
            },
            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: 'blue'
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
            series: [{
                name: '',
                data: dataChart
            }]
        });
    }

    function hightChartErrorCodeColum(idChart, textHtml, dataChart) {
        Highcharts.chart(idChart, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: textHtml,
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
    }

    function hightCHartTrendChart(idHtml, titleText, dataxAxis, data) {
        Highcharts.chart(idHtml, {

            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                },
                // backgroundColor: 'rgba(39,39,39,0.7)'
            },
            title: {
                text: titleText,
                style: {
                    fontSize: '14px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                categories: dataxAxis,
                type: 'category',
                labels: {
                    enabled: true,
                    rotation: -40,
                    align: 'right',
                    // style: {
                    //     fontSize: '11px'
                    // }
                },
                gridLineWidth: 0,
                minorGridLineWidth: 0,
                tickLength: 0,
                tickWidth: 0,
            },
            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: 'blue'
                }],
                title: {
                    text: ''
                },
                gridLineWidth: 0,
                minorGridLineWidth: 0,
                tickLength: 0,
                tickWidth: 0,
                // offset: -320,
            },
            legend: {
                enabled: false,
                itemStyle: {
                    fontSize: '10px',
                },
                align: 'center',
                verticalAlign: 'bottom',
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
                series: {
                    borderWidth: 0,
                    label: {
                        connectorAllowed: false
                    },
                    //   pointStart: 2010,
                    dataLabels: {
                        enabled: true,
                    }
                }
            },

            series: data,
            responsive: {
                rules: [{
                    condition: {
                        maxWidth: 500
                    },
                    chartOptions: {
                        legend: {
                            layout: 'horizontal',
                            align: 'center',
                            verticalAlign: 'bottom'
                        }
                    }
                }]
            }

        });
    }


</script>