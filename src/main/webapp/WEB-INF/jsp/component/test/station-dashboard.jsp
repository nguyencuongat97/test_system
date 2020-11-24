<!-- <link rel="stylesheet" href="/assets/custom/css/nbb/style.css" /> -->
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/css/custom/station-dashboard.css" />
<!-- STATION DASHBOARD -->
<div class="loader"></div>
<div class="row">
    <div class="panel panel-overview" style="margin:unset; background-color:#272727; color:#fff; text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
        <span style="font-size:14px;"><span class="text-uppercase" name="factory">${factory}</span> - TESTER STATUS MANAGEMENT DASHBOARD</span>
    </div>
    <div class="col-sm-7">
        <div class="input-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        </div>
    </div>
    <div class="col-sm-2" style="margin: 5px 0px;">
        <select class="form-control bootstrap-select" data-style="btn-xs" name="customerName" id="selectCustomer">
        </select>
    </div>
    <div class="col-sm-2" style="margin: 5px 0px;">
        <select class="form-control bootstrap-select" data-style="btn-xs" name="moType">
            <option value="">ALL</option>
            <option value="NORMAL">NORMAL</option>
            <option value="REWORK">REWORK</option>
            <option value="NPI">NPI</option>
            <option value="CONTROL_RUN">CONTROL RUN</option>
        </select>
    </div>
    <div class="col-sm-1">
        <div style="width: 100%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;background:#ddd; color: #333;" onclick="exportActionHistories()"><i class="fa fa-download"></i></button>
        </div>
        <!-- <div style="width: 30%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;" onclick="changeStyle()"><i class="fa fa-exchange"></i></button>
        </div>
        <div style="width: 30%; display: inline-block;">
            <button class="btn btn-xs" style="width: 100%; margin: 5px 0px;" ><i class="fa fa-gear"></i></button>
        </div> -->
    </div>
    <div class="col-xs-12" id="old-style" style="margin: 10px 0px;">
        <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
            <table class="table table-xxs table-bordered table-sticky" id="tbl-model-status" style="background-color: #fff; font-size: 15px; font-weight: 400;" >
                <thead style="background-color: #545b62; color: #fff" >
                    <tr class="thead">
                        <th> No </th>
                        <th>Customer</th>
                        <th>Model</th>
                        <th>Plan</th>
                        <th>M.Plan</th>
                        <th>Input</th>
                        <th>Output</th>
                        <th>M.OutPut</th>
                        <th>Hit Rate</th>
                        <th>M.Hit Rate</th>
                        <th>E.T.E</th>
                        <th>First.P.Y</th>
                        <th>Retest Rate</th>
                        <!-- <th>Retest</th>
                        <th>R.Fail</th> -->
                        <th>F.Fail</th>
                        <th>Fail</th>
                        <!-- <th colspan="4" style="text-align: center;">Error</th> -->
                    </tr>
                </thead>
                <tbody>
                </tbody>
                <tfoot>
                </tfoot>
            </table>
        </div>
    </div>
</div>
<!-- Modal -->
<div id="mydiv" style="font-size: 18px; min-width: 800px;">
    <div id="mydivheader" style="background: #6c757d;" >
        <button id="btnAddNewPlan" class="btn btn-lg" style="float: left; padding: 3px 10px; background: #6c757d; color: #fff; border: 1px solid #fff;" onclick="addNewPlanClick()">
            <i class="fa fa-plus"></i>
        </button>
        <span style="font-weight:bold">SET PLAN</span>
        <a title="Close" style="float: right; margin-right: 5px; color: #fff;" onclick="closeModal()" >
            <i class="icon icon-cross"></i>
        </a>
    </div>
    <div class="row" style="color: #333333; margin: 0; padding: 5px;">
        <span id="modelAddPlan" class="hidden"></span>
    </div>
    <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 290px);">
        <table id="tblTarget" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px;">
            <thead>
                <tr>
                    <th style="padding-left: 10px; background-color: #eeeeee;">Model Name</th>
                    <th style="padding-left: 10px; background-color: #eeeeee;">Plan</th>
                    <th style="padding-left: 10px; background-color: #eeeeee;">Mo</th>
                    <th style="padding-left: 10px; background-color: #eeeeee;">Shift</th>
                    <th style="padding-left: 10px; background-color: #eeeeee;">Shift Time</th>
                    <th style="padding-left: 10px; background-color: #eeeeee; z-index: 1;">Action</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
    <div id="mydivfooter" style="text-align: right;">
    </div>
