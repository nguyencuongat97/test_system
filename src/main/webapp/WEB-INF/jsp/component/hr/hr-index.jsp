<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!-- <link rel="stylesheet" href="/assets/css/hr/dataTables.min.css"> -->
<!-- <link rel="stylesheet" href="/assets/css/hr/style.css"> -->
<!-- <link rel="stylesheet" href="/assets/js/hr/bootstrap-select.min.css"> -->
<script type="text/javascript" src="/assets/js/hr/dataTables.min.js"></script>
<!--
<script type="text/javascript" src="/assets/js/hr/bootstrap.bundle.min.js"></script>
<script type="text/javascript" src="/assets/js/hr/bootstrap-select.min.js"></script>
<script type="text/javascript" src="/assets/js/hr/jquery-3.3.1.slim.min.js"></script> -->

<!-- <script type="text/javascript" src="/assets/js/hr/hr.js"></script> -->


<div class="loader"></div>
<div class="container-fluid" style="color: #333; background: #D3E2E6">
    <div class="row dateRow">
        <div class="col-sm-4">
            <div class=" panel panel-overview input-group" style="background-color: #6c757d; color: #fff; height: 26px; margin: 5px 0px;">
                <span class="input-group-addon" style="padding: 0px 5px; color: inherit;"><i
                        class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimehr" side="right" name="dateTimeSpan"
                    style="height: 38px; color: inherit;border-bottom: none;">
            </div>
        </div>
        <div class="col-sm-8 panel-overview chonCa">
            <!-- <select id="selectDayNight" onchange="changeDayNight(this.value)"> -->
            <select id="selectDayNight">
                <!-- <option value="" disabled="" selected="">Ca làm việc</option> -->
                <option value="0">Tổng 2 ca</option>
                <option value="1">Ca ngày</option>
                <option value="2">Ca đêm</option>
            </select>

            <select class="" id="selectFactory" onchange="changeFactory(this.value)">
                <option value="" disabled="" selected="">Chọn Xưởng</option>
            </select>

            <select id="selectDepart" class="selectpicker" multiple data-live-search="true" data-none-selected-text="Chọn tên bộ phận" data-width="auto", data-actions-box="true">
            </select>

            <button class="btn" onclick="filterData()" id="filter-button"><i class="fa fa-search"></i> Filter</button>


        </div>
    </div>
        
    <div class="row selectedBP" style="border-radius: 5px; border-color: #333; border-style: double; margin-left: 0px; margin-right: 0px; margin-bottom: 10px;">
        <span>Các bộ phận đã chọn</span>
        <div class="col-md-12" id="departSelectedList" style="width: 100%; height: auto; margin-bottom: 0px; max-height: 100px; overflow: auto;"></div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div id="chartWMA" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12" style="overflow-x: auto">
            <table class="table table-bordered" id="tblDayWorking" style="text-align: center; background: #71d2e0">
                <thead>
                    <tr id="item">

                    </tr>
                </thead>
                <tbody class="tBodyWMA">
                </tbody>
            </table>
        </div>

    </div>
    <div class="row">
        <div class="col-lg-4 col-md-4 workingDetail">
            <div class="row today-working">
            </div>
            <table id="dtBasicExample" class="table table-bordered table-sm" cellspacing="0" width="100%"
                style="background: #fff; color: #333;">
                <thead>
                    <tr>
                        <th class="th-sm tableName">Name
                        </th>
                        <th class="th-sm tableEmpNo">EmpNo
                        </th>
                        <!-- <th class="th-sm">CardNo
                        </th> -->
                        <th class="th-sm tableOfficeUnitName">OfficeUnit Name
                        </th>
                        <!-- <th class="th-sm">OfficeUnit Code
                        </th> -->
                    </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
        <div class="col-lg-4 col-md-4 workingDetail">
            <div class="row today-missing">
            </div>
            <table id="dtBasicExample1" class="table table-bordered table-sm" cellspacing="0" width="100%"
                style="background: #fff; color: #333;">
                <thead>
                    <tr>
                        <th class="th-sm tableName">Name
                        </th>
                        <th class="th-sm tableEmpNo">EmpNo
                        </th>
                        <!-- <th class="th-sm">CardNo
                        </th> -->
                        <th class="th-sm tableOfficeUnitName">OfficeUnit Name
                        </th>
                        <!-- <th class="th-sm">OfficeUnit Code
                        </th> -->
                    </tr>
                </thead>
                <tbody>

                </tbody>

            </table>
        </div>

        <div class="col-lg-4 col-md-4 workingDetail">
            <div class="row today-alert">
            </div>
            <table id="dtBasicExample2" class="table table-bordered table-sm" cellspacing="0" width="100%"
                style="background: #fff; color: #333;">
                <thead>
                    <tr>
                        <th class="th-sm tableName">Name
                        </th>
                        <th class="th-sm tableEmpNo">EmpNo
                        </th>
                        <!-- <th class="th-sm">CardNo
                        </th> -->
                        <th class="th-sm tableOfficeUnitName">OfficeUnit Name
                        </th>
                        <!-- <th class="th-sm">OfficeUnit Code
                        </th> -->
                    </tr>
                </thead>
                <tbody>

                </tbody>

            </table>
        </div>
    </div>
