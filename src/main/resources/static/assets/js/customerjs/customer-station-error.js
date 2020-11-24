var charts = {};

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
                min: -5,
                softMax: 105,
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

function loadModelList() {
    clearTimeout(dataset.timeout['loadModelList']);
    $('.loader').css('display', 'block');
    $.ajax({
        type: "GET",
        url: "/api/test/model/daily",
        data: {
            factory: dataset.factory,
            customerName: "",
            timeSpan: dataset.timeSpan,
            customer: true,
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
                    }

                    html += '<label class="btn modelSort ' + convertStatus((100 - data[i].retestRate), 90, 97) + '" model="' + data[i].modelName + '" data-popup="popover" data-trigger="hover" data-placement="bottom" data-html="true" data-content="' + value + '" style="margin: 0px 1px 1px 0px;">' +
                        '<input class="control-primary hidden" type="radio" name="model-name" value="' + data[i].modelName + '">' + data[i].modelName +
                        //'<span class="badge bg-warning-400" style="position: absolute; top: -13px; right: 0; border-radius: 0px">'+data[i].retestRate.toFixed(2)+'</span>' +
                        '</label>';
                    var rtr = data[i].retestRate;
                    var fpr = data[i].firstPassRate;
                    var ffail = data[i].firstFail;
                    var sfail = data[i].secondFail;
                    var ete = data[i].output;
                    var valueETE, valueUbee;
                    if(data[i].customerName == null){
                        valueUbee = '';
                    } else{
                        valueUbee = data[i].customerName;
                    }
                    if(ete == 0){
                        valueETE = 0;
                    }else{
                        valueETE = (data[i].output - sfail) * 100.0 / data[i].output;
                    }

                    if (rtr < 0) {
                        rtr = 0;
                        sfail = ffail;
                        fpr = (data[i].wip - ffail) * 100.0 / data[i].wip;
                       
                    }
                   
                    if (dataset.factory == 'B04') {
                        console.log("b04: ", dataset.factory)
                        htmlTbl += '<tr class="row-model" data-model="' + data[i].modelName + '">' +
                            '<td>' + (Number(i) + 1) + '</td>' +
                            '<td class="text-semibold" ><a class="model-name" data-model="' + data[i].modelName + '" >' + data[i].modelName + '</a></td>' +
                            '<td class="target" onclick=showModal("' + data[i].modelName + '") title="Click to Set Plan"><a class="model-plan" data-model="' + data[i].plan + '">' + data[i].plan + '</a></td>' +
                            '<td></td>' +
                            '<td>' + data[i].wip + '</td>' +
                            '<td>' + data[i].output + '</td>' +
                            '<td>' + data[i].outputWeekly + '</td>' +
                            '<td>' + data[i].hitRate.toFixed(2) + '% </td>' +
                           // '<td class="' + convertStatus(data[i].ete, 95, 98) + '" >' + data[i].ete.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus(valueETE, 97, 98) + '" >' + valueETE.toFixed(2) + '% </td>' +
                            // '<td class="' + convertStatus(fpr, 90, 97) + '" >' + fpr.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus((100 - rtr), 90, 94) + '" >' + rtr.toFixed(2) + '% </td>' +
                            // '<td>' + data[i].retest + '</td>' +
                            // '<td>' + data[i].secondFail + '</td>' +
                            '<td>' + ffail + '</td>' +
                            '<td>' + sfail + '</td>' +
                            '</tr>';
                        totalPlan += data[i].plan;
                    } else if (dataset.factory == 'B06') {   
                        htmlTbl += '<tr class="row-model" data-model="' + data[i].modelName + '">' +
                            '<td>' + (Number(i) + 1) + '</td>' +
                            // '<td class="text-semibold" ><a class="model-name" data-model="' + data[i].modelName + '" >' + data[i].modelName + '</a></td>' +
                            '<td>' + data[i].modelName + '</td>' +
                            '<td>' + valueUbee + '</td>' +
                            '<td class="target">' + data[i].planMonth + '</td>' +
                            '<td>' + data[i].wip + '</td>' +
                            '<td>' + data[i].output + '</td>' +
                            '<td>' + data[i].outputWeekly + '</td>' +
                            '<td>' + data[i].hitRateMonth.toFixed(2) + '% </td>' +
                            // '<td class="' + convertStatus(data[i].ete, 95, 98) + '" >' + data[i].ete.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus(valueETE, 97, 98) + '" >' + valueETE.toFixed(2) + '% </td>' +
                            // '<td class="' + convertStatus(fpr, 90, 97) + '" >' + fpr.toFixed(2) + '%</td>' +
                            '<td class="' + convertStatus((100 - rtr), 90, 94) + '" >' + rtr.toFixed(2) + '% </td>' +
                            // '<td>' + data[i].retest + '</td>' +

                            // '<td>' + data[i].secondFail + '</td>' +
                            '<td>' + ffail + '</td>' +
                            '<td>' + sfail + '</td>' +
                            '</tr>';
                        totalPlan += data[i].planMonth;
                    }

                    totalFail += data[i].secondFail;
                    //totalRFail += data[i].secondFail;
                    totalFFail += data[i].firstFail;
                    //totalRetest += data[i].retest;
                    totalInput += data[i].wip;
                    totalOutPut += data[i].output;
                    totalWeeklyOutPut += data[i].outputWeekly;

                }
                if(totalPlan==0){
                    var totalHitRate = (0 * 100.0).toFixed(2);
                } else{
                    var totalHitRate = ((totalWeeklyOutPut / totalPlan) * 100).toFixed(2);
                }
                htmlTbl += '<tr>' +
                    '<td></td>' +
                    '<td>TOTAL</td>' +
                    '<td class="text-semibold" ></td>' +
                    '<td>' + totalPlan + '</td>' +
                    '<td>' + totalInput + '</td>' +
                    '<td>' + totalOutPut + '</td>' +
                    '<td>' + totalWeeklyOutPut + '</td>' +
                    '<td>' + totalHitRate + '%</td>' +
                    '<td></td>' +
                    // '<td></td>' +
                    '<td></td>' +
                    '<td>' + totalFFail + '</td>' +
                    '<td>' + totalFail + '</td>' +
                    '</tr>';

                $('.mds').html(html);
                $('#tbl-model-status>tbody').html(htmlTbl);
                $('[data-popup=popover]').popover({
                    template: '<div class="popover" style="width: 250px; background-color: #e0e0e0; border: 1px solid #4e7af3; z-index-: 100"><div class="arrow"></div><div class="popover-content"></div></div>'
                });
                $('.model-name').on('click', function (event) {
                    var url = "/customer/customer-detail?factory=" + dataset.factory + "&modelName=" + this.dataset.model;
                    openWindow(url);
                });

                delete html;
                delete htmlTbl;
            } else {
                $('.mds').html('<label class="btn modelSort bg-gray" locked warning idle style="margin: 0px 1px 1px 0px;" >' +
                                    '<input class="control-primary hidden" type="radio" name="model-name" value="all" > ALL' +
                                    '</label>');

                $('#tbl-model-status>tbody').html('');
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
function showModal(model) {
    $("#mydiv").addClass("show");
    // $("#mydiv").draggable({ handle: "#mydivheader"});
    $.ajax({
        type: "GET",
        url: "/api/test/model/plan",
        data: {
            factory: dataset.factory,
            modelName: model
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log("data modal: ", data);
            if (dataset.factory == 'B04') {
                console.log("bo4 modal: ", dataset.factory)
                var html = '';
                if (data) {
                    html = '<tr id="row">'
                        + '<td><span class="hidden" id="idPlanConfirm">' + data.id + '</span><div id="txtEditModel" data-info="' + data.modelName + '">' + data.modelName + '</div></td>'
                        + '<td><input id="txtEditPlan" class="form-control hidden" name="plan" type="number" value="' + data.plan + '" /><span id="plan">' + data.plan + '</span></td>'
                        + '<td><input id="txtEditMO" class="form-control hidden" name="mo" type="text" value="' + data.mo + '" /><span id="mo">' + data.mo + '</span></td>'
                        + '<td><div id="txtEditShift">' + data.shift + '</div></td>'
                        + '<td><div id="txtEditShiftTime">' + data.shiftTime + '</div></td>'
                        + '<td>'
                        + '<button class="btn btn-warning" id="editRow" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit()">'
                        + '<i class="icon icon-pencil"></i>'
                        + '</button>'
                        + '<button class="btn btn-success hidden" id="confirmRow" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel(\'' + data.modelName + '\')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button><button class="btn btn-danger hidden" id="cancelRow" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit()">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    $("#tblTarget tbody").html(html);

                } else {
                    html = '<tr id="rowAdd">'
                        + '<td><div id="txtEditModelNameAdd">' + model + '</div></td>'
                        + '<td><input id="txtAddPlan" class="form-control" name="plan" type="number"/></td>'
                        + '<td><input id="txtAddMO" class="form-control" name="mo" type="text"/></td>'
                        + '<td><div id="txtEditShift" name="shift" type="text" value=""></div></td>'
                        + '<td><div id="txtEditShiftTime" name="shiftTime" type="text" value=""></div></td>'
                        + '<td>'
                        + '<button class="btn btn-success " id="addPlan" title="Add" style="padding: 4px 10px;" onclick="AddNew(\'' + model + '\')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button>'
                        + '<button class="btn btn-danger " id="cancelAdd" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="closeModal()">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    $("#tblTarget tbody").html(html);

                }

                delete html;
            } else if (dataset.factory == 'B06') {
                console.log("bo6 modal: ", dataset.factory)
                var html = '';
                if (data) {
                    html = '<tr id="row">'
                        + '<td><span class="hidden" id="idPlanConfirm">' + data.id + '</span><div id="txtEditModel" data-info="' + data.modelName + '">' + data.modelName + '</div></td>'
                        + '<td><input id="txtEditPlan" class="form-control hidden" name="plan" type="number" value="' + data.planMonth + '" /><span id="plan">' + data.planMonth + '</span></td>'
                        + '<td><input id="txtEditMO" class="form-control hidden" name="mo" type="text" value="' + data.mo + '" /><span id="mo">' + data.mo + '</span></td>'
                        + '<td><div id="txtEditShift">' + data.shift + '</div></td>'
                        + '<td><div id="txtEditShiftTime">' + data.shiftTime + '</div></td>'
                        + '<td>'
                        + '<button class="btn btn-warning" id="editRow" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit()">'
                        + '<i class="icon icon-pencil"></i>'
                        + '</button>'
                        + '<button class="btn btn-success hidden" id="confirmRow" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel(\'' + data.modelName + '\')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button><button class="btn btn-danger hidden" id="cancelRow" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit()">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    $("#tblTarget tbody").html(html);

                } else {
                    html = '<tr id="rowAdd">'
                        + '<td><div id="txtEditModelNameAdd">' + model + '</div></td>'
                        + '<td><input id="txtAddPlan" class="form-control" name="plan" type="number"/></td>'
                        + '<td><input id="txtAddMO" class="form-control" name="mo" type="text"/></td>'
                        + '<td><div id="txtEditShift" name="shift" type="text" value=""></div></td>'
                        + '<td><div id="txtEditShiftTime" name="shiftTime" type="text" value=""></div></td>'
                        + '<td>'
                        + '<button class="btn btn-success " id="addPlan" title="Add" style="padding: 4px 10px;" onclick="AddNew(\'' + model + '\')">'
                        + '<i class="icon icon-checkmark"></i>'
                        + '</button>'
                        + '<button class="btn btn-danger " id="cancelAdd" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="closeModal()">'
                        + '<i class="icon icon-spinner11"></i>'
                        + '</button>'
                        + '</td>'
                        + '</tr>';
                    $("#tblTarget tbody").html(html);

                }
                delete html;
            }
            delete this.data;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
//event 
function edit() {
    $("#editRow").addClass("hidden");
    $("#confirmRow").removeClass("hidden");
    $("#cancelRow").removeClass("hidden");

    $("#mo").addClass("hidden");
    $("#txtEditMO").removeClass("hidden");

    $("#plan").addClass("hidden");
    $("#txtEditPlan").removeClass("hidden");

    $(".btn-warning").attr("disabled", "disabled");
}
function cancelEdit() {
    $("#editRow").removeClass("hidden");
    $("#confirmRow").addClass("hidden");
    $("#cancelRow").addClass("hidden");

    $("#mo").removeClass("hidden");
    $("#txtEditMO").addClass("hidden");

    $("#plan").removeClass("hidden");
    $("#txtEditPlan").addClass("hidden");

    $(".btn-warning").removeAttr("disabled");
}
function confirmModel(modelName) {
    editPlan = Number($('#txtEditPlan').val());
    editMO = $('#txtEditMO').val();
    model = $('#txtEditModel').val();
    var idPlanConfirm = Number($('#idPlanConfirm').html());
    data = {
        factory: dataset.factory,
        modelName: modelName,
        plan: editPlan,
        mo: editMO,
        id: idPlanConfirm
    }
    $.ajax({
        type: 'POST',
        url: '/api/test/model/saveplan',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response) {
                showModal(modelName);
                loadModelList();
            }
            else {
                alert("Set Plan Fail!! Please Try Again!");
                showModal(modelName);
            }

            delete this.response;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
    // }
}
function AddNew(modelName) {
    var addPlan = Number($('#txtAddPlan').val());
    var addMO = $('#txtAddMO').val();
    var data = {
        factory: dataset.factory,
        modelName: modelName,
        plan: addPlan,
        mo: addMO,
        id: 0,
    }
    $.ajax({
        type: 'POST',
        url: '/api/test/model/saveplan',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            if (response) {
                showModal(modelName);
                loadModelList();
            }
            else {
                alert("Set Plan Fail!! Please Try Again!");
                showModal(modelName);
            }
            delete this.response;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
    // }

}
function closeModal() {
    $("#mydiv").removeClass("show");
}