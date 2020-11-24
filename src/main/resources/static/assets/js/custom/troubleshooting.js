function showInf(){
    var si = Highcharts.chart('pc-info-chart', {
    chart: {
        type: 'line',
        id: 'informationpc'
    },
    title: {
        text: '	'
    },
    subtitle: {
        text: ''
    },
    xAxis: {
        categories: ['23/12/2018', '24/12/2018', '25/12/2018', '26/12/2018', '27/12/2018', '28/12/2018', '29/12/2018']
    },
    yAxis: {
        title: {
            text: 'Percent (%)'
        }
    },
    plotOptions: {
        line: {
            dataLabels: {
                enabled: true
            },
            enableMouseTracking: true
        }
    },
    series: [{
        name: 'CPU',
        data: [7.0, 6.9, 9.5, 14.5, 18.4, 21.5, 25.2 ]
    }, {
        name: 'RAM USAGE',
        data: [69, 67, 63, 66, 75, 60, 80]
    },
    {
        name: 'DISK',
        data: [8.0, 10.9, 7.5, 10.5, 5.4, 15.5, 20.2 ]
    },]
    });
}

function showAdd() {
    var i = document.getElementById("table").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length -1;
    while(1)
    {
        i++;
        document.getElementById("table").insertRow(i).innerHTML = '<td class="newStep">'+[i]+'</td><td class="st_content"><input type="text" class="form-control"></td><td class="st_file"><input type="file" class="file-input" data-show-upload="false" data-show-preview="false" data-browse-class="btn btn-xs" data-remove-class="btn btn-xs" accept="image/*"></td>';
        break;
    }
    initUploadBtn();
};

function delRow() {
    var i = document.getElementById("table").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length-2;
    while(i>-1)
    {
        i++;
        document.getElementById("table").deleteRow(i);
        break;
    }

};

