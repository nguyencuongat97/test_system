<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div class="panel panel-overview" id="header">
    <b style="font-size: 20px; line-height: 40px;"><span>ENHANCING PEOPLE'S LIVES BY PROVIDING SOLUTION, PRODUCTS,
            SERVICES AND DRIVE SYNERGY WITHIN THE ICT ECOSYSTEM</span></b></br>
    <i style="font-size: 18px;">NÂNG TẦM CHẤT LƯỢNG CUỘC SỐNG QUA CÁC GIẢI PHÁP, SẢN PHẨM, DỊCH VỤ, THÚC ĐẨY SỰ ĐỒNG HỢP
        TRONG <br>HỆ THỐNG CÔNG NGHỆ THÔNG TIN VÀ TRUYỀN THÔNG</i>
</div>
<div class="col-md-12">
    <div class="col-md-1" style="text-align: center">
        <div class="row">
            <a data-href="/re/checklist/standard" onclick="openWindow(this.dataset.href);">
                <div style="margin-top: 10px;">
                    <img class="d-img" src="/assets/images/custom/re/SI_Repair_Tool.png">
                </div>
                <label class="label cl-label">8S Standard for RE</label>
            </a>
        </div>
        <div class="row">
            <a data-href="/re/checklist/ownerdwfinearea" onclick="openWindow(this.dataset.href);">
                <div style="margin-top: 10px;">
                    <img class="cl-img" src="/assets/images/custom/re/Bone_Pile.png">
                </div>
                <label class="label cl-label">Owner define area</label>
            </a>
        </div>
        <div class="row">
            <div style="margin-top: 10px;" onclick="popupChooseOwnerChecklist()">
                <img class="cl-img" src="/assets/images/custom/re/Check_in.png">
            </div>
            <label class="label cl-label">Daily Check list</label>
        </div>
        <div class="row">
            <div style="margin-top: 10px;" onclick="popupChooseAuditTeam()">
                <img class="cl-img" src="/assets/images/custom/re/BOM.png">
            </div>
            <label class="label cl-label">8s Audit team </label>
        </div>
        <div class="row">
            <a data-href="/re/checklist/result" onclick="openWindow(this.dataset.href);">
                <div style="margin-top: 10px;">
                    <img class="cl-img" src="/assets/images/custom/re/Check_out.png">
                </div>
                <label class="label cl-label">Result for 8S</label>
            </a>
        </div>
        <div class="row">
            <a onclick="popupSettingsLocationShift()">
                <div style="margin-top: 10px;">
                    <img class="cl-img" src="/assets/images/custom/re/settinglocation.png">
                </div>
                <label class="label cl-label">Settings</label>
            </a>
        </div>
    </div>
    <div class="col-md-11" style="text-align: center">
        <img style="width: 80%" src="/assets/images/custom/re/bg2.png">
    </div>
</div>



