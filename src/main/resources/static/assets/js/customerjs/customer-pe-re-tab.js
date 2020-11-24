var dataGlo={};
function loadTableList(){
    $.ajax({
        type: "GET",
        url: "/api/test/station/error/top3/detail",
        data: {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan,
            customer: true
        },
        contentType: "application/data; charset=utf-8",
        success: function (data) {
             console.log("loadTableList:::",data);
            if (!$.isEmptyObject(data)) {
               $("#tbl-top-3-pe-issue>tbody").html('');
               var tbodyTop ='';
               var stt = 1;
               var idstt = 1;
            //    var totalFail = 0;
               
               var check = true;
                for(var key in data){
                   if(data.hasOwnProperty(key)){
                    var element = data[key];
                    var valueRows = data[key].length;
                    var keyUbee = key;
                    var keyModel;
                    if (check) {
                        loadHighChartWeekAllGroupName(data[key][0].modelName);
                    }
                    for(var j in data[key]){
                        keyModel = data[key][j].modelName;    
                    }
                    tbodyTop += '<tr class="sticky-colum">'
                             +'<td rowspan="'+valueRows+'">' + stt + '</td>'
                             +'<td rowspan="'+valueRows+'" id="ubee'+ stt +'">' + keyUbee + '</td>'
                             +'<td class=" class-key-model valueModel'+idstt+'" rowspan="'+valueRows+'"><a id="idModel'+ stt +'" class= "model-link'+idstt+'" href="#" onclick="loadHighChartWeekAllGroupName(\''+keyModel+'\')">' + keyModel + '</a></td>';
                        var numberFail = 0;
                        for (var index = 0; index < element.length; index++) {
                            var e = element[index];
                            var defectrate =0;
                            if (e.pass == '' || e.pass == null) {
                                defectrate = 0;
                            }else{
                                defectrate = (e.testFail)*100 /(e.pass);
                            } 
                            var tmpRC, tmpA, tmpD, tmpO, tmpS, tmpAt;
                            tmpRC = checkNull(e.rootCause);
                            tmpA = checkNull(e.action);
                            tmpD = checkNull(e.duedate);
                            tmpO = checkNull(e.owner);
                            tmpS = checkNull(e.status);
                            tmpAt = checkNull(tmpAt);
                            numberFail++;

                            tbodyTop += '<td class="class-columns">' + numberFail + '</td>'
                                        +'<td class="valueError'+idstt+'">' + e.errorCode + '</td>'
                                        +'<td class="valueModelName'+idstt+' hidden">' + keyModel + '</td>'
                                        +'<td class="class-columns">' + e.testFail + '</td>'
                                        +'<td class="class-columns">' + defectrate.toFixed(2) + '%</td>'

                                        + '<td class="class-textarea"><textarea id="txtEditRC'+idstt+'" class="form-control hidden" data-idt="'+e.idNoteError+'" name=""  type="text" oninput="textwrite('+idstt+')">'+ tmpRC +'</textarea>'
                                        +   '<span id="rootCause'+idstt+'" value="'+ tmpRC +'">'+tmpRC+'</span></td>'
                                       
                                        +   '<td class="class-textarea">'
                                        +   '<input id="txtEditAttach'+idstt+'" type="file" class="file-input hidden " name="uploaded-file" data-show-preview="false" data-show-upload="false" data-browse-class="btn btn-xs" data-remove-class="btn btn-xs" accept="image/*">'
                                        +   '<span id="attach'+idstt+'">'+tmpAt+'</span>'
                                        +   '</td>'
                                      
                                        +   '<td class="class-textarea"><textarea id="txtEditAction'+idstt+'" oninput="textwrite('+idstt+')" class="form-control hidden" name="" type="text">'+tmpA+'</textarea>'
                                        +   '<span id="action'+idstt+'" value="'+ tmpA +'">'+tmpA+'</span></td>'
                                        +   '<td class="class-textarea">'
                                        +       '<div id ="txtDue'+idstt+'" class="input-group hidden">'
                                        +            '<input type="text" id="txtEditD'+idstt+'" class=" form-control daterange-single" value="'+tmpD+'" side="right" name="duedate">'
                                        +        '</div>'
                                        +        '<div id="idErrorDueDate'+idstt+'" ><span id="errorDueDate'+idstt+'" value="'+ tmpD +'">'+tmpD+'</span></div>'
                                        +   '</td>'

                                        +   '<td class="class-textarea"><textarea id="txtOwner'+idstt+'" class="form-control hidden" name="" type="text">'+tmpO+'</textarea>'
                                        +   '<span id="errorOwner'+idstt+'" value="'+ tmpO +'">'+tmpO+'</span></td>'
                                      
                                        +   '<td class="class-textarea">'
                                        +   '<select id="txtStatus'+idstt+'" class="form-control hidden select-textarea" name = "SelectStatus">'
                                            +   '<option value = "ON_GOING">On Going</option>'
                                            +   '<option value = "PENDING">Pending</option>'
                                            +   '<option value = "DONE">Done</option>'
                                            +   '<option value = "CLOSE">Close</option>'
                                        +   '</select>'
                                        +   '<span id="errorStatus'+idstt+'" value="'+tmpS+'">'+tmpS+'</span></td>';
                                
                            tbodyTop+=   '<td class="class-button">'
                                    +       '<button class="btn btn-warning" id="editRow'+idstt+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+idstt+')">'
                                    +             '<i class="icon icon-pencil"></i>'
                                    +       '</button>'                     
                                    +       '<button class="btn btn-success hidden" id="confirmRow'+idstt+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel('+idstt+')">'
                                    +             '<i class="icon icon-checkmark"></i>'
                                    +       '</button><button class="btn btn-danger hidden" id="cancelRow'+idstt+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+idstt+')">'
                                    +             '<i class="icon icon-spinner11"></i>'
                                    +       '</button>'
                                    +   '</td>'
                                    +   '</tr>';
                            idstt++;
                        }
                    }
                    stt++;
                    check = false;
               }
               $('#tbl-top-3-pe-issue>tbody').html(tbodyTop);
                $('.daterange-single').daterangepicker({
                    singleDatePicker: true,
                    opens: "right",
                    applyClass: 'bg-slate-600',
                    cancelClass: 'btn-default',
                    locale: {
                        format: 'YYYY/MM/DD'
                    }
                });

            } else {
                $('#tbl-top-3-pe-issue>tbody').html('<tr><td colspan="14" align="center">-- NO ERROR --</td></tr>');
            }
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}
function textwrite(stt){
    var editRC = $('#txtEditRC'+stt).val().replace(/\r\n|\r|\n/g,"<br />");
    var editD = $('#txtEditD'+stt).val();
    var editA  = $('#txtEditAction'+stt).val().replace(/\r\n|\r|\n/g,"<br />");
    var editO = $('#txtOwner'+stt).val().replace(/\r\n|\r|\n/g,"<br />");
    var editS  = $('#txtStatus'+stt).val();
    var editAt  = $('#txtEditAttach'+stt).val();
    console.log(editRC);
}
// $('select[name=SelectStatus]').on('change', function () {
//     var select ;
//     if (select == "ON_GOING") {
        
//     }
// });
function checkNull(text){
    var result ="";
    if (text !="null" && text != null) {
        result = text;
    } 
    return result;
}
function cancelEdit(stt) {
    $("#editRow" + stt).removeClass("hidden");
    $("#confirmRow" + stt).addClass("hidden");
    $("#cancelRow" + stt).addClass("hidden");

    $("#rootCause" + stt).removeClass("hidden");
    $("#txtEditRC" + stt).addClass("hidden");

    $("#action" + stt).removeClass("hidden");
    $("#txtEditAction" + stt).addClass("hidden");

    $("#idErrorDueDate" + stt).removeClass("hidden");
    $("#txtDue" + stt).addClass("hidden");

    $("#errorOwner" + stt).removeClass("hidden");
    $("#txtOwner" + stt).addClass("hidden");

    $("#attach" + stt).removeClass("hidden");
    $("#txtEditAttach" + stt).addClass("hidden");

    $("#errorStatus" + stt).removeClass("hidden");
    $("#txtStatus" + stt).addClass("hidden");

    $(".btn-warning").removeAttr("disabled");
}
function edit(stt) {
    $("#editRow" + stt).addClass("hidden");
    $("#confirmRow" + stt).removeClass("hidden");
    $("#cancelRow" + stt).removeClass("hidden");

    $("#rootCause" + stt).addClass("hidden");
    $("#txtEditRC" + stt).removeClass("hidden");

    $("#action" + stt).addClass("hidden");
    $("#txtEditAction" + stt).removeClass("hidden");

    $("#idErrorDueDate" + stt).addClass("hidden");
    $("#txtDue" + stt).removeClass("hidden");

    $("#attach" + stt).addClass("hidden");
    $("#txtEditAttach" + stt).removeClass("hidden");

    $("#errorOwner" + stt).addClass("hidden");
    $("#txtOwner" + stt).removeClass("hidden");

    $("#errorStatus" + stt).addClass("hidden");
    $("#txtStatus" + stt).removeClass("hidden");

    $(".btn-warning").attr("disabled", "disabled");

    $('.daterange-single').daterangepicker({
        singleDatePicker: true,
        opens: "right",
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        locale: {
            format: 'YYYY/MM/DD'
        }
    });
    if($('input[name="duedate'+stt+'"]').val() == 'Invalid date'){
        $('input[name="duedate'+id+'"]').data('daterangepicker').setStartDate(new Date());
    }
}
function confirmModel(stt){
    console.log("stt:", stt);
    var editRC, editA, editD, editS, editO, editAt;
    var errorC, errorM;
    errorM =  $('.valueModelName'+stt).html();
    errorC =  $('.valueError'+stt).html();
    editRC = $('#txtEditRC'+stt).val().replace(/\r\n|\r|\n/g,"<br />");
    editD = $('#txtEditD'+stt).val();
    console.log("txtEditDate:", editD);

    editA  = $('#txtEditAction'+stt).val().replace(/\r\n|\r|\n/g,"<br />");
    editO = $('#txtOwner'+stt).val();
    console.log("txtOwner", editO);

    editS  = $('#txtStatus'+stt).val();
    console.log("txtStatus", editS);
    // editAt = $('#txtAttach'+stt).val();
 
    var dd = $('#txtEditRC' + stt);
    var id = dd[0].dataset.idt * 1;
        
    data = new FormData();
    data.append('factory', dataset.factory);
    data.append('modelName', errorM);
    data.append('timeSpan',dataset.timeSpan);
    data.append('action', editA);
    data.append('dueDate', editD);
    data.append('rootCause', editRC);
    data.append('owner', editO);
    data.append('status', editS);
    data.append('error', errorC);
    data.append('id', id);
    
    file = $('input[name=uploaded-file]').get(0);
    if (file.files.length > 0) {
        data.append('uploadedFile', file.files[0]);
    }
    console.log('FormData:',data);
   
    $.ajax({
        type: 'POST',
        url: '/api/test/station/noteError',
        data: data,
        processData: false,
        contentType: false,
        mimeType: "multipart/form-data",
        success: function(response){
            console.log("DATAf: ",response);
            // loadDataTableTop3ErrorFromJson();
            loadTableList();
            // $("#rootCause"+stt).html(editRC);
            // $("#action"+stt).html(editA);
            // $("#errorDueDate"+stt).html(editD);
            // $("#errorStatus"+stt).html(editS);
            // $("#errorOwner"+stt).html(editO);
            // $("#attach"+stt).html(file);
            // $(".btn-warning").removeAttr("disabled");
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
    });
}