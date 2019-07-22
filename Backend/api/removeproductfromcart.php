<?php
	require 'includes/connection.php';
	$response = array('status'=>'PRODUCT_NOT_REMOVED');
	if(isset($_POST['username']) && isset($_POST['productId']))
	{
		$username=$_POST['username'];
		$productId=$_POST['productId'];
		$query="delete from cart where username='$username' and productId=$productId";
		mysqli_query($db,$query);
		if(mysqli_affected_rows($db))
			$response['status']='PRODUCT_REMOVED';
	}
	echo json_encode($response);
?>