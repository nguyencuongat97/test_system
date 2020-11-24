<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>

<link rel="stylesheet" href="/assets/custom/css/re/stock.css" />
<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />
<div class="panel panel-re panel-flat row">
    <div class="panel panel-overview" id="header">
        <b><span>B04-BBD-RE Repair Stock Management</span></b>
	</div>
	<div class="panel panel-overview input-group" style="margin-bottom: 5px; padding-left: 10px; background: #333;">
		<span class="input-group-addon" style="padding: 0px 5px; color: #fff;">
			<i class="icon-calendar22"></i>
		</span>
		<input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: #fff;">
	</div>
    <!-- <div class="row" style="height: 72px; margin-left: 0px; margin-top: 10px;">
        <div class="input-group" style="margin-left: 40px; width: 250px; top: 13px;">
            <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; color: inherit;">
        </div>
    </div> -->
	<div class="row" style="margin: unset">
		<div class="col-xs-12 col-sm-3 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="RE-wipTotalStatus"></div>
		</div>
		<div class="col-xs-12 col-sm-4 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="b04onlineWipStatus"></div>
		</div>
		<div class="col-xs-12 col-sm-5 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="totalWipByModel"></div>
		</div>
	</div>
	<div class="row" style="margin: unset">
		<div class="col-xs-12 col-sm-3 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="onlineWipStatus"></div>
		</div>
		<div class="col-xs-12 col-sm-4 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="re-rmaStatus"></div>
		</div>
		<div class="col-xs-12 col-sm-5 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="re-bc8mStatus"></div>
		</div>
	</div>
	<div class="row" style="margin: unset">
		<div class="col-xs-12 col-sm-6 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="wipOutputDaily"></div>
		</div>
		<div class="col-xs-12 col-sm-6 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="totalWipDaily"></div>
		</div>
	</div>
	<div class="row" style="margin: unset">
		<div class="col-xs-12 col-sm-6 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="wipUnder15D"></div>
		</div>
		<div class="col-xs-12 col-sm-6 infchart">
			<div class="panel panel-flat panel-body chart-sm" id="wipOver15D"></div>
		</div>
	</div>
	<!-- <div class="row" style="margin-top: 10px;">
        <div class="col-lg-3" style=" margin-left: 30px; padding:0px; width: 420px;">
            <div id="RE-wipTotalStatus" class="chart"></div>
        </div>
        <div class="col-lg-3" style=" margin-left: 0px; padding:0px; width: 420px;">
            <div id="b04onlineWipStatus" class="chart"></div>
        </div>
        <div class="col-lg-3" style=" margin-left: 0px; padding:0px; width: 420px;">
            <div id="totalWipByModel" class="chart"></div>
        </div>
    </div> -->
    <!-- <div class="row" style="margin-top: 0px;">
        <div class="col-lg-3" style=" margin-left: 30px; padding:0px; width: 420px;">
            <div id="onlineWipStatus" class="chart"></div>
        </div>
        <div class="col-lg-3" style=" margin-left: 0px; padding:0px; width: 420px;">
            <div id="re-rmaStatus" class="chart"></div>
        </div>
        <div class="col-lg-3" style=" margin-left: 0px; padding:0px; width: 420px;">
            <div id="re-bc8mStatus" class="chart"></div>
        </div>
    </div> -->
    <!-- <div class="row" style="margin-top: 0px;">
        <div class="col-lg-5" style=" margin-left: 30px; padding:0px; width: 630px;">
            <div id="wipOutputDaily" class="chart"></div>
        </div>
        <div class="col-lg-5" style=" margin-left: 0px; padding:0px; width: 630px;">
            <div id="totalWipDaily" class="chart"></div>
        </div>
    </div> -->
    <!-- <div class="row" style="margin-top: 0px;">
        <div class="col-lg-5" style=" margin-left: 30px; padding:0px; width: 630px;">
            <div id="wipUnder15D" class="chart"></div>
        </div>
        <div class="col-lg-5" style=" margin-left: 0px; padding:0px; width: 630px;">
            <div id="wipOver15D" class="chart"></div>
        </div>
    </div> -->
</div>
<style>
	body{
		overflow-x: hidden;
	}
