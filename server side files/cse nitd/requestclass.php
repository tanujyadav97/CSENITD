<?php

require_once('global.php');
if(isset($_POST['id'])&&isset($_POST['user']))
   {
       $id=$_POST['id'];
       $user=$_POST['user'];
       
       $query="select* from requesting where classid='$id' and username='$user'";       
       $result = mysqli_query($dbc, $query);
       $output='';
       if(mysqli_num_rows($result)>0)
       {
          //cancel attend
          $output='two';
          $query="delete from requesting where classid='$id' and username='$user'";       
          $result = mysqli_query($dbc, $query);
          $query="update reqclasses set studreq=studreq-1  where id='$id'";       
          $result = mysqli_query($dbc, $query);
       }
       else
       {
          //attend
          $output='one';
          $query="insert into requesting values('$id','$user')";       
          $result = mysqli_query($dbc, $query);
          $query="update reqclasses set studreq=studreq+1 where id='$id'";       
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