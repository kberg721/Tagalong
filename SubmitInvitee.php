<?php
  $con = mysqli_connect("mysql15.000webhost.com", "a4232919_kberg72", "Darkside721", "a4232919_User");
  $hostEmail = $_POST["hostEmail"];
  $hostEventCount = $_POST["hostEventCount"];
  $friendName = $_POST["friendName"];
  $friendEmail = $_POST["friendEmail"];
  $friendFBId = $_POST["friendFBId"];
  $isAttending = $_POST["isAttending"];
  $statement = mysqli_prepare($con, "INSERT INTO Invited (hostEmail, hostEventCount, friendName, friendEmail, friendFBId, isAttending) VALUES (?, ?, ?, ?, ?, ?)");
  mysqli_stmt_bind_param($statement, "ssssss", $hostEmail, $hostEventCount, $friendName, $friendEmail, $friendFBId, $isAttending);
  mysqli_stmt_execute($statement);
  mysqli_stmt_close($statement);
  mysqli_close($con);
?>