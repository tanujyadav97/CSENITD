<?php
if(isset($_POST['des'])&&isset($_POST['image'])){
 
 require_once('dbConnect.php'); 
 
 $des=$_POST['des'];
 $des=str_ireplace('drop','',$des);
$des=str_ireplace('alter','',$des);
 $title=$_POST['title'];
 $title=str_ireplace('drop','',$title);
$title=str_ireplace('alter','',$title);
 $image = $_POST['image'];
 $datetime=$_POST['datetime'];
 $username=$_POST['username'];
 $imgname=$_POST['imgname'];
 

 $path = "upload/$imgname.png"; 
 $actualpath = "https://nitd.000webhostapp.com/cse%20nitd/mohit/$path";
 
if($image=="n/a")
 $sql = "insert into acheivements values(null,'$title','$des','n/a',0,'$datetime')";
else
 $sql = "insert into acheivements values(null,'$title','$des','$actualpath',0,'$datetime')";
 
 if(mysqli_query($db,$sql)){
 
 $sql ="SELECT ach_id FROM acheivements ORDER BY ach_id ASC";
 
 $res = mysqli_query($db,$sql);
 
 $id = 0;
 
 while($row = mysqli_fetch_array($res)){
 $id = $row['ach_id'];
 }
 $sql= mysqli_query($db,"insert into userachievement values('$username','$id')");
if($image!="n/a")
 file_put_contents($path,base64_decode($image));
 echo "true";
 }
else
{
echo "false";
}
 }else{
 echo "false";
}

 mysqli_close($db);
?>