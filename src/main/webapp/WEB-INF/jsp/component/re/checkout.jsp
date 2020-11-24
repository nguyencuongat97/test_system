<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/checkOut.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<link rel="stylesheet" href="/assets/css/custom/style.css" />
<link rel="stylesheet" href="/assets/css/custom/theme.css" />

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span>B04-BBD-RE Repair Check Out Management</span></b>
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
                <a class=" btn btn-lg" onclick="exportDataCheckOut()"
                    style="height:26px;width:100%;font-size: 13px; padding: 4px; border-radius: 5px; color: #fff;background: #444;">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>

        <div class="row" style="margin: unset;">
            <div class="col-md-12" style="padding: unset;">
                <div class="panel panel-flat panel-body chart-sm" id="in_out_trend"></div>
                <!-- <div class="panel panel-flat panel-body chart-sm" id="in_out_si"></div>
                <div class="panel panel-flat panel-body chart-sm" id="in_out_smt"></div> -->
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-6">
                <div id="in_out_si" class="panel panel-flat panel-body chart-sm"></div>
            </div>
            <div class="col-xs-12 col-sm-6">
                <div id="in_out_smt" class="panel panel-flat panel-body chart-sm"></div>
            </div>
        </div>
        <div class="row">
            <!--<div class="col-xs-12 col-sm-5">
                <div class="panel panel-flat panel-body chart-sm" style="background: #333; color: #ccc; padding-top: 20px;">
                    <table class="table table-custom">
                        <thead>
                            <tr style="background: #444; color: #fff;">
                                <th>DEFECTED ISSUES</th>
                                <th>QUANTITY</th>
                                <th>RATE</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><a href="#">Total</a></td>
                                <td>1391</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><a href="#">Process</a></td>
                                <td>989</td>
                                <td>71,1%</td>
                            </tr>
                            <tr>
                                <td><a href="#">NTF</a></td>
                                <td>231</td>
                                <td>16,61%</td>
                            </tr>
                            <tr>
                                <td><a href="#">Material</a></td>
                                <td>171</td>
                                <td>12,29%</td>
                            </tr>
                            <tr>
                                <td><a href="#">Other</a></td>
                                <td>0</td>
                                <td>0%</td>
                            </tr>
                            <tr>
                                <td><a href="#">Upgrade</a></td>
                                <td>0</td>
                                <td>0%</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>-->
            <!-- <div class="col-xs-12 col-sm-5">
                <div class="panel panel-flat panel-body chart-sm" id="defected_status"></div>
            </div>
            <div class="col-xs-12 col-sm-7">
                <div class="panel panel-flat panel-body chart-sm" id="defected_issues"></div>
            </div> -->
            <div class="col-xs-12 col-sm-3">
                <div class="panel panel-flat panel-body chart-sm" id="defected_status"></div>
            </div>
            <div class="col-xs-12 col-sm-5">
                <div class="panel panel-flat panel-body chart-sm" id="defected_issues"></div>
            </div>
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="reasonErrorLocation"></div>
            </div>

        </div>
        <!-- column 3 -->
        <!-- <div class="row classAllColumn">
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="columnChart1"></div>
            </div>
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="columnChart2"></div>
            </div>
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="columnChart3"></div>
            </div>
        </div> -->

        <div class="row">
            <div class="col-xs-12 col-sm-3">
                <div class="panel panel-flat panel-body chart-sm" id="output_section"></div>
            </div>
            <div class="col-xs-12 col-sm-5">
                <div class="panel panel-flat panel-body chart-sm" id="drilldown_chart"></div>
            </div>
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="pie_chart"></div>
            </div>
        </div>
        <div class="row">
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
            <div class="export pull-right" style="margin: 10px 10px 0px 0px;">
                <a class="btn btn-lg" id="btnExport"
                    style="font-size: 13px; padding: 5px; border-radius: 10px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>
    </div>
