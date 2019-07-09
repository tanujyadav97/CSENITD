<?php

 require_once('global.php');
 if(isset($_POST['content'])&&isset($_POST['username']))
{
$content=mysqli_real_escape_string($dbc,$_POST['content']);
$content=str_ireplace('drop','',$content);
$content=str_ireplace('alter','',$content);
$username=$_POST['username'];

$time=time();

 $sql = "select name,desig from profile where username='$username'";
 $res = mysqli_query($dbc,$sql);
 $row=mysqli_fetch_array($res);
  $name=$row['name'];
  $desig=$row['desig'];

if($desig=='Faculty')
{
   
 $sql = "insert into noticedata values(null,'$content','$name','$time')";
 $res = mysqli_query($dbc,$sql);

 if($res){
    
     echo 'true';
     }
     else
      echo 'false';
}
else
echo 'negative';

 }
else
 echo 'false';
 mysqli_close($dbc);

?>