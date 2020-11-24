<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!-- <link rel="stylesheet" href="/assets/custom/css/re/customTheme.css" /> -->
<link rel="stylesheet" href="/assets/custom/css/nbb/vlrr.css" />


<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="margin-bottom: 0px;">
    <div class="col-sm-12">
        <div class="panel panel-overview" id="header">
            <b><span>NBB VLRR </span><span id="titlePage"></span></b>
        </div>
        <div class="row" style="margin: unset; padding: unset">
            <div class="drlMenu" style="margin: unset; min-width: 28%;float: left;">
                <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                            class="icon-calendar22"></i></span>
                    <input type="text" class="form-control datetimevlrr" side="right" name="timeSpan"
                        style="border-bottom: 0px !important; color: #fff;">
                </div>
            </div>
            <!-- <input type="text" class=" form-control buttonSearch" id="idSearch" placeholder="Enter Serial Number" /> -->
            <!-- <div class="classSerialNumber" style="margin: 0px 10px; min-width: 10%;float: left;">
                <select class="form-control bootstrap-select selectSerial" id="idSerial">
                    <option value="" disabled="" selected="">Serial Number</option>
                    <option value="ALL" selected="">ALL</option>
                    <option value="SV">Vento (SV)</option>
                    <option value="SM">Mistral (SM)</option> 
                </select>
            </div>    -->
            <div class="selectPath" style="margin: 0px 10px;min-width: 13%; float: left;">
                <!-- onchange="changeSelect(this.value)" -->
                <select required class="form-control bootstrap-select selectSearch" id="idSelect"
                    onchange="changeSelect(this.value)">
                    <option value="" disabled="" selected="">Key Part</option>
                    <option value="0DU">Foxconn (ODU)</option>
                    <option value="1F3">EVERWIN (1F3)</option>
                    <option value="0ET">Forbetter (0ET)</option>
                    <option value="1DM">XinKe (1DM)</option>
                    <option value="1G5">ANJIE (1G5)</option>
                    <option value="0FS">Green (0FS)</option>
                    <option value="1FF">Foxconn (1FF)</option>

                    <option value="1J5">Tongda (1J5)</option>
                    <option value="TKM">TK (TKM)</option>
                    <option value="1F6">Xinxiu (1F6)</option>
                    <option value="1A0">Tonly (1A0)</option>
                    <option value="">Other</option>
                </select>
            </div>
            <!-- onchange="changeSelect(this.value)" -->
            <!-- <div class="classPartNo" style="margin:0px 10px; width: 15%;float: left;">
                <select required class="form-control bootstrap-select selectPartNo" id="idSelectPartNo">
                    <option value="" disabled="" selected="">Key Part No</option>
                    <option value="">All</option>
                    <option value="">G852-01064-01 (Vento)</option>
                    <option value="">G852-01237-01 (Vento)</option>
                    <option value="">G852-00739-01 (Mistral)</option>
                    <option value="">G852-00741-01 (Mistral)</option>
                </select>
            </div>   -->
            <div class="classPartNo" style="min-width: 18%;float: left;">
                <select id="idSelectPartNo" class="form-control bootstrap-select selectPartNo selectpicker"
                    multiple="multiple" data-none-selected-text="Key Part No" data-width="auto" data-actions-box="true">
                    <option value="G852-01064-01">G852-01064-01 (Vento)</option>
                    <option value="G852-01237-01">G852-01237-01 (Vento)</option>
                    <option value="G852-00739-01">G852-00739-01 (Mistral)</option>
                    <option value="G852-00741-01">G852-00741-01 (Mistral)</option>
                    <option value="G852-01075-01">G852-01075-01 (Mistral)</option>
                    <option value="G864-00168-01">G864-00168-01 (Mistral)</option>
                    <option value="G852-01064-02">G852-01064-02 (Mistral)</option>
                    <option value="G852-01064-03">G852-01064-03 (Mistral)</option>
                    <option value="G852-01064-01">G852-01064-01 (EVERWIN)</option>
                    <option value="G852-01237-01">G852-01237-01 (EVERWIN)</option>
                    <option value="G852-00739-01">G852-00739-01 (EVERWIN)</option>
                    <option value="G852-00741-01">G852-00741-01 (EVERWIN)</option>
                    <option value="G850-00134-01">G850-00134-01 (EVERWIN)</option>
                    <option value="G850-00103-02">G850-00103-02 (EVERWIN)</option>
                    <option value="G804-00370-01">G804-00370-01 (Forbetter)</option>
                    <option value="G804-00293-01">G804-00293-01 (Forbetter)</option>
                    <option value="G804-00370-01">G804-00370-01 (1DM)</option>
                    <option value="G804-00293-01">G804-00293-01 (1DM)</option>
                    <option value="G804-00370-02">G804-00370-02 (1DM)</option>
                    <option value="G804-00370-03">G804-00370-03 (1DM)</option>
                    <option value="G805-00184-01">G805-00184-01 (Z1)</option>
                    <option value="G805-00192-07">G805-00192-07 (Z1)</option>
                    <option value="G805-00192-08">G805-00192-08 (Z1)</option>
                    <option value="G852-01674-02">G852-01674-02 (Z1)</option>
                    <option value="G730-05317-02">G730-05317-02 (Z1)</option>
                    <option value="G730-05317-04">G730-05317-04 (Z1)</option>
                    <option value="G730-05317-06">G730-05317-06 (Z1)</option>
                    <option value="G730-05317-07">G730-05317-07 (Z1)</option>
                    <option value="G730-05317-52">G730-05317-52 (Z1)</option>
                    <option value="G730-05317-54">G730-05317-54 (Z1)</option>
                    <option value="G730-05317-56">G730-05317-56 (Z1)</option>
                    <option value="G730-05317-57">G730-05317-57 (Z1)</option>
                    <option value="G852-01674-02">G852-01674-02 (Z1)</option>
                    <option value="G852-01674-03">G852-01674-03 (Z1)</option>
                    <option value="G852-01674-04">G852-01674-04 (Z1)</option>
                    <option value="G852-01674-06">G852-01674-06 (Z1)</option>

                    <option value="G805-00192-08">G805-00192-08 (Z1)</option>
                    <option value="G852-1331-01">G852-1331-01 (EVERWIN)</option>
                    <option value="G852-1331-02">G852-1331-02 (EVERWIN)</option>
                    <option value="G852-1331-01">G852-1331-01 (elaine)</option>
                    <option value="G852-1331-02">G852-1331-02 (elaine)</option>
                    <option value="G852-1331-03">G852-1331-03 (elaine)</option>
                    <option value="G852-1331-04">G852-1331-04 (elaine)</option>
                    <option value="G852-1331-05">G852-1331-05 (elaine)</option>
                    <option value="G730-04458-13">G730-04458-13 (elaine)</option>
                    <option value="G730-04458-14">G730-04458-14 (elaine)</option>
                    <option value="G730-04458-13">G730-04458-13 (elaine)</option>
                    <option value="G730-04458-14">G730-04458-14 (elaine)</option>
                    <option value="G730-04458-15">G730-04458-15 (elaine)</option>
                    <option value="G730-04458-16">G730-04458-16 (elaine)</option>
                    <option value="G730-04458-17">G730-04458-17 (elaine)</option>

                </select>
            </div>
            <div class="buttonFilter" style="float: left;margin: 0px 10px;">
                <button class="btn filterVlrr" id="idbtnVlrr" onclick="showSearch()"><i class="fa fa-search"></i>
                    Search</button>
            </div>
            <div class=" wrapperBtnExport" style="float: right;">
                <a class=" btn btn-lg" id="btnExport"
                    style="padding: 9px 9px; border-radius: 5px; color: #fff;background: #444;">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
            <div class="Total" id="idTotal" style="float: right;padding: 9px 10px;color: #fff;
             border-radius: 5px;margin-right: 15px;">

            </div>
        </div>
        <!-- <div class="row" style="margin:unset">
            <div class=" panel panel-overview input-group" style="width:100
            %; margin-bottom: 5px; background: #333;float: left;">
                <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i
                        class="icon-calendar22"></i></span>
                <input type="text" class="form-control datetimevlrr" side="right" name="timeSpan"
                    style="height: 33px; color: #fff; border-bottom: none;">
            </div>
        </div>
        <div class="row" style="margin: unset;"> -->
        <!-- <div class="inputSearch" style="margin: unset;width: 55%;">
                <input type="text" class=" form-control buttonSearch" id="idSearch" placeholder="Search key path..." />
            </div> -->
        <!-- <div class="selectPath" style="margin: unset; width: 20%;">
                <select class="form-control selectSearch" id="idSelect">
                    <option value="">1</option>
                    <option value="">2</option>
                    <option value="">3</option>
                </select>
            </div>
            <div class="buttonFilter">
                <button class="btn filterVlrr" id="idbtnVlrr" onclick="showSearch()"><i class="fa fa-search"></i> Search</button>
            </div>
        </div> -->

        <div class="row" style="margin: unset;margin-top: 5px;">
            <div class="col-sm-12 table-responsive table-scoll"
                style="padding: 0;max-height: calc(100vh - 165px);color: #ccc; padding: unset;">
                <table id="tblSearch" class="table table-bordered table-sticky scroll-div2" style="text-align: center;">
                    <thead class="theadSearch">
                        <tr class="classThead">
                            <th>EMP_NO</th>
                            <th>SERIAL_NUMBER</th>
                            <th>KEY_PART_NO</th>
                            <th>KEY_PART_SN</th>
                            <th>KP_RELATION</th>
                            <th>GROUP_NAME</th>
                            <th>CARTON_NO</th>
                            <th>WORK_TIME</th>
                            <th>VERSION</th>
                            <th>PART_MODE</th>
                            <th>KP_CODE</th>
                            <th>MO_NUMBER</th>
                        </tr>
                    </thead>
                    <tbody class="tbodySearch">
                    </tbody>
                </table>
            </div>
        </div>
        <div class="row">
            <!-- datatable-footer table -->
            <div class="classPagination" style="padding-top: 0px; padding-bottom: 0px; text-align: center; ">
                <ul id="pagination-table" class="pagination-lg pagination">
                    <li class="page-item first"><a href="#" class="page-link">First</a></li>
                    <li class="page-item prev"><a href="#" class="page-link">Prev</a></li>
                    <li class="page-item active"><a href="#" class="page-link">1</a></li>
                    <li class="page-item"><a href="#" class="page-link">2</a></li>
                    <li class="page-item "><a href="#" class="page-link">3</a></li>
                    <li class="page-item"><a href="#" class="page-link">4</a></li>
                    <li class="page-item"><a href="#" class="page-link">5</a></li>
                    <li class="page-item"><a href="#" class="page-link">6</a></li>
                    <li class="page-item next"><a href="#" class="page-link">Next</a></li>
                    <li class="page-item last"><a href="#" class="page-link">Last</a></li>
                </ul>
            </div>
        </div>
    </div>
