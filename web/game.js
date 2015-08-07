var playerlisttemplate =
        Handlebars.compile($("#playerlisttemplate").html());

var ipaddr = "";

var gameid = 1;//get from session

var choose = 0;

var position = new Array();
var i=0;

chosencards = new Array();

$(document).ready(function () {
    //This is used to size up the Grid Square
    $("#selectable li").css("height", $("#selectable li").css("width"));

//    $(function () {
//        $("#selectable").bind("mousedown", function (e) {
//            e.metaKey = true;
//        }).selectable({
//            stop: function () {
//                $(".ui-selected", this).each(function () {
//                    chosencards.push($(this).attr("id"));
//                });
//            }
//        });
//    });

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
    })

    $("#gamesubmit").click(function ()
    {

        if (choose = 3) {
            $("#selectable li").each(function () {
                if ($(this).hasClass("selected")) {
                    position[i]=$(this).attr("id");
                    i++;
                }
            });
            alert(position);
        }
        else {
            alert("No enough 3 cards");
        }
    });

    //passed session gameid into var gameid, to get the game object and then append accordingly to each field including the handlebar
    $.getJSON(ipaddr + "api/game/" + gameid)
            .done(function (result) {
                $("#tb_gametitle").val(result.title);
                $("#tb_timestarted").val(result.startTime);
                $("#tb_timeremaining").val();
                $("#tb_remainingcards").val(result.deck.length);
                $("#tb_noofplayers").val(result.playerScoreArray.length);

                var players = result.playerScoreArray;
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
            });
});

//YOUTUBE VIDEO
var youtubeFunc = '';
var outerDiv = document.getElementById("instructionmodal");
var youtubeIframe = outerDiv.getElementsByTagName("iframe")[0].contentWindow;
$('#introVideo').on('hidden.bs.modal', function (e) {
    youtubeFunc = 'pauseVideo';
    youtubeIframe.postMessage('{"event":"command","func":"' + youtubeFunc + '","args":""}', '*');
});
$('#introVideo').on('shown.bs.modal', function (e) {
    youtubeFunc = 'playVideo';
    youtubeIframe.postMessage('{"event":"command","func":"' + youtubeFunc + '","args":""}', '*');
});