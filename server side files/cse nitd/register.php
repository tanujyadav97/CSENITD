<?php
require_once('global.php');
if(isset($_POST['username']) && isset($_POST['password']))
     {
$username=mysqli_real_escape_string($dbc,$_POST['username']);
$password=$_POST['password'];
$name=mysqli_real_escape_string($dbc,$_POST['name']);
$name=str_ireplace('drop','',$name);
$name=str_ireplace('alter','',$name);
$email=mysqli_real_escape_string($dbc,$_POST['email']);
$email=str_ireplace('drop','',$email);
$email=str_ireplace('alter','',$email);
$phone=mysqli_real_escape_string($dbc,$_POST['phone']);
$phone=str_ireplace('drop','',$phone);
$phone=str_ireplace('alter','',$phone);
$location=mysqli_real_escape_string($dbc,$_POST['location']);
$location=str_ireplace('drop','',$location);
$location=str_ireplace('alter','',$location);
$desig=$_POST['desig'];

$query="insert into logindata values('$username',SHA('$password'),'')";
$query1="select* from logindata where username='$username'";
$query2="insert into profile values('$username','$name','$email','$phone','$location','$desig',0,0,'n/a','n/a','n/a',0,'n/a')";
$query3="insert into dp values('$username','n/a')";

$result = mysqli_query($dbc, $query1);
$output='';

if(!empty($result))
{
    if(mysqli_num_rows($result)>0)
    {
   
      $output='already';
    }
    else
   {
    $result = mysqli_query($dbc, $query);
    if(!empty($result))
    {
    $output='true';
       //enter query to enter profile data
       $result1 = mysqli_query($dbc, $query2);
       if(!empty($result1))
        {
          $output='true';
         $result2 = mysqli_query($dbc, $query3);
         }    
   else
       $output='false';
    } 
    else
    {
    $output='false';
    }
  }
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