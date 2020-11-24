<!-- Modal Confirm Solution -->
<div id="modal-confirm-solution" class="modal fade" role="dialog">
	<div class="modal-dialog" style="font-size: 13px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">CONFIRM</h6>
            </div>
        	<div class="modal-body">
                <div class="form-horizontal">
                    <div class="row">
                        <div class="form-group">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Employee ID</label>
                            <div class="col-xs-3">
                                <input type="text" name="employeeNo" class="form-control">
                            </div>
                            <label class="col-xs-4 control-label" name="search_emp" style="padding-top: 8px;"></label>
                            <button class="btn btn-xs" onclick="searchEmp(this)"> <i class="fa fa-search"></i></button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-sm" data-dismiss="modal"><i class="icon-cross"></i> Close</button>
                <button class="btn btn-sm" data-dismiss="modal" id="confirm_solution" name="btn-submit-solution" disabled="disabled" onclick="confirmSolution2()">Confirm <i class="icon-arrow-right14 position-right"></i> </button>
            </div>
        </div>
	</div>
</div>
<!-- /Modal Confirm Solution -->