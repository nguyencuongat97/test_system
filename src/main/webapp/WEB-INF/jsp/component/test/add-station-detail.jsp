<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<!-- <link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" /> -->
<link rel="stylesheet" href="/assets/css/custom/station-dashboard.css" />
<!-- STATION DETAIL -->
<div class="loader"></div>
<div class="row">
    <div class="panel panel-overview" style="margin:unset; background-color:#272727; color:#fff; text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
        <span style="font-size:14px;"><span class="text-uppercase" name="factory">${factory}</span> - TESTER GROUP DETAIL MANAGEMENT</span>
    </div>
    <div class="col-sm-11">
        <div class="input-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        </div>
    </div>
    <div class="col-sm-1">
        <div style="width: 90%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;background:#ddd; color: #333;" onclick="exportActionHistories()"><i class="fa fa-download"></i></button>
        </div>
    </div>
    <div class="col-xs-12 table-responsive pre-scrollable" id="old-style" style="margin: 10px 0px; max-height: calc(100vh - 165px);">
        <table class="table table-xxs table-bordered" id="tbl-model-status" style="background-color: #fff; font-size: 16px; font-weight: 400;" >
            <thead style="background-color: #545b62; color: #fff" >
                <tr class="thead">
                    <th>No</th>
                    <th >Model Name</th>
                    <th >Group Name</th>
                    <!-- <th style="width:6%;">Plan</th> -->
                    <th >Wip</th>
                    <th >OutPut</th>
                    <!-- <th style="width:8%;">Fwrate</th> -->
                    <th class="typeRTR">F.P.R</th>
                    <th class="typeRTR">Retest Rate</th>
                    <th class="typeETE">YieldRate</th>
                    <th class="typeRTR">F.Fail</th>
                    <th >Fail</th>
                    <th style="text-align: center;" colspan="4">Error</th>
                </tr>
            </thead>
            <tbody class="thbody" >
            </tbody>
        </table>
    </div>
</div>
<!-- Modal -->
  <div class="modal fade" id="modalSetup" role="dialog">
    <div class="modal-dialog modal-xl">
      <div class="modal-content" style="background-color: #333; color: #ccc;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
            <span style="font-weight:bold">STATION DETAIL</span>
          </div>
        </div>
        <div class="modal-body">
             <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px);">
                <table id="tblModelMeta" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px; color: #fff;">
                    <thead>
                        <tr>
                            <th >STT</th>
                            <th >Model Name</th>
                            <th >Group Name</th>
                            <th >Station Name</th>
                            <!-- <th style="width:6%;">Plan</th> -->
                            <th >Wip</th>
                            <th >OutPut</th>
                            <th >F.P.R</th>
                            <th >Retest Rate</th>
                            <th >YieldRate</th>
                            <th >F.Fail</th>
                            <th >Fail</th>
                            <th style="text-align: center;" colspan="4">Error</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>           
                </table>
            </div>
        </div>
        <!-- <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal" style="font-size: 13px; background: #333333; color: #ccc">Close</button>
        </div> -->
      </div>
    </div>
  </div>
  <!-- MODAL ERROR TOP STATION -->
  <div class="modal fade" id="modalError" role="dialog">
    <div class="modal-dialog modal-xl">
      <div class="modal-content" style="background-color: #333; color: #ccc; margin: 3%; margin-top: 4%;">
        <div class="modal-header">
          <button type="button" class="close" id="close3" data-dismiss="modal" style="margin-top: -10px;"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
                    <select class=" bootstrap-select selecthtmlMN3 selectMN" name="modelName3" id="idSelectMN" data-width:="auto" >
                    </select>
                    <select class="bootstrap-select selecthtmlGN3 selectGN" name="groupName3" id="idSelectGN" data-width:="auto" >
                    </select>
                    <select class="bootstrap-select selecthtmlSN3 selectSN" name="stationName3" data-width:="auto" id="idSelectSN"  >
                    </select>
          </div>
        </div>
        <div class="modal-body">
            <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px); margin-top: -10px;">
                <table id="tblModelErrorMGS" class="table table-bordered table-xxs table-sticky" style="font-size: 12px; margin-bottom: 10px; color: #fff;">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th >Model Name</th>
                            <th >Error Code</th>
                            <th >Group Name </th>
                            <th >Station Name</th>
                            <th >Description</th>
                            <th >Intput</th>
                            <th >Output</th>
                            <th >Test F.Qty</th>
                            <th >F.Qty</th>
                            <th >F.Rate</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>           
                </table>
            </div>
        </div>
      </div>
    </div>
  </div>
   <!-- MODAL ERROR TOP GROUP-->
   <div class="modal fade" id="modalErrorGroup" role="dialog">
    <div class="modal-dialog modal-xl">
      <div class="modal-content" style="background-color: #333; color: #ccc;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
            <!-- <span style="font-weight:bold">STATION ERROR</span> -->
            <select class=" bootstrap-select selecthtmlMN2 selectMN" name="modelName2" id="idGroup1" data-width:="auto" >
            </select>
            <select class="bootstrap-select selecthtmlGN2 selectGN" name="groupName2" id="idGroup2" data-width:="auto">
            </select>
            <select class="bootstrap-select selecthtmlSN2 selectSN" name="stationName2" data-width:="auto" id="idGroup3" >
            </select>
          </div>
        </div>
        <div class="modal-body">
             <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px);">
                <table id="tblModelErrorGroup" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px; color: #fff;">
                    <thead>
                        <tr>
                            <th >STT</th>
                            <th >Model Name</th>
                            <th >Error Code</th>
                            <th >Group Name </th>
                            <th >Station Name</th>
                            <th >Description</th>
                            <th >Intput</th>
                            <th >Output</th>
                            <th >Test F.Qty</th>
                            <th >F.Qty</th>
                            <th >F.Rate</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>           
                </table>
            </div>
        </div>
      </div>
    </div>
  </div>
  <!-- MODAL ERROR CODE -->
  <div class="modal fade" id="modalErrorCode" role="dialog">
    <div class="modal-dialog modal-xl">
      <div class="modal-content" style="background-color: #333; color: #ccc; margin: 3%; margin-top: 4%;">
        <div class="modal-header">
          <button type="button" id="close-error-3" class="close" data-dismiss="modal" style="margin-top: -10px;"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
            <span style="font-weight:bold">STATION ERROR CODE</span> 
          </div>
        </div>
        <div class="modal-body">
            <div id="chart-error-code-by-tester-3" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
        </div>
      </div>
    </div>
  </div>
  <!-- MODAL ERROR CODE GROUP -->
  <div class="modal fade" id="modalErrorCodeGroup" role="dialog">
    <div class="modal-dialog modal-xl">
      <div class="modal-content" style="background-color: #333; color: #ccc; margin: 3%; margin-top: 4%;">
        <div class="modal-header">
          <button type="button" id="close-error-2" class="close" data-dismiss="modal" style="margin-top: -10px;"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
            <span style="font-weight:bold">STATION ERROR CODE</span>
            <!-- <span style="font-weight:bold">STATION ERROR CODE (ModelName: <span id="addModel"></span>, GroupName: <span id="addGroup"></span>)</span>  -->
          </div>
        </div>
        <div class="modal-body">
            <div id="chart-error-code-by-tester-2" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
        </div>
      </div>
    </div>
  </div>
