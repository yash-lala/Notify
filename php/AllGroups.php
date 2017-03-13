<?php
require_once "Actions.php";
$trigger = new Actions();

$inputJson = file_get_contents('php://input');
$input = json_decode($inputJson,true);
$admin = $input['uid'];
$response = array();
$result = $trigger->getAllGroups($admin);
$response['list'] = $result;
echo json_encode($response);
 ?>
