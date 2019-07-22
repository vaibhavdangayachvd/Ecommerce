<?php
	function checkArguments()
	{
		if(isset($_GET['start'])&&isset($_GET['offset'])&&isset($_GET['category']))
			return 1;
		else
			return 0;
	}
	require 'includes/connection.php';
	$response=array('status'=>'DATA_NOT_FOUND');
	if(checkArguments())
	{
		$category=$_GET['category'];
		$start=$_GET['start'];
		$offset=$_GET['offset'];
		$query="SELECT * FROM products where category='$category' LIMIT $start,$offset";
		$data=mysqli_query($db,$query);
		if(mysqli_num_rows($data))
		{
			$response['status']='DATA_FOUND';
			$counter=0;
			while($row=mysqli_fetch_array($data))
			{
				$response['name'][$counter]=$row['name'];
				$response['price'][$counter]=$row['price'];
				$response['description'][$counter]=$row['description'];
				$response['image'][$counter]=$row['image'];
				$response['id'][$counter]=$row['id'];
				$counter++;
			}
		}
	}
	echo json_encode($response);
?>