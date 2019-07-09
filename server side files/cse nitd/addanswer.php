<?php 
require_once('global.php'); 

if(isset($_POST['quesid'])&&isset($_POST['ansby'])&&isset($_POST['ans'])) 
{ 
$quesid=$_POST['quesid']; 
$ansby=$_POST['ansby']; 
$ans=mysqli_real_escape_string($dbc,$_POST['ans']); 
$ans=str_ireplace('drop','',$ans);
$ans=str_ireplace('alter','',$ans);
$link=mysqli_real_escape_string($dbc,$_POST['link']); 
$link=str_ireplace('drop','',$link);
$link=str_ireplace('alter','',$link);
$time=time(); 

$sql = "insert into answers values('$quesid','$time','$ans','$ansby','$link',0,0,'$time')"; 
$res = mysqli_query($dbc,$sql); 

if($res) 
{ 
$sql = "update profile set answer=answer+1 where username='$ansby'"; 
$res = mysqli_query($dbc,$sql); 

echo 'true'; 
} 
else 
echo 'false'; 
} 
else 
echo 'false'; 
mysqli_close($dbc); 
?>