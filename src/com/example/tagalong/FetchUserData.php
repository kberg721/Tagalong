<?php
	$con = mysqli_connect("mysql15.000webhost.com", "a4232919_kberg72", "Darkside721", "a4232919_User");

	$password = $_POST["password"];
	$email = $_POST["email"];

	$statement = mysqli_prepare($con, "SELECT * FROM User WHERE email = ? AND password = ?");
	mysqli_stmt_bind_param($statement, "ss", $email, $password);
	mysqli_stmt_execute($statement);

	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $email, $name, $password);

	$user = array();

	while(mysqli_stmt_fetch($statement)) {
		$user[email] = $email;
		$user[name] = $name;
		$user[password] = $password;
	}

	echo json_encode($user);

	mysqli_stmt_close($statement);

	msqli_close($con);
?>