<style>
.text-semibold{
    font-weight: 550;
}
.errortop {
    width: 6%;
    /* font-size: 13px; */
}
.btn-group button{
    margin-right: 10px;
    border-radius: 5px !important;
    border: 1px solid #ccc;
    /* background: #fff; */
    color: #333;
    padding: 5px !important;
}
.styletextshad{
    color: #333;
    text-shadow: 2px 1px 2px #756d6d;
}
.selectMN{
    background: #fff ;
    color: #333 !important;
    width: 13% !important;
    border-radius: 3px;
}
.selectGN{
    background: #fff ;
    color: #333 !important;
    width: 12% !important;
    border-radius: 3px;
}
.selectSN{
    background: #fff ;
    color: #333 !important;
    width: 12% !important;
    border-radius: 3px;
}
.table-sticky thead th {
    border-top: none !important;
    border-bottom: none !important;
    box-shadow: inset 0 1px 1px #fff, inset 0 -1px 0 #fff;
}
#tblModelMeta thead th, #tblModelErrorGroup thead th{
    /* color: #3f4448; */
    background-color: #545b62;
    position: sticky !important;
    text-transform: uppercase !important;
    padding: 5px 5px !important;
}
#tblModelErrorMGS thead th, #tblModelErrorGroup thead th, #tblModelErrorStation thead th {
    background-color: #424242;
    text-transform: uppercase;
    position: sticky !important;
    text-transform: uppercase !important;
    padding: 5px 5px;
}
#tblModelMeta tbody{
    /* background: #cacaca; */
    background: #fff;
    color: #333;
    font-size: 13px;
}
#tblModelErrorGroup tbody{
    background: #fff;
    color: #333;
}
#tblModelErrorMGS tbody, #tblModelErrorGroup tbody, #tblModelErrorStation tbody{
    background: #fff;
    color: #333;
}
#tbl-model-status thead th{
    background-color: #545b62 !important;
    color: #fff;
    position: sticky !important;
    text-transform: uppercase !important;
    padding: 3px !important;
}
.table-xxs > tbody > tr > td, .table-xxs > tfoot > tr > td {
    padding: 3px !important;
}
.class-group-name{
    color: #333;
}
.caret:after{
    padding-right: 5px;
}
.selectMN .btn span{
    padding-left: 5px;
}
.selectGN .btn span{
    padding-left: 5px;
}
.selectSN .btn span{
    padding-left: 5px;
}
.selectMN .btn span.bs-caret .caret{
    padding-left: 5px;
    margin-right: 5px;
}
.selectGN .btn span.bs-caret .caret{
    padding-left: 5px;
    margin-right: 5px;
}
.selectSN .btn span.bs-caret .caret{
    padding-left: 5px;
    margin-right: 5px;
}
.selectSN .btn span.bs-caret .caret{
   margin-right: 5px;
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
</style>
<script>
    var dataset = {
        factory: '${factory}',
        timeSpan: '${timeSpan}',
        timeout: {},
        modelName: '${modelName}',
        groupName: '${groupName}',
        stationName: '${stationName}',
        selectedModel: 'all',
        status: 'all',
        style: 'new-style',
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
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    function searchParams(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if (results == null){
            return null;
        }
        else {
            return decodeURIComponent(results[1]) || 0;
        }
    }
    function getParameter(){
        var urlString = window.location.href;
        var timeSpan = searchParams("timeSpan");
      
        if(timeSpan == undefined || timeSpan == null){
            getTimeNow();
        }
        else{
            var time = timeSpan.split(' - ');
            $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(time[0]));
            $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(time[1]));
            dataset.timeSpan = timeSpan ;
            loadModelList();
        }
    }
$(document).ready(function() {
    getParameter();
    $('input[name=timeSpan]').on('change', function() {
        dataset.timeSpan = this.value;
        loadModelList();
    });
});
</script>
<!-- <script src="/assets/js/custom/common.js"></script> -->
<script src="/assets/js/custom/add-station-error.js"></script>

