<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/customer-dashboard.css" />

<link rel="stylesheet" href="/assets/css/custom/style.css">
<script src="/assets/js/custom/jquery-ui.js"></script>
<!-- STATION DASHBOARD -->
<div class="loader"></div>
<div class="row">
<div class="col-lg-12" style="background: #272727;">
    <div class="panel panel-overview" style="margin:unset; background-color:#333; color:#ccc; text-align:center;">
        <span style="font-size:18px;"><span class="text-uppercase" name="factory">${factory}</span> - TESTER STATUS MANAGEMENT DASHBOARD</span>
    </div>
    <div style="width: 91%; float: left;">
        <div class="input-group" style="background-color: #333; color: #fff; height: 26px; margin: 5px 0px;border-radius: 3px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style=" border-bottom:none; height: 26px; color: inherit;">
        </div>
    </div>
    <div style="width: 8.5%; float: right;">
        <div style="width: 100%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;background:#444; color: #fff;" onclick="exportActionHistories()"><i class="fa fa-download"></i></button>
        </div>
        <!-- <div style="width: 30%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;" onclick="changeStyle()"><i class="fa fa-exchange"></i></button>
        </div>
        <div style="width: 30%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;" ><i class="fa fa-gear"></i></button>
        </div> -->
    </div>
    <div class="col-xs-12" id="old-style" style="margin: 10px 0px; padding: unset;">
        <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
            <table class="table table-xxs table-bordered text-nowrap table-sticky" id="tbl-model-status" style="background-color: #fff; font-size: 16px; font-weight: 400;" >
                <thead style="background-color: #545b62; color: #fff" >
                    <tr class="thead">
                        <th> No </th>
                        <th style="padding-left: 10px;">Model</th>
                        <th style="padding-left: 10px;">Ubee Model</th>
                        <th> Plan </th>
                        <th> Input </th>
                        <th> Output </th>
                        <th> Month OutPut </th>
                        <th> Hit Rate </th>
                        <th> E.T.E </th>
                        <!-- <th>F.P.Y</th> -->
                        <th > Retest Rate </th>
                        <!-- <th>Retest</th>
                        <th>R.Fail</th> -->
                        <th> F.Fail </th>
                        <th> Fail </th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>


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
    <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 200px);">
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
#tbl-model-status tr th{
    background-color: #545b62 !important;
    text-transform: uppercase !important;
    position: sticky;
    color: #fff;
    padding: 5px;
    /* text-align: center; */
}
#tbl-model-status>td{
    padding: 5px;
}
.table-sticky thead td {
    border-top: none !important;
    border-bottom: none !important;
    box-shadow: inset 0 1px 0 #fff,
                inset 0 -1px 0 #fff;
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

<script src="/assets/js/customerjs/customer-station-error.js"></script>
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

    $(document).ready(function() {
        var spcTS = getTimeSpan();
        $('input[name="timeSpan"]').data('daterangepicker').setStartDate(spcTS.startDate);
        $('input[name="timeSpan"]').data('daterangepicker').setEndDate(spcTS.endDate);
        dataset.timeSpan = $('input[name="timeSpan"]').val();

        $('input[name=timeSpan]').on('change', function(event) {
            dataset.timeSpan = event.target.value;
            loadModelList();
            // loadStatus(dataset.factory, dataset.timeSpan);
            // if (dataset.selectedModel == 'all') {
            //     loadProgressStatus(dataset.factory, '', dataset.timeSpan);
            // } else {
            //     loadProgressStatus(dataset.factory, dataset.selectedModel, dataset.timeSpan);
            // }
        });
    });
</script>