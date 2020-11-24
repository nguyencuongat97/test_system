<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<!-- <div class="loader"></div> -->
<div class="panel panel-re panel-flat row" style="margin-bottom: 0px; min-height: calc(100vh - 100px);">
    <div class="col-lg-3" style="padding: 0;">
        <ul class="nav nav-tabs" style="margin-bottom: 10px;">
            <li class="active"><a data-toggle="tab" href="#tab-te-online" style="font-size:13px; padding: 8px;" onclick="loadAddNewConfirm('ONLINE')">TE Online</a></li>
            <li><a data-toggle="tab" href="#tab-te-cft" style="font-size:13px; padding: 8px;" onclick="loadAddNewConfirm('CFT')">TE CFT</a></li>
        </ul>
        <div class="tab-content">
            <div id="tab-te-cft" class="tab-pane">
                <div class="row" style="margin: 0; padding: 0">
                    <ul class="col-lg-12 media-list listEmpCFT" style="overflow: auto; max-height: calc(100vh - 158px);">
                        <li class="resource">
                            <div class="rcontent">
                                <div class="col-sm-6 information"></div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div id="tab-te-online" class="tab-pane active">
                <div class="row" style="margin: 0; padding: 0">
                    <ul class="col-lg-12 media-list listEmpOnline" style="overflow: auto; max-height: calc(100vh - 158px);">
                        <li class="resource">
                            <div class="rcontent">
                                <div class="col-sm-6 information"></div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="col-lg-9">
        <div class="row" style="margin-bottom: 5px; padding: 0">
            <div class="col-md-4 col-sm-6 col-xs-10">
                <div class="panel input-group" style="margin-bottom: 5px;">
                    <span class="input-group-addon" style="padding: 0px 4px;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="border-bottom: 0px !important;">
                </div>
            </div>
            <div class="col-md-1 col-sm-1 col-xs-2 no-padding">
                <button id="btnAddConfirm" href="#modal-resource-edit" data-toggle="modal" class="panel btn btn-default" style="margin: 0;"><i class="fa fa-plus"></i></button>
            </div>
            
            <div class="col-md-7 col-sm-5">
                <label class="panel input-group empName" style="margin-bottom: 5px; padding: 9px; float: right;">Nguyen Duc Tien</label>
            </div>    
        </div>
        <div class="row" id="view-content" style="margin-bottom: 5px; overflow: auto; max-height: calc(100vh - 158px);">
        </div>
    </div>