</div>
<!-- <div class="modal fade" id="modalSetPlan" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content" style="background-color: #333; color: #ccc;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
            <button id="btnAddNewPlan" class="btn btn-lg" style="float: left; padding: 3px 10px; background: #6c757d; color: #fff; border: 1px solid #fff;" onclick="addNewPlanClick()">
                <i class="fa fa-plus"></i>
            </button>
            <span style="font-weight:bold">SET PLAN</span>
            <a title="Close" style="float: right; margin-right: 5px; color: #fff;" onclick="closeModal()" >
                <i class="icon icon-cross"></i>
            </a>
          </div>
        </div>
        <div class="modal-body">
             <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px);">
                <table id="tblModelErrorModel" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px; color: #fff;">
                    <thead>
                        <tr>
                            <th style=" padding-left: 7px;">STT</th>
                            <th style="padding-left: 10px;width: 11%;">Model Name</th>
                            <th style="padding-left: 10px;">Error Code</th>
                            <th style="padding-left: 10px;width: 11%;">Group Name </th>
                            <th style="padding-left: 10px;width: 12%;">Station Name</th>
                            <th style="padding-left: 10px;width: 10%;">Description</th>
                            <th style="padding-left: 10px;">Intput</th>
                            <th style="padding-left: 10px;">Output</th>
                            <th style="padding-left: 10px;">Test F.Qty</th>
                            <th style="padding-left: 10px;">F.Qty</th>
                            <th style="padding-left: 10px;">F.Rate</th>
                        </tr>
                    </thead>
                    <tbody>

                    </tbody>
                </table>
            </div>
        </div>
      </div>
    </div>
  </div> -->
   <!-- MODAL ERROR -->
   <div class="modal fade" id="modalErrorModel" role="dialog">
    <div class="modal-dialog modal-xl">
      <div class="modal-content" style="background-color: #333; color: #ccc;">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
            <!-- <span style="font-weight:bold">STATION ERROR</span> -->
            <select class=" bootstrap-select SelecthtmlMN selectMN" name="modelName" id="idSelectMN1" data-width:="auto" ></select>
            <select class="bootstrap-select SelecthtmlGN selectGN" name="groupName" id="idSelectGN1" data-width:="auto" ></select>
            <select class="bootstrap-select SelecthtmlSN  selectSN" name="stationName" data-width:="auto" id="idSelectSN1" ></select>
          </div>
        </div>
        <div class="modal-body">
             <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px);">
                <table id="tblModelErrorModel" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px; color: #fff;">
                    <thead>
                        <tr>
                            <th style=" padding-left: 7px;">STT</th>
                            <th style="padding-left: 10px;width: 11%;">Model Name</th>
                            <th style="padding-left: 10px;">Error Code</th>
                            <th style="padding-left: 10px;width: 11%;">Group Name </th>
                            <th style="padding-left: 10px;width: 12%;">Station Name</th>
                            <th style="padding-left: 10px;width: 10%;">Description</th>
                            <th style="padding-left: 10px;">Intput</th>
                            <th style="padding-left: 10px;">Output</th>
                            <th style="padding-left: 10px;">Test F.Qty</th>
                            <th style="padding-left: 10px;">F.Qty</th>
                            <th style="padding-left: 10px;">F.Rate</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
      </div>
    </div>
  </div>
   <!-- MODAL ERROR CODE -->
   <div class="modal fade" id="modalErrorCode1" role="dialog">
    <div class="modal-dialog modal-xl">
      <div class="modal-content" style="background-color: #333; color: #ccc; margin: 3%; margin-top: 4%;">
        <div class="modal-header">
          <button type="button" id="close-error" class="close" data-dismiss="modal" style="margin-top: -10px;"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
            <span style="font-weight:bold">STATION ERROR CODE</span> 
            <!-- <span style="font-weight:bold">STATION ERROR CODE (ModelName: <span id="addModel"></span>, GroupName: <span id="addGroup"></span>)</span>  -->
          </div>
        </div>
        <div class="modal-body">
            <div id="chart-error-code-by-tester" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
        </div>
      </div>
    </div>
  </div>
