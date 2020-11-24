function loadSelectCustomer(allFlag){
    $.ajax({
        type: "GET",
        url: "/api/test/sfc/customer",
        data: {
            factory: dataset.factory
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if(!$.isEmptyObject(data)){
                if(dataset.customer == "" || dataset.customer == undefined){
                    dataset.customer = data[0];
                    window.localStorage.setItem('dataset', JSON.stringify(dataset));
                }
                var list = '';
                if (allFlag != undefined && allFlag) {
                    list += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">All</a></li>';
                }
                for(i in data){
                    list += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
                }
                $(".select-customer").html(list);
                $("#titlePage").html(dataset.customer.toUpperCase());
            }
            $('.dropdown-select-customer').find('.dropdown-toggle').html(dataset.customer + ' <span class="caret"></span>');
            if (dataset.stage == "" || dataset.stage == undefined) {
                $('.dropdown-select-stage').find('.dropdown-toggle').html('Stage <span class="caret"></span>');
            } else $('.dropdown-select-stage').find('.dropdown-toggle').html(dataset.stage + ' <span class="caret"></span>');
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
        complete: function() {
            loadSelectStage();
            loadDataTypeShift(dataset.shiftType);
        }
    });
}

function loadSelectStage(){
    $.ajax({
        type: "GET",
        url: "/api/test/sfc/stage",
        data: {
            factory: dataset.factory,
            customer: dataset.customer
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
            var list = '';
            for(i in data){
                list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
            }
            $(".select-stage").html(list);
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
        complete: function() {
            loadSelectModel();
        }
    });
}

function loadSelectModel(){
    $.ajax({
        type: "GET",
        url: "/api/test/sfc/model",
        data: {
            factory: dataset.factory,
            customer: dataset.customer,
            stage: dataset.stage,
            workDate: dataset.workDate,
            shiftType: dataset.shiftType,
            timeSpan: dataset.timeSpan
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            if(!$.isEmptyObject(data)){
                if(dataset.factory != "nbb" && dataset.factory != "s03"){
                    if(dataset.modelName == "" || dataset.modelName == undefined){
                        dataset.modelName = data[0];
                        window.localStorage.setItem('dataset', JSON.stringify(dataset));
                    }
                    loadDataTypeShift(dataset.shiftType);
                }
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
                for(i in data){
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
                }
                $(".select-model").html(list);
                if (dataset.modelName == "" || dataset.modelName == undefined) {
                    $('.dropdown-select-model').find('.dropdown-toggle').html('Model Name <span class="caret"></span>');
                } else $('.dropdown-select-model').find('.dropdown-toggle').html(dataset.modelName + ' <span class="caret"></span>');
            
                var selector = $("select[name='modelName']");
                selector.children('option').remove();
                var options = "<option value=''>Select All</option>";
                for(i in data){
                    if(data[i] == dataset.modelName){
                        options+='<option value=' + data[i] + ' selected>' + data[i] + '</option>';
                        $('li[data-original-index="'+i+'"]').addClass("active");
                    }
                    else
                        options+='<option value=' + data[i] + '>' + data[i] + '</option>';
                }
                selector.append(options);
                selector.selectpicker('refresh');
            }
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
        complete: function() {
            loadSelectLine();
            loadSelectMO();
        }
    });
}

function loadSelectLine(){
    $.ajax({
        type: "GET",
        url: "/api/test/sfc/line",
        data: {
            factory: dataset.factory,
            workDate: dataset.workDate,
            shiftType: dataset.shiftType,
            customer: dataset.customer,
            stage: dataset.stage,
            modelName: dataset.modelName,
            //mo: dataset.mo
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="">Select All</a></li>';
            for(i in data){
                list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="'+i+'">'+ data[i] +'</a></li>';
                if(i == dataset.lineName){
                    $('.dropdown-select').find('.dropdown-toggle').html(data[i] + ' <span class="caret"></span>');
                }
            }
            $(".select-line").html(list);
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
    });
}

function loadSelectMO(){
    $.ajax({
        type: "GET",
        url: "/api/test/sfc/mo",
        data: {
            factory: dataset.factory,
            workDate: dataset.workDate,
            shiftType: dataset.shiftType,
            customer: dataset.customer,
            stage: dataset.stage,
            modelName: dataset.modelName,
        },
        contentType: "application/json; charset=utf-8",
        success: function(data){
            var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="">Select All</a></li>';
            for(i in data){
                list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="'+i+'">'+ data[i] +'</a></li>';
                if(data[i] == dataset.mo){
                    $('.dropdown-select-mo').find('.dropdown-toggle').html(data[i] + ' <span class="caret"></span>');
                }
            }
            $(".select-mo").html(list);
        },
        failure: function(errMsg) {
            console.log(errMsg);
        },
    });
}