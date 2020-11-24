<!-- STATION DETAIL -->
<div class="loader"></div>
<div class="row" style="background-color: #fff;">
    <div class="col-12 col-lg-9 col-xl-10">
        <div class="input-group" style="margin: 10px 0px 10px 0px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        </div>
        <div class="panel panel-overview" style="margin-bottom: unset; padding: 1px 15px;">
            <div class="row" style="position: relative;">
                <div class="col-xs-12 col-sm-4">
                    <select class="form-control bootstrap-select" name="modelName" >
                    </select>
                </div>
                <div class="col-xs-12 col-sm-4">
                    <select class="form-control bootstrap-select" name="groupName" >
                    </select>
                </div>
                <div class="col-xs-12 col-sm-4">
                    <select class="form-control bootstrap-select" name="stationName" >
                    </select>
                </div>
            </div>
        </div>

        <div class="row" style="margin: unset;">
            <div class="col-xs-12 col-sm-6 chart-sm" id="chart-group-overview" style="margin: 10px 0px;"></div>
            <div class="col-xs-12 col-sm-6 chart-sm" id="chart-group-name" style="margin: 10px 0px;" ></div>

            
            <hr>
            <!-- text-nowrap -->
            <div class="col-xs-12 table-responsive pre-scrollable" style="padding: unset; margin: 10px 0px; max-height: 166px;">
                <table class="table table-xxs table-bordered text-nowrap" id="tbl-top-3-error" >
                    <thead class="bg-primary-400">
                        <tr class="thead">
                            <th style="max-width: 45px;">No</th>
                            <th style="max-width: 14%;">Error</th>
                            <th style="max-width: 27%;">Root Cause</th>
                            <th style="max-width: 26%;">Action</th>
                            <th style="max-width: 17%;">Description</th>
                            <th>Input</th>
                            <th>Pass</th>
                            <th style="padding: 0px 5px;">Test F.Qty</th>
                            <th style="padding: 0px 11px;">F.Qty</th>
                            <th>F.Rate</th>
                            <th style="max-width: 11%;">Event</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>

            <div class="col-xs-12 table-responsive pre-scrollable" style="padding: unset; margin: 10px 0px; max-height: 166px;">
                <table class="table table-xxs table-bordered text-nowrap" id="tbl-error-history" >
                    <thead class="bg-primary-400">
                        <tr class="thead">
                            <th width="45px">No</th>
                            <th>Time</th>
                            <th>Station Name</th>
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
            <hr>

            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-7" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-8" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-5" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-6" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-9" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-1" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-10" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-2" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-4" ></div>
            <div class="col-xs-12 col-sm-6 col-lg-6 chart-sm" id="chart-content-3" ></div>
        </div>
    </div>
    <div class="col-12 col-lg-3 col-xl-2">
        <%@ include file="trouble-and-history.jsp" %>
    </div>
