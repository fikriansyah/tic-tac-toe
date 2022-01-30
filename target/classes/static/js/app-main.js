$(document).ready(function () {

  if ($("#is_game_over").val() !== "true") {
    $(".board-row-tile.available").click(function (event) {
      $("#tile_id").val(event.target.id);
      $("#form_input").submit();
    });
  }

  $("#btn-new-game").click(function (event) {
    $("#new_game").val("yes");
    $("#form_input").submit();
  });

  $("#link-user-logout").click(function (event) {
    $("#form-logout").submit();
  });

});
