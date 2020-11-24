function loadSelectCustomer(allFlag){
        $.ajax({
                type: "GET",
                url: "/api/test/"+dataset.factory+"/customer",
                contentType: "application/json; charset=utf-8",
                success: function(data){
                    var list = '';
                    if (allFlag != undefined && allFlag) {
                        list += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">All</a></li>';
                    }
                    for(i in data){
                        list += '<li role="presentation"><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
                    }
                    $(".select-customer").html(list);
                },
                failure: function(errMsg) {
                    console.log(errMsg);
                }
            });
        $("#titlePage").html(dataset.customer.toUpperCase());
    }

    function loadSelectStage(){
        $.ajax({
            type: "GET",
            url: "/api/test/"+dataset.factory+"/stage",
            data: {
                customer: dataset.customer
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
                for(i in data){
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
                }
                $(".select-stage").html(list);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }

    function loadSelectModel(){
        $.ajax({
            type: "GET",
            url: "/api/test/"+dataset.factory+"/model",
            data: {
                customer: dataset.customer,
                stage: dataset.stage,
                workDate: dataset.workDate,
                shiftType: dataset.shiftType,
                timeSpan: dataset.timeSpan
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">Select All</a></li>';
                for(i in data){
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#">'+ data[i] +'</a></li>';
                }
                $(".select-model").html(list);
            },
            failure: function(errMsg) {
                console.log(errMsg);
            },
        });
    }
    
    function loadSelectLine(){
        $.ajax({
            type: "GET",
            url: "/api/test/"+dataset.factory+"/line",
            data: {
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
                        // $("#titlePage").html(data[i].toUpperCase());
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
            url: "/api/test/"+dataset.factory+"/mo",
            data: {
                workDate: dataset.workDate,
                shiftType: dataset.shiftType,
                customer: dataset.customer,
                stage: dataset.stage,
                modelName: dataset.modelName,
                //lineName: dataset.lineName
            },
            contentType: "application/json; charset=utf-8",
            success: function(data){
                var list = '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="">Select All</a></li>';
                for(i in data){
                    list += '<li role="presentation" ><a role="menuitem" tabindex="-1" href="#" name="'+i+'">'+ data[i] +'</a></li>';
                    if(i == dataset.mo){
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