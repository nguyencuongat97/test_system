<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/checkOut.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<link rel="stylesheet" href="/assets/css/custom/style.css" />
<!-- <link rel="stylesheet" href="/assets/css/custom/theme.css" /> -->

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span class="text-uppercase" name="factory">${factory}</span><span>-BBD-RE Repair Check In/Out
                    Management</span></b>
        </div>
        <div class="row no-margin">
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

        <div class="row no-margin">
            <div class="col-md-12 no-padding">
                <div class="panel panel-flat panel-body chart-sm" id="in_out_trend"></div>
            </div>
        </div>
        <div class="row no-margin">
            <div class="col-md-12 no-padding">
                <div class="panel panel-flat panel-body chart-sm" id="chart_remain"></div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8">
                <div class="panel panel-flat panel-body chart-sm" id="chart_checkin_out"></div>
            </div>
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="chart_fail_qty"></div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8">
                <div class="panel panel-flat panel-body chart-sm" id="chart_bonepile"></div>
            </div>
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="pie_chart"></div>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-6">
                <div class="panel panel-flat panel-body chart-sm" id="si_section"></div>
            </div>
            <div class="col-xs-12 col-sm-6">
                <div class="panel panel-flat panel-body chart-sm" id="smt_section"></div>
            </div>
            <!-- <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="pth_section"></div>
            </div> -->
        </div>

        <div class="row">
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="detected_pieChart"></div>
            </div>
            <div class="col-xs-12 col-sm-4">
                <div class="panel panel-flat panel-body chart-sm" id="detected_columnChart"></div>
            </div>
            <div class="col-xs-12 col-sm-4 process">
                <div class="panel panel-flat panel-body chart-sm" id="detail_columnChart"></div>
            </div>
            <div class="col-xs-12 col-sm-4 processDetail hidden" style="position: relative;">
                <div class="panel panel-flat panel-body chart-sm" id="test"></div>
                <button class="btnBackto" style="position: absolute;right: 12px;top: 8px;">◁ Back to</button>
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
<script>
    var dataset = {
        factory: '${factory}'
    }

    //init();

    function init() {
        getTotalInOut();
        loadChartCheckin_out();
        loadChartBonePile();
        getCheckoutByDay();
        getInOutBySection('SI');
        getDataSectionSmt();
        // if (dataset.factory == "B04") {
        //     getInOutBySection('SI');
        //     getInOutBySection('SMT');
        //     getInOutBySection('PTH');
        // } else {
        //     getInOutBySection('TEST');
        //     getInOutBySection('SMT');
        //     getInOutBySection('PTH');
        // }
        loadDetectedData();
        loadTableData();

    }

    function getTotalInOut() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/total_in_out",
            data: {
                factory: dataset.factory,
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var input = [],
                    output = [],
                    rate = [],
                    remain = [];
                if (!$.isEmptyObject(data)) {
                    for (i in data.input) {
                        var obj = {
                            name: i,
                            y: data.input[i]
                        };
                        input.push(obj);

                        if (data.output[i] != undefined) {
                            if (data.input[i] != 0) {
                                var yieldRate = data.output[i] / data.input[i] * 100;
                                rate.push({
                                    name: i,
                                    y: Number(yieldRate.toFixed(2))
                                });
                            } else {
                                rate.push({
                                    name: i,
                                    y: 0
                                });
                            }
                            var remainNumber = data.input[i] - data.output[i];
                            remain.push({
                                name: i,
                                y: Number(remainNumber)
                            });
                        }
                    }
                    for (i in data.output) {
                        var obj = {
                            name: i,
                            y: data.output[i]
                        };
                        output.push(obj);
                    }
                }
                // console.log('input: ',input);
                // console.log('output: ',output);
                // console.log('rate: ',rate);
                // console.log('remain: ',remain);

                loadInOutTrendChart(input, output, rate);
                loadChartRemain(remain);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadInOutTrendChart(input, output, rate) {
        Highcharts.chart('in_out_trend', {
            chart: {
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: 'RE Total In/Out Trend Chart',
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
                name: 'Input',
                type: 'column',
                data: input
            }, {
                name: 'Output',
                type: 'column',
                data: output
            }, {
                name: 'Yield Rate',
                type: 'line',
                yAxis: 1,
                data: rate,
                tooltip: {
                    valueSuffix: '%'
                },
                dataLabels: {
                    enabled: true,
                    format: '{y}%'
                }
            },]
        });
    }

    function loadChartRemain(remain) {
        Highcharts.chart('chart_remain', {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: 'RE Remain Trend Chart By Daily',
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
                data: remain
            }],
        });
    }

    function getInOutBySection(section) {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_in_out_by_section",
            data: {
                factory: dataset.factory,
                section: section
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var input = [],
                    output = [],
                    rate = [],
                    remain = [];
                if (!$.isEmptyObject(data)) {
                    for (i in data.input) {
                        var obj = {
                            name: i,
                            y: data.input[i]
                        };
                        input.push(obj);

                        if (data.output[i] != undefined) {
                            if (data.input[i] != 0) {
                                var yieldRate = data.output[i] / data.input[i] * 100;
                                rate.push({
                                    name: i,
                                    y: Number(yieldRate.toFixed(2))
                                });
                            } else {
                                rate.push({
                                    name: i,
                                    y: 0
                                });
                            }
                            var remainNumber = data.input[i] - data.output[i];
                            if (remainNumber > 0) {
                                remain.push({
                                    name: i,
                                    y: Number(remainNumber)
                                });
                            } else {
                                remain.push({
                                    name: i,
                                    y: 0
                                });
                            }
                        }
                    }
                    for (i in data.output) {
                        var obj = {
                            name: i,
                            y: data.output[i]
                        };
                        output.push(obj);
                    }
                }
                if (dataset.factory == "B06" && section == "TEST") {
                    section = "SI";
                }
                Highcharts.chart(section.toLowerCase() + '_section', {
                    chart: {
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE ' + section + ' Total In/Out Trend Chart',
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
                        min: 0,
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
                        name: 'Input',
                        type: 'column',
                        data: input
                    }, {
                        name: 'Output',
                        type: 'column',
                        data: output
                    }, {
                        name: 'Remain',
                        type: 'column',
                        data: remain
                    }, {
                        name: 'Yield Rate',
                        type: 'line',
                        yAxis: 1,
                        data: rate,
                        tooltip: {
                            valueSuffix: '%'
                        },
                        dataLabels: {
                            enabled: true,
                            format: '{y}%'
                        }
                    }]
                });

            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadChartCheckin_out() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_in_out_weekly",
            data: {
                factory: dataset.factory,
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var input = [],
                    output = [];
                if (!$.isEmptyObject(data)) {
                    for (i in data.input) {
                        var obj = {
                            name: i,
                            y: data.input[i]
                        };
                        input.push(obj);
                    }
                    for (i in data.output) {
                        var obj = {
                            name: i,
                            y: data.output[i]
                        };
                        output.push(obj);
                    }
                }

                Highcharts.chart('chart_checkin_out', {
                    chart: {
                        type: 'line'
                    },
                    title: {
                        text: 'RE - Checkin / Checkout',
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
                            dataLabels: {
                                enabled: true,
                            }
                        }
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '11px',
                        },
                        layout: 'horizontal',
                        align: 'left',
                        verticalAlign: 'bottom',
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
                        name: 'Checkout',
                        data: output,
                    }, {
                        name: 'Checkin',
                        data: input,
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadChartBonePile() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/bonepile",
            data: {
                factory: dataset.factory,
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var dataChart = [];
                if (!$.isEmptyObject(data)) {
                    for (i in data) {
                        var obj = {
                            name: i,
                            y: data[i]
                        };
                        dataChart.push(obj);
                    }
                }

                Highcharts.chart('chart_bonepile', {
                    chart: {
                        type: 'line'
                    },
                    title: {
                        text: 'RE - Bone Pile',
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
                        tickAmount: 4,
                    },
                    yAxis: {
                        title: {
                            text: '',
                            style: {
                                fontSize: '16px',
                                fontWeight: 'bold'
                            }
                        },
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                            }
                        }
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '11px',
                        },
                        layout: 'horizontal',
                        align: 'left',
                        verticalAlign: 'bottom',
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
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function getCheckoutByDay() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_checkout_by_day",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var reason = [],
                    checkout = [];
                if (!$.isEmptyObject(data)) {
                    for (i in data.reason) {
                        var obj = {
                            name: i,
                            y: data.reason[i]
                        };
                        reason.push(obj);
                    }
                    var tmp = 0;
                    for (i in data.checkout) {
                        var obj = {
                            name: i,
                            y: data.checkout[i]
                        };
                        checkout.push(obj);
                        tmp++;
                        if (tmp == 10) {
                            break;
                        }
                    }
                }
                // console.log('reason', reason);
                loadPieChart(reason);
                loadColumnChart(checkout);

            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadPieChart(dataChart) {
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
                text: 'Fail QTY By Rootcause',
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
                    fontSize: '9px',
                    fontWeight: '100'
                },
                layout: 'right',
                align: 'right',
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
    }

    function loadColumnChart(dataChart) {
        Highcharts.chart('chart_fail_qty', {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: 'Fail Qty By Model',
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

    function getDataSectionSmt() {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_in_out_by_section_smt",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var input = [],
                    output = [],
                    rate = [],
                    remain = [];
                if (!$.isEmptyObject(data)) {
                    for (i in data.input) {
                        var obj = {
                            name: i,
                            y: data.input[i]
                        };
                        input.push(obj);

                        if (data.output[i] != undefined) {
                            if (data.input[i] != 0) {
                                var yieldRate = data.output[i] / data.input[i] * 100;
                                rate.push({
                                    name: i,
                                    y: Number(yieldRate.toFixed(2))
                                });
                            } else {
                                rate.push({
                                    name: i,
                                    y: 0
                                });
                            }
                            var remainNumber = data.input[i] - data.output[i];
                            // remain.push({ name: i, y: Number(remainNumber) });
                            if (remainNumber > 0) {
                                remain.push({
                                    name: i,
                                    y: Number(remainNumber)
                                });
                            } else {
                                remain.push({
                                    name: i,
                                    y: 0
                                });
                            }
                        }
                    }
                    for (i in data.output) {
                        var obj = {
                            name: i,
                            y: data.output[i]
                        };
                        output.push(obj);
                    }
                }
                chartColum("smt_section", "RE SMT/PTH Total In/Out Trend Chart", input, output, remain, rate)
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
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
                            } else {
                                if ((Object.values(giangShift)[i])[j].yieldRate >= 92) {
                                    classRate = "rate-green";
                                } else if ((Object.values(giangShift)[i])[j].yieldRate > 70 && (Object.values(giangShift)[i])[j].yieldRate < 92) {
                                    classRate = "rate-yellow";
                                } else {
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
                var startDate = moment(current).add(-1, "day").format("YYYY/MM/DD") + ' 00:00:00';
                var endDate = moment(current).format("YYYY/MM/DD") + ' 00:00:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
                dataset.timeSpan = startDate + " - " + endDate;
                init();
                $('input[name=timeSpan]').on('change', function () {
                    console.log()
                    dataset.timeSpan = this.value;
                    init();
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    var tableToExcel = (function () {
        var uri = 'data:application/vnd.ms-excel;base64,',
            template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>',
            base64 = function (s) {
                return window.btoa(unescape(encodeURIComponent(s)))
            },
            format = function (s, c) {
                return s.replace(/{(\w+)}/g, function (m, p) {
                    return c[p];
                })
            }
        return function (table, name) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {
                worksheet: name || 'Worksheet',
                table: table.innerHTML
            }
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

    $(document).ready(function () {
        getTimeNow();
    });

    function exportDataCheckOut() {
        window.location.href = '/api/re/online/importData?factory=' + dataset.factory + '&timeSpan=' + dataset.timeSpan + '';
    }



    function chartColum(id, htmlTitle, input, output, remain, rate) {
        Highcharts.chart(id, {
            chart: {
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: htmlTitle,
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
                min: 0,
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
                name: 'Input',
                type: 'column',
                data: input
            }, {
                name: 'Output',
                type: 'column',
                data: output
            }, {
                name: 'Remain',
                type: 'column',
                data: remain
            }, {
                name: 'Yield Rate',
                type: 'line',
                yAxis: 1,
                data: rate,
                tooltip: {
                    valueSuffix: '%'
                },
                dataLabels: {
                    enabled: true,
                    format: '{y}%'
                }
            }]
        });
    }
    var dataDetected;

    var total;

    function loadDetectedData() {
        $.ajax({
            type: "GET",
            // url: "http://10.224.56.46:8888/api/re/online/ProcessNTF",
            url: "/api/re/online/ProcessNTF",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var data = res.result;
                dataDetected = data;
                if (data) {
                    total = data.TOTAL;
                    var dataPieChart = [{
                        name: 'NTF',
                        y: data.NTF
                    }, {
                        name: 'COMPONENT',
                        y: data.COMPONENT
                    }, {
                        name: 'PROCESS',
                        y: data.PROCESS
                    },]
                    loadDetectedPieChart(dataPieChart);
                    loadDetectedColumChart('NTF');
                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadDetectedPieChart(dataChart) {
        Highcharts.chart('detected_pieChart', {
            chart: {
                type: 'pie',
                options3d: {
                    enabled: true,
                    alpha: 45,
                    beta: 0
                }
            },
            title: {
                text: 'RE Total Repair Check out by Defect Status',
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
                        enabled: true,
                        format: '{point.percentage:.1f} %',
                    },
                    point: {
                        events: {
                            click: function (event) {
                                var defected = this.name;
                                loadDetectedColumChart(defected)
                            }
                        }
                    },
                    showInLegend: true
                }
            },
            legend: {
                itemStyle: {
                    fontSize: '9px',
                    fontWeight: '100'
                },
                layout: 'right',
                align: 'right',
                verticalAlign: 'bottom'
            },
            navigation: {
                buttonOptions: {
                    enabled: false
                }
            },
            tooltip: {
                pointFormat: '<b>{point.y}/' + total + '</b>',
                style: {
                    zindex: 9999
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

    function loadDetectedColumChart(defected) {
        var data = dataDetected['DATA_' + defected];
        var dataChart = [];
        for (i in data) {
            dataChart.push({
                name: i,
                y: data[i]
            });
        }
        var modelName = Object.keys(data);
        loadDetectedDetailData(defected, modelName[0])
        Highcharts.chart('detected_columnChart', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Repair Check out by Defect ' + defected,
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
                                loadDetectedDetailData(defected, model_name);
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
                name: ' ',
                data: dataChart
            }]
        });
    }
    var getDetected, getModelName;

    function loadDetectedDetailData(defected, modelName) {
        getDetected = defected;
        getModelName = modelName;
        $.ajax({
            type: "GET",
            // url: "http://10.224.56.46:8888/api/re/online/defected/detail",
            url: "/api/re/online/defected/detail",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                defected: defected,
                modelName: modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var data = res.result;
                var dataChart = [];
                if (data) {
                    for (i in data) {
                        dataChart.push({
                            name: i,
                            y: data[i]
                        });
                    }
                }
                loadDetectedDetailChart(dataChart, modelName, defected)
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadDetectedDetailChart(dataChart, modelName, defected) {
        var titleChart = "";
        if (defected == 'NTF') {
            titleChart += 'Error Code BY ' + modelName;
        } else if (defected == 'COMPONENT') {
            titleChart += 'Location Code BY ' + modelName;
        } else {
            titleChart += 'Reason Code BY ' + modelName;
        }
        Highcharts.chart('detail_columnChart', {
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
                type: 'category'
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
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function (event) {
                                var reasonCode = this.name;
                                // loadDetectedDetailData(defected, model_name);
                                // loadProcessData();
                                // $('.process').addClass('hidden');
                                // $('.processDetail').removeClass('hidden');
                                if (getDetected == 'PROCESS') {
                                    loadProcessData(reasonCode, getModelName)
                                    $('.process').addClass('hidden');
                                    $('.processDetail').removeClass('hidden');
                                }
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
                data: dataChart
            }]
        });
    }

    function loadProcessData(reasonCode, modelName) {
        var dataChart = [];
        $.ajax({
            type: "GET",
            // url: "http://10.224.56.46:8888/api/re/online/defected/reason/detail",
            url: "/api/re/online/defected/reason/detail",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                reasonCode: reasonCode,
                modelName: modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var data = res.result;
                if (data) {
                    for (i in data) {
                        dataChart.push({
                            name: i,
                            y: data[i]
                        });
                    }
                    loadProcessChart(dataChart, reasonCode);
                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadProcessChart(dataChart, reasonCode) {
        Highcharts.chart('test', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Error code BY '+ reasonCode,
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category'
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
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.1f}%'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
            },

            series: [{
                name: "Browsers",
                colorByPoint: true,
                data: dataChart
            }]
        });
    }
    $('.btnBackto').on('click', function () {
        $('.process').removeClass('hidden');
        $('.processDetail').addClass('hidden');
    });
</script>