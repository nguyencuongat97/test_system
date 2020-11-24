<div class="row" style="padding: 10px; background-color: #272727; text-align: center; color: #ccc">
    <div class="col-lg-12" style="text-align: center;">
        <div>
            <h3 class="header">
                MANAGEMENT MODEL
                <a class="btn" onclick="cancel()" title="Reload" style="float: right; top: -8px;">
                    <i class="fa fa-refresh" aria-hidden="true" style="color: #fff; font-size: 18px;"></i>
                </a>
            </h3>
            
        </div>
        <div style="min-height: 300px; border: solid 1px rgba(0, 0, 0, 0.2); padding: 15px; margin-bottom: 10px;">
            <div class="row" style="width: 100%; margin: 0px 4% 10px 0px">
                <button class="btn btn-lg" id="btnAdd" style="float: left; padding: 5px 10px; border-radius: 10px; color: #ccc; background: #272727; border: 1px solid #fff;" onclick="addNewClick()">
                    <i class="fa fa-plus"></i> Add
                </button>
                <input id="txtSearch" class="form-control" type="search" placeholder="Search" style="float: right; padding: 5px 10px; border-radius: 10px; color: #ccc; background: #272727; border: 1px solid #fff; width: 20%; height: 32px;" />
            </div>
            <div class="row" style="width: 100%; margin: unset; padding: 0px 0px 0px 0%;">
                <table class="table table-bordered table-hover-dark table-sm tblInf" id="tblInf1">
                    <thead>
                        <tr>
                            <th style="width: 10%">STT</th>
                            <th style="width: 30%">Model Name</th>
                            <th style="width: 35%">Customer Name</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <table class="table table-bordered table-hover-dark table-sm hidden" id="tblInf2" style="margin: 0px 7px">
                    <thead>
                        <tr>
                            <th style="width: 10%">STT</th>
                            <th style="width: 30%">Model Name</th>
                            <th style="width: 35%">Customer Name</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
                <table class="table table-bordered table-hover-dark table-sm hidden" id="tblInf3">
                    <thead>
                        <tr>
                            <th style="width: 10%">STT</th>
                            <th style="width: 30%">Model Name</th>
                            <th style="width: 35%">Customer Name</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<style>
    .tblInf{
        color: #ccc;
        width: 33%;
        text-align: center;
        float: left;
    }
    .tblInf thead tr th{
        background: #333;
        color: #fff;
    }

    .table-sm > thead > tr > th, 
    .table-sm > tbody > tr > th, 
    .table-sm > tfoot > tr > th, 
    .table-sm > thead > tr > td, 
    .table-sm > tbody > tr > td, 
    .table-sm > tfoot > tr > td{
        padding: 3px 5px;
        font-size: 14px;
    }

    .btn{
        transition: all 0.5s;
    }

    .btn:hover{
        transform: translateY(-2px);
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
    }
    .btn:active{
        outline: none;
        transform: translateY(-1px);
        box-shadow: 0 5px 10px rgba(0, 0, 0, 0.2);
    }

    .header {
        text-align: left;
        font-size: 18px;
        margin: 5px;
        color: #fff;
        font-weight: 500;
    }
    .table-hover-dark > tbody > tr:hover {
        background-color: #444;
    }
