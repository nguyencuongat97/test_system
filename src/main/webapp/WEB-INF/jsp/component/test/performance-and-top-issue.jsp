<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<style>
    .table-xxs > thead > tr > th, .table-xxs > tbody > tr > th, .table-xxs > tfoot > tr > th, .table-xxs > thead > tr > td, .table-xxs > tbody > tr > td, .table-xxs > tfoot > tr > td{
        padding: 3px;
    }
    .loader{
        display: block;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) 
        url('/assets/images/loadingg.gif') 
        50% 50% 
        no-repeat;
    }

    #tblETE thead th, #tblRetest thead th{
        background-color: #013d9d;
        color: #FFF;
    }
    #tblETE thead th, #tblRetest thead th , #tblETE tbody td, #tblRetest tbody td {
        border: 1px solid #272727;
    }

    .bgcolor-green {
        background-color: #22ff3b;
        color: unset;
    }
    .bgcolor-red {
        background-color: #ff3030;
        color: unset;
    }
    .bgcolor-yellow {
        background-color: #fff660;
        color: unset;
    }
    .content-wrapper {
        background-color: #FFF;
    }
    .footer {
        width: 100%;
    }
</style>
<div class="loader"></div>
<div class="row">
    <div class="col-lg-12" style="background: #ffffff;">
        <div class="panel panel-overview" id="header" style="font-size: 14px;">
            <span class="text-uppercase">BDD VN ${factory} performance and top issue</span>
        </div>
        <div class="row no-margin">
            <div class="col-sm-9 no-padding">
                <div class="input-group">
                    <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
                </div>
            </div>
            <div class="col-sm-2">
                <select class="form-control bootstrap-select" data-style="btn-xs" name="customer">
                    <option value="AMMAN">Amman</option>
                    <option value="OPTIMATOR">Optimator</option>
                </select>
            </div>
            <div class="col-sm-1 pl-0">
                <select class="form-control bootstrap-select" data-style="btn-xs" name="change-style">
                    <option value="table">Table</option>
                    <option value="chart">Chart</option>
                </select>
            </div>
        </div>
        <div id="table-mode">
            <div class="row no-margin pt-10">
                <label class="text-bold"><span class="customerName"></span><span style="color: green;">ETE</span></label>
                <div class="col-sm-12 no-padding table-responsive pre-scrollable" style="max-height: calc(100vh - 360px);">
                    <table id="tblETE" class="table table-xxs" style="text-align: center">
                        <thead></thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
            <div class="row no-margin pt-10">
                <label class="text-bold"><span class="customerName"></span><span style="color: green;">Retest Rate</span></label>
                <div class="col-sm-12 no-padding table-responsive pre-scrollable" style="max-height: calc(100vh - 360px);">
                    <table id="tblRetest" class="table table-xxs" style="text-align: center">
                        <thead></thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
        <div id="chart-mode" class="hidden">
            <div class="row no-margin pb-10">
                <div class="col-xs-12 no-padding" id="chart-ete" style="height: 360px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
            </div>
            <div class="row no-margin pb-10">
                <div class="col-xs-12 no-padding" id="chart-retest" style="height: 360px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
            </div>
        </div>
    </div>
</div>


