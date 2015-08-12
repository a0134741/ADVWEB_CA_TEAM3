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
    alert(evt.data);
    loadimage(evt.data);
    loadscore(evt.data);
    loadtime(evt.data);
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
            $ceHours = $example.find('.ce-hours'),
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
});

////YOUTUBE VIDEO
//var youtubeFunc = '';
//var outerDiv = document.getElementById("instructionmodal");
//var youtubeIframe = outerDiv.getElementsByTagName("iframe")[0].contentWindow;
//$('#introVideo').on('hidden.bs.modal', function (e) {
//    youtubeFunc = 'pauseVideo';
//    youtubeIframe.postMessage('{"event":"command","func":"' + youtubeFunc + '","args":""}', '*');
//});
//$('#introVideo').on('shown.bs.modal', function (e) {
//    youtubeFunc = 'playVideo';
//    youtubeIframe.postMessage('{"event":"command","func":"' + youtubeFunc + '","args":""}', '*');
//});