</div>
<style>
    .classAllColumn {
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

    //init();

    function init() {
        loadInOutTrendChart();
        loadInputOutputSISMT();
        loadDefectedStatus();
        loadOutputSection();
        loadTableData();
    }

    function loadInOutTrendChart() {
        var dt = {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan
        };
        console.log(dt)
        $.ajax({
            type: "GET",
            url: "/api/re/status/daily",
            data: dt,
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var input = new Array(keys.length);
                var output = new Array(keys.length);
                var remain = new Array(keys.length);
                var yield_rate = new Array(keys.length);
                for (j in keys) {
                    var value = data[keys[j]];
                    if (!$.isEmptyObject(value)) {
                        var yt = parseFloat(((value.output / value.input) * 100).toFixed(2));
                        input[j] = { name: keys[j], y: value.input };
                        output[j] = { name: keys[j], y: value.output };
                        remain[j] = { name: keys[j], y: value.remain };
                        yield_rate[j] = { name: keys[j], y: yt };
                    } else {
                        input[j] = { name: keys[j], y: 0 };
                        output[j] = { name: keys[j], y: 0 };
                        remain[j] = { name: keys[j], y: 0 };
                        yield_rate[j] = { name: keys[j], y: 0 };
                    }
                }
                // console.log("input:" + JSON.stringify(input))
                // console.log("output:" + JSON.stringify(output))
                // console.log("remain:" + JSON.stringify(remain))
                // console.log("yield_rate:" + JSON.stringify(yield_rate))

                Highcharts.chart('in_out_trend', {
                    chart: {
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE Total In_Out Trend Chart',
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
                    }, {
                        name: 'Remain',
                        type: 'line',
                        data: remain,
                        color: Highcharts.getOptions().colors[6]
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadInputOutputSISMT() {
        // DATE TODAY   
        var curday = function (sp) {
            today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1; //As January is 0.
            var yyyy = today.getFullYear();
            if (dd < 10) dd = '0' + dd;
            if (mm < 10) mm = '0' + mm;
            return (yyyy + sp + mm + sp + dd);
        };
        var endDate = curday('/');
        // END DATE TODAY

        // 7 day ago
        var d = new Date();
        var pastDate = d.getDate() - 7;
        d.setDate(pastDate);
        var startDate = d.getFullYear().toString() + "/" + ((d.getMonth() + 1).toString().length == 2 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1).toString()) + "/" + (d.getDate().toString().length == 2 ? d.getDate().toString() : "0" + d.getDate().toString());
        // END 7 days ago
        // var timeSpan = startDate + " 07:30 - " + endDate + " 07:30";
        // var factory = "B04";
        // console.log(timeSpan)

        $.ajax({
            type: "GET",
            url: "/api/re/status/inOutTrendChart",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                //  console.log("keys get date data: ",data);
                var inputSI = [], outputSI = [], remainSI = [], yield_rateSI = [];
                var inputSMT = [], outputSMT = [], remainSMT = [], yield_rateSMT = [];
                var ordered = {};
                var keysDate = Object.keys(data["SI"]).sort();


                for (let key in keysDate) {
                    if (data.SI.hasOwnProperty(keysDate[key])) {
                        let element = data.SI[keysDate[key]];
                        var yt = parseFloat(((element.output / element.input) * 100).toFixed(2));
                        inputSI.push({ name: keysDate[key], y: element.input });
                        outputSI.push({ name: keysDate[key], y: element.output });
                        remainSI.push({ name: keysDate[key], y: element.remain });
                        yield_rateSI.push({ name: keysDate[key], y: yt });
                    }
                }
                for (let key in keysDate) {
                    if (data.SMT.hasOwnProperty(keysDate[key])) {
                        let element = data.SMT[keysDate[key]];
                        var yt = parseFloat(((element.output / element.input) * 100).toFixed(2));
                        inputSMT.push({ name: keysDate[key], y: element.input });
                        outputSMT.push({ name: keysDate[key], y: element.output });
                        remainSMT.push({ name: keysDate[key], y: element.remain });
                        yield_rateSMT.push({ name: keysDate[key], y: yt });
                    }
                }
                chartInPutOutPut("in_out_si", inputSI, outputSI, remainSI, yield_rateSI);
                chartInPutOutPut("in_out_smt", inputSMT, outputSMT, remainSMT, yield_rateSMT);

            },
            failure: function (errMsg) {
                console.log(errMsg);
            }

        })
    }

    function loadOutputSection() {
        $.ajax({
            type: "GET",
            url: "/api/re/output/section",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                // console.log("loadOutputSection:", data)
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var drilldown = new Array(keys.length);

                for (i in keys) {
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

                    dataChart[i] = {
                        name: keys[i],
                        y: data[keys[i]].size,
                        drilldown: (modelMap.length > 0 ? keys[i] : null)
                    }

                    drilldown[i] = {
                        name: keys[i],
                        id: keys[i],
                        data: drillData
                    };
                }
                Highcharts.chart('output_section', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
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
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
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
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
                    }],
                });
                loadDrillDownChart(drilldown[0].name, drilldown);
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
                data: drillData
            }]
        });
        loadPiechart(drillData[0].name);
    }

    function loadPiechart(model_name) {
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

    function loadDefectedStatus() {
        $.ajax({
            type: 'GET',
            url: '/api/re/online/ProcessNTF',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
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
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE Total Repair Check out by Defect Status',
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
                    tooltip: {
                        pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
                        shared: true
                    },
                    plotOptions: {
                        column: {
                            stacking: 'normal'
                        },
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        var di_name = this.name;
                                        loadDefectedIssues(di_name);
                                    }
                                }
                            }
                        },
                    },
                    legend: {
                        enabled: false,
                        itemStyle: {
                            fontSize: '10px',
                        },
                        align: 'right',
                        verticalAlign: 'top',
                        //floating: true,
                        borderWidth: 1,
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
                        name: 'TOTAL',
                        colorByPoint: true,
                        data: dataChart,
                        marker: {
                            enabled: false
                        },
                        dataLabels: {
                            enabled: true
                        }
                    }]
                });
                loadDefectedIssues("PROCESS");
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadDefectedIssues(di_name) {
        $.ajax({
            type: 'GET',
            url: '/api/re/online/defected/all',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                defected: di_name
            },
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length);
                var categories = new Array(keys.length);
                for (i in keys) {
                    categories[i] = keys[i];
                    dataChart[i] = { name: keys[i], y: data[keys[i]] }
                }
                Highcharts.chart('defected_issues', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE Repair Check out by Defect ' + di_name + '',
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
                        column: {
                            stacking: 'normal'
                        },
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function (event) {
                                        var name_model = this.name;
                                        // if (di_name == "PROCESS") {
                                        //     $('.classAllColumn').css('display', 'block');
                                        loadAllColumn(di_name, name_model);
                                        // } else {
                                        //     $('.classAllColumn').css('display', 'none');

                                        // }

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
                    }],
                    // drilldown: {
                    //     series: drilldown
                    // }
                });
                loadAllColumn(di_name, dataChart[0].name);

            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadAllColumn(defected, name_model) {
        $.ajax({
            type: "GET",
            url: "/api/re/online/errorReasonLocation",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                modelName: name_model,
                defected: defected
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                console.log("errorReasonLocation::", data);
                var title = 'Top 15 ';
                if (defected == "NTF") {
                    title += ' Error Code';
                } else if (defected == "COMPONENT") {
                    title += ' Location Code';
                }
                if (defected == "PROCESS") {
                    title += ' Reason Code';
                    var totalData = [];
                    var dataDrip = [];
                    for (let i = 0; i < data.length; i++) {
                        var arrayDrillDown = [];
                        let element = data[i];
                        totalData.push({ name: element.reason, y: element.qty, drilldown: element.reason });
                        if (!$.isEmptyObject(element.data)) {
                            for (var key in element.data) {
                                if (element.data.hasOwnProperty(key)) {
                                    var e = element.data[key];
                                    var tmpArr = [];
                                    tmpArr.push(key);
                                    tmpArr.push(e);
                                    arrayDrillDown.push(tmpArr);
                                }
                            }
                        }
                        dataDrip.push({ name: element.reason, id: element.reason, data: arrayDrillDown });
                    }
                    highChartDrippdownReason(totalData, dataDrip, "reasonErrorLocation", title);
                } else {
                    var count = 0;
                    var dataHighchart = [];
                    if (!$.isEmptyObject(data)) {
                        for (var key in data) {
                            if (data.hasOwnProperty(key)) {
                                var e = data[key];
                                var tmpArr = [];
                                tmpArr.push(key);
                                tmpArr.push(e);
                                dataHighchart.push(tmpArr);
                                count++;
                            }
                            if (count == 15) {
                                break;
                            }
                        }
                    }
                    highchartByColum(dataHighchart, "reasonErrorLocation", title);

                }

            },
            failure: function (errMsg) {
                console.log(errMsg);
            }

        })
    }

    function loadTableData() {
        $.ajax({
            type: 'GET',
            url: '/api/re/status/daily/model',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $('#checkOutTable>tbody>tr').remove();
                $('#checkOutTable>thead>tr>th').remove();

                var body = '';
                var thead = '<th>MODEL_NAME</th>';

                var giangModel = [];
                var giangShift = {};
                if (!$.isEmptyObject(data)) {
                    var caKeys = Object.keys(data);
                    for (i in caKeys) {
                        giangShift[caKeys[i]] = [];
                        if (!$.isEmptyObject(data[caKeys[i]])) {
                            var modelKeys = Object.keys(data[caKeys[i]]);
                            for (j in modelKeys) {
                                var index = giangModel.indexOf(modelKeys[j]);
                                if (index > -1) {
                                    giangShift[caKeys[i]][index] = data[caKeys[i]][modelKeys[j]];
                                } else {
                                    giangModel.push(modelKeys[j]);
                                    giangShift[caKeys[i]][giangModel.length - 1] = data[caKeys[i]][modelKeys[j]];
                                }
                            }
                        }
                    }

                    for (i in Object.keys(giangShift)) {
                        thead += '<th>' + Object.keys(giangShift)[i] + '</th>';
                    }
                    $('#checkOutTable>thead>tr').append(thead);
                    var classRate;
                    for (j in giangModel) {
                        body += '<tr><td class="bg-dark-gray">' + giangModel[j] + '</td>';
                        for (i in Object.keys(giangShift)) {
                            if ((Object.values(giangShift)[i])[j] === undefined || (Object.values(giangShift)[i])[j] === null) {
                                body += '<td></td>';
                            }
                            else {
                                if ((Object.values(giangShift)[i])[j].yieldRate >= 92) {
                                    classRate = "rate-green";
                                }
                                else if ((Object.values(giangShift)[i])[j].yieldRate > 70 && (Object.values(giangShift)[i])[j].yieldRate < 92) {
                                    classRate = "rate-yellow";
                                }
                                else {
                                    classRate = "rate-red";
                                }
                                body += '<td class=' + classRate + '>' + (Object.values(giangShift)[i])[j].yieldRate.toFixed(2) + '% <span class="rate-percent">(' + (Object.values(giangShift)[i])[j].output + '/' + (Object.values(giangShift)[i])[j].input + ')</span></td>';
                            }
                        }
                    }
                    body += '</tr>';
                }
                $('#checkOutTable>tbody').append(body);
            },
            error: function () {
                alert("Fail!");
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
        var blobURL = tableToExcel('checkOutTable', 'test_table');
        $(this).attr('download', 'RE Checkout (' + startDateCode + ' - ' + endDateCode + ').xls')
        $(this).attr('href', blobURL);
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
        // var spcTS = getTimeSpan();
        // $('.datetimerange').data('daterangepicker').setStartDate(spcTS.startDate);
        // $('.datetimerange').data('daterangepicker').setEndDate(spcTS.endDate);
        getTimeNow();
        $('input[name=timeSpan]').on('change', function () {
            dataset.timeSpan = this.value;
            init();
        });
    });

    function exportDataCheckOut() {
        // var modelN = $('#slModelName').val();
        // if (modelN == '' || modelN == null) {
        //     alert("Please select model name!");
        // } else {
        window.location.href = '/api/re/online/importData?factory=' + dataset.factory + '&timeSpan=' + dataset.timeSpan + '';
        // }
        //  console.log(dataset)

    }
    // FUNCTION
    function chartInPutOutPut(idHtml, input, output, remain, yield_rate) {
        var textTitle = "";
        if (idHtml == "in_out_si") {
            textTitle = 'RE SI In_Out Trend Chart';
        }
        if (idHtml == "in_out_smt") {
            textTitle = 'RE SMT In_Out Trend Chart';
        }
        Highcharts.chart(idHtml, {
            chart: {
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: textTitle,
                style: {
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category',
                // tickInterval: Math.ceil(keys.length / 7),
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
            }, {
                name: 'Remain',
                type: 'line',
                data: remain,
                color: Highcharts.getOptions().colors[6]
            }]
        });

    }


    function highChartDrippdownReason(totalData, dataDrip, idHtml, titleChart) {
        Highcharts.chart(idHtml, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: titleChart,
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
                data: totalData,
                colorByPoint: true
            }],
            drilldown: {
                series: dataDrip
            }
        });
    }

    function highchartByColum(dataChart, idHtml, titleChart) {
        Highcharts.chart(idHtml, {
            chart: {
                type: 'column'
            },
            title: {
                text: titleChart,
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category',
                // tickInterval: Math.ceil(keys.length / 7)
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
    }
</script>