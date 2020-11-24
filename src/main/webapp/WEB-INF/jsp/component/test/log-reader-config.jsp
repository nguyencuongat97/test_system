<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<style>
    .loader{
        display: block;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) 
        url('/assets/images/loadingg.gif') 
        50% 50% 
        no-repeat;
    }
    .loader p {
        margin-top: 5%;
        color: #FFF;
        font-size: 20px;
        text-align: center;
    }
    .rate-danger {
        background-color: #FF040F;
        color: #FFF;
    }
    .rate-success {
        background-color: #00B15A;
        color: #FFF;
    }

    #tblReader tr td {
        cursor: pointer;
    }

    .border-blue {
        /* border: 1px solid #0e7bd2 !important; */
        border: 1px solid !important;
        border-radius: 3px;
    }
    .line-active {
        background-color: #2196F3;
        color: #FFF;
        font-weight: bold;
    }

    .btn-sm {
        padding: 3px 10px;
    }
</style>

<div class="loader hidden">
    <p>The Process Is Take A While </br> Please Waiting A Minute!</p>
</div>
<div class="row" style="padding: 10px; background-color: #fff;">
    <div class="col-sm-6">
        <div class="row mb-5">
            <div class="col-lg-6">
                <select class="form-control bootstrap-select" name="modelName" data-live-search="true" data-focus-off="true">
                </select>
            </div>
            <div class="col-lg-6">
                <select class="form-control bootstrap-select" name="groupName">
                </select>
            </div>
        </div>
        <div class="row no-margin no-padding mb-10">
            <div class="col-sm-12 border-blue">
                <input type="file" id="inputfile" name="file" class="form-control" data-show-preview="false" data-show-upload="true" data-browse-class="btn btn-xs" data-remove-class="btn btn-xs" >
            </div>
        </div>
        <div class="row no-margin no-padding">
            <div class="col-sm-12 table-responsive pre-scrollable no-margin no-padding border-blue" style="max-height: calc(100vh - 200px);">
                <table id="tblReader" class="table table-xxs">
                    <thead><tr><td><input id="txtFilterSetup" class="form-control" type="text" placeholder="Filter" style="height: 32px;" /></td></tr></thead>
                    <tbody></tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="col-sm-6">
        <div class="row no-margin no-padding">
            <label class="col-sm-12 control-label text-bold">File Name Pattern</label>
        </div>
        <div class="row no-margin p-5 border-blue mb-5">
            <label id="fileName" class="col-sm-12 control-label text-bold"></label>
        </div>
        <div class="row no-margin no-padding mb-10">
            <div class="col-sm-12 border-blue">
                <div class="form-group row no-margin">
                    <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Status: </label>
                    <div class="col-xs-9">
                        <input type="text" name="status" class="form-control">
                    </div>
                </div>
                <div class="form-group row no-margin">
                    <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">SN: </label>
                    <div class="col-xs-9">
                        <input type="text" name="sn" class="form-control">
                    </div>
                </div>
                <div class="form-group row no-margin">
                    <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Date time: </label>
                    <div class="col-xs-9">
                        <div class="input-group" style="margin-bottom: 5px;">
                            <span class="input-group-addon" style="padding: 0px 5px;"><i class="icon-calendar22"></i></span>
                            <input type="text" class="form-control daterange-single" side="right" name="timeSpan">
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row no-margin no-padding">
            <label class="col-sm-12 control-label text-bold">Value Pattern</label>
        </div>
        <div class="row no-margin no-padding">
            <div id="valuePattern" class="col-sm-12 no-margin no-padding table-responsive pre-scrollable" style="max-height: calc(100vh - 365px);">

                <!-- <div class="row no-margin p-5 border-blue mb-10">
                    <div class="col-sm-10"><label id="line-preview" class="control-label text-bold"></label></div>
                    <div class="col-sm-10"><label id="line-preview-id" class="hidden"></label></div>
                    <div class="com-sm-2" style="text-align: center;">
                        <botton class="btn btn-xs btn-primary"><i class="fa fa-plus"></i></botton>
                        <botton class="btn btn-xs btn-primary"><i class="fa fa-minus"></i></botton>
                    </div>
                </div>
                <div class="row no-margin no-padding mb-10">
                    <div class="col-sm-12 border-blue">
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Test item: </label>
                            <div class="col-xs-9">
                                <input type="text" name="testItem" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Value: </label>
                            <div class="col-xs-9">
                                <input type="text" name="value" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Spec: </label>
                            <div class="col-xs-9">
                                <input type="text" name="spec" class="form-control">
                            </div>
                        </div>
                    </div>
                </div> -->
            </div>
        </div>


        <div class="row mt-10">
            <div class="col-sm-12 text-right">
                <button class="btn btn-sm btn-success" onclick="submit()"><i class="fa fa-check"></i> Submit</button>
                <button class="btn btn-sm btn-danger"><i class="fa fa-times"></i> Cancel</button>
            </div>
        </div>
    </div>

</div>

