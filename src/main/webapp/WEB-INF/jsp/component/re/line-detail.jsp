<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/bc8m.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<script type='text/javascript' src='/assets/custom/css/re/x3dom.js'> </script>
<link rel='stylesheet' type='text/css' href='/assets/custom/css/re/x3dom.css'/>

<div class="panel panel-re panel-flat row">
    <div class="panel panel-overview input-group" style="margin-bottom: 5px; padding-left: 10px; background: #333;">
        <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
        <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: #fff;">
    </div>

    <div class="row" style="margin: unset;">
        <div class="col-xs-12" style="position: relative; min-height: 610px;">
          <%@ include file="filex3d.jsp" %>
          <!-- <img src="\assets\images\wrl\Bone_Pile.png"/> -->
            <div style="margin: 10px 10px 0px 0px; z-index:1000; right:260px; top:0px;position:absolute;">
                <a class="btn btn-lg" onclick='document.getElementById("x3dElement").runtime.resetView();' style="font-size: 13px; padding: 5px; border-radius: 10px; color: #ccc">
                Reset View
                </a>
            </div>
            <div class="" style="position: absolute; top: 0px; right: 10px; z-index: 10;">
                <div id="defectByMaChine" class="panel panel-flat panel-body chart-custom"></div>
                <div id="defectByFeeder" class="panel panel-flat panel-body chart-custom"></div>
                <div id="defectByNozzle" class="panel panel-flat panel-body chart-custom"></div>
            </div>
            <div class="" style="position: absolute; top: 0px; left: 10px; z-index: 10;">
                <div id="demo1" class="panel panel-flat panel-body chart-custom"></div>
                <div id="demo11" class="panel panel-flat panel-body chart-custom"></div>
                <div id="demo111" class="panel panel-flat panel-body chart-custom"></div>
            </div>
        </div>
        
    </div>

</div>

<style>
    .chart-custom{
        width: 260px;
        height: 180px;
        padding: 0px;
        background: rgba(225, 225, 225, 0);
        margin-bottom: 5px;
    }
    .highcharts-no-data text{
        color: #cccccc !important;
        fill: #cccccc !important;
    }
</style>

<script>