</style>
<script>
	$('.input').val("4390");
	$(document).ready(function(){
		loadTotalStatus();
		loadB04OnlineWipStatus();
		loadTotalWipByModel();
		loadOnlineWipStatus();
		loadRmaStatus();
		loadBc8mStatus();
		loadWipUnder15d();
		loadWipOver15d();
		loadWipOutputDaily();
		loadTotalWipDaily();
	})
	function loadTotalStatus(){
		Highcharts.chart('RE-wipTotalStatus', {
			chart: {
				type: 'column',
				style: {
					fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
				}
			},
            title : {
               text: 'B04 - RE WIP TOTAL STATUS',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
               categories: ['WIP Total', 'BC8M Qty', 'RMA Qty', 'WIP-Online Qty']

            },
			plotOptions: {
				series: {
					dataLabels: {
						enabled: true,
						color: '#1fb2ec'
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
            series : [{
                data: [14620, 7530, 3854, 3236]
           }]
        });
	}
	function loadB04OnlineWipStatus(){
		Highcharts.chart('b04onlineWipStatus', {
			chart: {
				type: 'column',
				options3d: {
					enabled: true,
					alpha: 10,
					beta: 10
				},
				style: {
					fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
				}
			},
            title : {
               text: 'B04 - RE ONLINE WIP STATUS BY TIME',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
               categories: ['<12H', '>12H', '>24h', '>48h', '>72H', '>7D']

            },
			plotOptions: {
				series: {
					dataLabels: {
						enabled: true,
						color: '#1fb2ec'
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
            series : [{
                data: [1841, 1, 5, 49, 69, 155]
           }]
        });
	}
	function loadTotalWipByModel(){
		Highcharts.chart('totalWipByModel', {
			chart: {
				type: 'column',
				options3d: {
					enabled: true,
					alpha: 10,
					beta: 10
				},
				style: {
					fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
				}
			},
            title : {
               text: 'RE TOTAL WIP BY MODEL STATUS BY MODEL',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
               categories: ['101L091.01', '101X102.00', 'P02H003.01', 'T77X850.11', 'P02X001.00', 'RAIDENM...',
			                '101X086.02', '101L087.01', '101X108.00', 'T77X850.00', '101X112.00', 'T77X850.10', 'K17X001.00']

            },
            yAxis : {
               plotLines: [{
                   value: 0,
                   width: 1,
                   color: 'blue'
               }],
			   title: {
					text: ''
			    }
            },
            legend: {
                style: {
                    fontSize: '11px'
                },
                layout: 'horizontal',
                align: 'left',
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
            series : [{
                name: 'WIP Total',
				data: [64, 45, 39, 27, 12, 11, 10, 9, 9, 5, 3, 1, 1]
            },{
				name: 'BC8M',
				color: '#FA5858',
				data: [43, 30, 23, 12, 8, 6, 6, 10, 4, 2, 3, 1, 1]
			},{
				name: 'Accumulated Qty',
				color: '#A5DF00',
				data: [20, 12, 15, 3, 3, 4, 3, 4, 1, 2, 1, 1, 1]
			}]
        });
	}
	function loadOnlineWipStatus(){
		Highcharts.chart('onlineWipStatus', {
			chart: {
				type: 'column'
			},
            title : {
               text: 'RE ONLINE WIP STATUS BY MODEL',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
               type: 'category'
            },
			plotOptions: {
				series: {
					dataLabels: {
						enabled: true,
						color: '#1fb2ec'
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
            series : [{
                data:[
					["40-2411-1001", 1980],
					["40-1563-0003", 596],
					["40-1563-1001", 450],
					["Optimator", 210]
				]
           }]
        });
	}
	function loadRmaStatus(){
		Highcharts.chart('re-rmaStatus', {
			chart: {
				type: 'column'
			},
            title : {
               text: 'RE RMA STATUS BY MODEL',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
               categories: ['26-1204', '25-1904', '26-2452-0401', '26-2452-0451', '26-2473', '26-1691', '26-1905-1003',
							'26-1552-0004', '26-2478', '26-2454-0401', '26-1693', '26-2450-0401', '40-2411-1001']
            },
            yAxis : {
               plotLines: [{
                   value: 0,
                   width: 1,
                   color: 'blue'
               }],
			   title: {
					text: ''
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
            series : [{
                data: [965, 820, 455, 447, 387, 371, 352, 288, 266, 264, 263, 192, 191]
           }]
        });
	}
	function loadBc8mStatus(){
		Highcharts.chart('re-bc8mStatus', {
			chart: {
				type: 'column'
			},
            title : {
               text: 'RE BC8M STATUS BY MODEL',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
               categories: ['26-1204', '26-2452-0401', '26-2473','26-1095-1003', '26-2478', '26-1693', '40-2411-1001', '26-2452-0481', '26-1905-2000',
							'26-2454-0451', '40-5024', 'P02H003.01', '26-2448-0401', '26-2476', '26-2450-0451']
            },
            yAxis : {
               plotLines: [{
                   value: 0,
                   width: 1,
                   color: 'blue'
               }],
			   title: {
					text: ''
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
            series : [{
                data: [1300, 1005, 480, 470, 390, 370, 360, 300, 280, 280, 270, 190, 190, 170, 160]
            }]
        });
	}
	function loadWipOutputDaily(){
		Highcharts.chart('wipOutputDaily', {
			chart: {
				type: 'line'
			},
            title : {
               text: 'RE WIP REPAIR OUTPUT STATUS (DAILY UPDATE)',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
               categories: ['43579', '43580', '43581', '43582', '43587', '43588',
			                '43589', '43590', '43591', '43592', '43593', '43594', '43595']

            },
            yAxis : {
               plotLines: [{
                   value: 0,
                   width: 1,
                   color: 'blue'
               }],
			   title: {
					text: ''
			    }
            },
            legend: {
                style: {
                    fontSize: '11px'
                },
                layout: 'horizontal',
                align: 'left',
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
            series : [{
                name: 'BC8M Qty',
				data: [797, 257, 125, 69, 68, 74, 79, 60, 92, 110, 59]
            },{
				name: 'WIP Online Qty',
				color: '#FA5858',
				data: [0, 0, 120, 120, 0, 0, 0, 0, 0, 0, 0]
			},{
				name: 'RMA Qty',
				color: '#A5DF00',
				data: [56, 120, 90, 85, 121, 13, 0, 0, 57, 25, 5]
			},{
				name: 'WIP Total',
				color: '#AC58FA',
				data: [853, 377, 335, 274, 189, 87, 79, 60, 149, 135, 64]
			}]
        });
	}
	function loadTotalWipDaily(){
		Highcharts.chart('totalWipDaily', {
			chart: {
				type: 'line'
			},
            title : {
               text: 'RE TOTAL WIP REPAIR  STATUS (DAILY UPDATE)',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
               categories: ['Qty', '43579', '43580', '43581', '43582', '43587', '43588',
			                '43589', '43590', '43591', '43592', '43593', '43594', '43595'],
				allowDecimals: false,
				minorGridLineWidth: 0,
                minTickInterval: 1,
                tickAmount: 4
            },
            yAxis : {
               plotLines: [{
                   value: 0,
                   width: 1,
                   color: 'blue'
               }],
			   title: {
					text: ''
			    }
            },
            legend: {
                style: {
                    fontSize: '11px'
                },
                layout: 'horizontal',
                align: 'left',
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
            series : [{
                name: 'Plan (BC8M)',
				data: [7624, 7559, 7494, 7429, 7364, 7364, 7299, 7234, 7169, 7104, 7039, 6974, 6909, 6844]
            },{
				name: 'BC8M Qty',
				color: '#FA5858',
				data: [7624, 6827, 6570, 6445, 6376, 6308, 6234, 6155, 6095, 6003, 5893, 5834]
			},{
				name: 'WIP Online Qty',
				color: '#A5DF00',
				data: [3236, 3236, 3236, 3116, 2996, 2996, 2996, 2996, 2996, 2996, 2996, 2996]
			},{
				name: 'RMA Qty',
				color: '#AC58FA',
				data: [5433, 5377, 5257, 5167, 5082, 4961, 4948, 4948, 4948, 4891, 4866, 4861]
			},{
				name: 'WIP Total',
				color: '#58D3F7',
				data: [16290, 15440, 15060, 14720, 14450, 14260, 14170, 14090, 14030, 13890, 13750, 13690]
			}]
        });
	}
	function loadWipUnder15d(){
		Highcharts.chart('wipUnder15D', {
			chart: {
				type: 'column',
				options3d: {
					enabled: true,
					alpha: 10,
					beta: 10
				}
			},
            title : {
               text: 'RE WIP <15 Days Qty by Model',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
                categories: ['40-2411-1001', '40-1563-0003', '26-1905-1003', '25-1101', '25-1103', '26-1552-0004', '25-1904', '25-2402T11-1', '15-PR009',
							'P02H003.01', '25-1745', 'P100008170', '25-1546T00', '26-2446-0401', '40-5024', '25-2402T09-1', '15FGM1001', '26-2452-0451',
							'P000250860', '26-1204', '26-2488-0012', 'P000250880'],
				allowDecimals: false,
				minorGridLineWidth: 0,
                minTickInterval: 1,
                tickAmount: 4
            },
            yAxis : {
               plotLines: [{
                   value: 0,
                   width: 1,
                   color: 'blue'
               }],
			   title: {
					text: ''
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
            series : [{
                data: [1044, 149, 78, 69, 69, 65, 49, 47, 26, 22, 21, 17, 12, 9, 8, 7, 6, 4, 4, 4, 4, 2]
            }]
        });
	}
	function loadWipOver15d(){
		Highcharts.chart('wipOver15D', {
			chart: {
				type: 'column'
			},
            title : {
               text: 'RE WIP >15 Days Qty by Model',
			   style: {
					font: 'normal 12px Verdana, sans-serif',
					color: 'black',
					fontWeight: 'bold'
			   }
            },
            xAxis : {
                categories: ['40-2411-1001', '26-1552-0004', '40-1563-0003', '25-1103', '26-1204', '26-2454-0401', '26-2473', '26-2452-0401', '26-2452-0481',
							'40-2412-1000', '40-2060', '25-1745', '15-PR009'],
				allowDecimals: false,
				minorGridLineWidth: 0,
                minTickInterval: 1,
                tickAmount: 4
            },
            yAxis : {
               plotLines: [{
                   value: 0,
                   width: 1,
                   color: 'blue'
               }],
			   title: {
					text: ''
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
            series : [{
                data: [903, 89, 70, 58, 47, 44, 41, 38, 30, 28, 22, 19, 18]
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