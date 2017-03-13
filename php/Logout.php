<?php

require_once 'Actions.php';
$trigger = new Actions();

$inputJson = file_get_contents('php://input');
$input = json_decode($inputJson,true);
$uniqueID = $input['uid'];
$response = array('error' => FALSE );

if($trigger->setLoggedIn(FALSE,$uniqueID)){
  echo json_encode($response);
}else {
  $response['error'] = TRUE;
  echo json_encode($response);
}

 ?>
