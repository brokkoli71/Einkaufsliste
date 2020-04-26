<?php
require 'inc/db.php';

if (!$pwd_got = $db->real_escape_string(trim($_POST['pwd']))){
    $pwd_got = $db->real_escape_string(trim($_GET['pwd']));
}
$pwd_set = "samplepwd";

if ($pwd_got == $pwd_set){
    $erg = $db->query($_POST['sql']);
    $erg = $db->query($_GET['sql']);
    
    if ($_POST['json'] == "true" || $_GET['json'] == "true"){
        $arr = array();
        while($zeile = $erg->fetch_array(MYSQLI_ASSOC)){
            $arr[] = $zeile;
        }
        echo json_encode($arr);
    }else{
        echo 'password accepted';
        echo '<pre>';
        print_r($erg->fetch_all(MYSQLI_ASSOC));
        echo '</pre>';
    }
}else{
    echo 'password not accepted: '.$pwd_got;
}

$erg->free();
$db->close();
?>