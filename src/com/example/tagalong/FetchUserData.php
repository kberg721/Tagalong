<?php
	$con = mysqli_connect("mysql15.000webhost.com", "a4232919_kberg72", "Darkside721", "a4232919_User");

	if (mysqli_connect_errno()) {
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

	$password = mysqli_real_escape_string($con, $_POST["password"]);
	$email = mysqli_real_escape_string($con, $_POST["email"]);

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
	
	if (count($user) == 0) {
	  echo json_encode (new stdClass);
	} else {
	  echo json_encode($user);
	}

	mysqli_stmt_close($statement);

	mysqli_close($con);
?>