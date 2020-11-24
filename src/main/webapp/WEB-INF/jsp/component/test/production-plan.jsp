<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/style.css" />
<style>
    #tblPlan thead th{
        background-color: #FFFF99;
        color: #272727;
        text-align: center;
        text-transform: uppercase;
    }
    #tblPlan thead tr:nth-of-type(1) th{
        background-color: #FFFF99;
        color: #272727;
        text-align: center;
        text-transform: uppercase;
        /* font-weight: bold; */
        height: 40px;
        position: -webkit-sticky;
        position : sticky;
        top : -1px;
        box-shadow: inset 0px 0px 0 #272727,
                    inset 0px -1px 0 #272727;
    }
    #tblPlan thead tr:nth-of-type(2) th{
        background-color: #FFFF99;
        color: #272727;
        text-align: center;
        text-transform: uppercase;
        position: -webkit-sticky;
        position : sticky;
        top : 39px;
        box-shadow: inset 0px 0px 0 #272727,
                    inset 0px -1px 0 #272727;
    }
    #tblPlan tr #first-th{
        left: -1px; 
        z-index:10; 
        position:sticky;
        border: none !important;
        box-shadow: inset 0px 1px 0 #272727,
                    inset -1px -1px 0 #272727;
    }
    #tblPlan thead th,
    #tblPlan tbody td{
        padding: 5px !important;
        text-align: center;
        border: 1px solid #272727;
    }
    #tblPlan tbody td:first-child{
        min-width: 60px;
        font-weight: 600;
        left: -1px; 
        position:sticky;
        border: none !important;
        box-shadow: inset 0px 0px 0 #272727,
                    inset -1px -1px 0 #272727;
    }
    #tblPlan tbody td:nth-of-type(2){
        left: 59px; 
        position:sticky;
        border: none !important;
        box-shadow: inset 0px 0px 0 #272727,
                    inset -1px -1px 0 #272727;
    }
    #tblPlan tbody td:nth-of-type(3){
        border-left: none !important;
    }
    .rowH{
        text-align: center;
        color: #272727;
        background-color: #FFFF99;
    }
    .rowD{
        text-align: center;
        color: #272727;
        background-color: #bedfff;
    }
    .rowDN{
        text-align: center;
        color: #272727;
        background-color: #74b5f7;
    }
    .rowG{
        text-align: center;
        color: #272727;
        background-color: #CCFFCC;
    }
    .rowGN{
        text-align: center;
        color: #272727;
        background-color: #a3ffa3;
    }
    .remarkOpen{
        background-color: #FFFF00;
    }
    .remarkClose{
        background-color: #FEB0BD;
    }
    .pointer{
        cursor: pointer;
    }
    #tblPlan tbody td a{
        color: #0000FF;
    }
    .no-borderT{
        border-top: 0px !important;
    }
    .no-borderB{
        border-bottom: 0px !important;
    }
    .table-sticky tr td{
        text-align: center;
    }
    .table-sticky thead th {
        background-color: #FFC000;
        position: -webkit-sticky;
        position : sticky;
        top : -1px;
    }
    /* here is the trick */
    .table-sticky tbody:nth-of-type(1) tr:nth-of-type(1) td {
        border-top: none !important;
    }
    .table-sticky thead th {
        border-top: none !important;
        border-bottom: none !important;
        box-shadow: inset 1px 1px 0 #ddd,
                    inset 0 -1px 0 #ddd;
    }
    .table-small > thead > tr > th, .table-small > tbody > tr > th, .table-small > tfoot > tr > th, .table-small > thead > tr > td, .table-small > tbody > tr > td, .table-small > tfoot > tr > td {
        padding: 3px 5px;
        border: 1px solid #cccccc;
    }
    .modal-body {
        padding-top: 5px;
    }
    .button-setting{
        width: 20%;
        display: inline-block;
    }
    .button-setting button{
        width: 100%;
        margin: 5px 0;
        background:#ddd;
        color: #333;
    }
    .labelCopy{
        background-color: #FFFFFF;
        border: 1px solid #272727;
        color: #272727;
        padding: 0px 5px;
        float: right;
        border-radius: 5px;
    }
    .loader{
        display: block;
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
<div class="loader"></div>
<div class="row">
    <div class="panel panel-overview no-margin" style="background-color:#272727; color:#fff; text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
        <span style="font-size:16px;">PRODUCTION PLAN</span>
    </div>
    <div class="col-sm-10">
        <div class="input-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        </div>
    </div>
    <div class="col-sm-2">
        <div class="form-group" style="height: 26px; margin: 5px; width: 30%; float: left;">
            <select class="form-control bootstrap-select" data-style="btn-xs" name="selectSection">
                <option value="SI">SI</option>
                <option value="PTH">PTH</option>
                <option value="SMT">SMT</option>
            </select>
        </div>
        <div class="button-setting">
            <button class="btn btn-xs" onclick="loadDetail(0)" title="Add New Plan"><i class="fa fa-plus"></i></button>
        </div>
        <div class="button-setting">
            <button class="btn btn-xs" data-toggle="modal" data-target="#modal-upload" onclick=""><i class="fa fa-upload"></i></button>
        </div>
        <div class="button-setting">
            <button class="btn btn-xs" onclick="settingMail()" title="Setting E-Mail"><i class="fa fa-envelope"></i></button>
        </div>
        <!-- <div class="button-setting">
            <button class="btn btn-xs" onclick="copyData()" title="Copy-Paste"><i class="fa fa-paste"></i></button>
        </div> -->
    </div>
    <div class="col-xs-12" style="margin: 10px 0px;">
        <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
            <table class="table table-xxs table-sticky" id="tblPlan">
                <thead></thead>
                <tbody></tbody>
            </table>
        </div>
    </div>
</div>

<!-- MODAL Detail-->
<div id="modalDetail" class="modal fade" role="dialog">
	<div class="modal-dialog" style="font-size: 13px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <!-- <h6 class="modal-title text-bold" style="margin: 5px 0;">NEW / EDIT</h6> -->
                <ul class="nav nav-tabs" style="margin: 5px 0;">
                    <li id="li-edit" class="active"><a data-toggle="tab" href="#tab-edit" style="font-size:13px; padding: 8px;">Add/Edit Plan</a></li>
                    <li id="li-generate"><a data-toggle="tab" href="#tab-generate" style="font-size:13px; padding: 8px;">Generate Plan</a></li>
                </ul>
            </div>
        	<div class="modal-body">
                <div class="tab-content">
                    <div id="tab-edit" class="tab-pane active">
                        <div class="row">
                            <input type="hidden" name="id" value=""/>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Line</label>
                                <div class="col-xs-3">
                                    <input type="text" name="lineName" class="form-control">
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Section</label>
                                <div class="col-xs-3">
                                    <select class="form-control bootstrap-select" name="section">
                                        <option value="SI">SI</option>
                                        <option value="PTH">PTH</option>
                                        <option value="SMT">SMT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Model</label>
                                <div class="col-xs-9">
                                    <input type="text" name="modelName" class="form-control">
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Work Date</label>
                                <div class="col-xs-3">
                                    <input class="form-control daterange-single" name="shiftTime" type="text" />
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Shift</label>
                                <div class="col-xs-3">
                                    <select class="form-control bootstrap-select" name="shift">
                                        <option value="DAY">DAY</option>
                                        <option value="NIGHT">NIGHT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">W/O</label>
                                <div class="col-xs-3">
                                    <input type="text" name="mo" class="form-control" placeholder="">
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Remark</label>
                                <div class="col-xs-3">
                                    <select class="form-control bootstrap-select" name="remark">
                                        <option value="ONLINE">ONLINE</option>
                                        <option value="OPEN">OPEN</option>
                                        <option value="CLOSE">CLOSE</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Plan Qty</label>
                                <div class="col-xs-3">
                                    <input type="text" name="plan" class="form-control" placeholder="">
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Total Plan</label>
                                <div class="col-xs-3">
                                    <input type="text" name="total" class="form-control" placeholder="">
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Output Qty</label>
                                <div class="col-xs-3">
                                    <input type="text" name="output" class="form-control" placeholder="">
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Total Output</label>
                                <div class="col-xs-3">
                                    <input type="text" name="totalOutput" class="form-control" placeholder="">
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Work Time (h)</label>
                                <div class="col-xs-3">
                                    <input type="number" step="0.1" name="workTime" class="form-control">
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Demand</label>
                                <div class="col-xs-3">
                                    <input type="number" name="demand" class="form-control" placeholder="">
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Note</label>
                                <div class="col-xs-9">
                                    <textarea class="form-control" name="note" type="text" value=""></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="tab-generate" class="tab-pane">
                        <div class="row">
                            <input type="text" name="gen-id" class="form-control hidden" />
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Line</label>
                                <div class="col-xs-3">
                                    <input type="text" name="gen-lineName" class="form-control">
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Section</label>
                                <div class="col-xs-3">
                                    <select class="form-control bootstrap-select" name="gen-section">
                                        <option value="SI">SI</option>
                                        <option value="PTH">PTH</option>
                                        <option value="SMT">SMT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Model</label>
                                <div class="col-xs-9">
                                    <input type="text" name="gen-modelName" class="form-control">
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Work Date</label>
                                <div class="col-xs-3">
                                    <input class="form-control daterange-single" name="gen-shiftTime" type="text" />
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Shift</label>
                                <div class="col-xs-3">
                                    <select class="form-control bootstrap-select" name="gen-shift">
                                        <option value="DAY">DAY</option>
                                        <option value="NIGHT">NIGHT</option>
                                        <option value="ALL">ALL</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">W/O</label>
                                <div class="col-xs-3">
                                    <input type="text" name="gen-mo" class="form-control" placeholder="">
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">W/O Qty</label>
                                <div class="col-xs-3">
                                    <input type="number" name="gen-moQty" class="form-control" placeholder="">
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">UPH</label>
                                <div class="col-xs-9">
                                    <input type="number" name="gen-uph" class="form-control" placeholder="">
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Work Time (h)</label>
                                <div class="col-xs-3">
                                    <input type="number" step="0.1" name="gen-workTime" class="form-control">
                                </div>
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Demand</label>
                                <div class="col-xs-3">
                                    <input type="number" name="gen-demand" class="form-control" placeholder="">
                                </div>
                            </div>
                            <div class="form-group row no-margin">
                                <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Note</label>
                                <div class="col-xs-9">
                                    <textarea class="form-control" name="gen-note" type="text" value=""></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-xs bg-white" style="width: 75px; border: 1px solid #F44337; color: #F44337;" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-xs bg-white hidden pull-left" style="width: 75px; border: 1px solid #F44337; color: #F44337;" id="btnDelete" onclick="deletePlan()"><i class="fa fa-trash position-left"></i>Delete</button>
                <button class="btn btn-xs bg-white" style="width: 80px; border: 1px solid #4CAF50; color: #4CAF50;" data-dismiss="modal" onclick="postPlan();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
	</div>
</div>

<!-- Modal Upload-->
<div class="modal fade" id="modal-upload">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Upload File </h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row addLayout">
                    <div class="col-sm-12" style="padding: 0;">
                            <input type="file" class="form-control btn-border" id="uploadFile">
                    </div>
                    <div class="col-sm-12" style="text-align: right;">
                        <button id="btnSubmitPublish" class="btn btn-xs" style="width: 30%; margin: 5px 0px" onclick="publishFile()"><i class="fa fa-upload"></i> Publish</button>
                        <button id="btnSubmitUpload" class="btn btn-xs" style="width: 30%; margin: 5px 0px" onclick="uploadFile()"><i class="fa fa-upload"></i> Upload</button>
                        <!-- <button id="btnCancel" class="btn btn-xs" style="width: 30%; margin: 5px 0px; background:#ddd; color: #333;"><i class="fa fa-undo"></i> Cancel</button> -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal Setting-->
<div class="modal fade" id="modal-setting">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">List Mail Publish</h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row" style="margin-bottom: 15px;">
                    <div class="col-sm-12 no-padding table-responsive pre-scrollable" style="max-height: calc(100vh - 450px);">
                        <table id="tblListMail" class="table table-small table-sticky">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>Factory</th>
                                    <th>Email</th>
                                    <th>Enabled</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <label class="control-label text-bold no-margin">Input List Email </br>Example : email1@mail.foxconn.vn, email2@mail.foxconn.vn, email3@mail.foxconn.vn, ...</label>
                        <textarea class="form-control" style="padding: 5px;" name="txtListMail" type="text" value="" spellcheck="false" rows="5"></textarea>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-xs bg-white" style="width: 70px; border: 1px solid #4CAF50; color: #4CAF50;" data-dismiss="modal" onclick="saveMail()">Save<i class="fa fa-check-circle position-right"></i></button>
            </div>
        </div>
    </div>
</div>
<!-- Modal Copy Data-->
<div class="modal fade" id="modal-copy">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"> Copy Data </h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="form-group row no-margin">
                        <label class="col-xs-1 control-label text-bold" style="padding-top: 8px;">From</label>
                        <div class="col-xs-5">
                            <input class="form-control daterange-single" name="copyFrom" type="text" />
                        </div>
                        <label class="col-xs-1 control-label text-bold" style="padding-top: 8px;">To</label>
                        <div class="col-xs-5">
                            <input class="form-control daterange-single" name="pasteTo" type="text" />
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="postCopy();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
    </div>
</div>

<script>
    var dataset = {
        factory: '${factory}',
        section: 'SI'
    }

    function loadData(){
        $('.loader').removeClass('hidden');
        $.ajax({
            type: "GET",
            url: "/api/test/model/plan/prd",
            data: {
                factory: dataset.factory,
                type: "DAILY",
                sectionName: dataset.section,
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $('#tblPlan thead').html('');
                $('#tblPlan tbody').html('');
                var time = [];
                var line = [];

                for(i in data){
                    if(line.indexOf(i) == -1){
                        line.push(i);
                    }
                    for(j in data[i]){
                        if(time.indexOf(j) == -1){
                            time.push(j);
                        }
                    }
                }
                // Add Header
                var header = '<tr><th colspan="2" rowspan="2" id="first-th"></th>';
                var header2 = '<tr>';
                for(i in time){
                    if(i == 0){
                        header += '<th colspan="8" class="no-borderB" style="border-left: none !important;">'+time[i]
                            +  ' <label class="labelCopy" data-start="'+time[i]+'" onclick="postCopy(this)" title="Copy Plan to next day"><i class="fa fa-paste"></i></label></th>';
                        header2 += '<th class="no-borderT" style="border-left: none !important;">機種 </br> Model</th>'
                            +  '<th class="no-borderT">工單 </br> W/O</th>'
                            +  '<th class="no-borderT">計划量 </br> Plan Qty</th>'
                            +  '<th class="no-borderT">累計量 </br> Total Qty</th>'
                            +  '<th class="no-borderT" </br> Output Qty</th>'
                            +  '<th class="no-borderT" </br> Total Output</th>'
                            +  '<th class="no-borderT" </br> Work Time (h)</th>'
                            +  '<th class="no-borderT">需求人力 </br> Demand</th>';
                    }else {
                        header += '<th colspan="8" class="no-borderB">'+time[i]
                            +  ' <label class="labelCopy" data-start="'+time[i]+'" onclick="postCopy(this)" title="Copy Plan to next day"><i class="fa fa-paste"></i></label></th>';
                        header2 += '<th class="no-borderT">機種 </br> Model</th>'
                            +  '<th class="no-borderT">工單 </br> W/O</th>'
                            +  '<th class="no-borderT">計划量 </br> Plan Qty</th>'
                            +  '<th class="no-borderT">累計量 </br> Total Qty</th>'
                            +  '<th class="no-borderT"> </br> Output Qty</th>'
                            +  '<th class="no-borderT"> </br> Total Output</th>'
                            +  '<th class="no-borderT"> </br> Work Time (h)</th>'
                            +  '<th class="no-borderT">需求人力 </br> Demand</th>';
                    }
                }
                header += '</tr>';
                header2 += '</tr>';
                $('#tblPlan thead').append(header);
                $('#tblPlan thead').append(header2);
                // End Add Header

                // Add Body
                var tmp2 = {};
                var tmp3 = {};
                for(i in data){
                    var rowSpanD = [];
                    var rowSpanN = [];
                    for(j in data[i]){
                        if(data[i][j]["DAY"] !== undefined)
                            rowSpanD.push(data[i][j]["DAY"].length);
                        if(data[i][j]["NIGHT"] !== undefined)
                            rowSpanN.push(data[i][j]["NIGHT"].length);
                    }
                    tmp2[i] = Math.max.apply(null, rowSpanD);
                    tmp3[i] = Math.max.apply(null, rowSpanN);
                }

                var body = '';
                // Day Shift
                for(i in tmp2){
                    for(let j = 0; j< tmp2[i]; j++){
                        body += '<tr><td class="rowH line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'">'+i+'</td>'
                            +  '<td class="rowH line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_ShiftD">D</td>';
                        for(k in time){
                            if(time[k] == moment().format("YYYY/MM/DD")){
                                body += '<td class="rowD '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_model"></td>'
                                    +  '<td class="rowD '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wo pointer"></td>'
                                    +  '<td class="rowD '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_plan"></td>'
                                    +  '<td class="rowD '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_total"></td>'
                                    +  '<td class="rowD '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_output"></td>'
                                    +  '<td class="rowD '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_t_out"></td>'
                                    +  '<td class="rowD '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wt"></td>'
                                    +  '<td class="rowD '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_demand"></td>';
                            } else{
                                body += '<td class="rowG '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_model"></td>'
                                    +  '<td class="rowG '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wo pointer"></td>'
                                    +  '<td class="rowG '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_plan"></td>'
                                    +  '<td class="rowG '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_total"></td>'
                                    +  '<td class="rowG '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_output"></td>'
                                    +  '<td class="rowG '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_t_out"></td>'
                                    +  '<td class="rowG '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wt"></td>'
                                    +  '<td class="rowG '+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_demand"></td>';
                            }
                        }
                        body += '</tr>';
                        // Add row empty no border top
                        if(j == tmp2[i] -1){
                            body += '<tr><td class="rowH line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'">'+i+'</td>'
                                +  '<td class="rowH line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_ShiftD">D</td>';
                            for(k in time){
                                if(time[k] == moment().format("YYYY/MM/DD")){
                                    body += '<td class="rowD no-borderT"></td>'
                                        +  '<td class="rowD no-borderT"></td>'
                                        +  '<td class="rowD no-borderT"></td>'
                                        +  '<td class="rowD no-borderT"></td>'
                                        +  '<td class="rowD no-borderT"></td>'
                                        +  '<td class="rowD no-borderT"></td>'
                                        +  '<td class="rowD no-borderT"></td>'
                                        +  '<td class="rowD no-borderT"></td>';
                                } else{
                                    body += '<td class="rowG no-borderT"></td>'
                                        +  '<td class="rowG no-borderT"></td>'
                                        +  '<td class="rowG no-borderT"></td>'
                                        +  '<td class="rowG no-borderT"></td>'
                                        +  '<td class="rowG no-borderT"></td>'
                                        +  '<td class="rowG no-borderT"></td>'
                                        +  '<td class="rowG no-borderT"></td>'
                                        +  '<td class="rowG no-borderT"></td>';
                                }
                            }
                            body += '</tr>';
                        }
                    }
                    // Night Shift
                    for(i2 in tmp3){
                        if(i2 == i){
                            for(let j2 = 0; j2< tmp3[i2]; j2++){
                                body += '<tr><td class="rowH line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'">'+i+'</td>'
                                    +  '<td class="rowH line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_ShiftN">N</td>';
                                for(k2 in time){
                                    if(time[k2] == moment().format("YYYY/MM/DD")){
                                        body += '<td class="rowDN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_model"></td>'
                                            +  '<td class="rowDN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wo pointer"></td>'
                                            +  '<td class="rowDN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_plan"></td>'
                                            +  '<td class="rowDN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_total"></td>'
                                            +  '<td class="rowDN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_output"></td>'
                                            +  '<td class="rowDN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_t_out"></td>'
                                            +  '<td class="rowDN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wt"></td>'
                                            +  '<td class="rowDN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_demand"></td>';
                                    } else{
                                        body += '<td class="rowGN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_model"></td>'
                                            +  '<td class="rowGN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wo pointer"></td>'
                                            +  '<td class="rowGN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_plan"></td>'
                                            +  '<td class="rowGN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_total"></td>'
                                            +  '<td class="rowGN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_output"></td>'
                                            +  '<td class="rowGN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_t_out"></td>'
                                            +  '<td class="rowGN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wt"></td>'
                                            +  '<td class="rowGN '+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_demand"></td>';
                                    }
                                }
                                body += '</tr>';
                                if(j2 == tmp3[i2] -1){
                                    body += '<tr><td class="rowH line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'">'+i+'</td>'
                                        +  '<td class="rowH line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_ShiftN">N</td>';
                                    for(k2 in time){
                                        if(time[k2] == moment().format("YYYY/MM/DD")){
                                            body += '<td class="rowDN no-borderT"></td>'
                                                +  '<td class="rowDN no-borderT"></td>'
                                                +  '<td class="rowDN no-borderT"></td>'
                                                +  '<td class="rowDN no-borderT"></td>'
                                                +  '<td class="rowDN no-borderT"></td>'
                                                +  '<td class="rowDN no-borderT"></td>'
                                                +  '<td class="rowDN no-borderT"></td>'
                                                +  '<td class="rowDN no-borderT"></td>';
                                        } else{
                                            body += '<td class="rowGN no-borderT"></td>'
                                                +  '<td class="rowGN no-borderT"></td>'
                                                +  '<td class="rowGN no-borderT"></td>'
                                                +  '<td class="rowGN no-borderT"></td>'
                                                +  '<td class="rowGN no-borderT"></td>'
                                                +  '<td class="rowGN no-borderT"></td>'
                                                +  '<td class="rowGN no-borderT"></td>'
                                                +  '<td class="rowGN no-borderT"></td>';
                                        }
                                    }
                                    body += '</tr>';
                                }
                            }
                        }
                    }
                }
                $('#tblPlan tbody').append(body);
                // End Add Body

                // Map value to column
                // Day shift
                for(i in tmp2){
                    for(let j = 0; j< tmp2[i]; j++){
                        var classLine =  $('.line_'+i.replace(/\s|\.|\(|\)|\//g,'_'));
                        if(classLine.length > 1){
                            for(let x = 0; x< classLine.length; x++){
                                classLine[0].rowSpan = classLine.length;
                                if(x > 0){
                                    classLine[x].className += ' hidden';
                                }
                            }
                        }
                        var classShiftN =  $('.line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_ShiftD');
                        if(classShiftN.length > 1){
                            for(let x = 0; x< classShiftN.length; x++){
                                classShiftN[0].rowSpan = classShiftN.length;
                                if(x > 0){
                                    classShiftN[x].className += ' hidden';
                                }
                            }
                        }
                        for(k in time){
                            if(data[i][time[k]] !== undefined){
                                if(data[i][time[k]]["DAY"] !== undefined && data[i][time[k]]["DAY"][j] !== undefined)
                                {
                                    var txt = '<a onclick="loadDetail('+data[i][time[k]]["DAY"][j].id+')">'+data[i][time[k]]["DAY"][j].modelName+'</a>';
                                    $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_model').html(txt);
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wo').html(data[i][time[k]]["DAY"][j].mo);
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_plan').html(data[i][time[k]]["DAY"][j].plan);
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_total').html(data[i][time[k]]["DAY"][j].total);
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_output').html(data[i][time[k]]["DAY"][j].output);
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_t_out').html(data[i][time[k]]["DAY"][j].totalOutput);
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_demand').html(data[i][time[k]]["DAY"][j].demand);
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wt').html(data[i][time[k]]["DAY"][j].workTime);
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wo').attr('title',data[i][time[k]]["DAY"][j].note);
                                        if(data[i][time[k]]["DAY"][j].note != '' && data[i][time[k]]["DAY"][j].note != ' ' && data[i][time[k]]["DAY"][j].note != null){
                                            $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wo').css('color','#BE2222');
                                        }
                                    if(data[i][time[k]]["DAY"][j].remark == 'OPEN'){
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_model').addClass('remarkOpen');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wo').addClass('remarkOpen');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_plan').addClass('remarkOpen');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_total').addClass('remarkOpen');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_output').addClass('remarkOpen');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_t_out').addClass('remarkOpen');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_demand').addClass('remarkOpen');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wt').addClass('remarkOpen');
                                    } else if(data[i][time[k]]["DAY"][j].remark == 'CLOSE') {
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_model').addClass('remarkClose');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wo').addClass('remarkClose');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_plan').addClass('remarkClose');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_total').addClass('remarkClose');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_output').addClass('remarkClose');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_t_out').addClass('remarkClose');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_demand').addClass('remarkClose');
                                        $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wt').addClass('remarkClose');
                                    }
                                }
                            }
                            if(j == tmp2[i] - 1){
                                $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_model').addClass("no-borderB");
                                $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wo').addClass("no-borderB");
                                $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_plan').addClass("no-borderB");
                                $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_total').addClass("no-borderB");
                                $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_output').addClass("no-borderB");
                                $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_t_out').addClass("no-borderB");
                                $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_demand').addClass("no-borderB");
                                $('.'+j+'Dline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k].replace(/\//g,'_')+'_wt').addClass("no-borderB");
                            }
                        }
                    }
                    // Night shift
                    for(i2 in tmp3){
                        if(i2 == i){
                            for(let j2 = 0; j2< tmp3[i2]; j2++){
                                var classLine =  $('.line_'+i.replace(/\s|\.|\(|\)|\//g,'_'));
                                if(classLine.length > 1){
                                    for(let x = 0; x< classLine.length; x++){
                                        classLine[0].rowSpan = classLine.length;
                                        if(x > 0){
                                            classLine[x].className += ' hidden';
                                        }
                                    }
                                }
                                var classShiftN =  $('.line_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_ShiftN');
                                if(classShiftN.length > 1){
                                    for(let x = 0; x< classShiftN.length; x++){
                                        classShiftN[0].rowSpan = classShiftN.length;
                                        if(x > 0){
                                            classShiftN[x].className += ' hidden';
                                        }
                                    }
                                }
                                for(k2 in time){
                                    if(data[i][time[k2]] !== undefined){
                                        if(data[i][time[k2]]["NIGHT"] !== undefined && data[i][time[k2]]["NIGHT"][j2] !== undefined)
                                        {
                                            var txt = '<a onclick="loadDetail('+data[i][time[k2]]["NIGHT"][j2].id+')">'+data[i][time[k2]]["NIGHT"][j2].modelName+'</a>';
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_model').html(txt);
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wo').html(data[i][time[k2]]["NIGHT"][j2].mo);
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_plan').html(data[i][time[k2]]["NIGHT"][j2].plan);
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_total').html(data[i][time[k2]]["NIGHT"][j2].total);
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_output').html(data[i][time[k2]]["NIGHT"][j2].output);
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_t_out').html(data[i][time[k2]]["NIGHT"][j2].totalOutput);
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wt').html(data[i][time[k2]]["NIGHT"][j2].workTime);
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_demand').html(data[i][time[k2]]["NIGHT"][j2].demand);
                                            $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wo').attr('title',data[i][time[k2]]["NIGHT"][j2].note);
                                            if(data[i][time[k2]]["NIGHT"][j2].note != '' && data[i][time[k2]]["NIGHT"][j2].note != ' ' && data[i][time[k2]]["NIGHT"][j2].note != null){
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wo').css('color','#BE2222');
                                            }
                                            if(data[i][time[k2]]["NIGHT"][j2].remark == 'OPEN'){
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_model').addClass('remarkOpen');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wo').addClass('remarkOpen');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_plan').addClass('remarkOpen');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_total').addClass('remarkOpen');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_output').addClass('remarkOpen');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_t_out').addClass('remarkOpen');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_demand').addClass('remarkOpen');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wt').addClass('remarkOpen');
                                            } else if(data[i][time[k2]]["NIGHT"][j2].remark == 'CLOSE') {
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_model').addClass('remarkClose');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wo').addClass('remarkClose');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_plan').addClass('remarkClose');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_total').addClass('remarkClose');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_output').addClass('remarkClose');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_t_out').addClass('remarkClose');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_demand').addClass('remarkClose');
                                                $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wt').addClass('remarkClose');
                                            }
                                        }
                                    }
                                    if(j2 == tmp3[i] - 1){
                                        $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_model').addClass("no-borderB");
                                        $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wo').addClass("no-borderB");
                                        $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_plan').addClass("no-borderB");
                                        $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_total').addClass("no-borderB");
                                        $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_output').addClass("no-borderB");
                                        $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_t_out').addClass("no-borderB");
                                        $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_wt').addClass("no-borderB");
                                        $('.'+j2+'Nline_'+i.replace(/\s|\.|\(|\)|\//g,'_')+'_'+time[k2].replace(/\//g,'_')+'_demand').addClass("no-borderB");
                                    }
                                }
                            }
                        }
                    }
                }
                // End Map value to column

            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').addClass('hidden');

                $("#tblPlan>tbody>tr>td").filter(function() {
                    if($(this).text() == 0){
                        $(this).html('');
                    }
                });
            }
        });
    }

    function loadDetail(id){
        $('#modalDetail').modal('show');
        // $('#modalDetail input[name="lineName"]').val();
        $('#modalDetail input[name="id"]').val(id);

        $('#li-edit').addClass('active');
        $('#tab-edit').addClass('active');
        $('#li-generate').removeClass('active');
        $('#tab-generate').removeClass('active');
        if(id != 0){
            $('#li-generate').addClass('hidden');
            $('#tab-generate').addClass('hidden');

            $('#btnDelete').removeClass('hidden');
            $('#modalDetail select[name="shift"]').attr('disabled','disabled');
            $('#modalDetail input[name="lineName"]').attr('disabled','disabled');
            $('#modalDetail input[name="modelName"]').attr('disabled','disabled');
            $('#modalDetail input[name="shiftTime"]').attr('disabled','disabled');
            $('#modalDetail input[name="mo"]').attr('disabled','disabled');
            $.ajax({
                type: "GET",
                url: "/api/test/model/plan/"+id,
                data: {
                    factory: dataset.factory
                },
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    $('.loader').removeClass('hidden');
                    if(!$.isEmptyObject(data)){
                        $('#modalDetail input[name="lineName"]').val(data.lineName);
                        $('#modalDetail select[name="section"]').val(data.sectionName);
                        $('#modalDetail select[name="section"]').selectpicker('refresh');
                        $('#modalDetail input[name="modelName"]').val(data.modelName);
                        $('#modalDetail input[name="plan"]').val(data.plan);
                        $('#modalDetail input[name="mo"]').val(data.mo);
                        $('#modalDetail select[name="remark"]').val(data.remark);
                        $('#modalDetail select[name="remark"]').selectpicker('refresh');
                        $('#modalDetail select[name="shift"]').val(data.shift);
                        $('#modalDetail select[name="shift"]').selectpicker('refresh');
                        $('#modalDetail input[name="shiftTime"]').val(data.shiftTime);
                        $('#modalDetail input[name="workTime"]').val(data.workTime);
                        $('#modalDetail input[name="total"]').val(data.total);
                        $('#modalDetail input[name="output"]').val(data.output);
                        $('#modalDetail input[name="totalOutput"]').val(data.totalOutput);
                        $('#modalDetail input[name="demand"]').val(data.demand);
                        $('#modalDetail textarea[name="note"]').val(data.note);
                    }
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
                complete: function () {
                    $('.loader').addClass('hidden');
                }
            });
        } else{
            $('#li-generate').removeClass('hidden');
            $('#tab-generate').removeClass('hidden');
            $('#modalDetail input[name="gen-id"]').val("false");

            $('#btnDelete').addClass('hidden');
            $('#modalDetail select[name="shift"]').removeAttr('disabled');
            $('#modalDetail input[name="lineName"]').removeAttr('disabled');
            $('#modalDetail input[name="modelName"]').removeAttr('disabled');
            $('#modalDetail input[name="shiftTime"]').removeAttr('disabled');
            $('#modalDetail input[name="mo"]').removeAttr('disabled');

            $('#modalDetail input[name="lineName"]').val('');
            $('#modalDetail select[name="section"]').selectpicker('refresh');
            $('#modalDetail input[name="modelName"]').val('');
            $('#modalDetail select[name="shift"]').val('DAY');
            $('#modalDetail select[name="shift"]').selectpicker('refresh');
            $('#modalDetail input[name="plan"]').val('');
            $('#modalDetail input[name="mo"]').val('');
            $('#modalDetail select[name="remark"]').val('OPEN');
            $('#modalDetail select[name="remark"]').selectpicker('refresh');
            $('#modalDetail input[name="workTime"]').val('');
            $('#modalDetail input[name="total"]').val('');
            $('#modalDetail input[name="output"]').val('');
            $('#modalDetail input[name="totalOutput"]').val('');
            $('#modalDetail input[name="demand"]').val('');
            $('#modalDetail textarea[name="note"]').val('');
        }
        $('.daterange-single').daterangepicker({
            singleDatePicker: true,
            opens: "right",
            applyClass: 'bg-slate-600',
            cancelClass: 'btn-default',
            locale: {
                format: 'YYYY/MM/DD'
            }
        });
    }

    $('#li-generate').on('click', function() {
        $('#modalDetail input[name="gen-id"]').val("true");
    });
    $('#li-edit').on('click', function() {
        $('#modalDetail input[name="gen-id"]').val("false");
    });

    function uploadFile(){
        $('.loader').removeClass('hidden');
        var form = new FormData();
        form.append('factory', dataset.factory);
        file = $('#uploadFile').get(0);
        if (file.files.length > 0) {
            if(file.files[0].name.indexOf('.xlsx') > -1){
                form.append('file', file.files[0]);
                $.ajax({
                    type: "POST",
                    url: "/api/test/model/plan/from-file",
                    data: form,
                    processData: false,
                    contentType: false,
                    mimeType: "multipart/form-data",
                    success: function(data){
                        if(JSON.parse(data).result){
                            alert('Upload Success!');
                        } else{
                            alert('Upload Fail!');
                        }
                    },
                    failure: function(errMsg) {
                        console.log(errMsg);
                    },
                    complete: function () {
                        $('.loader').addClass('hidden');
                    }
                });
            } else{
                alert('Pls choose file has extention ".xlsx"!');
            }
        }
    }

    function publishFile(){
        $('.loader').removeClass('hidden');
        var form = new FormData();
        form.append('factory', dataset.factory);
        file = $('#uploadFile').get(0);
        if (file.files.length > 0) {
            if(file.files[0].name.indexOf('.xlsx') > -1){
                form.append('file', file.files[0]);
                $.ajax({
                    type: "POST",
                    url: "/api/test/model/plan/publish",
                    data: form,
                    processData: false,
                    contentType: false,
                    mimeType: "multipart/form-data",
                    success: function(data){
                        if(JSON.parse(data).result){
                            alert('Upload Success!');
                        } else{
                            alert('Upload Fail!');
                        }
                    },
                    failure: function(errMsg) {
                        console.log(errMsg);
                    },
                    complete: function () {
                        $('.loader').addClass('hidden');
                    }
                });
            } else{
                alert('Pls choose file has extention ".xlsx"!');
            }
        }
    }

    function postPlan() {
        var id= Number($('#modalDetail input[name="id"]').val());
        $('.loader').removeClass('hidden');
        if(id != 0){
            var data = {
                sectionName: $('#modalDetail select[name="section"]').val(),
                plan: Number($('#modalDetail input[name="plan"]').val()),
                workTime: Number($('#modalDetail input[name="workTime"]').val()),
                total: Number($('#modalDetail input[name="total"]').val()),
                output: Number($('#modalDetail input[name="output"]').val()),
                totalOutput: Number($('#modalDetail input[name="totalOutput"]').val()),
                demand: Number($('#modalDetail input[name="demand"]').val()),
                remark: $('#modalDetail select[name="remark"]').val(),
                note: $('#modalDetail textarea[name="note"]').val(),
            }
            $.ajax({
                type: 'PUT',
                url: '/api/test/model/plan/'+id,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                success: function (response) {
                    if (response) {
                        alert("Set Plan Success!!");
                        loadData();
                    }
                    else {
                        alert("Set Plan Fail!! Please Try Again!");
                        loadData();
                    }
                    delete this.response;
                },
                failure: function (errMsg) {
                    console.log(errMsg);
                },
                complete: function () {
                    $('.loader').addClass('hidden');
                }
            });
        } else{
            if($('#modalDetail input[name="gen-id"]').val() == "true"){
                var data = {
                    factory: dataset.factory,
                    lineName: $('#modalDetail input[name="gen-lineName"]').val(),
                    sectionName: $('#modalDetail input[name="gen-section"]').val(),
                    modelName: $('#modalDetail input[name="gen-modelName"]').val(),
                    mo: $('#modalDetail input[name="gen-mo"]').val(),
                    moQty: $('#modalDetail input[name="gen-moQty"]').val(),
                    uph: $('#modalDetail input[name="gen-uph"]').val(),
                    shift: $('#modalDetail select[name="gen-shift"]').val(),
                    shiftTime: $('#modalDetail input[name="gen-shiftTime"]').val(),
                    workTime:  Number($('#modalDetail input[name="gen-workTime"]').val()),
                    demand: Number($('#modalDetail input[name="gen-demand"]').val()),
                    note: $('#modalDetail textarea[name="gen-note"]').val(),
                }
                $.ajax({
                    type: 'POST',
                    url: '/api/test/model/plan/generate',
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    success: function (response) {
                        if (response) {
                            alert("Add Plan Success!!");
                            loadData();
                        }
                        else {
                            alert("Add Plan Fail!! Please Try Again!");
                            loadData();
                        }
                        delete this.response;
                    },
                    failure: function (errMsg) {
                        console.log(errMsg);
                    },
                    complete: function () {
                        $('.loader').addClass('hidden');
                    }
                });
            } else{
                var data = {
                    id: id,
                    factory: dataset.factory,
                    lineName: $('#modalDetail input[name="lineName"]').val(),
                    sectionName: $('#modalDetail select[name="section"]').val(),
                    modelName: $('#modalDetail input[name="modelName"]').val(),
                    plan: Number($('#modalDetail input[name="plan"]').val()),
                    mo: $('#modalDetail input[name="mo"]').val(),
                    shift: $('#modalDetail select[name="shift"]').val(),
                    shiftTime: $('#modalDetail input[name="shiftTime"]').val(),
                    workTime:  Number($('#modalDetail input[name="workTime"]').val()),
                    total: Number($('#modalDetail input[name="total"]').val()),
                    output: Number($('#modalDetail input[name="output"]').val()),
                    totalOutput: Number($('#modalDetail input[name="totalOutput"]').val()),
                    demand: Number($('#modalDetail input[name="demand"]').val()),
                    remark: $('#modalDetail select[name="remark"]').val(),
                    note: $('#modalDetail textarea[name="note"]').val(),
                }
                $.ajax({
                    type: 'POST',
                    url: '/api/test/model/plan',
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    success: function (response) {
                        if (response) {
                            alert("Add Plan Success!!");
                            loadData();
                        }
                        else {
                            alert("Add Plan Fail!! Please Try Again!");
                            loadData();
                        }
                        delete this.response;
                    },
                    failure: function (errMsg) {
                        console.log(errMsg);
                    },
                    complete: function () {
                        $('.loader').addClass('hidden');
                    }
                });
            }
        }
    }

    function deletePlan(){
        var id= Number($('#modalDetail input[name="id"]').val());
        var confirm = window.confirm("Do you want to DETELE?");
        if(confirm == true){
            $('.loader').removeClass('hidden');
            $.ajax({
                type: 'DELETE',
                url: '/api/test/model/plan/' + id,
                contentType: "application/json; charset=utf-8",
                success: function (response) {
                    if (response) {
                        alert("DELETE Success!!");
                        loadData();
                        $('#modalDetail').modal('hide');
                    }
                    else {
                        alert("DELETE Fail!! Please Try Again!");
                        loadData();
                    }
                    delete this.response;
                },
                failure: function (errMsg) {
                    console.log(errMsg);
                    alert("DELETE Fail!! Please Try Again!");
                    loadData();
                },
                complete: function () {
                    $('.loader').addClass('hidden');
                }
            });
        }
    }

    function settingMail(){
        $('#modal-setting').modal('show');
        $('.loader').removeClass('hidden');
        $.ajax({
            type: "GET",
            url: "/api/test/email-list",
            data: {
                factory: dataset.factory,
                department: "PC",
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(!$.isEmptyObject(data)){
                    $('#tblListMail>tbody').html('');
                    var html = '';
                    var textValue = '';
                    for(i in data){
                        html += '<tr><td>'+(Number(i)+1)+'</td><td>'+data[i].factory+'</td><td>'+data[i].email+'</td><td>'+data[i].enabled+'</td></tr>'
                        if(data[i].enabled){
                            textValue += data[i].email + ',';
                        }
                    }
                    $('#tblListMail>tbody').html(html);
                    $('textarea[name=txtListMail]').val(textValue);
                    $('textarea[name=txtListMail]').focus();
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').addClass('hidden');
            }
        });
    }

    function saveMail(){
        $('#modal-setting').modal('hide');
        $('.loader').removeClass('hidden');
        var listMail = $('textarea[name=txtListMail]').val().replace(/\n|\s/g,'');
        console.log(listMail);
        var form = {
            factory: dataset.factory,
            emailList: listMail,
        }
        $.ajax({
            type: "POST",
            url: "/api/test/email-list",
            data: JSON.stringify(form),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                alert('Setting Success!');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').addClass('hidden');
            }
        });
    }

    function saveEmail(){
        var data = {
            factory: dataset.factory,
            emailList: $('textarea[name="txtListMail"]').val(),
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/model/plan/'+id,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response) {
                    alert("Set Plan Success!!");
                    loadData();
                }
                else {
                    alert("Set Plan Fail!! Please Try Again!");
                    loadData();
                }
                delete this.response;
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').addClass('hidden');
            }
        });
    }

    function postCopy(context){
        var start = context.dataset.start;
        var end = moment(start).add(1,'day').format('YYYY/MM/DD');
        var confirm = window.confirm("Copy Data From " + start + " to " + end + "\nAll Data in " + end + " to be deleted\nDo you really want to copy?");
        if(confirm == true){
            $('.loader').removeClass('hidden');
            $.ajax({
                type: "POST",
                url: "/api/test/model/plan/copy",
                data: {
                    factory: dataset.factory,
                    srcWorkDate: start,
                    targetWorkDate: end
                },
                success: function(data){
                    if(data.result){
                        alert('Copy Success!');
                        loadData();
                    }
                    else{
                        alert('Copy Fail (~.~)" Please try again!');
                        // loadData();
                    }
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
                complete: function () {
                    $('.loader').addClass('hidden');
                }
            });
        }
    }

    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var endDate = moment(current).add(6,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).add(-1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
                $('input[name=timeSpan]').on('change', function () {
                    dataset.timeSpan = this.value;
                    loadData();
                });

                dataset.timeSpan = startDate + ' - ' + endDate;
                loadData();

                delete current;
                delete startDate;
                delete endDate;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    $(document).ready(function() {
        getTimeNow();
        $('select[name=selectSection]').on('change', function (){
            dataset.section = this.value;
            loadData();
        });
    });
</script>