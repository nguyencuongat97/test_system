var modelList = [
    "US8W600T01",
    "US8W600TSD",
    "US8W600T0A",
    "US8W2501T01",
    "US8W1500T01",
    "US8W1500TSD",
    "ES8W1500T01",
    "ES8W1500TSD",
    "US24LT00T01",
    "US24LT00TSD",
    "ES24LT00T01",
    "ES24LT00TSD",
    "US48LT00T01",
    "ES48LT00T01",
    "US16N150T01",
    "US24N250T01",
    "US48N500T01",
    "UVCG3PR0T0L",
    "UVCG3PR0T0S",
    "UVCG3PR1T01",
    "UVCG4PR0T0L",
    "UVCG4PR0T0S",
    "UVCG4PR0T01",
    "UVCSD000T01",
    "UVCSD000T0A",
    "UVCSD000T0B",
    "USG00000T01",
    "USG00000TSD",
    "USGPR000T01",
    "UCK00003T01",
    "UCK00003TSD",
    "UCKG2000T01",
    "UCKG2P00T01",
    "ERG00002T01",
    "CKG2RM01T01",
    "UAPACPR1T02",
    "UAPACPR1T0B",
    "UAPACPR0T02",
    "UAPACPR0TSD",
    "UAPACLR3TSD",
    "UAPACLT3T01",
    "UAPACLT3T02",
    "UAPACLT3TSD",
    "UAPACHD1T02",
    "UAPACMPRT00",
    "UAPACM00T02",
    "AIRRAC00T01",
    "AIRRAC00T02",
    "AIRRAC00T0A",
    "ACB ISP00T01",
    "ACB ISP00T02",
    "AFIRHD00T02",
    "AFIRHD00T0B",
    "AFIPHD00T02",
    "AFIPHD001.VS"
];

