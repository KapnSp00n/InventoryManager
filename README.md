"# InventoryManager<br />
   This inventory manager app makes use of Java for backend while using JavaFX <br /> 
   and SceneBuilder for UI. The logIn system goes through a locally hosted <br/>
   MySQL server. There are some important MYSQL procedures at the end of the <br>
   README if you would like to replicate<br/> 
   something similar<br />
   
   Here are some images to showcase the program
   
   Log In and Registering 
   ![login](https://github.com/NguyenLe-Dev/InventoryManager/assets/129339432/0d619c6d-e7c4-4b73-aefe-44355b37aa6f)
   
   Main Menu
   ![image](https://github.com/NguyenLe-Dev/InventoryManager/assets/129339432/a441a061-ee08-446a-899d-099dbafd5256)
   
   Adding new order
   ![image](https://github.com/NguyenLe-Dev/InventoryManager/assets/129339432/5230bfe8-d084-4322-b714-455d261d8e8f)
   
   Editable field in cell which only accept decimals that other elements react to 
   ![image](https://github.com/NguyenLe-Dev/InventoryManager/assets/129339432/b6ace5fa-cd0f-4dd2-a1cc-c452f62ac3ec)
   
   'addUser' procedure (This is called in the Java code to try registering an account)<br />
```
    CREATE DEFINER=`root`@`localhost` PROCEDURE `addUser`(
		username varchar(30),
    	pass varchar (30),
    	userdata MEDIUMBLOB)
	Begin
		DECLARE salty VARCHAR(6);
    	SET salty = SUBSTRING(SHA1(RAND()), 1, 6);
		INSERT INTO users 
    	VALUES (username, SHA2(CONCAT(pass,salty),256), salty, userdata);
	END
```
   'logIn' procedure (Called in the logInController to try logging in) <br />
```
	CREATE DEFINER=`root`@`localhost` PROCEDURE `logInTime`(
		username varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs,
    	password1 varchar (30)
		)
	BEGIN
		DECLARE salty varchar(6);
    	DECLARE pass varchar(256);
    	DECLARE object MEDIUMBLOB;
    	SET pass = (select pass_word_hash FROM users WHERE user_name = username);
    	SET object = (select user_data FROM users WHERE user_name = username);
    	SET salty = (select salt FROM users WHERE user_name = username);
    	IF (SHA2(CONCAT(password1,salty),256)=pass) THEN
			SELECT object;
		ELSE 
			SELECT null;
    	END IF;
	END
```	
" 
