<?php
	require 'includes/connection.php';
	$response=array('status'=>'DATA_NOT_FOUND');
	$query="select * from categories";
	$data=mysqli_query($db,$query);
	if(mysqli_num_rows($data))
	{
		$response['status']='DATA_FOUND';
		$counter=0;
		while($row=mysqli_fetch_array($data))
		{
			$response['name'][$counter]=$row['name'];
			$response['image'][$counter]=$row['image'];
			$counter++;
		}
	}
	echo json_encode($response);
?>