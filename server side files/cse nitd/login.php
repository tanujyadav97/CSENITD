<?php
require_once('global.php');
if(isset($_POST['username']) && isset($_POST['password']))
     {
$username=$_POST['username'];
$password=$_POST['password'];

$hexed=bin2hex($username);

#$query="select* from logindata where username='$username' AND password=SHA('$password')";
$query="select* from logindata where username=UNHEX('$hexed') AND password=SHA('$password')";


$result = mysqli_query($dbc, $query);
$output='';

if(!empty($result))
{
    if(mysqli_num_rows($result)>0)
    {
      //$row=mysqli_fetch_array($result);
     // $response["success"] = 1;
      // echo json_encode($response);
      $output='true';
    }
    else
    {
      // $response["success"] = 0;
      // echo json_encode($response);
      $output='false';
    }
}
else
{
    $output='false';
    //$response["success"] = 0;
    //   echo json_encode($response);
}




echo $output;

mysqli_close($dbc);
     }
?>