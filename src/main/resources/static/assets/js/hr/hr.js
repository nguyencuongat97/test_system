getDataWorking();
function getDataWorking() {

    // DATE TODAY   
    var curday = function (sp) {
        today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //As January is 0.
        var yyyy = today.getFullYear();

        if (dd < 10) dd = '0' + dd;
        if (mm < 10) mm = '0' + mm;
        return (yyyy + sp + mm + sp + dd);
    };
    var endDate = curday('/');
    // END DATE TODAY

    // 7 day ago
    var d = new Date();
    var pastDate = d.getDate() - 7;
    d.setDate(pastDate);
    var startDate = d.getFullYear().toString() + "/" + ((d.getMonth() + 1).toString().length == 2 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1).toString()) + "/" + (d.getDate().toString().length == 2 ? d.getDate().toString() : "0" + d.getDate().toString());
    // END 7 days ago
    getDataTotal(startDate, endDate);
}


// date
$(function () {

    // single date picker
    $('.daterange-single').daterangepicker({
        singleDatePicker: true,
        opens: "right",
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        locale: {
            format: 'YYYY/MM/DD'
        }
    });

    // range date time 24 picker
    $('.datetimehr[side=right]').daterangepicker({
        maxSpan: {
            days: 30
        },
        timePicker: true,
        timePicker24Hour: true,
        opens: "right",
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        timePickerIncrement: 30,
        locale: {
            format: 'YYYY/MM/DD HH:mm'
        }
    });

    $('.datetimehr[side=left]').daterangepicker({
        maxSpan: {
            days: 30
        },
        timePicker: true,
        timePicker24Hour: true,
        opens: "left",
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        timePickerIncrement: 30,
        locale: {
            format: 'YYYY/MM/DD HH:mm'
        }
    });

    $('.bootstrap-select').selectpicker();

    $(".file-input-overwrite").fileinput({
        previewFileType: 'image',
        browseLabel: '',
        browseIcon: '<i class="icon-image2 position-left"></i> ',
        removeLabel: '',
        removeIcon: '<i class="icon-cross3"></i>',
        layoutTemplates: {
            icon: '<i class="icon-file-check"></i>',
            main1: "{preview}\n" +
                "<div class='input-group {class}'>\n" +
                "   <div class='input-group-btn'>\n" +
                "       {browse}\n" +
                "   </div>\n" +
                "</div>"
        },
        initialPreview: [
            "<img src='assets/images/placeholder.jpg' class='file-preview-image' alt=''>",
        ],
        overwriteInitial: true
    });

    $('input.file-input').fileinput({
        previewFileType: 'image',
        browseLabel: '',
        browseIcon: '<i class="icon-image2 position-left"></i> ',
        removeLabel: '',
        removeIcon: '<i class="icon-cross3"></i>',
        layoutTemplates: {
            icon: '<i class="icon-file-check"></i>'
        }
    });

});


$(document).ready(function () {
    var times = $('input[name="dateTimeSpan"]').val();
    $('input[name=dateTimeSpan]').on('change', function (event) {
        var imeSpan = event.target.value;
        var date1 = imeSpan.split(" - ");
        var a = date1[0].split(" ");
        var b = date1[1].split(" ");
        let startD = a[0];
        let endD = b[0];
        console.log(startD + "---" + endD)
        $('#item').html('');
        $('#tblDayWorking>tbody').html('');
        $('#chartWMA').html('');
        //  $('#item').append('')
        getDataTotal(startD, endD)

    });
});  // END DOcument.ready

