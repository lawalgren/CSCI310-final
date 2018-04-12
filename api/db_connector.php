<?php
    function connect() {
        require_once __DIR__ . '/db_config.php';
 
       $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die( mysqli_connect_error());
 
        $db = DB_DATABASE;
 
        return $con;
    }
?>