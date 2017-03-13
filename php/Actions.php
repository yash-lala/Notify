<?php

class Actions {
  private $connection;
  private $response;

  function __construct(){
    require_once 'Configuration.php';
    $this->connection = new mysqli(host,db_user,db_password,db_name);
    if(mysqli_connect_errno()){
      $this->response['error'] = TRUE;
      $this->response['message'] = "Something went horribly wrong!!!";
      echo json_encode($this->response);
    }
  }

  function isExisting($field,$value){
    $result = $this->connection->prepare("SELECT $field FROM users WHERE $field = ? ");
    $result->bind_param('s' , $value);
    $result->execute();
    $result->store_result();
    if($result->num_rows > 0){
      $result->close();
      return true;
    }
    $result->close();
    return false;
  }

  function getByCredentials($firebase_id,$user_name,$password){
    $result = $this->connection->prepare("SELECT * FROM users WHERE user_name = '$user_name' AND password = ? ");
    $result->bind_param('s' , $password);
    #$result->bind_param(1, $email);
    #$result->bind_param(2, $password);
    if($result->execute()){
      $information = $result->get_result()->fetch_assoc();
      if($information["firebase_id"] != $firebase_id ){ $this->updateFirebaseID($firebase_id,$user_name); }
      $result->close();
      return $information;
    }else return NULL;
  }

  function storeValues($firebase_id,$name,$user_name,$email,$password){
    $uniqueID = md5($user_name.$email);
    $result = $this->connection->prepare("INSERT INTO users(uid,firebase_id,nameOfUser, user_name, email, password) VALUES(?,?,?,?,?,?)");
    $result->bind_param('ssssss',$uniqueID,$firebase_id,$name,$user_name,$email,$password);
    $result->execute();
    if($this->connection->affected_rows > 0){
        $result->close();
        return true;
    }else {
        $result->close();
        return false;
    }

  }

  function setLoggedIn($bool, $id){
    $result= $this->connection->prepare("UPDATE users set loggedIn = '$bool' where uid = ?");
    $result->bind_param('s', $id);
    $result->execute();
    if($this->connection->affected_rows > 0){
        $result->close();
        return true;
    }else {
        $result->close();
        return false;
    }
  }

  function isLoggedIn($user_name){
    $result = $this->connection->prepare("SELECT loggedIn from users where user_name  = ?");
    $result->bind_param('s', $user_name);
    $result->execute();
    $result->bind_result($bool);
    $result->fetch();
    $result->close();
    return $bool;
  }

  function updateFirebaseID($firebase_id,$user_name){
    $query = $this->connection->prepare("UPDATE users SET firebase_id = '$firebase_id' where user_name = ?");
    $query->bind_param('s',$user_name);
    $query->execute();
    $query->close();
  }

  function getAllNameAndUsername($uid){
    $result = $this->connection->prepare("SELECT nameOfUser,user_name from users where uid not in (?)");
    $result->bind_param('s',$uid);
    $result->execute();
    $data = $result->get_result();
    $userArr = array();
    while($buffer = $data->fetch_assoc()){
      $userArr[] = $buffer;
    }
    $result->close();
    return $userArr;
  }


  function getAllGroups($admin){
    $result = $this->connection->prepare("SELECT group_name,members,created_at from groups where admin = ? ORDER BY created_at DESC");
    $result->bind_param('s',$admin);
    $result->execute();
    $data = $result->get_result();
    $groupArr =array();
    while ($buffer = $data->fetch_assoc()) {
      $groupArr[] = $buffer;
    }
    $result->close();
    return $groupArr;
  }


  function createGroup($userNames,$groupName,$groupAdmin){
    $groupId = md5($groupName.$groupAdmin);
    $member_count = count($userNames);
    $result = $this->connection->prepare("INSERT INTO groups(group_id,group_name,members,admin) VALUES(?,?,?,?);");
    $result->bind_param('ssss',$groupId,$groupName,$member_count,$groupAdmin);
    $result->execute();
    $result->close();
    $this->addGroupsAndUsers($userNames,$groupName,$groupAdmin);
    }


