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
        <div class="row" style="margin:unset">
            <div class=" panel panel-overview input-group" style="width:65%; margin-bottom: 5px; background: #333;float: left;">
                <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                        class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                        style="height: 33px; color: #fff;">
            </div>
            <div class=" panel chooseModel" style="width: 26%;margin: 0 1% 5px;float: left;">
                <select class=" form-control chooseModelExport" id="selectModel"  style="height: 33px; color: #fff;">
                    <option value="" disabled="" selected="">Choose Model Export</option>
                </select>
            </div>
            <!-- <div class=" wrapperBtnExport"> -->
                <a class=" btn btn-lg" id="btnExport" style="width:7%;font-size: 13px; padding: 7px; border-radius: 5px; color: #fff;background: #444;">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            <!-- </div> -->
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-md-12" style="padding: unset;">
                <div class="panel panel-flat panel-body chart-sm" id="in_out_trend"></div>
                <div class="panel panel-flat panel-body chart-sm" id="in_out_si"></div>
                <div class="panel panel-flat panel-body chart-sm" id="in_out_smt"></div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-3">
                <div class="panel panel-flat panel-body chart-sm" id="defected_status"></div>
            </div>
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="defected_issues"></div>
            </div>
            <div class="col-xs-12 col-sm-5">
                <div class="panel panel-flat panel-body chart-sm" id="column_chart"></div>
            </div>
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
    </div>
</div>
<style>
    .chooseModel{
        float: left;
        background: #333;
        /* margin-left: 9px;
        margin-right: 9px; */
    }
    .chooseModelExport{
        border-bottom: none;
    }
    /* #btnExport{
        float: left;
        width: 93px;
    } */
    .highcharts-button-box{
        display: none !important;
    }
    /* #output_section .highcharts-container .highcharts-root .highcharts-button-normal .highcharts-button-box{
        display: none !important;
    } */
