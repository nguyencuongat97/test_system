<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!-- <link rel="stylesheet" href="/assets/css/custom/calc-line-balance.css"> -->
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/pareto.js"></script>
<link rel="stylesheet" href="/assets/css/custom/customer-line.css" />
<style>
	.content{background: #00132c;}
	#header {background: #00132c;}
	.chart-sm {background: #00132c;}
	.highcharts-series-label{display: none;}
	.datetimerange {height: 26px;color: #fff;border-bottom: none;}
	.input-group-addon {padding: 0px 5px;color: #fff;}
	.panel {
    background: none;
}
	button.btn.dropdown-toggle.btn-default {color: #cccccc;}
	/* #header {
    	background: #00132c;
		background: url('/assets/images/template/form91.png') center center / 100% 100% no-repeat;
	}
	.p-0{
		padding: 0 !important;
	}
	.row1 {
		background: url('/assets/images/template/form7.png') center center / 100% 100% no-repeat;
	} */
</style>
<div class="row">
	<div class="col-lg-12 p-0" >
		<div class="panel panel-overview" id="header">
			<span class="text-uppercase"></span>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-10">
				<div class="panel panel-overview input-group" style="margin-bottom: 5px;float: left;">
					<span class="input-group-addon"><i class="icon-calendar22"></i></span>
					<input type="text" class="form-control datetimerange" side="right" id="datetime" name="timeSpan">
				</div>
			</div>
			<div class="col-xs-12 col-sm-1">
				<select class="form-control bootstrap-select" name="modelName" >
				</select>
			</div>
			<div class="col-xs-12 col-sm-1">
				<select class="form-control bootstrap-select" name="groupName" >
				</select>
			</div>
		</div>
		<div class="row row1">
			<div class="col-xs-12 col-sm-12">
				<div class="panel panel-flat panel-body chart-sm" id="dailyByGroup"
					style="height: 150px !important;margin-bottom:0.5rem;"></div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-12">
				<div class="panel panel-flat panel-body chart-sm" id="dailyByMO"
					style="height: 150px !important;margin-bottom:0.5rem;"></div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-12">
				<div class="panel panel-flat panel-body chart-sm" id="hourlyByGroup"
					style="height: 150px !important;margin-bottom:0.5rem;"></div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-6 col-sm-6">
				<div class="panel panel-flat panel-body chart-sm" id="errorGroup"
					style="height: 300px !important;margin-bottom:0.5rem;"></div>
			</div>
			<div class="col-xs-6 col-sm-6">
				<div class="panel panel-flat panel-body chart-sm" id="stationErrorGroup"
					style="height: 300px !important;margin-bottom:0.5rem;"></div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-6 col-sm-6">
				<div class="panel panel-flat panel-body chart-sm" id="stationError"
					style="height: 300px !important;margin-bottom:0.5rem;"></div>
			</div>
			<div class="col-xs-6 col-sm-6">
				<div class="panel panel-flat panel-body chart-sm" id="chart2"
					style="height: 300px !important;margin-bottom:0.5rem;"></div>
			</div>
		</div>

	</div>
</div>


<script>
	
	var dataset = {
        factory: '${factory}',
        modelName: '${modelName}',
        groupName: '${groupName}',
        stationName: '${stationName}',
        timeSpan: '${timeSpan}'
    };
	$('#header span').html(dataset.factory + ' - E.T.E test management');
	var dataSeries = [];
	getTimeNow();
	function getTimeNow() {
		$.ajax({
			type: "GET",
			url: "/api/time/now",
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				var current = new Date(data);
				var startDate = moment(current).add(-2, "day").format("YYYY/MM/DD") + ' 07:30';
				var endDate = moment(current).add(-1, "day").format("YYYY/MM/DD") + ' 07:30';
				$('#datetime').daterangepicker({
					maxSpan: {
						months: 1
					},
					timePicker: true,
					timePicker24Hour: true,
					applyClass: 'bg-slate-600',
					cancelClass: 'btn-default',
					timePickerIncrement: 1,
					locale: {
						format: 'YYYY/MM/DD HH:mm'
					}
				});
				$('#datetime').data('daterangepicker').setStartDate(new Date(startDate));
				$('#datetime').data('daterangepicker').setEndDate(new Date(endDate));
				$('#datetime').on('change', function () {
					dataset.timeSpan = this.value;
					init(dataset.timeSpan);
				});
				dataset.timeSpan = startDate + ' - ' + endDate;
				var setDate=moment(current).add(-7, "day").format("YYYY/MM/DD") + ' 07:30';
				var timeSpan=setDate + ' - ' + endDate;
				init(timeSpan);
				delete current;
				delete startDate;
				delete endDate;
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	function init(timeSpan) {
        if (dataset.modelName != '') {
            loadModels(dataset, true, function(dataset) {
                loadGroups(dataset, dataset.groupName != '', function(dataset) {
					dataDailyByMO(timeSpan);
					dataHourlyByGroup();
					dataErrorGroup();
					dataStationErrorGroup()
                });
                dataDailyByGroup(timeSpan);
            });
        } else {
            loadModels(dataset, false, function(dataset) {
                loadGroups(dataset, false, function(dataset) {
					dataDailyByMO(timeSpan);
					dataHourlyByGroup();
					dataErrorGroup();
					dataStationErrorGroup();
                });
                dataDailyByGroup(timeSpan);
            });
        }
        $('select[name=modelName]').on('change', function() {
            dataset.modelName = this.value;
            dataset.errorCode = null;
            dataset.errorDesc = null;
            loadGroups(dataset, false, function(dataset) {
				dataDailyByMO(timeSpan);
				dataHourlyByGroup();
				dataErrorGroup();
				dataStationErrorGroup();
			
            });
			dataDailyByGroup(timeSpan);
        });
        $('select[name=groupName]').on('change', function() {
            dataset.groupName = this.value;
			dataDailyByMO(timeSpan);
			dataHourlyByGroup();
			dataErrorGroup();
			dataStationErrorGroup()
        });
      
	}
	function dataDailyByGroup(timeSpan) {
		var modelName = $('select[name=modelName]').val();
		
		$.ajax({
			type: "GET",
			url: "/api/test/ete/daily-by-group",
			data: {
				factory: dataset.factory,
				modelName:modelName,
				timeSpan: timeSpan
			},
			contentType: "application/json; charset=utf-8",
			success: function (res) {
				var data = res.data;
				dataSeries = [];
				if(!$.isEmptyObject(data)){
					for(i in data){
						var dataItem = [];
						for(j in data[i]){
							if(data[i][j]){
								dataItem.push({ name: j, y: data[i][j].ete });
							}
							else{
								dataItem.push({ name: j, y: 0 });
							}
						}
						dataSeries.push({
							name: i,
							type: 'line',
							// color:'blue',
							data: dataItem
						});
					}
				}
				loadLineChart('dailyByGroup',dataSeries,'E.T.E daily by group');
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	function dataDailyByMO(timeSpan){
		var modelName = $('select[name=modelName]').val();
		var groupName = $('select[name=groupName]').val();
		
		$.ajax({
			type: "GET",
			url: "/api/test/ete/daily-by-mo",
			data: {
				factory: dataset.factory,
				modelName:modelName,
				groupName: groupName,
				timeSpan: timeSpan
			},
			contentType: "application/json; charset=utf-8",
			success: function (res) {
				var data = res.data;
				dataSeries = [];
				if(!$.isEmptyObject(data)){
					for(i in data){
						var dataItem = [];
						for(j in data[i]){
							if(data[i][j]){
								dataItem.push({ name: j, y: data[i][j].ete });
							}
							else{
								dataItem.push({ name: j, y: 0 });
							}
						}
						dataSeries.push({
							name: i,
							type: 'line',
							data: dataItem
						});
					}
				}
				loadLineChart('dailyByMO',dataSeries,'E.T.E daily by MO');
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	function loadLineChart(id,dataSeries, title){
		color = ["#55ffff","#8cd17d","#e15759"];
		Highcharts.chart(id, {
			chart: {
				backgroundColor: 'none',
				style: {
					fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
				},
		// marginRight: 2,
		// marginBottom: 50
			},
			title: {
				text: dataset.factory +' '+ title,
				style: {
					fontSize: '13px',
					// fontWeight: 'bold'
				}
			},
			xAxis: {
				type: 'category',
			},
			yAxis: [{
					title: {
						text: '',
					}
				}, 
				{
					title: {
						text: '',
						style: {
							color: Highcharts.getOptions().colors[0]
						}
					},
					labels: {
						format: '{value}',
						style: {
						}
					},
					opposite: true
				}],
			tooltip: {
				shared: true,
				// pointFormat: '{series.name}: <b>{point.y:.2f}</b>'
			},
			plotOptions: {
				series: {
					dataLabels: {
						enabled: true,
						format: '{point.y:.2f}'
					}
				}
				
			},
			colors: color,
			legend: {
				// enabled: true,
				align: 'right',
				layout: 'vertical',
				verticalAlign: 'top'
				// style: {
				// 	fontSize: '11px'
				// },
				// layout: 'horizontal',
				// align: 'center',
				// verticalAlign: 'bottom'
			},
			navigation: {
				buttonOptions: {
					enabled: false
				}
			},
			credits: {
				enabled: false
			},
			series: dataSeries
		});
	}

	function dataHourlyByGroup(){
		var modelName = $('select[name=modelName]').val();
        var groupName = $('select[name=groupName]').val();
		$.ajax({
			type: "GET",
			url: "/api/test/ete/hourly-by-group",
			data: {
				factory: dataset.factory,
				modelName:modelName,
				groupName: groupName,
				timeSpan: dataset.timeSpan
			},
			contentType: "application/json; charset=utf-8",
			success: function (res) {
				var data = res.data;
				if(!$.isEmptyObject(data)){
					dataSeries = [];
					var dataItem = [];
					for(i in data){
						if(data[i]){
							dataItem.push({ name: i, y: data[i].ete });
						}
						else{
							dataItem.push({ name: i, y: 0 });
						}
						
					}
					dataSeries.push({
						name: '',
						type: 'line',
						data: dataItem
					});
					color = ["#55ffff","#8cd17d","#e15759"];
					Highcharts.chart('hourlyByGroup', {
						chart: {
							backgroundColor: '#04132a',
							style: {
								fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
							},
						},
						title: {
							text: dataset.factory +' E.T.E hourly by group',
							style: {
								fontSize: '13px',
							}
						},
						xAxis: {
							type: 'category',
						},
						yAxis: [{
								title: {
									text: '',
								}
							}, 
							{
								title: {
									text: '',
									style: {
										color: Highcharts.getOptions().colors[0]
									}
								},
								labels: {
									format: '{value}',
									style: {
									}
								},
								opposite: true
							}],
						tooltip: {
							shared: true
						},
						plotOptions: {
							line: {
								states: {
									hover: {
										lineWidth: 2
									}
								},
								marker: {
									enabled: false
								}
							}
						},
						colors: color,
						legend: {
							enabled: false,
						},
						navigation: {
							buttonOptions: {
								enabled: false
							}
						},
						credits: {
							enabled: false
						},
						series: dataSeries
					});
				}
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}

	function dataErrorGroup(){
		var modelName = $('select[name=modelName]').val();
        var groupName = $('select[name=groupName]').val();
		$.ajax({
			type: "GET",
			url: "/api/test/ete/error-of-group",
			data: {
				factory: dataset.factory,
				modelName:modelName,
				groupName: groupName,
				timeSpan: dataset.timeSpan
			},
			contentType: "application/json; charset=utf-8",
			success: function (res) {
				var data = res.data;
				var dataSeries=[];
				if(!$.isEmptyObject(data)){
					dataStationError(data[0].errorCode);
					for (i=0;i<data.length;i++) {
						if (data[i]) {
							dataSeries[i] = { name: data[i].errorCode, y: data[i].fail };
						}
						else {
							dataSeries[i] = { name: data[i].errorCode, y: 0 };
						}
					}
				}
				color = ["#55ffff","#8cd17d","#e15759"];
				Highcharts.chart('errorGroup', {
					chart: {
						backgroundColor: '#04132a',
						type: 'column'
					},
					title: {
						text: 'Error of group',
						style: {
							fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
							fontSize: '13px'
						}
					},
					xAxis: {
						type: 'category',
					},
					yAxis: {
						title: {
							text: '',
							style: {
								fontSize: '16px',
								fontWeight: 'bold'
							}
						},
					},
					plotOptions: {
						series: {
							// borderWidth: 0,
							dataLabels: {
								enabled: true
							},
							cursor: 'pointer',
							point: {
								events: {
									click: function (event) {
										var error_code = this.name;
										dataStationError(error_code);
									}
								}
							}
						}
					},
					legend: {
						itemStyle: {
							fontSize: '11px',
						},
						layout: 'horizontal',
						align: 'left',
						verticalAlign: 'bottom',
						enabled: false
					},
					colors: color,
					navigation: {
						buttonOptions: {
							enabled: false
						}
					},
					credits: {
						enabled: false
					},
					series: [{
						name: 'Actual reduce',
						data: dataSeries,
					}]
				});
					},
					failure: function (errMsg) {
						console.log(errMsg);
					}
				});
	}
	
	function dataStationError(errorCode){
		var modelName = $('select[name=modelName]').val();
        var groupName = $('select[name=groupName]').val();
		$.ajax({
			type: "GET",
			url: "/api/test/ete/station-of-error",
			data: {
				factory: dataset.factory,
				modelName:modelName,
				groupName: groupName,
				errorCode:errorCode,
				timeSpan: dataset.timeSpan
			},
			contentType: "application/json; charset=utf-8",
			success: function (res) {
				var data = res.data;
				var dataSeries=[];
				var categories=[];
				if(!$.isEmptyObject(data)){
					for (i=0;i<data.length;i++) {
						categories[i]=data[i].stationName;
						// if (data[i]) {
						// 	dataSeries[i] = { name: data[i].stationName, y: data[i].fail };
						// }
						// else {
						// 	dataSeries[i] = { name: data[i].stationName, y: 0 };
						// }
						if (data[i]) {
							dataSeries[i] = { y: data[i].fail };
						}
						else {
							dataSeries[i] = { y: 0 };
						}
					}
				}
				paretoChart('stationError','Station of error',categories,dataSeries)
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	

	

	function dataStationErrorGroup(){
		var modelName = $('select[name=modelName]').val();
        var groupName = $('select[name=groupName]').val();
		$.ajax({
			type: "GET",
			url: "/api/test/ete/station-by-error-of-group",
			data: {
				factory: dataset.factory,
				modelName:modelName,
				groupName: groupName,
				timeSpan: dataset.timeSpan
			},
			contentType: "application/json; charset=utf-8",
			success: function (res) {
				var data = res.data;

				var dataxAxis=[];
				var keyError = [];
				var dataKey = {};
				var dataSeries=[];
				if(!$.isEmptyObject(data)){
					dataErrorStation(data[0].stationName);
					for(var i = 0 ; i < data.length; i++){
						var errorMetas = data[i]['errorMetaMap'];
						dataxAxis.push(data[i].stationName);
						for(var key in errorMetas){
							if(keyError.indexOf(key) == -1){
								keyError.push(key);
							}
						}
					}

					for(var i = 0; i < keyError.length; i++){
						var tmpData = [];
						for(var j = 0 ; j < data.length; j++){
							tmpData.push(0);
						}
						dataKey[keyError[i]] = tmpData;
					}

					for(var i = 0 ; i < data.length; i++){
						var errorMetas = data[i]['errorMetaMap'];
						for(var key in errorMetas){
							dataKey[key][i] = errorMetas[key]['fail'];
						}
					}

					for(i in dataKey){
						dataSeries.push({name:i,data:dataKey[i]})
					}
				}
				Highcharts.chart('stationErrorGroup', {
					chart: {
						type: 'column',
						backgroundColor: '#04132a',
						style: {
							fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
						}
					},
					title: {
						text: 'Station by error of group',
						style: {
							fontSize: '14px',
						}
					},
					xAxis: {
						categories: dataxAxis,
						labels: {
							rotation: -90,
							align: 'right',
							style: {
								fontSize: '12px'
							}
						}
					},
					yAxis: {
						min: 0,
						title: {
							text: ''
						}
					},
					tooltip: {
						pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
						shared: true
					},
					plotOptions: {
						column: {
							stacking: true,
							borderWidth: 0
						},
						series: {
							borderWidth: 0,
							label: {
								connectorAllowed: false
							},
							dataLabels: {
								enabled: false,
							},
							cursor: 'pointer',
							point: {
								events: {
									click: function (event) {
										var station_name = this.category.name;
										dataErrorStation(station_name);
									}
								}
							}
						}

					},
					legend: {
						itemStyle: {
							fontSize: '10px',
						},
						layout: 'vertical',
						align: 'right',
						verticalAlign: 'middle'
					},
					credits: {
						enabled: false
					},
					navigation: {
						buttonOptions: {
							enabled: false
						}
					},
					series: dataSeries
				});
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	
	function dataErrorStation(stationName){
		var modelName = $('select[name=modelName]').val();
        var groupName = $('select[name=groupName]').val();
		$.ajax({
			type: "GET",
			url: "/api/test/ete/error-of-station",
			data: {
				factory: dataset.factory,
				modelName:modelName,
				groupName: groupName,
				stationName:stationName,
				timeSpan: dataset.timeSpan
			},
			contentType: "application/json; charset=utf-8",
			success: function (res) {
				var data = res.data;
				var dataSeries=[];
				var categories=[];
				if(!$.isEmptyObject(data)){
					for (i=0;i<data.length;i++) {
						categories[i]=data[i].errorCode;
						if (data[i]) {
							dataSeries[i] = {y: data[i].fail };
						}
						else {
							dataSeries[i] = {y: 0 };
						}
					}
				}
				paretoChart('chart2','Error of station',categories,dataSeries)
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	
	function paretoChart(id,title,categories,dataSeries){
		color = ["#55ffff","#8cd17d","#e15759"];
		Highcharts.chart(id, {
			chart: {
				backgroundColor: '#04132a',
				type: 'column',
				scrollablePlotArea: {
					minWidth: scroll,
					scrollPositionX: 0
				},
				style: {
					fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
				}
			},
			title: {
				text: title,
				style: {
					fontSize: '14px',
				}
			},
			tooltip: {
				shared: true
			},
			xAxis: {
				categories: categories,
				allowDecimals: false,
				crosshair: true,
				labels: {
					rotation: -45,
					align: 'right',
					style: {
						fontSize: '11px',
					}
				}
			},
			yAxis: [{
				title: {
					text: ''
				}
			}, {
				title: {
					text: ''
				},
				minPadding: 0,
				maxPadding: 0,
				max: 100,
				min: 0,
				opposite: true,
				labels: {
					format: "{value}%"
				}
			}],
			colors: color,
			legend: {
				enabled: false
			},
			navigation: {
				buttonOptions: {
					enabled: false
				}
			},
			plotOptions: {
				series: {
					dataLabels: {
						enabled: true
					}
				},
				pareto: {
					allowPointSelect: true,
					dataLabels: {
						enabled: true,
						format: '{point.y:.2f} %'
					}
				}
			},
			credits: {
				enabled: false
			},
			series: [{
				type: 'pareto',
				name: ' ',
				yAxis: 1,
				zIndex: 10,
				baseSeries: 1,
				color: '#46719d',
				tooltip: {
					valueDecimals: 2,
					valueSuffix: '%'
				}
			}, {
				name: ' ',
				type: 'column',
				zIndex: 2,
				data: dataSeries
			}]
		});
	}

</script>