<?php
require_once('dbConnect.php'); 
$user=$_POST['user'];
$sql = "select * from acheivements inner join userachievement on acheivements.ach_id=userachievement.ach_id order by acheivements.ach_id desc";
 
$res = mysqli_query($db,$sql);
 
$result = array();
 $username=0;
while($row = mysqli_fetch_array($res)){

$username= $row['username'];
$sql1=mysqli_query($db,"select* from achievementlikes where ach_id='$row[0]' and username='$user'");
$row1=mysqli_fetch_array($sql1);
$liked=0;
if(mysqli_num_rows($sql1)>0)
$liked=1;

$sql2=mysqli_query($db,"select image from dp where username='$username'");
$row3=mysqli_fetch_array($sql2);
$image= $row3[0];
$sql3=mysqli_query($db,"select name,reputation from profile where username='$username'");
$row4=mysqli_fetch_array($sql3);
$name=$row4['name'];
$reputation=$row4['reputation'];
array_push($result,
array(
'title'=>$row[1],
'des'=>$row[2],
'img'=>$row[3],
'likes'=>$row[4],
'datetime'=>$row[5],
'username'=> $username,
'userimage'=>$image,
'name'=>$name,
'reputation'=>$reputation,
'liked'=>$liked." ".$row[0]
));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);

?>