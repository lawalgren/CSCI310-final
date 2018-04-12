<?php
    $response = array();
    require_once __DIR__.'/db_connector.php';
    require_once __DIR__.'/login.php';
    $conn = connect();

if(isset($_GET['username']) && isset($_GET['password'])) {
        $uid = login($_GET['username'], $_GET['password'], $conn);
        if ($uid) {
            $response['success'] = 1;
            $response['message'] = "Login successful";
            
            echo json_encode($response);
        } else {
          $response['success'] = 0;
          $response['message'] = "Unable to login";
            
          echo json_encode($response);
        }
        mysqli_close($conn);
    }
else {
    $response["success"] = 0;
    $response["message"] = "Required fields(s) is missing";
    
    echo json_encode($response);
} 
mysqli_close($conn);
?>