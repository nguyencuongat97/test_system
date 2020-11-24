<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<div class="panel panel-re panel-flat row" style="margin-bottom: 0px; min-height: calc(100vh - 100px);">
    <div class="col-lg-12">
        <div class="row" style="margin-bottom: 5px; padding: 0">
            <div class="col-md-9 col-xs-12 no-padding" style="margin-bottom: 5px;">
                <div class="col-md-3 col-xs-12">
                    <ul class="nav nav-tabs" style="margin-bottom: 10px;">
                        <!-- <li class="active"><a data-toggle="tab" href="#tab-all-task" style="font-size:13px; padding: 8px;">All Task</a></li> -->
                        <li id="li-online"><a data-toggle="tab" href="#tab-te-online" style="font-size:13px; padding: 8px;">TE-ONLINE</a></li>
                        <li id="li-cft"><a data-toggle="tab" href="#tab-te-cft" style="font-size:13px; padding: 8px;">TE-CFT</a></li>
                        <li id="li-my-task"><a data-toggle="tab" href="#tab-my-task" style="font-size:13px; padding: 8px;">My Task</a></li>
                    </ul>
                </div>
                <div class="col-md-4 col-xs-10">
                    <div class="panel input-group" style="margin-bottom: 5px;">
                        <span class="input-group-addon" style="padding: 0px 4px;"><i class="icon-calendar22"></i></span>
                        <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="border-bottom: 0px !important;">
                    </div>
                </div>
                <div class="col-xs-2 no-padding" style="margin-bottom: 5px;">
                    <button id="btnAddOwner" onclick="showModalEdit(0)" class="panel btn btn-default" style="margin: 0;" ><i class="fa fa-plus"></i></button>
                </div>
            </div>
            <div class="col-md-3 col-xs-12 no-padding">
                <div class="col-md-10 col-xs-10">
                    <div class="panel input-group" style="margin-bottom: 5px; width: 100%;">
                        <input type="text" class="form-control" id="txtSearch" name="search" style="border-bottom: 0px !important; padding: 0 10px;" placeholder="Filter By ID">
                    </div>
                </div>
                <div class="col-xs-2 no-padding">
                    <button class="panel btn btn-default" style="margin: 0;" onclick="filterTask()" disabled="disabled"><i class="fa fa-search"></i></button>
                </div>
            </div> 
        </div>
        <div class="tab-content">
            <!-- <div id="tab-all-task" class="tab-pane active">
                <div class="row" style="margin-bottom: 5px">
                    <div class="col-sm-12">
                        <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                            <table id="tblOwner" class="table table-small table-bordered table-sticky" style="text-align: center">
                                <thead>
                                    <tr>
                                        <th style="width: 3%;">STT</th>
                                        <th style="width: 8%;">ID</th>
                                        <th>Name</th>
                                        <th>Task</th>
                                        <th>Detail</th>
                                        <th>Creator</th>
                                        <th style="width: 5%;">Status</th>
                                        <th>Created</th>
                                        <th>Duedate</th>
                                        <th>Note</th>
                                        <th style="width: 9%; z-index: 1">Comment</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div> -->
            <div id="tab-te-online" class="tab-pane">
                <div class="row" style="margin-bottom: 5px">
                    <div class="col-sm-12">
                        <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                            <table id="tblOnline" class="table table-small table-bordered table-sticky" style="text-align: center">
                                <thead>
                                    <tr>
                                        <th style="width: 3%;">STT</th>
                                        <th style="width: 8%;">ID</th>
                                        <th>Name</th>
                                        <th>Task</th>
                                        <th>Detail</th>
                                        <th>Creator</th>
                                        <th style="width: 5%;">Status</th>
                                        <th>Created</th>
                                        <th>Duedate</th>
                                        <th>Note</th>
                                        <th style="width: 9%; z-index: 1">Comment</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-te-cft" class="tab-pane">
                <div class="row" style="margin-bottom: 5px">
                    <div class="col-sm-12">
                        <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                            <table id="tblCft" class="table table-small table-bordered table-sticky" style="text-align: center">
                                <thead>
                                    <tr>
                                        <th style="width: 3%;">STT</th>
                                        <th style="width: 8%;">ID</th>
                                        <th>Name</th>
                                        <th>Task</th>
                                        <th>Detail</th>
                                        <th>Creator</th>
                                        <th style="width: 5%;">Status</th>
                                        <th>Created</th>
                                        <th>Duedate</th>
                                        <th>Note</th>
                                        <th style="width: 9%; z-index: 1">Comment</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-my-task" class="tab-pane">
                <div class="row" style="margin-bottom: 5px">
                    <div class="col-sm-12">
                        <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
                            <table id="tblMyTask" class="table table-small table-bordered table-sticky" style="text-align: center">
                                <thead>
                                    <tr>
                                        <th style="width: 3%;">STT</th>
                                        <th style="width: 8%;">ID</th>
                                        <th>Name</th>
                                        <th>Task</th>
                                        <th>Detail</th>
                                        <th>Creator</th>
                                        <th style="width: 5%;">Status</th>
                                        <th>Created</th>
                                        <th>Duedate</th>
                                        <th>Note</th>
                                        <th style="width: 9%; z-index: 1">Comment</th>
                                    </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Modal Edit -->
