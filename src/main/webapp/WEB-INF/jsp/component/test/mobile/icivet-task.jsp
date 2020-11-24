<link rel="stylesheet" href="/assets/css/custom/modal-trouble-and-history.css">
<link rel="stylesheet" href="/assets/css/custom/style.css">

<div class="col-lg-4 col-sm-12"></div>
<div class="col-lg-4 col-sm-12">
    <div style="background-color: #fff;">
        <div class="panel-notify-content">
            <div class="panel-notify-header" style="padding: 10px 0px 0px 0px;">
                <div class="col-lg-4 col-sm-12" style="text-align: center;">
                    <h3 class="text-bold" style="margin-top: 5px; margin-bottom: 15px;"><a data-toggle="modal" data-target="#modal-task-history" onclick="loadTaskHistory()"><span name="model-name"></span> | <span name="group-name"></span> | <span name="station-name"></span></a></h3>
                </div>
                <div class="col-lg-4 col-sm-12" style="text-align: center;">
                    <h5><span name="tracking-detail"></span></h5>
                </div>
                <div class="col-lg-4 col-sm-12">
                    <div class="btn-group btn-group-justified" name="btn-group-error-code" data-toggle="buttons">
                    </div>
                </div>
                <div class="col-lg-4 col-sm-12" style="text-align: center;">
                    <h6><span name="error-desc"></span></h6>
                </div>
            </div>
            <div class="panel-notify-body" style="padding: 5px;">
                <div id="trouble" class="tab-pane active" style="margin:0 auto; font-size: 13px;">
                    <div class="form-horizontal" id="solution" style="margin-left: 10px; margin-right: 10px;">
                        <div class="form-group form-group-xs">
                            <div class="col-xs-12" style="text-align: center;">
                                <button class="btn btn-primary" name="scanqr" onclick="scanQR()" disabled="disabled"><i class="fa fa-qrcode"></i> SCAN QR</button>
                                <button class="btn btn-primary hidden" name="add" data-error-code="" data-error-desc="" data-toggle="modal" data-target="#modal-add-new-solution" onclick="showAddNewModal(this)" ><i class="fa fa-plus"></i> ADD NEW</button>
                                <button class="btn btn-primary hidden" name="confirm" data-solution-id="" data-error-code="" data-toggle="modal" data-target="#modal-confirm-solution" onclick="showConfirmModal(this)"><i class="fa fa-check"></i> CONFIRM</button>
                                <button class="btn btn-primary hidden" name="give-up" onclick="giveUp()"><i class="fa fa-close"></i> GIVE UP</button>
                            </div>
                        </div>
                        <div class="form-group form-group-xs">
                            <label class="col-xs-1 control-label text-bold" style="padding-top: 8px;"><i class="fa fa-question"></i></label>
                            <div class="col-xs-11">
                                <select class="form-control bootstrap-select" name="reason" >
                                    <option>NO DATA</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group form-group-xs">
                            <label class="col-xs-1 control-label text-bold" style="padding-top: 8px;"><i class="fa fa-pencil-square-o"></i></label>
                            <label class="col-xs-11" name="action" style="padding-top: 8px;">NO DATA</label>
                        </div>
                        <div class="form-group form-group-xs">
                            <div class="col-xs-12 ">
                                <label class="control-label text-bold" style="padding-top: 8px;">Troubleshooting Guide</label>
                                <button class="btn btn-xs pull-right" name="guiding" href="#modal-guiding" data-toggle="modal" data-backdrop="false" onclick="loadImageGuiding();" style="width: 26px; height: 26px; padding: 1px;" disabled="disabled"><i class="icon-images2"></i></button>
                            </div>
                            <div class="col-xs-12" id="guiding">
                                <ol>
                                    <li>NO DATA</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                    <div class="table-responsive pre-scrollable" style="margin: 10px 0px; max-height: 200px">
                        <table class="table table-xxs table-bordered text-nowrap" id="tbl-error-history" >
                            <thead class="bg-primary-400">
                                <tr class="thead">
                                    <th width="45px">No</th>
                                    <th>Time</th>
                                    <th>Tester</th>
                                    <th>Chamber</th>
                                    <th>Serial</th>
                                    <th>Error</th>
                                    <th>LSL</th>
                                    <th>USL</th>
                                    <th>Value</th>
                                    <th>Cycle</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    <div id="chart-defected-issue" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <div id="chart-ntf-issue" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <div id="chart-error-code-by-tester" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../modal/modal-guiding.jsp" %>
