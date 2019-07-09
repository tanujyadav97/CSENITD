<?php

require_once('global.php');
if(isset($_POST['id'])&&isset($_POST['user']))
     {
       $id=$_POST['id'];
       $user=$_POST['user'];
       
       $query="select* from timelinelikes where post_id='$id' and username='$user'";       
       $result = mysqli_query($dbc, $query);
       $output='';
       if(mysqli_num_rows($result)>0)
       {
          //cancel vote
          $output='two';
          $query="delete from timelinelikes where post_id='$id' and username='$user'";       
          $result = mysqli_query($dbc, $query);
          $query="update Timeline set likes=likes-1 where post_id='$id'";       
          $result = mysqli_query($dbc, $query);
       }
       else
       {
          //like
          $output='one';
          $query="insert into timelinelikes values('$id','$user')";       
          $result = mysqli_query($dbc, $query);
          $query="update Timeline set likes=likes+1 where post_id='$id'";       
          $result = mysqli_query($dbc, $query);
       }
 }
else
{
    $output='false';  
}

echo $output;

mysqli_close($dbc);
     
?>