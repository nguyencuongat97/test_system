<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<script type="text/javascript" src="/assets/js/custom/item-about.js"></script>
<style>
    body {
        background: url(../assets/images/custom/home/bg_body_new_a.png)no-repeat right top fixed;
        background-size: cover;
        -webkit-background-size: cover;
        -moz-background-size: cover;
        -o-background-size: cover;
        -ms-background-size: cover;
        background-attachment: fixed;
    }

    .page-container {
        background: #EBEDF9 !important;
    }

    @media screen and (max-width: 799px) {
        #item-web li {
            width: 100%;
        }

        #item-app li {
            width: 100%;
        }

        .txt-tilte-page {
            font-size: 23px;
        }

        .bg-content {
            background-color: #e0e3f5;
            font-size: 14px;
            margin-bottom: 5px;
            margin-left: 14px;
            padding: 5px;
            border-radius: 2px;
            border: 1px solid #e0e3f5;

        }

        .logo-fii {
            display: none;
        }

        .css-footer-add {
            display: none;
        }
        .icon-title-modal{
            display: none;
        }
    }

    @media screen and (min-width: 800px) and (max-width: 1024px) {
        #item-web li {
            width: 393px;
        }

        #item-app li {
            width: 394px;
        }

        .txt-tilte-page {
            font-size: 23px;
        }

        .bg-content {
            background-color: #e0e3f5;
            font-size: 14px;
            margin-bottom: 5px;
            margin-left: 14px;
            padding: 5px;
            border-radius: 2px;
            border: 1px solid #e0e3f5;

        }
    }

    @media only screen and (min-width: 1024px) and (max-width: 1280px) {
        #item-web li {
            width: 234px;
        }

        #item-app li {
            width: 150px;
        }

        .txt-tilte-page {
            font-size: 23px;
        }

        .bg-content {
            background-color: #e0e3f5;
            font-size: 14px;
            margin-bottom: 5px;
            margin-left: 14px;
            padding: 5px;
            border-radius: 2px;
            border: 1px solid #e0e3f5;

        }
    }

    @media only screen and (min-width: 1280px) and (max-width: 1365px) {
        #item-web li {
            width: 255px;
        }

        #item-app li {
            width: 150px;
        }

        .txt-tilte-page {
            font-size: 25px;
        }

        .bg-content {
            background-color: #e0e3f5;
            font-size: 14px;
            margin-bottom: 5px;
            margin-left: 14px;
            padding: 5px;
            border-radius: 2px;
            border: 1px solid #e0e3f5;

        }
    }

    @media only screen and (min-width: 1366px) and (max-width: 1599px) {
        #item-web li {
            width: 255px;
        }

        #item-app li {
            width: 150px;
        }

        .txt-tilte-page {
            font-size: 25px;
        }

        .bg-content {
            background-color: #e0e3f5;
            font-size: 14px;
            margin-bottom: 5px;
            margin-left: 14px;
            padding: 5px;
            border-radius: 2px;
            border: 1px solid #e0e3f5;

        }
    }

    @media only screen and (min-width: 1600px) and (max-width: 1919px) {
        #item-web li {
            width: 304px;
        }

        #item-app li {
            width: 170px;
        }

        .txt-tilte-page {
            font-size: 30px;
        }

        .bg-content {
            background-color: #e0e3f5;
            font-size: 17px;
            margin-bottom: 10px;
            margin-left: 15px;
            padding: 8px;
            border-radius: 2px;
            border: 1px solid #e0e3f5;

        }
    }

    @media only screen and (min-width: 1920px) {
        #item-web li {
            width: 304px;
        }

        #item-app li {
            width: 170px;
        }

        .txt-tilte-page {
            font-size: 30px;
        }

        .bg-content {
            background-color: #e0e3f5;
            font-size: 17px;
            margin-bottom: 10px;
            margin-left: 15px;
            padding: 8px;
            border-radius: 2px;
            border: 1px solid #e0e3f5;

        }
    }

    .css-item-web-res {
        width: 578px;
        height: 330px;

    }

    #item-web li {
        list-style-type: none;
        float: left;
        margin-right: 10px;
    }

    #item-app li {
        list-style-type: none;
        float: left;
        margin-right: 10px;
    }

    .content-about {
        background-color: #ebedf9;
        max-width: 100%;
        color: #000000;
    }

    .card {
        box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
        margin-bottom: 1rem;
    }

    .card {
        position: relative;
        display: -ms-flexbox;
        display: flex;
        -ms-flex-direction: column;
        flex-direction: column;
        min-width: 0;
        word-wrap: break-word;
        background-color: #fff;
        background-clip: border-box;
        border-radius: .25rem;
        border: 1px solid #d7d7d7;
    }

    .text-web {
        margin: 8px;
    }

    .icon-star {
        color: #737373;
    }

    .content-title-web {
        font-weight: 500;
        font-size: 15px;
        color: #1E88E5;
    }

    .content-image {
        position: relative;
        width: 100%;
    }

    .image {
        display: block;
        height: auto;
    }

    .overlay {
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        height: 100%;
        width: 100%;
        opacity: 0;
        transition: .3s ease;
        text-align: center;
        margin: auto;
        background-color: #d7d7d7;
    }

    .overlay:hover {
        cursor: pointer;
    }

    .content-image:hover .overlay {
        opacity: 0.75;
    }

    .fa-user:hover {
        color: #eee;
    }

    .title-modal {
        margin-left: 20px;
        margin-top: -10px;
    }

    .icon-title-modal {
        background: #DFDFDF;
        border: 1px solid #DFDFDF;
        border-radius: 15px;
        padding: 5px;
    }

    .icon-yes-android {
        width: 50px;
        height: auto;
        float: right;
    }

    .margin-icon {
        margin-right: 8px;
        color: #F88C07;
    }

    .btn-go-website {
        float: right;
        background: #007BFF;
        color: #fff;
        border: 1px solid #007BFF;
        border-radius: 3px;
    }

    .color-red {
        color: #737373;
    }

    .text-app {
        border-top: 1px solid #d7d7d7;
        padding: 6px;
    }

    .text_limit {
        text-align: center;
        color: black;
        height: 10%;
        font-size: 16px;
        margin: auto;
        font-weight: 600;
    }

    p {
        margin: 0 0 1px;
    }

    .txt-tilte-page {

        text-align: center;
    }

    .img-about {
        transform: rotate(180deg);
        margin-left: 15px;

    }

    .logo-fii {
        position: absolute;
        right: 20px;
        width: 143px;
        top: 10px;
        height: 30px;
    }

    .text-proceduce {
        font-size: 20px;
        margin-left: 20px;
    }

    .css-icon {
        font-size: 24px;
        margin-right: 8px;
        color: #328bea;
    }

    @keyframes rotate360 {
        to {
            transform: rotate(360deg);
        }
    }

    .fa-rotate-45 {
        animation: 5s rotate360 infinite linear;
    }

    .css-icon-app {
        font-size: 24px;
        margin-right: 8px;
        color: #33c374;
    }



    .fa-rotate-45 {
        -webkit-transform: rotate(45deg);
        -moz-transform: rotate(45deg);
        -ms-transform: rotate(45deg);
        -o-transform: rotate(45deg);
        transform: rotate(45deg);
    }

    .icon-des {
        font-size: 20px;
        margin-right: 6px;
        color: #007BFF;
    }

    /* css mai khong xong, buc minh vl */
