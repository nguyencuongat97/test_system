<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/solid-gauge.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/checkOut.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<link rel="stylesheet" href="/assets/css/custom/style.css" />
<link rel="stylesheet" href="/assets/css/custom/theme.css" />

<style>
.locked{
    background: #222;
    color: #fff;
    font-weight: bold;
}

.listLC{
    cursor: pointer;
}

.badge-custom{
    padding: 2px 8px 2px 7px;
    font-size: 10px;
    letter-spacing: 0.1px;
    vertical-align: baseline;
    background-color: #df5353 !important;
    border: 1px solid transparent;
    border-radius: 100px;
}
</style>

<div class="panel panel-re panel-flat row">
    <div class="col-lg-12">
        <div class="row" style="margin-bottom: 15px; margin-top: 10px;">
            <div class="input-group col-sm-3" style="float: left; padding: 8px 16px;">
                <span class="input-group-addon" style="padding: 0px 5px; color: #ccc;"><i class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: #ccc;">
            </div>
            <div class="col-sm-3">
                <select class="form-control factory"  style="color: #ccc; border: 1px solid; border-radius: 5px; width: 100px">
                    <option value="B04">B04</option>
                    <option value="B06" selected="true">B06</option>
                </select>
            </div>
            <div class="col-sm-3">
                <select class="form-control line" style="color: #ccc; border: 1px solid; border-radius: 5px; width: 100px">
                    <option value="D1">D1</option>
                    <option value="D2" selected="true">D2</option>
                    <option value="D3">D3</option>
                    <option value="D4">D4</option>
                    <option value="D5">D5</option>
                    <option value="D6">D6</option>
                    <option value="D7">D7</option>

                </select>
            </div>
            <div class="col-sm-2">
                <select class="form-control model_name" style="color: #ccc; border: 1px solid; border-radius: 5px; width: 200px;">
                    <option value="U10C100A30R1055A0" selected="true">U10C100A30R1055A0</option>

                </select>
            </div>
        </div>
        <div class="row">
            <div class="table-responsive pre-scrollable col-sm-2" style="max-height: 485px !important;">
                <table class="table table-custom table-bordered location_code" style="background: #333; color: #ccc; text-align: center; width: 80%; margin-left: 20px;">

                </table>
            </div>
            <div class="col-sm-10">
                <div class="col-xs-4">
                    <div class="panel panel-flat panel-body" style="background: #333; color: #ccc; padding: 10px;">
                        <table class="table table-custom tblDataDetail" style="min-height: 147px;">
                            <tr>
                                <td colspan="2" style="text-align: left">Machine: <span id="machine_name">B61D2AG2</span></td>
                            </tr>
                            <tr>
                                <td colspan="2" style="text-align: left">Error Qty: <span id="error_qty">1</span></td>
                            </tr>
                            <tr>
                                <td style="text-align: left">Slot No: <span id="slot_no">126</span></td>
                                <td style="text-align: left">Slot Pickup: <span id="slot_pickup">100</span></td>
                            </tr>
                            <tr>
                                <td style="text-align: left">Nozzle No: <span id="nozzle_no">2</span></td>
                                <td style="text-align: left">Nozzle Pickup: <span id="nozzle_pickup">126</span></td>
                            </tr>
                        </table>
                    </div>
                </div>
                <div class="col-xs-8">
                    <div class="col-sm-4">
                        <div class="panel panel-flat panel-body chart-xs" id="feeder_chart"></div>
                    </div>
                    <div class="col-sm-4">
                        <div class="panel panel-flat panel-body chart-xs" id="slot_chart"></div>
                    </div>
                    <div class="col-sm-4">
                        <div class="panel panel-flat panel-body chart-xs" id="nozzle_chart"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-sm-6">
                        <div class="panel panel-flat panel-body chart-sm" id="slot_pickup_chart"></div>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                        <div class="panel panel-flat panel-body chart-sm" id="nozzle_pickup_chart"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>

    var dataset = {
        factory: 'B06',
        line: 'D2',
        locationCode: 'C1410',
        modelName: 'U10C10030T',
        block:'1'
    }

    init();

    function init() {
        loadLine();
        loadModelName();
        loadColumnChart();
        loadSolidGaugeChart();
    }

    $("select.factory").change(function(){
        dataset.factory = $(this).children("option:selected").val();
        loadLine();
        loadModelName();
    });

    function loadLine(){
        $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/api/list/line",
            data: {
                factory: dataset.factory
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                $('.line').html("");
                var optLine = '';
                for(i in data){
                    if(data[i] == 'D2'){
                        optLine += '<option value='+data[i]+' selected="true">'+data[i]+'</option>';
                    }
                    else optLine += '<option value='+data[i]+'>'+data[i]+'</option>';
                }
                $('.line').html(optLine);
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }

    $("select.line").change(function(){
        dataset.line = $(this).children("option:selected").val();
        loadModelName();
        //dataset.locationCode = $('.locked .frontLC').html();
        //dataset.modelName = $( ".model_name option:selected" ).val();
        //dataset.block = $('.locked .backLC').html();
        loadColumnChart();
        loadSolidGaugeChart();
    });

    function loadModelName(){
        $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/api/list/modelname/locationcode/bytime",
            data: {
                factory: dataset.factory,
                line: dataset.line
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if (!$.isEmptyObject(data)) {
                    $('.model_name').html("");
                    $('.location_code').html("");
                    var optModel = '';
                    var location_code = '<tr><th>Location Code</th></tr>';
                    $.each(data, function(key, value){
                        optModel += '<option value='+value.modelNameMounter+'>'+value.modelNameMounter+'</option>';
                        dataset.modelName = value.modelNameMounter;
                        var getLocation = value.locationList
                        for(i in getLocation){
                            if(i == 0){
                                 location_code += '<tr class="listLC locked" onclick="selectedLC(this)"><td><span class="frontLC">'+getLocation[i].locationCode+'</span>_<span class="backLC">'+getLocation[i].block+'</span><span class="error_quantity badge-custom pull-right">'+getLocation[i].qty+'</span></td></tr>';
                                 dataset.locationCode = getLocation[i].locationCode;
                                 dataset.block = getLocation[i].block;
                            }
                            else  location_code += '<tr class="listLC" onclick="selectedLC(this)"><td><span class="frontLC">'+getLocation[i].locationCode+'</span>_<span class="backLC">'+getLocation[i].block+'</span><span class="error_quantity badge-custom pull-right">'+getLocation[i].qty+'</span></td></tr>';
                        }
                    });
                    $('.model_name').html(optModel);
                    $('.location_code').html(location_code);
                }
                else{
                    $('.model_name').html("<option value=''>No Model Yet!</option>");
                    $('.location_code').html("<tr><td>No Location Code Yet!</td></tr>");
                }
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }

    function selectedLC(context){
        $('.listLC').removeClass('locked');
        $(context).addClass('locked');
        dataset.locationCode = $('.locked .frontLC').html();
        dataset.modelName = $( ".model_name option:selected" ).val();
        dataset.block = $('.locked .backLC').html();
        loadColumnChart();
        loadSolidGaugeChart();
    }

    function loadColumnChart(){
        $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/api/pickup/feeder/nozzle/by/locationcode",
            data: {
                factory: dataset.factory,
                line: dataset.line,
                locationCode: dataset.locationCode,
                modelName: dataset.modelName,
                block: dataset.block
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                if(!$.isEmptyObject(data)){
                    $('#machine_name').html(data.machine);
                    $('#slot_no').html(data.slotNo);
                    $('#slot_pickup').html(data.slotPickup.toFixed(2)+"%");
                    $('#nozzle_no').html(data.nozzleNo);
                    $('#nozzle_pickup').html(data.nozzlePickup.toFixed(2)+"%");
                    $('#error_qty').html(data.errorNum);

                    var keysNozzle = data.nozzleError;
                    var dataChartNozzle = new Array();
                    var keysSlot = data.slotError;
                    var dataChartSlot = new Array();
                    if(keysNozzle == undefined || keysNozzle.length < 1){
                        dataChartNozzle[0] = {name: 0, y: 0}
                    }
                    else{
                        for(i in keysNozzle){
                            dataChartNozzle[i] = {
                                name: keysNozzle[i].locationCode,
                                y: keysNozzle[i].errorNum,
                            }

                        }
                    }
                    if(keysSlot == undefined || keysSlot.length < 1){
                        dataChartSlot[0] = {name: 0, y: 0}
                    }
                    else{
                        for(i in keysSlot){
                            dataChartSlot[i] = {
                                name: keysSlot[i].locationCode,
                                y: keysSlot[i].errorNum,
                            }

                        }
                    }
                }
                else{
                    $('#machine_name').html("");
                    $('#slot_no').html("");
                    $('#slot_pickup').html("");
                    $('#nozzle_no').html("");
                    $('#nozzle_pickup').html("");
                    $('#error_qty').html("");

                    var dataChartNozzle = new Array();
                    var dataChartSlot = new Array();
                    dataChartNozzle[0] = {name: 0, y: 0};
                    dataChartSlot[0] = {name: 0, y: 0};
                }
                Highcharts.chart('slot_pickup_chart', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'Slot Error',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        },
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                            },
                            cursor: 'pointer',
                            events: {
                                click: function (event) {

                                }
                            }
                        },
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
                    },
                    legend: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series : [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChartSlot
                    }],
                });

                Highcharts.chart('nozzle_pickup_chart', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: {
                        text: 'Nozzle Error',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                            fontSize: '16px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis: {
                        type: 'category',
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: ' '
                        },
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                            },
                            cursor: 'pointer',
                            events: {
                                click: function (event) {
                                }
                            }
                        },
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b>'
                    },
                    legend: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series : [{
                        name: ' ',
                        colorByPoint: true,
                        data: dataChartNozzle
                    }],
                });
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadSolidGaugeChart(){
        Highcharts.chart('feeder_chart', {
            chart: {
                type: 'solidgauge',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: 'Feeder',
            xAxis: {
                type: 'category',
            },
            yAxis: {
                   plotBands: [{
                       from: 1,
                       to: 25,
                       color: '#666',
                       thickness: '40%'
                     }, {
                       from: 26,
                       to: 50,
                       color: '#999',
                       thickness: '40%'
                     }, {
                       from: 51,
                       to: 75,
                       color: '#bbb',
                       thickness: '40%'
                     }, {
                       from: 76,
                       to: 100,
                       color: '#bbb',
                       thickness: '40%'
                    }],
               lineWidth: 0,
               minorTickInterval: 1,
               tickPositions:[0 ,100],
               tickAmount: 1,
               min: 0,
               max: 100,
               stops: [
                   [0.1, '#55BF3B'], // green
                   [0.5, '#DDDF0D'], // yellow
                   [0.9, '#DF5353'] // red
               ],
               title: {
                   y: -60,
                   text: 'Feeder',
                   style: {fontSize: '16px'}
               },
               labels: {
                   y: 16
               }
            },
            plotOptions: {
                solidgauge: {
                    dataLabels: {
                        y: 5,
                        borderWidth: 0,
                    },
                              marker: {
                    enabled: true,
                    symbol: 'triangle',
                    }
                },
            },
            pane: {
                center: ['50%', '85%'],
                size: '140%',
                startAngle: -90,
                endAngle: 90,
                background: {
                    backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
                    innerRadius: '60%',
                    outerRadius: '100%',
                    shape: 'solid'
                }
            },
            tooltip: {
                enabled: false
            },
            legend: {
                enabled: false
            },
            credits: {
                enabled: false
            },
            navigation: {
                buttonOptions: {
                    enabled: false
                }
            },
            series: [{
                name: 'Feeder',
                data: [92],
                dataLabels: {
                    format:
                        '<div style="text-align:center">' +
                        '<span style="font-size:18px">{y}</span>' +
                        '<span style="font-size:18px;opacity:0.8">%</span>' +
                        '</div>'
                },
                tooltip: {
                    valueSuffix: ' %'
                },
            }]
        });

        Highcharts.chart('slot_chart', {
            chart: {
                type: 'solidgauge',
                style: {
                    fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                }
            },
            title: 'Slot',
            xAxis: {
                type: 'category',
            },
            yAxis: {
                   plotBands: [{
                       from: 1,
                       to: 25,
                       color: '#666',
                       thickness: '40%'
                     }, {
                       from: 26,
                       to: 50,
                       color: '#999',
                       thickness: '40%'
                     }, {
                       from: 51,
                       to: 75,
                       color: '#bbb',
                       thickness: '40%'
                     }, {
                       from: 76,
                       to: 100,
                       color: '#bbb',
                       thickness: '40%'
                    }],
               lineWidth: 0,
               minorTickInterval: 1,
               tickPositions:[0 ,100],
               tickAmount: 1,
               min: 0,
               max: 100,
               stops: [
                   [0.1, '#55BF3B'], // green
                   [0.5, '#DDDF0D'], // yellow
                   [0.9, '#DF5353'] // red
               ],
               title: {
                   y: -60,
                   text: 'Slot',
                   style: {fontSize: '16px'}
               },
               labels: {
                   y: 16
               }
            },
            plotOptions: {
                solidgauge: {
                    dataLabels: {
                        y: 5,
                        borderWidth: 0,
                    },
                              marker: {
                    enabled: true,
                    symbol: 'triangle',
                    }
                },
            },
            pane: {
                center: ['50%', '85%'],
                size: '140%',
                startAngle: -90,
                endAngle: 90,
                background: {
                    backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
                    innerRadius: '60%',
                    outerRadius: '100%',
                    shape: 'solid'
                }
            },
            tooltip: {
                enabled: false
            },
            legend: {
                enabled: false
            },
            credits: {
                enabled: false
            },
            navigation: {
                buttonOptions: {
                    enabled: false
                }
            },
            series: [{
                name: 'Slot',
                data: [68],
                dataLabels: {
                    format:
                        '<div style="text-align:center">' +
                        '<span style="font-size:18px">{y}</span>' +
                        '<span style="font-size:18px;opacity:0.8">%</span>' +
                        '</div>'
                },
                tooltip: {
                    valueSuffix: ' %'
                },
            }]
        });

        Highcharts.chart('nozzle_chart', {
                    chart: {
                        type: 'solidgauge',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        }
                    },
                    title: 'Nozzle',
                    xAxis: {
                        type: 'category',
                    },
                    yAxis: {
                           plotBands: [{
                               from: 1,
                               to: 25,
                               color: '#666',
                               thickness: '40%'
                             }, {
                               from: 26,
                               to: 50,
                               color: '#999',
                               thickness: '40%'
                             }, {
                               from: 51,
                               to: 75,
                               color: '#bbb',
                               thickness: '40%'
                             }, {
                               from: 76,
                               to: 100,
                               color: '#bbb',
                               thickness: '40%'
                            }],
                       lineWidth: 0,
                       minorTickInterval: 1,
                       tickPositions:[0 ,100],
                       tickAmount: 1,
                       min: 0,
                       max: 100,
                       stops: [
                           [0.1, '#55BF3B'], // green
                           [0.5, '#DDDF0D'], // yellow
                           [0.9, '#DF5353'] // red
                       ],
                       title: {
                           y: -60,
                           text: 'Nozzle',
                           style: {fontSize: '16px'}
                       },
                       labels: {
                           y: 16
                       }
                    },
                    plotOptions: {
                        solidgauge: {
                            dataLabels: {
                                y: 5,
                                borderWidth: 0,
                            },
                                      marker: {
                            enabled: true,
                            symbol: 'triangle',
                            }
                        },
                    },
                    pane: {
                        center: ['50%', '85%'],
                        size: '140%',
                        startAngle: -90,
                        endAngle: 90,
                        background: {
                            backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
                            innerRadius: '60%',
                            outerRadius: '100%',
                            shape: 'solid'
                        }
                    },
                    tooltip: {
                        enabled: false
                    },
                    legend: {
                        enabled: false
                    },
                    credits: {
                        enabled: false
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    series: [{
                        name: 'Nozzle',
                        data: [2],
                        dataLabels: {
                            format:
                                '<div style="text-align:center">' +
                                '<span style="font-size:18px">{y}</span>' +
                                '<span style="font-size:18px;opacity:0.8">%</span>' +
                                '</div>'
                        },
                        tooltip: {
                            valueSuffix: ' %'
                        },
                    }]
                });
    }

    $(document).ready(function() {
        var spcTS = getTimeSpan();
        $('.datetimerange').data('daterangepicker').setStartDate(spcTS.startDate);
        $('.datetimerange').data('daterangepicker').setEndDate(spcTS.endDate);

        $('input[name=timeSpan]').on('change', function() {
            dataset.timeSpan = this.value;
            init();
        });
    });

</script>