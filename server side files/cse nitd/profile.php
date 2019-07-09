<?php

require_once('global.php');
if(isset($_POST['username']))
     {
       $username=$_POST['username'];
       
       $query="select* from profile where username='$username'";       
       $result = mysqli_query($dbc, $query);
       $output='';

       if(!empty($result))
       {
          
            $row = mysqli_fetch_array($result);
           
            $output='true'.'~'.$row['answer'].'~'.$row['question'].'~'.$row['status'].'~'.$row['branch'].'~'.$row['org'].'~'.$row['email'].'~'.$row['phone'].'~'.$row['knowntech'].'~'.$row['location'].'~'.$row['desig'].'~'.$row['reputation'].'~'.$row['name'].'~'.$row['username'];
         
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