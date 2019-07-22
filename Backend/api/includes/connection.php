<?php
	define("SERVER","localhost");
	define("USER","id10188401_vaibhav");
	define("PASSWORD","ghost@4.0");
	define("DATABASE","id10188401_ecommerce");
	$db=mysqli_connect(SERVER,USER,PASSWORD,DATABASE);
	if(!$db)
		die("Server Error".mysqli_connect_error());
?>