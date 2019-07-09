<?php
require_once('global.php');
$output='';
if(isset($_POST['username']))
     {
       
        $username=$_POST['username'];
        $query1="select email from profile where username='$username'";
        $result = mysqli_query($dbc, $query1);
        if(!empty($result))
        {
          if(mysqli_num_rows($result)==0)
            {
   
              $output='notexist';
            }
            else
            {
                   #retrieve email
                   $row = mysqli_fetch_array($result);
                   $to=$row['email'];
                    

                   $new=time();
                   $msg='The code to reset your password is'.$new;
                   $sub='Password reset code';

#save new code to db and if successful then send mail to user.


                  $query="update logindata set code='$new' where username='$username'";

                  $result = mysqli_query($dbc, $query);
        
                  if(!empty($result))
                   {
                  
                      $output='true';
                      $re=mail($to,$sub,$msg);
                       if(!$re)
                        $output='mailerror';
                      
                    }
                    else
                    {
                      $output='false';  
                     }


               }
        }
        else
        $output='false';

    }
      else
      $output='false';


echo $output;

mysqli_close($dbc);
     
?>