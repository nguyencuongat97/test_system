<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<style>
#tblTrackingAll tr th{
    background-color: #F79646 !important;
    color: #272727;
    text-align: center;
}
#tblTrackingAll{
    table-layout: fixed;
    /* word-wrap: break-word; */
    max-width: 15000px;
}
#realTime td, .uph-target{
    background-color: #04318F !important;
    color: #fff;
    text-align: center;
}
.shiftDay, .shiftNight{
    width: 120px;
}
.sumAcc{
    background-color: #92D050 !important;
    color: #272727;
}
.table-sticky thead td {
    position: -webkit-sticky;
    position : sticky;
    top : 50px;
}
.table-sticky thead td {
    border-top: none !important;
    border-bottom: none !important;
    box-shadow: inset 0 1px 0 #fff,
                inset 0 -1px 0 #fff;
}
</style>
<div class="loader"></div>
<div class="panel panel-re panel-flat row"  style="background-color: #272727; text-align: center; color: #ccc">
    <div class="col-lg-12">
         <div class="panel panel-overview" id="header">
            <b><span id="titlePage">B06</span><span> - UPH Tracking Dashboard All Model </span><span id="txtDateTime"></span></b>
        </div>
        <div class="row no-padding no-margin">
            <div class="drlMenu no-margin" style="width: 10%">
                <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control daterange-single" side="right" name="timeSpan" style="border-bottom: 0px !important; color: #fff;" />
                </div>
            </div>
            <div class="btn-group" style="margin: 0 15px; float:left">
                <button id="a-shift" class="btn btn-default select-shift selected" onclick="loadDataTypeShift('all')">All</button>
                <button id="d-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('day-shift')">Day</button>
                <button id="n-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('night-shift')">Night</button>
            </div>
            <div class='drlMenu selectModel hidden'>
                <div class="dropdown dropdown-select-model">
                    <button  style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">Select Model<span class="caret"></span></button>
                    <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel"></ul>
                </div>
            </div>
            <div class="col-sm-1 export pull-right">
                <a class="btn btn-lg" id="btnExport" style="font-size: 13px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>
        <div class="row no-margin">
            <div class="col-sm-12 table-responsive pre-scrollable no-padding" style="max-height: calc(100vh - 195px);color: #ccc;">
                <table id="tblTrackingAll" class="table table-xxs table-bordered table-sticky" style="text-align: center">
                    <thead>
                        <tr>
                            <th style="width: 45px;">No</th>
                            <th style="width: 120px;">Model</th>
                            <th style="width: 150px;">Station</th>
                            <th style="width: 75px;">UPH Target</th>
                            <th style="width: 65px;">SUM</th>
                            <th class="shiftDay">07:30 - 08:30</th>
                            <th class="shiftDay">08:30 - 09:30</th>
                            <th class="shiftDay">09:30 - 10:30</th>
                            <th class="shiftDay">10:30 - 11:30</th>
                            <th class="shiftDay">11:30 - 12:30</th>
                            <th class="shiftDay">12:30 - 13:30</th>
                            <th class="shiftDay">13:30 - 14:30</th>
                            <th class="shiftDay">14:30 - 15:30</th>
                            <th class="shiftDay">15:30 - 16:30</th>
                            <th class="shiftDay">16:30 - 17:30</th>
                            <th class="shiftDay">17:30 - 18:30</th>
                            <th class="shiftDay">18:30 - 19:30</th>
                            <th class="shiftNight">19:30 - 20:30</th>
                            <th class="shiftNight">20:30 - 21:30</th>
                            <th class="shiftNight">21:30 - 22:30</th>
                            <th class="shiftNight">22:30 - 23:30</th>
                            <th class="shiftNight">23:30 - 00:30</th>
                            <th class="shiftNight">00:30 - 01:30</th>
                            <th class="shiftNight">01:30 - 02:30</th>
                            <th class="shiftNight">02:30 - 03:30</th>
                            <th class="shiftNight">03:30 - 04:30</th>
                            <th class="shiftNight">04:30 - 05:30</th>
                            <th class="shiftNight">05:30 - 06:30</th>
                            <th class="shiftNight">06:30 - 07:30</th>
                        </tr>
                        <tr id="realTime">
                            <th colspan="5">Real Time</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftDay">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                            <th class="shiftNight">1</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<iframe id="txtArea1" style="display:none"></iframe>
