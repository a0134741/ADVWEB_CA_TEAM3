var playerlisttemplate =
        Handlebars.compile($("#playerlisttemplate").html());

var gameid = 1;//get from session

chosencards = new Array();

$(document).ready(function () {
    //This is used to size the the card squareness
    $("#selectable li").css("height", $("#selectable li").css("width"));

    //passed session gameid into var gameid, to get the game object and then append accordingly to each field including the handlebar
        $.getJSON(ipaddr + "api/game/" + gameid)
                .done(function (result) {
                    $("#tb_gametitle").val(result.title);
                    $("#tb_timestarted").val(result.startTime);
                    $("#tb_timeremaining").val();
                    $("#tb_remainingcards").val(result.deck.length);
                    $("#tb_noofplayers").val(result.playerScoreArray.length);
                });
    });




$(function () {

    $("#selectable").bind("mousedown", function (e) {
        e.metaKey = true;
    }).selectable({
        stop: function () {
            $(".ui-selected", this).each(function () {
                chosencards.push($(this).attr("id"));
            });
        }
    });
});

$("#gamesubmit").click(function ()
{
    alert(chosencards.toString());
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

 