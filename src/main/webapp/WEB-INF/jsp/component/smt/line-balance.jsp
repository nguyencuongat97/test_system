<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<style>
	#liAdd {
		float: right;
	}
</style>
<link rel="stylesheet" href="/assets/css/custom/line-balance.css">
<script src="/assets/js/datatables/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>
<link rel="stylesheet" href="/assets/css/custom/customer-line.css" />
<div class="row">
	<div class="col-lg-12" style="background: #272727;">
		<div class="row no-margin">
			<div class="panel panel-overview" id="header"
				style="margin-bottom: 5px; background: #333;width: 91%; float: left;">
				<span class="text-uppercase" style="padding: 0px 5px; color: inherit;"></span>
			</div>
			<div class="panel chooseModel"
				style="width: 8.5%;margin: 0 1% 5px;float: right;background: #333; height: 26px; margin-right: 0;margin-left: 0;">
				<div class="dropdown">
					<button class="btn dropdown-toggle" type="button" data-toggle="dropdown"><i
							class="fa fa-cog"></i>Config</button>

					<div class="dropdown-menu dropdown-content">
						<ul id="chooseModel">
							<li onclick="lineMetaClick()" data-toggle='modal' data-target="#myModal"
								aria-haspopup="true" aria-expanded="false">IE Line
							</li>
							<div class="dropdown-divider"></div>
							<li onclick="modelMetaClick()" data-toggle='modal' data-target="#myModal"
								aria-haspopup="true" aria-expanded="false">Model Meta
							</li>
							<div class="dropdown-divider"></div>
							<li id="modelLineMeta" data-toggle='modal' data-target="#myModal" aria-haspopup="true"
								aria-expanded="false"> SMT model line
							</li>
							<div class="dropdown-divider"></div>
							<li id="SIModelLineMeta" data-toggle='modal' data-target="#myModal" aria-haspopup="true"
								aria-expanded="false"> SI&PTH model line
							</li>
							<div class="dropdown-divider"></div>
							<li onclick="settingMail('PUBLISH')">List Email Publish
							</li>
							<div class="dropdown-divider"></div>
							<li onclick="settingMail('NOTIFY')">List Email Notify
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div class="row no-margin">
			<div class=" pre-scrollable table-responsive" style="color: #333333; max-height: calc(100vh - 120px);">
				<table id="tblLineBalance" class="table table-xxs table-sticky table-bordered"
					style="margin-bottom: 10px; color: #cfcfcf;">
					<thead>
						<tr>
							<th style=" width: 10px;">#</th>
							<th>CFT</th>
							<th>Model<br />Sản Phẩm</th>
							<th>Side<br /> Mặt bản</th>
							<th>LinkQty<br /> Số bản liền</th>
							<th>Line <br />Chuyền</th>
							<th>Layout</th>
							<th>MounterNumber<br /> Số máy dán</th>
							<th>Printer<br /> Máy in</th>
							<th width="50px">H1</th>
							<th width="50px">H2</th>
							<th width="50px">H3</th>
							<th width="50px">H4</th>
							<th width="50px">H5</th>
							<th width="50px">G1</th>
							<th width="50px">G2</th>
							<th width="50px">G3</th>
							<th>Reflow<br /> Lò hàn</th>
							<th>Line balance<br />Cân bằng dây chuyền</th>
							<th>Real CT<br /> Giờ công thực tế </th>
							<th>PCAS CT<br /> Giờ công hệ thống</th>
							<th>Diff<br /> Chênh lệch</th>
							<th>Status<br /> Đánh giá bất thường</th>
						</tr>
					</thead>
					<tbody>
						<tr></tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

<!-- Modal -->
<div class="modal fade" id="myModal" role="dialog">
	<div class="modal-dialog modal-xl">
		<!-- Modal content-->
		<div class="modal-content" style="background: #3c3c3c;font-size:13px;">
			<div class="modal-header" id="header_config">
				<h4 style="margin:0px;" class="config">IE line config</h4>
				<button type="button" onclick="addNewClick()" class="btn btn-default add-new"><i
						class="fa fa-plus"></i>Add</button>

			</div>
			<div class="modal-body" style="padding: 0px 20px;">
				<div class="table-wrapper col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin: 0px;padding:0">

					<div style="overflow-x:auto" id="myTable">
						<table class="table table-bordered" id="tblModal">
							<thead>
								<tr>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td></td>
								</tr>
							</tbody>
						</table>
					</div>
					<div id="pagination-bar"></div>
				</div>
			</div>
			<div class="modal-footer" style="padding: 5px 30px;margin-bottom: 10px;">
				<button type="button" data-dismiss="modal"
					style="background: inherit;border-radius: 5px;border: 1px solid;">Close</button>
			</div>
		</div>

	</div>
</div>

<!-- Modal Setting Email-->
<div class="modal fade" id="modal-setting">
	<div class="modal-dialog">
		<div class="modal-content" style="background: #3c3c3c;font-size:13px;">
			<div class="modal-header">
				<h4 class="modal-title email-title">List Email</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-sm-12">
						<label id="lblType" class="hidden"></label>
						<div class="row" style="margin-bottom: 15px;">
							<div class="col-sm-12 no-padding table-responsive pre-scrollable"
								style="max-height: calc(100vh - 450px);">
								<table id="tblListMail" class="table table-small table-sticky table-bordered">
									<thead>
										<tr>
											<th>No</th>
											<th>Factory</th>
											<th>Email</th>
											<th>Enabled</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</div>
						</div>
						<label class="control-label text-bold no-margin">Input List Email </br>Example :
							email1@mail.foxconn.vn, email2@mail.foxconn.vn, email3@mail.foxconn.vn, ...</label>
						<textarea class="form-control" style="padding: 5px; color: #DDDDDD" name="txtListMail"
							type="text" value="" spellcheck="false" rows="5"></textarea>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-xs"
					style="width: 80px; background-color: inherit; border: 1px solid #DDDDDD; color: #DDDDDD;"
					data-dismiss="modal" onclick="saveMail()">Save<i
						class="fa fa-check-circle position-right"></i></button>
			</div>
		</div>
	</div>