function confirmSolution2(){
    var data = new FormData();
    data.append('trackingId', dataset.trackingId);
    data.append('factory', dataset.factory);
    data.append('modelName', dataset.modelName);
    data.append('groupName', dataset.groupName);
    data.append('stationName', dataset.stationName);
    data.append('errorCode', dataset.errorCode);
    data.append('employee', dataset.employee);
    data.append('solutionId', dataset.solutionId);

    $.ajax({
        type: "POST",
        url: "/api/test/tracking/confirm",
        data: data,
        processData: false,
        contentType: false,
        mimeType: "multipart/form-data",
        success: function(json){
            if (json == "success") {
                alert("Confirm success!");
                $('button[name="add"]').addClass("hidden");
                $('button[name="confirm"]').addClass("hidden");
                $('button[name="give-up"]').addClass("hidden");
            } else {
                alert("Confirm failed!");
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
            alert("Confirm failed!");
        }
    });
};

//Add New Solution
function addNew2(){
    var data = new FormData();
    data.append('trackingId', dataset.trackingId);
    data.append('factory', dataset.factory);
    data.append('modelName', dataset.modelName);
    data.append('groupName', dataset.groupName);
    data.append('stationName', dataset.stationName);
    data.append('errorCode', dataset.errorCode);
    data.append('employee', dataset.employee);
    data.append('solutionName', $('input[name=solutionName]').val());
    var action = $('textarea[name="action"]').val().replace(/\r\n|\r|\n/g,"<br />");
    data.append('action', action);

    var size = document.getElementById("table").getElementsByTagName("tbody")[0].getElementsByTagName("tr").length-1;

    var captions = $('#table>tbody>tr>td.st_content>input');
    var files = $('#table').find('input.file-input');

    for(var i=0; i<size; i++){
        data.append('captionFiles['+i+']', captions[i].value);
        if (files[i].files.length > 0) {
            data.append('uploadingFiles['+i+']', files[i].files[0]);
        }
    }

    $.ajax({
        type: "POST",
        url: "/api/test/tracking/confirm",
        data: data,
        processData: false,
        contentType: false,
        mimeType: "multipart/form-data",
        success: function(data){
            if (data == "success") {
                alert("Confirm success!");
                $('button[name="add"]').addClass("hidden");
                $('button[name="confirm"]').addClass("hidden");
                $('button[name="give-up"]').addClass("hidden");
                loadDetail();
                loadHistory();
            } else {
                alert("Confirm failed!");
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
            alert("Confirm failed!");
        }
    });
};

function giveUp() {
    if (confirm("Are you sure give up this task?") == true) {
        $.ajax({
            type: "GET",
            url: "/api/test/station/error",
            data: {
                trackingId: dataset.trackingId,
                employee: dataset.employee
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if (data == "success") {
                    alert("Give up success!");
                    $('button[name="add"]').addClass("hidden");
                    $('button[name="confirm"]').addClass("hidden");
                    $('button[name="give-up"]').addClass("hidden");
                } else {
                    alert("Give up failed!");
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
                alert("Confirm failed!");
            }
        });
    }
}

function loadErrorCodes(requestData) {
    $.ajax({
        type: "GET",
        url: "/api/test/station/error",
        data: requestData,
        contentType: "application/json; charset=utf-8",
        success: function(data){
            $('.erc').remove();
            $('#error-name').html("");
            if (!$.isEmptyObject(data)) {
                var op = "";
                var count = Object.keys(data).length;
                $.each(data, function(key,value) {
                    var description;
                    if(value == null){
                        description = "NO DESCRIPTION YET";
                    }
                    else{
                        description = value;
                    }
                    if(count == 1){
                        op = op + '<label class="btn btn-default erc selectedErr" title="'+description+'">'
                        + '<a class="errorName">'+key+'</a>'
                        + '<input class="control-primary hidden" id="'+description+' ('+key+')'+'" type="radio" name="error-code" value="'+key+'" checked="true"></label>';
                    }
                    else{
                        op = op + '<label class="btn btn-default erc" title="'+description+'" onclick="selectedErrorCode(this)">'
                        + '<a class="errorName">'+key+'</a>'
                        + '<input class="control-primary hidden" id="'+description+' ('+key+')'+'" type="radio" name="error-code" value="'+key+'"></label>';
                    }
                });
                $('#radioBtn').append(op);
                loadSolution();
            }
            else{
                $('#radioBtn').append("<label class='erc' style='font-size:14px;'>NO ERROR CODE YET</label>");
                loadSolution();
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}
function showErrName(){
    var t = $('input[name=error-code]');
    var error_name = "";
    for(var j=0; j<t.length; j++){
        if(t[j].checked == true){
            error_name = t[j].id;
            $("#error-name").html(error_name);
        }
    }
}

function selectedErrorCode(context){
    $('.erc').removeClass('selectedErr');
    $(context).addClass('selectedErr');
}
function removeTableErr(){
    $('.tableErr tbody tr').remove();
    $('.tableErr tfoot').remove();
}

function loadSolution2() {
   showErrName();
    var t = $('input[name=error-code]');
    var error_code = "";
    for(var j=0; j<t.length; j++){
        if(t[j].checked == true){
            error_code = error_code + t[j].value;
        }
    }
    $.ajax({
        type: "GET",
        url: "/api/test/solution",
        data: {
           errorCode: error_code,
           factory: requestData.factory,
           modelName: requestData.modelName
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
           removeTableErr();
            var op = "";
            var dem = 0;
            if(error_code != ""){
                if(!$.isEmptyObject(data)) {
                    for(i in data){
                        dem++;
                        var li = "";
                        $.each(data[i].steps, function(k,v){
                            li = li+ k + '. ' + v + '<br>';
                        });
                        op = op + '<tr><td class="priority">'+dem+'</td>'
                            +'<td class="solution_name">'+data[i].solution+'</td>'
                            +'<td class="number_usage">'+data[i].numberUsed+'</td>'
                            +'<td class="list_steps">'+li+'</td>'
                            +'<td><a href="#" data-id="' + data[i].id + '" data-toggle="modal" data-target="#modal-confirm-solution" class="btn btn-primary btnConfirm" onclick="showConfirmModal(this)">Confirm</a></td></tr>';
                    };
                }
                else{
                    op = '<tr><td colspan="4" align="center">-- NO CAUSE YET --</td></tr>';
                }
                $('.tableErr tbody').append(op);
                var tf ="<tfoot><tr>"
                      + "<td colspan='4' align='center'><a style='font-size:10px;' href='#' data-toggle='modal' data-target='#modal-add-new-solution' class='button-next btn btn-default' onclick='showAddNewModal()'>"
                      + "<i class='fa fa-plus'></i><b> Add New Cause</b></a></td>"
                      + "</tr></tfoot>";
                $('.tableErr').append(tf);
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
};

function loadCalibration(requestData) {
    $.ajax({
        type: "GET",
        url: "/api/test/history/calibration",
        data: requestData,
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var gd = $('.aCal');
            for (i = 0; i<gd.length; i++) {
                gd[i].remove();
            }
            var lGSample = "";
            if (!$.isEmptyObject(data)) {
                $.each(data, function(key,value){
                    lGSample = lGSample + '<li class="aCal">'+value+'</li>';
                });
            }
            $('#list_cal').append(lGSample);

            //Get Last
            var dategs=data[0];
            $('#dateGS').html(dategs);
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
};

function loadRepairHistory(requestData) {
    $.ajax({
        type: "GET",
        url: "/api/test/history/fixed",
        data: requestData,
        contentType: "application/json; charset=utf-8",
        success: function(data){
            $(".repair_history tbody tr").remove();
            var rh = "";
            $.each(data, function(key,value){
                rh = rh + '<tr><td>'+value.dateTimeLock+'</td><td>'+value.dateTimeUnlock+'</td><td>'+value.owner+'</td><td style="text-align:left; padding-left:2px;">'+value.why+'</td><td style="text-align:left;padding-left:2px;">'+value.action+'</td></tr>';
            });
            rh = rh + "</tbody>";
            $('.repair_history tbody').append(rh);

            //Get Last
            if (data.length > 0) {
                $('#dateUL').html(data[0].dateTimeUnlock);
                $('#empUL').html(data[0].owner);
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
};

function searchEmp(context){
    var employeeNo = context.previousElementSibling.previousElementSibling.children.employeeNo.value;
    searchEmployee(employeeNo);
}

function searchEmployee(employeeNo) {
    $("input[name=employeeNo]").val(employeeNo);
    $.ajax({
        type: "GET",
        url: "/api/test/employee",
        data: {
            employeeNo: employeeNo
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if (!$.isEmptyObject(data)) {
                dataset.employee = data.employeeNo;
                dataset.employeeName = data.name;

                $("label[name=search_emp]").html(data.name);
                $('button[name="btn-submit-solution"]').removeAttr("disabled");
            } else {
                dataset.employee = null;
                dataset.employeeName = null;

                $("label[name=search_emp]").html('NOT FOUND');
                $('button[name="btn-submit-solution"]').attr("disabled", "disabled");
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}

//Enter For SearchEmp
$('input[name=employeeNo]').keypress(function(event){
    var keyCode = (event.keyCode ? event.keyCode : event.wich);
    if(keyCode == '13'){
        searchEmployee(event.target.value)
    }
});

function showAddNewModal(context) {
    if (dataset.employee !=  null) {
        $("input[name=employeeNo]").val(dataset.employee);
        $("label[name=search_emp]").html(dataset.employeeName);
        $('button[name="btn-submit-solution"]').removeAttr("disabled");
    }

    $('label[name="error-code"').html(context.dataset.errorCode);
    if (context.dataset.errorDesc == null || context.dataset.errorDesc == "") {
        $('label[name="error-desc"').html("No error description");
    } else {
        $('label[name="error-desc"').html(context.dataset.errorDesc);
    }

    dataset.errorCode = context.dataset.errorCode;
    dataset.solutionId = null;
}

function showConfirmModal(context) {
    if (dataset.employee !=  null) {
        $("input[name=employeeNo]").val(dataset.employee);
        $("label[name=search_emp]").html(dataset.employeeName);
        $('button[name="btn-submit-solution"]').removeAttr("disabled");
    }

    dataset.solutionId = context.dataset.solutionId;
    dataset.errorCode = context.dataset.errorCode;
}

function loadSolutionList(requestData) {
    $.ajax({
        type: "GET",
        url: "/api/test/station/solution",
        data: requestData,
        contentType: "application/json; charset=utf-8",
        success: function(data){
            $('table.solution>tbody>tr').remove();
            var table = $('table.solution>tbody');
            if ($.isEmptyObject(data)) {
                table.append('<tr><td colspan="6" align="center">-- NO DATA --</td></tr>');
            } else {
                for(i in data) {
                    var reason = "";
                    var action = "";
                    var confirmActive = "";
                    if (data[i].solution && data[i].steps != null) {
                        reason = data[i].solution;
                        action = data[i].action;
                    } else {
                        confirmActive = "disabled";
                    }
                    table.append('<tr><td><a data-error-desc="'+data[i].errorDescription+'" data-error-code="'+data[i].errorCode+'" data-model-name="'+data[i].modelName+'" onclick="showGuiding(this);">'+data[i].errorCode+'</a></td><td class="alignLeft">'+reason+'</td><td class="alignLeft">'+action+'</td><td>1</td><td>'+data[i].numberSuccess+'</td><td>' +
                                 '<button class="btn btnAction" data-error-code="'+data[i].errorCode+'" data-error-desc="'+data[i].errorDescription+'" data-toggle="modal" data-target="#modal-add-new-solution" onclick="showAddNewModal(this)"><i class="fa fa-plus"></i></button>' +
                                 '<button class="btn btnAction '+confirmActive+'" data-solution-id="'+data[i].id+'" data-error-code="'+data[i].errorCode+'" data-toggle="modal" data-target="#modal-confirm-solution" onclick="showConfirmModal(this)"><i class="fa fa-check"></i></button>' +
                                 '</td></tr>');
                }
                $('a[name="error-code"]').html(data[0].errorCode);
                if (data[0].errorDescription == null || data[0].errorDescription == "") {
                    $('label[name="error-desc"]').html('No error description');
                } else {
                    $('label[name="error-desc"]').html(data[0].errorDescription);
                }
                loadTroubleAndHistory(data[0].modelName, data[0].errorCode, data[0].errorDescription);
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        }
    });
}