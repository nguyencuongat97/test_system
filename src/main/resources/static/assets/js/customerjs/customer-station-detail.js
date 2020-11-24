// $body = $("body");
// $(document).on({
//     ajaxStart: function() { $body.addClass("loading");    },
//      ajaxStop: function() { $body.removeClass("loading"); }
// });
var editRC, editA, editD, errorC, data;

function loadDataByTimeOfStationFromJson(chart, graphProps, json) {
    if (chart.series.length > 2) {
        (chart.series)[4].remove();
        (chart.series)[3].remove();
        (chart.series)[2].remove();
        (chart.series)[1].remove();
    }

    var keys = Object.keys(json);
    var data = new Array(keys.length);
    for (j in keys) {
        var value = json[keys[j]];
        if (!$.isEmptyObject(value)) {
            data[j] = { name: keys[j], y: value.retestRate, wip: value.wip, firstFail: value.firstFail, secondFail: value.secondFail }
        } else {
            data[j] = { name: keys[j], y: 0, wip: 0, firstFail: 0, secondFail: 0 }
        }
    }
    chart.update({
        series: [{
            data: data
        }],
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.2f} %',
                    style: {
                        fontWeight: 'normal',
                        fontSize: '9px',
                    }
                }
            }
        },
        xAxis: {
            tickInterval: Math.ceil(keys.length / 5),
        },
        yAxis: {
            plotLines: [{
                value: graphProps.errorValue,
                color: 'red',
                dashStyle: 'longdash',
                width: 1,
                zIndex: 2,
                label: {
                    text: graphProps.errorValue + '%',
                    align: 'right',
                    x: 0,
                    style: {
                        fontSize: '10px',
                    }
                }
            }],
            minTickInterval: 5,
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
        credits: {
            enabled: false
        }
    });
}

function loadDataFirstPassRateFromJson(chart, graphProps, json) {
    if (chart.series.length > 2) {
        (chart.series)[4].remove();
        (chart.series)[3].remove();
        (chart.series)[2].remove();
        (chart.series)[1].remove();
    }

    var keys = Object.keys(json);
    var data = new Array(keys.length);
    for (j in keys) {
        var value = json[keys[j]];
        if (!$.isEmptyObject(value)) {
            data[j] = { name: keys[j], y: value.firstPassRate, wip: value.wip, firstFail: value.firstFail }
        } else {
            data[j] = { name: keys[j], y: 0, wip: 0, firstFail: 0 }
        }
    }

    chart.update({
        series: [{
            data: data
        }],
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.2f} %',
                    style: {
                        fontWeight: 'normal',
                        fontSize: '9px',
                    }
                }
            }
        },
        xAxis: {
            tickInterval: Math.ceil(keys.length / 5),
        },
        yAxis: {
            plotLines: [{
                value: graphProps.errorValue,
                color: 'green',
                dashStyle: 'longdash',
                width: 1,
                zIndex: 2,
                label: {
                    text: graphProps.errorValue + '%',
                    align: 'right',
                    x: 0,
                    style: {
                        fontSize: '10px',
                    }
                }
            }],
            minTickInterval: 5,
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
        credits: {
            enabled: false
        }
    });
}

function loadDataHitRateFromJson(chart, graphProps, json) {
    if (chart.series.length > 2) {
        (chart.series)[4].remove();
        (chart.series)[3].remove();
        (chart.series)[2].remove();
        (chart.series)[1].remove();
    }

    var keys = Object.keys(json);
    var data = new Array(keys.length);
    for (j in keys) {
        var value = json[keys[j]];
        if (!$.isEmptyObject(value)) {
            data[j] = { name: keys[j], y: value.hitRate, wip: value.wip, plan: value.plan }
        } else {
            data[j] = { name: keys[j], y: 0, wip: 0, plan: 0 }
        }
    }

    chart.update({
        series: [{
            data: data
        }],
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.2f} %',
                    style: {
                        fontWeight: 'normal',
                        fontSize: '9px',
                    }
                }
            }
        },
        xAxis: {
            tickInterval: Math.ceil(keys.length / 5),
        },
        yAxis: {
            plotLines: [{
                value: graphProps.errorValue,
                color: 'green',
                dashStyle: 'longdash',
                width: 1,
                zIndex: 2,
                label: {
                    text: graphProps.errorValue + '%',
                    align: 'right',
                    x: 0,
                    style: {
                        fontSize: '10px',
                    }
                }
            }],
            minTickInterval: 5,
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
        credits: {
            enabled: false
        }
    });
}

