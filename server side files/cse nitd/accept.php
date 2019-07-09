<?php

require_once('global.php');
if(isset($_POST['id'])&&isset($_POST['puid'])&&isset($_POST['type'])&&isset($_POST['quesid']))
     {
       $id=$_POST['id'];
       $puid=$_POST['puid'];
       $type=$_POST['type'];
       $quesid=$_POST['quesid'];

       $query="";
       if($type==1)
       {
           $query="update answers set accepted='0' where ansid='$id'";
           $result = mysqli_query($dbc, $query);
          
           $query="select accepted from question where time='$quesid'";
           $result = mysqli_query($dbc, $query);
           if(mysqli_num_rows($result)>0)
           {
            $row = mysqli_fetch_array($result);
             if($row['accepted']==1)
              {
                $query="update question set accepted='0' where time='$quesid'";
                $result = mysqli_query($dbc, $query);
           
             $query="update profile set reputation=reputation-10 where username='$puid'";
              $result = mysqli_query($dbc, $query);
                }
             }
           
       }
       else
       {
          $query="select ansby from answers where quesid='$quesid' and accepted=1";
          $result = mysqli_query($dbc, $query);  
           if(mysqli_num_rows($result)>0)    // accepting second time
           {
              $row = mysqli_fetch_array($result);
              $ansby=$row['ansby'];
              $query1="update answers set accepted='0' where quesid='$quesid' and accepted=1";
              $result1 = mysqli_query($dbc, $query1);

              $query1="update answers set accepted='1' where ansid='$id'";
              $result1 = mysqli_query($dbc, $query1);

              $query1="update profile set reputation=reputation-10 where username='$ansby'";
              $result1 = mysqli_query($dbc, $query1);
             
              $query1="update profile set reputation=reputation+10 where username='$puid'";
              $result1 = mysqli_query($dbc, $query1);
           }
           else
           {
                 $query="update answers set accepted='1' where ansid='$id'";
                 $result = mysqli_query($dbc, $query);
           
                $query="update question set accepted='1' where time='$quesid'";
                $result = mysqli_query($dbc, $query);
           
                $query="update profile set reputation=reputation+10 where username='$puid'";
                $result = mysqli_query($dbc, $query);
           }  
       }
      echo 'true';
 }
else
{
    echo 'false';  
}

mysqli_close($dbc);
     
?>