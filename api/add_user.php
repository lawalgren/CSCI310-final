<?php
$response = array();

if (isset($_GET['username']) && isset($_GET['password'])) {
    
    require_once __DIR__.'/db_connector.php';
    $conn = connect();
    if ($conn == mysqli_connect_error())
        die("could not connect to database");
    
    $username = $_GET['username'];
    $password = $_GET['password'];
    
    $unResult = mysqli_query($conn, "SELECT * from users where username = '".mysqli_real_escape_string($conn, $username)."'");
    
    if ($unResult && mysqli_num_rows($unResult) > 0) {
        $response["success"] = 0;
        $response["message"] = "That username is taken. Maybe try logging in?";
        
        echo json_encode($response);
    } else {
    
  
    $password = password_hash($password, PASSWORD_DEFAULT);
    
    $result = mysqli_query($conn, "INSERT INTO users (username, password) VALUES ('".mysqli_real_escape_string($conn, $username)."', '".mysqli_real_escape_string($conn, $password)."')");
    
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "User successfully created.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Could not create user.";
        echo mysqli_error($conn);
        echo json_encode($response);
    }
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required fields(s) is missing";
    
    echo json_encode($response);
}
mysqli_close($conn);
?>