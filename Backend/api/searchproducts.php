<?php
	require 'includes/connection.php';
	$response=array('status'=>'DATA_NOT_FOUND');
	if(isset($_GET['keyword']))
	{
		$key=$_GET['keyword'];
		$query="select * from products where name like '".$key."%' LIMIT 8";
		$data=mysqli_query($db,$query);
		if(mysqli_num_rows($data))
		{
			$response['status']='DATA_FOUND';
			$counter=0;
			while($row=mysqli_fetch_array($data))
			{
				$response['id'][$counter]=$row['id'];
				$response['name'][$counter]=$row['name'];
				$response['image'][$counter]=$row['image'];
				$response['description'][$counter]=$row['description'];
				$response['price'][$counter]=$row['price'];
				$response['category'][$counter]=$row['category'];
				$counter++;
			}
		}
	}
	echo json_encode($response);
?>