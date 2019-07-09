<?php
 
 if(isset($_POST['username'])&&isset($_POST['image'])){
 
 $username=$_POST['username'];
 $image = $_POST['image'];
 
 require_once('global.php'); 

 $path = "cse%20nitd/dpuploads/$username.png";
 $path1 = "dpuploads/$username.png"; 
 $actualpath = "https://nitd.000webhostapp.com/$path";
 
 $sql = "update dp set image='$actualpath' where username='$username'";
 
 if(mysqli_query($dbc,$sql)){
 file_put_contents($path1,base64_decode($image));
 echo "true";
 }
else
{
echo "false";
}
 

 }else{
 echo "false";
 }

 mysqli_close($dbc);
?>