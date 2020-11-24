function loadModels(dataset, flag, onLoad) {
    $.ajax({
        type: 'GET',
        url: '/api/test/model',
        data: {
            factory: dataset.factory,
            parameter: dataset.parameter,
            timeSpan: dataset.timeSpan
        },
        success: function(data){
            var selector = $("select[name='modelName']");
            selector.children('option').remove();

            var options = "";
            for(i in data){
                options+='<option value=' + data[i].modelName + '>' + data[i].modelName + '</option>';
            }
            selector.append(options);

            if (data.length > 0) {
                if (flag == undefined || flag == false) {
                    dataset.modelName = data[0].modelName;
                }
                selector.val(dataset.modelName);
                onLoad(dataset, flag);
            } else {
                dataset.modelName = "";
                onLoad(dataset, flag);
            }

            selector.selectpicker('refresh');
        },
        failure: function(errMsg) {
             console.log(errMsg);
        },
   });
}

function loadGroups(dataset, flag, onLoad) {
    $.ajax({
        type: 'GET',
        url: '/api/test/group',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            parameter: dataset.parameter,
            timeSpan: dataset.timeSpan
        },
        success: function(data){
            var selector = $("select[name='groupName']");
            selector.children('option').remove();

            var options = "";
            for(i in data){
                options+='<option value=' + data[i].groupName + '>' + data[i].groupName + '</option>';
            }
            selector.append(options);

            if (data.length > 0) {
                if (flag == undefined || flag == false) {
                    dataset.groupName = data[0].groupName;
                }
                selector.val(dataset.groupName);
                onLoad(dataset, flag);
            } else {
                dataset.groupName = "";
                onLoad(dataset, flag);
            }

            selector.selectpicker('refresh');
        },
        failure: function(errMsg) {
             console.log(errMsg);
        },
   });
}

function loadStations(dataset, flag, onLoad, all) {
    $.ajax({
        type: 'GET',
        url: '/api/test/station',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            groupName: dataset.groupName,
            parameter: dataset.parameter,
            timeSpan: dataset.timeSpan
        },
        success: function(data){
            var selector = $("select[name='stationName']");
            selector.children('option').remove();

            var options = "";
            if (all != undefined && all == true) {
                options+='<option value="">ALL</option>';
            }
            for(i in data){
                options+='<option value=' + data[i].stationName + '>' + (data[i].factory == 'B04' ? data[i].testerName : data[i].stationName) + '</option>';
            }
            selector.append(options);

            if (data.length > 0) {
                if (flag == undefined || flag == false) {
                    if (all != undefined && all == true) {
                        dataset.stationName = "";
                    } else {
                        dataset.stationName = data[0].stationName;
                    }
                }
                selector.val(dataset.stationName);
                onLoad(dataset, flag);
            } else {
                dataset.stationName = "";
                onLoad(dataset, flag);
            }

            selector.selectpicker('refresh');
        },
        failure: function(errMsg) {
             console.log(errMsg);
        },
   });
}

function loadParameters(dataset, flag, onLoad) {
    $.ajax({
        type: 'GET',
        url: '/api/test/model/parameter',
        data: {
            factory: dataset.factory,
            modelName: dataset.modelName,
            groupName: dataset.groupName
        },
        success: function(data){
            var selector = $("select[name='parameter']");
            selector.children('option').remove();

            var options = "";
            for(i in data){
                options+='<option value="' + data[i].parameters + '" data-low=' + data[i].lowSpec + ' data-high=' + data[i].highSpec + '>' + data[i].parameters + '</option>';
            }
            selector.append(options);
            selector.selectpicker('refresh');

            onLoad(dataset, flag);
        },
        failure: function(errMsg) {
             console.log(errMsg);
        },
   });
}

function getUrl(type, factory, modelName, groupName, stationName) {
    var url = "http://10.224.81.70:8888";

    // add path
    switch (type) {
        case "LOCKED_A":
        case "LOCKED_B":
        case "LOCKED_TIMEOUT":
            url += "/troubleshooting-guide";
            break;
        case "WARNING_CPK":
            url += "/station-cpk";
            break;
        default:
            url += "/station-detail";
    }

    // add params
    url = url + "?factory=" + factory + "&modelName=" + modelName + "&groupName=" + groupName + "&stationName=" + stationName;

    return url;
}

