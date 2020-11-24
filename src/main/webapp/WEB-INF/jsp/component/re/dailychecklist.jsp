<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div class="container-fluid" style="color: #fff">
    <div class="row" style="text-align: center">
        <h1><b>TIÊU CHUẨN THỰC HIỆN 8S TẠI KHU VỰC SỬA CHỮA RE</b></h1>
        <i style="font-size: 16px;">(Sàng lọc- Sắp xếp - Sạch sẽ - Săn sóc - Sẵn sàng - An toàn - Tiết Kiệm - Bảo
            mật)</i>
    </div>
    <div class="export pull-right" style="margin: 5px 0px 10px;">
        <a class="btn btn-lg btn-primary saveTT" onclick="saveCheckDaily()" style=" padding: 7px 15px; color: #fff">
            Save <i class="fa fa-check-circle" style="font-size: 16px;"></i>
        </a>
    </div>
    <div class="row">
        <table class="table table-bordered" id="tblDailyCheck" style="text-align: center">
            <thead>
                <tr id="item">
                    <th style="width: 8%">Hạng mục</th>
                    <th style="width: 1%">STT</th>
                    <th style="width: 60%">Các bước thực hiện</th>
                    <th style="width: 12%">Người phụ trách</th>
                    <th>Nhận xét</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="3"></td>
                    <td style="text-align: center">Select All <input type="checkbox" onchange="allCheck(this)"
                            name="checkBoxAll" value="1"></td>
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

    input[type='checkbox'] {
        width: 15px;
        height: 15px;
        top: 2px;
        position: relative;
        background-color: #ffffff;
        content: '';
        display: inline-block;
        visibility: visible;
        border: 2px solid #ffffff;
    }

    input[type='checkbox']:checked:after {
        width: 15px;
        height: 15px;
        top: -2px;
        left: -1px;
        position: relative;
        background-color: #04a026;
        content: '';
        display: inline-block;
        visibility: visible;
        border: 2px solid #ffffff;
    }

    .infoAc {
        position: absolute;
        top: 9px;
        font-size: 20px;
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
    var ow = getUrlParameter('owner');
    var loca = getUrlParameter('location');
    console.log(ow + "_______________" + loca)
    if ((ow != null) || (ow != '')) {
        // alert("yes")
        getDataDailyOwnerAll(ow)
    }

    function getDataDailyOwnerAll(owner) {
        // var owner1 = "owner_test";
        // var location1 = "ocation_test";
        var data = {
            owner: owner

        }
        $.ajax({
            url: "/api/re/8s/checklist/by/daily/getdata",
            data: data,
            dataType: "JSON",
            type: "GET"
        }).done(function (result) {
            console.log("result::", result)
            var str = "";
            let position = 0;
            var info = '<div class="infoAc"><span>' + result.owner + '</span> / <span>' + result.location + '</span></div>';
            $('.container-fluid').first().append(info);
            for (i in result.data) {
                var dataJ = result.data[i].list
                for (j in dataJ) {
                    str += "<tr>";
                    if (position == 0) {
                        str += '<th scope="row" rowspan="' + dataJ.length + '">' + result.data[i].title + '</th>';
                    }
                    str += '<td>' + dataJ[j].number + '</td>' +
                        '<td>' + dataJ[j].description + '</td>';

                    if (dataJ[j].confirm == 0) {
                        str += '<td><input type="checkbox" data-info=' + dataJ[j].id + ' class="cb_checked"></td>' +
                            '<td></td></tr>';
                    } else {
                        str += '<td><input type="checkbox" data-info=' + dataJ[j].id + ' class="cb_checked" checked></td>' +
                            '<td></td></tr>';
                    }
                    position++;
                }
                position = 0;
            }
            $('#tblDailyCheck>tbody').html(str);
        })
    }
    function saveCheckDaily() {
        var arr = [];
        var a = $('.cb_checked');
        var b, c, d;
        for (var i = 0; i < a.length; i++) {
            b = $('.cb_checked')[i].checked;
            c = $('.cb_checked')[i].dataset.info;
            if (b) {
                d = 1;
            } else {
                d = 0;
            }
            arr.push({ id: c, confirm: d });
        }
        $.ajax({
            type: 'POST',
            url: "/api/re/8s/checklist/by/daily/savedata",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(arr),
            success: function (data) {
                if (data) {
                    alert("Success");
                    getDataDailyOwnerAll(ow, loca);
                }
            },
            error: function () {
                alert("Fail!");
            }
        });
    }

    function allCheck() {
        var check = document.getElementsByName("checkBoxAll"),
            checkBox = document.getElementsByClassName("cb_checked");
        if (check[0].checked) {
            for (i = 0; i < checkBox.length; i++) {
                if (checkBox[i].type == "checkbox") {
                    if (!checkBox[i].checked) {
                        checkBox[i].checked = true;
                    }
                }//if
            }//for
        } else {
            for (i = 0; i < checkBox.length; i++) {
                if (checkBox[i].type == "checkbox") {
                    if (checkBox[i].checked) {
                        checkBox[i].checked = false;
                    }
                }//if
            }//for
        }
    }
</script>