function loadSelectModel() {
    if (dataset.factory == "S03") {
        $.ajax({
            type: "GET",
            url: "/api/re/sfc/get_model_meta",
            data: {
                factory: dataset.factory,
                // timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                console.log("dada", data)
                $('#tblModel tbody').html('');
                if (!$.isEmptyObject(data)) {
                    var html = '<tr><td class="model-name" onclick="loadDataByModel(this)">ALL MODEL</td></tr>';
                    for (i in data) {
                        if (dataset.factory == "C03") {
                            if (modelList.indexOf(data[i]) != -1) {
                                html += '<tr><td class="model-name" onclick="loadDataByModel(this)">' + data[i] + '</td></tr>';
                            }
                        } else {
                            html += '<tr><td class="model-name" onclick="loadDataByModel(this)">' + data[i] + '</td></tr>';
                        }
                    }
                    $('#tblModel tbody').html(html);
                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    } else {
        $.ajax({
            type: "GET",
            url: "/api/test/sfc/model",
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                $('#tblModel tbody').html('');
                if (!$.isEmptyObject(data)) {
                    var html = '<tr><td class="model-name" onclick="loadDataByModel(this)">ALL MODEL</td></tr>';
                    for (i in data) {
                        if (dataset.factory == "C03") {
                            if (modelList.indexOf(data[i]) != -1) {
                                html += '<tr><td class="model-name" onclick="loadDataByModel(this)">' + data[i] + '</td></tr>';
                            }
                        } else {
                            html += '<tr><td class="model-name" onclick="loadDataByModel(this)">' + data[i] + '</td></tr>';
                        }
                    }
                    $('#tblModel tbody').html(html);
                }
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

}

function loadDataByModel(context) {
    var modelName = '';
    if (context.innerText != 'ALL MODEL') {
        modelName = context.innerText;
    }
    $('.model-name').removeClass('active');
    context.className += ' active';
    loadData(modelName);
}

function loadData(modelName) {
    $('.loader').removeClass('hidden');
    $.ajax({
        type: "GET",
        url: "/api/re/sfc/get_te_report",
        data: {
            factory: dataset.factory,
            modelName: modelName,
            timeSpan: dataset.timeSpan,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            $('#tblDetail').html('');
            var dataLoad = {};
            if (!$.isEmptyObject(data)) {
                // =================  Add thead  =================//
                var thead = '<thead><tr><th rowspan="2">MODEL NAME</tg><th rowspan="2">GROUP NAME</th>';
                var thead2 = '<tr>';
                for (i in data) {
                    var cols = Object.keys(data[i]);
                    if (cols.length == 2) {
                        thead += '<th colspan="2">' + i + '</th>';
                    } else {
                        thead += '<th>' + i + '</th>';
                    }
                    for (j in data[i]) {
                        thead2 += '<th>' + j + '</th>';
                    }
                }
                thead += '</tr>';
                thead2 += '</tr></thead>';
                // ================= End Add thead  =================//

                // =================  Convert Data  =================//
                for (i in data) {
                    dataLoad[i] = {}
                    for (j in data[i]) {
                        dataLoad[i][j] = {};
                        for (k in data[i][j]) {
                            dataLoad[i][j][data[i][j][k].modelName] = {};
                        }
                    }
                }
                for (i in data) {
                    for (j in data[i]) {
                        for (k in data[i][j]) {
                            dataLoad[i][j][data[i][j][k].modelName][data[i][j][k].groupName] = data[i][j][k].retestRate
                        }
                    }
                }

                // console.log('dataLoad: ', dataLoad);
                var dataModel = {};
                for (i in dataLoad) {
                    for (j in dataLoad[i]) {
                        for (k in dataLoad[i][j]) {
                            if (dataset.factory == "C03") {
                                if (modelList.indexOf(k) != -1) {
                                    dataModel[k] = [];
                                    for (h in dataLoad[i][j][k]) {
                                        if (dataModel[k].indexOf(h) == -1) {
                                            dataModel[k].push(h);
                                        }
                                    }
                                }
                            } else {
                                dataModel[k] = [];
                                for (h in dataLoad[i][j][k]) {
                                    if (dataModel[k].indexOf(h) == -1) {
                                        dataModel[k].push(h);
                                    }
                                }
                            }
                        }
                    }
                }
                console.log(dataModel);

                var dataTime = [];
                for (i in data) {
                    for (j in data[i]) {
                        var obj = {};
                        obj[i] = j;
                        dataTime.push(obj);
                    }
                }
                // =================  End Convert Data  =================//

                // =================  Draw Table  =================//
                var tbody = '<tbody>';
                for (k in dataModel) {
                    for (h in dataModel[k]) {
                        tbody += '<tr class="row_' + convertText(i) + '_' + j + '_' + convertText(k) + '_' + convertText(dataModel[k][h]) + '">'
                            + '<td class="col_' + convertText(k) + '">' + k + '</td>'
                            + '<td>' + dataModel[k][h] + '</td>';
                        for (x in dataTime) {
                            for (y in dataTime[x]) {
                                tbody += '<td class="col_' + convertText(k) + '_' + convertText(dataModel[k][h]) + '_' + convertText(y) + '_' + dataTime[x][y] + '" data-popup="popover" data-trigger="hover" data-placement="left" data-html="true"></td>'
                            }
                        }
                        tbody += '</tr>';
                    }
                }

                tbody += '</tbody>';
                $('#tblDetail').append(thead + thead2);
                $('#tblDetail').append(tbody);

                // =================  End Draw Table  =================//

                // =================  Map Value To Table  =================//
                for (i in data) {
                    for (j in data[i]) {
                        for (k in data[i][j]) {
                            $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).html('<b>' + data[i][j][k].retestRate.toFixed(2) + '%</b>');
                            if (data[i][j][k].retestRate >= 10) {
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-warning');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-success');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-danger');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).addClass('rate-danger');
                            } else if (data[i][j][k].retestRate >= 5 && data[i][j][k].retestRate < 10) {
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-warning');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-success');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-danger');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).addClass('rate-warning');
                            } else {
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-warning');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-success');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).removeClass('rate-danger');
                                $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).addClass('rate-success');
                            }

                            var pop = '<div><b>WIP: </b><span>' + data[i][j][k].wip + '</span></div>' +
                                '<div><b>Yield Rate: </b><span>' + data[i][j][k].yieldRate.toFixed(2) + '%</span></div>' +
                                '<div><b>Fail: </b><span>' + data[i][j][k].fail + '</span></div>' +
                                '<div><b>First Fail: </b><span>' + data[i][j][k].firstFail + '</span></div>' +
                                '<div><b>Second Fail: </b><span>' + data[i][j][k].secondFail + '</span></div>' +
                                '<div><b>Retest Rate: </b><span>' + data[i][j][k].retestRate.toFixed(2) + '%</span></div>' +
                                '<div><b>Pass: </b><span>' + data[i][j][k].pass + '</span></div>' +
                                '<div><b>First Pass Rate: </b><span>' + data[i][j][k].firstPassRate.toFixed(2) + '%</span></div>';
                            $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).attr('data-content', pop);
                            $('.col_' + convertText(data[i][j][k].modelName) + '_' + convertText(data[i][j][k].groupName) + '_' + convertText(i) + '_' + j).css('cursor', 'pointer');
                        }
                    }
                }
                // =================  End Map Value To Table  =================//

                // =================  Rowspan column Model Name  =================//
                for (i in dataModel) {
                    var classRow = $('.col_' + convertText(i));
                    if (classRow.length > 1) {
                        for (let x = 0; x < classRow.length; x++) {
                            classRow[0].rowSpan = classRow.length;
                            if (x > 0) {
                                classRow[x].className += ' hidden';
                            }
                        }
                    }
                }
                // =================  End Rowspan column Model Name  =================//
            } else {
                alert('NO DATA!!');
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
        complete: function () {
            $('[data-popup=popover]').popover({
                template: '<div class="popover"><div class="arrow"></div><div class="popover-content"></div></div>'
                , container: 'body'
            });
            $('.loader').addClass('hidden');
        }
    });
}

function convertText(text) {
    var result = '';
    if (text != null && text != undefined) {
        result = text.replace(/\s|\.|\(|\)|\//g, '_');
    }
    return result;
}

$("#txtSearchModel").on("keyup", function () {
    var value = $(this).val().toLowerCase();
    $("#tblModel>tbody>tr").filter(function () {
        $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
    });
});

function getTimeNow() {
    $.ajax({
        type: "GET",
        url: "/api/time/now",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var current = new Date(data);
            var endDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
            var startDate = moment(current).add(-3, "day").format("YYYY/MM/DD") + ' 07:30:00';
            $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
            $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
            $('input[name=timeSpan]').on('change', function () {
                dataset.timeSpan = this.value;
                loadData();
            });

            dataset.timeSpan = startDate + ' - ' + endDate;
            loadSelectModel();
            loadData('');

            delete current;
            delete startDate;
            delete endDate;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}