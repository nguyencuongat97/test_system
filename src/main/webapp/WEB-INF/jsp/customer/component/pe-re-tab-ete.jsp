<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/css/custom/customer-pe-re.css" />
<!-- PE RE TAB -->
<!-- <div class="loader"></div> -->
<div class="row">
<div class="col-lg-12" style="background: #272727;">
    <div class="panel panel-overview" style="margin:unset; background-color:#333; color:#ccc; text-align:center;">
        <span style="font-size:18px;"><span class="text-uppercase" name="factory">${factory}</span> - ETE & TOP 3 REPAIR ISSUE </span>
    </div>
    <div class="row" style="margin: unset;">
        <div style="padding: unset; width: 91%; float: left;">
            <div class="input-group" style="border-radius:3px; background-color: #333; color: #fff; height: 26px; margin: 5px 0px;">
                <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;border-bottom: none;">
            </div>
        </div>
        <div class="" style="padding: unset;width: 8.5%;float: right;">
            <div style="width: 100%; display: inline-block;">
                <button class="btn btn-xs" style="width: 97%; margin-top: 5px; margin-bottom: 5px; background:#444; color: #fff;" onclick="exportDataTable()"><i class="fa fa-download"></i></button>
            </div>
        </div>
    </div>
    <div class="row" style="padding: unset;">
        <div class="col-xs-12" style="margin: 5px 0px; ">
            <div id="chart-group-name" class="panel panel-flat panel-body chart-md"></div>
        </div>
        <div class="col-xs-12 pre-scrollable" style="max-height: calc(100vh - 440px)">
            <table class="table table-xxs table-bordered text-nowrap table-sticky" id="tbl-top-3-pe-issue" style="background-color: #fff; font-size: 14px; font-weight: 400;" >
                <thead style="background-color: #545b62; color: #fff" >
                    <tr class="thead">
                        <th style="padding: 5px;" >No</th>
                        <th >UBee Model</th>
                        <th >Model</th>
                        <th colspan="2"> Top 3 issue </th>
                        <th >QTY</th>
                        <th > D.F.R</th>
                        <th >Root cause</th>
                        <th style="z-index: 10;">Attach File</th>
                        <th >Action</th>
                        <th style="z-index: 10;">Due date</th>
                        <th >Owner</th>
                        <th >Status</th>
                        <th style="z-index: 10;">Event</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
</div>
</div>
<style>
.class-dueDate{
    color: #fff;
    padding-left: 0px;
}
.class-textarea span{
    color: #fff;
}
.select-textarea{
    background: #fff;
    height: 33px;
    width: 150%;
    margin-left: -13px;
    margin-top: 0px;
}

.class-dueDate option{
    color: #fff;
    padding-left: 0px;
}
.class-textarea .input-group input{
    color: #fff;
    /* margin-top: 20px; */
}
.class-textarea textarea{
    margin-top: 0px;
}
.class-key-model{
    /* color: #1E88E5; */
    color: #6ab4ef;
}
#tbl-top-3-pe-issue>tbody td {
    /* background: #313132; */
    background: #353535;
    color: #e0e0e3;
    font-size: 14px;
}
.class-textarea textarea{
    color: #e0e0e3;
}
#tbl-top-3-pe-issue tr th{
    background-color: #545b62 !important;
    text-transform: uppercase !important;
    /* position: sticky; */
    color: #fff;
    padding: 5px;
}
.class-button{
    text-align: center;
}
.class-columns{
    padding: 8px;
    padding-bottom: 5px !important;
    padding-top: 5px !important;
}
.model-link{
    font-weight: bold;
}
.loader{
        display: none;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) 
        url('/assets/images/loadingg.gif') 
        50% 50% 
        no-repeat;
    }
</style>
<script src="/assets/js/customerjs/customer-pe-re-tab.js"></script>
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
    loadTableList();
    function loadHighChartWeekAllGroupName(modelName) {
    $.ajax({
        type: "GET",
        url: "/api/test/model/weekly",
        data: {
            factory: dataset.factory,
            modelName: modelName,
            timeSpan: dataset.timeSpan,
            customer: true,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var keys = Object.keys(data);
            var yieldRate = new Array(keys.length);
            for (j in keys) {
                var value = data[keys[j]];
                if (!$.isEmptyObject(value)) {
                    //var ele = parseFloat((value.ete).toFixed(1));
                    var valueETE = parseFloat(((value.output - value.secondFail) * 100.0 / value.output).toFixed(1));
                    yieldRate[j] = { name: keys[j], y: valueETE };
                } else {
                    yieldRate[j] = { name: keys[j], y: null };
                }
            }
            Highcharts.chart('chart-group-name', {
                chart: {
                    type: 'line'
                },
                title: {
                    text: modelName+ ' ETE TREND CHART',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        fontSize: '16px',
                        color: '#fff',
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:11px">'+modelName+'</span><br>',
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
                    // tickInterval: Math.ceil(keys.length / 7),
                    style: {
                        fontSize: '10px',
                    },

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
function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange').data('daterangepicker').setEndDate(new Date(endDate));
                $('input[name=timeSpan]').on('change', function () {
                    dataset.timeSpan = this.value;
                    loadTableList();
                });

                dataset.timeSpan = startDate + ' - ' + endDate;
                // loadTableList();

                delete current;
                delete startDate;
                delete endDate;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

</script>