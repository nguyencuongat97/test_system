function loadStationMaintain(factory) {
    $.ajax({
        type: "GET",
        url: "api/test/maintainNotification",
        data: {
            factory: factory
        },
        contentType: "application/json; charset=utf-8",
       success: function(data){
            $('.over-maintain').remove();
            if (!$.isEmptyObject(data)) {
                var stationMaintain = "";
               $.each(data, function(key,value){
                    stationMaintain = stationMaintain + '<div class="col-sm-4 over-maintain" data-toggle="modal" data-target="#modal_maintain"><div class="panel maintain">'
                                +'<a data-type="maintain" data-id="' + value.id + '" data-factory="' + value.factory + '" data-model-name="' + value.modelName + '" data-group-name="' + value.groupName + '" data-station-name="' + value.stationName + '" data-time="'+value.createdAt+'" onclick="notifyOnclick(this)">'
                                +'<div class="stationMaintain">'
                                +'<div class="pnlTitle"><label class="station-name">'+value.stationName+'</label></div>'
                                +'<div class="pnlContent">'
                                +'<b>Detail: </b><label class="time-maintain">'+value.detail+'</label></br>'
                                +'<b>Message: </b><label class="cause-maintain">'+value.message+'</label>'
                                +'</div></div></a></div></div>';
               });
               $("#station_maintain").append(stationMaintain);
            }
       },
       failure: function(errMsg) {
            console.log(errMsg);
       },
            complete: function() {
            setTimeout(loadStationMaintain, 300000, factory);
       }
    });
}

function loadModalMaintain(requestData){
    $.ajax({
        type: "GET",
        url: "api/test/stationEquipment",
        //data: requestData,
        data: {
            factory: requestData.factory,
            modelName: requestData.modelName,
            groupName: requestData.groupName,
            stationName: "L12B04-PT006"
        },
        contentType: "application/json; charset=utf-8",
       success: function(data){
            $('#tblMaintain tbody tr').remove();
            if (!$.isEmptyObject(data)) {
               var modalMaintain = "";
               for(i in data){
                   modalMaintain = modalMaintain + '<tr><td class="equipment">'+data[i].equipment+'</td>'
                       +'<td class="spec">'+data[i].spec+'</td>'
                       +'<td class="number_used">'+data[i].numberUsed+'</td>'
                       +'<td class="status"><select class="slt"><option value="0">OK</option><option value="1">Broken</option></select></td>'
                       +'<td class="action"><select class="slt"><option value="">No Action</option><option value="">Change New</option></select></td></tr>';
               };
               $("#tblMaintain tbody").append(modalMaintain);
            }
       },
       failure: function(errMsg) {
            console.log(errMsg);
       },
            complete: function() {
            setTimeout(loadModalMaintain, 300000);
       }
    });
}

function searchEmp(context){
    var employeeNo = context.value;
    $(".search_emp label").remove();
    $("input[name=employeeNo]").val(employeeNo);

    $.ajax({
        type: "GET",
        url: "api/test/employee",
        data: {
            employeeNo: employeeNo
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if (!$.isEmptyObject(data)) {
                var dt = "<label class='text-bold'>Name: </label><label class='control-label' id='empNameConfirm'> " + data.name + " ( " + data.dem + " )</label>";

                $(".search_emp").append(dt);
                $("button").removeAttr("disabled");
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}
//Enter For SearchEmp
$('.owner_confirm').keypress(function(event){
    var keycode = (event.keyCode ? event.keyCode : event.wich);
    if(keycode == '13'){
        searchEmp(this);
    }
});
