<?php
	function checkArguments()
	{
		if(isset($_POST['username'])&&isset($_POST['productId']))
			return 1;
		else
			return 0;
	}
	function checkIfExist($username,$productId)
	{
		global $db;
		$query="select count(username) from cart where username='$username' and productId=$productId";
		$data=mysqli_query($db,$query);
		$data=mysqli_fetch_array($data);
		return $data['count(username)'];
	}
	require 'includes/connection.php';
	$response=array('status'=>'PRODUCT_NOT_ADDED');
	if(checkArguments())
	{
		$username=$_POST['username'];
		$productId=$_POST['productId'];
		if(!checkIfExist($username,$productId))
		{
			$query="insert into cart values('$username',$productId,1)";
			if(mysqli_query($db,$query))
				$response['status']='PRODUCT_ADDED';
		}
		else
		    $response['status']='PRODUCT_ALREADY_ADDED';
	}
	echo json_encode($response);
?>