<!-- Modal -->
<div class="modal fade" id="myModalDownload" role="dialog">
    <div class="modal-dialog modal-md">
        <div class="modal-content" style="background: #2B2A2A">
            <div class="modal-header" style="padding: 6px 10px">
                <button type="button" class="close quit-modal" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" style="color: #fff">DAILY CHECK LIST</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <!-- <button type="button" class="buttonOK btn btn-default" data-dismiss="modal"
                        onclick="chooseTime()">OK</button> -->
                    <ul class="nav nav-tabs tabchoose">
                        <li class="active"><a href="#tab1" data-toggle="tab"><b>Check List by Daily</b></a></li>
                        <li><a href="#tab2" data-toggle="tab"><b>Leader Confirm</b></a></li>
                    </ul>
                </div>
                <div class="row" style="font-size: 16px;">
                    <div class="tab-content clearfix" style="color: #fff">
                        <div class="tab-pane active" id="tab1">
                            <div class="tab-allowner ownerDaily">
                                <form class="form-horizontal">
                                    <!-- <div class="row">
                                        <label class="control-label col-sm-2">Owner</label>
                                        <div class="col-sm-7 divInput">
                                            <input type="text" class="formInput" id="ownerID">
                                        </div>
                                    </div> -->
                                    <div class="row">
                                        <label class="control-label col-sm-2">Owner</label>
                                        <div class="col-sm-7 divInput">
                                            <input type="text" class="formInput" id="ownerID">
                                        </div>

                                        <div class="col-sm-3 divInput">
                                            <button type="button" class="btn btLogin btn-primary"
                                                onclick="loginOwnerAll('/re/checklist/daily')" data-dismiss="modal"
                                                style="padding: 6px 20px;">Login</button>
                                        </div>
                                    </div>


                                </form>
                            </div>
                        </div>
                        <div class="tab-pane" id="tab2">
                            <div class="tab-leader ownerDaily">
                                <form class="form-horizontal">

                                    <div class="row">
                                        <label class="control-label col-sm-2">Leader ID</label>
                                        <div class="col-sm-7 divInput">
                                            <input type="text" class="formInput" id="leaderConfirmId">
                                        </div>
                                    </div>
                                    <!-- <div class="row">
                                        <label class="control-label col-sm-2">Owner </label>
                                        <div class="col-sm-7 divInput">
                                            <input type="text" class="formInput" id="ownerConfirm">
                                        </div>
                                    </div> -->
                                    <div class="row">
                                        <label class="control-label col-sm-2">Owner</label>
                                        <div class="col-sm-7 divInput">
                                            <input type="text" class="formInput" id="ownerConfirm">
                                        </div>
                                        <div class="col-sm-3 divInput">
                                            <button type="button" class="btn btLogin btn-primary"
                                                onclick="loginOwnerLeader('/re/checklist/leaderconfirm')"
                                                data-dismiss="modal" style="padding: 6px 20px;">Login</button>
                                        </div>
                                    </div>

                                </form>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

        </div>
    </div>
</div>
<!-- Modal -->
<div class="modal fade" id="myModalAudit" role="dialog">
    <div class="modal-dialog modal-md">
        <div class="modal-content" style="background: #2B2A2A">
            <div class="modal-header" style="padding: 6px 10px">
                <button type="button" class="close quit-modal" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" style="color: #fff">8S AUDIT TEAM</h4>
            </div>
            <div class="modal-body" style="font-size: 16px">
                <div class="tab-leader ownerDaily">
                    <form class="form-horizontal" style="color: #fff;">
                        <div class="row">
                            <label class="control-label col-sm-2">Auditor </label>
                            <div class="col-sm-7 divInput">
                                <input type="text" class="formInput" id="auditor">
                            </div>
                        </div>
                        <!-- <div class="row">
                            <label class="control-label col-sm-2">Owner </label>
                            <div class="col-sm-7 divInput">
                                <input type="text" class="formInput" id="ownerAudit">
                            </div>
                        </div> -->
                        <div class="row">
                            <label class="control-label col-sm-2">Owner </label>
                            <div class="col-sm-7 divInput">
                                <input type="text" class="formInput" id="ownerAudit">
                            </div>
                            <div class="col-sm-3 divInput">
                                <button type="button" class="btn btLogin btn-primary"
                                    onclick="loginOwnerAudit('/re/checklist/auditor')" data-dismiss="modal"
                                    style="padding: 6px 20px;">Login</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <!-- <div class="modal-footer">
						<button type="button" class="btn btn-default col-lg-2" data-dismiss="modal">Close</button>
					</div> -->
        </div>
    </div>
</div>

