<?php
 require_once('global.php');

 if(isset($_POST['quesid'])) 
{
  $quesid=$_POST['quesid'];
  $sql = "select* from answers where quesid='$quesid' order by accepted desc,votes desc";
 
  $res = mysqli_query($dbc,$sql);
 
  $result = array();

  while($row = mysqli_fetch_array($res)){

 $ansby=$row['ansby'];
 $sql1 = "select name,reputation from profile where username='$ansby'";
 $sql2 = "select image from dp where username='$ansby'";

  $res1 = mysqli_query($dbc,$sql1);
  $res2 = mysqli_query($dbc,$sql2);
  $row1= mysqli_fetch_array($res1);
  $row2= mysqli_fetch_array($res2);

  $user=$_POST['username'];
  $ansid=$row['ansid'];
  $query5="select vote from votes where id='$ansid' and byy='$user'";       
  $result5 = mysqli_query($dbc, $query5);
  $voted=0;
  if(mysqli_num_rows($result5)>0)
  {
    $row5=mysqli_fetch_array($result5);
    $voted=$row5['vote'];
   }
  
 array_push($result,array('time'=>$row['time'],'votes'=>$row['votes'],'quesid'=>$row['quesid'],'ans'=>$row['ans'],'ansby'=>$row['ansby'],'link'=>$row['link'],'accepted'=>$row['accepted'],'ansid'=>$row['ansid'],'ansbyname'=>$row1['name'],'ansbyrepo'=>$row1['reputation'],'dpurl'=>$row2['image'],'voted'=>$voted));

  }
   echo json_encode(array("result"=>$result));
}
else
echo 'false';


 mysqli_close($dbc);

?>