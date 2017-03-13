<?php

#actions object to use functions of action class
require_once 'Actions.php';
$trigger = new Actions();

#we will send respone through this array
$response  = array('error' => FALSE );

#take input file
$inputJson = file_get_contents('php://input');
#decode the input file in which you're json resides to from assoc array of json obj
$input = json_decode($inputJson,true);

#extracting values from the input json
$firebase_id = $input['firebase_id'];
$name = $input['name'];
$user_name = $input['user_name'];
$email = $input['email'];
$password = $input['password'];
$emailBoolean = $trigger->isExisting('email',$email);
$user_nameBoolean = $trigger->isExisting('user_name',$user_name);

$existing_message = 'Towards the path of success one must truly be ORIGINAL through ';

if($emailBoolean | $user_nameBoolean){
  if($emailBoolean) $existing_message .= "Email"; else $existing_message .= "user_name";
  $response['error'] = TRUE;
  $response['message'] = $existing_message;
  echo json_encode($response);
}else{
  if($trigger->storeValues($firebase_id,$name,$user_name,$email,$password)){
    $response['error'] = FALSE;
    $response['message'] = 'You have successfully registered. I may warn you there is no Exit';
    echo json_encode($response);
  }else {
    $response['error'] = TRUE;
    $response['message'] = 'Ooops,You sir may try again, not this time perhaps';
    echo json_encode($response);
  }
}



 ?>
