<?php

 require_once('dbConnect.php');
 if(isset($_POST['comment'])&&isset($_POST['username']))
{
$content=$_POST['comment'];
$content=str_ireplace('drop','',$content);
$content=str_ireplace('alter','',$content);
$username=$_POST['username'];
$time=$_POST['time'];
$id=$_POST['postid'];
$idn=(int)$id;  
$sql = "insert into comments values('$username',null,'$idn','$content','$time')";
$res = mysqli_query($db,$sql);
if($res){
echo 'true';}
     else
echo 'false';
}
 
else
 echo 'false';
 mysqli_close($db);
?>