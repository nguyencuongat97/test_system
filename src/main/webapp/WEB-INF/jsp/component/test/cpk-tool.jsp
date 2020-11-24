<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<style>
    .card {
        box-shadow: 0 0 1px rgba(0, 0, 0, .125), 0 1px 3px rgba(0, 0, 0, .2);
        margin-bottom: 1rem;
    }

    .card {
        position: relative;
        display: -ms-flexbox;
        display: flex;
        -ms-flex-direction: column;
        flex-direction: column;
        min-width: 0;
        word-wrap: break-word;
        background-color: #fff;
        background-clip: border-box;
        border-radius: .25rem;
        border: 1px solid #d7d7d7;
    }

    .card-header {
        background-color: transparent;
        border-bottom: 1px solid rgba(0, 0, 0, .125);
        padding: 0px;
        position: relative;
        border-top-left-radius: .25rem;
        border-top-right-radius: .25rem;
        margin-bottom: 0px;
    }

    .custom-file-label {
        position: absolute;
        top: 0;
        right: 0;
        left: 0;
        z-index: 1;
        height: calc(2.25rem + 2px);
        padding: .375rem .75rem;
        font-weight: 400;
        line-height: 1.5;
        color: #495057;
        background-color: #fff;
        border: 1px solid #ced4da;
        border-radius: .25rem;
        box-shadow: none;
    }

    .custom-file-label {
        font-size: 13px;
        line-height: 1;
    }

    .custom-file-input {
        position: relative;
        z-index: 2;
        width: 100%;
        margin: 0;
        opacity: 0;
    }

    .custom-file {
        position: relative;
        display: inline-block;
        width: 100%;
        margin-bottom: 0;
    }

    .input-lsl {
        width: 50px;
    }

    .input-usl {
        width: 50px;
    }

    #tblDetailTablSetUp thead th,
    #tblDetailTablSetUp tbody td {
        text-align: center;
        padding: 3px;
        border: 1px solid #1D7831;
    }

    #tblDetailTablSetUp thead th {
        background-color: #1F74B1;
        color: #FFF;
    }

    #tblDetailTablSetUp tbody tr {
        cursor: pointer;
    }

    .rate-danger {
        background-color: #FF040F;
        color: #FFF;
    }

    .rate-success {
        background-color: #00B15A;
        color: #FFF;
    }

    .nav-tabs>li.active>a,
    .nav-tabs>li.active>a:hover,
    .nav-tabs>li.active>a:focus {
        color: #1F74B1;
        font-weight: bold;
        background-color: #fff;
        border: 1px solid #1F74B1;
        border-bottom-color: transparent;
        cursor: default;
    }

    .nav-tabs {
        border-bottom: 1px solid #1F74B1;
    }

    #tbl-item {
        margin: 0px;
    }

    #tbl-item {
        width: 100%;
        max-width: 100%;
    }

    #tbl-item thead th {
        text-align: center;
        background: #1F74B1;
        color: #fff;
        padding: 5px;
    }

    #tbl-item tbody tr td {
        text-align: center;
        padding: 5px;
    }

    #tbl-item tr:first-child th {
        position: sticky;
        top: 0;
    }

    #tbl-item tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    #tbl-item tr:hover {
        background-color: #ddd;
    }

    #tbl-item tbody tr:hover {
        background: #d7d7d7;
        cursor: pointer;
    }

    .w3-input {
        padding: 4px;
        display: block;
        border: none;
        width: 80%;
    }

    .w3-round-large {
        border-radius: 3px;
    }

    .w3-border {
        border: 1px solid #ccc !important;
    }

    ::-webkit-scrollbar {
        width: 4px;
    }

    ::-webkit-scrollbar-thumb {
        border-radius: 5px;
        background: #1F74B1;
    }

    ::-webkit-scrollbar-thumb:hover {
        background: #013992;
    }

    .btn-defaulta:focus,
    .btn-defaulta.focus,
    .btn-defaulta:hover {
        background-color: #1F74B1 !important;
        color: #fff !important;
    }

    .btn-sm,
    .btn-group-sm>.btn {
        padding: 6px 15px;
        background: #d7d7d7;
    }

    .custom-file-label {
        font-size: 13px;
        line-height: 0 !important;
    }

    .custom-file-label {
        position: absolute;
        top: 0;
        right: 0;
        left: 0;
        z-index: 1;
        height: calc(2.25rem + 2px);
        padding: 1.375rem 0.75rem;
        font-weight: 400;
        color: #495057;
        background-color: #fff;
        border: 1px solid #ced4da;
        border-radius: .25rem;
        box-shadow: none;
    }

    #btn-caculate {
        float: left;
        margin-left: 8px;
    }

    #tblDetailTablSetUp {
        margin: 0px;
    }

    #tblDetailTablSetUp {
        width: 100%;
        max-width: 100%;
    }

    #tblDetailTablSetUp thead th {
        text-align: center;
        background: #1F74B1;
        color: #fff;
        padding: 5px;
    }

    #tblDetailTablSetUp tbody tr td {
        text-align: center;
        padding: 5px;
    }

    #tblDetailTablSetUp tr:first-child th {
        position: sticky;
        top: 0;
    }

    #tblDetailTablSetUp tr:nth-child(even) {
        background-color: #f2f2f2;
    }

    #tblDetailTablSetUp tr:hover {
        background-color: #ddd;
    }

    #tblDetailTablSetUp tbody tr:hover {
        background: #d7d7d7;
        cursor: pointer;
    }

    #export-excel {
        float: right;
    }

    #btn-upload:disabled {
        cursor: not-allowed;
    }

    .loader {
        display: block;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) url('/assets/images/loadingg.gif') 50% 50% no-repeat;
    }

    .loader p {
        margin-top: 5%;
        color: #FFF;
        font-size: 20px;
        text-align: center;
    }

    #tblDetail thead th,
    #tblDetail tbody td {
        text-align: center;
        padding: 3px;
        border: 1px solid #1D7831;
    }

    #tblDetail thead th {
        background-color: #1F74B1;
        color: #FFF;
    }

    #tblDetail tbody tr {
        cursor: pointer;
    }

    #tblInfor tr td:first-child {
        text-align: center;
        padding: 2px 0;
    }

    #tblInfor tr td:last-child {
        text-align: right;
        font-weight: 500;
    }


    .form-control {
        height: 29px !important;
    }