</style>
<div class="row content-about">
    <div class="col-md-12" style="text-align: center; margin-top: 10px;">
        <span class="txt-tilte-page"><img src="/assets/images/custom/bkg_title.png" style="margin-right: 15px;" />About
            us<img class="img-about" src="/assets/images/custom/bkg_title.png" /></span>
        <!-- <marquee class="col-md-1" style="float: right;" width="100%" behavior="scroll"> -->
        <img class="logo-fii" src="/assets/images/custom/20180912_fii_logo_update.png" />
        <!-- </marquee> -->
    </div>
    <div class="col-md-12" style="margin-top: 8px;">
        <p class="bg-content">
            <span style="color: #007BFF; font-size: 17px;font-style: italic;">FII - Foxconn
                Industrial Internet</span> is a world's leading professional design and
            manufacturing service
            provider of communication network equipment, cloud service equipment, precision tools and industrial
            robots,
            providing customers with intelligent manufacturing services for new forms of electronic equipment
            products
            centered on the industrial Internet platform.
        </p>
        <p class="bg-content"><span style="color: #007BFF; font-size: 17px;">FII</span> is committed to providing
            enterprises with
            comprehensive solutions of technology services based on
            automation, network, platform and big data, leading the transformation from traditional manufacturing to
            intelligent manufacturing.</p>

        <p class="bg-content">
            <span style="color: #007BFF; font-size: 17px;">FII</span> is dedicated to building a new ecosystem of
            “advanced manufacturing + industrial Internet”, with
            technological platforms centered on cloud computing, mobile terminals, Internet of Things (IoT), Big data,
            artificial intelligence, high-speed networks and robots.
        </p>
        <p class="bg-content">
            The company is engaged in the design, research and development, manufacturing, and sales of various
            electronic equipment products. It relies on Industrial Internet to provide intelligent manufacturing and
            technology service solutions to world-renowned customers. Fii's main products cover communication network
            equipment, cloud service equipment, precision tools and industrial robots.
        </p>
        <p class="bg-content">

            Related products are used in smart phones, broadband and wireless networks, infrastructure of multimedia
            service operators, infrastructure of telecom operators, terminal products required by Internet value-added
            service providers, infrastructure of enterprise networks and data centers, and the automatic manufacturing
            of precision core components.
        </p>


    </div>
    <div class="col-md-12" style="margin-top: 10px;">
        <p class="text-proceduce"><i class="fa fa-globe css-icon fa-rotate-45"></i>Website</p>
        <div class="row" style="margin-top: 8px;">
            <ul class="item-web" id="item-web">

            </ul>
        </div>
    </div>
    <div class="col-md-12">
        <p class="text-proceduce"><i class="fa fa-android css-icon-app"></i>App</p>
        <div class="row" style="margin-top: 5px;">
            <ul class="item-app" id="item-app">
            </ul>
        </div>
    </div>

    <div class="col-md-12 css-footer-add" style="position: absolute; bottom: 0px;right: 10px;">
        <div style="float: right; font-size: 15px;">
            <span><span><i class="fa fa-copyright" style="font-size: 17px; color: #007BFF;"></i></span> 2018. <span
                    style="color: #265FD9;font-weight: 500;">FII</span> by <span
                    style="color:#265FD9; margin-right: 5px;font-weight: 500;">Icarus</span>cpe-vn-fii-sw@mail.foxconn.com</span>
        </div>
    </div>
