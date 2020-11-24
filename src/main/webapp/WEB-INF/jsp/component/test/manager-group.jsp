<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<div class="row">
    <div class="col-lg-12" style="background: #ffffff;">
        <div class="panel panel-overview" id="header" style="font-size: 15px;">
            <span class="text-uppercase">MANAGEMENT MODEL</span>
        </div>
        <div class="row" style="margin: unset;">
            <div class="row" style="margin: 0 0 10px 0;">
                <div class="col-md-2 col-xs-3 no-padding hidden">
                </div>
                <div class="col-md-1 col-xs-1 no-padding">
                    <button class="btn btn-xs" id="btnAdd" href="#modal-group-edit" data-toggle="modal"  onclick="edit()">
                        <i class="icon-plus2"></i>
                    </button>
                </div>
                <div class="col-md-7 col-xs-3"></div>
                <div class="col-md-2 col-xs-5 form-group no-padding no-margin" style="float: right;">
                    <input id="txtSearch" class="form-control" type="search" placeholder="Search" style="background: #f0f0f0; width: 100%; padding: 0px 10px; border-radius: 4px; height: 30px;" />
                </div>    
            </div>
            <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 200px);">
                <table class="table table-xxs table-bordered table-sticky" id="tblDetail">
                    <thead>
                        <tr>
                            <th style="width: 3%;">STT</th>
                            <th>Customer</th>
                            <th>Model Name</th>
                            <th>Customer Name</th>
                            <th style="width: 14%;">Stage</th>
                            <th>Sub-Stage</th>
                            <th>Visible</th>
                            <th style="z-index: 1;width: 10%;">Action</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- Model-Add -->
<!-- <div id="modal-group-model" class="modal fade" role="dialog">
<div class="modal-dialog" style="font-size: 13px;">
    <div class="modal-content" style="background-color: #333; color: #ccc;">
        <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
            <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
            <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">ADD NEW</h6>
        </div>
        <div class="modal-body">
            <div class="row">
                <div class="form-group row no-margin">
                    <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Model Name</label>
                    <div class="col-xs-9">
                        <input type="text" name="modelName" class="form-control">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group row no-margin">
                    <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Customer Name</label>
                    <div class="col-xs-9">
                        <input type="text" name="customerName" class="form-control">
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
            <button class="btn btn-sm" data-dismiss="modal" onclick="addNew();">Submit<i class="icon-arrow-right14 position-right"></i></button>
        </div>
    </div>
</div>
</div> -->
<!-- Set Model -->
<div class="modal fade" id="modalSetModel" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;color: #ccc;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">
                    SETUP GROUP BY MODEL NAME <span id="idModelSetUp"></span>
                    <button class="btn btn-default btn-xs" onclick="addGroupSetting()"><i class="fa fa-plus"></i></button>
                </h6>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 200px);">
                    <table id="tblModelMeta" class="table table-bordered table-hover-dark table-xxs table-sticky" style="margin-bottom: 10px; color: #fff; text-align: center;">
                        <thead>
                        </thead>
                        <tbody style="color: #333;"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal Edit -->
<div id="modal-group-edit" class="modal fade" role="dialog">
	<div class="modal-dialog" style="font-size: 13px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">EDIT</h6>
            </div>
        	<div class="modal-body">
                <div class="row">
                    <input type="hidden" name="id" value=""/>
                    <div class="form-group row no-margin class-factory">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Factory</label>
                        <div class="col-xs-9">
                            <input type="text" name="factory" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Customer</label>
                        <div class="col-xs-9">
                            <input type="text" name="customer" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Model Name</label>
                        <div class="col-xs-9">
                            <input type="text" name="modelName" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Customer Name</label>
                        <div class="col-xs-9">
                            <input type="text" name="customerName" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Stage</label>
                        <div class="col-xs-9">
                            <input type="text" name="stage" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Sub Stage</label>
                        <div class="col-xs-9">
                            <input type="text" name="subStage" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Visibled</label>
                        <div class="col-xs-9">
                            <select class="form-control bootstrap-select" name="visible">
                                <option value="true">True</option>
                                <option value="false">False</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="postGroup();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
	</div>
</div>

