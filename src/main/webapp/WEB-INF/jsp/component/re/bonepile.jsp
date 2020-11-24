<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/bonepile.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header" style="font-size: 18px;">
            <b><span>B04-BBD-RE Bone Pile Management</span></b>
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
        <!-- <div class="row" style="margin: unset;">
            <div class="col-lg-8 panel panel-overview input-group"
                style="margin-bottom: 5px; background: #333; float: left; height: 40px;">
                <span class="input-group-addon" style="padding: 0px 10px; color: #fff;"><i
                        class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                    style="color: #fff;">
            </div>
            <div class="col-lg-4 panel slModel"
                style="background: #3b3b3b; margin-left: 4px; width: 33%; height: 40px;">
                <select class="selectpicker" id="slModelName" data-live-search="true" data-size="7"
                    style="color: #fff;">

                </select>
                <button class="btn" onclick="exportDataDetailModelname()"
                    style="background: #bfbdbd; float: right; margin-top: 3px !important; height: 33px;">Export</button>
            </div>
        </div> -->
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-6 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="kpi"></div>
            </div>
            <div class="col-xs-12 col-sm-6 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="waiting-repair"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="status"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="top15"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="trending"></div>
            </div>
            <!-- <div class="col-xs-12 col-sm-12 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="in_out_trend"></div>
            </div> -->
        </div>

    </div>
</div>
<style>
    .slModel .bootstrap-select {
        width: 68% !important;
    }

    .slModel .bootstrap-select .dropdown-toggle.btn-default {
        color: #fff;
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
</style>
<script>
    var dataset = {
        factory: 'B04'
    }

    init();

    function init() {
        loadBonePileKPI();
        loadWaitingRepair();
        loadPieChart();
        loadTop15Model();
        loadBonePileTrend();
    }

    function loadBonePileKPI() {
        $.ajax({
            type: "GET",
            url: "/api/re/status/daily",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var yieldRate = new Array(keys.length);
                for (j in keys) {
                    var value = data[keys[j]];
                    if (!$.isEmptyObject(value)) {
                        var kpi = parseFloat(((value.output / value.input) * 100).toFixed(2));
                        yieldRate[j] = { name: keys[j], y: kpi };
                    } else {
                        yieldRate[j] = { name: keys[j], y: 0 };
                    }
                }

                Highcharts.chart('kpi', {
                    chart: {
                        type: 'line'
                    },
                    title: {
                        text: 'RE Repair Yield Rate Status',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                        allowDecimals: false,
                    },
                    yAxis: {
                        plotLines: [{
                            value: 92,
                            width: 1,
                            dashStyle: 'shortdash',
                            color: '#fff',
                            label: {
                                text: '92%',
                                style: {
                                    color: '#fff',
                                }
                            }
                        }],
                        title: {
                            text: 'Bonepile KPI',
                            style: {
                                fontSize: '14px',
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
                        name: 'KPI',
                        data: yieldRate,
                        tooltip: {
                            valueSuffix: '%'
                        },
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadWaitingRepair() {
        $.ajax({
            type: "GET",
            url: "/api/re/under-repair/time",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
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
                Highcharts.chart('waiting-repair', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE Waiting Repair Status (by Time)',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
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
                            events: {
                                click: function (event) {
                                    var drilldown = event.point.drilldown;
                                    if (drilldown == undefined || drilldown.levelNumber == '') {
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
                    drilldown: {
                        series: drilldown
                    }
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadPieChart(model_name) {
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

                Highcharts.chart('trending', {
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

    function loadTop15Model() {
        $.ajax({
            type: 'GET',
            url: '/api/re/under-repair/model/top15',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            success: function (data) {
                var keys = Object.keys(data);
                //   console.log(keys);
                var dataChart = new Array(keys.length);
                var categories = new Array(keys.length);
                for (i in keys) {
                    categories[i] = keys[i];
                    dataChart[i] = { name: keys[i], y: data[keys[i]] }
                }
                Highcharts.chart('top15', {
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
                                        var model_name = this.name;
                                        loadPieChart(model_name);
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
                        data: dataChart,
                        marker: {
                            enabled: false
                        },
                    }]
                });

                loadPieChart(keys[0]);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadBonePileTrend() {
        $.ajax({
            type: "GET",
            url: "/api/re/status/daily",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var output = new Array(keys.length);
                for (j in keys) {
                    var value = data[keys[j]];
                    if (!$.isEmptyObject(value)) {
                        output[j] = { name: keys[j], y: value.output };
                    }
                    else {
                        output[j] = { name: keys[j], y: 0 };
                    }
                }

                Highcharts.chart('status', {
                    chart: {
                        type: 'line'
                    },
                    title: {
                        text: 'RE Bonepile Status ',
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
                        plotLines: [{
                            value: 1800,
                            width: 1,
                            dashStyle: 'shortdash',
                            color: '#fff',
                            label: {
                                text: 'Plan: 1800',
                                style: {
                                    color: '#fff',
                                }
                            }
                        }],
                        title: {
                            text: '',
                            style: {
                                fontSize: '16px',
                                fontWeight: 'bold'
                            }
                        },
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
                        name: 'Actual reduce',
                        data: output,
                        dashStyle: 'shortdash'
                    }]
                });
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
                //  console.log(data)
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
        console.log(modelN);
        if (modelN == '' || modelN == null) {
            alert("Please select model name!");
        } else {
            window.location.href = '/api/re/importdatabymodelname?modelName=' + modelN + '';
        }

    }


</script>