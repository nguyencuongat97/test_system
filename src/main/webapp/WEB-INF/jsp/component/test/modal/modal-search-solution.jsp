<!-- Modal Search Solution -->
<div id="modal-search-solution" class="modal fade" role="dialog">
	<div class="modal-dialog modal-xl" style="font-size: 10px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 5px 15px;color: #1E88E5;border-bottom-color: #ddd;">
                <button type="button" class="close" data-dismiss="modal" style="position: absolute;right: 10px;top: 10px;"><i class="icon-cross"></i></button>
                <h6 class="modal-title text-bold" style="margin-top: 5px; margin-bottom: 5px;">SEARCH SOLUTION</h6>
            </div>
            <div class="modal-body">
                <div class="row" style="position: relative;">
                    <div class="col-lg-3">
                        <select class="form-control bootstrap-select" name="searchModelName" >
                        </select>
                    </div>
                    <div class="col-lg-3">
                        <select class="form-control bootstrap-select" name="searchErrorCode" >
                        </select>
                    </div>
                </div>

                <div class="table-responsive pre-scrollable" style="margin: 10px 0px; max-height: 200px">
                    <table class="table table-xxs table-bordered text-nowrap" name="search-solution" style="font-size: 13px;">
                        <thead style="background-color: #6c757d; color: #fff">
                            <tr>
                                <th width="15%">Error Code</th>
                                <th>Root cause</th>
                                <th>Action</th>
                                <th>Date</th>
                                <th>Author</th>
                                <th>Result</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td colspan="6" align="center">-- NO DATA --</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
	</div>
	<script>

        loadSearchModels('${factory}');

	    $(document).ready(function() {
            $('select[name=searchModelName]').on('change', function() {
                loadSearchErrors(dataset.factory, this.value);
            });

            $('select[name=searchErrorCode]').on('change', function() {
                loadSearchSolution(dataset.factory, $('select[name=searchModelName]').val(), this.value);
            });
        });

	    function loadSearchModels(factory, modelName, errorCode) {
            $.ajax({
                type: 'GET',
                url: '/api/test/model',
                data: {
                    factory: factory
                },
                success: function(data){
                    var selector = $("select[name='searchModelName']");
                    selector.children('option').remove();

                    var options = "";
                    for(i in data){
                        options+='<option value=' + data[i].modelName + '>' + data[i].modelName + '</option>';
                    }
                    selector.append(options);
                    if (modelName != undefined) {
                        selector.val(modelName);
                    }
                    selector.selectpicker('refresh');

                    if (modelName != undefined && errorCode != undefined) {
                        loadSearchErrors(factory, modelName, errorCode);
                    } else if (data.length > 0) {
                        loadSearchErrors(factory, data[0].modelName);
                    }
                },
                failure: function(errMsg) {
                     console.log(errMsg);
                },
           });
        }

        function loadSearchErrors(factory, modelName, errorCode) {
            $.ajax({
                type: 'GET',
                url: '/api/test/errorCode',
                data: {
                    factory: factory,
                    modelName: modelName
                },
                success: function(data){
                    var selector = $("select[name='searchErrorCode']");
                    selector.children('option').remove();

                    var options = "";
                    for(i in data){
                        options+='<option value=' + data[i] + '>' + data[i] + '</option>';
                    }
                    selector.append(options);
                    if (errorCode != undefined) {
                        selector.val(errorCode);
                    }
                    selector.selectpicker('refresh');

                    if (errorCode != undefined) {
                        loadSearchSolution(factory, modelName, errorCode);
                    } else if (data.length > 0) {
                        loadSearchSolution(factory, modelName, data[0]);
                    } else {
                        var table = $("table[name='search-solution']>tbody");
                        table.children('tr').remove();
                        table.append('<tr><td colspan="6" align="center">-- NO DATA --</td></tr>');
                    }
                },
                failure: function(errMsg) {
                     console.log(errMsg);
                },
           });
        }

        function loadSearchSolution(factory, modelName, errorCode) {
            if (factory == undefined) {
                factory = dataset.factory;
                modelName = dataset.modelName;
                errorCode = dataset.errorCode;

                $('select[name=searchModelName]').val(dataset.modelName);
                $('select[name=searchModelName]').selectpicker('refresh');
                loadSearchErrors(factory, modelName, errorCode);

                return;
            }

            $.ajax({
                type: 'GET',
                url: '/api/test/solution',
                data: {
                    factory: factory,
                    modelName: modelName,
                    errorCode: errorCode
                },
                success: function(data){
                    var selector = $("table[name='search-solution']>tbody");
                    selector.children('tr').remove();

                    if (!$.isEmptyObject(data)) {
                        var rows = "";
                        for(i in data){
                            rows+='<tr><td>'+data[i].errorCode+'</td><td>'+data[i].solution+'</td><td>'+data[i].action+'</td><td>'+(data[i].createdAt == null ? "" : data[i].createdAt)+'</td><td>'+(data[i].author == null ? "" : data[i].author)+'</td><td>'+data[i].numberSuccess+'</td></tr>';
                        }
                        selector.append(rows);
                    } else {
                        selector.append('<tr><td colspan="6" align="center">-- NO DATA --</td></tr>');
                    }
                },
                failure: function(errMsg) {
                     console.log(errMsg);
                },
           });
        }
	</script>
</div>
<!-- /Modal Search Solution -->