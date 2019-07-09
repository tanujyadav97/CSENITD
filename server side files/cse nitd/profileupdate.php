<?php

require_once('global.php');
if(isset($_POST['username']))
     {
       $username=$_POST['username'];
       $status=mysqli_real_escape_string($dbc,$_POST['status']);
       $status=str_ireplace('drop','',$status);
       $status=str_ireplace('alter','',$status);
       $branch=$_POST['branch'];
       $org=mysqli_real_escape_string($dbc,$_POST['organisation']);
       $org=str_ireplace('drop','',$org);
       $org=str_ireplace('alter','',$org);
       $email=mysqli_real_escape_string($dbc,$_POST['email']);
       $email=str_ireplace('drop','',$email);
       $email=str_ireplace('alter','',$email);
       $phone=mysqli_real_escape_string($dbc,$_POST['phone']);
       $phone=str_ireplace('drop','',$phone);
       $phone=str_ireplace('alter','',$phone);
       $tech=mysqli_real_escape_string($dbc,$_POST['tech']);
       $tech=str_ireplace('drop','',$tech);
       $tech=str_ireplace('alter','',$tech);
       $location=mysqli_real_escape_string($dbc,$_POST['location']);
       $location=str_ireplace('drop','',$location);
       $location=str_ireplace('alter','',$location);
       $desig=$_POST['desig'];

       $query="update profile set status='$status',branch='$branch',org='$org',email='$email',phone='$phone',knowntech='$tech',location='$location',desig='$desig' where username='$username'";       
       $result = mysqli_query($dbc, $query);
       $output='';

       if(!empty($result))
       {
          
        $output='true';
         
        }
        else
        {
          $output='false';
        }
 }
else
{
    $output='false';  
}

echo $output;

mysqli_close($dbc);
     
?>