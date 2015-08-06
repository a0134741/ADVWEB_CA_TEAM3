$(document).ready(function(){
	/*
	$("#loginBtn").on('click',function(){
		login();
    });
    */
	$("#userID").on("keypress", function(){
		var event = arguments.callee.caller.arguments[0]||window.event;//消除浏览器差异 
		if (event.keyCode == 13) {
//			$("#loginForm").submit();
			login();
		}
	}); 
	$("#userPass").on("keypress", function(){
		var event = arguments.callee.caller.arguments[0]||window.event;//消除浏览器差异 
		if (event.keyCode == 13) {
			login();
		} 
	}); 
	 $("#login-button").click(function(event){
		 event.preventDefault();
		 login();
		 
	 });
});
/*
function login(){
						 Lobibox.notify('success', {
		                    sound: false,
		                    msg: 'Login Successful! Welcome!'
		                });
					 $('form').fadeOut(500);
					 $('.wrapper').addClass('form-success');
					setTimeout("window.location.href='roomchoice.html';",1200);
}
*/
function login(){
	//var btn = $("#loginBtn");
    //btn.button('loading');
	var locationstr=window.location.host+"/T4Garage";
	var datasent = $("#loginForm").serializeObject();
	params = JSON.stringify(datasent); 
	$.ajax({
		type : "POST",
		url : "/T4Garage/loginCheck",
		dataType : "json",
		contentType : "application/json;charset=utf-8",
		data : params,
		async : false,
		success : function(data) {
			if(data.resultCode == 0){
				//alert(data.data.eid);
				if(data.data.erole=="ADM")
					{	
						 Lobibox.notify('success', {
			                    sound: false,
			                    msg: 'Login Successful! Welcome!'
			                });
						 $('form').fadeOut(500);
						 $('.wrapper').addClass('form-success');
						
						setTimeout("window.location.href='/T4Garage/aindex';",1200);
						
					}
				else {
					 Lobibox.notify('success', {
		                    sound: false,
		                    msg: 'Login Successful! Welcome!'
		                });
					 $('form').fadeOut(500);
					 $('.wrapper').addClass('form-success');
					setTimeout("window.location.href='/T4Garage/mindex';",1200);
				}
			}
			else {
				Lobibox.notify('error', {
		             width: $(window).width(),
		             msg: 'Login Fail! Incorrect Employee ID or Password. Please Try Again!'
		         });
			}
		}
	});
}

