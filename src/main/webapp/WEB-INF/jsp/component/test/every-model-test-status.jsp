<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/css/custom/customer-line.css" />

<div class="row">
    <div class="col-lg-12" style="background: #272727;">
        <div class="panel panel-overview" id="header">
            <span class="text-uppercase">B06 - Every Model OBA test Status</span>
        </div>
        <div class="row" style="margin: unset;">
            <div class="panel panel-overview input-group"
                style="margin-bottom: 5px; background: #333;width: 91%; float: left;">
                <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                        class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan"
                    style="height: 26px; color: #fff;border-bottom: none;">
            </div>
            <div class="panel chooseModel"
                style="width: 8.5%;margin: 0 1% 5px;float: right;background: #333; height: 26px; margin-right: 0;margin-left: 0;">
                <a class=" btn btn-lg" onclick="exportDataLine()"
                    style="height:26px;width:100%;font-size: 13px; padding: 3px; border-radius: 5px; color: #fff;background: #444;">
                    <i class="fa fa-download"></i>
                </a>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class=" pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px);">
                <table id="tblModelLine" class="table table-bordered table-xxs"
                    style="margin-bottom: 10px; color: #fff;">
                    <thead id='theadLine'>
                    </thead>
                    <tbody id='tbodyLine'>

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<style>
#tblModelLine tbody td {
    background: #353535;
    color: #e0e0e3;
    font-size: 12px;
    text-align: center;
}
.text-bold {
    font-weight: bold;
}
.text-semibold{
    font-weight: bold;
}
#tblModelLine thead th {
    background-color: #f79646 !important;
    text-transform: uppercase !important;
    position: sticky;
    color: #333;
    padding: 5px;
    text-align: center;
    width: 8%;
}
#input .classinput {
    color: #fff; 
    /* background: #eaeaea; */
}

#defect .classdefect {
   color: #fff; 
   /* background: #eaeaea; */
}

#yield .classyield {
    color: #fff;
    /* background: #eaeaea; */
}

#acumulate .classacumulate {
    color: #fff;
    /* background: #eaeaea; */
}

#target .classtarget {
    color: #fff;
    /* background: #b1a0c7; */
}
#input,
#defect,
#yield,
#acumulate,
#target {
    text-transform: uppercase;
    text-align: center;
}
.classDay{
    color: #333;
    /* text-shadow: 1px 1px 1px; */
    /* font-weight: bold; */
    /* text-decoration: underline; */
}
a.classDay:hover{
    color: #1E88E5;
}
/* #chart-line-name .highcharts-container .highcharts-series-label text{
    display: none;
} */
</style>
<script>
    var dataset = {
        factory: '${factory}',
        modelName: '${modelName}',
        timeout: {},
        selectedModel: 'all',
        status: 'all',
        style: 'new-style'
    };
getTimeNow();
function getTimeNow() {
    $.ajax({
        type: "GET",
        url: "/api/time/now",
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var current = new Date(data);
            var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
            var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
            $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
            $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
            $('input[name=timeSpan]').on('change', function () {
                dataset.timeSpan = this.value;
                showModelTotal();
            });

            dataset.timeSpan = startDate + ' - ' + endDate;
            // loadTableList();

            delete current;
            delete startDate;
            delete endDate;
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}
showModelTotal();
function showModelTotal(){
    $('#theadLine').html('');
    $('#tbodyLine').html('');
    $.ajax({
        type: "GET",
        url: "/api/smt/line/weekly",
        data: {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan,
            customer: true,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            // console.log("data error: ", data);
            $('#theadLine').html('<tr><th style="width:1%">No</th><th>Model</th><th>Date</th></tr>');
            $('#tbodyLine').html("<tr id='quantity'><td class='classstt' rowspan='3'></td><td class='classModel' rowspan='3'></td><td class='classinput'>OBA test Quantity</td></tr>"
                + "<tr id='failure'><td class='classfailure'>Failure Quantity</td></tr>"
                // + "<tr id='goal'><td class='classgoal'>Goal Dppm</td></tr>"
                + "<tr id='failureDppm'><td class='classafailureDppm'>Failure Dppm</td></tr>");
            // var valuedate = ''; 
            var totalQty = 0, totalFail = 0, totalGoal = 0, totalFailDppm = 0; 
            var stt =1;
            for (i in data) {
                stt++;
                var current = "2020/"+i;
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var gettime = startDate + ' - ' + endDate;
                // console.log("gettime", gettime);
                
                $('#theadLine tr').append('<th  class="dayofmonth"><a class="classDay" data-model="' + gettime + '">' + i + '</a></th>');
               
                if (data[i] == null) {
                    $('#quantity').append('<td></td>');
                    $('#failure').append('<td></td>');
                    $('.classModel').append('<td></td>');
                    // $('#goal').append('<td></td>');
                    $('#failureDppm').append('<td></td>');
                    // $('#target').append('<td></td>');
                }
                else {
                    $('.classstt').html('1');
                    $('.classModel').html('UBX1503');
                    $('#quantity').append('<td>' + data[i].wip + '</td>');
                    $('#failure').append('<td>' + data[i].fail + '</td>');
                    // $('#goal').append('<td>' + data[i].yieldRate.toFixed(2) + '%</td>');
                    $('#failureDppm').append('<td>' + data[i].accumulate.toFixed(2) + '%</td>');
                    // $('#target').append('<td>99%</td>');
                }

            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
</script>