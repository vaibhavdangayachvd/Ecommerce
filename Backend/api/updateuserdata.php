<?php
	function validateInput()
	{
		if(isset($_POST['firstName']) && isset($_POST['lastName'])&& isset($_POST['username'])&& isset($_POST['email'])&& isset($_POST['mobile'])&& isset($_POST['gender']))
			return 1;
		else
			return 0;
	}
	require 'includes/connection.php';
	$response=array('status'=>'UPDATE_FAILED');
	if(validateInput())
	{
		$firstName=$_POST['firstName'];
		$lastName=$_POST['lastName'];
		$username=$_POST['username'];
		$email=$_POST['email'];
		$mobile=$_POST['mobile'];
		$gender=$_POST['gender'];
		$query="update users set firstName='$firstName',lastName='$lastName',email='$email',mobile='$mobile',gender=$gender where username='$username'";
		mysqli_query($db,$query);
		if(mysqli_affected_rows($db))
		{
			$response['firstName']=$firstName;
			$response['status']='UPDATE_SUCCESS';
			if(isset($_POST['image']))
			{
				$query="update users set hasDp=1 where username='$username'"; 
				mysqli_query($db,$query);
				if(mysqli_affected_rows($db))
				{
					$path='images/'.$username.'.jpeg';
					file_put_contents($path,base64_decode($_POST['image']));
					$response['hasDp']=1;
				}
			}
			if(isset($_POST['oldPassword'])&& isset($_POST['newPassword']))
			{
				$newPassword=sha1($_POST['newPassword']);
				$oldPassword=sha1($_POST['oldPassword']);
				$query="update users set password='$newPassword' where username='$username' and password='$oldPassword'"; 
				mysqli_query($db,$query);
				if(!mysqli_affected_rows($db))
					$response['status']='INCORRECT_PASSWORD';
				else
					$response['password']=$newPassword;
			}
		}
	}
	echo json_encode($response);
?>