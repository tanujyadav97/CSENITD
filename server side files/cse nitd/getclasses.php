<?php
 require_once('global.php');
$user=$_POST['username'];
  $sql = "select* from classes order by id desc";
 
  $res = mysqli_query($dbc,$sql);
 
  $result = array();

  while($row = mysqli_fetch_array($res)){

 $username=$row['username'];
 $sql1 = "select name from profile where username='$username'";
 $sql2 = "select image from dp where username='$username'";

  $res1 = mysqli_query($dbc,$sql1);
  $res2 = mysqli_query($dbc,$sql2);
  $row1= mysqli_fetch_array($res1);
  $row2= mysqli_fetch_array($res2);

  
  $postid=$row['id'];
  $query5="select* from attending where classid='$postid' and username='$user'";       
  $result5 = mysqli_query($dbc, $query5);
  $attending=0;
  if(mysqli_num_rows($result5)>0)
  {
      $attending=1;
   }
  
 array_push($result,array('id'=>$row['id'],'title'=>$row['title'],'desc'=>$row['descr'],'link'=>$row['link'],'username'=>$row['username'],'studattending'=>$row['studattending'],'tutor'=>$row['tutor'],'date'=>$row['date'],'venue'=>$row['venue'],'postbyname'=>$row1['name'],'dpurl'=>$row2['image'],'attending'=>$attending));

  }
   echo json_encode(array("result"=>$result));


 mysqli_close($dbc);

?>