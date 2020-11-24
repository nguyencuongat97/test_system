
// function moveToSmtDetailPage() {
//     window.open("http://10.225.35.80:2810");
// }

// function moveToSmtEscPage() {
//     window.open("http://10.225.35.80:2810/escape");
// }

// function moveToSmtOffsetPage() {
//     window.open("http://10.225.35.80:2810/error");
// }
function moveToSmtDetailPage() {
    var machineName = dataset.machine;
    var sName = machineName.slice(machineName.indexOf("A"));
   // window.open("/aoi-analytics/aoi-detail?factory=" + dataset.factory + "&line=" + dataset.lineName + "&machine=" + sName);
     let url = 'http://10.225.35.80:2810/?factory=' + dataset.factory + '&line=' + dataset.lineName + '&machine=' + sName;
        let idTab = 'Detail' + dataset.factory + dataset.lineName + sName;
        let titleTab = 'Detail ' + dataset.factory + dataset.lineName + sName;
        var a = parent.addTabTest(titleTab, idTab, url)
}

function moveToSmtEscPage() {
    var machineName = dataset.machine;
    var sName = machineName.slice(machineName.indexOf("A"));
   // window.open("/aoi-analytics/aoi-escape?factory=" + dataset.factory + "&line=" + dataset.lineName + "&machine=" + sName);
     let url = 'http://10.225.35.80:2810/escape/?factory=' + dataset.factory + '&line=' + dataset.lineName + '&machine=' + sName;
        let idTab = 'Escape' + dataset.factory + dataset.lineName + sName;
        let titleTab = 'Escape ' + dataset.factory + dataset.lineName + sName;
        var b = parent.addTabTest(titleTab, idTab, url)
}

function moveToSmtOffsetPage() {
    var machineName = dataset.machine;
    var sName = machineName.slice(machineName.indexOf("A"));
   // window.open("/aoi-analytics/aoi-error-offset?factory=" + dataset.factory + "&line=" + dataset.lineName + "&machine=" + sName);
      let url = 'http://10.225.35.80:2810/error/?factory=' + dataset.factory + '&line=' + dataset.lineName + '&machine=' + sName;
       let idTab = 'Error' + dataset.factory + dataset.lineName + sName;
       let titleTab = 'Error ' + dataset.factory + dataset.lineName + sName;
       var c = parent.addTabTest(titleTab, idTab, url)
}
function toSmtOffsetError(line, machine) {
    var sName = machine.slice(machine.indexOf("A"));
   // window.open("/aoi-analytics/aoi-error-offset?factory=" + dataset.factory + "&line=" + line + "&machine=" + sName);
     var url = 'http://10.225.35.80:2810/error/?factory=' + dataset.factory + '&line=' + line + '&machine=' + sName;
     var idTab = 'Error' + dataset.factory + line + sName;
     var titleTab = 'Error ' + dataset.factory + line + sName;
     var d = parent.addTabTest(titleTab, idTab, url)
}