<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<style>
    .file-caption{
        height: 50px;
    }
</style>
<div class="row" style="padding: 10px; background-color: #fff;">
    <div>
        <ul class="nav nav-tabs" style="margin-bottom: 10px;">
            <li class="active"><a data-toggle="tab" href="#management" style="font-size:13px; padding: 8px;">ENGINEER MANAGEMENT</a></li>
            <li><a data-toggle="tab" href="#status" style="font-size:13px; padding: 8px;">ENGINEER TOP 3</a></li>
            <!-- <li><a data-toggle="tab" href="#statistics" style="font-size:13px; padding: 8px;">ENGINEER VALUE</a></li> -->
            
        </ul>
        <div class="tab-content">
            <div id="management" class="tab-pane active">
                <div class="row" style="margin: 0 0 10px 0;">
                    <div class="col-md-2 col-xs-3 no-padding">
                        <select class="form-xs form-control bootstrap-select" data-style="btn-xs" name="filter-shift">
                            <option value="ALL" selected=true>ALL</option>
                            <option value="DAY">DAY</option>
                            <option value="NIGHT">NIGHT</option>
                        </select>
                    </div>
                    <div class="col-md-1 col-xs-1">
                        <button class="btn btn-xs" href="#modal-resource-edit" data-toggle="modal" onclick="initResourceEditModal()"><i class="icon-plus2"></i></button>
                    </div>
                    <div class="col-md-7 col-xs-3"></div>
                    <div class="col-md-2 col-xs-5 form-group no-padding no-margin">
                        <input id="txtFilterResource" class="form-control" type="text" placeholder="Filter" style="background: #f0f0f0; width: 100%; padding: 0px 10px; border-radius: 4px; height: 27px;" />
                    </div>    
                </div>
                <!-- <div class="row" style="margin-bottom: 10px">
                    <div class="col-lg-4" id="chart-resource-1" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <div class="col-lg-4" id="chart-resource-2" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <div class="col-lg-4" id="chart-resource-3" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                </div> -->
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
            <div id="statistics" class="tab-pane">
                <div class="input-group" style="margin: 0px 0px 10px 0px;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control datetimerange" side="right" name="timeSpan1" style="height: 26px; color: inherit;">
                </div>
                <div class="row" style="margin: 0 0 10px 0;">
                    <div class="col-sm-2">
                        <select class="form-xs form-control bootstrap-select" data-style="btn-xs bg-gray" name="filter-action">
                        </select>
                    </div>
                    <div class="col-sm-2">
                        <select class="form-xs form-control bootstrap-select" data-style="btn-xs bg-gray" name="filter-resource">
                        </select>
                    </div>
                    <button class="btn btn-xs" data-href="/api/test/resource-value" onclick="hrefWindow(this.dataset.href)"><i class="icon-download4"></i></button>
                </div>
                <div class="col-lg-6" id="chart-action-time" style="height: 400px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                <div class="col-lg-6" id="chart-action-result" style="height: 400px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
            </div>
            <div id="status" class="tab-pane">
                <div>
                    <div class="col-sm-9">
                        <div class="input-group">
                            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <select class="form-control bootstrap-select" data-style="btn-xs bg-gray" name="sort">
                            <option value="ALPHABET">ALL</option>
                            <option value="ACTION">ICT</option>
                            <option value="SUCCESS">L+G</option>
                            <option value="TIME">TE-Auto</option>
                            <option value="TIME">OTT/FPT</option>
                        </select>
                    </div>
                </div>
                <ul class="col-lg-6 media-list" style="overflow: auto; height: 600px;">
                    <li class="resource">
                        <div class="rcontent">
                            <div class="col-sm-6 information">
                                <div class="row">
                                    <div class="col-xs-1" style="width: 26px; height: 26px; position: relative; top: 38px; padding: 2px 8px; margin-left: 5px;">
                                        <label style="margin: unset;">1</label>
                                    </div>
                                    <div class="col-xs-3" style="padding: 0px 3px;">
                                        <img class="avatar" src="/assets/images/avatar-default-icon.png" alt>
                                    </div>
                                    <div class="col-xs-8 form-horizontal" style="font-size: 12px; padding-top: 8px; margin-left: -8px; margin-right: -8px">
                                        <div class="form-group sm-padding">
                                            <label class="col-xs-12 text-bold" name="name" style="padding: 0px 10px; margin: unset;">Nguyễn Đức Tiến</label>
                                        </div>
                                        <div class="form-group sm-padding">
                                            <label class="col-xs-12" name="employeeNo" style="padding: 0px 10px; margin: unset;">V0946495</label>
                                        </div>
                                        <div class="form-group sm-padding">
                                            <label class="col-xs-12" name="experience" style="padding: 0px 10px; margin: unset;">2018/11/10</label>
                                        </div>
                                        <div class="form-group sm-padding">
                                            <label class="col-xs-12" name="level" style="padding: 0px 10px; margin: unset;">S</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6" id="chart-V0946495" style="height: 100px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                        </div>
                    </li>
                </ul>
                <div class="col-lg-6">
                    <div class="col-sm-6" id="chart-overview-tasks" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <div class="col-sm-6" id="chart-top-3-action" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <div class="col-sm-6" id="chart-top-3-result" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                    <div class="col-sm-6" id="chart-top-3-time" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                </div>
            </div>
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
<script>
    function initResourceEditModal(employeeNo) {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/' + employeeNo,
            data: {
            },
            success: function(data){
                console.log(data);
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
        data.append('groupLevel', $('#modal-resource-edit select[name="groupLevel"]').val());
        data.append('lineName', $('#modal-resource-edit input[name="lineOwner"]').val());
        data.append('modelName', $('#modal-resource-edit input[name="projectOwner"]').val());
        data.append('shift', $('#modal-resource-edit select[name="shift"]').val());
        data.append('experienceDetail', $('#modal-resource-edit textarea[name="detail"]').val().replace(/\r\n|\r|\n/g,"<br />"));
        data.append('workOffDay', $('#modal-resource-edit select[name="workOffDay"]').val());
        data.append('workStatus', $('#modal-resource-edit select[name="workStatus"]').val());

        file = $('#modal-resource-edit input[name=avatar-file]').get(0);
        if (file.files.length > 0) {
            data.append('avatarFile', file.files[0]);
        }

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
</script>
<!-- /Modal Edit -->

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
<script>
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
</script>
<!-- /Modal Delete -->

<script>
    var dataset = {
        factory: '${factory}'
    }

    init();

    function init() {
        loadInformation();

        loadResourceByLevel();
        loadResourceByAction();

        loadStatistics();

        loadResourceStatus();
        drawChartOverview();
    }

    function loadInformation() {
        loadResource();
        // loadResourceExperience(1, 'chart-resource-1', 'ENGINEER LEVEL 1', "#7cb5ec");
        // loadResourceExperience(2, 'chart-resource-2', 'ENGINEER LEVEL 2', "#f7a35c");
        // loadResourceExperience(3, 'chart-resource-3', 'ENGINEER LEVEL 3', "#90ee7e");
    }

    function loadStatistics() {
        loadStatisticsResourceByTime();
        loadStatisticsResourceByResult();
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
                        rows+='<tr '+(data[i].workStatus?'':'style="background-color: #cccccc;"')+'><td>'+(1 + Number(i))+'</td><td>'+data[i].shift+'</td><td>'+data[i].employeeNo+'</td><td>'+data[i].name+'('+data[i].chineseName+')</td><td>'+data[i].phone+'</td><td>'+data[i].level+'</td><td>'+data[i].dem+'</td><td>'+groupLevelName(data[i].groupLevel)+'</td><td>'+convertNull(data[i].lineName)+'</td><td title="'+convertNull(data[i].modelName)+'">'+projectOwner+'</td><td>'+data[i].experienceDetail+'</td><td>'+convertWorkOffDates(data[i].workOffDay)+'</td><td style="text-align: center;">' +
                              '<button class="btn" data-id="'+data[i].id+'" data-employee-no="'+data[i].employeeNo+'" href="#modal-resource-edit" data-toggle="modal" style="width: 26px; height: 26px; padding: 3px; margin: 2px; background:#ffc107;" onclick="initResourceEditModal(this.dataset.employeeNo)"><i class="fa fa-edit"></i></button>' +
                              '<button class="btn btn-danger" data-id="'+data[i].id+'" data-employee-no="'+data[i].employeeNo+'" href="#modal-resource-delete" data-toggle="modal" style="width: 26px; height: 26px; padding: 3px; margin: 2px;" onclick="initResourceDeleteModal(this.dataset.id, this.dataset.employeeNo)"><i class="fa fa-trash-o"></i></button>' +
                              '</td></tr>';
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

    function convertNull(str){
        var strCon = str;
        if(str === null){
            strCon = "";
        }
        return strCon;
    }

    function loadResourceByLevel() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/level',
            data: {
                factory: dataset.factory,
                level: 1
            },
            success: function(data){
                var html = '<option value="ALL">ALL</option>';

                for (i in data) {
                    html += '<option value="' + data[i].employeeNo + '">' + data[i].employeeNo + '</option>';
                }

                $('.bootstrap-select[name="filter-resource"]').html(html);
                $('.bootstrap-select[name="filter-resource"]').selectpicker('refresh');
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadResourceByAction() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/action',
            data: {
                factory: dataset.factory
            },
            success: function(data){
                var html = '<option value="ALL">ALL</option>';

                var keys = Object.keys(data);
                for (i in keys) {
                    html += '<option value="' + keys[i] + '">' + data[keys[i]] + '</option>';
                }

                $('.bootstrap-select[name="filter-action"]').html(html);
                $('.bootstrap-select[name="filter-action"]').selectpicker('refresh');
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadStatisticsResourceByTime() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/time',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var actions = new Array();
                var success = new Array();
                var times = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var keys = Object.keys(data);
                    var index = 0;
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            categories[j] = value[j].employeeNo;
                            actions[index] = {y: value[j].taskNumber, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            success[index] = {y: value[j].taskSuccess, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            times[index] = {y: value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }
                }

                Highcharts.chart('chart-action-time', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'ENGINEER ACTION BY TIME'
                    },
                    xAxis: {
                        categories: groups,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: [{
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    }, {
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4,
                        labels: {
                            format: '{value} (m)',
                        },
                        opposite: true
                    }],
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    legend: {
                        enabled: true
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    tooltip: {
                        pointFormat: 'Name: {point.vnName}({point.chineseName})<br/>Level: {point.level}<br/>{series.name}: {point.y}</b>'
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{
                        name: 'Action Times',
                        data: actions
                    }, {
                        yAxis: 1,
                        name: 'Time',
                        data: times
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadStatisticsResourceByResult() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/result',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var actions = new Array();
                var success = new Array();
                var times = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var keys = Object.keys(data);
                    var index = 0;
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            categories[j] = value[j].employeeNo;
                            actions[index] = {y: value[j].taskNumber, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            success[index] = {y: value[j].taskSuccess, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            times[index] = {y: value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }
                }

                Highcharts.chart('chart-action-result', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'ENGINEER ACTION BY RESULT'
                    },
                    xAxis: {
                        categories: groups,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: [{
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    }],
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    legend: {
                        enabled: true
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
                        pointFormat: 'Name: {point.vnName}({point.chineseName})<br/>Level: {point.level}<br/>{series.name}: {point.y}</b>'
                    },
                    series: [{
                        name: 'Action Times',
                        data: actions
                    }, {
                        name: 'Success',
                        color: '#90ee7e',
                        data: success
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadStatisticsOfResourceByTime(employeeNo) {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/time/'+employeeNo,
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var actions = new Array();
                var times = new Array();
                var categories = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    categories = Object.keys(data);
                    for(i in categories){
                        var value = data[categories[i]];
                        actions[i] = {y: value[0]};
                        times[i] = {y: value[1]};
                    }
                }

                Highcharts.chart('chart-action-time', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: employeeNo + ' ENGINEER ACTION BY TIME'
                    },
                    xAxis: {
                        categories: categories,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: [{
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    }, {
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4,
                        labels: {
                            format: '{value} (m)',
                        },
                        opposite: true
                    }],
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    legend: {
                        enabled: true
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{
                        name: 'Action Times',
                        data: actions
                    }, {
                        yAxis: 1,
                        name: 'Time',
                        data: times
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadStatisticsOfResourceByResult(employeeNo) {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/result/'+employeeNo,
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var actions = new Array();
                var success = new Array();
                var categories = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    categories = Object.keys(data);

                    for(i in categories){
                        var value = data[categories[i]];
                        actions[i] = {y: value[0]};
                        success[i] = {y: value[1]};
                    }
                }

                Highcharts.chart('chart-action-result', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: employeeNo + ' ENGINEER ACTION BY RESULT'
                    },
                    xAxis: {
                        categories: categories,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: [{
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    }],
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    legend: {
                        enabled: true
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{
                        name: 'Action Times',
                        data: actions
                    }, {
                        name: 'Success',
                        color: '#90ee7e',
                        data: success
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadResourceExperience(level, chart, name, color) {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/experience',
            data: {
                factory: dataset.factory,
                shift: dataset.shift,
                level: level
            },
            success: function(data){
                var experiences = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var keys = Object.keys(data);
                    var index = 0;
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            categories[j] = value[j].employeeNo;
                            experiences[index] = {y: value[j].experienceYears, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }
                }

                Highcharts.chart(chart, {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: name
                    },
                    xAxis: {
                        type: 'category',
                        categories: groups,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ''
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    },
                    legend: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    tooltip: {
                        pointFormat: 'Name: {point.vnName}({point.chineseName})<br/>Level: {point.level}<br/>{series.name}: {point.y}</b>'
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{
                        name: 'Experience Years',
                        color: color,
                        data: experiences
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadResourceStatus() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/status',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.rsTimeSpan,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var html = '';
                var actions = new Array();
                var success = new Array();
                var times = new Array();
                var idleTimes = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var index = 0;
                    var keys = Object.keys(data);
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            categories[j] = value[j].employeeNo;
                            actions[index] = {name: value[j].employeeNo, y: value[j].taskNumber, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            success[index] = {name: value[j].employeeNo, y: value[j].taskSuccess, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            times[index] = {name: value[j].employeeNo, y: value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            idleTimes[index] = {name: value[j].employeeNo, y: 300-value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};

                            html +=  '<li class="resource">'+
                                     '<div class="rcontent">'+
                                         '<div class="col-xs-12 col-sm-6 information">'+
                                             '<div class="row">'+
                                                 '<div class="col-xs-2 rank '+ranking(index+1)+'">'+
                                                     '<label style="margin: unset;">'+(index+1)+'</label>'+
                                                 '</div>'+
                                                 '<div class="col-xs-3" style="padding: 0px 3px;">'+
                                                     '<div class="avatar">'+
                                                        (value[j].avatar != null ? '<img class="" src="'+value[j].avatar+'" alt>' : '')+
                                                     '</div>'+
                                                 '</div>'+
                                                 '<div class="row col-xs-8 form-horizontal" style="font-size: 12px; padding-top: 8px; margin-left: -8px; margin-right: -8px">'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 text-bold" name="name" style="padding: 0px 10px; margin: unset;">'+value[j].name+'('+value[j].chineseName+')'+'</label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 " name="employeeNo" style="padding: 0px 10px; margin: unset;">'+value[j].employeeNo+'</label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 " name="experience" style="padding: 0px 10px; margin: unset;">'+value[j].experience+'</label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-6 " name="level" style="padding: 0px 10px; margin: unset;">'+value[j].level+'</label>'+
                                                         '<label class="col-xs-6 text-right" name="level" style="padding: 0px 10px; margin: unset;">'+value[j].status+'</label>'+
                                                     '</div>'+
                                                 '</div>'+
                                             '</div>'+
                                         '</div>'+
                                         '<div class="col-xs-12 col-sm-6" id="chart-'+index+'" style="height: 100px;"></div>'+
                                     '</div>'+
                                 '</li>';

                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }

                    $('.media-list').html(html);
                    for(i=0; i<index; i++) {
                        drawChartStatistics('chart-' + i, [actions[i]], [success[i]], [times[i]], [idleTimes[i]]);
                    }
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function drawChartStatistics(chartId, actions, success, times, idleTimes) {
        Highcharts.chart(chartId, {
            chart: {
                type: 'bar',
                spacing: [10, 20, 10, 20],
                margin: [0, 0, 0, 0],
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: ''
            },
            xAxis: {
                labels:{
                    enabled: false
                },
                gridLineWidth: 0,
                minorGridLineWidth: 0,
            },
            yAxis: [{
                min: 0,
                title: {
                    text: ''
                },
                minorGridLineWidth: 0,
                minTickInterval: 4,
                tickAmount: 5
            }, {
                min: 0,
                title: {
                    text: ''
                },
                minorGridLineWidth: 0,
                minTickInterval: 60,
                tickAmount: 5,
                labels: {
                    format: '{value} (m)',
                },
                opposite: true
            }],
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: true,
                        inside: true,
                        align: 'left',
                        format: '{series.name} - {point.y}'
                    }
                },
                series: {
                    groupPadding: 0
                }
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
            series: [{
                name: 'Action',
                data: actions
            }, {
                name: 'Success',
                data: success
            }, {
                yAxis: 1,
                name: 'Time',
                data: times
            }, {
                yAxis: 1,
                name: 'Idle Time',
                data: idleTimes
            }]
        });
    }

    function ranking(rank) {
        if (rank == 1)
            return "rank-1";
        else if (rank < 4)
            return "rank-2";
        return "rank-3";
    }

    function drawChartOverview() {
        $.ajax({
            type: 'GET',
            url: '/api/test/tracking/status',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.rsTimeSpan
            },
            success: function(data){

                Highcharts.chart('chart-overview-tasks',{
                    chart:{
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type:'pie',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'TASKS STATUS'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f}%',
                                inside: true,
                                distance: -10

                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: [{name: 'Auto Closed', y: data.auto_closed}, {name: 'Done', y: data.done}, {name: 'Not Yet', y: data.not_yet}]
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
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });

        $.ajax({
            type: 'GET',
            url: '/api/test/resource/status/top3',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId,
                timeSpan: dataset.rsTimeSpan
            },
            success: function(data){
                var dataChart = [];
                var keys = Object.keys(data);
                for (i in keys) {
                    var value = data[keys[i]];
                    dataChart.push({name: keys[i], y: value});
                }

                Highcharts.chart('chart-top-3-action',{
                    chart:{
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type:'pie',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'TOP 3 ENGINEER BY ACTION TIMES'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f}%',
                                inside: true,
                                distance: -10

                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
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
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });

        $.ajax({
            type: 'GET',
            url: '/api/test/resource/result/top3',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId,
                timeSpan: dataset.rsTimeSpan
            },
            success: function(data){
                var dataChart = [];
                var keys = Object.keys(data);
                for (i in keys) {
                    var value = data[keys[i]];
                    dataChart.push({name: keys[i], y: value});
                }

                Highcharts.chart('chart-top-3-result',{
                    chart:{
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type:'pie',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'TOP 3 ENGINEER BY RESULT'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f}%',
                                inside: true,
                                distance: -10
                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
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
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });

        $.ajax({
            type: 'GET',
            url: '/api/test/resource/time/top3',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId,
                timeSpan: dataset.rsTimeSpan
            },
            success: function(data){
                var dataChart = [];
                var keys = Object.keys(data);
                for (i in keys) {
                    var value = data[keys[i]];
                    dataChart.push({name: keys[i], y: value});
                }

                Highcharts.chart('chart-top-3-time',{
                    chart:{
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type:'pie',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'TOP 3 ENGINEER BY TIME'
                    },
                    xAxis: {
                        type: 'category'
                    },
                    yAxis: {
                        title: {
                            text: ''
                        },
                        labels: {
                            enabled: false
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f}%',
                                inside: true,
                                distance: -10
                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
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
            },
            failure: function(errMsg) {
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
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var startDate1 = moment(current).add(-6,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
                $('.datetimerange[name="timeSpan1"]').data('daterangepicker').setStartDate(new Date(startDate1));
                $('.datetimerange[name="timeSpan1"]').data('daterangepicker').setEndDate(new Date(endDate));
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    $("#txtFilterResource").on("keyup", function() {
        var value = $(this).val().toLowerCase();
       $("table[name='resource']>tbody>tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $(document).ready(function() {
        getTimeNow();
        // var spcTS = getTimeSpan(6);
        // $('.datetimerange[name="timeSpan1"]').data('daterangepicker').setStartDate(spcTS.startDate);
        // $('.datetimerange[name="timeSpan1"]').data('daterangepicker').setEndDate(spcTS.endDate);

        // var rsTS = getTimeSpan();
        // $('.datetimerange[name="timeSpan"]').data('daterangepicker').setStartDate(rsTS.startDate);
        // $('.datetimerange[name="timeSpan"]').data('daterangepicker').setEndDate(rsTS.endDate);

        $('select[name="filter-shift"]').on('change', function() {
            dataset.shift = this.value;
            loadInformation();
        });

        $('select[name="filter-action"]').on('change', function() {
            if (this.value == 'ALL') {
                dataset.solutionId = null;
            } else {
                dataset.solutionId = this.value;
            }
            loadStatistics();
        });

        $('select[name="filter-resource"]').on('change', function() {
            if (this.value == 'ALL') {
                loadStatistics();
            } else {
                loadStatisticsOfResourceByResult(this.value);
                loadStatisticsOfResourceByTime(this.value);
            }
        });

        $('input[name=timeSpan]').on('change', function(event) {
            dataset.rsTimeSpan = event.target.value;
            loadResourceStatus();
            drawChartOverview();
        });
    });
</script>
