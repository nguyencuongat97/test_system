<%@ page contentType="text/html;charset=UTF-8" language="java"%>


<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/bonepile.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header" style="font-size: 18px;">
            <b><span>JBD RE INPUT & OUTPUT REPORT</span></b>
        </div>

        <div class="row" style="margin:unset">
            <div class=" panel panel-overview input-group"
                style="width:100%; margin-bottom: 5px; background: #333;float: left;">
                <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                        class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                    style="height: 30px; color: #fff;">
            </div>
            <!-- <div class=" panel chooseModel"
                style="width: 26%;margin: 0 1% 5px;float: left;background: #333; height: 30px;">
                <select class=" form-control selectpicker chooseModelExport" id="slModelName" data-live-search="true"
                    style=" color: #fff;padding: 5px;border-bottom: none;"> -->
            <!-- <option value="" disabled="" selected="">Choose Model Export</option> -->
            <!-- </select>
            </div> -->
            <!-- <div class=" wrapperBtnExport"> -->
            <!-- <a class=" btn btn-lg" id="btnExport" onclick="exportDataDetailModelname()"
                style="height:29px;width:7%;font-size: 13px; padding: 7px; border-radius: 5px; color: #fff;background: #444;">
                <i class="fa fa-download"></i> EXPORT
            </a> -->
            <!-- <a class=" btn btn-lg" id="btnFillterRC" onclick="showReasonCode()"
            style="text-transform: capitalize;height:29px;width:7%;font-size: 13px; padding: 7px; border-radius: 5px; color: #fff;background: #444;">
                <i class="fa fa-search"></i> Fillter
            </a> -->
            <!-- </div> -->
        </div>

        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-6 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="input"></div>
            </div>
            <div class="col-xs-12 col-sm-6 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="output"></div>
            </div>
            <div class="col-xs-12 col-sm-6 infchart">
                <!-- <div class="panel panel-flat panel-body chart-sm" id="detailInput"></div> -->
                <table class="col-xs-12 col-sm-12 infchart" id="trellis">
                    <!-- <th>
                         <h1>Error code</h1>
                    </th> -->
                    <tr>
                        <td class="first panel panel-body no-padding"></td>
                        <td class="panel panel-body no-padding"></td>
                    </tr>
                </table>
            </div>
            <div class="col-xs-12 col-sm-6 infchart">
                <!-- <div class="panel panel-flat panel-body chart-sm" id="detailOutput"></div> -->
                <table class="col-xs-12 col-sm-12 infchart" id="trellisOut">
                    <tr>
                        <td class="first panel panel-body no-padding"></td>
                        <td class="panel panel-body no-padding"></td>
                    </tr>
                </table>
            </div>
            <div class="col-xs-12 col-sm-6 infchart" style="margin-top: 10px;">
                <table id="tblErrorCode" class="table table-bordered table-xxs table-sticky"
                    style="background: #333; color: #ccc; text-align: center;">
                    <thead>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <div class="col-xs-12 col-sm-6 infchart" style="margin-top: 10px;">
                <table id="tblReasonCode" class="table table-bordered table-xxs table-sticky"
                    style="background: #333; color: #ccc; text-align: center;">
                    <thead>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
            <!--    <div class="col-xs-12 col-sm-4 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="trending"></div>
            </div>
            <div class="col-xs-12 col-sm-12 infchart">
                <div class="panel panel-flat panel-body chart-sm" id="in_out_trend"></div>
            </div> -->
        </div>
        <!-- <div class="row" style="margin: unset;margin-top: 10px;">
        </div> -->
    </div>
</div>
<style>
    #trellis td,
    #trellisOut td {
        width: 45% !important;
        background: #333;
    }

    #trellis td.first,
    #trellisOut td.first {
        width: 55% !important;
        background: #333;
    }

    .chooseModelExport .btn {
        color: #fff;
        padding: 5px;
        border-bottom: none;
    }

    .chooseModelExport .btn .bs-caret .caret {
        margin-right: 5px;
    }

    .progress {
        background-color: #333 !important;
        border-radius: 0px !important;
    }

    .progress-bar {
        background-color: #2b908f;
        box-shadow: 1px 1px #989090;
    }

    #tblErrorCode thead tr,
    #tblReasonCode thead tr {
        /* background-color: #f79646; */
        color: #fdfafa;
        text-align: center;
        font-weight: bold;
    }

    .table-sticky thead th {
        box-shadow: inset 0 1px 0 #fdfafa, inset 0 -1px 0 #fdfafa;
        background-color: #4b5d5d !important;
        color: #fdfafa;
    }

    #tblErrorCode tbody tr td,
    #tblReasonCode tbody tr td {
        padding: 5px 5px;
    }

    .table-bordered tr>td:last-child {
        border: none;
        border-left: 1px solid #ccc;
    }

    .table-bordered tr>td:nth-of-type(4) {
        border: none;
    }
