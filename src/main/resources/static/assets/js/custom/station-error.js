var charts = {};
var dataGlo ={
}
function loadProgressStatus(factory, modelName, timeSpan) {
    clearTimeout(dataset.timeout['loadProgressStatus']);

    $.ajax({
        type: "GET",
        url: "/api/test/station/status/count2",
        data: {
            factory: factory,
            modelName: modelName,
            timeSpan: timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                var total = data.LOCKED + data.IDLE + data.WARNING + data.NORMAL;
                var testerLocked = (data.LOCKED * 100 / total).toFixed(2);
                var testerWarning = (data.WARNING * 100 / total).toFixed(2);
                var testerIdle = (data.IDLE * 100 / total).toFixed(2);
                var testerNormal = (data.NORMAL * 100 / total).toFixed(2);

                $('span[name="tester-issue"]').html(data.LOCKED);
                $('span[name="tester-warning"]').html(data.WARNING);
                $('span[name="tester-idle"]').html(data.IDLE);

                var progressBarContent = '<tr>'
                    + '<td class="tester-normal" width="' + testerNormal + '%" title="' + data.NORMAL + '/' + total + '" data-status="all" onclick="showByStatus(this.dataset.status)" >' + testerNormal + '% <span class="pull-right">' + data.NORMAL + '/' + total + '</span></td>'
                    + '<td class="tester-issue" width="' + testerLocked + '%" title="' + data.LOCKED + '/' + total + '" data-status="locked" onclick="showByStatus(this.dataset.status)">' + testerLocked + '% <span class="pull-right">' + data.LOCKED + '/' + total + '</span></td>'
                    + '<td class="tester-warning" width="' + testerWarning + '%" title="' + data.WARNING + '/' + total + '" data-status="warning" onclick="showByStatus(this.dataset.status)">' + testerWarning + '% <span class="pull-right">' + data.WARNING + '/' + total + '</span></td>'
                    + '<td class="tester-idle" width="' + testerIdle + '%" title="' + data.IDLE + '/' + total + '" data-status="idle" onclick="showByStatus(this.dataset.status)">' + testerIdle + '% <span class="pull-right">' + data.IDLE + '/' + total + '</span></td>';
                $('#progress-bar').html(progressBarContent);

                if (testerLocked == 0.00) {
                    $('.tester-issue').addClass('hide')
                }
                if (testerNormal == 0.00) {
                    $('.tester-normal').addClass('hide')
                }
                if (testerWarning == 0.00) {
                    $('.tester-warning').addClass('hide')
                }
                if (testerIdle == 0.00) {
                    $('.tester-idle').addClass('hide')
                }
                if (testerLocked < 10.00) {
                    $('.tester-issue').html(" ");
                }
                if (testerNormal < 10.00) {
                    $('.tester-normal').html(" ");
                }
                if (testerWarning < 10.00) {
                    $('.tester-warning').html(" ");
                }
                if (testerIdle < 10.00) {
                    $('.tester-idle').html(" ");
                }

                delete progressBarContent;
                delete total;
                delete testerLocked;
                delete testerWarning;
                delete testerIdle;
                delete testerNormal;
            }
            delete this.data;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
        complete: function () {
            if (timeSpan != null && timeSpan != '') {
                var time = timeSpan.split(' - ');
                if (time.length = 2) {
                    var start = moment(time[0], "YYYY/MM/DD HH:mm");
                    var end = moment(time[1], "YYYY/MM/DD HH:mm");
                    var now = moment();
                    if (start > now || end < now) {
                        return
                    }
                }
            }
            dataset.timeout['loadProgressStatus'] = setTimeout(loadProgressStatus, 180000, factory, modelName, timeSpan);
        }
    });
}

