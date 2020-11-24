<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page trimDirectiveWhitespaces="true"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <title>${title}</title>
    <link rel="icon" type="image/ico" href="/favicon.ico">

    <!-- Global stylesheets -->
    <link rel="stylesheet" type="text/css" href="/assets/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/core.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/components.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/colors.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/icons/icomoon/styles.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/icons/fontawesome/styles.min.css">
    <!-- /global stylesheets -->
    <link rel="stylesheet" href="/assets/tab/tab-scrolltab.css">
    <!-- <script type="text/javascript" src="/assets/js/core/libraries/jquery.min.js"></script> -->

    <!-- Core JS files -->
    <script type="text/javascript" src="/assets/js/core/libraries/jquery.min.js"></script>
    <script type="text/javascript" src="/assets/js/core/libraries/bootstrap.min.js"></script>
    <script type="text/javascript" src="/assets/js/core/app.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/loaders/pace.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/loaders/blockui.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/uploaders/fileinput.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/ui/headroom/headroom.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/ui/headroom/headroom_jquery.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/ui/nicescroll.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/ui/ripple.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/forms/selects/bootstrap_select.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/forms/tags/tagsinput.min.js"></script>
    <script type="text/javascript" src="/assets/js/core/layout_navbar_hideable_sidebar.js"></script>

    <script type="text/javascript" src="/assets/tab/jquery-ui.min.js"></script>
    <!-- /core JS files -->

    <!-- highchart -->
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/highcharts.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/highcharts-more.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/histogram-bellcurve.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/series-label.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/drilldown.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/exporting.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/export-data.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/offline-exporting.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/modules/no-data-to-display.js"></script>
    <!-- <script type="text/javascript" src="/assets/js/plugins/charts/highchart/themes/dark-unica.js" </script> -->
    <script type="text/javascript" src="/assets/js/plugins/charts/highchart/plugins/grouped-categories.js"></script>
    <!-- /highchart -->

    <!-- /daterangepicker -->
    <link rel="stylesheet" type="text/css" href="/assets/css/plugins/pickers/daterangepicker.css">
    <script type="text/javascript" src="/assets/js/plugins/ui/moment/moment.min.js"></script>
    <script type="text/javascript" src="/assets/js/plugins/pickers/daterangepicker.min.js"></script>
    <!-- daterangepicker -->

    <script type="text/javascript" src="/assets/js/plugins/notify/bootstrap-notify.min.js"></script>

    <link rel="stylesheet" type="text/css" href="/assets/css/custom/slider_home.css">
    <script type="text/javascript" src="/assets/js/core/libraries/jssor.slider-27.5.0.min.js"></script>

    <!-- Pagination -->
    <link rel="stylesheet" type="text/css" href="/assets/js/plugins/pagination/simplePagination.css">
    <script type="text/javascript" src="/assets/js/plugins/pagination/jquery.simplePagination.js"></script>
    <!-- <script type="text/javascript" src="/assets/js/plugins/pagination/pagination.min.js"></script> -->
    <!-- custom -->
    <script type="text/javascript" src="/assets/js/mobile/icivet.min.js"></script>
    <script type="text/javascript" src="/assets/js/custom/picker_date.js"></script>
    <script type="text/javascript" src="/assets/js/custom/chart.js"></script>

    <link rel="stylesheet" href="/assets/css/custom/style.css">
    <!-- custom -->

    <link rel="stylesheet" href="/assets/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="/assets/js/bootstrap/bootstrap-datetimepicker.min.js"></script>

</head>
<style>
    .class-image{
        background-size: cover;
    -webkit-background-size: cover;
    -moz-background-size: cover;
    -o-background-size: cover;
    -ms-background-size: cover;
    }
    @media only screen and (min-width: 1280px) and (max-width: 1365px) {
    .reponsive-footer {
      font-size: 1.2rem;
      font-family: " Segoe UI", Arial, sans-serif; } } @media only screen and (min-width: 1366px) and (max-width:
        1599px) { .reponsive-footer { font-size: 1.2rem; font-family: "Segoe UI" , Arial, sans-serif; } } @media only
        screen and (min-width: 1600px) and (max-width: 1919px) { .reponsive-footer { font-size: 1.2rem;
        font-family: "Segoe UI" , Arial, sans-serif; } } @media only screen and (min-width: 1920px) { .reponsive-footer
        { font-size: 1.4rem; font-family: "Segoe UI" , Arial, sans-serif; } } 
    .navbar{
        background: #001940;
    }
    .content-wrapper{
        background: #04132a;
    }
    .footer{
        background: #04132a
    }
</style>

<body class="navbar-top sidebar-xs">

    <%@ include file="common/commonETE/navbarETE.jsp" %>

    <!-- Page container -->
    <div class="page-container">

        <!-- Page content -->
        <div class="page-content">

            <%@ include file="common/commonETE/sidebarETE.jsp" %>

            <!-- Main content -->
            <div class="content-wrapper">
                <div class="scroller scroller-left"><i class="glyphicon glyphicon-chevron-left"></i></div>
                <div class="scroller scroller-right"><i class="glyphicon glyphicon-chevron-right"></i></div>
                <div class="wrapper" id="idWrapper">
                    <div id="myTab" class="tab mTab list hiddenImage" style="height: 37px;background: #263546;">
                    </div>
                </div>
                <!-- Content area -->
                <div class="content" style="padding:3px 2px 0px 3px;">
                    <div class="tocontentfii">
                        <div class="col-lg-12">
                            <%@ include file="router.jsp" %>
                        </div>
                    </div>
                </div>
                <div class="hiddenImage content-menu-tab">
                </div>
                <!-- /content area -->

            </div>
            <!-- /main content -->

        </div>
        <!-- /page content -->

    </div>
    <!-- /page container -->
    <div class="footer text-muted reponsive-footer" style="bottom: 0px; left: 0px;">
        &copy; 2018. <a href="#">FII</a> by <a href="#" target="_blank">Icarus </a>cpe-vn-fii-sw@mail.foxconn.com
    </div>

    <script type="text/javascript" src="/assets/tab/tab-scrolltab.js"></script>
    <script src="/assets/js/custom/error.js?v=1"></script>
    <script src="/assets/js/custom/status.js?v=1"></script>
</body>
<style>
    .tocontentfii {
        /* display: flex; */
        height: 100%;
    }

    .hiddenImage {
        display: none;
    }

    .scroll-smail {
        background: #272727;
        height: 100%;
        width: 100%;
        background: #F5F5F5;
        overflow-y: scroll;
    }

    #scroll-smail::-webkit-scrollbar-track {
        -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
        background-color: #212529;
    }

    #scroll-smail::-webkit-scrollbar {
        width: 0px;
        background-color: #212529;
    }

    #scroll-smail::-webkit-scrollbar-thumb {
        background-color: #212529;
    }
</style>

</html>