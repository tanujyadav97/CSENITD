<?php
 require_once('global.php');
 $query=$_POST['query'];

if($query=='')
 $sql = "select* from question order by time desc";
 else
{
  $tagarr=explode(' ',$query);
  $sql="select* from question where tags like '%";
  for($i=0;$i<sizeof($tagarr);$i++)
  {
      if($i<sizeof($tagarr)-1)
      $sql=$sql.$tagarr[$i]."%' or tags like '%";
      else
    $sql=$sql.$tagarr[$i]."%'";
  }
  $sql=$sql." order by time desc";
}
 $res = mysqli_query($dbc,$sql);
 
 $result = array();
 
 while($row = mysqli_fetch_array($res)){
 array_push($result,array('time'=>$row['time'],'votes'=>$row['votes'],'topic'=>$row['topic'],'ques'=>$row['ques'],'tags'=>$row['tags'],'username'=>$row['username'],'accepted'=>$row['accepted']));
 }
 
 echo json_encode(array("result"=>$result));
 
 mysqli_close($dbc);

?>