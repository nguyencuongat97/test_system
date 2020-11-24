<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/calc-line-balance.css">
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>
<link rel="stylesheet" href="/assets/css/custom/customer-line.css" />
<style>
	table.table-bordered tbody tr:nth-of-type(even) {
		background-color: #343435;
	}

	body {
		font-size: 15px;
	}

	.input-group {
		width: 100%;
	}

	.groupBtn {
		width: 100%;
	}

	.modal-header .close {
		top: 20%;
	}

	#tblDetail_filter {
		float: none;
		margin-bottom: 1rem;
	}
</style>
<div class="row">
	<div class="col-lg-12" style="background: #272727;">
		<div class="panel panel-overview" id="header">
			<span class="text-uppercase"></span>
		</div>
		<div class="row" style="margin: unset;">
			<div class="col-sm-12 col-md-11">
				<div class="panel panel-overview input-group">
					<span class="input-group-addon"><i class="icon-calendar22"></i></span>
					<input type="text" class="form-control datetimerange" side="right" id="datetime">
				</div>
			</div>
			<div class="col-sm-12 col-md-1" style="padding:0;">
				<div class="panel groupBtn">
					<button class="btn btnheader" id="download" onclick="downloadFile()"><i
							class="fa fa-download"></i></button>
					<label for="file" class="btn btnheader" style="float: right;"><i class="fa fa-upload"></i></label>
					<input id="file" onchange="onChangeFile()" type="file" name="photo" style="display: none;" />
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12 col-sm-12">
				<div class="panel panel-flat panel-body chart-sm" id="chart1"
					style="height: 300px !important;margin-bottom:0.5rem;"></div>
			</div>
		</div>
		<div class="row" style="margin: unset; margin-bottom: 0.5rem;">
			<table id="tblCacl" class="table table-bordered table-xxs">
				<thead>
					<tr id="item">
						<th rowspan="2" style="width: 6%;">CFT</th>
						<th style="width: 20%;text-align:left;font-size:11px;">Item</th>
					</tr>
					<tr id="runningDay">
						<th style="text-align:left;font-size:11px;">Manufacturing days <br />Số ngày sản xuất
						</th>
					</tr>
				</thead>
				<tbody>

				</tbody>
			</table>
		</div>

	</div>
</div>
<!-- Modal detail-->
<div class="modal fade" id="modalDetail" role="dialog">
	<div class="modal-dialog modal-xl">
		<div class="modal-content" style="background: #3c3c3c;font-size:13px;">
			<div class="modal-header" style="color: #ccc;border-bottom: 1px solid #757272;padding-top: 0.5rem;">
				<h4 style="margin:0px;" class="modal_title"></h4>
				<button type="button" style="color: #ccc" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body" style="padding: 0px 20px;">
				<!-- <div id="tblDetail_filter" class="dataTables_filter"><label><input id="search_tblDetail" type="search"
							class="" placeholder="" aria-controls="tblDetail" style="color: #ccc;"></label></div> -->
				<div class="table-responsive pre-scrollable" style="max-height: calc(100vh - 165px);">
					<table class="table table-bordered table-xxs table-sticky" id="tblDetail" style="color:#ccc">
						<thead>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer">
			</div>
		</div>

	</div>
</div>

