<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<style>
    .file-caption{
        height: 50px;
    }
</style>
<div class="row">
    <div class="col-lg-12" style="background: #ffffff;">
        <div class="panel panel-overview" id="header" style="font-size: 14px;">
            <span class="text-uppercase">Engineer Management</span>
        </div>
        <div class="row" style="margin: unset;">
            <div class="row" style="margin: 0 0 10px 0;">
                <div class="col-md-2 col-xs-3 no-padding">
                    <select class="form-xs form-control bootstrap-select" data-style="btn-xs" name="filter-shift">
                        <option value="ALL" selected=true>ALL</option>
                        <option value="DAY">DAY</option>
                        <option value="NIGHT">NIGHT</option>
                    </select>
                </div>
                <div class="col-md-1 col-xs-1">
                    <button class="btn btn-xs" id="btn-add-resource" href="#modal-resource-edit" data-toggle="modal" onclick="initResourceEditModal()"><i class="icon-plus2"></i></button>
                </div>
                <div class="col-md-7 col-xs-3"></div>
                <div class="col-md-2 col-xs-5 form-group no-padding no-margin">
                    <input id="txtFilterResource" class="form-control" type="text" placeholder="Filter" style="background: #f0f0f0; width: 100%; padding: 0px 10px; border-radius: 4px; height: 27px;" />
                </div>    
            </div>
            <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 200px);">
                <table name="resource" class="table table-small table-sticky">
                    <thead>
                        <tr>
                            <th>No</th>
                            <th>Shift</th>
                            <th>Employee No</th>
                            <th>Name</th>
                            <!-- <th>Experience</th> -->
                            <!-- <th>Experience Years</th> -->
                            <th>Phone</th>
                            <th>Level</th>
                            <!-- <th>DEM</th> -->
                            <th>Group</th>
                            <th>Group Level</th>
                            <th>Line Owner</th>
                            <th>Project Owner</th>
                            <th>Detail Experience</th>
                            <!-- <th>Work Status</th> -->
                            <th>Work Off</th>
                            <th style="z-index:1"></th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
<!-- Modal Edit -->
<div id="modal-resource-edit" class="modal fade" role="dialog">
	<div class="modal-dialog" style="font-size: 13px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">NEW / EDIT</h6>
            </div>
        	<div class="modal-body">
                <div class="row">
                    <input type="hidden" name="id" value=""/>
                    <div class="form-group row no-margin">
                        <div class="col-xs-3">
                            <div class="avatar" style="top: 0px;">
                                <img id="editAvatar" src="/assets/images/avatar-default-icon.png" alt="">
                            </div>
                        </div>
                        <div class="col-xs-9">
                            <input type="file" style="height: 75px;" class="file-input" placeholder="Select a Avatar" name="avatar-file" data-show-preview="false" data-show-upload="false" data-browse-class="btn btn-xs" data-remove-class="btn btn-xs" accept="image/*">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Employee No</label>
                        <div class="col-xs-9">
                            <input type="text" name="employeeNo" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Name</label>
                        <div class="col-xs-9">
                            <input type="text" name="name" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Chinese Name</label>
                        <div class="col-xs-3">
                            <input type="text" name="chineseName" class="form-control">
                        </div>
                        <label class="col-xs-2 control-label text-bold" style="padding-top: 8px;">Phone</label>
                        <div class="col-xs-4">
                            <input type="text" name="phone" class="form-control" placeholder="">
                        </div>
                    </div>
                    <!-- <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Avatar</label>
                        <div class="col-xs-9">
                            <input type="file" class="file-input" name="avatar-file" data-show-preview="false" data-show-upload="false" data-browse-class="btn btn-xs" data-remove-class="btn btn-xs" accept="image/*">
                        </div>
                    </div> -->
                    
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Group</label>
                        <div class="col-xs-3">
                            <input type="text" name="dem" class="form-control" placeholder="">
                        </div>
                        <label class="col-xs-2 control-label text-bold" style="padding-top: 8px;">Level</label>
                        <div class="col-xs-4">
                            <input type="text" name="level" class="form-control" placeholder="">
                        </div>
                    </div>
                    <!-- <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Level</label>
                        <div class="col-xs-9">
                            <input type="text" name="level" class="form-control" placeholder="">
                        </div>
                    </div> -->
                    <!-- <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Group</label>
                        <div class="col-xs-9">
                            <input type="text" name="dem" class="form-control" placeholder="">
                        </div>
                    </div> -->
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Group Level</label>
                        <div class="col-xs-9">
                            <select class="form-control bootstrap-select" name="groupLevel">
                                <option value="1">Staff</option>
                                <option value="2">Deputy Shift Leader</option>
                                <option value="3">Shift Leader</option>
                                <option value="4">Leader</option>
                                <option value="5">Manager</option>
                                <option value="6">Senior Manager</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Line Owner</label>
                        <div class="col-xs-9">
                            <input type="text" name="lineOwner" class="form-control" placeholder="">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Project Owner</label>
                        <div class="col-xs-9">
                            <input type="text" name="projectOwner" class="form-control" placeholder="">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Work Status</label>
                        <div class="col-xs-3">
                            <select class="form-control bootstrap-select" name="workStatus">
                                <option value="true">ONLINE</option>
                                <option value="false">OFFLINE</option>
                            </select>
                        </div>
                        <label class="col-xs-2 control-label text-bold" style="padding-top: 8px;">Shift</label>
                        <div class="col-xs-4">
                            <select class="form-control bootstrap-select" name="shift">
                                <option value="DAY">DAY</option>
                                <option value="NIGHT">NIGHT</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Experience Detail</label>
                        <div class="col-xs-9">
                            <textarea class="form-control" name="detail" type="text" value=""></textarea>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Work Off Days</label>
                        <div class="col-xs-9">
                            <select class="form-control bootstrap-select" name="workOffDay">
                                <option value="0">Sunday</option>
                                <option value="1">Monday</option>
                                <option value="2">Tuesday</option>
                                <option value="3">Wednesday</option>
                                <option value="4">Thursday</option>
                                <option value="5">Friday</option>
                                <option value="6">Saturday</option>
                            </select>
                        </div>
                    </div>
                    <!-- <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Work Status</label>
                        <div class="col-xs-9">
                            <select class="form-control bootstrap-select" name="workStatus">
                                <option value="true">ONLINE</option>
                                <option value="false">OFFLINE</option>
                            </select>
                        </div>
                    </div> -->
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="postResource();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
	</div>
