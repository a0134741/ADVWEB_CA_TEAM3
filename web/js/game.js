var playerlisttemplate =
        Handlebars.compile($("#playerlisttemplate").html());
var ipaddr = "";
var choose = 0;
var position = new Array();
var k = 0;
var wsocket;
var serviceLocation = 'ws://localhost:8080/team3_setgame/wssocket/';
var gameId = '';
var email = "a0134741@u.nus.edu";
var remingtime;


function connectToChatserver() {
    wsocket = new WebSocket(serviceLocation + gameId);
    wsocket.onmessage = onMessageReceived;
}
function onMessageReceived(evt) {
    //var msg = JSON.parse(evt.data); // native API

    if (evt.data == "Not_Set" || evt.data == "NOT_FOUND ")
    {

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

            endgame(msg);

        }
        else {
            loadimage(evt.data);
            loadscore(evt.data);
        }
    }
}
function endgame() {
    Lobibox.alert('success', {
                    msg: "Game has ended.Thank you for playing!"
                });
    window.location.href="index.html";
    
}
function sendChatMessage() {
    var msg = '{"message":"' + $message.val() + '", "sender":"'
            + $nickName + '", "received":""}';
    wsocket.send(msg);
    $message.val('');
}
function sendGameMessage() {
    var msg = '{"type":1,"position1":' + position[0]
            + ', "position2":' + position[1]
            + ', "position3":' + position[2]
            + ', "email":"' + email
            + '", "gameId":' + gameId + '}';
    alert(msg);
    wsocket.send(msg);
}
function loadtime(data) {
    var msg = JSON.parse(data);
    remingtime = msg.remainingTime;
    var $example = $(".example--modern"),
            $ceMinutes = $example.find('.ce-minutes'),
            $ceSeconds = $example.find('.ce-seconds'),
            now = new Date(),
            then = new Date(now.getTime() + remingtime);
    $example.find(".countdown").countEverest({
        second: then.getSeconds(),
        minute: then.getMinutes(),
        onChange: function () {
            countEverestAnimate($ceMinutes);
            countEverestAnimate($ceSeconds);
        }
    });

    function countEverestAnimate($el) {
        var fieldText = $el.text(),
                fieldData = $el.data('value'),
                fieldOld = $el.attr('data-old');

        if (typeof fieldOld === 'undefined') {
            $el.attr('data-old', fieldText);
        }

        if (fieldText != fieldData) {
            $el
                    .data('value', fieldText)
                    .attr('data-old', fieldData)
                    .addClass('animate');

            window.setTimeout(function () {
                $el
                        .removeClass('animate')
                        .attr('data-old', fieldText);
            }, 300);
        }
    }
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
function showMessage(msg) {
    if (msg.sender.toString() == "System")
    {
        var $messageLine = $('<div class="message"><img src="images/default_card.png"/>\n\
                            <div class="bubble">' + msg.message +
                '<div class="corner"></div><span>' +
                "now" + '</span></div></div>');
    }
    else
    {
        var gravatarcode = $.md5(msg.sender.toString());
        var url = "https://s.gravatar.com/avatar/" + gravatarcode + ".jpg";
        var $messageLine = $('<div class="message"><img src="' + url + '"/>\n\
                            <div class="bubble">' + msg.message +
                '<div class="corner"></div><span>' +
                "now" + '</span></div></div>');
    }
    $('#chatdetail').append($messageLine);
    document.getElementById("msg_end").scrollIntoView();
}
function loadimage(data) {
    var msg = JSON.parse(data);

    for (i = 0; i <= 11; i++) {
        var cardname = msg.table[i].number.toString() +
                msg.table[i].shading.toString() +
                msg.table[i].color.toString() +
                msg.table[i].shape.toString();
        var imgId = "#img" + i;
        var imgurl = "images/cards/" + cardname + ".gif";
        //alert(imgurl);
        $(imgId).attr("src", imgurl);
    }
}
function loadscore(data) {
    var result = JSON.parse(data);
    $("#tb_gametitle").val(result.title);
    $("#tb_timestarted").val(result.startTime);
//    $("#tb_timeremaining").val();
    $("#tb_remainingcards").val(result.deck.length);
    $("#tb_noofplayers").val(result.playerScoreArray.length);
    var players = result.playerScoreArray;
    $("#table_player").empty();
    for (var i in players) {
        var gravatarcode = $.md5(players[i].player.email);
        $("#table_player").append(
                playerlisttemplate({
                    gravatarurl: "https://s.gravatar.com/avatar/" + gravatarcode,
                    playername: players[i].player.name,
                    playerscore: players[i].currentScore
                })
                );
    }
}
$(document).ready(function () {
    showchat();
    email = sessionStorage.getItem("email");
    gameId = sessionStorage.getItem("gameId");
    //This is used to size up the Grid Square
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
                alert(">3");
            }
        }
    });

    $("#gamesubmit").click(function ()
    {
        if (choose = 3) {
            $("#selectable li").each(function () {
                if ($(this).hasClass("selected")) {
                    position[k] = $(this).attr("id");
                    k = k + 1;
                    $(this).removeClass("selected");
                }
            });
            alert(position);
            sendGameMessage();
            k = 0;
            choose = 0;
            position.splice(0, position.length);
        }
        else {
            alert("No enough 3 cards");
        }
    });


    connectToChatserver();
    autoPlayYouTubeModal();
});

//FUNCTION TO GET AND AUTO PLAY YOUTUBE VIDEO FROM DATATAG
function autoPlayYouTubeModal() {
    var trigger = $("body").find('[data-toggle="modal"]');
    trigger.click(function () {
        var theModal = $(this).data("target"),
                videoSRC = $(this).attr("data-theVideo"),
                videoSRCauto = videoSRC + "?autoplay=1";
        $(theModal + ' iframe').attr('src', videoSRCauto);
        $(theModal + ' button.close').click(function () {
            $(theModal + ' iframe').attr('src', videoSRC);
        });
        $('.modal').click(function () {
            $(theModal + ' iframe').attr('src', videoSRC);
        });
    });
}

