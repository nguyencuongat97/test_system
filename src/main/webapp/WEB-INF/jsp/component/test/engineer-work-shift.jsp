<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<div class="loader"></div>
<div class="row">
    <div class="col-lg-12" style="background: #ffffff;">
        <div class="panel panel-overview" id="header" style="font-size: 14px;">
            <span class="text-uppercase">Work Shift Handover</span>
        </div>
        <div class="row" style="margin: unset;">
            <div class="row" style="margin: 0 0 10px 0;">
                <div class="col-md-9 col-xs-6 no-padding">
                    <div class="input-group" style="width: 96%;">
                        <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                        <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
                    </div>
                </div>
                <!-- <div class="col-md-1 col-xs-1"> -->
                    <!-- <button class="btn btn-xs" href="#modal-work-shift-edit" data-toggle="modal" onclick="editWorkShift()"><i class="icon-plus2"></i></button> -->
                <!-- </div> -->
                <!-- <div class="col-md-7 col-xs-3"></div> -->
                <div class="col-md-3 col-xs-6 form-group no-padding no-margin">
                    <input id="txtFilterWorkShift" class="form-control" type="text" placeholder="Search" style=" width: 70%;float: left; margin-right:5px; background: #f0f0f0; padding: 0px 10px; border-radius: 4px; height: 27px;" />
                    <button class="btn filterWorkShift filter-option" id="idbtnWorkShift" style="float: left;
                    width: 27%; height: 28px;font-size: 10px;padding: 0px 0px;" onclick="showFilter()"><i class="fa fa-search"></i> Filter</button>
                </div>
            </div>
            <div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 200px);">
                <table id="tblWorkShift" name ="workShift" class="table table-xxs table-bordered table-sticky">
                    <thead id="theadLine">
                        <tr>
                            <th style="width: 2%;">Line</th>
                            <th style="width: 2%;">Shift</th>
                            <th style="width: 10%;" >Owner</th>
                            <th style="width: 6%;">Model</th>
                            <th style="width: 3%;">Station</th>
                            <th style="width: 3%;">OK</th>
                            <th style="width: 3%;">NG</th>
                            <th style="width: 30%;" colspan="3">Top High Retest Rate</th>
                            <th style="width: 15%;">Setup (New or add more)</th>
                            <th style="width: 12%;">Repaired Items</th>
                            <th style="width: 11%;">Abnormal</th>
                            <th style="width: 15%;">Notice</th>
                            <th style="z-index: 1; width: 5%;">Event</th>
                        </tr>
                    </thead>
                    <tbody id="tbodyLine"></tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- Modal Edit -->
<div id="modal-work-shift-edit" class="modal fade" role="dialog">
	<div class="modal-dialog" style="font-size: 13px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">EDIT</h6>
            </div>
        	<div class="modal-body">
                <div class="row">
                    <input type="hidden" name="id"/>
                    <input type="hidden" name="workDate"/>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Employee No</label>
                        <div class="col-xs-9">
                            <input id="idOwner" type="text" disabled name="owner" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Name</label>
                        <div class="col-xs-9">
                            <input id="idEmployeeNo" type="text" disabled name="employeeNo" class="form-control">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Model</label>
                        <div class="col-xs-9">
                            <input type="text" name="modelName" disabled class="form-control" placeholder="">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Station</label>
                        <div class="col-xs-9">
                            <input type="text" name="station" disabled class="form-control" placeholder="">
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Line</label>
                        <div class="col-xs-3">
                            <input type="text" name="lineName" disabled class="form-control" placeholder="">
                        </div>
                        <label class="col-xs-2 control-label text-bold" style="padding-top: 8px;">Shift</label>
                        <div class="col-xs-4">
                            <select class="form-control bootstrap-select" disabled name="shift">
                                <option value="DAY">DAY</option>
                                <option value="NIGHT">NIGHT</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Setup (New or add more)</label>
                        <div class="col-xs-9">
                            <textarea type="text" name="setup" class="form-control" placeholder=""></textarea>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Repaired Items</label>
                        <div class="col-xs-9">
                            <textarea type="text" name="repair" class="form-control" placeholder=""></textarea>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Abnormal</label>
                        <div class="col-xs-9">
                            <textarea type="text" name="abnormal" class="form-control" placeholder=""></textarea>
                        </div>
                    </div>
                    <div class="form-group row no-margin">
                        <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Notice</label>
                        <div class="col-xs-9">
                            <textarea type="text" name="notice" class="form-control" placeholder=""></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="post();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
	</div>
