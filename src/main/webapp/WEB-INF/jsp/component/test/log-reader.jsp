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

    #tblReader tbody tr td,
    #tblInfo tbody tr td {
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
            <div class="col-lg-12">
                <select class="form-control bootstrap-select" name="modelName" data-live-search="true" data-focus-off="true">
                </select>
            </div>
            <div class="col-lg-6 hidden">
                <select class="form-control bootstrap-select" name="groupName">
                </select>
            </div>
        </div>
        <div class="row no-margin no-padding mb-10">
            <div class="col-sm-12 table-responsive pre-scrollable no-margin no-padding border-blue" style="max-height: calc(100vh - 200px);">
                <table id="tblInfo" class="table table-xxs">
                    <thead>
                        <tr>
                            <td>STT</td>
                            <td>sampleFile</td>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
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
                    <label class="col-xs-3 control-label text-bold pt-5">Status: </label>
                    <label id="lblStatus" class="col-xs-9 control-label pt-5"></label>
                    <!-- <div class="col-xs-9">
                        <input type="text" name="status" class="form-control">
                    </div> -->
                </div>
                <div class="form-group row no-margin">
                    <label class="col-xs-3 control-label text-bold pt-5">SN: </label>
                    <label id="lblSN" class="col-xs-9 control-label pt-5"></label>
                </div>
                <div class="form-group row no-margin">
                    <label class="col-xs-3 control-label text-bold pt-5">Date time: </label>
                    <label id="lblDateTime" class="col-xs-9 control-label pt-5"></label>
                </div>
            </div>
        </div>

        <div class="row no-margin no-padding">
            <label class="col-sm-12 control-label text-bold">Value Pattern</label>
        </div>
        <div class="row no-margin no-padding">
            <div id="valuePattern" class="col-sm-12 no-margin no-padding table-responsive pre-scrollable" style="max-height: calc(100vh - 305px);"></div>
        </div>

<!-- 
        <div class="row mt-10">
            <div class="col-sm-12 text-right">
                <button class="btn btn-sm btn-success" onclick="submit()"><i class="fa fa-check"></i> Submit</button>
                <button class="btn btn-sm btn-danger"><i class="fa fa-times"></i> Cancel</button>
            </div>
        </div> -->
    </div>

</div>

<script>

    var dataset = {
        factory: '${factory}',
    };

    loadData();
    function loadData() {
        $.ajax({
            type: "GET",
            url: "/api/test/station/read-log-config-list",
            data: {
                factory: dataset.factory,
                modelName: dataset.modelName
                // modelName: '40-1563-1001'
            },
            contentType: "application/json; charset=utf-8",
            success: function(response){
                $('#tblInfo>tbody').html('');
                if(response.status == 'OK'){
                    var data = response.data;
                    var html = '';
                    for(i in data) {
                        html += '<tr id="log-file_' + data[i].id + '" data-id="' + data[i].id + '" class="log-file" onclick="loadListLog(this)"><td>' + (Number(i) + 1) + '</td>'
                             +  '<td>' + data[i].sampleFile + '</td></tr>';
                    }
                    $('#tblInfo>tbody').html(html);
                } else {
                    alert(response.message);
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {

            }
        });
    }

    function loadListLog(context) {
        if(context.className.indexOf('line-active') == -1) {
            $('.log-file').removeClass('line-active');
            $('#'+context.id).addClass('line-active');
            $.ajax({
                type: "GET",
                url: "/api/test/station/read-log-config/"+context.dataset.id,
                contentType: "application/json; charset=utf-8",
                success: function(response){
                    $('#tblReader>tbody').html('');
                    if(response.status == 'OK'){
                        var data = response.result;
                        
                        var html = '';
                        var evj = JSON.parse(data.expectValueJson);
                        if(data.sampleFileLineList.length > 0) {
                            for(let i=0; i< data.sampleFileLineList.length; i++) {
                                html += '<tr><td id="line_' + i + '">' + data.sampleFileLineList[i] + '</td></tr>';
                            }
                        }
                        $('#tblReader>tbody').html(html);
                        $('#valuePattern').html('');

                        for(i in evj) {
                            $('#fileName').html(evj[i].content);
                            $('#lblStatus').html(evj[i].status);
                            $('#lblSN').html(evj[i].sn);
                            $('#lblDateTime').html(evj[i].dateTime);
                            
                            for(let j=0; j< data.sampleFileLineList.length; j++) {
                                if(j == Number(evj[i].line)) {
                                    $('#line_'+j).addClass('line-active');
                                    var value = data.sampleFileLineList[j];
                                    if(value.length > 80) {
                                        value = value.slice(0,80) + '...';
                                    }
                                    var partten = '<div id="line-preview_' + evj[i].line + '" class="row no-margin p-5 mb-5 border-blue line-preview">'
                                            + '<div class="col-sm-12"><label class="control-label text-bold line-content" title="' + data.sampleFileLineList[i] + '">' + value + '</label></div>'
                                            + '<div class="col-sm-12"><label class="hidden line-id">' + evj[i].line + '</label></div></div>'
                                            + '<div id="line-preview_' + evj[i].line + '" class="row no-margin p-5 mb-5 border-blue line-preview">'
                                            + '<div class="col-sm-12"><input type="text" name="testItem" class="form-control"></div></div>'

                                            + '<div id="line-preview-content_' + evj[i].line + '" class="row no-margin no-padding mb-15">'
                                            + '<div class="col-sm-12 border-blue">'
                                            + '<div class="form-group row no-margin">'
                                            + '<label class="col-xs-6 control-label text-bold pt-5">Expect </label>'
                                            + '<label class="col-xs-6 control-label text-bold pt-5">Valid </label>'
                                            + '</div>'
                                            + '<div class="form-group row no-margin">'
                                            + '<label class="col-xs-2 control-label pt-5">Value: </label>'
                                            + '<label class="col-xs-4 control-label pt-5">' + evj[i].value + '</label>'
                                            + '<label class="col-xs-2 control-label pt-5">Value: </label>'
                                            + '<div class="col-xs-4"><input type="text" name="value" class="form-control"></div>'
                                            + '</div>'
                                            + '<div class="form-group row no-margin">'
                                            + '<label class="col-xs-2 control-label pt-5">Spec: </label>'
                                            + '<label class="col-xs-4 control-label pt-5">' + evj[i].spec + '</label>'
                                            + '<label class="col-xs-2 control-label pt-5">Spec: </label>'
                                            + '<div class="col-xs-4"><input type="text" name="spec" class="form-control"></div>'
                                            + '</div>'
                                            + '</div></div>';
                                    $('#valuePattern').append(partten);
                                }
                            }
                        }

                    } else {
                        alert(response.message);
                    }
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                },
                complete: function() {

                }
            });
        }
    }

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
            loadData();
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
