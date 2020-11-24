<!-- TROUBLE AND HISTORY -->
<link rel="stylesheet" href="/assets/css/custom/modal-trouble-and-history.css">
<link rel="stylesheet" href="/assets/css/custom/style.css">

<div class="panel-notify-content">
    <div class="panel-notify-header" style="padding: 10px 0px 0px 10px;">
        <h4 class="modal-title" style="padding-right: 36px;"><span class="error_code">HD0001</span> - <span class="error_desc">HD0001</span></h4>
    </div>
    <div class="panel-notify-body" style="padding: 5px;">
        <div class="tab-error-code">
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#trouble" style="font-size:13px; padding: 8px;">Trouble</a></li>
                <li><a data-toggle="tab" href="#history" style="font-size:13px; padding: 8px;">History</a></li>
            </ul>
            <div class="tab-content">
                <!--Tab trouble-->
                <div id="trouble" class="tab-pane active" style="margin:0 auto; font-size: 13px;">
                    <!-- <div id="data-analysis" style="margin-left: 10px; margin-right: 10px;">
                        <b style="font-size: 14px;">Test data analysis</b>
                    </div> -->
                    <div class="form-horizontal" id="solution" style="margin-left: 10px; margin-right: 10px;">
                        <div class="form-group form-group-xs">
                            <div class="col-xs-12 ">
                                <label class="control-label text-bold" style="font-size: 20px; padding-top: 8px;">Solution</label>
                                <div class="pull-right">
                                    <button class="btn btn-xs" name="search" data-toggle="modal" data-target="#modal-search-solution" onclick="loadSearchSolution()"><i class="fa fa-search"></i></button>
                                    <button class="btn btn-xs" name="add" data-error-code="" data-error-desc="" data-toggle="modal" data-target="#modal-add-new-solution" onclick="showAddNewModal(this)"><i class="fa fa-plus"></i></button>
                                    <button class="btn btn-xs" name="confirm" data-solution-id="" data-error-code="" data-toggle="modal" data-target="#modal-confirm-solution" onclick="showConfirmModal(this)"><i class="fa fa-check"></i></button>
                                </div>
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
                                <label class="control-label text-bold" style="font-size: 20px; padding-top: 8px;">Troubleshooting Guide</label>
                                <button class="btn btn-xs pull-right" name="guiding" href="#modal-guiding" data-toggle="modal" data-backdrop="false" onclick="loadImageGuiding();" style="width: 26px; height: 26px; padding: 1px;" disabled="disabled"><i class="icon-images2"></i></button>
                            </div>
                            <div class="col-xs-12" id="guiding">
                                <ol>
                                    <li>NO DATA</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                    <div id="chart-error-code-by-tester" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <!-- <div id="chart-defected-issue" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div> -->
                    <!-- <div id="chart-ntf-issue" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div> -->
                </div>
                <!--Tab history-->
                <div id="history" class="tab-pane">
                   <table class="table table-xxs table-bordered" name="table_solution_history">
                       <thead class="bg-primary" style="background-color: #545b62">
                           <tr>
                               <th>No</th>
                               <th>Root cause</th>
                               <th>Action</th>
                               <th>Qty</th>
                               <th>Guiding</th>
                           </tr>
                       </thead>
                       <tbody>
                       </tbody>
                   </table>
                   <div id="chart-solution-history" style="height: 250px;"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>

$('select[name="reason"]').on('change', function() {
   dataset.solutionId = this.value;
   loadGuiding();
});

function loadTroubleAndHistory() {
    $('span.error_code').html(dataset.errorCode);
    if (dataset.errorDesc != undefined && dataset.errorDesc != '') {
        $('span.error_desc').html(dataset.errorDesc);
    } else {
        $('span.error_desc').html('NO DESCRIPTION');
    }
    loadDetail();
    loadChartStationByError();
    loadHistory();
}