<!-- Modal Add -->
<div id="modal-add-group" class="modal fade" role="dialog">
	<div class="modal-dialog" style="font-size: 13px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">ADD</h6>
            </div>
        	<div class="modal-body">
                <div class="row">
                    <input type="hidden" name="id" value=""/>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Customer</label>
                        <div class="col-xs-9">
                            <input type="text" name="customer" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Model Name</label>
                        <div class="col-xs-9">
                            <input type="text" name="modelName" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Group Name</label>
                        <div class="col-xs-9">
                            <input type="text" name="groupName" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Stage</label>
                        <div class="col-xs-9">
                            <input type="text" name="stage" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Sub Stage</label>
                        <div class="col-xs-9">
                            <input type="text" name="subStage" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Step</label>
                        <div class="col-xs-9">
                            <input type="text" name="step" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Remark</label>
                        <div class="col-xs-9">
                            <select class="form-control bootstrap-select" name="remark">
                                <option value="">NONE</option>
                                <option value="IN">IN</option>
                                <option value="OUT">OUT</option>
                                <option value="WIP">WIP</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Target Retest Rate</label>
                        <div class="col-xs-9">
                            <input type="text" name="target" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Bonus Retest Rate</label>
                        <div class="col-xs-9">
                            <input type="text" name="bonus" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold pt-5">Visibled</label>
                        <div class="col-xs-9">
                            <select class="form-control bootstrap-select" name="visible">
                                <option value="true">True</option>
                                <option value="false">False</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="submitAddGroup();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
	</div>
</div>

<style>
    .disableSelect {
        display: none;
    }
    .btn {
        transition: all 0.5s;
    }
    #tblDetail{
        font-size: 14px;
    }
    #tblDetail thead tr th{
        padding: 3px 3px !important;
    }
    #tblDetail tbody tr td{
        padding: 3px 3px !important;
    }
    #tblDetail tbody tr td input, #tblModelMeta tbody tr td input{
        color: #333 !important;
        padding-left: 3px;
    }
    #tblModelMeta thead tr th{
        color: #333;
    }
    .btn-xs {
        width: 30px;
        height: 30px;
        padding: 3px;
        margin: 0px 2px;
    }
    #tblModelMeta thead tr th {
        padding: 3px 3px;
    }
    #tblModelMeta tbody tr td {
        padding: 3px 3px;
    }
    #tblModelMeta tbody tr td select {
        color: #333 !important;
    }
    .btnEditStyle{
        background-color: #FFC107;
        width: 26px;
        height: 26px;
        padding: 3px;
        margin: 2px;
        color:#333;
    }
