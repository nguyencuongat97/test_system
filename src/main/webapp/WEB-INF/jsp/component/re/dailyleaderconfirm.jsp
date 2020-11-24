<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div class="container-fluid" style="color: #fff">
    <div class="row" style="text-align: center">
        <h1><b>TIÊU CHUẨN THỰC HIỆN 8S TẠI KHU VỰC SỬA CHỮA RE</b></h1>
        <i style="font-size: 16px;">(Sàng lọc- Sắp xếp - Sạch sẽ - Săn sóc - Sẵn sàng - An toàn - Tiết Kiệm - Bảo
            mật)</i>
    </div>
    <div class="export pull-right" style="margin: 5px 0px 10px;">
        <a class="btn btn-lg btn-primary saveTT" onclick="saveLeaderConfirm()" style=" padding: 7px 15px; color: #fff">
            Save <i class="fa fa-check-circle" style="font-size: 16px;"></i>
        </a>
    </div>
    <div class="row">
        <table class="table table-bordered" id="tblCheckLeader" style="text-align: center">
            <thead>
                <tr id="item">
                    <th style="width: 8%">Hạng mục</th>
                    <th style="width: 1%">STT</th>
                    <th style="width: 60%">Các bước thực hiện</th>
                    <th>Xác Nhận</th>
                    <th>Nhận xét</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
            <tfoot>
                <tr>
                    <td colspan="3"></td>
                    <td style="text-align: center">
                        <input type="radio" onchange="allCheckGoodBad(this)" name="radiocheck" value="3"
                            class="rbtGood">Tốt &nbsp;&nbsp;
                        <input type="radio" onchange="allCheckGoodBad(this)" name="radiocheck" value="4"
                            class="rbtBad">Kém
                    </td>
                    <td></td>
                </tr>
            </tfoot>
        </table>
    </div>
</div>


<style>
    .table {
        font-size: 16px;
    }

    .table tr th,
    .table tr td {
        text-align: center;
        padding: 3px 7px !important;
    }

    .table tr td:nth-of-type(2) {
        text-align: left;
    }

    .btn {
        transition: all 0.5s;
    }

    .btn:hover {
        transform: translateY(-2px);
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
    }

    .btn:active {
        outline: none;
        transform: translateY(-1px);
        box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
    }

    input[type='radio'] {
        margin-right: 3px;
    }

    input[type='radio'] {
        width: 15px;
        height: 15px;
        border-radius: 15px;
        top: 2px;
        position: relative;
        background-color: #cccccc;
        content: '';
        display: inline-block;
        visibility: visible;
        border: 2px solid #ffffff;
    }

    .rbtGood:checked:after {
        width: 15px;
        height: 15px;
        border-radius: 15px;
        top: -2px;
        left: -1px;
        position: relative;
        background-color: #04a026;
        content: '';
        display: inline-block;
        visibility: visible;
        border: 2px solid #fff;
    }

    .rbtBad:checked:after {
        width: 15px;
        height: 15px;
        border-radius: 15px;
        top: -2px;
        left: -1px;
        position: relative;
        background-color: #c21d33;
        content: '';
        display: inline-block;
        visibility: visible;
        border: 2px solid #fff;
    }

    .nhanXet {
        background: #272727;
        color: #fff;
        height: 100%;
        width: 100%;
        border: 0px solid #333;
    }

    .customPadding {
        padding: 0 !important;
    }

    .lcO {
        position: absolute;
        top: 9px;
        font-size: 16px;
        left: 15px;
        border: 1px solid #333;
        color: #fff;
        background: #525252;
        border-radius: 5px;
        padding: 7px;
    }
</style>

