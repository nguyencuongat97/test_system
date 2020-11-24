<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/bonepile.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<style>
    .btnBackto {
        position: absolute;
        right: 12px;
        top: 8px;
    }

    .piechart {
        position: relative;
    }
</style>
<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header" style="font-size: 18px;">
            <b><span class="text-uppercase" name="factory">${factory}</span><Span id="teamS"></Span><span>-RE Bone Pile
                    Management</span></b>
        </div>

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
            <a class=" btn btn-lg" id="btnExport" onclick="exportDataDetailModelname()"
                style="height:29px;width:7%;font-size: 13px; padding: 7px; border-radius: 5px; color: #fff;background: #444;float: right;">
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
            <div class="col-xs-12 col-sm-4 infchart colum1">
                <div class="panel panel-flat panel-body chart-sm" id="status"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart piechart pie1">
                <div class="panel panel-flat panel-body chart-sm" id="piechart1"></div>
                <button class="btnBackto" id="backto1">◁ Back to</button>
            </div>
            <div class="col-xs-12 col-sm-4 infchart colum2">
                <div class="panel panel-flat panel-body chart-sm" id="top15"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart piechart pie2">
                <div class="panel panel-flat panel-body chart-sm" id="piechart2"></div>
                <button class="btnBackto" id="backto2">◁ Back to</button>
            </div>
            <div class="col-xs-12 col-sm-4 infchart colum3">
                <div class="panel panel-flat panel-body chart-sm" id="trending"></div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart piechart pie3">
                <div class="panel panel-flat panel-body chart-sm" id="piechart3"></div>
                <button class="btnBackto" id="backto3">◁ Back to</button>
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
    var team = "-JBD";
    var dataset = {
        factory: '${factory}'
    }
    $('.piechart').addClass('hidden');
    function init() {
        if (dataset.factory == "B04") {
            team = "-BBD";
        }
        $('#teamS').text(team)
        loadBonePileSfc();
        loadWaitingRepair();
        // loadPieChart();
        // loadTop15Model();
        // loadBonePileTrend();
        chartByModelName();
        //  loadWaitingRepair();
    }

    function loadBonePileSfc() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/wipInOutTrendChart",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                //console.log("wipInOutTrendChart ", data.result.balance);
                //  var keyBalance = Object.keys(data.result.balance);

                var scfBonePile = [], balance = [], over8h = [];
                for (let k in data.result.balance) {
                    if (data.result.balance.hasOwnProperty(k)) {
                        let element = data.result.balance[k];
                        balance.push({ name: k, y: element });
                    }
                }
                for (let k in data.result.overtime8h) {
                    if (data.result.overtime8h.hasOwnProperty(k)) {
                        let element = data.result.overtime8h[k];
                        over8h.push({ name: k, y: element });
                    }
                }
                for (let k in data.result.bonePile) {
                    if (data.result.bonePile.hasOwnProperty(k)) {
                        let element = data.result.bonePile[k];
                        scfBonePile.push({ name: k, y: element });
                    }
                }
                var res = [];
                res.push({ name: 'Bone Pile', data: scfBonePile });
                res.push({ name: 'Balance', data: balance });
                res.push({ name: 'OverTime 8H', data: over8h });

                hightChartLine("kpi", "Total SFC Bone Pile Daily Status", res);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    // function loadWaitingRepair() {
    //     $.ajax({
    //         type: "GET",
    //         url: "/api/re/under-repair/time",
    //         data: {
    //             factory: dataset.factory,
    //             timeSpan: dataset.timeSpan
    //         },
    //         contentType: "application/json; charset=utf-8",
    //         success: function (data) {
    //             // console.log("under-repair: ", data);
    //             return;
    //             var keys = Object.keys(data);
    //             var dataChart = new Array(keys.length);
    //             var drilldown = new Array(keys.length);

    //             for (i in keys) {
    //                 var modelMap = data[keys[i]].data;
    //                 if (modelMap.length < 15) {
    //                     var drillData = new Array(modelMap.length);
    //                     for (j in modelMap) {
    //                         drillData[j] = {
    //                             name: modelMap[j].key,
    //                             y: modelMap[j].value
    //                         }
    //                     }
    //                 }
    //                 else {
    //                     var drillData = new Array();
    //                     for (j = 0; j < 15; j++) {
    //                         drillData[j] = {
    //                             name: modelMap[j].key,
    //                             y: modelMap[j].value
    //                         }
    //                     }
    //                 }

    //                 dataChart[i] = {
    //                     name: keys[i],
    //                     y: data[keys[i]].size,
    //                     drilldown: (modelMap.length > 0 ? keys[i] : null)
    //                 }

    //                 drilldown[i] = {
    //                     name: keys[i],
    //                     id: keys[i],
    //                     data: drillData
    //                 };
    //             }
    //             Highcharts.chart('waiting-repair', {
    //                 chart: {
    //                     type: 'column',
    //                     style: {
    //                         fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
    //                     }
    //                 },
    //                 title: {
    //                     text: 'RE Waiting Repair Status (by Time)',
    //                     style: {
    //                         fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
    //                         fontSize: '16px',
    //                         fontWeight: 'bold'
    //                     }
    //                 },
    //                 xAxis: {
    //                     type: 'category',
    //                 },
    //                 yAxis: {
    //                     min: 0,
    //                     title: {
    //                         text: ' '
    //                     },
    //                 },
    //                 plotOptions: {
    //                     series: {
    //                         borderWidth: 0,
    //                         dataLabels: {
    //                             enabled: true,
    //                         },
    //                         cursor: 'pointer',
    //                         events: {
    //                             click: function (event) {
    //                                 var drilldown = event.point.drilldown;
    //                                 if (drilldown == undefined || drilldown.levelNumber == '') {
    //                                 }
    //                             }
    //                         }
    //                     },
    //                 },
    //                 tooltip: {
    //                     headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
    //                     pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
    //                 },
    //                 legend: {
    //                     enabled: false
    //                 },
    //                 credits: {
    //                     enabled: false
    //                 },
    //                 navigation: {
    //                     buttonOptions: {
    //                         enabled: false
    //                     }
    //                 },
    //                 series: [{
    //                     name: ' ',
    //                     colorByPoint: true,
    //                     data: dataChart
    //                 }],
    //                 drilldown: {
    //                     series: drilldown
    //                 }
    //             });
    //         },
    //         failure: function (errMsg) {
    //             console.log(errMsg);
    //         }
    //     });
    // }

    function loadWaitingRepair() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/modelByTime",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                // console.log("modelByTime: ", data)
                var array = Object.keys(data);
                var dataChart = new Array();
                var drilldown = new Array();

                for (const key in data) {
                    if (data.hasOwnProperty(key)) {
                        const element = data[key];
                        dataChart.push({ name: key, y: element.qty, drilldown: key });
                        var arrayDrillDown = [];
                        for (const k in element.data) {
                            if (element.data.hasOwnProperty(k)) {
                                const e = element.data[k];
                                var tmpArr = [];
                                tmpArr.push(k);
                                tmpArr.push(e);
                                arrayDrillDown.push(tmpArr);
                            }
                        }
                        drilldown.push({ name: key, id: key, data: arrayDrillDown });
                    }
                }

                dropdownChart("waiting-repair", "RE Waiting Repair Status (by Time)", dataChart, drilldown)


                //     Highcharts.chart('waiting-repair', {
                //         chart: {
                //             type: 'column',
                //             style: {
                //                 fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                //             }
                //         },
                //         title: {
                //             text: 'RE Waiting Repair Status (by Time)',
                //             style: {
                //                 fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                //                 fontSize: '16px',
                //                 fontWeight: 'bold'
                //             }
                //         },
                //         xAxis: {
                //             type: 'category',
                //         },
                //         yAxis: {
                //             min: 0,
                //             title: {
                //                 text: ' '
                //             },
                //         },
                //         plotOptions: {
                //             series: {
                //                 borderWidth: 0,
                //                 dataLabels: {
                //                     enabled: true,
                //                 },
                //                 cursor: 'pointer',
                //                 events: {
                //                     click: function (event) {
                //                         var drilldown = event.point.drilldown;
                //                         if (drilldown == undefined || drilldown.levelNumber == '') {
                //                         }
                //                     }
                //                 }
                //             },
                //         },
                //         tooltip: {
                //             headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                //             pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
                //         },
                //         legend: {
                //             enabled: false
                //         },
                //         credits: {
                //             enabled: false
                //         },
                //         navigation: {
                //             buttonOptions: {
                //                 enabled: false
                //             }
                //         },
                //         series: [{
                //             name: ' ',
                //             colorByPoint: true,
                //             data: dataChart
                //         }],
                //         drilldown: {
                //             series: drilldown
                //         }
                //     });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function chartByModelName() {
        $.ajax({
            type: 'GET',
            url: '/api/re/online/bonpile_by_model',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                // modelName: model_name
            },
            success: function (object) {
                // console.log("bonpile_by_model:", object)
                var balance = [], over8h = [], bonepile = [];
                for (let key in object.balance) {
                    if (object.balance.hasOwnProperty(key)) {
                        let element = object.balance[key];
                        balance.push({ name: key, y: element });
                    }
                }
                for (let key in object.bonepile) {
                    if (object.bonepile.hasOwnProperty(key)) {
                        let element = object.bonepile[key];
                        bonepile.push({ name: key, y: element });
                    }
                }
                for (let key in object.overTime) {
                    if (object.overTime.hasOwnProperty(key)) {
                        let element = object.overTime[key];
                        over8h.push({ name: key, y: element });
                    }
                }
                loadColumnChart("status", "Bone Pile " + object.date, bonepile, 1);
                loadColumnChart("top15", "balance " + object.date, balance, 2);
                loadColumnChart("trending", "over8h " + object.date, over8h, 3);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });

    }

    function loadPieChart(modelName, condis) {
        var action = "";
        var piechart = '';
        if (condis == 3) {
            action = "OVER8H";
            piechart = 'piechart3';
            $('.colum3').addClass('hidden');
            $('.pie3').removeClass('hidden');
            $('#backto3').on('click', function () {
                $('.colum3').removeClass('hidden');
                $('.pie3').addClass('hidden');
            });
        } else if (condis == 2) {
            action = "BALANCE";
            piechart = 'piechart2';
            $('.colum2').addClass('hidden');
            $('.pie2').removeClass('hidden');
            $('#backto2').on('click', function () {
                $('.colum2').removeClass('hidden');
                $('.pie2').addClass('hidden');
            });
        } else {
            action = "BONEPILE";
            piechart = 'piechart1';
            $('.colum1').addClass('hidden');
            $('.pie1').removeClass('hidden');
            $('#backto1').on('click', function () {
                $('.colum1').removeClass('hidden');
                $('.pie1').addClass('hidden');
            });
        }
        $.ajax({
            type: 'GET',
            url: '/api/re/online/bonpile_by_model_by_error_code',
            data: {
                factory: dataset.factory,
                action: action,
                timeSpan: dataset.timeSpan,
                modelName: modelName
            },
            success: function (data) {
                console.log("bonpile_by_model_by_error_code: ", data)
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

                Highcharts.chart(piechart, {
                    chart: {
                        type: 'pie',
                        options3d: {
                            enabled: true,
                            alpha: 45,
                            beta: 0
                        }
                    },
                    title: {
                        text: modelName,
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

    // function loadTop15Model() {
    //     $.ajax({
    //         type: 'GET',
    //         url: '/api/re/under-repair/model/top15',
    //         data: {
    //             factory: dataset.factory,
    //             timeSpan: dataset.timeSpan
    //         },
    //         success: function (data) {
    //             var keys = Object.keys(data);
    //             //   console.log(keys);
    //             var dataChart = new Array(keys.length);
    //             var categories = new Array(keys.length);
    //             for (i in keys) {
    //                 categories[i] = keys[i];
    //                 dataChart[i] = { name: keys[i], y: data[keys[i]] }
    //             }
    //             Highcharts.chart('top15', {
    //                 chart: {
    //                     type: 'bar',
    //                     style: {
    //                         fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
    //                     }
    //                 },
    //                 title: {
    //                     text: 'Top 15 model waiting repair',
    //                     style: {
    //                         fontSize: '16px',
    //                         fontWeight: 'bold'
    //                     }
    //                 },
    //                 xAxis: {
    //                     type: 'category',
    //                 },
    //                 yAxis: {
    //                     min: 0,
    //                     title: {
    //                         text: ' '
    //                     },
    //                 },
    //                 plotOptions: {
    //                     series: {
    //                         borderWidth: 0,
    //                         dataLabels: {
    //                             enabled: true,
    //                         },
    //                         cursor: 'pointer',
    //                         point: {
    //                             events: {
    //                                 click: function () {
    //                                     var model_name = this.name;
    //                                     // loadPieChart(model_name);
    //                                 }
    //                             }
    //                         }
    //                     }
    //                 },
    //                 legend: {
    //                     enabled: false
    //                 },
    //                 navigation: {
    //                     buttonOptions: {
    //                         enabled: false
    //                     }
    //                 },
    //                 credits: {
    //                     enabled: false
    //                 },
    //                 series: [{
    //                     name: ' ',
    //                     colorByPoint: true,
    //                     data: dataChart,
    //                     marker: {
    //                         enabled: false
    //                     },
    //                 }]
    //             });

    //             // loadPieChart(keys[0]);
    //         },
    //         failure: function (errMsg) {
    //             console.log(errMsg);
    //         }
    //     });
    // }
    // function loadBonePileTrend() {
    //     $.ajax({
    //         type: "GET",
    //         url: "/api/re/online/wipInOutTrendChart",
    //         data: {
    //             factory: dataset.factory,
    //             timeSpan: dataset.timeSpan
    //         },
    //         contentType: "application/json; charset=utf-8",
    //         success: function (data) {
    //             var keys = Object.keys(data);
    //             var output = new Array(keys.length);
    //             for (j in keys) {
    //                 var value = data[keys[j]];
    //                 if (!$.isEmptyObject(value)) {
    //                     output[j] = { name: keys[j], y: value.output };
    //                 }
    //                 else {
    //                     output[j] = { name: keys[j], y: 0 };
    //                 }
    //             }

    //             Highcharts.chart('status', {
    //                 chart: {
    //                     type: 'line'
    //                 },
    //                 title: {
    //                     text: 'RE Bonepile Status ',
    //                     style: {
    //                         fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
    //                         fontSize: '16px',
    //                         fontWeight: 'bold'
    //                     }
    //                 },
    //                 xAxis: {
    //                     type: 'category',
    //                     allowDecimals: false,
    //                     minorGridLineWidth: 0,
    //                     minTickInterval: 1,
    //                     tickAmount: 4,
    //                 },
    //                 yAxis: {
    //                     plotLines: [{
    //                         value: 1800,
    //                         width: 1,
    //                         dashStyle: 'shortdash',
    //                         color: '#fff',
    //                         label: {
    //                             text: 'Plan: 1800',
    //                             style: {
    //                                 color: '#fff',
    //                             }
    //                         }
    //                     }],
    //                     title: {
    //                         text: '',
    //                         style: {
    //                             fontSize: '16px',
    //                             fontWeight: 'bold'
    //                         }
    //                     },
    //                 },
    //                 legend: {
    //                     itemStyle: {
    //                         fontSize: '11px',
    //                     },
    //                     layout: 'horizontal',
    //                     align: 'left',
    //                     verticalAlign: 'bottom',
    //                     enabled: false
    //                 },
    //                 navigation: {
    //                     buttonOptions: {
    //                         enabled: false
    //                     }
    //                 },
    //                 credits: {
    //                     enabled: false
    //                 },
    //                 series: [{
    //                     name: 'Actual reduce',
    //                     data: output,
    //                     dashStyle: 'shortdash'
    //                 }]
    //             });
    //         },
    //         failure: function (errMsg) {
    //             console.log(errMsg);
    //         }
    //     });
    // }
    //   loadInOutTrendChart();
    function loadInOutTrendChart() {
        var dt = {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan
        };
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

    getTimeNow();
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
        // var modelN = $('#slModelName').val();
        // if (modelN == '' || modelN == null) {
        //     alert("Please select model name!");
        // } else {
        window.location.href = '/api/re/online/export_data_bonepile?factory=' + dataset.factory + '&timeSpan=' + dataset.timeSpan + '';
        // }

    }


    function hightChartLine(idChart, textTitle, data) {

        Highcharts.chart(idChart, {
            chart: {
                type: 'line'
            },
            title: {
                text: textTitle,
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
            series: data
        });
    }

    function dropdownChart(idChart, textTitle, dataChart, drilldown) {
        Highcharts.chart(idChart, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: textTitle,
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
    }

    function loadColumnChart(idHtml, titleText, dataChart, valTest) {
        Highcharts.chart(idHtml, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: titleText,
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
                            click: function () {
                                var model_name = this.name;
                                // var S('')
                                loadPieChart(model_name, valTest);
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
</script>