</div>
<style>
    .bs-actionsbox .btn-group button {
        width: 100%;
        text-align: left;
        color: #fff;
    }

    /* .classPartNo .selectPartNo{
        display: none;
    } */

    .selectPartNo .dropdown-menu .bs-searchbox input {
        color: #fff;
    }

    .bootstrap-select.btn-group .no-results {
        background: #999;
    }

    .selectPartNo {
        width: 255px !important;
    }

    .selectPartNo .dropdown-menu.inner {
        width: 230px !important;
        min-height: 33px !important;
        max-height: 200px !important;
    }

    .bootstrap-select.btn-group .dropdown-menu>li>a .check-mark {
        right: 15px;
        /* color: #fff !important; */
    }

    .classPartNo .selectPartNo.open .dropdown-menu.open .dropdown-menu.inner li.active a .glyphicon.glyphicon-ok.check-mark {
        color: #fff !important;
    }

    .bootstrap-select.btn-group .dropdown-menu>li>a .active {
        right: 15px;
        color: #fff !important;
    }

    #header {
        text-align: center;
        font-size: 25px;
        margin-top: 1px;
        margin-bottom: 5px;
        background: #333;
        color: #ccc;
    }

    .selectSerial .dropdown-toggle {
        background: #333;
        color: #fff !important;
        border-radius: 5px;
        border-bottom: none;
    }

    .selectSearch .dropdown-toggle {
        background: #333;
        color: #fff !important;
        border-radius: 5px;
        border-bottom: none;
    }

    .selectPartNo .dropdown-toggle {
        background: #333;
        color: #fff !important;
        border-radius: 5px;
        border-bottom: none;
    }

    .selectSerial .dropdown-menu.open {
        background-color: #444;
        color: #ccc;
    }

    .selectPartNo .dropdown-menu.open {
        background-color: #444;
        color: #ccc;
    }

    .selectSearch .dropdown-menu.open {
        background-color: #444;
        color: #ccc;
    }

    .dropdown-menu.inner>li a {
        background-color: #444;
        color: #fff;
    }

    .dropdown-menu.inner {
        background-color: #444;
        color: #fff;
    }

    .dropdown-menu>.active {
        background-color: #999;
    }

    .pagination>.active>a,
    .pagination>.active>span,
    .pagination>.active>a:hover,
    .pagination>.active>span:hover,
    .pagination>.active>a:focus,
    .pagination>.active>span:focus {
        background-color: #999;
        border-color: #999;
    }

    .dropdown-menu.inner li a:hover {
        color: #ccc;
        background: #696969;
    }

    .selectSerial .dropdown-toggle .filter-option {
        padding-left: 10px;
    }

    .selectSearch .dropdown-toggle .filter-option {
        padding-left: 10px;
    }

    .selectPartNo .dropdown-toggle .filter-option {
        padding-left: 10px;
    }

    .selectSerial .dropdown-toggle .bs-caret .caret {
        margin-right: 5px;
    }

    .selectSearch .dropdown-toggle .bs-caret .caret {
        margin-right: 5px;
    }

    .selectPartNo .dropdown-toggle .bs-caret .caret {
        margin-right: 5px;
    }

    #idSerial {
        background: #333;
        color: #fff;
        float: left;
        border-radius: 5px;
        box-sizing: border-box;
        position: relative;
        padding-left: 10px;
    }

    #idSelect {
        background: #333;
        color: #fff;
        float: left;
        border-radius: 5px;
        box-sizing: border-box;
        position: relative;
        padding-left: 10px;
    }

    #idSerial .dropdown-menu.open {
        background: #444444;
        color: #fff;
        width: 100%;
    }

    #idSelect .dropdown-menu.open {
        background: #444444;
        color: #fff;
        width: 100%;
    }

    #idbtnVlrr {
        border-radius: 4px;
        background: #555;
        color: #fff;
        text-transform: capitalize;
    }

    .classPagination .pagination-lg li a:hover {
        background: #444;
        color: #fff;
    }

    ul li.page-item a {
        background: #333;
        color: #fff;
    }

    .classTbody {
        color: #ccc;
        background: #333;
    }

    .hiddenPagi {
        display: none;
    }

    .pagination>.disabled>span,
    .pagination>.disabled>span:hover,
    .pagination>.disabled>span:focus,
    .pagination>.disabled>a,
    .pagination>.disabled>a:hover,
    .pagination>.disabled>a:focus {
        color: #999999;
        background-color: #444444;
        border-color: #ddd;
        cursor: not-allowed;
    }

    .table-responsive {
        display: none;
    }

    .classPagination {
        display: none;
    }

    .classPagination {
        margin-top: 10px;
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

    body.loading .loader {
        overflow: hidden;
    }

    body.loading .loader {
        display: block;
    }

    .table>thead>tr>th {
        padding-left: 8px !important;
        padding-top: 8px;
        padding-bottom: 8px;
        text-align: center;
    }

    .table>tbody>tr>td {
        padding-left: 8px;
        padding-top: 2px;
        padding-bottom: 2px;
    }

    .selectSerial:hover {
        margin-top: -1px;
    }

    .selectSearch:hover {
        margin-top: -1px;
    }

    .selectPartNo:hover {
        margin-top: -1px;
    }

    #btnExport:hover {
        margin-top: -1px;
    }

    .pagination>.active>a {
        background-color: #999;
    }

    /* li.active a span.check-mark{
        color: #fff !important;
    } */