<!-- Modal -->
<div id="mydiv" style="font-size: 16px; min-width: 540px;">
  <div id="mydivheader">
    <span style="font-weight:bold">Setup Real Time</span>
    <a title="Close" style="float: right; margin-right: 5px; color: #fff;" onclick="closeModal()" >
        <i class="icon icon-cross"></i>
    </a>
  </div>
  <div class="row" style="color: #333333; margin: 0; padding: 5px;">
    <button id="btnAddNew" class="btn btn-lg disableSelect" style="float: left; padding: 5px 10px; color: #ccc; background: #272727; border: 1px solid #fff;" onclick="addNewClick()">
        <i class="fa fa-plus"></i> Add
    </button>
    <!-- <input id="txtSearch" class="form-control" type="search" placeholder="Search by Moldel or Group" style="float: right; padding: 5px 10px; border-radius: 4px; color: #ccc; background: #272727; width: 40%; height: 32px;" /> -->
  </div>
  <div class="table-responsive pre-scrollable" style="color: #333333; max-height: 350px;">
        <table id="tblTarget" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px;">
            <thead>
                <tr><th style="width: 22%; text-align: center; background: #ddd;">STT</th>
                    <th style="width: 30%; text-align: center; background: #ddd;">Work Section</th>
                    <th style="width: 25%; text-align: center; background: #ddd;">Real Time</th>
                    <th style="width: 20%; text-align: center; background: #ddd; z-index:10">Action</th></tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
    <div id="mydivfooter" style="text-align: right;">
    </div>