<script>

    var dataset = {
        factory: '${factory}',
    };

    document.getElementById('inputfile').addEventListener('change', function() {
        var fr = new FileReader();
        fr.onload = function() {
            var data = fr.result.split('\n');
            var table = '';
            for(i in data) {
                table += '<tr><td id="line_' + i + '" class="line" data-id="' + i + '" onclick="viewLine(this)">' + data[i] + '</td></tr>';
            }
            $('#tblReader>tbody').html(table);
            $('#valuePattern').html('');
        }
        fr.readAsText(this.files[0]);
        $('#fileName').html(this.files[0].name);
    });

   function viewLine(context) {
        if(context.className.indexOf('line-active') == -1) {
            $('#'+context.id).addClass('line-active');
            var value = context.innerHTML;
            if(value.length > 80) {
                value = value.slice(0,80) + '...';
            }

            var html = '<div id="line-preview_' + context.dataset.id + '" class="row no-margin p-5 mb-5 border-blue line-preview">'
                    + '<div class="col-sm-11"><label class="control-label text-bold line-content" title="' + context.innerHTML + '">' + value + '</label></div>'
                    + '<div class="col-sm-11"><label class="hidden line-id">' + context.dataset.id + '</label></div>'
                    + '<div class="com-sm-1 text-right">'
                    + '<botton class="btn btn-xs btn-primary" onclick="removeValue(' + context.dataset.id + ')"><i class="fa fa-minus"></i></botton></div></div>'
                    + '<div id="line-preview-content_' + context.dataset.id + '" class="row no-margin no-padding mb-15">'
                    + '<div class="col-sm-12 border-blue">'
                    + '<div class="form-group row no-margin">'
                    + '<label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Test item: </label>'
                    + '<div class="col-xs-9"><input type="text" name="testItem" class="form-control"></div>'
                    + '</div>'
                    + '<div class="form-group row no-margin">'
                    + '<label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Value: </label>'
                    + '<div class="col-xs-9"><input type="text" name="value" class="form-control"></div>'
                    + '</div>'
                    + '<div class="form-group row no-margin">'
                    + '<label class="col-xs-3 control-label text-bold" style="padding-top: 8px;">Spec: </label>'
                    + '<div class="col-xs-9"><input type="text" name="spec" class="form-control"></div>'
                    + '</div>'
                    + '</div></div>';
            $('#valuePattern').append(html);
        }
    };

    function removeValue(id) {
        $('#line-preview_'+id).remove();
        $('#line-preview-content_'+id).remove();
        $('#line_'+id).removeClass('line-active');
    }

    function loadModels() {
        $.ajax({
            type: 'GET',
            url: '/api/test/model',
            data: {
                factory: dataset.factory,
                timeSpan: dataset.timeSpan
            },
            success: function(data){
                var selector = $("select[name='modelName']");
                selector.children('option').remove();

                var options = "";
                for(i in data){
                    options+='<option value=' + data[i].modelName + '>' + data[i].modelName + '</option>';
                }
                selector.append(options);

                if (data.length > 0) {
                    dataset.modelName = data[0].modelName;
                    selector.val(dataset.modelName);
                } else {
                    dataset.modelName = "";
                }

                selector.selectpicker('refresh');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function () {
                loadGroups();
            }
        });
    }
    function loadGroups() {
        $.ajax({
            type: 'GET',
            url: '/api/test/group',
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName,
                timeSpan: dataset.timeSpan
            },
            success: function(data){
                var selector = $("select[name='groupName']");
                selector.children('option').remove();

                var options = "";
                for(i in data){
                    options+='<option value=' + data[i].groupName + '>' + data[i].groupName + '</option>';
                }
                selector.append(options);

                if (data.length > 0) {
                    dataset.groupName = data[0].groupName;
                    selector.val(dataset.groupName);
                } else {
                    dataset.groupName = "";
                }

                selector.selectpicker('refresh');
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function submit() {
        var file = $('#inputfile').get(0);

        var item_value = [];
        var divLine = $('.line-preview');
        var listId = [];
        for(let i=0; i< divLine.length; i++) {
            listId.push(divLine[i].id);
        }
        for(i in listId) {
            var idContent = listId[i].replace('preview','preview-content');
            var value = {
                "line": $('#'+listId[i]+' .line-id').html(),
                "content": $('#fileName').html(),
                "status": $('input[name="status"]').val(),
                "sn": $('input[name="sn"]').val(),
                "dateTime": $('input[name="timeSpan"]').val(),
                "testItem": $('#'+idContent+' input[name="testItem"]').val(),
                "value": $('#'+idContent+' input[name="value"]').val(),
                "spec": $('#'+idContent+' input[name="spec"]').val(),
            };
            item_value.push(value);
        }
        // console.log(item_value);

        var expectValueJson = JSON.stringify(item_value);

        var form = new FormData();
        form.append('factory', dataset.factory);
        form.append('modelName', dataset.modelName);
        form.append('groupName', dataset.groupName);
        form.append('expectValueJson', expectValueJson);
        form.append('patternConverted', 'true');
        form.append('file', file.files[0]);

        // for(var pair of form.entries()){
        //     console.log(pair[0] + ' : ' + pair[1]);
        // }

        $.ajax({
            type: 'POST',
            url: '/api/test/station/read-log-config/request',
            data: form,
            processData: false,
            contentType: false,
            mimeType: "multipart/form-data",
            success: function(response) {
                var data = JSON.parse(response);
                alert(data.message);
                if(data.status == "OK") {
                    window.location.reload();
                }

            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    $("#txtFilterSetup").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#tblReader tbody tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });

    $(document).ready(function() {
        loadModels();
        $('select[name=modelName]').on('change', function() {
            dataset.modelName = this.value;
            loadGroups();
        });

        $('.daterange-single').daterangepicker({
            singleDatePicker: true,
            opens: "right",
            timePicker: true,
            timePicker24Hour: true,
            applyClass: 'bg-slate-600',
            cancelClass: 'btn-default',
            locale: {
                format: 'YYYY/MM/DD HH:mm:ss'
            }
        });
    });

</script>
