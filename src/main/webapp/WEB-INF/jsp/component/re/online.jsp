<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/onlineWip.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<link rel="stylesheet" href="/assets/css/custom/style.css" />

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
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
            <div class="col-xs-12 col-sm-6 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="chart1"></div>
            </div>
            <div class="col-xs-12 col-sm-6 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="chart2"></div>
            </div>
            <div class="col-xs-12 col-sm-6 infchart collapseChart" id="pre-chartDefectedStatus">
                <div class="panel panel-flat panel-body chart-sm" id="chart4"></div>
            </div>
            <div class="col-xs-12 col-sm-6 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="chart3"></div>
            </div>
            <div class="infchart collapseChart pieChart">
                <div class="panel panel-flat panel-body chart-sm" id="chart5"></div>
            </div>
        </div>
    </div>
</div>
<style>
    /* .chooseModel{
        float: left;
        background: #333;
    }
    .chooseModelExport{
        border-bottom: none;
    } */
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
        loadOnlineWipTotal();
        loadOnlineWipByTime();
        loadOnlineWipByDate();
        loadDefectedStatus();
    }

    function loadOnlineWipTotal() {
        $.ajax({
            type: "GET",
            url: "/api/re/status",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                console.log(data)
                var keys = Object.keys(data);
                var dataChart = new Array(keys.length - 2);
                var drilldown = new Array(keys.length - 2);
                for (i = 1; i < keys.length - 1; i++) {
                    var modelMap = data[keys[i]].data;
                    var drillData = new Array(modelMap.length);
                    for (j in modelMap) {
                        drillData[j] = {
                            name: modelMap[j].key,
                            y: modelMap[j].value
                        }
                    }

                    dataChart[i - 1] = {
                        name: keys[i],
                        y: data[keys[i]].size,
                        drilldown: (modelMap.length > 0 ? keys[i] : null)
                    }

                    drilldown[i - 1] = {
                        name: keys[i],
                        id: keys[i],
                        data: drillData
                    };
                }
                Highcharts.chart('chart1', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE Repair Daily Status',
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

    function loadOnlineWipByDate() {
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
                var input = new Array(keys.length);
                var output = new Array(keys.length);
                var remain = new Array(keys.length);
                for (j in keys) {
                    var value = data[keys[j]];
                    if (!$.isEmptyObject(value)) {
                        input[j] = { name: keys[j], y: value.input };
                        output[j] = { name: keys[j], y: value.output };
                        remain[j] = { name: keys[j], y: value.remain };
                    } else {
                        input[j] = { name: keys[j], y: 0 };
                        output[j] = { name: keys[j], y: 0 };
                        remain[j] = { name: keys[j], y: 0 };
                    }
                }

                Highcharts.chart('chart2', {
                    chart: {
                        type: 'line',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'RE Online WIP  Repair Weekly Status',
                        style: {
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                        tickInterval: Math.ceil(keys.length / 7),
                    },
                    yAxis: {
                        title: {
                            text: ''
                        },
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
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
                        name: 'Input Qty',
                        data: input
                    }, {
                        name: 'Output Qty',
                        data: output
                    }, {
                        name: 'Remain Qty',
                        data: remain
                    }]
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadOnlineWipByTime() {
        $.ajax({
            type: "GET",
            url: "/api/re/time",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var keys = Object.keys(data);
                var dataChart = new Array();
                var drilldown = new Array();

                for (i = 0; i < 3; i++) {
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
                Highcharts.chart('chart3', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE Waiting Repair Status (by Time)',
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
                            events: {
                                click: function (event) {
                                    var drilldown = event.point.drilldown;
                                    if (drilldown == undefined || drilldown.levelNumber == '') {
                                        var strClass = $('#pre-chartDefectedStatus').attr('class');
                                        if (strClass.indexOf('col-xs-3') == -1) {
                                            loadDefectedStatus();
                                        }
                                        $('.collapseChart').addClass('col-xs-3');
                                        $('.collapseChart').removeClass('col-sm-6');
                                        $('.collapseChart').removeClass('pieChart');
                                        var model_name = event.point.name;
                                        loadPiechart(model_name);
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
                            showInLegend: true,
                            borderWidth: 0,
                        }
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '10px',
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
            url: '/api/re/defected',
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

                Highcharts.chart('chart4', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'RE Check out Defect Status (by Daily)',
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
                        colorByPoint: true,
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
                console.log(data)
                var html = '<option value="" disabled="" selected="">Choose Model Export</option>';
                data.forEach(e => {
                    html += '<option>' + e + '</option>';
                });
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