</style>
<div class="loader hidden">
    <p>The Process Is Take A While </br> Please Waiting A Minute!</p>
</div>
<div class="row" style="padding-top:5px; background-color: #fff; height: 90vh;">
    <div class="col-md-12" style="height: 100%;">
        <div class="input-group" style="margin: 10px 0px 10px 0px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i
                    class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                style="height: 26px; color: inherit;" id="txt-time-span" onchange="changeTime(this)">
        </div>
        <div class="content" style="height: 95%;">
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#home">CPK From DB</a></li>
                <li><a data-toggle="tab" href="#menu1">CPK From Excel</a></li>
            </ul>
            <div class="tab-content" style="height: 90%;">
                <!-- load cpk -->
                <div id="home" class="tab-pane fade in active" style="height: 100%;">

                    <div class="panel panel-overview"
                        style="padding: 5px 15px; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
                        <div class="row">
                            <div class="col-lg-2" name="customer" style="display: none;">
                                <select class="form-control bootstrap-select" name="customer" data-live-search="true"
                                    data-focus-off="true">
                                </select>
                            </div>
                            <div class="col-lg-2" name="modelName">
                                <select class="form-control bootstrap-select" name="modelName" data-live-search="true"
                                    data-focus-off="true">
                                </select>
                            </div>
                            <div class="col-lg-2">
                                <select class="form-control bootstrap-select" name="groupName">
                                </select>
                            </div>
                            <div class="col-lg-2">
                                <select class="form-control bootstrap-select" name="stationName" data-live-search="true"
                                    data-focus-off="true">
                                </select>
                            </div>
                            <div class="col-lg-3">
                                <select class="form-control bootstrap-select" name="parameter" multiple
                                    data-live-search="true" data-none-selected-text="Select Parameters"
                                    data-focus-off="true">
                                </select>
                            </div>
                            <div class="col-lg-1">
                                <div class="form-group no-margin">
                                    <div class="col-xs-2 no-padding">
                                        <input type="checkbox" class="form-control" name="onlyPass" id="cbxOnlyPass"
                                            checked="true" style="width: 20px;">
                                    </div>
                                    <label class="col-xs-10 control-label text-bold no-padding" for="cbxOnlyPass"
                                        style="margin-top: 12px;">Only Pass</label>
                                </div>
                                <label></label>
                            </div>
                            <div class="col-lg-1">
                                <button class="btn btn-defaulta btn-sm" onclick="loadData()"><i
                                        class="fa fa-search"></i>
                                    Filter</button>
                            </div>
                            <button class="btn btn-defaulta btn-sm" onclick="exportData()"
                                style="float: right; margin-right: 8px;"><i class="fa fa-download"></i>
                                Export</button>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-7">
                            <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 222px);">
                                <table id="tblDetail" class="table table-small table-sticky hidden">
                                    <thead>
                                        <tr>
                                            <th>Test Item</th>
                                            <th>Min</th>
                                            <th>Avg</th>
                                            <th>Max</th>
                                            <th>LSL</th>
                                            <th>USL</th>
                                            <th>Prime</th>
                                            <th>CPK</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <div class="panel panel-flat panel-body no-padding hidden" id="chart-detail"
                                style="height: calc(100vh - 222px);"></div>
                        </div>
                        <div class="col-lg-2">
                            <table id="tblInfor" class="hidden">
                                <tbody></tbody>
                                <tfoot>

                                </tfoot>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- caculate cpk -->
                <div id="menu1" class="tab-pane fade" style="height: 100%;">
                    <div class="panel panel-overview"
                        style="padding: 5px 15px; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
                        <div class="row" style="padding: 5px;">
                            <div id="formCustomFile" style="float: left;margin-right: 0.5rem;">
                                <div class="custom-file">
                                    <input type="file" class="custom-file-input" id="customFile">
                                    <label class="custom-file-label" for="customFile">Choose file</label>
                                </div>
                            </div>
                            <button class="btn btn-defaulta btn-sm legitRipple" style="float: left;"
                                onclick="importFileCpk()" id="btn-upload"><i class="fa fa-download"></i>Upload</button>
                            <button class="btn btn-defaulta btn-sm legitRipple" onclick="queryData()"
                                id="btn-caculate">Caculate</button>
                            <button class="btn btn-defaulta btn-sm legitRipple" id="btn-back" onclick="back()"
                                style="float: left; margin-left: 8px;">Back</button>
                            <button class="btn btn-defaulta btn-sm legitRipple" id="btn-next" onclick="next()"
                                style="float: left; margin-left: 8px;">Next</button>
                            <button class="btn btn-defaulta btn-sm legitRipple" id="export-excel"
                                onclick="exportDataTabSetUp()"><i class="fa fa-download"></i>Export</button>
                        </div>
                    </div>
                    <div class="row" style="height: 88%;">
                        <div class="detail-item hidden" style="height: 100%;">
                            <div class="col-md-6" style="height: 100%;">
                                <div class="card" style="height: 100%;width: 100%;">
                                    <div class="card-header" style="padding: 2px 5px 2px 0px;line-height: 3rem;">
                                        <span id="txt-total-item" style="color: #1F74B1;">0</span><span
                                            style="margin-left: 5px;">Item Test</span>
                                        <div class="form-group" style="width: 200px;float: right;margin:0px;">
                                            <div class="input-group date">
                                                <input type="text" class="form-control" placeholder="Search..."
                                                    style="border: 1px solid #d7d7d7;padding: 8px;"
                                                    id="txt-search-table">
                                                <span class="input-group-addon"
                                                    style="padding: 5px 10px 5px 10px; background: #d7d7d7;">
                                                    <span class="fa fa-search"></span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="card-body" style="height: 100%; overflow: scroll;">
                                        <table class="table" style="width: 100%;" id="tbl-item">
                                            <thead>
                                                <th>#</th>
                                                <th>Test Item</th>
                                                <th>Sample Value</th>
                                                <th>LSL</th>
                                                <th>USL</th>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td colspan="5" style="background: #d7d7d7;">No data !</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-8"> </div>
                        </div>
                        <div class="detail-cpk" style="height: 100%;">
                            <div class="row" style="height: 100%;">
                                <div class="col-md-7" style="height: 100%;">

                                    <div class="" style="height: 100%; overflow: scroll;">
                                        <table id="tblDetailTablSetUp" class="table table-small table-sticky hidden">
                                            <thead>
                                                <tr>
                                                    <th>Test Item</th>
                                                    <th>Min</th>
                                                    <th>Avg</th>
                                                    <th>Max</th>
                                                    <th>LSL</th>
                                                    <th>USL</th>
                                                    <th>Prime</th>
                                                    <th>CPK</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="panel panel-flat panel-body no-padding hidden"
                                        id="chart-detail-tab-set-up" style="height: calc(100vh - 222px);"></div>
                                </div>
                                <div class="col-md-2">
                                    <table id="tblInforTabSetUp">
                                        <tbody></tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>