</div>
<style>
    .loader{
        display: none;
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
    /* span.classRootCause{
        width: 10px !important;
    } */
   
</style>
<%@ include file="modal/modal-guiding.jsp" %>
<%@ include file="modal/modal-serial-tracking.jsp" %>

<%@ include file="modal/modal-search-solution.jsp" %>
<%@ include file="modal/modal-confirm-solution.jsp" %>
<%@ include file="modal/modal-add-new-solution.jsp" %>

<script src="/assets/js/custom/common.js"></script>
<script src="/assets/js/custom/station-detail.js"></script>
<script src="/assets/js/custom/troubleshooting.js"></script>

<script>
    // init
    var retestRateProps = {
        id: 'chart-content-1',
        title: 'Retest Rate By Hour',
        type: 'line',
        name: 'Retest Rate',
        unit: '%',
        errorValue: 3,
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}%</b> <br/> Input: <b>{point.wip}</b> <br/> First Fail: <b>{point.firstFail}</b> <br/> Second Fail: <b>{point.secondFail}</b>'
    };
    var graph1 = initGraph(retestRateProps);

    var retestRateWeeklyProps = {
        id: 'chart-content-9',
        title: 'Retest Rate By Week',
        type: 'line',
        name: 'Retest Rate',
        unit: '%',
        errorValue: 3,
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}%</b> <br/> Input: <b>{point.wip}</b> <br/> First Fail: <b>{point.firstFail}</b> <br/> Second Fail: <b>{point.secondFail}</b>'
    };
    var graph9 = initGraph(retestRateWeeklyProps);
    // var retestRateWeeklyProps = {
    //     id: 'chart-group-name',
    //     title: 'Retest Rate Week By  All Group Name',
    //     type: 'line',
    //     name: 'Retest Rate',
    //     unit: '%',
    //     errorValue: 3,
    //     tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}%</b> <br/> Input: <b>{point.wip}</b> <br/> First Fail: <b>{point.firstFail}</b> <br/> Second Fail: <b>{point.secondFail}</b>'
    // };
    // var graph9 = initGraph(retestRateWeeklyProps);

    var firstPassRateProps = {
        id: 'chart-content-2',
        title: 'First Pass Rate By Hour',
        type: 'line',
        name: 'First Pass Rate',
        unit: '%',
        errorValue: 97,
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}%</b> <br/> Input: <b>{point.wip}</b> <br/> First Fail: <b>{point.firstFail}</b>'
    };
    var graph2 = initGraph(firstPassRateProps);

    var firstPassRateWeeklyProps = {
        id: 'chart-content-10',
        title: 'First Pass Rate By Week',
        type: 'line',
        name: 'First Pass Rate',
        unit: '%',
        errorValue: 97,
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}%</b> <br/> Input: <b>{point.wip}</b> <br/> First Fail: <b>{point.firstFail}</b>'
    };
    var graph10 = initGraph(firstPassRateWeeklyProps);

    var hitRateProps = {
        id: 'chart-content-3',
        title: 'Hit Rate By Hour',
        type: 'line',
        name: 'Hit Rate',
        unit: '%',
        errorValue: 95,
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}%</b> <br/> Input: <b>{point.wip}</b> <br/> Plan: <b>{point.plan}</b>'
    };
    var graph3 = initGraph(hitRateProps);

    var hitRateWeeklyProps = {
        id: 'chart-content-4',
        title: 'Hit Rate By Week',
        type: 'line',
        name: 'Hit Rate',
        unit: '%',
        errorValue: 95,
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}%</b> <br/> Input: <b>{point.wip}</b> <br/> Plan: <b>{point.plan}</b>'
    };
    var graph4 = initGraph(hitRateWeeklyProps);

    var errorByCodeProps = {
        id: 'chart-content-5',
        title: 'Top 3 error code issue by week',
        type: 'column',
        name: 'Test Fail Quality',
        unit: '',
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> <br/> Fail: <b>{point.fail}</b>'
    };
    var graph5 = initGraph(errorByCodeProps);

    var errorByTimeProps = {
        id: 'chart-content-6',
        title: 'Top 3 error code issue by hour',
        type: 'line',
        name: 'Test Fail Quality',
        unit: '',
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> <br/> Fail: <b>{point.fail}</b>'
    };
    var graph6 = initGraph(errorByTimeProps);

    var errorTop1ByWeek = {
        id: 'chart-content-7',
        title: 'Top 1 error code issue by week',
        type: 'line',
        name: 'Test Fail Quality',
        unit: '',
        tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b>'
    }
    var graph7 = initGraph(errorTop1ByWeek);

    var errorTop1ByHour = {
            id: 'chart-content-8',
            title: 'Top 1 error code issue by hour',
            type: 'line',
            name: 'Test Fail Quality',
            unit: '',
            tooltipFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b>'
        }
    var graph8 = initGraph(errorTop1ByHour);

    function loadChart(dataset) {
        var timeSpan = $('input[name="timeSpan"]').val();
        dataset.timeSpan = timeSpan;

        loadData(dataset,
                 graph1, retestRateProps,
                 graph2, firstPassRateProps,
                 graph3, hitRateProps,
                 graph4, hitRateWeeklyProps,
                 graph5, errorByCodeProps,
                 graph6, errorByTimeProps,
                 graph7, errorTop1ByWeek,
                 graph8, errorTop1ByHour,
                 graph9, retestRateWeeklyProps,
                 graph10, firstPassRateWeeklyProps);
    }

    // init
    var dataset = {
        factory: '${factory}',
        modelName: '${modelName}',
        groupName: '${groupName}',
        stationName: '${stationName}',
        timeSpan: '${timeSpan}'
    };

    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));

                init();
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function init() {
        if (dataset.modelName != '') {
            loadModels(dataset, true, function(dataset) {
                loadGroups(dataset, dataset.groupName != '', function(dataset) {
                    loadStations(dataset, dataset.stationName != '', loadChart, true);
                });
                loadGroupOverview();
                loadHighChartWeekAllGroupName();
            });
        } else {
            loadModels(dataset, false, function(dataset) {
                loadGroups(dataset, false, function(dataset) {
                    loadStations(dataset, false, loadChart, true);
                });
                loadGroupOverview();
                loadHighChartWeekAllGroupName();
            });
        }

        $('select[name=modelName]').on('change', function() {
            dataset.modelName = this.value;
            dataset.errorCode = null;
            dataset.errorDesc = null;

            loadGroups(dataset, false, function(dataset) {
                loadStations(dataset, false, loadChart, true);
            });
            loadGroupOverview();
            //loadGroupAllGroupName();
            loadHighChartWeekAllGroupName();
        });

        $('select[name=groupName]').on('change', function() {
            dataset.groupName = this.value;
            dataset.errorCode = null;
            dataset.errorDesc = null;

            loadStations(dataset, false, loadChart, true);
        });

        $('select[name=stationName]').on('change', function() {
            dataset.stationName = this.value;
            dataset.errorCode = null;
            dataset.errorDesc = null;

            loadChart(dataset);
        });

        $('input[name=timeSpan]').on('change', function() {
            dataset.timeSpan = this.value;
            dataset.errorCode = null;
            dataset.errorDesc = null;

            loadModels(dataset, true, function(dataset) {
                loadGroups(dataset, true, function(dataset) {
                    loadStations(dataset, true, loadChart, true);
                });
            });
            loadGroupOverview();
            //loadGroupAllGroupName();
            loadHighChartWeekAllGroupName();

        });
    }

    $(document).ready(function() {
        var defaultTimeSpan = true;
        if (dataset.timeSpan != null && dataset.timeSpan != '') {
            var time = dataset.timeSpan.split(' - ');
            if (time.length = 2) {
                defaultTimeSpan = false;
                $('input[name="timeSpan"]').data('daterangepicker').setStartDate(moment(time[0], "YYYY/MM/DD HH:mm"));
                $('input[name="timeSpan"]').data('daterangepicker').setEndDate(moment(time[1], "YYYY/MM/DD HH:mm"));
            }
            init();
        }
        if (defaultTimeSpan) {
            getTimeNow();
        }
    });

    function notifyOnclick(context) {
        dataset = {
             type: context.dataset.type,
             notifyId: context.dataset.id,
             factory: context.dataset.factory,
             modelName: context.dataset.modelName,
             groupName: context.dataset.groupName,
             stationName: context.dataset.stationName,
             errorCode: "",
             errorDesc: ""
         };

        $('select[name=modelName]').val(dataset.modelName);
        loadGroups(dataset, true, function(dataset) {
            loadGroups(dataset, true, function(dataset) {
                loadStations(dataset, true, loadChart, true);
            });
        });
    }

    function showHistory(context) {
        var modelName = $('select[name=modelName]').val();
        var groupName = $('select[name=groupName]').val();
        var stationName = $('select[name=stationName]').val();

        dataset.errorCode = context.dataset.errorCode;
        dataset.errorDesc = context.dataset.errorDesc;

        var requestData = {
            factory: dataset.factory,
            modelName: modelName,
            groupName: groupName,
            stationName: stationName,
            errorCode: dataset.errorCode,
            timeSpan: dataset.timeSpan
        }

        loadDataTableErrorHistoryFromJson(requestData);
        loadTroubleAndHistory();

        loadChartErrorHourly(requestData, graph8, errorTop1ByHour);
        loadChartErrorWeekly(requestData, graph7, errorTop1ByWeek);

    }

</script>

<!-- /STATION DETAIL -->