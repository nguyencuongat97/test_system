<%@ page contentType="text/html;charset=UTF-8" language="java"%>
    <link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
    <style>
        #tblOutput {
            table-layout: fixed;
            word-wrap: break-word;
            max-width: 15000px;
        }
        
        #tblOutput tr th {
            background-color: #F79646;
            color: #272727;
            text-align: center;
        }
        
        #tblOutput tr th,
        #tblOutput tr td {
            font-size: 16px;
        }
    </style>

    <div class="loader"></div>
    <div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
        <div class="col-lg-12">
            <div class="panel panel-overview" id="header">
                <b><span id="titlePage"></span><span> NBB SI Automatic Calling System</span></b>
            </div>
            <div class="row zui-wrapper" style="margin: unset;">
                <div class="col-sm-12" style="color: #ccc; padding: unset;">
                    <table id="tblOutput" class="table table-xxs table-bordered" style="text-align: center">
                        <thead>
                            <tr>
                                <th>優先級</th>
                                <th>線別</th>
                                <th style="width: 20%">工單</th>
                                <th style="width: 10%">料號</th>
                                <th>單個產品</th>
                                <th>標準在線數量</th>
                                <th>總需求數量 (pcs)</th>
                                <th>已發放料數量 (pcs)</th>
                                <th>已使用數量 (pcs)</th>
                                <th>在線剩餘數量 (pcs)</th>
                                <th>可生產時間 (min)</th>
                                <th>需補料數量</th>
                            </tr>
                            <tr>
                                <th>說明：</th>
                                <th>備料系統中已上線線別</th>
                                <th>備料系統中已上線工單</th>
                                <th>自動顯示</th>
                                <th>A=BOM中用量</th>
                                <th>B:需自定義</th>
                                <th>C：工單總需求數量</th>
                                <th>D:工單已發總數量</th>
                                <th>E：工單已使用總數量</th>
                                <th>F：F=D-E</th>
                                <th>G：G=F/B/A</th>
                                <th>缺料后固定量（固定量明細制定中）</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <script>
        loadData();

        function loadData() {
            $.ajax({
                type: 'GET',
                url: '/api/test/nbb/allpart/kitting',
                success: function(data) {
                    var tb = '';
                    var stt = 1;
                    for (i in data) {
                        var J = (data[i].deliverQty - data[i].checkoutQty);
                        var K = (J / data[i].standardQty / data[i].checkoutPH * 60).toFixed(2);
                        var L = data[i].checkoutPH - J;
                        var groupWO = data[i].groupWo;
                        if (data[i].groupWo.length > 25) {
                            groupWO = data[i].groupWo.slice(0, 25) + '...';
                        }
                        tb = '<tr><td>' + stt + '</td><td></td>' +
                            '<td title="' + data[i].groupWo + '">' + groupWO + '</td>' +
                            '<td>' + data[i].custKpNo + '</td>' +
                            '<td>' + data[i].standardQty + '</td>' +
                            '<td>' + data[i].checkoutPH + '</td>' +
                            '<td>' + data[i].woRequest + '</td>' +
                            '<td>' + data[i].deliverQty + '</td>' +
                            '<td>' + data[i].checkoutQty + '</td>' +
                            '<td>' + J + '</td>' +
                            '<td>' + K + '</td>' +
                            '<td>' + L + '</td>' +
                            '</tr>';
                        stt++;
                        $("#tblOutput tbody").append(tb);
                    }
                    $(".loader").addClass("disableSelect");
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });
        }
    </script>