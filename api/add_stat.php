<?php
$response = array();
$anon = 0;
$userid;

require_once __DIR__.'/db_connector.php';
require_once __DIR__.'/login.php';
$conn = connect();

if (isset($_GET['username']) && isset($_GET['password']) && ($_GET['username'] == 'anon') && ($_GET['password'] == '1234P@ssw0rd') && isset($_GET['foruser'])) {
    $result = mysqli_query($conn, "SELECT * FROM users WHERE username = '".mysqli_real_escape_string($conn, $_GET['foruser'])."'");
    $result = mysqli_fetch_array($result);
    $userid = $result[0];
    if (!$userid) {
                $result["success"] = 0;
                $result["message"] = "Could not find user";
                
                echo json_encode($result);
                exit();
    }
    $anon = 1;
}

if (isset($_GET['username']) && isset($_GET['password']) && isset($_GET['day']) && isset($_GET['firstName']) && isset($_GET['lastName']) && isset($_GET['groupname']))
{
    $username = $_GET['username'];
    $password = $_GET['password'];
    $day = $_GET['day'];
    $firstName = $_GET['firstName'];
    $lastName = $_GET['lastName'];
    $groupname = $_GET['groupname'];
    if (isset($_GET['role'])) {
        $role = $_GET['role'];
    }
    else {
        $role = "None";
    }
    
    if($anon == 0) {
        $userid = login($username, $password, $conn);
    }
    if ($userid)  
    {
        
            $idResult = mysqli_query($conn, "SELECT * FROM groups WHERE userid = ".$userid." AND groupname = '".mysqli_real_escape_string($conn, $groupname)."'");
            $idResult = mysqli_fetch_array($idResult);
            $groupid = $idResult[0];
        
            $statResult = mysqli_query($conn, "SELECT * FROM stats WHERE firstName = '".mysqli_real_escape_string($conn, $firstName)."' AND lastName = '".mysqli_real_escape_string($conn, $lastName)."' AND day = ".$day." AND groupid = ".$groupid);

            $statResult = mysqli_fetch_array($statResult);
            if($statResult) {
                $result["success"] = 0;
                $result["message"] = "Cannot have two entries for the same attendee on the same day.";
                
                echo json_encode($result);
                exit();
            }
        
            $result = mysqli_query($conn, "INSERT INTO stats (userid, groupid, firstName, lastName, day, role) VALUES (".$userid. ", ".$groupid.", '".mysqli_real_escape_string($conn, $firstName)."',  '".mysqli_real_escape_string($conn, $lastName)."', ".$day.", '".mysqli_real_escape_string($conn, $role)."')");

        if ($result) {
            $response["success"] = 1;
            $response["message"] = "Entry created successfully.";
            echo json_encode($response);
        } 
        else {
            $response["success"] = 0;
            $response["message"] = "Could not create entry."; 
            
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