function loadStatus(factory, timeSpan) {
    clearTimeout(dataset.timeout['loadStatus']);

    $.ajax({
        type: "GET",
        url: "/api/test/station/status2",
        data: {
            factory: factory,
            timeSpan: timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            for (i in charts) {
                charts[i].destroy();
            }
            charts = {};
            if (!$.isEmptyObject(data)) {
                var arrModelNames = [];
                var countHighchartsID = 0;

                countHighchartsID = loadStationStatus(timeSpan, 'locked', '#station-locked', data.LOCKED.data, arrModelNames, countHighchartsID);

                countHighchartsID = loadStationStatus(timeSpan, 'warning', '#station-warning', data.WARNING.data, arrModelNames, countHighchartsID);

                countHighchartsID = loadStationStatus(timeSpan, 'idle', '#station-idle', data.IDLE.data, arrModelNames, countHighchartsID);

                countHighchartsID = loadStationStatus(timeSpan, 'normal', '#station-normal', data.NORMAL.data, arrModelNames, countHighchartsID);

                $('input[name=model-name][value="' + dataset.selectedModel + '"]').parent('label').addClass('active');
                $('input[name=model-name][value="' + dataset.selectedModel + '"]').attr("checked", "checked");
                show();

                delete arrModelNames;
                delete countHighchartsID;
            }
            delete this.data;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
        complete: function () {
            if (timeSpan != null && timeSpan != '') {
                var time = timeSpan.split(' - ');
                if (time.length = 2) {
                    var start = moment(time[0], "YYYY/MM/DD HH:mm");
                    var end = moment(time[1], "YYYY/MM/DD HH:mm");
                    var now = moment();
                    if (start > now || end < now) {
                        return
                    }
                }
            }
            dataset.timeout['loadStatus'] = setTimeout(loadStatus, 180000, factory, timeSpan);
        }
    });
}

function loadStationStatus(timeSpan, type, dataId, dataLocked, arrModelNames, countHighchartsID) {
    $(dataId + ' .cldelsttlock').remove();
    $(dataId + ' .noDataStation').remove();

    if (!$.isEmptyObject(dataLocked)) {
        var stationLContent = '';
        var modelsHtml = '';
        for (i = 0; i < dataLocked.length; i++) {
            if ($.inArray(dataLocked[i].modelName, arrModelNames) == -1) {
                //                $('.mds').append('<label class="btn btn-default modelSort" model="'+dataLocked[i].modelName+'" '+type+'>' +
                //                                     '<a class="modelName" style=" font-size:11px;">'+dataLocked[i].modelName+'</a>' +
                //                                     '<input class="control-primary hidden" type="radio" name="model-name" value="'+dataLocked[i].modelName+'">' +
                //                                 '</label>');
                //
                //                arrModelNames.push(dataLocked[i].modelName);
                $('.modelSort[model="' + dataLocked[i].modelName + '"]').attr(type, "");
            } else {
                $('.modelSort[model="' + dataLocked[i].modelName + '"]').attr(type, "");
            }

            var timeLock = dataLocked[i].notifyTime;
            stationLContent += '<div class="col-xs-6 col-sm-3 col-lg-2 card cldelsttlock countStation' + countHighchartsID + '" model="' + dataLocked[i].modelName + '" ' + type + '><input class="modelNameHide hide" type="radio" value="' + dataLocked[i].modelName + '">'
                + '<div class="panel stt" data-type="station" data-id="' + dataLocked[i].id + '" data-factory="' + dataLocked[i].factory + '" data-model-name="' + dataLocked[i].modelName + '" data-group-name="' + dataLocked[i].groupName + '" data-station-name="' + dataLocked[i].stationName + '" data-status="' + dataLocked[i].status + '">'
                + '<div class="pnlTitle">'
                + '<label class="stationName" style="margin: 0px; max-height: 50px;"><a onclick="showPanelDetail();stationClick(this.parentElement.parentElement.parentElement);"><b>' + dataLocked[i].stationName + '</b></a></label>'
                + (type == 'locked' ? ('<span class="lockType" title="Error times in row">' + (dataLocked[i].status == 'LOCKED A' || dataLocked[i].status == 'ISSUE A' ? 'A' : 'B') + '</span>') : '')
                + (timeLock != null ? ('<label class="timeLock">' + timeLock.slice(11, 19) + '</label>') : '')
                + '<label class="rated"><a style="color:#333" title="Detail" onclick="moveToDetailPage(this.parentElement.parentElement.parentElement)"><h3 class="text-semibold">' + dataLocked[i].retestRate.toFixed(2) + '%</h3></a></label>'
                + '<label class="pnlModel"><a style="color:#333" title="Model" ><h6 class="text-semibold">' + dataLocked[i].modelName + '(' + dataLocked[i].totalOutput + '/' + dataLocked[i].totalPlan + ')' + '</h6></a></label></div>'
                + '<div class="panel panel-flat panel-body" id="chart-content' + countHighchartsID + '" style="height: 80px; margin:0px; padding: 0px;"></div>'
                + '<div class="passwip">First Pass/Input: <span class="pull-right">' + ((dataLocked[i].wip - dataLocked[i].firstFail) < 0 ? 0 : (dataLocked[i].wip - dataLocked[i].firstFail)) + '/' + dataLocked[i].wip + '</span></div>'
                + '<div class="label_notify">Notify: <a class="pull-right ' + dataLocked[i].status + '" data-toggle="modal" data-target="#modal-history-action" onclick="notifyOnclick(this)">' + dataLocked[i].notifyShiftIndexes.length + '</a></div></div></div>';

            var idChart = 'chart-content' + countHighchartsID;

            countHighchartsID++;
            var requestData = {
                factory: dataLocked[i].factory,
                stationName: dataLocked[i].stationName,
                groupName: dataLocked[i].groupName,
                modelName: dataLocked[i].modelName,
                timeSpan: timeSpan
            };

            var colorChart = getColorChart(type, dataLocked[i].retestRate);
            showRate(idChart, requestData, colorChart.colorChart, colorChart.lineColorChart, dataLocked[i].notifyShiftIndexes);

            delete timeLock;
            delete idChart;
            delete requestData;
            delete color;
        }

        $(dataId).append(stationLContent);

        delete stationLContent;
        delete modelsHtml;

    }
    //    else if($.isEmptyObject(dataLocked)){
    //        $(dataId).html("<span class='noDataStation'>No Station</span>");
    //    }ALL
    return countHighchartsID;
}

function getColorChart(type, retestRate) {
    if (type == "idle") {
        return { colorChart: '#e0e0e0', lineColorChart: '#d3d3d3' };
    }
    if (retestRate >= 10) {
        return { colorChart: '#e6717c', lineColorChart: '#dc3545' };
    }
    else if (retestRate >= 3 && retestRate < 10) {
        return { colorChart: '#ffda6a', lineColorChart: '#ffca2b' };
    }
    else {
        return { colorChart: '#c4d4a8', lineColorChart: '#acc284' };
    }
}

function showByStatus(status) {
    if (dataset.status == status) {
        dataset.status = 'all';
    } else {
        dataset.status = status;
    }
    show();
}

function showByModel() {
    var selectedModel = $('input[name=model-name]:checked').val();
    if (dataset.selectedModel != selectedModel) {
        dataset.selectedModel = selectedModel;
        if (dataset.selectedModel == "all") {
            loadProgressStatus(dataset.factory, '', dataset.timeSpan);
        } else {
            loadProgressStatus(dataset.factory, dataset.selectedModel, dataset.timeSpan);
        }
        show();
    }

    delete selectedModel;
}

function show() {
    if (dataset.selectedModel == 'all') {
        if (dataset.status == 'all') {
            $('.cldelsttlock').css("display", "block");
            $('.modelSort').css("display", "block");
        } else {
            $('.cldelsttlock').css("display", "none");
            $('.modelSort').css("display", "none");

            $('.cldelsttlock[' + dataset.status + ']').css("display", "block");
            $('.modelSort[' + dataset.status + ']').css("display", "block");
        }
    } else {
        if (dataset.status == 'all') {
            $('.cldelsttlock').css("display", "none");

            $('.cldelsttlock[model="' + dataset.selectedModel + '"]').css("display", "block");
            $('.modelSort').css("display", "block");
        } else {
            $('.cldelsttlock').css("display", "none");
            $('.modelSort').css("display", "none");

            $('.cldelsttlock[model="' + dataset.selectedModel + '"][' + dataset.status + ']').css("display", "block");
            $('.modelSort[' + dataset.status + ']').css("display", "block");
        }
    }
}

//function selectedModel(context){
//    $('.modelSort').removeClass('selectedmd');
//    $(context).addClass('selectedmd');
//    dataset.selectedModel = $('input[name=model-name]:checked').val();
//}

//function sortByModel(){
//    var t = $('input[name=model-name]');
//    var model_name = dataset.selectedModel;
//    var count_model = $('.modelNameHide');
//    for(var i=0; i< count_model.length; i++){
//        if(model_name == "all"){
//            $('.countStation'+i+'').css("display","block");
//        }
//        else if(model_name == count_model[i].value){
//            $('.countStation'+i+'').css("display","block");
//        }
//        else{
//            $('.countStation'+i+'').css("display","none");
//        }
//    }
//    if (model_name == "all") {
//        loadProgressStatus(dataset.factory, '', dataset.timeSpan);
//    } else {
//        loadProgressStatus(dataset.factory, model_name, dataset.timeSpan);
//    }
//}

function showRate(idChart, requestData, colorChart, lineColorChart, notifyIndexes) {
    $.ajax({
        type: "GET",
        url: "/api/test/station/hourly",
        data: requestData,
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                var categories = [];
                var dataChart = [];
                $.each(data, function (key, value) {
                    var timeRate = key;
                    categories.push(timeRate);
                    var dataRate;
                    if (value != null) {
                        dataRate = value.retestRate;
                    }
                    else {
                        dataRate = 0;
                    }
                    dataChart.push({ name: key, y: dataRate });
                });
                drawRChart(idChart, categories, dataChart, colorChart, lineColorChart, notifyIndexes);

                delete categories;
                delete dataChart;
            }
            delete this.data;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}

function drawRChart(idChart, categories, dataChart, colorChart, lineColorChart, notifyIndexes) {
    var plotLines = [];
    for (i in notifyIndexes) {
        plotLines[i] = {
            value: notifyIndexes[i],
            color: 'red',
            dashStyle: 'longdash',
            width: 1,
            zIndex: 10
        }
    }

//    if (charts[idChart] != undefined) {
//        charts[idChart].update({
//            series: [{
//                data: dataChart,
//                lineColor: lineColorChart,
//                color: colorChart
//            }],
//            xAxis: {
//                plotLines: plotLines
//            }
//        });
//    } else {
        charts[idChart] = Highcharts.chart(idChart, {
            chart: {
                type: 'areaspline',
                margin: [0, 0, 0, 0],
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
            },
            title: {
                text: ''
            },
            xAxis: {
                labels: {
                    enabled: false
                },
                gridLineWidth: 0,
                minorGridLineWidth: 0,
                tickLength: 0,
                tickWidth: 0,
                plotLines: plotLines,
            },
            yAxis: {
                visible: false,
//                min: -5,
//                softMax: 105,
            },
            legend: {
                enabled: false
            },
            series: [{
                name: ' ',
                data: dataChart,
                lineColor: lineColorChart,
                color: colorChart,
                fillOpacity: 10,
                marker: {
                    enabled: false
                },
                threshold: null
            }],
            tooltip: {
                pointFormat: '<span style="color:{series.color}">●</span> <b>{point.y:.2f}%</b>'
            },
            plotOptions: {
                series: {
                    animation: false
                }
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

//    }

    delete plotLines;
}

function stationClick(context) {
    dataset.factory = context.dataset.factory;
    dataset.modelName = context.dataset.modelName;
    dataset.groupName = context.dataset.groupName;
    dataset.stationName = context.dataset.stationName;

    loadModal(context.dataset.status);
}

function loadModal(status) {
    $('h5[name="station-name"]').html(dataset.stationName + " | " + dataset.groupName + " | " + dataset.modelName);
    if (status == "ISSUE A" || status == "ISSUE B" || status == "LOCKED A" || status == "LOCKED B") {
        $('.panel-heading').css("background-color", "#e6717c")
    } else if (status == "WARNING" || status == "ISSUE W") {
        $('.panel-heading').css("background-color", "#ffda6a")
    } else if (status == "IDLE") {
        $('.panel-heading').css("background-color", "#e6e6e6")
    } else {
        $('.panel-heading').css("background-color", "#fff")
    }

    $.ajax({
        type: "GET",
        url: "/api/test/station/detail",
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            groupName: dataset.groupName,
            stationName: dataset.stationName,
            timeSpan: dataset.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                $('label[name="retest-rate"]').html(data.retestRate.toFixed(2) + "%");
                $('label[name="first-pass-rate"]').html(data.firstPassRate.toFixed(2) + "%");
                $('label[name="hit-rate"]').html(data.hitRate.toFixed(2) + "%");
                $('.downtime').html(data.downTime);
                $('label[name="wip"]').html(data.wip);
                $('label[name="first-fail"]').html(data.firstFail);
                $('label[name="second-fail"]').html(data.secondFail);

                var html = '';
                if (!$.isEmptyObject(data.errorMetaMap)) {
                    $.each(data.errorMetaMap, function (key, value) {
                        html += '<label class="btn btn-sm bg-gray" data-popup="tooltip-custom" data-placement="bottom" title="' + value.errorDescription + '" data-tracking-id="' + (data.trackingId != null ? data.trackingId : '') + '" data-model-name="' + data.modelName + '" data-error-code="' + key + '" data-error-desc="' + value.errorDescription + '" onclick="showPanelRight();showTroubleAndHistory(this);" ><input type="radio" name="error-code">' + key + '</label>';
                    });
                }
                $('.btn-group[name="btn-group-error-code"]').html(html);

                var modalFooter = '<div class="row" align="center" data-type="station" data-id="' + data.id + '" data-factory="' + data.factory + '" data-model-name="' + data.modelName + '" data-group-name="' + data.groupName + '" data-station-name="' + data.stationName + '">'
                    + ' <button class="btn btnAction" data-href="10.224.76.108:554/Streaming/Channels/1" onclick="openVLC(this.dataset.href);">CAMVIEW</button> '
                    + ' <button class="btn btnAction" onclick="moveToDetailPage(this.parentElement)">DETAIL</button> '
                    + ' <button class="btn btnAction" data-href="' + (data.ip != null ? ('vnc://' + data.ip) : '#') + '" onclick="hrefWindow(this.dataset.href);">VNC</button></button>';
                $('.modalDetail').html(modalFooter);

                $('[data-popup=tooltip-custom]').tooltip({
                    template: '<div class="tooltip"><div><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div></div>'
                });

                delete html;
                delete modalFooter;
            }
            delete this.data;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}

function showTroubleAndHistory(context) {
    dataset.trackingId = context.dataset.trackingId;
    dataset.errorCode = context.dataset.errorCode;
    dataset.errorDesc = context.dataset.errorDesc;

    loadTroubleAndHistory();
}
// $(".panel-detail").draggable({
//     handle: ".panel-heading"
// });
$("#modalErrorCode1").on('hide.bs.modal',function(){
    $("#modalErrorModel").css('opacity','1');
 });

function showModalErrorModel(modelNameError) {
    dataGlo.modelName = modelNameError;
    dataGlo.groupName = ''
    dataGlo.stationName = '';
    functionSelectvalueMN1();
    loadTable1();
   
}
function loadTable1(){
    $.ajax({
        type: "GET",
        url: "/api/test/station/error/detail",
        data: {
            factory: dataset.factory,
            modelName: dataGlo.modelName,
            groupName: dataGlo.groupName,
            stationName: dataGlo.stationName
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log("data error: ", data);
            var html='';
            var number = 0;
                for(i in data){
                     // console.log("data modal iiiii: ", data[i].modelName);
                     var valueDescription = data[i].errorDescription;
                     var valueStationName = data[i].stationName;
                     if(valueDescription== null  ){
                         valueDescription = '';
                     }
                     if( valueStationName == null ){
                        valueStationName = '';
                     }
                  number++;
                  html += '<tr id="row"><td>'+ number +'</td>'
                      + '<td>'+data[i].modelName+'</td>'
                      // + '<td class="text-semibold" ><a class="errorCode">'+data[i].errorCode+'</a></td>'
                      + '<td class="text-semibold" data-toggle="modal" data-target="#modalErrorCode1" onclick="showModalErrorCode1(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ data[i].errorCode+ '\')" title="Click to show Error Code"><a class="class-error-code" >'+data[i].errorCode+'</a></td>'
                      + '<td>'+data[i].groupName+'</td>'
                      + '<td>'+valueStationName+'</td>'
                      + '<td>'+valueDescription+'</td>'
                      + '<td>'+data[i].wip+'</td>'
                      + '<td>'+data[i].pass+'</td>'
                      + '<td>'+ data[i].testFail+'</td>'
                      + '<td>'+data[i].fail+'</td>'
                      + '<td>'+(data[i].fail * 100 /data[i].wip).toFixed(2) +' %</td>'
                      + '</tr>'; 
                }

                 $("#tblModelErrorModel tbody").html(html);
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}
function functionSelectvalueMN1(){
    functionSelectvalueMN11();
    functionSelectvalueMN12();
    functionSelectvalueMN13();
}
function functionSelectvalueMN11(){
    $.ajax({
        type: "GET",
        url: "/api/test/model",
        data: {
            factory: dataset.factory,
            // modelName: dataset.modelName,
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var arrayModel = [];
            var html = '<option value="" disabled="" selected>Select Model</option>';
            var selector = $('#idSelectMN1');
            selector.children('option').remove();   
            for(i in data){
                if(arrayModel.indexOf(data[i].modelName) == -1){ 
                    arrayModel.push(data[i].modelName);
                }
            }
            for(j in arrayModel){
                if(arrayModel[j] == dataGlo.modelName){
                    html += '<option class="classvalueMN1" value="' + arrayModel[j] + '" selected>' + arrayModel[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                } else{
                    html += '<option class="classvalueMN1" value="' + arrayModel[j] + '">' + arrayModel[j] + '</option>'; 
                    
                }
            }
            $('#idSelectMN1').html(html);
            // selector.html(html);
            selector.selectpicker('refresh');
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
    });
}
function functionSelectvalueMN12(){
    $.ajax({
        type: "GET",
        url: "/api/test/group",
        data: {
            factory: dataset.factory,
            modelName: dataGlo.modelName,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var arrayGroup = [];
            var selector = $('#idSelectGN1');
            selector.children('option').remove();
            var html = '<option class="classvalueGN1" value="">All</option>';
            if (dataGlo.groupName == '') {
                $('.SelecthtmlSN').addClass('hidden');
            } else{
                $('.SelecthtmlSN').removeClass('hidden');
            }
            for(i in data){
                if(arrayGroup.indexOf(data[i].groupName) == -1){ 
                    arrayGroup.push(data[i].groupName);
                }
            }
            for(j in arrayGroup){
                if(arrayGroup[j] == dataGlo.groupName){
                    html += '<option class="classvalueGN1" value="' + arrayGroup[j] + '" selected>' + arrayGroup[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                }else{
                    html += '<option class="classvalueGN1" value="' + arrayGroup[j] + '">' + arrayGroup[j] + '</option>';
                }
            }
            $('#idSelectGN1').html(html);
            // selector.html(html);
            selector.selectpicker('refresh');
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
function functionSelectvalueMN13(){
    $.ajax({
        type: "GET",
        url: "/api/test/station",
        data: {
            factory: dataset.factory,
            modelName: dataGlo.modelName,
            groupName: dataGlo.groupName,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var arrayStation = [];
            var selector = $('#idSelectSN1');
            selector.children('option').remove(); 
            var html ='<option class="classvalueSN1" value="">All</option>';
            for(i in data){
                if((arrayStation.indexOf(data[i].stationName) == -1) && data[i].stationName != null ){ 
                    arrayStation.push(data[i].stationName);
                }
            }
            for(j in arrayStation){
                if(arrayStation[j] == dataGlo.stationName){
                    html += '<option class="classvalueSN1" value="' + arrayStation[j] + '" selected>' + arrayStation[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                }else{
                    html += '<option class="classvalueSN1" value="' + arrayStation[j] + '">' + arrayStation[j] + '</option>'; 
                }
            }
            $('#idSelectSN1').html(html);
            // $("#idSelectSN").val(options);
            // selector.html(html);
            selector.selectpicker('refresh');
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
$('select[name=modelName]').on('change', function () {
    dataGlo.modelName = this.value;
    dataGlo.groupName = "";
    dataGlo.stationName = "";
    functionSelectvalueMN12();
    functionSelectvalueMN13();
    loadTable1();
});
$('select[name=groupName]').on('change', function () {
    dataGlo.groupName = this.value;
    if( $('#idSelectGN1').val()=='' ||  $('#idSelectGN1').val()== null){
        $('.SelecthtmlSN').addClass('hidden');
        dataGlo.groupName = "";
        dataGlo.stationName = "";
        functionSelectvalueMN13();
        loadTable1();

    } else{
        $('.SelecthtmlSN').removeClass('hidden');
        dataGlo.groupName = this.value;
        dataGlo.stationName = "";  
        functionSelectvalueMN13();
        loadTable1();
    }

});
$('select[name=stationName]').on('change', function () {
    dataGlo.stationName = this.value;
    if($('#idSelectSN1').val() == ''){
        dataGlo.stationName = '';
        loadTable1();
    } else{
        dataGlo.stationName = this.value;
        loadTable1();
    }
});
function changeStyle() {
    if (dataset.style != 'new-style') {
        dataset.style = 'new-style';
        $('#old-style').addClass("hidden");
        $('#new-style').removeClass("hidden");
    } else {
        dataset.style = 'old-style';
        $('#new-style').addClass("hidden");
        $('#old-style').removeClass("hidden");
    }
}

function showModal(model,type) {
    $("#mydiv").addClass("show");
    var typeParam = '';
    if (type == 1) { // Type=1 : Get Daily
        typeParam = 'DAILY';
        $('#mydivheader span').html('SET PLAN');
    } else if (type == 2) { // Type=2 : Get Monthly
        typeParam = 'MONTHLY';
        $('#mydivheader span').html('SET PLAN MONTH');
    }
    dataset.typePlan = type;
    $('#modelAddPlan').html(model);
    $.ajax({
        type: "GET",
        url: "/api/test/model/plan",
        data: {
            factory: dataset.factory,
            modelName: model,
            type: typeParam,
            timeSpan: dataset.timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var html = '';
            if (data) {
                for (i in data) {
                    html += '<tr id="row'+data[i].id+'">'
                    + '<td><div id="txtEditModel'+data[i].id+'">' + data[i].modelName + '</div></td>'
                    + '<td><input id="txtEditPlan'+data[i].id+'" class="form-control hidden" name="plan" type="number" value="' + data[i].plan + '" /><span id="plan'+data[i].id+'">' + data[i].plan + '</span></td>'
                    + '<td><input id="txtEditMO'+data[i].id+'" class="form-control hidden" name="mo" type="text" value="' + data[i].mo + '" /><span id="mo'+data[i].id+'">' + data[i].mo + '</span></td>'
                    + '<td>'
                    // + '<input id="txtEditShift'+data[i].id+'" class="form-control hidden" name="shift" type="text" value="' + data[i].shift + '" /><span id="shift'+data[i].id+'">' + data[i].shift + '</span>'
                    + '<select id="txtEditShift'+data[i].id+'" class="form-control hidden" name="shift" value="'+data[i].shift+'"><option value="DAY">DAY</option><option value="NIGHT">NIGHT</option></select><span id="shift'+data[i].id+'">' + data[i].shift + '</span>'
                    // +'<div id="txtEditShift'+data[i].id+'">' + data[i].shift + '</div>'
                    +'</td>'
                    + '<td>'
                    + '<input id="txtEditShiftTime'+data[i].id+'" class="form-control hidden daterange-single" name="shiftTime" type="text" value="' + data[i].shiftTime + '" /><span id="shiftTime'+data[i].id+'">' + data[i].shiftTime + '</span>'
                    // + '<div id="txtEditShiftTime'+data[i].id+'">' + data[i].shiftTime + '</div>'
                    + '</td>'
                    + '<td>'
                    + '<button class="btn btn-warning" id="editRow'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+data[i].id+')">'
                    + '<i class="icon icon-pencil"></i>'
                    + '</button>'
                    + '<button class="btn btn-success hidden" id="confirmRow'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmPlan(\'' + data[i].modelName + '\', 2, '+data[i].id+')">'
                    + '<i class="icon icon-checkmark"></i>'
                    + '</button><button class="btn btn-danger hidden" id="cancelRow'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+data[i].id+')">'
                    + '<i class="icon icon-spinner11"></i>'
                    + '</button>'
                    + '</td>'
                    + '</tr>';
                }

                $("#tblTarget tbody").html(html);

            } else {
                html = '<tr id="rowAdd">'
                    + '<td><div id="txtEditModelNameAdd">' + model + '</div></td>'
                    + '<td><input id="txtAddPlan" class="form-control" name="plan" type="number"/></td>'
                    + '<td><input id="txtAddMO" class="form-control" name="mo" type="text"/></td>'
                    // + '<td><input id="txtAddShift" class="form-control" name="shift" type="text"/></td>'
                    + '<td><select id="txtAddShift" class="form-control" name="shift"><option value="DAY">DAY</option><option value="NIGHT">NIGHT</option></select></td>'
                    + '<td><input id="txtAddShiftTime" class="form-control daterange-single" name="shiftTime" type="text" /></td>'
                    + '<td>'
                    + '<button class="btn btn-success " id="addPlan" title="Add" style="padding: 4px 10px;" onclick="AddNewPlan(\'' + model + '\', '+type+')">'
                    + '<i class="icon icon-checkmark"></i>'
                    + '</button>'
                    + '<button class="btn btn-danger " id="cancelAdd" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="closeModal()">'
                    + '<i class="icon icon-spinner11"></i>'
                    + '</button>'
                    + '</td>'
                    + '</tr>';
                $("#tblTarget tbody").html(html);
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
            delete html;
            delete this.data;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
    
}
function loadModelList() {
    clearTimeout(dataset.timeout['loadModelList']);
    $('.loader').css('display', 'block');
    $.ajax({
        type: "GET",
        url: "/api/test/model/daily",
        data: {
            factory: dataset.factory,
            customerName: dataset.customerName,
            timeSpan: dataset.timeSpan,
            moType: dataset.moType,
            includeStation: false
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            //console.log("model/daily:", data);
            if (!$.isEmptyObject(data)) {
                var html = '<label class="btn modelSort bg-gray" locked warning idle style="margin: 0px 1px 1px 0px;" >' +
                    '<input class="control-primary hidden" type="radio" name="model-name" value="all" > ALL' +
                    '</label>';

                var htmlTbl = '';
                var totalPlan = 0;
                var totalPlanMonth = 0;
                var totalFail = 0;
                var totalRFail = 0;
                var totalFFail = 0;
                var totalRetest = 0;
                var totalInput = 0;
                var totalOutPut = 0;
                var totalWeeklyOutPut = 0;
                for (i in data) {
                    var value = '<div><b>Output/Plan: </b><span>' + data[i].output + '/' + data[i].plan + '</span></div>' +
                        '<div><b>First Pass Rate: </b><span>' + data[i].firstPassRate.toFixed(2) + '%</span></div>' +
                        '<div><b>Retest Rate: </b><span>' + data[i].retestRate.toFixed(2) + '%</span></div>';

                    for (j in data[i].groupList) {
                        value += '<div><b>Retest Rate - ' + data[i].groupList[j].groupName + ': </b><span>' + data[i].groupList[j].retestRate.toFixed(2) + ' %</span></div>';
                        // dataGlo.groupName = data[i].groupList[j].groupName;
                    }

                    html += '<label class="btn modelSort ' + convertStatus((100 - data[i].retestRate), 90, 97) + '" model="' + data[i].modelName + '" data-popup="popover" data-trigger="hover" data-placement="bottom" data-html="true" data-content="' + value + '" style="margin: 0px 1px 1px 0px;">' +
                        '<input class="control-primary hidden" type="radio" name="model-name" value="' + data[i].modelName + '">' + data[i].modelName +
                        //'<span class="badge bg-warning-400" style="position: absolute; top: -13px; right: 0; border-radius: 0px">'+data[i].retestRate.toFixed(2)+'</span>' +
                        '</label>';
                    var rtr = data[i].retestRate;
                    var fpr = data[i].firstPassRate;
                    var ete = data[i].ete/100;
                    if(data[i].groupList != undefined){
                        if (data[i].groupList.length > 1) {
                            ete = data[i].groupList[0].yieldRate/100;
                            for (let r = 1; r < data[i].groupList.length; r++) {
                                ete *= (data[i].groupList[r].yieldRate/100);
                            }
                        }
                    }
                    ete *= 100;
                    var ffail = data[i].firstFail;
                    var sfail = data[i].secondFail;

                    if (rtr < 0) {
                        rtr = 0;
                        sfail = ffail;
                        fpr = (data[i].wip - ffail) * 100.0 / data[i].wip;
                    }

                    htmlTbl += '<tr class="row-model" data-model="' + data[i].modelName + '">' +
                        '<td>' + (Number(i) + 1) + '</td>' +
                        '<td>' + data[i].customer + '</td>' +
                        '<td class="text-semibold" ><a class="model-name" data-model="' + data[i].modelName + '" >' + data[i].modelName + '</a></td>' +
                        '<td class="text-semibold" onclick="showModal(\'' + data[i].modelName + '\',1)" title="Click to Set Plan"><a class="model-plan" data-model="' + data[i].plan + '">' + data[i].plan + '</a></td>' +
                        '<td class="text-semibold" onclick="showModal(\'' + data[i].modelName + '\',2)" title="Click to Set Plan Month"><a class="model-plan" data-model="' + data[i].planMonth + '">' + data[i].planMonth + '</a></td>' +
                        '<td>' + data[i].wip + '</td>' +
                        '<td>' + data[i].output + '</td>' +
                        '<td>' + data[i].outputWeekly + '</td>' +
                        '<td>' + data[i].hitRate.toFixed(2) + '%</td>' +
                        '<td>' + data[i].hitRateMonth.toFixed(2) + '%</td>' +
                        '<td class=" styletextshad ' + convertStatus(data[i].ete, 95, 98) + '" title="Click to move page E.T.E Detail"><a class="class-model-name" data-model="' + data[i].modelName + '" data-type="ete" >' + ete.toFixed(2) + '%</a></td>' +
                        // '<td class=" styletextshad ' + convertStatus(fpr, 90, 97) + '" title="Click to move page Station Detail"><a class="class-model-name" data-model="' + data[i].modelName + '" data-type="fpr" >' + fpr.toFixed(2) + '%</a></td>' +
                        '<td class=" styletextshad ' + convertStatus(fpr, 90, 97) + '" title="Click to move page Station Detail"><a class="class-model-name" data-model="' + data[i].modelName + '" data-type="rtr" >' + fpr.toFixed(2) + '%</a></td>' +
                        '<td class=" styletextshad ' + convertStatus((100 - rtr), 100 - data[i].retestRateTarget, 100 - data[i].retestRateTarget / 2) + '" title="Click to move page Retest Rate Detail"><a class="class-model-name" data-model="' + data[i].modelName + '" data-type="rtr" >' + rtr.toFixed(2) + '%</a></td>' +
                        '<td>' + ffail + '</td>' +
                        '<td>' + sfail + '</td>'+
                        '</tr>';

                    totalPlan += data[i].plan;
                    totalPlanMonth += data[i].planMonth;

                    totalFail += data[i].secondFail;
                    //totalRFail += data[i].secondFail;
                    totalFFail += data[i].firstFail;
                    //totalRetest += data[i].retestNew;
                    totalInput += data[i].wip;
                    totalOutPut += data[i].output;
                    totalWeeklyOutPut += data[i].outputWeekly;

                }
                if(totalPlan==0){
                    var totalHitRate = (0 * 100.0).toFixed(2);
                    var totalHitRateMonth = (0 * 100.0).toFixed(2);
                } else{
                    var totalHitRate = ((totalOutPut / totalPlan) * 100).toFixed(2);
                    var totalHitRateMonth = ((totalWeeklyOutPut / totalPlanMonth) * 100).toFixed(2);
                }
                if(totalPlanMonth==0){
                    var totalHitRateMonth = (0 * 100.0).toFixed(2);
                } else{
                    var totalHitRateMonth = ((totalWeeklyOutPut / totalPlanMonth) * 100).toFixed(2);
                }
                if(totalPlanMonth==0){
                    var totalHitRateMonth = (0 * 100.0).toFixed(2);
                } else{
                    var totalHitRateMonth = ((totalWeeklyOutPut / totalPlanMonth) * 100).toFixed(2);
                }
                if(totalPlanMonth==0){
                    var totalHitRateMonth = (0 * 100.0).toFixed(2);
                } else{
                    var totalHitRateMonth = ((totalWeeklyOutPut / totalPlanMonth) * 100).toFixed(2);
                }
                // var totalHitRate = ((totalWeeklyOutPut / totalPlan) * 100).toFixed(2);
                var htmlTfoot = '';
                htmlTfoot += '<tr>' +
                    '<td></td>' +
                    '<td></td>' +
                    '<td class="text-semibold" >TOTAL</td>' +
                    '<td>' + totalPlan + '</td>' +
                    '<td>' + totalPlanMonth + '</td>' +
                    '<td>' + totalInput + '</td>' +
                    '<td>' + totalOutPut + '</td>' +
                    '<td>' + totalWeeklyOutPut + '</td>' +
                    '<td>' + totalHitRate + '%</td>' +
                    '<td>' + totalHitRateMonth + '%</td>' +
                    '<td></td>' +
                    '<td></td>' +
                    '<td></td>' +
                    '<td>' + totalFFail + '</td>' +
                    '<td>' + totalFail + '</td>' +
                    '</tr>';

                $('.mds').html(html);
                $('#tbl-model-status>tbody').html(htmlTbl);
                $('#tbl-model-status>tfoot').html(htmlTfoot);
                $('[data-popup=popover]').popover({
                    template: '<div class="popover" style="width: 250px; background-color: #e0e0e0; border: 1px solid #4e7af3; z-index-: 100"><div class="arrow"></div><div class="popover-content"></div></div>'
                });
                $('.model-name').on('click', function (event) {
                    var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + this.dataset.model + "&timeSpan="+dataset.timeSpan;
                    openWindow(url);
                });

                $('.class-model-name').on('click', function (event) {
                    var url = '';
                    if(this.dataset.type == 'fpr')
                        url = "/add-station-detail?factory=" + dataset.factory + "&modelName=" + this.dataset.model + "&timeSpan=" + dataset.timeSpan;
                    else if (this.dataset.type == 'ete')
                        url = "/ete-detail?factory=" + dataset.factory + "&modelName=" + this.dataset.model+ "&timeSpan=" + dataset.timeSpan;
                    else url = "/rtr-detail?factory=" + dataset.factory + "&modelName=" + this.dataset.model+ "&timeSpan=" + dataset.timeSpan;
                    // openWindow(url);
                    window.location.href = url;
                });
                delete html;
                delete htmlTbl;
            } else {
                $('.mds').html('<label class="btn modelSort bg-gray" locked warning idle style="margin: 0px 1px 1px 0px;" >' +
                    '<input class="control-primary hidden" type="radio" name="model-name" value="all" > ALL' +
                    '</label>');
                $('#tbl-model-status>tbody').html('');
                $('#tbl-model-status>tfoot').html('');
                alert('No data!!');
            }
            delete this.data;
        },
        failure: function (errMsg) {
            // $('.loader').css('display', 'none');
            console.log(errMsg);
        },
        complete: function () {
            $('.loader').css('display', 'none');
            if (dataset.timeSpan != null && dataset.timeSpan != '') {
                var time = dataset.timeSpan.split(' - ');
                if (time.length = 2) {
                    var start = moment(time[0], "YYYY/MM/DD HH:mm");
                    var end = moment(time[1], "YYYY/MM/DD HH:mm");
                    var now = moment();
                    if (start > now || end < now) {
                        return
                    }
                }
            }
            dataset.timeout['loadModelList'] = setTimeout(loadModelList, 180000);
        }
    });
}


function addNewPlanClick() {
    $('#btnAddNewPlan').attr('disabled', 'disabled');
    var model = $('#modelAddPlan').html();
    var html = '<tr id="rowAdd">'
        + '<td><div id="txtEditModelNameAdd">' + model + '</div></td>'
        + '<td><input id="txtAddPlan" class="form-control" name="plan" type="number"/></td>'
        + '<td><input id="txtAddMO" class="form-control" name="mo" type="text"/></td>'
        + '<td>'
        // + '<input id="txtAddShift" class="form-control" name="shift" type="text" value="" />'
        + '<select id="txtAddShift" class="form-control" name="shift"><option value="DAY">DAY</option><option value="NIGHT">NIGHT</option></select>'
        + '</td>'
        + '<td><input id="txtAddShiftTime" class="form-control daterange-single" name="shiftTime" type="text" value="" /></td>'
        + '<td>'
        + '<button class="btn btn-success " id="addPlan" title="Add" style="padding: 4px 10px;" onclick="AddNewPlan(\'' + model + '\', '+dataset.typePlan+')">'
        + '<i class="icon icon-checkmark"></i>'
        + '</button>'
        + '<button class="btn btn-danger " id="cancelAdd" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelAddPlan()">'
        + '<i class="icon icon-spinner11"></i>'
        + '</button>'
        + '</td>'
        + '</tr>';
    $("#tblTarget tbody").append(html);

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
//event 
function edit(id) {
    $("#editRow"+id).addClass("hidden");
    $("#confirmRow"+id).removeClass("hidden");
    $("#cancelRow"+id).removeClass("hidden");

    $("#mo"+id).addClass("hidden");
    $("#txtEditMO"+id).removeClass("hidden");

    $("#plan"+id).addClass("hidden");
    $("#txtEditPlan"+id).removeClass("hidden");

    $("#shift"+id).addClass("hidden");
    $("#txtEditShift"+id).removeClass("hidden");

    $("#shiftTime"+id).addClass("hidden");
    $("#txtEditShiftTime"+id).removeClass("hidden");

    $(".btn-warning").attr("disabled", "disabled");
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
function cancelEdit(id) {
    $("#editRow"+id).removeClass("hidden");
    $("#confirmRow"+id).addClass("hidden");
    $("#cancelRow"+id).addClass("hidden");

    $("#mo"+id).removeClass("hidden");
    $("#txtEditMO"+id).addClass("hidden");

    $("#plan"+id).removeClass("hidden");
    $("#txtEditPlan"+id).addClass("hidden");

    $("#shift"+id).removeClass("hidden");
    $("#txtEditShift"+id).addClass("hidden");

    $("#shiftTime"+id).removeClass("hidden");
    $("#txtEditShiftTime"+id).addClass("hidden");

    $(".btn-warning").removeAttr("disabled");
}

function cancelAddPlan() {
    $('#btnAddNewPlan').removeAttr('disabled');
    $('#rowAdd').remove();
}
function confirmPlan(modelName, type, id) {
    editPlan = Number($('#txtEditPlan'+id).val());
    editMO = $('#txtEditMO'+id).val();
    model = $('#txtEditModel'+id).val();
    shift = $('#txtEditShift'+id).val();
    shiftTime = $('#txtEditShiftTime'+id).val();
    var idPlanConfirm = Number(id);
    data = {
        factory: dataset.factory,
        modelName: modelName,
        plan: editPlan,
        mo: editMO,
        shift: shift,
        shiftTime: shiftTime,
        id: idPlanConfirm,
    }
    if (type == 1) {
        data.type = "DAILY";
    } else if (type == 2) {
        data.type = "MONTHLY";
    }
    $.ajax({
        type: 'POST',
        url: '/api/test/model/plan',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response) {
                showModal(modelName,type);
                loadModelList();
            }
            else {
                alert("Set Plan Fail!! Please Try Again!");
                showModal(modelName,type);
            }

            delete this.response;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
    // }
}
function AddNewPlan(modelName, type) {
    var addPlan = Number($('#txtAddPlan').val());
    var addMO = $('#txtAddMO').val();
    var addShift = $('#txtAddShift').val();
    var addShiftTime = $('#txtAddShiftTime').val();
    var data = {
        factory: dataset.factory,
        modelName: modelName,
        plan: addPlan,
        mo: addMO,
        shift: addShift,
        shiftTime: addShiftTime,
        id: 0,
    }
    if (type == 1) {
        data.type = "DAILY";
    } else if (type == 2) {
        data.type = "MONTHLY";
    }
    $.ajax({
        type: 'POST',
        url: '/api/test/model/plan',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response) {
                showModal(modelName,type);
                loadModelList();
                $('#btnAddNewPlan').removeAttr('disabled');
            }
            else {
                alert("Set Plan Fail!! Please Try Again!");
                showModal(modelName,type);
            }
            delete this.response;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}

function closeModal() {
    $("#mydiv").removeClass("show");
    $('#btnAddNewPlan').removeAttr('disabled');
}
$("#close-error").click(function (){
    // $("#modalSetup").remove('opacity','0.5');
    $("#modalErrorModel").css('opacity','1');
 });
function showModalErrorCode1(modelName, groupName, errorCode){
    $("#modalErrorModel").css('opacity','0.5');
    // $('#addModel1').html(modelName);
    // $('#addGroup1').html(groupName);
    $.ajax({
        type: 'GET',
        url: '/api/test/station/byError',
        data: {
            factory: dataset.factory,
            modelName: modelName,
            groupName: groupName,
            errorCode: errorCode,
            timeSpan: dataset.timeSpan
        },
        success: function(data){
            // console.log("dddddddd", data);
            var dataChart = new Array(data.length);
            var categories = new Array(data.length);
            if (!$.isEmptyObject(data)) {
                for(i in data){
                    categories[i] = data[i].stationName;
                    dataChart[i] = {name: data[i].stationName, y: data[i].testFail}
                }
            }
            Highcharts.chart('chart-error-code-by-tester', {
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },
                title: {
                    text: (errorCode + ' - ISSUE BY TESTER')
                },
                subtitle:{
                    text: '('+modelName+' - '+groupName+')'
                },
                xAxis: {
                    categories: categories,
                    labels:{
                        rotation: 90,
                        align: top,
                        style: {
                            fontSize: '11px'
                        }
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ' '
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
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
                },
                plotOptions: {
                    column: {
                        stacking: 'normal',
                    }
                },
                series: [{
                    name: 'Test Fail',
                    colorByPoint: true,
                    data: dataChart
                }]
            });
        },
        failure: function(errMsg) {
             console.log(errMsg);
        }
    });
}