<div id="modal_serial_tracking" class="modal fade" role="dialog">
	<div class="modal-dialog modal-xl">
	    <div class="modal-content">
	        <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">SERIAL HISTORY</h6>
            </div>
            <div class="modal-body">
                <div class="table-responsive pre-scrollable text-nowrap">
                    <table class="table table-xxs table-bordered" id="tbl-serial-tracking" style="font-size: 10px;">
                        <thead class="bg-primary-400">
                            <tr class="thead">
                                <th>No</th>
                                <th>Time</th>
                                <th>Serial</th>
                                <th>Group Name</th>
                                <th>Station Name</th>
                                <th>Tester</th>
                                <th>Chamber</th>
                                <th>Error</th>
                                <th>Description</th>
                                <th>LSL</th>
                                <th>USL</th>
                                <th>Value</th>
                                <th>Cycle</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
	</div>
</div>

<script>
    function loadSerialTracking(serial) {
        $.ajax({
            type: "GET",
            url: "/api/test/station/serialHistory",
            data: {
                serial: serial
            },
            contentType: "application/json; charset=utf-8",
            success: function(json){
                $('#tbl-serial-tracking>tbody>tr').remove();

                var body = $('#tbl-serial-tracking>tbody');
                if (!$.isEmptyObject(json)) {
                    for (i in json) {
                        body.append('<tr><td>'+ (1 + Number(i)) +'</td><td>'+ json[i].time +'</td><td>'+ json[i].serial +'</td><td>'+ json[i].groupName +'</td><td>'+ json[i].stationName +'</td><td>'+ json[i].tester +'</td><td>'+ json[i].chamber +'</td><td>'+ json[i].errorCode +'</td><td>'+ json[i].errorDescription +'</td><td>'+ json[i].lsl +'</td><td>'+ json[i].usl +'</td><td>'+ json[i].value +'</td><td>'+ json[i].cycle +'</td></tr>');
                    }
                } else {
                    body.append('<tr><td colspan="9" align="center">-- NO DATA HISTORY --</td></tr>');
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }
</script>