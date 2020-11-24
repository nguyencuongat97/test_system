<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" />

<!-- <script type="text/javascript" src="/assets/js/hr/hr-workcount.js"></script> -->
<script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js"></script>
<script type="text/javascript" src="/assets/js/hr/dataTables.min.js"></script>
<!-- <script type="text/javascript" src="/assets/js/hr/select2.min.js"></script> -->

<!-- <link rel="stylesheet" href="/assets/custom/css/nbb/style.css" /> -->
<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span id="titlePage"></span><span> Bảng Chấm Công Tháng <span id="valueMonth"></span>.<span id="valueYear"></span></span><span id="txtDateTime"></span></b>
        </div>
        <div class="row divWrappertAll no-margin">
            <select class="form-control bootstrap-select chonThang" id="selectMonth">
                <!-- <option value="disabled selected">Chọn tháng</option> -->
                <option value="1"> Tháng 1</option>
                <option value="2"> Tháng 2</option>
                <option value="3"> Tháng 3</option>
                <option value="4"> Tháng 4</option>
                <option value="5"> Tháng 5</option>
                <option value="6"> Tháng 6</option>
                <option value="7"> Tháng 7</option>
                <option value="8"> Tháng 8</option>
                <option value="9"> Tháng 9</option>
                <option value="10"> Tháng 10</option>
                <option value="11"> Tháng 11</option>
                <option value="12"> Tháng 12</option>
            </select>

            <select class="form-control bootstrap-select chonThang" id="selectYear">
                <option value="2019"> 2019</option>
                <option value="2020"> 2020</option>
            </select>

            <div class="nhapMaThe">
                <input type="text" class="form-control bootstrap-search" id="idinputMT" placeholder="Nhập mã thẻ" />
            </div>
            <div class="startDate">
                <input type="text" class="form-control bootstrap-search" id="idinputSD" placeholder="Ngày bắt đầu" />
            </div>
            <div class="endDate">
                <input type="text" class="form-control bootstrap-search" id="idinputED" placeholder="Ngày kết thúc" />
            </div>

            <select class="form-control chonXuong" id="selectFactory" onchange="changeFactory(this.value)">
                <option value="" disabled="" selected="">Chọn Xưởng</option>
            </select>
            <div class="chonTenBP">
                <select id="selectDepart" class="selectpicker" multiple data-live-search="true" data-none-selected-text="Chọn tên bộ phận" data-width="auto" data-actions-box="true"></select>
            </div>
            <button class="btn filterWorkCount filter-option" id="idbtnWorkCount" onclick="showFilter()"><i class="fa fa-search"></i> Filter</button>
            <div class="export pull-right" style="margin: 5px 10px 5px 0px;">
                <a class="btn btn-lg" id="btnExport"
                    style="font-size: 13px; padding: 5px; border-radius: 10px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>
        <div class="row divWrappertDepartment no-margin">
            <div class="selectedBP col-md-12" style="border-radius: 5px; border-color: #ccc; border-style: double; margin: 10px 0 0 0;">
                <span style="float: left; color: #fff; font-weight: bold;">Các bộ phận đã chọn</span>
                <div id="departSelectedList" style="width: 100%; height: auto; margin-bottom: 0px; max-height: 100px; overflow: auto;"></div>
            </div>
        </div>
        <!-- max-height: calc(100vh - 165px); -->
        <div class="row tabClassTC" style="margin: 10px 0 0 0;">
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#wordcount">Chấm công</a></li>
                <li><a data-toggle="tab" href="#tangca">Tăng ca</a></li>
            </ul>
        </div>
        <div class="row tab-content no-margin">
            <div id="wordcount" class="row tab-pane fade in active no-margin">
                <div class="col-sm-12 table-responsive tableScroll no-padding" style="max-height: calc(100vh - 165px);color: #ccc;">
                    <table id="tblworkcount" class="table table-xxs table-bordered table-sticky pre-scrollable scroll-div2" style="text-align: center;">
                        <thead class="theadwCT">
                            <tr></tr>
                        </thead>
                        <tbody class="tbodywCT"></tbody>
                    </table>
                </div>
            </div>

            <div id="tangca" class="row tab-pane fade no-margin">
                <!-- <h3>doan nay ghi du lieu tang ca</h3> -->
                <div class="col-sm-12 table-responsive tableScroll no-padding" style="max-height: calc(100vh - 165px);color: #ccc;">
                    <table id="tbltangca" class="table table-xxs table-bordered table-sticky pre-scrollable scroll-div2" style="text-align: center;">
                        <thead class="theadTC">
                            <tr></tr>
                        </thead>
                        <tbody class="tbodyTC"></tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-sm-12 classNote" style="background-color: #333; margin: 10px 0 0 0;">
            <!-- <div class="classFooter" style="margin: unset; background-color: #272727;padding-top: 110px;"> -->
            <div class="col-sm-1 divCT">
                <p><br><br><b>Ghi chú</b><br>（备注）:</p>
            </div>
            <div class="col-sm-1 divTag">
                <p><strong class="strongTag">P</strong></p>
                <p><strong class="strongTag">1</strong></p>
                <p><strong class="strongTag" style="color: #ff0000;">F</strong></p>
                <p><strong class="strongTag">0</strong></p>
                <p><strong class="strongTag">TS</strong></p>
                <p><strong class="strongTag">LV</strong></p>
                <p><strong class="strongTag">N</strong></p>
                <p><strong class="strongTag">V</strong></p>
                <p><strong class="strongTag">B</strong></p>
                <p><strong class="strongTag">BL</strong></p>
                <p><strong class="strongTag">K</strong></p>
                <p><strong class="strongTag">H</strong></p>
                <p><strong class="strongTag">T</strong></p>
                <p><strong class="strongTag">A</strong></p>
                <p><strong class="strongTag">CT</strong></p>
                <p style="background:#999999; width: 27px; padding-left: 10px; height: 21px; color: #fff; margin-left: -10px;"><strong class="strongTag">L</strong></p>
                <p><strong class="strongTag">D</strong></p>
                <p><strong class="strongTag">BT</strong></p>
                <p><strong class="strongTag">KT</strong></p>
                <p><strong class="strongTag">Ca 3</strong></p>
                <p><strong class="strongTag">ND</strong></p>
            </div>
            <div class="col-sm-10 divDescrition">
                <p><span>Nghỉ việc hưởng 950000/ tháng</span></p>
                <p><span>Nghỉ việc riêng có xin phép</span></p>
                <p><span>Ngày nghỉ phép được hưởng lương (1 tháng = 1 ngày phép)</span></p>
                <p><span>Nghỉ ốm, thai sản hưởng BHXH</span></p>
                <p><span>Nghỉ thai sản</span></p>
                <p><span>Ngày làm việc đủ công</span></p>
                <p><span>Nghỉ việc riêng không xin phép (nghỉ vô kỷ luật)</span></p>
                <p><span>Ngày nghỉ được hưởng lương (theo quy định của PL Việt Nam)</span></p>
                <p><span>Ngày nghỉ bù được hưởng lương（Nghỉ bù ít nhất là 0.5 ngày)</span></p>
                <p><span>Ngày nghỉ do trùng với ngày lễ, tết hay trùng với ngày nghỉ luân phiên</span></p>
                <p><span>Ngày nghỉ được hưởng 70% lương (có thoả thuận giữa NLĐ và người sử dụng LĐ)</span></p>
                <p><span>Ngày nghỉ do công ty hết việc được hưởng lương</span></p>
                <p><span>Nghỉ thôi việc</span></p>
                <p><span>Nghỉ lưu chức ngừng lương</span></p>
                <p><span>Ngày đi học, công tác có hưởng lương</span></p>
                <p><span>Nghỉ luân phiên</span></p>
                <p><span>Ngày nghỉ chuyển ca (1 tháng chỉ được nghỉ 1 ngày)</span></p>
                <p><span>Nghỉ bù thai sản</span></p>
                <p><span>Khám thai</span></p>
                <p><span>Số giờ làm ca 3 trong ngày (quy định từ 22h00 đến 06h00 ngày hôm sau)</span></p>
                <p><span>Chưa có dữ liệu</span></p>
                <p class="pLast" style="margin-left:-217px;"><span class="strongTag" style="color:#ff0000;">Chú ý:
                    </span><span>trường hợp nghỉ việc riêng dưới 8 giờ có thể lấy số giờ nghỉ/ 8
                        (ví dụ: chị A nghỉ việc riêng có xin phép 2 giờ ngày 01 thì ô của ngày 01 được tính =2/8)</span>
                </p>
            </div>
            <button onclick="topFunction()" id="myBtn" title="Về đầu trang">Về đầu trang</button>
            <!-- </div> -->
        </div>
        <!-- </div> -->
    </div>
