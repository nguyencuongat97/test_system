<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>
<style>
    .btn-group button {
        margin-right: 10px;
        border-radius: 5px !important;
        border: 1px solid #ccc;
        color: #333;
        padding: 5px !important;}
   
    .table-sticky thead th {
        border-top: none !important;
        border-bottom: none !important;
        box-shadow: inset 0 1px 1px #fff, inset 0 -1px 0 #fff;}
   
    .loader {
        display: none;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) url('/assets/images/loadingg.gif') 50% 50% no-repeat;}
    .edit{
        width: 26px; height: 26px; padding: 3px; margin: 2px; background:#ffc107;
    }
    .delete{
        width: 26px; height: 26px; padding: 3px; margin: 2px;
    }
    #tblReportrtr td,#tblReportrtr th{
        padding: 0 0.6rem;
    }
    .modal-header {
        position: relative;
        padding: 0.5rem 1rem;
        background: #484848;
        border-radius: 3px;
    }
    .modal-header .close {
        position: unset;
        right: unset;
        top: unset;
        margin-top: unset;
    }
    .modal-body {
        padding-bottom: 0;
        padding-top: 0.5rem;
    }
    .modal-footer {
        padding: 1rem;
    }
    .submitEdit{
        width: 80px; border: 1px solid #4CAF50; color: #4CAF50;background: #4c4c4c;
    }
    .form-control {
        color: #cccccc;
    }
    .bootstrap-select.btn-group .btn .filter-option {
        color: #cccccc;
    }
</style>
<!-- <link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" /> -->
<link rel="stylesheet" href="/assets/css/custom/station-dashboard.css" />
<!-- STATION DETAIL -->
<div class="loader"></div>
<div class="row">
    <div class="row" style="margin-top: 0.5rem;">
        <div class="col-sm-11">
            <div class="panel panel-overview" style="margin:unset; background-color:#333333; padding:3px; color:#fff; text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
                <span style="font-size:14px;"><span class="text-uppercase" name="factory">${factory}</span> - REPORT TESTER RETEST RATE DETAIL</span>
            </div>
        </div>
        <div class="col-sm-1">
            <a id="exportExl" class="btn btn-xs" style="width: 100%;background:#ddd; color: #333;"><i class="fa fa-download"></i></a>
        </div>
    </div>
    <div class="col-xs-12 table-responsive" id="old-style" style="margin: 10px 0px; max-height: calc(100vh - 165px);">
        <table class="table table-xxs table-bordered" id="tblReportrtr" style="background-color: #fff; font-size: 16px; font-weight: 400;">
            <thead style="background-color: #545b62; color: #fff">
                <tr class="thead">
                    <th>No</th>
                    <th>Model Name</th>
                    <th>Group Name</th>
                    <th>Station Name</th>
                    <th>InPut</th>
                    <th>OutPut</th>
                    <th>F.Fail</th>
                    <th>Fail</th>
                    <th>F.P.R</th>
                    <th>Retest Rate</th>
                    <th>Yield Rate</th>
                    <th>Rootcause</th>
                    <th>Action</th>
                    <th>Due date</th>
                    <th>Owner</th>
                    <th>Status</th>
                    <th>Photo</th>
                    <th>Edit</th>
                </tr>
            </thead>
            <tbody class="thbody">
            </tbody>
        </table>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="modalEditReport" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content" style="background-color: #333; color: #ccc;">
            <div class="modal-header">
                <span style="font-weight:bold">REPORT TESTER RETEST RATE DETAIL</span>
                <button type="button" class="close" data-dismiss="modal"><i class="icon icon-cross" style="color: #ccc"></i></button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="panel" style="margin-bottom: 0; padding: 1rem; background: #3c3c3c;color: #ccccc4;">
                        <input type="hidden" name="id" value=""/>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Model Name</label>
                            <div class="col-xs-3">
                                <input type="text" id="modelName" disabled="disabled" class="form-control">
                            </div>
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Group Name</label>
                            <div class="col-xs-3">
                                <input type="text" id="groupName" disabled="disabled" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Station Name</label>
                            <div class="col-xs-9">
                                <input type="text" id="stationName" disabled="disabled" class="form-control">
                            </div>
                        </div>
                        
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Input</label>
                            <div class="col-xs-3">
                                <input type="text" id="input" disabled="disabled" class="form-control">
                            </div>
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Output</label>
                            <div class="col-xs-3">
                                <input type="text" id="output" disabled="disabled" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">First Fail</label>
                            <div class="col-xs-3">
                                <input type="text" id="fFail" disabled="disabled" class="form-control">
                            </div>
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Fail</label>
                            <div class="col-xs-3">
                                <input type="text" id="fail" disabled="disabled" class="form-control">
                            </div>
                        </div>
                        
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">First pass rate</label>
                            <div class="col-xs-3">
                                <input type="text" id="fpRate" disabled="disabled" class="form-control">
                            </div>
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Retest rate</label>
                            <div class="col-xs-3">
                                <input type="text" id="rtRate"disabled="disabled"  class="form-control">
                            </div>
                        </div>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Yield rate</label>
                            <div class="col-xs-9">
                                <input type="text" id="yRate" disabled="disabled" class="form-control">
                            </div>
                        </div>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Due date</label>
                            <div class="col-xs-9">
                                <input class="form-control daterange-single" id="dueDate" type="text" />
                            </div>
                        </div>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Root cause</label>
                            <div class="col-xs-9">
                                <textarea class="form-control" id="rootCause" type="text" value=""></textarea>
                            </div>
                        </div>
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Action</label>
                            <div class="col-xs-9">
                                <input type="text" id="action" class="form-control">
                            </div>
                        </div>
                        
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Owner</label>
                            <div class="col-xs-3">
                                <input type="text" id="owner" class="form-control">
                            </div>
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Status</label>
                            <div class="col-xs-3">
                                <select class="form-control bootstrap-select" id="status">
                                    <option value="ON_GOING">ON_GOING</option>
                                    <option value="DONE">DONE</option>
                                    <option value="PENDING">PENDING</option>
                                    <option value="CLOSE">CLOSE</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="form-group row no-margin">
                            <label class="col-xs-3 control-label text-bold" style="padding-top: 8px;padding-right: 0;">Photo</label>
                            <div class="col-xs-9">
                                <input type="file" class="form-control btn-border" id="uploadFile">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-xs submitEdit" data-dismiss="modal"><i class="icon-check position-left"></i>Submit</button>
            </div>
        </div>
    </div>
