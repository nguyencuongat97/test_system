function loadStationLocked(factory) {
    $.ajax({
        type: "GET",
        url: "api/test/lockedNotification",
        data: {
            factory: factory
        },
        contentType: "application/json; charset=utf-8",
       success: function(data){
            $('.card').remove();
            if (!$.isEmptyObject(data)) {
                var stationLocked = "";
               $.each(data, function(key,value){
                   stationLocked = stationLocked + "<div class='col-sm-3 card'><div class='panel detail' onclick='selectedLocked(this)'>"
                              +"<div class='pnlHead'>"
                              +"<a data-type=\"error\" data-id=\"" + value.id + "\" data-factory=\"" + value.factory + "\" data-model-name=\"" + value.modelName + "\" data-group-name=\"" + value.groupName + "\" data-station-name=\"" + value.stationName + "\" onclick=\"notifyOnclick(this)\">"
                              +"<label class='station-name'>"+value.stationName+"</label></a></div>"
                              +"<div class='pnlBody'>"
                              +"<div class='pnlBodyHead'><label class='time-lock'>"+value.createdAt+"</label><a class='btn btn-primary pull-right btnSTL'>VNC</a></div>"
                              +"<div class='lock-detail'>"
                              +"<label class='model-name'><b>Model: </b>"+value.modelName+"</label><br/>"
                              +"<label class='lockDetail'><b>Detail: </b>"+value.detail+"</label></div></div>"
                              +"<div class='pnlFoot'>"
                              +"<a class='btn btn-primary btnSTL' onclick='moveToDetailPage(this.parentElement)'>Detail</a> <a class='btn btn-primary btnSTL' onclick='moveToGuidingPage(this.parentElement)'> Guiding</a></div>"
                              +"</div></div>";
               });
               $("#station_locked").append(stationLocked);
            }
       },
       failure: function(errMsg) {
            console.log(errMsg);
       },
            complete: function() {
            setTimeout(loadStationLocked, 300000, factory);
       }
    });
}

function loadStationLocked1(requestData) {
    $.ajax({
        type: "GET",
        url: "api/test/lockedNotification",
        data: requestData,
        contentType: "application/json; charset=utf-8",
       success: function(data){
            $('.card').remove();
            if (!$.isEmptyObject(data)) {
                var stationLocked = "";
               $.each(data, function(key,value){
                    if(value.stationName == requestData.stationName){
                    stationLocked = stationLocked + "<div class='col-sm-4 card'><div class='panel detail locked' onclick='selectedLocked(this)'>"
                                +"<div class='pnlHead'>"
                                +"<a data-type=\"error\" data-id=\"" + value.id + "\" data-factory=\"" + value.factory + "\" data-model-name=\"" + value.modelName + "\" data-group-name=\"" + value.groupName + "\" data-station-name=\"" + value.stationName + "\" onclick=\"notifyOnclick(this)\">"
                                +"<label class='station-name'>"+value.stationName+"</label></a></div>"
                                +"<div class='pnlBody'>"
                                +"<div class='pnlBodyHead'><label class='time-lock'>"+value.createdAt+"</label><a class='btn btn-primary pull-right btnSTL'>VNC</a></div>"
                                +"<div class='lock-detail'>"
                                +"<label class='model-name'><b>Model: </b>"+value.modelName+"</label><br/>"
                                +"<label class='lockDetail'><b>Detail: </b>"+value.detail+"</label></br>"
                                +"<label class='retestRate'><b>Retest Rate: </b>7.84%; </label><label class='retestRate'><b>&nbsp F.P.Y: </b>92.16%</label></br>"
                                +"<label class='suggest'><b>Suggest: </b>"+value.message+"</label></div></div>"
                                +"<div class='pnlFoot'>"
                                +"<a class='btn btn-primary btnSTL' onclick='moveToDetailPage(this.parentElement)'>Detail</a> <a class='btn btn-primary btnSTL' onclick='moveToGuidingPage(this.parentElement)'> Guiding</a></div>"
                                +"</div></div>";
                    }
                    else{
                     stationLocked = stationLocked + "<div class='col-sm-4 card'><div class='panel detail' onclick='selectedLocked(this)'>"
                               +"<div class='pnlHead'>"
                               +"<a data-type=\"error\" data-id=\"" + value.id + "\" data-factory=\"" + value.factory + "\" data-model-name=\"" + value.modelName + "\" data-group-name=\"" + value.groupName + "\" data-station-name=\"" + value.stationName + "\" onclick=\"notifyOnclick(this)\">"
                               +"<label class='station-name'>"+value.stationName+"</label></a></div>"
                               +"<div class='pnlBody'>"
                               +"<div class='pnlBodyHead'><label class='time-lock'>"+value.createdAt+"</label><a class='btn btn-primary pull-right btnSTL'>VNC</a></div>"
                               +"<div class='lock-detail'>"
                               +"<label class='model-name'><b>Model: </b>"+value.modelName+"</label><br/>"
                               +"<label class='lockDetail'><b>Detail: </b>"+value.detail+"</label></br>"
                               +"<label class='retestRate'><b>Retest Rate: </b>7.84%; </label><label class='retestRate'><b>&nbsp F.P.Y: </b>92.16%</label></br>"
                               +"<label class='suggest'><b>Suggest: </b>"+value.message+"</label></div></div>"
                               +"<div class='pnlFoot'>"
                               +"<a class='btn btn-primary btnSTL' onclick='moveToDetailPage(this.parentElement)'>Detail</a> <a class='btn btn-primary btnSTL' onclick='moveToGuidingPage(this.parentElement)'> Guiding</a></div>"
                               +"</div></div>";
                   }
               });
               $("#station_locked").append(stationLocked);
            }
       },
       failure: function(errMsg) {
            console.log(errMsg);
       },
            complete: function() {
            setTimeout(loadStationLocked1, 300000);
       }
    });
}

function selectedLocked(context){
    $('.detail').removeClass('locked');
    $(context).addClass('locked');
}