function getTimeSpan(days, isMonthly) {
    if (days == undefined) {
        days = 0;
    }

    var timeSpan = {
        shiftType: "",
        startDate: new Date(),
        endDate: new Date()
    }

    var day = new Date();
    day.setHours(7, 30, 0, 0);

    var night = new Date();

    night.setHours(19, 30, 0, 0);

    var now = new Date();

    if (now - day < 0) {
        timeSpan.shiftType = "NIGHT";
        timeSpan.startDate.setDate(timeSpan.startDate.getDate() - 1 - days);
        timeSpan.startDate.setHours(7, 30);
        timeSpan.endDate.setHours(7, 30);
    } else if (now - night < 0) {
        timeSpan.shiftType = "DAY";
        timeSpan.startDate.setDate(timeSpan.startDate.getDate() - days);
        timeSpan.startDate.setHours(7, 30);
        timeSpan.endDate.setDate(timeSpan.endDate.getDate() + 1);
        timeSpan.endDate.setHours(7, 30);
    } else {
        timeSpan.shiftType = "NIGHT";
        timeSpan.startDate.setDate(timeSpan.startDate.getDate() - days);
        timeSpan.startDate.setHours(7, 30);
        timeSpan.endDate.setDate(timeSpan.endDate.getDate() + 1);
        timeSpan.endDate.setHours(7, 30);
    }

    if (isMonthly) {
        timeSpan.startDate.setDate(1);

        timeSpan.endDate.setDate(timeSpan.endDate.getDate() - 1 - days);
        timeSpan.endDate.setDate(1);
        timeSpan.endDate.setMonth(timeSpan.endDate.getMonth() + 1);
    }
    return timeSpan;
}

function getOldTimeSpan(days) {
    if (days == undefined) {
        days = 0;
    }

    var timeSpan = {
        shiftType: "",
        startDate: new Date(),
        endDate: new Date()
    }

    var day = new Date();
    day.setHours(7, 30, 0, 0);

    var night = new Date();
    night.setHours(19, 30, 0, 0);

    var now = new Date();

    if (now - day < 0) {
        timeSpan.shiftType = "NIGHT";
        timeSpan.startDate.setDate(timeSpan.startDate.getDate() - 1 - days);
        timeSpan.startDate.setHours(19, 30);
        timeSpan.endDate.setHours(7, 30);
        return timeSpan;
    } else if (now - night < 0) {
        timeSpan.shiftType = "DAY";
        timeSpan.startDate.setDate(timeSpan.startDate.getDate() - days);
        timeSpan.startDate.setHours(7, 30);
        timeSpan.endDate.setHours(19, 30);
        return timeSpan;
    } else {
        timeSpan.shiftType = "NIGHT";
        timeSpan.startDate.setDate(timeSpan.startDate.getDate() - days);
        timeSpan.startDate.setHours(19, 30);
        timeSpan.endDate.setDate(timeSpan.endDate.getDate() + 1);
        timeSpan.endDate.setHours(7, 30);
        return timeSpan;
    }
}

function activeMenu(path, factory) {
    if (factory != '' && factory != undefined) {
        $('.imenu[path="'+factory.toLowerCase()+'-'+ path +'"]').addClass('active');
    }
    else $('.imenu[path="'+ path +'"]').addClass('active');
}

function openVLC(url) {
    if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
        window.location.href = 'intent://' + url + '#Intent;scheme=rtsp;package=org.videolan.vlc;end';
    } else {
        window.location.href = 'vlc://' + url;
    }
}

function openSingleVLC(url) {
    if(mobileCheck()) {
        window.location.href = 'intent://' + url + '#Intent;scheme=rtsp;package=org.videolan.vlc;end';
    } else {
        window.location.href = 'vlc://rtsp://' + url;
    }
}

function hrefWindow(href) {
    window.location.href = href;
}

function openWindow(href) {
    window.sessionStorage.clear();
    //alert(navigator.userAgent + navigator.vendor + window.opera);
    if(mobileAndTabletCheck()) {
        window.location.href = href;
    } else {
        window.open(href);
    }
}

function toString(value) {
    return value != null ? value : '-';
}

function toStringg(value, defaultValue) {
    return value != null ? value : defaultValue;
}

function convertStatus(rate, errorSpec, warningSpec) {
    if (rate < errorSpec) {
        return 'bg-red';
    }
    if (rate < warningSpec) {
        return 'bg-yellow';
    }
    return 'bg-green';
}
function convertRetestRate(rate, errorSpec, warningSpec) {
    if (rate > errorSpec) {
        return 'bg-red';
    }
    if (rate > warningSpec) {
        return 'bg-yellow';
    }
    return 'bg-green';
}

function mobileCheck() {
  var check = false;
  (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
  return check;
}

function mobileAndTabletCheck() {
  var check = false;
  (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
  return check;
}