<!-- Modal -->
<div class="modal fade" id="myModalSettingsLocation" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content" style="background: #2B2A2A">
            <div class="modal-header" style="padding: 6px 10px">
                <button type="button" class="close quit-modal" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" style="color: #fff">Setting location & shift</h4>
            </div>
            <div class="modal-body" style="font-size: 16px">
                <div class="tab-leader ownerDaily">
                    <form class="form-horizontal" style="color: #fff;">
                        <div class="display_Pass">
                            <div class="row">
                                <label class="control-label col-sm-2">Password </label>
                                <div class="col-sm-9 divInput">
                                    <input type="password" class="formInput" id="checkPassword"
                                        onkeypress="return checkKeyPress(event)">
                                </div>
                                <div class="col-sm-1 divInput">
                                    <button type="button" class="btn btn-primary"
                                        style="float: right; height: 30px; line-height: 0px;"
                                        onclick="checkSetingPassword()">OK</button>
                                </div>
                            </div>
                        </div>
                        <div class="display_Idcard">
                            <div class="row">
                                <label class="control-label col-sm-2">Id card </label>
                                <div class="col-sm-9 divInput">
                                    <input type="text" class="formInput" id="checkIdcard">
                                </div>
                                <div class="col-sm-1 divInput">
                                    <i class="fa fa-check iconCheck" aria-hidden="true"></i>
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-2">Full Name </label>
                                <div class="col-sm-9 divInput">
                                    <input type="text" class="formInput" id="fullName">
                                </div>
                            </div>
                            <div class="row">
                                <label class="control-label col-sm-2">Location </label>
                                <div class="col-sm-4 divInput">
                                    <select class="formInput" name="" id="chooseLocation" style="width: 100%;">

                                    </select>
                                </div>
                                <div class="col-sm-3 divInputCheck" style="margin-top: 10px;">
                                    <input class="checkDay" type="radio" name="checkdaynight" value="2"><span
                                        class="noidungC">Day</span>
                                    &nbsp;&nbsp;
                                    <input class="checkNight" type="radio" name="checkdaynight" value="1"><span
                                        class="noidungC">Night</span>

                                </div>
                                <div class="col-sm-3" style="margin-top: 10px;">
                                    <label class="switch" title="offline/online">
                                        <input id="checkOnline" type="checkbox" checked>
                                        <span class="slider round"></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <!-- <div class="row">
                            <div class="col-sm-3 divInput">
                                <button type="button" class="btn btLogin btn-primary"
                                    onclick="loginOwnerAudit('/re/checklist/auditor')" data-dismiss="modal"
                                    style="padding: 6px 20px;">Login</button>
                            </div>
                        </div> -->
                    </form>
                </div>
            </div>
            <div class="display_Idcard">
                <div class="modal-footer">
                    <span class="info-detail" style="float:left;" onclick="showDetailOwner()"><a
                            style="color: #fff;">Infomation detail
                            >>></a></span>
                    <button id="btn-save-update" type="button" class="btn btn-primary  col-lg-1" style="float: right;"
                        onclick="saveSettingsLocation()">Save</button>
                </div>
                <div class="modal-body list-detail hideTbl" style="padding-top: 0px; margin-top: 0px;">
                    <table class="table table-bordered" id="tblDetail"
                        style="text-align: center; background: #333; color: #fff; width: 75%; margin: auto;">
                        <thead>
                            <tr id="item">
                                <th style="width: 8%">STT</th>
                                <th style="width: 30%">Full Name</th>
                                <th style="width: 23%">ID Card</th>
                                <th>Location</th>
                                <th>Shift</th>
                                <!-- <th>Action</th> -->
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    .switch {
        position: relative;
        display: inline-block;
        width: 55px;
        height: 30px;
    }

    .switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }

    .slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #ccc;
        -webkit-transition: .4s;
        transition: .4s;
    }

    .slider:before {
        position: absolute;
        content: "";
        height: 23px;
        width: 23px;
        left: 4px;
        bottom: 4px;
        background-color: white;
        -webkit-transition: .4s;
        transition: .4s;
    }

    input:checked+.slider {
        background-color: #43a047;
    }

    input:focus+.slider {
        box-shadow: 0 0 1px #2196F3;
    }

    input:checked+.slider:before {
        -webkit-transform: translateX(26px);
        -ms-transform: translateX(26px);
        transform: translateX(26px);
    }

    /* Rounded sliders */
    .slider.round {
        border-radius: 34px;
    }

    .slider.round:before {
        border-radius: 50%;
    }

    /* dajhdjkahdkad */
    .info-detail {
        color: #fff;
        float: left;
        font-size: 15px;
        font-weight: 500;

    }

    .showTbl {
        display: block;
        max-height: 400px;
        display: inline-block;
        overflow: auto;
        margin-bottom: 15px;
    }

    .hideTbl {
        display: none;
    }

    #item th {
        text-align: center;
    }

    #item {
        background: #555252;
    }

    .noidungC {
        padding: 10px;
    }

    #header {
        text-align: center;
        margin-top: 1px;
        margin-bottom: 5px;
        background: #333;
        color: #ccc;
    }

    .divInput {
        padding: 8px;
    }

    .loginOwner {
        background: #333 !important;
        line-height: 25px !important;
        width: 70px !important;
        text-align: center !important;
    }

    .formInput {
        border: 1px solid #8e8888;
        background: #525252;
        padding: 2px 10px;
        width: 100%;
        border-radius: 5px;
    }

    .control-label {
        text-align: center;
    }

    .tabchoose {
        text-align: center;
        width: 100%;
    }

    .tabchoose li {
        width: 50%;
    }

    .tabchoose li a {
        border-radius: 5px 5px 0px 0px;
    }

    .ownerDaily {
        background: #333;
        padding: 5px 15px;
    }

    .col-5c {
        width: 20%;
        float: left;
        text-align: center;
    }

    .cl-img {
        width: 80px;
        height: 80px;
        cursor: pointer;
    }

    .d-img {
        width: 150px;
        height: 80px;
    }

    .quit-modal {
        right: -8px !important;
        top: -11px !important;
        color: #fff !important;
        z-index: 999;
        background: #0f112f !important;
        width: 18px;
        height: 18px;
        line-height: 16px;
        border-radius: 50%;
        opacity: 3 !important;
    }

    .iconCheck {
        padding: 2px;
        font-size: 22px;
        color: #09e51ad1;
    }



    /* CSS check box day night */
    .container {

        position: relative;
        padding-left: 35px;
        margin-bottom: 12px;
        cursor: pointer;
        font-size: 22px;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    /* Hide the browser's default radio button */
    .container input {
        position: absolute;
        opacity: 0;
        cursor: pointer;
    }

    /* Create a custom radio button */
    .checkmark {
        position: absolute;
        top: 0;
        left: 0;
        height: 25px;
        width: 25px;
        background-color: #eee;
        border-radius: 50%;
    }

    .display_Idcard {
        display: none;
    }

    /* On mouse-over, add a grey background color */
    .container:hover input~.checkmark {
        background-color: #ccc;
    }

    /* When the radio button is checked, add a blue background */
    .container input:checked~.checkmark {
        background-color: #2196F3;
    }

    /* Create the indicator (the dot/circle - hidden when not checked) */
    .checkmark:after {
        content: "";
        position: absolute;
        display: none;
    }

    /* Show the indicator (dot/circle) when checked */
    .container input:checked~.checkmark:after {
        display: block;
    }

    /* Style the indicator (dot/circle) */
    .container .checkmark:after {
        top: 9px;
        left: 9px;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: white;
    }
</style>
<script>
    function delay(callback, ms) {
        var timer = 0;
        return function () {
            var context = this, args = arguments;
            clearTimeout(timer);
            timer = setTimeout(function () {
                callback.apply(context, args);
            }, ms || 0);
        };
    }

    $('#checkIdcard').keyup(delay(function (e) {
        $.get("/api/re/8s/user/getconfig",
            {
                userId: this.value
            }, function (data, status) {
                var dt = data.data;
                console.log(dt)
                if (data.login && status == "success") {
                    $('.iconCheck').css('display', 'block');
                    var ml = '';
                    if (dt.shift == "DAY") {
                        $('.checkDay').attr('checked');
                        ml += ' <input class="checkDay" type="radio"' +
                            'name = "checkdaynight" value = "2" checked ><span class="noidungC">Day</span>' +
                            '<input class="checkNight" type="radio"' +
                            'name="checkdaynight" value="1"><span class="noidungC">Night</span>';
                    } else {
                        $('.checkNight').attr('checked');
                        ml += ' <input class="checkDay" type="radio"' +
                            'name = "checkdaynight" value = "2"><span class="noidungC">Day</span>' +
                            '<input class="checkNight" type="radio"' +
                            'name="checkdaynight" value="1" checked><span class="noidungC">Night</span>';
                    }
                    $('.divInputCheck').html(ml);
                    $('#chooseLocation').val(dt.location * 1);
                    $('#fullName').val(dt.fullName);
                    $('#btn-save-update').attr('typeData', 'updateData');
                    document.getElementById("checkOnline").checked = dt.status;

                } else {
                    // alert("Incorrect card code!");
                    $('#btn-save-update').attr('typeData', 'addData');
                }

            })

    }, 2000));
    showListLocation()
    function showListLocation() {
        $.get("/api/re/8s/location/list", function (data, status) {
            var html = '<option value="" disabled="" selected="">location</option>';
            data.forEach(e => {
                html += '<option value="' + e.id + '">' + e.location + '</option>';
            });
            $('#chooseLocation').html(html);
        })
    }
    function showShift() {
        $.get("/api/re/8s/shift/list", function (data, status) {
            //  console.log("data:", data);
            //console.log("status:", status);
        })
    }
    function getDataConfig() {
        $.get("/api/re/8s/user/getconfig",
            {
                userId: "V0958950"
            }, function (data, status) {
                console.log("data:", data);
                console.log("status:", status);

            })
    }

    function popupChooseOwnerChecklist() {
        $("#myModalDownload").modal('show');
    }
    function popupChooseAuditTeam() {
        $("#myModalAudit").modal('show');
    }

    function loginOwnerAll(link) {
        let owner = $('#ownerID').val();
        let location = $('#locationID').val();
        console.log(owner + "_____" + location)
        if (owner == null || owner == '') {
            alert("Please enter enough information!")
        } else {
            let url = link + "?owner=" + owner;
            openWindow(url);
        }
    }
    function loginOwnerLeader(link) {
        $('#myModalDownload').removeClass('in').css('display', 'none');
        let leaderConfirm = $('#leaderConfirmId').val(),
            owner = $('#ownerConfirm').val(),
            location = $('#locationConfirm').val();
        if (owner == null || leaderConfirm == null || owner == '' || leaderConfirm == '') {
            alert("Please enter enough information!")
        } else {
            let url = link + "?leader=" + leaderConfirm + "&owner=" + owner;
            openWindow(url);
        }
    }
    function loginOwnerAudit(link) {
        $('#myModalAudit').removeClass('in').css('display', 'none');
        let audit = $('#auditor').val(),
            owner = $('#ownerAudit').val(),
            location = $('#locationAudit').val();
        if (owner == null || audit == null || owner == '' || audit == '') {
            alert("Please enter enough information!")
        } else {
            let url = link + "?audit=" + audit + "&owner=" + owner;
            openWindow(url);
        }
    }
    function popupSettingsLocationShift() {
        $('#myModalSettingsLocation').modal('show');
        $('.iconCheck').css('display', 'none');
        $('.display_Pass').css('display', 'block');
        $('.display_Idcard').css('display', 'none');
        $('#checkPassword').val("");

    }
    function checkKeyPress(e) {
        if (e.keyCode == 13) { //Pess Enter
            checkSetingPassword();
        }
    }
    function checkSetingPassword() {
        $('#checkIdcard').val("");
        $('#chooseLocation').val("");
        $('#fullName').val("");
        var inputPass = $('#checkPassword').val();
        if (inputPass == "V0001050") {
            // alert("Success");
            $('.display_Idcard').css('display', 'block');
            $('.display_Pass').css('display', 'none');

        } else {
            alert("Please enter the password again!");
        }
        if ($('.list-detail').hasClass('showTbl')) {
            $('.list-detail').removeClass('showTbl');
            $('.list-detail').addClass('hideTbl');
        }

    }

    function saveSettingsLocation() {
        //  $('#myModalSettingsLocation').removeClass('in').css('display', 'none');
        var locationId = $('#chooseLocation').val();
        var shiftId = $('.checkDay')[0].checked;
        var shiftIdNight = $('.checkNight')[0].checked;
        var idUser = $('#checkIdcard').val();
        var fullName = $('#fullName').val();
        var typeData = $('#btn-save-update').attr('typeData');
        var typeDt;
        var statusOnOff = $('#checkOnline')[0].checked;

        if (typeData == "addData") {
            typeDt = 1; // Save Data
        } else {
            typeDt = 2; // update Data
        }
        var idShift;
        if (shiftId) {
            idShift = 2;
        } else if (shiftIdNight) {
            idShift = 1;
        }
        //    console.log(locationId, idShift, idUser, fullName, typeData);
        var valid = checkValue(locationId, idShift, idUser, fullName, typeData);
        //     console.log(valid)
        if (valid == "") {
            var dat = {
                type: typeDt,
                fullName: fullName,
                idShift: idShift,
                idLocation: locationId * 1,
                userId: idUser,
                status: statusOnOff
            }
            console.log("dat::", dat);
            //      return;
            $.ajax({
                type: 'POST',
                url: "/api/re/8s/user/saveupdateconfig",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(dat),
                success: function (data) {
                    console.log("dataaaa:", data)
                    if (data.update || data.save) {
                        listDataDeatilOwner();
                        alert("Save success!")
                    }
                },
                error: function () {
                    alert("Fail!");
                }
            });
        } else {
            alert(valid);
        }
    }
    listDataDeatilOwner()
    function listDataDeatilOwner() {
        $.get("/api/re/8s/user/listdatadetail", function (data, status) {
            console.log("data:", data);
            console.log("status:", status);
            if (status == "success") {
                var html = '';
                var i = 1;
                data.forEach(e => {
                    let location = e.location;
                    let shift = e.shift;
                    if (e.location == null || e.shift == null) {
                        location = '';
                        shift = '';
                    }
                    html += '<tr><td>' + i + '</td><td>' + e.fullName + '</td><td>' + e.user + '</td><td>' + location + '</td><td>' + shift + '</td></tr>';
                    i++;
                })
                $('#tblDetail>tbody').html(html);
            }
        })
    }
    function showDetailOwner() {
        if ($('.list-detail').hasClass('showTbl')) {
            $('.list-detail').removeClass('showTbl');
            $('.list-detail').addClass('hideTbl');
        } else {
            $('.list-detail').addClass('showTbl');
            $('.list-detail').removeClass('hideTbl');
        }
    }

    function checkValue(a, b, c, d, e) {
        var alert = "";
        if (a == '' || a == null) {
            alert = ("Please do not leave it bank !")
        } else if (b == '' || b == null) {
            alert = ("Please do not leave it bank !")
        } else if (c == '' || c == null) {
            alert = ("Please do not leave it bank !")
        } else if (d == '' || d == null) {
            alert = ("Please do not leave it bank !")
        } else if (e == '' || e == null) {
            alert = ("Please do not leave it bank !")
        }
        return alert;
    }


</script>