var dataset = {
        factory: 'B06',
        machine: 'AOI2',
        line: 'D5',
        // timeSpan: moment().format("YYYY/MM/DD") + " " + "07:30" + " - " + moment().add(1, 'days').format("YYYY/MM/DD") + " " + "07:30"
    }
  var ts = moment().subtract(
    1, 'd').format("YYYY/MM/DD") + " " + "19:30" + " - " + moment().add(1, 'days').format("YYYY/MM/DD") + " " + "19:30";
  dataset.timeSpan = ts.toString();

  init();
	function init(){
    loadMachineChart();
	}

	function loadMachineChart(){
	    $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/api/aoi/defected/by/machine",
            data: {
                factory: dataset.factory,
                line: dataset.line,
                machine: dataset.machine,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){

                var listMachine = data.machineList;
                var keys = Object.keys(data.resultData);
                var dataChart = new Array();
                
                var chartData = {};
                for(i in listMachine){
                  chartData[listMachine[i]] = [];
                }

                for(i in listMachine){
                  for(j in data.resultData){
                    var machine = 0;
                    if(data.resultData[j][listMachine[i]] !== undefined)
                      machine =  data.resultData[j][listMachine[i]];
                    chartData[listMachine[i]].push(machine);
                  }
                }
                var stt = 0;
                for(i in chartData){
                  dataChart[stt] = {
                    name: i,
                    data: chartData[i]
                  }
                  stt++;
                }

  // for(i in chartData){
  //   var classMachineName = "";
  //   if(i != "printer")
  //     classMachineName = "mounter_"+i.slice(6,8).toLowerCase();     
  //   else classMachineName = i+"_machine";

  //   var j = chartData[i].length - 1;
  //   var color;
  //   if(chartData[i][j] > 0 && chartData[i][j] < 5){
  //     color = "#4caf50";
  //     setStatusMachine(classMachineName, color);
  //   }      
  //   else if(chartData[i][j] >5 && chartData[i][j] <10){
  //     color = "#ffeb3b";
  //     setStatusMachine(classMachineName, color);
  //   }      
  //   else if(chartData[i][j] > 10){
  //     color = "#e63344";
  //     setStatusMachine(classMachineName, color);
  //   }
  // }              
  

                Highcharts.chart('defectByMaChine', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
                        },
                        backgroundColor: 'rgba(39,39,39,0.7)'
                    },
                    title : {
                        text: 'Defect Qty By Machine',
                        style: {
                            fontSize: '14px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis : {
                        categories: keys,
                        type: 'category',
                        labels: {
                            // enabled: false,
                            // rotation: -40,
                            // align: 'right',
                            style: {
                                fontSize: '9px'
                            }
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                        tickLength: 0,
                        tickWidth: 0,
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels:{
                                enabled: false
                            },
                            cursor: 'pointer',
                            // point: {
                                events: {
                                     click: function (event) {
                                         var mounter_name = this.name;
                                         if(mounter_name != 'printer'){
                                          loadFeederChart(mounter_name);
                                          loadNozzleChart(mounter_name);
                                         }                                                                     
                                     }
                                 }
                            // }
                        }
                    },
                    yAxis : {
                        min: 0,
                        title: {
                            text: ' '
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                        tickLength: 0,
                        tickWidth: 0,
                    },
                    legend: {
                      enabled: false,
                      itemStyle: {
                          fontSize: '10px',
                          padding: '0px'
                      },
                      align: 'center',
                      verticalAlign: 'bottom',
                      //floating: true,
                      // borderWidth: 1,
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    series : dataChart
                });

                loadFeederChart(listMachine[0]);
                loadNozzleChart(listMachine[0]);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
	}
	function loadFeederChart(mounter_name){
	    $.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/api/aoi/defected/by/machine/feeder",
            data: {
              factory: dataset.factory,
              line: dataset.line,
              machine: dataset.machine,
              mounter: mounter_name,
              timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var listFeeder = data.feederList;
                var keys = Object.keys(data.resultData);
                var dataChart = new Array();
                
                var chartData = {};
                for(i in listFeeder){
                  chartData[listFeeder[i]] = [];
                }

                for(i in listFeeder){
                  for(j in data.resultData){
                    var feeder = 0;
                    if(data.resultData[j][listFeeder[i]] !== undefined)
                      feeder =  data.resultData[j][listFeeder[i]];
                    chartData[listFeeder[i]].push(feeder);
                  }
                }
                var stt = 0;
                for(i in chartData){
                  dataChart[stt] = {
                    name: i,
                    data: chartData[i]
                  }
                  stt++;
                }

                Highcharts.chart('defectByFeeder', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        },
                        backgroundColor: 'rgba(39,39,39,0.7)'
                    },
                    title : {
                        text: mounter_name + ' Defect By Feeder',
                        style: {
                            fontSize: '14px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis : {
                        type: 'category',
                        categories: keys,
                        labels: {
                            // enabled: false,
                            // rotation: -40,
                            // align: 'right',
                            style: {
                                fontSize: '9px'
                            }
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                        tickLength: 0,
                        tickWidth: 0,
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: false,
                            }
                        }
                    },
                    yAxis : {
                        plotLines: [{
                           value: 0,
                           width: 1,
                           color: 'blue'
                        }],
                        title: {
                            text: ''
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                        tickLength: 0,
                        tickWidth: 0,
                    },
                    legend: {
                        enabled: false,
                      itemStyle: {
                          fontSize: '10px',
                      },
                      align: 'center',
                      verticalAlign: 'bottom',
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    series : dataChart
                });
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
	}
	function loadNozzleChart(mounter_name){
		$.ajax({
            type: "GET",
            url: "http://10.224.81.70:8888/aoi-analytics/smt/api/aoi/defected/by/machine/nozzle",
            data: {
              factory: dataset.factory,
              line: dataset.line,
              machine: dataset.machine,
              mounter: mounter_name,
              timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var listNozzle = data.nozzleList;
                var keys = Object.keys(data.resultData);
                var dataChart = new Array();
                
                var chartData = {};
                for(i in listNozzle){
                  chartData[listNozzle[i]] = [];
                }

                for(i in listNozzle){
                  for(j in data.resultData){
                    var nozzle = 0;
                    if(data.resultData[j][listNozzle[i]] !== undefined)
                      nozzle =  data.resultData[j][listNozzle[i]];
                    chartData[listNozzle[i]].push(nozzle);
                  }
                }
                var stt = 0;
                for(i in chartData){
                  dataChart[stt] = {
                    name: i,
                    data: chartData[i]
                  }
                  stt++;
                }

                Highcharts.chart('defectByNozzle', {
                    chart: {
                        type: 'column',
                        style: {
                            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                        },
                        backgroundColor: 'rgba(39,39,39,0.7)'
                    },
                    title : {
                        text: mounter_name + ' Defect By Nozzle ',
                        style: {
                            fontSize: '14px',
                            fontWeight: 'bold'
                        }
                    },
                    xAxis : {
                        type: 'category',
                        categories: keys,
                        labels: {
                            // enabled: false,
                            // rotation: -40,
                            // align: 'right',
                            style: {
                                fontSize: '9px'
                            }
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                        tickLength: 0,
                        tickWidth: 0,
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: false,
                            }
                        }
                    },
                    yAxis : {
                        plotLines: [{
                           value: 0,
                           width: 1,
                           color: 'blue'
                        }],
                        title: {
                            text: ''
                        },
                        gridLineWidth: 0,
                        minorGridLineWidth: 0,
                        tickLength: 0,
                        tickWidth: 0,
                        //opposite: true
                    },
                    legend: {
                        enabled: false,
                      itemStyle: {
                          fontSize: '10px',
                      },
                      align: 'center',
                      verticalAlign: 'bottom',
                    },
                    navigation: {
                        buttonOptions: {
                            enabled: false
                        }
                    },
                    credits: {
                        enabled: false
                    },
                    series : dataChart
                });
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
	}

  function setStatusMachine(machine_name, color){
    // document.getElementById('G3').setAttribute('diffuseColor', '1 0 0');
    $("."+machine_name).attr("diffuseColor", color);
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



    Highcharts.chart('demo111', {

    chart: {
        type: 'pie',
        style: {
            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
        },
        backgroundColor: 'rgba(39,39,39,0.7)'
    },
    title : {
        text: ' Defect By Nozzle ',
        style: {
            fontSize: '14px',
            fontWeight: 'bold'
        }
    },
    xAxis : {
        type: 'category',
        labels: {
            enabled: false,
            rotation: -40,
            align: 'right',
            style: {
                fontSize: '11px'
            }
        },
        gridLineWidth: 0,
        minorGridLineWidth: 0,
        tickLength: 0,
        tickWidth: 0,
    },
    yAxis : {
        plotLines: [{
        value: 0,
        width: 1,
        color: 'blue'
        }],
        title: {
            text: ''
        },
        gridLineWidth: 0,
        minorGridLineWidth: 0,
        tickLength: 0,
        tickWidth: 0,
        // offset: -320,
    },
    legend: {
        enabled: false,
    itemStyle: {
        fontSize: '10px',
    },
    align: 'center',
    verticalAlign: 'bottom',
    },
    navigation: {
        buttonOptions: {
            enabled: false
        }
    },
    credits: {
        enabled: false
    },
    plotOptions: {
        series: {
            borderWidth: 0,
            label: {
                connectorAllowed: false
            },
            pointStart: 2010,
            dataLabels: {
                enabled: false,
            }
        }
    },

    series: [{
        name: 'Installation',
        data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
    }, {
        name: 'Manufacturing',
        data: [24916, 24064, 29742, 29851, 32490, 30282, 38121, 40434]
    }, {
        name: 'Sales & Distribution',
        data: [11744, 17722, 16005, 19771, 20185, 24377, 32147, 39387]
    }, {
        name: 'Project Development',
        data: [null, null, 7988, 12169, 15112, 22452, 34400, 34227]
    }, {
        name: 'Other',
        data: [12908, 5948, 8105, 11248, 8989, 11816, 18274, 18111]
    }],

});

Highcharts.chart('demo11', {

   chart: {
        type: 'column',
        style: {
            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
        },
        backgroundColor: 'rgba(39,39,39,0.7)'
    },
    title : {
        text: ' Defect By Nozzle ',
        style: {
            fontSize: '14px',
            fontWeight: 'bold'
        }
    },
    xAxis : {
        type: 'category',
        labels: {
            enabled: false,
            rotation: -40,
            align: 'right',
            style: {
                fontSize: '11px'
            }
        },
        gridLineWidth: 0,
        minorGridLineWidth: 0,
        tickLength: 0,
        tickWidth: 0,
    },
    yAxis : {
        plotLines: [{
        value: 0,
        width: 1,
        color: 'blue'
        }],
        title: {
            text: ''
        },
        gridLineWidth: 0,
        minorGridLineWidth: 0,
        tickLength: 0,
        tickWidth: 0,
        // offset: -320,
    },
    legend: {
        enabled: false,
    itemStyle: {
        fontSize: '10px',
    },
    align: 'center',
    verticalAlign: 'bottom',
    },
    navigation: {
        buttonOptions: {
            enabled: false
        }
    },
    credits: {
        enabled: false
    },
    plotOptions: {
        series: {
            borderWidth: 0,
            label: {
                connectorAllowed: false
            },
            pointStart: 2010,
            dataLabels: {
                enabled: false,
            }
        }
    },

    series: [{
        name: 'Installation',
        data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
    }, {
        name: 'Manufacturing',
        data: [24916, 24064, 29742, 29851, 32490, 30282, 38121, 40434]
    }, {
        name: 'Sales & Distribution',
        data: [11744, 17722, 16005, 19771, 20185, 24377, 32147, 39387]
    }, {
        name: 'Project Development',
        data: [null, null, 7988, 12169, 15112, 22452, 34400, 34227]
    }, {
        name: 'Other',
        data: [12908, 5948, 8105, 11248, 8989, 11816, 18274, 18111]
    }],
    responsive: {
        rules: [{
            condition: {
                maxWidth: 500
            },
            chartOptions: {
                legend: {
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom'
                }
            }
        }]
    }

});

Highcharts.chart('demo1', {

    chart: {
        style: {
            fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
        },
        backgroundColor: 'rgba(39,39,39,0.7)'
    },
    title : {
        text: ' Defect By Nozzle ',
        style: {
            fontSize: '14px',
            fontWeight: 'bold'
        }
    },
    xAxis : {
        type: 'category',
        labels: {
            enabled: false,
            rotation: -40,
            align: 'right',
            style: {
                fontSize: '11px'
            }
        },
        gridLineWidth: 0,
        minorGridLineWidth: 0,
        tickLength: 0,
        tickWidth: 0,
    },
    yAxis : {
        plotLines: [{
        value: 0,
        width: 1,
        color: 'blue'
        }],
        title: {
            text: ''
        },
        gridLineWidth: 0,
        minorGridLineWidth: 0,
        tickLength: 0,
        tickWidth: 0,
        // offset: -320,
    },
    legend: {
        enabled: false,
    itemStyle: {
        fontSize: '10px',
    },
    align: 'center',
    verticalAlign: 'bottom',
    },
    navigation: {
        buttonOptions: {
            enabled: false
        }
    },
    credits: {
        enabled: false
    },
    plotOptions: {
        series: {
            borderWidth: 0,
            label: {
                connectorAllowed: false
            },
            pointStart: 2010,
            dataLabels: {
                enabled: false,
            }
        }
    },

    series: [{
        name: 'Installation',
        data: [43934, 52503, 57177, 69658, 97031, 119931, 137133, 154175]
    }, {
        name: 'Manufacturing',
        data: [24916, 24064, 29742, 29851, 32490, 30282, 38121, 40434]
    }, {
        name: 'Sales & Distribution',
        data: [11744, 17722, 16005, 19771, 20185, 24377, 32147, 39387]
    }, {
        name: 'Project Development',
        data: [null, null, 7988, 12169, 15112, 22452, 34400, 34227]
    }, {
        name: 'Other',
        data: [12908, 5948, 8105, 11248, 8989, 11816, 18274, 18111]
    }],
    responsive: {
        rules: [{
            condition: {
                maxWidth: 500
            },
            chartOptions: {
                legend: {
                    layout: 'horizontal',
                    align: 'center',
                    verticalAlign: 'bottom'
                }
            }
        }]
    }

});
</script>