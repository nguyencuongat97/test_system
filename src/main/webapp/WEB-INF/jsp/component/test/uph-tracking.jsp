<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<style>
.btn.dropdown-toggle{
    background: #333333;
    color: #ffffff;
    font-weight: 600;
    padding: 9px;
    border-bottom: 0px !important;
    border-radius: 5px;
    min-width: 110px;
}
.dropdown-menu{
    min-width: 160px !important;
    /* min-height: 160px !important; */
    /* max-height: 200px !important; */
}
.bootstrap-select.btn-group .btn-default .caret {
    right: 5px !important;
}

.btn-group.bootstrap-select.open .btn.dropdown-toggle{
    background: #333333;
    color: #ffffff;
    padding: 9px;
    /* border-bottom: 0px !important; */
}
</style>

<div class="loader"></div>
<div class="panel panel-re panel-flat row"  style="background-color: #272727; text-align: center; color: #ccc">
    <div class="col-lg-12">
         <div class="panel panel-overview" id="header">
            <b><span id="titlePage">B06</span><span> - UPH Tracking Dashboard </span><span id="txtDateTime"></span></b>
        </div>
        <div class="row" style="margin: unset; padding: unset">
            
            <div class="drlMenu" style="margin: unset; width: 10%">
                <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control daterange-single" side="right" name="timeSpan" style="border-bottom: 0px !important; color: #fff;">
                </div>
            </div>

            <div class="btn-group" style="margin: 0 15px; float:left">
                <button id="a-shift" class="btn btn-default select-shift selected" onclick="loadDataTypeShift('all')">All</button>
                <button id="d-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('day-shift')">Day</button>
                <button id="n-shift" class="btn btn-default select-shift" onclick="loadDataTypeShift('night-shift')">Night</button>
            </div>

            <div class='drlMenu selectModel disableSelect'>
                <div class="dropdown dropdown-select-model">
                    <button  style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">
                        Select Model
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel">

                    </ul>
                </div>
            </div>

            <!--TEST-->
            <div class="drlMenu">
                <!-- <select id="myListModel" class="selectpicker" data-live-search="true" onchange="validateSelectModel(this)">
                </select> -->
                <select class="form-control bootstrap-select" name="modelName" data-live-search="true" >
                </select>
            </div>
            <!--/TEST-->



            <div class="col-sm-1 export pull-right">
                <a class="btn btn-lg" id="btnExport" style="font-size: 13px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);color: #ccc; padding: unset;">
                <table id="tblDetail" class="table table-xxs table-bordered table-sticky" style="text-align: center">

                </table>
            </div>
        </div>
    </div>
</div>
<iframe id="txtArea1" style="display:none"></iframe>

