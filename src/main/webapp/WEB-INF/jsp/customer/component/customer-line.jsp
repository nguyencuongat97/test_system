<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/css/custom/customer-line.css" />
<!-- <link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" /> -->

<!-- <link rel="stylesheet" href="/assets/css/custom/station-dashboard.css" /> -->

<div class="row">
    <div class="col-lg-12" style="background: #272727;">
        <div class="panel panel-overview" id="header">
            <span class="text-uppercase">B06 - Line Dashboard</span>
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
        <div class="row" style="padding: unset;">
            <div class="col-xs-12 pre-scrollable"
                style="margin: 10px 0px; max-height: calc(100vh - 165px);">
                <table class="table table-xxs table-bordered text-nowrap table-sticky" id="tbl-line-dashBoard"
                    style="background-color: #fff; font-size: 15px; font-weight: 400;">
                    <thead style="background-color: #545b62; color: #fff">
                        <tr class="thead">
                            <th style="/* padding: 5px; */width: 1%;">No</th>
                            <th style="width: 8%;">Line Name</th>
                            <th style="width: 9%;">Model Name</th>
                            <th style="width: 8%;"> Side </th>
                            <th style="width: 8%;" >UBEE Name</th>
                            <th style="width: 5%;"> Pass</th>
                            <th style="width: 5%;">Fail</th>
                            <th style="width: 6%;">Total</th>
                            <!-- <th style="width: 7%;">Hit Rate</th> -->
                            <th style="width: 8%;">Yield Rate</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<!-- Show modal -->
<div class="modal fade" id="modalLine" role="dialog">
    <div class="modal-dialog modal-xl">
        <div class="modal-content" style="background-color: #272727; color: #ccc;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross"
                        style="color: #ccc"></i></button>
                <div class="modal-title">
                    <span style="font-weight:bold" id="idLine"></span>
                </div>
            </div>
            <div class="modal-body" style="margin-bottom: -25px;">
                <div class="row">
                    <div class="table-responsive pre-scrollable" style="color: #333333; max-height: calc(100vh - 165px);">
                        <table id="tblModelLine" class="table table-bordered table-xxs"
                            style="margin-bottom: 10px; color: #fff;">
                            <thead id='theadLine'>
                            </thead>
                            <tbody id='tbodyLine'>
    
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="row">
                    <div id="chart-line-name" class="panel panel-flat panel-body chart-sm"></div>
                </div>  
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
#tbl-line-dashBoard thead th {
    background-color: #545b62 !important;
    text-transform: uppercase !important;
    position: sticky;
    color: #fff;
    padding: 5px;
    text-align: center;
}
#tbl-line-dashBoard tbody td{
    text-align: center;
    /* background: #353535;
     color: #e0e0e3;
    font-size: 14px;
    text-align: center; */
}
#tblModelLine thead th {
    background-color: #545b62 !important;
    text-transform: uppercase !important;
    position: sticky;
    color: #fff;
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
/* #chart-line-name .highcharts-container .highcharts-series-label text{
    display: none;
} */
</style>

