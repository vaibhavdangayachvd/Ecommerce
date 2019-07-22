<?php
    require 'includes/connection.php';
    function hasValidArguments()
    {
         if(isset($_POST['firstName']) && isset($_POST['lastName'])&& isset($_POST['username'])&& isset($_POST['password'])&&isset($_POST['email'])&& isset($_POST['gender'])&& isset($_POST['mobile']))
            return 1;
        else
            return 0;
    }
    $response=array('status'=>'REGISTER_FAILED');
    if(hasValidArguments())
    {
        $firstName=$_POST['firstName'];
        $lastName=$_POST['lastName'];
        $username=$_POST['username'];
        $password=sha1($_POST['password']);
        $email=$_POST['email'];
        $gender=$_POST['gender'];
        $mobile=$_POST['mobile'];

        $chkuser="select username from users where username='$username'";
		$chkemail="select email from users where email='$email'";
		$chkmobile="select mobile from users where mobile='$mobile'";
		
		$chkuser=mysqli_query($db,$chkuser);
		$chkemail=mysqli_query($db,$chkemail);
		$chkmobile=mysqli_query($db,$chkmobile);
		
		
		if(mysqli_num_rows($chkuser)>0)
			$response=array('status'=>'USER_ALREADY_EXIST');
		else if(mysqli_num_rows($chkemail)>0)
			$response=array('status'=>'EMAIL_ALREADY_EXIST');
		else if(mysqli_num_rows($chkmobile)>0)
			$response=array('status'=>'MOBILE_ALREADY_EXIST');
        else
        {
            $query="insert into users(firstName,lastName,username,password,email,gender,mobile) values('$firstName','$lastName','$username','$password','$email',$gender,'$mobile')";
            if(mysqli_query($db,$query))
                $response=array('status'=>'REGISTER_SUCCESS');
        }
    }
    echo json_encode($response);
?>