<!-- Modal task history -->
<div id="modal-task-history" class="modal fade">
	<div class="modal-dialog modal-lg" style="font-size: 13px;">
		<div class="modal-content">
			<div class="modal-title">
				<div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
				    <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
					<h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">HISTORY</h6>
				</div>
			</div>
			<div class="modal-body">
				<div class="form-horizontal">
					<div class="row task-history">
						<div class="form-group form-group-xs">
							<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;"></label>
							<label class="col-xs-8 control-label" style="padding-top: 8px;"></label>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
	    function loadTaskHistory(){
        	$.ajax({
                type: "GET",
                url: "/api/test/tracking/detail",
                data: {
                    trackingId: dataset.trackingId
                },
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    if (!$.isEmptyObject(data)) {
                        var html = '';
                        if (data.notifiedAt != null) {
                            html += '<div class="form-group form-group-xs">' +
                                        '<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Notified At</label>' +
                                        '<label class="col-xs-8 control-label" style="padding-top: 8px;">'+data.notifiedAt+'</label>' +
                                    '</div>';
                        }
                        if (data.notify != null && data.notify.detail != null) {
                            html += '<div class="form-group form-group-xs">' +
                                        '<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Detail</label>' +
                                        '<label class="col-xs-8 control-label" style="padding-top: 8px;">'+data.notify.detail+'</label>' +
                                    '</div>';
                        }
                        if (data.assignedAt != null) {
                            html += '<div class="form-group form-group-xs">' +
                                        '<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Assigned At</label>' +
                                        '<label class="col-xs-8 control-label" style="padding-top: 8px;">'+data.assignedAt+'</label>' +
                                    '</div>';
                        }
                        if (data.employee != null) {
                            html += '<div class="form-group form-group-xs">' +
                                        '<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Handler</label>' +
                                        '<label class="col-xs-8 control-label" style="padding-top: 8px;">'+data.employee+'</label>' +
                                    '</div>';
                        }
                        if (data.confirmedAt != null) {
                            html += '<div class="form-group form-group-xs">' +
                                        '<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Confirmed At</label>' +
                                        '<label class="col-xs-8 control-label" style="padding-top: 8px;">'+data.confirmedAt+'</label>' +
                                    '</div>';
                        }
                        if (data.why != null) {
                            html += '<div class="form-group form-group-xs">' +
                                        '<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Root cause</label>' +
                                        '<label class="col-xs-8 control-label" style="padding-top: 8px;">'+data.why+'</label>' +
                                    '</div>';
                        }
                        if (data.action != null) {
                            html += '<div class="form-group form-group-xs">' +
                                        '<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Action</label>' +
                                        '<label class="col-xs-8 control-label" style="padding-top: 8px;">'+data.action+'</label>' +
                                    '</div>';
                        }
                        if (data.closedAt != null) {
                            html += '<div class="form-group form-group-xs">' +
                                        '<label class="col-xs-4 control-label text-bold" style="padding-top: 8px;">Closed At</label>' +
                                        '<label class="col-xs-8 control-label" style="padding-top: 8px;">'+data.closedAt+'</label>' +
                                    '</div>';
                        }
                        $('.task-history').html(html);
                    }
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });
        }
	</script>
</div>
<!-- /Modal task history -->