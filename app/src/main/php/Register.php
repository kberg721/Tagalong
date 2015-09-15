<?php
	$con = mysqli_connect("mysql15.000webhost.com", "a4232919_kberg72", "Darkside721", "a4232919_User" );

	$name = $_POST["name"];
	$password = $_POST["password"];
	$email = $_POST["email"];

	$statement = mysqli_prepare($con, "INSERT INTO User (email, name, password) VALUES (?, ?, ?) ");
	mysqli_stmt_bind_param($statement, "sss", $email, $name, $password);
	mysqli_stmt_execute($statement);

	mysqli_stmt_close($statement);

	msqli_close($con);
?>