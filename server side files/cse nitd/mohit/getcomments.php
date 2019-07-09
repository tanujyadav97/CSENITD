<?php
require_once('dbConnect.php');
$res=mysqli_query($db,"select * from comments order by cmt_id desc");

$result = array();
while($row=mysqli_fetch_array($res))
{
$res2=mysqli_query($db,"select name from profile where username='$row[0]'");
$row2=mysqli_fetch_array($res2);
$name=$row['name'];
$res3=mysqli_query($db,"select image from dp where username='$row[0]'");
$row3=mysqli_fetch_array($res3);
array_push($result,
array('postid'=>$row[2],
'commenterimg'=>$row3[0],
'commentername'=>$row2[0],
'comment'=>$row[3],
'date'=>$row[4]
));
}
echo json_encode(array("commentresult"=>$result));
 
mysqli_close($db);
?>