</div>


<!-- Add new IE Line config-->
<div class="modal fade" id="modal-addnewIELineConfig">
	<div class="modal-dialog">
		<div class="modal-content" style="background: #3c3c3c;font-size:13px;">
			<div class="modal-header">
				<h4 class="modal-title email-title">Add new</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<table id="tblAdd" class="table table-small table-sticky">
						<tbody>
							<tr>
								<td>Factory: </td>
								<td><input type="text" id="factoryIE" class="form-control" value="B06"></td>
								<td></td>
								<td>CFT: </td>
								<td>
									<select class="bootstrap-select" id="cftIE">
										<option value="UBEE">UBEE</option>
										<option value="UI">UI</option>
										<option value="NIC">NIC</option>
										<option value="AIRSPAN">AIRSPAN</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>Line: </td>
								<td><input id="lineIE" type="text" class="form-control"></td>
								<td></td>
								<td>Layout: </td>
								<td><input id="layoutIE" type="text" class="form-control"></td>
							</tr>
							<tr>
								<td>Mounter number:</td>
								<td><input id="mouter_number" type="number" class="form-control"></td>
								<td></td>
								<td>Printer number: </td>
								<td><input id="printer_number" type="number" class="form-control"></td>
							</tr>
							<tr>
								<td>Printer time:</td>
								<td><input id="printer_time" type="number" class="form-control"></td>
								<td></td>
								<td>Placement ability: </td>
								<td><input id="placement_ability" type="number" class="form-control"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-xs btndismiss" id="btnSaveIELineConfig" data-dismiss="modal">Save
					<i class="fa fa-check-circle position-right"></i></button>
			</div>
		</div>
	</div>
</div>

<!-- Add new Model Meta-->
<div class="modal fade" id="modal-addnewModelMeta">
	<div class="modal-dialog">
		<div class="modal-content" style="background: #3c3c3c;font-size:13px;">
			<div class="modal-header">
				<ul class="nav nav-tabs" style="margin: 5px 0;">
					<li id="li-edit" class="active"><a data-toggle="tab" href="#tab-add"
							style="font-size:13px; padding: 8px;">Add new</a></li>
					<li id="li-generate"><a data-toggle="tab" href="#tab-import"
							style="font-size:13px; padding: 8px;">Import</a></li>
				</ul>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="tab-content">
					<div id="tab-add" class="tab-pane active">
						<div class="row">
							<table id="tblAdd" class="table table-small table-sticky">
								<tbody>
									<tr>
										<td>Factory : </td>
										<td><input id="factoryModelMeta" type="text" class="form-control" value="B06">
										</td>
										<td></td>
										<td>CFT : </td>
										<td>
											<select id="cftModelMeta" class="bootstrap-select">
												<option value="SI">UBEE</option>
												<option value="PTH">UI</option>
												<option value="SMT">NIC</option>
												<option value="SMT">AIRSPAN</option>
											</select>
										</td>
									</tr>
									<tr>
										<td>Model T : </td>
										<td><input id="modelModelMeta" type="text" class="form-control"></td>
										<td></td>
										<td>Model . : </td>
										<td><input id="modelSIModelMeta" type="number" class="form-control"></td>
									</tr>
									<tr>
										<td>Link Qty :</td>
										<td colspan="4"><input id="link_qty" type="number" class="form-control"></td>
									</tr>
									<tr>
										<td>Panel length : </td>
										<td colspan="4"><input id="panel_length" type="number" class="form-control">
										</td>
									</tr>
									<tr>
										<td>SMT component :</td>
										<td colspan="4"><input id="SMT_component" type="number" class="form-control">
										</td>
									</tr>
								</tbody>
							</table>
						</div>
						<button class="btn btn-xs btndismiss" id="btnSaveModelMeta" data-dismiss="modal"
							style="float: right;margin-top: 1rem;">Save<i
								class="fa fa-check-circle position-right"></i></button>
					</div>
					<div id="tab-import" class="tab-pane">
						<div class="row addLayout">
							<div class="col-sm-12" style="padding: 0;">
								<input type="file" class="form-control btn-border" id="uploadFile">
							</div>
						</div>
						<button class="btn btn-xs btndismiss" id="btnSubmitUpload" style="float: right;"
							data-dismiss="modal">Upload<i class="fa fa-upload"
								style="margin-left: 0.5rem;"></i></button>
					</div>
				</div>
			</div>
			<div class="modal-footer">
			</div>
		</div>
	</div>
</div>

