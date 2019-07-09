<?php

require_once('global.php');
if(isset($_POST['id'])&&isset($_POST['luid'])&&isset($_POST['puid'])&&isset($_POST['vote'])&&isset($_POST['isques']))
     {
       $id=$_POST['id'];
       $luid=$_POST['luid'];
       $puid=$_POST['puid'];
       $vote=$_POST['vote'];
       $isques=$_POST['isques'];
       
       $query="select vote from votes where id='$id' and byy='$luid'";       
       $result = mysqli_query($dbc, $query);
       $output='';

       if(!empty($result))
       {
          $repochange=0;
          $votechange=0;
           if(mysqli_num_rows($result)>0)
           {
               //voting second time
               
               
               $newvote=0;

              $row = mysqli_fetch_array($result);
              $lastvote=$row['vote'];
              if($lastvote==$vote)
              {
                 if($vote==1)  //upvoting again
                  {
                      $output='three';
                      $repochange=-10;//deccrese repo by 10
                      $newvote=0;
                      $votechange=-1;
                  }
                  else   //downvoting again
                  {
                      $output='four';
                      $repochange=2;//increse repo by 2
                      $newvote=0;
                      $votechange=1;
                  }

              }
              else
              {
                  if($vote==1)  //upvoting after downvoting
                  {
                      $output='five';
                      $repochange=12;//increse repo by 12
                      $newvote=1;
                      $votechange=2;
                  }
                  else   //downvoting after upvoting
                  {
                      $output='six';
                      $repochange=-12;//decrese repo by 12
                      $newvote=-1;
                      $votechange=-2;
                  }

              }  

             
                  //update vote table, if newvote is zero then delete row
                  if($newvote==0)
                   $query1="delete from votes where id='$id' and byy='$luid'";       
                  else
                   $query1="update votes set vote='$newvote' where id='$id' and byy='$luid'";  
                   
                   $result1 = mysqli_query($dbc, $query1);

           }
           else
           {
               $newvote1=0;
              //voting first time
              if($vote==1)  //upvoting first time
                  {
                      $output='one';
                      $repochange=10;//increse repo by 10
                      $newvote1=1;
                      $votechange=1;
                  }
                  else   //downvoting first time
                  {
                      $output='two';
                      $repochange=-2;//decrese repo by 2
                      $newvote1=-1;
                      $votechange=-1;
                  }

                  //insert new row in table

                   $query2="insert into votes values('$id','$luid','$newvote1')";  
                   
                   $result2 = mysqli_query($dbc, $query2);

           }            

           //change repo
         $query3="update profile set reputation=reputation+'$repochange' where username='$puid'";  
                   
                   $result3 = mysqli_query($dbc, $query3);

          if($isques==1)
          {$query4="update question set votes=votes+'$votechange' where time='$id'";  
           $result4 = mysqli_query($dbc, $query4);
          }
          else
          {
              $query4="update answers set votes=votes+'$votechange' where ansid='$id'";  
              $result4 = mysqli_query($dbc, $query4);
          }
        }
        else
        {
          $output='false';
        }
 }
else
{
    $output='false';  
}

echo $output;

mysqli_close($dbc);
     
?>