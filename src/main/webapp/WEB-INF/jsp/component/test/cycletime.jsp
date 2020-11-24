<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<style>
.loader{
    display: none;
    position: fixed;
    z-index: 1051;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background: rgba(10, 10, 10, 0.8) 
    url('/assets/images/loadingg.gif') 
    50% 50% 
    no-repeat;
}
#btnAddNewPlan, #btnAddNewPcas{
    padding: 3px 10px;
    margin-top: -5px;
}
.difRed{
    color: #FF0000;
    font-weight: bold;
}
</style>
<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="margin-bottom: 0px; min-height: calc(100vh - 100px);">
    <div class="col-lg-12">
        <div class="row" style="margin: 0 0 5px 0; padding: 0">
            <div class="panel panel-overview" id="header" style="font-size: 16px;">
                <span class="text-uppercase">Cycle Time</span>
            </div>
            <div class="no-padding" style="margin: 0 0 5px 0;">
                <div class="col-md-3 col-xs-12 no-padding">
                    <div class="panel input-group" style="margin-bottom: 5px;">
                        <span class="input-group-addon" style="padding: 0px 4px;"><i class="icon-calendar22"></i></span>
                        <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="border-bottom: 0px !important; height: 27px">
                    </div>
                </div>
            </div>
        </div>
        <div class="row" style="margin-bottom: 5px">
            <div class="col-sm-12">
                <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                    <table id="tblWorkHandle" class="table table-small table-bordered table-sticky table-hover" style="text-align: center">
                        <thead>
                            <tr>
                                <th>Model</th>
                                <th>No</th>
                                <th>Station</th>
                                <!-- <th>Machines Qty</th> -->
                                <th>Plan</th>
                                <th>Evaluated Plan</th>
                                <th>Work Time (h)</th>
                                <th>UPH</th>
                                <th>Tester Demand</th>
                                <th>Tester Existing No</th>
                                <th>Different No</th>
                                <th>Operation Time</th>
                                <th>Ping Time</th>
                                <th>IE Cycle Time (s)</th>
                                <th>Avg Total Cycle Time (s)</th>
                                <th>Avg Pass Cycle Time (s)</th>
                                <th>Avg Fail Cycle Time (s)</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- MODAL Detail-->
<div class="modal fade" id="modalGroupDetail" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <span id="spGroupName"></span>
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross"></i></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                    <table id="tblGroupDetail" class="table table-small table-bordered table-hover table-sticky" style="margin-bottom: 10px;">
                        <thead>
                            <tr>
                                <th>Model</th>
                                <th>No</th>
                                <th>Station</th>
                                <th>Machines Qty</th>
                                <th>Plan</th>
                                <th>Evaluated Plan</th>
                                <th>Work Time (h)</th>
                                <th>UPH</th>
                                <th>Tester Demand</th>
                                <th>Tester Existing No</th>
                                <th>Different No</th>
                                <th>Operation Time</th>
                                <th>Ping Time</th>
                                <th>IE Cycle Time (s)</th>
                                <th>Avg Total Cycle Time (s)</th>
                                <th>Avg Pass Cycle Time (s)</th>
                                <th>Avg Fail Cycle Time (s)</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal Plan-->
<div class="modal fade" id="modalSetPlan" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <span><b>SET PLAN</b></span>
                <span id="addModelPlan" class="hidden"></span>
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross"></i></button>
                <button type="button" class="btn btn-sm" id="btnAddNewPlan" onclick="addNewPlanClick()"><i class="fa fa-plus"></i></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                    <table id="tblSetPlan" class="table table-small table-bordered table-hover table-sticky" style="margin-bottom: 10px;">
                        <thead>
                            <tr>
                                <!-- <th>Line</th> -->
                                <th>Model</th>
                                <th>MO</th>
                                <th>Plan</th>
                                <th>Work Time (h)</th>
                                <th>Shift</th>
                                <th>Time</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal PCAS-->
