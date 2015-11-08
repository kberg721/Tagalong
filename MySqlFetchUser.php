<?php
  // ini_set('display_errors',1);
  // error_reporting(E_ALL);
	$con = mysqli_connect("localhost", "cpf5193_admin", "Cpf1092281!", "cpf5193_tagalong");

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