<div id="modal-task-edit" class="modal fade" role="dialog">
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
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Employee No</label>
                        <div class="col-xs-8">
                            <input type="text" name="employeeNo" class="form-control">
                        </div>
                        <div class="col-xs-1 no-padding">
                            <button class="btn btn-xs legitRipple" onclick="searchEmployee()"> <i class="fa fa-search"></i></button>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Name</label>
                        <div class="col-xs-9">
                            <input type="text" name="employeeName" class="form-control" disabled="disabled">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Task</label>
                        <div class="col-xs-9">
                            <textarea class="form-control" name="taskTitle" type="text" value="" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Detail</label>
                        <div class="col-xs-9">
                            <textarea class="form-control" name="detail" type="text" value="" rows="3"></textarea>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Status</label>
                        <div class="col-xs-9">
                            <select class="form-control bootstrap-select" name="status">
                                <option value="ON_GOING">ON_GOING</option>
                                <option value="DONE">DONE</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Duedate</label>
                        <div class="col-xs-9">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="icon-calendar22"></i></span>
                                <input type="text" class="form-control daterange-single" side="right" name="duedate">
                            </div>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Note</label>
                        <div class="col-xs-9">
                            <textarea class="form-control" name="note" type="text" value=""></textarea>
                        </div>
                    </div>
                    <div class="form-group row no-margin hidden">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Tracking ID</label>
                        <div class="col-xs-9">
                            <input type="text" name="trackingId" class="form-control" disabled="disabled">
                        </div>
                    </div>
                    <div class="form-group row no-margin hidden">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Task ID</label>
                        <div class="col-xs-9">
                            <input type="text" name="taskId" class="form-control" disabled="disabled">
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="confirmEditRowOwner();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
	</div>
</div>