</style>

<script>
    var dataset = {};
    var EnterSerial, EnterKeyPart, EnterKeyPartNo;
    var xml = '';
    $body = $("body");
    $(document).on({
        ajaxStart: function () {
            $body.addClass("loading");
        },
        ajaxStop: function () {
            $body.removeClass("loading");
        }
    });
    //total
    $('#idTotal').html('<span style ="font-size:16px">Total:<b> 0</b></span>');

    function showSearch() {
        xml = '';
        EnterKeyPart = $('#idSelect').val();
        // dataset.valueSelect =  EnterKeyPart;

        EnterKeyPartNo = $('#idSelectPartNo').val();
        // dataset.valueSelectNo =  EnterKeyPartNo;

        if (EnterKeyPartNo != null) {
            for (let index = 0; index < EnterKeyPartNo.length; index++) {

                if (index == EnterKeyPartNo.length - 1) {
                    xml += EnterKeyPartNo[index];
                } else {
                    xml += EnterKeyPartNo[index] + ';';
                }
            }
        }
        if (EnterKeyPartNo == null && EnterKeyPart == null) {
            alert('Enter key part, Please!!');
        } else {
            loadPageIndex();

        }
    }

    function changeSelect(element) {
        var html = '';
        switch (element) {
            case "0DU":
                html = '<option class="foxconn" value="G852-01064-01">G852-01064-01 (Vento)</option>' +
                    '<option class="foxconn" value="G852-01237-01">G852-01237-01 (Vento)</option>' +
                    '<option class="foxconn" value="G852-00739-01">G852-00739-01 (Mistral)</option>' +
                    '<option class="foxconn" value="G852-00741-01">G852-00741-01 (Mistral)</option>' +
                    '<option class="foxconn" value="G852-01075-01">G852-01075-01 (Mistral)</option>' +
                    '<option class="foxconn" value="G864-00168-01">G864-00168-01 (Mistral)</option>' +
                    '<option class="foxconn" value="G852-01064-02">G852-01064-02 (Mistral)</option>' +
                    '<option class="foxconn" value="G852-01064-03">G852-01064-03 (Mistral)</option>';

                break;
            case "1F3":
                html = '<option class="everwin" value="G852-01064-01">G852-01064-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-01237-01">G852-01237-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-00739-01">G852-00739-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-00741-01">G852-00741-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G850-00134-01">G850-00134-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G850-00103-02">G850-00103-02 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-1331-01">G852-1331-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-1331-02">G852-1331-02 (EVERWIN)</option>';
                break;
            case "0ET":
                html = '<option class="forbetter" value="G804-00370-01">G804-00370-01 (Forbetter)</option>' +
                    '<option class="forbetter" value="G804-00293-01">G804-00293-01 (Forbetter)</option>';
                break;
            case "1DM":
                html = '<option class="1dm" value="G804-00370-01">G804-00370-01 (1DM)</option>' +
                    '<option class="1dm" value="G804-00293-01">G804-00293-01 (1DM)</option>' +
                    '<option class="1dm" value="G804-00370-02">G804-00370-02 (1DM)</option>' +
                    '<option class="1dm" value="G804-00370-03">G804-00370-03 (1DM)</option>';

                break;
            case "1G5":
                html = '<option class="1g5" value="G805-00184-01">G805-00184-01 (Z1)</option>' +
                    '<option class="1g5" value="G805-00192-07">G805-00192-07 (Z1)</option>' +
                    '<option class="1g5" value="G805-00192-08">G805-00192-08 (Z1)</option>';
                break;
            case "0FS":
                html = '<option class="0sf" value="G852-01674-02">G852-01674-02 (Z1)</option>';
                break;
            case "1FF":
                html = '<option class="1ff" value="G730-05317-02">G730-05317-02 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-04">G730-05317-04 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-06">G730-05317-06 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-07">G730-05317-07 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-52">G730-05317-52 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-54">G730-05317-54 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-56">G730-05317-56 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-57">G730-05317-57 (Z1)</option>' +
                    '<option class="1ff" value="G852-01674-02">G852-01674-02 (Z1)</option>' +
                    '<option class="1ff" value="G852-01674-03">G852-01674-03 (Z1)</option>' +
                    '<option class="1ff" value="G852-01674-04">G852-01674-04 (Z1)</option>' +
                    '<option class="1ff" value="G852-01674-06">G852-01674-06 (Z1)</option>';
                break;
            case "1J5":
                html = '<option class="1j5" value="G805-00192-08">G805-00192-08 (Z1)</option>';
                break;
            case "TKM":
                html = '<option class="tkm" value="G852-1331-01">G852-1331-01 (TKM)</option>' +
                    '<option class="tkm" value="G852-1331-02">G852-1331-02 (TKM)</option>' +
                    '<option class="tkm" value="G852-1331-03">G852-1331-03 (TKM)</option>' +
                    '<option class="tkm" value="G852-1331-04">G852-1331-04 (TKM)</option>' +
                    '<option class="tkm" value="G852-1331-05">G852-1331-05 (TKM)</option>';
                break;
            case "1F6":
                html = '<option class="1f6" value="G730-04458-13">G730-04458-13 (1F6)</option>' +
                    '<option class="1f6" value="G730-04458-14">G730-04458-14 (1F6)</option>';
                break;
            case "1A0":
                html = '<option class="1a0" value="G730-04458-13">G730-04458-13 (1A0)</option>' +
                    '<option class="1a0" value="G730-04458-14">G730-04458-14 (1A0)</option>' +
                    '<option class="1a0" value="G730-04458-15">G730-04458-15 (1A0)</option>' +
                    '<option class="1a0" value="G730-04458-16">G730-04458-16 (1A0)</option>' +
                    '<option class="1a0" value="G730-04458-17">G730-04458-17 (1A0)</option>';
                break;
            default:
                html = '<option class="foxconn" value="G852-01064-01">G852-01064-01 (Vento)</option>' +
                    '<option class="foxconn" value="G852-01237-01">G852-01237-01 (Vento)</option>' +
                    '<option class="foxconn" value="G852-00739-01">G852-00739-01 (Mistral)</option>' +
                    '<option class="foxconn" value="G852-00741-01">G852-00741-01 (Mistral)</option>' +
                    '<option class="foxconn" value="G852-01075-01">G852-01075-01 (Mistral)</option>' +
                    '<option class="foxconn" value="G864-00168-01">G864-00168-01 (Mistral)</option>' +
                    '<option class="foxconn" value="G852-01064-02">G852-01064-02 (Mistral)</option>' +
                    '<option class="foxconn" value="G852-01064-03">G852-01064-03 (Mistral)</option>' +
                    '<option class="everwin" value="G852-01064-01">G852-01064-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-01237-01">G852-01237-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-00739-01">G852-00739-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-00741-01">G852-00741-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G850-00134-01">G850-00134-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G850-00103-02">G850-00103-02 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-1331-01">G852-1331-01 (EVERWIN)</option>' +
                    '<option class="everwin" value="G852-1331-02">G852-1331-02 (EVERWIN)</option>' +
                    '<option class="forbetter" value="G804-00370-01">G804-00370-01 (Forbetter)</option>' +
                    '<option class="forbetter" value="G804-00293-01">G804-00293-01 (Forbetter)</option>' +
                    '<option class="1dm" value="G804-00370-01">G804-00370-01 (1DM)</option>' +
                    '<option class="1dm" value="G804-00293-01">G804-00293-01 (1DM)</option>' +
                    '<option class="1dm" value="G804-00370-02">G804-00370-02 (1DM)</option>' +
                    '<option class="1dm" value="G804-00370-03">G804-00370-03 (1DM)</option>' +
                    '<option class="1g5" value="G805-00184-01">G805-00184-01 (Z1)</option>' +
                    '<option class="1g5" value="G805-00192-07">G805-00192-07 (Z1)</option>' +
                    '<option class="1g5" value="G805-00192-08">G805-00192-08 (Z1)</option>' +
                    '<option class="0sf" value="G852-01674-02">G852-01674-02 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-02">G730-05317-02 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-04">G730-05317-04 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-06">G730-05317-06 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-07">G730-05317-07 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-52">G730-05317-52 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-54">G730-05317-54 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-56">G730-05317-56 (Z1)</option>' +
                    '<option class="1ff" value="G730-05317-57">G730-05317-57 (Z1)</option>' +
                    '<option class="1ff" value="G852-01674-02">G852-01674-02 (Z1)</option>' +
                    '<option class="1ff" value="G852-01674-03">G852-01674-03 (Z1)</option>' +
                    '<option class="1ff" value="G852-01674-04">G852-01674-04 (Z1)</option>' +
                    '<option class="1ff" value="G852-01674-06">G852-01674-06 (Z1)</option>' +
                    '<option class="1j5" value="G805-00192-08">G805-00192-08 (Z1)</option>' +
                    '<option class="tkm" value="G852-1331-01">G852-1331-01 (TKM)</option>' +
                    '<option class="tkm" value="G852-1331-02">G852-1331-02 (TKM)</option>' +
                    '<option class="tkm" value="G852-1331-03">G852-1331-03 (TKM)</option>' +
                    '<option class="tkm" value="G852-1331-04">G852-1331-04 (TKM)</option>' +
                    '<option class="tkm" value="G852-1331-05">G852-1331-05 (TKM)</option>' +
                    '<option class="1f6" value="G730-04458-13">G730-04458-13 (1F6)</option>' +
                    '<option class="1f6" value="G730-04458-14">G730-04458-14 (1F6)</option>' +
                    '<option class="1a0" value="G730-04458-13">G730-04458-13 (1A0)</option>' +
                    '<option class="1a0" value="G730-04458-14">G730-04458-14 (1A0)</option>' +
                    '<option class="1a0" value="G730-04458-15">G730-04458-15 (1A0)</option>' +
                    '<option class="1a0" value="G730-04458-16">G730-04458-16 (1A0)</option>' +
                    '<option class="1a0" value="G730-04458-17">G730-04458-17 (1A0)</option>';
                break;
        }
        $('#idSelectPartNo').html(html);
        $('#idSelectPartNo').selectpicker('refresh');
    }

    function loadPageIndex() {
        $.ajax({
            type: "GET",
            url: "/api/test/nbb/vlrr/pageIndex",
            data: {
                keyPart: EnterKeyPart,
                keyPartNo: xml,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                //  console.log("loadPageIndex:",data)
                dataset.lastPage = data.pageQty;

                $('#idTotal').html('<span style ="font-size:16px">Total: <b>' + data.totalData + '</b></span>');
                if (dataset.lastPage > 0) {
                    loadData(1);
                    var html = '<li class="page-item page-first disabled"><a href="#" onclick="loadData(1)" class="page-link">First</a></li>' +
                        '<li class="page-item page-prev disabled"><a href="#" onclick="loadPagePrev()" class="page-link">Prev</a></li>';
                    for (let index = 0; index < data.pageQty; index++) {
                        if (index < 6) {
                            if (index == 0)
                                html += '<li class="page-item active page_' + (index + 1) + '"><a href="#" onclick="loadData(' + (index + 1) + ')" class="page-link">' + (index + 1) + '</a></li>';
                            else html += '<li class="page-item page_' + (index + 1) + '"><a href="#" onclick="loadData(' + (index + 1) + ')" class="page-link">' + (index + 1) + '</a></li>';
                        } else html += '<li class="page-item page_' + (index + 1) + '"><a href="#" onclick="loadData(' + (index + 1) + ')" class="page-link hiddenPagi">' + (index + 1) + '</a></li>';

                    }
                    html += '<li class="page-item page-next"><a href="#" onclick="loadPageNext()" class="page-link">Next</a></li>' +
                        '<li class="page-item page-last"><a href="#" onclick="loadData(' + data.pageQty + ')" class="page-link">Last</a></li>';

                    $('.pagination').html(html);
                    $('.classPagination').css('display', 'block');
                    $('.table-responsive').css('display', 'block');
                } else {
                    $('.pagination').html("");
                    $('.classPagination').css('display', 'none');
                    $('.table-responsive').css('display', 'none');
                    loadData(0);
                }

            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    function loadData(index) {
        $.ajax({
            type: "GET",
            url: "/api/test/nbb/vlrr/getdata",
            data: {
                keyPart: EnterKeyPart,
                page: index,
                keyPartNo: xml,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                // console.log("datagetdata",data);
                var thSearch = '';
                $("#tblSearch>tbody").html(thSearch);
                if (data.length > 0) {
                    for (let i = 0; i < data.length; i++) {
                        thSearch += '<tr class="classTbody"><td>' + data[i].EMP_NO + '</td>' +
                            '<td>' + data[i].SERIAL_NUMBER + '</td>' +
                            '<td>' + data[i].KEY_PART_NO + '</td>' +
                            '<td>' + data[i].KEY_PART_SN + '</td>' +
                            '<td>' + data[i].KP_RELATION + '</td>' +
                            '<td>' + data[i].GROUP_NAME + '</td>' +
                            '<td>' + data[i].CARTON_NO + '</td>' +
                            '<td>' + data[i].WORK_TIME + '</td>' +
                            '<td>' + data[i].VERSION + '</td>' +
                            '<td>' + data[i].PART_MODE + '</td>' +
                            '<td>' + data[i].KP_CODE + '</td>' +
                            '<td>' + data[i].MO_NUMBER + '</td></tr>';
                    }
                    $("#tblSearch>tbody").html(thSearch);
                } else {
                    $('.classPagination').css('display', 'none');
                    $('.table-responsive').css('display', 'none');
                    alert("No Data, Please select other Key Part No !!!")
                }
                // loadPageIndex(data.page);
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });

        if (index == 1) {
            $('.page-first').addClass('disabled');
            $('.page-prev').addClass('disabled');
            $('.page-next').removeClass('disabled');
            $('.page-last').removeClass('disabled');

            for (let i = index; i < 7; i++) {
                $('.page_' + (i) + ' a').removeClass("hiddenPagi");
            }
            for (let i = 7; i <= (dataset.lastPage); i++) {
                $('.page_' + (i) + ' a').addClass("hiddenPagi");
            }
            $('.page-item').removeClass('active');
            $('.page_' + index).addClass('active');
        } else if (index == dataset.lastPage) {
            $('.page-next').addClass('disabled');
            $('.page-last').addClass('disabled');
            $('.page-first').removeClass('disabled');
            $('.page-prev').removeClass('disabled');

            for (let i = index; i > (index - 6); i--) {
                $('.page_' + (i) + ' a').removeClass("hiddenPagi");
            }
            for (let i = 1; i < (index - 6); i++) {
                $('.page_' + (i) + ' a').addClass("hiddenPagi");
            }
            $('.page-item').removeClass('active');
            $('.page_' + index).addClass('active');
        } else {
            $('.page-first').removeClass('disabled');
            $('.page-prev').removeClass('disabled');
            $('.page-next').removeClass('disabled');
            $('.page-last').removeClass('disabled');
        }

        if (dataset.prePage < index) {
            if (index > 3 && (index + 3) <= dataset.lastPage) {
                for (let i = 1; i < index - 2; i++) {
                    $('.page_' + (i) + ' a').addClass("hiddenPagi");
                }
                for (let i = index; i <= (index + 3); i++) {
                    $('.page_' + (i) + ' a').removeClass("hiddenPagi");
                }
                $('.page-item').removeClass('active');
                $('.page_' + index).addClass('active');
            } else {
                $('.page-item').removeClass('active');
                $('.page_' + index).addClass('active');
            }
        } else if (dataset.prePage > index) {
            if (index > 3 && index <= dataset.lastPage) {
                if (index < (dataset.lastPage - 3)) {
                    for (let i = index; i > index - 3; i--) {
                        $('.page_' + (i) + ' a').removeClass("hiddenPagi");
                    }
                    for (let i = index + 4; i <= dataset.lastPage; i++) {
                        $('.page_' + (i) + ' a').addClass("hiddenPagi");
                    }
                } else if (index < dataset.lastPage) {
                    for (let i = index; i > (dataset.lastPage - 6); i--) {
                        $('.page_' + (i) + ' a').removeClass("hiddenPagi");
                    }
                    for (let i = 1; i < (dataset.lastPage - 6); i++) {
                        $('.page_' + (i) + ' a').addClass("hiddenPagi");
                    }
                }

                $('.page-item').removeClass('active');
                $('.page_' + index).addClass('active');
            } else {
                if (dataset.prePage > 3) {
                    for (let i = index; i > 0; i--) {
                        $('.page_' + (i) + ' a').removeClass("hiddenPagi");
                    }
                    for (let i = dataset.prePage + 3; i < dataset.lastPage; i++) {
                        $('.page_' + (i) + ' a').addClass("hiddenPagi");
                    }
                }

                $('.page-item').removeClass('active');
                $('.page_' + index).addClass('active');
            }
        }
        dataset.prePage = index;

    }

    function loadPagePrev() {
        var page = dataset.prePage - 1;
        if (page > 0) {
            loadData(page);
        }
    }

    function loadPageNext() {
        var page = dataset.prePage + 1;
        if (page <= dataset.lastPage) {
            loadData(page);
        }
    }

    $("#btnExport").click(function () {
        var mKeyPart = $('#idSelect').val();
        var keyPartN = $('#idSelectPartNo').val();
        var ml = '';
        var EnterTime = dataset.timeSpan;
        if ((keyPartN == null && mKeyPart == null) || (keyPartN == null && mKeyPart == '')) {
            alert('Enter Key Part No, Please!!');
        } else if (keyPartN != null) {
            for (let index = 0; index < keyPartN.length; index++) {
                if (index == keyPartN.length - 1) {
                    ml += keyPartN[index];
                } else {
                    ml += keyPartN[index] + ';';
                }
            }
            if (ml != null || mKeyPart != null) {
                window.open('/api/test/nbb/vlrr/exportKeyPart?keyPart=' + mKeyPart + '&keyPartNo=' + ml + '&timeSpan=' + EnterTime);
            } else {
                alert('Enter key part, Please!!');
            }
        } else {
            window.open('/api/test/nbb/vlrr/exportKeyPart?keyPart=' + mKeyPart + '&keyPartNo=' + ml + '&timeSpan=' + EnterTime);
        }
    });

    // Get date and month diffrent
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
        $('.datetimevlrr[side=right]').daterangepicker({
            maxSpan: {
                days: 90
                // months: 4
            },
            timePicker: true,
            timePicker24Hour: true,
            opens: "right",
            applyClass: 'bg-slate-600',
            cancelClass: 'btn-default',
            timePickerIncrement: 30,
            locale: {
                format: 'YYYY/MM/DD HH:mm:ss'
            }

        });

        $('.datetimevlrr[side=left]').daterangepicker({
            maxSpan: {
                days: 90
                // months:4
            },
            timePicker: true,

            timePicker24Hour: true,
            opens: "left",
            applyClass: 'bg-slate-600',
            cancelClass: 'btn-default',
            // timePickerIncrement: 30,
            locale: {
                format: 'YYYY/MM/DD HH:mm:ss'
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

    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                var current = new Date(data);
                var startDate = moment(current).format("YYYY/MM/DD") + ' 07:30:00';
                var endDate = moment(current).add(1, "day").format("YYYY/MM/DD") + ' 07:30:00';
                $('.datetimevlrr').data('daterangepicker').setStartDate(new Date(startDate));
                $('.datetimevlrr').data('daterangepicker').setEndDate(new Date(endDate));
            },
            failure: function (errMsg) {
                console.log(errMsg);
            }
        });
    }

    $(document).ready(function () {
        getTimeNow();
        $('input[name=timeSpan]').on('change', function () {
            dataset.timeSpan = this.value;

        });

    }); // END DOcument.ready
        // $(document).ready(function(){
        //search filter
        // $('#idSearch').on('keyup', function(){
        //     var value = $(this).val().toLowerCase();
        //     $('#tblSearch tbody tr').filter(function(){
        //         $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        //     });
        // });

        // });
</script>