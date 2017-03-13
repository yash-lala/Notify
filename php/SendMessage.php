<?php

require_once 'Configuration.php';
require_once 'Actions.php';
$response = array('error' => FALSE );

$trigger = new Actions();

$inputJson = file_get_contents('php://input');
$input = json_decode($inputJson,true);

$groupName = $input['groupName'];
$admin =  $input['admin'];
$message = $input['message'];

$trigger->saveMessage($admin,$groupName,$message);

$regids = $trigger->getFirebaseIdsFromGroupName($groupName,$admin);
if($trigger->sendMessage($regids,$message,$admin)){
  $response['message'] = "Message sucessfully sent";
  echo json_encode($response);
}else{
  $response['error'] = true;
  $response['message'] = 'ohhh boi!';
  echo json_encode($response);

}


?>
