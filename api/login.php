<?php
    function login($username, $password, &$conn) {
        $result = mysqli_query($conn, "SELECT * FROM users WHERE username='".mysqli_real_escape_string($conn, $username)."'");
        if ($result) {
            if (mysqli_num_rows($result) > 0) {
                $result = mysqli_fetch_array($result);
                $storedpassword = $result[2];
                
                if (password_verify($password, $storedpassword)) {
                    return $result[0];
                }
            }
        }
        return 0;
    }
?>