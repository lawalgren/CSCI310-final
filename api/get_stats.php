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
            $groupresult = mysqli_query($conn, "SELECT id from groups where userid = ".$userid." AND groupname = '".mysqli_real_escape_string($conn, $groupname)."'");
            if($groupresult && mysqli_num_rows($groupresult) > 0) {
            $groupresult = mysqli_fetch_array($groupresult);
            $groupid = $groupresult[0];
            } else {
                $response["success"] = 0;
                $response["message"] = "No group found with that name";
                
                echo json_encode($response);
            }
        
            $result = mysqli_query($conn, "SELECT * FROM stats WHERE userid = ".$userid." AND groupid = ".$groupid);
            if ($result && mysqli_num_rows($result) > 0) {
                $response["stats"] = array();
                while ($row = mysqli_fetch_array($result)) {
                    $stat = array();
                    $stat["statid"] = $row["id"];
                    $stat["firstName"] = $row["firstName"];
                    $stat["lastName"] = $row["lastName"];
                    $stat["day"] = $row["day"];
                    $stat["role"] = $row["role"];
                    
                    array_push($response["stats"], $stat);
                }
                $response["success"] = 1;
                
                echo json_encode($response);
            } else {
                $response["success"] = 0;
                $response["message"] = "No stats found."; 
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