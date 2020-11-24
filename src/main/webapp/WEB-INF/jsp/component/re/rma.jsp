<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/rma.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />

<div class="panel panel-re panel-flat row">
    <div class="panel panel-overview" id="header">
        <b><span class="text-uppercase" name="factory">${factory}</span><Span id="teamS"></Span><span>-RE RMA
                Management</span></b>
    </div>
    <div class="row" style="margin: unset;">
        <div class="panel panel-overview input-group"
            style="margin-bottom: 5px; background: #333;width: 91%; float: left;">
            <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                    class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                style="height: 26px; color: #fff;">
        </div>
        <div class="panel chooseModel"
            style="width: 8.5%;margin: 0 1% 5px;float: right;background: #333; height: 26px; margin-right: 0;margin-left: 0;">
            <a class=" btn btn-lg" onclick="exportDataRMA()" title="Export Remain RMA"
                style="height:26px;width:100%;font-size: 13px; padding: 4px; border-radius: 5px; color: #fff;background: #444;">
                <i class="fa fa-download"></i> EXPORT
            </a>
        </div>
    </div>
    <!--<div class="panel panel-overview input-group" style="margin-bottom: 5px; padding-left: 10px; background: #333;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        <div class="rma-button">
            <button class="btn">RMA</button>
        </div>
        <div class="count"> Count:
            <input type="text" side="right" name="count" style="padding-left: 5px;">
        </div>
	</div>-->
    <div class="row" style="margin: unset">
        <div class="col-xs-12 col-sm-3 infchart">
            <div class="panel panel-flat panel-body chart-sm" id="chart2"></div>
        </div>
        <div class="col-xs-12 col-sm-6 infchart">
            <div class="panel panel-flat panel-body chart-sm" id="chart1"></div>
        </div>
        <div class="col-xs-12 col-sm-3 infchart">
            <div class="panel panel-flat panel-body chart-sm" id="pieChartRma"></div>
        </div>
    </div>
    <div class="row" style="margin: unset">
        <div class="col-xs-12 col-sm-12 infchart">
            <div class="panel panel-flat panel-body chart-sm" id="chart3"></div>
        </div>
    </div>
    <!-- <div class="row" style="margin-top: 10px;">
        <div class="col-lg-5" style=" margin-left: 30px; padding:0px; width: 630px;">
            <div id="chart1" class="chart"></div>
        </div>
        <div class="col-lg-5 pie" style=" margin-left: 0px; padding:0px; width: 630px; display: none;">
            <div id="pieChartRma" class="chart"></div>
        </div>
    </div> -->
    <!-- <div class="row" style="margin-top: 10px;">
        <div class="col-lg-3" style=" margin-left: 30px; padding:0px; width: 630px;">
            <div id="chart2" class="chart"></div>
        </div>
        <div class="col-lg-3" style=" margin-left: 0px; padding:0px; width: 630px;">
            <div id="chart3" class="chart"></div>
        </div>
    </div> -->
    <!-- <div class="row" style="padding-right: 15px;">
        <div class="table-responsive pre-scrollable col-md-12" style="max-height: auto; color: #ccc;">
            <table id="rmaTable" class="table table-xxs table-bordered table-sticky">
                <thead>
                    <tr class="bg-primary">
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
        <div class="export" style="cursor: pointer; left: -21px; margin-top: 10px; display: inline-block; position: relative; top: 8px; left: 1212px;">
            <a class="btn btn-lg" id="download" style="font-size: 13px; padding: 5px; border-radius: 10px;">
              <i class="fa fa-download"></i> EXPORT
            </a>
		</div>
        <div class="export pull-right" style="margin: 10px 10px 0px 0px;">
            <a class="btn btn-lg" id="btnExport" style="font-size: 13px; padding: 5px; border-radius: 10px;color: #ccc">
                <i class="fa fa-download"></i> EXPORT
            </a>
        </div>
    </div> -->
</div>
<style>
    body {
        overflow-x: hidden;
    }