<!-- Add new SMT Model Line Meta-->
<div class="modal fade" id="modal-addnewModelLineMeta">
	<div class="modal-dialog">
		<div class="modal-content" style="background: #3c3c3c;font-size:13px;">
			<div class="modal-header">
				<h4 class="modal-title email-title">Add new</h4>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<table id="tblAdd" class="table table-small table-sticky">
						<tbody>
							<tr>
								<td>Factory: </td>
								<td><input id="factory3" type="text" class="form-control" value="B06"></td>
								<td></td>
								<td>Section: </td>
								<td><input id="section3" type="text" class="form-control"></td>
							</tr>
							<tr>
								<td>Model: </td>
								<td><input id="model3" type="text" class="form-control"></td>
								<td></td>
								<td>Line: </td>
								<td><input id="line3" type="text" class="form-control"></td>
							</tr>
							<tr>
								<td>Side: </td>
								<td><select id="side3" class="bootstrap-select">
										<option value="T">T</option>
										<option value="B">B</option>
									</select></td>
								<td></td>
								<td>Cycle time: </td>
								<td><input id="cycle_time3" type="number" class="form-control"></td>
							</tr>
							<tr>
								<td>Reflow speed:</td>
								<td colspan="4"><input id="reflow_speed3" type="number" class="form-control"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-xs btndismiss" id="btnSaveModelLineMeta" data-dismiss="modal">Save<i
						class="fa fa-check-circle position-right"></i></button>
			</div>
		</div>
	</div>
</div>