function loadDataYieldRateFromJson(chart, graphProps, json) {
    if (chart.series.length > 2) {
        (chart.series)[4].remove();
        (chart.series)[3].remove();
        (chart.series)[2].remove();
        (chart.series)[1].remove();
    }

    var keys = Object.keys(json);
    var data = new Array(keys.length);
    for (j in keys) {
        var value = json[keys[j]];
        if (!$.isEmptyObject(value)) {
            data[j] = { name: keys[j], y: value.yieldRate, wip: value.wip, pass: value.pass }
        } else {
            data[j] = { name: keys[j], y: 0, wip: 0, pass: 0 }
        }
    }

    chart.update({
        series: [{
            data: data
        }],
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    format: '{point.y:.2f} %',
                    style: {
                        fontWeight: 'normal',
                        fontSize: '9px',
                    }
                }
            }
        },
        xAxis: {
            tickInterval: Math.ceil(keys.length / 5),
        },
        yAxis: {
            plotLines: [{
                value: graphProps.errorValue,
                color: 'green',
                dashStyle: 'longdash',
                width: 1,
                zIndex: 2,
                label: {
                    text: graphProps.errorValue + '%',
                    align: 'right',
                    x: 0,
                    style: {
                        fontSize: '10px',
                    }
                }
            }],
            minTickInterval: 5,
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
        credits: {
            enabled: false
        }
    });
}

function loadDataByErrorFromJson(chart, graphProps, json) {
    var data = [[], [], [], [], []];

    var keys = Object.keys(json);
    for (j in keys) {
        var errorCodes = ['Total', 'Other'];
        var keys2 = Object.keys(json[keys[j]]);
        for (i in keys2) {
            if (keys2[i] != 'Other' && keys2[i] != 'Total') {
                errorCodes.push(keys2[i]);
            }
        }

        for (i in errorCodes) {
            var value = json[keys[j]][errorCodes[i]];
            data[i][j] = { name: keys[j], y: value.testFail, fail: value.fail }
        }
    }

    chart.update({
        series: [{
            name: errorCodes[2],
            type: 'column',
            data: data[2]
        }, {
            name: errorCodes[3],
            type: 'column',
            data: data[3]
        }, {
            name: errorCodes[4],
            type: 'column',
            data: data[4]
        }, {
            name: 'Other',
            type: 'column',
            data: data[1]
        }, {
            name: 'Total',
            type: 'spline',
            data: data[0]
        }],

        xAxis: {
            categories: keys,
            labels: {
                autoRotation: false,
                style: {
                    fontSize: '10px'
                },
            },
            tickInterval: Math.ceil(keys.length / 5)
        },

        yAxis: {
            minTickInterval: 1
        },
        credits: {
            enabled: false
        }
    });

    if (data[4].length > 0) {
        (chart.series)[2].show();
    } else {
        (chart.series)[2].hide();
    }

    if (data[3].length > 0) {
        (chart.series)[1].show();
    } else {
        (chart.series)[1].hide();
    }

    if (data[2].length > 0) {
        (chart.series)[0].show();
    } else {
        (chart.series)[0].hide();
    }
}

