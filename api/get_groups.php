<?php
$response = array();

require_once __DIR__.'/login.php';
require_once __DIR__.'/db_connector.php';
$conn = connect();

if (isset($_GET['username']) && isset($_GET['password'])) {
    $username = $_GET['username'];
    $password = $_GET['password'];
    $id = login($username, $password, $conn);
    if ($id) {
            $result = mysqli_query($conn, "SELECT * FROM groups WHERE userid = ".$id);
            if ($result && mysqli_num_rows($result) > 0) {
                $response["groups"] = array();
                while ($row = mysqli_fetch_array($result)) {
                    $group = array();
                    $group["groupid"] = $row["id"];
                    $group["groupname"] = $row["groupname"];
                    
                    array_push($response["groups"], $group);
                }
                $response["success"] = 1;
                
                echo json_encode($response);
            } else {
                $response["success"] = 0;
                $response["message"] = "No groups found."; 
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