<div class="modal fade" id="modalSetPcas" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <span><b>SET PCAS</b></span>
                <span id="addModel" class="hidden"></span>
                <span id="addGroup" class="hidden"></span>
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross"></i></button>
                <button type="button" class="btn btn-sm" id="btnAddNewPcas" onclick="addNewPcasClick()"><i class="fa fa-plus"></i></button>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                    <table id="tblSetPcas" class="table table-small table-bordered table-hover table-sticky" style="margin-bottom: 10px;">
                        <thead>
                            <tr>
                                <th>Model</th>
                                <th>Group</th>
                                <th>Work Time</th>
                                <th>Tester Existing</th>
                                <th>Operation Time</th>
                                <th>Ping Time</th>
                                <th>IE Cycle Time (s)</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    var dataset = {
        factory: '${factory}'
    };

    function loadData(){
        $('.loader').css('display', 'block');
        $.ajax({
            type: "GET",
            url: "/api/test/cycle-time",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
               $('#tblWorkHandle>tbody').html();
               if(!$.isEmptyObject(data)){
                    var html = '';
                    for(i in data){
                        var classDif = 'difNor';
                        if(data[i].testerDiff != 0) {
                            classDif = 'difRed';
                        }
                        html += '<tr><td class="row_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'">'+data[i].modelName+'</td>'
                             +  '<td class="no_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'"></td>'
                             +  '<td><a href="#" onclick="loadDetail(\''+data[i].modelName+'\',\''+data[i].groupName+'\')">'+data[i].groupName+'</a></td>'
                            //  +  '<td>'+data[i].stationNumber+'</td>'
                             +  '<td class="row_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'_rPlan">'+data[i].realPlan+'</td>'
                             +  '<td class="row_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'_plan"><a onclick="setPlan(\''+ data[i].modelName + '\')">'+data[i].plan+'</a></td>'
                             +  '<td class="row_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'_wt">'+data[i].workTime+'</td>'
                             +  '<td>'+data[i].uph.toFixed(2)+'</td>'
                             +  '<td>'+data[i].testerDemand+'</td>'
                             +  '<td><a onclick="setPcas(\''+ data[i].modelName + '\', \''+ data[i].groupName + '\')">'+data[i].testerExist+'</a></td>'
                             +  '<td class="'+classDif+'">'+data[i].testerDiff+'</td>'
                             +  '<td><a onclick="setPcas(\''+ data[i].modelName + '\', \''+ data[i].groupName + '\')">'+data[i].operationTime.toFixed(2)+'</a></td>'
                             +  '<td><a onclick="setPcas(\''+ data[i].modelName + '\', \''+ data[i].groupName + '\')">'+data[i].pingTime.toFixed(2)+'</a></td>'
                             +  '<td><a onclick="setPcas(\''+ data[i].modelName + '\', \''+ data[i].groupName + '\')">'+data[i].cycle.toFixed(2)+'</a></td>'
                             +  '<td>'+data[i].totalAvgCycle.toFixed(2)+'</td>'
                             +  '<td>'+data[i].passAvgCycle.toFixed(2)+'</td>'
                             +  '<td>'+data[i].failAvgCycle.toFixed(2)+'</td></tr>';
                    }
                    $('#tblWorkHandle>tbody').html(html);

                    for(i in data){
                        var classRow = $('.row_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_'));
                        var classNo = $('.no_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_'));
                        var classPlan = $('.row_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'_plan');
                        var classRPlan = $('.row_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'_rPlan');
                        //var classWT = $('.row_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'_wt');
                        if(classRow.length > 1){
                            for(let j = 0; j< classRow.length; j++){
                                classRow[0].rowSpan = classRow.length;
                                classPlan[0].rowSpan = classRow.length;
                                classRPlan[0].rowSpan = classRow.length;
                                //classWT[0].rowSpan = classRow.length;
                                if(j > 0){
                                    classRow[j].className += ' hidden';
                                    classPlan[j].className += ' hidden';
                                    classRPlan[j].className += ' hidden';
                                    //classWT[j].className += ' hidden';
                                }
                                classNo[j].innerText = Number(j+1);
                            }
                        } else{
                            classNo[0].innerText = '1';
                        }
                    }
               }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').css('display', 'none');
                //dataset.timeout = setTimeout(loadData, 300000);
            }
        });
    }

    function loadDetail(modelName,groupName){
        $('#spGroupName').html('<b>'+modelName+' - '+groupName+'</b>');
        $('#modalGroupDetail').modal('show');
        $('.loader').css('display', 'block');
        $.ajax({
            type: "GET",
            url: "/api/test/cycle-time",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan,
                modelName: modelName,
                groupName: groupName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
               $('#tblGroupDetail>tbody').html();
               if(!$.isEmptyObject(data)){
                    var html = '';
                    for(i in data){
                        html += '<tr><td class="rowDetail_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'">'+data[i].modelName+'</td>'
                             +  '<td class="noDetail_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_')+'"></td>'
                             +  '<td>'+data[i].groupName+'</td>'
                             +  '<td>'+data[i].stationNumber+'</td>'
                             +  '<td>'+data[i].realPlan+'</td>'
                             +  '<td>'+data[i].plan+'</td>'
                             +  '<td>'+data[i].workTime+'</td>'
                             +  '<td>'+data[i].uph+'</td>'
                             +  '<td>'+data[i].testerDemand+'</td>'
                             +  '<td>'+data[i].testerExist+'</td>'
                             +  '<td>'+data[i].testerDiff+'</td>'
                             +  '<td>'+data[i].operationTime+'</td>'
                             +  '<td>'+data[i].pingTime+'</td>'
                             +  '<td>'+data[i].cycle+'</td>'
                             +  '<td>'+data[i].totalAvgCycle.toFixed(2)+'</td>'
                             +  '<td>'+data[i].passAvgCycle.toFixed(2)+'</td>'
                             +  '<td>'+data[i].failAvgCycle.toFixed(2)+'</td></tr>';
                    }
                    $('#tblGroupDetail>tbody').html(html);

                    for(i in data){
                        var classRow = $('.rowDetail_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_'));
                        var classNo = $('.noDetail_'+data[i].modelName.replace(/\s|\.|\(|\)/g,'_'));
                        if(classRow.length > 1){
                            for(let j = 0; j< classRow.length; j++){
                                classRow[0].rowSpan = classRow.length;
                                if(j > 0){
                                    classRow[j].className += ' hidden';
                                }
                                classNo[j].innerText = Number(j+1);
                            }
                        } else{
                            classNo[0].innerText = '1';
                        }
                    }
               }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').css('display', 'none');
            }
        });
    }

    function setPlan(model){
        $('#modalSetPlan').modal('show');
        $('.loader').css('display', 'block');
        $('#addModelPlan').html(model);
        $.ajax({
            type: "GET",
            url: "/api/test/model/plan/tmp",
            data: {
                factory: dataset.factory,
                sectionName: "SI",
                modelName: model,
                type: "DAILY",
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
               $('#tblSetPlan>tbody').html('');
               if(!$.isEmptyObject(data)){
                    var html = '';
                    for (i in data) {
                        html += '<tr id="row'+data[i].id+'">'
                        // + '<td><div id="txtEditLine'+data[i].id+'">' + data[i].lineName + '</div></td>'
                        + '<td><div id="txtEditModel'+data[i].id+'">' + data[i].modelName + '</div></td>'
                        + '<td><div id="txtEditMO'+data[i].id+'">' + data[i].mo + '</div></td>'
                        + '<td><input id="txtEditPlan'+data[i].id+'" class="form-control hidden" name="plan" type="number" value="' + data[i].plan + '" /><span id="plan'+data[i].id+'">' + data[i].plan + '</span></td>'
                        + '<td><input id="txtEditWT'+data[i].id+'" class="form-control hidden" name="workTime" type="number" value="' + data[i].workTime + '" /><span id="workTime'+data[i].id+'">' + data[i].workTime + '</span></td>'
                        // + '<td><input id="txtEditCT'+data[i].id+'" class="form-control hidden" name="cycleTime" type="number" value="' + data[i].cycleTime + '" /><span id="cycleTime'+data[i].id+'">' + data[i].cycleTime + '</span></td>'
                        + '<td><div id="txtEditShift'+data[i].id+'">' + data[i].shift + '</div></td>'
                        + '<td><div id="txtEditTime'+data[i].id+'">' + data[i].shiftTime + '</div></td>'
                        + '<td>'
                        + '<button class="btn btn-warning" id="editRow'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="editPlan('+data[i].id+')">'
                        + '<i class="icon icon-pencil"></i>'
                        + '</button>'
                        + '<button class="btn btn-success hidden" id="confirmRow'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmPlan(\'' + data[i].modelName + '\','+data[i].id+')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button><button class="btn btn-danger hidden" id="cancelRow'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEditPlan('+data[i].id+')">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    }
                    $('#tblSetPlan>tbody').html(html);
               }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').css('display', 'none');
            }
        });
    }

    function addNewPlanClick() {
        $('#btnAddNewPlan').attr('disabled', 'disabled');
        var model = $('#addModelPlan').html();
        var html = '<tr id="rowAdd">'
            // + '<td><div id="lineAdd"></div></td>'
            + '<td><div id="modelAdd">' + model + '</div></td>'
            + '<td><input id="txtAddMO" class="form-control" name="mo" type="text"/></td>'
            + '<td><input id="txtAddPlan" class="form-control" name="plan" type="number"/></td>'
            + '<td><input id="txtAddWT" class="form-control" name="workTime" type="number"/></td>'
            + '<td>'
            + '<select id="txtAddShift" class="form-control" name="shift"><option value="DAY">DAY</option><option value="NIGHT">NIGHT</option></select>'
            + '</td>'
            + '<td><input id="txtAddShiftTime" class="form-control daterange-single" name="shiftTime" type="text" value="" /></td>'
            + '<td>'
            + '<button class="btn btn-success " id="addPlan" title="Add" style="padding: 4px 10px;" onclick="AddNewPlan(\'' + model + '\')">'
            + '<i class="icon icon-checkmark"></i>'
            + '</button>'
            + '<button class="btn btn-danger " id="cancelAdd" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelAddPlan()">'
            + '<i class="icon icon-spinner11"></i>'
            + '</button>'
            + '</td>'
            + '</tr>';
        $("#tblSetPlan tbody").append(html);
        
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
    //event 
    function editPlan(id) {
        $("#editRow"+id).addClass("hidden");
        $("#confirmRow"+id).removeClass("hidden");
        $("#cancelRow"+id).removeClass("hidden");

        $("#plan"+id).addClass("hidden");
        $("#txtEditPlan"+id).removeClass("hidden");

        $("#workTime"+id).addClass("hidden");
        $("#txtEditWT"+id).removeClass("hidden");

        // $("#cycleTime"+id).addClass("hidden");
        // $("#txtEditCT"+id).removeClass("hidden");
    }
    function cancelEditPlan(id) {
        $("#editRow"+id).removeClass("hidden");
        $("#confirmRow"+id).addClass("hidden");
        $("#cancelRow"+id).addClass("hidden");

        $("#workTime"+id).removeClass("hidden");
        $("#txtEditWT"+id).addClass("hidden");

        // $("#cycleTime"+id).removeClass("hidden");
        // $("#txtEditCT"+id).addClass("hidden");

        $("#plan"+id).removeClass("hidden");
        $("#txtEditPlan"+id).addClass("hidden");

        $(".btn-warning").removeAttr("disabled");
    }

    function cancelAddPlan() {
        $('#btnAddNewPlan').removeAttr('disabled');
        $('#rowAdd').remove();
    }

    function confirmPlan(modelName, id) {
        var editPlan = Number($('#txtEditPlan'+id).val());
        var editWT = $('#txtEditWT'+id).val();
        var editMO = $('#txtEditMO'+id).html();
        // editCT = $('#txtEditCT'+id).val();
        var model = $('#txtEditModel'+id).html();
        var editShift = $('#txtEditShift'+id).html();
        var editShiftTime = $('#txtEditTime'+id).html();
        var idPlanConfirm = Number(id);
        data = {
            factory: dataset.factory,
            modelName: modelName,
            mo: editMO,
            plan: editPlan,
            workTime: editWT,
            shift: editShift,
            shiftTime: editShiftTime,
            id: idPlanConfirm,
            type: 'DAILY'
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/model/plan/tmp',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response) {
                    setPlan(modelName);
                    loadData();
                }
                else {
                    alert("Set Plan Fail!! Please Try Again!");
                    setPlan(modelName);
                }

                delete this.response;
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
        // }
    }
    function AddNewPlan() {
        var addPlan = Number($('#txtAddPlan').val());
        var addWT = $('#txtAddWT').val();
        var addModel = $('#modelAdd').html();
        var addMO = $('#txtAddMO').val();
        var addShift = $('#txtAddShift').val();
        var addShiftTime = $('#txtAddShiftTime').val();
        // var addCT = $('#txtAddCT').val();
        var data = {
            factory: dataset.factory,
            modelName: addModel,
            plan: addPlan,
            workTime: addWT,
            mo: addMO,
            shift: addShift,
            shiftTime: addShiftTime,
            id: 0,
            type: "DAILY"
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/model/plan/tmp',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response) {
                    loadData();
                    setPlan(addModel);
                    $('#btnAddNewPlan').removeAttr('disabled');
                }
                else {
                    alert("Set Plan Fail!! Please Try Again!");
                    setPlan(addModel);
                }
                delete this.response;
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
    }

    function setPcas(model,group){
        $('#modalSetPcas').modal('show');
        $('.loader').css('display', 'block');
        $('#addModel').html(model);
        $('#addGroup').html(group);
        $.ajax({
            type: "GET",
            url: "/api/test/model/pcas",
            data: {
                factory: dataset.factory,
                modelName: model,
                type: "DAILY",
                timeSpan: dataset.timeSpan,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $('#tblSetPcas>tbody').html('');
               if(!$.isEmptyObject(data)){
                    var html = '';
                    for (i in data) {
                        html += '<tr id="row'+data[i].id+'">'
                        + '<td><div id="txtEditModel'+data[i].id+'">' + data[i].modelName + '</div></td>'
                        + '<td><input id="txtEditGroup'+data[i].id+'" class="form-control hidden" name="groupName" type="text" value="' + data[i].groupName + '" /><span id="groupName'+data[i].id+'">' + data[i].groupName + '</span></td>'
                        + '<td><input id="txtEditWorkTime'+data[i].id+'" class="form-control hidden" name="workTime" type="text" value="' + data[i].workTime + '" /><span id="workTime'+data[i].id+'">' + data[i].workTime + '</span></td>'
                        + '<td><input id="txtEditExist'+data[i].id+'" class="form-control hidden" name="testerExist" type="number" value="' + data[i].testerExist + '" /><span id="testerExist'+data[i].id+'">' + data[i].testerExist + '</span></td>'
                        + '<td><input id="txtEditOperation'+data[i].id+'" class="form-control hidden" name="operatorTime" type="number" value="' + data[i].operatorTime + '" /><span id="operatorTime'+data[i].id+'">' + data[i].operatorTime + '</span></td>'
                        + '<td><input id="txtEditPing'+data[i].id+'" class="form-control hidden" name="pingTime" type="number" value="' + data[i].pingTime + '" /><span id="pingTime'+data[i].id+'">' + data[i].pingTime + '</span></td>'
                        + '<td><input id="txtEditCT'+data[i].id+'" class="form-control hidden" name="cycleTime" type="number" value="' + data[i].cycleTime + '" /><span id="cycleTime'+data[i].id+'">' + data[i].cycleTime + '</span></td>'
                        + '<td>'
                        + '<button class="btn btn-warning" id="editPcas'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="editPcas('+data[i].id+')">'
                        + '<i class="icon icon-pencil"></i>'
                        + '</button>'
                        + '<button class="btn btn-success hidden" id="confirmPcas'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmPcas(\'' + data[i].modelName + '\','+data[i].id+')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button><button class="btn btn-danger hidden" id="cancelPcas'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEditPcas('+data[i].id+')">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    }
                    $('#tblSetPcas>tbody').html(html);
               }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                $('.loader').css('display', 'none');
            }
        });
    }

    function addNewPcasClick() {
        $('#btnAddNewPcas').attr('disabled', 'disabled');
        var model = $('#addModel').html();
        var group = $('#addGroup').html();
        var html = '<tr id="rowAdd">'
            + '<td><input id="txtAddModel" class="form-control" name="modelName" type="text" value="'+model+'" /></td>'
            + '<td><input id="txtAddGroup" class="form-control" name="groupName" type="text" value="'+group+'" /></td>'
            + '<td><input id="txtAddWT" class="form-control" name="workTime" type="number" /></td>'
            + '<td><input id="txtAddTE" class="form-control" name="testerExist" type="number"/></td>'
            + '<td><input id="txtAddOT" class="form-control" name="operatorTime" type="number"/></td>'
            + '<td><input id="txtAddPT" class="form-control" name="pingTime" type="number"/></td>'
            + '<td><input id="txtAddCT" class="form-control" name="cycleTime" type="number"/></td>'
            + '<td>'
            + '<button class="btn btn-success " id="addPcas" title="Add" style="padding: 4px 10px;" onclick="AddNewPcas()">'
            + '<i class="icon icon-checkmark"></i>'
            + '</button>'
            + '<button class="btn btn-danger " id="cancelAdd" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelAddPcas()">'
            + '<i class="icon icon-spinner11"></i>'
            + '</button>'
            + '</td>'
            + '</tr>';
        $("#tblSetPcas tbody").append(html);
    }
    //event 
    function editPcas(id) {
        $("#editPcas"+id).addClass("hidden");
        $("#confirmPcas"+id).removeClass("hidden");
        $("#cancelPcas"+id).removeClass("hidden");

        $("#groupName"+id).addClass("hidden");
        $("#txtEditGroup"+id).removeClass("hidden");

        $("#workTime"+id).addClass("hidden");
        $("#txtEditWorkTime"+id).removeClass("hidden");

        $("#testerExist"+id).addClass("hidden");
        $("#txtEditExist"+id).removeClass("hidden");

        $("#pingTime"+id).addClass("hidden");
        $("#txtEditOperation"+id).removeClass("hidden");

        $("#operatorTime"+id).addClass("hidden");
        $("#txtEditPing"+id).removeClass("hidden");

        $("#cycleTime"+id).addClass("hidden");
        $("#txtEditCT"+id).removeClass("hidden");
    }
    function cancelEditPcas(id) {
        $("#editPcas"+id).removeClass("hidden");
        $("#confirmPcas"+id).addClass("hidden");
        $("#cancelPcas"+id).addClass("hidden");

        $("#groupName"+id).removeClass("hidden");
        $("#txtEditGroup"+id).addClass("hidden");

        $("#workTime"+id).removeClass("hidden");
        $("#txtEditWorkTime"+id).addClass("hidden");

        $("#testerExist"+id).removeClass("hidden");
        $("#txtEditExist"+id).addClass("hidden");

        $("#pingTime"+id).removeClass("hidden");
        $("#txtEditOperation"+id).addClass("hidden");

        $("#operatorTime"+id).removeClass("hidden");
        $("#txtEditPing"+id).addClass("hidden");

        $("#cycleTime"+id).removeClass("hidden");
        $("#txtEditCT"+id).addClass("hidden");

        $(".btn-warning").removeAttr("disabled");
    }

    function cancelAddPcas() {
        $('#btnAddNewPcas').removeAttr('disabled');
        $('#rowAdd').remove();
    }
    function confirmPcas(modelName, id) {
        editGroupName = $('#txtEditGroup'+id).val();
        editWT = Number($('#txtEditWorkTime'+id).val());
        editTE = Number($('#txtEditExist'+id).val());
        editOT = Number($('#txtEditOperation'+id).val());
        editPT = Number($('#txtEditPing'+id).val());
        editCT = Number($('#txtEditCT'+id).val());
        var idPcasConfirm = Number(id);
        data = {
            factory: dataset.factory,
            modelName: modelName,
            groupName: editGroupName,
            workTime: editWT,
            testerExist: editTE,
            operatorTime: editOT,
            pingTime: editPT,
            cycleTime: editCT,
            id: idPcasConfirm,
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/model/pcas',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response) {
                    setPcas(modelName,editGroupName);
                    loadData();
                }
                else {
                    alert("Set PCAS Fail!! Please Try Again!");
                    setPcas(modelName, editGroupName);
                }

                delete this.response;
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
        // }
    }
    function AddNewPcas() {
        var addModelName = $('#txtAddModel').val();
        var addGroupName = $('#txtAddGroup').val();
        var addWT = $('#txtAddWT').val();
        var addTE = $('#txtAddTE').val();
        var addOT = $('#txtAddOT').val();
        var addPT = $('#txtAddPT').val();
        var addCT = $('#txtAddCT').val();
        var data = {
            factory: dataset.factory,
            modelName: addModelName,
            groupName: addGroupName,
            workTime: addWT,
            testerExist: addTE,
            operatorTime: addOT,
            pingTime: addPT,
            cycleTime: addCT,
            id: 0,
            type: "DAILY"
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/model/pcas',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                if (response) {
                    setPcas(addModelName, addGroupName);
                    loadData();
                    $('#btnAddNewPcas').removeAttr('disabled');
                }
                else {
                    alert("Set PCAS Fail!! Please Try Again!");
                    setPcas(addModelName, addGroupName);
                }
                delete this.response;
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
    }


    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30';
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30';
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
        $('#modalSetPlan').on('hidden.bs.modal', function() {
            cancelAddPlan();
        });
    });
</script>