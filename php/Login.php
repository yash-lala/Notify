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

#extracting user_name and password from the input json
$firebase_id = $input['firebase_id'];
$user_name = $input['user_name'];
$password = $input['password'];


#first check if then user_name exists
$user = $trigger->isExisting("user_name",$user_name);
if($user != false){
  #check if user is already loggedIn
  if($trigger->isLoggedIn($user_name)){
    $response ['error'] = TRUE;
    $response ['message'] = "$user_name is already loggedIn, ARE YOU HACKER-BOI?";
    echo json_encode($response);
  }
  #if user isnt't loggedIn
  else {
    #if user_name exists find user data by email and password
    $user = $trigger->getByCredentials($firebase_id,$user_name,$password);
    if($user != NULL){
      $trigger->setLoggedIn(TRUE,$user['uid']);
      $response['error'] = FALSE;
      $response['uid'] = $user['uid'];
      $response['user']['name'] = $user['nameOfUser'];
      $response['user']['user_name'] = $user['user_name'];
      $response['user']['email'] = $user['email'];
      $response['user']['DateOfCreation'] = $user['created_at'];
      echo json_encode($response);
    }else{
      #password wasnt correct (remember we're already checking for email)
      $response['error'] = TRUE;
      $response['message'] = "All you had to do was enter the damn right Password $user_name!!";
      echo json_encode($response);
    }
  }
}
else{
  #user_name didnt exist
  $response['error'] = TRUE;
  $response['message'] = "$user_name Ain't no user";
  echo json_encode($response);
}






 ?>
