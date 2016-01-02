<?php
  $con = mysqli_connect("mysql15.000webhost.com", "a4232919_kberg72", "Darkside721", "a4232919_User" );
  $hostEmail = $_POST["hostEmail"];
  $hostEventCount = $_POST["hostEventCount"];
  $name = $_POST["name"];
  $location = $_POST["location"];
  $time = $_POST["time"];
  $description = $_POST["description"];
  $statement = mysqli_prepare($con, "INSERT INTO Event (hostEmail, hostEventCount, name, location, time, description) VALUES (?, ?, ?, ?, ?, ?)");
  mysqli_stmt_bind_param($statement, "ssssss", $hostEmail, $hostEventCount, $name, $location, $time, $description);
  mysqli_stmt_execute($statement);
  mysqli_stmt_close($statement);
  mysqli_close($con);
?>