</div>
<style>
    html * {
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
    }

    #departSelectedList .btn {
        float: left !important;
    }

    .tabClassTC {
        display: none;
    }

    .nav-tabs li a {
        text-transform: capitalize;
        color: #fff;
        padding-left: 7px;
        padding-right: 9px;
    }

    .nav-tabs li {
        float: left;
        background: #444;
        border-radius: 9px 9px 0px 0px;
        margin-left: 5px;
    }

    .nav-tabs li.active a {
        text-transform: capitalize;
        color: #fff;
        background: #555;
        border-radius: 9px 9px 0px 0px;
    }

    .nav-tabs>li.active>a:focus {
        background-color: #555 !important;
        color: #fff;

    }

    .nav-tabs>li.active>a:hover {
        background-color: #555 !important;
        color: #ccc;
        border-radius: 9px 9px 0px 0px;

    }

    .nav-tabs>li>a:hover {
        background-color: #555 !important;
        color: #ccc;
        border-radius: 9px 9px 0px 0px;

    }
    #idinputSD {
        width: 120px;
        background: #333;
        border-radius: 4px;
        padding-left: 11px;
        padding-right: 8px;
        border-bottom: none;
        height: 39px;
        color: #fff;
    }

    /* .wmd-view-topscroll{
        overflow-x: scroll;
        overflow-y: hidden;
        width: 100%;
        border: none 0px RED;

    } .tableScroll {
        overflow-x: scroll;
        overflow-y: scroll;
        width: 100%;
        border: none 0px RED;
    } */

    /* .wmd-view-topscroll { height: 20px; } */
    /* .tableScroll { height: 200px; } */
    /* .scroll-div1 { 
        min-width: 2500px; 
        overflow-x: scroll;
        overflow-y: hidden;
        /* height:20px; */
    /* }
    .scroll-div2 { 
        width: 1700px; 
        overflow-x: hidden; */
    /* height:20px; */
    /* }
    .wmd-view-topscroll{
        display: none;
    } */


    #idinputED {
        width: 123px;
        background: #333;
        border-radius: 4px;
        padding-left: 11px;
        padding-right: 8px;
        border-bottom: none;
        height: 39px;
        color: #fff;
    }

    #idinputMT {
        width: 110px;
        background: #333;
        border-radius: 4px;
        padding-left: 11px;
        border-bottom: none;
        height: 39px;
    }

    .sticky-colum td:nth-of-type(1) {
        position: -webkit-sticky;
        position: sticky;
        left: -1px;
        background-color: #272727;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #fff,
                    inset 0px 0 0 #fff;
    }

    .sticky-colum td:nth-of-type(2) {
        position: -webkit-sticky;
        position: sticky;
        left: 33px;
        background-color: #272727;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #fff,
                    inset 0px 0 0 #fff;
    }

    .sticky-colum td:nth-of-type(3) {
        position: -webkit-sticky;
        position: sticky;
        left: 111px;
        background-color: #272727;
        border-left: none !important;
        border-right: none !important;
        box-shadow: inset 1px 0 0 #fff,
            inset -1px 0 0 #fff;
    }

    #tblworkcount tr th:last-of-type {
        left: 115px;
        z-index: 10;
        position: sticky;
        /* border: none !important; */
        box-shadow: inset 0px 1px 1px 1px #fff inset 0px 1px 1px 1px #fff;
    }

    #tbltangca tr th:last-of-type {
        left: 115px;
        z-index: 10;
        position: sticky;
        /* border: none !important; */
        box-shadow: inset 0px 1px 1px 1px #fff inset 0px 1px 1px 1px #fff;
    }

    .sticky-colum td:nth-of-type(4),
    #tblworkcount tr th:nth-of-type(4) {
        border-left: none !important;
    }

    #tbltangca tr th:nth-of-type(4) {
        border-left: none !important;
    }

    #tblworkcount tr th:nth-of-type(1),
    #tbltangca tr th:nth-of-type(1) {
        left: -1px;
        z-index: 10;
        position: sticky;
        border: none !important;
        box-shadow: inset 1px 1px 0 #fff,
            inset 0px -1px 0 #fff;
    }

    #tblworkcount tr th:nth-of-type(2),
    #tbltangca tr th:nth-of-type(2) {
        left: 33px;
        z-index: 10;
        position: sticky;
        border: none !important;
        box-shadow: inset 1px 1px 0 #fff,
            inset 0px -1px 0 #fff;
    }

    #tblworkcount tr th:nth-of-type(3),
    #tbltangca tr th:nth-of-type(3) {
        left: 111px;
        z-index: 10;
        position: sticky;
        border: none !important;
        box-shadow: inset 1px 1px 0 #fff,
            inset -1px -1px 0 #fff;
    }

    .loader {
        display: none;
        position: fixed;
        z-index: 1000;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        background: rgba(10, 10, 10, 0.8) url('/assets/images/loadingg.gif') 50% 50% no-repeat;
    }
    #header {
        font-size: 25px;
        text-align: center;
    }

    #tblworkcount {
        text-align: center;
        table-layout: fixed;
        max-width: 12000px;
        font-size: 14px;
    }

    #tbltangca {
        text-align: center;
        table-layout: fixed;
        max-width: 12000px;
        font-size: 14px;
    }

    .chonThang {
        width: auto !important;
        float: left !important;
        margin-right: 10px !important;
    }

    .bootstrap-select.btn-group .btn-default .caret {
        right: 5px !important;
    }

    #selectMonth {
        height: 26px;
        border-radius: 4px;
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
        line-height: 1.5;
        background: #333;
        color: #ffffff;
        padding-left: 40px;
        padding-right: 40px;
        border: none;
    }

    .btn.dropdown-toggle {
        /* width: 140px; */
        background: #333;
        color: #ffffff;
        font-weight: 600;
        padding: 9px;
        border-bottom: 0px !important;
        border-radius: 4px;
        min-width: 110px;
        color: #fff;
        height: 39px;
    }

    .dropdown-menu.inner {
        background: #444444 !important;
        color: #ccc;
        overflow: auto;
        width: 100px;
    }

    .dropdown-menu.inner li a {
        color: #ccc;
    }

    .dropdown-menu>.active>a {
        background-color: #696969;
    }

    .dropdown-menu.inner li a:hover {
        color: #ccc;
        background: #696969;
    }

    .filter-option {
        color: #fff;
    }

    .dropdown-menu {
        background: #444444 !important;
    }

    .chonThang .dropdown-menu.open {
        background: #444444;
        color: #fff;
        width: 100%;
    }

    .dropdown-menu.open {
        background: #444444;
        color: #fff;
        width: 300px;
        /* max-height: 300px !important;
        overflow: auto; */
    }

    .startDate,
    .endDate,
    .nhapMaThe,
    .chonXuong,
    .chonTenBP {
        width: auto !important;
        float: left !important;
        margin-left: 10px !important;
        margin-right: 10px !important;
        color: #fff !important;
        padding: 0px;
    }

    /* .chonXuong>.dropdown-menu{
        width: 141px !important;
    } */

    .chonTenBP .bootstrap-select .dropdown-menu.open .bs-actionsbox .btn-group .actions-btn {
        color: #fff !important;
    }

    .bs-actionsbox .btn-group button {
        width: 100% !important;
        text-align: left;
    }

    #selectFactory {
        height: 39px;
        border-radius: 4px;
        font-family: "Roboto", Helvetica Neue, Helvetica, Arial, sans-serif;
        line-height: 1.5;
        background: #333;
        color: #fff;
        border: none;
        padding-left: 10px;
    }

    #selectFactory:hover {
        color: #fff;
        margin-top: -2px;
    }

    /* #selectFactory option:before{
        padding: 20px;
        font-size: 14px;
    } */

    #myBtn {
        display: none;
        position: fixed;
        bottom: 50px;
        right: 40px;
        z-index: 99;
        /* font-size: 18px; */
        border: none;
        outline: none;
        background-color: #555;
        color: white;
        cursor: pointer;
        padding: 7px;
        border-radius: 3px;
        height: 37px;
        width: 100px;
    }

    #myBtn:hover {
        background-color: #444;
    }

    #idbtnWorkCount {
        font-size: 14px;
        width: 90px;
        float: left;
        margin-left: 10px;
        margin-right: 10px;
        background: #555;
        text-transform: capitalize;
        color: white;
        height: 39px;
        border-radius: 4px;
    }

    #idbtnWorkCount:hover {
        color: #fff;
        background-color: #444;
        /* text-decoration: none; */
    }

    input {
        border: 1px solid transparent;
        color: #ccc;
        font-size: 16px;
    }

    input[type=text] {
        color: #ccc;
        width: 100%;
        padding: 2px;
    }

    .divDescrition,
    .divTag {
        padding-top: 10px;
        text-align: left;
    }

    .divDescrition p strong {
        padding-right: 30px;
    }

    .popover-content {
        padding: 10px;
    }

    .theadwCT tr .nameLabellist {
        width: 38px;
        text-align: center;
        padding: 0px;
        background: #f9aa6a;
        color: #272727;
    }

    .valueTotalCount {
        padding: 0 !important;
    }

    .valueLV {
        padding: 0 !important;
        color: #fff !important;
    }

    .valueL {
        color: #fff !important;
    }

    .valueF {
        padding: 0 !important;
        color: #ff0000 !important;
    }

    .dayofmonth {
        width: 40px !important;
        text-align: center !important;
        padding: 0px !important;
        background: #f79646 !important;
        color: #272727 !important;
    }

    .ca3Time,
    .ca3OtTime {
        text-align: center !important;
        font-size: 13px;
        padding: 6px !important;
        background: #333 !important;
        /* color: #444; */
    }

    .ca3Time:first-of-type {
        border-left: none !important;
    }

    .ca3OtTime:first-of-type {
        border-left: none !important;
    }

    .classTT {
        text-align: center;
        width: 35px !important;
        padding: 0px 0px 0 0px !important;
        background: #f79646 !important;
        color: #272727 !important;
    }

    .classMNV {
        text-align: center;
        width: 78px !important;
        padding: 0 0 0 0px !important;
        background: #f79646 !important;
        color: #272727 !important;
    }

    .classHVT {
        text-align: center;
        width: 110px !important;
        padding: 6px !important;
        background: #f79646 !important;
        color: #272727 !important;
    }
    .classTotal {
        text-align: center;
        width: 47px !important;
        padding: 6px !important;
        background: #F79646 !important;
        color: #272727 !important;
    }

    .selectedBP {
        display: none;
    }

    /* .classFactory{
        margin: 20px !important;
        height: 50px !important;
    } */
    .closeX {
        margin-left: 5px;
        border-color: #fdf3f2;
        background-color: #838b9a;
        color: #fff;
        font-family: cursive;
    }

    .btn-text-select {
        user-select: text;
        -webkit-user-select: text;
    }
    .valueTTM{
        text-align: center;
        width: 80px !important;
        padding: 6px !important;
        background: #F79646 !important;
        color: #272727 !important;
    }
    #tblworkcount th:nth-child(53){
        width: 55px !important;
        background: #ff7300;
        box-shadow: inset 0px 1px 1px 1px #fff inset 0px 1px 1px 1px #fff;
    }
    .tbodywCT tr{
        border-color: #fdf3f2;
    }
