<?php

require_once('global.php');
if(isset($_POST['username'])&&isset($_POST['id']))
  
     {
       $username=$_POST['username'];
       $id=$_POST['id'];
       $title=$_POST['title'];
       
       $query1="select email from profile where username='$username'";       
       $query="select username from attending where classid='$id'";

       $result = mysqli_query($dbc, $query1);
       $output='';

    
            $row = mysqli_fetch_array($result);
            $email=$row['email'];
            
              $result = mysqli_query($dbc, $query);
            
            $mailcontent='No participants yet!';
            if(mysqli_num_rows($result)>0)
            {
                $arr='(';
                while($row = mysqli_fetch_array($result))
                {
                      $user=$row['username'];
                      if($arr=='(')
                      $arr=$arr."'".$user."'";
                      else
                      $arr=$arr.",'".$user."'";
                }
                      $arr=$arr.')';
                
                
                $query="select username,name,email,phone,location,desig,org,branch from profile where username in$arr";
          
      $result = mysqli_query($dbc, $query);
                
                $mailcontent='S.No | Username | Name | Email | Phone | Location | Designation | Organisation | Branch'."\r\n";
                $c=1;
                while($row = mysqli_fetch_array($result))
                {
                    $mailcontent=$mailcontent.$c.'  '.$row[0].'   '.$row[1].'   '.$row[2].'   '.$row[3].'   '.$row[4].'   '.$row[5].'   '.$row[6].'   '.$row[7]."\r\n";
$c=$c+1;
                }

            }
          
       
            $re=mail($email,'participation info of '.$title,$mailcontent);
                       //echo $mailcontent;
                       if(!$re)
                        $output='mailerror';
                       else
                        $output='true';

 }
else
{
    $output='false';  
}

echo $output;

mysqli_close($dbc);
     
?>