</style>
<script>
    var team = "-JBD";
    $('input[name="count"]').val("4390");
    $(document).ready(function () {
        // loadRmastatus();
        // loadRmaInOut();
        // loadRmaDaily();
        // loadTableData();
        // init();
    });

    function init() {
        if (dataset.factory == "B04") {
            team = "-BBD";
        }
        $('#teamS').text(team)
        getRMAInOutStatus();
        getDataTrendChartRMA();
    }
    var dataset = {
        factory: '${factory}'
    }
    function loadRmastatus() {

        Highcharts.chart('chart1', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'RE RMA STATUS BY MODEL',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category'
            },
            plotOptions: {
                series: {
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function (event) {
                                var model_name = this.name;
                                drawPiechart(model_name);
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
                data: [
                    ['26-1204', 965],
                    ['25-1904', 820],
                    ['26-2452-0401', 455],
                    ['26-2452-0451', 447],
                    ['26-2473', 387],
                    ['26-1691', 371],
                    ['26-1905-1003', 352],
                    ['26-1552-0004', 288],
                    ['26-2478', 266],
                    ['26-2454-0401', 264],
                    ['26-1693', 263],
                    ['26-2450-0401', 192],
                    ['40-2411-1001', 191]
                ]
            }]
        });
    }

    function loadRmaInOut() {
        Highcharts.chart('chart2', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'RE RMA TOTAL IN/OUT STATUS',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category'
            },
            plotOptions: {
                series: {
                    dataLabels: {
                        enabled: true,
                        color: '#1fb2ec'
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
                data: [
                    ["Input Qty", 1186],
                    ["Output Qty", 1134],
                    ["Remain Qty", 52]
                ]
            }]
        });
    }
    function loadRmaDaily() {
        Highcharts.chart('chart3', {
            chart: {
                type: 'line'
            },
            title: {
                text: 'RE RMA DAILY IN/OUT STATUS',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category',
                allowDecimals: false,
                minorGridLineWidth: 0,
                minTickInterval: 1,
                tickAmount: 4
            },
            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: 'blue'
                }],
                title: {
                    text: ''
                }
            },
            legend: {
                style: {
                    fontSize: '11px'
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
                name: 'Output Qty',
                color: 'red',
                data: [
                    ['24-Apr', 100],
                    ['25-Apr', 234],
                    ['26-Apr', 120],
                    ['27-Apr', 120],
                    ['29-Apr', 125],
                    ['2-May', 50],
                    ['3-May', 78],
                    ['4-May', 65],
                    ['6-May', 62],
                    ['7-May', 130],
                    ['8-May', 50]
                ]
            }, {
                name: 'Input Qty',
                data: [
                    ['24-Apr', 120],
                    ['25-Apr', 260],
                    ['26-Apr', 120],
                    ['27-Apr', 70],
                    ['29-Apr', 70],
                    ['2-May', 75],
                    ['3-May', 78],
                    ['4-May', 63],
                    ['6-May', 95],
                    ['7-May', 110],
                    ['8-May', 60]
                ]
            }, {
                name: 'Remain Qty',
                color: '#01DF74',
                data: [
                    ['24-Apr', -25],
                    ['25-Apr', -25],
                    ['26-Apr', -10],
                    ['27-Apr', 50],
                    ['29-Apr', 55],
                    ['2-May', -25],
                    ['3-May', 0],
                    ['4-May', 5],
                    ['6-May', -30],
                    ['7-May', 20],
                    ['8-May', -10]
                ]
            }]
        });
    }

    function loadTableData() {
        $.ajax({
            type: 'GET',
            url: '/assets/custom/js/re/rma.json',
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var result = jQuery.parseJSON(JSON.stringify(data));
                var html = '';
                for (i in result) {
                    html += '<tr class="bg-secondary tbRow" role="row" style="border-color: #2E9AFE;">' +
                        '<td name="no" tabindex="0" rowspan="1" colspan="1">' + result[i].no + '</td>' +
                        '<td name="type" tabindex="0" rowspan="1" colspan="1">' + result[i].type + '</td>' +
                        '<td name="model" tabindex="0" rowspan="1" colspan="1">' + result[i].model + '</td>' +
                        '<td name="station" tabindex="0" rowspan="1" colspan="1">' + result[i].station + '</td>' +
                        '<td name="mo" tabindex="0" rowspan="1" colspan="1">' + result[i].mo + '</td>' +
                        '<td name="sn" tabindex="0" rowspan="1" colspan="1">' + result[i].sn + '</td>' +
                        '<td name="mac" tabindex="0" rowspan="1" colspan="1">' + result[i].mac + '</td>' +
                        '<td name="qty" tabindex="0" rowspan="1" colspan="1">' + result[i].qty + '</td>' +
                        '<td name="errorCode" tabindex="0" rowspan="1" colspan="1">' + result[i].errorCode + '</td>' +
                        '<td name="description" tabindex="0" rowspan="1" colspan="1">' + result[i].description + '</td>' +
                        '<td name="fa" tabindex="0" rowspan="1" colspan="1">' + result[i].fa + '</td>' +
                        '<td name="inputTime" tabindex="0" rowspan="1" colspan="1">' + result[i].inputTime + '</td>' +
                        '<td name="outputTime" tabindex="0" rowspan="1" colspan="1">' + result[i].outputTime + '</td>' +
                        '<td name="owner" tabindex="0" rowspan="1" colspan="1">' + result[i].owner + '</td>' +
                        '<td name="status" tabindex="0" rowspan="1" colspan="1">' + result[i].status + '</td>' +
                        '</tr>';
                }
                $('#rmaTable>tbody').html(html);
            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    //  drawPiechart('40-2411-1001');
    function drawPiechart(model_name) {
        $.ajax({
            type: 'GET',
            url: '/api/re/under-repair/model',
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

                Highcharts.chart('pieChartRma', {
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
                        data: dataChart
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    // function drawPiechart(){
    // 	$('.pie').css('display', 'inline-block');
    // 	Highcharts.chart('pieChartRma', {
    // 		chart: {
    // 			type: 'pie',
    // 			options3d: {
    // 				enabled: true,
    // 				alpha: 45,
    // 				beta: 0
    // 			}
    // 		},
    // 		title: {
    // 			text: '40-2411-1001',
    // 			fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
    // 			fontSize: '15px',
    //             fontWeight: 'bold'
    // 		},
    // 		plotOptions: {
    //             pie: {
    //                 allowPointSelect: true,
    //                 cursor: 'pointer',
    // 				depth: 35,
    //                 dataLabels: {
    //                     enabled: false
    //                 },
    //                 showInLegend: true
    //             }
    //         },
    //         legend: {
    //             style: {
    //                 fontSize: '11px'
    //             },
    //             layout: 'horizontal',
    //             align: 'left',
    //             verticalAlign: 'bottom'
    //         },
    // 		navigation: {
    // 			buttonOptions: {
    // 				enabled: false
    // 			}
    // 		},
    // 		credits: {
    // 			enabled: false
    // 		},
    //         series : [{
    //             data: [
    // 				['LG248', 156],
    // 				['LG178',56],
    // 				['LG182', 42],
    // 				['LG176', 32],
    // 				['LG177', 21],
    // 				['LG179', 12],
    // 				['LG180', 10],
    // 				['LG181', 6],
    // 				['LG182', 5]
    // 			]
    // 		}]
    //     });
    // }
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
        var todaysDate = $('input[name=timeSpan]').value;
        var blobURL = tableToExcel('rmaTable', 'test_table');
        $(this).attr('download', 'Top_10.xls')
        $(this).attr('href', blobURL);
    });

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
        // var spcTS = getTimeSpan();
        // $('.datetimerange').data('daterangepicker').setStartDate(spcTS.startDate);
        // $('.datetimerange').data('daterangepicker').setEndDate(spcTS.endDate);
        getTimeNow();

        $('input[name=timeSpan]').on('change', function () {
            dataset.timeSpan = this.value;
            init();
        });
    });

    function getRMAInOutStatus() {
        var dt = {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan
        };
        $.ajax({
            type: "GET",
            url: "/api/re/online/inoutrma",
            data: dt,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var dataChart = new Array();
                var dataChartStatus = new Array();
                dataChart.push({ name: "INPUT", y: data.totalInput })
                dataChart.push({ name: "OUTPUT", y: data.totalOutput })
                dataChart.push({ name: "REMAIN", y: data.totalRemain })
                hightChartInOutStatus1("chart2", "RE RMA TOTAL IN/OUT  STATUS", dataChart);
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
                hightChartInOutStatus("chart1", "RE RMA STATUS BY MODEL", dataChartStatus);
                getTop15ErrorCodeRMA(model1);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function getTop15ErrorCodeRMA(modelName) {
        var dt = {
            factory: dataset.factory,
            modelName: modelName
        };
        $.ajax({
            type: "GET",
            url: "/api/re/online/errorcoderma",
            data: dt,
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
                highChartPie(pieChartRma, modelName, dataChart);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function getDataTrendChartRMA() {
        var dt = {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan
        };
        $.ajax({
            type: "GET",
            url: "/api/re/online/rmatrendchart",
            data: dt,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var dataInput = [];
                var dataOutput = [];
                var dataRemain = [];
                for (let key in data) {
                    if (data.hasOwnProperty(key)) {
                        let element = data[key];
                        var tmpArrIn = [];
                        var tmpArrOut = [];
                        var tmpArrRemain = [];
                        tmpArrIn.push(key, element.input);
                        tmpArrOut.push(key, element.output);
                        tmpArrRemain.push(key, element.remain);

                        //  tmpArrRemain.push(key, (element.input * 1 - element.output * 1));
                        dataInput.push(tmpArrIn);
                        dataOutput.push(tmpArrOut);
                        dataRemain.push(tmpArrRemain);

                    }
                }
                loadRmaDailyTrendChart("chart3", "RE RMA DAILY IN/OUT STATUS", dataInput, dataOutput, dataRemain);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function exportDataRMA() {
        window.location.href = '/api/re/online/downloadRMA?factory=' + dataset.factory;

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
                                getTop15ErrorCodeRMA(model_name)
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

    function highChartPie(idHtml, titleText, dataChart) {
        Highcharts.chart(idHtml, {
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0
                }
            },
            title: {
                text: titleText,
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
                        enabled: true
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
                data: dataChart
            }]
        });
    }

    function loadRmaDailyTrendChart(idHtml, textHtml, dataInput, dataOutput, dataRemain) {
        Highcharts.chart(idHtml, {
            chart: {
                type: 'line'
            },
            title: {
                text: textHtml,
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category',
                allowDecimals: false,
                minorGridLineWidth: 0,
                minTickInterval: 1,
                tickAmount: 4
            },
            yAxis: {
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: 'blue'
                }],
                title: {
                    text: ''
                }
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
            legend: {
                style: {
                    fontSize: '11px'
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
                name: 'Output Qty',
                color: 'red',
                data: dataOutput
            }, {
                name: 'Input Qty',
                data: dataInput
            }, {
                name: 'Remain Qty',
                color: '#01DF74',
                data: dataRemain
            }]
        });
    }

</script>