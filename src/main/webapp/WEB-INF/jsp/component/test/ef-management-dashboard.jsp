<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/tables/datatables/datatables.min.js"></script>
<style>
    .panel {margin-bottom: 12.5px;background-color: #fff;box-shadow: 0 4px 8px rgba(0, 0, 0, 0.20), 0 1px 2px rgba(0, 0, 0, 0.24);}
    #using {background: #28a745;color: #fff;}
    #offline {background: #ababab;}
    #cabrating {background: #1a9bba;color: #fff;}
    #repairing {background: #fac010;}
    .inner h4 {font-weight: 700;margin: 0px 0 0 20px;}
    .inner p {margin: 0 0 0px 20px;}
    .content_ef {padding: 0.5rem;padding-top: 1rem;background-image: linear-gradient(#f7f7f7, #c2c2c2);}
    .tableFixHead {overflow-y: auto;height: 210px;}
    .pre-scrollable {max-height: 210px !important;}
    .tableFixHead thead th {position: sticky;top: 0;}
    table {border-collapse: collapse;width: 100%;}
    .table-fixed th {background: #eee;}
    ul.nav.nav-tabs {margin-bottom: 0rem;}
    .table-responsive.pre-scrollable.tableFixHead {max-height: 230px;border: 1px solid #dddddd;border-top: none;}
    ::-webkit-scrollbar-thumb {border-radius: 5px;background: #cecfd0;}
    .infchart {padding: 0px 6.5px 0px 6.5px;}
    .inner {padding: 1rem;}
    .panel-tittle span {font-size: 16px;font-weight: 600;padding-left: 1rem;}
    .panel-tittle {border-bottom: 1px solid #dddddd;margin-bottom: 1rem;width: 100%;margin-left: 0.05rem;padding-top: 0.7rem;}
    .panel-body {padding: 0px;}
    .panel-flat {height: 250px;}
    .inner sup {font-size: 17px;}
    .dataTables_paginate .paginate_button {padding: 1px;min-width: 30px;}
    .dataTables_filter {float: right;margin: 7px;}
    .table>thead>tr>th,.table>tbody>tr>th,.table>tfoot>tr>th,.table>thead>tr>td,.table>tbody>tr>td,.table>tfoot>tr>td {padding: 6px 8px;}
    .dataTable thead .sorting,.dataTable thead .sorting_asc,.dataTable thead .sorting_desc,.dataTable thead .sorting_asc_disabled,.dataTable thead .sorting_desc_disabled {padding-right: 35px;}
    .dataTable thead .sorting:before,.dataTable thead .sorting:after,.dataTable thead .sorting_asc:after,.dataTable thead .sorting_desc:after,.dataTable thead .sorting_asc_disabled:after,.dataTable thead .sorting_desc_disabled:after {right: 8px;}
    .dataTables_filter input {height: 27px;padding: 0px 0;}
    .dataTables_filter {display: none;}
    .tableFitter>label:after {content: "\e98e";font-family: 'icomoon';font-size: 12px;display: inline-block;position: absolute;top: 50%;right: 0;margin-top: -6px;color: #999999;line-height: 1;-webkit-font-smoothing: antialiased;-moz-osx-font-smoothing: grayscale;}
    .tableFitter input {outline: 0;width: 200px;height: 26px;padding: 8px 0;padding-right: 24px;font-size: 13px;line-height: 1.5384616;color: #333333;background-color: transparent;border: 1px solid transparent;border-width: 1px 0;border-bottom-color: #ddd;}
    .reminderAlert{color:red;font-weight: 700;background: #f1e2e2;}
    .warning{color:#ff7600;font-weight: 700;}
</style>

<div class="panel panel-re row">
    <div class="content_ef">
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-12 infchart">
                <div class="panel panel-overview" style="font-size: 18px;text-align: center;">
                    <b><span>E/F Management Dashboard</span></b>
                </div>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-3">
                <div id="using" class="panel panel-body">
                    <div class="inner">
                        <h4 id="h4Using"></h4>
                        <p id="pUsing"></p>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 col-sm-3">
                <div id="offline" class="panel panel-body">
                    <div class="inner">
                        <h4 id="h4Offline"></h4>
                        <p id="pOffline"></p>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 col-sm-3">
                <div id="cabrating" class="panel panel-body">
                    <div class="inner">
                        <h4 id="h4Cabrating"></h4>
                        <p id="pCabrating"></p>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 col-sm-3">
                <div id="repairing" class="panel panel-body">
                    <div class="inner">
                        <h4 id="h4Repairing"></h4>
                        <p id="pRepairing"></p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-5 infchart">
                <div class=" panel panel-flat panel-body">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tabE" data-toggle="tab"
                                style="font-size:13px; padding: 6px;">Equipment</a>
                        </li>
                        <li><a href="#tabF" data-toggle="tab"
                                style="font-size:13px; padding: 6px;">Fixture</a></li>
                        <li style="float: right;">
                            <div class="input-group">
                                <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                                    style="height: 28px; width: 18rem;">
                                <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i
                                        class="icon-calendar22"></i></span>
                            </div>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="tabE" class='tab-pane active'>
                            <div id="peformanceChartE" style="height: 207px;">jj</div>
                        </div>
                        <div id="tabF" class='tab-pane'>
                            <div id="peformanceChartF" style="height: 207px;">ffjj</div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body">
                    <ul class="nav nav-tabs">
                        <li id="liBuy" class="active"><a href="#tabBuy" data-toggle="tab"
                                style="font-size:13px; padding: 6px;">Buy</a>
                        </li>
                        <li id="liBorrow"><a href="#tabBorrow" data-toggle="tab"
                                style="font-size:13px; padding: 6px;">Borrow</a></li>
                        <li id="liRent"><a href="#tabRent" data-toggle="tab"
                                style="font-size:13px; padding: 6px;">Rent</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="tabBuy" class="table-responsive pre-scrollable tableFixHead tab-pane active">
                            <table id="tblBuy" class="table table-xxs table-fixed text-nowrap table-sticky">
                                <thead>
                                    <tr>
                                        <th> No </th>
                                        <th>Name</th>
                                        <th>E/F</th>
                                        <th>Type</th>
                                        <th>L.Calib Time</th>
                                        <th>Due date</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                        <div id="tabBorrow" class="table-responsive pre-scrollable tableFixHead tab-pane">
                            <table id="tblBorrow" class="table table-xxs table-fixed text-nowrap table-sticky">
                                <thead>
                                    <tr>
                                        <th> No </th>
                                        <th>Name</th>
                                        <th>E/F</th>
                                        <th>Type</th>
                                        <th>Borrow Time</th>
                                        <th>Due date</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                        <div id="tabRent" class="table-responsive pre-scrollable tableFixHead tab-pane">
                            <table id="tblRent" class="table table-xxs table-fixed text-nowrap table-sticky">
                                <thead>
                                    <tr>
                                        <th> No </th>
                                        <th>Name</th>
                                        <th>E/F</th>
                                        <th>Type</th>
                                        <th>Rent Time</th>
                                        <th>Due date</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-xs-12 col-sm-3 infchart">
                <div class=" panel panel-flat panel-body">
                    <div class="panel-tittle row" style="margin-bottom: 0;">
                        <span id="spanBuychart">Buy</span>
                    </div>
                    <div id="buyChart" style="height: 219px;"></div>
                </div>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-3 infchart">
                <div class=" panel panel-flat panel-body">
                    <div class="panel-tittle row">
                        <span>Owner Type</span>
                    </div>
                    <div id="ownedTypeChart" style="height: 207px;"></div>
                </div>
            </div>
            <div class="col-xs-12 col-sm-9 infchart">
                <div class=" panel panel-flat panel-body">
                    <div class="panel-tittle row">
                        <span>Online Equipments by Device Type</span>
                    </div>
                    <div id="onlineChart" style="height: 207px;"></div>
                </div>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-12">
                <div class="panel panel-body" style="min-height: 420px;">
                    <ul class="nav nav-tabs">
                        <li class="active"><a href="#tblEquipment" id="liEquipments" data-toggle="tab"
                                style="font-size:13px; padding: 6px;">Equipments</a>
                        </li>
                        <li><a href="#tblFixture" id="liFixture" data-toggle="tab"
                                style="font-size:13px; padding: 6px;">Fixture</a></li>
                        <li style='float:right;margin-right: 1rem;'>
                            <div class="tableFitter">
                                <label><input type="search" id="searchbox" placeholder="Search..."
                                        aria-controls="tblEfDetail"></label>
                            </div>
                        </li>
                    </ul>
                    <div class="tab-content">
                        <div id="tblEquipment" class="tab-pane active">
                            <div id="divTableEquipment" class="table-responsive col-md-12"
                                style="max-height: auto;max-width: 100%">

                            </div>
                        </div>
                        <div id="tblFixture" class="tab-pane">
                            <div id="divTableFixture" class="table-responsive col-md-12"
                                style="max-height: auto;max-width: 100%">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var ownerType = 'buy';
    getTimeNow();
    loadEfSumary();
    loadTblReminder();
    loadBuyChart(ownerType);
    loadOwnedTypeChart();
    loadOnlineChart();
    loadEfDetail();
    function loadEfSumary() {
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/ef/status/sumary",
            data: {
                factory: 'b06',
                ownerType: ''
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var data = res;
                for (i in data) {
                    if (data[i].rate != 0) {
                        data[i].rate = data[i].rate.toFixed(2);
                    }
                }
                $('#h4Using').html('' + data.using.rate + '<sup>%</sup>');
                $('#pUsing').html('Using (' + data.using.qty + '/' + data.using.total + ')');
                $('#h4Offline').html('' + data.offline.rate + '<sup>%</sup>');
                $('#pOffline').html('Offline (' + data.offline.qty + '/' + data.offline.total + ')');
                $('#h4Cabrating').html('' + data.calibrating.rate + '<sup>%</sup>');
                $('#pCabrating').html('Calibrating (' + data.calibrating.qty + '/' + data.calibrating.total + ')');
                $('#h4Repairing').html('' + data.repairing.rate + '<sup>%</sup>');
                $('#pRepairing').html('Repairing (' + data.repairing.qty + '/' + data.repairing.total + ')');
            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    function loadEfDetail() {
        var html1 = '';
        var html2 = '';
        var tableEquipment = '<table id="tblEfDetailEquipment" class="table table-custom table-bordered table-sticky"></table>';
        var tableFixture = '<table id="tblEfDetailFixture" class="table table-custom table-bordered table-sticky"></table>';
        var myTable = '<thead>'
            + '<tr><th>No</th><th>Name</th><th>Type</th>'
            + '<th>Ownner</th><th>Line</th><th>User model</th>'
            + '<th>Station</th><th>Status</th><th>Time on line</th>'
            + '<th>Time off line</th><th>Used time</th><th>Free time</th>'
            + '<th>Performance (%)</th></tr>'
            + '</thead>'
            + '<tbody></tbody>';
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/ef/detail/list",
            data: {
                factory: 'b06',
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var index = 0;
                for (i in res) {
                    var data = res[i];
                    if (i == 'equipmentResult') {
                        $('#divTableEquipment').html('');
                        $('#divTableEquipment').html(tableEquipment);
                        $('#tblEfDetailEquipment').html(myTable);
                        for (j in data) {
                            index = Number(j) + 1;
                            html1 += '<tr><td>' + index + '</td>'
                                + '<td style="white-space: initial;">' + data[j].name + '</td>'
                                + '<td>' + data[j].type + '</td>'
                                + '<td>' + data[j].ownner + '</td>'
                                + '<td>' + data[j].line + '</td>'
                                + '<td>' + data[j].model + '</td>'
                                + '<td>' + data[j].station + '</td>'
                                + '<td>' + data[j].status + '</td>'
                                + '<td>' + data[j].onlineTime.toFixed(2) + '</td>'
                                + '<td>' + data[j].offlineTime.toFixed(2) + '</td>'
                                + '<td>' + data[j].useTime.toFixed(2) + '</td>'
                                + '<td>' + data[j].freeTime.toFixed(2) + '</td>'
                                + '<td>' + data[j].performance + '</td></tr>';
                        }
                        $('#tblEfDetailEquipment tbody').html(html1);
                        var myTable1 = $('#tblEfDetailEquipment').DataTable({
                            scrollX: true,
                            "bLengthChange": false,
                            "bInfo": false,
                            "bAutoWidth": false,
                            "columnDefs": [
                                { "width": "0.5rem", "targets": 0 },
                            ]
                        });
                        $("#searchbox").keyup(function () {
                            myTable1.search(this.value).draw();
                        });
                    }
                    else {
                        $('#divTableFixture').html('');
                        $('#divTableFixture').html(tableFixture);
                        $('#tblEfDetailFixture').html(myTable);
                        for (j in data) {
                            index = Number(j) + 1;
                            html2 += '<tr><td>' + index + '</td>'
                                + '<td style="white-space: initial;">' + data[j].name + '</td>'
                                + '<td>' + data[j].type + '</td>'
                                + '<td>' + data[j].ownner + '</td>'
                                + '<td>' + data[j].line + '</td>'
                                + '<td>' + data[j].model + '</td>'
                                + '<td>' + data[j].station + '</td>'
                                + '<td>' + data[j].status + '</td>'
                                + '<td>' + data[j].onlineTime.toFixed(2) + '</td>'
                                + '<td>' + data[j].offlineTime.toFixed(2) + '</td>'
                                + '<td>' + data[j].useTime.toFixed(2) + '</td>'
                                + '<td>' + data[j].freeTime.toFixed(2) + '</td>'
                                + '<td>' + data[j].performance + '</td></tr>';
                        }
                        $('#tblEquipment').removeClass('active');
                        $('#tblFixture').addClass('active');
                        $('#tblEfDetailFixture tbody').html(html2);
                        // myTable1.clear().draw();
                        myTable2 = $('#tblEfDetailFixture').DataTable({
                            scrollX: true,
                            "bLengthChange": false,
                            "bInfo": false,
                            "bAutoWidth": false,
                            "columnDefs": [
                                { "width": "0.5rem", "targets": 0 },
                            ]
                        });
                        $("#searchbox").keyup(function () {
                            myTable2.search(this.value).draw();
                        });

                    }
                    $('#tblEquipment').addClass('active');
                    $('#tblFixture').removeClass('active');


                }
            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    function loadPeformanceChart(startTime, endTime) {
        var dataHE = [], dataLE = [], dataHF = [], dataLF = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/ef/performance/trend",
            data: {
                factory: 'b06',
                startTime: startTime,
                endTime: endTime
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var SYear = new Date(startTime).getFullYear();
                var SMonth = new Date(startTime).getMonth();
                var SDay = new Date(startTime).getDate();
                var EYear = new Date(endTime).getFullYear();
                var EMonth = new Date(endTime).getMonth();
                var EDay = new Date(endTime).getDate();
                for (i in res) {
                    data = res[i].data;
                    if (i == 'highestEquipment') {
                        for (j in data) { dataHE.push(data[j].USEDTIME); }
                    }
                    if (i == 'highestFixture') {
                        for (j in data) { dataHF.push(data[j].USEDTIME); }
                    }
                    if (i == 'lowestFixture') {
                        for (j in data) { dataLF.push(data[j].USEDTIME); }
                    }
                    if (i == 'lowestEquipment') {
                        for (j in data) { dataLE.push(data[j].USEDTIME); }
                    }
                }
                // console.log('startTime',startTime)
                // console.log('endTime',endTime)
                // console.log('start',SYear,SMonth,SDay);
                // console.log('end',EYear,EMonth,EDay);
                Highcharts.chart('peformanceChartF', {
                    chart: {
                        type: 'spline',
                        scrollablePlotArea: {
                            minWidth: 0,
                            scrollPositionX: 1
                        }
                    },
                    title: {
                        text: '',
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    yAxis: {
                        title: '',
                        minorGridLineWidth: 0,
                        gridLineWidth: 0,
                        alternateGridColor: null,
                    },
                    tooltip: {
                        valueSuffix: ' times/h'
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 2,
                            states: {
                                hover: {
                                    lineWidth: 3
                                }
                            },
                            marker: {
                                enabled: false
                            },
                            pointInterval: 3600000,
                            pointStart: Date.UTC(SYear, SMonth, SDay, 7, 30, 0),
                            pointEnd: Date.UTC(EYear, EMonth, EDay, 7, 30, 0),
                            label: {
                                enabled: false
                            }
                        }
                    },
                    credits: { enabled: false },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: [{
                        name: 'H.Fixture',
                        color:'#1f7f23',
                        data: dataHF
                    }, {
                        name: 'L.Fixture',
                        color:'#333333',
                        data: dataLF
                    }],
                });
                Highcharts.chart('peformanceChartE', {
                    chart: {
                        type: 'spline',
                        scrollablePlotArea: {
                            minWidth: 0,
                            scrollPositionX: 1
                        }
                    },
                    title: {
                        text: '',
                    },
                    xAxis: {
                        type: 'datetime',
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    yAxis: {
                        title: '',
                        minorGridLineWidth: 0,
                        gridLineWidth: 0,
                        alternateGridColor: null,
                    },
                    tooltip: {
                        valueSuffix: ' times/h'
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 2,
                            states: {
                                hover: {
                                    lineWidth: 3
                                }
                            },
                            marker: {
                                enabled: false
                            },
                            pointInterval: 3600000,
                            pointStart: Date.UTC(SYear, SMonth, SDay, 7, 30, 0),
                            pointEnd: Date.UTC(EYear, EMonth, EDay, 7, 30, 0),
                            label: {
                                enabled: false
                            }
                        }
                    },
                    credits: { enabled: false },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: [{
                        name: 'H.Equipment',
                        data: dataHE

                    }, {
                        name: 'L.Equipment',
                        data: dataLE
                    }],
                });
            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    $("#liBuy").on("click", function () {
        $('#spanBuychart').html('Buy');
        ownerType = 'buy';
        loadBuyChart(ownerType);
    });
    $("#liBorrow").on("click", function () {
        $('#spanBuychart').html('Borow');
        ownerType = 'borrow';
        loadBuyChart(ownerType);
    });
    $("#liRent").on("click", function () {
        $('#spanBuychart').html('Rent');
        ownerType = 'rent';
        loadBuyChart(ownerType);
    });

    function loadTblReminder() {
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/ef/duedate/reminder",
            data: {
                factory: 'b06',
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var index = 1;
                var html;
                for (i in res) {
                    data = res[i];
                    // console.log(data);
                    for (j in data) {
                        if (data[j].receiveTime != null) {
                            data[j].receiveTime = moment(new Date(data[j].receiveTime)).format("YYYY/MM/DD");
                            data[j].returnTime = moment(new Date(data[j].returnTime)).format("YYYY/MM/DD");
                        }
                    }
                    if (i == 'buy') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html= '<tr><td>' + index + '</td>'
                                + '<td style="white-space: initial;">' + data[j].name + '</td>'
                                + '<td>' + data[j].EF + '</td>'
                                + '<td>' + data[j].type + '</td>'
                                + '<td>' + data[j].receiveTime + '</td>'
                                + '<td class="reminderbuy'+j+'">' + data[j].returnTime + '</td>'
                                + '</tr>';
                            $('#tblBuy tbody').append(html);
                            if(data[j].reminder=='alert'){
                                $('.reminderbuy'+j+'').addClass('reminderAlert');
                            }
                            else if(data[j].reminder=='warning'){
                                $('.reminderbuy'+j+'').addClass('warning');
                            }
                        }
                    }
                    else if (i == 'borrow') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html= '<tr><td>' + index + '</td>'
                                + '<td style="white-space: initial;">' + data[j].name + '</td>'
                                + '<td>' + data[j].EF + '</td>'
                                + '<td>' + data[j].type + '</td>'
                                + '<td>' + data[j].receiveTime + '</td>'
                                + '<td class="reminderborrow'+j+'">' + data[j].returnTime + '</td>'
                                + '</tr>';
                            $('#tblBorrow tbody').append(html);
                            if(data[j].reminder=='alert'){
                                $('.reminderborrow'+j+'').addClass('reminderAlert');
                            }
                            else if(data[j].reminder=='warning'){
                                $('.reminderborrow'+j+'').addClass('warning');
                            }
                        }
                        
                    }
                    else if (i == 'rent') {
                        html = '';
                        for (j in data) {
                            index = Number(j) + 1;
                            html= '<tr><td>' + index + '</td>'
                                + '<td style="white-space: initial;">' + data[j].name + '</td>'
                                + '<td>' + data[j].EF + '</td>'
                                + '<td>' + data[j].type + '</td>'
                                + '<td>' + data[j].receiveTime + '</td>'
                                + '<td class="reminderrent'+j+'">' + data[j].returnTime + '</td>'
                                + '</tr>';
                            $('#tblRent tbody').append(html);
                            if(data[j].reminder=='alert'){
                                $('.reminderrent'+j+'').addClass('reminderAlert');
                            }
                            else if(data[j].reminder=='warning'){
                                $('.reminderrent'+j+'').addClass('warning');
                            }
                        }
                        
                    }
                }

            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    function loadBuyChart(ownerType) {
        var colors = ['#ababab', '#28a745', '#1a9bba', '#fac010'];
        dataBuyChart = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/ef/status/sumary",
            data: {
                factory: 'b06',
                ownerType: ownerType
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                data = res;
                for (i in data) {
                    dataBuyChart.push({ name: i, y: data[i].qty, total: data[i].total });
                }
                Highcharts.chart('buyChart', {
                    chart: {
                        type: 'pie'
                    },
                    title: {
                        text: '',
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.y}/{point.total}</b>'
                    },
                    accessibility: {
                        point: {
                            valueSuffix: '%'
                        }
                    },
                    plotOptions: {
                        pie: {
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f} %',
                                connectorColor: 'silver'
                            },
                            showInLegend: true
                        }
                    },
                    legend: {
                        itemStyle: {
                            fontSize: '12px',
                            fontWeight: '100'
                        },
                        layout: 'horizontal',
                        align: 'center',
                        verticalAlign: 'bottom'
                    },
                    credits: { enabled: false },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function (color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')]
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataBuyChart
                    }]
                });
            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    function loadOwnedTypeChart() {
        var colors = ['#7cb5ec', '#90ed7d', '#8085e9'];
        var dataOwnerChart = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/ef/owner/sumary",
            data: {
                factory: 'b06',
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                data = res;
                for (i in data) {
                    dataOwnerChart.push({ name: data[i].owner, y: data[i].qty, totalError: data[i].qty });
                }
                Highcharts.chart('ownedTypeChart', {
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },
                    title: {
                        text: '',
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.y}/{point.total}</b>'
                    },
                    accessibility: {
                        point: {
                            valueSuffix: '%'
                        }
                    },
                    plotOptions: {
                        pie: {
                            // allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f} %',
                                connectorColor: 'silver'
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
                        align: 'center',
                        verticalAlign: 'bottom'
                    },
                    credits: { enabled: false },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function (color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataOwnerChart,
                    }]
                });
            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    function loadOnlineChart() {
        var dataOnlineChart = [];
        $.ajax({
            type: "GET",
            url: "/api/test/efmanager/ef/status/online/by/type",
            data: {
                factory: 'b06',
            },
            contentType: "application/json; charset=utf-8",
            success: function (res) {
                var data = res;
                for (i in data) {
                    dataOnlineChart[i] = { name: data[i].TYPE, y: data[i].RATE, qty: data[i].QTY, total: data[i].TOTAL };
                }
                var colors = ['#28a745', '#ababab', '#1a9bba', '#fac010'];
                Highcharts.chart('onlineChart', {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: '',
                    },
                    xAxis: {
                        type: 'category',
                        labels: {
                            rotation: -45,
                            style: {
                                fontSize: '13px',
                                fontFamily: 'Verdana, sans-serif'
                            }
                        }
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'Rate (%)'
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    tooltip: {
                        pointFormat: 'Qty: <b>{point.qty}/{point.total}</b>'
                    },
                    credits: { enabled: false },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    colors: Highcharts.map(colors, function (color) {
                        return {
                            radialGradient: {
                                cx: 0.5,
                                cy: 0.3,
                                r: 0.7
                            },
                            stops: [
                                [0, color],
                                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
                            ]
                        };
                    }),
                    series: [{
                        name: '',
                        data: dataOnlineChart,
                        dataLabels: {
                            enabled: true,
                            format: '{point.y:.1f}%', // one decimal
                            style: {
                                fontSize: '9px',
                                fontFamily: 'Verdana, sans-serif'
                            }
                        }
                    }]
                });
            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "api/test/efmanager/time/shift/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(data.startTime));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(data.endTime));
                loadPeformanceChart(data.startTime, data.endTime);
                $('input[name=timeSpan]').on('change', function () {
                    var startTime = $('.datetimerange').data('daterangepicker').startDate.format('YYYY/MM/DD HH:mm:00');
                    var endTime = $('.datetimerange').data('daterangepicker').endDate.format('YYYY/MM/DD HH:mm:00');
                    loadPeformanceChart(startTime, endTime);
                });
            },
            error: function () {
                alert("Fail!");
            }
        });
    }
</script>