</div>
<div class="popupDetail">
    <div class="listBoPhan" index="0"
        style="background-color: #fff; border-radius: 4px; width: 90%; margin-top: 120px !important;margin: auto;">

    </div>
</div>
<div class="outer">
    <!-- <button type="button" class="btn btn-demo rotate" data-toggle="modal" data-target="#myModal2">
        card swipe history
    </button> -->
</div>

<div class="container demo">
    <!-- Modal -->
    <div class="modal right fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2">
        <div class="modal-dialog" role="document">
            <div class="modal-content">

                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel2"></h4>

                </div>

                <div class="modal-body">
                    <div class="row contentBody"></div>
                    <div style="overflow-x: auto">
                        <table class="table table-bordered table-striped table-hover" id="tblHistory" style="text-align: center">
                            <thead>
                                <tr id="item">
                                    <th class="customTbl" rowspan="" style="width: 25%">Date</th>
                                    <th class="customTbl" style="width: 20%">Time</th>
                                    <th class="customTbl">Location</th>
                                    <th class="customTbl">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>

            </div><!-- modal-content -->
        </div><!-- modal-dialog -->
    </div><!-- modal -->


</div><!-- container -->


<style>
    /*******************************
* Get free snippets
*******************************/
    html * {
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
    }

    .modal.left .modal-dialog,
    .modal.right .modal-dialog {
        position: fixed;
        margin: auto;
        width: 550px;
        height: 100%;
        -webkit-transform: translate3d(0%, 0, 0);
        -ms-transform: translate3d(0%, 0, 0);
        -o-transform: translate3d(0%, 0, 0);
        transform: translate3d(0%, 0, 0);
    }

    .modal.left .modal-content,
    .modal.right .modal-content {
        height: 100%;
        overflow-y: auto;
        max-height: 768px;
    }

    .modal.left .modal-body,
    .modal.right .modal-body {
        padding: 15px 15px 80px;
    }

    /*Right*/
    .modal.right.fade .modal-dialog {
        right: -320px;
        -webkit-transition: opacity 0.3s linear, right 0.3s ease-out;
        -moz-transition: opacity 0.3s linear, right 0.3s ease-out;
        -o-transition: opacity 0.3s linear, right 0.3s ease-out;
        transition: opacity 0.3s linear, right 0.3s ease-out;
    }

    .modal.right.fade.in .modal-dialog {
        right: 0;
    }

    /* ----- MODAL STYLE ----- */
    .modal-content {
        border-radius: 0;
        border: none;
    }

    .modal-header {
        border-bottom-color: #EEEEEE;
        background-color: #FAFAFA;
    }
    .modal-header .close {
        top: 30% !important;
    }

    /* ----- v CAN BE DELETED v ----- */
    body {
        background-color: #78909C;
    }

    .demo {
        padding-top: 60px;
        padding-bottom: 110px;
    }

    .outer {
        display: inline-block;
    }

    .btn-demo {
        position: absolute;
        padding: 6px 6px;
        border-radius: 5px;
        font-size: 16px;
        background-color: #1a98fb;
        top: 447px;
        right: 0px;
    }

    .rotate {
        -moz-transform: translateX(40%) translateY(0%) rotate(-90deg);
        -webkit-transform: translateX(40%) translateY(0%) rotate(-90deg);
        transform: translateX(40%) translateY(0%) rotate(-90deg);
    }

    .btn-demo:focus {
        outline: 0;
    }

    .demo-footer {
        position: fixed;
        bottom: 0;
        width: 100%;
        padding: 15px;
        background-color: #212121;
        text-align: center;
    }

    .demo-footer>a {
        text-decoration: none;
        font-weight: bold;
        font-size: 16px;
        color: #fff;
    }



    .activeFii {
        background: #4ebeff;
        color: #fff;
    }

    .showBp {
        margin-left: 20px;
        /* padding: 0px; */
        padding-left: 15px;
        padding-right: 15px;
        background: #6C757D;
        color: #fff;
        height: 26px;
        border-radius: 6px;
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
        line-height: 1.5;
        background: #6C757D;
        color: #fff;
        padding-left: 40px;
        padding-right: 40px;
    }

    .ssOK {
        float: right;
        margin: 0px 10px;
        background: #4ebeff;
    }

    .oke {
        height: 40px;
    }

    .colBP {
        padding: 5px 0px;
        border: 1px solid #444444;
        height: 50px;
    }

    .rowBP {
        margin: 0;
        padding: 0px 5px;
    }

    .popupDetail {
        position: absolute;
        height: 100%;
        width: 100%;
        top: 0px;
        left: 0px;
        background: #00030f7a;
        display: none;
    }

    input[type="search"] {
        background: #f6f7f7;
        margin-left: 8px;
        padding: 0px 5px;
        height: 28px;
        border-bottom: 2px solid #acb8c0;
        border-top: 1px solid #acb8c0;
        border-left: 1px solid #acb8c0;
        border-right: 1px solid #acb8c0;
        border-radius: 4px;
    }

    .dataTables_filter>label:after {
        right: 4px;
        top: 40%;
        font-size: 17px;
    }

    .bootstrap-select button span {
        color: #fff;
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
        line-height: 1.5;
    }
    .btn.legitRipple{
        margin-left: 10px;
        width: 155px;
    }

    #filter-button {
        height: 38px;
        border-radius: 3px;
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
        line-height: 1.5;
        background: #5d80ff82;
        color: #fff;
        text-transform: capitalize;
    }
    #filter-button:hover{
        margin-top: -2px;
        color: #fff;
    }
    .btn.dropdown-toggle{
        border-bottom: none;
    }

    #selectDayNight, #selectFactory, .bootstrap-select {
        margin-right: 10px;
        height: 38px;
        border-radius: 4px;
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
        line-height: 1.5;
        background: #6C757D;
        color: #fff;
        padding-left: 40px;
        padding-right: 40px;
    }
    /* #selectDayNight:hover{
        margin-top: -2px;
        color: #fff;
    } */

    .chonCa {
        margin: 5px 0px;
    }

    .paginate_button {
        color: #fff !important;
    }

    .dataTables_length {
        float: left;
        margin: 20px 0 0 0;
    }

    .dataTables_filter {
        float: right;
    }

    .dateRow {
        margin-bottom: 15px;
        margin-top: 5px;
    }

    select[name="dtBasicExample_length"] {
        width: 44px;
        /* background: #333; */
        border-radius: 5px;

    }

    select[name="dtBasicExample1_length"] {
        width: 44px;
        /* background: #333; */
        border-radius: 5px;

    }

    select[name="dtBasicExample2_length"] {
        width: 44px;
        /* background: #333; */
        border-radius: 5px;

    }

    .customTbl {
        padding: 5px 5px !important;
    }

    #item {
        background-color: #9abdc7;
    }

    #item th {
        border-color: #ece9e966;
        padding: 2px 3px;
        vertical-align: middle;
        text-align: center;
    }

    .table {
        border-collapse: separate;
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
        line-height: 1.5;
        margin-top: 10px;
        border: 2px solid #c6ababb0;
    }

    .tBodyWMA tr td {
        border: 1px solid #ddd
    }

    .a33 a {
        color: #202224;
    }

    .a33 a:hover {
        color: #2652cee6;
        text-decoration: none;
    }

    .today-working span,
    .today-missing span,
    .today-alert span {
        margin: unset;
        font-weight: bold;
        font-size: 14px;
    }

    .workingDetail {
        margin-top: 28px;
    }

    .paginate_button {
        color: #333 !important;
    }

    /* #dtBasicExample>tbody>tr>td,
    #dtBasicExample1>tbody>tr>td,
    #dtBasicExample2>tbody>tr>td { */
        /* padding: 10px 0px !important;
        text-align: center !important; */
    /* } */
    .tableOfficeUnitName{
        width: 250px !important;
    }
    /* .tableName{
        padding-left: 15px !important;
    } */

    .colBP {
        cursor: pointer;
    }
    .btn-group.bootstrap-select.show-tick{
        width: 210px !important;
    }
    .loader{
        display: none;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: url('/assets/images/loader.gif') 
        50% 50% 
        no-repeat;
    }
    body.loading .loader {
        overflow: hidden;   
    }
    body.loading .loader {
        display: block;
    }
    .selectedBP{
        display: none;
    }
    .bs-actionsbox .btn-group button{
        width: 100% !important;
        text-align: left ;
    }
