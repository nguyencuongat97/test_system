<!-- STATION ERROR -->
<link rel="stylesheet" href="/assets/css/custom/customer-dashboard.css" />
<link rel="stylesheet" href="/assets/css/custom/station-error.css">
<link rel="stylesheet" href="/assets/css/custom/style.css">
<script src="/assets/js/custom/jquery-ui.js"></script>

<div class="loader"></div>
<div class="row">
    <div class="panel panel-overview" style="margin:unset; background-color:#272727; color:#fff; text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
        <span style="font-size:14px;"><span class="text-uppercase" name="factory">${factory}</span> - TESTER STATUS MANAGEMENT</span>
    </div>
    <div class="col-sm-11">
        <div class="input-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        </div>
    </div>
    <div class="col-sm-1">
        <div style="width: 30%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;" onclick="exportActionHistories()"><i class="fa fa-download"></i></button>
        </div>
        <div style="width: 30%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;" onclick="changeStyle()"><i class="fa fa-exchange"></i></button>
        </div>
        <div style="width: 30%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;" ><i class="fa fa-gear"></i></button>
        </div>
    </div>
    <div class="" id="new-style">
        <div class="col-lg-12">
            <table id="progress-bar">
                <tr>
                    <!--
                    <td class="tester-normal" width="44%">44% <span class="pull-right">50/112</span></td>
                    <td class="tester-issue" width="28%">28% <span class="pull-right">32/112</span></td>
                    <td class="tester-warning" width="14%">14% <span class="pull-right">15/112</span></td>
                    <td class="tester-idle">14% <span class="pull-right">15/112</span></td>
                    -->
                </tr>
            </table>
        </div>
        <div class="col-lg-12" style="height: 45px; ">
            <!-- <div class="btn-group mds scrolling-wrapper-flexbox" data-toggle="buttons" style="padding: 5px 0px; overflow-x: scroll; overflow-y: auto; white-space: nowrap;" onchange="showByModel()"></div> -->
            <div class="btn-group mds scrolling-wrapper-flexbox" data-toggle="buttons" style="padding: 5px 0px; white-space: nowrap;" onchange="showByModel()"></div>
        </div>

        <div style="margin: 10px 10px 5px 10px; font-weight:bold; color: #fff; font-size: 14px">Tester Issue (<span name="tester-issue"></span>)</div>
        <div id="station-locked" class="row scrolling-wrapper-flexbox" style="margin: 0 10px; max-height: 260px;overflow-x: scroll; white-space: nowrap;"></div>

        <div style="margin: 10px 10px 5px 10px; font-weight:bold; color: #fff; font-size: 14px">Tester Warning (<span name="tester-warning"></span>)</div>
        <div id="station-warning" class="row scrolling-wrapper-flexbox" style="margin: 0 10px; max-height: 260px; overflow-x: scroll; white-space: nowrap;"></div>

        <div style="margin: 10px 10px 5px 10px; font-weight:bold; color: #fff; font-size: 14px">Tester Idle (<span name="tester-idle"></span>)</div>
        <div id="station-idle" class="row scrolling-wrapper-flexbox" style="margin: 0 10px; max-height: 260px;overflow-x: scroll; white-space: nowrap;"></div>

        <!--
        <div style="margin: 10px 10px 5px 10px; font-weight:bold; color: #fff; font-size: 14px">Tester Normal (<span name="tester-normal"></span>)</div>
        <div id="station-normal" class="row scrolling-wrapper-flexbox" style="margin: unset; max-height: 260px;overflow-x: scroll;overflow-y: ;white-space: nowrap;"></div>
        -->
    </div>
    <div class="col-xs-12 table-responsive pre-scrollable hidden" id="old-style" style="margin: 10px 0px; max-height: calc(100vh - 165px);">
        <table class="table table-xxs table-bordered text-nowrap table-sticky" id="tbl-model-status" style="background-color: #fff; font-size: 20px; font-weight: 400;" >
                <thead style="background-color: #545b62; color: #fff" >
                    <tr class="thead">
                        <th width="45px">No</th>
                        <th width="155px">Model</th>
                        <th>Plan</th>
                        <th>Input</th>
                        <th style="width:8%;">Output</th>
                        <th width="14%">Month OutPut</th>
                        <th width="9%">Hit Rate</th>
                        <th>E.T.E</th>
                        <!-- <th>F.P.Y</th> -->
                        <th width="11%" >Retest Rate</th>
                        <!-- <th>Retest</th>
                        <th>R.Fail</th> -->
                        <th>F.Fail</th>
                        <th>Fail</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
        </table>
    </div>
</div>
<!-- Modal -->
<div id="mydiv" style="font-size: 18px; min-width: 740px;">
        <div id="mydivheader" style="background: #6c757d;" >
          <span style="font-weight:bold">SET PLAN</span>
          <a title="Close" style="float: right; margin-right: 5px; color: #fff;" onclick="closeModal()" >
              <i class="icon icon-cross"></i>
          </a>
        </div>
        <div class="row" style="color: #333333; margin: 0; padding: 5px;">
          <button id="btnAddNew" class="btn btn-lg" style="display: none;">
              <!-- <i class="fa fa-plus"></i> Add -->
          </button>
        </div>
        <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 243px);">
              <table id="tblTarget" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px;">
                  <thead>
                        <tr>
                            <!-- <th style="width: 5%; text-align: center">STT</th> -->
                            <th style="width: 21%; text-align: center">Model Name</th>
                            <th style="width: 12%; text-align: center">Plan</th>
                            <th style="width: 10%; text-align: center">Mo</th>
                            <th style="width: 12%; text-align: center">Shift</th>
                            <th style="width: 21%; text-align: center">Shift Time</th>
                            <th style="width: 34%; text-align: center">Action</th>
                        </tr>
                  </thead>
                  <tbody>
                  </tbody>           
              </table>
          </div>
          <div id="mydivfooter" style="text-align: right;">
          </div>
      </div>