  function getColumnFromValues($col,$table,$valuesOFCol,$values){
    $alteredValueArr = join(',',array_fill(0,count($values),'?'));
    $result = $this->connection->prepare("SELECT $col FROM $table WHERE $valuesOFCol In ($alteredValueArr)");
    $result->bind_param(str_repeat('s',count($values)), ...$values);
    $result->execute();
    $data = $result->get_result();
    $column = array();
    while ($buffer = $data->fetch_assoc()) {
      $column[] = $buffer[$col];
    }
    $result->close();
    return $column;
  }

  function addGroupsAndUsers($userNames,$groupName,$admin){
    $userIds = $this->getColumnFromValues('uid','users','user_name',$userNames);
    $queryForGroupID = $this->connection->prepare("SELECT group_id from groups where group_name = '$groupName' AND admin = '$admin';");
    $queryForGroupID->execute();
    $queryForGroupID->bind_result($groupId);
    $queryForGroupID->fetch();
    $queryForGroupID->close();

    $insertQuery = $this->connection->prepare("INSERT INTO usersingroups(uid,group_id) Values (?,?); ");
    foreach ( $userIds as $idHolder ) {
      $insertQuery->bind_param('ss',$idHolder,$groupId);
      $insertQuery->execute();
    }

  }


function groupIdFromGroupNameAdmin($groupName,$admin){
  $queryForGroupID = $this->connection->prepare("SELECT group_id from groups where group_name = '$groupName' AND admin = (SELECT uid FROM users WHERE user_name = '$admin' );");
  $queryForGroupID->execute();
  $queryForGroupID->bind_result($groupId);
  $queryForGroupID->fetch();
  $queryForGroupID->close();
  return $groupId;
}


  function getFirebaseIdsFromGroupName($groupName,$admin){
    $groupId = $this->groupIdFromGroupNameAdmin($groupName,$admin);
    $queryForFirebaseIds =  $this->connection->prepare("SELECT firebase_id FROM `users` WHERE uid in(SELECT uid FROM usersingroups WHERE group_id = ?);");
    $queryForFirebaseIds->bind_param('s',$groupId);
    $queryForFirebaseIds->execute();
    $data = $queryForFirebaseIds->get_result();
    $ids = array();
    while ($buffer = $data->fetch_assoc()) {
      $ids[] = $buffer['firebase_id'];
    }
    $queryForFirebaseIds->close();
    return $ids;
  }



  function sendMessage($firebaseIds,$message,$admin){

  $headers = array(
    'Authorization: key='.fcm_key,
    'Content-Type: application/json'
  );
  $data = array
  (
    "message" => $message,
    "sender" => $admin
  );
  $msg = array
  (
    "body" => $message,
    "title" => 'Notify'
  );
  $fields = array
  (
    'registration_ids'=> $firebaseIds,
    'notification' => $msg,
    'data' => $data
  );
  $ch = curl_init();
  curl_setopt( $ch,CURLOPT_URL, fcm_url );
  curl_setopt( $ch,CURLOPT_POST, true );
  curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
  curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
  curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
  curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
  $result = curl_exec($ch);
  if($result){
    curl_close( $ch );
    return true;
  } else{
    curl_close( $ch );
    return false;
  }
 }

 function saveMessage($admin,$groupName,$message){
   $date = new DateTime();
   $time = $date->getTimestamp();
   $groupId = $this->groupIdFromGroupNameAdmin($groupName,$admin);
   $messageId = md5($groupId.$message.$time);
   $queryToSave = $this->connection->prepare("INSERT INTO messages(message_id,group_id,message) VALUES (?,?,?);");
   $queryToSave->bind_param("sss",$messageId,$groupId,$message);
   $queryToSave->execute();
 }


 function getUserMessages($uid){
   $result = $this->connection->prepare("SELECT messages.message,messages.created_at,groups.group_name FROM messages,groups WHERE messages.group_id in (SELECT group_id FROM usersingroups WHERE uid = ?) AND messages.group_id = groups.group_id ORDER BY messages.created_at DESC;");
   $result->bind_param("s",$uid);
   $result->execute();
   $output = $result->get_result();
   $groupNameMessageAndTime = array();
   while ($buffer = $output->fetch_assoc()) {
     $groupNameMessageAndTime[] = $buffer;
   }
   $result->close();
     return $groupNameMessageAndTime;

 }




}

 ?>
