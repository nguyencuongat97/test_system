<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<style>
    #tblOutput tr th {
        background-color: #F79646;
        color: #272727;
        text-align: center;
    }
    
    #txtPager {
        width: 60px;
        color: #ccc;
        float: right;
        border: unset;
        margin-left: 5px;
    }
    
    .btn-pager {
        background: #333333;
        color: #ccc;
        float: right;
        margin-left: 5px;
    }
    
    .btn-pager:hover {
        background: #222222;
        color: #ccc;
    }
</style>

<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span id="titlePage" class="disableSelect"></span><span class="text-uppercase">${factory}</span><span> COMPARE DATA SFIF AND TEST DATA</span></b>
        </div>
        <div class="row" style="margin: unset; padding: unset">
            <div class="drlMenu" style="margin: unset; width: 20%">
                <div class="panel panel-overview input-group" style="margin-bottom: 5px; background: #333333;">
                    <span class="input-group-addon" style="padding: 0px 5px; color: #fff;"><i class="icon-calendar22"></i></span>
                    <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="border-bottom: 0px !important; color: #fff;">
                </div>
            </div>
            <div class='drlMenu selectCustomer'>
                <div class="dropdown dropdown-select-customer">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                        Vento
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer">
                        <li role="presentation"><a role="menuitem" tabindex="-1" href="#" name="V71">Vento</a></li>
                        <li role="presentation"><a role="menuitem" tabindex="-1" href="#" name="M71">Mistral</a></li>
                    </ul>
                </div>
            </div>
            <div class="col-sm-1 export pull-right">
                <a class="btn btn-lg" id="btnExport" style="font-size: 13px; color: #ccc">
                    <i class="fa fa-download"></i> EXPORT
                </a>
            </div>
        </div>
        <div class="row" style="margin: unset;">
            <div class="col-sm-12 table-responsive pre-scrollable" style="max-height: calc(100vh - 260px);color: #ccc; padding: unset;">
                <table id="tblOutput" class="table table-xxs table-bordered table-sticky" style="text-align: center">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th style="width: 200px">DATETIME</th>
                            <th style="width: 125px">SN</th>
                            <th style="width: 120px">MO</th>
                            <th style="width: 160px">SFIS DSN</th>
                            <th style="width: 160px">TEST DSN</th>
                            <th style="width: 80px">RESULT DSN</th>
                            <th style="width: 100px">SFIS COUNTRY</th>
                            <th style="width: 100px">TEST COUNTRY</th>
                            <th style="width: 100px">RESULT COUNTRY</th>
                            <th>SFIS QR</th>
                            <th>TEST QR</th>
                            <th>RESULT QR</th>
                            <th>LANG</th>
                            <th>TOTAL RESULT</th>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
            <div class="row" style="margin: unset;">
                <a class="btn btn-lg" onclick="loadMore()" style="font-size: 13px; color: #fff">
                     Load More <span id="numberOfPage"></span>
                </a>
            </div>
        </div>
    </div>
</div>
<iframe id="txtArea1" style="display:none"></iframe>

