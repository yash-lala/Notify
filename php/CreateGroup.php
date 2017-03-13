<?php
require_once 'Actions.php';
$response = array('error' => FALSE );

$trigger = new Actions();
$inputJson = file_get_contents('php://input');
$input = json_decode($inputJson,true);
$uid = $input['uid'];
$group_name = $input['group_name'];
$users = $input['members'];


if(!$trigger->createGroup($users,$group_name,$uid)){
  echo json_encode($response);
}else {
  $response['error'] = true;
  echo json_encode($response);
}


 ?>
