<?php
$response = array();

require_once __DIR__.'/login.php';
require_once __DIR__.'/db_connector.php';
$conn = connect();

if (isset($_GET['username']) && isset($_GET['password']) && isset($_GET['groupname'])) {
    $username = $_GET['username'];
    $password = $_GET['password'];
    $groupname = $_GET['groupname'];
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
        
            $result = mysqli_query($conn, "DELETE FROM groups WHERE id = ".$groupid);
            $result2 = mysqli_query($conn, "DELETE FROM stats where groupid = ".$groupid);
            if ($result && $result2) {
                $response["success"] = 1;
                $response["message"] = "Group and stats successfully removed.";
                
                echo json_encode($response);   
            } elseif ($result) {
                $response["success"] = 0;
                $response["message"] = "Group removed, but related stats still exist";
                
                echo json_encode($response);
            } elseif($result2) {
                $response["success"] = 0;
                $response["message"] = "Related stats removed, but group still exists";
                
                echo json_encode($response);
            }  else {
                $response["success"] = 0;
                $response["message"] = "Neither group, nor related stats were removed";
                
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