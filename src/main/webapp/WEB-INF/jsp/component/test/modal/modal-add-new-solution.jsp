<!-- Modal Add New Solution -->
<div id="modal-add-new-solution" class="modal fade">
	<div class="modal-dialog modal-lg" style="font-size: 13px;">
		<div class="modal-content">
			<div class="modal-title">
				<div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
				    <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
					<h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">CONFIRM WITH NEW</h6>
				</div>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="row">
						<div class="form-group">
							<label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Error Code</label>
							<label class="col-xs-3 control-label" name="error-code" style="padding-top: 8px;"></label>
							<label class="col-xs-6 control-label" name="error-desc" style="padding-top: 8px;"></label>
						</div>
						<div class="form-group">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Employee ID</label>
                            <div class="col-xs-3">
                                <input type="text" name="employeeNo" class="form-control">
                            </div>
                            <label class="col-xs-4 control-label" name="search_emp" style="padding-top: 8px;"></label>
                            <button class="btn btn-xs" onclick="searchEmp(this)"> <i class="fa fa-search"></i></button>
                        </div>
						<div class="form-group">
							<label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Root cause</label>
							<div class="col-xs-9">
							    <input type="text" name="solutionName" class="form-control" placeholder="">
							</div>
						</div>
						<div class="form-group">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Action</label>
                            <div class="col-xs-9">
                                <!-- <input type="text" name="action" class="form-control" placeholder=""> -->
                                <textarea class="form-control" name="action" type="text" value=""></textarea>
                            </div>
                        </div>
					</div>

					<div class="row">
						<div class="table-responsive">
							<table id="table" class="table table-xxs">
								<thead>
								<tr style="background-color: #dddddd;">
									<th style="text-align: center;" width="10%">Step</th>
									<th style="text-align: center;" width="55%">Content</th>
									<th style="text-align: center;" width="35%">Image Attachment</th>
								</tr>
								</thead>
								<tbody id="tbody">
								<tr>
									<td class="newStep">1</td>
									<td class="st_content"><input type="text" class="form-control" placeholder=""></td>
									<td class="st_file"><input type="file" class="file-input" data-show-upload="false" data-show-preview="false" data-browse-class="btn btn-xs" data-remove-class="btn btn-xs" accept="image/*"></td>
								</tr>
								<tr>
									<td colspan="3" align="center">
										<button class="btn btn-xs btn-label" onclick="showAdd()"><i class="fa fa-plus"></i></button>
										<button class="btn btn-xs btn-label" onclick="delRow()"><i class="fa fa-minus"></i></button>
									</td>
								</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<div class="row">
					<button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross"></i> Close</button>
					<button class="btn btn-sm" data-dismiss="modal" id="add_solution" name="btn-submit-solution" disabled="disabled" onclick="addNew2()">Confirm <i class="icon-arrow-right14 position-right"></i> </button>
				</div>
			</div>
		</div>
	</div>
	<script>
	    initUploadBtn();
	    function initUploadBtn(){
            $('input.file-input').fileinput({
                previewFileType: 'image',
                browseLabel: '',
                browseIcon: '<i class="icon-image2 position-left"></i> ',
                removeLabel: '',
                removeIcon: '<i class="icon-cross3"></i>',
                layoutTemplates: {
                    icon: '<i class="icon-file-check"></i>'
                }
            });
        }
	</script>
</div>
<!-- /Modal Add New Solution -->