<script>
    var getUrlParameter = function getUrlParameter(sParam) {
        var sParamURL = window.location.search.substring(1),
            sURLVariables = sParamURL.split('&'),
            sParameterName,
            i;
        for (i = 0; i < sURLVariables.length; i++) {
            sParameterName = sURLVariables[i].split('=');
            if (sParameterName[0] === sParam) {
                return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1])
            }
        }
    };
    var lID = getUrlParameter('leader')
    var ow = getUrlParameter('owner');
    var loca = getUrlParameter('location');
    // console.log(lID + "______" + ow + "_______________" + loca)
    getDataDailyLeaderConfirm(lID, ow, loca);
    function getDataDailyLeaderConfirm(leaderId, owner, location) {
        //   var leaderId1 = "V0909765";
        //  var owner1 = "owner_test";
        //  var location1 = "ocation_test";
        var data = {
            leaderId: leaderId,
            owner: owner,
            location: location
        }
        $.ajax({
            url: "/api/re/8s/leaderconfirm/by/daily/getdata",
            data: data,
            dataType: "JSON",
            type: "GET"
        }).done(function (result) {
            console.log("resultLeader::", result)
            var str = "";
            let position = 0;
            var info = '<div class="lcO">Leader: ' + result.leaderId +
                ': <i><span>' + result.owner + '</span> - <span>' + result.location + '</span></i>' +
                '</div>';
            $('.container-fluid').first().append(info);
            for (i in result.data) {
                var dataJ = result.data[i].list
                for (j in dataJ) {
                    var content = dataJ[j].content;
                    if (dataJ[j].content == null) {
                        content = '';
                    }
                    str += "<tr>";
                    if (position == 0) {
                        str += '<th scope="row" rowspan="' + dataJ.length + '">' + result.data[i].title + '</th>';
                    }
                    str += '<td>' + dataJ[j].number + '</td>' +
                        '<td>' + dataJ[j].description + '</td>';
                    if (dataJ[j].confirm == 2) {
                        str += '<td> ' +
                            '   <div class="radio-group-fii" cardID="' + leaderId + '" data-name="' + dataJ[j].id + '"><input class="form-check-input-1 rbtGood" type="radio" name="' + dataJ[j].id + '" value="3">Tốt &nbsp;&nbsp;' +
                            '   <input class="form-check-input-2 rbtBad" type="radio" name="' + dataJ[j].id + '" value="4" checked>Kém' +
                            '</div></td>' +
                            '<td class="customPadding"><textarea class="nhanXet" cols="20" rows="2">' + content + '</textarea></td>' +
                            '</tr>';
                    } else if (dataJ[j].confirm == 3) {
                        str += '<td> ' +
                            '   <div class="radio-group-fii" cardID="' + leaderId + '" data-name="' + dataJ[j].id + '"><input class="form-check-input-1 rbtGood" type="radio" name="' + dataJ[j].id + '" value="3" checked>Tốt &nbsp;&nbsp;' +
                            '   <input class="form-check-input-2 rbtBad" type="radio" name="' + dataJ[j].id + '" value="4" >Kém' +
                            '</div></td>' +
                            '<td class="customPadding"><textarea class="nhanXet" cols="20" rows="2">' + content + '</textarea></td>' +
                            '</tr>';
                    } else {
                        str += '<td> ' +
                            '   <div class="radio-group-fii" cardID="' + leaderId + '" data-name="' + dataJ[j].id + '"><input class="form-check-input-1 rbtGood" type="radio" name="' + dataJ[j].id + '" value="3">Tốt &nbsp;&nbsp;' +
                            '   <input class="form-check-input-2 rbtBad" type="radio" name="' + dataJ[j].id + '" value="4" >Kém' +
                            '</div></td>' +
                            '<td class="customPadding"><textarea class="nhanXet" cols="20" rows="2">' + content + '</textarea></td>' +
                            '</tr>';
                    }

                    position++;
                }
                position = 0;
            }

            $('#tblCheckLeader>tbody').html(str);
        })
    }

    function saveLeaderConfirm() {
        var arr = [];
        var arrF = [];
        var e = $('.radio-group-fii');
        var f = $('.nhanXet');
        var d;
        var card = e.attr("cardID");
        for (let i = 0; i < e.length; i++) {
            let tmpData = e[i].dataset.name;
            let tmpE = e[i];
            if (tmpE.children[0].checked) {
                d = 3;
            } else {
                d = 2;
            }
            arr.push({ leaderId: card, id: tmpData, confirm: d });
        }
        for (let i = 0; i < f.length; i++) {
            let g = f[i].value;
            let valLeaderId = arr[i].leaderId;
            let valId = arr[i].id;
            let valConfirm = arr[i].confirm;
            arrF.push({ leaderId: valLeaderId, id: valId, confirm: valConfirm, description: g })
        }
        $.ajax({
            type: 'POST',
            url: "/api/re/8s/leaderconfirm/by/daily/savedata",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(arrF),
            success: function (data) {
                if (data) {
                    alert("Success");
                    getDataDailyLeaderConfirm(lID, ow, loca);
                }
            },
            error: function () {
                alert("Fail!");
            }
        });
    }
    function allCheckGoodBad() {
        var check = document.getElementsByName("radiocheck"),
            radios1 = document.getElementsByClassName("form-check-input-1"),
            radios2 = document.getElementsByClassName("form-check-input-2");
        if (check[0].checked) {
            for (i = 0; i < radios1.length; i++) {
                if (radios1[i].type == "radio") {
                    if (radios1[i].value == 3) {
                        radios1[i].checked = true;
                    }
                }//if
            }//for
        } else {
            for (i = 0; i < radios2.length; i++) {
                if (radios2[i].type == "radio") {
                    if (radios2[i].value == 4) {
                        radios2[i].checked = true;
                    }
                }//if
            }//for
        };//if
        return null;

    }



</script>