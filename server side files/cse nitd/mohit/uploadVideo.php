<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 $file_name = $_FILES['myFile']['name'];
 $file_size = $_FILES['myFile']['size'];
 $file_type = $_FILES['myFile']['type'];
 $temp_name = $_FILES['myFile']['tmp_name'];
 $text=$_POST['text']['name'];
 $location = "upload/";
 
 move_uploaded_file($temp_name, $location.$file_name);
 echo "https://nitd.000webhostapp.com/cse%20nitd/mohit/upload/".$file_name;
 }else{
 echo "Error";
 }