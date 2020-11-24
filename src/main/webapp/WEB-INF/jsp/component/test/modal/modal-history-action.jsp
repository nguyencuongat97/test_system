<!-- Modal Confirm -->
<div id="modal-history-action" class="modal fade" role="dialog">
	<div class="modal-dialog modal-xl" style="font-size: 10px;">
		<div class="modal-content">
		    <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
		        <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">ACTION HISTORY</h6>
            </div>
        	<div class="modal-body">
        	    <div class="table-responsive pre-scrollable" style="margin: 10px 0px; max-height: 200px">
                    <table class="table table-xxs table-bordered text-nowrap" name="table-history">
                        <thead style="background-color: #6c757d; color: #fff">
                            <tr>
                                <th>No</th>
                                <th>Date Time</th>
                                <th>Model Name</th>
                                <th>Station Name</th>
                                <th>Tester Name</th>
                                <th>Type</th>
                                <th>Top 3 Error Code</th>
                                <th>Status</th>
                                <th>Engineer</th>
                                <th>Assigned Time</th>
                                <th>Error Code</th>
                                <th>Root Cause</th>
                                <th>Action</th>
                                <th>Confirmed Time</th>
                                <th>Downtime</th>
                                <th>Closed Time</th>
                                <th>Result</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                   </table>
                </div>
            </div>
        </div>
	</div>
	<script>
	    function loadActionHistories() {
            $.ajax({
                type: 'GET',
                url: '/api/test/tracking/history',
                data: {
                    factory: dataset.factory,
                    modelName: dataset.modelName,
                    groupName: dataset.groupName,
                    stationName: dataset.stationName,
                    timeSpan: dataset.timeSpan
                },
                success: function(data){
                    var dataChart = new Array(data.length);
                    var selector = $("table[name='table-history']>tbody");
                    selector.children('tr').remove();

                    if (!$.isEmptyObject(data)) {
                        var rows = "";
                        for(i in data){
                            rows+='<tr><td>'+(1 + Number(i))+'</td><td>'+data[i].createdAt+'</td><td>'+data[i].modelName+'</td><td>'+data[i].groupName+'</td><td>'+data[i].stationName+'</td><td>'+data[i].type+'</td><td>'+data[i].top3ErrorCode+'</td><td>'+data[i].status+'</td><td>'+toString(data[i].employee)+'</td><td>'+toString(data[i].assignedAt)+'</td><td>'+toString(data[i].errorCode)+'</td><td>'+toString(data[i].why)+'</td><td>'+toString(data[i].action)+'</td><td>'+toString(data[i].confirmedAt)+'</td><td>'+toString(data[i].downtime)+'</td><td>'+toString(data[i].closedAt)+'</td><td>'+toString(data[i].result)+'</td></tr>';
                        }
                        selector.append(rows);
                    } else {
                        selector.append('<tr><td colspan="17" align="center">-- NO DATA --</td></tr>');
                    }
                },
                failure: function(errMsg) {
                     console.log(errMsg);
                },
           });
        }
	</script>
</div>
<!-- /Modal Confirm -->