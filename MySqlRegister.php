<?php
  $con = mysqli_connect("localhost", "cpf5193_admin", "Cpf1092281!", "cpf5193_tagalong");

  if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

  $name = mysqli_real_escape_string($con, $_POST["name"]);
  $password = mysqli_real_escape_string($con, $_POST["password"]);
  $email = mysqli_real_escape_string($con, $_POST["email"]);
  $eventCount = mysqli_real_escape_string($con, $_POST["eventCount"]);

  $statement = mysqli_prepare($con, "INSERT INTO User (email, name, password, eventCount) VALUES (?, ?, ?, ?)");

  mysqli_stmt_bind_param($statement, "ssss", $email, $name, $password, $eventCount);
  mysqli_stmt_execute($statement);
  mysqli_stmt_close($statement);

  msqli_close($con);
?>
