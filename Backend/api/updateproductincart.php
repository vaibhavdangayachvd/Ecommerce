<?php
	function checkArguments()
	{
		if(isset($_POST['username'])&& isset($_POST['productId'])&&isset($_POST['quantity']))
			return 1;
		else
			return 0;
	}
	require 'includes/connection.php';
	$response=array('status'=>'UPDATE_FAILED');
	if(checkArguments())
	{
		$username=$_POST['username'];
		$productId=$_POST['productId'];
		$quantity=$_POST['quantity'];
		if($quantity>0)
			$query="update cart set quantity=quantity+$quantity where username='$username' and productId=$productId";
		else
			$query="update cart set quantity=quantity+$quantity where username='$username' and productId=$productId and quantity+$quantity > 0";
		mysqli_query($db,$query);
		if(mysqli_affected_rows($db))
			$response['status']='UPDATE_SUCCESS';
	}
	echo json_encode($response);
?>