</div>
<script type="text/javascript" src="/assets/custom/js/nbb/getTimeNow.js"></script>
<script>
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if(dataset == null || dataset == undefined || dataset.path != "${path}" || dataset.path != "${factory}")
    {
        dataset = {
            factory: '${factory}',
            shiftType: "",
            path: "${path}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }

    var dataModelNames = [];
    function init(){
        $(".loader").removeClass("disableSelect");
        loadDataTypeShift(dataset.shiftType);
        $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName+' <span class="caret"></span>');
        $("#titlePage").html(dataset.factory.toUpperCase());
    }
    function loadDataTypeShift(typeShift){
        if(typeShift == "all" || typeShift == ""){
            dataset.shiftType = "";
            $("#a-shift").addClass("selected");
            $("#d-shift").removeClass("selected");
            $("#n-shift").removeClass("selected");
            $(".shiftDay").removeClass("disableSelect");
            $(".shiftNight").removeClass("disableSelect");
        }
        else if(typeShift == "night-shift" || typeShift == "NIGHT"){
            dataset.shiftType = "NIGHT";
            $("#n-shift").addClass("selected");
            $("#d-shift").removeClass("selected");
            $("#a-shift").removeClass("selected");
            $(".shiftNight").removeClass("disableSelect");
            $(".shiftDay").addClass("disableSelect");
        }
        else{
            dataset.shiftType = "DAY";
            $("#d-shift").addClass("selected");
            $("#n-shift").removeClass("selected");
            $("#a-shift").removeClass("selected");
            $(".shiftDay").removeClass("disableSelect");
            $(".shiftNight").addClass("disableSelect");
        }
        $(".loader").removeClass("disableSelect");
        loadRealTime();
        loadDataByModel();
        closeModal();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        var getD = moment(dataset.workDate).format("MM/DD");
        $("#txtDateTime").html(getD + " ("+dataset.shiftType+")");
    }

    function loadDataByModel(){
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/group/uph-all-model",
            data: {
                factory: dataset.factory,
                shiftType: dataset.shiftType,
                workDate: dataset.workDate,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#tblTrackingAll tbody").html("");
                var dataTime = [];
                if(dataset.shiftType == ""){
                    dataTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30"];
                }else if(dataset.shiftType == "DAY"){
                    dataTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30"];
                }else if(dataset.shiftType == "NIGHT"){
                    dataTime = ["19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30"];
                }
                
                var html = "";
                var stt = 1;
                var no = 1;
                var dataStations = ["DL","DL1","DL_2","PT","PT1","PT_2","WT","UP","UK","FT","FT1","FT_2","RC","RC1","RC_2","OBA","W_OBA"];
                for(i in data){
                    var dataStage = Object.keys(data[i]);
                    if(dataStage.length == 0){
                        break;
                    }
                    else{
                        // Fix Station
                        var dss = [];
                        for(j in dataStations){
                            for(k in dataStage){
                                if(dataStage[k] == dataStations[j] && dss.indexOf(dataStage[k]) == -1){
                                    dss.push(dataStage[k]);
                                }
                            }
                        }
                        for(j in dataStage){
                            if(dataStations.indexOf(dataStage[j]) == -1 && dss.indexOf(dataStage[j]) == -1){
                                dss.push(dataStage[j]);
                            }
                        }
                        html += '<tr class="row'+dss[0]+'_'+stt+'"><td rowspan="'+dss.length+'"><b>'+no+'</b></td><td rowspan="'+dss.length+'"><b>'+i+'</b></td><td><b>'+dss[0]+'</b></td></tr>';
                        for(j in dss){
                            if(j != 0)
                                html += '<tr class="row'+dss[j]+'_'+stt+'"><td><b>'+dss[j]+'</b></td></tr>';
                            stt++;
                        }
                        no++;
                        console.log("dss: "+no,dss);
                    }
                }
                $("#tblTrackingAll tbody").html(html);

                stt = 1;
                for(i in data){
                    var dataStage = Object.keys(data[i]);
                    if(dataStage.length == 0){
                        break;
                    }
                    else{
                        // Fix Station
                        var dss = [];
                        for(j in dataStations){
                            for(k in dataStage){
                                if(dataStage[k] == dataStations[j] && dss.indexOf(dataStage[k]) == -1){
                                    dss.push(dataStage[k]);
                                }
                            }
                        }
                        for(j in dataStage){
                            if(dataStations.indexOf(dataStage[j]) == -1 && dss.indexOf(dataStage[j]) == -1){
                                dss.push(dataStage[j]);
                            }
                        }

                        for(j in dss){
                            $(".row"+dss[j]+'_'+stt).append("<td class='uph-target'>"+data[i][dss[j]]["uph-target"]+"</td>");
                            $(".row"+dss[j]+'_'+stt).append("<td class='sumAcc'><b>"+data[i][dss[j]]["accumulate"]+"</b></td>");
                            for(k in dataTime){
                                if(data[i][dss[j]][dataTime[k]] == null || data[i][dss[j]][dataTime[k]] == undefined){
                                    $(".row"+dss[j]+'_'+stt).append("<td></td>");
                                }
                                else{
                                    if(data[i][dss[j]][dataTime[k]].rate == null || data[i][dss[j]][dataTime[k]].rate == 0){
                                        data[i][dss[j]][dataTime[k]].rate = "0";
                                    }
                                    $(".row"+dss[j]+'_'+stt).append("<td><b>"+data[i][dss[j]][dataTime[k]].qty+"</b> ("+data[i][dss[j]][dataTime[k]].rate+"%)</td>");
                                }
                            }
                            stt++;
                        }
                    }
                }

                $(".loader").addClass("disableSelect");
                $("#tblTrackingAll").append("<div id='phantomjsMark'></div>");
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
            complete: function() {
               dataset.timeout = setTimeout(loadDataByModel, 150000, dataset.factory, dataset.shiftType, dataset.workDate, dataset.modelName);
            }
        });
    }

    function loadRealTime(){
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/uph/realtime",
            data: {
                factory: dataset.factory,
                shiftType: dataset.shiftType,
                workDate: dataset.workDate,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#realTime").html("");
                if(!$.isEmptyObject(data)){
                    var html='<td colspan="5">Real Time</td>';
                    for(var i=0; i<24; i++){
                        if(data[i].workSection > i){
                            data[i].workSection = i;
                            data[i].realTime = data[i].realTime;
                        }
                    }
                    if(dataset.shiftType == "")
                    {
                        for(i in data){
                            if(data[i].workSection > 7 && data[i].workSection < 20){
                                html += '<td id="workSection'+data[i].workSection+'" onclick="showModal('+data[i].workSection+')" title="Click to Setup Real Time">' + data[i].realTime + '</td>';
                            }   
                        }
                        for(i in data){
                            if((data[i].workSection >= 20 && data[i].workSection < 24)){
                                html += '<td id="workSection'+data[i].workSection+'" onclick="showModal('+data[i].workSection+')" title="Click to Setup Real Time">' + data[i].realTime + '</td>';
                            }
                        }
                        for(i in data){
                            if((data[i].workSection >= 0 && data[i].workSection <= 7)){
                                html += '<td id="workSection'+data[i].workSection+'" onclick="showModal('+data[i].workSection+')" title="Click to Setup Real Time">' + data[i].realTime + '</td>';
                            }
                        }
                    }
                    else if(dataset.shiftType == "DAY"){
                        for(i in data){
                            if(data[i].workSection > 7 && data[i].workSection < 20){
                                html += '<td id="workSection'+data[i].workSection+'" onclick="showModal('+data[i].workSection+')" title="Click to Setup Real Time">' + data[i].realTime + '</td>';
                            }
                        }
                    }
                    else if(dataset.shiftType == "NIGHT"){
                        for(i in data){
                            if((data[i].workSection >= 20 && data[i].workSection < 24)){
                                html += '<td id="workSection'+data[i].workSection+'" onclick="showModal('+data[i].workSection+')" title="Click to Setup Real Time">' + data[i].realTime + '</td>';
                            }
                        }
                        for(i in data){
                            if((data[i].workSection >= 0 && data[i].workSection <= 7)){
                                html += '<td id="workSection'+data[i].workSection+'" onclick="showModal('+data[i].workSection+')" title="Click to Setup Real Time">' + data[i].realTime + '</td>';
                            }
                        }
                    }
                    $("#realTime").html(html);
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
            complete: function() {
               dataset.timeout = setTimeout(loadRealTime, 150000, dataset);
            }
        });
    }

