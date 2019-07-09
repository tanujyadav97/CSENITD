<?php

require_once('global.php');
if(isset($_POST['username'])&&isset($_POST['code'])&&isset($_POST['password']))
     {
       $username=$_POST['username'];
       $code=$_POST['code'];
       $password=$_POST['password'];
       $query1="select code from logindata where username='$username'";       
       $query="update logindata set password=SHA('$password') where username='$username'";

       $result = mysqli_query($dbc, $query1);
       $output='';

       if(!empty($result))
       {
          if(mysqli_num_rows($result)==0)
          {
             $output='notexist';
           }
          else
          {
            $row = mysqli_fetch_array($result);
            $dbcode=$row['code'];
            if($dbcode!=$code)
              $output='wrongcode';
            else
            {
              $result = mysqli_query($dbc, $query);
              if(!empty($result))
              {
                $output='true';
              } 
              else
              {
                $output='false';
              }
            }
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