function loadDetail() {
    // loadDataAnalysis();

    if (dataset.trackingId != undefined && dataset.trackingId != null && dataset.trackingId != '') {
        $('button[name="add"').removeClass('hidden');
        $('button[name="confirm"').removeClass('hidden');
        $.ajax({
            type: "GET",
            url: "/api/test/tracking/detail",
            data: {
                trackingId: dataset.trackingId
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if (!$.isEmptyObject(data) && data.employee != null) {
                    dataset.employee = data.employee;
                    searchEmployee(dataset.employee);
                    return;
                }

                dataset.employee = null;
                dataset.employeeName = null;

                $("input[name=employeeNo]").val('');
                $("label[name=search_emp]").html('');
                $('button[name="btn-submit-solution"]').attr("disabled", "disabled");
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    } else {
        $('button[name="add"').addClass('hidden');
        $('button[name="confirm"').addClass('hidden');
    }
    loadSolutionAndGuiding();
}

function loadDataAnalysis() {
    $('#data-analysis>ol').remove();

    if (dataset.factory.toUpperCase() == 'B04') {
        $('#data-analysis').append(
            '<ol>' +
                '<li>CPK: 0.25</li>' +
                '<li>Average test value: 12.06</li>' +
                '<li>Cable loss: 14dBm</li>' +
            '</ol>');
    } else {
        $('#data-analysis').append('<ol><li>NO DATA</li></ol>');
    }
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

function loadGuiding2() {
    $.ajax({
        type: 'GET',
        url: '/api/test/solution',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            errorCode: dataset.errorCode
        },
        success: function(data){
            $('#guiding>ol').remove();
            var btnAdd = $('button[name="add"]')[0];
            btnAdd.dataset.errorCode = dataset.errorCode;
            btnAdd.dataset.errorDesc = dataset.errorDesc;

            var btnConfirm = $('button[name="confirm"]')[0];
            btnConfirm.dataset.errorCode = dataset.errorCode;
            btnConfirm.dataset.errorDesc = dataset.errorDesc;
            btnConfirm.dataset.solutionId = '';
            btnConfirm.setAttribute("disabled", "disabled");

            var btnGuiding = $('button[name="guiding"]')[0];
            btnGuiding.setAttribute("disabled", "disabled");

            dataset.solution = data[0];
            if (!$.isEmptyObject(data)) {
                btnConfirm.dataset.solutionId = data[0].id;
                btnConfirm.removeAttribute("disabled");

                $('label[name="reason"]').html(data[0].solution);
                $('label[name="action"]').html(data[0].action);

                var guiding = '';
                if (data[0].steps.length > 0) {
                    for (i in data[0].steps) {
                        guiding += '<li>'+data[0].steps[i].text+'</li>'
                    }
                    $('#guiding').append('<ol>'+guiding+'</ol>');
                    btnGuiding.removeAttribute("disabled");
                } else {
                    $('#guiding').append('<ol><li>NO DATA</li></ol>');
                }
            } else {
                $('label[name="reason"]').html('NO DATA');
                $('label[name="action"]').html('NO DATA');

                $('#guiding').append('<ol><li>NO DATA</li></ol>');
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
            groupName: dataset.groupName,
            errorCode: dataset.errorCode,
            timeSpan: dataset.timeSpan
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
                        rotation: 90,
                        align: top,
                        style: {
                            fontSize: '11px'
                        }
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

    // $.ajax({
    //     type: 'GET',
    //     url: '/api/test/error/defectedIssue',
    //     data: {
    //         factory: dataset.factory,
    //         modelName: dataset.modelName,
    //         errorCode: dataset.errorCode
    //     },
    //     success: function(data){
    //         var keys = Object.keys(data);
    //         var dataChart = new Array(keys.length);
    //         var categories = new Array(keys.length);
    //         for(i in keys){
    //             categories[i] = keys[i];
    //             dataChart[i] = {name: keys[i], y: data[keys[i]]}
    //         }

    //         Highcharts.chart('chart-defected-issue', {
    //             chart: {
    //                 type: 'column',
    //                 style: {
    //                     fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
    //                 }
    //             },
    //             title: {
    //                 text: dataset.errorCode + ' DEFECTED ISSUE'
    //             },
    //             xAxis: {
    //                 categories: categories,
    //                 gridLineWidth: 0,
    //                 minorGridLineWidth: 0,
    //                 type: 'category',
    //                 labels: {
    //                     autoRotation: false,
    //                     style: {
    //                         fontSize: '11px'
    //                     },
    //                 }
    //             },
    //             yAxis: {
    //                 min: 0,
    //                 title: {
    //                     text: ' '
    //                 },
    //                 stackLabels: {
    //                     enabled: true,
    //                     style: {
    //                         fontWeight: 'bold',
    //                         color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
    //                     }
    //                 },
    //                 minorGridLineWidth: 0,
    //                 minTickInterval: 1,
    //                 tickAmount: 4
    //             },
    //             legend: {
    //                 enabled: false
    //             },
    //             navigation: {
    //                 buttonOptions: {
    //                     enabled: false
    //                 }
    //             },
    //             credits: {
    //                 enabled: false
    //             },
    //             plotOptions: {
    //                 column: {
    //                     stacking: 'normal',
    //                 }
    //             },
    //             series: [{
    //                 name: ' ',
    //                 colorByPoint: true,
    //                 data: dataChart,
    //                 marker: {
    //                     enabled: false
    //                 },
    //             }]
    //         });
    //     },
    //     failure: function(errMsg) {
    //          console.log(errMsg);
    //     }
    // });

    // $.ajax({
    //     type: 'GET',
    //     url: '/api/test/station/ntfIssue',
    //     data: {
    //         factory: dataset.factory,
    //         modelName: dataset.modelName,
    //         errorCode: dataset.errorCode
    //     },
    //     success: function(data){
    //         var keys = Object.keys(data);
    //         var dataChart = new Array(keys.length);
    //         var categories = new Array(keys.length);
    //         for(i in keys){
    //             categories[i] = keys[i];
    //             dataChart[i] = {name: keys[i], y: data[keys[i]]}
    //         }

    //         Highcharts.chart('chart-ntf-issue',{
    //             chart: {
    //                 type: 'column',
    //                 style: {
    //                     fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
    //                 }
    //             },
    //             title: {
    //                 text: (dataset.errorCode + ' NTF ISSUE BY TESTER')
    //             },
    //             xAxis: {
    //                 categories: categories,
    //                 labels:{
    //                     style: {
    //                         fontSize: '11px'
    //                     }
    //                 },
    //                 gridLineWidth: 0,
    //                 minorGridLineWidth: 0,
    //             },
    //             yAxis: {
    //                 min: 0,
    //                 title: {
    //                     text: ' '
    //                 },
    //                 stackLabels: {
    //                     enabled: true,
    //                     style: {
    //                         fontWeight: 'bold',
    //                         color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
    //                     }
    //                 },
    //                 minorGridLineWidth: 0,
    //             },
    //             legend: {
    //                 enabled: false
    //             },
    //             navigation: {
    //                 buttonOptions: {
    //                     enabled: false
    //                 }
    //             },
    //             credits: {
    //                 enabled: false
    //             },
    //             plotOptions: {
    //                 column: {
    //                     stacking: 'normal',
    //                 }
    //             },
    //             series: [{
    //                 name: ' ',
    //                 colorByPoint: true,
    //                 data: dataChart
    //             }]
    //         });
    //     },
    //     failure: function(errMsg) {
    //          console.log(errMsg);
    //     }
    // });

}

function loadHistory() {
    $.ajax({
        type: 'GET',
        url: '/api/test/solution',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            errorCode: dataset.errorCode
        },
        success: function(data){
            var dataChart = new Array(data.length);
            var selector = $("table[name='table_solution_history']>tbody");
            selector.children('tr').remove();

            if (!$.isEmptyObject(data)) {
                var rows = "";
                for(i in data){
                    rows+='<tr><td>'+(1 + Number(i))+'</td><td>'+data[i].solution+'</td><td>'+data[i].action+'</td><td>'+data[i].numberSuccess+'</td>'+
                            '<td><button class="btn btnAction pull-right" href="#modal-guiding" data-toggle="modal" data-backdrop="false" onclick="loadImageGuiding('+i+');" style="width: 26px; height: 26px; padding: 1px;"><i class="icon-images2"></i></button></td></tr>';

                    dataChart[i] = {name: data[i].action, y: (data[i].numberSuccess == 0 ? 1 : data[i].numberSuccess)}
                }
                selector.append(rows);
            } else {
                selector.append('<tr><td colspan="5" align="center">-- NO DATA --</td></tr>');
            }

            Highcharts.chart('chart-solution-history',{
                chart:{
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type:'pie',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },
                title: {
                    text: ''
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: false
                        },
                        showInLegend: true
                    }
                },
                series: [{
                    name: ' ',
                    colorByPoint: true,
                    data: dataChart
                }],
                credits:{
                    enabled:false,
                },
                legend: {
                    layout: 'horizontal',
                    align: 'left',
                    verticalAlign: 'bottom'
                }
            })
        },
        failure: function(errMsg) {
             console.log(errMsg);
        },
   });
}

</script>
<!-- /TROUBLE AND HISTORY -->