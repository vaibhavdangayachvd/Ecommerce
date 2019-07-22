<?php
	require 'includes/connection.php';
	$response=array('status'=>'USER_NOT_FOUND');
	if(isset($_GET['username']))
	{
		$username=$_GET['username'];
		$query="select firstName,lastName,email,mobile,gender,hasDp from users where username='$username'";
		$data=mysqli_query($db,$query);
		if(mysqli_num_rows($data))
		{
			$row=mysqli_fetch_array($data);
			$response['status']='USER_FOUND';
			$response['firstName']=$row['firstName'];
			$response['lastName']=$row['lastName'];
			$response['email']=$row['email'];
			$response['mobile']=$row['mobile'];
			$response['gender']=$row['gender'];
			$response['hasDp']=$row['hasDp'];
		}
	}
	echo json_encode($response);
?>