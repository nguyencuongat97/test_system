<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />

<!-- <div class="loader"></div> -->
<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-2">
        <div class="row" style="margin: 0px; padding: 5px; min-height: 105px; text-align: left;">
            <table class="table tblSelection">
                <tr>
                    <td>Customer: </td>
                    <td>
                        <select style="background: #333333; color: #fff; width: 100%;" id="selectCustomer">
                        </select>
                    </td>
                </tr>
                <!-- <tr>
                    <td>Stage: </td>
                    <td>
                        <select style="background: #333333; color: #fff; width: 100%;" id="selectStage">
                        </select>
                    </td>
                </tr> -->
                <!-- <tr>
                    <td>Group: </td>
                    <td>
                        <select style="background: #333333; color: #fff; width: 100%;" id="selectGroup">
                        </select>
                    </td>
                </tr> -->
                <tr>
                    <td colspan="2" style="text-align: center"><button id="okBtn" class="btn btn-primary" style="width: 40%; padding: 1px;">OK</button></td>
                </tr>
            </table>
        </div>
        <div class="row" style="min-height: 100px; margin: 0px; border: 1px solid #999999; background-color: #333333;">
            <p style="font-size: 22px; margin-top: 5px;">Total: <span id="totalNum">0</span></p>
            <p style="font-size: 22px; margin: 0px;">Average: <span id="totalRate" style="font-size: 22px; color: #ffffff">00.00</span>%</p>
        </div>
        <div class="row" style="margin: 10px 0px 0px 0px; padding: 0px; min-height: 220px; border: 1px solid #999999; background-color: #333333;">
            <div class="col-sm-9">
                <div class="row">
                    <strong style="font-size: 18px;">Status</strong>
                </div>
                <div class="row">
                    <div class="statusCheck msnormal"></div> <span style="margin: 5px; float: left;"> RUNNING</span> </br>
                </div>
                <div class="row">
                    <div class="statusCheck mswarning"></div> <span style="margin: 5px; float: left;"> WAITING</span> </br>
                </div>
                <div class="row">
                    <div class="statusCheck msdanger"></div> <span style="margin: 5px; float: left;"> FAILURE</span> </br>
                </div>
                <div class="row">
                    <div class="statusCheck msdowntime"></div> <span style="margin: 5px; float: left;"> DOWNTIME</span> </br>
                </div>
                <div class="row">
                    <div class="statusCheck msoffnet"></div> <span style="margin: 5px; float: left;"> DISCONNECTED</span> </br>
                </div>
                <div class="row">
                    <div class="statusCheck msoffline"></div> <span style="margin: 5px; float: left;"> SHUTDOWN</span> </br>
                </div>
            </div>
            <div class="col-sm-3">
                <div class="row" style="margin: auto;">
                    <strong style="font-size: 18px;">Qty</strong>
                </div>
                <div class="row" style="margin: auto;">
                    <span style="margin: 5px; float: left;" id="runningNum"> 0</span>
                </div>
                <div class="row" style="margin: auto;">
                    <span style="margin: 5px; float: left;" id="waitingNum"> 0</span>
                </div>
                <div class="row" style="margin: auto;">
                    <span style="margin: 5px; float: left;" id="failureNum"> 0</span>
                </div>
                <div class="row" style="margin: auto;">
                    <span style="margin: 5px; float: left;" id="downtimeNum"> 0</span>
                </div>
                <div class="row" style="margin: auto;">
                    <span style="margin: 5px; float: left;" id="disconnectedNum"> 0</span>
                </div>
                <div class="row" style="margin: auto;">
                    <span style="margin: 5px; float: left;" id="shutdownNum"> 0</span>
                </div>
            </div>
        </div>
    </div>
    <div class="col-md-10" id="dataViewPort">
        <div class="panel panel-overview" id="header">
            <b><span></span></b>
        </div>
    </div>
</div>