<style>
    #tbl-model-status thead tr th {
        background-color: #545b62 !important;
        color: #fff;
        /* text-align: center; */
        position: sticky !important;
        text-transform: uppercase !important;
    }
    .table-sticky tr th:nth-of-type(1) {
        padding-left: 10px;
    }
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
    /* #mydivheader{
        cursor: move;
    } */

</style>
<div class="panel-notify-detail">
    <button type="button" class="close" style="position: absolute;right: 10px;top: 10px;z-index: 1;" onclick="hiddenPanelRight();"><i class="icon-cross"></i></button>
    <%@ include file="../../component/test/trouble-and-history.jsp" %>
</div>

<%@ include file="../../component/test/modal/modal-guiding.jsp" %>

<%@ include file="../../component/test/modal/modal-search-solution.jsp" %>
<%@ include file="../../component/test/modal/modal-confirm-solution.jsp" %>
<%@ include file="../../component/test/modal/modal-add-new-solution.jsp" %>

<%@ include file="../../component/test/modal/modal-station-detail.jsp" %>
<%@ include file="../../component/test/modal/modal-history-action.jsp" %>

<script src="/assets/js/customerjs/customer-station-error.js"></script>
<script src="/assets/js/customerjs/customer-notify-list.js"></script>
<script src="/assets/js/custom/troubleshooting.js"></script>

<script>
    // init
    var dataset = {
        factory: '${factory}',
        timeout: {},
        selectedModel: 'all',
        status: 'all',
        style: 'new-style'
    };


    loadModelList();
    loadStatus(dataset.factory);
    loadProgressStatus(dataset.factory);

//    $.ajax({
//        type: "GET",
//        url: "/api/test/notification",
//        data: {
//            factory: '${factory}'
//        },
//        contentType: "application/json; charset=utf-8",
//        success: function(data){
//            if (!$.isEmptyObject(data)) {
//                dataset.latestNotifyId = data[0].id;
//                getNewestNotification('${factory}', dataset.latestNotifyId);
//            }
//        },
//        failure: function(errMsg) {
//            console.log(errMsg);
//        }
//  });

//    function getNewestNotification(factory, latestNotifyId) {
//        $.ajax({
//            type: "GET",
//            url: "/api/test/newestNotification",
//            data: {
//                factory: factory,
//                latestNotifyId: latestNotifyId
//            },
//            contentType: "application/json; charset=utf-8",
//            success: function(data){
//                if (!$.isEmptyObject(data)) {
//                    $.notify({
//                        title: '<strong>'+data[0].stationName+'</strong>',
//                        message: data[0].detail,
//                        url: "http://10.224.81.70:8888/station-detail" + "?factory=" + data[0].factory + "&modelName=" + data[0].modelName + "&groupName=" + data[0].groupName + "&stationName=" + data[0].stationName,
//                        target: '_blank'
//                    },{
//                        type: 'warning',
//                        placement: {
//                            from: "bottom",
//                            align: "right"
//                        },
//                    });
//                    dataset.latestNotifyId = data[0].id;
//                }
//            },
//            failure: function(errMsg) {
//                console.log(errMsg);
//            },
//             complete: function() {
//                 setTimeout(getNewestNotification, 30000, factory, dataset.latestNotifyId);
//             }
//        });
//    }

    function showPanelRight() {
        $('.panel-notify-detail').addClass("show");
    }
    function hiddenPanelRight() {
        $('.panel-notify-detail').removeClass("show");
    }

    function notifyOnclick(context) {
        var contextData = $(context).parents(".stt").get(0);
        dataset.modelName = contextData.dataset.modelName;
        dataset.groupName = contextData.dataset.groupName;
        dataset.stationName = contextData.dataset.stationName;

        loadActionHistories();
        delete contextData;
    }

    function exportActionHistories() {
        var url = "/api/test/tracking/history/export?factory=" + dataset.factory + "&timeSpan=" + dataset.timeSpan;
        hrefWindow(url);
    }

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

                dataset.timeSpan = $('input[name="timeSpan"]').val();

                $('input[name=timeSpan]').on('change', function(event) {
                    dataset.timeSpan = event.target.value;
                    loadModelList();
                    loadStatus(dataset.factory, dataset.timeSpan);
                    if (dataset.selectedModel == 'all') {
                        loadProgressStatus(dataset.factory, '', dataset.timeSpan);
                    } else {
                        loadProgressStatus(dataset.factory, dataset.selectedModel, dataset.timeSpan);
                    }
                });

                delete current;
                delete startDate;
                delete endDate;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    getTimeNow();

</script>

<!-- /STATION ERROR -->