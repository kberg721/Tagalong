<?php
  // ini_set('display_errors',1);
  // error_reporting(E_ALL);
  $con = mysqli_connect("mysql15.000webhost.com", "a4232919_kberg72", "Darkside721", "a4232919_User");

  if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

  $email = mysqli_real_escape_string($con, $_POST["email"]);
  $update_statement = mysqli_prepare($con, "DELETE FROM User WHERE email = ?");
  mysqli_stmt_bind_param($update_statement, "s", $email);
  mysqli_stmt_execute($update_statement);

  $name = mysqli_real_escape_string($con, $_POST["name"]);
  $password = mysqli_real_escape_string($con, $_POST["password"]);
  $eventCount = mysqli_real_escape_string($con, $_POST["eventCount"]);
  $statement = mysqli_prepare($con, "INSERT INTO User (email, name, password, eventCount) VALUES (?, ?, ?, ?) ");
  mysqli_stmt_bind_param($statement, "ssss", $email, $name, $password, $eventCount);
  mysqli_stmt_execute($statement);

  mysqli_stmt_close($statement);
  mysqli_close($con);
?>