</style>
<script>
    // var dataset = {
    //     factory: 'B04'
    // }

    init();

    function init() {
        loadInOutTrendChart();
        loadInOutSIChart();
        loadInOutSMTChart();
        wipDefectedStatus();
        loadOutputSection();
    }

    function loadInOutTrendChart() {
        // var dt = {
        //     factory: dataset.factory,
        //     timeSpan: dataset.timeSpan
        // };
        // console.log(dt)
        // $.ajax({
        //     type: "GET",
        //     url: "/api/re/status/daily",
        //     data: dt,
        //     contentType: "application/json; charset=utf-8",
        //     success: function (data) {
            var data={
                "11/21 - 11/28": {
                    "id": 42518,
                    "factory": "B04",
                    "modelName": "T99X171.00",
                    "sectionName": "SI",
                    "startDate": "2019-11-21 07:30:00",
                    "endDate": "2019-11-21 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1212,
                    "output": 1514,
                    "remain": -302,
                    "tat": 0,
                    "yieldRate": 124.91749
                },
                "11/28 - 12/05": {
                    "id": 44477,
                    "factory": "B04",
                    "modelName": "40-2411-3006",
                    "sectionName": "SI",
                    "startDate": "2019-11-28 07:30:00",
                    "endDate": "2019-11-28 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1002,
                    "output": 1509,
                    "remain": -506,
                    "tat": 0,
                    "yieldRate": 150.5988
                },
                "12/05 - 12/12": {
                    "id": 46270,
                    "factory": "B04",
                    "modelName": "40-2411-1004",
                    "sectionName": "SI",
                    "startDate": "2019-12-05 07:30:00",
                    "endDate": "2019-12-05 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1049,
                    "output": 1441,
                    "remain": -392,
                    "tat": 0,
                    "yieldRate": 137.36893
                },
                "12/12": {
                    "id": 48512,
                    "factory": "B04",
                    "modelName": "P100009060-1",
                    "sectionName": "SI",
                    "startDate": "2019-12-12 07:30:00",
                    "endDate": "2019-12-12 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1095,
                    "output": 1559,
                    "remain": -464,
                    "tat": 0,
                    "yieldRate": 142.37444
                },
                "12/13": {
                    "id": 48733,
                    "factory": "B04",
                    "modelName": "40-1563-0003",
                    "sectionName": "SMT",
                    "startDate": "2019-12-13 07:30:00",
                    "endDate": "2019-12-13 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 892,
                    "output": 1410,
                    "remain": -518,
                    "tat": 0,
                    "yieldRate": 158.07175
                },
                "12/14": {
                    "id": 48911,
                    "factory": "B04",
                    "modelName": "40-1563-0003",
                    "sectionName": "SMT",
                    "startDate": "2019-12-14 07:30:00",
                    "endDate": "2019-12-14 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1040,
                    "output": 1134,
                    "remain": -94,
                    "tat": 0,
                    "yieldRate": 109.03846
                },
                "12/15": {
                    "id": 49123,
                    "factory": "B04",
                    "modelName": "P100009070-1",
                    "sectionName": "SI",
                    "startDate": "2019-12-15 07:30:00",
                    "endDate": "2019-12-15 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 919,
                    "output": 1150,
                    "remain": -231,
                    "tat": 0,
                    "yieldRate": 125.13602
                }
            };
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
        //     },
        //     failure: function (errMsg) {
        //         console.log(errMsg);
        //     }
        // });
    }
    function loadInOutSIChart() {
        // var dt = {
        //     factory: dataset.factory,
        //     timeSpan: dataset.timeSpan
        // };
        // console.log(dt)
        // $.ajax({
        //     type: "GET",
        //     url: "/api/re/status/daily",
        //     data: dt,
        //     contentType: "application/json; charset=utf-8",
        //     success: function (data) {
            var data={
                "12/13": {
                    "id": 48733,
                    "factory": "B04",
                    "modelName": "40-1563-0003",
                    "sectionName": "SMT",
                    "startDate": "2019-12-13 07:30:00",
                    "endDate": "2019-12-13 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 892,
                    "output": 1410,
                    "remain": -518,
                    "tat": 0,
                    "yieldRate": 158.07175
                },
                "12/14": {
                    "id": 48911,
                    "factory": "B04",
                    "modelName": "40-1563-0003",
                    "sectionName": "SMT",
                    "startDate": "2019-12-14 07:30:00",
                    "endDate": "2019-12-14 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1040,
                    "output": 1134,
                    "remain": -94,
                    "tat": 0,
                    "yieldRate": 109.03846
                },
                "12/15": {
                    "id": 49123,
                    "factory": "B04",
                    "modelName": "P100009070-1",
                    "sectionName": "SI",
                    "startDate": "2019-12-15 07:30:00",
                    "endDate": "2019-12-15 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 919,
                    "output": 1150,
                    "remain": -231,
                    "tat": 0,
                    "yieldRate": 125.13602
                },
                "12/16": {
                    "id": 49275,
                    "factory": "B04",
                    "modelName": "P100009070-1",
                    "sectionName": "SI",
                    "startDate": "2019-12-16 07:30:00",
                    "endDate": "2019-12-16 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1064,
                    "output": 1422,
                    "remain": -358,
                    "tat": 0,
                    "yieldRate": 133.64662
                },
                "12/17": {
                    "id": 49513,
                    "factory": "B04",
                    "modelName": "P000250860",
                    "sectionName": "SI",
                    "startDate": "2019-12-17 07:30:00",
                    "endDate": "2019-12-17 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1062,
                    "output": 1404,
                    "remain": -342,
                    "tat": 0,
                    "yieldRate": 132.20338
                },
                "12/18": {
                    "id": 49705,
                    "factory": "B04",
                    "modelName": "40-2411-1001",
                    "sectionName": "SI",
                    "startDate": "2019-12-18 07:30:00",
                    "endDate": "2019-12-18 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 457,
                    "output": 718,
                    "remain": -261,
                    "tat": 0,
                    "yieldRate": 157.1116
                }
            };
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
                Highcharts.chart('in_out_si', {
                    chart: {
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE SI In_Out Trend Chart',
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
        //     },
        //     failure: function (errMsg) {
        //         console.log(errMsg);
        //     }
        // });
    }
    function loadInOutSMTChart() {
        // var dt = {
        //     factory: dataset.factory,
        //     timeSpan: dataset.timeSpan
        // };
        // console.log(dt)
        // $.ajax({
        //     type: "GET",
        //     url: "/api/re/status/daily",
        //     data: dt,
        //     contentType: "application/json; charset=utf-8",
        //     success: function (data) {
            var data={
                "11/21 - 11/28": {
                    "id": 42518,
                    "factory": "B04",
                    "modelName": "T99X171.00",
                    "sectionName": "SI",
                    "startDate": "2019-11-21 07:30:00",
                    "endDate": "2019-11-21 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1212,
                    "output": 1514,
                    "remain": -302,
                    "tat": 0,
                    "yieldRate": 124.91749
                },
                "11/28 - 12/05": {
                    "id": 44477,
                    "factory": "B04",
                    "modelName": "40-2411-3006",
                    "sectionName": "SI",
                    "startDate": "2019-11-28 07:30:00",
                    "endDate": "2019-11-28 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1002,
                    "output": 1509,
                    "remain": -506,
                    "tat": 0,
                    "yieldRate": 150.5988
                },
                "12/05 - 12/12": {
                    "id": 46270,
                    "factory": "B04",
                    "modelName": "40-2411-1004",
                    "sectionName": "SI",
                    "startDate": "2019-12-05 07:30:00",
                    "endDate": "2019-12-05 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1049,
                    "output": 1441,
                    "remain": -392,
                    "tat": 0,
                    "yieldRate": 137.36893
                },
                "12/12": {
                    "id": 48512,
                    "factory": "B04",
                    "modelName": "P100009060-1",
                    "sectionName": "SI",
                    "startDate": "2019-12-12 07:30:00",
                    "endDate": "2019-12-12 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1095,
                    "output": 1559,
                    "remain": -464,
                    "tat": 0,
                    "yieldRate": 142.37444
                },
                "12/13": {
                    "id": 48733,
                    "factory": "B04",
                    "modelName": "40-1563-0003",
                    "sectionName": "SMT",
                    "startDate": "2019-12-13 07:30:00",
                    "endDate": "2019-12-13 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 892,
                    "output": 1410,
                    "remain": -518,
                    "tat": 0,
                    "yieldRate": 158.07175
                },
                "12/14": {
                    "id": 48911,
                    "factory": "B04",
                    "modelName": "40-1563-0003",
                    "sectionName": "SMT",
                    "startDate": "2019-12-14 07:30:00",
                    "endDate": "2019-12-14 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 1040,
                    "output": 1134,
                    "remain": -94,
                    "tat": 0,
                    "yieldRate": 109.03846
                },
                "12/15": {
                    "id": 49123,
                    "factory": "B04",
                    "modelName": "P100009070-1",
                    "sectionName": "SI",
                    "startDate": "2019-12-15 07:30:00",
                    "endDate": "2019-12-15 08:30:00",
                    "status": "REPAIRED",
                    "total": 0,
                    "input": 919,
                    "output": 1150,
                    "remain": -231,
                    "tat": 0,
                    "yieldRate": 125.13602
                }
            };
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
                Highcharts.chart('in_out_smt', {
                    chart: {
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE SMT In_Out Trend Chart',
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
    }
    function wipDefectedStatus(){
    // $.ajax({
    //     type: "GET",
    //     url: "/api/re/output/section",
    //     data: {
    //         factory: dataset.factory,
    //         timeSpan: dataset.timeSpan
    //     },
    //     contentType: "application/json; charset=utf-8",
    //     success: function (data) {
    var data= {
    "NTF": {
        "data": [{
                    "key": "40-2412-1000",
                    "value": 50
                },
                {
                    "key": "40-1563-1005",
                    "value": 18
                },
                {
                    "key": "P100009070-1",
                    "value": 12
                },
                {
                    "key": "25-1546T00",
                    "value": 8
                },
                {
                    "key": "25-2402T11-1",
                    "value": 1
                },
                {
                    "key": "26-1552-0004",
                    "value": 5
                },
                {
                    "key": "40-2411-1001",
                    "value": 8
                },
                {
                    "key": "849964/35RJ",
                    "value": 3
                }],
        "size": 110
    },
    "PROCESS": {
            "data": [{
                "key": "40-1563-1005",
                "value": 26
                },  
                {
                "key": "40-2412-1000",
                "value": 5
                },
                {
                "key": "P100009070-1",
                "value": 9
                }
            ],
            "size": 57
        },
    "COMPONENT": {
        "data": [{
            "key": "40-1563-1005",
            "value": 26
            },  
            {
            "key": "40-2412-1000",
            "value": 5
            },
            {
            "key": "P100009070-1",
            "value": 9
            }
        ],
        "size": 57
    },
    "OTHER": {
            "data": [{
                "key": "40-1563-1005",
                "value": 26
                },  
                {
                "key": "40-2412-1000",
                "value": 5
                },
                {
                "key": "P100009070-1",
                "value": 9
                }
            ],
            "size": 59
        }
}
        //console.log("loadOutputSection:", data)
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
        Highcharts.chart('defected_status', {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    
                }
            },
            title: {
                // text: 'RE TOTAL REPAIR CHECK OUT BY DEFECT STATUS',
                text: 'RE TOTAL REPAIR CHECK OUT BY DEFECT STATUS',
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
                                loadDefectedIssues(section_name, drilldown);
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
        loadDefectedIssues(drilldown[0].name, drilldown);
//     },
//     failure: function (errMsg) {
//         console.log(errMsg);
//     }
// });
}
function loadDefectedIssues(section_name, drilldown){
    var drillData;
        for (i in drilldown) {
            if (drilldown[i].name == section_name) {
                drillData = drilldown[i].data;
            }
        }
    //console.log("section name: ",section_name);
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
                                loadColumnschart1(model_name, section_name)
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
        loadColumnschart1(drillData[0].name, section_name);
}
function loadColumnschart1(model_name, section_name) {
    var data={
            "C004": 11,
            "C008":30,
            "C009": 35,
            "C012": 11,
            "E0V0005": 17,
            "E0V0008": 52,
            "E0V0021": 17,
            "E0V0028": 13,
            "E0V0034": 17,
            "LG349": 10,
            "LG598": 28,
            "LG677": 23
        }
        if (!$.isEmptyObject(data)) {
                    var keys = Object.keys(data);
                    var dataChart = new Array(keys.length - 1);
                    var categories = new Array(keys.length - 1);
                    for (i = 1; i < keys.length; i++) {
                        categories[i - 1] = keys[i];
                        dataChart[i - 1] = { name: keys[i], y: data[keys[i]] };
                    }
                }
        Highcharts.chart('column_chart', {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: 'RE Repair Check out by Defect ' + section_name + ' (Top 15)',
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
                colorByPoint: true,
                data: dataChart,
                marker: {
                    enabled: false
                },
            }]
        });

}
function loadOutputSection() {
    // $.ajax({
    //     type: "GET",
    //     url: "/api/re/output/section",
    //     data: {
    //         factory: dataset.factory,
    //         timeSpan: dataset.timeSpan
    //     },
    //     contentType: "application/json; charset=utf-8",
    //     success: function (data) {
    var data= {
    "SI": {
        "data": [{
                    "key": "40-2412-1000",
                    "value": 50
                },
                {
                    "key": "40-1563-1005",
                    "value": 18
                },
                {
                    "key": "P100009070-1",
                    "value": 12
                },
                {
                    "key": "25-1546T00",
                    "value": 8
                },
                {
                    "key": "25-2402T11-1",
                    "value": 1
                },
                {
                    "key": "26-1552-0004",
                    "value": 5
                },
                {
                    "key": "40-2411-1001",
                    "value": 8
                },
                {
                    "key": "849964/35RJ",
                    "value": 3
                }],
        "size": 110
    },
    "SMT": {
            "data": [{
                "key": "40-1563-1005",
                "value": 26
                },  
                {
                "key": "40-2412-1000",
                "value": 5
                },
                {
                "key": "P100009070-1",
                "value": 9
                }
            ],
            "size": 57
    },
    "PTH": {
            "data": [{
                "key": "40-1563-1005",
                "value": 26
                },  
                {
                "key": "40-2412-1000",
                "value": 15
                },
                {
                "key": "P100009070-1",
                "value": 9
                }
            ],
            "size": 80
        }
    }
        //console.log("loadOutputSection:", data)
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
            // exporting: {
            //     buttons: {
            //         contextButton: {
            //             enabled: false
            //         }
            //     }
            // },
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
//     },
//     failure: function (errMsg) {
//         console.log(errMsg);
//     }
// });
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
                        color: '#f0f0f3',
                        fontWeight: 'bold',
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
    // $.ajax({
    //     type: 'GET',
    //     url: '/api/re/under-repair/model',
    //     data: {
    //         factory: dataset.factory,
    //         timeSpan: dataset.timeSpan,
    //         modelName: model_name
    //     },
    //     success: function (data) {
            var data={
                "C004": 11,
                "C008":30,
                "C009": 35,
                "C012": 11,
                "E0V0005": 17,
                "E0V0008": 52,
                "E0V0021": 17,
                "E0V0028": 13,
                "E0V0034": 17,
                "LG349": 10,
                "LG598": 28,
                "LG677": 23
            }
            console.log("pie:",data)
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
    //     },
    //     failure: function (errMsg) {
    //         console.log(errMsg);
    //     }
    // });
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
</script>