<script>
    var dataset = {
        factory: '${factory}',
        empId : '${employeeId}',
        empName: '${employeeName}',
        dem: '${employeeDem}',
        level: '${employeeLevel}'
    };
    
    var employeeIdSearch = '';
    function loadData(){
        if(dataset.level > 3){
            employeeIdSearch = '';
            $('input[name="search"]').val('');
        }
        else{
            employeeIdSearch = dataset.empId;
            $('input[name="search"]').val(dataset.empId);
        }
        $('.empName').html(dataset.empId + ' - ' + dataset.empName);
        loadTaskDaily();
    }

    function loadTaskDaily(){
        if(dataset.dem == "TE-ONLINE"){
            $('#li-online').removeClass('hidden');
            $('#li-cft').addClass('hidden');
            $('#li-online').addClass('active');
            $('#tab-te-online').addClass('active');
            $('#li-cft').removeClass('active');
            $('#tab-te-cft').removeClass('active');
            $('#li-my-task').removeClass('active');
            $('#tab-my-task').removeClass('active');
            loadDataOnline();
        } else if(dataset.dem == "TE-CFT"){
            $('#li-cft').removeClass('hidden');
            $('#li-online').addClass('hidden');
            $('#li-cft').addClass('active');
            $('#tab-te-cft').addClass('active');
            $('#li-online').removeClass('active');
            $('#tab-te-online').removeClass('active');
            $('#li-my-task').removeClass('active');
            $('#tab-my-task').removeClass('active');
            loadDataCFT();
        } else{
            $('#li-online').removeClass('hidden');
            $('#li-cft').removeClass('hidden');
            $('#li-online').addClass('active');
            $('#tab-te-online').addClass('active');
            $('#li-cft').removeClass('active');
            $('#tab-te-cft').removeClass('active');
            $('#li-my-task').removeClass('active');
            $('#tab-my-task').removeClass('active');
            loadDataOnline();
            loadDataCFT();
        }
        // $.ajax({
        //     type: "GET",
        //     url: "/api/test/task-daily",
        //     data: {
        //         factory: dataset.factory,
        //         employee: dataset.empId,
        //         timeSpan: dataset.timeSpan
        //     },
        //     contentType: "application/json; charset=utf-8",
        //     success: function(data){
        //         $("#tblOwner tbody").html("");
        //         var tbody = '';
        //         var stt = 1;
        //         for(i in data){
        //             var duedate = '';
        //             if(data[i].duedate != null){
        //                 duedate = data[i].duedate.slice(0,10);
        //             }
        //             var createdAt = '';
        //             if(data[i].createdAt != null){
        //                 createdAt = moment(data[i].createdAt).format("YYYY/MM/DD");
        //             }
        //             if(data[i].id == 0){
        //                 tbody += '<tr id="row' + data[i].id + '">'
        //                     +  '<td>' + stt +'</td>'
        //                     +  '<td><span id="spId'+stt+'" class="hidden">' + data[i].id +'</span><span id="spEmpID'+stt+'">' + convertNull(data[i].employeeId) +'</span></td>'
        //                     +  '<td></td><td><span id="spEmpName'+stt+'">' + convertNull(data[i].employeeName) +'</span></td>'
        //                     +  '<td><a data-href="'+data[i].taskUrl+'" onclick="openWindow(this.dataset.href)"><span id="spTaskTitle'+stt+'">' + data[i].taskTitle +'</span></a> <span id="spTrackingID'+stt+'" class="hidden">' + convertNull(data[i].trackingId) +'</span></td>'
        //                     +  '<td style="text-align: left;"><span id="spTaskContent'+stt+'">' + convertNull(data[i].taskContent) +'</span></td>'
        //                     +  '<td><span id="spTaskStatus'+stt+'" class="' + data[i].status.toLowerCase() +'">' + data[i].status +'</span></td>'
        //                     +  '<td><span id="spTaskCreator'+stt+'">' + convertNull(data[i].creator) +'</span></td>'
        //                     +  '<td><span id="spTaskCreatedAt'+stt+'">' + createdAt +'</span></td>'
        //                     +  '<td><span id="spTaskDueDate'+stt+'">' + duedate +'</span></td>'
        //                     +  '<td><span id="spTaskNote'+stt+'">' + convertNull(data[i].note) +'</span></td>'
        //                     +  '<td><button id="btnComment' + stt + '" class="btn btn-sm" disabled="disabled" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #999999;" onclick="showComment(' + stt + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
        //                     +  '<button id="btnEditRowOwner' + stt + '" class="btn" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #ffc107;" onclick="showModalEdit('+stt+')"><i class="fa fa-edit"></i></button>'
        //                     +  '<button id="btnDeleteRowOwner' + stt + '" class="btn" disabled="disabled" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #999999;" onclick="deleteRowOwner(' + stt + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
        //             }
        //             else{
        //                 tbody += '<tr id="row' + data[i].id + '"><td>' + stt +'</td><td><span id="spId'+stt+'" class="hidden">' + data[i].id +'</span><span id="spEmpId'+stt+'">' + convertNull(data[i].employeeId) +'</span></td>'
        //                     +  '<td><span id="spEmpName'+stt+'">' + convertNull(data[i].employeeName) +'</span><span id="spTrackingID'+stt+'" class="hidden">' + convertNull(data[i].trackingId) +'</span></td>'
        //                     +  '<td><span id="spTaskTitle'+stt+'">' + data[i].taskTitle +'</span></td>'
        //                     +  '<td style="text-align: left;"><span id="spTaskContent'+stt+'">' + convertNull(data[i].taskContent) +'</span></td>'
        //                     +  '<td><span id="spTaskCreator'+stt+'">' + convertNull(data[i].creator) +'</span></td>'
        //                     +  '<td><span id="spTaskStatus'+stt+'" class="' + data[i].status.toLowerCase() +'">' + data[i].status +'</span></td>'
        //                     +  '<td><span id="spTaskCreatedAt'+stt+'">' + createdAt +'</span></td>'
        //                     +  '<td><span class="task-duedate" data-id="' + data[i].id + '" data-status="' + data[i].status + '" id="spTaskDueDate'+stt+'">' + duedate +'</span></td>'
        //                     +  '<td><span id="spTaskNote'+stt+'">' + convertNull(data[i].note) +'</span></td>';
        //                 if(data[i].creator == "FOXCONN"){
        //                     tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-sm" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
        //                     +  '<button id="btnEditRowOwner' + stt + '" class="btn" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #ffc107;" onclick="showModalEdit('+stt+')"><i class="fa fa-edit"></i></button>'
        //                     +  '<button id="btnDeleteRowOwner' + stt + '" disabled="disabled" class="btn btn-danger" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #999999;" onclick="deleteRowOwner(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
        //                 } else if(data[i].creator == dataset.empId || dataset.level > 3){
        //                     tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-sm" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
        //                     +  '<button id="btnEditRowOwner' + stt + '" class="btn" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #ffc107;" onclick="showModalEdit('+stt+')"><i class="fa fa-edit"></i></button>'
        //                     +  '<button id="btnDeleteRowOwner' + stt + '" class="btn btn-danger" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727;" onclick="deleteRowOwner(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
        //                 } else{
        //                     tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-sm" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
        //                     +  '<button id="btnEditRowOwner' + stt + '" disabled="disabled" class="btn" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #999999;" onclick="editRowOwner('+stt+')"><i class="fa fa-edit"></i></button>'
        //                     +  '<button id="btnDeleteRowOwner' + stt + '" disabled="disabled" class="btn" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #999999;" onclick="deleteRowOwner(' + stt + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
        //                 }
        //             }
        //             stt++;
        //         }

        //         var stt1 = 1;
        //         var stt2 = 1;
        //         var tbody1 = '';
        //         for(i in data){
        //             var duedate = '';
        //             if(data[i].duedate != null){
        //                 duedate = data[i].duedate.slice(0,10);
        //             }
        //             var createdAt = '';
        //             if(data[i].createdAt != null){
        //                 createdAt = moment(data[i].createdAt).format("YYYY/MM/DD");
        //             }
        //             if(data[i].employeeId == dataset.empId){
        //                 tbody1 += '<tr id="rowMyTask' + data[i].id + '"><td>' + stt2 +'</td><td><span id="spId'+stt1+'" class="hidden">' + data[i].id +'</span><span id="spEmpId'+stt1+'">' + data[i].employeeId +'</span></td>'
        //                     +  '<td><span id="spEmpName'+stt1+'">' + data[i].employeeName +'</span><span id="spTrackingID'+stt1+'" class="hidden">' + convertNull(data[i].trackingId) +'</span></td>'
        //                     +  '<td><span id="spTaskTitle'+stt1+'">' + data[i].taskTitle +'</span></td>'
        //                     +  '<td style="text-align: left;"><span id="spTaskContent'+stt1+'">' + convertNull(data[i].taskContent) +'</span></td>'
        //                     +  '<td><span id="spTaskCreator'+stt+'">' + convertNull(data[i].creator) +'</span></td>'
        //                     +  '<td><span id="spTaskStatus'+stt1+'" class="' + data[i].status.toLowerCase() +'">' + data[i].status +'</span></td>'
        //                     +  '<td><span id="spTaskCreatedAt'+stt+'">' + createdAt +'</span></td>'
        //                     +  '<td><span class="task-duedate" data-id="' + data[i].id + '" data-status="' + data[i].status + '" id="spTaskDueDate'+stt1+'">' + duedate +'</span></td>'
        //                     +  '<td><span id="spTaskNote'+stt1+'">' + convertNull(data[i].note) +'</span></td>';
        //                 if(data[i].creator == "FOXCONN"){
        //                     tbody1 += '<td><button id="btnComment' + data[i].id + '" class="btn btn-sm" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
        //                     +  '<button id="btnEditRowOwner' + stt1 + '" class="btn" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #ffc107;" onclick="showModalEdit('+stt1+')"><i class="fa fa-edit"></i></button>'
        //                     +  '<button id="btnDeleteRowOwner' + stt1 + '" disabled="disabled" class="btn btn-danger" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #999999;" onclick="deleteRowOwner(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
        //                 } else{
        //                     tbody1 += '<td><button id="btnComment' + data[i].id + '" class="btn btn-sm" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
        //                     +  '<button id="btnEditRowOwner' + stt1 + '" class="btn" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #ffc107;" onclick="showModalEdit('+stt1+')"><i class="fa fa-edit"></i></button>'
        //                     +  '<button id="btnDeleteRowOwner' + stt1 + '" class="btn btn-danger" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727;" onclick="deleteRowOwner(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
        //                 }
        //                 stt2++;
        //             }
        //             stt1++;
        //         }
        //         $("#tblOwner tbody").html(tbody);
        //         $("#tblMyTask tbody").html(tbody1);
        //         statusByDuedate();
        //         $(".loader").addClass("disableSelect");
        //     },
        //     failure: function(errMsg) {
        //          console.log(errMsg);
        //     }
        // });
    }

    function loadDataOnline(){
        $.ajax({
            type: "GET",
            url: "/api/test/task-daily",
            data: {
                factory: dataset.factory,
                employee: dataset.empId,
                resourceGroup: 'TE-ONLINE',
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#tblOnline tbody").html("");
                var tbody = '';
                var stt = 1;
                for(i in data){
                    var duedate = '';
                    if(data[i].duedate != null){
                        duedate = data[i].duedate.slice(0,10);
                    }
                    var createdAt = '';
                    if(data[i].createdAt != null){
                        createdAt = moment(data[i].createdAt).format("YYYY/MM/DD");
                    }

                    tbody += '<tr id="row' + data[i].id + '"><td>' + stt +'</td><td><span id="spEmpId'+data[i].id+'">' + convertNull(data[i].employeeId) +'</span></td>'
                        +  '<td><span id="spEmpName'+data[i].id+'">' + convertNull(data[i].employeeName) +'</span></td>'
                        +  '<td><span id="spTaskTitle'+data[i].id+'">' + data[i].taskTitle +'</span></td>'
                        +  '<td style="text-align: left;"><span id="spTaskContent'+data[i].id+'">' + convertNull(data[i].taskContent) +'</span></td>'
                        +  '<td><span id="spTaskCreator'+data[i].id+'">' + convertNull(data[i].creator) +'</span></td>'
                        +  '<td><span id="spTaskStatus'+data[i].id+'" class="' + data[i].status.toLowerCase() +'">' + data[i].status +'</span></td>'
                        +  '<td><span id="spTaskCreatedAt'+data[i].id+'">' + createdAt +'</span></td>'
                        +  '<td><span class="task-duedate" data-id="' + data[i].id + '" data-status="' + data[i].status + '" id="spTaskDueDate'+data[i].id+'">' + duedate +'</span></td>'
                        +  '<td><span id="spTaskNote'+data[i].id+'">' + convertNull(data[i].note) +'</span></td>';
                    if(data[i].creator == "FOXCONN"){
                        tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                        +  '<button id="btnEditRowOwner' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #ffc107;" onclick="showModalEdit('+data[i].id+')"><i class="fa fa-edit"></i></button>'
                        +  '<button id="btnDeleteRowOwner' + data[i].id + '" disabled="disabled" class="btn btn-xs btn-danger" style="color: #272727; background: #999999;"><i class="fa fa-trash-o"></i></button></td></tr>';
                    } else if(data[i].creator == dataset.empId || dataset.level > 3){
                        tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                        +  '<button id="btnEditRowOwner' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #ffc107;" onclick="showModalEdit('+data[i].id+')"><i class="fa fa-edit"></i></button>'
                        +  '<button id="btnDeleteRowOwner' + data[i].id + '" class="btn btn-xs btn-danger" style="color: #272727;" onclick="deleteRowOwner(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
                    } else{
                        tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                        +  '<button id="btnEditRowOwner' + data[i].id + '" disabled="disabled" class="btn btn-xs" style="color: #272727; background: #999999;"><i class="fa fa-edit"></i></button>'
                        +  '<button id="btnDeleteRowOwner' + data[i].id + '" disabled="disabled" class="btn btn-xs" style="color: #272727; background: #999999;"><i class="fa fa-trash-o"></i></button></td></tr>';
                    }
                    stt++;
                }

                var stt2 = 1;
                var tbody2 = '';
                for(i in data){
                    var duedate = '';
                    if(data[i].duedate != null){
                        duedate = data[i].duedate.slice(0,10);
                    }
                    var createdAt = '';
                    if(data[i].createdAt != null){
                        createdAt = moment(data[i].createdAt).format("YYYY/MM/DD");
                    }
                    if(data[i].employeeId == dataset.empId){
                        tbody2 += '<tr id="rowMyTask' + data[i].id + '"><td>' + stt2 +'</td><td>' + data[i].employeeId + '</td>'
                            +  '<td>' + data[i].employeeName + '</td>'
                            +  '<td>' + data[i].taskTitle + '</td>'
                            +  '<td style="text-align: left;">' + convertNull(data[i].taskContent) + '</td>'
                            +  '<td>' + convertNull(data[i].creator) + '</td>'
                            +  '<td>' + data[i].status + '</td>'
                            +  '<td>' + createdAt + '</td>'
                            +  '<td><span class="task-duedate" data-id="' + data[i].id + '" data-status="' + data[i].status + '">' + duedate +'</span></td>'
                            +  '<td>' + convertNull(data[i].note) +'</td>';
                        if(data[i].creator == "FOXCONN"){
                            tbody2 += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                            +  '<button id="btnEditRowOwner' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #ffc107;" onclick="showModalEdit('+data[i].id+')"><i class="fa fa-edit"></i></button>'
                            +  '<button id="btnDeleteRowOwner' + data[i].id + '" disabled="disabled" class="btn btn-xs btn-danger" style="color: #272727; background: #999999;"><i class="fa fa-trash-o"></i></button></td></tr>';
                        } else{
                            tbody2 += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                            +  '<button id="btnEditRowOwner' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #ffc107;" onclick="showModalEdit('+data[i].id+')"><i class="fa fa-edit"></i></button>'
                            +  '<button id="btnDeleteRowOwner' + data[i].id + '" class="btn btn-xs btn-danger" style="color: #272727;" onclick="deleteRowOwner(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
                        }
                        stt2++;
                    }
                }
                $("#tblOnline tbody").html(tbody);
                $("#tblMyTask tbody").html(tbody2);
                statusByDuedate();
                $(".loader").addClass("disableSelect");
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
    }

    function loadDataCFT(){
        $.ajax({
            type: "GET",
            url: "/api/test/task-daily",
            data: {
                factory: dataset.factory,
                employee: dataset.empId,
                resourceGroup: 'TE-CFT',
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#tblCft tbody").html("");
                var tbody = '';
                var stt = 1;
                for(i in data){
                    var duedate = '';
                    if(data[i].duedate != null){
                        duedate = data[i].duedate.slice(0,10);
                    }
                    var createdAt = '';
                    if(data[i].createdAt != null){
                        createdAt = moment(data[i].createdAt).format("YYYY/MM/DD");
                    }

                    tbody += '<tr id="row' + data[i].id + '"><td>' + stt +'</td><td><span id="spEmpId'+data[i].id+'">' + convertNull(data[i].employeeId) +'</span></td>'
                        +  '<td><span id="spEmpName'+data[i].id+'">' + convertNull(data[i].employeeName) +'</span></td>'
                        +  '<td style="text-align: left;"><span id="spTaskTitle'+data[i].id+'">' + data[i].taskTitle +'</span></td>'
                        +  '<td style="text-align: left;"><span id="spTaskContent'+data[i].id+'">' + convertNull(data[i].taskContent) +'</span></td>'
                        +  '<td><span id="spTaskCreator'+data[i].id+'">' + convertNull(data[i].creator) +'</span></td>'
                        +  '<td><span id="spTaskStatus'+data[i].id+'" class="' + data[i].status.toLowerCase() +'">' + data[i].status +'</span></td>'
                        +  '<td><span id="spTaskCreatedAt'+data[i].id+'">' + createdAt +'</span></td>'
                        +  '<td><span class="task-duedate" data-id="' + data[i].id + '" data-status="' + data[i].status + '" id="spTaskDueDate'+data[i].id+'">' + duedate +'</span></td>'
                        +  '<td><span id="spTaskNote'+data[i].id+'">' + convertNull(data[i].note) +'</span></td>';
                    if(data[i].creator == "FOXCONN"){
                        tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                        +  '<button id="btnEditRowOwner' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #ffc107;" onclick="showModalEdit('+data[i].id+')"><i class="fa fa-edit"></i></button>'
                        +  '<button id="btnDeleteRowOwner' + data[i].id + '" disabled="disabled" class="btn btn-xs btn-danger" style="color: #272727; background: #999999;"><i class="fa fa-trash-o"></i></button></td></tr>';
                    } else if(data[i].creator == dataset.empId || dataset.level > 3){
                        tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                        +  '<button id="btnEditRowOwner' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #ffc107;" onclick="showModalEdit('+data[i].id+')"><i class="fa fa-edit"></i></button>'
                        +  '<button id="btnDeleteRowOwner' + data[i].id + '" class="btn btn-xs btn-danger" style="color: #272727;" onclick="deleteRowOwner(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
                    } else{
                        tbody += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                        +  '<button id="btnEditRowOwner' + data[i].id + '" disabled="disabled" class="btn btn-xs" style="color: #272727; background: #999999;"><i class="fa fa-edit"></i></button>'
                        +  '<button id="btnDeleteRowOwner' + data[i].id + '" disabled="disabled" class="btn btn-xs" style="color: #272727; background: #999999;"><i class="fa fa-trash-o"></i></button></td></tr>';
                    }
                    stt++;
                }

                var stt2 = 1;
                var tbody2 = '';
                for(i in data){
                    var duedate = '';
                    if(data[i].duedate != null){
                        duedate = data[i].duedate.slice(0,10);
                    }
                    var createdAt = '';
                    if(data[i].createdAt != null){
                        createdAt = moment(data[i].createdAt).format("YYYY/MM/DD");
                    }
                    if(data[i].employeeId == dataset.empId){
                        tbody2 += '<tr id="rowMyTask' + data[i].id + '"><td>' + stt2 +'</td><td>' + data[i].employeeId + '</td>'
                            +  '<td>' + data[i].employeeName + '</td>'
                            +  '<td>' + data[i].taskTitle + '</td>'
                            +  '<td style="text-align: left;">' + convertNull(data[i].taskContent) + '</td>'
                            +  '<td>' + convertNull(data[i].creator) + '</td>'
                            +  '<td>' + data[i].status + '</td>'
                            +  '<td>' + createdAt + '</td>'
                            +  '<td><span class="task-duedate" data-id="' + data[i].id + '" data-status="' + data[i].status + '">' + duedate +'</span></td>'
                            +  '<td>' + convertNull(data[i].note) +'</td>';
                        if(data[i].creator == "FOXCONN"){
                            tbody2 += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                            +  '<button id="btnEditRowOwner' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #ffc107;" onclick="showModalEdit('+data[i].id+')"><i class="fa fa-edit"></i></button>'
                            +  '<button id="btnDeleteRowOwner' + data[i].id + '" disabled="disabled" class="btn btn-xs btn-danger" style="color: #272727; background: #999999;"><i class="fa fa-trash-o"></i></button></td></tr>';
                        } else{
                            tbody2 += '<td><button id="btnComment' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')" data-popup="popover" data-container="body"><i class="fa fa-comment" style="color: #272727;"></i></button>'
                            +  '<button id="btnEditRowOwner' + data[i].id + '" class="btn btn-xs" style="color: #272727; background: #ffc107;" onclick="showModalEdit('+data[i].id+')"><i class="fa fa-edit"></i></button>'
                            +  '<button id="btnDeleteRowOwner' + data[i].id + '" class="btn btn-xs btn-danger" style="color: #272727;" onclick="deleteRowOwner(' + data[i].id + ')"><i class="fa fa-trash-o"></i></button></td></tr>';
                        }
                        stt2++;
                    }
                }
                $("#tblCft tbody").html(tbody);
                $("#tblMyTask tbody").html(tbody2);
                statusByDuedate();
                $(".loader").addClass("disableSelect");
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
    }

    function statusByDuedate(){
        var listDuedate = $('.task-duedate');
        var date = moment().format("YYYY/MM/DD");
        for(let i=0; i< listDuedate.length; i++){
            if(listDuedate[i].innerText == date && listDuedate[i].dataset.status != 'DONE'){
                $('#row'+listDuedate[i].dataset.id).addClass('bg-duedate-warning');
                $('#rowMyTask'+listDuedate[i].dataset.id).addClass('bg-duedate-warning');
            } else if(listDuedate[i].innerText < date && listDuedate[i].dataset.status != 'DONE'){
                $('#row'+listDuedate[i].dataset.id).addClass('bg-duedate-danger');
                $('#rowMyTask'+listDuedate[i].dataset.id).addClass('bg-duedate-danger');
            }
        }
    }

    function convertNull(str){
        var strCon = str;
        if(str === null){
            strCon = "";
        }
        return strCon;
    }

    function showComment(id){
        $.ajax({
            type: "GET",
            url: "/api/test/task-comment",
            data: {
                taskId: id,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $('.rowComment').remove();
                var value = '<tr class="rowComment rowComment'+id+'" style="background-color: #efefef"><td colspan="11">'
                            +  '<div class="direct-chat">'
                            +  '<div class="card-header" style="border-bottom: 1px solid rgba(0,0,0,.125);">'
                            +  '<h3 class="card-title no-margin">Comment<button class="btn btn-danger" style="padding: 3px 10px;float: right; color: #272727" onclick="closePopup('+id+')"><i class="fa fa-times"></i></button></h3></div>'
                            +  '<div class="card-body"><div class="direct-chat-messages">';
                for(let i=0; i< data.length; i++){
                    value += '<div class="direct-chat-msg" style="padding: 5px 0px;">'
                            +  '<div class="direct-chat-infos clearfix"></div>'
                            +  '<div class="direct-chat-text"><span class="direct-chat-timestamp" style="color:#697582">'+data[i].createdAt+'</span></br>'
                            +  '<span class="direct-chat-name" style="font-weight: 600;">'+data[i].employeeName+': </span><span> '+data[i].comment+'</span></div></div>';
                }
                value += '</div></div><div class="card-footer" style="padding: 10px;">'
                    +  '<input data-id="'+id+'" type="text" name="comment" placeholder="Comment..." class="form-control" style="height: 3rem;color: #272727;padding: 0 5px;width: 92%;float: left;">'
                    +  '<button type="submit" style="padding: 5px 10px;" class="btn btn-success" onclick="addComment('+id+')">Send</button>'  
                    +  '</div></div></td></tr>';
                $(value).insertAfter("#row"+id);
                $(value).insertAfter("#rowMyTask"+id);
                $('input[name=comment]').focus();
                $('input[name=comment]').keypress(function(event){
                    var keyCode = (event.keyCode ? event.keyCode : event.wich);
                    if(keyCode == '13'){
                        addComment(this.dataset.id);
                    }
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
    }
    function closePopup(id){
        $('.rowComment'+id).remove();
    }

    function addComment(id){
        var comment = $('input[name="comment"]').val();
        var addComment = {
            taskId: id,
            employeeId: dataset.empId,
            employeeName: dataset.empName,
            comment: comment
        }
        $.ajax({
            type: "POST",
            url: "/api/test/task-comment",
            data: JSON.stringify(addComment),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                showComment(id);
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
    }

    function showModalEdit(stt){
        if(stt == 0){
            $('#modal-task-edit input[name="employeeNo"]').val(dataset.empId);
            $('#modal-task-edit input[name="employeeName"]').val(dataset.empName);
            $('#modal-task-edit textarea[name="taskTitle"]').val("");
            $('#modal-task-edit textarea[name="taskTitle"]').removeAttr("disabled","disabled");
            $('#modal-task-edit select[name="status"]').val("ON_GOING");
            $('#modal-task-edit select[name="status"]').selectpicker('refresh');
            $('#modal-task-edit textarea[name="detail"]').val("");
            $('#modal-task-edit input[name="duedate"]').data('daterangepicker').setStartDate(new Date());
            $('#modal-task-edit textarea[name="note"]').val("");
            $('#modal-task-edit input[name="trackingId"]').val("");
            $('#modal-task-edit input[name="taskId"]').val("");

            $('#modal-task-edit textarea[name="taskTitle"]').removeAttr('disabled');
            var detail = $('#spTaskContent'+stt).html();
            $('#modal-task-edit select[name="status"]').on('change', function(){
                $('#modal-task-edit textarea[name="detail"]').val(detail);
            });
        } else{
            $.ajax({
                type: "GET",
                url: "/api/test/task-daily/"+stt,
                data: {
                    factory: dataset.factory,
                    employee: dataset.empId,
                    timeSpan: dataset.timeSpan
                },
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    if(dataset.level <= 3){
                        $('#modal-task-edit input[name="employeeNo"]').val(dataset.empId);
                        $('#modal-task-edit input[name="employeeName"]').val(dataset.empName);
                        $('#modal-task-edit input[name="employeeNo"]').attr('disabled','disabled');
                    }
                    else{
                        if(dataset.empId != data.employeeId){
                            $('#modal-task-edit input[name="employeeNo"]').val(data.employeeId);
                            $('#modal-task-edit input[name="employeeName"]').val(data.employeeName);
                        }else{
                            $('#modal-task-edit input[name="employeeNo"]').val(dataset.empId);
                            $('#modal-task-edit input[name="employeeName"]').val(dataset.empName);
                        }
                    }

                    if(data.trackingId != null && stt != 0){
                        $('#modal-task-edit textarea[name="taskTitle"]').attr('disabled','disabled');
                        var detail = $('#spTaskContent'+stt).html();
                        $('#modal-task-edit select[name="status"]').on('change', function(){
                            if($('#modal-task-edit select[name="status"]').val() == "DONE"){
                                $('#modal-task-edit textarea[name="detail"]').val('ERROR CODE: \nROOT CAUSE: \nACTION: ');
                                $('#modal-task-edit textarea[name="detail"]').attr('rows','3');
                                $('#modal-task-edit textarea[name="detail"]').focus();
                            }
                            else{
                                $('#modal-task-edit textarea[name="detail"]').val(detail);
                                $('#modal-task-edit textarea[name="detail"]').attr('rows','1');
                            }
                        });
                    } else{
                        $('#modal-task-edit textarea[name="taskTitle"]').removeAttr('disabled');
                        var detail = $('#spTaskContent'+stt).html();
                        $('#modal-task-edit select[name="status"]').on('change', function(){
                            $('#modal-task-edit textarea[name="detail"]').val(detail);
                        });
                    }

                    $('#modal-task-edit input[name="taskId"]').val(stt)
                    $('#modal-task-edit input[name="trackingId"]').val(data.trackingId);
                    $('#modal-task-edit textarea[name="taskTitle"]').val(convertNull(data.taskTitle).replace(/<br\s*\/?>/g,'\n'));
                    $('#modal-task-edit select[name="status"]').val(data.status);
                    $('#modal-task-edit select[name="status"]').selectpicker('refresh');
                    $('#modal-task-edit textarea[name="detail"]').val(convertNull(data.taskContent).replace(/<br\s*\/?>/g,'\n'));
                    $('#modal-task-edit input[name="duedate"]').val(data.duedate);
                    $('#modal-task-edit textarea[name="note"]').val(convertNull(data.note).replace(/<br\s*\/?>/g,'\n'));
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });
        }
        $('#modal-task-edit').modal('show');
    }

    function confirmEditRowOwner(){
        var trackingId = '';
        if($('#modal-task-edit input[name="trackingId"]').val() == ""){
            trackingId = null;
        } else {
            trackingId = Number($('#modal-task-edit input[name="trackingId"]').val());
        }
        var editOwner = {
            "factory" : dataset.factory,
            "creator" : dataset.empId,
            "id" : $('#modal-task-edit input[name="taskId"]').val(),
            "employeeId" : $('#modal-task-edit input[name="employeeNo"]').val(),
            "employeeName" : $('#modal-task-edit input[name="employeeName"]').val(),
            "taskTitle" : $('#modal-task-edit textarea[name="taskTitle"]').val().replace(/\r\n|\r|\n/g,"<br />"),
            "taskContent" : $('#modal-task-edit textarea[name="detail"]').val().replace(/\r\n|\r|\n/g,"<br />"),
            "status" : $('#modal-task-edit select[name="status"]').val(),
            "duedate" : moment($('#modal-task-edit input[name="duedate"]').val()).format("YYYY/MM/DD"),
            "note" : $('#modal-task-edit textarea[name="note"]').val().replace(/\r\n|\r|\n/g,"<br />"),
            "trackingId" : trackingId
        }

        $.ajax({
            type: "POST",
            url: "/api/test/task-daily",
            data: JSON.stringify(editOwner),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(data)
                    loadTaskDaily();
                else alert("Fail!");
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
    }

    function deleteRowOwner(id){
        var del = window.confirm("Do you want to DELETE?");
        if(del){
            $.ajax({
                type: "DELETE",
                url: "/api/test/task-daily/"+id,
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    if(data)
                        loadTaskDaily();
                    else alert("DELETE Fail!");
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });
        }
    }

    function searchEmployee() {
        var employeeNo = $('#modal-task-edit input[name="employeeNo"]').val();
        $.ajax({
            type: "GET",
            url: "/api/test/employee",
            data: {
                employeeNo: employeeNo
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if (!$.isEmptyObject(data)) {
                    $('#modal-task-edit input[name="employeeName"]').val(data.name);
                } else {
                    $('#modal-task-edit input[name="employeeName"]').val('NOT FOUND');
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    $("#txtSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblCft>tbody>tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
        $("#tblOnline>tbody>tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
        $("#tblMyTask>tbody>tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
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
    getTimeNow();
</script>