function detailWorkingMissingAlert(nUrl, mDate) {
    var check = false;
    // if (nUrl == "working") {
    //     $('#dtBasicExample>tbody').html('');
    // }
    $.ajax({
        type: 'GET',
        url: "/api/hr/employee/tracking/" + nUrl + "/list/by/day",
        contentType: "application/json; charset=utf-8",
        data: { date: mDate },
        success: function (result) {
            var html = '';
            result.forEach(e => {
                html += '  <tr><td>' + e.name + '</td >' +
                    '<td>' + e.empNo + '</td>' +
                    '<td>' + e.cardNo + '</td></tr > ';
            });
            if (nUrl == "working") {
                $('.today-working').html("<h5>Today " + nUrl + " " + mDate + "</h5>")
                $('#dtBasicExample>tbody').html(html);
                $('#dtBasicExample').DataTable();
            } else if (nUrl == "missing") {
                $('.today-missing').html("<h5>Today " + nUrl + " " + mDate + "</h5>")
                $('#dtBasicExample1>tbody').html(html);
                $('#dtBasicExample1').DataTable();
            } else {
                $('.today-alert').html("<h5>Today " + nUrl + " " + mDate + "</h5>")
                $('#dtBasicExample2>tbody').html(html);
                check = true;
                if (check) {
                    $('#dtBasicExample2').DataTable();
                    $('.dataTables_length').addClass('bs-select');
                }

            }
            $('.dataTables_length').addClass('bs-select');

        },
        error: function () {
            alert("Fail!");
        }
    });
}
detailTodayWorking();
function detailTodayWorking() {

    // DATE TODAY   
    var curday = function (sp) {
        today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //As January is 0.
        var yyyy = today.getFullYear();

        if (dd < 10) dd = '0' + dd;
        if (mm < 10) mm = '0' + mm;
        return (yyyy + sp + mm + sp + dd);
    };
    var endDate = curday('/');
    // END DATE TODAY

    $.ajax({
        type: 'GET',
        url: "/api/hr/employee/tracking/working/list/by/day",
        contentType: "application/json; charset=utf-8",
        data: { date: endDate },
        success: function (result) {
            var html = '';
            result.forEach(e => {
                html += '  <tr><td>' + e.name + '</td >' +
                    '<td>' + e.empNo + '</td>' +
                    '<td>' + e.cardNo + '</td></tr > ';
            });
            $('.dataTables_length select[name="dtBasicExample_length"]').val("5");
            $('.today-working').html("<h5>Số người đi làm hôm nay " + endDate + "</h5>");
            $('#dtBasicExample>tbody').html(html);
            $('#dtBasicExample').DataTable();
            $('.dataTables_length').addClass('bs-select');
            $('.dataTables_length select[name="dtBasicExample_length"]').prepend("<option value='5'>5</option>");
            $('.dataTables_length select[name="dtBasicExample_length"]').val("5");
        },
        error: function () {
            alert("Fail!");
        }
    });
}
detailTodayMissing();
function detailTodayMissing() {
    // DATE TODAY   
    var curday = function (sp) {
        today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //As January is 0.
        var yyyy = today.getFullYear();

        if (dd < 10) dd = '0' + dd;
        if (mm < 10) mm = '0' + mm;
        return (yyyy + sp + mm + sp + dd);
    };
    var endDate = curday('/');
    // END DATE TODAY

    $.ajax({
        type: 'GET',
        url: "/api/hr/employee/tracking/missing/list/by/day",
        contentType: "application/json; charset=utf-8",
        data: { date: endDate },
        success: function (result) {
            var html = '';
            result.forEach(e => {
                html += '  <tr><td>' + e.name + '</td >' +
                    '<td>' + e.empNo + '</td>' +
                    '<td>' + e.cardNo + '</td></tr > ';
            });
            $('.today-missing').html("<h5>Số người không đi làm hôm nay " + endDate + "</h5>");
            $('#dtBasicExample1>tbody').html(html);
            $('#dtBasicExample1').DataTable();
            $('.dataTables_length').addClass('bs-select');
        },
        error: function () {
            alert("Fail!");
        }
    });
}
detailTodayAlert();
function detailTodayAlert() {
    // DATE TODAY   
    var curday = function (sp) {
        today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //As January is 0.
        var yyyy = today.getFullYear();

        if (dd < 10) dd = '0' + dd;
        if (mm < 10) mm = '0' + mm;
        return (yyyy + sp + mm + sp + dd);
    };
    var endDate = curday('/');
    // END DATE TODAY

    $.ajax({
        type: 'GET',
        url: "/api/hr/employee/tracking/alert/list/by/day",
        contentType: "application/json; charset=utf-8",
        data: { date: endDate },
        success: function (result) {
            var html = '';
            result.forEach(e => {
                html += '  <tr><td>' + e.name + '</td >' +
                    '<td>' + e.empNo + '</td>' +
                    '<td>' + e.cardNo + '</td></tr > ';
            });
            $('.today-alert').html("<h5>Số người nghỉ quá 5 ngày hiện tại " + endDate + "</h5>");
            $('#dtBasicExample2>tbody').html(html);
            $('#dtBasicExample2').DataTable();
            $('.dataTables_length').addClass('bs-select');
        },
        error: function () {
            alert("Fail!");
        }
    });
}

