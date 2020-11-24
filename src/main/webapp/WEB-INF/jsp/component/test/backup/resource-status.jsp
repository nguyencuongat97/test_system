<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<style>
    .form-xs {
        height: 26px;
    }
    .btn-xs {
        width: 26px;
        height: 26px;
        padding: 3px;
        margin: 0px 2px;
    }
    .sm-padding {
        margin-bottom: 5px;
    }
    .resource {
        padding: 5px;
    }
    .resource .rcontent {
        width: 100%;
        height: max-content;
        display: table;
        background-color: #fff;
        border-radius: 3px;
        border: solid 1px rgba(0, 0, 0, 0.2);
    }
    .resource .rcontent .information {
        border-right: solid 1px rgba(0, 0, 0, 0.2);
    }
    .resource .information .avatar {
        width: 70px;
        height: 70px;
        position: relative;
        top: 15px;
    }
    .resource .information .rank {
        width: 26px;
        height: 26px;
        position: relative;
        top: 38px;
        padding: 3px 5px;
        margin-left: 5px;
        background-color: #7cb5ec;
        display: table;
        text-align: center;
    }
    .resource .information .rank.rank-1 {
        background-color: #e6717c;
    }
    .resource .information .rank.rank-2 {
        background-color: #ffda6a;
    }
</style>
<div class="row" style="padding: 10px; background: #fff;">
    <div>
        <div class="col-sm-9">
            <div class="input-group">
                <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
            </div>
        </div>
        <div class="col-sm-3">
            <select class="form-control bootstrap-select" data-style="btn-xs bg-gray" name="sort">
                <option value="ALPHABET">A-Z</option>
                <option value="ACTION">ACTION</option>
                <option value="SUCCESS">SUCCESS</option>
                <option value="TIME">TIME</option>
            </select>
        </div>
    </div>
    <ul class="col-lg-6 media-list" style="overflow: auto; height: 500px;">
        <li class="resource">
            <div class="rcontent">
                <div class="col-sm-6 information">
                    <div class="row">
                        <div class="col-xs-1" style="width: 26px; height: 26px; position: relative; top: 38px; padding: 2px 8px; margin-left: 5px;">
                            <label style="margin: unset;">1</label>
                        </div>
                        <div class="col-xs-3" style="padding: 0px 3px;">
                            <img class="avatar" src="/assets/images/avatar-default-icon.png" alt>
                        </div>
                        <div class="col-xs-8 form-horizontal" style="font-size: 12px; padding-top: 8px; margin-left: -8px; margin-right: -8px">
                            <div class="form-group sm-padding">
                                <label class="col-xs-12 text-bold" name="name" style="padding: 0px 10px; margin: unset;">Nguyễn Đức Tiến</label>
                            </div>
                            <div class="form-group sm-padding">
                                <label class="col-xs-12" name="employeeNo" style="padding: 0px 10px; margin: unset;">V0946495</label>
                            </div>
                            <div class="form-group sm-padding">
                                <label class="col-xs-12" name="experience" style="padding: 0px 10px; margin: unset;">2018/11/10</label>
                            </div>
                            <div class="form-group sm-padding">
                                <label class="col-xs-12" name="level" style="padding: 0px 10px; margin: unset;">S</label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6" id="chart-V0946495" style="height: 100px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
            </div>
        </li>
    </ul>
    <div class="col-lg-6">
        <div class="col-sm-6" id="chart-top-3-action" style="height: 250px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
        <div class="col-sm-6" id="chart-top-3-result" style="height: 250px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
        <div class="col-sm-12" id="chart-top-3-time" style="height: 250px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
    </div>
</div>

