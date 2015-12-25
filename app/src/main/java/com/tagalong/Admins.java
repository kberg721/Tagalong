package com.tagalong;

/**
 * Created by cpf5193 on 9/30/2015.
 */
public class Admins {
  protected static final String ADMIN = "Chip";
  //protected static final String ADMIN = "Kyle";

  protected static final String SERVER_ADDRESS = ADMIN.equals("Chip") ?
    "http://chipfukuhara.com/Tagalong/" :
    "http://kberg721.freeiz.com/";

  protected static String getRegisterFile() {
    return SERVER_ADDRESS + (ADMIN.equals("Chip") ? "MySqlRegister.php" : "Register.php");
  }

  protected String getFetchUserFile() {
    return SERVER_ADDRESS + (ADMIN.equals("Chip") ? "MySqlFetchUser.php" : 
      "FetchUserData.php");
  }

  protected String getSubmitEventFile() {
    return SERVER_ADDRESS + (ADMIN.equals("Chip") ? "MySqlSubmitEvent.php" : "SubmitEvent.php");
  }

  protected String getFetchEventFile() {
    return SERVER_ADDRESS + (ADMIN.equals("Chip") ? "MySqlFetchEvent.php" : "FetchEventData.php");
  }
}
