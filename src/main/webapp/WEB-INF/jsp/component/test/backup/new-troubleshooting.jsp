
<link rel="stylesheet" href="/assets/css/custom/troubleshooting.css">

<div class="row" style="background-color: #fff;">
    <div class="col-12 col-lg-9 col-xl-10">
         <div class="troubleshooting-guide">
             <div class="panel panel-violet" style="margin-top:10px;">
                 <div class="panel-heading" style="padding: 0px;">
                     <div class="panel panel-overview bg-primary" style="text-align:center; margin-bottom: 10px;">
                         <b style="font-size:20px;">TroubleShooting Guide</b>
                     </div>
                     <b style="font-size: 24px; margin: 10px;">Solution</b>
                 </div>
                 <div class="panel panel-overview" style="margin-bottom: unset; padding: 1px 15px;">
                     <div class="row" style="position: relative;">
                         <div class="col-lg-3">
                             <select class="form-control bootstrap-select" name="modelName" >
                             </select>
                         </div>
                         <div class="col-lg-2">
                             <select class="form-control bootstrap-select" name="groupName" >
                             </select>
                         </div>
                         <div class="col-lg-3">
                             <select class="form-control bootstrap-select" name="stationName" >
                             </select>
                         </div>
                         <button class="btn" data-toggle="modal" data-target="#modal_search_solution" style="position: absolute; top: 5px; right: 15px; padding: 5px 10px;">SEARCH SOLUTION</button>
                     </div>
                 </div>

                 <div class="panel-heading" style="padding: unset; margin: 10px;">
                     <div class="table-responsive" style="margin: 10px 0px;">
                         <table class="table table-xxs table-bordered solution">
                             <thead class="bg-primary">
                                 <tr>
                                     <th width="15%">Error Code</th>
                                     <th width="25%">Reason</th>
                                     <th>Action</th>
                                     <th width="10%">Priority</th>
                                     <th width="10%">Result</th>
                                     <th width="10%"></th>
                                 </tr>
                             </thead>
                             <tbody>
                                 <tr>
                                     <td colspan="6" align="center">-- NO DATA --</td>
                                 </tr>
                             </tbody>
                         </table>
                     </div>
                 </div>
                 <div class="row" style="margin: unset; padding-bottom: 10px;">
                     <b style="font-size: 24px; margin: 5px;">TroubleShooting Guide</b>
                     <div class="row" style="text-align:center; margin: unset;"><a name="error-code" data-toggle="modal" data-target="#modal_solution_history" onclick="loadSolutionHistory()">FR00031</a> <label name="error-desc"> wifi power TX1 CH1 low value fail</label></div>
                     <div class="col-sm-6" id="chart-defected-issue" style="height: 250px;"></div>
                     <div class="col-sm-6" id="chart-ntf-issue" style="height: 250px;"></div>
                 </div>
             </div>
         </div>
    </div>
    <div class="col-12 col-lg-3 col-xl-2">
        <%@ include file="trouble-and-history.jsp" %>
    </div>
</div>

<%@ include file="modal/modal-guiding.jsp" %>
<%@ include file="modal/modal-search-solution.jsp" %>
<%@ include file="modal/modal-confirm-solution.jsp" %>
<%@ include file="modal/modal-add-new-solution.jsp" %>

<script src="/assets/js/custom/troubleshooting.js"></script>
<script>
    Highcharts.chart('chart-defected-issue', {
        chart: {
            type: 'column'
        },
        title: {
            text: 'DEFECTED ISSUE'
        },
        xAxis: {
            categories: ['NTF', 'Process issue', 'Component issue', 'Other'],
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
        tooltip: {
            headerFormat: '<b>{point.x}</b><br/>',
            pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
        },
        plotOptions: {
            column: {
                stacking: 'normal',
            }
        },
        series: [{
            name: ' ',
            data: [60, 30, 20, 2],
            marker: {
                enabled: false
            },
        }]
    });

    Highcharts.chart('chart-ntf-issue',{
        chart:{
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type:'pie'
        },
        title: {
            text: 'NTF ISSUE'
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
            data: [
                {name: 'Probe TP21 damaged', y: 55},
                {name: 'Probe TP32 damaged', y: 40},
                {name: 'Flasher hang issue', y: 15},
                {name: 'Other', y: 3}
            ]
        }],
        credits:{
            enabled:false,
        },
        legend: {
            style: {
                fontSize: '11px'
            },
            layout: 'horizontal',
            align: 'left',
            verticalAlign: 'bottom'
        }
    });
</script>
<script>
    // init
    var dataset = {
        factory: '${factory}',
        modelName: '${modelName}',
        groupName: '${groupName}',
        stationName: '${stationName}'
    };

    if (dataset.modelName != '') {
        loadModels(dataset, true, function(dataset) {
            loadGroups(dataset, true, function(dataset) {
                loadStations(dataset, true, loadSolutionList);
            });
        });
    } else {
        loadModels(dataset, false, function(dataset) {
            loadGroups(dataset, false, function(dataset) {
                loadStations(dataset, false, loadSolutionList);
            });
        });
    }

    $(document).ready(function() {
        $('select[name=modelName]').on('change', function() {
            dataset.modelName = this.value;
            loadGroups(dataset, false, function(dataset) {
                loadStations(dataset, false, loadSolutionList);
            });
        });

        $('select[name=groupName]').on('change', function() {
            dataset.groupName = this.value;
            loadStations(dataset, false, loadSolutionList);
        });

        $('select[name=stationName]').on('change', function() {
            dataset.stationName = this.value;
            loadSolutionList(dataset);
        });

        $('#confirm_solution').on('click', function(){
            confirmSolution(function(){
                $('.locked>a[data-id=' + notifyId + ']')[0].parentElement.remove();
                removeNotify(notifyId);
            });
        });
    });

    function notifyOnclick(context) {
        $("span.station-title").html(context.dataset.stationName);
        $("span.group-title").html(context.dataset.groupName);
        $("span.model-title").html(context.dataset.modelName);

        dataset = {
            type: context.dataset.type,
            notifyId: context.dataset.id,
            factory: context.dataset.factory,
            modelName: context.dataset.modelName,
            groupName: context.dataset.groupName,
            stationName: context.dataset.stationName
        };

        loadSolutionList(dataset);
    }

    function showGuiding(context) {
        $('a[name="error-code"]').html(context.dataset.errorCode);
        if (context.dataset.errorDesc == null || context.dataset.errorDesc == "") {
            $('label[name="error-desc"]').html('No error description');
        } else {
            $('label[name="error-desc"]').html(context.dataset.errorDesc);
        }
        loadTroubleAndHistory(context.dataset.modelName, context.dataset.errorCode, context.dataset.errorDesc);
    }

</script>