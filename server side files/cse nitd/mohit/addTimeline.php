<?php
if(isset($_POST['username'])){
 
$des=$_POST['text'];
$des=str_ireplace('drop','',$des);
$des=str_ireplace('alter','',$des);
$img1=$_POST['img1'];
$img2 = $_POST['img2'];
$img3=$_POST['img3'];
$img4=$_POST['img4'];
$img5=$_POST['img5'];
$username=$_POST['username'];
if(isset($_POST['video']))
{$video=$_POST['video'];}
else
{$video=NULL;}
$gif=NULL;
$datetime=$_POST['datetime'];
require_once('dbConnect.php'); 
$curr=time();
$path1 = "upload/$curr.1.png"; 
$path2= "upload/$curr.2.png";
$path3= "upload/$curr.3.png";
$path4="upload/$curr.4.png";
$path5="upload/$curr.5.png";
if(isset($_POST['img1'])){$actualpath1 = "https://nitd.000webhostapp.com/cse%20nitd/mohit/$path1";}
else{$actualpath1=NULL;}
if(isset($_POST['img2'])){$actualpath2 = "https://nitd.000webhostapp.com/cse%20nitd/mohit/$path2";}
else{$actualpath2=NULL;}
if(isset($_POST['img3'])){$actualpath3 = "https://nitd.000webhostapp.com/cse%20nitd/mohit/$path3";}
else{$actualpath3=NULL;}
if(isset($_POST['img4'])){$actualpath4 = "https://nitd.000webhostapp.com/cse%20nitd/mohit/$path4";}
else{$actualpath4=NULL;}
if(isset($_POST['img5'])){$actualpath5 = "https://nitd.000webhostapp.com/cse%20nitd/mohit/$path5";}
else{$actualpath5=NULL;}
 $sql = "insert into Timeline values(null,'$username','$des','$actualpath1','$actualpath2','$actualpath3','$actualpath4','$actualpath5','$video','$gif',null,0,'$datetime')";
 
 if(mysqli_query($db,$sql)){
if(isset($_POST['img1'])){
 file_put_contents($path1,base64_decode($img1));}
if(isset($_POST['img2'])){
file_put_contents($path2,base64_decode($img2));}
if(isset($_POST['img3'])){
file_put_contents($path3,base64_decode($img3));}
if(isset($_POST['img4'])){
file_put_contents($path4,base64_decode($img4));}
if(isset($_POST['img5'])){
file_put_contents($path5,base64_decode($img5));}
 echo "true";
 }
else
{
echo "false1";
}
 }else{
 echo "false";
}

 mysqli_close($db);
?>