// FUNCTION
function getDataTotal(startDate, endDate) {
    var dataWork = {};
    var dataMissing = {};
    var dataAlert = {};
    var dataDate = [];
    $.ajax({
        type: 'GET',
        url: "/api/hr/employee/tracking/by/day",
        data: { startDate: startDate, endDate: endDate },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            console.log("getData:", data)
            var html = '<th class="customTbl" style="width: 8%">#</th>';
            var val = '<tr><td>Số Người nghỉ quá 5 ngày</td>';
            var missingList = '<tr><td>số người không đi làm</td>';
            var workingList = '<tr><td>Số người đi làm</td>';
            var tmpDataWork = [], tmpDataMissing = [], tmpDataAlert = [];
            for (const k in data.workingList) {
                if (data.workingList.hasOwnProperty(k)) {
                    const element = data.workingList[k];
                    html += ' <th class="customTbl">' + element.workDate + '</th>'
                    workingList += ' <td class="33a"><a onclick=detailWorkingMissingAlert("working","' + element.workDate + '")>' + element.qty + '</a></td>'
                    dataDate.push(element.workDate);
                    tmpDataWork.push(element.qty);
                }
            }
            dataWork.name = "Working";
            dataWork.data = tmpDataWork;
            workingList += '</tr>';
            $('#tblDayWorking>tbody').append(workingList);

            for (const i in data.missingList) {
                if (data.missingList.hasOwnProperty(i)) {
                    const element = data.missingList[i];
                    missingList += ' <td class="33"><a onclick=detailWorkingMissingAlert("missing","' + element.workDate + '")>' + element.qty + '</a></td>'
                    tmpDataMissing.push(element.qty);
                }
            }
            missingList += '</tr>';
            $('#tblDayWorking>tbody').append(missingList);
            dataMissing.name = "Missing";
            dataMissing.data = tmpDataMissing;

            for (const key in data.alertList) {
                if (data.alertList.hasOwnProperty(key)) {
                    const element = data.alertList[key];
                    //   console.log(element.workDate)

                    val += ' <td class="33"><a onclick=detailWorkingMissingAlert("alert","' + element.workDate + '")>' + element.qty + '</a></td>'
                    tmpDataAlert.push(element.qty);
                }
            }
            val += '</tr>'
            $('#item').append(html);
            $('#tblDayWorking>tbody').append(val);
            dataAlert.name = "Alert";
            dataAlert.data = tmpDataAlert;
            console.log(dataDate)
            console.log(dataWork)
            console.log(dataMissing)
            console.log(dataAlert)
            hightChartStaticNBB(dataDate, dataWork, dataMissing, dataAlert);
        },
        error: function () {
            alert("Fail!");
        }
    });
}


// highchart  
function hightChartStaticNBB(dataDate, dataWork, dataMissing, dataAlert) {
    Highcharts.chart('chartWMA', {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Stacked column chart'
        },
        xAxis: {
            categories: dataDate
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Total fruit consumption'
            },
            stackLabels: {
                enabled: true,
                style: {
                    fontWeight: 'bold',
                    color: ( // theme
                        Highcharts.defaultOptions.title.style &&
                        Highcharts.defaultOptions.title.style.color
                    ) || 'gray'
                }
            }
        },
        legend: {
            align: 'right',
            x: -30,
            verticalAlign: 'top',
            y: 25,
            floating: true,
            backgroundColor:
                Highcharts.defaultOptions.legend.backgroundColor || 'white',
            borderColor: '#CCC',
            borderWidth: 1,
            shadow: false
        },
        tooltip: {
            headerFormat: '<b>{point.x}</b><br/>',
            pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
        },
        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: true
                }
            }
        },
        series: [dataAlert, dataMissing, dataWork]
    });
}