<script>    
    var dataset = {
        factory: '${factory}',
        modelName: '${modelName}',
        timeSpan: '${timeSpan}',
        timeout: {},
        selectedModel: 'all',
        status: 'all',
        style: 'new-style'
    };
    // console.log("timeeee:::", dataset.timeSpan);
    // getTimeNow();
    // function getTimeNow() {
    //     $.ajax({
    //         type: "GET",
    //         url: "/api/time/now",
    //         contentType: "application/json; charset=utf-8",
    //         success: function (data) {
    //             var current = new Date(data);

    //             var current_url = window.location.href;
    //             var url = new URL(current_url);
    //             var timeSpan = url.searchParams.get('timeSpan');
                
    //             var time = timeSpan.split(' - ');
    //             console.log('time ' + time);
    //             if (time.length = 2) {
    //                 defaultTimeSpan = false;
    //                 $('.datetimerange').data('daterangepicker').setStartDate(moment(time[0], "YYYY/MM/DD HH:mm"));
    //                 $('.datetimerange').data('daterangepicker').setEndDate(moment(time[1], "YYYY/MM/DD HH:mm"));
    //             }
    //             $('input[name=timeSpan]').on('change', function () {
    //                 //alert("bbbbbbb");
    //                 dataset.timeSpan = this.value;
    //                 TableLineList();
    //             });

    //             dataset.timeSpan = timeSpan;
    //             TableLineList();
    //             delete current;
    //             delete timeSpan;
    //         },
    //         failure: function (errMsg) {
    //             console.log(errMsg);
    //         }
    //     });
    // }

    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    function searchParams(name){
        var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
        if (results == null){
            return null;
        }
        else {
            return decodeURIComponent(results[1]) || 0;
        }
    }
    function getParameter(){
        var urlString = window.location.href;
        var timeSpan = searchParams("timeSpan");
      
        if(timeSpan == undefined || timeSpan == null){
            getTimeNow();
        }
        else{
            var time = timeSpan.split(' - ');
            $('input[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(time[0]));
            $('input[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(time[1]));
            dataset.timeSpan = timeSpan ;
            TableLineList();
        }
    }
    // TableLineList()
function TableLineList() {
    // $("#tbl-line-dashBoard tbody").html('');
    $.ajax({
        type: "GET",
        url: "/api/smt/line/all",
        data: {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan,
            customer: true,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            if (!$.isEmptyObject(data)) {
                var html = '';
                var number = 0;
                var totalHR = 0, totalYR = 0, totalFail = 0, total = 0, totalPass = 0, totalUph = 0;
                    for (i in data) {
                        var valuetotal, valuePass;
                        valuePass = data[i].wip - data[i].fail;
                        valuetotal = valuePass + data[i].fail;

                        var valueCusName = data[i].customerName;
                        if (valueCusName== null || valueCusName=='') {
                            valueCusName ='';
                        } else{
                            valueCusName = data[i].customerName;
                        }

                        number++;
                        html += '<tr id="row' + number + '"><td>' + number + '</td>'
                            + '<td class="text-semibold" data-toggle="modal" data-target="#modalLine" onclick=showModalLine("' + data[i].lineName + '") title="Click to show modal line">'
                            + '<a class="class-line" >' + data[i].lineName + '</a></td>'
                            + '<td>' + data[i].modelName + '</td>'
                            + '<td>' + data[i].groupName + '</td>'
                            + '<td>'+ valueCusName +'</td>'
                            + '<td>' + valuePass + '</td>'
                            + '<td>' + data[i].fail + '</td>'
                            + '<td>' + valuetotal + '</td>'
                            // + '<td class=" styletextshad ' + convertStatus(data[i].hitRate, 98, 99) + '">' + data[i].hitRate.toFixed(2) + '%</td>'
                            + '<td class=" styletextshad ' + convertStatus1(data[i].yieldRate, 98, 99) + '">' + data[i].yieldRate.toFixed(2) + '%</td>'
                            + '</tr>';

                            // totalHR += data[i].hitRate;
                            totalFail += data[i].fail;
                            total += valuetotal;
                            totalPass += valuePass;
                            totalUph += data[i].uph;
                    }
                
                    if(totalUph < 0 || totalUph == 0 ){
                        totalHR = 0;
                    } else{
                        totalHR = totalPass*100 / totalUph;
                    }
                    if(total < 0 || total == 0 ){
                        totalYR = 0;
                    } else{
                        totalYR = totalPass*100 / total;
                    }

                    html += '<tr id="total"><td></td>'
                        // + '<td class="text-semibold" data-toggle="modal" data-target="#modalLine" onclick = showModelTotal()><a class="class-line">TOTAL</a></td>'
                        + '<td>TOTAL</td>'
                        + '<td colspan="3"></td>'
                        // + '<td></td>'
                        + '<td>'+totalPass+'</td>'
                        // + '<td></td>'
                        + '<td>'+totalFail+'</td>'
                        + '<td>'+total+'</td>'
                        // + '<td>'+totalHR.toFixed(2)+'%</td>'
                        + '<td>'+totalYR.toFixed(2)+'%</td>'
                        + '</tr>';
                    $("#tbl-line-dashBoard tbody").html(html);
                } else{
                    $('#tbl-line-dashBoard tbody').html('<tr><td colspan="9" align="center">-- NO ERROR --</td></tr>');
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
function convertStatus1(rate, errorSpec, warningSpec) {
    if (rate < errorSpec) {
        return 'bg-red';
    }
    if (rate < warningSpec) {
        return 'bg-yellow';
    }
    if (rate >= warningSpec) {
        return 'bg-green';
    }
}

function showModalLine(line) {
        $('#idLine').html(line);
        $('#theadLine').html('');
        $('#tbodyLine').html('');
        $.ajax({
            type: "GET",
            url: "/api/smt/line/weekly",
            data: {
                factory: dataset.factory,
                // timeSpan: dataset.timeSpan,
                lineName: line,
                customer: true,
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                // console.log("data error: ", data);

                $('#theadLine').html('<tr><th>Date</th></tr>');
                $('#tbodyLine').html("<tr id='input'><td class='classinput'>Input</td></tr>"
                    + "<tr id='defect'><td class='classdefect'>Defect Qty</td></tr>"
                    + "<tr id='yield'><td class='classyield'>SMT Yield Rate</td></tr>"
                    + "<tr id='acumulate'><td class='classacumulate'>Accumulate</td></tr>"
                    + "<tr id='target'><td class='classtarget'>Target</td></tr>");
                for (i in data) {
                    $('#theadLine tr').append('<th  class="dayofmonth">' + i + '</th>');
                    // console.log('dddd', data[i])
                    if (data[i] == null) {
                        $('#input').append('<td></td>');
                        $('#defect').append('<td></td>');
                        $('#yield').append('<td></td>');
                        $('#acumulate').append('<td></td>');
                        $('#target').append('<td></td>');
                    }
                    else {
                        $('#input').append('<td>' + data[i].wip + '</td>');
                        $('#defect').append('<td>' + data[i].fail + '</td>');
                        $('#yield').append('<td>' + data[i].yieldRate.toFixed(2) + '</td>');
                        $('#acumulate').append('<td>' + data[i].accumulate.toFixed(2) + '</td>');
                        $('#target').append('<td>99%</td>');
                    }

                }
            //chart
            var keys = Object.keys(data);
        // console.log("keysss:::",keys);
            var yield_rate = new Array(keys.length);
            var accumulate = new Array(keys.length);
            // var target = new Array(keys.length);
            var defect = new Array(keys.length);
            for (j in keys) {
                var value = data[keys[j]];
                if (!$.isEmptyObject(value)) {
                    var dft = value.fail;
                    var yt = parseFloat(value.yieldRate.toFixed(2));
                    var mc = parseFloat(value.accumulate.toFixed(2));
                    // var tg = parseFloat(value.hitRate.toFixed(2));
                    
                    defect[j] = { name: keys[j], y: dft };
                    yield_rate[j] = { name: keys[j], y: yt };
                    accumulate[j] = { name: keys[j], y: mc };
                    // target[j] = { name: keys[j], y: tg };
                } else {
                    defect[j] = { name: keys[j], y: 0 };
                    yield_rate[j] = { name: keys[j], y: 0 };
                    accumulate[j] = { name: keys[j], y: 0 };
                    // target[j] = { name: keys[j], y: 0 };
                }
            }
            Highcharts.chart('chart-line-name', {
                chart: {
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                    }
                },
                title: {
                    text: line +' - LINE TREND CHART',
                    style: {
                        fontSize: '16px',
                        fontWeight: 'bold',
                        color: '#ccc',
                    }
                },
                xAxis: {
                    type: 'category',
                    // tickInterval: Math.ceil(keys.length / 7),
                },
                yAxis: [{ // Primary yAxis
                    title: {
                        text: '',
                    },
                }, { // Secondary yAxis
                    plotLines: [{
                        value: 99,
                        width: 1.5,
                        dashStyle: 'shortdash',
                        color: '#fff',
                        label: {
                            text: 'Target: 99%',
                            style: {
                                color: '#fff',
                                fontWeight: 'bold',
                            },
                            align: 'right',
                        },
                    }],
                    softMin: 98.90,
                    title: {
                        text: '',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value}%',
                    },
                    opposite: true
                }],
                plotOptions: {
                    spline: {
                        label: {
                            enabled: false,
                        },
                    }
                },
                tooltip: {
                    shared: true
                },
                legend: {
                    style: {
                        fontSize: '11px'
                    },
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom'
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
                    name: 'Defect Qty',
                    type: 'column',
                    yAxis: 0,
                    data: defect,
                    dataLabels: {
                        enabled: true,
                        // format: '{y}%'
                    }
                }, 
                {
                    name: 'Yield Rate',
                    type: 'spline',
                    yAxis: 1,
                    data: yield_rate,
                    dataLabels: {
                        enabled: true,
                        format: '{y}%'
                    },
                    tooltip: {
                        valueSuffix: '%'
                    },
                    color: Highcharts.getOptions().colors[1]

                },
                {
                    name: 'Accumulate',
                    type: 'spline',
                    yAxis: 1,
                    data: accumulate,
                    dataLabels: {
                        enabled: true,
                        format: '{y}%'
                    },
                    tooltip: {
                        valueSuffix: '%'
                    },
                    color: Highcharts.getOptions().colors[2]

                },
            ]
            });
            },
            failure: function (errMsg) {
                console.log(errMsg);
            },
        });
}
$(document).ready(function() {
    getParameter();
    $('input[name=timeSpan]').on('change', function() {
        dataset.timeSpan = this.value;
        TableLineList();
    });
});

</script>