<%@ include file="../modal/modal-task-history.jsp" %>
<%@ include file="../modal/modal-serial-tracking.jsp" %>

<%@ include file="../modal/modal-confirm-solution.jsp" %>
<%@ include file="../modal/modal-add-new-solution.jsp" %>

<script src="/assets/js/custom/troubleshooting.js"></script>

<script>

var dataset = {
    trackingId: '${trackingId}'
}

if (cm.useCivet ==  true ) {
    cm.cleanCache();

    cm.getCurrentUser({
        success : function (res) {
            dataset.employee = res.value.emp_no;
            dataset.employeeName = res.value.emp_name;
        }
    });
} else {
    console.log( "Not a sweet environment." );
}

loadDetail();

function changeErrorCode(context) {
    dataset.errorCode = context.value;
    dataset.errorDesc = $(context).data("error-desc");
    loadSolutionAndGuiding();
    loadErrorHistory();
    loadChartStationByError();
}

$('select[name="reason"]').on('change', function() {
   dataset.solutionId = this.value;
   loadGuiding();
});

function loadDetail(){
	$.ajax({
        type: "GET",
        url: "/api/test/tracking/detail",
        data: {
            trackingId: dataset.trackingId
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if (!$.isEmptyObject(data)) {
                dataset.trackingId = data.id;
                dataset.factory = data.factory;
                dataset.modelName = data.modelName;
                dataset.groupName = data.groupName;
                dataset.stationName = data.stationName;

                $('span[name="model-name"]').html(dataset.modelName);
                $('span[name="group-name"]').html(dataset.groupName);
                $('span[name="station-name"]').html(dataset.stationName);
                $('span[name="tracking-detail"]').html(data.notify.detail);

                var html = '';
                var isFirst = true;
                $.each(data.errorMetaMap, function(key,value){
                    if (isFirst) {
                        isFirst = false;
                        dataset.errorCode = key;
                        dataset.errorDesc = value.errorDescription;
                        html += '<label class="btn btn-primary active"><input type="radio" name="error-code" onchange="changeErrorCode(this);" value="'+key+'" data-error-desc="'+value.errorDescription+'">'+key+'</label>';
                    } else {
                        html += '<label class="btn btn-primary"><input type="radio" name="error-code" onchange="changeErrorCode(this);" value="'+key+'" data-error-desc="'+value.errorDescription+'">'+key+'</label>';
                    }
                });
                $('.btn-group[name="btn-group-error-code"]').html(html);

                if (isFirst) {
                    dataset.errorCode = '';
                }
                loadErrorHistory();
                loadChartStationByError();

                if (data.status == 'CONFIRMED' || data.status == 'CLOSED' || data.status == 'REOPEN') {
                    $('button[name="scanqr"]').addClass('hidden');
                    dataset.solutionId = data.solutionId;
                    loadSolutionFromSolutionId();
                } else {
                    if (data.employee == null || data.employee == dataset.employee) {
                        if (data.status == 'ARRIVED' || data.status == 'UNLOCKED') {
                            $('button[name="scanqr"]').addClass('hidden');
                            $('button[name="add"]').removeClass("hidden");
                            $('button[name="confirm"]').removeClass("hidden");
                            $('button[name="give-up"]').removeClass("hidden");
                            loadSolutionAndGuiding();
                        } else {
                            $('button[name="scanqr"]').removeAttr('disabled')
                            loadSolutionAndGuiding();
                        }
                    } else {
                        $('button[name="scanqr"]').attr('disabled', 'disabled');
                        loadSolutionAndGuiding();
                    }
                }
            } else {
                $('button[name="scanqr"]').attr('disabled', 'disabled');
                $('select[name="reason"]').prop('disabled',true);
                $('select[name="reason"]').selectpicker('refresh');
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}

function loadSolutionAndGuiding() {
    $('span[name="error-desc"]').html(dataset.errorDesc != '' ? dataset.errorDesc : 'NO DESCRIPTION');

    var btnAdd = $('button[name="add"]')[0];
    btnAdd.dataset.errorCode = dataset.errorCode;
    btnAdd.dataset.errorDesc = dataset.errorDesc;

    var btnConfirm = $('button[name="confirm"]')[0];
    btnConfirm.dataset.errorCode = dataset.errorCode;
    btnConfirm.dataset.errorDesc = dataset.errorDesc;

    loadSolution();
}

function loadSolution() {
    $.ajax({
        type: 'GET',
        url: '/api/test/solution',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            errorCode: dataset.errorCode
        },
        success: function(data){
            if (!$.isEmptyObject(data)) {
                var html = "";
                for(i in data){
                    html += '<option value="' + data[i].id + '">' + data[i].solution + '</option>';
                }
                $('select[name="reason"]').html(html);
                $('select[name="reason"]').prop('disabled',false);
                dataset.solutionId = data[0].id;
            } else {
                $('select[name="reason"]').html('<option value="" disabled>NO DATA</option>');
                $('select[name="reason"]').prop('disabled',true);
                dataset.solutionId = 0;
            }
            $('select[name="reason"]').selectpicker('refresh');
            loadGuiding(dataset.solutionId);
        },
        failure: function(errMsg) {
             console.log(errMsg);
        },
   });
}

function loadSolutionFromSolutionId() {
    $.ajax({
        type: 'GET',
        url: '/api/test/solution/'+dataset.solutionId,
        data: {},
        success: function(data){
            if (!$.isEmptyObject(data)) {
                var html = '<option value="' + data.id + '">' + data.solution + '</option>';
                $('select[name="reason"]').html(html);
                $('select[name="reason"]').prop('disabled',true);
                dataset.solutionId = data.id;
            } else {
                $('select[name="reason"]').html('<option value="" disabled>NO DATA</option>');
                $('select[name="reason"]').prop('disabled',true);
                dataset.solutionId = 0;
            }
            $('select[name="reason"]').selectpicker('refresh');
            loadGuiding(dataset.solutionId);
        },
        failure: function(errMsg) {
             console.log(errMsg);
        },
   });
}

function loadGuiding() {
    $.ajax({
        type: 'GET',
        url: '/api/test/solution/' + dataset.solutionId,
        data: {},
        success: function(data){
            var btnConfirm = $('button[name="confirm"]')[0];
            btnConfirm.dataset.solutionId = '';
            btnConfirm.setAttribute("disabled", "disabled");

            var btnGuiding = $('button[name="guiding"]')[0];
            btnGuiding.setAttribute("disabled", "disabled");

            dataset.solution = data;
            if (!$.isEmptyObject(data)) {
                btnConfirm.dataset.solutionId = data.id;
                btnConfirm.removeAttribute("disabled");

                $('label[name="action"]').html(data.action);

                var guiding = '';
                if (data.steps.length > 0) {
                    for (i in data.steps) {
                        guiding += '<li>'+data.steps[i].text+'</li>'
                    }
                    $('#guiding>ol').html(guiding);
                    btnGuiding.removeAttribute("disabled");
                } else {
                    $('#guiding>ol').html('<li>NO DATA</li>');
                }
            } else {
                $('label[name="action"]').html('NO DATA');

                $('#guiding>ol').html('<li>NO DATA</li>');
            }
        },
        failure: function(errMsg) {
             console.log(errMsg);
        },
   });

}

function loadChartStationByError() {
    $.ajax({
        type: 'GET',
        url: '/api/test/station/byError',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            errorCode: dataset.errorCode
        },
        success: function(data){
            var dataChart = new Array(data.length);
            var categories = new Array(data.length);
            if (!$.isEmptyObject(data)) {
                for(i in data){
                    categories[i] = data[i].stationName;
                    dataChart[i] = {name: data[i].stationName, y: data[i].testFail}
                }
            }

            Highcharts.chart('chart-error-code-by-tester', {
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },
                title: {
                    text: (dataset.errorCode + ' ISSUE BY TESTER')
                },
                xAxis: {
                    categories: categories,
                    labels:{
                        style: {
                            fontSize: '11px'
                        }
                    },
                    gridLineWidth: 0,
                    minorGridLineWidth: 0,
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ' '
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    },
                    minorGridLineWidth: 0

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
                    column: {
                        stacking: 'normal',
                    }
                },
                series: [{
                    name: ' ',
                    colorByPoint: true,
                    data: dataChart
                }]
            });
        },
        failure: function(errMsg) {
             console.log(errMsg);
        }
    });

    $.ajax({
        type: 'GET',
        url: '/api/test/error/defectedIssue',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            errorCode: dataset.errorCode
        },
        success: function(data){
            var keys = Object.keys(data);
            var dataChart = new Array(keys.length);
            var categories = new Array(keys.length);
            for(i in keys){
                categories[i] = keys[i];
                dataChart[i] = {name: keys[i], y: data[keys[i]]}
            }

            Highcharts.chart('chart-defected-issue', {
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },
                title: {
                    text: dataset.errorCode + ' DEFECTED ISSUE'
                },
                xAxis: {
                    categories: categories,
                    gridLineWidth: 0,
                    minorGridLineWidth: 0,
                    type: 'category',
                    labels: {
                        autoRotation: false,
                        style: {
                            fontSize: '11px'
                        },
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ' '
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    },
                    minorGridLineWidth: 0,
                    minTickInterval: 1,
                    tickAmount: 4
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
                    column: {
                        stacking: 'normal',
                    }
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
        failure: function(errMsg) {
             console.log(errMsg);
        }
    });

    $.ajax({
        type: 'GET',
        url: '/api/test/station/ntfIssue',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            errorCode: dataset.errorCode
        },
        success: function(data){
            var keys = Object.keys(data);
            var dataChart = new Array(keys.length);
            var categories = new Array(keys.length);
            for(i in keys){
                categories[i] = keys[i];
                dataChart[i] = {name: keys[i], y: data[keys[i]]}
            }

            Highcharts.chart('chart-ntf-issue',{
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },
                title: {
                    text: (dataset.errorCode + ' NTF ISSUE BY TESTER')
                },
                xAxis: {
                    categories: categories,
                    labels:{
                        rotation: 90,
                        align: top,
                        style: {
                            fontSize: '11px'
                        }
                    },
                    gridLineWidth: 0,
                    minorGridLineWidth: 0,
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ' '
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    },
                    minorGridLineWidth: 0,
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
                    column: {
                        stacking: 'normal',
                    }
                },
                series: [{
                    name: ' ',
                    colorByPoint: true,
                    data: dataChart
                }]
            });
        },
        failure: function(errMsg) {
             console.log(errMsg);
        }
    });
}

