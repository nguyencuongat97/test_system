<link rel="stylesheet" href="/assets/custom/css/nbb/style.css" />
<style>
    .table caption {
        color: #ffffff;
        font-size: 20px;
    }
    
    .table tr th {
        background-color: #F79646;
        color: #272727;
        text-align: center;
    }
    
    .table tr th {
        font-size: 16px;
        padding: 2px 5px !important;
    }
    
    .table tr td {
        font-weight: bold;
        padding: 5px 5px !important;
    }
    
    .noData {
        background: #e75849;
        color: #333;
    }
    
    .sss {
        float: left;
        font-size: 18px;
        font-weight: bold;
        margin-top: 5px;
    }
</style>

<div class="loader">Loading&#8230;</div>
<div class="panel panel-re panel-flat row" style="background-color: #272727; text-align: center; color: #ccc; margin-bottom: 0px;">
    <div class="col-lg-12">
        <div class="panel panel-overview" id="header">
            <b><span id="titlePage"></span><span class="text-uppercase"> ${factory}</span><span> WIP</span></b>
        </div>
        <div class="row no-margin no-padding">
            <div class="col-md-2 col-xs-3 no-padding selectCustomer">
                <div class="dropdown dropdown-select-customer">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectCustomer" data-toggle="dropdown" aria-expanded="true">
                        Vento
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-customer table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectCustomer"></ul>
                </div>
            </div>

            <div class='drlMenu selectStage disableSelect'>
                <div class="dropdown dropdown-select-stage">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectStage" data-toggle="dropdown" aria-expanded="true">
                        Select Stage
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-stage table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectStage">

                    </ul>
                </div>
            </div>

            <div class='drlMenu selectModel disableSelect'>
                <div class="dropdown dropdown-select-model">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectModel" data-toggle="dropdown" aria-expanded="true">
                        Select Model
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-model table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectModel">

                    </ul>
                </div>
            </div>

            <div class='drlMenu selectMO'>
                <div class="dropdown dropdown-select-mo">
                    <button style="background: #333; color:#FFF" class="btn btn-default dropdown-toggle" type="button" id="drlSelectMO" data-toggle="dropdown" aria-expanded="true">
                        Select MO
                        <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu select-mo table-responsive pre-scrollable" role="menu" aria-labelledby="drlSelectMO">

                    </ul>
                </div>
            </div>
        </div>
        <div class="row" style="margin: unset;" id="table-con"></div>
    </div>
</div>
<iframe id="txtArea1" style="display:none"></iframe>