</style>
<script>
$body = $("body");

$(document).on({
    ajaxStart: function() { $body.addClass("loading");    },
     ajaxStop: function() { $body.removeClass("loading"); }
});
    var gData = [];
    getDataWorking(0);
    function getDataWorking(checkDayNight) {

        // DATE TODAY
        var curday = function (sp) {
            today = new Date();
            var dd = today.getDate();
            var mm = today.getMonth() + 1; //As January is 0.
            var yyyy = today.getFullYear();

            if (dd < 10) dd = '0' + dd;
            if (mm < 10) mm = '0' + mm;
            return (yyyy + sp + mm + sp + dd);
        };
        var endDate = curday('/');
        // END DATE TODAY

        // 7 day ago
        var d = new Date();
        var pastDate = d.getDate() - 13;
        d.setDate(pastDate);
        var startDate = d.getFullYear().toString() + "/" + ((d.getMonth() + 1).toString().length == 2 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1).toString()) + "/" + (d.getDate().toString().length == 2 ? d.getDate().toString() : "0" + d.getDate().toString());
        // END 7 days ago
        getDataTotal(startDate, endDate, checkDayNight, null);
    }

    function filterData() {
        var times = $('input[name="dateTimeSpan"]').val();
        var date1 = times.split(" - ");
        var a = date1[0].split(" ");
        var b = date1[1].split(" ");
        let startDate = a[0];
        let endDate = b[0];
        var ouCodeList = '';

        if ($("#selectDayNight").val(), $('#selectDepart').val() != null) {
            ouCodeList = $('#selectDepart').val().join(';');
        }

        getDataTotal(startDate, endDate, $("#selectDayNight").val(), ouCodeList);
        detailTodayWorking(ouCodeList);
        detailTodayMissing(ouCodeList);
        detailTodayAlert(ouCodeList);
    }

    function changeDayNight(checkCa) {
       // console.log("checkCa:", checkCa)
        if (checkCa == 1) {
            getDataWorking(1);
        } else if (checkCa == 2) {
            getDataWorking(2);
        }
        else {
            getDataWorking(0);
        }
    }

    showFactory();
    function showFactory () {
        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/office/factory/list",
            contentType: "application/json; charset=utf-8",
            // data: { date: mDate },
            success: function (result) {
                var html = '';
                for (var i = 0; i < result.length; i++) {
                    html += '<option value="' + result[i] + '">' + result[i] + '</option>'
                }
                $('#selectFactory').append(html);
            },
            error: function () {
                alert("Fail!");
            }
        })
    }

    function changeFactory(element) {
        $('.selectedBP').css('display', 'none');
        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/office/unit/list",
            contentType: "application/json; charset=utf-8",
            data: { factory: element },
            success: function (result) {
                var html = '';
                for (var i = 0; i < result.length; i++) {
                    html += '<option value="' + result[i].ouCode + '">' + result[i].ouName + '</option>'
                }
                $('#selectDepart').html(html);
                $('#selectDepart').selectpicker('refresh');
            },
            error: function () {
                alert("Fail!");
            }
        })
    }

    $('#selectDepart').on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue){  
        var textList = $('#selectDepart option:selected');
        var html = '';
        for (var i = 0; i < textList.length; i++) {
            html += '<div type="button" class="btn" style="margin: 5px; background: #5d80ff82;" data-departCode =' + textList.get(i).value + '>' + textList.get(i).text + '<span class="badge removeDepart" style="margin-left: 5px; border-color: #F44336; background-color: #da9b96;">x</span></div>';
        }
        $('.selectedBP').css('display', 'block');
        $('#departSelectedList').html(html);
    });

    $(document).on('click', '#departSelectedList .removeDepart', function(){
        var departCode = this.parentNode.dataset.departcode;
        $('#selectDepart option[value=' + departCode + ']').prop('selected', false);
        if (isIE()) {
            var divParentNode = this.parentNode;
            divParentNode.removeChild(this);
            divParentNode.innerHTML = '';
            divParentNode.removeNode();
        } else {
            $(this).parent().remove();
        }       

        var textList = $('#selectDepart option:selected');
        var text ='';
        if (textList.length == 0) {
            text = 'Chọn tên bộ phận';
        } else {
            for(var i = 0; i < textList.length; i++) {
                if (i > 0) {
                    text += ', ';
                }
                text += textList.get(i).text;
            }
        }

        $("button[data-id='selectDepart']").prop('title', text);
        $("button[data-id='selectDepart'] .filter-option").html(text);
    });

    // date
    $(function () {

        // single date picker
        $('.daterange-single').daterangepicker({
            singleDatePicker: true,
            opens: "right",
            applyClass: 'bg-slate-600',
            cancelClass: 'btn-default',
            locale: {
                format: 'YYYY/MM/DD'
            }
        });

        // range date time 24 picker
        $('.datetimehr[side=right]').daterangepicker({
            maxSpan: {
                days: 30
            },
            timePicker: true,
            timePicker24Hour: true,
            opens: "right",
            applyClass: 'bg-slate-600',
            cancelClass: 'btn-default',
            timePickerIncrement: 30,
            locale: {
                format: 'YYYY/MM/DD HH:mm'
            }
        });

        $('.datetimehr[side=left]').daterangepicker({
            maxSpan: {
                days: 30
            },
            timePicker: true,
            timePicker24Hour: true,
            opens: "left",
            applyClass: 'bg-slate-600',
            cancelClass: 'btn-default',
            timePickerIncrement: 30,
            locale: {
                format: 'YYYY/MM/DD'
            }
        });

        $('.bootstrap-select').selectpicker();

        $(".file-input-overwrite").fileinput({
            previewFileType: 'image',
            browseLabel: '',
            browseIcon: '<i class="icon-image2 position-left"></i> ',
            removeLabel: '',
            removeIcon: '<i class="icon-cross3"></i>',
            layoutTemplates: {
                icon: '<i class="icon-file-check"></i>',
                main1: "{preview}\n" +
                    "<div class='input-group {class}'>\n" +
                    "   <div class='input-group-btn'>\n" +
                    "       {browse}\n" +
                    "   </div>\n" +
                    "</div>"
            },
            initialPreview: [
                "<img src='assets/images/placeholder.jpg' class='file-preview-image' alt=''>",
            ],
            overwriteInitial: true
        });

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

    });
    var date_start, date_end;

    $(document).ready(function () {
        // let d = moment()._d;
        // let dd = moment().add(1, 'days')._d;
        date_start = moment().subtract(
            13, 'd').format("YYYY-MM-DD") + " " + "07:30";
        date_end = moment().format("YYYY-MM-DD") + " " + "07:30";
        console.log(date_start + "---" + date_end)
        // const dateFormat = 'YYYY-MM-DD HH:mm';
        $('.datetimehr').data('daterangepicker').setStartDate(date_start);
        $('.datetimehr').data('daterangepicker').setEndDate(date_end);
        $(function () {
            $('.selectpicker').selectpicker();
        });
        var times = $('input[name="dateTimeSpan"]').val();
        /*
        $('input[name=dateTimeSpan]').on('change', function (event) {
            var imeSpan = event.target.value;
            var date1 = imeSpan.split(" - ");
            var a = date1[0].split(" ");
            var b = date1[1].split(" ");
            let startD = a[0];
            let endD = b[0];
            console.log(startD + "---" + endD)
            $('#item').html('');
            $('#tblDayWorking>tbody').html('');
            $('#chartWMA').html('');
            //  $('#item').append('')
            getDataTotal(startD, endD, 0, null)

        });
        */
        $('#selectMuti').on('change', function (event) {
            var muti = $('#selectMuti').val();
         //   console.log(muti)

        })

    });  // END DOcument.ready

