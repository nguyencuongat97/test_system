<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/calc-line-balance.css">
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>
<link rel="stylesheet" href="/assets/css/custom/customer-line.css" />
<style>
    body {
        font-size: 16px;
    }

    table.table-bordered tbody tr:nth-of-type(odd) {
        background-color: #333333;
    }

    table.table-bordered tbody tr:nth-of-type(even) {
        background-color: #373737;
    }

    table.table th {
        font-weight: normal;
        text-transform: uppercase;
        color: #ccc;
    }

    .input-group {
        width: 100%;
        float: none;
    }

    .nav-tabs {
        border-bottom: none;
    }

    .nav-tabs>li.active>a,
    .nav-tabs>li.active>a:hover,
    .nav-tabs>li.active>a:focus {
        color: #eee;
        background-color: #515151;
        border: none;
        border-radius: 4px;
    }

    .nav-tabs>li {
        float: none;
        width: 48%;
        text-align: center;
    }

    .nav-tabs>li>a,
    .nav-tabs>li>a:hover,
    .nav-tabs>li>a:focus {
        color: #eee;
        background-color: #333333;
        border: none;
        border-radius: 4px;
    }

    button.btn.dropdown-toggle.btn-default {
        padding: 0.25rem 1rem;
        color: #ccc;
    }

    .bootstrap-select.btn-group .btn-default .caret {
        right: 0.5rem;
    }

    .dropdown-menu.open {
        background: #333333;
        color: #ccc;
    }

    ul.dropdown-menu.inner {
        background: #333333;
    }

    .dropdown-menu>li a {
        color: #ccc;
    }

    .dropdown-menu>li {
        position: relative;
        margin-bottom: 1px;
        border-top: 1px solid #515151;
    }

    .btn-default:active:hover,
    .btn-default.active:hover,
    .open>.dropdown-toggle.btn-default:hover,
    .btn-default:active:focus,
    .btn-default.active:focus,
    .open>.dropdown-toggle.btn-default:focus,
    .btn-default:active.focus,
    .btn-default.active.focus,
    .open>.dropdown-toggle.btn-default.focus {
        color: #fff;
        background-color: #515151;
        border-color: rgba(0, 0, 0, 0);
    }

    .dropdown-menu>.active>a,
    .dropdown-menu>.active>a:hover,
    .dropdown-menu>.active>a:focus {
        color: #fff;
        text-decoration: none;
        outline: 0;
        background-color: #515151;
    }

    .pre-scrollable {
        color: #ccc;
        max-height: calc(100vh - 165px);
    }

    input.form-control {
        color: #ccc !important;
    }

    .bootstrap-select.btn-group .no-results {
        background: #333333;
        color: #ccc;
    }

    .dropdown-content ul li {
        padding: 0.5rem;
        border-top: 1px solid #515151;
        background: #333333;
        color: #ccc;
        list-style: none;
        padding-left: 2rem;
        cursor: pointer;
    }

    .dropdown-content ul li:hover {
        background: #515151;
    }

    .dropdown-content ul li a {
        color: #ccc;
    }

    .dropdown-model .dropdown-content ul {
        background: #333333;
        padding: 0;
        margin: 0;
        max-height: 200px;
        overflow: auto;
    }

    .table {
        color: #ccc;
    }