</div>


<script>
    var positionWeb = -1;
    var positionApp = -1;
    var data = websites;
    var dataApp = app;

    loadDataTabWeb();
    loadDataApp();
    function loadDataTabWeb() {
        var htmlItem = "";
        var tmpI = 6;
        for (var i = 0; i < data.length; i++) {
            var itemApp = "";
            if (data[i]['app']['android'].trim().length > 0) {
                itemApp = '<a href="' + data[i]['app']['android'] + '"><img class="icon-yes-android" src="/assets/images/custom/icon_app.png"></a>';
            }
            htmlItem += '<li>' +
                '<div class="card">' +
                '<div class="card-body">' +
                '<div class="content-image">' +
                '<img class="image" src="' + data[i]['image'] + '" width="100%">' +
                '<div class="overlay btn-show-detail" data-toggle="modal" data-target="#myModal" position-view="' + i + '">' +
                '<span><i class="fa fa-arrow-circle-right" style="padding: 50px 0; font-size: 50px;"></i></span>' +
                '</div>' +
                '</div>' +
                '<div class="text-web">' +
                '<p class="content-title-web"><a href="' + data[i]['link_website'] + '">' + data[i]['name'] + '</a></p>' +
                '<span>SOP:</span><span style="margin-left: 5px;"><a href="' + data[i]['sop_link'] + '">Link download</a></span>' +
                itemApp +
                '</div>' +
                '</div>' +
                '</div>' +
                '</li>';
        }
        $("#item-web").html(htmlItem);
        clickBtnShowDetail();
    }

    function clickBtnShowDetail() {
        $(".btn-show-detail").click(function () {
            positionWeb = $(this).attr("position-view");
            loadDataModel();
        });
    }

    function loadDataModel() {
        $(".m-web #name").html(data[positionWeb]['name']);
        $(".m-web #sop_link").attr("href", data[positionWeb]['sop_link']);
        $(".m-web #tel_support").html(data[positionWeb]['tel_support']);
        $(".m-web #mail").html(data[positionWeb]['mail']);
        $(".m-web #logo").attr("src", data[positionWeb]['logo']);
        $(".m-web #description").html(data[positionWeb]['description']);
        $(".m-web #link-website").attr("href", data[positionWeb]['link_website']);
        var itemDowApp = "";
        if (data[positionWeb]['app']['android'].trim().length > 0) {
            itemDowApp = '<a href="' + data[positionWeb]['app']['android'] + '"><img style="margin:20px 0px 0px 0px" class="icon-yes-android" src="/assets/images/custom/icon_app.png"></a>';
        }
        $(".m-web #txt-show-icon").html(itemDowApp);

        var medias = data[positionWeb]['media'];

        var htmlSilde = "";
        var htmlDotSlide = "";
        for (var i = 0; i < medias.length; i++) {
            var classActive = "";
            if (i == 0) {
                classActive = " active";
            }
            htmlDotSlide += '<li data-target="#myCarousel" data-slide-to="' + i + '" class="' + classActive + '"></li>';
            htmlSilde += '<div class="item css-item-web-res' + classActive + '"><img src="' + medias[i] + '"  alt="Chania"></div>';
        }
        $(".m-web .view-slide-system").html(htmlSilde);
        $(".m-web .carousel-indicators").html(htmlDotSlide);
    }

    function loadDataApp() {
        var htmlItem = "";
        for (var i = 0; i < app.length; i++) {
            htmlItem += '<li>' +
                '<div class="card">' +
                '<div class="content-image">' +
                '<img data-u="image" src="' + app[i]['logo'] + '" style="margin: auto; width: 100%; margin-top: 8px; padding:5px;" />' +
                '<p class="text_limit">' + app[i]['name'] + '</p>' +
                '<table style="width: 90%;height: 15%; text-align: center; margin: auto; ">' +
                '<tr>' +
                '<td><i class="fa fa-star color-red"></i></td>' +
                '<td><i class="fa fa-star color-red"></i></td>' +
                '<td><i class="fa fa-star color-red"></i></td>' +
                '<td><i class="fa fa-star color-red"></i></td>' +
                '<td><i class="fa fa-star color-red"></i></td>' +
                '</tr>' +
                '</table>' +
                '<div class="overlay btn-show-detail-app" data-toggle="modal" data-target="#myModalapp" position-view-app="' + i + '">' +
                '<span><i class="fa fa-arrow-circle-right" style="padding: 50px 0; font-size: 50px;"></i></span>' +
                '</div>' +
                '</div>' +
                '<div class="text-app">' +
                '<span>SOP:</span><span style="margin-left: 5px;font-style: italic;"><a href="' + app[i]['sop_link'] + '">Link</a></span>' +
                '<span style="float: right;"><a href="' + app[i]['app']['android'] + '"><i class="fa fa-download" style="color: green;font-size: 18px;"></i></a></span>' +
                '</div>' +
                '</div>' +
                '</li>';
        }
        $("#item-app").html(htmlItem);
        clickBtnShowDetailApp();
    }

    function clickBtnShowDetailApp() {
        $(".btn-show-detail-app").click(function () {
            positionApp = $(this).attr("position-view-app");
            loadDataModelApp();
        })
    }


    function loadDataModelApp() {
        $(".m-app #name-app").html(app[positionApp]['name']);
        $(".m-app #sop_link_app").attr("href", app[positionApp]['sop_link']);
        $(".m-app #tel_support_app").html(app[positionApp]['tel_support']);
        $(".m-app #mail_app").html(app[positionApp]['mail']);
        $(".m-app #logo-app").attr("src", app[positionApp]['logo']);
        $(".m-app #description_app").html(app[positionApp]['description']);
        $(".m-app #link_down_app").attr("href", app[positionApp]['app']['android']);
        console.log(app[positionApp]);
        var medias = app[positionApp]['media'];
        var htmlItem = "";
        for (var i = 0; i < medias.length; i++) {
            htmlItem += '<td><img src="' + medias[i] + '" style="width: 277px; height: 456px;"></td>';
        }
        $("#tbl-item tr").html(htmlItem);
    }

