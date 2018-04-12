<?php

$response = array();

require_once __DIR__.'/login.php';
require_once __DIR__.'/db_connector.php';
$conn = connect();

if (isset($_GET['username']) && isset($_GET['password']) && isset($_GET['groupname'])) {
    $username = $_GET['username'];
    $password = $_GET['password'];
    $groupname = $_GET['groupname'];
    $id = login($username, $password, $conn);
    if ($id) {        
            $nameResult = mysqli_query($conn, "SELECT * FROM groups WHERE userid = ".$id." AND groupname = '".mysqli_escape_string($conn, $groupname)."'");
            $nameResult = mysqli_fetch_array($nameResult);
            $name = $nameResult[2];
            if($groupname == $name) {
                $result["success"] = 0;
                $result["message"] = "Cannot have two groups with the same name.";
                
                echo json_encode($result);
                exit();
            }
        
            $result = mysqli_query($conn, "INSERT INTO groups (userid, groupname) VALUES (".$id.", '".mysqli_real_escape_string($conn, $groupname)."')");
        if ($result) {
            $response["success"] = 1;
            $response["message"] = "Group created successfully.";
            echo json_encode($response);
        }
        else {
            $response["success"] = 0;
            $response["message"] = "Could not create group.";
            echo json_encode($response);
        }
    }                         
    else {
        $response["success"] = 0;
        $response["message"] = "Improper credentials supplied";
        
        echo json_encode($response); 
    } 
}
    else {
    $response["success"] = 0;
    $response["message"] = "Required fields(s) is missing";
    
    echo json_encode($response);
}
mysqli_close($conn);
?>