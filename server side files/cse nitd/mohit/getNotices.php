<?php
require_once('dbConnect.php');
$res=mysqli_query($db,"select * from noticedata order by notice_id desc");
$result = array();
while($row=mysqli_fetch_array($res))
{
array_push($result,
array('notice'=>$row[1],
'name'=>$row[2],
'datetime'=>$row[3]
));
}
echo json_encode(array("noticeresult"=>$result));
 
mysqli_close($db);
?>