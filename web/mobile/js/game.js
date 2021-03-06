var choose = 0;
var position = new Array();
var k = 0;
var wsocket;
var serviceLocation = 'ws://10.10.2.93:8080/team3_setgame/wssocket/';
var gameId;
var $message;
var email;
var remingtime;

function connectToChatserver($) {
    wsocket = new WebSocket(serviceLocation + gameId);
    wsocket.onmessage = onMessageReceived;
    //alert(serviceLocation + gameId);
}
function onMessageReceived(evt) {
    //alert(evt.data);
    if (evt.data == "Not_Set"||evt.data == "NOT_FOUND ")
    {
    alert("Not Set");
    }
    else
    {
        var msg = JSON.parse(evt.data); // native API
        if (msg.type == 0)
        {
            showMessage(msg);
        }
        else if (msg.type == 3)
        {
            endgame();
            
        }
        else {
            //alert(evt.data);
            loadimage(evt.data);
            loadscore(evt.data) ;
        }
    }
}
function sendChatMessage() {
    var msg = '{"type":0,"message":"' + $message.val() + '", "sender":"'
            + email + '", "received":""}';
    wsocket.send(msg);
    $message.val('');
}
function sendGameMessage() {
    var msg = '{"type":1,"position1":' + position[0]
            + ', "position2":' + position[1]
            + ', "position3":' + position[2]
            + ', "email":"' + email
            + '", "gameId":' + gameId + '}';
    wsocket.send(msg);
}
function endgame() {
    alert("Game has ended.Thank you for playing!");
    window.location.href="roomchoice.html";
    
}

function loadimage(data) {
    var msg = JSON.parse(data);
    for (var i = 0; i <= 11; i++) {
        var cardname = msg.table[i].number.toString() +
                msg.table[i].shading.toString() +
                msg.table[i].color.toString() +
                msg.table[i].shape.toString();
        var imgId = "#img" + i;
        var imgurl = "img/cards/" + cardname + ".gif";
        jQuery(imgId).attr("src", imgurl);
    }
}
function loadscore(data) {
    var result = JSON.parse(data);
    var players = result.playerScoreArray;
    $("#scoreboard").empty();
    for (var i in players) {
        var gravatarcode = $.md5(players[i].player.email);
        $("#table_player").append(
                "<tr><td><img src='https://s.gravatar.com/avatar/"
                +gravatarcode
                +"'</td><td>"+players[i].player.name
                +"</td><td>"+players[i].currentScore+"</td></tr>");
    }
}
function showMessage(msg) {
    if (msg.sender.toString() == email)
    {
        var gravatarcode = $.md5(msg.sender.toString());
        var url="https://s.gravatar.com/avatar/" + gravatarcode+".jpg";
        var $messageLine = $('<div class="message right"><img src="'+url+'"/>\n\
                        <div class="bubble">' + msg.message +
                '<div class="corner"></div><span>' +
                "now" + '</span></div></div>');
    }
    else if(msg.sender.toString() == "System")
    {
        var $messageLine = $('<div class="message"><img src="img/default_card.png"/>\n\
                            <div class="bubble">' + msg.message +
                '<div class="corner"></div><span>' +
                "now" + '</span></div></div>');
    }
    else
    {
        var gravatarcode = $.md5(msg.sender.toString());
        var url="https://s.gravatar.com/avatar/" + gravatarcode+".jpg";
        var $messageLine = $('<div class="message"><img src="'+url+'"/>\n\
                            <div class="bubble">' + msg.message +
                '<div class="corner"></div><span>' +
                "now" + '</span></div></div>');
    }
    $('#chatdetail').append($messageLine);
    document.getElementById("msg_end").scrollIntoView();
}
function showchat() {
    var preloadbg = document.createElement('img');
    var clone = $(this).find('img').eq(0).clone();
    setTimeout(function () {
        $('#profile p').addClass('animate');
        $('#profile').addClass('animate');
    }, 100);
    setTimeout(function () {
        $('#chat-messages').addClass('animate');
        $('.cx, .cy').addClass('s1');
        setTimeout(function () {
            $('.cx, .cy').addClass('s2');
        }, 100);
        setTimeout(function () {
            $('.cx, .cy').addClass('s3');
        }, 200);
    }, 150);
    $('.floatingImg').animate({
        'width': '68px',
        'left': '108px',
        'top': '20px'
    }, 200);
    $('.message').not('.right').find('img').attr('src', $(clone).attr('src'));
    $('#chatview').fadeIn();
}

jQuery(document).ready(function ($) {
    showchat();
    gameId = sessionStorage.getItem("gameId");
    email = sessionStorage.getItem("email");

    //alert(gameId);
    $("#selectable li").css("height", $("#selectable li").css("width"));
    $("#selectable li").on("click", function () {
        if (choose < 3)
        {
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                choose = choose - 1;
            }
            else {
                $(this).addClass("selected");
                choose = choose + 1;
            }
        }
        else {
            if ($(this).hasClass("selected")) {
                $(this).removeClass("selected");
                choose = choose - 1;
            }
            else {
                alert("You cannot choose more than 3 cards!");
            }
        }
    });
    $("#gamesubmit").click(function ()
    {
        if (choose == 3) {
            //alert(choose);
            $("#selectable li").each(function () {
                if ($(this).hasClass("selected")) {
                    position[k] = $(this).attr("id");
                    k = k + 1;
                    $(this).removeClass("selected");
                }
            });
            //alert(position);
            sendGameMessage();
            k = 0;
            choose = 0;
            //alert(choose);
            position.splice(0, position.length);
        }
        else {
            alert("No enough 3 cards");
        }
    });
    $('#send').click(function () {
        $message=$('#message');
        if($('#message').val()!="")
        {
            sendChatMessage();
        }
        
    });
    $("#message").keyup(function () {
        if (event.keyCode == 13) {
            $message=$('#message');
                    if($('#message').val()!="")
        {
            sendChatMessage();
        }
        }
    });
    connectToChatserver();
});