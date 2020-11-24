function getTimeNow(){
    $.ajax({
        type: "GET",
        url: "/api/time/now",
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var current = moment(data).toDate();
            var formatted_date = moment(current).format("YYYY/MM/DD");
            dataset.workDate = formatted_date;
            window.localStorage.setItem('dataset', JSON.stringify(dataset));  
            
            $('.daterange-single').data('daterangepicker').setStartDate(current);
            setNightShift(current);
            init();
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}

function setNightShift(current){
    var getH = moment(current).format("HH:mm");

    if(getH < "07:30"){
        dataset.shiftType = "NIGHT";
        var yesterday = new Date((current.setDate(current.getDate()-1)));
        dataset.workDate = moment(yesterday).format("YYYY/MM/DD");
    }
    else if(getH >= "07:30" && getH < "19:30"){
        dataset.shiftType = "DAY";
    }
    else{
        dataset.shiftType = "NIGHT";
    }
    var getD = moment(dataset.workDate).format("MM/DD");
    $("#txtDateTime").html(" "+getD + " ("+dataset.shiftType+")");
}