function loadDataByTimeOfErrorFromJson(chart, graphProps, json) {
    var data = [3];
    var error = Object.keys(json);
    var keys = [];
    for (i in error) {
        keys = Object.keys(json[error[i]]);
        data[i] = new Array(keys.length);
        for (j in keys) {
            var value = json[error[i]][keys[j]];
            if (!$.isEmptyObject(json[error[i]][keys[j]])) {
                data[i][j] = { name: keys[j], y: value.testFail, fail: value.fail }
            } else {
                data[i][j] = { name: keys[j], y: 0, fail: 0 }
            }
        }
    }

    chart.update({
        series: [{
            name: error[0],
            data: data[0]
        }, {
            label: {
                enabled: false
            },
            name: error[1],
            data: data[1]
        }, {
            label: {
                enabled: false
            },
            name: error[2],
            data: data[2]
        }],
        xAxis: {
            tickInterval: Math.ceil(keys.length / 5),
        },
        yAxis: {
            minTickInterval: 1
        },
        credits: {
            enabled: false
        }
    });

    if (chart.series.length > 3) {
        (chart.series)[4].remove();
        (chart.series)[3].remove();
    }

    for (var i = 2; i >= 0; i--) {
        if (i >= error.length) {
            (chart.series)[i].hide();
        } else {
            (chart.series)[i].show();
        }
    }
}

function loadDataTop1ErrorByWeekFromJson(chart, graphProps, json) {
    if (chart.series.length > 2) {
        (chart.series)[4].remove();
        (chart.series)[3].remove();
        (chart.series)[2].remove();
        (chart.series)[1].remove();
    }

    var errorCode = Object.keys(json);

    var keys = Object.keys(json[errorCode[0]]);
    var data = new Array(keys.length);
    for (j in keys) {
        var value = json[errorCode[0]][keys[j]];
        if (!$.isEmptyObject(value)) {
            data[j] = { name: keys[j], y: value.testFail }
        } else {
            data[j] = { name: keys[j], y: 0 }
        }
    }

    chart.update({
        title: {
            text: errorCode + ' TEST FAIL BY WEEK'
        },
        series: [{
            data: data
        }],

        xAxis: {
            categories: keys,
            labels: {
                autoRotation: false,
                style: {
                    fontSize: '10px'
                },
            },
            tickInterval: Math.ceil(keys.length / 5),
        },

        yAxis: {
            minTickInterval: 1
        },

        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'normal',
                        fontSize: '9px',
                    }
                }
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
        }
    });

    if (errorCode[0] == "") {
        (chart.series)[0].hide();
    } else {
        (chart.series)[0].show();
    }
}