function showModal(workSection){
        $("#mydiv").addClass("show");
        $.ajax({
            type: "GET",
            url: "/api/test/uph/realtime",
            data: {
                factory: dataset.factory
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#tblTarget tbody").html("");
                var stt = 0;
                var html = '';

                if(data.length > 0){
                    for(i in data){
                        if(data[i].workSection == workSection){
                            stt++;
                            html = '<tr id="row'+data[i].id+'">'
                                +  '<td>'+stt+'</td>'
                                +  '<td><input id="txtEditWorkSection'+data[i].id+'" class="form-control hidden" name="editWorkSection" type="number" value="'+data[i].workSection+'" /><span id="txtWorkSection'+data[i].id+'">'+data[i].workSection+'</span></td>'
                                +  '<td><input id="txtEditRealTime'+data[i].id+'" class="form-control hidden" name="editRealTime" type="text" value="'+data[i].realTime+'" /><span id="txtRealTime'+data[i].id+'">'+data[i].realTime+'</span></td>'
                                +  '<td>'
                                +           '<button class="btn btn-warning" id="editRow'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+data[i].id+')">'
                                +               '<i class="icon icon-pencil"></i>'
                                +           '</button>'
                                +          '<button class="btn btn-danger disableSelect" id="deleteRow'+data[i].id+'" title="Delete" style="padding: 4px 10px; margin-left: 5px;" onclick="deleteModel('+data[i].id+')">'
                                +               '<i class="icon icon-trash"></i>'
                                +           '</button>'
                                +           '<button class="btn btn-success hidden" id="confirmRow'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel('+data[i].id+')">'
                                +               '<i class="icon icon-checkmark"></i>'
                                +           '</button><button class="btn btn-danger hidden" id="cancelRow'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+data[i].id+')">'
                                +               '<i class="icon icon-spinner11"></i>'
                                +           '</button>'
                                +   '</td>'
                                + '</tr>';
                            $("#tblTarget tbody").append(html);
                        }
                    }
                    dataset.noTarget = "false";
                }
                else{
                    $("#tblTarget tbody").html("<tr><td colspan='4'>NO DATA!!</td></tr>");
                    dataset.noTarget = "true";
                }

            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }
    function closeModal(){
        $("#mydiv").removeClass("show");
    }
    function addNewClick(){
        if(dataset.noTarget == "true"){
            $("#tblTarget tbody").html("");
        }
        var html = '<tr id="newRow"><td></td><td id="newWorkSection"><input id="txtAddWorkSection" class="form-control" type="number"/></td>'
                 + '<td id="newRealTime"><input id="txtAddRealTime" class="form-control" type="number"/></td>'
                 + '<td><button class="btn btn-success" title="Add" style="padding: 4px 10px;" onclick="addNew()"><i class="icon icon-checkmark"></i></button>'
                 + '<button class="btn btn-danger" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelAddNew()"><i class="icon icon-cross"></i></button></td></tr>';
        $("#tblTarget tbody").append(html);
        $("#txtAddRealTime").focus();
        $("#btnAddNew").attr("disabled","disabled");
        $(".btn-warning").attr("disabled","disabled");
    }
    function addNew(){
        var newWorkSection = $("#txtAddWorkSection").val();
        var newRealTime = $("#txtAddRealTime").val();
        var data = {
            factory: dataset.factory,
            workSection: newWorkSection,
            realTime: newRealTime
        }

        $.ajax({
            type: 'POST',
            url: '/api/test/uph/realtime',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                showModal();
                loadDataByModel();
                loadRealTime();
                $("#btnAddNew").removeAttr("disabled");
                $(".btn-warning").removeAttr("disabled");
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }
    function cancelAddNew(){
        $("#newRow").remove();
        $("#btnAddNew").removeAttr("disabled");
        $(".btn-warning").removeAttr("disabled");
    }

    function edit(stt){
        $("#deleteRow"+stt).addClass("hidden");
        $("#editRow"+stt).addClass("hidden");
        $("#confirmRow"+stt).removeClass("hidden");
        $("#cancelRow"+stt).removeClass("hidden");

        $("#txtRealTime"+stt).addClass("hidden");
        // $("#txtWorkSection"+stt).addClass("hidden");
        $("#txtEditRealTime"+stt).removeClass("hidden");
        // $("#txtEditWorkSection"+stt).removeClass("hidden");
        $("#btnAddNew").attr("disabled", "disabled");
        $(".btn-warning").attr("disabled","disabled");
    }
    function cancelEdit(stt){
        $("#deleteRow"+stt).removeClass("hidden");
        $("#editRow"+stt).removeClass("hidden");
        $("#confirmRow"+stt).addClass("hidden");
        $("#cancelRow"+stt).addClass("hidden");

        $("#txtRealTime"+stt).removeClass("hidden");
        // $("#txtWorkSection"+stt).removeClass("hidden");
        $("#txtEditRealTime"+stt).addClass("hidden");
        // $("#txtEditWorkSection"+stt).addClass("hidden");
        $("#btnAddNew").removeAttr("disabled");
        $(".btn-warning").removeAttr("disabled");
    }

    function confirmModel(stt){
        var confirm = window.confirm("Are you sure CHANGE?");
        if(confirm == true){
            // var editWorkSection = $("#txtEditWorkSection"+stt).val();
            var editRealTime = Number($("#txtEditRealTime"+stt).val());
            if(editRealTime > 1 || editRealTime <0){
                alert("0 < Real Time < 1 !!!");
            }
            else{
                var data = {
                    factory: dataset.factory,
                    // workSection: editWorkSection,
                    realTime: editRealTime.toFixed(2)
                }

                $.ajax({
                    type: 'PUT',
                    url: '/api/test/uph/realtime/'+stt,
                    data: JSON.stringify(data),
                    contentType: "application/json; charset=utf-8",
                    success: function(data){
                        closeModal();
                        loadDataByModel();
                        loadRealTime();
                        $("#btnAddNew").removeAttr("disabled");
                        $(".btn-warning").removeAttr("disabled");
                    },
                    failure: function(errMsg) {
                        console.log(errMsg);
                    },
                });
            }
        }
    }

    function deleteModel(stt){
        var del = window.confirm("Do you want to DELETE?");
        if(del == true){
            $.ajax({
                type: 'DELETE',
                url: '/api/test/uph/realtime/'+stt,
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    showModal();
                    loadDataByModel();
                    loadRealTime();
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
    }

    var tableToExcel = (function() {
      var uri = 'data:application/vnd.ms-excel;base64,'
        , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
        , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
        , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
      return function(table, name) {
        if (!table.nodeType) table = document.getElementById(table)
        var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
        var blob = new Blob([format(template, ctx)]);
      var blobURL = window.URL.createObjectURL(blob);
        return blobURL;
      }
    })();
    $("#btnExport").click(function () {
        var tab_text = "<table border='1px'><tr bgcolor='#F79646'>";
        var textRange; var j = 0;
        tab = document.getElementById('tblTrackingAll'); // id of table

        for (j = 0 ; j < tab.rows.length ; j++) {
            tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
            //tab_text=tab_text+"";
        }

        tab_text = tab_text + "</table>";
        tab_text = tab_text.replace(/<a[^>]*>|<\/A>/g, "");//remove if u want links in your table
        tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
        tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

        var ua = window.navigator.userAgent;
        var msie = ua.indexOf("MSIE ");

        var sa;
        if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
        {
            txtArea1.document.open("txt/html", "replace");
            txtArea1.document.write(tab_text);
            txtArea1.document.close();
            txtArea1.focus();
            sa = txtArea1.document.execCommand("SaveAs", true, "${factory} - UPH Tracking By All Model.xls");
            return (sa);
        }
        else {
            //other browser not tested on IE 11
            //sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            var a = document.createElement('a');
            a.href = 'data:application/vnd.ms-excel,' + encodeURIComponent(tab_text);
            a.download = '${factory} - UPH Tracking By All Model.xls';
            a.click();
            // e.preventDefault();
        }
    });

    $(document).ready(function() {
        getTimeNow();
        $('input[name=timeSpan]').on('change', function() {
            dataset.workDate = this.value;
            $(".loader").removeClass("disableSelect");
            loadDataByModel();
            window.localStorage.setItem('dataset', JSON.stringify(dataset));
            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " ("+dataset.shiftType+")");
        });
    });
</script>
<script type="text/javascript" src="/assets/custom/js/nbb/dragModal.js"></script>