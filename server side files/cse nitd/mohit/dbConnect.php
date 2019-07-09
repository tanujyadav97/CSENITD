<?php
 define('HOST','localhost');
 define('USER','username');
 define('PASS','password');
 define('DB','database name');
 
 $db = mysqli_connect(HOST,USER,PASS,DB) or die('Unable to Connect');
 ?>