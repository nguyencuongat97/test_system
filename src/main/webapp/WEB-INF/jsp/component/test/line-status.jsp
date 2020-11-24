<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<style>
    .form-xs {
        height: 26px;
    }
    .btn-xs {
        width: 26px;
        height: 26px;
        padding: 3px;
        margin: 0px 2px;
    }
    .sm-padding {
        margin-bottom: 5px;
    }
    .resource {
        padding: 5px;
    }
    .resource .rcontent {
        width: 100%;
        height: max-content;
        display: table;
        background-color: #fff;
        border-radius: 3px;
        border: solid 1px rgba(0, 0, 0, 0.2);
    }
    .resource .information .avatar {
        width: 70px;
        height: 70px;
        position: relative;
        top: 15px;
    }
    .resource .information .rank {
        width: 26px;
        height: 26px;
        position: relative;
        top: 38px;
        padding: 3px 5px;
        margin-left: 5px;
        background-color: #7cb5ec;
        display: table;
        text-align: center;
    }
    .resource .information .rank.rank-1 {
        background-color: #e6717c;
    }
    .resource .information .rank.rank-2 {
        background-color: #ffda6a;
    }
</style>
<div class="row" style="padding: 10px; background: #fff;">
    <div>
        <div class="col-sm-1">
            <label class="text-bold text-center"  style="width: 100%;">H1</label>
        </div>
        <div class="row col-sm-11">
            <div class="col-sm-3" style="border-left: solid 1px;">
                <label class="text-bold text-center"  style="width: 100%;">WT</label>
                <div class="row">
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #c4d4a8; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #e6717c; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #ffda6a; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #e0e0e0; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-sm-4" style="text-align: center;">
                        <div class="avatar" style="width: 100%; padding-top: 100%; border: solid 1px black; position: relative; display: inline-block;">
                            <img class="avatar" src="/assets/images/avatar-default-icon.png" alt="" style="position: absolute; width: 98%; top: 0; left: 0">
                            <div style="width: 100%; height: 25px; border-top: solid 1px black; position: absolute; bottom: 0px; background-color: #e6717c; opacity: 0.75;"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-3" style="border-left: solid 1px;">
                <label class="text-bold text-center"  style="width: 100%;">WT</label>
                <div class="row">
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #c4d4a8; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #e6717c; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #ffda6a; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #e0e0e0; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-sm-4" style="text-align: center;">
                        <div class="avatar" style="width: 100%; padding-top: 100%; border: solid 1px black; position: relative; display: inline-block;">
                            <img class="avatar" src="/assets/images/avatar-default-icon.png" alt="" style="position: absolute; width: 98%; top: 0; left: 0">
                            <div style="width: 100%; height: 25px; border-top: solid 1px black; position: absolute; bottom: 0px; background-color: #e6717c; opacity: 0.75;"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-3" style="border-left: solid 1px;">
                <label class="text-bold text-center"  style="width: 100%;">WT</label>
                <div class="row">
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #c4d4a8; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #e6717c; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #ffda6a; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #e0e0e0; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-sm-4" style="text-align: center;">
                        <div class="avatar" style="width: 100%; padding-top: 100%; border: solid 1px black; position: relative; display: inline-block;">
                            <img class="avatar" src="/assets/images/avatar-default-icon.png" alt="" style="position: absolute; width: 98%; top: 0; left: 0">
                            <div style="width: 100%; height: 25px; border-top: solid 1px black; position: absolute; bottom: 0px; background-color: #e6717c; opacity: 0.75;"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-3" style="border-left: solid 1px;">
                <label class="text-bold text-center"  style="width: 100%;">WT</label>
                <div class="row">
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #c4d4a8; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #e6717c; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #ffda6a; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                    <div class="col-sm-3" style="text-align: center;">
                        <div class="circle" style="width: 100%; padding-top: 100%; border-radius: 50%; display: inline-block; border: solid 1px; background-color: #e0e0e0; position: relative;">
                            <label style="position: absolute;top: 40%;left: 45%;">5</label>
                        </div>
                    </div>
                </div>
                <div class="row" style="padding-top: 10px;">
                    <div class="col-sm-4" style="text-align: center;">
                        <div class="avatar" style="width: 100%; padding-top: 100%; border: solid 1px black; position: relative; display: inline-block;">
                            <img class="avatar" src="/assets/images/avatar-default-icon.png" alt="" style="position: absolute; width: 98%; top: 0; left: 0">
                            <div style="width: 100%; height: 25px; border-top: solid 1px black; position: absolute; bottom: 0px; background-color: #e6717c; opacity: 0.75;"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    var dataset = {
        factory: '${factory}'
    }

</script>

