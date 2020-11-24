<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div class="container-fluid" style="color: #fff">
    <div class="row" style="text-align: center">
        <h1><b>TIÊU CHUẨN THỰC HIỆN 8S TẠI KHU VỰC SỬA CHỮA RE</b></h1>
        <i style="font-size: 16px;">(Sàng lọc- Sắp xếp - Sạch sẽ - Săn sóc - Sẵn sàng - An toàn - Tiết Kiệm - Bảo mật)</i>
    </div>
    <div style="padding-top: 5px">
        <table class="table table-bordered" id="tblTemplateChecklist" style="text-align: center">
            <thead>
                <tr id="item">
                    <th style="width: 8%">Hạng mục</th>
                    <th style="width: 1%">STT</th>
                    <th style="width: 60%">Các bước thực hiện</th>
                    <th>Ảnh tiêu chuẩn</th>
                    <th>Người phụ trách</th>
                    <th>Nhận xét</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</div>
<style>
    .table{
        font-size: 16px;
    }
    .table tr th{
        text-align: center;
    }
    .table tr td:nth-of-type(2){
        text-align: left;
    }
</style>
<script>
    getDataTemplateChecklist();
    function getDataTemplateChecklist() {
        $.ajax({
            url: "/api/re/8s/introduce/getdata",
            dataType: "JSON",
            type: "GET"
        }).done(function (result) {
            console.log("data:", result);
            var str = "";
            let position = 0;
            for (i in result) {
                var dataJ = result[i].stepsList
                for (j in dataJ) {
                    str += "<tr>";
                    if (position == 0) {
                        str += '<th scope="row" rowspan="' + dataJ.length + '">' + result[i].title + '</th>';
                    }
                    let roleName = "All";
                    if (dataJ[j].idRole * 1 == 2) {
                        roleName = "Leader";
                    }

                    str += '<td>' + dataJ[j].number + '</td>' +
                        '<td>' + dataJ[j].description + '</td>' +
                        '<td>Images</td>' +
                        '<td>' + roleName + '</td>' +
                        '<td></td></tr>';
                    position++;
                }
                position = 0;
            }

            $('#tblTemplateChecklist>tbody').html(str);
        })
    }
</script>