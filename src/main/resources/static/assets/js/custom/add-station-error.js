var dataGlo ={};
var dataGroup = {};
loadModelList();
function loadModelList() {
    $('.loader').css('display', 'block');
    $.ajax({
        type: "GET",
        url: "/api/test/group/total",
        data: {
            factory: dataset.factory,
            timeSpan:dataset.timeSpan,
            modelName: dataset.modelName,
            includeStation: false
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var htmlTbody = '';
            var number= 0;
            // if(dataset.factory== 'B04'){
            //     for(var i = 0; i < data.length; i++ ){
            //     dataGlo.groupName = data[i].groupName;
            //     var rtr = data[i].retestRate;
            //     var fpr = data[i].firstPassRate;
            //     var ffail = data[i].firstFail;
            //     var sfail = data[i].secondFail;
            //     if (rtr < 0) {
            //         rtr = 0;
            //         sfail = ffail;
            //         fpr = (data[i].inPut - ffail) * 100.0 / data[i].inPut;
            //     }
            //     number++;
            //     htmlTbody += '<tr><td>'+ number +'</td>'
            //         + '<td>'+data[i].modelName+'</td>'
            //         + '<td class="text-semibold" title="Click to move Station Detail"><a class="group-name-detail" onclick=getGroup("'+  data[i].groupName +'") data-model="' + data[i].groupName + '" >'+data[i].groupName+'</a></td>'
            //         // + '<td>'+data[i].plan+'</td>'
            //         + '<td>'+data[i].wip+'</td>'
            //         + '<td>'+ data[i].pass+'</td>'
            //         + '<td class=" styletextshad ' + convertStatus(fpr, 90, 97) + '" data-toggle="modal" data-target="#modalSetup" onclick=showModal("' + data[i].groupName + '") title="Click to show Station"><a class="class-group-name">' + fpr.toFixed(2) + '%</a></td>'
            //         + '<td class=" styletextshad  ' + convertStatus((100 - rtr), 90, 95) + '" data-toggle="modal" data-target="#modalSetup" onclick=showModal("' + data[i].groupName + '") title="Click to show Station"><a class="class-group-name">'+ rtr.toFixed(2) +'%</a></td>'
            //         + '<td class=" styletextshad  ' + convertStatus(data[i].yieldRate, 95, 98) + '" data-toggle="modal" data-target="#modalSetup" onclick=showModal("' + data[i].groupName + '") title="Click to show Station"><a class="class-group-name">'+ data[i].yieldRate.toFixed(2) +'%</a></td>'
            //         + '<td>'+ data[i].firstFail+'</td>'
            //         + '<td>'+ data[i].secondFail+'</td>';

            //         var valueTop3 = Object.keys(data[i].errorMetaMap);
            //         if(Object.keys(valueTop3).length == 3) {
            //             // htmlTbody += '<td class="text-semibold">';
            //                 for(j in valueTop3){
            //                     var subString = valueTop3[j].length;
            //                     if (subString > 7) {
            //                         var str1 = valueTop3[j];

            //                         var str = str1.slice(0, 7) + '...';
            //                         htmlTbody +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+'</a></td>';
            //                     } else{
            //                         htmlTbody +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+'</a></td>';

            //                     }
            //                 }
            //                     htmlTbody +=  '<td class="text-semibold errortop"><a class="class-error" data-toggle="modal" data-target="#modalErrorGroup" onclick=showModalErrorDashboard("' + data[i].modelName + '","'+ data[i].groupName +'") title="Click to show Station Error Group">OTHER</a></td>';

            //         }else if(Object.keys(valueTop3).length == 2) {
            //             // htmlTbody += '<td class="text-semibold">';
            //             for(j in valueTop3){
            //                 var subString = valueTop3[j].length;
            //                 if (subString > 7) {
            //                     var str1 = valueTop3[j];
            //                     var str = str1.slice(0, 7) + '...';
            //                     htmlTbody += '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+' </a></td>';
            //                 } else{
            //                     htmlTbody += '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+' </a></td>';
            //                 }
            //             } 
            //             htmlTbody += '<td class="text-semibold errortop"></td><td class="text-semibold errortop"></td>';
            //             // htmlTbody += '</td>';
            //         } else if(Object.keys(valueTop3).length == 1){
            //             for(j in valueTop3){
            //                 var subString = valueTop3[j].length;
            //                 if (subString > 7) {
            //                     var str1 = valueTop3[j];
            //                     var str = str1.slice(0, 7) + '...';
            //                     htmlTbody += '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+' </a></td>';
            //                 } else{
            //                     htmlTbody += '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+' </a></td>';
            //                 }
            //             } 
            //             htmlTbody += '<td class="text-semibold errortop"></td><td class="text-semibold errortop"></td><td class="text-semibold errortop"></td>';
            //             // htmlTbody += '</td>';
            //         } else if(Object.keys(valueTop3).length == '' || Object.keys(valueTop3).length == null ){
            //             htmlTbody += '<td class="text-semibold errortop"></td><td class="text-semibold errortop"></td><td class="text-semibold errortop"></td><td class="text-semibold errortop"></td>';
            //             // htmlTbody += '</td>';
            //         }
            //         // + '<td class="text-semibold" data-toggle="modal" data-target="#modalErrorGroup" onclick=showModalErrorDashboard("' + data[i].modelName + '","'+ data[i].groupName +'") title="Click to show Station Error Group"><a class="class-error-dashboard" >TOP</a></td></tr>';
            //     }
            // } else if(dataset.factory== 'B06'){
                for(var i = 0; i < data.length; i++ ){
                    dataGlo.groupName = data[i].groupName;
                    var rtr = data[i].retestRate;
                    var fpr = data[i].firstPassRate;
                    var ffail = data[i].firstFail;
                    var sfail = data[i].secondFail;
                    if (rtr < 0) {
                        rtr = 0;
                        sfail = ffail;
                        fpr = (data[i].inPutNew - ffail) * 100.0 / data[i].inPutNew;
                    }
                    number++;
                    htmlTbody += '<tr><td>'+ number +'</td>'
                                + '<td>'+data[i].modelName+'</td>'
                                + '<td class="text-semibold" title="Click to move Station Detail"><a class="group-name-detail" onclick=getGroup("'+  data[i].groupName +'") data-model="' + data[i].groupName + '" >'+data[i].groupName+'</a></td>'
                                // + '<td>'+data[i].plan+'</td>'
                                + '<td>'+data[i].wip+'</td>'
                                + '<td>'+ data[i].pass+'</td>'
                                + '<td class=" styletextshad  ' + convertStatus(fpr, 90, 97) + '" data-toggle="modal" data-target="#modalSetup" onclick=showModal("' + data[i].groupName + '") title="Click to show Station"><a class="class-group-name">' + fpr.toFixed(2) + '%</a></td>'
                                + '<td class=" styletextshad  ' + convertStatus((100 - rtr), 90, 95) + '" data-toggle="modal" data-target="#modalSetup" onclick=showModal("' + data[i].groupName + '") title="Click to show Station"><a class="class-group-name">' + rtr.toFixed(2) + '%</a></td>'
                                + '<td class=" styletextshad  ' + convertStatus(data[i].yieldRate, 95, 98) + '" data-toggle="modal" data-target="#modalSetup" onclick=showModal("' + data[i].groupName + '") title="Click to show Station"><a class="class-group-name">' + data[i].yieldRate.toFixed(2) + '%</a></td>'
                                + '<td>'+ data[i].firstFail+'</td>'
                                + '<td>'+ data[i].secondFail+'</td>';

                                var valueTop3 = Object.keys(data[i].errorMetaMap);
                                if(Object.keys(valueTop3).length == 3) {
                                    // htmlTbody += '<td class="text-semibold">';
                                        for(j in valueTop3){
                                            var subString = valueTop3[j].length;
                                            if (subString > 7) {
                                                var str1 = valueTop3[j];
                                                var str = str1.slice(0, 7) + '...';
                                                htmlTbody +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+'</a></td>';
                                            } else{
                                                htmlTbody +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+'</a></td>';

                                            }
                                        }
                                            htmlTbody +=  '<td class="text-semibold errortop"><a class="class-error" data-toggle="modal" data-target="#modalErrorGroup" onclick=showModalErrorDashboard("' + data[i].modelName + '","'+ data[i].groupName +'") title="Click to show Station Error Group">OTHER</a></td>';

                                }else if(Object.keys(valueTop3).length == 2) {
                                    // htmlTbody += '<td class="text-semibold">';
                                    for(j in valueTop3){
                                        var subString = valueTop3[j].length;
                                        if (subString > 7) {
                                            var str1 = valueTop3[j];
                                            var str = str1.slice(0, 7) + '...';
                                            htmlTbody += '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+' </a></td>';
                                        } else{
                                            htmlTbody += '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+' </a></td>';
                                        }
                                    } 
                                    htmlTbody += '<td class="text-semibold errortop"></td><td class="text-semibold errortop"></td>';
                                    // htmlTbody += '</td>';
                                } else if(Object.keys(valueTop3).length == 1){
                                    for(j in valueTop3){
                                        var subString = valueTop3[j].length;
                                        if (subString > 7) {
                                            var str1 = valueTop3[j];
                                            var str = str1.slice(0, 7) + '...';
                                            htmlTbody += '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+' </a></td>';
                                        } else{
                                            htmlTbody += '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+' </a></td>';
                                        }
                                    } 
                                    htmlTbody += '<td class="text-semibold errortop"></td><td class="text-semibold errortop"></td><td class="text-semibold errortop"></td>';
                                    // htmlTbody += '</td>';
                                } else if(Object.keys(valueTop3).length == '' || Object.keys(valueTop3).length == null ){
                                    htmlTbody += '<td class="text-semibold errortop"></td><td class="text-semibold errortop"></td><td class="text-semibold errortop"></td><td class="text-semibold errortop"></td>';
                                    // htmlTbody += '</td>';
                                }
                        // + '<td class="text-semibold" data-toggle="modal" data-target="#modalErrorGroup" onclick=showModalErrorDashboard("' + data[i].modelName + '","'+ data[i].groupName +'") title="Click to show Station Error Group"><a class="class-error-dashboard" >TOP</a></td></tr>';
                }
            // }
            $(".thbody").html(htmlTbody);
            $('.model-name').on('click', function (event) {
                var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName;
                openWindow(url);
            });

        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
        complete: function(){
            $('.loader').css('display', 'none');
        }
    });
}
function getGroup(group){
    var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName + "&groupName=" + group;
    openWindow(url);
}
function showModal(groupName) {
    $("#modalSetup").css('opacity','1');
    $('.loader').css('display', 'block');
   $.ajax({
       type: "GET",
       url: "/api/test/station/total",
       data: {
           factory: dataset.factory,
           timeSpan:dataset.timeSpan,
           modelName: dataset.modelName,
           groupName: groupName,
           includeStation: false
       },
       contentType: "application/json; charset=utf-8",
       success: function (data) {
               var html = '';
               var number = 0;
            //    if(dataset.factory == 'B04'){
            //        for(var i = 0; i < data.length; i++ ){
            //            // dataGloGroup = data[i].groupName; 
            //            dataStation = data[i].stationName;
            //            var rtr = data[i].retestRate;
            //            var fpr = data[i].firstPassRate;
            //            var ffail = data[i].firstFail;
            //            var sfail = data[i].secondFail;
            //            if (rtr < 0) {
            //                rtr = 0;
            //                sfail = ffail;
            //                fpr = (data[i].inPutNew - ffail) * 100.0 / data[i].inPutNew;
            //            }
            //            number++;
            //            html += '<tr id="row"><td>'+ number +'</td>'
            //            // + '<td class="text-semibold" title="Click to move Station Detail"><a class="group-name-station" data-model="' + data[i].groupName + '" >'+data[i].groupName+'</a></td>'
            //            + '<td>'+data[i].modelName+'</td>'
            //            + '<td>'+data[i].groupName+'</td>'
            //            + '<td class="text-semibold" title="Click to move Station Detail"><a class="station-name" onclick=getStationName("'+  data[i].groupName +'","'+  data[i].stationName +'") data-model="' + data[i].stationName + '" >'+data[i].stationName+'</a></td>'
            //                // + '<td>'+data[i].plan+'</td>'
            //                + '<td>'+data[i].wip+'</td>'
            //                + '<td>'+ data[i].pass+'</td>'
            //                + '<td class="' + convertStatus(fpr, 90, 97) + '">' + fpr.toFixed(2) + '%</td>'
            //                + '<td class="' + convertStatus((100 - rtr), 90, 95) + '">' +  rtr.toFixed(2) + '%</td>'
            //                + '<td class="' + convertStatus(data[i].yieldRate, 90, 97) + '">' + data[i].yieldRate.toFixed(2) + '%</td>'
            //                + '<td>'+ data[i].firstFail+'</td>'
            //                + '<td>'+ data[i].secondFail+'</td>'
            //                // + '<td class="text-semibold" data-toggle="modal" data-target="#modalError" title="Click to show Station Error"><a class="class-error-edit" >TOP</a></td>'
                          
            //                var valueTop3 = Object.keys(data[i].errorMetaMap);
            //                if(Object.keys(valueTop3).length == 3) {
            //                    // html += '<td class="text-semibold errortop">';
            //                        for(j in valueTop3){
            //                         var subString = valueTop3[j].length;
            //                             if (subString >7) {
            //                                 var str1 = valueTop3[j];
            //                                 var str = str1.slice(0, 7) + '...';
            //                                 html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+'</a></td>';
            //                             } else{
            //                                 html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+'</a></td>';
            //                             }
            //                         }
            //                        html +=  '<td class="text-semibold errortop"><a class="class-error" data-toggle="modal" data-target="#modalErrorGroup" onclick=showModalErrorDashboard("' + data[i].modelName + '","'+ data[i].groupName +'") title="Click to show Station Error Group">OTHER</a></td>';
       
            //                }else if(Object.keys(valueTop3).length == 2) {
            //                    // html += '<td class="text-semibold errortop">';
            //                    for(j in valueTop3){
            //                     var subString = valueTop3[j].length;
            //                         if (subString >7) {
            //                             var str1 = valueTop3[j];
            //                             var str = str1.slice(0, 7) + '...';
            //                             html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+'</a></td>';
            //                         } else{
            //                             html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+'</a></td>';
            //                         }
            //                    }
            //                    html += '<td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td>';
            //                    // html += '</td>';
            //                } else if(Object.keys(valueTop3).length == 1){
            //                    for(j in valueTop3){
            //                     var subString = valueTop3[j].length;
            //                         if (subString >7) {
            //                             var str1 = valueTop3[j];
            //                             var str = str1.slice(0, 7) + '...';
            //                             html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+'</a></td>';
            //                         } else{
            //                             html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+'</a></td>';
            //                         }
            //                    }
            //                    html += '<td class="text-semibold errortop"></td><td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td>';
            //                } else if(Object.keys(valueTop3).length == null || Object.keys(valueTop3).length == ''){
            //                    html += '<td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td>';
            //                }
            //                + '</tr>';
            //        }  
                   
            //    } else if(dataset.factory == 'B06'){
                   for(var i = 0; i < data.length; i++ ){
                       // dataGloGroup = data[i].groupName;
                       dataStation = data[i].stationName;
                       var rtr = data[i].retestRate;
                       var fpr = data[i].firstPassRate;
                       var ffail = data[i].firstFail;
                       var sfail = data[i].secondFail;
                       if (rtr < 0) {
                           rtr = 0;
                           sfail = ffail;
                           fpr = (data[i].inPutNew - ffail) * 100.0 / data[i].inPutNew;
                       }
                       number++;
                       html += '<tr id="row"><td>'+ number +'</td>'
                       // + '<td class="text-semibold" title="Click to move Station Detail"><a class="group-name-station" data-model="' + data[i].groupName + '" >'+data[i].groupName+'</a></td>'
                       + '<td>'+data[i].modelName+'</td>'
                       + '<td>'+data[i].groupName+'</td>'
                       + '<td class="text-semibold" title="Click to move Station Detail"><a class="station-name" onclick=getStationName("'+  data[i].groupName +'","'+  data[i].stationName +'") data-model="' + data[i].stationName + '" >'+data[i].stationName+'</a></td>'
                           // + '<td>'+data[i].plan+'</td>'
                           + '<td>'+data[i].wip+'</td>'
                           + '<td>'+ data[i].pass+'</td>'
                           + '<td class="' + convertStatus(fpr, 90, 97) + '">' + fpr.toFixed(2) + '%</td>'
                           + '<td class="' + convertStatus((100 - rtr), 90, 95) + '">' +  rtr.toFixed(2) + '%</td>'
                           + '<td class="' + convertStatus(data[i].yieldRate, 90, 97) + '">' + data[i].yieldRate.toFixed(2) + '%</td>'
                           + '<td>'+ data[i].firstFail+'</td>'
                           + '<td>'+ data[i].secondFail+'</td>'
                           // + '<td class="text-semibold" data-toggle="modal" data-target="#modalError" title="Click to show Station Error"><a class="class-error-edit" >TOP</a></td>'
                          
                           var valueTop3 = Object.keys(data[i].errorMetaMap);
                           if(Object.keys(valueTop3).length == 3) {
                               // html += '<td class="text-semibold errortop">';
                                   for(j in valueTop3){
                                    var subString = valueTop3[j].length;
                                        if (subString >7) {
                                            var str1 = valueTop3[j];
                                            var str = str1.slice(0, 7) + '...';
                                            html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+'</a></td>';
                                        } else{
                                            html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+'</a></td>';
                                        }
                                    }
                                   html +=  '<td class="text-semibold errortop"><a class="class-error" data-toggle="modal" data-target="#modalErrorGroup" onclick=showModalErrorDashboard("' + data[i].modelName + '","'+ data[i].groupName +'") title="Click to show Station Error Group">OTHER</a></td>';
       
                           }else if(Object.keys(valueTop3).length == 2) {
                               // html += '<td class="text-semibold errortop">';
                               for(j in valueTop3){
                                var subString = valueTop3[j].length;
                                    if (subString >7) {
                                        var str1 = valueTop3[j];
                                        var str = str1.slice(0, 7) + '...';
                                        html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+'</a></td>';
                                    } else{
                                        html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+'</a></td>';
                                    }
                               }
                               html += '<td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td>';
                               // html += '</td>';
                           } else if(Object.keys(valueTop3).length == 1){
                               for(j in valueTop3){
                                var subString = valueTop3[j].length;
                                    if (subString >7) {
                                        var str1 = valueTop3[j];
                                        var str = str1.slice(0, 7) + '...';
                                        html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+str+'</a></td>';
                                    } else{
                                        html +=  '<td class="text-semibold errortop"><a class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ valueTop3[j] + '\')" title="'+valueTop3[j]+'">'+valueTop3[j]+'</a></td>';
                                    }
                               }
                               html += '<td class="text-semibold errortop"></td><td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td>';
                           } else if(Object.keys(valueTop3).length == null || Object.keys(valueTop3).length == ''){
                               html += '<td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td><td class="text-semibold errortop" ></td>';
                           }
                           + '</tr>';
                   
                   }  
                     
            //    }
               $("#tblModelMeta tbody").html(html);
               $('.model-name-station').on('click', function (event) {
                   var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName;
                   openWindow(url);
               });
               $('.group-name-station').on('click', function (event) {
                   var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName + "&groupName=" + groupName;
                   openWindow(url);
               });
       },
       failure: function (errMsg) {
           console.log(errMsg);
       },
       complete: function(){
           $('.loader').css('display', 'none');
      }
   });
}
function getStationName(groupName,stationName){
    var url = "/station-detail?factory=" + dataset.factory + "&modelName=" + dataset.modelName + "&groupName=" + groupName + "&stationName=" + stationName;
    openWindow(url);
}
function showModalErrorDashboard(modelNameError2, groupName2) {
    dataGroup.modelName = modelNameError2;
    dataGroup.groupName = groupName2;
    dataGroup.stationName = '';
    functionSelectvalueMNGroup();
    loadTableGroup();
}
function loadTableGroup(){
    $.ajax({
        type: "GET",
        url: "/api/test/station/error/detail",
        data: {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan,
            modelName: dataGroup.modelName,
            groupName: dataGroup.groupName,
            stationName: dataGroup.stationName
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var html='';
            var number = 0;
                for(i in data){          
                     var valueDescription = data[i].errorDescription;
                     var valueStationName = data[i].stationName;
                     if(valueDescription== null  ){
                         valueDescription = '';
                     }
                     if( valueStationName == null ){
                        valueStationName = '';
                     }
                     var valuefail;
                     if (data[i].wip == 0 || data[i].wip== null) {
                        valuefail = 0;
                     } else{
                        valuefail = data[i].fail * 100 /data[i].wip;
                     }
                  number++;
                  html += '<tr id="row"><td>'+ number +'</td>'
                      + '<td>'+data[i].modelName+'</td>'
                      // + '<td class="text-semibold" ><a class="errorCode">'+data[i].errorCode+'</a></td>'
                      + '<td class="text-semibold" data-toggle="modal" data-target="#modalErrorCodeGroup" onclick="showModalErrorCode(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ data[i].errorCode+ '\')" title="Click to show Error Code"><a class="class-error-code" >'+data[i].errorCode+'</a></td>'
                      + '<td>'+data[i].groupName+'</td>'
                      + '<td>'+valueStationName+'</td>'
                      + '<td>'+valueDescription+'</td>'
                      + '<td>'+data[i].wip+'</td>'
                      + '<td>'+data[i].pass+'</td>'
                      + '<td>'+ data[i].testFail+'</td>'
                      + '<td>'+data[i].fail+'</td>'
                      + '<td>'+valuefail.toFixed(2) +' %</td>'
                      + '</tr>'; 
                }

                 $("#tblModelErrorGroup tbody").html(html);
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}
function functionSelectvalueMNGroup(){
    functionSelectvalueMNGroup1();
    functionSelectvalueMNGroup2();
    functionSelectvalueMNGroup3();
}
function functionSelectvalueMNGroup1(){
    $.ajax({
        type: "GET",
        url: "/api/test/model",
        data: {
            factory: dataset.factory,
            parameter: dataset.parameter,
            timeSpan: dataset.timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var arrayModel = [];
            var html = '<option value="" disabled="" selected>Select Model</option>';
            var selector = $('#idGroup1');
            selector.children('option').remove();   
            for(i in data){
                if(arrayModel.indexOf(data[i].modelName) == -1){ 
                    arrayModel.push(data[i].modelName);
                }
            }
            for(j in arrayModel){
                if(arrayModel[j] == dataset.modelName){
                    html += '<option class="classvalueMN2" value="' + arrayModel[j] + '" selected>' + arrayModel[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                } else{
                    html += '<option class="classvalueMN2" value="' + arrayModel[j] + '">' + arrayModel[j] + '</option>';  
                }
            }
            $('#idGroup1').html(html);
            selector.selectpicker('refresh');
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
    });
}
function functionSelectvalueMNGroup2(){
    $.ajax({
        type: "GET",
        url: "/api/test/group",
        data: {
            factory: dataset.factory,
            modelName:  dataGroup.modelName,
            parameter: dataset.parameter,
            timeSpan: dataset.timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var arrayGroup = [];
            var selector = $('#idGroup2');
            var html = '<option value="">All</option>';
            selector.children('option').remove();
            if (dataGroup.groupName == '') {
                $('.selecthtmlSN2').addClass('hidden');
            } else{
                $('.selecthtmlSN2').removeClass('hidden');
            }
            for(i in data){
                if(arrayGroup.indexOf(data[i].groupName) == -1){ 
                    arrayGroup.push(data[i].groupName);
                }
            }
            for(j in arrayGroup){
                if(arrayGroup[j] == dataGroup.groupName){
                    html += '<option class="classvalueGN" value="' + arrayGroup[j] + '" selected>' + arrayGroup[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                }else{
                    html += '<option class="classvalueGN" value="' + arrayGroup[j] + '">' + arrayGroup[j] + '</option>';
                }
            }
            $('#idGroup2').html(html);
            selector.selectpicker('refresh');
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
function functionSelectvalueMNGroup3(){
    $.ajax({
        type: "GET",
        url: "/api/test/station",
        data: {
            factory: dataset.factory,
            modelName:  dataGroup.modelName,
            groupName:  dataGroup.groupName,
            parameter: dataset.parameter,
            timeSpan: dataset.timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var arrayStation = [];
            var selector = $('#idGroup3');
            var html = '<option value="">All</option>';
            selector.children('option').remove(); 
            // selector.removeAttr('selected').find('option:first').attr('selected','selected');
            for(i in data){
                if((arrayStation.indexOf(data[i].stationName) == -1) && data[i].stationName != null){ 
                    arrayStation.push(data[i].stationName);
                }
            }
            for(j in arrayStation){
                if(arrayStation[j] == dataGroup.stationName){
                    html += '<option class="classvalueSN" value="' + arrayStation[j] + '" selected>' + arrayStation[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                }else{
                    html += '<option class="classvalueSN" value="' + arrayStation[j] + '">' + arrayStation[j] + '</option>'; 
                }
            }
            $('#idGroup3').html(html);
            // $("#idSelectSN").val(options);
            // selector.append(options);
            selector.selectpicker('refresh');
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
$('select[name=modelName2]').on('change', function () {
    dataGroup.modelName = this.value;
    dataGroup.groupName = "";
    dataGroup.stationName = "";
    functionSelectvalueMNGroup2();
    functionSelectvalueMNGroup3();
    loadTableGroup();
});
$('select[name=groupName2]').on('change', function () {
    dataGroup.groupName = this.value;
    if( $('#idGroup2').val()=='' ||  $('#idGroup2').val()== null){
        $('.selecthtmlSN2').addClass('hidden');
        dataGroup.groupName = "";
        dataGroup.stationName = "";
        functionSelectvalueMNGroup3();
        loadTableGroup();
    } else{
        $('.selecthtmlSN2').removeClass('hidden');
        dataGroup.groupName = this.value;
        dataGroup.stationName = "";  
        functionSelectvalueMNGroup3();
        loadTableGroup();
    }
});
$('select[name=stationName2]').on('change', function () {
    dataGroup.stationName = this.value;
    if($('#idGroup3').val() == ''){
        dataGroup.stationName = '';
        loadTableGroup();
    } else{
        dataGroup.stationName = this.value;
        loadTableGroup();
    }
});
function closeModal() {
    $("#mydiv").removeClass("show");
}
 $("#close3").click(function (){
    $("#modalSetup").css('opacity','1');
 });
 $("#close-error-2").click(function (){
    $("#modalErrorGroup").css('opacity','1');
    $("#modalSetup").css('opacity','1');
 });
 $("#close-error-3").click(function (){
    $("#modalSetup").css('opacity','1');
 });
 $("#modalErrorCodeGroup").on('hide.bs.modal',function(){
    $("#modalErrorGroup").css('opacity','1');
    $("#modalSetup").css('opacity','1');
 });
 function showModalError(modelNameError, groupName, stationName) {
    $("#modalSetup").css('opacity','0.5');
    dataGlo.modelName = modelNameError;
    dataGlo.groupName = groupName;
    dataGlo.stationName = stationName;
    // dataStation = stationName;
    functionSelectvalueMN();
    loadTable();
}
function functionSelectvalueMN(){
   functionSelectvalueMN1();
   functionSelectvalueMN2();
   functionSelectvalueMN3();
}
function loadTable(){
    $.ajax({
        type: "GET",
        url: "/api/test/station/error/detail",
        data: {
            factory: dataset.factory,
            timeSpan: dataset.timeSpan,
            modelName: dataGlo.modelName,
            groupName: dataGlo.groupName,
            stationName: dataGlo.stationName
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var html='';
            var number = 0;
                for(i in data){          
                  var valueDescription = data[i].errorDescription;
                  var valueStationName = data[i].stationName;
                  if(valueDescription== null  ){
                      valueDescription = '';
                  }
                  if( valueStationName == null ){
                     valueStationName = '';
                  }
                  var valuefail;
                  if (data[i].wip == 0 || data[i].wip== null) {
                     valuefail = 0;
                  } else{
                     valuefail = data[i].fail * 100 /data[i].wip;
                  }
                  number++;
                  html += '<tr id="row"><td>'+ number +'</td>'
                      + '<td>'+data[i].modelName+'</td>'
                      // + '<td class="text-semibold" ><a class="errorCode">'+data[i].errorCode+'</a></td>'
                      + '<td class="text-semibold" data-toggle="modal" data-target="#modalErrorCode" onclick="showModalErrorCode3(\''+ data[i].modelName + '\',\''+ data[i].groupName + '\',\''+ data[i].errorCode+ '\')" title="Click to show Error Code"><a class="class-error-code" >'+data[i].errorCode+'</a></td>'
                      + '<td>'+data[i].groupName+'</td>'
                      + '<td>'+valueStationName+'</td>'
                      + '<td>'+valueDescription+'</td>'
                      + '<td>'+data[i].wip+'</td>'
                      + '<td>'+data[i].pass+'</td>'
                      + '<td>'+ data[i].testFail+'</td>'
                      + '<td>'+data[i].fail+'</td>'
                      + '<td>'+valuefail.toFixed(2) +' %</td>'
                      + '</tr>'; 
                }

                 $("#tblModelErrorMGS tbody").html(html);
        },
        failure: function (errMsg) {
            console.log(errMsg);
        }
    });
}
function functionSelectvalueMN1(){
    $.ajax({
        type: "GET",
        url: "/api/test/model",
        data: {
            factory: dataset.factory,
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var arrayModel = [];
            var selector = $('#idSelectMN');
            var html = '<option value="" disabled="" selected>Select Model</option>'; 
            selector.children('option').remove();  
            for(i in data){
                if(arrayModel.indexOf(data[i].modelName) == -1){ 
                    arrayModel.push(data[i].modelName);
                }
            }
            for(j in arrayModel){
                if(arrayModel[j] == dataset.modelName){
                    html += '<option class="classvalueMN" value="' + arrayModel[j] + '" selected>' + arrayModel[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                } else{
                    html += '<option class="classvalueMN" value="' + arrayModel[j] + '">' + arrayModel[j] + '</option>'; 
                    
                }
            }
            $('#idSelectMN').html(html);
            selector.selectpicker('refresh');
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
    });
}
function functionSelectvalueMN2(){
    $.ajax({
        type: "GET",
        url: "/api/test/group",
        data: {
            factory: dataset.factory,
            modelName: dataGlo.modelName,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var arrayGroup = [];
            var selector = $('#idSelectGN');
            selector.children('option').remove();
            var html = '<option value="">All</option>';
            if (dataGlo.groupName == '') {
                $('.SelecthtmlSN3').addClass('hidden');
            } else{
                $('.SelecthtmlSN3').removeClass('hidden');
            }
            for(i in data){
                if(arrayGroup.indexOf(data[i].groupName) == -1){ 
                    arrayGroup.push(data[i].groupName);
                }
            }
            for(j in arrayGroup){
                if(arrayGroup[j] == dataGlo.groupName){
                    html += '<option class="classvalueGN" value="' + arrayGroup[j] + '" selected>' + arrayGroup[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                }else{
                    html += '<option class="classvalueGN" value="' + arrayGroup[j] + '">' + arrayGroup[j] + '</option>';
                }
            }
            $('#idSelectGN').html(html);
            selector.selectpicker('refresh');
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
function functionSelectvalueMN3(){
    $.ajax({
        type: "GET",
        url: "/api/test/station",
        data: {
            factory: dataset.factory,
            modelName: dataGlo.modelName,
            groupName: dataGlo.groupName,
        },
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            var arrayStation = [];
            var selector = $('#idSelectSN');
            selector.children('option').remove(); 
            var html = '<option value="">All</option>';
            // selector.removeAttr('selected').find('option:first').attr('selected','selected');
            for(i in data){
                if((arrayStation.indexOf(data[i].stationName) == -1) && data[i].stationName != null){ 
                    arrayStation.push(data[i].stationName);
                }
            }
            for(j in arrayStation){
                if(arrayStation[j] == dataGlo.stationName){
                    html += '<option class="classvalueSN" value="' + arrayStation[j] + '" selected>' + arrayStation[j] + '</option>';
                    // $('li[data-original-index="'+j+'"]').addClass("active");
                }else{
                    html += '<option class="classvalueSN" value="' + arrayStation[j] + '">' + arrayStation[j] + '</option>'; 
                }
            }
            $('#idSelectSN').html(html);
            selector.selectpicker('refresh');
        },
        failure: function (errMsg) {
            console.log(errMsg);
        },
    });
}
$('select[name=modelName3]').on('change', function () {
    dataGlo.modelName = this.value;
    dataGlo.groupName = "";
    dataGlo.stationName = "";
    functionSelectvalueMN2();
    functionSelectvalueMN3();
    loadTable();
});

$('select[name=groupName3]').on('change', function () {
    dataGlo.groupName = this.value;
    if( $('#idSelectGN').val()=='' ||  $('#idSelectGN').val()== null){
        $('.selecthtmlSN3').addClass('hidden');
        dataGlo.groupName = "";
        dataGlo.stationName = "";
        functionSelectvalueMN3();
        loadTable();
    } else{
        $('.selecthtmlSN3').removeClass('hidden');
        dataGlo.groupName = this.value;
        dataGlo.stationName = "";
        functionSelectvalueMN3();
        loadTable();
    }

});
$('select[name=stationName3]').on('change', function () {
    dataGlo.stationName = this.value;
    if($('#idSelectSN').val() == ''){
        dataGlo.stationName = '';
        loadTable();
    } else{
        dataGlo.stationName = this.value;
        loadTable();
    }
});
function showModalErrorCode(model2, group2, errorCode){
    $("#modalErrorGroup").css('opacity','0.5');
    $("#modalSetup").css('opacity','0.5');
    // $('#addModel').html(model2);
    // $('#addGroup').html(group2);
    $.ajax({
        type: 'GET',
        url: '/api/test/station/byError',
        data: {
            factory: dataset.factory,
            modelName: model2,
            groupName: group2,
            errorCode: errorCode,
            timeSpan: dataset.timeSpan
        },
        success: function(data){
            var dataChart = new Array(data.length);
            var categories = new Array(data.length);
            if (!$.isEmptyObject(data)) {
                for(i in data){
                    categories[i] = data[i].stationName;
                    dataChart[i] = {name: data[i].stationName, y: data[i].testFail}
                }
            }

            Highcharts.chart('chart-error-code-by-tester-2', {
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },
                title: {
                    text: (errorCode + ' - ISSUE BY TESTER'),
                    color: '#ccc'
                },
                subtitle:{
                    text: '('+model2+' - '+group2+')'
                },
                xAxis: {
                    categories: categories,
                    labels:{
                        rotation: 90,
                        align: top,
                        style: {
                            fontSize: '11px'
                        }
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ' '
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    }
                },
                legend: {
                    enabled: false
                },
                navigation: {
                    buttonOptions: {
                        enabled: false
                    }
                },
                credits: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        stacking: 'normal',
                    }
                },
                series: [{
                    name: 'Test Fail',
                    colorByPoint: true,
                    data: dataChart
                }]
            });
        },
        failure: function(errMsg) {
             console.log(errMsg);
        }
    });
}
function showModalErrorCode3(modelName3, groupName3, errorCode){
    $("#modalSetup").css('opacity','0.5');
    $.ajax({
        type: 'GET',
        url: '/api/test/station/byError',
        data: {
            factory: dataset.factory,
            modelName: modelName3,
            groupName: groupName3,
            errorCode: errorCode,
            timeSpan: dataset.timeSpan
        },
        success: function(data){
            var dataChart = new Array(data.length);
            var categories = new Array(data.length);
            if (!$.isEmptyObject(data)) {
                for(i in data){
                    categories[i] = data[i].stationName;
                    dataChart[i] = {name: data[i].stationName, y: data[i].testFail}
                }
            }

            Highcharts.chart('chart-error-code-by-tester-3', {
                chart: {
                    type: 'column',
                    style: {
                        fontFamily: '-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji"'
                    }
                },
                title: {
                    text: (errorCode + ' - ISSUE BY TESTER')
                },
                xAxis: {
                    categories: categories,
                    labels:{
                        rotation: 90,
                        align: top,
                        style: {
                            fontSize: '11px'
                        }
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ' '
                    },
                    stackLabels: {
                        enabled: true,
                        style: {
                            fontWeight: 'bold',
                            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                        }
                    }
                },
                legend: {
                    enabled: false
                },
                navigation: {
                    buttonOptions: {
                        enabled: false
                    }
                },
                credits: {
                    enabled: false
                },
                plotOptions: {
                    column: {
                        stacking: 'normal',
                    }
                },
                series: [{
                    name: 'Test Fail',
                    colorByPoint: true,
                    data: dataChart
                }]
            });
        },
        failure: function(errMsg) {
             console.log(errMsg);
        }
    });
}