</style>
<script>
    //0-> in/out 1->in 2->none 3->out
    var dataset = {
        factory: '${factory}'
    }
    var dataModelName = [];
    loadData();
    function loadData() {
        $.ajax({
            type: 'GET',
            url: '/api/test/model/list',
            data: {
                factory: dataset.factory
            },
            success: function(data) {
                createDataTable(data);
                dataModelName = data;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    function createDataTable(data) {
        $("#tblDetail>tbody").html("");
        var html = '';
        var stt = 0;
        if(dataset.factory=='UNKNOWN'){
            $("#tblDetail>thead").html("<tr><th style='width: 3%;'>STT</th><th>Factory</th><th>Customer</th><th>Model Name</th><th>Customer Name</th><th style='width: 14%;'>Stage</th><th>Sub-Stage</th><th style='z-index: 1;width: 10%;'>Action</th></tr>");
            for (i in data) {
                stt++;
                var valueUnknown = data[i].factory;
                if(valueUnknown == 'UNKNOWN'){
                    valueUnknown='';
                }else{
                    valueUnknown = data[i].factory;
                }
                html += '<tr id="row' + data[i].id + '">' +
                    // '<td class="hidden"><span id="groupId'+stt+'" class="hidden">' + data[i].id +'</span></td>'+
                    '<td><span id="groupId'+stt+'" class="hidden">' + data[i].id +'</span>' + stt + '</td>' +
                    '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditFactory' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].factory + '" /><span id="spfactory' + data[i].id + '">' + convertNull(valueUnknown) + '</span></td>' +
                    '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditCus' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].customer + '" /><span id="spCustomer' + data[i].id + '">' + convertNull(data[i].customer) + '</span></td>' +
                    '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditModel' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].modelName + '" /><span id="spModelName' + data[i].id + '">' + convertNull(data[i].modelName) + '</span></td>' +
                    '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditCusName' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' +  convertNull(data[i].customerName) + '" /><span id="spCustomerName' + data[i].id + '">' + convertNull(data[i].customerName) + '</span></td>' +
                    '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditStage' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].stage + '" /><span id="spStage' + data[i].id + '">' + convertNull(data[i].stage) + '</span></td>' +
                    '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditSubStage' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].subStage + '" /><span id="spSubStage' + data[i].id + '">' + convertNull(data[i].subStage) + '</span></td>' +
                    '<td>'
                    +'<button class="btn btnEditStyle" href="#modal-group-edit" data-toggle="modal" id="editRow' + data[i].id + '" onclick="edit(' + data[i].id + ')">'
                    +    '<i class="fa fa-edit"></i>'
                    +'</button>' +
                    '<button class="btn btn-danger" id="deleteRow' + data[i].id + '" title="Delete" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="deleteModel(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button>' +
                    '<button class="btn btn-success hidden" id="confirmRow' + data[i].id + '" title="Confirm" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="confirmModel(' + data[i].id + ')"><i class="icon icon-checkmark"></i></button>' + 
                    '<button class="btn btn-danger hidden" id="cancelRow' + data[i].id + '" title="Cancel" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="cancelEdit(' + data[i].id + ')"><i class="icon icon-spinner11"></i></button></td></tr>';
            }
        }else{
            for (i in data) {
            stt++;
            html += '<tr id="row' + data[i].id + '">' +
                '<td><span id="groupId'+stt+'" class="hidden">' + data[i].id +'</span>' + stt + '</td>' +
                // '<td hidden style="cursor: pointer;"><input id="txtEditFactory' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].factory + '" /><span id="spfactory' + data[i].id + '">' + convertNull(data[i].factory) + '</span></td>' +
                '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditCus' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].customer + '" /><span id="spCustomer' + data[i].id + '">' + convertNull(data[i].customer) + '</span></td>' +
                '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditModel' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].modelName + '" /><span id="spModelName' + data[i].id + '">' + convertNull(data[i].modelName) + '</span></td>' +
                '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditCusName' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' +  convertNull(data[i].customerName) + '" /><span id="spCustomerName' + data[i].id + '">' + convertNull(data[i].customerName) + '</span></td>' +
                '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditStage' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].stage + '" /><span id="spStage' + data[i].id + '">' + convertNull(data[i].stage) + '</span></td>' +
                '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer;"><input id="txtEditSubStage' + data[i].id + '" class="form-control hidden" type="text" style="color: #fff" value="' + data[i].subStage + '" /><span id="spSubStage' + data[i].id + '">' + convertNull(data[i].subStage) + '</span></td>';
                // if(data[i].visible){
                //     html += '<td><input id="cbxVisible' + data[i].id + '" type="checkbox" checked="checked" data-id="'+data[i].id+'" disabled="disabled" style="width: 2rem; height: 2rem" /></td>';
                // } else{
                //     html += '<td><input id="cbxVisible' + data[i].id + '" type="checkbox" data-id="'+data[i].id+'" disabled="disabled" style="width: 2rem; height: 2rem" /></td>';
                // }
                html += '<td data-toggle="modal" data-target="#modalSetModel" onclick="showSetModel(\'' + data[i].modelName + '\',\'' + data[i].customer + '\')" style="cursor: pointer; text-transform: uppercase;"><span id="spVisible' + data[i].id + '">' + convertNull(data[i].visible) + '</span></td>'
                html += '<td>'
                +'<button class="btn btnEditStyle" href="#modal-group-edit" data-toggle="modal" id="editRow' + data[i].id + '" onclick="edit(' + data[i].id + ')">'
                +    '<i class="fa fa-edit"></i>'
                +'</button>' +
                '<button class="btn btn-danger" id="deleteRow' + data[i].id + '" title="Delete" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="deleteModel(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button>' +
                '<button class="btn btn-success hidden" id="confirmRow' + data[i].id + '" title="Confirm" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="confirmModel(' + data[i].id + ')"><i class="icon icon-checkmark"></i></button>' + 
                '<button class="btn btn-danger hidden" id="cancelRow' + data[i].id + '" title="Cancel" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="cancelEdit(' + data[i].id + ')"><i class="icon icon-spinner11"></i></button></td></tr>';
            }
        }
        $("#tblDetail>tbody").html(html);
    }

    function convertNull(str) {
        var value = '';
        if (str !== null && str !== undefined) {
            value = str;
        }
        return value;
    }

    function setDataModels() {
        var models = $('input[name="models"]');
        for (var i = 0; i < models.length; i++) {
            dataModelName.push(models[i].value);
        }
    }

    function save() {
        console.log(dataModelName);
        var data = {
            factory: dataset.factory,
            //models: $('input[name="models"]').tagsinput('items')
            models: dataModelName
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/model/list',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                // $('button[name="save"]').attr('disabled', 'disabled');
                // $('button[name="cancel"]').attr('disabled', 'disabled');
                cancel();
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function cancel() {
        location.reload();
    }

    // function addNewClick() {
    //     $('#btnAdd').attr('disabled', 'disabled');
    //     var html = '<tr id="newRow"><td></td><td><input id="txtAddCustomer" class="form-control" type="text" style="color: #fff" /></td>' +
    //         '<td><input id="txtAddModel" class="form-control" type="text" style="color: #fff" /></td>' +
    //         '</td><td><input id="txtAddCustomerName" class="form-control" type="text" style="color: #fff" /></td>' +
    //         '</td><td><input id="txtAddStage" class="form-control" type="text" style="color: #fff" /></td>' +
    //         '</td><td><input id="txtSubStage" class="form-control" type="text" style="color: #fff" /></td>' +
    //         '<td><button class="btn btn-success" title="Add" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="addNew()"><i class="icon icon-checkmark"></i></button>' +
    //         '<button class="btn btn-danger" title="Cancel" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="cancelAddNew()"><i class="icon icon-cross"></i></button></td></tr>';
    //     $("#tblDetail>tbody").append(html);
    //     $("#txtAddCustomer").focus();
    // }

    function addNew() {
        $("#txtAdd").remove("hidden");
        var data = {
            factory: dataset.factory,
            customer: $("#txtAddCustomer").val(),
            modelName: $("#txtAddModel").val(),
            customerName: $("#txtAddCustomerName").val(),
            stage: $("#txtAddStage").val(),
            subStage: $("#txtAddSubStage").val(),
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/model/list',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                alert('ADD NEW OK!');
                cancel();
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }
    function cancelAddNew() {
        $("#newRow").remove();
        $('#btnAdd').removeAttr('disabled');
    }

    function edit(stt) {
        $('.class-factory').addClass('hidden');
        if(stt == 0){
            if(dataset.factory=='UNKNOWN'){
                $('.class-factory').removeClass('hidden');
                $('#modal-group-edit input[name="factory"]').val("");
            }else{
                $(".class-factory").addClass('hidden');
            }
            $('#modal-group-edit input[name="id"]').val("");
            $('#modal-group-edit input[name="customer"]').val("");
            $('#modal-group-edit input[name="modelName"]').val("");
            $('#modal-group-edit input[name="customerName"]').val("");
            $('#modal-group-edit input[name="stage"]').val("");
            $('#modal-group-edit input[name="subStage"]').val("");
        }else{
            if(dataset.factory=='UNKNOWN'){
                $('.class-factory').removeClass('hidden');
                $('#modal-group-edit input[name="factory"]').val($('#spfactory'+stt).html());
            }else{
                $(".class-factory").addClass('hidden');
            }
            $('#modal-group-edit input[name="id"]').val($('#groupId'+stt).html());
            $('#modal-group-edit input[name="customer"]').val($("#spCustomer" + stt).html());
            $('#modal-group-edit input[name="modelName"]').val($("#spModelName" + stt).html());
            $('#modal-group-edit input[name="customerName"]').val($("#spCustomerName" + stt).html());
            $('#modal-group-edit input[name="stage"]').val($("#spStage" + stt).html());
            $('#modal-group-edit input[name="subStage"]').val($("#spSubStage" + stt).html());
            $('#modal-group-edit select[name="visible"]').val($("#spVisible" + stt).html());
            $('#modal-group-edit select[name="visible"]').selectpicker('refresh');
        }
    }
    function postGroup(){
        if (dataset.factory== 'UNKNOWN') {
            var valueFactory = $('#modal-group-edit input[name="factory"]').val();
            if(valueFactory==''){
                valueFactory = 'UNKNOWN';
            }
            var arr={
                id: $('#modal-group-edit input[name="id"]').val(),
                factory: valueFactory,
                customer: $('#modal-group-edit input[name="customer"]').val(),
                modelName: $('#modal-group-edit input[name="modelName"]').val(),
                customerName: $('#modal-group-edit input[name="customerName"]').val(),
                stage: $('#modal-group-edit input[name="stage"]').val(),
                subStage: $('#modal-group-edit input[name="subStage"]').val(),
                visible: $('#modal-group-edit select[name="visible"]').val(),
            };
        } else {
            var arr={
                id: $('#modal-group-edit input[name="id"]').val(),
                factory: dataset.factory,
                customer: $('#modal-group-edit input[name="customer"]').val(),
                modelName: $('#modal-group-edit input[name="modelName"]').val(),
                customerName: $('#modal-group-edit input[name="customerName"]').val(),
                stage: $('#modal-group-edit input[name="stage"]').val(),
                subStage: $('#modal-group-edit input[name="subStage"]').val(),
                visible: $('#modal-group-edit select[name="visible"]').val(),
            };
        }
        $.ajax({
            type: "POST",
            url: "/api/test/model/list",
            data: JSON.stringify(arr),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(data){
                    alert("Edit Success!!");
                    loadData();
                } else{
                    alert("Edit faile!!");
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    function cancelEdit(stt) {
        $("#deleteRow" + stt).removeClass("hidden");
        $("#editRow" + stt).removeClass("hidden");
        $("#confirmRow" + stt).addClass("hidden");
        $("#cancelRow" + stt).addClass("hidden");

        $("#spCustomer" + stt).removeClass("hidden");
        $("#txtEditCus" + stt).addClass("hidden");
        $("#spModelName" + stt).removeClass("hidden");
        $("#txtEditModel" + stt).addClass("hidden");
        $("#spCustomerName" + stt).removeClass("hidden");
        $("#txtEditCusName" + stt).addClass("hidden");
        $("#spStage" + stt).removeClass("hidden");
        $("#txtEditStage" + stt).addClass("hidden");
        $("#spSubStage" + stt).removeClass("hidden");
        $("#txtEditSubStage" + stt).addClass("hidden");
    }

    function confirmModel(stt) {
        var data = {
            id: stt,
            factory: dataset.factory,
            customer: $("#txtEditCus").val(),
            customerName: $("#txtEditCusName" + stt).val(),
            modelName: $("#txtEditModel" + stt).val(),
            stage: $("#txtEditStage" + stt).val(),
            subStage: $("#txtEditSubStage" + stt).val(),
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/model/list',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                alert('UPDATE OK!');
                cancel();
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function deleteModel(stt) {
        var del = window.confirm("Do you want to DELETE?");
        if (del == true) {
            var data = {
                id: stt,
                factory: dataset.factory,
                customer: $("#txtEditCus").val(),
                customerName: $("#txtEditCusName" + stt).val(),
                modelName: $("#txtEditModel" + stt).val(),
                stage: $("#txtEditStage" + stt).val(),
                subStage: $("#txtEditSubStage" + stt).val(),
            }
            $.ajax({
                type: 'DELETE',
                url: '/api/test/model/list',
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    alert('DELETE OK!');
                    cancel();
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
    }
    function showSetModel(model,customer) {
        $.ajax({
            type: "GET",
            url: "/api/test/group/list",
            data: {
                factory: dataset.factory,
                customer: customer,
                modelName: model
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var html = '';
                var stt = 0;
                if (data != null && data != undefined) {
                    var thead = '<tr>' +
                        '<th style="width: 1%;">STT</th>' +
                        '<th>Customer</th>' +
                        '<th>Model Name</th>' +
                        '<th>Group Name</th>' +
                        '<th>Stage</th>' +
                        '<th>Sub Stage</th>' +
                        '<th>Step</th>' +
                        '<th style="width: 8%; z-index: 10;">Remark</th>' +
                        '<th>Target Retest Rate</th>' +
                        '<th>Bonus Retest Rate</th>' +
                        '<th style="width: 7%;">Visible</th>' +
                        '<th style="width: 10%; z-index: 10;">Action</th>' +
                        '</tr>';
                    $("#tblModelMeta thead").html(thead);
                    $("#tblModelMeta tbody").html("");
                    for (i in data) {
                        var remark = ""
                        if (data[i].remark == null || data[i].remark == 2) {
                            remark = "NONE";
                        }
                        if (data[i].remark == 0) {
                            remark = "IN/OUT";
                        }
                        if (data[i].remark == 1) {
                            remark = "IN";
                        }
                        if (data[i].remark == 3) {
                            remark = "OUT";
                        }
                        // var valueOptionRemark = '<option value="2">NONE</option>' +
                        //                         '<option value="1">IN</option>' +
                        //                         '<option value="3">OUT</option>' +
                        //                         '<option value="0">IN/OUT</option>';
                        var valueOptionRemark = '<option value="">NONE</option>' +
                                            '<option value="IN">IN</option>' +
                                            '<option value="OUT">OUT</option>' +
                                            '<option value="WIP">WIP</option>';
                        var tmpGroup, tmpStep, tmpTarget, tmpBonus;
                        tmpGroup = convertNull(data[i].groupName);
                        tmpStep = convertNull(data[i].step);
                        tmpTarget = convertNull(data[i].targetRetestRate);
                        tmpBonus = convertNull(data[i].bonusRetestRate);
                        tmpVisible = convertNull(data[i].visible);
                        var valueOption;
                        // console.log("true/false", tmpVisible);
                        if (tmpVisible == true) {
                            valueOption = '<option value="true" selected>True</option><option value="false">False</option>';
                        } else {
                            valueOption = '<option value="true">True</option><option value="false" selected>False</option>';
                        }
                        stt++;
                        html = '<tr id="row' + data[i].id + '">' +
                            '<td>' + stt + '</td>' +
                            '<td><span id="spSetupCustomer' + data[i].id + '">' + convertNull(data[i].customer) + '</span></td>' +
                            '<td><input type="text" class="form-control hidden" id="txtSetupModelName' + data[i].id + '" value="' + convertNull(data[i].modelName) + '" /><span id="spSetupModelName' + data[i].id + '">' + convertNull(data[i].modelName) + '</span></td>' +
                            '<td><input type="text" class="form-control hidden" id="txtSetupGroup' + data[i].id + '" value="' + tmpGroup + '" /><span id="spSetupGroup' + data[i].id + '">' + tmpGroup + '</span></td>' +
                            '<td><input type="text" class="form-control hidden" id="txtSetupStage' + data[i].id + '" value="' + convertNull(data[i].stage) + '" /><span id="spSetupStage' + data[i].id + '">' + convertNull(data[i].stage) + '</span></td>' +
                            '<td><input type="text" class="form-control hidden" id="txtSetupSubStage' + data[i].id + '" value="' + convertNull(data[i].subStage) + '" /><span id="spSetupSubStage' + data[i].id + '">' + convertNull(data[i].subStage) + '</span></td>' +
                            '<td><input type="number" min=0 class="form-control hidden" id="txtSetupStep' + data[i].id + '" value="' + tmpStep + '" /><span id="spSetupStep' + data[i].id + '">' + tmpStep + '</span></td>' +
                            '<td><select id="slSetupRemark' + data[i].id + '" class="form-control bootstrap-select" multiple="multiple" disabled="disabled">' +valueOptionRemark +'</select></td>' +
                            '<td><input type="number" min=0 step="0.01" class="form-control hidden" id="txtSetupTarget' + data[i].id + '" value="' + tmpTarget + '" /><span id="spSetupTarget' + data[i].id + '">' + tmpTarget + '</span></td>' +
                            '<td><input type="number" min=0 step="0.01" class="form-control hidden" id="txtSetupBonus' + data[i].id + '" value="' + tmpBonus + '" /><span id="spSetupBonus' + data[i].id + '">' + tmpBonus + '</span></td>' +
                            '<td><select id="slSetupVisible' + data[i].id + '" class="form-control hidden">' +valueOption +'</select><span id="spSetupVisible' + data[i].id + '">' + tmpVisible + '</span></td>' +
                            '<td>' +
                            '<button class="btn btn-warning group-edit" id="btnSetupGroup' + data[i].id + '" title="Edit" style="background-color: #FFC107; width: 26px; height: 26px; padding: 3px; margin: 2px;color:#333;" onclick="editGroup(' + data[i].id + ')">' +
                            '<i class="fa fa-edit"></i>' +
                            '</button>' +
                            '<button class="btn btn-danger group-edit" id="btnDeleteGroup' + data[i].id + '" title="Edit" style="width: 26px; height: 26px; padding: 3px; margin: 2px;color:#333;" onclick="deleteGroup(' + data[i].id + ')">' +
                            '<i class="fa fa-trash"></i>' +
                            '</button>' +
                            '<button class="btn btn-success hidden" id="btnConfirmSetupGroup' + data[i].id + '" title="Confirm" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="confirmEditGroup(' + data[i].id + ')">' +
                            '<i class="icon icon-checkmark"></i>' +
                            '</button><button class="btn btn-danger hidden" id="btnCancelSetupGroup' + data[i].id + '" title="Cancel" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="cancelEditGroup(' + data[i].id + ')">' +
                            '<i class="icon icon-spinner11"></i>' +
                            '</button>' +
                            '</td>' +
                            '</tr>';
                        $("#tblModelMeta tbody").append(html);
                        $("#slSetupRemark"+data[i].id).val(data[i].remark);
                        $('#slSetupRemark' + data[i].id).val(data[i].remark);
                        $('#slSetupRemark' + data[i].id).selectpicker('refresh');
                    }
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    $("#modalSetModel").on('hide.bs.modal', function() {
        $(".btn-edit-class").removeClass("disabled");
    });

    function editGroup(id) {
        $("#btnSetupGroup" + id).addClass("hidden");
        $("#btnDeleteGroup" + id).addClass("hidden");
        $("#btnConfirmSetupGroup" + id).removeClass("hidden");
        $("#btnCancelSetupGroup" + id).removeClass("hidden");

        $("#spSetupModelName" + id).addClass("hidden");
        $("#txtSetupModelName" + id).removeClass("hidden");
        $("#spSetupGroup" + id).addClass("hidden");
        $("#txtSetupGroup" + id).removeClass("hidden");
        $("#spSetupStage" + id).addClass("hidden");
        $("#txtSetupStage" + id).removeClass("hidden");
        $("#spSetupSubStage" + id).addClass("hidden");
        $("#txtSetupSubStage" + id).removeClass("hidden");
        $("#spSetupSubStage" + id).addClass("hidden");
        $("#txtSetupSubStage" + id).removeClass("hidden");
        $("#spSetupStep" + id).addClass("hidden");
        $("#txtSetupStep" + id).removeClass("hidden");
        $("#spSetupTarget" + id).addClass("hidden");
        $("#txtSetupTarget" + id).removeClass("hidden");
        $("#spSetupBonus" + id).addClass("hidden");
        $("#txtSetupBonus" + id).removeClass("hidden");
        $("#spSetupVisible" + id).addClass("hidden");
        $("#slSetupVisible" + id).removeClass("hidden");
        // $("#spSetupRemark" + id).addClass("hidden");
        // $("#slSetupRemark" + id).removeClass("hidden");
        $("#slSetupRemark" + id).removeAttr("disabled");
        $('#slSetupRemark' + id).selectpicker('refresh');

        $(".group-edit").attr("disabled", "disabled");
        // $(".select-shift").attr("disabled", "disabled");
    }

    function cancelEditGroup(id) {
        $("#btnSetupGroup" + id).removeClass("hidden");
        $("#btnDeleteGroup" + id).removeClass("hidden");
        $("#btnConfirmSetupGroup" + id).addClass("hidden");
        $("#btnCancelSetupGroup" + id).addClass("hidden");

        $("#spSetupModelName" + id).removeClass("hidden");
        $("#txtSetupModelName" + id).addClass("hidden");
        $("#spSetupGroup" + id).removeClass("hidden");
        $("#txtSetupGroup" + id).addClass("hidden");
        $("#spSetupStage" + id).removeClass("hidden");
        $("#txtSetupStage" + id).addClass("hidden");
        $("#spSetupSubStage" + id).removeClass("hidden");
        $("#txtSetupSubStage" + id).addClass("hidden");
        $("#spSetupSubStage" + id).removeClass("hidden");
        $("#txtSetupSubStage" + id).addClass("hidden");
        $("#spSetupStep" + id).removeClass("hidden");
        $("#txtSetupStep" + id).addClass("hidden");
        $("#spSetupTarget" + id).removeClass("hidden");
        $("#txtSetupTarget" + id).addClass("hidden");
        $("#spSetupBonus" + id).removeClass("hidden");
        $("#txtSetupBonus" + id).addClass("hidden");
        $("#spSetupVisible" + id).removeClass("hidden");
        $("#slSetupVisible" + id).addClass("hidden");
        // $("#spSetupRemark" + id).removeClass("hidden");
        // $("#slSetupRemark" + id).addClass("hidden");
        $("#slSetupRemark" + id).attr("disabled","disabled");
        $('#slSetupRemark' + id).selectpicker('refresh');

        $(".group-edit").removeAttr("disabled");
    }

    function confirmEditGroup(id) {
        var confirm = window.confirm("Are you sure CHANGE?");
        if (confirm == true) {
            var editGroup = $('#txtSetupGroup' + id).val();
            var editStep = parseFloat($('#txtSetupStep' + id).val());
            var editTarget = parseFloat($('#txtSetupTarget' + id).val());
            var editBonus = parseFloat($('#txtSetupBonus' + id).val());
            var editVisible = $('#slSetupVisible' + id).val();
            var editRemark = null;
            if($('#slSetupRemark' + id).val() != null){
                editRemark = $('#slSetupRemark' + id).val()[0];
                for(i = 1; i< $('#slSetupRemark' + id).val().length; i++){
                    editRemark += ','+$('#slSetupRemark' + id).val()[i];
                }
            }
            var dataEditGroup = {
                id: id,
                factory: dataset.factory,
                customer: $("#spSetupCustomer" + id).html(),
                modelName: $("#spSetupModelName" + id).html(),
                groupName: editGroup,
                stage: $('#txtSetupStage').val(),
                subStage: $('#txtSetupSubStage').val(),
                step: editStep,
                remark: editRemark,
                targetRetestRate: editTarget,
                bonusRetestRate: editBonus,
                visible: editVisible
            }
            $.ajax({
                type: 'POST',
                url: '/api/test/group/list',
                data: JSON.stringify(dataEditGroup),
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    showSetModel($("#spSetupModelName" + id).html(), $("#spSetupCustomer" + id).html());
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
    }

    $("#txtSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblDetail tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    function addGroupSetting() {
        $('#modal-add-group').modal('show');
    }

    function submitAddGroup() {
        var dataAdd = {
            id: 0,
            factory: dataset.factory,
            customer: $('#modal-add-group input[name="customer"]').val(),
            modelName: $('#modal-add-group input[name="modelName"]').val(),
            groupName: $('#modal-add-group input[name="groupName"]').val(),
            stage: $('#modal-add-group input[name="stage"]').val(),
            subStage: $('#modal-add-group input[name="subStage"]').val(),
            step: $('#modal-add-group input[name="step"]').val(),
            remark: $('#modal-add-group select[name="remark"]').val(),
            targetRetestRate: $('#modal-add-group input[name="target"]').val(),
            bonusRetestRate: $('#modal-add-group input[name="bonus"]').val(),
            visible: $('#modal-add-group select[name="visible"]').val(),
        }
        $.ajax({
            type: 'POST',
            url: '/api/test/group/list',
            data: JSON.stringify(dataAdd),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                if(data) {
                    alert('ADD SUCCESS!');
                    showSetModel($('#modal-add-group input[name="modelName"]').val(), $('#modal-add-group input[name="customer"]').val());
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });

        $('#modal-add-group input[name="customer"]').val("");
        $('#modal-add-group input[name="modelName"]').val("");
        $('#modal-add-group input[name="groupName"]').val("");
        $('#modal-add-group input[name="stage"]').val("");
        $('#modal-add-group input[name="subStage"]').val("");
        $('#modal-add-group input[name="step"]').val("");
        $('#modal-add-group input[name="target"]').val("");
        $('#modal-add-group input[name="bonus"]').val("");
    }

    function deleteGroup(id) {
        var del = window.confirm("Do you want to DELETE?");
        if (del == true) {
            $.ajax({
                type: 'DELETE',
                url: '/api/test/group/list/'+id,
                contentType: "application/json; charset=utf-8",
                success: function(data) {
                    alert('DELETE OK!');
                    cancel();
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
    }
    
$(document).ready(function() {
 //Sort Table By Column
    function sortTable(f,n, idTable){
        var rows = $('#'+idTable+' tbody  tr').get();
        rows.sort(function(a, b) {
            var A = getVal(a);
            var B = getVal(b);

            if(A < B) {
                return -1*f;
            }
            if(A > B) {
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
    $('#tblDetail thead th').click(function(){
        num *= -1;
        var n = $(this).prevAll().length;
        sortTable(num,n,'tblDetail');
    });
});
</script>