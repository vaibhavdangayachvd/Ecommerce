<?php
	require 'includes/connection.php';
	$response=array('status'=>'DATA_NOT_FOUND');
	if(isset($_GET['username']))
	{
		$username=$_GET['username'];
		$query="select * from addresses where username='$username'";
		$data=mysqli_query($db,$query);
		if(mysqli_num_rows($data))
		{
			$response['status']='DATA_FOUND';
			$counter=0;
			while($row=mysqli_fetch_array($data))
			{
				$response['id'][$counter]=$row['id'];
				$response['address'][$counter]=$row['address'];
				$counter++;
			}
		}
	}
	echo json_encode($response);
?>