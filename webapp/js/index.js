$(document).ready(function(){
    PopUpHide();
    SecondPopUpHide();
});

function PopUpShow(){
    $("#popup").show();
}

function PopUpHide(){
    $("#popup").hide();
}

function SecondPopUpShow(){
    $("#s-popup").show();
}

function SecondPopUpHide(){
    $("#s-popup").hide();
}

$('#qqqqq').click(function(e) {
	PopUpShow();
});

$('#btn-gogo').click(function(e) {
	var status = getCookie('status');
	if (typeof status == "undefined"){
		SecondPopUpShow()
		return;
	}
	window.location.href = "/download.html";
});

$('#popup-info').click(function(e) {
	SecondPopUpHide()
});

$('#login-submit').click(function(e) {

	$.ajax({
		url: '/signin',
		type: 'POST',
		dataType: 'json',
		data: {
			login: $('#username').val(),
			password: $('#password').val()
		},
		success: function (response) {
			console.log("good");
            if(response.status == "ok"){
            	setCookie('ok');
            }
        },
        error: function (xhr, status, error) {
            console.log("Произошла ошибка: " + status);
		}
	})

	PopUpHide();
});

$('#register-submit').click(function(e) {
	
	$.ajax({
		url: '/signup',
		type: 'POST',
		dataType: 'json',
		data: {
			login: $('#username').val(),
			password: $('#password').val()
		},
		success: function (response) {
			console.log("good");
            if(response.status == "ok"){
            	setCookie('ok');
            }
        },
        error: function (xhr, status, error) {
            console.log("Произошла ошибка: " + status);
		}
	})

	PopUpHide();
});

$('#login-form-link').click(function(e) {
	$("#login-form").delay(100).fadeIn(100);
	$("#register-form").fadeOut(100);
	$('#register-form-link').removeClass('active');
	$(this).addClass('active');
	e.preventDefault();
});

$('#register-form-link').click(function(e) {
	$("#register-form").delay(100).fadeIn(100);
		$("#login-form").fadeOut(100);
	$('#login-form-link').removeClass('active');
	$(this).addClass('active');
	e.preventDefault();
});

$('#change-foto').click(function(e) {
	var bEye = '0';
	var bNose = '0'; 
	var bMouth = '0';

	if ($("#checkbox1").is(':checked'))
		bEye = '1';
	if ($("#checkbox2").is(':checked'))
		bNose = '1';
	if ($("#checkbox3").is(':checked'))
		bMouth = '1';

	console.log (bEye + bNose + bMouth);

	$.ajax({
		url: '/change',
		type: 'POST',
		dataType: 'json',
		data: {
			eye: bEye,
			nose: bNose,
			mouth: bMouth
		},
		success: function (response) {
			console.log("good");
			$('#left-foto').attr('src','image/result.jpg');
        },
        error: function (xhr, status, error) {
            console.log("Произошла ошибка: " + error);
		}
	})
});

$('#send-data').click(function(e) {

	var form = $('form')[0]; 
	var formData = new FormData();
	formData.append('section', 'general');
	formData.append('action', 'previewImg');
	formData.append('image', $('input[type=file]')[0].files[0]); 

	$.ajax({
		url: '/download',
		type: 'POST',
		data: formData,
    	contentType: false,
   		processData: false,
		success: function (response) {
			console.log("good");
			window.location.href = "/image.html";		
        },
        error: function (xhr, status, error) {
            console.log("Произошла ошибка: " + error);
		}
	})
});



function getCookie (status) {
    var matches = document.cookie.match(new RegExp(
        "(?:^|; )" + status.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;
}

function setCookie (status) {
    var date = new Date(new Date().getTime() + 600 * 1000);
    document.cookie = "status=" + status + "; path=/; expires=" + date.toUTCString();
}

function deleteCookie()  {
    var date = new Date(0);
    document.cookie = "status=; path=/; expires=" + date.toUTCString();
    document.cookie = "token=; path=/; expires=" + date.toUTCString();
}