/*
    function showBoPhan() {
        $('.popupDetail').css('display', 'block');
        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/office/unit/list",
            contentType: "application/json; charset=utf-8",
            // data: { date: mDate },
            success: function (result) {
                var html = '<tr>';
                for (let index = 0; index < result.length; index += 3) {
                    html += '<div class="row"><div class="col-md-2"></div></div>';
                }
                var ht = '';
                var i = 0, j = 0;
                while (i < result.length) {
                    if (j == 0) {
                        ht += '<div class="row rowBP">';
                    }
                    ht += '<div class="col-md-2 colBP loca-' + i + '" onclick="detailActive(this)" data-ae="0" data-oucode="' + result[i].ouCode + '">' + result[i].ouName + '</div>'
                    if (j == 5 || i == result.length - 1) {
                        ht += '</div>';
                        j = 0;
                    } else {
                        j += 1;
                    }

                    i += 1;
                }
                ht += '<div class="oke"><button class="btn ssOK" onclick="saveDataBP()">OK</button></div>';
                $('.listBoPhan').html(ht);
                //  html += '</tr>';
                //$('#tblListBoPhan>tbody').html(html);
            },
            error: function () {
                alert("Fail!");
            }
        })
    }
*/

    function detailActive(event) {
        var aq = $(event);
        // console.log(aq)
        //  console.log(event)
        var af = event.dataset.oucode;
        var aee = $(event).attr("data-ae")
        var oo = gData.indexOf(af);
        if (aee * 1 == 1) {
            $(event).removeClass('activeFii');
            $(event).attr("data-ae", "0");
            gData.splice(oo, 1);
        } else {
            $(event).addClass('activeFii');
            $(event).attr("data-ae", "1");
            gData.push(af);
        }
    }
    function saveDataBP() {
        var data = "";
        $('.popupDetail').css('display', 'none');
        if (gData.length > 0) {
            for (var index = 0; index < gData.length; index++) {
                data += gData[index] + ";"
            }
            // gData.forEach(e => {
            //     data += e + ";"
            // })
            var aw = data.slice(0, data.length - 1)
            // DATE TODAY
            var curday = function (sp) {
                today = new Date();
                var dd = today.getDate();
                var mm = today.getMonth() + 1; //As January is 0.
                var yyyy = today.getFullYear();
                if (dd < 10) dd = '0' + dd;
                if (mm < 10) mm = '0' + mm;
                return (yyyy + sp + mm + sp + dd);
            };
            var endDate = curday('/');
            // END DATE TODAY

            // 7 day ago
            var d = new Date();
            var pastDate = d.getDate() - 13;
            d.setDate(pastDate);
            var startDate = d.getFullYear().toString() + "/" + ((d.getMonth() + 1).toString().length == 2 ? (d.getMonth() + 1).toString() : "0" + (d.getMonth() + 1).toString()) + "/" + (d.getDate().toString().length == 2 ? d.getDate().toString() : "0" + d.getDate().toString());
            // END 7 days ago
            getDataTotal(startDate, endDate, null, aw);
            detailTodayWorking(aw);
            detailTodayMissing(aw);
            detailTodayAlert(aw);
        } else {
            return;
        }

    }

    function getDataHistoryByempNo(element) {
        var times = $('input[name="dateTimeSpan"]').val();
        var date1 = times.split(" - ");
        var a = date1[0].split(" ");
        var b = date1[1].split(" ");
        let startDate = a[0] + " 00:00:00";
        let endDate = b[0] + " 23:59:59";
        var r = element.dataset.date.replace(/\//g, '-');

        var empNo = $(element).data("empno");
        $('#myModalLabel2').html("");
        $('.contentBody').html("");
        $('#tblHistory>tbody').html("");
        
        var dt = {
            empNo: empNo,
            type: element.dataset.type,
            starDate: startDate,
            endDate: endDate
        }

        $.get('/api/hr/employee/history', dt, function (result) {
           console.log("result:", result)

            var html = '<h6>Mã thẻ:  <b> ' + empNo + '  </b>  /  Tên: <b>' + result.userName + '</b></h6>';
            var ml = 'Tra cứu lịch sử quẹt thẻ từ ngày ngày:</br> <i>' + result.startDate + '</i> đến <i>' + result.endDate + '</i>';
            var str='';
            var std='';
            var date = result.dataByDay;
            console.log("dateeeeee:",date)
            // if (result.date != 0) {
            for( var i in date){
                
                if(date[i].length > 0){
                    console.log("test data[i].length: ",date[i].length)
                    str = '<tr id="'+i+'"><td  rowspan="'+date[i].length+'">' + i + '</td></tr>';
                    $('#tblHistory>tbody').append(str);
                    for(var j in date[i]){
                        var action;
                        if (date[i][j].F_INOUT == null) {
                            action = "Không xác định";
                        } else if (date[i][j].F_INOUT == "出") {
                            action = "Quẹt Ra";
                        } else {
                            action = "Quẹt Vào";
                        }
                        
                        var subTime = (date[i][j].F_CARDTIME).substr(date[i][j].F_CARDTIME.indexOf("T")+1);
                        if(j==0){
                            std = '<td>' +subTime + '</td><td>' + date[i][j].F_BELLNO + '</td><td>' + action + '</td>';   
                            $("#"+i).append(std);               
                        } else{
                            std = '<tr><td>' + subTime + '</td><td>' + date[i][j].F_BELLNO + '</td><td>' + action + '</td></tr>';
                            $('#tblHistory>tbody').append(std);
                        }
                        
                    }


                } else{
                    $('#myModalLabel2').html(ml);
                    $('.contentBody').html(html);
                    str = '<tr id="'+i+'"><td  colspan="1">' + i + '</td><td colspan="3">No data</td></tr>';   
                    $('#tblHistory>tbody').append(str);               
                        
                }

            }
            $('#myModalLabel2').html(ml);
            $('.contentBody').html(html);

            $('#myModal2').modal('show').on('shown.bs.modal', function(){
                var ele =  document.getElementById(r);
                ele.scrollIntoView();
            });
        })
    }
    
    function detailWorkingMissingAlert(nUrl, mDate) {
        // $('#dtBasicExample').DataTable().clear().draw();
        var ouCodeList = '';

        if ($('#selectDepart').val() != null) {
            ouCodeList = $('#selectDepart').val().join(';');
        }

        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/" + nUrl + "/list/by/day",
            contentType: "application/json; charset=utf-8",
            data: { date: mDate, ouCodeList: ouCodeList},
            success: function (result) {
                console.log("result:", result)
                var html = '';
                // result.forEach(e => {
                for (var index = 0; index < result.length; index++) {
                    var e = result[index];
                    html += '  <tr><td>' + e.name + '</td >' +
                        '<td><a data-empno="' + e.empNo + '" data-type="' + nUrl + '" data-date="' + mDate + '" onclick="getDataHistoryByempNo(this)">' + e.empNo + '</a></td>' +
                        // '<td>' + e.cardNo + '</td>' +
                        // '<td>' + e.officeUnitName + '</td>' +
                        '<td>' + e.officeUnitName + '</td></tr> ';
                }
                // });
                if (nUrl == "working") {
                    $('.today-working').html("<span>Đi làm ngày:  " + mDate + "</span > ")
                    $('#dtBasicExample').DataTable().destroy();
                    $('#dtBasicExample>tbody').html(html);
                    $('#dtBasicExample').DataTable();

                }
                if (nUrl == "missing") {
                    $('.today-missing').html("<span>Không đi làm ngày: " + mDate + "</span>")
                    $('#dtBasicExample1').DataTable().destroy();
                    $('#dtBasicExample1>tbody').html(html);
                    $('#dtBasicExample1').DataTable();
                }
                if (nUrl == "alert") {
                    $('.today-alert').html("<span>Nghỉ quá 5 ngày: " + mDate + "</span>")
                    $('#dtBasicExample2').DataTable().destroy();
                    $('#dtBasicExample2>tbody').html(html);
                    $('#dtBasicExample2').DataTable();
                    //     $('.dataTables_length').addClass('bs-select');

                }
                $('.dataTables_length').addClass('bs-select');

            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    detailTodayWorking('');
    function detailTodayWorking(ouCodeList) {
        $('#dtBasicExample>tbody').html('');

        // DATE TODAY
        var endDate;
        var times = $('input[name="dateTimeSpan"]').val();
        if (times == "") {
            var curday = function (sp) {
                today = new Date();
                var dd = today.getDate();
                var mm = today.getMonth() + 1; //As January is 0.
                var yyyy = today.getFullYear();

                if (dd < 10) dd = '0' + dd;
                if (mm < 10) mm = '0' + mm;
                return (yyyy + sp + mm + sp + dd);
            };
            endDate = curday('/');
        } else {
            var date1 = times.split(" - ");
            var b = date1[1].split(" ");
            endDate = b[0];
        }
        // END DATE TODAY

        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/working/list/by/day",
            contentType: "application/json; charset=utf-8",
            data: { date: endDate, ouCodeList: ouCodeList },
            success: function (result) {
            //    console.log("detailTodayWorking:", result)
                var html = '';
                for (var index = 0; index < result.length; index++) {
                    var e = result[index];
                // result.forEach(e => {
                    html += '  <tr><td>' + e.name + '</td >' +
                        '<td><a data-empno="' + e.empNo + '" data-type="working" data-date="' + endDate + '" onclick="getDataHistoryByempNo(this)">' + e.empNo + '</a></td>' +
                        // '<td>' + e.cardNo + '</td>' +
                        //    '<td>' + e.officeUnitName + '</td>' +
                        '<td>' + e.officeUnitName + '</td></tr > ';
                // });
                }
                // $('.dataTables_length select[name="dtBasicExample_length"]').val("5");
                $('.today-working').html("<span>Đi làm hôm nay " + endDate + "</span>");
                $('#dtBasicExample').DataTable().destroy();
                $('#dtBasicExample>tbody').html(html);
                $('#dtBasicExample').DataTable();
                $('.dataTables_length').addClass('bs-select');
                // $('.dataTables_length select[name="dtBasicExample_length"]').prepend("<option value='5'>5</option>");
                // $('.dataTables_length select[name="dtBasicExample_length"]').val("5");
            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    detailTodayMissing('');
    function detailTodayMissing(ouCodeList) {
        $('#dtBasicExample1>tbody').html('');
        // DATE TODAY
        var endDate;
        var times = $('input[name="dateTimeSpan"]').val();
        if (times == "") {
            var curday = function (sp) {
                today = new Date();
                var dd = today.getDate();
                var mm = today.getMonth() + 1; //As January is 0.
                var yyyy = today.getFullYear();

                if (dd < 10) dd = '0' + dd;
                if (mm < 10) mm = '0' + mm;
                return (yyyy + sp + mm + sp + dd);
            };
            endDate = curday('/');
        } else {
            var date1 = times.split(" - ");
            var b = date1[1].split(" ");
            endDate = b[0];
        }
        // END DATE TODAY

        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/missing/list/by/day",
            contentType: "application/json; charset=utf-8",
            data: { date: endDate, ouCodeList: ouCodeList },
            success: function (result) {
             //   console.log("detailTodayMissing:", result)
                var html = '';
                for (var index = 0; index < result.length; index++) {
                    var e = result[index];
                // result.forEach(e => {
                    html += '  <tr><td>' + e.name + '</td >' +
                        '<td><a data-empno="' + e.empNo + '" data-type="missing" data-date="' + endDate + '" onclick="getDataHistoryByempNo(this)">' + e.empNo + '</a></td>' +
                        // '<td>' + e.cardNo + '</td>' +
                        //  '<td>' + e.officeUnitName + '</td>' +
                        '<td>' + e.officeUnitName + '</td></tr > ';
                // });
                }
                $('.today-missing').html("<span>Không đi làm hôm nay " + endDate + "</span>");
                $('#dtBasicExample1').DataTable().destroy();
                $('#dtBasicExample1>tbody').html(html);
                $('#dtBasicExample1').DataTable();
                $('.dataTables_length').addClass('bs-select');
            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    detailTodayAlert('');
    function detailTodayAlert(ouCodeList) {
        $('#dtBasicExample2>tbody').html('');
        // DATE TODAY
        var endDate;
        var times = $('input[name="dateTimeSpan"]').val();
        if (times == "") {
            var curday = function (sp) {
                today = new Date();
                var dd = today.getDate();
                var mm = today.getMonth() + 1; //As January is 0.
                var yyyy = today.getFullYear();

                if (dd < 10) dd = '0' + dd;
                if (mm < 10) mm = '0' + mm;
                return (yyyy + sp + mm + sp + dd);
            };
            endDate = curday('/');
        } else {
            var date1 = times.split(" - ");
            var b = date1[1].split(" ");
            endDate = b[0];
        }
        // END DATE TODAY

        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/alert/list/by/day",
            contentType: "application/json; charset=utf-8",
            data: { date: endDate, ouCodeList: ouCodeList },
            success: function (result) {
           //     console.log("detailTodayAlert:", result)
                var html = '';
                for (var index = 0; index < result.length; index++) {
                    var e = result[index];
                // result.forEach(e => {
                    html += '  <tr><td>' + e.name + '</td >' +
                        '<td><a data-empno="' + e.empNo + '" data-type="alert" data-date="' + endDate + '" onclick="getDataHistoryByempNo(this)">' + e.empNo + '</a></td>' +
                        // '<td>' + e.cardNo + '</td>' +
                        // '<td>' + e.officeUnitName + '</td>' +
                        '<td>' + e.officeUnitName + '</td></tr > ';
                // });
                }
                $('.today-alert').html("<span>Nghỉ quá 5 ngày hiện tại " + endDate + "</span>");
                $('#dtBasicExample2').DataTable().destroy();
                $('#dtBasicExample2>tbody').html(html);
                $('#dtBasicExample2').DataTable();
                $('.dataTables_length').addClass('bs-select');
            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    // FUNCTION
    function getDataTotal(startDate, endDate, checkDayNight, ouCodeList) {
     //   console.log(checkDayNight)
        $('#tblDayWorking>tbody').html("");
        $('#item').html('');
        var dataWork = {};
        var dataWorkDay = {};
        var dataWorkNight = {};
        var dataMissing = {};
        var dataAlert = {};
        var dataDate = [];
        var dataCheckDaynight;
        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/by/day",
            data: { startDate: startDate, endDate: endDate, ouCodeList: ouCodeList },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
           //     console.log("getData:", data)
                var html = '<th class="customTbl" style="width: 15%"><b>#</b></th>';
                var val = '<tr style="background:#bdced2"><td><b>Nghỉ quá 5 ngày</b></td>';
                var missingList = '<tr style="background:#dce9ec"><td><b>Không đi làm</b></td>';
                var workingList = '<tr style="background: #ffffff"><td><b>Đi làm</b></td>';
                var tmpDataWork = [], tmpDataMissing = [], tmpDataAlert = [], tmpDataWorkDay = [], tmpDataWorkNight = [];
                if (checkDayNight == 1) {
                    dataCheckDaynight = data.dayWorkingList;
                } else if (checkDayNight == 2) {
                    dataCheckDaynight = data.nightWorkingList;
                } else {
                    dataCheckDaynight = data.workingList;
                }
                for (var k in dataCheckDaynight) {
                    if (dataCheckDaynight.hasOwnProperty(k)) {
                        var element = dataCheckDaynight[k];
                        var ddmm = element.workDate.slice(5);
                        html += ' <th class="customTbl"><b>' + ddmm + '</b></th>'
                        workingList += ' <td class="a33"><a onclick=detailWorkingMissingAlert("working","' + element.workDate + '")>' + element.qty + '</a></td>'
                        dataDate.push(ddmm);
                        tmpDataWork.push(element.qty);
                    }
                }
                dataWork.name = "Đi làm";
                dataWork.data = tmpDataWork;
                workingList += '</tr>';
                $('#tblDayWorking>tbody').html(workingList);

                for (var i in data.missingList) {
                    if (data.missingList.hasOwnProperty(i)) {
                        var element = data.missingList[i];
                        missingList += ' <td class="a33"><a onclick=detailWorkingMissingAlert("missing","' + element.workDate + '")>' + element.qty + '</a></td>'
                        tmpDataMissing.push(element.qty);
                    }
                }
                missingList += '</tr>';
                $('#tblDayWorking>tbody').append(missingList);
                dataMissing.name = "Không đi làm";
                dataMissing.data = tmpDataMissing;

                for (var key in data.alertList) {
                    if (data.alertList.hasOwnProperty(key)) {
                        var element = data.alertList[key];
                        //   console.log(element.workDate)

                        val += ' <td class="a33"><a onclick=detailWorkingMissingAlert("alert","' + element.workDate + '")>' + element.qty + '</a></td>'
                        tmpDataAlert.push(element.qty);
                    }
                }
                val += '</tr>'
                $('#item').append(html);
                $('#tblDayWorking>tbody').append(val);
                dataAlert.name = "Nghỉ quá 5 ngày";
                dataAlert.data = tmpDataAlert;
                hightChartStaticNBB(dataDate, dataWork, dataMissing, dataAlert);
            },
            error: function () {
                alert("Fail!");
            }
        });
    }


    // highchart
    function hightChartStaticNBB(dataDate, dataWork, dataMissing, dataAlert) {
        Highcharts.chart('chartWMA', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'Thống kê số người đi làm, không đi làm và nghỉ quá 5 ngày'
            },
            xAxis: {
                categories: dataDate
            },
            yAxis: {
                min: 0,
                title: {
                    text: ''
                },
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: ( // theme
                            Highcharts.defaultOptions.title.style &&
                            Highcharts.defaultOptions.title.style.color
                        ) || 'gray'
                    }
                }
            },
            legend: {
                align: 'right',
                x: -30,
                verticalAlign: 'top',
                y: 0,
                floating: true,
                backgroundColor:
                    Highcharts.defaultOptions.legend.backgroundColor || 'white',
                borderColor: '#CCC',
                borderWidth: 1,
                shadow: false
            },
            tooltip: {
                headerFormat: '<b>{point.x}</b><br/>',
                pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true
                    }
                }
            },
            credits: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            series: [dataAlert, dataMissing, dataWork]
        });
    }

    function hightChartNBB(dataDate, dataWork, dataMissing, dataAlert, dataWorkDay, dataWorkNight) {
        Highcharts.chart('chartWMA', {

        chart: {
            type: 'column'
        },

        title: {
            text: 'Thống kê số người đi làm, không đi làm và nghỉ quá 5 ngày'
        },

        xAxis: {
            categories: dataDate
        },

        yAxis: {
            min: 0,
            title: {
                text: ''
            },
            stackLabels: {
                enabled: true,
                style: {
                    fontWeight: 'bold',
                    color: ( // theme
                        Highcharts.defaultOptions.title.style &&
                        Highcharts.defaultOptions.title.style.color
                    ) || 'gray'
                }
            }
        },

        tooltip: {
            headerFormat: '<b>{point.x}</b><br/>',
            pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
        },

        plotOptions: {
            column: {
                stacking: 'normal',
                dataLabels: {
                    enabled: true
                }
            }
        },

        credits: {
            enabled: false
        },

        exporting: {
            enabled: false
        },

        legend: {
            align: 'right',
            x: -30,
            verticalAlign: 'top',
            y: 0,
            floating: true,
            backgroundColor:
                Highcharts.defaultOptions.legend.backgroundColor || 'white',
            borderColor: '#CCC',
            borderWidth: 1,
            shadow: false
        },

        series: [
            {
                name: dataMissing.name,
                data: dataMissing.data,
                stack: 'total',
                color: '#434348'
            }, {
                name: dataWork.name,
                data: dataWork.data,
                stack: 'total',
                color: '#7cb5ec'
            }, {
                name: dataWorkDay.name,
                data: dataWorkDay.data,
                stack: 'shift',
                color: '#90ed7d'
            }, {
                name: dataWorkNight.name,
                data: dataWorkNight.data,
                stack: 'shift',
                color: '#997ded'
            }, {
                name: dataAlert.name,
                data: dataAlert.data,
                stack: 'alert',
                color: '#e22323'
            }]
        });
    }

    function isIE() {
        var ua = window.navigator.userAgent; //Check the userAgent property of the window.navigator object
        var msie = ua.indexOf('MSIE '); // IE 10 or older
        var trident = ua.indexOf('Trident/'); //IE 11

        return (msie > 0 || trident > 0);
    }

</script>