</script>
<!-- Modal -->
<div id="myModal" class="modal fade m-web" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-3" style="margin: 0px; padding: 0px;">
                        <div class="icon-title-modal">
                            <img id="logo" data-u="image" src="/assets/images/custom/esd_safe_large.png"
                                style="width: 130px; height: 130px;" />
                        </div>
                    </div>
                    <div class="col-md-9" style="margin: 0px; padding: 0px;">
                        <div class="title-modal">
                            <div class="row col-md-12" style="margin: 0px; padding: 0px;">
                                <span style="font-size: 30px;" id="name">ESD System</span>
                                <span id="txt-show-icon"></span>
                            </div>
                            <div class="row col-md-12">
                                <table>
                                    <tr>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                    </tr>
                                </table>
                            </div>
                            <div class="row col-md-12" style="margin-top: 5px;">
                                <span>SOP: <a id="sop_link" href="">Link download</a></span>
                            </div>
                            <div class="row col-md-12" style="margin-top: 6px;">
                                <p><i class="fa fa-phone margin-icon"></i>Tel: <span id="tel_support">261</span></p>
                            </div>
                            <div class="row col-md-12" style="margin: 0px; padding: 0px;">
                                <span><i class="fa fa-envelope margin-icon"></i>Email: <span id="mail">261</span></span>
                                <button type="button" class="button btn-go-website" style="float: right;"><a href="#"
                                        id="link-website" style="color: #fff;">Go to Website</a></button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row" style="margin-top: 22px;">
                    <div id="myCarousel" class="carousel slide" data-ride="carousel">
                        <ol class="carousel-indicators">
                            <li data-target="#myCarousel" data-slide-to="0" class=""></li>
                            <li data-target="#myCarousel" data-slide-to="1" class=""></li>
                            <li data-target="#myCarousel" data-slide-to="2" class=""></li>
                            <li data-target="#myCarousel" data-slide-to="3" class="active"></li>
                        </ol>
                        <div class="carousel-inner view-slide-system">
                            <div class="item active">
                                <img src="/assets/images/custom/ESDcut.PNG" style="width: 578px; height: 277px;"
                                    alt="Chania">
                            </div>
                            <div class="item">
                                <img src="/assets/images/custom/Uthing.PNG" style="width: 578px; height: 277px;"
                                    alt="Chicago">
                                <div class="carousel-caption">
                                </div>
                            </div>
                            <div class="item">
                                <img src="/assets/images/custom/warning.PNG" style="width: 578px; height: 277px;"
                                    alt="New York">
                                <div class="carousel-caption">
                                </div>
                            </div>
                        </div>
                        <a class="left carousel-control" href="#myCarousel" data-slide="prev">
                            <span class="glyphicon glyphicon-chevron-left"></span>
                            <span class="sr-only">Previous</span>
                        </a>
                        <a class="right carousel-control" href="#myCarousel" data-slide="next">
                            <span class="glyphicon glyphicon-chevron-right"></span>
                            <span class="sr-only">Next</span>
                        </a>
                    </div>
                </div>
                <div class="row" style="margin-top: 8px 0px 8px 0px;">
                    <h6><i class="fa fa-info-circle icon-des"></i>Description</h6>
                </div>
                <div class="row" style="font-size: 15px; border: 1px solid #d7d7d7; padding: 5px; border-radius: 4px; "
                    id="description">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>