</style>
<div class="row">
    <div class="col-lg-12" style="background: #272727;">
        <div class="panel panel-overview" id="header">
            <span class="text-uppercase">B06 - FATP line & man calculations</span>
        </div>
        <div class="row" style="margin: unset;">
            <div class="row" style="margin: unset;">
                <div class="col-sm-12 col-md-8 col-lg-9" style="padding-left: 0;">
                    <div class="panel input-group">
                        <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                        <input type="text" class="form-control datetimerange" side="right" id="datetime">
                    </div>
                </div>
                <div class="col-sm-12 col-md-2 col-lg-1" style="padding: 0;">
                    <ul class="nav nav-tabs" style="margin: 0;">
                        <li class="active" style="margin-right: 0.389rem;"><a style="padding: 0.28rem;"
                                data-toggle="tab" href="#pth">PTH</a></li>
                        <li style="float: right;"><a style="padding: 0.28rem;" data-toggle="tab" href="#si">SI</a></li>
                    </ul>
                </div>
                <div class="col-sm-12 col-md-2 col-lg-2dropdown dropdown-model" style="padding-right: 0;">
                    <div class="panel input-group">
                        <span class="input-group-addon"><i class="fa fa-search"></i></span>
                        <input type="text" style="height: 26px;border-bottom: none;"
                            class="form-control dropdown-toggle" data-toggle="dropdown" id="txtSearch"
                            placeholder="Search....">
                        <!-- <div class=" panel chooseModel" style="background: #333; height: 26px;">
                        <select id="selectModel" class="form-control bootstrap-select" data-live-search="true">

                        </select>
                    </div> -->
                        <div class="dropdown-menu dropdown-content">
                            <ul id="chooseModel">
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

        </div>

        <div class="row tab-content no-margin">
            <div id="pth" class="row tab-pane fade in active no-margin">
                <div class="col-xs-12 col-sm-12" style="margin: 1rem 0;padding: 0;">
                    <div class="panel panel-flat panel-body chart-sm" id="chartPTH"
                        style="height: 250px !important;margin: 0;">
                    </div>
                </div>
                <h4 style="color: #ccc;margin-top: 0;">TOTAL</h4>
                <div class="row" style="margin: unset;">
                    <table id="tblTotalPTH" class="table table-bordered table-xxs">
                        <thead>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <h4 style="color: #ccc;">Forecast (Kpcs)</h4>
                <div class="row" style="margin: unset;">
                    <table id="tblForecastPTH" class="table table-bordered table-xxs">
                    </table>
                </div>
                <h4 style="color: #ccc;">Line requestment - PTH</h4>
                <div class="row" style="margin: unset;">
                    <table id="tblMpointPTH" class="table table-bordered table-xxs">
                    </table>
                </div>

                <h4 style="color: #ccc;">Man power - PTH</h4>
                <div class="row" style="margin: unset;">
                    <table id="tblManPowerPTH" class="table table-bordered table-xxs">
                    </table>
                </div>

            </div>
            <div id="si" class="row tab-pane fade no-margin">
                <div class="row">
                    <div class="col-xs-12 col-sm-12" style="margin: 1rem 0;">
                        <div class="panel panel-flat panel-body chart-sm" id="chartSI"
                            style="height: 250px !important;margin: 0;">
                        </div>
                    </div>
                </div>
                <h4 style="color: #ccc;margin-top: 0;">TOTAL</h4>
                <div class="row" style="margin: unset;">
                    <table id="tblTotalSI" class="table table-bordered table-xxs">
                        <thead>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <h4 style="color: #ccc;margin-top: 0;">Forecast (Kpcs)</h4>
                <div class="row" style="margin: unset;">
                    <table id="tblForecastSI" class="table table-bordered table-xxs">
                    </table>
                </div>
                <h4 style="color: #ccc;">Line requestment - SI</h4>
                <div class="row" style="margin: unset;">
                    <table id="tblMpointSI" class="table table-bordered table-xxs">
                    </table>
                </div>

                <h4 style="color: #ccc;">Man power - SI</h4>
                <div class="row" style="margin: unset;">
                    <table id="tblManPowerSI" class="table table-bordered table-xxs">
                    </table>
                </div>

            </div>

        </div>
    </div>
