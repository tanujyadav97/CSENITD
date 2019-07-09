<?php

 require_once('global.php');
$a=mysqli_real_escape_string($dbc,$_POST['title']);
$a=str_ireplace('drop','',$a);
$a=str_ireplace('alter','',$a);
$b=mysqli_real_escape_string($dbc,$_POST['desc']);
$b=str_ireplace('drop','',$b);
$b=str_ireplace('alter','',$b);
$c=mysqli_real_escape_string($dbc,$_POST['link']);
$c=str_ireplace('drop','',$c);
$c=str_ireplace('alter','',$c);
$d=mysqli_real_escape_string($dbc,$_POST['tutor']);
$d=str_ireplace('drop','',$d);
$d=str_ireplace('alter','',$d);
$e=$_POST['date'];
$f=mysqli_real_escape_string($dbc,$_POST['venue']);
$f=str_ireplace('drop','',$f);
$f=str_ireplace('alter','',$f);
$g=$_POST['id'];

 $sql = "update classes set title='$a',descr='$b',link='$c',tutor='$d',date='$e',venue='$f' where id='$g'";
 $res = mysqli_query($dbc,$sql);

 if($res){
echo 'true';
 }
else
echo 'false';
 
 mysqli_close($dbc);

?>