</style>
<script>
    var dataset = {
        factory: '${factory}',
        timeSpan: ''
    }
    var gloData;
    // var gloReason={};
    // var gloModel = {};
    // var gloTime = {timeStart : null, timeEnd : null};
    var checkin = false;
    var checkout = false;
    function getModel() {
        $.ajax({
            type: 'GET',
            url: '/api/test/model',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            success: function (data) {
                // console.log("model:",data);
                var arrayModel = [];
                var html = '<option value="" disabled="" selected>Select Model</option>';
                var selector = $('#slModelName');
                selector.children('option').remove();
                for (i in data) {
                    if (arrayModel.indexOf(data[i].modelName) == -1) {
                        arrayModel.push(data[i].modelName);
                    }
                }
                for (j in arrayModel) {
                    html += '<option class="classvalueMN" value="' + arrayModel[j] + '">' + arrayModel[j] + '</option>';
                    // gloModel.modelName = arrayModel[j];
                }
                // console.log("getModel:", gloModel.modelName);
                $('#slModelName').html(html);
                selector.selectpicker('refresh');
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    // $('#slModelName').on('change', function () {
    //     gloModel.modelName = this.value;
    //     // showReasonCode(gloModel.modelName);
    // });
    // function showReasonCode(){
    // var valueSearch = gloModel.modelName;
    // getErrorCode(valueSearch);
    // getReasonCode(valueSearch);
    // }
    function getErrorCode(modelName) {
        // $('#tblErrorCode>thead').html('');
        // $('#tblErrorCode>tbody').html('');
        $.ajax({
            type: "GET",
            url: "/api/re/online/get_error_code_repair",
            data: {
                factory: dataset.factory,
                modelName: modelName,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var valueTime = dataset.timeSpan;
                // var subTime = valueTime.slice(0, 10);
                var subTime = data.time[0];
                // var subTime2 = valueTime.slice(19, 29);
                var subTime2 = data.time[1];
                var htmlthead = "<tr>"
                    + "<th>Model Name</th><th>Test Group</th><th>Test Code</th>"
                    + "<th style='z-index:1'>" + data.time[0] + "</th><th style='z-index:1'>" + data.time[1] + "</th></tr>";
                $('#tblErrorCode thead').html(htmlthead);
                if (!$.isEmptyObject(data.errorCode)) {
                    var htmltbody = '';
                    htmltbody += '<tr>';
                    for (var key in data.errorCode) {
                        if (data.errorCode.hasOwnProperty(key)) {
                            var element = data.errorCode[key];
                            for (var k in element) {
                                htmltbody += '<td class="classModel">' + modelName + '</td><td class="classGroup_' + key.replace(/\s|\.|\(|\)/g, '_') + '">' + key.replace(/\s|\.|\(|\)/g, '_') + '</td>';
                                if (element.hasOwnProperty(k)) {
                                    var el = element[k];
                                    htmltbody += '<td>' + k + '</td>'
                                        + '<td class="">'
                                        + '<div class="progress">'
                                        + '<div class="progress-bar col_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_01" role="progressbar" style="">'
                                        + '</div>'
                                        + '</div>'
                                        + '</td>'
                                        + '<td class="">'
                                        + '<div class="progress">'
                                        + '<div class="progress-bar col_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_02" role="progressbar" style="">'
                                        + '</div>'
                                        + '</div>'
                                        + '</td>';
                                }
                                htmltbody += '</tr>';
                            }
                        }
                    }
                    $('#tblErrorCode tbody').html(htmltbody);
                    var arrMax1 = [], arrMax2 = [];
                    var valueMax1 = 0, valueMax2 = 0;
                    for (var key in data.errorCode) {
                        if (data.errorCode.hasOwnProperty(key)) {
                            var element = data.errorCode[key];
                            for (var k in element) {
                                if (element.hasOwnProperty(k)) {
                                    var el = element[k];
                                    for (l in el) {
                                        if (el[l].TIMER == subTime) {
                                            // arrMax1 = el[l].QTY;
                                            let objPush = {
                                                key: el[l].TEST_GROUP,
                                                k: el[l].TEST_CODE,
                                                qty: el[l].QTY
                                            }
                                            arrMax1.push(objPush);
                                            valueMax1 = valueMax1 < el[l].QTY ? el[l].QTY : valueMax1;
                                            $('.col_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_01').html(el[l].QTY);
                                            $('.col_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_01').attr('title', 'QTY: ' + el[l].QTY);
                                        } else if (el[l].TIMER == subTime2) {
                                            var objPush2 = {
                                                key: el[l].TEST_GROUP,
                                                k: el[l].TEST_CODE,
                                                qty: el[l].QTY
                                            }
                                            arrMax2.push(objPush2);
                                            valueMax2 = valueMax2 < el[l].QTY ? el[l].QTY : valueMax2;

                                            valueMax2 = valueMax2 < el[l].QTY ? el[l].QTY : valueMax2;

                                            $('.col_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_02').attr('title', 'QTY: ' + el[l].QTY);
                                            $('.col_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_02').html(el[l].QTY);
                                        }
                                    }
                                }

                            }
                        }
                    }
                    for (var index = 0; index < arrMax1.length; index++) {
                        var element = arrMax1[index];
                        var getValue1 = ((element.qty / valueMax1) * 100);
                        if (getValue1 < 20) {
                            // $('.col_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_01').html('');
                            $('.col_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_01').css('width', getValue1 + '%');
                        } else {
                            $('.col_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_01').css('width', getValue1 + '%');
                        }
                    }
                    for (var index = 0; index < arrMax2.length; index++) {
                        var element = arrMax2[index];
                        var getValue2 = ((element.qty / valueMax2) * 100);
                        // var getValue2 = element.qty;
                        if (getValue2 < 20) {
                            // $('.col_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_02').html('');
                            $('.col_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_02').css('width', getValue2 + '%');
                        } else {
                            $('.col_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_02').css('width', getValue2 + '%');
                        }
                    }
                    var classModel = $('.classModel');
                    if (classModel.length > 1) {
                        classModel[0].rowSpan = classModel.length;

                        for (let i = 1; i < classModel.length; i++) {
                            classModel[i].className += ' hidden';
                        }
                    }
                    for (var i in data.errorCode) {
                        if (data.errorCode.hasOwnProperty(i)) {
                            var element = data.errorCode[i];

                            for (var k in element) {
                                var classGroup = i;
                                var getClassGroup = $('.classGroup_' + classGroup);
                                if (getClassGroup.length > 1) {
                                    getClassGroup[0].rowSpan = getClassGroup.length;
                                    for (var j = 1; j < getClassGroup.length; j++) {
                                        getClassGroup[j].className += ' hidden';
                                    }
                                }
                            }
                        }
                    }
                } else {
                    $('#tblErrorCode tbody').html('<tr><td colspan="5" align="center">-- NO DATA --</td></tr>');

                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    //reasoncode
    function getReasonCode(modelName) {
        // $('#tblReasonCode>thead').html('');
        // $('#tblReasonCode>tbody').html('');
        $.ajax({
            type: "GET",
            url: "/api/re/online/get_error_code_repair",
            data: {
                factory: dataset.factory,
                modelName: modelName,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var valueTime = dataset.timeSpan;
                // var subTime = valueTime.slice(0, 10);
                var subTime = data.time[0];
                // var subTime2 = valueTime.slice(19, 29);
                var subTime2 = data.time[1];
                console.log(data.time)
                var htmlthead = "<tr>"
                    + "<th>Model Name</th><th>Test Group</th><th>Reason Code</th>"
                    + "<th style='z-index:1'>" + data.time[0] + "</th><th style='z-index:1'>" + data.time[1] + "</th></tr>";
                $('#tblReasonCode thead').html(htmlthead);
                if (!$.isEmptyObject(data.reasonCode)) {
                    var htmltbody = '';
                    htmltbody += '<tr>';
                    for (var key in data.reasonCode) {
                        if (data.reasonCode.hasOwnProperty(key)) {
                            var element = data.reasonCode[key];
                            for (var k in element) {
                                htmltbody += '<td class="classModelRC">' + modelName + '</td><td class="classGroupRC_' + key.replace(/\s|\.|\(|\)/g, '_') + '">' + key.replace(/\s|\.|\(|\)/g, '_') + '</td>';
                                if (element.hasOwnProperty(k)) {
                                    var el = element[k];
                                    htmltbody += '<td>' + k + '</td>'
                                        + '<td class="">'
                                        + '<div class="progress">'
                                        + '<div class="progress-bar colRC_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_01" role="progressbar">'
                                        + '</div>'
                                        + '</div>'
                                        + '</td>'
                                        + '<td class="">'
                                        + '<div class="progress">'
                                        + '<div class="progress-bar colRC_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_02" role="progressbar">'
                                        + '</div>'
                                        + '</div>'
                                        + '</td>';
                                }
                                htmltbody += '</tr>';
                            }
                        }
                    }
                    $('#tblReasonCode tbody').html(htmltbody);
                    var arrMax1 = [], arrMax2 = [];
                    var valueMax1 = 0, valueMax2 = 0;
                    for (var key in data.reasonCode) {
                        if (data.reasonCode.hasOwnProperty(key)) {
                            var element = data.reasonCode[key];
                            for (var k in element) {
                                if (element.hasOwnProperty(k)) {
                                    var el = element[k];
                                    for (l in el) {
                                        if (el[l].TIMER == subTime) {
                                            // arrMax1 = el[l].QTY;
                                            let objPush = {
                                                key: el[l].TEST_GROUP,
                                                k: el[l].REASON_CODE,
                                                qty: el[l].QTY
                                            }
                                            arrMax1.push(objPush);
                                            valueMax1 = valueMax1 < el[l].QTY ? el[l].QTY : valueMax1;
                                            $('.colRC_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_01').html(el[l].QTY);
                                            $('.colRC_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_01').attr('title', 'QTY: ' + el[l].QTY);
                                        } else if (el[l].TIMER == subTime2) {
                                            var objPush2 = {
                                                key: el[l].TEST_GROUP,
                                                k: el[l].REASON_CODE,
                                                qty: el[l].QTY
                                            }
                                            arrMax2.push(objPush2);
                                            valueMax2 = valueMax2 < el[l].QTY ? el[l].QTY : valueMax2;

                                            valueMax2 = valueMax2 < el[l].QTY ? el[l].QTY : valueMax2;

                                            $('.colRC_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_02').html(el[l].QTY);
                                            $('.colRC_' + key.replace(/\s|\.|\(|\)/g, '_') + '_' + k.replace(/\s|\.|\(|\)/g, '_') + '_02').attr('title', 'QTY: ' + el[l].QTY);
                                        }
                                    }
                                }

                            }
                        }
                    }
                    for (var index = 0; index < arrMax1.length; index++) {
                        var element = arrMax1[index];
                        var getValue1 = ((element.qty / valueMax1) * 100);
                        if (getValue1 < 20) {
                            // $('.colRC_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_01').html('');
                            $('.colRC_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_01').css('width', getValue1 + '%');
                        } else {
                            $('.colRC_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_01').css('width', getValue1 + '%');
                        }
                    }
                    for (var index = 0; index < arrMax2.length; index++) {
                        var element = arrMax2[index];
                        var getValue2 = ((element.qty / valueMax2) * 100);
                        if (getValue2 < 20) {
                            // $('.col_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_02').html('');
                            $('.colRC_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_02').css('width', getValue2 + '%');
                        } else {
                            $('.colRC_' + element.key.replace(/\s|\.|\(|\)/g, '_') + '_' + element.k.replace(/\s|\.|\(|\)/g, '_') + '_02').css('width', getValue2 + '%');
                        }
                    }
                    var classModel = $('.classModelRC');
                    if (classModel.length > 1) {
                        classModel[0].rowSpan = classModel.length;
                        for (let i = 1; i < classModel.length; i++) {
                            classModel[i].className += ' hidden';
                        }
                    }
                    for (let i in data.reasonCode) {
                        if (data.reasonCode.hasOwnProperty(i)) {
                            let element = data.reasonCode[i];
                            for (let k in element) {
                                var classGroup = i;
                                var getClassGroup = $('.classGroupRC_' + classGroup);
                                if (getClassGroup.length > 1) {
                                    getClassGroup[0].rowSpan = getClassGroup.length;
                                    for (let j = 1; j < getClassGroup.length; j++) {
                                        getClassGroup[j].className += ' hidden';
                                    }
                                }
                            }
                        }
                    }
                } else {
                    $('#tblReasonCode tbody').html('<tr><td colspan="5" align="center">-- NO DATA --</td></tr>');

                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    function loadWipInOutStatus() {
        $.ajax({
            type: "GET",
            url: "/api/re/online/wipInOutStatus",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                if (Object.keys(data).length > 0) {
                    gloData = data;
                    arrInput = [];
                    arrOutput = [];
                    // var timeOut = Object.keys(data.output[0]); //array
                    // gloTime.timeStart = timeOut[0];
                    // var timeOutLast = Object.keys(data.output[data.output.length-1]); //array
                    // gloTime.timeEnd = timeOutLast[0];
                    // console.log("time test: ",gloTime);
                    // setDataForInputTime(gloTime.timeStart, gloTime.timeEnd)

                    var strlast = data.input[data.input.length - 1];
                    var strlast2 = data.input[data.input.length - 2];
                    detailDailyInput("trellis", strlast2, strlast);
                    for (var index = 0; index < data.input.length; index++) {
                        var element = data.input[index];
                        var arr = Object.keys(element)
                        var a = arr[0];
                        arrInput.push({ name: arr[0], y: element[a] });
                    }
                    hightChartInOutStatus("input", "TOTAL CHECK IN", arrInput);
                    var strlastOut = data.output[data.output.length - 1];
                    var strlast2Out = data.output[data.output.length - 2];
                    detailDailyOutput("trellisOut", strlast2Out, strlastOut);
                    for (let index = 0; index < data.output.length; index++) {
                        var element = data.output[index];
                        var arr = Object.keys(element)
                        var a = arr[0];
                        arrOutput.push({ name: arr[0], y: element[a] });

                    }
                    hightChartInOutStatusOut("output", "TOTAL CHECK OUT", arrOutput);
                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }
    // function dateailModelName(key) {
    //     console.log("key::",key);
    //     var detailInput = [];
    //     if (Object.keys(gloData).length > 0) {
    //         if (checkin) {
    //             for (let index = 0; index < gloData.input.length; index++) {
    //                 const element = gloData.input[index];
    //                 var arr = Object.keys(element);
    //                 if (arr[0] == key) {
    //                     for (const k in element.data) {
    //                         if (element.data.hasOwnProperty(k)) {
    //                             const e = element.data[k];
    //                             detailInput.push({ name: k, y: e });
    //                         }
    //                     }
    //                 }
    //             }
    //             hightChartInOutStatus1("detailInput", "Detail Check In " + key, detailInput, strlast, strlast2);
    //         }
    //         if (checkout) {
    //             for (let index = 0; index < gloData.output.length; index++) {
    //                 const element = gloData.output[index];
    //                 var arr = Object.keys(element);
    //                 if (arr[0] == key) {
    //                     for (const k in element.data) {
    //                         if (element.data.hasOwnProperty(k)) {
    //                             const e = element.data[k];
    //                             detailInput.push({ name: k, y: e });
    //                         }
    //                     }
    //                 }
    //             }
    //             hightChartInOutStatus2("detailOutput", "Detail Check OUT " + key, detailInput);
    //         }
    //     }
    // }
    // FUNCTION
    function detailDailyInput(idChart, strlast, strlast2) {
        var nameChart = Object.keys(strlast)[0];
        var nameChart2 = Object.keys(strlast2)[0];
        var model1 = Object.keys(strlast.data);
        var model2 = Object.keys(strlast2.data);
        if (model1.length > 0) {
            //    model1.forEach(e => {
            //        if(!model2.includes(e)){
            //             model2.push(e)
            //        }
            //    });
            for (var index = 0; index < model1.length; index++) {
                var element = model1[index];
                if (model2.indexOf(element) == -1) {
                    model2.push(element);
                }
            }
        }
        //   var df = model2[0];
        //   console.log("model20:::",model2[0]);

        var dtChart = [];
        var dtChart2 = [];
        for (var index = 0; index < model2.length; index++) {
            var el = model2[index];
            var df = el;
            var tmp;
            if (strlast.data[df] == undefined) {
                tmp = 0;
                dtChart.push(tmp);
            } else {
                tmp = strlast.data[df];
                dtChart.push(tmp);
            }
            if (strlast2.data[df] == undefined) {
                dtChart2.push(0);
            } else {
                let tmp2 = strlast2.data[df];
                dtChart2.push(tmp2);
            }
        }

        var charts = [],
            $containers = $('#trellis td'),
            datasets = [{
                name: nameChart,
                data: dtChart
            },
            {
                name: nameChart2,
                data: dtChart2
            }
            ];
        $.each(datasets, function (i, dataset) {
            charts.push(new Highcharts.Chart({
                chart: {
                    renderTo: $containers[i],
                    type: 'bar',
                    // marginLeft: i === 0 ? 100 : 10
                },
                title: {
                    text: dataset.name,
                    align: 'center',
                    style: {
                        fontSize: '16px',
                        fontWeight: 'bold',
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    },
                    // x: i === 0 ? 90 : 0
                },

                credits: {
                    enabled: false
                },

                xAxis: {
                    categories: model2,
                    labels: {
                        enabled: i === 0
                    }
                },
                plotOptions: {
                    series: {
                        borderWidth: false,
                        dataLabels: {
                            enabled: true,
                            // color: 'blue'
                        },
                        point: {
                            events: {
                                click: function () {
                                    var model_name = this.category.name;
                                    $('#tblErrorCode').removeClass('hidden');
                                    getErrorCode(model_name);
                                }
                            }
                        }
                    }
                },
                yAxis: {
                    allowDecimals: false,
                    title: {
                        text: ''
                    },
                    min: 0,
                    max: 100
                },
                navigation: {
                    buttonOptions: {
                        enabled: false
                    }
                },
                credits: {
                    enabled: false
                },
                legend: {
                    enabled: false
                },
                series: [dataset]

            }));
        });
    }

    function detailDailyOutput(idChart, strlast, strlast2) {
        var nameChart = Object.keys(strlast)[0];
        var nameChart2 = Object.keys(strlast2)[0];
        var model1 = Object.keys(strlast.data);
        var model2 = Object.keys(strlast2.data);
        if (model1.length > 0) {
            // model1.forEach(e => {
            //     if(!model2.includes(e)){
            //             model2.push(e)
            //     }
            // });
            for (var index = 0; index < model1.length; index++) {
                var element = model1[index];
                if (model2.indexOf(element) == -1) {
                    model2.push(element);
                }
            }
        }
        var dtChart = [];
        var dtChart2 = [];
        // model2.forEach(el => {
        //     var df = el;
        //     var tmp;
        //         if(strlast.data[df] == undefined){
        //             tmp = 0;
        //             dtChart.push(tmp);
        //         }else{
        //             tmp = strlast.data[df];
        //             dtChart.push(tmp);
        //         }
        //         if(strlast2.data[df] == undefined){
        //             dtChart2.push(0);
        //         }else{
        //             let tmp2 = strlast2.data[df];
        //             dtChart2.push(tmp2);
        //         }
        // });
        for (var index = 0; index < model2.length; index++) {
            var el = model2[index];
            var df = el;
            var tmp;
            if (strlast.data[df] == undefined) {
                tmp = 0;
                dtChart.push(tmp);
            } else {
                tmp = strlast.data[df];
                dtChart.push(tmp);
            }
            if (strlast2.data[df] == undefined) {
                dtChart2.push(0);
            } else {
                let tmp2 = strlast2.data[df];
                dtChart2.push(tmp2);
            }
        }
        var charts = [],
            $containers = $('#trellisOut td'),
            datasets = [{
                name: nameChart,
                data: dtChart
            },
            {
                name: nameChart2,
                data: dtChart2
            }
            ];
        $.each(datasets, function (i, dataset) {
            charts.push(new Highcharts.Chart({
                chart: {
                    renderTo: $containers[i],
                    type: 'bar',
                    // marginLeft: i === 0 ? 100 : 10
                },
                title: {
                    text: dataset.name,
                    align: 'center',
                    style: {
                        fontSize: '16px',
                        fontWeight: 'bold',
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    },
                    // x: i === 0 ? 90 : 0
                },

                credits: {
                    enabled: false
                },

                xAxis: {
                    categories: model2,
                    labels: {
                        enabled: i === 0
                    }
                },
                plotOptions: {
                    series: {
                        borderWidth: false,
                        dataLabels: {
                            enabled: true,
                            // color: 'blue'
                        },
                        point: {
                            events: {
                                click: function () {
                                    var model_name = this.category.name;
                                    $('#tblReasonCode').removeClass('hidden');
                                    getReasonCode(model_name);
                                }
                            }
                        }
                    }
                },
                yAxis: {
                    // allowDecimals: false,
                    title: {
                        text: ''
                    },
                    min: 0,
                    max: 100
                },
                navigation: {
                    buttonOptions: {
                        enabled: false
                    }
                },
                credits: {
                    enabled: false
                },
                legend: {
                    enabled: false
                },
                series: [dataset]
            }));
        });
    }
    function hightChartInOutStatus(idChart, textHtml, dataChart) {
        Highcharts.chart(idChart, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: textHtml,
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
                    // point: {
                    //     events: {
                    //         click: function (event) {
                    //             var model_name = this.name;
                    //             checkin = true;
                    //             checkout = false;
                    //             dateailModelName(model_name)
                    //         }
                    //     }
                    // }
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
                name: '',
                data: dataChart
            }]
        });
        //  hightChartInOutStatus1(idChart, textHtml, dataChart);
    }
    function hightChartInOutStatusOut(idChart, textHtml, dataChart) {
        Highcharts.chart(idChart, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: textHtml,
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
                    point: {
                        events: {
                            click: function (event) {
                                var model_name = this.name;
                                checkout = true;
                                checkin = false;
                                dateailModelName(model_name)
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
                name: '',
                data: dataChart
            }]
        });
        //   hightChartInOutStatus2(idChart, textHtml, dataChart);
    }

    function hightChartErrorCodeColum(idChart, textHtml, dataChart) {
        Highcharts.chart(idChart, {
            chart: {
                type: 'column',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: textHtml,
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
                            click: function (event) {
                                var model_name = this.name;
                                loadColumnschart1(model_name, section_name);
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
    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var d = new Date();
                var yesterdayDate = d - 1000 * 60 * 60 * 24 * 1;
                yesterdayDate = new Date(yesterdayDate);
                var startDate = moment(yesterdayDate).format("YYYY/MM/DD") + ' 07:30';
                var endDate = moment(current.setDate(current.getDate() - 1)).add(1, "day").format("YYYY/MM/DD") + ' 07:30';
                $('.datetimerange').data('daterangepicker').setStartDate(startDate);
                $('.datetimerange').data('daterangepicker').setEndDate(endDate);

                dataset.timeSpan = startDate + ' - ' + endDate;
                getModel();
                loadWipInOutStatus();
                $('input[name=timeSpan]').on('change', function () {
                    $('#tblErrorCode').addClass('hidden');
                    $('#tblReasonCode').addClass('hidden');
                    dataset.timeSpan = this.value;
                    getModel();
                    loadWipInOutStatus();
                });
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

        // $('input[name=timeSpan]').on('change', function () {
        //     // console.log("da:",this.value)
        //     dataset.timeSpan = this.value;
        //     init();
        // });

    });
    // function daysBetween(timeStart, timeEnd){
    //     const oneDay = 1000*60*60*24;
    //     const diff = Math.abs*(timeEnd-timeStart);
    //     return Math.round(diff/oneDay);
    // }

    // function setDataForInputTime(time){
    //     var current = new Date(time);
    //     var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
    //     var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
    //     $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
    //     $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));     
    // }

    // function setDataForInputTime(timeStart, timeEnd){
    //     var start = new Date(timeStart);
    //     var end = new Date(timeEnd);
    //     var startDate = moment(start).format("YYYY/MM/DD") + ' 07:30:00';
    //     var endDate = moment(start).add(10, "day").format("YYYY/MM/DD") + ' 07:30:00';
    //     console.log(daysBetween(start,end));
    //     $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
    //     $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));     
    // }
</script>