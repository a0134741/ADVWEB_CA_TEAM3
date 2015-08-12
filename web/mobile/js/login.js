$(document).ready(function () {
    $("#userID").on("keypress", function () {
        var event = arguments.callee.caller.arguments[0] || window.event;//消除浏览器差异 
        if (event.keyCode == 13) {
//			$("#loginForm").submit();
            login();
        }
    });
    $("#userPass").on("keypress", function () {
        var event = arguments.callee.caller.arguments[0] || window.event;//消除浏览器差异 
        if (event.keyCode == 13) {
            login();
        }
    });
    $("#login-button").click(function (event) {
        event.preventDefault();
        login();

    });
});
function login() {
    //var btn = $("#loginBtn");
    //btn.button('loading');
    var locationstr = "http://10.10.2.185:8080/team3_setgame/api/access/";
    var email = $("#email").val();
    var epass = $("#epass").val();
    var locationstrs = locationstr + email + "/" + epass;
    alert(locationstrs);
    $.getJSON(locationstrs)
            .done(function (result) {
                alert(result.email);
                Lobibox.notify('success', {
                    sound: false,
                    msg: 'Login Successful! Welcome!'
                });
                $('form').fadeOut(500);
                $('.wrapper').addClass('form-success');
                sessionStorage.setItem("email",result.email);
                setTimeout("window.location.href='roomchoice.html';", 1200);
            })
            .fail(function () {
                Lobibox.notify('error', {
                    width: $(window).width(),
                    msg: 'Login Fail! Incorrect Employee ID or Password. Please Try Again!'
                });
            });
}