<script type="text/javascript" src="/assets/custom/js/nbb/loadSelection.js"></script>
<script>
    var dataset = JSON.parse(window.localStorage.getItem('dataset'));
    if (dataset == null || dataset == undefined || dataset.path != "${path}") {
        dataset = {
            customer: "Vento",
            stage: "",
            modelName: "",
            mo: "",
            path: "${path}",
            factory: "${factory}"
        }
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    }

    init();

    function init() {
        $("#titlePage").html(dataset.customer.toUpperCase());
        loadDataByModel();
        loadSelectCustomer();
        loadSelectMO();
        $('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        if (dataset.mo == "" || dataset.mo == undefined) {
            $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        } else $('.dropdown-select-mo').find('.dropdown-toggle').html(dataset.mo + ' <span class="caret"></span>');
    }

    $('.dropdown-select-customer').on('click', '.select-customer li a', function() {
        dataset.customer = $(this).html();

        //Adds active class to selected item
        $(this).parents('.select-customer').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
        // dataset.modelName = "";
        // dataset.stage = "";
        dataset.mo = "";
        $('.dropdown-select-mo').find('.dropdown-toggle').html('Select MO <span class="caret"></span>');
        $("#titlePage").html(dataset.customer.toUpperCase());
        $(".loader").removeClass("disableSelect");
        init();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    $('.dropdown-select-mo').on('click', '.select-mo li a', function() {
        var target = $(this).html();
        if (target == "Select All") {
            dataset.mo = "";
        } else dataset.mo = target;

        //Adds active class to selected item
        $(this).parents('.select-mo').find('li').removeClass('active');
        $(this).parent('li').addClass('active');

        //Displays selected text on dropdown-toggle button
        $(this).parents('.dropdown-select-mo').find('.dropdown-toggle').html(target + ' <span class="caret"></span>');
        $(".loader").removeClass("disableSelect");
        loadDataByModel();
        window.localStorage.setItem('dataset', JSON.stringify(dataset));
    });

    function loadDataByModel() {
        clearTimeout(dataset.timeout);
        $.ajax({
            type: "GET",
            url: "/api/test/" + dataset.factory + "/group/wipByModel",
            data: {
                customer: dataset.customer,
                mo: dataset.mo
            },
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                $("#table-con").html("");

                var dataStage = ["SMA", "FAT", "SUB-PACK"];

                for (i in dataStage) {
                    var html = '<div class="col-sm-4" style="color: #ccc; padding: 5px;">';
                    html += '<table class="table table-xxs table-bordered" style="text-align: center">';
                    html += '<caption>' + dataStage[i] + '</caption>';
                    html += '<tr><th>Model Name</th><th>MO</th><th>Target Qty</th><th>Total Input</th><th>Stockin Qty</th><th>Link Qty</th><th>Unlink Qty</th></tr>'
                    if (data[dataStage[i]] == null || data[dataStage[i]] == undefined) {
                        html += '<tr><td colspan="5">NO DATA!</td></tr>';
                    } else {
                        for (j in data[dataStage[i]]) {
                            html += '<tr><td>' + data[dataStage[i]][j].modelName + '</td><td>' + data[dataStage[i]][j].mo + '</td><td>' + data[dataStage[i]][j].plan + '</td><td>' + data[dataStage[i]][j].wip + '</td><td>' + (data[dataStage[i]][j].pass + data[dataStage[i]][j].fail) + '</td>' +
                                '<td>' + data[dataStage[i]][j].pass + '</td><td>' + data[dataStage[i]][j].fail + '</td></tr>';
                        }
                    }

                    $("#table-con").append(html);
                }
                $(".loader").addClass("disableSelect");
                $("#table-con").append("<div id='phantomjsMark'></div>");
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
            complete: function() {
                dataset.timeout = setTimeout(loadDataByModel, 30000, dataset.shiftType, dataset.workDate, dataset.customer, dataset.stage, dataset.modelName);
            }
        });
    }

    // function loadDataByModel(){
    //     clearTimeout(dataset.timeout);
    //     $.ajax({
    //         type: "GET",
    //         url: "/api/test/nbb/group/wipByModel",
    //         data: {
    //             customer: dataset.customer,
    //             mo: dataset.mo
    //         },
    //         contentType: "application/json; charset=utf-8",
    //         success: function(data){
    //             $("#table-con").html("");

    //             var dataStage = ["SMA", "FAT", "SUB-PACK"];

    //             for(i in dataStage){
    //                 var html = '<div class="col-sm-4" style="color: #ccc; padding: 5px;">';
    //                     html += '<table class="table table-xxs table-bordered" style="text-align: center">';
    //                     html += '<caption>'+dataStage[i]+'</caption>';
    //                     html += '<tr><th>Model Name</th><th>Total Input</th><th>Stockin Qty</th><th>Link Qty</th><th>Unlink Qty</th></tr>'
    //                 if(data[dataStage[i]] == null || data[dataStage[i]] == undefined){
    //                     html += '<tr><td colspan="5">NO DATA!</td></tr>';
    //                 }
    //                 else{
    //                     for(j in data[dataStage[i]]){
    //                         html += '<tr><td>'+j+'</td><td>'+data[dataStage[i]][j].plan+'</td><td>'+data[dataStage[i]][j].wip+'</td>'
    //                              +  '<td>'+data[dataStage[i]][j].pass+'</td><td>'+data[dataStage[i]][j].fail+'</td></tr>';
    //                     }
    //                 }

    //                 $("#table-con").append(html);
    //             }   
    //             $(".loader").addClass("disableSelect");
    //             $("#table-con").append("<div id='phantomjsMark'></div>");  
    //         },
    //         failure: function(errMsg) {
    //              console.log(errMsg);
    //         },
    //         complete: function() {
    //            dataset.timeout = setTimeout(loadDataByModel, 30000, dataset.shiftType, dataset.workDate, dataset.customer, dataset.stage, dataset.modelName);
    //         }
    //     });
    // }


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
            tab = document.getElementById('tblDetail'); // id of table

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
                sa = txtArea1.document.execCommand("SaveAs", true, "NBB WIP().xls");
            } else //other browser not tested on IE 11
                sa = window.open('data:application/vnd.ms-excel,' + encodeURIComponent(tab_text));
            return (sa);
        } else {
            var blobURL = tableToExcel('tblDetail', 'uph_table');
            $(this).attr('download', 'NBB WIP (' + dataset.customer + ', ' + dataset.stage + ', ' + dataset.model + ',).xls')
            $(this).attr('href', blobURL);
        }


    });
</script>