<style>
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
#tbl-model-status thead th{
    background-color: #545b62 !important;
    text-transform: uppercase !important;
    position: sticky;
    color: #fff;
    padding: 5px 5px;
    cursor: pointer;
    /* text-align: center; */
}
#tbl-model-status tbody td{
    padding: 5px;
}
.table-sticky thead td {
    border-top: none !important;
    border-bottom: none !important;
    box-shadow: inset 0 1px 0 #fff,
                inset 0 -1px 0 #fff;
}
.class-group-name{
    color: #333;
}
#tblModelMeta thead th, #tblModelErrorModel thead th {
    /* color: #3f4448; */
    background-color: #545b62;
    text-transform: uppercase;
    /* text-align: center; */
}
#tblModelMeta tbody, #tblModelErrorModel tbody{
    background: #fff;
    color: #333;
}
.class-group-name{
    color: #333;
}
.styletextshad{
    color: #333;
    text-shadow: 2px 1px 2px #756d6d;
}
/* .errorCode a:hover{
    color: #333;
} */
/* .table-sticky tr th:nth-of-type(1) {
    width: 58px ;
    padding-left: 10px;
    left: -1px; 
    z-index:10; 
    position:sticky;
    border: none !important;
    box-shadow: inset 1px 2px 0 #fff,
                inset -1px -1px 0 #fff;
}
.table-sticky tr th:nth-of-type(2) {
    left: 33px;
    z-index:10; 
    position:sticky;
    border: none !important;
}
.table-sticky tr th:nth-of-type(9) {
    left: -1px; 
    z-index:10; 
    position:sticky;
    border: none !important;
} */

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
.table-xxs > thead > tr > th, .table-xxs > tbody > tr > th, .table-xxs > tfoot > tr > th, .table-xxs > thead > tr > td, .table-xxs > tbody > tr > td, .table-xxs > tfoot > tr > td{
    padding: 3px;
}
</style>
<!-- <script src="/assets/js/custom/common.js"></script> -->
<script src="/assets/js/custom/station-error.js"></script>
<script>
    // init
    var dataset = {
        factory: '${factory}',
        modelName: '${modelName}',
        stationName: '${stationName}',
        timeout: {},
        selectedModel: 'all',
        status: 'all',
        style: 'new-style',
        customerName: '',
        moType: ''
    };

    loadModelList();

    $(document).ready(function() {
        if(dataset.factory == 'S03') {
            $('span[name="factory"]').html('F12');
        }
        var spcTS = getTimeSpan();
        $('input[name="timeSpan"]').data('daterangepicker').setStartDate(spcTS.startDate);
        $('input[name="timeSpan"]').data('daterangepicker').setEndDate(spcTS.endDate);
        dataset.timeSpan = $('input[name="timeSpan"]').val();

        $.ajax({
            type: "GET",
            url: "/api/test/sfc/customer",
            data: {
                factory: dataset.factory
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var list = '<option value="">ALL</option>';
                for (i in data) {
                    if (dataset.customerName == data[i]) {
                        list += '<option selected="selected" value="'+data[i]+'">' + data[i] + '</option>';
                        $("#selectCustomer").val(dataset.customerName);
                    } else {
                        list += '<option value="'+data[i]+'">' + data[i] + '</option>';
                    }
                }

                $("#selectCustomer").html(list);
                $("#selectCustomer").selectpicker('refresh');

                $('#selectCustomer').on('change', function() {
                    dataset.customerName = this.value;
                    loadModelList();
                });

                $('select[name="moType"]').on('change', function() {
                    dataset.moType = this.value;
                    loadModelList();
                });
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });

        $('input[name=timeSpan]').on('change', function(event) {
            dataset.timeSpan = event.target.value;
            loadModelList();
            //loadStatus(dataset.factory, dataset.timeSpan);
            //if (dataset.selectedModel == 'all') {
            //    loadProgressStatus(dataset.factory, '', dataset.timeSpan);
            //} else {
            //    loadProgressStatus(dataset.factory, dataset.selectedModel, dataset.timeSpan);
            //}
        });

        //Sort Table By Column
        function sortTable(f,n, idTable){
            var rows = $('#'+idTable+' tbody  tr').get();

            rows.sort(function(a, b) {

                var A = getVal(a);
                var B = getVal(b);

                if(parseInt(A) < parseInt(B)) {
                    return -1*f;
                }
                if(parseInt(A) > parseInt(B)) {
                    return 1*f;
                }
                return 0;
            });

            function getVal(elm){
                var v = $(elm).children('td').eq(n).text().toUpperCase();
                if($.isNumeric(v)){
                    v = parseInt(v,10);
                }
                return v;
            }

            $.each(rows, function(index, row) {
                $('#'+idTable).children('tbody').append(row);
            });
        }

        var num = 1;
        $('#tbl-model-status thead th').click(function(){
            num *= -1;
            var n = $(this).prevAll().length;
            sortTable(num,n,'tbl-model-status');
        });
    });
</script>
<script type="text/javascript" src="/assets/custom/js/nbb/dragModal.js"></script>