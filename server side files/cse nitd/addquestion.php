<?php

 require_once('global.php');
 if(isset($_POST['head'])&&isset($_POST['text'])&&isset($_POST['quesby']))
{
$head=mysqli_real_escape_string($dbc,$_POST['head']);
$head=str_ireplace('drop','',$head);
$head=str_ireplace('alter','',$head);
$quesby=$_POST['quesby'];
$text=mysqli_real_escape_string($dbc,$_POST['text']);
$text=str_ireplace('drop','',$text);
$text=str_ireplace('alter','',$text);
$link=mysqli_real_escape_string($dbc,$_POST['link']);
$link=str_ireplace('drop','',$link);
$link=str_ireplace('alter','',$link);
$tags=mysqli_real_escape_string($dbc,$_POST['tags']);
$tags=str_ireplace('drop','',$tags);
$tags=str_ireplace('alter','',$tags);
$time=time();

 $sql = "insert into question values('$time',0,'$head','$text','$tags','$quesby',0,'$link')";
 $res = mysqli_query($dbc,$sql);

 if($res){
$sql = "update profile set question=question+1 where username='$quesby'";
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