<script>
    //var dataset = {
    //    factory: '${factory}'
    //}

    loadStatisticsResourceByTime();
    drawChartOverview();

    function loadStatisticsResourceByTime() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/time',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var html = '';
                var actions = new Array();
                var success = new Array();
                var times = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var index = 0;
                    var keys = Object.keys(data);
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            categories[j] = value[j].employeeNo;
                            actions[index] = {name: value[j].employeeNo, y: value[j].taskNumber, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            success[index] = {name: value[j].employeeNo, y: value[j].taskSuccess, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            times[index] = {name: value[j].employeeNo, y: value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};

                            html +=  '<li class="resource">'+
                                     '<div class="rcontent">'+
                                         '<div class="col-xs-12 col-sm-6 information">'+
                                             '<div class="row">'+
                                                 '<div class="col-xs-2 rank '+ranking(index+1)+'">'+
                                                     '<label style="margin: unset;">'+(index+1)+'</label>'+
                                                 '</div>'+
                                                 '<div class="col-xs-3" style="padding: 0px 3px;">'+
                                                     '<img class="avatar" src="/assets/images/avatar-default-icon.png" alt>'+
                                                 '</div>'+
                                                 '<div class="row col-xs-8 form-horizontal" style="font-size: 12px; padding-top: 8px; margin-left: -8px; margin-right: -8px">'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 text-bold" name="name" style="padding: 0px 10px; margin: unset;">'+value[j].name+'('+value[j].chineseName+')'+'</label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 " name="employeeNo" style="padding: 0px 10px; margin: unset;">'+value[j].employeeNo+'</label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-12 " name="experience" style="padding: 0px 10px; margin: unset;">'+value[j].experience+'</label>'+
                                                     '</div>'+
                                                     '<div class="form-group sm-padding">'+
                                                         '<label class="col-xs-6 " name="level" style="padding: 0px 10px; margin: unset;">'+value[j].level+'</label>'+
                                                         '<label class="col-xs-6 text-right" name="level" style="padding: 0px 10px; margin: unset;">'+value[j].status+'</label>'+
                                                     '</div>'+
                                                 '</div>'+
                                             '</div>'+
                                         '</div>'+
                                         '<div class="col-xs-12 col-sm-6" id="chart-'+index+'" style="height: 100px;"></div>'+
                                     '</div>'+
                                 '</li>';

                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }

                    $('.media-list').html(html);
                    for(i=0; i<index; i++) {
                        drawChartStatistics('chart-' + i, [actions[i]], [success[i]], [times[i]]);
                    }
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function drawChartStatistics(chartId, actions, success, times) {
        Highcharts.chart(chartId, {
            chart: {
                type: 'bar',
                margin: [0, 0, 0, 0]
            },
            title: {
                text: ''
            },
            xAxis: {
                labels:{
                    enabled: false
                },
                gridLineWidth: 0,
                minorGridLineWidth: 0,
            },
            yAxis: [{
                min: 0,
                title: {
                    text: ''
                },
                minorGridLineWidth: 0,
                minTickInterval: 1,
                tickAmount: 4
            }, {
                min: 0,
                title: {
                    text: ''
                },
                minorGridLineWidth: 0,
                minTickInterval: 1,
                tickAmount: 4,
                labels: {
                    format: '{value} (m)',
                },
                opposite: true
            }],
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
                name: 'Action',
                data: actions
            }, {
                name: 'Success',
                data: success
            }, {
                yAxis: 1,
                name: 'Time',
                data: times
            }]
        });
    }

    function ranking(rank) {
        if (rank == 1)
            return "rank-1";
        else if (rank < 4)
            return "rank-2";
        return "rank-3";
    }

    function drawChartOverview() {
        Highcharts.chart('chart-top-3-action',{
            chart:{
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type:'pie'
            },
            title: {
                text: 'TOP 3 ENGINEER BY ACTION TIMES'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                name: ' ',
                colorByPoint: true,
                data: [{name: 'V091705', y: 7}, {name: 'V0900626', y: 4}, {name: 'V0924101', y: 2}, {name: 'Other', y: 6}]
            }],
            credits:{
                enabled:false,
            },
            legend: {
                style: {
                    fontSize: '11px'
                },
                layout: 'horizontal',
                align: 'left',
                verticalAlign: 'bottom'
            }
        });

        Highcharts.chart('chart-top-3-result',{
            chart:{
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type:'pie'
            },
            title: {
                text: 'TOP 3 ENGINEER BY RESULT'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },
            series: [{
                name: ' ',
                colorByPoint: true,
                data: [{name: 'V091705', y: 5}, {name: 'V0900626', y: 4}, {name: 'V0924101', y: 1}, {name: 'Other', y: 4}]
            }],
            credits:{
                enabled:false,
            },
            legend: {
                style: {
                    fontSize: '11px'
                },
                layout: 'horizontal',
                align: 'left',
                verticalAlign: 'bottom'
            }
        });

        Highcharts.chart('chart-top-3-time',{
            chart:{
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type:'column'
            },
            title: {
                text: 'TOP 3 ENGINEER BY TIME'
            },
            xAxis: {
                type: 'category'
            },
            series: [{
                name: ' ',
                colorByPoint: true,
                data: [{name: 'V091705', y: 90}, {name: 'V0900626', y: 50}, {name: 'V0924101', y: 20}, {name: 'Other', y: 120}]
            }],
            credits:{
                enabled:false,
            },
            legend: {
                enabled: false
            }
        });
    }

    $(document).ready(function() {
        var spcTS = getTimeSpan(7);
        $('input[name="timeSpan"]').data('daterangepicker').setStartDate(spcTS.startDate);
        $('input[name="timeSpan"]').data('daterangepicker').setEndDate(spcTS.endDate);
    });

</script>
