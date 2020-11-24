<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/css/custom/customer-line.css" />

<div class="row">
    <div class="col-lg-12" style="background: #272727;">
        <div class="panel panel-overview" id="header">
            <span class="text-uppercase">B06 - Total Line Dashboard</span>
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
        <div class="row" style="margin: unset;">
            <div id="chart-line-name" class="panel panel-flat panel-body chart-sm"></div>
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
.classDay{
    color: #fff;
    /* text-shadow: 1px 1px 1px; */
    font-weight: bold;
    text-decoration: underline;
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
            $('#theadLine').html('<tr><th>Date</th></tr>');
            $('#tbodyLine').html("<tr id='input'><td class='classinput'>Input</td></tr>"
                + "<tr id='defect'><td class='classdefect'>Defect Qty</td></tr>"
                + "<tr id='yield'><td class='classyield'>SMT Yield Rate</td></tr>"
                + "<tr id='acumulate'><td class='classacumulate'>Accumulate</td></tr>"
                + "<tr id='target'><td class='classtarget'>Target</td></tr>");
            // var valuedate = ''; 
            for (i in data) {
                var current = "2020/"+i;
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var gettime = startDate + ' - ' + endDate;
                // console.log("gettime", gettime);
                $('#theadLine tr').append('<th  class="dayofmonth"><a class="classDay" data-model="' + gettime + '">' + i + '</a></th>');
               
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
                    $('#yield').append('<td>' + data[i].yieldRate.toFixed(2) + '%</td>');
                    $('#acumulate').append('<td>' + data[i].accumulate.toFixed(2) + '%</td>');
                    $('#target').append('<td>99%</td>');
                }

            }
            $('.classDay').on('click', function (event) {
                console.log(this.dataset.model);
                    var url = "/customer/customer-line?factory=" + dataset.factory + "&timeSpan=" +this.dataset.model;
                    // openWindow(url);
                    window.location.href = url;

                });
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
                    text: 'TOTAL LINE TREND CHART',
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
                            align: 'right'
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
</script>