<script>
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if (dataset == null || dataset == undefined || dataset.path != "${path}") {
        dataset = {
            countryCode: "V71",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }

    var isOpening = false;

    function getTimeNow() {
        $.ajax({
            type: "GET",
            url: "/api/time/now",
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                dataset.startTime = data.slice(0, 10) + " 07:30";
                dataset.endTime = data.slice(0, 10) + " 11:30";
                window.localStorage.setItem('dataset', JSON.stringify(dataset));

                var startDate = new Date(dataset.startTime);
                var endDate = new Date(dataset.endTime);
                isOpening = false;
                $('.datetimerange').data('daterangepicker').setStartDate(startDate);
                $('.datetimerange').data('daterangepicker').setEndDate(endDate);

                var target = "Vento";
                if ("V71" == dataset.countryCode) {
                    target = "Vento";
                } else if ("M71" == dataset.countryCode) {
                    target = "Mistral";
                }
                $('.dropdown-select-customer').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');

                loadData();
                isOpening = true;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }

    $('.dropdown-select-customer').on('click', '.select-customer li a', function() {
        var target = $(this).html();
        dataset.countryCode = $(this).attr("name");

        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");
        loadData();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });


    function loadData() {
        dataset.page = 0;
        var data = {
            countryCode: dataset.countryCode,
            startTime: dataset.startTime,
            endTime: dataset.endTime,
        }

        $.ajax({
            type: 'POST',
            url: 'http://10.224.81.103:2017/api/values/nbbguobiebidui',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                dataset.totalOfPage = data.totalNum;
                var dataOfDetail = data.data;
                $("#tblOutput tbody").html("");
                var html = '';
                if (!$.isEmptyObject(dataOfDetail)) {
                    var stt = 1;
                    for (i in dataOfDetail) {
                        html += '<tr class="disableSelect"><td>' + stt + '</td>' +
                            '<td>' + dataOfDetail[i].dateTime + '</td>' +
                            '<td>' + dataOfDetail[i].sn + '</td>' +
                            '<td>' + dataOfDetail[i].mo + '</td>' +
                            '<td>' + dataOfDetail[i].sfiS_DSN + '</td>' +
                            '<td>' + dataOfDetail[i].test_DSN + '</td>' +
                            '<td>' + dataOfDetail[i].result_DSN + '</td>' +
                            '<td>' + dataOfDetail[i].sfiS_Country + '</td>' +
                            '<td>' + dataOfDetail[i].test_Country + '</td>' +
                            '<td>' + dataOfDetail[i].result_Country + '</td>' +
                            '<td title="' + dataOfDetail[i].sfiS_QR + '">' + dataOfDetail[i].sfiS_QR + '</td>' +
                            '<td title="' + dataOfDetail[i].test_QR + '">' + dataOfDetail[i].test_QR + '</td>' +
                            '<td>' + dataOfDetail[i].result_QR + '</td>' +
                            '<td>' + dataOfDetail[i].lang + '</td>' +
                            '<td>' + dataOfDetail[i].total_Result + '</td></tr>';
                        stt++;
                    }
                    $("#tblOutput tbody").html(html);
                } else {
                    $("#tblOutput tbody").html("<tr><td colspan='15'><b>NO DATA</b></td></tr>");
                }

                loadMore();
            },
            failure: function(errMsg) {
                console.log(errMsg);
                $("#tblOutput tbody").html("");
            },
            complete: function() {
                $(".loader").addClass("disableSelect");
            }
        });
    }

    function loadMore() {
        dataset.page += 10;
        var rowCount = $("#tblOutput >tbody >tr");
        if (dataset.page > rowCount.length)
            dataset.page = rowCount.length;
        for (var i = 0; i < dataset.page; i++) {
            rowCount[i].className = "";
        }
        $("#numberOfPage").html("(" + dataset.page + "/" + dataset.totalOfPage + ")");
    }

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
    $("#btnExport").click(function() {
        var isIE = /*@cc_on!@*/ false || !!document.documentMode;
        if (isIE) {
            var tab_text = "<table border='2px'><tr bgcolor='#F79646'>";
            var textRange;
            var j = 0;
            tab = document.getElementById('tblOutput'); // id of table

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
                sa = txtArea1.document.execCommand("SaveAs", true, "Compare Data SFIS And Test Data.xls");
            } else //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        } else {
            var blobURL = tableToExcel('tblOutput', 'TEST DATA');
            $(this).attr('download', 'Compare Data SFIS And Test Data.xls')
            $(this).attr('href', blobURL);
        }
    });

    $(document).ready(function() {
        getTimeNow();
        $('input[name=timeSpan]').on('change', function() {
            if (isOpening != false) {
                var sT = this.value.slice(0, 16);
                var eT = this.value.slice(19, 35);
                dataset.startTime = sT;
                dataset.endTime = eT;

                window.localStorage.setItem('dataset', JSON.stringify(dataset));
                $(".loader").removeClass("disableSelect");
                loadData();
            }
        });
    });
</script>