<?php
$response = array();

require_once __DIR__.'/login.php';
require_once __DIR__.'/db_connector.php';
$conn = connect();

if (isset($_GET['username']) && isset($_GET['password']) && isset($_GET['groupname']) && isset($_GET['newgroupname'])) {
    $username = $_GET['username'];
    $password = $_GET['password'];
    $groupname = $_GET['groupname'];
    $newgroupname = $_GET['newgroupname'];
    $userid = login($username, $password, $conn);
    if ($userid) {
            
            $groupresult = mysqli_query($conn, "SELECT id from groups where userid = ".$userid." AND groupname = '".mysqli_escape_string($conn, $groupname)."'");
            if ($groupresult && mysqli_num_rows($groupresult) > 0) {
                $groupresult = mysqli_fetch_array($groupresult);
                $groupid = $groupresult[0];
            }
            else {
                $response["success"] = 0;
                $response["message"] = "No group found with that name";
                
                echo json_encode($response);
                exit();
            }
        
            $result = mysqli_query($conn, "UPDATE groups SET groupname = '".mysqli_real_escape_string($conn, $newgroupname)."' where userid = ".$userid." AND id = ".$groupid);
            if ($result) {
                $response["success"] = 1;
                $response["message"] = "Group updated successfully.";
                    
                echo json_encode($response);   
            } else {
                $response["success"] = 0;
                $response["message"] = "Unable to update group"; 
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