<?php

require_once('global.php');
if(isset($_POST['username']))
     {
       $username=$_POST['username'];
       
       $query="select image from dp where username='$username'";       
       $result = mysqli_query($dbc, $query);
       $output='';

       if(!empty($result))
       {
          
            $row = mysqli_fetch_array($result);
           
            $output=$row['image'];
         
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