<!-- Modal APP -->
<div id="myModalapp" class="modal fade m-app" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-3" style="margin: 0px; padding: 0px;">
                        <div class="icon-title-modal">
                            <img id="logo-app" data-u="image" src="/assets/images/custom/esd_safe_large.png"
                                style="width: 130px; height: 130px;" />
                        </div>
                    </div>
                    <div class="col-md-9" style="margin: 0px; padding: 0px;">
                        <div class="title-modal">
                            <div class="row col-md-12" style="margin: 0px; padding: 0px;">
                                <span style="font-size: 30px;" id="name-app">ESD System</span>
                                <span id="txt-show-icon"></span>

                            </div>
                            <div class="row col-md-12">
                                <table>
                                    <tr>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                        <td><i class="fa fa-star icon-star"></i></td>
                                    </tr>
                                </table>
                            </div>
                            <div class="row col-md-12" style="margin-top: 5px;">
                                <span>SOP: <a id="sop_link_app" href="">Link download</a></span>
                            </div>
                            <div class="row col-md-12" style="margin-top: 6px;">
                                <p><i class="fa fa-phone margin-icon"></i>Tel: <span id="tel_support_app">261</span></p>
                            </div>
                            <div class="row col-md-12" style="margin: 0px; padding: 0px;">
                                <span><i class="fa fa-envelope margin-icon"></i>Email: <span
                                        id="mail_app">261</span></span>
                                <button type="button" class="button btn-go-website" style="float: right;"><a href="#"
                                        id="link_down_app" style="color: #fff;">Download</a></button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row" style="margin-top: 10px; overflow: scroll; width: 100%;">
                    <table style="border-collapse: separate;border-spacing: 10px 0px; margin-left: -10px;"
                        id="tbl-item">
                        <tr></tr>
                    </table>
                </div>
                <div class="row" style="margin-top: 8px 0px 8px 0px;">
                    <h6><i class="fa fa-info-circle icon-des"></i>Description</h6>
                </div>
                <div class="row" style="font-size: 15px; border: 1px solid #d7d7d7; padding: 5px; border-radius: 4px; "
                    id="description_app">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>

    </div>
</div>