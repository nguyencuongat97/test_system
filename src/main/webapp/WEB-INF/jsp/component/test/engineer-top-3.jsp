<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/task-management.css" />
<style>
    .file-caption{
        height: 50px;
    }
</style>
<div class="row">
    <div class="col-lg-12" style="background: #ffffff;">
        <div class="panel panel-overview" id="header" style="font-size: 18px;">
            <span class="text-uppercase">Engineer Top 3</span>
        </div>
        <div class="row" style="margin: unset;">
            <div>
                <div class="col-sm-9">
                    <div class="input-group">
                        <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
                        <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
                    </div>
                </div>
                <div class="col-sm-3">
                    <select class="form-control bootstrap-select" data-style="btn-xs bg-gray" name="sort">
                        <option value="ALPHABET">ALL</option>
                        <option value="ACTION">ICT</option>
                        <option value="SUCCESS">L+G</option>
                        <option value="TIME">TE-Auto</option>
                        <option value="TIME">OTT/FPT</option>
                    </select>
                </div>
            </div>
            <ul class="col-lg-6 media-list" style="overflow: auto; height: 600px;">
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
                <div class="col-sm-6" id="chart-overview-tasks" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                <div class="col-sm-6" id="chart-top-3-action" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                <div class="col-sm-6" id="chart-top-3-result" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
                <div class="col-sm-6" id="chart-top-3-time" style="height: 300px; border: solid 1px rgba(0, 0, 0, 0.2);"></div>
            </div>
        </div>
    </div>
</div>
<style>
</style>
<script>
     var dataset = {
        factory: '${factory}'
    }
    init();

