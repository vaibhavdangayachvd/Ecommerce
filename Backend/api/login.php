<?php
    require 'includes/connection.php';
    $response=array('status'=>'ACCESS_DENIED');
    if(isset($_POST['username']) && isset($_POST['password']))
    {
        $username=$_POST['username'];
		$password=null;
		if(isset($_GET['forSessionCheck']))
		    $password=$_POST['password'];
	    else
			$password=sha1($_POST['password']);
        $query="select firstName,password,hasDp from users where username='$username'";
        $data=mysqli_query($db,$query);
        if(mysqli_num_rows($data))
        {
            $row=mysqli_fetch_array($data);
            if(!strcmp($password,$row['password']))
            {
                $response['status']="ACCESS_GRANTED";
                $response['firstName']=$row['firstName'];
				$response['passwordHash']=$row['password'];
				$response['hasDp']=$row['hasDp'];
            }
        }
    }
    echo json_encode($response);
?>