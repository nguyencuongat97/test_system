<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<link rel="stylesheet" href="/assets/css/custom/te-report.css" />
<div class="loader"></div>
<div class="panel panel-re panel-flat row" style="margin-bottom: 0px; min-height: calc(100vh - 100px); background-color: #B7D2F2;">
    <div class="panel panel-overview no-margin" style="text-align:center; box-shadow: 1px 1px 1px 1px rgba(0, 0, 0, 0.12), 1px 1px 1px 1px rgba(0, 0, 0, 0.24);">
        <span style="font-size:16px;">TE REPORT</span>
    </div>
    <div class="col-sm-12">
        <div class="input-group" style="height: 26px; margin: 5px 0px; background-color: #FFF;">
            <span class="input-group-addon" style="padding: 0px 5px;"><i class="icon-calendar22"></i></span>
            <input type="text" class="form-control datetimerange" side="right" name="timeSpan" style="height: 26px; border-bottom: 0;">
        </div>
    </div>
    <div class="row no-margin pt-5">
        <div class="col-md-2 table-responsive list-model" style="max-height: calc(100vh - 150px);">
            <table id="tblModel" class="table table-striped table-sticky">
                <thead>
                    <tr>
                        <th>
                            <div class="panel input-group" style="margin: 3px 0; width: 100%;">
                                <input type="text" class="form-control" id="txtSearchModel" name="search" style="border-bottom: 0px !important; padding: 0 10px; height: 28px;" placeholder="Search Model">
                            </div>
                        </th>
                    </tr>
                </thead>
                <tbody></tbody>
            </table>
        </div>
        <div class="col-md-10 table-responsive" style="max-height: calc(100vh - 150px);">
            <table id="tblDetail" class="table table-xxs table-bordered table-sticky" style="text-align: center">
            </table>
        </div>
    </div>
</div>

<script type="text/javascript" src="/assets/js/custom/te-report.js"></script>
<script>
    var dataset = {
        factory: '${factory}'
    }

    $(document).ready(function() {
        getTimeNow();
    });
</script>