<script>
	var dataset = {
		factory: '${factory}'
	};
	$('#header span').html(dataset.factory + ' - SMT line requirements calculate');
	
	getTimeNow();
	function getTimeNow() {
		$.ajax({
			type: "GET",
			url: "/api/time/now",
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				var current = new Date(data);
				var startDate = moment(current).add(-3, "month").format("YYYY/MM/DD") + ' 07:30:00';
				var endDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
				$('#datetime').daterangepicker({
					maxSpan: {
						months: 3
					},
					timePicker: true,
					timePicker24Hour: true,
					applyClass: 'bg-slate-600',
					cancelClass: 'btn-default',
					timePickerIncrement: 3,
					locale: {
						format: 'YYYY/MM/DD HH:mm'
					}
				});
				$('#datetime').data('daterangepicker').setStartDate(new Date(startDate));
				$('#datetime').data('daterangepicker').setEndDate(new Date(endDate));
				$('#datetime').on('change', function () {
					dataset.timeSpan = this.value;
					UpLoadDataCacl();
				});
				dataset.timeSpan = startDate + ' - ' + endDate;
				UpLoadDataCacl();
				delete current;
				delete startDate;
				delete endDate;
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	var dataSeries = [];
	function toFloat(value) {
		return value == 0 ? value : Number(parseFloat(value).toFixed(2));
	}
	
	function UpLoadDataCacl() {
		$.ajax({
			type: "GET",
			url: "/api/smt/mps",
			data: {
				factory: dataset.factory,
				timeSpan: $('#datetime').val()
			},
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				var response = data.data;
				if (!$.isEmptyObject(data)) {
					$('#tblCacl tbody').html('');
					var dateObject = Object.keys(response);
					var tbodyTitleTTL =
						'<tr id="TTL3">' +
						'<td rowspan="3">TTL</td>' +
						'<td style="text-align:left;font-size:11px;">Line Requirements <br/> Số chuyền nhu cầu</td></tr>' +
						'<tr id="TTL4"><td style="text-align:left;font-size:11px;">Existing Line <br/> Số chuyền hiện có</td></tr>' +
						'<tr id="TTL5"><td style="text-align:left;font-size:11px;">Diff <br/> Chênh lệch</td></tr>';
					$('#tblCacl tbody').html(tbodyTitleTTL);
					var itemObject = Object.keys(response[dateObject[0]]);
					for (var i = 0; i < itemObject.length; i++) {
						if (itemObject[i] != 'TTL') {
							var tbodyTitle =
								'<tr id="' + itemObject[i] + '1">' +
								'<td rowspan="5" style="color:#00a1ff;cursor:pointer;font-weight: bold;" onclick="loadDetail(\'' + itemObject[i] + '\')">' + itemObject[i] + '</td>' +
								'<td style="text-align:left;font-size:11px;">TTL forecast(Kpcs) <br/></td></tr>' +
								'<tr id="' + itemObject[i] + '2"><td style="text-align:left;font-size:11px;">Placement Requirements <br/> Nhu cầu dán kiện</td></tr>' +
								'<tr id="' + itemObject[i] + '3"><td style="text-align:left;font-size:11px;">Line Requirements <br/> Số chuyền nhu cầu</td></tr>' +
								'<tr id="' + itemObject[i] + '4"><td style="text-align:left;font-size:11px;">Existing Line <br/> Số chuyền hiện có</td></tr>' +
								'<tr id="' + itemObject[i] + '5"><td style="text-align:left;font-size:11px;">Diff <br/> Chênh lệch</td></tr>';
							$('#tblCacl tbody').append(tbodyTitle);
						}
					}
					$("#item").html('<th rowspan="2" style="width: 6%;">CFT</th><th style="width: 20%;text-align:left;font-size:11px;">Item</th>');
					$("#runningDay").html('<th style="text-align:left;font-size:11px;">Manufacturing days <br />Số ngày sản xuất</th>');
					var dataNameSeris = new Object();
					for (i in itemObject) {
						dataNameSeris[itemObject[i]] = [];
					}
					dataNameSeris['Available'] = [];
					// var yield_rate = [];
					for (i in dateObject) {
						// yield_rate[i] = { name: dateObject[i], y: toFloat(response[dateObject[i]]['TTL'].planLine) };
						var cftObject = Object.keys(response[dateObject[i]]);
						$("#item").append('<th style="width:140px;text-align:center">' + dateObject[i] + '</th>');
						$("#runningDay").append('<th style="text-align:center">' + response[dateObject[i]][cftObject[0]].runningDay + '</th>');
						for (j in cftObject) {
							var arrayValue = response[dateObject[i]][cftObject[j]];
							$("#" + cftObject[j] + "1").append('<td>' + toFloat(arrayValue.plan) + '</td>');
							$("#" + cftObject[j] + "2").append('<td>' + toFloat(arrayValue.planPart) + '</td>');
							$("#" + cftObject[j] + "3").append('<td>' + toFloat(arrayValue.planLine) + '</td>');
							$("#" + cftObject[j] + "4").append('<td>' + toFloat(arrayValue.totalLine) + '</td>');
							$("#" + cftObject[j] + "5").append('<td id="Diff' + cftObject[j] + i + '">' + toFloat(arrayValue.diff) + '</td>');

							if (toFloat(arrayValue.diff) < 0) {
								$("#Diff" + cftObject[j] + i + "").addClass('colorHighLight');
							}
							else {
								$("#Diff" + cftObject[j] + i + "").addClass('HighLight');
							}
							for (k in itemObject) {
								if (itemObject[k] == cftObject[j]) {
									dataNameSeris[itemObject[k]].push({ name: dateObject[i], y: Number(arrayValue.planLine.toFixed(2)) });
								}

							}

						};
						dataNameSeris['Available'].push({ name: dateObject[i], y: 15 });
					};
					dataSeries=[];
					dataSeries.push({
						name: 'Available',
						type: 'line',
						color:'blue',
						data: dataNameSeris['Available']
					})
					for (i in itemObject) {
						if (itemObject[i] != 'TTL') {
							dataSeries.push({
								name: itemObject[i],
								type: 'line',
								dashStyle: 'shortdash',
								data: dataNameSeris[itemObject[i]]
							})
						}
					}
					dataSeries.push({
						name: 'TTL',
						type: 'line',
						color:'red',
						data: dataNameSeris['TTL']
					});
					Highcharts.chart('chart1', {
						chart: {
							style: {
								fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"',
							}
						},
						title: {
							text: dataset.factory + ' - Line Requirements',
							style: {
								fontSize: '16px',
								fontWeight: 'bold'
							}
						},
						xAxis: {
							type: 'category',
						},
						yAxis: [{
							title: {
								text: '',
							}
						}, {
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
						series: dataSeries
					});
				}
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	function onChangeFile() {
		var file_data = $('#file').prop('files')[0];
		var form = new FormData();
		form.append('file', file_data);
		form.append('factory', dataset.factory);
		$.ajax({
			type: "POST",
			url: "/api/smt/mps/upload",
			data: form,
			dataType: "json",
			processData: false,
			contentType: false,
			mimeType: "multipart/form-data",
			success: function (data) {
				alert(data.message);
				window.location.reload();
			},
			error: function (errMsg) {
				console.log(errMsg);
				alert("Confirm failed!");
			}
		});
		$('#file').val('');
	}

	function downloadFile() {
		window.location.href = "/api/smt/mps/download?factory=" + dataset.factory;
	}
	function toFix(value) {
		return value == 0 ? value : value.toFixed(2);
	}
	function loadDetail(cft) {
		$('#modalDetail').modal('show');
		$('.modal_title').html(cft + ' detail');
		$.ajax({
			type: "GET",
			url: "/api/smt/mps/detail",
			// url: "http://10.224.56.73:8888/api/smt/mps/detail",
			data: {
				factory: dataset.factory,
				cft: cft,
				timeSpan: $('#datetime').val()
			},
			contentType: "application/json; charset=utf-8",
			success: function (res) {
				$('#tblDetail thead').html('');
				var data = res.data;
				var thDetail = '';
				var objectDay = Object.keys(data);
				var arrModel = [];
				var index = 1;
				for (i = 0; i < objectDay.length; i++) {
					thDetail += '<th>' + objectDay[i].substring(5) + '</th>';
					var detail = data[objectDay[i]];
					var objectModel = Object.keys(detail);
					var trDetail = '';
					for (j = 0; j < objectModel.length; j++) {
						if (arrModel.indexOf(objectModel[j]) == -1) {
							arrModel.push(objectModel[j]);
							// trDetail += '<tr id="plan_' + objectModel[j] + '"><td rowspan="3">' + index + '</td><td rowspan="3">' + objectModel[j] + '</td><td>Plan</td></tr>' +
							// 	'<tr id="planLine_' + objectModel[j] + '"><td>Plan line</td></tr>' +
							// 	'<tr id="planPart_' + objectModel[j] + '"><td>Plan part</td></tr>';
							// 	index++;
						}
					}

				}
				for (i = 0; i < arrModel.length; i++) {
					trDetail += '<tr id="plan_' + arrModel[i] + '"><td rowspan="3">' + Number(i + 1) + '</td><td rowspan="3">' + arrModel[i] + '</td><td style="text-align: left;">Plan <span style="font-size:11px">(Kpcs)<span></td></tr>' +
						'<tr id="planLine_' + arrModel[i] + '"><td style="text-align: left;">Plan line</td></tr>' +
						'<tr id="planPart_' + arrModel[i] + '"><td style="text-align: left;">Plan part <span style="font-size:11px">(mpoint)<span></td></tr>';
				}
				$('#tblDetail tbody').html(trDetail);
				$('#tblDetail thead').html('<tr><th style="width:4%">STT</th><th style="width: 15%;">Model</th><th style="width: 12%;">Item</th>' + thDetail + '</tr>');
				var detailVal;
				for (i = 0; i < objectDay.length; i++) {
					for (j = 0; j < arrModel.length; j++) {
						detailVal = data[objectDay[i]][arrModel[j]];
						console.log(detailVal)
						if (detailVal == undefined) {
							$('#plan_' + arrModel[j]).append('<td></td>')
							$('#planLine_' + arrModel[j]).append('<td></td>')
							$('#planPart_' + arrModel[j]).append('<td></td>')
						}
						else {
							$('#plan_' + arrModel[j]).append('<td>' + detailVal.plan + '</td>');
							$('#planLine_' + arrModel[j]).append('<td>' + toFix(detailVal.planLine) + '</td>')
							$('#planPart_' + arrModel[j]).append('<td>' + toFix(detailVal.planPart) + '</td>')
						}
					}
				}
				// $('#search_tblDetail').on('keyup', function () {
				// 	var value = $(this).val().toLowerCase();
				// 	$('#tblDetail tbody tr').filter(function () {
				// 		$('#tblDetail tbody tr td:eq(2)').toggle($(this).text().toLowerCase().indexOf(value) > -1)
				// 	});
				// });
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}


</script>