<style>
    .ms {
        width: 12px;
        height: 12px;
        border-radius: 50%;
        float: left;
        margin: 0px 0px 5px 3px;
    }
    
    .statusCheck {
        width: 15px;
        height: 15px;
        border-radius: 50%;
        float: left;
        margin: 8px 0px 5px 5px;
    }
    
    .msnormal {
        background-color: #00DF53;
    }
    
    .mswarning {
        background-color: #D1DF00;
    }
    
    .msdanger {
        background-color: #DF0000;
    }
    
    .msdowntime {
        background-color: #5F9EA0;
    }
    
    .msoffnet {
        background-color: #663300;
    }
    
    .msoffline {
        background-color: #626463;
    }
    
    .msline {
        background-color: #aee0fd;
        color: #272727;
        font-size: 61%;
    }
    
    .popover {
        min-width: 16rem;
        background-color: #ffffff;
        border: 1px solid #4e7af3;
        box-shadow: 0.5rem 0.5rem 2rem #272727;
        font-size: 85%;
    }
    
    .popover-content {
        color: #272727;
        padding: 7px;
    }
    
    .popover-title {
        color: #272727;
        text-align: center;
    }
    
    .tblSelection tr td {
        border-top: 0px !important;
        padding: 12px 0px !important;
    }
    
    .boxCustomer {
        border: 0.1px solid #989898;
        padding: 5px;
        margin: 0px 0px 5px 0px;
        float: left;
        background-color: #333333;
    }
    
    .boxCustomer .row {
        margin: unset;
    }
    
    .boxAppend {
        padding-top: 5px;
        min-height: 160px;
    }
</style>