</div>
<!-- Modal Delete -->
<div id="modal-resource-delete" class="modal fade" role="dialog">
	<div class="modal-dialog" style="font-size: 13px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">DELETE</h6>
            </div>
        	<div class="modal-body">
                <div class="row">
                    <div class="form-group">
                        <label class="col-xs-12 control-label" style="padding-top: 8px;">Do you want to delete <span name="employeeNo"></span> ?</label>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="deleteResource();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
	</div>
</div>
</div>
<style>
</style>
<script>
     var dataset = {
        factory: '${factory}',
        empId : '${employeeId}',
        empName: '${employeeName}',
        dem: '${employeeDem}',
        level: '${employeeLevel}',
        // employeeShift: '${employeeShift}'
        employeeShift: 'DAY'
    };

    init();

    function init() {
        loadInformation();

    }
    function loadInformation() {
        loadResource();
    }
function loadResource() {
    $.ajax({
        type: 'GET',
        url: '/api/test/resource',
        data: {
            factory: dataset.factory,
            shift: dataset.shift
        },
        success: function(data){
            var selector = $("table[name='resource']>tbody");
            selector.children('tr').remove();
            if (!$.isEmptyObject(data)) { 
                var rows = "";
                for(i in data){
                    var projectOwner = '';
                    if(convertNull(data[i].modelName).length > 25){
                        projectOwner = data[i].modelName.slice(0,25) + '...';
                    } else projectOwner = convertNull(data[i].modelName);
                    if(data[i].workOffDay == new Date().getDay()){
                        data[i].workStatus = false;
                    }
                    if(dataset.level > 3){
                        rows+='<tr '+(data[i].workStatus?'':'style="background-color: #cccccc;"')+'><td>'+(1 + Number(i))+'</td><td>'+data[i].shift+'</td><td>'+data[i].employeeNo+'</td><td>'+data[i].name+'('+data[i].chineseName+')</td><td>'+data[i].phone+'</td><td>'+data[i].level+'</td><td>'+data[i].dem+'</td><td>'+groupLevelName(data[i].groupLevel)+'</td><td>'+convertNull(data[i].lineName)+'</td><td title="'+convertNull(data[i].modelName)+'">'+projectOwner+'</td><td>'+data[i].experienceDetail+'</td><td>'+convertWorkOffDates(data[i].workOffDay)+'</td><td style="text-align: center;">' +
                                '<button class="btn" data-id="'+data[i].id+'" data-employee-no="'+data[i].employeeNo+'" href="#modal-resource-edit" data-toggle="modal" style="width: 26px; height: 26px; padding: 3px; margin: 2px; background:#ffc107;" onclick="initResourceEditModal(this.dataset.employeeNo)"><i class="fa fa-edit"></i></button>' +
                                '<button class="btn btn-danger" data-id="'+data[i].id+'" data-employee-no="'+data[i].employeeNo+'" href="#modal-resource-delete" data-toggle="modal" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="initResourceDeleteModal(this.dataset.id, this.dataset.employeeNo)"><i class="fa fa-trash-o"></i></button>' +
                                '</td></tr>';
                    } else{
                        if(dataset.level == 3 && data[i].shift == dataset.employeeShift){
                            rows+='<tr '+(data[i].workStatus?'':'style="background-color: #cccccc;"')+'><td>'+(1 + Number(i))+'</td><td>'+data[i].shift+'</td><td>'+data[i].employeeNo+'</td><td>'+data[i].name+'('+data[i].chineseName+')</td><td>'+data[i].phone+'</td><td>'+data[i].level+'</td><td>'+data[i].dem+'</td><td>'+groupLevelName(data[i].groupLevel)+'</td><td>'+convertNull(data[i].lineName)+'</td><td title="'+convertNull(data[i].modelName)+'">'+projectOwner+'</td><td>'+data[i].experienceDetail+'</td><td>'+convertWorkOffDates(data[i].workOffDay)+'</td><td style="text-align: center;">' +
                                '<button class="btn" data-id="'+data[i].id+'" data-employee-no="'+data[i].employeeNo+'" href="#modal-resource-edit" data-toggle="modal" style="width: 26px; height: 26px; padding: 3px; margin: 2px; background:#ffc107;" onclick="initResourceEditModal(this.dataset.employeeNo)"><i class="fa fa-edit"></i></button>' +
                                '</td></tr>';
                        // } else if(dataset.level == 3 && data[i].shift == "NIGHT"){
                        //     rows+='<tr '+(data[i].workStatus?'':'style="background-color: #cccccc;"')+'><td>'+(1 + Number(i))+'</td><td>'+data[i].shift+'</td><td>'+data[i].employeeNo+'</td><td>'+data[i].name+'('+data[i].chineseName+')</td><td>'+data[i].phone+'</td><td>'+data[i].level+'</td><td>'+data[i].dem+'</td><td>'+groupLevelName(data[i].groupLevel)+'</td><td>'+convertNull(data[i].lineName)+'</td><td title="'+convertNull(data[i].modelName)+'">'+projectOwner+'</td><td>'+data[i].experienceDetail+'</td><td>'+convertWorkOffDates(data[i].workOffDay)+'</td><td style="text-align: center;">' +
                        //         '<button class="btn" data-id="'+data[i].id+'" data-employee-no="'+data[i].employeeNo+'" href="#modal-resource-edit" data-toggle="modal" style="width: 26px; height: 26px; padding: 3px; margin: 2px; background:#ffc107;" onclick="initResourceEditModal(this.dataset.employeeNo)"><i class="fa fa-edit"></i></button>' +
                        //         '</td></tr>';
                        } else{
                            if(dataset.empId == data[i].employeeNo){
                                rows+='<tr '+(data[i].workStatus?'':'style="background-color: #cccccc;"')+'><td>'+(1 + Number(i))+'</td><td>'+data[i].shift+'</td><td>'+data[i].employeeNo+'</td><td>'+data[i].name+'('+data[i].chineseName+')</td><td>'+data[i].phone+'</td><td>'+data[i].level+'</td><td>'+data[i].dem+'</td><td>'+groupLevelName(data[i].groupLevel)+'</td><td>'+convertNull(data[i].lineName)+'</td><td title="'+convertNull(data[i].modelName)+'">'+projectOwner+'</td><td>'+data[i].experienceDetail+'</td><td>'+convertWorkOffDates(data[i].workOffDay)+'</td><td style="text-align: center;">' +
                                        '<button class="btn" data-id="'+data[i].id+'" data-employee-no="'+data[i].employeeNo+'" href="#modal-resource-edit" data-toggle="modal" style="width: 26px; height: 26px; padding: 3px; margin: 2px; background:#ffc107;" onclick="initResourceEditModal(this.dataset.employeeNo)"><i class="fa fa-edit"></i></button>' +
                                        '</td></tr>';
                            } else{
                                rows+='<tr '+(data[i].workStatus?'':'style="background-color: #cccccc;"')+'><td>'+(1 + Number(i))+'</td><td>'+data[i].shift+'</td><td>'+data[i].employeeNo+'</td><td>'+data[i].name+'('+data[i].chineseName+')</td><td>'+data[i].phone+'</td><td>'+data[i].level+'</td><td>'+data[i].dem+'</td><td>'+groupLevelName(data[i].groupLevel)+'</td><td>'+convertNull(data[i].lineName)+'</td><td title="'+convertNull(data[i].modelName)+'">'+projectOwner+'</td><td>'+data[i].experienceDetail+'</td><td>'+convertWorkOffDates(data[i].workOffDay)+'</td><td style="text-align: center;">' +
                                        '<button class="btn" disabled="disabled" data-id="'+data[i].id+'" data-employee-no="'+data[i].employeeNo+'" href="#modal-resource-edit" data-toggle="modal" style="width: 26px; height: 26px; padding: 3px; margin: 2px; background:#999999;" onclick="initResourceEditModal(this.dataset.employeeNo)"><i class="fa fa-edit"></i></button>' +
                                        '</td></tr>';
                            }
                        }
                        
                        $('#btn-add-resource').addClass('hidden');
                    }
                }
                selector.append(rows);
            } else {
                selector.append('<tr><td colspan="12" align="center">-- NO DATA --</td></tr>');
            }
        },
        failure: function(errMsg) {
                console.log(errMsg);
        },
    });
}
function postResource() {
        var data = new FormData();
        data.append('factory', dataset.factory);
        data.append('id', $('#modal-resource-edit input[name="id"]').val());
        data.append('employeeNo', $('#modal-resource-edit input[name="employeeNo"]').val());
        data.append('name', $('#modal-resource-edit input[name="name"]').val());
        data.append('chineseName', $('#modal-resource-edit input[name="chineseName"]').val());
        data.append('phone', $('#modal-resource-edit input[name="phone"]').val());
        data.append('level', $('#modal-resource-edit input[name="level"]').val());
        data.append('dem', $('#modal-resource-edit input[name="dem"]').val());
        // data.append('groupLevel', $('#modal-resource-edit select[name="groupLevel"]').val());
        data.append('lineName', $('#modal-resource-edit input[name="lineOwner"]').val());
        data.append('modelName', $('#modal-resource-edit input[name="projectOwner"]').val());
        data.append('shift', $('#modal-resource-edit select[name="shift"]').val());
        data.append('experienceDetail', $('#modal-resource-edit textarea[name="detail"]').val().replace(/\r\n|\r|\n/g,"<br />"));
        data.append('workOffDay', $('#modal-resource-edit select[name="workOffDay"]').val());
        data.append('workStatus', $('#modal-resource-edit select[name="workStatus"]').val());

        if(dataset.level > 3){
            data.append('groupLevel', $('#modal-resource-edit select[name="groupLevel"]').val());
        }

        file = $('#modal-resource-edit input[name=avatar-file]').get(0);
        if (file.files.length > 0) {
            data.append('avatarFile', file.files[0]);
        }

        // for(var pair of data.entries()){
        //     console.log(pair[0] + ',' + pair[1]);
        // }
        if(dataset.level >= 3 || dataset.empId == $('#modal-resource-edit input[name="id"]').val()){
            $.ajax({
                type: "POST",
                url: "/api/test/resource",
                data: data,
                processData: false,
                contentType: false,
                mimeType: "multipart/form-data",
                success: function(data){
                    init();
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });
        }
    }
function convertNull(str){
    var strCon = str;
    if(str === null){
        strCon = "";
    }
    return strCon;
}
function groupLevelName(num){
    var name = '';
    if(num == "1"){
        name = "Staff";
    } else if(num == "2"){
        name = "Deputy Shift Leader";
    } else if(num == "3"){
        name = "Shift Leader";
    } else if(num == "4"){
        name = "Leader";
    } else if(num == "5"){
        name = "Manager";
    } else if(num == "6"){
        name = "Senior Manager";
    }
    return name;
}
function convertWorkOffDates(dateNum){
    var dateStr = '';
    if(dateNum == "0"){
        dateStr = 'SUN';
    } else if(dateNum == "1"){
        dateStr = 'MON';
    } else if(dateNum == "2"){
        dateStr = 'TUE';
    } else if(dateNum == "3"){
        dateStr = 'WED';
    } else if(dateNum == "4"){
        dateStr = 'THU';
    } else if(dateNum == "5"){
        dateStr = 'FRI';
    } else if(dateNum == "6"){
        dateStr = 'SAT';
    }
    return dateStr;
}
function initResourceEditModal(employeeNo) {
    $.ajax({
        type: 'GET',
        url: '/api/test/resource/' + employeeNo,
        data: {
        },
        success: function(data){
            // console.log(data);
            if (!$.isEmptyObject(data)) {
                var expDetail;
                if(data.experienceDetail == null){
                    expDetail = "";
                } else{
                    expDetail = data.experienceDetail.replace(/<br\s*\/?>/g,'\n');
                }
                $('#modal-resource-edit input[name="id"]').val(data.id);
                $('#modal-resource-edit input[name="employeeNo"]').val(data.employeeNo);
                $('#modal-resource-edit input[name="name"]').val(data.name);
                $('#modal-resource-edit input[name="chineseName"]').val(data.chineseName);
                $('#modal-resource-edit input[name="phone"]').val(data.phone);
                // if(data.experience != null){
                //     $('#modal-resource-edit input[name="experience"]').data('daterangepicker').setStartDate(data.experience);
                //     $('#modal-resource-edit input[name="experience"]').data('daterangepicker').setEndDate(data.experience);
                // } else{
                //     $('#modal-resource-edit input[name="experience"]').data('daterangepicker').setStartDate(new Date());
                //     $('#modal-resource-edit input[name="experience"]').data('daterangepicker').setEndDate(new Date());
                // }    
                $('#modal-resource-edit select[name="shift"]').val(data.shift);
                $('#modal-resource-edit select[name="shift"]').selectpicker('refresh');
                $('#modal-resource-edit input[name="level"]').val(data.level);
                $('#modal-resource-edit input[name="dem"]').val(data.dem);
                $('#modal-resource-edit textarea[name="detail"]').val(expDetail);
                $('#modal-resource-edit select[name="groupLevel"]').val(data.groupLevel);
                $('#modal-resource-edit select[name="groupLevel"]').selectpicker('refresh');
                $('#modal-resource-edit input[name="lineOwner"]').val(data.lineName);
                $('#modal-resource-edit input[name="projectOwner"]').val(data.modelName);
                $('#modal-resource-edit select[name="workOffDay"]').val(""+data.workOffDay);
                $('#modal-resource-edit select[name="workOffDay"]').selectpicker('refresh');
                $('#modal-resource-edit select[name="workStatus"]').val(""+data.workStatus);
                $('#modal-resource-edit select[name="workStatus"]').selectpicker('refresh');
                if(dataset.level > 3){
                    $('#modal-resource-edit select[name="groupLevel"]').removeAttr('disabled');
                } else{
                    $('#modal-resource-edit select[name="groupLevel"]').attr('disabled','disabled');
                }
                if(data.avatar != null){
                    $('#editAvatar').attr('src',data.avatar);
                    $('#modal-resource-edit input[name=avatar-file]').val(data.avatar);
                } else{
                    $('#editAvatar').attr('src', '/assets/images/avatar-default-icon.png');
                    $('#modal-resource-edit input[name=avatar-file]').val();
                }
            } else {
                $('#modal-resource-edit input[name="id"]').val("0");
                $('#modal-resource-edit input[name="employeeNo"]').val("");
                $('#modal-resource-edit input[name="name"]').val("");
                $('#modal-resource-edit input[name="chineseName"]').val("");
                $('#modal-resource-edit input[name="phone"]').val("");
                $('#modal-resource-edit select[name="shift"]').val("DAY");
                $('#modal-resource-edit select[name="shift"]').selectpicker('refresh');
                $('#modal-resource-edit input[name="level"]').val("");
                $('#modal-resource-edit textarea[name="detail"]').val("");
                $('#modal-resource-edit input[name="dem"]').val("");
                $('#modal-resource-edit select[name="groupLevel"]').selectpicker('refresh');
                $('#modal-resource-edit select[name="workStatus"]').selectpicker('refresh');
                $('#modal-resource-edit input[name="lineOwner"]').val("");
                $('#modal-resource-edit input[name="projectOwner"]').val("");
            }
        },
        failure: function(errMsg) {
                console.log(errMsg);
        },
    });
}
function initResourceDeleteModal(id, employeeNo) {
    dataset.resourceId = id;
    $('#modal-resource-delete span[name="employeeNo"]').html(employeeNo);
}
function deleteResource() {
        $.ajax({
            type: 'DELETE',
            url: '/api/test/resource/'+dataset.resourceId,
            data: {},
            contentType: "application/json; charset=utf-8",
            success: function(data){
                init();
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
}
$("#txtFilterResource").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("table[name='resource']>tbody>tr").filter(function() {
        $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
});
$(document).ready(function() {
    $('select[name="filter-shift"]').on('change', function() {
        dataset.shift = this.value;
        loadInformation();
    });
});
</script>