function loadErrorHistory() {
    $.ajax({
        type: "GET",
        url: "/api/test/station/errorHistory",
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            groupName: dataset.groupName,
            stationName: dataset.stationName,
            errorCode: dataset.errorCode
        },
        contentType: "application/json; charset=utf-8",
        success: function(json){
            $('#tbl-error-history>tbody>tr').remove();

            var body = $('#tbl-error-history>tbody');
            if (!$.isEmptyObject(json)) {
                for (i in json) {
                    body.append('<tr><td>'+ (1 + Number(i)) +'</td><td>'+ json[i].time +'</td><td>'+ json[i].stationName +'</td><td>'+ json[i].chamber +'</td><td><a data-serial="'+ json[i].serial +'" data-toggle="modal" data-target="#modal_serial_tracking" onclick="loadSerialTracking(this.dataset.serial)">'+ json[i].serial +'</a></td><td>'+ json[i].errorCode +'</td><td>'+ json[i].lsl +'</td><td>'+ json[i].usl +'</td><td>'+ json[i].value +'</td><td>'+ json[i].cycle +'</td></tr>');
                }
            } else {
                body.append('<tr><td colspan="9" align="center">-- NO DATA HISTORY --</td></tr>');
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}

function scanQR() {
    var useCivet = cm.useCivet;
    if (useCivet == true) {
        cm.scanQRCode({
            success: function (res) {
                var resultStr = res.resultStr;
                if (res.resultStr == dataset.stationName) {
                    $.ajax({
                        type: 'POST',
                        url: '/api/test/tracking/arrive',
                        data: jQuery.param({
                            trackingId: dataset.trackingId,
                            employee: dataset.employee
                        }),
                        success: function(data){
                            if (data == 'success') {
                                $('button[name="add"]').removeClass("hidden");
                                $('button[name="confirm"]').removeClass("hidden");
                                $('button[name="give-up"]').removeClass("hidden");
                                $('button[name="scanqr"]').addClass("hidden");
                            } else {
                                alert('Scan QR code send API failed!');
                            }
                        },
                        failure: function(errMsg) {
                            console.log(errMsg);
                            alert('Scan QR code failed!');
                        }
                    });
                } else {
                    alert('QR code is not matched!');
                }
            }
        });
    }
    else {
        console.log( "Not a sweet environment." );
    }
}

function filePicker() {
    cm.chooseImage({
            type:  "all" ,
            maxCount:  "1" ,
            success: function (res) {

                var localIds = res.localIds;   //return the list of local IDs for the selected photo, localId can be used as the src attribute of the img tag to display the image
                var isVedio = res.isVedio;   //whether it is a video (string type, "false" / "true")
                alert(res.localIds);
            },
           Deal: function(){}
      });
}

</script>