<script>

    var testItemList = {
        testItem: "",
        columnIndex: "",
        lsl: "",
        usl: ""
    }

    var tmpFile = "";
    init();
    function init() {
        unactiveView();
    }
    function unactiveView() {
        $("#btn-caculate").css("display", "none");
        $("#export-excel").css("display", "none");
        $(".detail-cpk").css("display", "none");
        $("#btn-back").css("display", "none");
        $("#btn-next").css("display", "none");
        $("#btn-upload").attr("disabled", true);
    }
    function activeView() {
        $("#btn-caculate").css("display", "block");
        $("#btn-caculate").attr("disabled", false);
    }
    function changeTime(obj) {


    }
    $("#customFile").on("change", function () {
        $(".custom-file-label").html($("#customFile").val());
        $("#btn-upload").attr("disabled", false);
        $("#btn-caculate").css("display", "none");
        $(".detail-cpk").css("display", "none");
        $("#export-excel").css("display", "none");
        $(".detail-item").css("display", "block");
        $("#tbl-item tbody").html('<tr><td colspan="5" style="background: #d7d7d7;">No data !</td></tr>');
        $("#txt-total-item").html("0");
        $("#btn-next").css("display", "none");
        $("#btn-back").css("display", "none");
    });

    function importFileCpk() {
        $(".detail-item").removeClass("hidden");
        var fileImport = $("#customFile").prop('files')[0];
        var form = new FormData();
        form.append("file", fileImport);
        $.ajax({
            "async": true,
            "crossDomain": true,
            "url": "/api/test/station/cpk/from-file/upload",
            "method": "POST",
            "processData": false,
            "contentType": false,
            "mimeType": "multipart/form-data",
            "data": form,
            success: function (res) {
                res = JSON.parse(res);
                tmpFile = res["result"]["tmpFile"];
                var testItemList = res["result"]['testItemList'];
                var htmlRow = "";
                if (testItemList.length > 0) {
                    for (var i = 0; i < testItemList.length; i++) {
                        htmlRow += '<tr class="row-cpk" id="row-cpk-' + i + '" col-index="' + testItemList[i].columnIndex + '">' +
                            '<td>' + (i + 1) + '</td >' +
                            '<td class="testItem">' + testItemList[i].testItem + '</td>' +
                            '<td>#</td>' +
                            '<td><input type="number" class="input-lsl w3-input w3-border w3-round-large" onkeyup="checkInput(this)"/></td>' +
                            '<td><input type="number" class="input-usl w3-input w3-border w3-round-large" onkeyup="checkInput(this)"/></td>' +
                            '</tr>';
                    }
                    $("#txt-total-item").html(testItemList.length);
                    $("#tbl-item tbody").html(htmlRow);
                    $("#btn-upload").attr("disabled", true);
                }
            }
        });

    }

    function checkInput(obj) {
        var inputLsl = obj.value;
        var inputUsl = obj.value;
        if (inputLsl.length > 0 && inputUsl.length > 0) {
            activeView();

        }
    }

    var dataset = {
        factory: '${factory}',
        customer: '${customer}',
        modelName: '${modelName}',
        groupName: '${groupName}',
        stationName: '${stationName}',
        parameter: true
    }
    var dataDraw = [];
    function loadDataViewRowCpk() {
        var rowCpks = document.getElementsByClassName("row-cpk");
        var cpkItems = [];
        for (var i = 0; i < rowCpks.length; i++) {
            var idView = rowCpks[i].getAttribute("id");
            var testItem = $("#" + idView + " .testItem").html();
            var colIndex = $("#" + idView).attr("col-index");
            var inputLsl = $("#" + idView + " .input-lsl").val();
            var inputUsl = $("#" + idView + " .input-usl").val();
            var item = {
                "testItem": testItem,
                "columnIndex": colIndex,
                "lsl": inputLsl,
                "usl": inputUsl
            }
            cpkItems.push(item);
        }
        return cpkItems;
    }

    function queryData() {
        $("#btn-caculate").attr("disabled", true);
        $("#btn-back").attr("disabled", false);
        $("#btn-next").attr("disabled", true);
        $("#btn-back").css("display", "block");
        $('#tblInforTabSetUp').addClass('hidden');
        $('#chart-detail-tab-set-up').addClass('hidden');
        $(".detail-item").css("display", "none");
        $(".detail-cpk").css("display", "block");
        $("#export-excel").css("display", "block");

        var cpkItems = loadDataViewRowCpk();
        var dataReq = {
            "tmpFile": tmpFile,
            "testItemList": cpkItems
        }

        $.ajax({
            type: 'POST',
            url: "/api/test/station/cpk/from-file/calc",
            cache: false,
            data: JSON.stringify(dataReq),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                $('#tblDetailTablSetUp>tbody').html('');
                $('#tblDetailTablSetUp').removeClass('hidden');
                dataset.index = -1;
                if (response != null) {
                    var data = response.data;
                    dataDraw = response.data;
                    var html = '';
                    for (i in data) {
                        var classRate = '';
                        if (data[i].cpk < 1.33) {
                            classRate = 'rate-danger';
                        } else {
                            classRate = 'rate-success';
                        }
                        html += '<tr onclick="loadDetailTabSetUp(' + i + ')"><td>' + data[i].parameter + '</td>'
                            + '<td>' + data[i].min.toFixed(3) + '</td>'
                            + '<td>' + data[i].average.toFixed(3) + '</td>'
                            + '<td>' + data[i].max.toFixed(3) + '</td>'
                            + '<td>' + (data[i].lsl != null ? data[i].lsl.toFixed(3) : '') + '</td>'
                            + '<td>' + (data[i].usl != null ? data[i].usl.toFixed(3) : '') + '</td>'
                            + '<td>' + data[i].sigmaWithin.toFixed(3) + '</td>'
                            + '<td class="' + classRate + '">' + data[i].cpk.toFixed(3) + '</td></tr>';
                    }
                    $('#tblDetailTablSetUp>tbody').html(html);
                }
            }
        });
    }

    function loadDetailTabSetUp(index) {
        console.log(dataDraw[index])
        $('#tblInforTabSetUp').removeClass('hidden');
        $('#chart-detail-tab-set-up').removeClass('hidden');
        if (dataset.index != index) {
            $('#tblInforTabSetUp>tbody').html('');
            var html = '';
            html += '<tr><td></td><td>Process Data</td></tr>'
                + '<tr><td>Sample Size</td><td>' + dataDraw[index].sampleSize + '</td></tr>'
                + '<tr><td>Avg</td><td>' + dataDraw[index].average.toFixed(3) + '</td></tr>'
                + '<tr><td>Sigma Within</td><td>' + dataDraw[index].sigmaWithin.toFixed(3) + '</td></tr>'
                + '<tr><td>Sigma Overall</td><td>' + dataDraw[index].sigmaOverall.toFixed(3) + '</td></tr>'
                + '<tr><td></td><td>Potential(Within) Capability</td></tr>'
                + '<tr><td>Cp</td><td>' + (dataDraw[index].cp != null ? dataDraw[index].cp.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Cpl</td><td>' + (dataDraw[index].cpl != null ? dataDraw[index].cpl.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Cpu</td><td>' + (dataDraw[index].cpu != null ? dataDraw[index].cpu.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Cpk</td><td>' + dataDraw[index].cpk.toFixed(3) + '</td></tr>'
                + '<tr><td></td><td>Overall Capability</td></tr>'
                + '<tr><td>Pp</td><td>' + (dataDraw[index].pp != null ? dataDraw[index].pp.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Ppl</td><td>' + (dataDraw[index].ppl != null ? dataDraw[index].ppl.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Ppu</td><td>' + (dataDraw[index].ppu != null ? dataDraw[index].ppu.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Ppk</td><td>' + dataDraw[index].ppk.toFixed(3) + '</td></tr>'
                + '<tr><td>Cpm</td><td>' + (dataDraw[index].cpm != null ? dataDraw[index].cpm.toFixed(3) : '') + '</td></tr>';
            $('#tblInforTabSetUp>tbody').html(html);
            $('#tblInforTabSetUp>tfoot').html('');

            var width = $('#chart-detail-tab-set-up')[0].offsetWidth;
            $('#chart-detail-tab-set-up').css('height', width + 'px');
            var chart = Highcharts.chart('chart-detail-tab-set-up', {
                chart: {
                    zoomType: 'x',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },

                title: {
                    text: 'Process Capability of ' + dataDraw[index].parameter
                },

                tooltip: {
                    valueDecimals: 4
                },
                xAxis: [{
                    title: { text: '' },
                    alignTicks: false,
                    opposite: true,
                    tickInterval: 1,
                    tickPositions: [dataDraw[index].sampleSize > 0 ? dataDraw[index].sampleSize - 1 : 0]
                }, {
                    alignTicks: false,
                    softMax: dataDraw[index].usl,
                    softMin: dataDraw[index].lsl,
                    plotLines: [{
                        value: dataDraw[index].usl,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                        label: {
                            text: 'USL',
                            style: {
                                color: '#666666'
                            }
                        }
                    }, {
                        value: dataDraw[index].lsl,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                        label: {
                            text: 'LSL',
                            style: {
                                color: '#666666'
                            }
                        }
                    }]
                }],

                yAxis: [{
                    title: { text: '' },
                    labels: {
                        enabled: true
                    },
                    alignTicks: false,
                    softMax: dataDraw[index].usl,
                    softMin: dataDraw[index].lsl
                }, {
                    title: { text: '' },
                    opposite: true,
                    labels: {
                        enabled: false
                    }
                }, {
                    title: { text: '' },
                    opposite: true,
                    labels: {
                        enabled: false
                    }
                }],

                series: [dataDraw[index].sampleSize > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Quantity',
                    type: 'histogram',
                    xAxis: 1,
                    yAxis: 2,
                    baseSeries: 's1',
                    zIndex: -2,
                    tooltip: {
                        valueDecimals: 0
                    },
                    showInLegend: false,
                    color: "#B3B3B3"
                } : {}, dataDraw[index].sampleSize > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Overall',
                    type: 'bellcurve',// dduong cong hinh chuong
                    dashStyle: 'longdash',
                    xAxis: 1,
                    yAxis: 1,
                    baseSeries: 's1',
                    zIndex: -1,
                    fillOpacity: 0,
                    color: "#323933"
                } : {}, dataDraw[index].sampleSize > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Within',
                    type: 'areaspline',// duong rich rac khu vuc 
                    xAxis: 1,
                    yAxis: 1,
                    data: dataDraw[index].pointCpkList,
                    zIndex: -1,
                    fillOpacity: 0,
                    color: "#FA0000"
                } : {}, dataDraw[index].sampleSize > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Data',
                    type: 'line',// cot
                    id: 's1',
                    data: dataDraw[index].values,
                    visible: false,
                    showInLegend: false

                } : {}],

                legend: {
                    style: {
                        fontSize: '11px'
                    },
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom'
                },
                credits: {
                    enabled: false
                },
                navigation: {
                    buttonOptions: {
                        enabled: false
                    }
                },
            });
            dataset.index = index;
        }
    }

    function back() {
        $(".detail-item").css("display", "block");
        $(".detail-cpk").css("display", "none");
        $("#export-excel").css("display", "none");
        $("#btn-next").css("display", "block");
        $("#btn-back").attr("disabled", true);
        $("#btn-next").attr("disabled", false);
        $("#btn-caculate").attr("disabled", true);

    }

    function next() {
        $("#btn-next").attr("disabled", true);
        $("#btn-back").attr("disabled", false);
        $(".detail-item").css("display", "none");
        $(".detail-cpk").css("display", "block");
        $("#export-excel").css("display", "block");
        $("#btn-back").css("display", "block");
        $("#btn-caculate").attr("disabled", false);
    }
    $("#txt-search-table").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#tbl-item tbody tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    //Tab Home
    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
                $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
                dataset.timeSpan = $('input[name="timeSpan"]').val();

                if (dataset.factory.toUpperCase() == 'NBB') {
                    $('div[name=customer]').css('display', 'block');
                    $('div[name=modelName]').css('display', 'none');

                    if (dataset.customer != '') {
                        loadCpkCustomerList(dataset, true, function (dataset) {
                            loadCpkGroupList(dataset, true, function (dataset) {
                                loadCpkStationList(dataset, true, function (dataset) {
                                    loadCpkParameterList(dataset, true, onLoadNone);
                                }, true);
                            });
                        });
                    } else {
                        loadCpkCustomerList(dataset, false, function (dataset) {
                            loadCpkGroupList(dataset, false, function (dataset) {
                                loadCpkStationList(dataset, false, function (dataset) {
                                    loadCpkParameterList(dataset, false, onLoadNone);
                                }, true);
                            });
                        });
                    }

                    $('input[name=timeSpan]').on('change', function () {
                        dataset.timeSpan = this.value;
                        loadCpkCustomerList(dataset, false, function (dataset) {
                            loadCpkGroupList(dataset, false, function (dataset) {
                                loadCpkStationList(dataset, false, function (dataset) {
                                    loadCpkParameterList(dataset, false, function (dataset) { });
                                }, true);
                            });
                        });
                    });

                    $('select[name=customer]').on('change', function () {
                        dataset.customer = this.value;
                        loadCpkGroupList(dataset, false, function (dataset) {
                            loadCpkStationList(dataset, false, function (dataset) {
                                loadCpkParameterList(dataset, false, onLoadNone);
                            }, true);
                        });
                    });
                } else {
                    $('div[name=customer]').css('display', 'none');
                    $('div[name=modelName]').css('display', 'block');

                    if (dataset.modelName != '') {
                        loadCpkModelList(dataset, true, function (dataset) {
                            loadCpkGroupList(dataset, true, function (dataset) {
                                loadCpkStationList(dataset, true, function (dataset) {
                                    loadCpkParameterList(dataset, true, onLoadNone);
                                }, true);
                            });
                        });
                    } else {
                        loadCpkModelList(dataset, false, function (dataset) {
                            loadCpkGroupList(dataset, false, function (dataset) {
                                loadCpkStationList(dataset, false, function (dataset) {
                                    loadCpkParameterList(dataset, false, onLoadNone);
                                }, true);
                            });
                        });
                    }

                    $('input[name=timeSpan]').on('change', function () {
                        dataset.timeSpan = this.value;
                        loadCpkModelList(dataset, false, function (dataset) {
                            loadCpkGroupList(dataset, false, function (dataset) {
                                loadCpkStationList(dataset, false, function (dataset) {
                                    loadCpkParameterList(dataset, false, function (dataset) { });
                                }, true);
                            });
                        });
                    });

                    $('select[name=modelName]').on('change', function () {
                        dataset.modelName = this.value;
                        loadCpkGroupList(dataset, false, function (dataset) {
                            loadCpkStationList(dataset, false, function (dataset) {
                                loadCpkParameterList(dataset, false, onLoadNone);
                            }, true);
                        });
                    });
                }

                $('select[name=groupName]').on('change', function () {
                    dataset.groupName = this.value;
                    loadCpkStationList(dataset, false, function (dataset) {
                        loadCpkParameterList(dataset, false, onLoadNone);
                    }, true);
                });

                $('select[name=stationName]').on('change', function () {
                    dataset.stationName = this.value;
                    onLoadNone(dataset);
                });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function onLoadNone() {
        $('#tblDetail').addClass('hidden');
        $('#tblInfor').addClass('hidden');
        $('#chart-detail').addClass('hidden');
    }

    $(document).ready(function () {
        getTimeNow();
    });

    var dataDraw = [];
    function loadData() {
        dataDraw = [];
        $('#tblDetail>tbody').html('');

        var parameters = $("select[name='parameter']").val();
        var requestData = {
            factory: dataset.factory,
            customer: dataset.customer,
            modelName: dataset.modelName,
            groupName: dataset.groupName,
            stationName: dataset.stationName,
            parameters: parameters,
            onlyPass: cbxOnlyPass.checked,
            timeSpan: dataset.timeSpan
        }

        $('.loader').removeClass('hidden');
        $.ajax({
            type: "POST",
            url: "/api/test/station/cpk/all",
            data: JSON.stringify(requestData),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                $('#tblDetail').removeClass('hidden');
                $('#tblDetail>tbody').html('');
                dataset.index = -1;
                if (response != null) {
                    var data = response.data;
                    dataDraw = response.data;
                    var html = '';
                    for (i in data) {
                        var classRate = '';
                        if (data[i].cpk < 1.33) {
                            classRate = 'rate-danger';
                        } else {
                            classRate = 'rate-success';
                        }
                        html += '<tr onclick="loadDetail(' + i + ')"><td>' + data[i].parameter + '</td>'
                            + '<td>' + data[i].min.toFixed(3) + '</td>'
                            + '<td>' + data[i].average.toFixed(3) + '</td>'
                            + '<td>' + data[i].max.toFixed(3) + '</td>'
                            + '<td>' + (data[i].lsl != null ? data[i].lsl.toFixed(3) : '') + '</td>'
                            + '<td>' + (data[i].usl != null ? data[i].usl.toFixed(3) : '') + '</td>'
                            + '<td>' + data[i].sigmaWithin.toFixed(3) + '</td>'
                            + '<td class="' + classRate + '">' + data[i].cpk.toFixed(3) + '</td></tr>';
                    }
                    $('#tblDetail>tbody').html(html);
                }
                $('.loader').addClass('hidden');
            },
            error: function () {
                alert('FAIL!');
                $('.loader').addClass('hidden');
            },
            complete: function () {
                $('.loader').addClass('hidden');
            }
        });

    }

    function loadDetail(index) {
        $('#tblInfor').removeClass('hidden');
        $('#chart-detail').removeClass('hidden');
        if (dataset.index != index) {
            $('#tblInfor>tbody').html('');
            var html = '';
            html += '<tr><td></td><td>Process Data</td></tr>'
                + '<tr><td>Sample Size</td><td>' + dataDraw[index].sampleSize + '</td></tr>'
                + '<tr><td>Avg</td><td>' + dataDraw[index].average.toFixed(3) + '</td></tr>'
                + '<tr><td>Sigma Within</td><td>' + dataDraw[index].sigmaWithin.toFixed(3) + '</td></tr>'
                + '<tr><td>Sigma Overall</td><td>' + dataDraw[index].sigmaOverall.toFixed(3) + '</td></tr>'
                + '<tr><td></td><td>Potential(Within) Capability</td></tr>'
                + '<tr><td>Cp</td><td>' + (dataDraw[index].cp != null ? dataDraw[index].cp.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Cpl</td><td>' + (dataDraw[index].cpl != null ? dataDraw[index].cpl.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Cpu</td><td>' + (dataDraw[index].cpu != null ? dataDraw[index].cpu.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Cpk</td><td>' + dataDraw[index].cpk.toFixed(3) + '</td></tr>'
                + '<tr><td></td><td>Overall Capability</td></tr>'
                + '<tr><td>Pp</td><td>' + (dataDraw[index].pp != null ? dataDraw[index].pp.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Ppl</td><td>' + (dataDraw[index].ppl != null ? dataDraw[index].ppl.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Ppu</td><td>' + (dataDraw[index].ppu != null ? dataDraw[index].ppu.toFixed(3) : '') + '</td></tr>'
                + '<tr><td>Ppk</td><td>' + dataDraw[index].ppk.toFixed(3) + '</td></tr>'
                + '<tr><td>Cpm</td><td>' + (dataDraw[index].cpm != null ? dataDraw[index].cpm.toFixed(3) : '') + '</td></tr>';
            $('#tblInfor>tbody').html(html);
            $('#tblInfor>tfoot').html('');
            //var tfoot = '<tr><td></td><td style="padding-top:10px;"><a class="btn btn-default btn-sm" href="/api/test/station/cpk/all/export?'+dataset.dataParam+'" style="padding: 3px; font-weight: bold;"><i class="fa fa-download"></i> Export</a></td></tr>'
            //$('#tblInfor>tfoot').html(tfoot);

            var width = $('#chart-detail')[0].offsetWidth;
            $('#chart-detail').css('height', width + 'px');
            var chart = Highcharts.chart('chart-detail', {
                chart: {
                    zoomType: 'x',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },

                title: {
                    text: 'Process Capability of ' + dataDraw[index].parameter
                },

                tooltip: {
                    valueDecimals: 4
                },

                xAxis: [{
                    title: { text: '' },
                    alignTicks: false,
                    opposite: true,
                    tickInterval: 1,
                    tickPositions: [dataDraw[index].sampleSize > 0 ? dataDraw[index].sampleSize - 1 : 0]
                }, {
                    //    title: { text: '' },
                    //    alignTicks: false,
                    //    visible: true,
                    //    softMax: dataDraw[index].usl,
                    //    softMin: dataDraw[index].lsl,
                    //}, {
                    //title: { text: 'Process Capability of ' + dataDraw[index].parameter },
                    alignTicks: false,
                    softMax: dataDraw[index].usl,
                    softMin: dataDraw[index].lsl,
                    //tickPositions: [dataDraw[index].lsl, dataDraw[index].usl],
                    plotLines: [{
                        value: dataDraw[index].usl,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                        label: {
                            text: 'USL',
                            style: {
                                color: '#666666'
                            }
                        }
                    }, {
                        value: dataDraw[index].lsl,
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                        label: {
                            text: 'LSL',
                            style: {
                                color: '#666666'
                            }
                        }
                    }]
                }],

                yAxis: [{
                    title: { text: '' },
                    labels: {
                        enabled: true
                    },
                    alignTicks: false,
                    softMax: dataDraw[index].usl,
                    softMin: dataDraw[index].lsl
                }, {
                    title: { text: '' },
                    opposite: true,
                    labels: {
                        enabled: false
                    }
                }, {
                    title: { text: '' },
                    opposite: true,
                    labels: {
                        enabled: false
                    }
                }],

                series: [dataDraw[index].sampleSize > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Quantity',
                    type: 'histogram',
                    xAxis: 1,
                    yAxis: 2,
                    baseSeries: 's1',
                    zIndex: -2,
                    tooltip: {
                        valueDecimals: 0
                    },
                    showInLegend: false,
                    color: "#B3B3B3"
                } : {}, dataDraw[index].sampleSize > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Overall',
                    type: 'bellcurve',
                    dashStyle: 'longdash',
                    xAxis: 1,
                    yAxis: 1,
                    baseSeries: 's1',
                    zIndex: -1,
                    fillOpacity: 0,
                    color: "#323933"
                } : {}, dataDraw[index].sampleSize > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Within',
                    type: 'areaspline',
                    xAxis: 1,
                    yAxis: 1,
                    data: dataDraw[index].pointCpkList,
                    zIndex: -1,
                    fillOpacity: 0,
                    color: "#FA0000"
                } : {}, dataDraw[index].sampleSize > 0 ? {
                    label: {
                        enabled: false
                    },
                    name: 'Data',
                    type: 'line',
                    id: 's1',
                    data: dataDraw[index].values,
                    visible: false,
                    showInLegend: false,

                } : {}],

                legend: {
                    style: {
                        fontSize: '11px'
                    },
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom'
                },
                credits: {
                    enabled: false
                },
                navigation: {
                    buttonOptions: {
                        enabled: false
                    }
                },
            });
            dataset.index = index;
        }
    }

    function exportData() {
        var parameters = $("select[name='parameter']").val();
        var requestData = {
            factory: dataset.factory,
            customer: dataset.customer,
            modelName: dataset.modelName,
            groupName: dataset.groupName,
            stationName: dataset.stationName,
            parameters: parameters,
            onlyPass: cbxOnlyPass.checked,
            timeSpan: dataset.timeSpan
        }

        var request = new XMLHttpRequest();
        request.open("POST", "/api/test/station/cpk/all/export", true);
        request.setRequestHeader('Content-Type', "application/json; charset=utf-8");
        request.responseType = 'blob';

        request.onload = function (e) {
            if (this.status === 200) {
                var filename = "";
                var disposition = this.getResponseHeader('Content-Disposition');
                if (disposition && disposition.indexOf('attachment') !== -1) {
                    var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    var matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
                }

                var blob = this.response;
                if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(blob, filename);
                } else {
                    var URL = window.URL || window.webkitURL;
                    var downloadUrl = URL.createObjectURL(blob);
                    if (filename) {
                        var a = document.createElement('a');
                        if (typeof a.download === 'undefined') {
                            window.location.href = downloadUrl;
                        } else {
                            a.href = downloadUrl;
                            a.download = filename;
                            document.body.appendChild(a);
                            a.click();
                        }
                    } else {
                        window.location.href = downloadUrl;
                    }

                    setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100);
                }
            }
        }

        request.send(JSON.stringify(requestData));
    }

    function loadCpkCustomerList(dataset, flag, onLoad) {
        $.ajax({
            type: 'GET',
            url: '/api/test/station/cpk/customer',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            success: function (response) {
                var data = response.data;

                var selector = $("select[name='customer']");
                selector.children('option').remove();

                var options = "";
                for (i in data) {
                    options += '<option value="' + data[i] + '">' + data[i] + '</option>';
                }
                selector.append(options);

                if (data.length > 0) {
                    if (flag == undefined || flag == false) {
                        dataset.customer = data[0];
                    }
                    selector.val(dataset.customer);
                    onLoad(dataset, flag);
                } else {
                    dataset.customer = "";
                    onLoad(dataset, flag);
                }

                selector.selectpicker('refresh');
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
    }

    function loadCpkModelList(dataset, flag, onLoad) {
        $.ajax({
            type: 'GET',
            url: '/api/test/station/cpk/model',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            success: function (response) {
                var data = response.data;

                var selector = $("select[name='modelName']");
                selector.children('option').remove();

                var options = "";
                for (i in data) {
                    options += '<option value="' + data[i] + '">' + data[i] + '</option>';
                }
                selector.append(options);

                if (data.length > 0) {
                    if (flag == undefined || flag == false) {
                        dataset.modelName = data[0];
                    }
                    selector.val(dataset);
                    onLoad(dataset, flag);
                } else {
                    dataset.modelName = "";
                    onLoad(dataset, flag);
                }

                selector.selectpicker('refresh');
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
    }

    function loadCpkGroupList(dataset, flag, onLoad) {
        $.ajax({
            type: 'GET',
            url: '/api/test/station/cpk/group',
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                modelName: dataset.modelName,
                timeSpan: dataset.timeSpan
            },
            success: function (response) {
                var data = response.data;

                var selector = $("select[name='groupName']");
                selector.children('option').remove();

                var options = "";
                for (i in data) {
                    options += '<option value="' + data[i] + '">' + data[i] + '</option>';
                }
                selector.append(options);

                if (data.length > 0) {
                    if (flag == undefined || flag == false) {
                        dataset.groupName = data[0];
                    }
                    selector.val(dataset.groupName);
                    onLoad(dataset, flag);
                } else {
                    dataset.groupName = "";
                    onLoad(dataset, flag);
                }

                selector.selectpicker('refresh');
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
    }

    function loadCpkStationList(dataset, flag, onLoad, all) {
        $.ajax({
            type: 'GET',
            url: '/api/test/station/cpk/station',
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                modelName: dataset.modelName,
                groupName: dataset.groupName,
                timeSpan: dataset.timeSpan
            },
            success: function (response) {
                var data = response.data;

                var selector = $("select[name='stationName']");
                selector.children('option').remove();

                var options = "";
                if (all != undefined && all == true) {
                    options += '<option value="">ALL</option>';
                }
                for (i in data) {
                    options += '<option value="' + data[i] + '">' + data[i] + '</option>';
                }
                selector.append(options);

                if (data.length > 0) {
                    if (flag == undefined || flag == false) {
                        if (all != undefined && all == true) {
                            dataset.stationName = "";
                        } else {
                            dataset.stationName = data[0];
                        }
                    }
                    selector.val(dataset.stationName);
                    onLoad(dataset, flag);
                } else {
                    dataset.stationName = "";
                    onLoad(dataset, flag);
                }

                selector.selectpicker('refresh');
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
    }

    function loadCpkParameterList(dataset, flag, onLoad) {
        $.ajax({
            type: 'GET',
            url: '/api/test/station/cpk/parameter',
            data: {
                factory: dataset.factory,
                customer: dataset.customer,
                modelName: dataset.modelName,
                groupName: dataset.groupName
            },
            success: function (response) {
                var data = response.data;

                var selector = $("select[name='parameter']");
                selector.children('option').remove();

                var options = "";
                for (i in data) {
                    options += '<option value="' + data[i] + '">' + data[i] + '</option>';
                }

                if (data.length > 0) {
                    dataset.parameter = data[0];
                    selector.val(dataset.parameter);
                } else {
                    dataset.parameter = "";
                }

                selector.append(options);
                selector.selectpicker('refresh');

                onLoad(dataset, flag);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
    }

    function exportDataTabSetUp() {
        var testItemList = loadDataViewRowCpk();
        var requestData = {
            "tmpFile": tmpFile,
            "testItemList": testItemList
        }

        var request = new XMLHttpRequest();
        request.open("POST", "/api/test/station/cpk/from-file/export", true);
        request.setRequestHeader('Content-Type', "application/json; charset=utf-8");
        request.responseType = 'blob';

        request.onload = function (e) {
            if (this.status === 200) {
                var filename = "";
                var disposition = this.getResponseHeader('Content-Disposition');
                if (disposition && disposition.indexOf('attachment') !== -1) {
                    var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    var matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
                }

                var blob = this.response;
                if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(blob, filename);
                } else {
                    var URL = window.URL || window.webkitURL;
                    var downloadUrl = URL.createObjectURL(blob);
                    if (filename) {
                        var a = document.createElement('a');
                        if (typeof a.download === 'undefined') {
                            window.location.href = downloadUrl;
                        } else {
                            a.href = downloadUrl;
                            a.download = filename;
                            document.body.appendChild(a);
                            a.click();
                        }
                    } else {
                        window.location.href = downloadUrl;
                    }

                    setTimeout(function () { URL.revokeObjectURL(downloadUrl); }, 100);
                }
            }
        }

        request.send(JSON.stringify(requestData));
    }
</script>
<!-- /CPK -->