function loadDataTop1ErrorByHourFromJson(chart, graphProps, json) {
    if (chart.series.length > 2) {
        (chart.series)[4].remove();
        (chart.series)[3].remove();
        (chart.series)[2].remove();
        (chart.series)[1].remove();
    }

    var errorCode = Object.keys(json);

    var keys = Object.keys(json[errorCode[0]]);
    var data = new Array(keys.length);
    for (j in keys) {
        var value = json[errorCode[0]][keys[j]];
        if (!$.isEmptyObject(value)) {
            data[j] = { name: keys[j], y: value.testFail }
        } else {
            data[j] = { name: keys[j], y: 0 }
        }
    }

    chart.update({
        title: {
            text: errorCode + ' TEST FAIL BY HOUR'
        },
        series: [{
            data: data
        }],
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'normal',
                        fontSize: '9px',
                    }
                }
            }
        },
        xAxis: {
            tickInterval: Math.ceil(keys.length / 5),
        },
        yAxis: {
            minTickInterval: 1
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
        }
    });

    if (errorCode[0] == "") {
        (chart.series)[0].hide();
    } else {
        (chart.series)[0].show();
    }
}
function loadDataTableTop3ErrorFromJson(json) {
    $('#tbl-top-3-error>tbody>tr').remove();
    var stt = 0;
    var body = '';
    var tmpRC, tmpA,tmpD;
    if (!$.isEmptyObject(json)) {
        var keys = Object.keys(json);
        for (i in keys) {    
            stt ++;
            body+='<tr id="row'+stt+'"><td>' + stt + '</td>'
            +   '<td class="valueError'+stt+'">'
            +   '<a data-model-name="' + json[keys[i]].modelName + '" data-error-code="' + json[keys[i]].errorCode + '" data-error-desc="' + json[keys[i]].errorDescription + '" onclick="showHistory(this);">' + json[keys[i]].errorCode + '</a>'
            +   '</td>';
            if(json[keys[i]].rootCause==null || json[keys[i]].action==null || json[keys[i]].description==null){
                body+=  '<td><textarea style="white-space:pre" id="txtEditRC'+stt+'" class="form-control hidden" data-idt="'+json[keys[i]].idNoteError+'" oninput="textwrite('+stt+')" name="" type="text" value="" ></textarea>'
                        +   '<span id="rootCause'+stt+'"  value=""></span></td>'
                        +   '<td><textarea id="txtEditAction'+stt+'" class="form-control hidden" name="" type="text" value="" oninput="textwrite('+stt+')"></textarea>'
                        +   '<span id="action'+stt+'" value=""></span></td>'
                        +   '<td><textarea id="txtEditD'+stt+'" class="form-control hidden" name="" type="text" value="" oninput="textwrite('+stt+')"></textarea>'
                        +   '<span id="errorDescription'+stt+'" value=""></span></td>' ;
            }else{
                //tmpRC = json[keys[i]].rootCause.split('<br />').join('/[\r\n]+/gm');
                tmpRC = json[keys[i]].rootCause.replace(/<br\s*\/?>/g,'\n');
                tmpD = json[keys[i]].description.replace(/<br\s*\/?>/g,'\n');
                tmpA = json[keys[i]].action.replace(/<br\s*\/?>/g,'\n');
                

                body+=  '<td><textarea id="txtEditRC'+stt+'" class="form-control hidden" oninput="textwrite('+stt+')" data-idt="'+json[keys[i]].idNoteError+'" name=""  type="text" value="text break text">'+ tmpRC +'</textarea>'
                        +   '<span id="rootCause'+stt+'" value="'+ json[keys[i]].rootCause +'">'+json[keys[i]].rootCause+'</span></td>'
                        +   '<td><textarea id="txtEditAction'+stt+'" class="form-control hidden" name="" type="text" value="" oninput="textwrite('+stt+')">'+tmpA+'</textarea>'
                        +   '<span id="action'+stt+'" value="'+ json[keys[i]].action +'">'+json[keys[i]].action+'</span></td>'
                        +   '<td><textarea id="txtEditD'+stt+'" class="form-control hidden" name="" type="text" value="" oninput="textwrite('+stt+')">'+tmpD+'</textarea>'
                        +   '<span id="errorDescription'+stt+'" value="'+ json[keys[i]].description +'">'+ json[keys[i]].description +'</span>'
                        +   '</td>';
            }
           
            
            body+= '<td>' + json[keys[i]].wip + '</td><td>' + json[keys[i]].pass + '</td><td>' + json[keys[i]].testFail + '</td>' //'<td>' + json[keys[i]].fail + '</td><td>' + (json[keys[i]].fail * 100 / json[keys[i]].wip).toFixed(2) + ' %</td>'
            +   '<td>'
            +       '<button class="btn btn-warning" id="editRow'+stt+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+stt+')">'
            +             '<i class="icon icon-pencil"></i>'
            +       '</button>'
            // +       '<button class="btn btn-danger" id="deleteRow'+json[keys[i]].id+'" title="Delete" style="padding: 4px 10px; margin-left: 5px;" onclick="deleteModel('+json[keys[i]].id+')">'
            // +             '<i class="icon icon-trash"></i>'
            // +       '</button>'                     
            +       '<button class="btn btn-success hidden" id="confirmRow'+stt+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel('+stt+')">'
            +             '<i class="icon icon-checkmark"></i>'
            +       '</button><button class="btn btn-danger hidden" id="cancelRow'+stt+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+stt+')">'
            +             '<i class="icon icon-spinner11"></i>'
            +       '</button>'
            +   '</td>'
            +   '</tr>';
            
        }
        $('#tbl-top-3-error>tbody').html(body);
    } else {
        $('#tbl-top-3-error>tbody').html('<tr><td colspan="11" align="center">-- NO ERROR --</td></tr>');
    }
}
//event 
function edit(stt) {
    $("#editRow" + stt).addClass("hidden");
    $("#confirmRow" + stt).removeClass("hidden");
    $("#cancelRow" + stt).removeClass("hidden");

    $("#rootCause" + stt).addClass("hidden");
    $("#txtEditRC" + stt).removeClass("hidden");

    $("#action" + stt).addClass("hidden");
    $("#txtEditAction" + stt).removeClass("hidden");

    $("#errorDescription" + stt).addClass("hidden");
    $("#txtEditD" + stt).removeClass("hidden");
    // if($("#rootCause"+stt).val()==null || $("#action"+stt).val()==null|| $("#errorDescription"+stt).val()==null){
    //     $("#rootCause"+stt).val('');
    //     $("#action"+stt).val('');
    //     $("#errorDescription"+stt).val('');
    // }


    $(".btn-warning").attr("disabled", "disabled");
}
function textwrite(stt){
    editRC = $('#txtEditRC'+stt).val().replace(/\r\n|\r|\n/g,"<br />");
    editD = $('#txtEditD'+stt).val().replace(/\r\n|\r|\n/g,"<br />");
    editA  = $('#txtEditAction'+stt).val().replace(/\r\n|\r|\n/g,"<br />");
    console.log(editRC);
}
function confirmModel(stt){
    // var confirm = window.confirm("Are you sure want to change?");
    // if(confirm == true){
        //  editRC = $('#txtEditRC'+stt).val();
        //  editA = $('#txtEditAction'+stt).val();
        //  editD = $('#txtEditD'+stt).val();
         errorC =  $('.valueError'+stt+' a').html();
		 var dd = $('#txtEditRC' + stt);
         // console.log("dd::", dd[0].dataset.idt)
    	 var id = dd[0].dataset.idt * 1;

         var data = new FormData();
         data.append('factory', dataset.factory);
         data.append('modelName', dataset.modelName);
         data.append('timeSpan', dataset.timeSpan);
         data.append('action', editA);
         data.append('description', editD);
         data.append('rootCause', editRC);
         data.append('error', errorC);
         data.append('id', id);

         $.ajax({
             type: 'POST',
             url: '/api/test/station/noteError',
             data: data,
             processData: false,
             contentType: false,
             mimeType: "multipart/form-data",
             success: function(response){
                 $("#editRow"+stt).removeClass("hidden");
                 $("#confirmRow"+stt).addClass("hidden");
                 $("#cancelRow"+stt).addClass("hidden");

                 $("#rootCause"+stt).html(editRC);
                 $("#action"+stt).html(editA);
                 $("#errorDescription"+stt).html(editD);

                 $('#action'+stt).removeClass("hidden");
                 $('#rootCause'+stt).removeClass("hidden");
                 $('#errorDescription'+stt).removeClass("hidden");

                 $("#txtEditRC"+stt).addClass("hidden");
                 $("#txtEditAction"+stt).addClass("hidden");
                 $("#txtEditD"+stt).addClass("hidden");

                 $(".btn-warning").removeAttr("disabled");

             },
             failure: function(errMsg) {
                 console.log(errMsg);
             },
         });
    // }
}
function cancelEdit(stt) {
    $("#editRow" + stt).removeClass("hidden");
    $("#confirmRow" + stt).addClass("hidden");
    $("#cancelRow" + stt).addClass("hidden");

    $("#rootCause" + stt).removeClass("hidden");
    $("#txtEditRC" + stt).addClass("hidden");

    $("#action" + stt).removeClass("hidden");
    $("#txtEditAction" + stt).addClass("hidden");

    $("#errorDescription" + stt).removeClass("hidden");
    $("#txtEditD" + stt).addClass("hidden");

    // $("#btnAddNew").removeAttr("disabled");

    $(".btn-warning").removeAttr("disabled");
}

