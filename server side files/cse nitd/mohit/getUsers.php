<?php
require_once('dbConnect.php');
$res=mysqli_query($db,"select * from profile");
$result = array();
while($row=mysqli_fetch_array($res))
{
$usernm=mysqli_real_escape_string($db,$row['username']);
$res2=mysqli_query($db,"select image from dp where username='$usernm'");
$row2=mysqli_fetch_array($res2);

array_push($result,
array('username'=>$row['username'],
'name'=>$row['name'],
'reputation'=>$row['reputation'],
'image'=>$row2['image']
));
}
echo json_encode(array("userresult"=>$result));
?>