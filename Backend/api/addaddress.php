<?php
	function checkArguments()
	{
		if(isset($_POST['username'])&&isset($_POST['address']))
			return 1;
		else
			return 0;
	}
	require 'includes/connection.php';
	$response=array('status'=>'DATA_NOT_ADDED');
	if(checkArguments())
	{
		$username=$_POST['username'];
		$address=$_POST['address'];
		$query="insert into addresses (address,username)values('$address','$username')";
		if(mysqli_query($db,$query))
			$response['status']='DATA_ADDED';
	}
	echo json_encode($response);
?>