</div>
<!-- Model Delete -->
<!-- <div id="modal-work-shift-delete" class="modal fade" role="dialog">
	<div class="modal-dialog" style="font-size: 13px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">DELETE</h6>
            </div>
        	<div class="modal-body">
                <div class="row">
                    <div class="form-group">
                        <label class="col-xs-12 control-label" style="padding-top: 8px;">Do you want to delete <span name="spanModel"></span> ?</label>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross position-left"></i>Close</button>
                <button class="btn btn-sm" data-dismiss="modal" onclick="deleteWorkShift();">Submit<i class="icon-arrow-right14 position-right"></i></button>
            </div>
        </div>
	</div>
</div> -->
<style>
#tblWorkShift thead th{
    padding: 3px 3px !important;
    /* background-color: #FFC000; */
}
#idpopover{
    color: #333;
}
.table-xxs > tbody > tr > td{
    padding: 3px 3px !important;
}
.popover{
    max-height: 223px;
    overflow: scroll;
    border: 1px solid #ccc;
    /* left: 435px !important; */
}
.pemployeeNo{
    font-weight: 500;
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
    };
getTimeNow();
function getTimeNow() {
    $.ajax({
        type: "GET",
        url: "/api/time/now",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var current = new Date(data);
            var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
            var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
            $('.datetimerange[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
            $('.datetimerange[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
            $('.datetimerange[side=right]').daterangepicker({
                maxSpan: {
                    days: 1
                },
                timePicker: true,
                timePicker24Hour: true,
                opens: "right",
                applyClass: 'bg-slate-600',
                cancelClass: 'btn-default',
                timePickerIncrement: 30,
                locale: {
                    format: 'YYYY/MM/DD HH:mm'
                }
            });
            dataset.timeSpan = startDate + ' - ' + endDate;
            loadTableWorkShift('');

            $('input[name=timeSpan]').on('change', function(event) {
                dataset.timeSpan = event.target.value;
                loadTableWorkShift('');
            });
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}
function loadTableWorkShift(search){
    $('.loader').css('display', 'block');
    $.ajax({
        type: 'GET',
        url: '/api/test/work-handover',
        data: {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan,
            shift: dataset.shift
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if (!$.isEmptyObject(data)) { 
                $('#tbodyLine').html('');
                var html='';
                var idstt = 1;
                    for (i in data) {
                        var getvalueStationOK = data[i].stationOK;
                        var getvalueStationNG = data[i].stationNG;
                        var getvalueStationNGTop = data[i].stationNGTop;

                        if (getvalueStationOK== null) {
                            getvalueStationOK = 0;
                        }else{
                            getvalueStationOK = data[i].stationOK.split(/[\s,]+/);
                        }
                        if (getvalueStationNG== null) {
                            getvalueStationNG = 0;
                        }else{
                            getvalueStationNG = data[i].stationNG.split(/[\s,]+/);
                        }
                        if (getvalueStationNGTop== null) {
                            getvalueStationNGTop = 0;
                        }else{
                            getvalueStationNGTop = data[i].stationNGTop.split(/[\s,]+/);
                        }
                        // console.log("data[i].stationOK", data[i].workDate);
                        var totalTop ='';
                        for (var index = 0; index < 3; index++) {
                            if(getvalueStationNGTop[index] == undefined){
                                totalTop += '<td style="width:5%"></td>';
                            } else{
                                totalTop += '<td style="width:5%">'+getvalueStationNGTop[index]+'</td>';
                            }
                        }
                        var hoverOK ='';
                        var totalOK=0, value='';
                        for (var index = 0; index < getvalueStationOK.length; index++) {
                            if(getvalueStationOK[index] != ''){
                                totalOK++;
                                value += '<div><b>'+totalOK+': </b><span>' + getvalueStationOK[index] + '</span></div>';
                            }
                        }
                        var totalNG=0, valueNG='';
                        for (var index = 0; index < getvalueStationNG.length; index++) {
                            if(getvalueStationNG[index] != ''){
                                totalNG++;
                                valueNG += '<div><b>'+totalNG+': </b><span>' + getvalueStationNG[index] + '</span></div>';
                            }
                        }
                        var valueSetup = checkNull(data[i].setupOrAdd);
                        var valueRepair = checkNull(data[i].repair);
                        var valueAbnormal = checkNull(data[i].abnormal);
                        var valueNotice = checkNull(data[i].notice);
                        var classRow = data[i].lineName.replace(/\s+/g,'');
                        var classShift = data[i].shift.replace(/\s+/g,'');
                        var classOwner = data[i].owner.replace(/\s+/g,'');
                        // var classEmployeeNo = data[i].employeeNo.replace(/\s+/g,'');
                        var classModel = data[i].modelName.replace('.','_');
                        var str = data[i].workDate;
                        var strWorkDate = str.slice(5);
                        // console.log("search:::", search);
                        if(search==''){
                            html += '<tr><td class="row'+classRow+' valueLineName'+idstt+'">'+data[i].lineName+'</td>'
                                        +'<td class="row'+classRow+classShift+' valueShift'+idstt+' " ><b class="class-date">'+strWorkDate+'</b> ('+data[i].shift+')</td>'
                                        +'<td class="row'+classRow+classShift+classOwner+'pls valueOwner'+idstt+'" ><a class="text-bold classOwner" data-owner="'+data[i].owner+'">'+data[i].employeeNo+' <p class="pemployeeNo">('+data[i].owner+')</p></a></td>'
                                        +'<td class="row'+classRow+classShift+classOwner+classModel+' valueModelName'+idstt+'" ><a class="text-bold classModelName" data-model="'+data[i].modelName+'">'+data[i].modelName+'</a></td>'
                                        +'<td class="valueGroupName'+idstt+'">'+data[i].groupName+'</td>'
                                        +'<td class="valuePopover" data-toggle="popover" data-trigger="hover click" data-placement="auto right" data-html="true" data-content="' + value + '">'
                                            +'<a class="text-bold" >'+totalOK+'</a>'
                                        +'</td>'
                                        +'<td class="valuePopover" data-toggle="popover" data-trigger="hover click" data-placement=" auto right" data-html="true" data-content="' + valueNG + '">'
                                            +'<a class="text-bold">'+totalNG+'</a>'
                                        +'</td>'
                                        +totalTop
                                        +'<td class="valueSetup'+idstt+'">'+valueSetup+'</td>'
                                        +'<td class="valueRepair'+idstt+'">'+valueRepair+'</td>'
                                        +'<td class="valueAbnormal'+idstt+'" >'+valueAbnormal+'</td>'
                                        +'<td  class="valuenotice'+idstt+'">'+valueNotice+'</td>'
                                        +'<td>'
                                            + '<button class="btn" id="btnEditWorkShift'+idstt+'" style="width: 26px; height: 26px; padding: 3px; margin: 2px; background:#ffc107;" href="#modal-work-shift-edit" data-toggle="modal" data-id="'+data[i].id+'" data-shift="'+data[i].shift+'" data-employee="'+data[i].employeeNo+'" data-owner="'+data[i].owner+'" data-line="'+data[i].lineName+'" data-model="'+data[i].modelName+'" data-station="'+data[i].groupName+'" data-abnormal="'+data[i].abnormal+'" data-setup="'+data[i].setupOrAdd+'" data-repair="'+data[i].repair+'" data-setup="'+data[i].setupOrAdd+'" data-notice="'+data[i].notice+'"data-date="'+data[i].workDate+'" onclick="editWorkShift(this)"><i class="fa fa-edit"></i></button>'
                                            // +    '<button class="btn btn-danger" id="btnDeleteWorkShift'+idstt+'" data-id="9"  href="#modal-work-shift-delete" data-toggle="modal"  data-id="'+data[i].id+'" data-shift="'+data[i].shift+'" data-owner="'+data[i].owner+'" data-line="'+data[i].lineName+'" data-model="'+data[i].modelName+'" data-station="'+data[i].groupName+'" data-abnormal="'+data[i].abnormal+'" data-repair="'+data[i].repair+'" data-notice="'+data[i].notice+'" onclick="btndeleteWorkShift(this)" style="width: 26px; height: 26px; padding: 3px; margin: 2px;"><i class="fa fa-trash-o"></i></button>'
                                        +'</td>'
                                    +'</tr>';
                            idstt++;
                        }else if(data[i].modelName.indexOf(search) > -1 || data[i].owner.indexOf(search) > -1){
                            html += '<tr><td class="row'+classRow+' valueLineName'+idstt+'">'+data[i].lineName+'</td>'
                                        +'<td class="row'+classRow+classShift+' valueShift'+idstt+' "><b class="class-date">'+strWorkDate+'</b> ('+data[i].shift+')</td>'
                                        +'<td class="row'+classRow+classShift+classOwner+'pls valueOwner'+idstt+'" ><a class="text-bold classOwner" data-owner="'+data[i].owner+'">'+data[i].employeeNo+' <p class="pemployeeNo">('+data[i].owner+')</p></a></td>'
                                        +'<td class="row'+classRow+classShift+classOwner+classModel+' valueModelName'+idstt+'" ><a class="text-bold classModelName" data-model="'+data[i].modelName+'">'+data[i].modelName+'</a></td>'
                                        +'<td class="valueGroupName'+idstt+'">'+data[i].groupName+'</td>'
                                        +'<td class="valuePopover" data-toggle="popover" data-trigger="hover click" data-placement=" auto right" data-html="true" data-content="' + value + '">'
                                            +'<a class="text-bold" >'+totalOK+'</a>'
                                        +'</td>'
                                        +'<td class="valuePopover" data-toggle="popover" data-trigger="hover click" data-placement="auto right" data-html="true" data-content="' + valueNG + '">'
                                            +'<a class="text-bold">'+totalNG+'</a>'
                                        +'</td>'
                                        +totalTop
                                        +'<td class="valueSetup'+idstt+'">'+valueSetup+'</td>'
                                        +'<td class="valueRepair'+idstt+'">'+valueRepair+'</td>'
                                        +'<td class="valueAbnormal'+idstt+'" >'+valueAbnormal+'</td>'
                                        +'<td  class="valuenotice'+idstt+'">'+valueNotice+'</td>'
                                        +'<td>'
                                            + '<button class="btn" id="btnEditWorkShift'+idstt+'" style="width: 26px; height: 26px; padding: 3px; margin: 2px; background:#ffc107;" href="#modal-work-shift-edit" data-toggle="modal" data-id="'+data[i].id+'" data-shift="'+data[i].shift+'" data-employee="'+data[i].employeeNo+'" data-owner="'+data[i].owner+'" data-line="'+data[i].lineName+'" data-model="'+data[i].modelName+'" data-station="'+data[i].groupName+'" data-abnormal="'+data[i].abnormal+'" data-setup="'+data[i].setupOrAdd+'" data-repair="'+data[i].repair+'" data-notice="'+data[i].notice+'"  data-date="'+data[i].workDate+'" onclick="editWorkShift(this)"><i class="fa fa-edit"></i></button>'
                                            // +    '<button class="btn btn-danger" id="btnDeleteWorkShift'+idstt+'" data-id="9"  href="#modal-work-shift-delete" data-toggle="modal"  data-id="'+data[i].id+'" data-shift="'+data[i].shift+'" data-owner="'+data[i].owner+'" data-line="'+data[i].lineName+'" data-model="'+data[i].modelName+'" data-station="'+data[i].groupName+'" data-abnormal="'+data[i].abnormal+'" data-repair="'+data[i].repair+'" data-notice="'+data[i].notice+'" onclick="btndeleteWorkShift(this)" style="width: 26px; height: 26px; padding: 3px; margin: 2px;"><i class="fa fa-trash-o"></i></button>'
                                        +'</td>'
                                    +'</tr>';
                            idstt++;
                            }
                        }
                        $('#tbodyLine').append(html);
                        // $('[data-toggle=popover]').popover({container: 'body', placement: 'auto right',  trigger: 'hover click', html: true});
                        $('[data-toggle=popover]').popover({
                            container: 'body', 
                            placement: 'auto right',
                            trigger: "hover click",
                            html: true
                        }).on("show.bs.popover", function(){
                            if (window._bsPopover) {
                                $(window._bsPopover).popover('hide')
                            }
                            window._bsPopover= this;
                        }).on('hide.bs.popover', function (){
                            window._bsPopover = null;
                        });
                        $('.classOwner').on('click', function (event) {
                        var url = "/engineer-management?factory=" + dataset.factory;
                            // openWindow(url);
                            window.location.href = url;
                        });
                        $('.classModelName').on('click', function (event) {
                        var url = "/station-detail?factory=" + dataset.factory + "&modelName=" +this.dataset.model;
                            // openWindow(url);
                            window.location.href = url;
                        });
                } else {
                    $('#tbodyLine').html('<tr><td colspan="15" align="center">-- NO DATA --</td></tr>');
                }
                for(i in data){
                    var classRow = data[i].lineName.replace(/\s+/g,'');
                    var classShift = data[i].shift.replace(/\s+/g,'');
                    var classOwner = data[i].owner.replace(/\s+/g,'');
                    // var classEmployeeNo = data[i].employeeNo.replace(/\s+/g,'');
                    var classModel = data[i].modelName.replace('.','_');
                    var getClassRow = $('.row'+classRow);
                    var getClassShift = $('.row'+classRow+classShift);
                    var getClassOwner = $('.row'+classRow+classShift+classOwner+'pls');
                    console.log('.row'+classRow+classShift+classOwner)
                    var getClassModel = $('.row'+classRow+classShift+classOwner+classModel);
                    // console.log("getClassRow",getClassRow.length);
                    if(getClassRow.length > 1){
                        for(i in getClassRow){
                            getClassRow[0].rowSpan = getClassRow.length;
                            if(i > 0){
                                getClassRow[i].className += ' hidden';
                            }
                        }
                    }
                    if (getClassShift.length > 1) {
                        for(i in getClassShift){
                            getClassShift[0].rowSpan = getClassShift.length;
                            if(i > 0){
                                getClassShift[i].className += ' hidden';
                            }
                        }
                    }
                    if (getClassOwner.length > 1) {
                        for(i in getClassOwner){
                            getClassOwner[0].rowSpan = getClassOwner.length;
                            if(i > 0){
                                getClassOwner[i].className += ' hidden';
                            }
                        }
                    }
                    if (getClassModel.length > 1) {
                        for(i in getClassModel){
                        getClassModel[0].rowSpan = getClassModel.length;
                            if(i > 0){
                                getClassModel[i].className += ' hidden';
                            }
                        }
                    }
                }
        },
        failure: function(errMsg) {
                console.log(errMsg);
        },
        complete: function(){
           $('.loader').css('display', 'none');
        }
    });
}
function checkNull(text){
    var result ="";
    if (text !="null" && text != null) {
        result = text;
    } 
    return result;
}
function editWorkShift(context){
    var setup= checkNull(context.dataset.setup);
    var repair= checkNull(context.dataset.repair);
    var abnormal= checkNull(context.dataset.abnormal);
    var notice= checkNull(context.dataset.notice);
    console.log("pls::", context.dataset.owner);
    var workDate = checkNull(context.dataset.date);
    $('input[name="id"]').val(context.dataset.id);
    $('input[name="lineName"]').val(context.dataset.line);
    $('input[name="employeeNo"]').val(context.dataset.employee);
    $('input[name="owner"]').val(context.dataset.owner);
    $('input[name="modelName"]').val(context.dataset.model);
    $('select[name="shift"]').val(context.dataset.shift);
    $('input[name="workDate"]').val(workDate);
    $('input[name="station"]').val(context.dataset.station);
    $('textarea[name="setup"]').val(setup);
    $('textarea[name="abnormal"]').val(abnormal);
    $('textarea[name="repair"]').val(repair);
    $('textarea[name="notice"]').val(notice);
}
function post(){
    var arr={
        id: $('#modal-work-shift-edit input[name="id"]').val(),
        factory: dataset.factory,
        owner: $('#modal-work-shift-edit input[name="owner"]').val(),
        employeeNo: $('#modal-work-shift-edit input[name="employeeNo"]').val(),
        modelName: $('#modal-work-shift-edit input[name="modelName"]').val(),
        lineName: $('#modal-work-shift-edit input[name="lineName"]').val(),
        shift: $('#modal-work-shift-edit select[name="shift"]').val(),
        workDate: $('#modal-work-shift-edit input[name="workDate"]').val(),
        groupName: $('#modal-work-shift-edit input[name="station"]').val(),
        setupOrAdd: $('#modal-work-shift-edit textarea[name="setup"]').val().replace(/\r\n|\r|\n/g,"<br />"),
        repair: $('#modal-work-shift-edit textarea[name="repair"]').val().replace(/\r\n|\r|\n/g,"<br />"),
        abnormal: $('#modal-work-shift-edit textarea[name="abnormal"]').val().replace(/\r\n|\r|\n/g,"<br />"),
        notice: $('#modal-work-shift-edit textarea[name="notice"]').val().replace(/\r\n|\r|\n/g,"<br />")
    };
    $.ajax({
        type: "POST",
        url: "/api/test/work-handover",
        data: JSON.stringify(arr),
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if(data){
                alert("Edit Success!!");
                loadTableWorkShift('');
            } else{
                alert("Edit faile!!");
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}
function showFilter(){
    var search = $("#txtFilterWorkShift").val();
    loadTableWorkShift(search);
}
// function btndeleteWorkShift(context){
//     $('input[name="id"]').val(context.dataset.id);
//     $('span[name="spanModel"]').html(context.dataset.model);
// }
// function deleteWorkShift() {
//     var data = new FormData();
//     data.append('factory', dataset.factory);
//     data.append('id', $('#modal-work-shift-edit input[name="id"]').val());
//     $.ajax({
//         type: "DELETE",
//         url: "/api/test/work-handover",
//         data: data,
//         processData: false,
//         contentType: false,
//         mimeType: "multipart/form-data",
//         success: function(data){
//             loadTableWorkShift('');
//         },
//         failure: function(errMsg) {
//             console.log(errMsg);
//         }
//     });
// }
$('select[name="filter-shift"]').on('change', function() {
    dataset.shift = this.value;
    loadTableWorkShift('');
});
</script>