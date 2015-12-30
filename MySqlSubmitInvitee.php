<?php
	$con = mysqli_connect("localhost", "cpf5193_admin", "Cpf1092281!", "cpf5193_tagalong");
	$hostEmail = $_POST["hostEmail"];
	$hostEventCount = $_POST["hostEventCount"];
	$name = $_POST["friendName"];
	$location = $_POST["friendEmail"];
	$time = $_POST["isAttending"];
	$statement = mysqli_prepare($con, "INSERT INTO Invited (hostEmail, hostEventCount, friendName, friendEmail, isAttending) VALUES (?, ?, ?, ?, ?)");
	mysqli_stmt_bind_param($statement, "sssss", $hostEmail, $hostEventCount, $friendName, $friendEmail, $isAttending);
	mysqli_stmt_execute($statement);
	mysqli_stmt_close($statement);
	mysqli_close($con);
?>