function loadDataTableErrorHistoryFromJson(requestData) {
    $.ajax({
        type: "GET",
        url: "/api/test/station/errorHistory",
        data: requestData,
        contentType: "application/json; charset=utf-8",
        success: function (json) {
            $('#tbl-error-history>tbody>tr').remove();

            var body = $('#tbl-error-history>tbody');
            if (!$.isEmptyObject(json)) {
                for (i in json) {
                    body.append('<tr><td>' + (1 + Number(i)) + '</td><td>' + json[i].time + '</td><td>' + json[i].stationName + '</td><td>' + json[i].chamber + '</td><td><a data-serial="' + json[i].serial + '" data-toggle="modal" data-target="#modal_serial_tracking" onclick="loadSerialTracking(this.dataset.serial)">' + json[i].serial + '</a></td><td>' + json[i].errorCode + '</td><td>' + json[i].lsl + '</td><td>' + json[i].usl + '</td><td>' + json[i].value + '</td><td>' + json[i].cycle + '</td></tr>');
                }
            } else {
                body.append('<tr><td colspan="11" align="center">-- NO DATA HISTORY --</td></tr>');
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}

function loadGroupOverview() {
    $('.loader').css('display', 'block');
    $.ajax({
        type: "GET",
        url: "/api/test/group/total",
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            groupName: dataset.groupName,
            timeSpan: dataset.timeSpan,
            customer: true,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var categories = new Array(data.length);
            var dataChart = new Array(data.length);
            var drilldown = new Array(data.length);
            for (i in data) {
                categories[i] = data[i].groupName;
                dataChart[i] = {
                    name: data[i].groupName,
                    y: data[i].retestRate,
                    drilldown: data[i].groupName,
                    wip: data[i].wip,
                    pass: data[i].pass,
                    firstFail: data[i].firstFail,
                    secondFail: data[i].secondFail,
                    yieldRate: data[i].yieldRate,
                    firstPassRate: data[i].firstPassRate
                }

                var stationList = data[i].stationList;
                var drillData = new Array(stationList.length);
                for (j in stationList) {
                    drillData[j] = {
                        name: stationList[j].stationName,
                        y: stationList[j].retestRate,
                        wip: stationList[j].wip,
                        pass: stationList[j].pass,
                        firstFail: stationList[j].firstFail,
                        secondFail: stationList[j].secondFail,
                        yieldRate: stationList[j].yieldRate,
                        firstPassRate: stationList[j].firstPassRate
                    }
                }
                drilldown[i] = {
                    name: data[i].groupName,
                    id: data[i].groupName,
                    data: drillData
                };
            }

            Highcharts.chart('chart-group-overview', {
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },
                title: {
                    text: dataset.modelName + ' OVERVIEW'
                },
                xAxis: {
                    type: 'category',
                    labels: {
                        //                        autoRotation: false,
                        style: {
                            fontSize: '11px'
                        },
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ' '
                    }
                },
                plotOptions: {
                    series: {
                        borderWidth: 0,
                        dataLabels: {
                            enabled: true,
                            format: '{point.y:.2f}%'
                        },
                        cursor: 'pointer',
                        events: {
                            click: function (event) {
                                var drilldown = event.point.drilldown;
                                if (drilldown == undefined || drilldown.levelNumber == '') {
                                    dataset.groupName = this.name;
                                    dataset.stationName = event.point.name;
                                    loadGroups(dataset, true, function (dataset) {
                                        loadStations(dataset, true, loadChart, true);
                                    });
                                }
                            }
                        }
                    },
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                    // pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> <br/> Input: <b>{point.wip}</b> <br/> Output: <b>{point.pass}</b> <br/> First Fail: <b>{point.firstFail}</b> <br/> Second Fail: <b>{point.secondFail}</b> <br/> Yield Rate: <b>{point.yieldRate:.2f}%</b> <br/> First Pass Rate: <b>{point.firstPassRate:.2f}%</b>'
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b>'
                },
                legend: {
                    enabled: false
                },
                credits: {
                    enabled: false
                },
                series: [{
                    name: ' ',
                    colorByPoint: true,
                    data: dataChart,
                    marker: {
                        enabled: false
                    },
                }],
                drilldown: {
                    series: drilldown
                }
            });

        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
        complete: function(){
            $('.loader').css('display', 'none');
        }
    });
}
function loadHighChartWeekAllGroupName() {
    $.ajax({
        type: "GET",
        url: "/api/test/model/weekly",
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            customer: true,
            // timeSpan: dataset.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var keys = Object.keys(data);
            var yieldRate = new Array(keys.length);
            for (j in keys) {
                var value = data[keys[j]];
                if (!$.isEmptyObject(value)) {
                    var ele = parseFloat((value.retestRate).toFixed(1));
                    yieldRate[j] = { name: keys[j], y: ele };
                } else {
                    yieldRate[j] = { name: keys[j], y: 0 };
                }
            }
            Highcharts.chart('chart-group-name', {
                chart: {
                    type: 'line'
                },
                title: {
                    text: dataset.modelName + ' RETEST RATE BY WEEK ALL GROUP NAME',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        fontSize: '18px',
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">'+dataset.modelName+'</span><br>',
                    pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b>'
                },
                plotOptions: {
                    line: {
                        dataLabels: {
                            enabled: true,
                            format: '{point.y:.2f} %',
                            style: {
                                fontWeight: 'normal',
                                fontSize: '9px',
                            }
                        }
                    }
                },
                xAxis: {
                    type: 'category',
                    //tickInterval: Math.ceil(keys.length / 7),
                    labels: {
                        enabled: true,
                        rotation: -40,
                        align: 'right',
                        style: {
                            fontSize: '9px'
                        }
                    }
                },
                yAxis: {
                    title: ' ',
                    plotLines: [{
                        color: 'red',
                        dashStyle: 'longdash',
                        width: 1,
                        zIndex: 2,
                        label: {
                            text: '' + '%',
                            align: 'right',
                            x: 0,
                        }
                    }],
                    minTickInterval: 5,
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
                credits: {
                    enabled: false
                },
                series: [{
                    name: '',
                    data: yieldRate,
                    tooltip: {
                        valueSuffix: '%'
                    },
                }]
            });
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}
var loadDataTimeout;

function loadData(requestData,
    // retestRateChart, graphProps,
    // firstPassRateChart, firstPassRateProps,
    // hitRateChart, hitRateProps,
    hitRateWeeklyChart, hitRateWeeklyProps,
    errorByCodeChart, errorByCodeProps,
    // errorByTimeChart, errorByTimeProps,
    // top1ErrorByWeekChart, top1ErrorByWeekProps,
    // top1ErrorByHourChart, top1ErrorByHourProps,
    retestRateWeeklyChart, graphWeeklyProps,
    firstPassRateWeeklyChart, firstPassRateWeeklyProps) {

    if (loadDataTimeout !== undefined) {
        clearTimeout(loadDataTimeout);
    }

    $.ajax({
        type: "GET",
        url: "/api/test/station/hourly",
        data: {
            factory: requestData.factory,
            modelName: requestData.modelName,
            groupName: requestData.groupName,
            stationName: requestData.stationName,
            timeSpan: requestData.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                // loadDataByTimeOfStationFromJson(retestRateChart, graphProps, data);
                // loadDataFirstPassRateFromJson(firstPassRateChart, firstPassRateProps, data);
                // loadDataHitRateFromJson(hitRateChart, hitRateProps, data);
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });

    $.ajax({
        type: "GET",
        url: "/api/test/station/weekly",
        data: {
            factory: requestData.factory,
            modelName: requestData.modelName,
            groupName: requestData.groupName,
            stationName: requestData.stationName,
            timeSpan: requestData.timeSpan,
            customer: true
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                loadDataByTimeOfStationFromJson(retestRateWeeklyChart, graphWeeklyProps, data);
                loadDataFirstPassRateFromJson(firstPassRateWeeklyChart, firstPassRateWeeklyProps, data);
                loadDataHitRateFromJson(hitRateWeeklyChart, hitRateWeeklyProps, data);
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });

    $.ajax({
        type: "GET",
        url: "/api/test/station/weekly/error",
        data: {
            factory: requestData.factory,
            modelName: requestData.modelName,
            groupName: requestData.groupName,
            stationName: requestData.stationName,
            timeSpan: requestData.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                loadDataByErrorFromJson(errorByCodeChart, errorByCodeProps, data);
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });

    $.ajax({
        type: "GET",
        url: "/api/test/station/hourly/error",
        data: {
            factory: requestData.factory,
            modelName: requestData.modelName,
            groupName: requestData.groupName,
            stationName: requestData.stationName,
            timeSpan: requestData.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            // loadDataByTimeOfErrorFromJson(errorByTimeChart, errorByTimeProps, data);
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });

    $.ajax({
        type: "GET",
        url: "/api/test/station/weekly/error/top1",
        data: {
            factory: requestData.factory,
            modelName: requestData.modelName,
            groupName: requestData.groupName,
            stationName: requestData.stationName,
            timeSpan: requestData.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            // loadDataTop1ErrorByWeekFromJson(top1ErrorByWeekChart, top1ErrorByWeekProps, data);
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });

    $.ajax({
        type: "GET",
        url: "/api/test/station/hourly/error/top1",
        data: {
            factory: requestData.factory,
            modelName: requestData.modelName,
            groupName: requestData.groupName,
            stationName: requestData.stationName,
            timeSpan: requestData.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            // loadDataTop1ErrorByHourFromJson(top1ErrorByHourChart, top1ErrorByHourProps, data);
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });

    $.ajax({
        type: "GET",
        url: "/api/test/station/errorDetail",
        data: {
            factory: requestData.factory,
            modelName: requestData.modelName,
            groupName: requestData.groupName,
            stationName: requestData.stationName,
            timeSpan: requestData.timeSpan,
            customer: true
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            // console.log("data::", data)
            loadDataTableTop3ErrorFromJson(data);
            if (!$.isEmptyObject(data)) {
                var keys = Object.keys(data);
                dataset.trackingId = data[keys[0]].trackingId;
                dataset.errorCode = data[keys[0]].errorCode;
                dataset.errorDesc = data[keys[0]].errorDescription;

                requestData.errorCode = data[keys[0]].errorCode;
                loadDataTableErrorHistoryFromJson(requestData);
            } else {
                dataset.errorCode = '';
                dataset.errorDesc = '';
                $('#tbl-error-history>tbody').html('<tr><td colspan="10" align="center">-- NO DATA HISTORY --</td></tr>');
            }

            loadTroubleAndHistory();
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });


    loadDataTimeout = setTimeout(loadData, 300000,
        requestData,
        // retestRateChart, graphProps,
        // firstPassRateChart, firstPassRateProps,
        // hitRateChart, hitRateProps,
        hitRateWeeklyChart, hitRateWeeklyProps,
        errorByCodeChart, errorByCodeProps,
        // errorByTimeChart, errorByTimeProps,
        // top1ErrorByWeekChart, top1ErrorByWeekProps,
        // top1ErrorByHourChart, top1ErrorByHourProps,
        retestRateWeeklyChart, graphWeeklyProps,
        firstPassRateWeeklyChart, firstPassRateWeeklyProps);
}
// $('input').trigger(
//     jQuery.Event('keydown', { which: 13 })
//   );
$("input").keyup(function (event) {
    if (event.keyCode == 13) {
        $("#input").click();
    }
});