<script>
    $(document).ready(function() {
        init();
    });

    function init() {
        loadSelectCustomer('VENTO', 'SUB-PACK', 'LASER');
        // $(".loader").addClass("disableSelect");
    }

    var listStage = [];
    var listGroup = [];

    function loadSelectCustomer(preCustomer, preStage, preGroup) {
        $.ajax({
            type: "GET",
            url: "/api/test/nbb/customer",
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var list = '';
                for (i in data) {
                    if (preCustomer == data[i]) {
                        list += '<option selected="selected">' + data[i] + '</option>';
                        $("#selectCustomer").val(preCustomer);
                    } else {
                        list += '<option>' + data[i] + '</option>';
                    }
                }
                $("#selectCustomer").html(list);

                loadSelectStage($("#selectCustomer").val(), preStage, preGroup);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadSelectStage(customerName, preStage, preGroup) {
        $.ajax({
            type: "GET",
            url: "/api/test/nbb/stage",
            data: {
                customer: customerName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var list = '';
                for (i in data) {
                    if (preStage == data[i]) {
                        list += '<option selected="selected">' + data[i] + '</option>';
                        $("#selectStage").val(preStage);
                    } else {
                        list += '<option>' + data[i] + '</option>';
                    }
                }
                // $("#selectStage").html(list);
                // loadSelectGroup($("#selectCustomer").val(), $("#selectStage").val(), preGroup, true);
                loadSelectGroup($("#selectCustomer").val(), preStage, preGroup, true);
                listStage = data;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadSelectGroup(customerName, stageName, preGroup, isLoadData) {
        $.ajax({
            type: "GET",
            url: "/api/test/nbb/oee/list/group",
            data: {
                customer: customerName,
                stage: stageName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var list = '';
                for (i in data) {
                    if (preGroup == data[i]) {
                        list += '<option selected="selected">' + data[i] + '</option>';
                        $("#selectGroup").val(preGroup);
                    } else {
                        list += '<option>' + data[i] + '</option>';
                    }
                }
                // $("#selectGroup").html(list);
                listGroup = data;
                if (isLoadData) {
                    loadData();
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadData() {
        var divheader = '<div class="panel panel-overview" style="text-align: center; margin-bottom: 5px; background: #333; color: #ccc"><b><span style="font-size:18px">' + $("#selectCustomer").val() + '</span></b></div>';
        $("#dataViewPort").html(divheader);

        var total = 0;
        var rate = 0;
        var running = 0;
        var waiting = 0;
        var failure = 0;
        var downtime = 0;
        var disconnected = 0;
        var shutdown = 0;
        $('#totalNum').html(total);
        $('#totalRate').html(rate);
        $('#runningNum').html(running);
        $('#waitingNum').html(waiting);
        $('#failureNum').html(failure);
        $('#downtimeNum').html(downtime);
        $('#disconnectedNum').html(disconnected);
        $('#shutdownNum').html(shutdown);

        for (var i = 0; i < listStage.length; i++) {
            var stageName = listStage[i];

            $.ajax({
                type: "GET",
                url: "/api/test/nbb/oee/list/group",
                stage: stageName,
                data: {
                    customer: $("#selectCustomer").val(),
                    stage: stageName
                },
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    var stName = this.stage;
                    var x = 0;
                    while (x < data.length) {
                        var groupName = data[x];
                        $.ajax({
                            type: "GET",
                            url: "/api/test/nbb/oee/station/status/v2",
                            stage: stName,
                            data: {
                                customer: $("#selectCustomer").val(),
                                stage: stName,
                                group: groupName
                            },
                            contentType: "application/json; charset=utf-8",
                            success: function(data) {
                                $(".loader").removeClass("disableSelect");
                                total += Number(data["statusQty"].total);

                                running += Number(data["statusQty"]['RUNNING']);
                                waiting += Number(data["statusQty"]['WAITING']);
                                failure += Number(data["statusQty"]['FAILURE']);
                                downtime += Number(data["statusQty"]['DOWNTIME']);
                                disconnected += Number(data["statusQty"]['DISCONNECTED']);
                                shutdown += Number(data["statusQty"]['SHUTDOWN']);

                                rate = ((running + waiting) * 100 / total).toFixed(2);
                                if (data["statusQty"].total > 0 && data["statusQty"].total < 100) {
                                    var html = '';
                                    html += '<div class="col-md-2 boxCustomer">' +
                                        '<div class="row" style="border-bottom: 1px dotted;"><span style="font-size: 90%; margin: 5px; font-weight: bold;">' + this.stage + ' - <span class="spGroup"></span> (' + data["statusQty"].RUNNING + "/" + data["statusQty"].total + ')</span></div>' +
                                        '<div class="row boxAppend">';
                                    html += '</div></div>';
                                    $('#dataViewPort').append(html);
                                    var htmlTmp = '';
                                    for (var lineName in data['data']) {
                                        htmlTmp += '<div class="row"><div class="ms msline">' + lineName + '</div>';

                                        for (var j = 0; j < (data['data'])[lineName].length; j++) {
                                            var statusClass = '';
                                            if (data['data'][lineName][j]['status'] == 'RUNNING') {
                                                statusClass = 'msnormal';
                                            } else if (data['data'][lineName][j]['status'] == 'WAITING') {
                                                statusClass = 'mswarning';
                                            } else if (data['data'][lineName][j]['status'] == 'FAILURE') {
                                                statusClass = 'msdanger';
                                            } else if (data['data'][lineName][j]['status'] == 'DOWNTIME') {
                                                statusClass = 'msdowntime';
                                            } else if (data['data'][lineName][j]['status'] == 'DISCONNECTED') {
                                                statusClass = 'msoffnet';
                                            } else if (data['data'][lineName][j]['status'] == 'SHUTDOWN') {
                                                statusClass = 'msoffline';
                                            }

                                            var spGroup = data['data'][lineName][j]['group'];
                                            for (var k = 0; k < $(".spGroup").length; k++) {
                                                if ($(".spGroup")[k].innerHTML == "") {
                                                    $(".spGroup")[k].innerHTML = spGroup;
                                                }
                                            }
                                            var contentpop = '<div><b>Last connected: </b><span>' + data['data'][lineName][j]['lastConnectedTime'] + '</span></div>' +
                                                '<div><b>Station ID: </b><span>' + data['data'][lineName][j]['stationId'] + '</span></div>' +
                                                '<div><b>MAC: </b><span>' + data['data'][lineName][j]['macAddress'] + '</span></div>' +
                                                '<div><b>IP: </b><span>' + data['data'][lineName][j]['ipAddress'] + '</span></div>';
                                            htmlTmp += '<div class="ms ' + statusClass + '" data-toggle="popover" data-content="' + contentpop + '" data-container="body" data-trigger="hover click"></div>';
                                        }
                                        htmlTmp += '</div>';
                                    }
                                } else if (data["statusQty"].total >= 100) {
                                    var html = '';
                                    html += '<div class="col-md-4 boxCustomer">' +
                                        '<div class="row" style="border-bottom: 1px dotted;"><span style="font-size: 90%; margin: 5px; font-weight: bold;">' + this.stage + ' - <span class="spGroup"></span> (' + data["statusQty"].RUNNING + "/" + data["statusQty"].total + ')</span></div>' +
                                        '<div class="row boxAppend">';
                                    html += '</div></div>';
                                    $('#dataViewPort').append(html);
                                    var htmlTmp = '';
                                    for (var lineName in data['data']) {
                                        htmlTmp += '<div class="row"><div class="ms msline">' + lineName + '</div>';

                                        for (var j = 0; j < (data['data'])[lineName].length; j++) {
                                            var statusClass = '';
                                            if (data['data'][lineName][j]['status'] == 'RUNNING') {
                                                statusClass = 'msnormal';
                                            } else if (data['data'][lineName][j]['status'] == 'WAITING') {
                                                statusClass = 'mswarning';
                                            } else if (data['data'][lineName][j]['status'] == 'FAILURE') {
                                                statusClass = 'msdanger';
                                            } else if (data['data'][lineName][j]['status'] == 'DOWNTIME') {
                                                statusClass = 'msdowntime';
                                            } else if (data['data'][lineName][j]['status'] == 'DISCONNECTED') {
                                                statusClass = 'msoffnet';
                                            } else if (data['data'][lineName][j]['status'] == 'SHUTDOWN') {
                                                statusClass = 'msoffline';
                                            }

                                            var spGroup = data['data'][lineName][j]['group'];
                                            for (var k = 0; k < $(".spGroup").length; k++) {
                                                if ($(".spGroup")[k].innerHTML == "") {
                                                    $(".spGroup")[k].innerHTML = spGroup;
                                                }
                                            }
                                            var contentpop = '<div><b>Last connected: </b><span>' + data['data'][lineName][j]['lastConnectedTime'] + '</span></div>' +
                                                '<div><b>Station ID: </b><span>' + data['data'][lineName][j]['stationId'] + '</span></div>' +
                                                '<div><b>MAC: </b><span>' + data['data'][lineName][j]['macAddress'] + '</span></div>' +
                                                '<div><b>IP: </b><span>' + data['data'][lineName][j]['ipAddress'] + '</span></div>';
                                            htmlTmp += '<div class="ms ' + statusClass + '" data-toggle="popover" data-content="' + contentpop + '" data-container="body" data-trigger="hover click"></div>';
                                        }
                                        htmlTmp += '</div>';
                                    }

                                }
                                for (var k = 0; k < $(".boxAppend").length; k++) {
                                    if ($(".boxAppend")[k].innerHTML == "") {
                                        $(".boxAppend")[k].innerHTML = htmlTmp;
                                    }
                                }

                                $('[data-toggle=popover]').popover({
                                    template: '<div class="popover"><div class="arrow"></div><div class="popover-content"></div></div>',
                                    placement: "left",
                                    html: "true"
                                });

                                // $('#dataViewPort').append(html);

                                $('#totalNum').html(total);
                                $('#totalRate').html(rate);
                                if (rate < 50) {
                                    $('#totalRate').css('color', '#DF0000');
                                } else if (rate > 50 && rate < 75) {
                                    $('#totalRate').css('color', '#D1DF00');
                                } else if (rate > 75) {
                                    $('#totalRate').css('color', '#00DF53');
                                }


                                $('#runningNum').html(running);
                                $('#waitingNum').html(waiting);
                                $('#failureNum').html(failure);
                                $('#downtimeNum').html(downtime);
                                $('#disconnectedNum').html(disconnected);
                                $('#shutdownNum').html(shutdown);
                                $(".loader").addClass("disableSelect");
                            },
                            failure: function(errMsg) {
                                console.log(errMsg);
                            }
                        });
                        x++;
                    }
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });
        }

    }

    $('#selectCustomer').change(function() {
        loadSelectStage($("#selectCustomer").val());
    });

    $('#selectStage').change(function() {
        loadSelectGroup($("#selectCustomer").val(), $("#selectStage").val());
    });

    $('#okBtn').click(function() {
        loadData();
    });
</script>