<?php

 require_once('global.php');
 if(isset($_POST['quesid'])&&isset($_POST['quesusername']))
{
$quesid=$_POST['quesid'];
$quesusername=$_POST['quesusername'];
 $sql = "select* from question where time='$quesid'";
 $sql1 = "select name,reputation from profile where username='$quesusername'";
 $res = mysqli_query($dbc,$sql);
 $res1 = mysqli_query($dbc,$sql1);

$user=$_POST['username'];
$query5="select vote from votes where id='$quesid' and byy='$user'";       
       $result5 = mysqli_query($dbc, $query5);
$vote=0;
if(mysqli_num_rows($result5)>0)
{
$row5 = mysqli_fetch_array($result5);
$vote=$row5['vote'];
}

$result="";
 //if(mysqli_num_rows($res)>0&&mysqli_num_rows($res1)>0){
 if(($row = mysqli_fetch_array($res))&&( $row1 = mysqli_fetch_array($res1))){
 $result=$row1['name'].'___'.$row1['reputation'].'___'.$row['votes'].'___'.$row['topic'].'___'.$row['tags'].'___'.$row['ques'].'___'.$row['link'].'___'.$row['time'].'___'.$vote;

echo $result;
 }
else
echo 'false';
 }
else
 echo 'false';
 mysqli_close($dbc);

?>