<!-- Modal -->
<div id="mydiv" style="font-size: 18px; min-width: 740px;">
  <div id="mydivheader">
    <span style="font-weight:bold">Set UPH Target</span>
    <a title="Close" style="float: right; margin-right: 5px; color: #fff;" onclick="closeModal()" >
        <i class="icon icon-cross"></i>
    </a>
  </div>
  <div class="row" style="color: #333333; margin: 0; padding: 5px;">
    <button id="btnAddNew" class="btn btn-lg" style="float: left; padding: 5px 10px; color: #ccc; background: #272727; border: 1px solid #fff;" onclick="addNewClick()">
        <i class="fa fa-plus"></i> Add
    </button>
    <input id="txtSearch" class="form-control" type="search" placeholder="Search by Moldel or Group" style="float: right; padding: 5px 10px; border-radius: 4px; color: #ccc; background: #272727; width: 40%; height: 32px;" />
  </div>
  <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 200px);">
        <table id="tblTarget" class="table table-bordered table-xxs table-sticky" style="margin-bottom: 10px;">
            <thead>
                <tr><th style="width: 10%; text-align: center">STT</th>
                    <th style="width: 30%; text-align: center">Model Name</th>
                    <th style="width: 20%; text-align: center">Group Name</th>
                    <th style="width: 20%; text-align: center">UPH</th>
                    <th style="width: 20%; text-align: center">Action</th></tr>
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
    if(dataset == null || dataset == undefined || dataset.path != "uph-tracking?factory=B06")
    {
        dataset = {
            factory: '${factory}',
            shiftType: "",
            search: "",
            path: "uph-tracking?factory=B06"
        }
        $.ajax({
            type: "GET",
            url: "/api/test/model",
            data: {
                factory: dataset.factory,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                dataset.modelName = data[0].modelName;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }
    else{
        dataset.factory = '${factory}';
        dataset.search = "";
    } 
   
    var dataModelNames = [];
    var dataGroupNames = [];

    function init(){
        $(".loader").removeClass("disableSelect");
        $("#titlePage").html(dataset.factory.toUpperCase());
        loadSelectModel();
        loadDataTypeShift(dataset.shiftType);
        $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName+' <span class="caret"></span>');
    }
    function loadSelectModel(){
        $.ajax({
            type: "GET",
            url: "/api/test/model",
            data: {
                factory: dataset.factory,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var selector = $("select[name='modelName']");
                selector.children('option').remove();

                var options = "";
                for(i in data){
                    dataModelNames.push(data[i].modelName);
                    if(data[i].modelName == dataset.modelName){
                        options+='<option value=' + data[i].modelName + ' selected>' + data[i].modelName + '</option>';
                        $('li[data-original-index="'+i+'"]').addClass("active");
                    }
                    else
                        options+='<option value=' + data[i].modelName + '>' + data[i].modelName + '</option>';
                }
                selector.append(options);
                selector.selectpicker('refresh');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function validateSelectModel(obj){
        var options = obj.children;
        var list=[];
        for(var i =0;i<options.length;i++){
            if(options[i].selected){
                list.push(options[i].value);
                $('li[data-original-index="'+i+'"]').addClass("active");
            }
        }
        dataset.modelName = list[0];
        $(".loader").removeClass("disableSelect");
        loadDataByModel();
        closeModal();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
        console.log(list);
    }

    $('select[name=modelName]').on('change', function() {
        dataset.modelName = this.value;
        $(".loader").removeClass("disableSelect");
        loadDataByModel();
        closeModal();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });
    
    $('.dropdown-select-model').on( 'click', '.select-model li a', function() { 
        dataset.modelName = $(this).html();

        //Adds active class to selected item
        $(this).parents('.select-model').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");
        loadDataByModel();
        closeModal();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    function loadDataTypeShift(typeShift){
        if(typeShift == "all" || typeShift == ""){
            dataset.shiftType = "";
            $("#a-shift").addClass("selected");
            $("#d-shift").removeClass("selected");
            $("#n-shift").removeClass("selected");
        }
        else if(typeShift == "night-shift" || typeShift == "NIGHT"){
            dataset.shiftType = "NIGHT";
            $("#n-shift").addClass("selected");
            $("#d-shift").removeClass("selected");
            $("#a-shift").removeClass("selected");
        }
        else{
            dataset.shiftType = "DAY";
            $("#d-shift").addClass("selected");
            $("#n-shift").removeClass("selected");
            $("#a-shift").removeClass("selected");
        }
        $(".loader").removeClass("disableSelect");
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
            url: "/api/test/group/uph",
            data: {
                factory: dataset.factory,
                shiftType: dataset.shiftType,
                workDate: dataset.workDate,
                modelName: dataset.modelName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#tblDetail").html("");
                var header = Object.keys(data);
                dataGroupNames = header;
                var thead = '<thead><tr><th style="width:8%;">Time</th>';
                for(i in header){
                    thead += '<th>'+header[i]+'</th>';
                }
                thead += '</tr></thead><tbody>';
                if(dataset.shiftType == "DAY")
                {
                    var dataTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "accumulate", "uph-average", "uph-target"];

                    thead += '<tr class="target" onclick="showModal()" title="Click to Set UPH Target"><td>UPH Target</td></tr>';
                    thead += '<tr class="accumulate"><td>Accumulated</td></tr>';
                    thead += '<tr class="time0"><td>07:30 - 08:30</td></tr>';
                    thead += '<tr class="time1"><td>08:30 - 09:30</td></tr>';
                    thead += '<tr class="time2"><td>09:30 - 10:30</td></tr>';
                    thead += '<tr class="time3"><td>10:30 - 11:30</td></tr>';
                    thead += '<tr class="time4"><td>11:30 - 12:30</td></tr>';
                    thead += '<tr class="time5"><td>12:30 - 13:30</td></tr>';
                    thead += '<tr class="time6"><td>13:30 - 14:30</td></tr>';
                    thead += '<tr class="time7"><td>14:30 - 15:30</td></tr>';
                    thead += '<tr class="time8"><td>15:30 - 16:30</td></tr>';
                    thead += '<tr class="time9"><td>16:30 - 17:30</td></tr>';
                    thead += '<tr class="time10"><td>17:30 - 18:30</td></tr>';
                    thead += '<tr class="time11"><td>18:30 - 19:30</td></tr>';
                    thead += '<tr class="uph-average"><td>UPH/Average</td></tr></tbody>';
                    $("#tblDetail").append(thead);

                    for(i in data){
                        for(j in dataTime){
                            if(data[i][dataTime[j]] == null || data[i][dataTime[j]] == undefined){
                                $(".time"+j).append("<td class='noData'>N/A</td>");
                            }
                            else{
                                $(".time"+j).append("<td><b>"+data[i][dataTime[j]]+"</b></td>");
                            }

                        }
                        var percent = data[i]["uph-target"]==0 ? 0 : data[i]["uph-average"] * 100.0 / data[i]["uph-target"];
                        if(data[i]["uph-target"] == 0) data[i]["uph-target"] = "N/A";
                        $(".target").append("<td class='noData'>"+data[i]["uph-target"]+ (data[i]["uph-target"] == "N/A"? "" : "(" + percent.toFixed(2) + "%)") + "</td>");
                        $(".accumulate").append("<td><b>"+data[i]["accumulate"] + "</b></td>");
                        $(".uph-average").append("<td><b>"+data[i]["uph-average"]+"</b></td>");
                    }

                }
                else if(dataset.shiftType == ""){
                    var dataTime = ["07:30 - 08:30", "08:30 - 09:30", "09:30 - 10:30", "10:30 - 11:30", "11:30 - 12:30", "12:30 - 13:30", "13:30 - 14:30", "14:30 - 15:30", "15:30 - 16:30", "16:30 - 17:30", "17:30 - 18:30", "18:30 - 19:30", "19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30", "accumulate", "uph-average", "uph-target"];

                    thead += '<tr class="target" onclick="showModal()" title="Click to Set UPH Target"><td>UPH Target</td></tr>';
                    thead += '<tr class="accumulate"><td>Accumulated</td></tr>';
                    thead += '<tr class="time0"><td>07:30 - 08:30</td></tr>';
                    thead += '<tr class="time1"><td>08:30 - 09:30</td></tr>';
                    thead += '<tr class="time2"><td>09:30 - 10:30</td></tr>';
                    thead += '<tr class="time3"><td>10:30 - 11:30</td></tr>';
                    thead += '<tr class="time4"><td>11:30 - 12:30</td></tr>';
                    thead += '<tr class="time5"><td>12:30 - 13:30</td></tr>';
                    thead += '<tr class="time6"><td>13:30 - 14:30</td></tr>';
                    thead += '<tr class="time7"><td>14:30 - 15:30</td></tr>';
                    thead += '<tr class="time8"><td>15:30 - 16:30</td></tr>';
                    thead += '<tr class="time9"><td>16:30 - 17:30</td></tr>';
                    thead += '<tr class="time10"><td>17:30 - 18:30</td></tr>';
                    thead += '<tr class="time11"><td>18:30 - 19:30</td></tr>';
                    thead += '<tr class="time12"><td>19:30 - 20:30</td></tr>';
                    thead += '<tr class="time13"><td>20:30 - 21:30</td></tr>';
                    thead += '<tr class="time14"><td>21:30 - 22:30</td></tr>';
                    thead += '<tr class="time15"><td>22:30 - 23:30</td></tr>';
                    thead += '<tr class="time16"><td>23:30 - 00:30</td></tr>';
                    thead += '<tr class="time17"><td>00:30 - 01:30</td></tr>';
                    thead += '<tr class="time18"><td>01:30 - 02:30</td></tr>';
                    thead += '<tr class="time19"><td>02:30 - 03:30</td></tr>';
                    thead += '<tr class="time20"><td>03:30 - 04:30</td></tr>';
                    thead += '<tr class="time21"><td>04:30 - 05:30</td></tr>';
                    thead += '<tr class="time22"><td>05:30 - 06:30</td></tr>';
                    thead += '<tr class="time23"><td>06:30 - 07:30</td></tr>';
                    thead += '<tr class="uph-average"><td>UPH/Average</td></tr></tbody>';
                    $("#tblDetail").append(thead);


                    for(i in data){
                        for(j in dataTime){
                            if(data[i][dataTime[j]] == null || data[i][dataTime[j]] == undefined){
                                $(".time"+j).append("<td class='noData'>N/A</td>");
                            }
                            else{
                                $(".time"+j).append("<td><b>"+data[i][dataTime[j]]+"</b></td>");
                            }

                        }
                        var percent = data[i]["uph-target"]==0 ? 0 : data[i]["uph-average"] * 100.0 / data[i]["uph-target"];
                        if(data[i]["uph-target"] == 0) data[i]["uph-target"] = "N/A";
                        $(".target").append("<td class='noData'>"+data[i]["uph-target"]+ (data[i]["uph-target"] == "N/A"? "" : "(" + percent.toFixed(2) + "%)") + "</td>");
                        $(".accumulate").append("<td><b>"+data[i]["accumulate"] + "</b></td>");
                        $(".uph-average").append("<td><b>"+data[i]["uph-average"]+"</b></td>");
                    }

                }
                else {
                    var dataTime = ["19:30 - 20:30", "20:30 - 21:30", "21:30 - 22:30", "22:30 - 23:30", "23:30 - 00:30", "00:30 - 01:30", "01:30 - 02:30", "02:30 - 03:30", "03:30 - 04:30", "04:30 - 05:30", "05:30 - 06:30", "06:30 - 07:30", "accumulate", "uph-average", "uph-target"];

                    thead += '<tr class="target" onclick="showModal()" title="Click to Set UPH Target"><td>UPH Target</td></tr>';
                    thead += '<tr class="accumulate"><td>Accumulated</td></tr>';                    
                    thead += '<tr class="time0"><td>19:30 - 20:30</td></tr>';
                    thead += '<tr class="time1"><td>20:30 - 21:30</td></tr>';
                    thead += '<tr class="time2"><td>21:30 - 22:30</td></tr>';
                    thead += '<tr class="time3"><td>22:30 - 23:30</td></tr>';
                    thead += '<tr class="time4"><td>23:30 - 00:30</td></tr>';
                    thead += '<tr class="time5"><td>00:30 - 01:30</td></tr>';
                    thead += '<tr class="time6"><td>01:30 - 02:30</td></tr>';
                    thead += '<tr class="time7"><td>02:30 - 03:30</td></tr>';
                    thead += '<tr class="time8"><td>03:30 - 04:30</td></tr>';
                    thead += '<tr class="time9"><td>04:30 - 05:30</td></tr>';
                    thead += '<tr class="time10"><td>05:30 - 06:30</td></tr>';
                    thead += '<tr class="time11"><td>06:30 - 07:30</td></tr>';
                    thead += '<tr class="uph-average"><td>UPH/Average</td></tr></tbody>';
                    $("#tblDetail").append(thead);


                    for(i in data){
                        for(j in dataTime){
                            if(data[i][dataTime[j]] == null || data[i][dataTime[j]] == undefined){
                                $(".time"+j).append("<td class='noData'>N/A</td>");
                            }
                            else{
                                $(".time"+j).append("<td><b>"+data[i][dataTime[j]]+"</b></td>");
                            }

                        }
                        var percent = data[i]["uph-target"]==0 ? 0 : data[i]["uph-average"] * 100.0 / data[i]["uph-target"];
                        if(data[i]["uph-target"] == 0) data[i]["uph-target"] = "N/A";
                        $(".target").append("<td class='noData'>"+data[i]["uph-target"]+ (data[i]["uph-target"] == "N/A"? "" : "(" + percent.toFixed(2) + "%)") + "</td>");
                        $(".accumulate").append("<td><b>"+data[i]["accumulate"] + "</b></td>");
                        $(".uph-average").append("<td><b>"+data[i]["uph-average"]+"</b></td>");
                    }

                }
                $(".loader").addClass("disableSelect");
                $("#tblDetail").append("<div id='phantomjsMark'></div>");
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
            complete: function() {
               dataset.timeout = setTimeout(loadDataByModel, 150000, dataset.factory, dataset.shiftType, dataset.workDate, dataset.modelName);
            }
        });
    }

    function showModal(){
        var shift = dataset.shiftType;
        if(shift == ""){
            shift = "DAY";
        }
        $("#mydiv").addClass("show");
        $.ajax({
            type: "GET",
            url: "/api/test/uph",
            data: {
                modelName: dataset.modelName,
                shiftType: shift,
                workDate: dataset.workDate,
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $("#tblTarget tbody").html("");
                var stt = 0;
                var html = '';
                var listModelNames = '<select class="form-control selectModels">';
                for(i in dataModelNames){
                    listModelNames += '<option value="'+dataModelNames[i]+'">'+dataModelNames[i]+'</option>';
                }
                listModelNames += '</select>';

                var listGroupNames = '<select class="form-control selectGroups"';
                for(i in dataGroupNames){
                    listGroupNames += '<option value="'+dataGroupNames[i]+'">'+dataGroupNames[i]+'</option>';
                }
                listGroupNames += '</select>';

                if(data.length > 0){
                    for(i in data){
                        if((data[i].modelName).indexOf(dataset.search) != -1 || (data[i].groupName).indexOf(dataset.search) != -1){
                            stt++;
                            html = '<tr id="row'+data[i].id+'">'
                                +  '<td>'+stt+'</td>'
                                +  '<td><div id="txtEditModel'+data[i].id+'">'+data[i].modelName+'</div></td>'
                                +  '<td><div id="txtEditGroup'+data[i].id+'">'+data[i].groupName+'</div></td>'
                                +  '<td><input id="txtEditUPH'+data[i].id+'" class="form-control hidden" name="uph" type="text" value="'+data[i].uph+'" /><span id="uph'+data[i].id+'">'+data[i].uph+'</span></td>'
                                +  '<td>'
                                +           '<button class="btn btn-warning" id="editRow'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+data[i].id+')">'
                                +               '<i class="icon icon-pencil"></i>'
                                +           '</button>'
                                +          '<button class="btn btn-danger" id="deleteRow'+data[i].id+'" title="Delete" style="padding: 4px 10px; margin-left: 5px;" onclick="deleteModel('+data[i].id+')">'
                                +               '<i class="icon icon-trash"></i>'
                                +           '</button>'
                                +           '<button class="btn btn-success hidden" id="confirmRow'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel('+data[i].id+')">'
                                +               '<i class="icon icon-checkmark"></i>'
                                +           '</button><button class="btn btn-danger hidden" id="cancelRow'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+data[i].id+')">'
                                +               '<i class="icon icon-spinner11"></i>'
                                +           '</button>'
                                +   '</td>'
                                + '</tr>';
                        }
                        else html = '';
                        $("#tblTarget tbody").append(html);  
                    }
                    dataset.noTarget = "false";
                }
                else{
                    $("#tblTarget tbody").html("<tr><td colspan='5'>NO TARGET!!</td></tr>");
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

    //Filter DragModal
    $("#txtSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblTarget tbody tr").filter(function() {
        $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
    function addNewClick(){
        if(dataset.noTarget == "true"){
            $("#tblTarget tbody").html("");
        }
        var listModelNames = '<select class="form-control selectModelsAdd">';
        for(i in dataModelNames){
            listModelNames += '<option value="'+dataModelNames[i]+'" ' + (dataset.modelName == dataModelNames[i] ? 'selected' : '') +'>'+dataModelNames[i]+'</option>';
        }
        listModelNames += '</select>';

        var listGroupNames = '<select class="form-control selectGroupsAdd">';
        for(i in dataGroupNames){
            listGroupNames += '<option value="'+dataGroupNames[i]+'">'+dataGroupNames[i]+'</option>';
        }
        listGroupNames += '</select>';

        var html = '<tr id="newRow"><td></td><td id="newModel">'+listModelNames+'</td>'
                 + '<td id="newGroup">'+listGroupNames+'</td>'
                 + '<td id="newuph"><input id="txtAddUPH" class="form-control" type="text"/></td>'
                 + '<td><button class="btn btn-success" title="Add" style="padding: 4px 10px;" onclick="addNew()"><i class="icon icon-checkmark"></i></button>'
                 + '<button class="btn btn-danger" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelAddNew()"><i class="icon icon-cross"></i></button></td></tr>';
        $("#tblTarget tbody").append(html);
        $("#txtAddUPH").focus();
        $("#btnAddNew").attr("disabled","disabled");
        $(".btn-warning").attr("disabled","disabled");
    }
    function addNew(){
        var newModel = $('select.selectModelsAdd option:selected').val();
        var newGroup = $('select.selectGroupsAdd option:selected').val();
        var newUPH = Number($("#txtAddUPH").val());
        var shift = dataset.shiftType;
        if(shift == ""){
            shift = "DAY";
        }
        var data = {
            factory: dataset.factory,
            modelName: newModel,
            groupName: newGroup,
            workDate: dataset.workDate,
            shiftType: shift,
            workTime: 12,
            uph: newUPH,
        }

        $.ajax({
            type: 'POST',
            url: '/api/test/uph',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                showModal();
                loadDataByModel();
                $("#btnAddNew").removeAttr("disabled");
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

        $("#uph"+stt).addClass("hidden");
        $("#txtEditUPH"+stt).removeClass("hidden");
        $("#btnAddNew").attr("disabled", "disabled");

        $(".btn-warning").attr("disabled","disabled");
        $("#txtSearch").attr("disabled","disabled");
    }
    function cancelEdit(stt){
        $("#deleteRow"+stt).removeClass("hidden");
        $("#editRow"+stt).removeClass("hidden");
        $("#confirmRow"+stt).addClass("hidden");
        $("#cancelRow"+stt).addClass("hidden");

        $("#uph"+stt).removeClass("hidden");
        $("#txtEditUPH"+stt).addClass("hidden");
        $("#btnAddNew").removeAttr("disabled");

        $(".btn-warning").removeAttr("disabled");
        $("#txtSearch").removeAttr("disabled");
    }

    function confirmModel(stt){
        var confirm = window.confirm("Are you sure CHANGE?");
        if(confirm == true){
            var shift = dataset.shiftType;
            if(shift == ""){
                shift = "DAY";
            }
            var editModel = $('select.selectModels option:selected').val();
            var editGroup = $('select.selectGroups option:selected').val();
            var editUPH = Number($("#txtEditUPH"+stt).val());
            var data = {
                factory: dataset.factory,
                modelName: editModel,
                groupName: editGroup,
                workDate: dataset.workDate + " 00:00:00",
                shiftType: shift,
                workTime: 12,
                uph: editUPH,
            }

            $.ajax({
                type: 'PUT',
                url: '/api/test/uph/'+stt,
                data: JSON.stringify(data),
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    showModal();
                    loadDataByModel();
                    $("#btnAddNew").removeAttr("disabled");
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
            });
        }
    }

    function deleteModel(stt){
        var del = window.confirm("Do you want to DELETE?");
        if(del == true){
            $.ajax({
                type: 'DELETE',
                url: '/api/test/uph/'+stt,
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    showModal();
                    loadDataByModel();
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
        var isIE = /*@cc_on!@*/false || !!document.documentMode;
        if(isIE){
            var tab_text="<table border='2px'><tr bgcolor='#F79646'>";
            var textRange; var j=0;
            tab = document.getElementById('tblDetail'); // id of table

            for(j = 0 ; j < tab.rows.length ; j++)
            {
                tab_text=tab_text+tab.rows[j].innerHTML+"</tr>";
                //tab_text=tab_text+"</tr>";
            }

            tab_text=tab_text+"</table>";
            tab_text= tab_text.replace(/<A[^>]*>|<\/A>/g, "");//remove if u want links in your table
            tab_text= tab_text.replace(/<img[^>]*>/gi,""); // remove if u want images in your table
            tab_text= tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

            var ua = window.navigator.userAgent;
            var msie = ua.indexOf("MSIE ");

            if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))      // If Internet Explorer
            {
                txtArea1.document.open("txt/html","replace");
                txtArea1.document.write(tab_text);
                txtArea1.document.close();
                txtArea1.focus();
                sa=txtArea1.document.execCommand("SaveAs",true,"UPH MB Assembly Line.xls");
            }
            else                 //other browser not tested on IE 11
            sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        }
        else{
            var blobURL = tableToExcel('tblDetail', 'uph_table');
            $(this).attr('download','UPH MB Assembly Line('+dataset.shiftType+').xls')
            $(this).attr('href',blobURL);
        }

    });

    $(document).ready(function() {
        getTimeNow();
        $('option[value="'+dataset.modelName+'"]').attr("selected","selected");
        $('input[name=timeSpan]').on('change', function() {
            dataset.workDate = this.value;
            $(".loader").removeClass("disableSelect");
            loadDataByModel();
            closeModal();
            window.localStorage.setItem('dataset', JSON.stringify(dataset));
            var getD = moment(dataset.workDate).format("MM/DD");
            $("#txtDateTime").html(getD + " ("+dataset.shiftType+")");
        });
    });
</script>

<script type="text/javascript" src="/assets/custom/js/nbb/dragModal.js"></script>
