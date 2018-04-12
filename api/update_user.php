<?php
$response = array();

require_once __DIR__.'/login.php';
require_once __DIR__.'/db_connector.php';
$conn = connect();

if (isset($_GET['username']) && isset($_GET['password']) && isset($_GET['newusername'])) {
    $username = $_GET['username'];
    $password = $_GET['password'];
    $newusername = $_GET['newusername'];
    $userid = login($username, $password, $conn);
    if ($userid) {       
            $result = mysqli_query($conn, "UPDATE users SET username = '".mysqli_real_escape_string($conn, $newusername)."' where id = ".$userid);
            if ($result) {
                $response["success"] = 1;
                $response["message"] = "Username updated successfully.";
                    
                echo json_encode($response);   
            } else {
                $response["success"] = 0;
                $response["message"] = "Unable to update username"; 
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