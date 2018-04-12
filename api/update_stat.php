<?php
$response = array();

require_once __DIR__.'/login.php';
require_once __DIR__.'/db_connector.php';
$conn = connect();

if (isset($_GET['username']) && isset($_GET['password']) && isset($_GET['groupname']) && isset($_GET['firstName']) && isset($_GET['lastName']) && isset($_GET['day']) && isset($_GET['newday']) && isset($_GET['newfirstName']) && isset($_GET['newlastName']) && isset($_GET['newrole'])) {
    $username = $_GET['username'];
    $password = $_GET['password'];
    $groupname = $_GET['groupname'];
    $firstName = $_GET['firstName'];
    $lastName = $_GET['lastName'];
    $day = $_GET['day'];
    $newday = $_GET['newday'];
    $newfirstName = $_GET['newfirstName'];
    $newlastName = $_GET['newlastName'];
    $newrole = $_GET['newrole'];
    $userid = login($username, $password, $conn);
    if ($userid) {
            
            $groupresult = mysqli_query($conn, "SELECT id from groups where userid = ".$userid." AND groupname = '".mysqli_real_escape_string($conn, $groupname)."'");
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
        
            $statresult = mysqli_query($conn, "SELECT id from stats where groupid = ".$groupid." AND firstName = '".mysqli_real_escape_string($conn, $firstName)."' AND lastName = '".mysqli_real_escape_string($conn, $lastName)."' AND day = ".$day);
            if ($statresult && mysqli_num_rows($statresult) > 0) {
                $statresult = mysqli_fetch_array($statresult);
                $statid = $statresult[0];
            }
            else {
                $response["success"] = 0;
                $response["message"] = "Stat not found.";
                
                echo json_encode($response);
                exit();
            }
        
            $result = mysqli_query($conn, "UPDATE stats SET day = ".$newday.", firstName = '".mysqli_real_escape_string($conn, $newfirstName)."', lastName = '".mysqli_real_escape_string($conn, $newlastName)."', role = '".mysqli_real_escape_string($conn, $newrole)."' where id =".$statid);
            if ($result) {
                $response["success"] = 1;
                $response["message"] = "Stat updated successfully.";
                    
                echo json_encode($response);   
            } else {
                $response["success"] = 0;
                $response["message"] = "Unable to update stat"; 
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