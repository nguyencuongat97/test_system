<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<style>
  body {
    font-size: 100%;
    background: url(../assets/images/custom/home/bg_body_new_a.png)no-repeat right top fixed;
    background-size: cover;
    -webkit-background-size: cover;
    -moz-background-size: cover;
    -o-background-size: cover;
    -ms-background-size: cover;
    background-attachment: fixed;
  }
  @media screen and (max-width: 1024px){
    .txt-fii-sw {
      font-size: 3.5rem;
      line-height: 41px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .txt-desc-fii-sw {
      font-size: 1.6rem;
      line-height: 17px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .text-size {
      font-size: 1.8rem;
      line-height: 20px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }
    
  }

  @media only screen and (min-width: 1024px) and (max-width: 1280px) {
    .txt-fii-sw {
      font-size: 3.5rem;
      line-height: 41px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .txt-desc-fii-sw {
      font-size: 1.6rem;
      line-height: 17px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .text-size {
      font-size: 1.8rem;
      line-height: 20px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }
  }

  @media only screen and (min-width: 1280px) and (max-width: 1365px) {
    .txt-fii-sw {
      font-size: 3.3rem;
      line-height: 41px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .txt-desc-fii-sw {
      font-size: 1.6rem;
      line-height: 17px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .text-size {
      font-size: 1.8rem;
      line-height: 20px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }
  }

  @media only screen and (min-width: 1366px) and (max-width: 1599px) {
    .txt-fii-sw {
      font-size: 4.3rem;
      color: #fff;
      line-height: 51px;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .txt-desc-fii-sw {
      font-size: 1.6rem;
      line-height: 20px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;

    }

    .text-size {
      font-size: 1.8rem;
      line-height: 24px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }
  }

  @media only screen and (min-width: 1600px) and (max-width: 1919px) {
    .txt-fii-sw {
      font-size: 5.2rem;
      line-height: 61px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .txt-desc-fii-sw {
      font-size: 1.6rem;
      line-height: 21px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;

    }

    .text-size {
      font-size: 2rem;
      line-height: 26px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }
  }

  @media only screen and (min-width: 1920px) {
    .txt-fii-sw {
      font-size: 5.9rem;
      line-height: 76px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .txt-desc-fii-sw {
      font-size: 1.8rem;
      line-height: 28px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }

    .text-size {
      font-size: 2.5rem;
      line-height: 31px;
      color: #fff;
      font-family: "Segoe UI", Arial, sans-serif;
    }
  }

  .page-container,
  .footer {
    background-color: transparent !important;
  }

  .content-detail {
    width: 70%;
    margin: auto;
    margin-top: 15%;
  }

  .content-bu {
    width: 90%;
    text-align: center;
    margin: auto;
  }

  .button {
    border: 2px solid #00FFFF;
    color: #fff;
    font-size: 2rem;
    border-radius: 30px;
    padding: 10px 40px;
    background: transparent;
    bottom: 0px;
    margin-top: 50px;
  }

  .hover-img:hover,
  .hover-img:focus {
    -webkit-transform: translateY(-5px) scale(1.02);
    transform: translateY(-5px) scale(1.02);
    transition: all 0.4s;
    cursor: pointer;
  }

  .sidebar {
    background-color: #002C82 !important;
  }

  @keyframes rotate360 {
    to {
      transform: rotate(360deg);
    }
  }

  .rotation-image {
    animation: 10s rotate360 infinite linear;
  }

  .desc_mobile,
  .desc_aplycation,
  .desc_iot,
  .desc_big_data,
  .desc_data_analysis,
  .desc_cloud,
  .desc_auto,
  .desc_colection {
    position: absolute;
    display: none;
  }
/* width */
::-webkit-scrollbar {
  width: 5px;
}

/* Track */
::-webkit-scrollbar-track {
  background: #003163; 
}
 
/* Handle */
::-webkit-scrollbar-thumb {
  background: #003163; 
}

/* Handle on hover */
::-webkit-scrollbar-thumb:hover {
  background: #013992; 
}
@media (min-width: 769px) {
  .sidebar-xs .header-highlight .navbar-header {
    min-width: 0;
    width: 56px;
    padding-left: 0;
  }
  .sidebar-xs .header-highlight .navbar-header .navbar-brand {
    padding-left: 0;
    padding-right: 0;
    background: url(/assets/images/logoFavicon.png) no-repeat center center;
    float: none;
    background-size: 55px !important;
    display: block;
  }
  .sidebar-xs .header-highlight .navbar-header .navbar-brand > img {
    display: none;
  }
  .sidebar-main-hidden .header-highlight .navbar-header,
  .sidebar-all-hidden .header-highlight .navbar-header {
    display: none;
  }
}
</style>
<div class="content-home viewscroll">
  <img id="bg" src="/assets/images/custom/home/img_body_new_b.png" style="float:right">
  <div class="row" style="height: 100%; width: 100%; position: relative">
    <div class="col-xs-5" style="position: absolute;">
      <div class="content-detail">
        <p class="txt-fii-sw " style="color: #00FFFF;">FII - SOFTWARE</p>
        <p class="txt-fii-sw" style="margin-top: 10px;">TECHNOLOGY</p>
        <P class="txt-desc-fii-sw" style="margin-top: 20px; padding: 5px;">
          Fii is committed to providing enterprises with comprehensive solutions of technology services based on
          automation, network, platform and big data, leading the transformation from traditional manufacturing to
          intelligent manufacturing.
        </P>
        <button type="button" class="button hover-img text-size"><a href="/about"><span
              style="color: white;">About Us</span> </a></button>
      </div>

    </div>
    <div class="img-bu">
      <img class="hover-img" src="/assets/images/custom/home/quangchau.png" style="position: absolute;" id="img_qc">
      <img class="hover-img" src="/assets/images/custom/home/quevo.png" style="position: absolute;" id="img_qv">
      <img class="hover-img" src="/assets/images/custom/home/dongvang.png" style="position: absolute;" id="img_dv">
    </div>
    <img src="/assets/images/custom/home/lienket_new.png" style="position: absolute;" id="img_lienket" />
    <img src="/assets/images/custom/home/user_new.png" style="position: absolute;" id="img_user" />
    <div class="hover_mobile">
      <div class="">
        <img class="rotation-image image_mobile" src="/assets/images/custom/home/ic_mobile_a.png"
          style="position: absolute;width: 8.7rem;" id="img_mobile" />
        <img class="hover-img" src="/assets/images/custom/home/mobile.png" style="position: absolute;" id="mobile" />
      </div>
      <div class="desc_mobile" id="txt_mobile">
        <img src="/assets/images/custom/home/text_mobile_a.png" />
      </div>
    </div>

    <div class="hover_aplycation">
      <div class="">
        <img class="rotation-image" src="/assets/images/custom/home/ic_aplycation.png"
          style="position: absolute; width: 8rem;" id="img_aplycation" />
        <img class="hover-img" src="/assets/images/custom/home/aplycation.png" style="position: absolute;"
          id="aplycation" />
      </div>
      <div class="desc_aplycation" id="txt_aplycation">
        <img src="/assets/images/custom/home/text_data-applycaiton_a.png" />
      </div>
    </div>

    <div class="hover_iot">
      <div class="image_iot">
        <img class="rotation-image" src="/assets/images/custom/home/ic_analysis.png"
          style="position: absolute; width: 8rem;" id="img_analysis" />
        <img class="hover-img" src="/assets/images/custom/home/iot_new.png" style="position: absolute;" id="iot" />
      </div>
      <div class="desc_iot" id="txt_iot">
        <img src="/assets/images/custom/home/text_data_iot_a.png" />
      </div>
    </div>

    <div class="hover_big_data">
      <div class="image_big_data">
        <img class="rotation-image" src="/assets/images/custom/home/ic_colection.png"
          style="position: absolute; width: 9rem;" id="img_collection" />
        <img class="hover-img" src="/assets/images/custom/home/big_data.png" style="position: absolute;"
          id="big_data" />
      </div>
      <div class="desc_big_data" id="txt_big_data">
        <img src="/assets/images/custom/home/text_big_data_a.png" />
      </div>
    </div>

    <div class="hover_data_analysis">
      <div class="image_data_analysis">
        <img class="rotation-image" src="/assets/images/custom/home/ic_iot_a.png" style="position: absolute;"
          id="img_iot" />
        <img class="hover-img" src="/assets/images/custom/home/analysis.png" style="position: absolute;"
          id="analysis" />
      </div>
      <div class="desc_data_analysis" id="txt_analysis">
        <img src="/assets/images/custom/home/text_data_analysis_a.png" />
      </div>
    </div>


    <div class="hover_cloud">
      <div class="image_cloud">
        <img class="rotation-image" src="/assets/images/custom/home/ic_cloud_a.png"
          style="position: absolute; width: 10rem;" id="img_cloud" />
        <img class="hover-img" src="/assets/images/custom/home/cloud.png" style="position: absolute;" id="cloud" />
      </div>
      <div class="desc_cloud" id="txt_cloud">
        <img src="/assets/images/custom/home/text_cloud_a.png" />
      </div>
    </div>

    <div class="hover_auto">
      <div class="image_auto">
        <img class="rotation-image" src="/assets/images/custom/home/ic_autpmation.png"
          style="position: absolute;width: 8rem;" id="img_automation" />
        <img class="hover-img" src="/assets/images/custom/home/auto.png" style="position: absolute;" id="automation" />
      </div>
      <div class="desc_auto" id="txt_automation">
        <img src="/assets/images/custom/home/text_automation_a.png" />
      </div>
    </div>

    <div class="hover_colection">
      <div class="image_colection">
        <img class="rotation-image" src="/assets/images/custom/home/ic_big_data.png"
          style="position: absolute; width: 9rem;" id="img_bigdata" />
        <img class="hover-img" src="/assets/images/custom/home/colection.png" style="position: absolute;"
          id="colection" />
      </div>
      <div class="desc_colection" id="txt_colection">
        <img src="/assets/images/custom/home/text_data_collection_a.png" />
      </div>
    </div>
  </div>
     <div class="footer text-muted reponsive-footer" style="position: absolute; bottom: 0px; left: 0px;">
        &copy; 2018. <a href="#">FII</a> by <a href="#" target="_blank">Icarus </a>cpe-vn-fii-sw@mail.foxconn.com
    </div>
</div>
<script>

  var scale = 1;
  function fixResponse() {
    var scrWidth = screen.width;
    var scrHeight = screen.height;

    var scaleW = scrWidth / 1920;
    var scaleH = scrHeight / 887;

    var ratio = scrWidth / scrHeight;
    if (ratio > 1.5) {
        scale = scaleW;
        $('#bg').css('width', (1920 * scale) + 'px');
        $('#bg').css('height', (887 * scale) + 'px');

        $('.content-home').css('width', (1920 * scaleW) + 'px');
        $('.content-home').css('height', (887 * scaleW) + 'px');
        $('.content-detail').css("display","block");
      $('.img-bu').css("display","block");
    } else {
      $('.content-detail').css("display","none");
      $('.img-bu').css("display","none");

        if (scaleW > scaleH) {
          scale = scaleW;
        } else {
          scale = scaleH;
        }
        $('#bg').css('width', (1920 * scale) + 'px');
        $('#bg').css('height', (887 * scale) + 'px');

        $('.content-home').css('width', (1920 * scaleW) + 'px');
        if (scale == scaleH) {
          $('.content-home').css('height', (887 * scaleH) + 'px');
        } else {
          $('.content-home').css('height', (887 * scaleW) + 'px');
        }
    }
  }
  function getResponsiveY(y) {
    var r = y * scale;
    return r;
  }
  function getResponsiveX(x) {
    var r = x * scale;
    return r;
  }
  var dataLoc = [{
    "id": 'img_mobile',
    "locX": 367,
    "locY": 716,
    "width": 160,
    "height": 160
  },
  {
    "id": 'img_aplycation',
    "locX": 555,
    "locY": 729,
    "width": 126,
    "height": 126
  },

  {
    "id": 'img_analysis',
    "locX": 347,
    "locY": 1091,
    "width": 136,
    "height": 136
  },
  {
    "id": 'img_collection',
    "locX": 93,
    "locY": 245,
    "width": 180,
    "height": 180
  },
  {
    "id": 'img_iot',
    "locX": 211,
    "locY": 174,
    "width": 135,
    "height": 135
  },
  {
    "id": 'img_cloud',
    "locX": 542,
    "locY": 201,
    "width": 170,
    "height": 170
  },
  {
    "id": 'img_automation',
    "locX": 329,
    "locY": 884,
    "width": 165,
    "height": 165
  },
  {
    "id": 'img_bigdata',
    "locX": 174,
    "locY": 710,
    "width": 185,
    "height": 185
  },
  {
    "id": 'img_lienket',
    "locX": 129,
    "locY": 264,
    "width": 865,
    "height": 464
  },
  {
    "id": 'img_user',
    "locX": 53,
    "locY": 69,
    "width": 1017,
    "height": 529
  },

  ////// image
  {
    "id": 'mobile',
    "locX": 416,
    "locY": 760,
    "width": 72,
    "height": 72
  },
  {
    "id": 'aplycation',
    "locX": 598,
    "locY": 768,
    "width": 40,
    "height": 41
  },

  {
    "id": 'iot',
    "locX": 391,
    "locY": 1121,
    "width": 70,
    "height": 47
  },
  {
    "id": 'big_data',
    "locX": 154,
    "locY": 303,
    "width": 61,
    "height": 61
  },
  {
    "id": 'analysis',
    "locX": 253,
    "locY": 216,
    "width": 48,
    "height": 45
  },
  {
    "id": 'cloud',
    "locX": 603,
    "locY": 258,
    "width": 56,
    "height": 49
  },
  {
    "id": 'automation',
    "locX": 386,
    "locY": 937,
    "width": 55,
    "height": 55
  },
  {
    "id": 'colection',
    "locX": 240,
    "locY": 770,
    "width": 62,
    "height": 60
  },
  ///// text image
  {
    "id": 'txt_mobile',
    "locX": 370,
    "locY": 670,
    "width": 231,
    "height": 25
  },
  {
    "id": 'txt_aplycation',
    "locX": 667,
    "locY": 675,
    "width": 231,
    "height": 25
  },

  {
    "id": 'txt_iot',
    "locX": 346,
    "locY": 946,
    "width": 231,
    "height": 25
  },
  {
    "id": 'txt_big_data',
    "locX": 99,
    "locY": 154,
    "width": 231,
    "height": 25
  },
  {
    "id": 'txt_analysis',
    "locX": 325,
    "locY": 103,
    "width": 231,
    "height": 25
  },
  {
    "id": 'txt_cloud',
    "locX": 688,
    "locY": 171,
    "width": 231,
    "height": 25
  },
  {
    "id": 'txt_automation',
    "locX": 332,
    "locY": 813,
    "width": 231,
    "height": 25
  },
  {
    "id": 'txt_colection',
    "locX": 185,
    "locY": 631,
    "width": 231,
    "height": 25
  },

  //// area
  {
    "id": 'img_qv',
    "locX": 601,
    "locY": 1372,
    "width": 242,
    "height": 220
  },
  {
    "id": 'img_qc',
    "locX": 647,
    "locY": 1609,
    "width": 190,
    "height": 172
  },

  {
    "id": 'img_dv',
    "locX": 647,
    "locY": 1192,
    "width": 183,
    "height": 164
  },
  ];

  function fixLayoutImg() {
    for (i in dataLoc) {
      var locX = getResponsiveX(dataLoc[i].locX);
      var locY = getResponsiveY(dataLoc[i].locY);
      $('#' + dataLoc[i].id).css('top', locX + 'px');
      $('#' + dataLoc[i].id).css('right', locY + 'px');
      $('#' + dataLoc[i].id).css('width', (dataLoc[i].width * scale) + 'px');
      $('#' + dataLoc[i].id).css('height', (dataLoc[i].height * scale) + 'px');
    }
  }
  $(document).on('ready', function () {
    fixResponse();
    fixLayoutImg();

    // $('.content-home').on('click', function (e) {
    //     e.preventDefault();

    //     // Toggle min sidebar class
    //     $('body').addClass('sidebar-xs');
    // });

    $('#img_qv').on('click', function (e) {
        e.preventDefault();
        $('body').toggleClass('sidebar-xs');
        $("#bu_qv").addClass("active");
        $("#ul_bu_qv").css("display","block");
        $("#bu_dv").removeClass("active");
        $("#ul_bu_dv").css("display","none");
        $("#bu_qc").removeClass("active");
        $("#ul_bu_qc").css("display","none");
    });

    $('#img_qc').on('click', function (e) {
        e.preventDefault();
        $('body').toggleClass('sidebar-xs');
        $("#bu_qc").addClass("active");
        $("#ul_bu_qc").css("display","block");
        $("#bu_dv").removeClass("active");
        $("#ul_bu_dv").css("display","none");
        $("#bu_qv").removeClass("active");
        $("#ul_bu_qv").css("display","none");
    });
    $('#img_dv').on('click', function (e) {
        e.preventDefault();
        $('body').toggleClass('sidebar-xs');
        $("#bu_dv").addClass("active");
        $("#ul_bu_dv").css("display","block");
        $("#bu_qv").removeClass("active");
        $("#ul_bu_qv").css("display","none");
        $("#bu_qc").removeClass("active");
        $("#ul_bu_qc").css("display","none");
    });
  });

  window.addEventListener('resize', function () {
    fixResponse();
    fixLayoutImg();
  });

  $('.hover_mobile').hover(function () {
    $(this).find('.desc_mobile').fadeIn();
    $(this).css("cursor", "pointer");
  }, function () {
    $(this).find('.desc_mobile').fadeOut();
  });

  $('.hover_aplycation').hover(function () {
    $(this).find('.desc_aplycation').fadeIn();
    $(this).css("cursor", "pointer");
  }, function () {
    $(this).find('.desc_aplycation').fadeOut();
  });

  $('.hover_iot').hover(function () {
    $(this).css("cursor", "pointer");
    $(this).find('.desc_iot').fadeIn();
  }, function () {
    $(this).find('.desc_iot').fadeOut();
  });

  $('.hover_big_data').hover(function () {
    $(this).find('.desc_big_data').fadeIn();
    $(this).css("cursor", "pointer");
  }, function () {
    $(this).find('.desc_big_data').fadeOut();
  });

  $('.hover_data_analysis').hover(function () {
    $(this).css("cursor", "pointer");
    $(this).find('.desc_data_analysis').fadeIn();
  }, function () {
    $(this).find('.desc_data_analysis').fadeOut();
  });

  $('.hover_cloud').hover(function () {
    $(this).css("cursor", "pointer");
    $(this).find('.desc_cloud').fadeIn();
  }, function () {
    $(this).find('.desc_cloud').fadeOut();
  });

  $('.hover_auto').hover(function () {
    $(this).css("cursor", "pointer");
    $(this).find('.desc_auto').fadeIn();
  }, function () {
    $(this).find('.desc_auto').fadeOut();
  });

  $('.hover_colection').hover(function () {
    $(this).css("cursor", "pointer");
    $(this).find('.desc_colection').fadeIn();
  }, function () {
    $(this).find('.desc_colection').fadeOut();
  });

</script>