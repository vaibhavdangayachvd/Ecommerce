<?php
	require 'includes/connection.php';
	$response=array('status'=>'DATA_NOT_REMOVED');
	if(isset($_POST['id']))
	{
		$id=$_POST['id'];
		$query="delete from addresses where id=$id";
		mysqli_query($db,$query);
		if(mysqli_affected_rows($db))
			$response['status']='DATA_REMOVED';
	}
	echo json_encode($response);
?>