</div>
<script>
    var dataset = {
        factory: '${factory}',
        timeSpan: '${timeSpan}',
        modelName: '${modelName}',
        groupName: '${groupName}',
    };
    // var dataset = {
    //     factory: 'B04',
    //     timeSpan: '2020/07/16 07:30 - 2020/07/17 07:30',
    //     modelName: 'P000250860',
    //     groupName: 'FCT',
    // };
    var dataReport;
    loadData();
    function toFix(value) {
        return value == null ? value : value.toFixed(2);
    }
    function loadData(){
        $.ajax({
            type: 'GET',
            url: '/api/test/station/error/report',
            data: dataset,
            success: function(res){
                console.log(res);
                var data=res.data;
                dataReport=data;
                if(res){
                    $('#tblReportrtr tbody').html('');
                    var tbody='';
                    var dueDate;
                    for(i=0;i<data.length;i++){
                        dueDate=moment(dataReport[i].dueDate).format("YYYY/MM/DD");
                        tbody='<tr><td>'+(Number(i)+1)+'</td>'+
                        '<td>'+data[i].modelName+'</td>'+
                        '<td>'+data[i].groupName+'</td>'+
                        '<td>'+data[i].stationName+'</td>'+
                        '<td>'+toStringg(data[i].wip,'')+'</td>'+
                        '<td>'+toStringg(data[i].pass,'')+'</td>'+
                        '<td>'+toStringg(data[i].firstFail,'')+'</td>'+
                        '<td>'+toStringg(data[i].fail,'')+'</td>'+
                        '<td>'+toStringg(toFix(data[i].firstPassRate),'')+'</td>'+
                        '<td class="rtr'+i+'">'+toStringg(toFix(data[i].retestRate),'')+'</td>'+
                        '<td>'+toStringg(toFix(data[i].yieldRate),'')+'</td>'+
                        '<td>'+toStringg(data[i].rootCause,'')+'</td>'+
                        '<td>'+toStringg(data[i].action,'')+'</td>'+
                        '<td>'+toStringg(dueDate,'')+'</td>'+
                        '<td>'+toStringg(data[i].owner,'')+'</td>'+
                        '<td>'+toStringg(data[i].status,'')+'</td>'+
                        '<td>'+toStringg(data[i].uploadedFile)+'</td>'+
                        '<td style="text-align: center;">'+
                            '<button class="btn edit" onclick="loadModal('+data[i].id+')" href="#modalEditReport" data-toggle="modal""><i class="fa fa-edit"></i></button></td>'
                        '</tr>';
                        $('#tblReportrtr tbody').append(tbody);
                        if(data[i].retestRate>10){
                            $('.rtr'+i).css('background','#e6717c');
                        }
                        if(data[i].retestRate>3){
                            $('.rtr'+i).css('background','#ffda6a');
                        }
                    }
                   
                }
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }
    var id_report;
    function loadModal(idReport){
        id_report=idReport;
        for(i=0;i<dataReport.length;i++){
            if(idReport==dataReport[i].id){
                var dueDate=moment(dataReport[i].dueDate).format("YYYY/MM/DD");
                $('#modelName').val(dataset.modelName);
                $('#groupName').val(dataset.groupName);
                $('#stationName').val(dataReport[i].stationName);
                $('#input').val(dataReport[i].wip);
                $('#output').val(dataReport[i].pass);
                $('#fFail').val(dataReport[i].firstFail);
                $('#fail').val(dataReport[i].fail);
                $('#dueDate').val(dueDate);
                $('#fpRate').val(dataReport[i].firstPassRate);
                $('#rtRate').val(dataReport[i].retestRate);
                $('#yRate').val(dataReport[i].yieldRate);
                $('#rootCause').val(dataReport[i].rootCause);
                $('#action').val(dataReport[i].action);
                $('#owner').val(dataReport[i].owner);
                $('#uploadFile').val(dataReport[i].uploadedFile);
                
            }
        }
    }
    $('.submitEdit').click(function(){
        data = new FormData();
        var file = $("#uploadFile").prop('files')[0];
        if(file != undefined){
            data.append('uploadedFile', file);
        }
        data.append('id',id_report);
        data.append('factory', dataset.factory);
        data.append('modelName', dataset.modelName);
        data.append('groupName', dataset.groupName);
        data.append('stationName', $('#stationName').val());
        data.append('rootCause', $('#rootCause').val());
        data.append('action', $('#action').val());
        data.append('status', $('#status').val());
        data.append('owner', $('#owner').val());
        data.append('dueDate', $('#dueDate').val());
        data.append('timeSpan', dataset.timeSpan);
       
        $.ajax({
            type: 'POST',
            url: '/api/test/station/error/report',
            data: data,
            processData: false,
            contentType: false,
            mimeType: "multipart/form-data",
            success: function(response){
                loadData();
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    });



    var tableToExcel = (function() {
        var uri = 'data:application/vnd.ms-excel;base64,',
            template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>',
            base64 = function(s) {
                return window.btoa(unescape(encodeURIComponent(s)))
            },
            format = function(s, c) {
                return s.replace(/{(\w+)}/g, function(m, p) {
                    return c[p];
                })
            }
        return function(table, name) {
            if (!table.nodeType) table = document.getElementById(table)
            var ctx = {
                worksheet: name || 'Worksheet',
                table: table.innerHTML
            }
            var blob = new Blob([format(template, ctx)]);
            var blobURL = window.URL.createObjectURL(blob);
            return blobURL;
        }
    })();
    $("#exportExl").click(function() {
        var isIE = /*@cc_on!@*/ false || !!document.documentMode;
        if (isIE) {
            var tab_text = "<table border='2px'><tr bgcolor='#F79646'>";
            var textRange;
            var j = 0;
            tab = document.getElementById('tblReportrtr'); // id of table

            for (j = 0; j < tab.rows.length; j++) {
                tab_text = tab_text + tab.rows[j].innerHTML + "</tr>";
                //tab_text=tab_text+"</tr>";
            }

            tab_text = tab_text + "</table>";
            tab_text = tab_text.replace(/<A[^>]*>|<\/A>/g, ""); //remove if u want links in your table
            tab_text = tab_text.replace(/<img[^>]*>/gi, ""); // remove if u want images in your table
            tab_text = tab_text.replace(/<input[^>]*>|<\/input>/gi, ""); // reomves input params

            var ua = window.navigator.userAgent;
            var msie = ua.indexOf("MSIE ");

            if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) // If Internet Explorer
            {
                txtArea1.document.open("txt/html", "replace");
                txtArea1.document.write(tab_text);
                txtArea1.document.close();
                txtArea1.focus();
                sa = txtArea1.document.execCommand("SaveAs", true, "Report retest rate detail.xls");
            } else //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        } else {
            var blobURL = tableToExcel('tblReportrtr', 'uph_table');
            $(this).attr('download', 'Report retest rate detail (' + dataset.shiftType + ').xls')
            $(this).attr('href', blobURL);
        }
    });
</script>