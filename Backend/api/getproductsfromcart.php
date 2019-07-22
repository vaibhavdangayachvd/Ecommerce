<?php
	require 'includes/connection.php';
	$response=array('status'=>'DATA_NOT_FOUND');
	if(isset($_GET['username']))
	{
		$username=$_GET['username'];
		$query="select productId,quantity from cart where username='$username'";
		$data=mysqli_query($db,$query);
		if(mysqli_num_rows($data))
		{
			$counter=0;
			while($row=mysqli_fetch_array($data))
			{
				$productId=$row['productId'];
				$query="select name,price,image,category from products where id=$productId";
				$info=mysqli_query($db,$query);
				if(mysqli_num_rows($info))
				{
					$response['status']='DATA_FOUND';
					$product=mysqli_fetch_array($info);
					$response['productId'][$counter]=$row['productId'];
					$response['quantity'][$counter]=$row['quantity'];
					$response['image'][$counter]=$product['image'];
					$response['name'][$counter]=$product['name'];
					$response['price'][$counter]=$product['price'];
					$response['category'][$counter]=$product['category'];
					++$counter;
				}
				else
				{
					$query="delete from cart where productId=$productId";
					mysqli_query($db,$query);
				}
			}
		}
	}
	echo json_encode($response);
?>