<!-- Add new SI Model Line Meta-->
<div class="modal fade" id="modal-addnewSIModelLineMeta">
	<div class="modal-dialog">
		<div class="modal-content" style="background: #3c3c3c;font-size:13px;">
			<div class="modal-header">
				<ul class="nav nav-tabs" style="margin: 5px 0;">
					<li id="li-edit" class="active"><a data-toggle="tab" href="#tab-addSI"
							style="font-size:13px; padding: 8px;">Add new</a></li>
					<li id="li-generate"><a data-toggle="tab" href="#tab-importSI"
							style="font-size:13px; padding: 8px;">Import</a></li>
				</ul>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
			</div>
			<div class="modal-body">
				<div class="tab-content">
					<div id="tab-addSI" class="tab-pane active">
						<div class="row">
							<table id="tblSIAdd" class="table table-small table-sticky">
								<tbody>
									<tr>
										<td>Factory: </td>
										<td><input id="factory4" type="text" class="form-control" value="B06"></td>
										<td></td>
										<td>Section: </td>
										<td><input id="section4" type="text" class="form-control"></td>
									</tr>
									<tr>
										<td>Model: </td>
										<td><input id="model4" type="text" class="form-control"></td>
										<td></td>
										<td>Line: </td>
										<td><input id="line4" type="text" class="form-control"></td>
									</tr>
									<tr>
										<td>Side: </td>
										<td><select id="side4" class="bootstrap-select">
												<option value="T">T</option>
												<option value="B">B</option>
											</select></td>
										<td></td>
										<td>Cycle time: </td>
										<td><input id="cycle_time4" type="number" class="form-control"></td>
									</tr>
									<tr>
										<td>Man power:</td>
										<td colspan="4"><input id="man_power4" type="number" class="form-control"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<button class="btn btn-xs btndismiss" id="btnSaveModelLineMeta" data-dismiss="modal">Save<i
								class="fa fa-check-circle position-right"></i></button>
					</div>
					<div id="tab-importSI" class="tab-pane">
						<div class="row addLayout">
							<div class="col-sm-12" style="padding: 0;">
								<input type="file" class="form-control btn-border" id="siLineUpload">
							</div>
						</div>
						<button class="btn btn-xs btndismiss" id="btnSubmitUploadSI" style="float: right;"
							data-dismiss="modal">Upload<i class="fa fa-upload"
								style="margin-left: 0.5rem;"></i></button>
					</div>
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
	$('#header span').html(dataset.factory + ' - Line Balance Report');
	UpLoadData();
	function UpLoadData() {
		$.ajax({
			type: "GET",
			url: "/api/smt/lineMeta/status",
			// url: "http://10.224.81.70:8888/api/smt/lineMeta/status",
			data: {
				factory: dataset.factory
			},
			cache: false,
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				$('#tblLineBalance tbody tr').remove();
				var response = data.data;
				if (!$.isEmptyObject(data)) {
					var modalTable = "";
					var index;
					for (i in response) {
						index = parseInt(i) + 1;
						modalTable = '<tr><td>' + index + '</td>' +
							'<td>' + response[i].cft + '</td>' +
							'<td>' + response[i].modelName + '</td>' +
							'<td>' + response[i].side + '</td>' +
							'<td>' + response[i].linkQty + '</td>' +
							'<td>' + response[i].lineName + '</td>' +
							'<td>' + response[i].layout + '</td>' +
							'<td>' + response[i].mounterNumber + '</td>' +
							'<td>' + (response[i].printerCycleTime).toFixed(2) + '</td>' +
							'<td id="h1' + i + '"></td>' +
							'<td id="h2' + i + '"></td>' +
							'<td id="h3' + i + '"></td>' +
							'<td id="h4' + i + '"></td>' +
							'<td id="h5' + i + '"></td>' +
							'<td id="g1' + i + '"></td>' +
							'<td id="g2' + i + '"></td>' +
							'<td id="g3' + i + '"></td>' +
							'<td>' + (response[i].reflowCycleTime).toFixed(2) + '</td>' +
							'<td id="LineBalance' + i + '">' + (response[i].lineBalance).toFixed(2) + '%</td>' +
							'<td>' + (response[i].realCycleTime).toFixed(2) + '</td>' +
							'<td>' + (response[i].pcasCycleTime).toFixed(2) + '</td>' +
							'<td id="Diff' + i + '">' + (response[i].diff).toFixed(2) + '%</td>' +
							'<td>' + response[i].status + '</td></tr>';
						$("#tblLineBalance tbody").append(modalTable);
						if (response[i].lineBalance < 95) {
							$("#LineBalance" + i + "").addClass('colorHighLight');
						}
						if (response[i].diff > 5) {
							$("#Diff" + i + "").addClass('colorHighLight');
						}
						for (j in response[i].mounterMetaList) {
							if (response[i].mounterMetaList[j].layoutSlot == "H1") {
								$("#h1" + i + "").html((response[i].mounterMetaList[j].cycleTime).toFixed(2));
							}
							if (response[i].mounterMetaList[j].layoutSlot == "H2") {
								$("#h2" + i + "").html((response[i].mounterMetaList[j].cycleTime).toFixed(2));
							}
							if (response[i].mounterMetaList[j].layoutSlot == "H3") {
								$("#h3" + i + "").html((response[i].mounterMetaList[j].cycleTime).toFixed(2));
							}
							if (response[i].mounterMetaList[j].layoutSlot == "H4") {
								$("#h4" + i + "").html((response[i].mounterMetaList[j].cycleTime).toFixed(2));
							}
							if (response[i].mounterMetaList[j].layoutSlot == "H5") {
								$("#h5" + i + "").html((response[i].mounterMetaList[j].cycleTime).toFixed(2));
							}
							if (response[i].mounterMetaList[j].layoutSlot == "G1") {
								$("#g1" + i + "").html((response[i].mounterMetaList[j].cycleTime).toFixed(2));
							}
							if (response[i].mounterMetaList[j].layoutSlot == "G2") {
								$("#g2" + i + "").html((response[i].mounterMetaList[j].cycleTime).toFixed(2));
							}
							if (response[i].mounterMetaList[j].layoutSlot == "G3") {
								$("#g3" + i + "").html((response[i].mounterMetaList[j].cycleTime).toFixed(2));
							}
						}
					};
				}
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	var status;
	var lastIndex;
	var tableModal;
	var actionTD;
	function loadDataConfig(status) {
		$.ajax({
			type: "GET",
			url: "/api/smt/" + status,
			// url: "http://10.224.81.70:8888/api/smt/" + status,
			data: {
				factory: dataset.factory
			},
			cache: false,
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				var response = data.data;
				if (!$.isEmptyObject(data)) {
					$('#myTable').empty();
					var tableFixed
					if (status == "lineMeta") {
						$('.config').html('IE line config');
						tableFixed =
							'<table class="table table-bordered" id="tblModal">' +
							'<thead>' +
							'<tr><th>No.<br/>STT</th><th>Factory<br/>Xưởng</th><th>CFT</th><th>Line<br/>Chuyền</th><th>Layout</th><th>Mounter number<br/>Số máy dán</th>' +
							'<th>Printer number<br/>Số máy in</th><th>Printer time<br/>Thời gian máy in</th><th>Placement ability<br/>Năng lực dán kiện</th><th>Actions</th></tr>' +
							'</thead>' +
							'<tbody>' +
							'</tbody>' +
							'</table>';
					}
					if (status == "modelMeta") {
						$('.config').html('Modal Meta config');
						tableFixed = '<table class="table table-bordered" id="tblModal">' +
							'<thead>' +
							'<tr><th>No.<br/>STT</th><th>Factory<br/>Xưởng</th><th>CFT</th><th>Model T</th><th>Model .</th>' +
							'<th>LinkQty<br/>Số bản liền</th><th>Panel length<br/> Độ dài panel(cm)</th>' +
							'<th>SMT Components <br/>Số linh kiện SMT(B&T)</th><th>Actions</th></tr>' +
							'</thead>' +
							'<tbody>' +
							'</tbody>' +
							'</table>';
					}
					$('#myTable').html(tableFixed);
					tableModal = $('#tblModal').DataTable({
						"bPaginate": true,
						"bLengthChange": false,
						"bFilter": true,
						"bInfo": false,
						"bAutoWidth": false
					});

					tableModal.clear();
					var index;

					for (i in response) {
						index = parseInt(i) + 1;
						lastIndex = parseInt(i) + 1;
						var rowTable;
						actionTD = '<a class="addnew" title="Add" data-toggle="tooltip"><i class="fa fa-check-circle"></i></a>' +
							'<a class="add" onclick="addEdit(' + response[i].id + ')" title="Add" data-toggle="tooltip"><i class="fa fa-check-circle"></i></a>' +
							'<a class="edit" title="Edit" data-toggle="tooltip"><i class="fa fa-pencil"></i></i></a>' +
							'<a class="cancel" title="Cancel" data-toggle="tooltip"><i class="fa fa-close"></i></a>' +
							'<a class="delete" onclick="deleteClick(' + response[i].id + ')" title="Delete" data-toggle="tooltip" ><i class="fa fa-trash"></i></a>';
						if (status == "lineMeta") {
							rowTable = [
								index,
								response[i].factory,
								response[i].cft,
								response[i].lineName,
								response[i].layout,
								response[i].mounterNumber,
								response[i].printerNumber,
								response[i].printerTime,
								response[i].ability,
								actionTD];
						}
						if (status == "modelMeta") {
							rowTable = [
								index,
								response[i].factory,
								response[i].cft,
								response[i].modelName,
								response[i].modelNameSI,
								response[i].linkQty,
								response[i].panelLength,
								response[i].totalPart,
								actionTD];
						}
						tableModal.row.add(rowTable).draw();
					};
					$(".add-new").removeAttr("disabled");
					$('#tblModal tr').find('td:last-child').addClass('action');
				}
			},
			failure: function (errMsg) {
				console.log(errMsg);
			}
		});
	}
	function lineMetaClick() {
		status = 'lineMeta';
		$('#header_config').html('<h4 style="margin:0px;" class="config">IE line config</h4>' +
			'<button type="button" onclick="addNewClick()" class="btn btn-default add-new"><i class="fa fa-plus"></i>Add</button>');
		loadDataConfig(status);
		$('#pagination-bar').addClass('hidden');
	}
	function modelMetaClick() {
		status = 'modelMeta';
		$('#header_config').html('<h4 style="margin:0px;" class="config">IE line config</h4>' +
			'<button type="button" onclick="addNewClick()" class="btn btn-default add-new"><i class="fa fa-plus"></i>Add</button>');
		loadDataConfig(status);
		$('#pagination-bar').addClass('hidden');
	}
	var items;
	var section;
	$('#modelLineMeta').on('click', function () {
		$('#header_config').html('<h4 style="margin:0px;" class="config">IE line config</h4>' +
			'<button type="button" onclick="addNewClick()" class="btn btn-default add-new"><i class="fa fa-plus"></i>Add</button>');
		loadModelLineMeta('SMT');
	})
	$('#SIModelLineMeta').on('click', function () {
		loadModelLineMeta('SI');
		$('#header_config').html('<ul class="nav nav-tabs" style="margin: 5px 0;">' +
			'<li id="liSI" class="active"><a data-toggle="tab" style="font-size:13px; padding: 8px;">SI</a></li>' +
			'<li id="liPTH"><a data-toggle="tab" style="font-size:13px; padding: 8px;">PTH</a></li>' +
			'<li id="liAdd"><button type="button" onclick="addNewClick()" class="btn btn-default add-new"><i class="fa fa-plus"></i>Add</button></li></ul>');
		$('#liSI').on('click', function () {
			loadModelLineMeta('SI');
		});
		$('#liPTH').on('click', function () {
			loadModelLineMeta('PTH');
		});

	})
	function modelLineMetaClick(page) {
		$('#pagination-bar').removeClass('hidden');
		status = 'modelLineMeta';
		$.ajax({
			type: "GET",
			url: "/api/smt/modelLineMeta",
			// url: "http://10.224.81.70:8888/api/smt/modelLineMeta",
			data: {
				factory: dataset.factory,
				page: page,
				size: 10,
				sectionName: section
			},
			cache: false,
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				var response = data.data;
				if (!$.isEmptyObject(data)) {
					$('#myTable').html('');
					var tableFixed;
					$('.config').html(section + ' line meta config');
					if (section == 'SMT') {
						var thShow = '<th>CycleTime</th><th>Reflow speed<br/>Vận tốc lò hàn(cm/min)</th>';
					}
					else {
						var thShow = '<th>ManPower</th><th>CycleTime</th>';
					}
					tableFixed = '<table class="table table-bordered" id="tblModal">' +
						'<thead>' +
						'<tr><th>No.<br/>STT</th><th>Factory<br/>Xưởng</th><th>SectionName</th><th>Model<br/>Tên hàng</th><th>LineName</th>' +
						'<th>Side<br/>Mặt</th>' + thShow + '<th>Actions</th></tr>' +
						'</thead>' +
						'<tbody>' +
						'</tbody>' +
						'</table>';
					$('#myTable').html(tableFixed);
					var index = 0;
					if (page > 0) {
						index = Number(page) * 10;
					}
					for (i in response) {
						index++;
						lastIndex = parseInt(i) + 1;
						var rowTable;
						if (section == 'SMT') {
							var tdShow = '<td>' + toStringg(response[i].cycleTime, '') + '</td>' +
								'<td>' + toStringg(response[i].reflowSpeed, '') + '</td>';
						}
						else {
							var tdShow = '<td>' + toStringg(response[i].manPower, '') + '</td>' +
								'<td>' + toStringg(response[i].cycleTime, '') + '</td>';
						}
						actionTD = '<a class="addnew" title="Add" data-toggle="tooltip"><i class="fa fa-check-circle"></i></a>' +
							'<a class="add" onclick="addEdit(' + response[i].id + ')" title="Add" data-toggle="tooltip"><i class="fa fa-check-circle"></i></a>' +
							'<a class="edit" title="Edit" data-toggle="tooltip"><i class="fa fa-pencil"></i></i></a>' +
							'<a class="cancel" title="Cancel" data-toggle="tooltip"><i class="fa fa-close"></i></a>' +
							'<a class="delete" onclick="deleteClick(' + response[i].id + ')" title="Delete" data-toggle="tooltip" ><i class="fa fa-trash"></i></a>';
						$('#tblModal').append('<tr><td>' + index + '</td>' +
							'<td>' + response[i].factory + '</td>' +
							'<td>' + toStringg(response[i].sectionName, '') + '</td>' +
							'<td>' + response[i].modelName + '</td>' +
							'<td>' + toStringg(response[i].lineName, '') + '</td>' +
							'<td>' + toStringg(response[i].side, '') + '</td>' +
							tdShow +
							'<td>' + actionTD + '</td></tr>')
					};

					$(".add-new").removeAttr("disabled");
					$('#tblModal tr').find('td:last-child').addClass('action');
				}
			},
			failure: function (errMsg) {
				console.log(errMsg);
			},
			complete: function () {
			}
		});
	}

	function loadModelLineMeta(sectionName) {
		section = sectionName;
		$('#pagination-bar').removeClass('hidden');
		status = 'modelLineMeta';
		$.ajax({
			type: "GET",
			url: "/api/smt/modelLineMeta",
			// url: "http://10.224.81.70:8888/api/smt/modelLineMeta",
			data: {
				factory: dataset.factory,
				page: 0,
				size: 10,
				sectionName: sectionName
			},
			cache: false,
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				var response = data.data;
				if (!$.isEmptyObject(data)) {
					items = data.size;
					$('#myTable').html('');
					var tableFixed;
					$('.config').html(sectionName + ' line meta config');
					if (section == 'SMT') {
						var thShow = '<th>CycleTime</th><th>Reflow speed<br/>Vận tốc lò hàn(cm/min)</th>';
					}
					else {
						var thShow = '<th>ManPower</th><th>CycleTime</th>';
					}
					tableFixed = '<table class="table table-bordered" id="tblModal">' +
						'<thead>' +
						'<tr><th>No.<br/>STT</th><th>Factory<br/>Xưởng</th><th>SectionName</th><th>Model<br/>Tên hàng</th><th>LineName</th>' +
						'<th>Side<br/>Mặt</th>' + thShow + '<th>Actions</th></tr>' +
						'</thead>' +
						'<tbody>' +
						'</tbody>' +
						'</table>';
					$('#myTable').html(tableFixed);
					var index;
					for (i in response) {
						index = parseInt(i) + 1;
						lastIndex = parseInt(i) + 1;
						var rowTable;
						if (section == 'SMT') {
							var tdShow = '<td>' + toStringg(response[i].cycleTime, '') + '</td>' +
								'<td>' + toStringg(response[i].reflowSpeed, '') + '</td>';
						}
						else {
							var tdShow = '<td>' + toStringg(response[i].manPower, '') + '</td>' +
								'<td>' + toStringg(response[i].cycleTime, '') + '</td>';
						}
						actionTD = '<a class="addnew" title="Add" data-toggle="tooltip"><i class="fa fa-check-circle"></i></a>' +
							'<a class="add" onclick="addEdit(' + response[i].id + ')" title="Add" data-toggle="tooltip"><i class="fa fa-check-circle"></i></a>' +
							'<a class="edit" title="Edit" data-toggle="tooltip"><i class="fa fa-pencil"></i></i></a>' +
							'<a class="cancel" title="Cancel" data-toggle="tooltip"><i class="fa fa-close"></i></a>' +
							'<a class="delete" onclick="deleteClick(' + response[i].id + ')" title="Delete" data-toggle="tooltip" ><i class="fa fa-trash"></i></a>';
						$('#tblModal').append('<tr><td>' + index + '</td>' +
							'<td>' + response[i].factory + '</td>' +
							'<td>' + toStringg(response[i].sectionName, '') + '</td>' +
							'<td>' + response[i].modelName + '</td>' +
							'<td>' + toStringg(response[i].lineName, '') + '</td>' +
							'<td>' + toStringg(response[i].side, '') + '</td>' +
							tdShow +
							'<td>' + actionTD + '</td></tr>')
					};

					$(".add-new").removeAttr("disabled");
					$('#tblModal tr').find('td:last-child').addClass('action');
					$('#pagination-bar').pagination({
						items: data.size,
						itemsOnPage: 10,
						cssStyle: 'dark-theme'
					});
				}
			},
			failure: function (errMsg) {
				console.log(errMsg);
			},
			complete: function () {
			}
		});
	}
	var idAdd;
	function cancelRow(r) {
		var i = r.parentNode.parentNode.rowIndex;
		document.getElementById("tblModal").deleteRow(i);
		$(".add-new").removeAttr("disabled");

	}
	$(document).on("click", ".cancelAddNew", function () {
		var tableRow = tableModal.row($(this).parents('tr'));
		tableModal.row(tableRow).remove();
		lastIndex--;

	});
	$(document).on("click", "#btnSaveIELineConfig", function () {
		data = {
			factory: $('#factoryIE').val(),
			cft: $('#cftIE').val(),
			lineName: $('#lineIE').val(),
			layout: $('#layoutIE').val(),
			mounterNumber: $('#mouter_number').val(),
			printerNumber: $('#printer_number').val(),
			printerTime: $('#printer_time').val(),
			ability: $('#placement_ability').val(),
		};
		$.ajax({
			type: "POST",
			url: "/api/smt/" + status,
			// url: "http://10.224.81.70:8888/api/smt/" + status,
			contentType: "application/json; charset=utf-8",
			cache: false,
			data: JSON.stringify(data),
			success: function (data) {
				$('#modal-addnewIELineConfig .modal-footer').html('<button class="btn btn-xs btndismiss" id="btnSaveIELineConfig" data-dismiss="modal">Save' +
					'<i class="fa fa-check-circle position-right"></i></button>');
				alert(data.message);
				UpLoadData();
				loadDataConfig(status);
			},
			error: function (e) {
				$('#modal-addnewIELineConfig .modal-footer').html('<button class="btn btn-xs btndismiss" id="btnSaveIELineConfig" data-dismiss="modal">Save' +
					'<i class="fa fa-check-circle position-right"></i></button>');
				alert(e.responseJSON.message);

			}
		});
	})
	$(document).on("click", "#btnSaveModelMeta", function () {
		data = {
			factory: $('#factoryModelMeta').val(),
			cft: $('#cftModelMeta').val(),
			modelName: $('#modelModelMeta').val(),
			modelNameSI: $('#modelSIModelMeta').val(),
			linkQty: $('#link_qty').val(),
			panelLength: $('#panel_length').val(),
			totalPart: $('#SMT_component').val(),
		};
		$.ajax({
			type: "POST",
			url: "/api/smt/" + status,
			// url: "http://10.224.81.70:8888/api/smt/" + status,
			contentType: "application/json; charset=utf-8",
			cache: false,
			data: JSON.stringify(data),
			success: function (data) {
				alert(data.message);
				$('#modal-addnewModelMeta .modal-footer').html('<button class="btn btn-xs btndismiss" id="btnSaveIELineConfig" data-dismiss="modal">Save' +
					'<i class="fa fa-check-circle position-right"></i></button>');
				UpLoadData();
				$('#myModal').show();
				loadDataConfig(status);
			},
			error: function (e) {
				$('#modal-addnewModelMeta .modal-footer').html('<button class="btn btn-xs btndismiss" id="btnSaveIELineConfig" data-dismiss="modal">Save' +
					'<i class="fa fa-check-circle position-right"></i></button>');
				alert(e.responseJSON.message);

			}
		});
	})
	$(document).on("click", "#btnSaveModelLineMeta", function () {
		if (section == 'SMT') {
			data = {
				factory: $('#factory3').val(),
				sectionName: $('#section3').val(),
				modelName: $('#model3').val(),
				lineName: $('#line3').val(),
				side: $('#side3').val(),
				cycleTime: $('#cycle_time3').val(),
				reflowSpeed: $('#reflow_speed3').val(),
			};
		}
		else {
			data = {
				factory: $('#factory4').val(),
				sectionName: $('#section4').val(),
				modelName: $('#model4').val(),
				lineName: $('#line4').val(),
				side: $('#side4').val(),
				manPower: $('#man_power4').val(),
				cycleTime: $('#cycle_time4').val(),
			};
		}
		$.ajax({
			type: "POST",
			url: "/api/smt/" + status,
			// url: "http://10.224.81.70:8888/api/smt/" + status,
			contentType: "application/json; charset=utf-8",
			cache: false,
			data: JSON.stringify(data),
			success: function (data) {
				// $('#modal-addnewModelLineMeta .modal-footer').html('<button class="btn btn-xs btndismiss" id="btnSaveModelLineMeta" data-dismiss="modal">Save' +
				// 	'<i class="fa fa-check-circle position-right"></i></button>');
				alert(data.message);
				UpLoadData();
				loadModelLineMeta(section)
			},
			error: function (e) {
				// $('#modal-addnewModelLineMeta .modal-footer').html('<button class="btn btn-xs btndismiss" id="btnSaveModelLineMeta" data-dismiss="modal">Save' +
				// 	'<i class="fa fa-check-circle position-right"></i></button>');
				alert(e.responseJSON.message);
			}
		});
	})
	function addNewClick() {
		var data;
		if (status == 'lineMeta') {
			$('#modal-addnewIELineConfig').modal('show');
		}
		if (status == 'modelMeta') {
			$('#modal-addnewModelMeta').modal('show');

		}
		if (status == 'modelLineMeta') {
			if (section == 'SMT') {
				$('#modal-addnewModelLineMeta').modal('show');
				$('#section3').val(section);
			}
			else {
				$('#modal-addnewSIModelLineMeta').modal('show');
				$('#section4').val(section);
			}

		}
	}


	$(document).on("click", ".edit", function () {
		$(this).parents("tr").find("td:not(:last-child,:first-child)").each(function () {
			$(this).html('<input type="text" class="form-control" value="' + $(this).text() + '">');
		});
		$(this).parents("tr").find(".add, .edit").toggle();
		$(this).parents("tr").find(".cancel, .delete").toggle();
		$(".add-new").attr("disabled", "disabled");
	});
	$(document).on("click", ".add", function () {
		var input = $(this).parents("tr").find('input[type="text"]');
		input.each(function () {
			$(this).parent("td").html($(this).val());
		});
		$(this).parents("tr").find(".add, .edit").toggle();
		$(this).parents("tr").find(".cancel, .delete").toggle();
		$(".add-new").removeAttr("disabled");
	});
	$(document).on("click", ".cancel", function () {
		var input = $(this).parents("tr").find('input[type="text"]');
		input.each(function () {
			$(this).parent("td").html($(this).val());
		});
		$(this).parents("tr").find(".add, .edit").toggle();
		$(this).parents("tr").find(".cancel, .delete").toggle();
		$(".add-new").removeAttr("disabled");
	});
	function addEdit(idEdit) {
		var editValue = [];
		$("input[type='text']").each(function () {
			editValue.push($(this).val());
		});
		var data;
		if (status == "lineMeta") {
			data = {
				factory: editValue[0],
				cft: editValue[1],
				lineName: editValue[2],
				layout: editValue[3],
				mounterNumber: editValue[4],
				printerNumber: editValue[5],
				printerTime: editValue[6],
				ability: editValue[7],
			};
		}
		if (status == "modelMeta") {
			data = {
				factory: editValue[0],
				cft: editValue[1],
				modelName: editValue[2],
				modelNameSI: editValue[3],
				linkQty: editValue[4],
				panelLength: editValue[5],
				totalPart: editValue[6],
			};
		}
		if (status == "modelLineMeta") {
			if (section == 'SMT') {
				data = {
					factory: editValue[0],
					sectionName: editValue[1],
					modelName: editValue[2],
					lineName: editValue[3],
					side: editValue[4],
					cycleTime: editValue[5],
					reflowSpeed: editValue[6],
				};
			}
			else {
				data = {
					factory: editValue[0],
					sectionName: editValue[1],
					modelName: editValue[2],
					lineName: editValue[3],
					side: editValue[4],
					manPower: editValue[5],
					cycleTime: editValue[6],
				};
			}

		}
		$.ajax({
			type: "PUT",
			url: "/api/smt/" + status + "/" + idEdit,
			// url: "http://10.224.81.70:8888/api/smt/" + status + "/" + idEdit,
			contentType: "application/json; charset=utf-8",
			data: JSON.stringify(data),
			cache: false,
			success: function (data) {
				UpLoadData();
			},
			failure: function (errMsg) {
				console.log(errMsg);
				alert("Confirm failed!");
			}
		});
	}
	var megDelete;
	function deleteClick(idDelete) {
		megDelete = confirm("Do you want to detele?");
		if (megDelete == true) {
			$.ajax({
				type: "DELETE",
				url: "/api/smt/" + status + "/" + idDelete,
				// url: "http://10.224.81.70:8888/api/smt/" + status + "/" + idDelete,
				contentType: "application/json; charset=utf-8",
				cache: false,
				success: function (data) {
					UpLoadData();
				},
				failure: function (errMsg) {
					console.log(errMsg);
					alert("Confirm failed!");
				},
				error: function (errMsg) {
					console.log(errMsg);
				}
			});
		}
	}


	function settingMail(type) {
		$('#modal-setting').modal('show');
		$('.loader').removeClass('hidden');
		$('#lblType').html(type);
		$('.email-title').html('List Email ' + type);
		$.ajax({
			type: "GET",
			url: "/api/test/email-list",
			// url: "http://10.224.81.70:8888/api/test/email-list",
			data: {
				factory: dataset.factory,
				department: "IE",
				group: type
			},
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				$('textarea[name=txtListMail]').val('');
				$('textarea[name=txtListMail]').focus();
				if (!$.isEmptyObject(data)) {
					$('#tblListMail>tbody').html('');
					var html = '';
					var textValue = '';
					for (i in data) {
						html += '<tr><td>' + (Number(i) + 1) + '</td><td>' + data[i].factory + '</td><td>' + data[i].email + '</td><td>' + data[i].enabled + '</td></tr>'
						if (data[i].enabled) {
							textValue += data[i].email + ',';
						}
					}
					$('#tblListMail>tbody').html(html);
					$('textarea[name=txtListMail]').val(textValue);
				}
			},
			failure: function (errMsg) {
				console.log(errMsg);
			},
			complete: function () {
				$('.loader').addClass('hidden');
			}
		});
	}

	function saveMail() {
		$('#modal-setting').modal('hide');
		$('.loader').removeClass('hidden');
		var type = $('#lblType').html();
		var listMail = $('textarea[name=txtListMail]').val().replace(/\n|\s/g, '');
		// console.log(type);
		var form = {
			factory: dataset.factory,
			emailList: listMail,
			group: type
		}
		$.ajax({
			type: "POST",
			url: "/api/test/email-list",
			data: JSON.stringify(form),
			contentType: "application/json; charset=utf-8",
			success: function (data) {
				if (!data) {
					alert('Setting FAIL!');
				} else {
					alert('Setting Success!');
				}
			},
			failure: function (errMsg) {
				console.log(errMsg);
			},
			complete: function () {
				$('.loader').addClass('hidden');
			}
		});
	}
	$(document).on("click", ".delete", function () {
		if (megDelete == true) {
			$(this).parents("tr").remove();
			$(".add-new").removeAttr("disabled");
			var tableRow = tableModal.row($(this).parents('tr'));
			tableModal.row(tableRow).remove();
		}
	});

	$('#btnSubmitUpload').on('click', function () {
		var form = new FormData();
		form.append('factory', dataset.factory);
		file = $('#uploadFile').get(0);
		if (file.files.length > 0) {
			// if (file.files[0].name.indexOf('.xlsx') > -1) {
			form.append('file', file.files[0]);
			$.ajax({
				type: "POST",
				url: "/api/smt/modelMeta/upload",
				// url: "http://10.224.81.70:8888/api/smt/modelMeta/upload",
				data: form,
				processData: false,
				contentType: false,
				mimeType: "multipart/form-data",
				success: function (data) {
					loadDataConfig('modelMeta');
					console.log(data)
				},
				error: function (errMsg) {
					console.log(errMsg);
				}
			});
			// } else {
			// 	alert('Pls choose file has extention ".xlsx"!');
			// }
		}
	});

	$('#btnSubmitUploadSI').on('click', function () {
		var fileData = $('#siLineUpload').prop('files')[0];
		var form = new FormData();
		form.append('file', fileData);
		form.append('factory', dataset.factory);
		$.ajax({
			type: "POST",
			url: "/api/smt/upload_si_meta",
			data: form,
			dataType: "json",
			processData: false,
			contentType: false,
			mimeType: "multipart/form-data",
			success: function (data) {
				console.log("dataFile: ", data);
			},
			error: function (errMsg) {
				console.log(errMsg);
				alert("Upload failed!");
			}
		})
		$('#siLineUpload').val('');
	});

</script>