</div>
<script>
    var dataset = {
        factory: '${factory}'
    };
    getTimeNow();
    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var endDate = moment(current).add(3, "month").format("YYYY/MM/DD") + ' 07:30';
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30';
                dataset.timeSpan = startDate + ' - ' + endDate;
                loadOject('PTH');
                loadOject('SI');
                $('#datetime').daterangepicker({
                    maxSpan: {
                        days: 7
                    },
                    timePicker: true,
                    timePicker24Hour: true,
                    applyClass: 'bg-slate-600',
                    cancelClass: 'btn-default',
                    timePickerIncrement: 3,
                    locale: {
                        format: 'YYYY/MM/DD HH:mm'
                    }
                });
                $('#datetime').data('daterangepicker').setStartDate(new Date(startDate));
                $('#datetime').data('daterangepicker').setEndDate(new Date(endDate));
                $('#datetime').on('change', function () {
                    dataset.timeSpan = this.value;
                    loadOject('PTH');
                    loadOject('SI');
                });
                delete current;
                delete startDate;
                delete endDate;
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    // var dataTbl;

    function toFixed2(value) {
        return value == 0 ? value : Number(parseFloat(value).toFixed(2));
    }
    var dataForecast = [], dataMPOINT = [], dataMan = [];
    function loadOject(sectionName) {
        $.ajax({
            type: "GET",
            //url: "http://10.224.81.70:8888/api/test/manpower",
            url: "/api/test/manpower",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                sectionName: sectionName
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                if (res) {
                    $('#tblForecast' + sectionName).html('');
                    $('#tblMpoint' + sectionName).html('');
                    $('#tblManPower' + sectionName).html('');
                    var optionModel = '';
                    var opjectModel = Object.keys(res);
                    $('#tblForecast' + sectionName).append('<thead><tr><th rowspan="2">Model</th></tr></thead>');
                    $('#tblForecast' + sectionName).append('<tbody></tbody>');
                    $('#tblMpoint' + sectionName).append('<thead><tr><th>Model</th><th>Cycle Time</th><th>Pcas ManPower</th><th>Daily Output</th></tr></thead>');
                    $('#tblMpoint' + sectionName).append('<tbody></tbody>');
                    $('#tblManPower' + sectionName).append('<thead><tr><th>Model</th></tr></thead>');
                    $('#tblManPower' + sectionName).append('<tbody></tbody>');
                    $('#tblTotal' + sectionName + ' tbody').append('<tr class="trForecast"></tr><tr class="trMPOINT"></tr><tr class="trMan"></tr>');
                    var tdHtml = '';
                    for (i = 0; i < opjectModel.length; i++) {
                        if (opjectModel[i] != 'TOTAL') {
                            optionModel += '<li>' + opjectModel[i] + '</li>';
                            tdHtml = '<tr class="' + i + '"><td>' + opjectModel[i] + '</td></tr>';
                            $('#tblForecast' + sectionName + ' tbody').append(tdHtml);
                            $('#tblMpoint' + sectionName + ' tbody').append(tdHtml);
                            $('#tblManPower' + sectionName + ' tbody').append(tdHtml);
                            var data = res[opjectModel[i]];
                            var itemList = data[0].itemList;
                            for (j in data) {
                                if (data[j].sectionName == sectionName) {
                                    $('#tblMpoint' + sectionName + ' tbody .' + i).append('<td>' + data[j].cycleTime + '</td>' +
                                        '<td>' + data[j].pcasManPower + '</td>' +
                                        '<td>' + data[j].dailyOutput.toFixed(2) + '</td>');
                                    for (k in data[j].itemList) {
                                        $('#tblForecast' + sectionName + ' tbody .' + i).append('<td>' + data[j].itemList[k].forecast + '</td>');
                                        $('#tblMpoint' + sectionName + ' tbody .' + i).append('<td>' + toFixed2(data[j].itemList[k].mpoint) + '</td>');
                                        $('#tblManPower' + sectionName + ' tbody .' + i).append('<td>' + toFixed2(data[j].itemList[k].manPower) + '</td>');

                                    }
                                }
                            }
                        }
                        else {
                            var total = res[opjectModel[i]]
                            for (j = 0; j < total.length; j++) {
                                if (total[j].sectionName == sectionName) {
                                    var itemListTotal = total[j].itemList;
                                }
                            }
                        }

                    }
                    dataForecast = [], dataMPOINT = [], dataMan = [];
                    var trForecast = '', trMPOINT = '', trMan = '';
                    console.log(itemListTotal)
                    for (i in itemListTotal) {
                        trForecast += '<td>' + toFixed2(itemListTotal[i].forecast) + '</td>';
                        trMPOINT += '<td>' + toFixed2(itemListTotal[i].mpoint) + '</td>';
                        trMan += '<td>' + toFixed2(itemListTotal[i].manPower) + '</td>';
                        dataForecast.push({ name: i, y: toFixed2(itemListTotal[i].forecast) });
                        dataMPOINT.push({ name: i, y: toFixed2(itemListTotal[i].mpoint) });
                        dataMan.push({ name: i, y: toFixed2(itemListTotal[i].manPower) });
                    }
                    $('#tblTotal' + sectionName + ' tbody .trForecast').html('<td>Forecast (Kpcs)</td>' + trForecast);
                    $('#tblTotal' + sectionName + ' tbody .trMPOINT').html('<td>Line requestment</td>' + trMPOINT);
                    $('#tblTotal' + sectionName + ' tbody .trMan').html('<td>Man power</td>' + trMan);
                    var thDate = '';
                    var thForcast = '';
                    for (i in itemList) {
                        thDate += '<th>' + i.substring(5) + '</th>';
                        thForcast += '<th>' + itemList[i].runningDay + '</th>';
                    }
                    $('#tblForecast' + sectionName + ' thead tr').append(thDate);
                    $('#tblForecast' + sectionName + ' thead').append('<tr>' + thForcast + '</tr>');
                    $('#tblMpoint' + sectionName + ' thead tr').append(thDate);
                    $('#tblManPower' + sectionName + ' thead tr').append(thDate);
                    $('#tblTotal' + sectionName + ' thead').append('<tr><th>Item</th>' + thDate + '</tr>');


                    $('#chooseModel').html(optionModel);

                    $("#chooseModel li").on("click", function () {
                        $("#txtSearch").val($(this).html())
                        var value = $(this).html().toLowerCase();
                        filterByModel(value);
                    });
                    $("#txtSearch").on("keyup", function () {
                        var value = $(this).val().toLowerCase();
                        filterByModel(value);
                    });
                    loadChart(sectionName);

                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    $("#txtSearch").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#chooseModel>li").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    function loadChart(sectionName) {
        color = ['orange', "#2b908f"];
        Highcharts.chart('chart' + sectionName, {
            chart: {
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: {
                text: 'Line Requirements - ' + sectionName,
                style: {
                    fontSize: '16px',
                    fontWeight: 'bold'
                }
            },
            xAxis: {
                type: 'category',
            },
            yAxis: [

                {
                    title: {
                        text: '',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                },
                {
                    title: {
                        text: '',
                    }
                }
            ],

            tooltip: {
                shared: true
            },
            colors: color,
            legend: {
                style: {
                    fontSize: '11px'
                },
                layout: 'horizontal',
                align: 'center',
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
            series: [
                {
                    name: 'Man power',
                    type: 'line',
                    yAxis: 1,
                    data: dataMan,
                },
                {
                    name: 'Line requestment',
                    type: 'line',
                    data: dataMPOINT
                }]
        });

    }

    function filterByModel(value) {
        $("#tblForecastPTH>tbody>tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
        $("#tblForecastSI>tbody>tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
        $("#tblMpointPTH>tbody>tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
        $("#tblManPowerPTH>tbody>tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
        $("#tblMpointSI>tbody>tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
        $("#tblManPowerSI>tbody>tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    }
</script>