</style>
<script>
    // $('.chonBoPhan').selectpicker({
    //     noneSelectedText: "Chọn bộ phận"
    // });
    getMonth();
    function getMonth() {
        var d = new Date();
        var m = Number(d.getMonth()) + 1;
        var y = (d.getFullYear());
        $('#selectMonth option[value="' + m + '"]').attr('selected', 'selected');
        $('#selectYear option[value="' + y + '"]').attr('selected', 'selected');
    }

    var month = '', departName = '', cardId = '', department = '', startDay = '', endDay = '';

    function loadWorkCount(month, cardId, nameCode, startDay, endDay, year) {
        $('.loader').css('display', 'block');
        $.ajax({
            type: 'GET',
            url: '/api/hr/monthly/workcount',
            data: {
                month: month,
                departName: nameCode,
                cardId: cardId,
                startDay: startDay,
                endDay: endDay,
                year: year
            },
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                if (jQuery.isEmptyObject(result)) {
                    $(".theadwCT>tr").html('');
                    $(".theadTC>tr").html('');
                    $("#tbltangca>tbody").html('');
                    $("#tblworkcount>tbody").html('');
                    $('.tabClassTC').css('display', 'none');
                    return;
                }
                var thead = '<th class="classTT"><b>TT</b><br/></th><th class="classMNV"><b>MNV<br></b>(工號)</th><th class="classHVT"><b>Họ và tên</b><br/>(姓名)</th>';
                var theadTC = '<th class="classTT"><b>TT</b></th><th class="classMNV"><b>MNV</b><br/>(工號)</th><th class="classHVT"><b>Họ và tên</b><br/>( 姓名)</th><th class="classTotal"><b>Tổng</b></th>';

                var tbodyMNV = '';
                var tbodyTC = '';
                var stt=0
                // var ArrStr = result.dayOfMonth + result.dayOfWeek;
                for (i in result.dayOfMonth) {
                    stt++;
                    thead += '<th class="dayofmonth"><b id="idDay'+stt+'">'+result.dayOfMonth[i]+'</b><br/> ('+result.dayOfWeek[i]+')</th>';
                    theadTC += '<th class="dayofmonth"><b>' + result.dayOfMonth[i] + '</b><br/> ('+result.dayOfWeek[i]+')</th>';
                }
                var arrTT = result.labelList;
                var insertTT = ['T.Meal'];
                arrTT.splice(19, 0, ...insertTT);
                for (i in arrTT) {
                    thead += '<th class="nameLabellist"><b>' + arrTT[i] + '</b></th>';
                }
                $(".theadwCT>tr").html(thead);
                $(".theadTC>tr").html(theadTC);
                var obj = result.data;
                var i = 1;
                var department = '';
                var factory = '';
                for (var key in obj) {
                    if (obj.hasOwnProperty(key)) {
                        var element = obj[key];
                        var workResult = element.workResultList;
                        var valueTotalCount = element.totalCountResult;
                        var valueTotalMeal = element.totalMeal;
                        var valueTotalFreeDuty =element.totalFreeDuty;
                        var valueFreeDuty = element.usedFreeDuty;
                        // valueTotalCount.splice(4, 0, ...valueTotalMeal);
                        department = element.department;
                        factory = element.factory;

                        tbodyMNV += '<tr class="sticky-colum"><td style="padding: 0px;" rowspan="2">' + i + '</td><td style="padding:0px;" rowspan="2">' + element.userId + '</td><td rowspan="2">' + element.userName + '</td>';
                        tbodyTC += '<tr class="sticky-colum classColumnTC"><td style="padding: 0px;" rowspan="2" >' + i + '</td><td style="padding:0px;" rowspan="2">' + element.userId + '</td><td rowspan="2">' + element.userName + '</td>';
                        var subTC = '';
                        var totalTC = element.normalOtTotal;
                        // tbodyTC += '<td style="background:#272727;padding: 6px 0px;"><div style="color: #ccc;text-align:center;"></div></td>';
                        for (var index = 0; index < workResult.length; index++) {
                            var e = workResult[index];
                            var resource = '<div style=\'margin-bottom: 5px; text-align: center;\'><b>' + e.workDate + '</b></div>' +
                                '<div style=\'margin-bottom: 5px; text-align: center;\'><b>' + e.workShiftCode + ' (' + e.workShiftType + ')</b></div>' +

                                '<div style=\'margin-top: 5px;\'><b>Begin Work: </b><span>' + e.beginWork + '</span></div>' +
                                '<div style=\'margin-bottom: 5px;\'><b>Begin Rest: </b><span>' + e.beginRest + '</span></div>' +

                                '<div style=\'margin-top: 5px;\'><b>End Rest: </b><span>' + e.endRest + '</span></div>' +
                                '<div style=\'margin-bottom: 5px;\'><b>End Work: </b><span>' + e.endWork + '</span></div>' +

                                '<div style=\'margin-top: 5px;\'><b>Overtime From: </b><span>' + e.otFrom + '</span></div>' +
                                '<div style=\'margin-bottom: 5px;\'><b>Overtime To: </b><span>' + e.otTo + '</span></div>' +
                                '<div style=\'margin-top: 5px; text-align: center\'><span>' + e.workResultCn + ' (Edited: ' + e.resultModified + ')</span></div>';
                            // tbodyMNV += '<td><div data-popup="popover" data-trigger="hover" data-placement="right" data-html="true" data-content="'+resource+'"><a class="hoverdetail" style="color:#ccc;">'+e.workResult+'</a></div></td>';

                            if ((e.workResult) == 'L'||(e.workResult) == 'LV') {
                                tbodyMNV += '<td style="background: #999999; color: #fff; padding: 0px; cursor: pointer;" data-popup="popover" data-trigger="hover click" data-placement="left" data-html="true" data-content="' + resource + '"><div style="color:#fff;">' + e.workResult + '</div></td>';

                            }
                            else if ((e.workResult) == 'LV/F' || (e.workResult) == 'F/LV' || (e.workResult) == 'F') {
                                tbodyMNV += '<td style="background:#272727; padding: 0; cursor: pointer;" data-popup="popover" data-trigger="hover click" data-placement="left" data-html="true" data-content="' + resource + '"><div style="color:#ff0000;">' + e.workResult + '</div></td>';

                            }
                            else if ((e.workResult) == 'NULL') {
                                var nd = 'ND';
                                tbodyMNV += '<td style="background:#272727; padding: 0; cursor: pointer;" data-popup="popover" data-trigger="hover click" data-placement="left" data-html="true" data-content="' + resource + '"><div style="color: #ccc;">' + nd + '</div></td>';
                            }
                            else {
                                tbodyMNV += '<td style="background:#272727; padding: 0; cursor: pointer;" data-popup="popover" data-trigger="hover click" data-placement="left" data-html="true" data-content="' + resource + '"><div style="color: #ffe10a">' + e.workResult + '</div></td>';
                            }

                            if(e.normalOtTime != 0 && e.normalOtTime != null){
                                subTC += '<td style="background:#272727; padding: 6px 0px; cursor: pointer;" data-popup="popover" data-trigger="hover click" data-placement="left" data-html="true" data-content="' + resource + '"><div style="color: #ccc;text-align:center;"><b>' + e.normalOtTime + '</b></div></td>';
                            } else subTC += '<td style="background:#272727; padding: 6px 0px; cursor: pointer;" data-popup="popover" data-trigger="hover click" data-placement="left" data-html="true" data-content="' + resource + '"><div style="color: #ccc;text-align:center;"></div></td>';
                            // totalTC += e.normalOtTime;
                        }
                        tbodyTC += '<td style="background:#92D050; padding: 6px 0px;"><div style="color: #272727;text-align:center;"><b>'+totalTC+'</b></div></td>';
                        tbodyTC += subTC;
                        // for (var value in valueTotalCount) {
                            // if (valueTotalCount.hasOwnProperty(value)) {
                                var value1= Object.values(valueTotalCount).map(Number)[0];

                                //value///total//1//F/LV/
                                tbodyMNV += '<td class="'+element.userId+'_1">'+value1+'</td>'
                                            + '<td class="valueF">' +valueTotalCount.F+ '</td>'
                                            + '<td class="valueO">' +valueTotalCount.O+ '</td>'
                                            + '<td class="valueLV">' +valueTotalCount.LV+ '</td>'
                                            + '<td class="valueN">' +valueTotalCount.N+ '</td>'
                                            + '<td class="valueV">' +valueTotalCount.V+ '</td>'
                                            + '<td class="valueB">' +valueTotalCount.B+ '</td>'
                                            + '<td class="valueBL">' +valueTotalCount.BL+ '</td>'
                                            + '<td class="valueBT">' +valueTotalCount.BT+ '</td>'
                                            + '<td class="valueKT">' +valueTotalCount.KT+ '</td>'
                                            + '<td class="valueL">' +valueTotalCount.L+ '</td>'
                                            + '<td class="valueD">' +valueTotalCount.D+ '</td>'
                                            + '<td class="valueK">' +valueTotalCount.K+ '</td>'
                                            + '<td class="valueH">' +valueTotalCount.H+ '</td>'
                                            + '<td class="valueT">' +valueTotalCount.T+ '</td>'
                                            + '<td class="valueCT">' +valueTotalCount.CT+ '</td>'
                                            + '<td class="valueA">' +valueTotalCount.A+ '</td>'
                                            + '<td class="valueTS">' +valueTotalCount.TS+ '</td>'
                                            + '<td class="valueP">' +valueTotalCount.F+ '</td>'
                                            + '<td class="value_TTM"><b>' +valueTotalMeal+ '</b></td>';
                                // if (value == 'LV') {
                                //     tbodyMNV += '<td class="valueLV">' + valueTotalCount[value] + '</td>';
                                // }
                                // else if (value == 'F') {
                                //     tbodyMNV += '<td class="valueF">' + valueTotalCount[value] + '</td>';
                                // }
                                // else if (value == 'L') {
                                //     tbodyMNV += '<td class="valueL">' + valueTotalCount[value] + '</td>';
                                // }
                                // else {
                                //     tbodyMNV += '<td class="valueTotalCount">' + valueTotalCount[value] + '</td>';
                                // }
                            // }
                            // console.log('pls::',valueTotalCount.F);
                            // tbodyMNV+='<td class="valueTotalMeal">'+valueTotalMeal+'</td>';
                        // }
                        tbodyMNV += '</tr>';
                        tbodyTC += '</tr>';
                        //append rowspan ca3time
                        tbodyMNV += '<tr>';
                        tbodyTC += '<tr>';
                        var subCa3 = '';
                        var totalCa3 = element.ca3OtTotal;
                        for (var index = 0; index < workResult.length; index++) {
                            var e = workResult[index];

                            tbodyMNV += '<td class="ca3Time">' + e.ca3Time + '</td>';
                            if(e.ca3OtTime != 0 && e.ca3OtTime != null){
                                subCa3 += '<td class="ca3OtTime"><b>' + e.ca3OtTime + '</b></td>';
                            } else subCa3 += '<td class="ca3OtTime"></td>';
                            // totalCa3 += e.ca3OtTime;

                            // tbodyTC+='<td class="ca3Time"></td>';
                        }
                        tbodyTC += '<td style="background:#92D050; padding: 6px 0px;"><div style="color: #272727;text-align:center;"><b>'+totalCa3+'</b></div></td>';
                        tbodyTC += subCa3;
                        var titleMeal = '<div><b>Used free duty: </b><span>' + valueFreeDuty + '</span></div>'
                                        +'<div><b>Total free duty: </b><span>' + valueTotalFreeDuty + '</span></div>';
                        for (var value in valueTotalCount) {
                            if (valueTotalCount.hasOwnProperty(value)) {
                                if (value == 'LV') {
                                    tbodyMNV += '<td class="valueLV">' + element.ca3TotalTime; + '</td>';
                                }
                                if(value=='F'){
                                    tbodyMNV += '<td class="valueF" cursor: pointer;" data-popup="popover" data-trigger="hover click" data-placement="left" data-html="true" data-content="' + titleMeal + '" ><b style="color:#ccc">'+valueFreeDuty+'/'+valueTotalFreeDuty+'</b></td>';
                                }
                                 else tbodyMNV += '<td class="ca3TimeGC"></td>';
                                
                                // tbodyTC+='<td class="ca3TimeGC"></td>';
                            }
                        }
                        tbodyMNV += '<td></td></tr>';
                        tbodyTC += '</tr>';


                        i++;

                    }
                }
                $("#tbltangca>tbody").html(tbodyTC);
                // console.log("HFDwerhn: ",tbodyTC)
                $("#tblworkcount>tbody").html(tbodyMNV);

                $('.tabClassTC').css('display', 'block');
                $('.valueF').popover({
                    template: '<div class="popover" style="margin-top:10px;background-color: #333; border:1px solid #ccc;color:#ccc"><div class="arrow"></div><div class="popover-content"></div></div>'
                    , container: 'body'
                });
                $('[data-popup=popover]').popover({
                    template: '<div class="popover" style="margin-top:85px;background-color: #333; border:1px solid #ccc;color:#ccc"><div class="arrow"></div><div class="popover-content"></div></div>'
                    , container: 'body'
                });

                if ($('#idinputMT').val() != '' && $('#idinputMT').val() != null && department != '') {
                    departPreSelection(factory, department);
                }
                //$('#selectMonth option[value="' +result.month+ '"]').prop('selected', true);
            },
            error: function () {
                alert("Fail!");
            },
            complete: function(){
            $('.loader').css('display', 'none');
        }
        })
    }

    function departPreSelection(factory, department) {
        $('#selectFactory').val(factory);
        departList = [department];
        changeFactory(factory);
    }

    var departList = [];

    $('#selectDepart').on('refreshed.bs.select', function () {
        if (departList.length > 0) {
            $('#selectDepart').selectpicker("val", departList);
            showDepartSelected();
            departList = [];
        }
    });

    function changeFactory(element) {
        // $('#idinputMT').val("");
        // $('#idinputSD').val("");
        // $('#idinputED').val("");
        $('.selectedBP').css('display', 'none');
        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/office/unit/list",
            contentType: "application/json; charset=utf-8",
            data: { factory: element },
            success: function (result) {
                var html = '';
                for (var i = 0; i < result.length; i++) {
                    html += '<option value="' + result[i].ouName + '" name="index'+i+'">' + result[i].ouName + '</option>'
                }
                $('#selectDepart').html(html);
                $('#selectDepart').selectpicker('refresh');

            },
            error: function () {
                alert("Fail!");
            }
        })
    }
    $('#selectDepart').on('changed.bs.select', function (e, clickedIndex, isSelected, previousValue) {
        showDepartSelected();
    });

    function showDepartSelected() {
        var textList = $('#selectDepart option:selected');
        //console.log("asdfgkGFEW",textList);
        var html = '';
        for (var i = 0; i < textList.length; i++) {
            html += '<div type="button" class="btn btn-text-select" style="margin: 5px; background: #555;" data-departCode =' + textList.get(i).value + ' data-depart_index ="' + textList.get(i).getAttribute('name') + '">' + textList.get(i).text + '<span class="badge closeX removeDepart"><b>x</b></span></div>';
        }
        $('.selectedBP').css('display', 'block');
        $('#departSelectedList').html(html);
        return;
    }

    $(document).on('click', '#departSelectedList .removeDepart', function () {
        // var departCode = this.parentNode.dataset.departcode;
        // console.log("show departcode: ", departCode);
        var departIndex = this.parentNode.dataset.depart_index;
        // $('#selectDepart option[value=' + departCode + ']').prop('selected', false);
        $('#selectDepart option[name=' + departIndex + ']').prop('selected', false);
        if (isIE()) {
            var divParentNode = this.parentNode;
            divParentNode.removeChild(this);
            divParentNode.innerHTML = '';
            divParentNode.removeNode();
        } else {
            $(this).parent().remove();
        }

        var textList = $('#selectDepart option:selected');
        var text = '';
        if (textList.length == 0) {
            text = 'Chọn tên bộ phận';
        } else {
            for (var i = 0; i < textList.length; i++) {
                if (i > 0) {
                    text += ', ';
                }
                text += textList.get(i).text;
            }
        }

        $("button[data-id='selectDepart']").prop('title', text);
        $("button[data-id='selectDepart'] .filter-option").html(text);
    });
    showFactory();
    function showFactory() {
        $.ajax({
            type: 'GET',
            url: "/api/hr/employee/tracking/office/factory/list",
            contentType: "application/json; charset=utf-8",
            // data: { date: mDate },
            success: function (result) {
                var html = '';
                for (var i = 0; i < result.length; i++) {
                    html += '<option class="classFactory" value="' + result[i] + '">' + result[i] + '</option>'
                }
                $('#selectFactory').append(html);
            },
            error: function () {
                alert("Fail!");
            }
        })
    }

    $("#btnExport").click(function () {
        // var exportOptions = {};
        month = $('#selectMonth').val();
        departName = $('#selectDepart').val();
        startDay = $('#idinputSD').val();
        endDay = $('#idinputED').val();

        var departNameExport = '';
        if (departName != null) {
            for (let index = 0; index < departName.length; index++) {
                departNameExport += departName[index] + ';';
            }
        }
        cardId = $('#idinputMT').val();
        if (cardId == null) {
            cardId = "";
        }
        // window.location.href='/api/hr/monthly/workcount/export?month='+exportOptions.month+'&departName='+exportOptions.departName+'&cardId='+exportOptions.cardId+'';
        window.open('/api/hr/monthly/workcount/export?month=' + month + '&departName=' + departNameExport + '&cardId=' + cardId + '&startDay=' + startDay + '&endDay=' + endDay + '');

    });
    function showFilter() {
        var month = $('#selectMonth').val();
        $("#valueMonth").html(month);

        var year = $('#selectYear').val();
        $("#valueYear").html(year);

        var cardId = $('#idinputMT').val();

        var startDay = $('#idinputSD').val();

        var endDay = $('#idinputED').val();

        if (cardId == '' || cardId == null) {
            var nameCode = '';
            var gData = $('#selectDepart').val();
            if (gData != null && gData.length > 0) {
                for (var index = 0; index < gData.length; index++) {
                    var e = gData[index];
                    // gData.forEach(e => {
                    nameCode += e + ";";
                    // })
                }
                loadWorkCount(month, '', nameCode, startDay, endDay, year);
                // $(".wmd-view-topscroll").css('display','block');
            }
        } else {
            loadWorkCount(month, cardId, '', startDay, endDay, year);
            // $(".wmd-view-topscroll").css('display','block');
        }
        return;
    }
    //Get the button
    var mybutton = document.getElementById("myBtn");

    // When the user scrolls down 20px from the top of the document, show the button
    window.onscroll = function () { scrollFunction() };

    function scrollFunction() {
        if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
            mybutton.style.display = "block";
        } else {
            mybutton.style.display = "none";
        }
    }

    // When the user clicks on the button, scroll to the top of the document
    function topFunction() {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
    }

    function isIE() {
        var ua = window.navigator.userAgent; //Check the userAgent property of the window.navigator object
        var msie = ua.indexOf('MSIE '); // IE 10 or older
        var trident = ua.indexOf('Trident/'); //IE 11

        return (msie > 0 || trident > 0);
    }


    $(document).on('click', function (e) {
        $('[data-toggle="popover"],[data-original-title]').each(function () {
            //the 'is' for buttons that trigger popups
            //the 'has' for icons within a button that triggers a popup
            if (!$(this).is(e.target) && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0) {
                $(this).popover('hide').data('bs.popover').inState.click = false // fix for BS 3.3.6
            }

        });
    });


// $(function(){
//     $(".wmd-view-topscroll").scroll(function(){
//         $(".tableScroll")
//             .scrollLeft($(".wmd-view-topscroll").scrollLeft());
//     });
//     $(".tableScroll").scroll(function(){
//         $(".wmd-view-topscroll")
//             .scrollLeft($(".tableScroll").scrollLeft());
//     });
// });
//$('#tblworkcount>div:last-child').css('top', "0px");

//date
// $(document).ready(function () {
//         var spcTS = getTimeSpan();
//         $('.datetimerange').data('daterangepicker').setStartDate(spcTS.startDay);
//         $('.datetimerange').data('daterangepicker').setEndDate(spcTS.endDay);

//         $('input[name=timeSpan]').on('change', function () {
//             dataset.timeSpan = this.value;
//             init();
//         });
//     });
</script>