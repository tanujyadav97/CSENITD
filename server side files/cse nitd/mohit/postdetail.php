<?php
require_once('dbConnect.php');
$id=$_POST['postid'];
//$id='50';

$idn=(int)$id;
$user=$_POST['username'];
$res=mysqli_query($db,"select * from Timeline where post_id='$idn'");
$res2=mysqli_query($db,"select name from profile where username='$user'");
if($r1=mysqli_fetch_array($res2)){
$usern=$r1['name'];
}
$sql11=mysqli_query($db,"select* from timelinelikes where post_id='$idn' and username='$user'");
$row11=mysqli_fetch_array($sql11);
$liked=0;
if(mysqli_num_rows($sql11)>0)
$liked=1;



$res3=mysqli_query($db,"select image from dp where username='$user'");
if($r2=mysqli_fetch_array($res3)){
$userimg=$r2['image'];
}
$result = "";
if($row=mysqli_fetch_array($res))
{
$username=$row[1];
$res2=mysqli_query($db,"select name from profile where username='$username'");
$row2=mysqli_fetch_array($res2);
$name=$row2[0];
$res3=mysqli_query($db,"select image from dp where username='$username'");
$row3=mysqli_fetch_array($res3);
$posterimg=$row3['image'];
 $result=$name.'___'.$row['text'].'___'.$row['img1'].'___'.$row['img2'].'___'.$row['img3'].'___'.$row['img4'].'___'.$row['img5'].'___'.$row['datetime'].'___'.$row['video'].'___'.$row['likes'].'___'.$userimg.'___'.$usern.'___'.$liked.'___'.
$posterimg;

echo $result;
 }
else
{ echo false;}
mysqli_close($db);
?>