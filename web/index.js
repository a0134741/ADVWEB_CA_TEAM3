var ipaddr = "";

var gamelisttemplate =
        Handlebars.compile($("#gamelisttemplate").html());

var highscorelisttemplate =
        Handlebars.compile($("#highscorelisttemplate").html());

var playerlisttemplate =
        Handlebars.compile($("#playerlisttemplate").html());
var selcectgameId=null;

$(document).ready(function () {
    //Fetches all games upon load of web site
    getallgame();
    //Fetches highscore board upon load of web site
    $.getJSON(ipaddr + "api/main/gettopplayers")
            .done(function (result) {
                $("#highscorelist").empty();
                var topPlayer = result.topPlayersArray;
                for (var i in topPlayer) {
                    $("#highscorelist").append(
                            highscorelisttemplate({
                                gravatarurl: "",
                                playername: topPlayer[i].name,
                                playerscore: topPlayer[i].highscore
                            })
                            );
                }
            });

    //create new player        
    $("#btn_submitplayer").on("click", function () {
        $.getJSON("api/player/" + $("#inputEmail").val() + "/" + $("#inputName").val() + "/" + $("#inputPassword").val())
                .done(function (result) {
                    alert("Player created!");
                    $("#createplayermodal").modal('toggle');
                });
    });

    //create new game
    $("#btn_submitgame").on("click", function () {
        //alert("api/game/" + $("#inputTitle").val() + "/" + $("#inputDuration").val() + "/" + $("#inputPlayers").val());
        $.getJSON("api/game/" + $("#inputTitle").val() + "/" + $("#inputDuration").val() + "/" + $("#inputPlayers").val())
                .done(function (result) {
                    $("#creategamemodal").modal('toggle');
                    getallgame();
                })
                .fail(function () {
                    alert("wrong");
                });
    });


    $("#btn_viewgame").on("click", function () {
        if(selcectgameId!=null){
            alert(selcectgameId);
            $.session.set("gameId",selcectgameId);
           
            window.location.href="game.html";
        }
    });
    
    


//    $('#gameform')
//            .formValidation({
//                framework: 'bootstrap',
//                icon: {
//                    valid: 'glyphicon glyphicon-ok',
//                    invalid: 'glyphicon glyphicon-remove',
//                    validating: 'glyphicon glyphicon-refresh'
//                },
//                excluded: ':disabled',
//                fields: {
//                    title: {
//                        validators: {
//                            notEmpty: {
//                                message: 'The name is required'
//                            },
//                            stringLength: {
//                                min: 6,
//                                max: 30,
//                                message: 'The name must be more than 6 and less than 30 characters long'
//                            }
//                        }
//                    },
//                    players: {
//                        validators: {
//                            notEmpty: {
//                                message: 'The size is required'
//                            }
//                        }
//                    },
//                    duration: {
//                        validators: {
//                            notEmpty: {
//                                message: 'The color is required'
//                            }
//                        }
//                    }
//                }
//            })
//            // Using Bootbox for color and size select elements
//            .find('[name="color"], [name="size"]')
//            .combobox()
//            .end();

});
function getgamedetail(gameid) {
    //alert(gameid);
    $.getJSON(ipaddr + "api/game/" + gameid)
            .done(function (result) {
                $("#tb_gametitle").val(result.title);
                $("#tb_timestarted").val(result.startTime);
                $("#tb_timeremaining").val();
                $("#tb_remainingcards").val(result.deck.length);
                $("#tb_noofplayers").val(result.playerScoreArray.length);
            });
};


function getallgame() {
    $.getJSON(ipaddr + "api/main/getallgames")
            .done(function (result) {
                $("#gamelist").empty();
                var game = result.gamesArray;
                for (var i in game) {
                    $("#gamelist").append(
                            gamelisttemplate({
                                gameid: game[i].gameId,
                                gametitle: game[i].title,
                                gamenoofplayer: game[i].maxPlayers,
                                gametimestart: game[i].startTime
                            })
                            );
                }
                $("#gametable tr").click(function () {
                    var gameid = $(this).find(".gameid").text();
                    selcectgameId=gameid;
                    getgamedetail(gameid);
                    $("#gametable tr").removeAttr( "style" );
                    $(this).css({ color: "#ff0011", background: "blue" });
                });
            });

}