</style>
<script>
    var dataset = {
        factory: '${factory}'
    }

    var dataModelName = [];
    loadData();
    function loadData(){
        $.ajax({
            type: 'GET',
            url: 'http://10.224.56.234:8888/api/smt/model/list',
            data: {
                factory: dataset.factory
            },
            success: function(data){
                createDataTable(data);
                dataModelName = data;
            },
            failure: function(errMsg) {
                console.log(errMsg);
            }
        });
    }  

    $('input[type=search]').on('input', function(){
       clearTimeout(this.delay);
       this.delay = setTimeout(function(){
          $(this).trigger('search');
       }.bind(this), 600);
    }).on('search', function(){
      $.ajax({
          type: 'GET',
          url: 'http://10.224.56.234:8888/api/smt/model/list',
          data: {
              factory: dataset.factory
          },
          success: function(data){
              var dataSearch = new Array();
              var search = ($("#txtSearch").val()).toString();

              for(var i=0; i<data.length; i++){
                  if((data[i].modelName).indexOf(search) != -1){
                      dataSearch.push(data[i]);
                  }
              }
              createDataTable(dataSearch);
          },
          failure: function(errMsg) {
              console.log(errMsg);
          }
      });
    });

    function createDataTable(data){
        $("#tblInf1 tbody tr").remove();
        $("#tblInf2 tbody tr").remove();
        $("#tblInf3 tbody tr").remove();

        var html = '';
        var stt = 0;
        if(data.length < 10){
            for(i in data){
                stt++;
                if(data[i].customerName == null){
                    data[i].customerName = "";
                }
                html = '<tr id="row'+data[i].id+'">'
                    +  '<td>'+stt+'</td>'

                    +  '<td><input id="txtEditModel'+data[i].id+'" class="form-control hidden" name="models" type="text" style="color: #fff" value="'+data[i].modelName+'" /><span id="modelName'+data[i].id+'">'+data[i].modelName+'</span></td>'
                    +  '<td><input id="txtEditCus'+data[i].id+'" class="form-control hidden" name="customers" type="text" style="color: #fff" value="'+data[i].customerName+'" /><span id="customerName'+data[i].id+'">'+data[i].customerName+'</span></td>'
                    +  '<td>'
                    +           '<button class="btn btn-warning" id="editRow'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+data[i].id+')">'
                    +               '<i class="icon icon-pencil"></i>'
                    +           '</button>'
                    +          '<button class="btn btn-danger" id="deleteRow'+data[i].id+'" title="Delete" style="padding: 4px 10px; margin-left: 5px;" onclick="deleteModel('+data[i].id+')">'
                    +               '<i class="icon icon-trash"></i>'
                    +           '</button>'
                    +           '<button class="btn btn-success hidden" id="confirmRow'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel('+data[i].id+')">'
                    +               '<i class="icon icon-checkmark"></i>'
                    +           '</button><button class="btn btn-danger hidden" id="cancelRow'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+data[i].id+')">'
                    +               '<i class="icon icon-spinner11"></i>'
                    +           '</button>'
                    +   '</td>'
                    + '</tr>';
                $("#tblInf1 tbody").append(html);
            }
            $("#tblInf2").addClass("hidden");
            $("#tblInf2").removeClass("tblInf");
            $("#tblInf3").addClass("hidden");
            $("#tblInf3").removeClass("tblInf");
        }
        else{
            var l = parseInt(data.length/3);
            if(l < data.length/3){
                l = l + 1;
            }
            var l2 = parseInt(data.length/3*2);
            if(l2 < data.length/3*2){
                l2 = l2 + 1;
            }

            for( var i = 0; i < l; i++){
                stt ++;
                if(data[i].customerName == null){
                    data[i].customerName = "";
                }
                html = '<tr id="row'+data[i].id+'">'
                    +  '<td>'+stt+'</td>'

                    +  '<td><input id="txtEditModel'+data[i].id+'" class="form-control hidden" name="models" type="text" style="color: #fff" value="'+data[i].modelName+'" /><span id="modelName'+data[i].id+'">'+data[i].modelName+'</span></td>'
                    +  '<td><input id="txtEditCus'+data[i].id+'" class="form-control hidden" name="customers" type="text" style="color: #fff" value="'+data[i].customerName+'" /><span id="customerName'+data[i].id+'">'+data[i].customerName+'</span></td>'
                    +  '<td>'
                    +           '<button class="btn btn-warning" id="editRow'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+data[i].id+')">'
                    +               '<i class="icon icon-pencil"></i>'
                    +           '</button>'
                    +          '<button class="btn btn-danger" id="deleteRow'+data[i].id+'" title="Delete" style="padding: 4px 10px; margin-left: 5px;" onclick="deleteModel('+data[i].id+')">'
                    +               '<i class="icon icon-trash"></i>'
                    +           '</button>'
                    +           '<button class="btn btn-success hidden" id="confirmRow'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel('+data[i].id+')">'
                    +               '<i class="icon icon-checkmark"></i>'
                    +           '</button><button class="btn btn-danger hidden" id="cancelRow'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+data[i].id+')">'
                    +               '<i class="icon icon-spinner11"></i>'
                    +           '</button>'
                    +   '</td>'
                    + '</tr>';
                $("#tblInf1 tbody").append(html);
            }

            var html2 = '';
            for( var i = l; i < l2; i++){
                stt ++;
                if(data[i].customerName == null){
                    data[i].customerName = "";
                }
                html2 = '<tr id="row'+data[i].id+'">'
                    +  '<td>'+stt+'</td>'

                    +  '<td><input id="txtEditModel'+data[i].id+'" class="form-control hidden" name="models" type="text" style="color: #fff" value="'+data[i].modelName+'" /><span id="modelName'+data[i].id+'">'+data[i].modelName+'</span></td>'
                    +  '<td><input id="txtEditCus'+data[i].id+'" class="form-control hidden" name="customers" type="text" style="color: #fff" value="'+data[i].customerName+'" /><span id="customerName'+data[i].id+'">'+data[i].customerName+'</span></td>'
                    +  '<td>'
                    +           '<button class="btn btn-warning" id="editRow'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+data[i].id+')">'
                    +               '<i class="icon icon-pencil"></i>'
                    +           '</button>'
                    +          '<button class="btn btn-danger" id="deleteRow'+data[i].id+'" title="Delete" style="padding: 4px 10px; margin-left: 5px;" onclick="deleteModel('+data[i].id+')">'
                    +               '<i class="icon icon-trash"></i>'
                    +           '</button>'
                    +           '<button class="btn btn-success hidden" id="confirmRow'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel('+data[i].id+')">'
                    +               '<i class="icon icon-checkmark"></i>'
                    +           '</button><button class="btn btn-danger hidden" id="cancelRow'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+data[i].id+')">'
                    +               '<i class="icon icon-spinner11"></i>'
                    +           '</button>'
                    +   '</td>'
                    + '</tr>';
                $("#tblInf2 tbody").append(html2);
            }
            $("#tblInf2").removeClass("hidden");
            $("#tblInf2").addClass("tblInf");

            var html3 = '';
            for( var i = l2; i < data.length; i++){
                stt ++;
                if(data[i].customerName == null){
                    data[i].customerName = "";
                }
                html3 = '<tr id="row'+data[i].id+'">'
                    +  '<td>'+stt+'</td>'

                    +  '<td><input id="txtEditModel'+data[i].id+'" class="form-control hidden" name="models" type="text" style="color: #fff" value="'+data[i].modelName+'" /><span id="modelName'+data[i].id+'">'+data[i].modelName+'</span></td>'
                    +  '<td><input id="txtEditCus'+data[i].id+'" class="form-control hidden" name="customers" type="text" style="color: #fff" value="'+data[i].customerName+'" /><span id="customerName'+data[i].id+'">'+data[i].customerName+'</span></td>'
                    +  '<td>'
                    +           '<button class="btn btn-warning" id="editRow'+data[i].id+'" title="Edit" style="background-color: #FFC107; padding: 4px 10px;" onclick="edit('+data[i].id+')">'
                    +               '<i class="icon icon-pencil"></i>'
                    +           '</button>'
                    +          '<button class="btn btn-danger" id="deleteRow'+data[i].id+'" title="Delete" style="padding: 4px 10px; margin-left: 5px;" onclick="deleteModel('+data[i].id+')">'
                    +               '<i class="icon icon-trash"></i>'
                    +           '</button>'
                    +           '<button class="btn btn-success hidden" id="confirmRow'+data[i].id+'" title="Confirm" style="padding: 4px 10px;" onclick="confirmModel('+data[i].id+')">'
                    +               '<i class="icon icon-checkmark"></i>'
                    +           '</button><button class="btn btn-danger hidden" id="cancelRow'+data[i].id+'" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelEdit('+data[i].id+')">'
                    +               '<i class="icon icon-spinner11"></i>'
                    +           '</button>'
                    +   '</td>'
                    + '</tr>';
                    $("#tblInf3 tbody").append(html3);
            }
            $("#tblInf3").removeClass("hidden");
            $("#tblInf3").addClass("tblInf");
        }
    }
    

    function setDataModels(){
        var models =  $('input[name="models"]');
        for(var i=0; i<models.length; i++){
            dataModelName.push(models[i].value);
        }
    }

    function save() {
        console.log(dataModelName);
        var data = {
            factory: dataset.factory,
            //models: $('input[name="models"]').tagsinput('items')
            models: dataModelName
        }

        $.ajax({
            type: 'POST',
            url: 'http://10.224.56.234:8888/api/smt/model/list',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                // $('button[name="save"]').attr('disabled', 'disabled');
                // $('button[name="cancel"]').attr('disabled', 'disabled');
                cancel();
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }

    function cancel() {
        location.reload();
    }

    function addNewClick(){
        $('#btnAdd').attr('disabled', 'disabled');
        var qtyTable = $('.tblInf');
        var lastTable;
        for(var i=0; i<qtyTable.length; i++){
            lastTable = qtyTable[i].id;
        }
        var html = '<tr id="newRow"><td></td><td><input id="txtAddModel" class="form-control" type="text" style="color: #fff" /></td>'
                 + '</td><td><input id="txtAddCus" class="form-control" type="text" style="color: #fff" /></td>'
                 + '<td><button class="btn btn-success" title="Add" style="padding: 4px 10px;" onclick="addNew()"><i class="icon icon-checkmark"></i></button>'
                 + '<button class="btn btn-danger" title="Cancel" style="padding: 4px 10px; margin-left: 5px;" onclick="cancelAddNew()"><i class="icon icon-cross"></i></button></td></tr>';
        $("#"+lastTable+" tbody").append(html);
        $("#txtAddModel").focus();
    }

    function addNew(){
        $("#txtAdd").addClass("hidden");
        var data = {
            factory: dataset.factory,
            customerName: $("#txtAddCus").val(),
            modelName: $("#txtAddModel").val()
        }

        $.ajax({
            type: 'POST',
            url: 'http://10.224.56.234:8888/api/smt/model/list',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                alert('ADD NEW OK!');
                cancel();
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }

    function cancelAddNew(){
        $("#newRow").remove();
        $('#btnAdd').removeAttr('disabled');
    }

    function edit(stt){
        $("#deleteRow"+stt).addClass("hidden");
        $("#editRow"+stt).addClass("hidden");
        $("#confirmRow"+stt).removeClass("hidden");
        $("#cancelRow"+stt).removeClass("hidden");

        $("#customerName"+stt).addClass("hidden");
        $("#txtEditCus"+stt).removeClass("hidden");
        $("#modelName"+stt).addClass("hidden");
        $("#txtEditModel"+stt).removeClass("hidden");
    }
    function cancelEdit(stt){
        $("#deleteRow"+stt).removeClass("hidden");
        $("#editRow"+stt).removeClass("hidden");
        $("#confirmRow"+stt).addClass("hidden");
        $("#cancelRow"+stt).addClass("hidden");

        $("#customerName"+stt).removeClass("hidden");
        $("#txtEditCus"+stt).addClass("hidden");
        $("#modelName"+stt).removeClass("hidden");
        $("#txtEditModel"+stt).addClass("hidden");
    }

    function confirmModel(stt){
        var data = {
            id: stt,
            factory: dataset.factory,
            customerName: $("#txtEditCus"+stt).val(),
            modelName: $("#txtEditModel"+stt).val()
        }

        $.ajax({
            type: 'POST',
            url: 'http://10.224.56.234:8888/api/smt/model/list',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                alert('UPDATE OK!');
                cancel();
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
    }

    function deleteModel(stt){
        var del = window.confirm("Do you want to DELETE?");
        if(del == true){
            var data = {
                id: stt,
                factory: dataset.factory,
                customerName: $("#txtEditCus"+stt).val(),
                modelName: $("#txtEditModel"+stt).val()
            }
            $.ajax({
            type: 'DELETE',
            url: 'http://10.224.56.234:8888/api/smt/model/list',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data){
                alert('DELETE OK!');
                cancel();
            },
            failure: function(errMsg) {
                 console.log(errMsg);
            },
        });
        }
    }
</script>