<script>
    var dataset = {
        factory: '${factory}',
        customer: 'AMMAN'
    };

    function loadData() {
        $('.loader').removeClass('hidden');
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_quality_daily",
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                timeSpan: dataset.timeSpan

            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $('.customerName').html(dataset.customer + ' - ');
                $('#tblETE>thead').html('');
                $('#tblETE>tbody').html('');
                $('#tblretest>thead').html('');
                $('#tblretest>tbody').html('');
                if(!$.isEmptyObject(data)) {
                    var ete = data["ETE"];
                    var theadETE = '<tr><th>Station</th><th>Target</th>';
                    var tbodyETE = '';
                    var keysETE = [];
                    for(i in ete) {
                        for(j in ete[i]) {
                            if(keysETE.indexOf(j) == -1){
                                keysETE.push(j);
                            }
                        }
                    }
                    // keysETE.sort();
                    for(i in keysETE) {
                        theadETE += '<th>' + keysETE[i] + '</th>';
                    }
                    for(i in ete) {
                        if(i == 'SMT') {
                            tbodyETE += '<tr><td>' + i + '</td><td>98.50%</td>';
                        } else if(i == 'Actual ETE') {
                            tbodyETE += '<tr><td style="color: green; font-weight: bold">' + i + '</td><td>98.00%</td>';
                        } else {
                            tbodyETE += '<tr><td>' + i + '</td><td>99.00%</td>';
                        }
                        for(j in keysETE) {
                            if(ete[i][keysETE[j]] == null || ete[i][keysETE[j]] == undefined || ete[i][keysETE[j]] == "NaN" || ete[i][keysETE[j]] == 0) {
                                tbodyETE += '<td> 0%</td>';
                            } else if(i != 'Actual ETE') {
                                tbodyETE += '<td class="' + colorStation(ete[i][keysETE[j]], 98, 99) + '">' + ete[i][keysETE[j]].toFixed(2) + '%</td>';
                            } else {
                                tbodyETE += '<td class="' + colorStation(ete[i][keysETE[j]], 97, 98) + '">' + ete[i][keysETE[j]].toFixed(2) + '%</td>';
                            }
                        }
                        tbodyETE += '</tr>'
                    }
                    theadETE += '</tr>';
                    $('#tblETE>thead').html(theadETE);
                    $('#tblETE>tbody').html(tbodyETE);

                    var retest = data["RETEST-RATE"];
                    var theadRetest = '<tr><th>Station</th><th>Target</th>';
                    var tbodyRetest = '';
                    var keysRetest = [];
                    for(i in retest) {
                        for(j in retest[i]) {
                            if(keysRetest.indexOf(j) == -1){
                                keysRetest.push(j);
                            }
                        }
                    }
                    // keysRetest.sort();
                    for(i in keysRetest) {
                        theadRetest += '<th>' + keysRetest[i] + '</th>';
                    }
                    for(i in retest) {
                        tbodyRetest += '<tr><td>' + i + '</td><td>3.00%</td>';
                        for(j in keysRetest) {
                            if(retest[i][keysRetest[j]] == null || retest[i][keysRetest[j]] == undefined || retest[i][keysRetest[j]] == "NaN" || retest[i][keysRetest[j]] == 0) {
                                tbodyRetest += '<td> 0% </td>';
                            } else {
                                tbodyRetest += '<td class="' + colorRetest(retest[i][keysRetest[j]], 3, 10) + '">' + retest[i][keysRetest[j]].toFixed(2) + '%</td>';
                            }
                        }
                        tbodyRetest += '</tr>'
                    }
                    theadRetest += '</tr>';
                    $('#tblRetest>thead').html(theadRetest);
                    $('#tblRetest>tbody').html(tbodyRetest);


                    var dataChartETE = [];
                    var chartETEByStation = [];
                    var dataChartRetest = [];
                    var chartRetestByStation = [];
                    var colorIndex = 0;
                    var colorChart = ['#4572A7', '#AA4643', '#89A54E', '#80699B', '#3D96AE', '#DB843D', '#92A8CD', '#A47D7C', '#B5CA92'];
                    // var colorChart = ['#e6194b', '#3cb44b', '#ffe119', '#4363d8', '#f58231', '#911eb4', '#46f0f0', '#f032e6', '#bcf60c', '#fabebe', '#008080', '#e6beff', '#9a6324', '#fffac8', '#800000', '#aaffc3', '#808000', '#ffd8b1', '#000075', '#808080', '#ffffff', '#000000'];
                    for(i in ete) {
                        var tmpData = [];
                        for(j in ete[i]) {
                            if(ete[i][j] != 0) {
                                tmpData.push({'name': j,'y': ete[i][j]});
                            } else {
                                tmpData.push({'name': j,'y': null});
                            }
                        }
                        if(colorIndex == 9){
                            colorIndex = 0;
                        }
                        dataChartETE.push({'name': i, color: colorChart[colorIndex], 'data': tmpData});
                        colorIndex++;
                    }
                    for(i in retest) {
                        var tmpData = [];
                        for(j in retest[i]) {
                            if(retest[i][j] != 0) {
                                tmpData.push({'name': j,'y': retest[i][j]});
                            } else {
                                tmpData.push({'name': j,'y': null});
                            }
                        }
                        if(colorIndex == 9){
                            colorIndex = 0;
                        }
                        dataChartRetest.push({'name': i, color: colorChart[colorIndex], 'data': tmpData});
                        colorIndex++;
                    }
                    var categoryETE = Object.keys(ete['Actual ETE']);
                    loadchart('chart-ete', dataset.customer + ' - ETE', dataChartETE);
                    loadchart('chart-retest', dataset.customer + ' - Retest Rate', dataChartRetest);
                }
                $('.loader').addClass('hidden');
            },
            error: function (errMsg) {
                console.log(errMsg);
                alert('FAIL!!');
                $('.loader').addClass('hidden');
            }
        });
    }

    function colorStation(rate, errorSpec, warningSpec) {
        if (rate < errorSpec) {
            return 'bgcolor-red';
        }
        if (rate < warningSpec) {
            return 'bgcolor-yellow';
        }
        return 'bgcolor-green';
    }
    function colorRetest(rate, errorSpec, warningSpec) {
        if (rate < errorSpec) {
            return 'bgcolor-green';
        }
        if (rate < warningSpec) {
            return 'bgcolor-yellow';
        }
        return 'bgcolor-red';
    }

    function loadchart(idHtml, textHtml, dataChart) {
        Highcharts.chart(idHtml, {
            chart: {
                type: 'line'
            },
            title: {
                text: textHtml,
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    fontSize: '14px',
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
                },
                labels: {
                    format: '{value}%',
                },
                tickInterval: 0.5
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    label: {
                        connectorAllowed: false
                    },
                    dataLabels: {
                        format: '{point.y:.2f} %',
                    }
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b>'
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
            series: dataChart
        });
    }


    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).add(-15, "day").format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
                $('.datetimerange[side=right]').daterangepicker({
                    timePicker: true,
                    timePicker24Hour: true,
                    maxSpan: {
                        days: 30
                    },
                    opens: "right",
                    applyClass: 'bg-slate-600',
                    cancelClass: 'btn-default',
                    timePickerIncrement: 30,
                    locale: {
                        format: 'YYYY/MM/DD HH:mm'
                    }
                });
                dataset.timeSpan = startDate + ' - ' + endDate;
                loadData();

                $('input[name=timeSpan]').on('change', function() {
                    dataset.timeSpan = this.value;
                    loadData('');
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    $(document).ready(function() {
        getTimeNow();

        $('select[name="customer"]').on('change', function() {
            dataset.customer = this.value;
            loadData();
        });
        $('select[name="change-style"]').on('change', function() {
            if(this.value == 'table') {
                $('#chart-mode').addClass('hidden');
                $('#table-mode').removeClass('hidden');
            } else {
                $('#chart-mode').removeClass('hidden');
                $('#table-mode').addClass('hidden');
            }
        });
    });
</script>