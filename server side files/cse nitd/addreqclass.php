<?php

 require_once('global.php');
$a=mysqli_real_escape_string($dbc,$_POST['title']);
$a=str_ireplace('drop','',$a);
$a=str_ireplace('alter','',$a);
$b=mysqli_real_escape_string($dbc,$_POST['desc']);
$b=str_ireplace('drop','',$b);
$b=str_ireplace('alter','',$b);
$g=$_POST['user'];
$tm=time();

 $sql = "insert into reqclasses values(null,'$a','$b',0,'$tm','$g')";
 $res = mysqli_query($dbc,$sql);

 if($res){
echo 'true';
 }
else
echo 'false';
 
 mysqli_close($dbc);

?>