</div>
<div class="modal fade" id="modalTask" role="dialog">
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
          <div class="modal-title">
                <label class="panel input-group empName" style="margin-bottom: 5px; padding: 9px;">Nguyen Duc Tien</label>
          </div>
        </div>
        <div class="modal-body">
            <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 210px);">
            <table id="tblOwner" class="table table-small table-bordered table-sticky" style="text-align: center">
                <thead>
                    <tr>
                        <th style="width: 3%;">STT</th>
                        <th>Task</th>
                        <th>Detail</th>
                        <th style="width: 5%;">Status</th>
                        <th>Duedate</th>
                        <th>Note</th>
                        <th>CreatedAt</th>
                        <th>UpdatedAt</th>
                        <th style="width: 5%; z-index: 1;">Comment</th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
            </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal" style="font-size: 13px;">Close</button>
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
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">NEW</h6>            
            </div>
        	<div class="modal-body">
                <div class="row setNewTask">
                </div>
        	</div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="postConfirm();">Submit<i class="icon-arrow-right14 position-right"></i></button>
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
    };
    loadResourceStatus();
    function loadData(id, name, dem){
        if(id != undefined){
            dataset.empIdLoad = id;
            dataset.empNameLoad = name;
            dataset.demLoad = dem;
        }
        $('.empName').html(dataset.empIdLoad + ' - ' + dataset.empNameLoad);
        loadTaskDailyConfirm();
    }
    function loadResourceStatus() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/status',
            data: {
                factory: dataset.factory
            },
            success: function(data){
                var html_cft = '';
                var html_online = '';
                var actions = new Array();
                var success = new Array();
                var times = new Array();
                var idleTimes = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var index_online = 0;
                    var index_cft = 0;
                    var index = 0;
                    var keys = Object.keys(data);
                    var fl = true;
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            if(fl){
                                if(value[j].dem == 'TE-ONLINE'){
                                    dataset.empIdLoad = value[j].employeeNo;
                                    dataset.empNameLoad = value[j].name;
                                    dataset.demLoad = value[j].dem;
                                    fl = false
                                }
                            }

                            categories[j] = value[j].employeeNo;
                            actions[index] = {name: value[j].employeeNo, y: value[j].taskNumber, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            success[index] = {name: value[j].employeeNo, y: value[j].taskSuccess, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            times[index] = {name: value[j].employeeNo, y: value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            idleTimes[index] = {name: value[j].employeeNo, y: 300-value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};

                            if(value[j].dem == "TE-CFT"){
                                html_cft +=  '<li class="resource">'+
                                     '<div class="rcontent">'+
                                         '<div class="col-xs-12 information">'+
                                             '<div class="row">'+
                                                 '<div class="col-xs-2 rank '+ranking(index_cft+1)+'">'+
                                                     '<label style="margin: unset;">'+(index_cft+1)+'</label>'+
                                                 '</div>'+
                                                 '<div class="col-xs-3" style="padding: 0px 3px;">'+
                                                     '<div class="avatar">'+
                                                        (value[j].avatar != null ? '<img class="" src="'+value[j].avatar+'" alt>' : '')+
                                                     '</div>'+
                                                 '</div>'+
                                                 '<div class="row col-xs-8 form-horizontal" style="font-size: 12px; padding-top: 8px; margin-left: -8px; margin-right: -8px">'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 text-bold" name="name" style="padding: 0px 10px; margin: unset;"><a href="#" onclick="loadData(\''+value[j].employeeNo+'\', \''+value[j].name+'('+value[j].chineseName+')'+'\', \''+value[j].dem+'\')">'+value[j].name+'('+value[j].chineseName+')'+'</a></label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 " name="employeeNo" style="padding: 0px 10px; margin: unset;">'+value[j].employeeNo+' <span style="font-size:11px;">(WOD: '+convertWorkOffDates(value[j].workOffDay)+')<span></label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 " name="experience" style="padding: 0px 10px; margin: unset;">'+value[j].dem+' (' + value[j].shift + ')</label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-6 " name="level" style="padding: 0px 10px; margin: unset;">'+groupLevelName(value[j].groupLevel)+'</label>'+
                                                         '<label class="col-xs-6 text-bold text-right" name="level" style="padding: 0px 10px; margin: unset;">'+value[j].totalScore+'</label>'+
                                                     '</div>'+
                                                 '</div>'+
                                             '</div>'+
                                         '</div>'+
                                     '</div>'+
                                 '</li>';
                                 index_cft = index_cft+1;
                            } else if(value[j].dem == "TE-ONLINE"){
                                html_online +=  '<li class="resource">'+
                                     '<div class="rcontent">'+
                                         '<div class="col-xs-12 information">'+
                                             '<div class="row">'+
                                                 '<div class="col-xs-2 rank '+ranking(index_online+1)+'">'+
                                                     '<label style="margin: unset;">'+(index_online+1)+'</label>'+
                                                 '</div>'+
                                                 '<div class="col-xs-3" style="padding: 0px 3px;">'+
                                                     '<div class="avatar">'+
                                                        (value[j].avatar != null ? '<img class="" src="'+value[j].avatar+'" alt>' : '')+
                                                     '</div>'+
                                                 '</div>'+
                                                 '<div class="row col-xs-8 form-horizontal" style="font-size: 12px; padding-top: 8px; margin-left: -8px; margin-right: -8px">'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 text-bold" name="name" style="padding: 0px 10px; margin: unset;"><a href="#" onclick="loadData(\''+value[j].employeeNo+'\', \''+value[j].name+'('+value[j].chineseName+')'+'\', \''+value[j].dem+'\')">'+value[j].name+'('+value[j].chineseName+')'+'</a></label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 " name="employeeNo" style="padding: 0px 10px; margin: unset;">'+value[j].employeeNo+' <span style="font-size:11px;">(WOD: '+convertWorkOffDates(value[j].workOffDay)+')</span></label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 " name="experience" style="padding: 0px 10px; margin: unset;">'+value[j].dem+' (' + value[j].shift + ')</label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-6 " name="level" style="padding: 0px 10px; margin: unset;">'+groupLevelName(value[j].groupLevel)+'</label>'+
                                                         '<label class="col-xs-6 text-bold text-right" name="level" style="padding: 0px 10px; margin: unset;">'+value[j].totalScore+'</label>'+
                                                     '</div>'+
                                                 '</div>'+
                                             '</div>'+
                                         '</div>'+
                                     '</div>'+
                                 '</li>';
                                 index_online = index_online +1;
                            }
                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }
                    $('.media-list.listEmpCFT').html(html_cft);
                    $('.media-list.listEmpOnline').html(html_online);
                }
                getTimeNow();
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }
    function ranking(rank) {
        if (rank == 1)
            return "rank-1";
        else if (rank < 4)
            return "rank-2";
        return "rank-3";
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
    function loadTaskDailyConfirm(){
        $.ajax({
            type: "GET",
            url: "/api/test/task-daily-confirm",
            data: {
                factory: dataset.factory,
                employee: dataset.empIdLoad,
                dem: dataset.demLoad,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#view-content").html("");
                if(!$.isEmptyObject(data)){
                    var tbody = '';
                    if(dataset.dem == "TE-CFT"){
                        for(i in data){
                            if(data[i].teCftData != null){
                                var dueDate = '';
                                if(data[i].teCftData.duedate === null){
                                    dueDate = "NA";
                                } else dueDate = data[i].teCftData.duedate.slice(0,10);
                                var model = data[i].project;
                                if(data[i].project.length > 16){
                                    model = data[i].project.slice(0,16) + '...';
                                }
                                var improveProject = convertNull(data[i].teCftData.improveProject);
                                if(convertNull(data[i].teCftData.improveProject).length > 12){
                                    improveProject = convertNull(data[i].teCftData.improveProject).slice(0,10) + '...';
                                }
                                tbody = '<div class="col-12 col-md-3 col-sm-6"><div class="panel stt">'
                                    +  '<div class="panel panel-flat panel-body" style="margin:0px; padding: 0px;"><table class="table table-sm-custom">'
                                    +  '<tr style="background-color: #FFC000; font-size: 12px; cursor: pointer;" onclick="loadTaskDaily(\''+data[i].inputDate+'\')" title="'+data[i].project+'"><td style="text-align: left;width: 60%;">'+convertNull(data[i].line)+'</td>'
                                    +  '<td style="text-align: right;">'+data[i].inputDate.slice(5,10)+' '+data[i].shift+'</td></tr>'
                                    +  '<tr><td>'+model+'</td><td>Status<a id="btnDeleteConfirm'+data[i].id+'" onclick="deleteConfirm('+data[i].id+')"><i class="fa fa-trash" style="float: right;top: 4px;"></i></a>'
                                    +  '<a id="btnEditConfirm'+data[i].id+'" onclick="editConfirm('+data[i].id+')"><i class="fa fa-edit" style="float: right;top: 4px;"></i></a>'
                                    +  '<a id="btnUndoConfirm'+data[i].id+'" class="hidden" onclick="undoConfirm('+data[i].id+')"><i class="fa fa-undo" style="float: right;top: 4px;"></i></a><a id="btnConfirmConfirm'+data[i].id+'" class="hidden" onclick="confirmConfirm('+data[i].id+')"><i class="fa fa-check" style="float: right;top: 4px;"></i></a></td></tr>'
                                    +  '<tr><td style="text-align: left;">Project Owner</td><td><span id="spProjectOwner'+data[i].id+'">'+convertNull(data[i].teCftData.projectOwner)+'</span><input id="txtEditProjectOwner'+data[i].id+'" type="text" class="form-control hidden" value="'+convertNull(data[i].teCftData.projectOwner)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Project Qty</td><td class="rateColor"><span id="spProjectQty'+data[i].id+'">'+convertNull(data[i].teCftData.projectQuantity)+'</span><input id="txtEditProjectQty'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teCftData.projectQuantity)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Status</td><td class="rateColor"><span id="spStatus'+data[i].id+'">'+convertNull(data[i].teCftData.status)+'</span><input id="txtEditStatus'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teCftData.status)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Abnormal</td><td class="rateColor"><span id="spAbnormal'+data[i].id+'">'+convertNull(data[i].teCftData.abnormal)+'</span><input id="txtEditAbnormal'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teCftData.abnormal)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Duedate</td><td style="text-align: right"><span id="spDuedate'+data[i].id+'">'+dueDate+'</span></td></tr>'
                                    +  '<tr><td style="text-align: left;">Work Off Day</td><td style="text-align: right;">'+convertWorkOffDates(data[i].workOffDay)+'</td></tr>'
                                    +  '<tr><td style="text-align: left;">Improve Project</td><td style="text-align: right;"><span id="spImproveProject'+data[i].id+'" title="'+convertNull(data[i].teCftData.improveProject)+'">'+improveProject+'</span><input id="txtEditImproveProject'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teCftData.improveProject)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Bonus Score</td><td style="text-align: right;"><span id="spBonusScore'+data[i].id+'">'+convertNull(data[i].teCftData.bonusScore)+'</span><input id="txtEditBonusScore'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teCftData.bonusScore)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left; color: red">Total Score</td><td style="text-align: right;"><span id="spScore'+data[i].id+'">'+convertNull(data[i].score)+'</span><input id="txtEditScore'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].score)+'" style="height:20px" /></td></tr>'
                                    +  '</table></div></div></div>';
                                $("#view-content").append(tbody);
                            }
                        }
                    } else{
                        for(i in data){
                            if(data[i].teOnlineData != null){
                                var model = data[i].project;
                                if(data[i].project.length > 16){
                                    model = data[i].project.slice(0,16) + '...';
                                }
                                var improveProject = convertNull(data[i].teOnlineData.improveProject);
                                if(convertNull(data[i].teOnlineData.improveProject).length > 12){
                                    improveProject = convertNull(data[i].teOnlineData.improveProject).slice(0,10) + '...';
                                }
                                tbody = '<div class="col-12 col-md-3 col-sm-6"><div class="panel stt">'
                                    +  '<div class="panel panel-flat panel-body" style="margin:0px; padding: 0px;"><table class="table table-sm-custom">'
                                    +  '<tr style="background-color: #FFC000; font-size: 12px; cursor: pointer;" onclick="loadTaskDaily(\''+data[i].inputDate+'\')" title="'+data[i].project+'"><td style="text-align: left;width: 60%;">'+data[i].line+'</td>'
                                    +  '<td style="text-align: right;">'+data[i].inputDate.slice(5,10)+' '+data[i].shift+'</td></tr>'
                                    +  '<tr><td>'+model+'</td><td>Status<a id="btnDeleteConfirm'+data[i].id+'" onclick="deleteConfirm('+data[i].id+')"><i class="fa fa-trash" style="float: right;top: 4px;"></i></a>'
                                    +  '<a id="btnEditConfirm'+data[i].id+'" onclick="editConfirm('+data[i].id+')"><i class="fa fa-edit" style="float: right;top: 4px;"></i></a>'
                                    +  '<a id="btnUndoConfirm'+data[i].id+'" class="hidden" onclick="undoConfirm('+data[i].id+')"><i class="fa fa-undo" style="float: right;top: 4px;"></i></a><a id="btnConfirmConfirm'+data[i].id+'" class="hidden" onclick="confirmConfirm('+data[i].id+')"><i class="fa fa-check" style="float: right;top: 4px;"></i></a></td></tr>'
                                    +  '<tr><td style="text-align: left;">Retest Rate(20)</td><td class="rateColor" title="'+data[i].teOnlineData.retestRateNote+'"><span id="spRetestRate'+data[i].id+'">'+convertNull(data[i].teOnlineData.retestRate)+'</span><input id="txtEditRetestRate'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teOnlineData.retestRate)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">NTF(20)</td><td class="rateColor" title="'+data[i].teOnlineData.ntfNote+'"><span id="spNTF'+data[i].id+'">'+convertNull(data[i].teOnlineData.ntf)+'</span><input id="txtEditNTF'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teOnlineData.ntf)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Hit Rate(20)</td><td class="rateColor" title="'+data[i].teOnlineData.hitRateNote+'"><span id="spHitRate'+data[i].id+'">'+convertNull(data[i].teOnlineData.hitRate)+'</span><input id="txtEditHitRate'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teOnlineData.hitRate)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">5s(20)</td><td class="rateColor" title="'+data[i].teOnlineData.fiveSNote+'"><span id="spFiveS'+data[i].id+'">'+convertNull(data[i].teOnlineData.fiveS)+'</span><input id="txtEditFiveS'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teOnlineData.fiveS)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Work Interactive(20)</td><td class="rateColor" title="'+data[i].teOnlineData.workInteractiveNote+'"><span id="spWorkInteractive'+data[i].id+'">'+convertNull(data[i].teOnlineData.workInteractive)+'</span><input id="txtEditWorkInteractive'+data[i].id+'" type="text" class="form-control hidden" value="'+convertNull(data[i].teOnlineData.workInteractive)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Work Off Days</td><td style="text-align: right;">'+convertWorkOffDates(data[i].workOffDay)+'</td></tr>'
                                     +  '<tr><td style="text-align: left;">Improve Project</td><td style="text-align: right;"><span id="spImproveProject'+data[i].id+'" title="'+convertNull(data[i].teOnlineData.improveProject)+'">'+improveProject+'</span><input id="txtEditImproveProject'+data[i].id+'" type="text" class="form-control hidden" value="'+convertNull(data[i].teOnlineData.improveProject)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left;">Bonus Score</td><td style="text-align: right;"><span id="spBonusScore'+data[i].id+'">'+convertNull(data[i].teOnlineData.bonusScore)+'</span><input id="txtEditBonusScore'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].teOnlineData.bonusScore)+'" style="height:20px" /></td></tr>'
                                    +  '<tr><td style="text-align: left; color: red">Total Score(100)</td><td style="text-align: right;"><span id="spScore'+data[i].id+'">'+convertNull(data[i].score)+'</span><input id="txtEditScore'+data[i].id+'" type="number" class="form-control hidden" value="'+convertNull(data[i].score)+'" style="height:20px" /></td></tr>'
                                    +  '</table></div></div></div>';
                                $("#view-content").append(tbody);
                            }
                        }
                    }
                    $(".loader").addClass("disableSelect");
                    applyStyleColor();
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
    }

    function convertNull(str){
        var strCon = str;
        if(str === null){
            strCon = "NA";
        }
        return strCon;
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
    function applyStyleColor(){
        var cols = $('.rateColor');
        for(let i=0; i<cols.length; i++){
            if(cols[i].firstChild.innerHTML == "NA"){
                cols[i].style.textAlign = 'right';
            } else if(Number(cols[i].firstChild.innerHTML) >= 20){
                cols[i].className = 'rateColor rateColorSuccess';
            } else {
                cols[i].className = 'rateColor rateColorDanger';
            }
        }
    }

    loadAddNewConfirm('ONLINE');
    function loadAddNewConfirm(dem){
        dataset.dem = 'TE-'+dem;
        var html = '';
        if(dem == "CFT"){
            html = '<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Employee No</label><div class="col-xs-9"><input type="text" name="employeeNo" class="form-control" value="'+dataset.empId+'"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Leader Confirm</label><div class="col-xs-9"><input type="text" name="leaderConfirm" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Score</label><div class="col-xs-9"><input type="text" name="score" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Retest Rate</label><div class="col-xs-9"><input type="number" name="retestRate" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">ntf</label><div class="col-xs-9"><input type="number" name="ntf" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Hit Rate</label><div class="col-xs-9"><input type="number" name="hitRate" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">5s</label><div class="col-xs-9"><input type="number" name="fiveS" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Work Interactive</label><div class="col-xs-9"><input type="number" name="workInteractive" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Project Owner</label><div class="col-xs-9"><input type="text" name="projectOwner" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Project Qty</label><div class="col-xs-9"><input type="number" name="projectQuantity" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Time Span</label><div class="col-xs-9"><input type="text" name="timeSpan" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Duedate</label><div class="col-xs-9"><div class="input-group"><span class="input-group-addon" ><i class="icon-calendar22"></i></span><input type="text" class="form-control daterange-single" side="right" name="duedate" ></div></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Status</label><div class="col-xs-9"><input type="number" name="status" class="form-control" placeholder=""></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Abnormal</label><div class="col-xs-9"><input type="int" name="abnormal" class="form-control" placeholder=""></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Improve Project</label><div class="col-xs-9"><input type="text" name="improveProject" class="form-control" placeholder=""></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Bonus Scrore</label><div class="col-xs-9"><input type="number" name="bonusScore" class="form-control" placeholder=""></div></div>';
            $('.setNewTask').html(html);
        } else if(dem == "ONLINE"){
            html = '<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Employee No</label><div class="col-xs-9"><input type="text" name="employeeNo" value="'+dataset.empId+'" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Leader Confirm</label><div class="col-xs-9"><input type="text" name="leaderConfirm" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Score</label><div class="col-xs-9"><input type="text" name="score" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Retest Rate (20)</label><div class="col-xs-9"><input type="number" name="retestRate" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">NTF (20)</label><div class="col-xs-9"><input type="number" name="ntf" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Hit Rate (20)</label><div class="col-xs-9"><input type="number" name="hitRate" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">5s (20)</label><div class="col-xs-9"><input type="number" name="fiveS" class="form-control"></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Work Interactive (20)</label><div class="col-xs-8"><input type="number" name="workInteractive" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Project Owner</label><div class="col-xs-9"><input type="text" name="projectOwner" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Project Qty</label><div class="col-xs-9"><input type="number" name="projectQuantity" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Time Span</label><div class="col-xs-9"><input type="text" name="timeSpan" class="form-control"></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Duedate</label><div class="col-xs-9"><div class="input-group"><span class="input-group-addon" ><i class="icon-calendar22"></i></span><input type="text" class="form-control daterange-single" side="right" name="duedate" ></div></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Status</label><div class="col-xs-9"><input type="number" name="status" class="form-control" placeholder=""></div></div>'
            +'<div class="form-group row no-margin hidden"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Abnormal</label><div class="col-xs-9"><input type="int" name="abnormal" class="form-control" placeholder=""></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Improve Project</label><div class="col-xs-9"><input type="text" name="improveProject" class="form-control" placeholder=""></div></div>'
            +'<div class="form-group row no-margin"><label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Bonus Scrore</label><div class="col-xs-9"><input type="number" name="bonusScore" class="form-control" placeholder=""></div></div>';
            $('.setNewTask').html(html);
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

    function postConfirm(){
        var addConfirm = {
            "factory" :  dataset.factory,
            "employeeId" :  $('#modal-resource-edit input[name="employeeNo"]').val(),
            "leaderConfirm" :  $('#modal-resource-edit input[name="leaderConfirm"]').val(),
            "score" :  Number($('#modal-resource-edit input[name="score"]').val()),
            "retestRate" :  Number($('#modal-resource-edit input[name="retestRate"]').val()),
            "ntf" :  Number($('#modal-resource-edit input[name="ntf"]').val()),
            "hitRate" :  Number($('#modal-resource-edit input[name="hitRate"]').val()),
            "fiveS" :  Number($('#modal-resource-edit input[name="fiveS"]').val()),
            "workInteractive" : Number($('#modal-resource-edit input[name="workInteractive"]').val()),
            "projectOwner" : $('#modal-resource-edit input[name="projectOwner"]').val(),
            "projectQuantity" :  Number($('#modal-resource-edit input[name="projectQuantity"]').val()),
            "timeSpan" :  $('#modal-resource-edit input[name="timeSpan"]').val(),
            "duedate" :  $('#modal-resource-edit input[name="duedate"]').val(),
            "status" :  Number($('#modal-resource-edit input[name="status"]').val()),
            "abnormal" :  Number($('#modal-resource-edit input[name="abnormal"]').val()),
            "improveProject" :  $('#modal-resource-edit input[name="improveProject"]').val(),
            "bonusScore" :  Number($('#modal-resource-edit input[name="bonusScore"]').val())
        }

        $.ajax({
            type: "POST",
            url: "/api/test/task-daily-confirm",
            data: JSON.stringify(addConfirm),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(data)
                    loadTaskDailyConfirm();
                else alert("Add New Fail!");
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
    }
    function editConfirm(id){
        $('#spProjectOwner'+id).addClass('hidden');
        $('#txtEditProjectOwner'+id).removeClass('hidden');

        $('#spProjectQty'+id).addClass('hidden');
        $('#txtEditProjectQty'+id).removeClass('hidden');

        $('#spStatus'+id).addClass('hidden');
        $('#txtEditStatus'+id).removeClass('hidden');

        $('#spAbnormal'+id).addClass('hidden');
        $('#txtEditAbnormal'+id).removeClass('hidden');

        $('#spImproveProject'+id).addClass('hidden');
        $('#txtEditImproveProject'+id).removeClass('hidden');

        $('#spBonusScore'+id).addClass('hidden');
        $('#txtEditBonusScore'+id).removeClass('hidden');

        // $('#spScore'+id).addClass('hidden');
        // $('#txtEditScore'+id).removeClass('hidden');

        // $('#spScore'+id).addClass('hidden');
        // $('#txtEditScore'+id).removeClass('hidden');

        $('#spRetestRate'+id).addClass('hidden');
        $('#txtEditRetestRate'+id).removeClass('hidden');
        $('#spHitRate'+id).addClass('hidden');
        $('#txtEditHitRate'+id).removeClass('hidden');
        $('#spNTF'+id).addClass('hidden');
        $('#txtEditNTF'+id).removeClass('hidden');
        $('#spFiveS'+id).addClass('hidden');
        $('#txtEditFiveS'+id).removeClass('hidden');
        $('#spWorkInteractive'+id).addClass('hidden');
        $('#txtEditWorkInteractive'+id).removeClass('hidden');

        $('#btnEditConfirm'+id).addClass('hidden');
        $('#btnDeleteConfirm'+id).addClass('hidden');
        $('#btnConfirmConfirm'+id).removeClass('hidden');
        $('#btnUndoConfirm'+id).removeClass('hidden');
    }

    function undoConfirm(id){
        $('#spProjectOwner'+id).removeClass('hidden');
        $('#txtEditProjectOwner'+id).addClass('hidden');

        $('#spProjectQty'+id).removeClass('hidden');
        $('#txtEditProjectQty'+id).addClass('hidden');

        $('#spStatus'+id).removeClass('hidden');
        $('#txtEditStatus'+id).addClass('hidden');

        $('#spAbnormal'+id).removeClass('hidden');
        $('#txtEditAbnormal'+id).addClass('hidden');

        $('#spImproveProject'+id).removeClass('hidden');
        $('#txtEditImproveProject'+id).addClass('hidden');

        $('#spBonusScore'+id).removeClass('hidden');
        $('#txtEditBonusScore'+id).addClass('hidden');

        // $('#spScore'+id).removeClass('hidden');
        // $('#txtEditScore'+id).addClass('hidden');

        // $('#spScore'+id).removeClass('hidden');
        // $('#txtEditScore'+id).addClass('hidden');

        $('#spRetestRate'+id).removeClass('hidden');
        $('#txtEditRetestRate'+id).addClass('hidden');
        $('#spHitRate'+id).removeClass('hidden');
        $('#txtEditHitRate'+id).addClass('hidden');
        $('#spNTF'+id).removeClass('hidden');
        $('#txtEditNTF'+id).addClass('hidden');
        $('#spFiveS'+id).removeClass('hidden');
        $('#txtEditFiveS'+id).addClass('hidden');
        $('#spWorkInteractive'+id).removeClass('hidden');
        $('#txtEditWorkInteractive'+id).addClass('hidden');

        $('#btnEditConfirm'+id).removeClass('hidden');
        $('#btnDeleteConfirm'+id).removeClass('hidden');
        $('#btnConfirmConfirm'+id).addClass('hidden');
        $('#btnUndoConfirm'+id).addClass('hidden');
    }

    function deleteConfirm(id){
        var del = window.confirm("Do you want to DELETE?");
        if(del){
            $.ajax({
                type: "DELETE",
                url: "/api/test/task-daily-confirm/"+id,
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    if(data)
                        loadTaskDailyConfirm();
                    else alert("Delete Fail!")
                },
                failure: function(errMsg) {
                        console.log(errMsg);
                }
            });
        }
    }

    function confirmConfirm(id){
        var editConfirm = {};
        if(dataset.dem == "TE-CFT"){
            editConfirm = {
                "factory" :  dataset.factory,
                "employeeId" :  dataset.empIdLoad,
                "leaderConfirm" :  dataset.empId,
                "score" :  Number($('#txtEditScore'+id).val()),
                "retestRate" :  "",
                "ntf" :  "",
                "hitRate" :  "",
                "fiveS" : "",
                "workInteractive" : "",
                "projectOwner" : $('#txtEditProjectOwner'+id).val(),
                "projectQuantity" : Number($('#txtEditProjectQty'+id).val()),
                "timeSpan" :  "",
                "status" :  Number($('#txtEditStatus'+id).val()),
                "abnormal" :  Number($('#txtEditAbnormal'+id).val()),
                "improveProject" :  $('#txtEditImproveProject'+id).val(),
                "bonusScore" :  Number($('#txtEditBonusScore'+id).val())
            }
            if($('#spDuedate'+id).html() != 'NA' && $('#spDuedate'+id).html() != ''){
                editConfirm.duedate = moment($('#spDuedate'+id).html()).format("YYYY/MM/DD");
            }
        } else{
            editConfirm = {
                "factory" :  dataset.factory,
                "employeeId" :  dataset.empIdLoad,
                "leaderConfirm" :  dataset.empId,
                "score" :  Number($('#txtEditScore'+id).val()),
                "retestRate" : Number($('#txtEditRetestRate'+id).val()),
                "ntf" : Number($('#txtEditNTF'+id).val()),
                "hitRate" : Number($('#txtEditHitRate'+id).val()),
                "fiveS" : Number($('#txtEditFiveS'+id).val()),
                "workInteractive" : Number($('#txtEditWorkInteractive'+id).val()),
                "projectOwner" : $('#txtEditProjectOwner'+id).val(),
                "projectQuantity" : "",
                "timeSpan" :  "",
                "duedate" : "",
                "status" : "",
                "abnormal" : "",
                "improveProject" : $('#txtEditImproveProject'+id).val(),
                "bonusScore" : Number($('#txtEditBonusScore'+id).val())
            }
        }

        $.ajax({
            type: "PUT",
            url: "/api/test/task-daily-confirm/"+id,
            data: JSON.stringify(editConfirm),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(data)
                    loadTaskDailyConfirm();
                else alert("Edit Fail!")
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
    }

    function loadTaskDaily(time){
        var timeText = moment(time).format("YYYY/MM/DD") + ' 07:30:00' + ' - ' + moment(time).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
        $("#modalTask").modal('show')
        $.ajax({
            type: "GET",
            url: "/api/test/task-daily",
            data: {
                factory: dataset.factory,
                employee: dataset.empIdLoad,
                resourceGroup: dataset.demLoad,
                timeSpan: timeText
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#tblOwner tbody").html("");
                var tbody = '';
                for(let i=0; i< data.length; i++){
                    if(data[i].duedate == null){
                        data[i].duedate = "";
                    }
                    if(data[i].employeeId == dataset.empIdLoad){
                        tbody += '<tr id="row'+data[i].id+'"><td>' + (Number(i)+1) +'</td><td class="hidden">' + convertNull(data[i].employeeId) +'</td>'
                            +  '<td class="hidden"><span>' + convertNull(data[i].employeeName) +'</span></td>'
                            +  '<td><span id="spTaskTitle'+data[i].id+'">' + data[i].taskTitle +'</span><input class="form-control hidden" type="text" id="txtEditTaskTitle' + data[i].id +'" value="' + data[i].taskTitle +'" /></td>'
                            +  '<td><span id="spTaskContent'+data[i].id+'">' + convertNull(data[i].taskContent) +'</span><textarea class="form-control hidden" type="text" id="txtEditTaskContent' + data[i].id +'" rows="1">' + data[i].taskContent +'</textarea></td>'
                            +  '<td><span id="spTaskStatus'+data[i].id+'" class="' + data[i].status.toLowerCase() +'">' + data[i].status +'</span><select class="form-control hidden" id="slEditStatus'+data[i].id+'"><option value="PENDING">PENDING</option><option value="ON_GOING">ON_GOING</option><option value="DONE">DONE</option><option value="CLOSED">CLOSED</option></select></td>'
                            +  '<td><span id="spTaskDueDate'+data[i].id+'">' + data[i].duedate +'</span><input class="form-control hidden" type="text" id="txtEditTaskDueDate' + data[i].id +'" value="' + data[i].duedate +'" /></td>'
                            +  '<td><span id="spCreated'+data[i].id+'">' + data[i].createdAt +'</span></td>'
                            +  '<td><span id="spUpdated'+data[i].id+'">' + data[i].updatedAt +'</span></td>'
                            +  '<td><span id="spTaskNote'+data[i].id+'">' + convertNull(data[i].note) +'</span><textarea class="form-control hidden" type="text" id="txtEditNote'+data[i].id+'" rows="1">' + data[i].note +'</textarea></td>'
                            +  '<td><button id="btnComment' + data[i].id + '" class="btn btn-sm" style="width: 26px; height: 26px; padding: 3px; margin: 0px 2px; color: #272727; background: #34bcf9;" onclick="showComment(' + data[i].id + ')"><i class="fa fa-comment" style="color: #272727;"></i></button></td></tr>';
                    }
                }
                $("#tblOwner tbody").html(tbody);
                $(".loader").addClass("disableSelect");
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            }
        });
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
                var value = '<tr class="rowComment rowComment'+id+'" style="background-color: #efefef"><td colspan="10">'
                            +  '<div class="direct-chat">'
                            +  '<div class="card-header" style="border-bottom: 1px solid rgba(0,0,0,.125);">'
                            +  '<h3 style="margin: unset;" class="card-title">Comment<button class="btn btn-danger" style="padding: 3px 10px;float: right; color: #272727" onclick="closePopup('+id+')"><i class="fa fa-times"></i></button></h3></div>'
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

    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).format("YYYY/MM/01") + ' 07:30:00';
                dataset.timeSpan = startDate + ' - ' + endDate;
                loadData();
                $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
                $('input[name=timeSpan]').on('change', function(event) {
                    dataset.timeSpan = this.value;
                    loadData();
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
</script>