function init() {

    loadResourceByLevel();
    loadResourceByAction();

    // loadResourceStatus();
    // drawChartOverview();
}
function loadResourceByLevel() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/level',
            data: {
                factory: dataset.factory,
                level: 1
            },
            success: function(data){
                var html = '<option value="ALL">ALL</option>';

                for (i in data) {
                    html += '<option value="' + data[i].employeeNo + '">' + data[i].employeeNo + '</option>';
                }

                $('.bootstrap-select[name="filter-resource"]').html(html);
                $('.bootstrap-select[name="filter-resource"]').selectpicker('refresh');
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }
    function loadResourceByAction() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/action',
            data: {
                factory: dataset.factory
            },
            success: function(data){
                var html = '<option value="ALL">ALL</option>';

                var keys = Object.keys(data);
                for (i in keys) {
                    html += '<option value="' + keys[i] + '">' + data[keys[i]] + '</option>';
                }

                $('.bootstrap-select[name="filter-action"]').html(html);
                $('.bootstrap-select[name="filter-action"]').selectpicker('refresh');
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadStatisticsResourceByTime() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/time',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var actions = new Array();
                var success = new Array();
                var times = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var keys = Object.keys(data);
                    var index = 0;
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            categories[j] = value[j].employeeNo;
                            actions[index] = {y: value[j].taskNumber, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            success[index] = {y: value[j].taskSuccess, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            times[index] = {y: value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }
                }

                Highcharts.chart('chart-action-time', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'ENGINEER ACTION BY TIME'
                    },
                    xAxis: {
                        categories: groups,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: [{
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    }, {
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4,
                        labels: {
                            format: '{value} (m)',
                        },
                        opposite: true
                    }],
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    legend: {
                        enabled: true
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    tooltip: {
                        pointFormat: 'Name: {point.vnName}({point.chineseName})<br/>Level: {point.level}<br/>{series.name}: {point.y}</b>'
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{
                        name: 'Action Times',
                        data: actions
                    }, {
                        yAxis: 1,
                        name: 'Time',
                        data: times
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }
    function loadStatisticsResourceByResult() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/result',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var actions = new Array();
                var success = new Array();
                var times = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var keys = Object.keys(data);
                    var index = 0;
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            categories[j] = value[j].employeeNo;
                            actions[index] = {y: value[j].taskNumber, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            success[index] = {y: value[j].taskSuccess, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            times[index] = {y: value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }
                }

                Highcharts.chart('chart-action-result', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'ENGINEER ACTION BY RESULT'
                    },
                    xAxis: {
                        categories: groups,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: [{
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    }],
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    legend: {
                        enabled: true
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    tooltip: {
                        pointFormat: 'Name: {point.vnName}({point.chineseName})<br/>Level: {point.level}<br/>{series.name}: {point.y}</b>'
                    },
                    series: [{
                        name: 'Action Times',
                        data: actions
                    }, {
                        name: 'Success',
                        color: '#90ee7e',
                        data: success
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadStatisticsOfResourceByTime(employeeNo) {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/time/'+employeeNo,
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var actions = new Array();
                var times = new Array();
                var categories = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    categories = Object.keys(data);
                    for(i in categories){
                        var value = data[categories[i]];
                        actions[i] = {y: value[0]};
                        times[i] = {y: value[1]};
                    }
                }

                Highcharts.chart('chart-action-time', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: employeeNo + ' ENGINEER ACTION BY TIME'
                    },
                    xAxis: {
                        categories: categories,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: [{
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    }, {
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4,
                        labels: {
                            format: '{value} (m)',
                        },
                        opposite: true
                    }],
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    legend: {
                        enabled: true
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
                        name: 'Action Times',
                        data: actions
                    }, {
                        yAxis: 1,
                        name: 'Time',
                        data: times
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadStatisticsOfResourceByResult(employeeNo) {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/result/'+employeeNo,
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var actions = new Array();
                var success = new Array();
                var categories = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    categories = Object.keys(data);

                    for(i in categories){
                        var value = data[categories[i]];
                        actions[i] = {y: value[0]};
                        success[i] = {y: value[1]};
                    }
                }

                Highcharts.chart('chart-action-result', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: employeeNo + ' ENGINEER ACTION BY RESULT'
                    },
                    xAxis: {
                        categories: categories,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: [{
                        min: 0,
                        title: {
                            text: ''
                        },
                        stackLabels: {
                            enabled: true,
                            style: {
                                fontWeight: 'bold',
                                color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                            }
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
                        tickAmount: 4
                    }],
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    legend: {
                        enabled: true
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
                        name: 'Action Times',
                        data: actions
                    }, {
                        name: 'Success',
                        color: '#90ee7e',
                        data: success
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }

    function loadResourceExperience(level, chart, name, color) {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/experience',
            data: {
                factory: dataset.factory,
                shift: dataset.shift,
                level: level
            },
            success: function(data){
                var experiences = new Array();
                var groups = new Array();

                if (!$.isEmptyObject(data)) {
                    var rows = "";
                    var keys = Object.keys(data);
                    var index = 0;
                    for(i in keys){
                        var value = data[keys[i]];
                        var categories = new Array(value.length);
                        for (j in value) {
                            categories[j] = value[j].employeeNo;
                            experiences[index] = {y: value[j].experienceYears, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};
                            index = index + 1;
                        }
                        groups[i] = {name: keys[i], categories: categories}
                    }
                }

                Highcharts.chart(chart, {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: name
                    },
                    xAxis: {
                        type: 'category',
                        categories: groups,
                        labels:{
                            style: {
                                fontSize: '10px'
                            },
                            autoRotation: [-90]
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ''
                        },
                        minorGridLineWidth: 0,
                        minTickInterval: 1,
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
                    tooltip: {
                        pointFormat: 'Name: {point.vnName}({point.chineseName})<br/>Level: {point.level}<br/>{series.name}: {point.y}</b>'
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: true,
                            },
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{
                        name: 'Experience Years',
                        color: color,
                        data: experiences
                    }]
                });
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
       });
    }
    function loadResourceStatus() {
        $.ajax({
            type: 'GET',
            url: '/api/test/resource/status',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.rsTimeSpan,
                solutionId: dataset.solutionId
            },
            success: function(data){
                var html = '';
                var actions = new Array();
                var success = new Array();
                var times = new Array();
                var idleTimes = new Array();
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
                            idleTimes[index] = {name: value[j].employeeNo, y: 300-value[j].processingTime, vnName: value[j].name, chineseName: value[j].chineseName, level: value[j].level};

                            html +=  '<li class="resource">'+
                                     '<div class="rcontent">'+
                                         '<div class="col-xs-12 col-sm-6 information">'+
                                             '<div class="row">'+
                                                 '<div class="col-xs-2 rank '+ranking(index+1)+'">'+
                                                     '<label style="margin: unset;">'+(index+1)+'</label>'+
                                                 '</div>'+
                                                 '<div class="col-xs-3" style="padding: 0px 3px;">'+
                                                     '<div class="avatar">'+
                                                        (value[j].avatar != null ? '<img class="" src="'+value[j].avatar+'" alt>' : '')+
                                                     '</div>'+
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
                        drawChartStatistics('chart-' + i, [actions[i]], [success[i]], [times[i]], [idleTimes[i]]);
                    }
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
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
        $.ajax({
            type: 'GET',
            url: '/api/test/tracking/status',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.rsTimeSpan
            },
            success: function(data){

                Highcharts.chart('chart-overview-tasks',{
                    chart:{
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type:'pie',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'TASKS STATUS'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f}%',
                                inside: true,
                                distance: -10

                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: [{name: 'Auto Closed', y: data.auto_closed}, {name: 'Done', y: data.done}, {name: 'Not Yet', y: data.not_yet}]
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
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });

        $.ajax({
            type: 'GET',
            url: '/api/test/resource/status/top3',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId,
                timeSpan: dataset.rsTimeSpan
            },
            success: function(data){
                var dataChart = [];
                var keys = Object.keys(data);
                for (i in keys) {
                    var value = data[keys[i]];
                    dataChart.push({name: keys[i], y: value});
                }

                Highcharts.chart('chart-top-3-action',{
                    chart:{
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type:'pie',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'TOP 3 ENGINEER BY ACTION TIMES'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f}%',
                                inside: true,
                                distance: -10

                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
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
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });

        $.ajax({
            type: 'GET',
            url: '/api/test/resource/result/top3',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId,
                timeSpan: dataset.rsTimeSpan
            },
            success: function(data){
                var dataChart = [];
                var keys = Object.keys(data);
                for (i in keys) {
                    var value = data[keys[i]];
                    dataChart.push({name: keys[i], y: value});
                }

                Highcharts.chart('chart-top-3-result',{
                    chart:{
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type:'pie',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'TOP 3 ENGINEER BY RESULT'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f}%',
                                inside: true,
                                distance: -10
                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
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
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });

        $.ajax({
            type: 'GET',
            url: '/api/test/resource/time/top3',
            data: {
                factory: dataset.factory,
                solutionId: dataset.solutionId,
                timeSpan: dataset.rsTimeSpan
            },
            success: function(data){
                var dataChart = [];
                var keys = Object.keys(data);
                for (i in keys) {
                    var value = data[keys[i]];
                    dataChart.push({name: keys[i], y: value});
                }

                Highcharts.chart('chart-top-3-time',{
                    chart:{
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type:'pie',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        }
                    },
                    title: {
                        text: 'TOP 3 ENGINEER BY TIME'
                    },
                    xAxis: {
                        type: 'category'
                    },
                    yAxis: {
                        title: {
                            text: ''
                        },
                        labels: {
                            enabled: false
                        }
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '{point.percentage:.1f}%',
                                inside: true,
                                distance: -10
                            },
                            showInLegend: true
                        }
                    },
                    series: [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChart
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
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }
    function drawChartStatistics(chartId, actions, success, times, idleTimes) {
        Highcharts.chart(chartId, {
            chart: {
                type: 'bar',
                spacing: [10, 20, 10, 20],
                margin: [0, 0, 0, 0],
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                }
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
                minTickInterval: 4,
                tickAmount: 5
            }, {
                min: 0,
                title: {
                    text: ''
                },
                minorGridLineWidth: 0,
                minTickInterval: 60,
                tickAmount: 5,
                labels: {
                    format: '{value} (m)',
                },
                opposite: true
            }],
            plotOptions: {
                bar: {
                    dataLabels: {
                        enabled: true,
                        inside: true,
                        align: 'left',
                        format: '{series.name} - {point.y}'
                    }
                },
                series: {
                    groupPadding: 0
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
            }, {
                yAxis: 1,
                name: 'Idle Time',
                data: idleTimes
            }]
        });
    }
    function getTimeNow(){
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var startDate1 = moment(current).add(-6,"day").format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1,"day").format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimerange[name="timeSpan"]').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimerange[name="timeSpan"]').data('daterangepicker').setEndDate(new Date(endDate));
                // $('.datetimerange[name="timeSpan1"]').data('daterangepicker').setStartDate(new Date(startDate1));
                // $('.datetimerange[name="timeSpan1"]').data('daterangepicker').setEndDate(new Date(endDate));
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
    $("#txtFilterResource").on("keyup", function() {
        var value = $(this).val().toLowerCase();
       $("table[name='resource']>tbody>tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $(document).ready(function() {
        getTimeNow();
        // var spcTS = getTimeSpan(6);
        // $('.datetimerange[name="timeSpan1"]').data('daterangepicker').setStartDate(spcTS.startDate);
        // $('.datetimerange[name="timeSpan1"]').data('daterangepicker').setEndDate(spcTS.endDate);

        // var rsTS = getTimeSpan();
        // $('.datetimerange[name="timeSpan"]').data('daterangepicker').setStartDate(rsTS.startDate);
        // $('.datetimerange[name="timeSpan"]').data('daterangepicker').setEndDate(rsTS.endDate);

        $('select[name="filter-shift"]').on('change', function() {
            dataset.shift = this.value;
            loadInformation();
        });

        $('select[name="filter-action"]').on('change', function() {
            if (this.value == 'ALL') {
                dataset.solutionId = null;
            } else {
                dataset.solutionId = this.value;
            }
            loadStatistics();
        });

        $('select[name="filter-resource"]').on('change', function() {
            if (this.value == 'ALL') {
                loadStatistics();
            } else {
                loadStatisticsOfResourceByResult(this.value);
                loadStatisticsOfResourceByTime(this.value);
            }
        });

        $('input[name=timeSpan]').on('change', function(event) {
            dataset.rsTimeSpan = event.target.value;
            loadResourceStatus();
            drawChartOverview();
        });
    });

</script>