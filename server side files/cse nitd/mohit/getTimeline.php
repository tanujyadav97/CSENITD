<?php
require_once('dbConnect.php');
$user=$_POST['user'];

$res=mysqli_query($db,"select * from Timeline order by post_id desc");
$result = array();
while($row=mysqli_fetch_array($res))
{

$sql11=mysqli_query($db,"select* from timelinelikes where post_id='$row[0]' and username='$user'");
$row11=mysqli_fetch_array($sql11);
$liked=0;
if(mysqli_num_rows($sql11)>0)
$liked=1;

$username=$row[1];
$res2=mysqli_query($db,"select name from profile where username='$username'");
$row2=mysqli_fetch_array($res2);
$name=$row2[0];
$res3=mysqli_query($db,"select image from dp where username='$username'");
$row3=mysqli_fetch_array($res3);
$usrimg=$row3['image'];
$res4=mysqli_query($db,"select count(*)  from comments where post_id='$row[0]'  group by post_id");

$row4=mysqli_fetch_array($res4);
if($row4[0]==null)
    $row4[0]=0;
array_push($result,
array('name'=>$name,
'post_id'=>$row[0].' '.$liked,
'text'=>$row[2],
'img1'=>$row[3],
'img2'=>$row[4],
'img3'=>$row[5],
'img4'=>$row[6],
'img5'=>$row[7],
'video'=>$row[8],
'likes'=>$row[11],
'datetime'=>$row[12],
'userimage'=>$usrimg,
'comments'=>$row4[0]
));
}
echo json_encode(array("timelineresult"=>$result));
 
mysqli_close($db);
?>